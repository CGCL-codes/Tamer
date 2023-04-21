package report;

import objects.*;
import objects.battle.Battle;
import objects.battle.ShipGroupState;
import objects.bombing.Bombing;
import objects.bombing.PlanetBombings;
import util.Utils;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public final class RaceListParameter extends AbstractParameter {

    public static class RaceIntel {

        public Race race;

        public TechBlock techs = new TechBlock();

        public double population;

        public double industry;

        public int planets;

        public double votes;
    }

    @Override
    public final void doIt(Writer writer, Galaxy galaxy, Race recipient) throws IOException {
        double totalVotes = galaxy.totalVotes();
        ReportFormatter.Table table = AbstractParameter.newReportFormatterTable(writer, "Status of Players (total " + Utils.toReport(totalVotes) + " votes)");
        table.addHeader("N", ReportFormatter.Align.LEFT);
        table.addHeader("D", ReportFormatter.Align.RIGHT);
        table.addHeader("W", ReportFormatter.Align.RIGHT);
        table.addHeader("S", ReportFormatter.Align.RIGHT);
        table.addHeader("C", ReportFormatter.Align.RIGHT);
        table.addHeader("P", ReportFormatter.Align.RIGHT);
        table.addHeader("I", ReportFormatter.Align.RIGHT);
        table.addHeader("#", ReportFormatter.Align.RIGHT);
        if (recipient == null) table.addHeader("M", ReportFormatter.Align.LEFT); else table.addHeader("R", ReportFormatter.Align.LEFT);
        table.addHeader("V", ReportFormatter.Align.RIGHT);
        if (galaxy.hasTeams()) table.addHeader("*", ReportFormatter.Align.LEFT);
        if (galaxy.getState() == Galaxy.State.FINAL || recipient == null) table.addHeader("T", ReportFormatter.Align.LEFT);
        if (galaxy.getState() == Galaxy.State.FINAL) table.addHeader("", ReportFormatter.Align.LEFT);
        SortedMap<Race, RaceIntel> intels = calcIntels(galaxy, recipient);
        if (recipient != null) {
            ReportFormatter.Table.Row row = table.newRow();
            outRace(row, galaxy, recipient, intels.get(recipient));
        }
        for (RaceIntel intel : intels.values()) if (intel.race.isActive() && intel.race != recipient) {
            ReportFormatter.Table.Row row = table.newRow();
            outRace(row, galaxy, recipient, intel);
        }
        for (RaceIntel intel : intels.values()) if (!intel.race.isActive()) {
            ReportFormatter.Table.Row row = table.newRow();
            outRace(row, galaxy, recipient, intel);
        }
        table.close();
    }

    public static SortedMap<Race, RaceIntel> calcIntels(Galaxy galaxy, Race recipient) {
        SortedMap<Race, RaceIntel> intels = new TreeMap<Race, RaceIntel>();
        for (Race race : galaxy.getAllRaces()) {
            RaceIntel intel = new RaceIntel();
            intel.race = race;
            boolean visible = false;
            if (!galaxy.isBlind() || recipient == null || race.trusts(recipient) || !race.isActive()) {
                visible = true;
                intel.techs = race.getTechBlock().clone();
            } else {
                for (ShipGroup group : race.getShipGroups()) if (recipient.isVisible(group)) {
                    visible = true;
                    intel.techs.setMax(group.getTechBlock());
                }
            }
            for (Planet planet : race.getOwnedPlanets()) if (!galaxy.isBlind() || recipient == null || recipient.isVisible(planet)) {
                visible = true;
                intel.population += planet.getPopulation();
                intel.industry += planet.getIndustry();
                intel.planets++;
            }
            if (!galaxy.isBlind() || race.trusts(recipient)) intel.votes = galaxy.receivedVotes(race); else intel.votes = intel.population / GalaxyConstants.VOTE_SCALE;
            if (visible) intels.put(race, intel);
        }
        if (galaxy.isBlind() && recipient != null) {
            for (PlanetBombings pb : galaxy.getBombings()) {
                if (!pb.isWitness(recipient)) continue;
                if (pb.getOwner() != null) intelRace(intels, pb.getOwner());
                for (Bombing bombing : pb.getBombings()) intelRace(intels, bombing.who());
            }
            for (Battle battle : galaxy.getBattles()) {
                if (!battle.isWitness(recipient)) continue;
                for (Race race : battle.getParticipants()) {
                    if (race.trusts(recipient)) continue;
                    List<ShipGroupState> groupStates = battle.getGroupsStates(race);
                    if (groupStates.isEmpty()) continue;
                    RaceIntel intel = intelRace(intels, race);
                    for (ShipGroupState sgs : groupStates) intel.techs.setMax(sgs.group().getTechBlock());
                }
            }
        }
        return intels;
    }

    private static RaceIntel intelRace(SortedMap<Race, RaceIntel> intels, Race race) {
        RaceIntel intel = intels.get(race);
        if (intel == null) {
            intel = new RaceIntel();
            intel.race = race;
            intels.put(race, intel);
        }
        return intel;
    }

    private static void outRace(ReportFormatter.Table.Row row, Galaxy galaxy, Race recipient, RaceIntel intel) throws IOException {
        Race race = intel.race;
        String name = race.getName();
        if (!race.isActive()) name += "_RIP";
        row.add(name);
        row.add(intel.techs.drive());
        row.add(intel.techs.weapons());
        row.add(intel.techs.shields());
        row.add(intel.techs.cargo());
        row.add(intel.population);
        row.add(intel.industry);
        row.add(intel.planets);
        if (recipient == null) {
            double totalMass = 0.0;
            for (ShipGroup group : race.getShipGroups()) totalMass += group.getType().mass() * (double) group.getCount();
            row.add(totalMass);
        } else row.add(getDiplomacyStateStr(recipient, race));
        row.add(intel.votes);
        if (galaxy.hasTeams()) row.add(race.getTeam().getName());
        if (galaxy.getState() == Galaxy.State.FINAL || recipient == null) row.add(race.getVote().getName());
        if (galaxy.getState() == Galaxy.State.FINAL) row.add(galaxy.getWinners().contains(race) ? "WINNER" : "");
    }

    public static String getDiplomacyStateStr(Race recipient, Race race) {
        return race == recipient ? "-" : recipient.getDiplomacyState(race).toString();
    }
}
