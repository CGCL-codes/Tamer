package GA;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import GA.tournaments.ExtendedPlayerResult;
import GA.tournaments.ExtendedTournament;
import LUDOSimulator.LUDO;
import LUDOSimulator.LUDOBoard;
import LUDOSimulator.LUDOPlayer;
import LUDOSimulator.SemiSmartLUDOPlayer;

public class GA extends AGA {

    public GA(GAPlayer player) {
        super(player);
    }

    GA(GAPlayer player, ChromosomePair gene) {
        super(player, gene);
    }

    @Override
    public void build(GAPlayer player) {
        Map<String, Method> map = getMap(player);
        action("hit opponent").when(map.get("hitOpponentHome"), map.get("twoboards_param")).weights(0).gives(0).action("move out").when(map.get("moveOut"), map.get("twoboards_param")).weights(1).gives(1).action("hit my self home").when(map.get("hitMySelfHome"), map.get("twoboards_param")).weights(2).gives((float) 0.1).action("is a star").when(map.get("reachStar"), map.get("futureSteps_param")).weights(3).gives(2).action("is at home").when(map.get("atHome"), map.get("futureSteps_param")).weights(4).gives(3).action("is in danger").when(map.get("inDanger"), map.get("brickID_param")).weights(5).gives(4).action("is almost home").when(map.get("almostHome"), map.get("brickID_param")).weights(6).gives(5).action("close to home field").when(map.get("closeToHome"), map.get("brickID_param")).weights(7).gives(6).action("grouping bricks together").when(map.get("groupingBricks"), map.get("futureSteps_param")).weights(8).gives(7).action("moving from a group of bricks").when(map.get("movingFromGroup"), map.get("brickID_param")).weights(9).gives(8).action("default").gives((float) 0.9);
    }

    public Map<String, Method> getMap(GAPlayer pl) {
        map = new HashMap<String, Method>();
        Method[] methods = GAPlayer.class.getMethods();
        for (Method m : methods) {
            if (m.getName().equalsIgnoreCase("hitOpponentHome")) map.put("hitOpponentHome", m);
            if (m.getName().equalsIgnoreCase("twoboards_param")) map.put("twoboards_param", m);
            if (m.getName().equalsIgnoreCase("moveOut")) map.put("moveOut", m);
            if (m.getName().equalsIgnoreCase("hitMySelfHome")) map.put("hitMySelfHome", m);
            if (m.getName().equalsIgnoreCase("atHome")) map.put("atHome", m);
            if (m.getName().equalsIgnoreCase("reachStar")) map.put("reachStar", m);
            if (m.getName().equalsIgnoreCase("futureSteps_param")) map.put("futureSteps_param", m);
            if (m.getName().equalsIgnoreCase("inDanger")) map.put("inDanger", m);
            if (m.getName().equalsIgnoreCase("almostHome")) map.put("almostHome", m);
            if (m.getName().equalsIgnoreCase("closeToHome")) map.put("closeToHome", m);
            if (m.getName().equalsIgnoreCase("groupingBricks")) map.put("groupingBricks", m);
            if (m.getName().equalsIgnoreCase("movingFromGroup")) map.put("movingFromGroup", m);
            if (m.getName().equalsIgnoreCase("brickID_param")) map.put("brickID_param", m);
        }
        return map;
    }

    private enum MANIPUTATING {

        X, Y
    }

    ;

    /**
	 * 
	 * @param player
	 */
    public static void inversion(MANIPUTATING man, GAPlayer player) {
        double probability = 50;
        int rand1;
        int rand2;
        int doInversion;
        switch(man) {
            case X:
                rand1 = ((int) Math.random() * player.brain.chromosome.XChromosome.length);
                rand2 = ((int) Math.random() * Binary.size);
                doInversion = ((int) Math.random() * 1000);
                if (doInversion < probability) {
                    for (int i = 0; i < 10; i++) {
                        if (player.brain.chromosome.XChromosome[rand1].binary.length > rand2) player.brain.chromosome.XChromosome[rand1].invert(rand2++); else if (player.brain.chromosome.XChromosome.length > rand1) {
                            player.brain.chromosome.XChromosome[++rand1].invert(rand2 = 0);
                            rand2++;
                        } else break;
                    }
                }
                player.brain.chromosome.update();
                break;
            case Y:
                rand1 = ((int) Math.random() * player.brain.chromosome.YChromosome.length);
                rand2 = ((int) Math.random() * Binary.size);
                doInversion = ((int) Math.random() * 1000);
                if (doInversion < probability) {
                    for (int i = 0; i < 10; i++) {
                        if (player.brain.chromosome.YChromosome[rand1].binary.length > rand2) player.brain.chromosome.YChromosome[rand1].invert(rand2++); else if (player.brain.chromosome.YChromosome.length > rand1) {
                            player.brain.chromosome.YChromosome[++rand1].invert(rand2 = 0);
                            rand2++;
                        } else break;
                    }
                }
                player.brain.chromosome.update();
                break;
        }
    }

    /**
	 * This method only uses the two first players in the parameter list.
	 * @param players GAPlayers
	 */
    public static void onePointCrossover(MANIPUTATING man, GAPlayer... players) {
        int rand1;
        int rand2;
        switch(man) {
            case X:
                rand1 = (int) (Math.random() * players[0].brain.chromosome.YChromosome.length / 2);
                rand2 = (int) (Math.random() * Binary.size);
                for (int i = 0; i < 1000; i++) {
                    if (Binary.size > rand2) {
                        int temp = players[0].brain.chromosome.YChromosome[rand1].binary[rand2];
                        players[0].brain.chromosome.YChromosome[rand1].binary[rand2] = players[1].brain.chromosome.YChromosome[rand1].binary[rand2];
                        players[1].brain.chromosome.YChromosome[rand1].binary[rand2++] = temp;
                    } else if (ChromosomePair.actionLength - 1 > rand1) {
                        int temp = players[0].brain.chromosome.YChromosome[++rand1].binary[rand2 = 0];
                        players[0].brain.chromosome.YChromosome[rand1].binary[rand2] = players[1].brain.chromosome.YChromosome[rand1].binary[rand2];
                        players[1].brain.chromosome.YChromosome[rand1].binary[rand2++] = temp;
                    } else break;
                }
                players[0].brain.chromosome.update();
                players[1].brain.chromosome.update();
                break;
            case Y:
                rand1 = (int) (Math.random() * players[0].brain.chromosome.YChromosome.length / 2);
                rand2 = (int) (Math.random() * Binary.size);
                for (int i = 0; i < 1000; i++) {
                    if (Binary.size > rand2) {
                        int temp = players[0].brain.chromosome.YChromosome[rand1].binary[rand2];
                        players[0].brain.chromosome.YChromosome[rand1].binary[rand2] = players[1].brain.chromosome.YChromosome[rand1].binary[rand2];
                        players[1].brain.chromosome.YChromosome[rand1].binary[rand2++] = temp;
                    } else if (ChromosomePair.actionLength - 1 > rand1) {
                        int temp = players[0].brain.chromosome.YChromosome[++rand1].binary[rand2 = 0];
                        players[0].brain.chromosome.YChromosome[rand1].binary[rand2] = players[1].brain.chromosome.YChromosome[rand1].binary[rand2];
                        players[1].brain.chromosome.YChromosome[rand1].binary[rand2++] = temp;
                    } else break;
                }
                players[0].brain.chromosome.update();
                players[1].brain.chromosome.update();
                break;
        }
    }

    public class ControlledLUDO {

        LUDOBoard board;

        @SuppressWarnings("unused")
        private ArrayList<LUDOPlayer> players = new ArrayList<LUDOPlayer>();

        private ArrayList<SemiSmartLUDOPlayer> evaluationPlayers = new ArrayList<SemiSmartLUDOPlayer>();

        ControlledLUDO() {
            LUDO.visual = false;
            board = new LUDOBoard();
            ExtendedTournament.board = board;
        }

        public void train() {
            evaluationPlayers.add(new SemiSmartLUDOPlayer(ExtendedTournament.board));
            evaluationPlayers.add(new SemiSmartLUDOPlayer(ExtendedTournament.board));
            evaluationPlayers.add(new SemiSmartLUDOPlayer(ExtendedTournament.board));
            evaluationPlayers.trimToSize();
            List<LUDOPlayer> players = new ArrayList<LUDOPlayer>();
            for (int i = 0; i < ExtendedTournament.players; i++) {
                players.add(new GAPlayer(ExtendedTournament.board, null));
            }
            ((ArrayList<LUDOPlayer>) players).trimToSize();
            List<ExtendedPlayerResult> r;
            for (int i = 0; i <= 100; i++) {
                ExtendedTournament.writeGeneration(i, players);
                r = ExtendedTournament.playExtendedRoundWithSemi(players, evaluationPlayers);
                System.out.println("done");
                Collections.sort(r);
                ExtendedTournament.writeAverageResult(r);
                ExtendedTournament.writeBestChromosome(r.get(0));
                print(r);
                long time = System.currentTimeMillis();
                players = steadyState(r);
                System.out.println("\nTrained in " + (System.currentTimeMillis() - time) + " miliseconds");
            }
        }

        private List<LUDOPlayer> steadyState(List<ExtendedPlayerResult> r) {
            ((GAPlayer) r.get(0).getPlayer()).weights();
            List<LUDOPlayer> players = new ArrayList<LUDOPlayer>();
            for (int i = 0; i < r.size(); i = i + 2) {
                GAPlayer ga1 = ((GAPlayer) r.get(i).getPlayer()).clone();
                GAPlayer ga2 = ((GAPlayer) r.get(i + 1).getPlayer()).clone();
                GAPlayer ga3 = ((GAPlayer) r.get(i).getPlayer()).clone();
                GAPlayer ga4 = ((GAPlayer) r.get(i + 1).getPlayer()).clone();
                ga1.setParents(((GAPlayer) r.get(i).getPlayer()), ((GAPlayer) r.get(i + 1).getPlayer()));
                ga2.setParents(((GAPlayer) r.get(i).getPlayer()), ((GAPlayer) r.get(i + 1).getPlayer()));
                ga3.setParents(((GAPlayer) r.get(i).getPlayer()), ((GAPlayer) r.get(i + 1).getPlayer()));
                ga4.setParents(((GAPlayer) r.get(i).getPlayer()), ((GAPlayer) r.get(i + 1).getPlayer()));
                onePointCrossover(MANIPUTATING.Y, ga1, ga2);
                inversion(MANIPUTATING.Y, ga1);
                inversion(MANIPUTATING.Y, ga2);
                onePointCrossover(MANIPUTATING.X, ga3, ga4);
                inversion(MANIPUTATING.X, ga3);
                inversion(MANIPUTATING.X, ga4);
                ArrayList<ExtendedPlayerResult> rr = new ArrayList<ExtendedPlayerResult>();
                ExtendedPlayerResult r1old = (ExtendedPlayerResult) ExtendedTournament.evaluateBestPlayer(r.get(i).getPlayer(), evaluationPlayers);
                ExtendedPlayerResult r2old = (ExtendedPlayerResult) ExtendedTournament.evaluateBestPlayer(r.get(i + 1).getPlayer(), evaluationPlayers);
                ExtendedPlayerResult r1new = (ExtendedPlayerResult) ExtendedTournament.evaluateBestPlayer(ga1, evaluationPlayers);
                ExtendedPlayerResult r2new = (ExtendedPlayerResult) ExtendedTournament.evaluateBestPlayer(ga2, evaluationPlayers);
                ExtendedPlayerResult r3new = (ExtendedPlayerResult) ExtendedTournament.evaluateBestPlayer(ga3, evaluationPlayers);
                ExtendedPlayerResult r4new = (ExtendedPlayerResult) ExtendedTournament.evaluateBestPlayer(ga4, evaluationPlayers);
                rr.add(r1old);
                rr.add(r2old);
                rr.add(r1new);
                rr.add(r2new);
                rr.add(r3new);
                rr.add(r4new);
                Collections.sort(rr);
                players.add(rr.get(0).getPlayer());
                players.add(rr.get(1).getPlayer());
            }
            return players;
        }

        private void print(List<ExtendedPlayerResult> r) {
            int i = 0;
            for (ExtendedPlayerResult rr : r) {
                if (i++ == 5) break;
                System.out.println("Games: " + rr.getGames() + ", Wins: " + rr.getWins() + ", second: " + ((ExtendedPlayerResult) rr).getSecondPlace() + ", third: " + ((ExtendedPlayerResult) rr).getThirdPlace() + ", Losses: " + ((ExtendedPlayerResult) rr).getLosses() + ", Results: " + rr.getResult());
                System.out.println("-------------");
            }
        }
    }

    private GA() {
        super();
        new ControlledLUDO().train();
    }

    public static void main(String[] args) {
        new GA();
    }
}
