package csa.jportal.ai.standardAI.sim;

import csa.jportal.ai.standardAI.hints.AIHelper;
import csa.jportal.ai.*;
import csa.jportal.card.*;
import csa.jportal.gui.*;
import java.util.*;

/**
 * OneCombatSim - contains all information about a ONE blocker list and all possible attacker lists.
 * ->
 *
 * @author Malban
 */
public class OneCombatSim {

    boolean printResultList = true;

    AttackerFormation mAllInOneAttackerFormations = new AttackerFormation();

    Vector<AttackerFormation> mAttackerPossibelFormations = new Vector<AttackerFormation>();

    CardList mAttackers = new CardList();

    CardList mAllBlockers = new CardList();

    OneCombatSim(CardList attackers, CardList blockers, boolean mode) {
        mAttackers = attackers;
        mAllBlockers = blockers;
        sorceryMode = mode;
        mAllInOneAttackerFormations.setSorceryMode(sorceryMode);
    }

    final CombatFormation simBlockerKillAttacker() {
        CombatFormation good = null;
        buildAllAttackerBlockerKombis();
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        mAllInOneAttackerFormations.sim();
        D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), 2);
        mAllInOneAttackerFormations.leastBlockerDeadOnly();
        D.addLog("Least blocker dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), 2);
        mAllInOneAttackerFormations.leastBlockerManaDeadOnly();
        D.addLog("Least blockermana dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), 2);
        mAllInOneAttackerFormations.leastBlockerUsed();
        D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), 2);
        good = mAllInOneAttackerFormations.getTopResult();
        D.addLog("Best Result: " + good, 2);
        if (good != null) good.lastCleanup();
        D.addLog("Best Result cleanup: " + good, 2);
        return good;
    }

    private final void buildAllAttackerBlockerKombis() {
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        Vector<CardList> allAtackerPermutations = new Vector<CardList>();
        mAllInOneAttackerFormations = new AttackerFormation();
        mAllInOneAttackerFormations.setSorceryMode(sorceryMode);
        mAllInOneAttackerFormations.attackerCardList = mAttackers;
        for (int a = 0; a < mAttackers.size(); a++) {
            Vector<CardList> attackerPermutations = builtCombatPermutations(mAttackers, new CardList(), a + 1, null);
            allAtackerPermutations.addAll(attackerPermutations);
        }
        D.addLog("All Attacker Combis: " + allAtackerPermutations.size(), 2);
        ProgressPanel panel = null;
        int psize = allAtackerPermutations.size();
        if ((mAttackers.size() > 4) || (mAllBlockers.size() > 4)) {
            panel = new ProgressPanel(csa.jportal.config.Configuration.getConfiguration().getMainFrame());
            panel.setText("Finding best (attack) combat strategy!");
            panel.setVisible(true);
            panel.setProgress(0, psize);
        }
        for (int a = 0; a < allAtackerPermutations.size(); a++) {
            CardList list = allAtackerPermutations.elementAt(a);
            AttackerFormation al = new AttackerFormation();
            al.setSorceryMode(sorceryMode);
            al.attackerCardList = list;
            mAttackerPossibelFormations.addElement(al);
        }
        int maxManaDead = -1;
        for (int i = 0; i < allAtackerPermutations.size(); i++) {
            Vector<CombatFormation> forOneAttackAllKombis = new Vector<CombatFormation>();
            forOneAttackAllKombis = buildAllCombinations(allAtackerPermutations.elementAt(i), mAllBlockers, new CombatFormation(), sorceryMode);
            if (!sorceryMode) removeNotAllBlockersUsed(mAllBlockers, forOneAttackAllKombis);
            for (int t = 0; t < forOneAttackAllKombis.size(); t++) {
                CombatFormation bf = forOneAttackAllKombis.elementAt(t);
                bf.addMissingAttackers(mAttackers);
            }
            mAttackerPossibelFormations.elementAt(i).blockerKombinationsForAttacker = forOneAttackAllKombis;
            D.addLog("" + i + ". (" + mAttackerPossibelFormations.size() + ") ", 2);
            int newMaxManaDead = mAttackerPossibelFormations.elementAt(i).buildAllOrderPermutations(maxManaDead);
            if (maxManaDead < newMaxManaDead) {
                maxManaDead = newMaxManaDead;
                D.addLog("Overall Size cleared of: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), 2);
                mAllInOneAttackerFormations.blockerKombinationsForAttacker.clear();
            }
            mAllInOneAttackerFormations.blockerKombinationsForAttacker.addAll(mAttackerPossibelFormations.elementAt(i).blockerKombinationsForAttacker);
            if (panel != null) {
                panel.setProgress(i, psize);
            }
        }
        D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size() + ") ", 2);
        if (panel != null) {
            panel.setVisible(false);
            panel = null;
        }
        if (printResultList) {
            csa.jportal.config.Configuration.getConfiguration().getDebugEntity().addLog("Ordered Combination size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), 2);
            D.addLog("SingleFights todo: " + SingleFight.mMap.size(), 2);
        }
    }

    private static final Vector<CombatFormation> buildAllCombinations(CardList attackers, CardList blockers, CombatFormation one, boolean sorceryMode) {
        Vector<CombatFormation> all = new Vector<CombatFormation>();
        if (attackers.size() == 0) return all;
        CardList attackersRest = attackers.copyList();
        Card a1 = attackersRest.getCard(0);
        attackersRest.removeCard(attackersRest.getCard(0));
        Vector<CardList> allBlockerGroupsA1 = new Vector<CardList>();
        for (int a = 0; a < blockers.size(); a++) {
            Vector<CardList> blockerGroups = builtCombatPermutations(blockers, new CardList(), a + 1, a1);
            allBlockerGroupsA1.addAll(blockerGroups);
        }
        for (int i = 0; i < allBlockerGroupsA1.size(); i++) {
            SingleFight s = SingleFight.getFight(a1, allBlockerGroupsA1.elementAt(i));
            CombatFormation oneNew = new CombatFormation();
            for (int t = 0; t < one.singleFights.size(); t++) {
                oneNew.singleFights.addElement(one.singleFights.elementAt(t));
            }
            oneNew.singleFights.add(s);
            if (attackersRest.size() == 0) {
                all.add(oneNew);
            } else {
                Vector<CombatFormation> allOther = buildAllCombinations(attackersRest, invers(blockers, allBlockerGroupsA1.elementAt(i)), oneNew, sorceryMode);
                all.addAll(allOther);
            }
        }
        return all;
    }

    private static final CardList invers(CardList allBlockers, CardList usedBlockers) {
        CardList unused = new CardList();
        for (int i = 0; i < allBlockers.size(); i++) {
            Card c = allBlockers.getCard(i);
            if (!usedBlockers.isInList(c)) unused.addCard(c);
        }
        return unused;
    }

    static final Vector<CardList> builtCombatPermutations(CardList from, CardList work, int no, Card attacker) {
        Vector<CardList> ret = new Vector<CardList>();
        if (no == 1) {
            CardList fromNew = from.copyList();
            for (int i = 0; i < fromNew.size(); i++) {
                CardList workNew = work.copyList();
                Card card = fromNew.getCard(i);
                workNew.addCard(card);
                ret.addElement(workNew);
            }
            return ret;
        }
        CardList fromNew = from.copyList();
        int i = 0;
        while (fromNew.size() > 1) {
            CardList workNew = work.copyList();
            Card card = fromNew.getCard(i);
            fromNew.removeCard(card);
            workNew.addCard(card);
            ret.addAll(builtCombatPermutations(fromNew, workNew, no - 1, attacker));
        }
        return ret;
    }

    private final void removeNotAllBlockersUsed(CardList mAllBlockers, Vector<CombatFormation> combis) {
        Vector<CombatFormation> toBeRemoved = new Vector<CombatFormation>();
        for (int i = 0; i < combis.size(); i++) {
            CombatFormation blockerFormation = combis.elementAt(i);
            if (!blockerFormation.containsAll(mAllBlockers)) {
                toBeRemoved.addElement(blockerFormation);
            }
        }
        for (int i = 0; i < toBeRemoved.size(); i++) {
            combis.removeElement(toBeRemoved.elementAt(i));
        }
    }

    public CombatFormation simDefensiveBlocker(int intelligence, int minorMood) {
        int debuglevel = 2;
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        D.addLog("simDefensiveBlocker, " + intelligence + ", " + ", " + minorMood, debuglevel);
        CombatFormation good = null;
        buildAllAttackerBlockerKombis();
        mAllInOneAttackerFormations.sim();
        if (intelligence == 10) {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastPlayerDamageOnly();
            D.addLog("Least player damage: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerDeadOnly();
            D.addLog("Least blocker dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerManaDeadOnly();
            D.addLog("Least blockermana dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerUsed();
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        } else {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            int dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.playerDamageOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least player damage  size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blocker dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerManaDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blockermana dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerUsed(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        }
        D.addLog("Best Result: " + good, debuglevel);
        if (good != null) good.lastCleanup();
        D.addLog("Best Result cleanup: " + good, debuglevel);
        return good;
    }

    public CombatFormation simMediumBlocker(int intelligence, int minorMood) {
        int debuglevel = 2;
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        D.addLog("simMediumBlocker, " + intelligence + ", " + ", " + minorMood, debuglevel);
        CombatFormation good = null;
        buildAllAttackerBlockerKombis();
        mAllInOneAttackerFormations.sim();
        if (intelligence == 10) {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerDeadOnly();
            D.addLog("Least blocker dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerManaDeadOnly();
            D.addLog("Least blockermana dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerUsed();
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastPlayerDamageOnly();
            D.addLog("Least player damage: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        } else {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            int dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blocker dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerManaDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blockermana dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerUsed(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.playerDamageOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least player damage  size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        }
        D.addLog("Best Result: " + good, debuglevel);
        if (good != null) good.lastCleanup();
        D.addLog("Best Result cleanup: " + good, debuglevel);
        return good;
    }

    public CombatFormation simJustBlock(int intelligence, int minorMood) {
        int debuglevel = 2;
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        D.addLog("simMediumBlocker, " + intelligence + ", " + ", " + minorMood, debuglevel);
        CombatFormation good = null;
        buildAllAttackerBlockerKombis();
        mAllInOneAttackerFormations.sim();
        if (intelligence == 10) {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastPlayerDamageOnly();
            D.addLog("leastPlayerDamageOnly size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerUsed();
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        } else {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            int dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.playerDamageOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("playerDamageOnly: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerUsed(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        }
        D.addLog("Best Result: " + good, debuglevel);
        if (good != null) good.lastCleanup();
        D.addLog("Best Result cleanup: " + good, debuglevel);
        return good;
    }

    public CombatFormation simAgressiveBlocker(int intelligence, int minorMood) {
        int debuglevel = 2;
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        D.addLog("simAgressiveBlocker, " + intelligence + ", " + ", " + minorMood, debuglevel);
        CombatFormation good = null;
        buildAllAttackerBlockerKombis();
        mAllInOneAttackerFormations.sim();
        if (intelligence == 10) {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.maxAttackerDeadOnly();
            D.addLog("Least blocker dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.maxAttackerManaDeadOnly();
            D.addLog("Least blockermana dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastBlockerDeadOnly();
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastPlayerDamageOnly();
            D.addLog("Least player damage: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        } else {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            int dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.attackerDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blocker dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.attackerManaDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blockermana dead size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.blockerDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least blocker used size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.playerDamageOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("Least player damage  size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            good = mAllInOneAttackerFormations.getTopResult();
        }
        D.addLog("Best Result: " + good, debuglevel);
        if (good != null) good.lastCleanup();
        D.addLog("Best Result cleanup: " + good, debuglevel);
        return good;
    }

    boolean sorceryMode = false;

    final CombatFormation simSorceryKillCreature(CardList casterLands, CardList ownCreatures, int intelligence) {
        int debuglevel = 2;
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        D.addLog("simSorceryKillCreature, " + intelligence + ", ", debuglevel);
        D.addLog("own creatures: " + ownCreatures + ", ", debuglevel);
        CombatFormation good = null;
        sorceryMode = true;
        buildAllAttackerBlockerKombis();
        if (mAllInOneAttackerFormations.blockerKombinationsForAttacker.size() > 100000) {
            csa.jportal.config.Configuration.getConfiguration().getDebugEntity().addLog("Combat Sim To many possibities snailing along!", 3);
        }
        mAllInOneAttackerFormations.simSorcery(casterLands, ownCreatures);
        if (intelligence > 3) {
            mAllInOneAttackerFormations.removeOwnDeadLarger();
        }
        if (intelligence == 10) {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.maxAttackerDeadOnly();
            D.addLog("attackerDeadOnly size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.leastSpellsOnly();
            D.addLog("leastSpellsOnly size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            mAllInOneAttackerFormations.maxManaDeadOnly();
            D.addLog("maxManaDeadOnly size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
        } else {
            D.addLog("All: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            int dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.attackerDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("attackerDeadOnly size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.spellsOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("leastSpellsOnly size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
            dumbness = AIHelper.getDumbness(intelligence, mAllInOneAttackerFormations.blockerKombinationsForAttacker.size());
            D.addLog("dumbness: " + dumbness, debuglevel);
            mAllInOneAttackerFormations.manaDeadOnly(csa.Global.getRand().nextInt(dumbness));
            D.addLog("maxManaDeadOnly size: " + mAllInOneAttackerFormations.blockerKombinationsForAttacker.size(), debuglevel);
        }
        good = mAllInOneAttackerFormations.getTopResult();
        D.addLog("good 1: " + good, debuglevel);
        if (good == null) return null;
        if (good.attackerDead == 0) return null;
        D.addLog("good 2: " + good, debuglevel);
        good.reduceToSpellsUsed();
        if (good.singleFights.size() == 0) return null;
        D.addLog("good final : " + good, debuglevel);
        return good;
    }

    AttackerFormation getAllAttackerFormation() {
        int debuglevel = 2;
        CombatFormation good = null;
        buildAllAttackerBlockerKombis();
        return mAllInOneAttackerFormations;
    }
}
