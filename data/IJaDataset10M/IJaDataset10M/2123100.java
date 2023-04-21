package megamek.common.weapons;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import megamek.common.Aero;
import megamek.common.BattleArmor;
import megamek.common.Building;
import megamek.common.Compute;
import megamek.common.Coords;
import megamek.common.Entity;
import megamek.common.HitData;
import megamek.common.IGame;
import megamek.common.ITerrain;
import megamek.common.Infantry;
import megamek.common.Mech;
import megamek.common.Mounted;
import megamek.common.RangeType;
import megamek.common.Report;
import megamek.common.TargetRoll;
import megamek.common.Targetable;
import megamek.common.Terrains;
import megamek.common.ToHitData;
import megamek.common.WeaponType;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;
import megamek.server.Server.DamageType;

/**
 * @author Andrew Hunter A basic, simple attack handler. May or may not work for
 *         any particular weapon; must be overloaded to support special rules.
 */
public class WeaponHandler implements AttackHandler, Serializable {

    private static final long serialVersionUID = 7137408139594693559L;

    public ToHitData toHit;

    public WeaponAttackAction waa;

    public int roll;

    protected IGame game;

    protected transient Server server;

    protected boolean bMissed;

    protected boolean bSalvo = false;

    protected boolean bGlancing = false;

    protected boolean bDirect = false;

    protected boolean nukeS2S = false;

    protected WeaponType wtype;

    protected Mounted weapon;

    protected Entity ae;

    protected Targetable target;

    protected int subjectId;

    protected int nRange;

    protected int nDamPerHit;

    protected int attackValue;

    protected boolean throughFront;

    protected boolean underWater;

    protected boolean announcedEntityFiring = false;

    protected boolean missed = false;

    protected DamageType damageType;

    protected int generalDamageType = HitData.DAMAGE_NONE;

    protected Vector<Integer> insertedAttacks = new Vector<Integer>();

    protected int nweapons;

    /**
     * return the <code>int</code> Id of the attacking <code>Entity</code>
     */
    public int getAttackerId() {
        return ae.getId();
    }

    /**
     * Do we care about the specified phase?
     */
    public boolean cares(IGame.Phase phase) {
        if (phase == IGame.Phase.PHASE_FIRING) {
            return true;
        }
        return false;
    }

    /**
     * @param vPhaseReport - A <code>Vector</code> containing the phasereport.
     * @return a <code>boolean</code> value indicating wether or not the
     *         attack misses because of a failed check.
     */
    protected boolean doChecks(Vector<Report> vPhaseReport) {
        return false;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        server = Server.getServerInstance();
    }

    /**
     * @return a <code>boolean</code> value indicating wether or not this
     *         attack needs further calculating, like a missed shot hitting a
     *         building, or an AMS only shooting down some missiles.
     */
    protected boolean handleSpecialMiss(Entity entityTarget, boolean targetInBuilding, Building bldg, Vector<Report> vPhaseReport) {
        if ((entityTarget != null) && ((bldg == null) && (wtype.getFireTN() != TargetRoll.IMPOSSIBLE))) {
            server.tryIgniteHex(target.getPosition(), subjectId, false, false, new TargetRoll(wtype.getFireTN(), wtype.getName()), 3, vPhaseReport);
        }
        server.checkExplodeIndustrialZone(target.getPosition(), vPhaseReport);
        if (!targetInBuilding || (toHit.getValue() == TargetRoll.AUTOMATIC_FAIL)) {
            return false;
        }
        return true;
    }

    /**
     * Calculate the number of hits
     *
     * @param vPhaseReport - the <code>Vector</code> containing the phase
     *            report.
     * @return an <code>int</code> containing the number of hits.
     */
    protected int calcHits(Vector<Report> vPhaseReport) {
        if ((ae instanceof BattleArmor) && (weapon.getLocation() == BattleArmor.LOC_SQUAD) && !(ae.getSwarmTargetId() == target.getTargetId())) {
            bSalvo = true;
            int toReturn = allShotsHit() ? ((BattleArmor) ae).getShootingStrength() : Compute.missilesHit(((BattleArmor) ae).getShootingStrength());
            Report r = new Report(3325);
            r.subject = subjectId;
            r.add(toReturn);
            r.add(" troopers ");
            r.add(toHit.getTableDesc());
            vPhaseReport.add(r);
            return toReturn;
        }
        if (ae instanceof BattleArmor) {
            BattleArmor ba = (BattleArmor) ae;
            if (!ba.isAttacksDuringSwarmResolved()) {
                for (AttackHandler ah : server.getGame().getAttacksVector()) {
                    if ((ah.getAttackerId() == subjectId) && !(ah.equals(this)) && (ah.getWaa().getTargetId() == target.getTargetId()) && (((WeaponHandler) ah).toHit.getValue() == TargetRoll.AUTOMATIC_SUCCESS)) {
                        WeaponType baWType = (WeaponType) ba.getEquipment(ah.getWaa().getWeaponId()).getType();
                        int addToDamage = baWType.getDamage(nRange);
                        if (ba.getEquipment().get(ah.getWaa().getWeaponId()).getLocation() == BattleArmor.LOC_SQUAD) {
                            addToDamage *= ba.getShootingStrength();
                        }
                        nDamPerHit += addToDamage;
                    }
                    if (ba.hasMyomerBooster()) {
                        nDamPerHit += ba.getTroopers() * 2;
                    }
                }
                ba.setAttacksDuringSwarmResolved(true);
            } else {
                Report r = new Report(3375);
                r.subject = subjectId;
                r.add(wtype.getDamage(nRange));
                r.indent(2);
                vPhaseReport.add(r);
                return 0;
            }
        }
        return 1;
    }

    /**
     * Calculate the clustering of the hits
     *
     * @return a <code>int</code> value saying how much hits are in each
     *         cluster of damage.
     */
    protected int calcnCluster() {
        return 1;
    }

    /**
     * handle this weapons firing
     *
     * @return a <code>boolean</code> value indicating whether this should be
     *         kept or not
     */
    public boolean handle(IGame.Phase phase, Vector<Report> vPhaseReport) {
        if (!cares(phase)) {
            return true;
        }
        insertAttacks(phase, vPhaseReport);
        Entity entityTarget = (target.getTargetType() == Targetable.TYPE_ENTITY) ? (Entity) target : null;
        final boolean targetInBuilding = Compute.isInBuilding(game, entityTarget);
        if (entityTarget != null) {
            ae.setLastTarget(entityTarget.getId());
        }
        Building bldg = game.getBoard().getBuildingAt(target.getPosition());
        String number = nweapons > 1 ? " (" + nweapons + ")" : "";
        Report r = new Report(3115);
        r.indent();
        r.newlines = 0;
        r.subject = subjectId;
        r.add(wtype.getName() + number);
        if (entityTarget != null) {
            r.addDesc(entityTarget);
        } else {
            r.messageId = 3120;
            r.add(target.getDisplayName(), true);
        }
        vPhaseReport.addElement(r);
        if (toHit.getValue() == TargetRoll.IMPOSSIBLE) {
            r = new Report(3135);
            r.subject = subjectId;
            r.add(toHit.getDesc());
            vPhaseReport.addElement(r);
            return false;
        } else if (toHit.getValue() == TargetRoll.AUTOMATIC_FAIL) {
            r = new Report(3140);
            r.newlines = 0;
            r.subject = subjectId;
            r.add(toHit.getDesc());
            vPhaseReport.addElement(r);
        } else if (toHit.getValue() == TargetRoll.AUTOMATIC_SUCCESS) {
            r = new Report(3145);
            r.newlines = 0;
            r.subject = subjectId;
            r.add(toHit.getDesc());
            vPhaseReport.addElement(r);
        } else {
            r = new Report(3150);
            r.newlines = 0;
            r.subject = subjectId;
            r.add(toHit.getValue());
            vPhaseReport.addElement(r);
        }
        r = new Report(3155);
        r.newlines = 0;
        r.subject = subjectId;
        r.add(roll);
        vPhaseReport.addElement(r);
        bMissed = roll < toHit.getValue();
        if (game.getOptions().booleanOption("tacops_glancing_blows")) {
            if (roll == toHit.getValue()) {
                bGlancing = true;
                r = new Report(3186);
                r.subject = ae.getId();
                r.newlines = 0;
                vPhaseReport.addElement(r);
            } else {
                bGlancing = false;
            }
        } else {
            bGlancing = false;
        }
        toHit.setMoS(roll - Math.max(2, toHit.getValue()));
        bDirect = game.getOptions().booleanOption("tacops_direct_blow") && ((toHit.getMoS() / 3) >= 1) && (entityTarget != null);
        if (bDirect) {
            r = new Report(3189);
            r.subject = ae.getId();
            r.newlines = 0;
            vPhaseReport.addElement(r);
        }
        nDamPerHit = calcDamagePerHit();
        addHeat();
        boolean missReported = doChecks(vPhaseReport);
        if (missReported) {
            bMissed = true;
        }
        if (specialResolution(vPhaseReport, entityTarget)) {
            return false;
        }
        if (bMissed && !missReported) {
            reportMiss(vPhaseReport);
            if (!handleSpecialMiss(entityTarget, targetInBuilding, bldg, vPhaseReport)) {
                return false;
            }
        }
        int hits = 1;
        if (!(target.isAirborne())) {
            hits = calcHits(vPhaseReport);
        }
        int nCluster = calcnCluster();
        if (target.isAirborne()) {
            if (wtype.hasFlag(WeaponType.F_SPACE_BOMB)) {
                bSalvo = true;
                nDamPerHit = 1;
                hits = attackValue;
                nCluster = 5;
            } else if (ae.isCapitalFighter()) {
                bSalvo = true;
                int nhit = 1;
                if (nweapons > 1) {
                    nhit = Compute.missilesHit(nweapons, ((Aero) ae).getClusterMods());
                    r = new Report(3325);
                    r.subject = subjectId;
                    r.add(nhit);
                    r.add(" weapon(s) ");
                    r.add(" ");
                    vPhaseReport.add(r);
                }
                nDamPerHit = attackValue * nhit;
                hits = 1;
                nCluster = 1;
            } else if (usesClusterTable() && (entityTarget != null) && !entityTarget.isCapitalScale()) {
                bSalvo = true;
                nDamPerHit = 1;
                hits = attackValue;
                nCluster = 5;
            } else {
                nDamPerHit = attackValue;
                hits = 1;
                nCluster = 1;
            }
        }
        if (bMissed) {
            return false;
        }
        int bldgAbsorbs = 0;
        if (targetInBuilding && (bldg != null)) {
            bldgAbsorbs = bldg.getAbsorbtion(target.getPosition());
        }
        if (hits == 0) {
            r = new Report(3365);
            r.subject = subjectId;
            vPhaseReport.addElement(r);
        }
        while (hits > 0) {
            int nDamage;
            if ((target.getTargetType() == Targetable.TYPE_HEX_IGNITE) || (target.getTargetType() == Targetable.TYPE_BLDG_IGNITE)) {
                handleIgnitionDamage(vPhaseReport, bldg, hits);
                return false;
            }
            if (target.getTargetType() == Targetable.TYPE_HEX_CLEAR) {
                nDamage = nDamPerHit * hits;
                handleClearDamage(vPhaseReport, bldg, nDamage);
                return false;
            }
            if (target.getTargetType() == Targetable.TYPE_BUILDING) {
                nDamage = nDamPerHit * hits;
                handleBuildingDamage(vPhaseReport, bldg, nDamage, target.getPosition());
                return false;
            }
            if (entityTarget != null) {
                handleEntityDamage(entityTarget, vPhaseReport, bldg, hits, nCluster, bldgAbsorbs);
                server.creditKill(entityTarget, ae);
                hits -= nCluster;
            }
        }
        Report.addNewline(vPhaseReport);
        return false;
    }

    /**
     * Calculate the damage per hit.
     *
     * @return an <code>int</code> representing the damage dealt per hit.
     */
    protected int calcDamagePerHit() {
        double toReturn = wtype.getDamage(nRange);
        if ((ae instanceof BattleArmor) && (ae.getSwarmTargetId() == target.getTargetId())) {
            BattleArmor ba = (BattleArmor) ae;
            if (weapon.getLocation() == BattleArmor.LOC_SQUAD) {
                toReturn *= ba.getShootingStrength();
            }
        }
        if ((target instanceof Infantry) && !(target instanceof BattleArmor)) {
            toReturn = Compute.directBlowInfantryDamage(toReturn, bDirect ? toHit.getMoS() / 3 : 0, wtype.getInfantryDamageClass(), ((Infantry) target).isMechanized());
        } else if (bDirect) {
            toReturn = Math.min(toReturn + (toHit.getMoS() / 3), toReturn * 2);
        }
        if (bGlancing) {
            toReturn = (int) Math.floor(toReturn / 2.0);
        }
        if (game.getOptions().booleanOption("tacops_range") && (nRange > wtype.getRanges(weapon)[RangeType.RANGE_LONG])) {
            toReturn = (int) Math.floor(toReturn * .75);
        }
        return (int) toReturn;
    }

    /**
     * Calculate the attack value based on range
     *
     * @return an <code>int</code> representing the attack value at that range.
     */
    protected int calcAttackValue() {
        int av = 0;
        if (!ae.isAirborne()) {
            if (usesClusterTable()) {
                av = wtype.getRoundShortAV();
            } else {
                av = wtype.getDamage(nRange);
            }
        } else {
            int range = RangeType.rangeBracket(nRange, wtype.getATRanges(), true);
            if (range == WeaponType.RANGE_SHORT) {
                av = wtype.getRoundShortAV();
            } else if (range == WeaponType.RANGE_MED) {
                av = wtype.getRoundMedAV();
            } else if (range == WeaponType.RANGE_LONG) {
                av = wtype.getRoundLongAV();
            } else if (range == WeaponType.RANGE_EXT) {
                av = wtype.getRoundExtAV();
            }
        }
        return av;
    }

    /****
     * adjustment factor on attack value for fighter squadrons
     */
    protected double getBracketingMultiplier() {
        double mult = 1.0;
        if (wtype.hasModes() && weapon.curMode().equals("Bracket 80%")) {
            mult = 0.8;
        }
        if (wtype.hasModes() && weapon.curMode().equals("Bracket 60%")) {
            mult = 0.6;
        }
        if (wtype.hasModes() && weapon.curMode().equals("Bracket 40%")) {
            mult = 0.4;
        }
        return mult;
    }

    protected int getCapMisMod() {
        return 0;
    }

    /**
     * Handle damage against an entity, called once per hit by default.
     *
     * @param entityTarget
     * @param vPhaseReport
     * @param bldg
     * @param hits
     * @param nCluster
     * @param bldgAbsorbs
     */
    protected void handleEntityDamage(Entity entityTarget, Vector<Report> vPhaseReport, Building bldg, int hits, int nCluster, int bldgAbsorbs) {
        int nDamage;
        missed = false;
        HitData hit = entityTarget.rollHitLocation(toHit.getHitTable(), toHit.getSideTable(), waa.getAimedLocation(), waa.getAimingMode());
        hit.setGeneralDamageType(generalDamageType);
        hit.setCapital(wtype.isCapital());
        hit.setBoxCars(roll == 12);
        hit.setCapMisCritMod(getCapMisMod());
        if (weapon.isWeaponGroup()) {
            hit.setSingleAV(attackValue);
        }
        boolean isIndirect = wtype.hasModes() && weapon.curMode().equals("Indirect");
        if (!isIndirect && entityTarget.removePartialCoverHits(hit.getLocation(), toHit.getCover(), Compute.targetSideTable(ae, entityTarget, weapon.getCalledShot().getCall()))) {
            Report r = new Report(3460);
            r.subject = subjectId;
            r.add(entityTarget.getShortName());
            r.add(entityTarget.getLocationAbbr(hit));
            r.indent(2);
            vPhaseReport.addElement(r);
            nDamage = 0;
            missed = true;
            return;
        }
        if (!bSalvo) {
            Report r = new Report(3405);
            r.subject = subjectId;
            r.add(toHit.getTableDesc());
            r.add(entityTarget.getLocationAbbr(hit));
            vPhaseReport.addElement(r);
        } else {
            Report.addNewline(vPhaseReport);
        }
        if (hit.hitAimedLocation() && !bSalvo) {
            Report r = new Report(3410);
            r.subject = subjectId;
            r.newlines = 0;
            vPhaseReport.addElement(r);
        }
        nDamage = nDamPerHit * Math.min(nCluster, hits);
        if (bDirect) {
            hit.makeDirectBlow(toHit.getMoS() / 3);
        }
        if (bldgAbsorbs > 0) {
            int toBldg = Math.min(bldgAbsorbs, nDamage);
            nDamage -= toBldg;
            Report.addNewline(vPhaseReport);
            Vector<Report> buildingReport = server.damageBuilding(bldg, toBldg, entityTarget.getPosition());
            for (Report report : buildingReport) {
                report.subject = subjectId;
            }
            vPhaseReport.addAll(buildingReport);
        }
        nDamage = checkTerrain(nDamage, entityTarget, vPhaseReport);
        nDamage = checkLI(nDamage, entityTarget, vPhaseReport);
        if (null != bldg) {
            nDamage = (int) Math.floor(bldg.getDamageToScale() * nDamage);
        }
        if (nDamage == 0) {
            Report r = new Report(3415);
            r.subject = subjectId;
            r.indent(2);
            r.addDesc(entityTarget);
            r.newlines = 0;
            vPhaseReport.addElement(r);
            missed = true;
        } else {
            if (bGlancing) {
                hit.makeGlancingBlow();
            }
            vPhaseReport.addAll(server.damageEntity(entityTarget, hit, nDamage, false, ae.getSwarmTargetId() == entityTarget.getId() ? DamageType.IGNORE_PASSENGER : damageType, false, false, throughFront, underWater, nukeS2S));
            if (hit.hitAimedLocation() && bSalvo) {
                Report r = new Report(3410);
                r.subject = subjectId;
                r.newlines = 0;
                vPhaseReport.addElement(r);
            }
        }
    }

    protected void handleIgnitionDamage(Vector<Report> vPhaseReport, Building bldg, int hits) {
        if (!bSalvo) {
            Report r = new Report(2270);
            r.subject = subjectId;
            r.newlines = 0;
            vPhaseReport.addElement(r);
        }
        TargetRoll tn = new TargetRoll(wtype.getFireTN(), wtype.getName());
        if (tn.getValue() != TargetRoll.IMPOSSIBLE) {
            Report.addNewline(vPhaseReport);
            server.tryIgniteHex(target.getPosition(), subjectId, false, false, tn, true, -1, vPhaseReport);
        }
    }

    protected void handleClearDamage(Vector<Report> vPhaseReport, Building bldg, int nDamage) {
        if (!bSalvo) {
            Report r = new Report(2270);
            r.subject = subjectId;
            r.newlines = 0;
            vPhaseReport.addElement(r);
        }
        Report r = new Report(3385);
        r.indent();
        r.subject = subjectId;
        r.add(nDamage);
        vPhaseReport.addElement(r);
        if ((bldg != null) && server.tryIgniteHex(target.getPosition(), subjectId, false, false, new TargetRoll(wtype.getFireTN(), wtype.getName()), 5, vPhaseReport)) {
            return;
        }
        vPhaseReport.addAll(server.tryClearHex(target.getPosition(), nDamage, subjectId));
        return;
    }

    protected void handleBuildingDamage(Vector<Report> vPhaseReport, Building bldg, int nDamage, Coords coords) {
        if (!bSalvo) {
            Report r = new Report(3390);
            r.subject = subjectId;
            vPhaseReport.addElement(r);
        }
        Report.addNewline(vPhaseReport);
        Vector<Report> buildingReport = server.damageBuilding(bldg, nDamage, coords);
        for (Report report : buildingReport) {
            report.subject = subjectId;
        }
        vPhaseReport.addAll(buildingReport);
        vPhaseReport.addAll(server.damageInfantryIn(bldg, nDamage, coords, wtype.getInfantryDamageClass()));
    }

    protected boolean allShotsHit() {
        if ((((target.getTargetType() == Targetable.TYPE_BLDG_IGNITE) || (target.getTargetType() == Targetable.TYPE_BUILDING)) && (nRange <= 1)) || (target.getTargetType() == Targetable.TYPE_HEX_CLEAR)) {
            return true;
        }
        return false;
    }

    protected void reportMiss(Vector<Report> vPhaseReport) {
        Report r = new Report(3220);
        r.subject = subjectId;
        r.newlines = 2;
        vPhaseReport.addElement(r);
    }

    protected WeaponHandler() {
    }

    public WeaponHandler(ToHitData t, WeaponAttackAction w, IGame g, Server s) {
        damageType = DamageType.NONE;
        toHit = t;
        waa = w;
        game = g;
        ae = game.getEntity(waa.getEntityId());
        weapon = ae.getEquipment(waa.getWeaponId());
        wtype = (WeaponType) weapon.getType();
        target = game.getTarget(waa.getTargetType(), waa.getTargetId());
        server = s;
        subjectId = getAttackerId();
        nRange = Compute.effectiveDistance(game, ae, target);
        if (target instanceof Mech) {
            throughFront = Compute.isThroughFrontHex(game, ae.getPosition(), (Entity) target);
        } else {
            throughFront = true;
        }
        underWater = toHit.getHitTable() == ToHitData.HIT_UNDERWATER;
        if (null != ae.getCrew()) {
            roll = ae.getCrew().rollGunnerySkill();
        } else {
            roll = Compute.d6(2);
        }
        nweapons = getNumberWeapons();
        for (int i = 0; i < nweapons; i++) {
            useAmmo();
        }
        attackValue = (int) Math.floor(getBracketingMultiplier() * calcAttackValue());
    }

    protected void useAmmo() {
        setDone();
    }

    protected void setDone() {
        weapon.setUsedThisRound(true);
    }

    protected void addHeat() {
        if (!(toHit.getValue() == TargetRoll.IMPOSSIBLE)) {
            ae.heatBuildup += (weapon.getCurrentHeat());
        }
    }

    /**
     * Does this attack use the cluster hit table?
     * necessary to determine how Aero damage should be applied
     */
    protected boolean usesClusterTable() {
        return false;
    }

    /**
     * special resolution, like minefields and arty
     *
     * @param vPhaseReport - a <code>Vector</code> containing the phase report
     * @param entityTarget - the <code>Entity</code> targeted, or
     *            <code>null</code>, if no Entity targeted
     * @return true when done with processing, false when not
     */
    protected boolean specialResolution(Vector<Report> vPhaseReport, Entity entityTarget) {
        return false;
    }

    public boolean announcedEntityFiring() {
        return announcedEntityFiring;
    }

    public void setAnnouncedEntityFiring(boolean announcedEntityFiring) {
        this.announcedEntityFiring = announcedEntityFiring;
    }

    public WeaponAttackAction getWaa() {
        return waa;
    }

    public int checkTerrain(int nDamage, Entity entityTarget, Vector<Report> vPhaseReport) {
        boolean isAboveWoods = ((entityTarget != null) && (entityTarget.absHeight() >= 2));
        if (game.getOptions().booleanOption("tacops_woods_cover") && !isAboveWoods && (game.getBoard().getHex(entityTarget.getPosition()).containsTerrain(Terrains.WOODS) || game.getBoard().getHex(entityTarget.getPosition()).containsTerrain(Terrains.JUNGLE)) && !(entityTarget.getSwarmAttackerId() == ae.getId())) {
            ITerrain woodHex = game.getBoard().getHex(entityTarget.getPosition()).getTerrain(Terrains.WOODS);
            ITerrain jungleHex = game.getBoard().getHex(entityTarget.getPosition()).getTerrain(Terrains.JUNGLE);
            int treeAbsorbs = 0;
            String hexType = "";
            if (woodHex != null) {
                treeAbsorbs = woodHex.getLevel() * 2;
                hexType = "wooded";
            } else if (jungleHex != null) {
                treeAbsorbs = jungleHex.getLevel() * 2;
                hexType = "jungle";
            }
            treeAbsorbs = Math.min(nDamage, treeAbsorbs);
            nDamage = Math.max(0, nDamage - treeAbsorbs);
            server.tryClearHex(entityTarget.getPosition(), treeAbsorbs, ae.getId());
            Report.addNewline(vPhaseReport);
            Report terrainReport = new Report(6427);
            terrainReport.subject = entityTarget.getId();
            terrainReport.add(hexType);
            terrainReport.add(treeAbsorbs);
            terrainReport.indent(2);
            terrainReport.newlines = 0;
            vPhaseReport.add(terrainReport);
        }
        return nDamage;
    }

    /**
     * Check for Laser Inhibiting smoke clouds
     */
    public int checkLI(int nDamage, Entity entityTarget, Vector<Report> vPhaseReport) {
        weapon = ae.getEquipment(waa.getWeaponId());
        wtype = (WeaponType) weapon.getType();
        ArrayList<Coords> coords = Coords.intervening(ae.getPosition(), entityTarget.getPosition());
        int refrac = 0;
        double travel = 0;
        double range = ae.getPosition().distance(target.getPosition());
        double atkLev = ae.absHeight();
        double tarLev = entityTarget.absHeight();
        double levDif = Math.abs(atkLev - tarLev);
        String hexType = "LASER inhibiting smoke";
        for (Coords curr : coords) {
            ITerrain smokeHex = game.getBoard().getHex(curr).getTerrain(Terrains.SMOKE);
            if (game.getBoard().getHex(curr).containsTerrain(Terrains.SMOKE) && wtype.hasFlag(WeaponType.F_ENERGY) && ((smokeHex.getLevel() == 3) || (smokeHex.getLevel() == 4))) {
                int levit = ((game.getBoard().getHex(curr).getElevation()) + 2);
                if ((tarLev > atkLev) && (levit >= ((travel * (levDif / range)) + atkLev))) {
                    refrac++;
                } else if ((atkLev > tarLev) && (levit >= (((range - travel) * (levDif / range)) + tarLev))) {
                    refrac++;
                } else if ((atkLev == tarLev) && (levit >= 0)) {
                    refrac++;
                }
                travel++;
            }
        }
        if (refrac != 0) {
            refrac = (refrac * 2);
            refrac = Math.min(nDamage, refrac);
            nDamage = Math.max(0, (nDamage - refrac));
            Report.addNewline(vPhaseReport);
            Report fogReport = new Report(6427);
            fogReport.subject = entityTarget.getId();
            fogReport.add(hexType);
            fogReport.add(refrac);
            fogReport.indent(2);
            fogReport.newlines = 0;
            vPhaseReport.add(fogReport);
        }
        return nDamage;
    }

    protected boolean canDoDirectBlowDamage() {
        return true;
    }

    /**
     * Insert any additionaly attacks that should occur before this attack
     */
    protected void insertAttacks(IGame.Phase phase, Vector<Report> vPhaseReport) {
        return;
    }

    /**
     * @return the number of weapons of this type firing (for squadron weapon groups)
     */
    protected int getNumberWeapons() {
        return weapon.getNWeapons();
    }
}
