package csa.jportal.ai.standardAI.sim;

import csa.jportal.card.*;
import java.util.*;
import csa.jportal.card.*;
import csa.jportal.gui.*;
import java.util.*;

/**
 * AttackerFormation - contains all information about ONE blocker list and ONE attacker list
 *                     it contains all permutations of that one blocker list in regard to the one attacker list
 *
 * @author Malban
 */
public class AttackerFormation {

    CardList attackerCardList = new CardList();

    boolean sorceryMode = false;

    void setSorceryMode(boolean b) {
        sorceryMode = b;
    }

    Vector<CombatFormation> blockerKombinationsForAttacker = new Vector<CombatFormation>();

    public Vector<CombatFormation> getAllKombinations() {
        return blockerKombinationsForAttacker;
    }

    void sortAttackerDead() {
        Collections.sort(blockerKombinationsForAttacker, new Comparator<CombatFormation>() {

            public int compare(CombatFormation s1, CombatFormation s2) {
                return (s1.attackerDead - s2.attackerDead);
            }
        });
    }

    void sortBlockerDead() {
        Collections.sort(blockerKombinationsForAttacker, new Comparator<CombatFormation>() {

            public int compare(CombatFormation s1, CombatFormation s2) {
                return (s1.blockerDead - s2.blockerDead);
            }
        });
    }

    void sortBlockerUsed() {
        Collections.sort(blockerKombinationsForAttacker, new Comparator<CombatFormation>() {

            public int compare(CombatFormation s1, CombatFormation s2) {
                return (s1.unUsedBlockerList.size() - s2.unUsedBlockerList.size());
            }
        });
    }

    void sortSpellsUsed() {
        Collections.sort(blockerKombinationsForAttacker, new Comparator<CombatFormation>() {

            public int compare(CombatFormation s1, CombatFormation s2) {
                return (s1.spellsCast - s2.spellsCast);
            }
        });
    }

    void sortManaDead() {
        Collections.sort(blockerKombinationsForAttacker, new Comparator<CombatFormation>() {

            public int compare(CombatFormation s1, CombatFormation s2) {
                return (s1.attackerManaDead - s2.attackerManaDead);
            }
        });
    }

    void sortBlockerManaDead() {
        Collections.sort(blockerKombinationsForAttacker, new Comparator<CombatFormation>() {

            public int compare(CombatFormation s1, CombatFormation s2) {
                return (s1.blockerManaDead - s2.blockerManaDead);
            }
        });
    }

    void sortPlayerDamage() {
        Collections.sort(blockerKombinationsForAttacker, new Comparator<CombatFormation>() {

            public int compare(CombatFormation s1, CombatFormation s2) {
                return (s1.playerDamage - s2.playerDamage);
            }
        });
    }

    void playerDamageOnly(int pos) {
        pos = blockerKombinationsForAttacker.size() - 1 - pos;
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortPlayerDamage();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.attackerManaDead == sim.attackerManaDead) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void leastPlayerDamageOnly() {
        playerDamageOnly(0);
    }

    void manaDeadOnly(int pos) {
        pos = blockerKombinationsForAttacker.size() - 1 - pos;
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortManaDead();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.attackerManaDead == sim.attackerManaDead) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void maxManaDeadOnly() {
        manaDeadOnly(0);
    }

    void attackerManaDeadOnly(int pos) {
        pos = blockerKombinationsForAttacker.size() - 1 - pos;
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortManaDead();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.attackerManaDead == sim.attackerManaDead) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void maxAttackerManaDeadOnly() {
        attackerManaDeadOnly(0);
    }

    void attackerDeadOnly(int pos) {
        pos = blockerKombinationsForAttacker.size() - 1 - pos;
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortAttackerDead();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.attackerDead == sim.attackerDead) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void maxAttackerDeadOnly() {
        attackerDeadOnly(0);
    }

    void blockerManaDeadOnly(int pos) {
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortBlockerManaDead();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.blockerManaDead == sim.blockerManaDead) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void leastBlockerManaDeadOnly() {
        blockerManaDeadOnly(0);
    }

    void blockerDeadOnly(int pos) {
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortBlockerDead();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.blockerDead == sim.blockerDead) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void leastBlockerDeadOnly() {
        blockerDeadOnly(0);
    }

    void blockerUsed(int pos) {
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortBlockerUsed();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.unUsedBlockerList.size() == sim.unUsedBlockerList.size()) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void leastBlockerUsed() {
        blockerUsed(0);
    }

    void spellsOnly(int pos) {
        if (blockerKombinationsForAttacker.size() == 0) return;
        sortSpellsUsed();
        CombatFormation sim = blockerKombinationsForAttacker.elementAt(pos);
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.spellsCast == sim.spellsCast) {
                newList.addElement(s);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    void leastSpellsOnly() {
        spellsOnly(0);
    }

    void removeOwnDeadLarger() {
        if (blockerKombinationsForAttacker.size() == 0) return;
        Vector<CombatFormation> newList = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation s = blockerKombinationsForAttacker.elementAt(i);
            if (s.ownDead <= s.attackerDead) {
                newList.addElement(s);
            } else {
                csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
                D.addLog("Removing Suicide sorcery formation: " + s, 2);
            }
        }
        blockerKombinationsForAttacker = newList;
    }

    public int buildAllOrderPermutations(int maxBefore) {
        csa.jportal.config.Logable D = csa.jportal.config.Configuration.getConfiguration().getDebugEntity();
        D.addLog("Size: " + blockerKombinationsForAttacker.size() + ", sim started ->", 2);
        if (blockerKombinationsForAttacker.size() == 0) return maxBefore;
        if (maxBefore == -1) maxBefore = 0;
        int maxManaDead = maxBefore;
        if (!sorceryMode) {
            sim();
            maxAttackerDeadOnly();
            maxAttackerManaDeadOnly();
            maxManaDead = blockerKombinationsForAttacker.elementAt(0).attackerManaDead;
            D.addLog("MaxMana befor: " + maxBefore + " Max Mana here: " + maxManaDead, 2);
            if (maxBefore > maxManaDead) {
                blockerKombinationsForAttacker.clear();
                D.addLog("Not max Mana dead, removing! Size: " + blockerKombinationsForAttacker.size() + "", 2);
                return maxBefore;
            }
        }
        D.addLog("Unordered Combination size reduced (only max dead attacker): " + blockerKombinationsForAttacker.size(), 2);
        Vector<CombatFormation> allCombinations = new Vector<CombatFormation>();
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation blockerFormation = blockerKombinationsForAttacker.elementAt(i);
            Vector<CombatFormation> orderCombinations = blockerFormation.buildAllOrderPermutations();
            allCombinations.addAll(orderCombinations);
        }
        blockerKombinationsForAttacker = allCombinations;
        D.addLog(" Order Combinations : " + blockerKombinationsForAttacker.size(), 2);
        return maxManaDead;
    }

    public void sim() {
        ProgressPanel panel = null;
        int psize = blockerKombinationsForAttacker.size();
        if (psize > 10000) {
            panel = new ProgressPanel(csa.jportal.config.Configuration.getConfiguration().getMainFrame());
            panel.setText("Simulating combats! (" + blockerKombinationsForAttacker.size() + ")");
            panel.setVisible(true);
            panel.setProgress(0, psize / 1000);
        }
        psize = psize / 1000;
        int c = 0;
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation blockerFormation = blockerKombinationsForAttacker.elementAt(i);
            blockerFormation.sim();
            if (panel != null) {
                if (c != i / 1000) {
                    panel.setProgress(c, psize);
                }
                c = c = i / 1000;
            }
        }
        if (panel != null) {
            panel.setVisible(false);
            panel = null;
        }
    }

    CombatFormation getTopResult() {
        if (blockerKombinationsForAttacker.size() == 0) return null;
        return blockerKombinationsForAttacker.elementAt(0);
    }

    public void simSorcery(CardList casterLands, CardList ownCreatures) {
        ProgressPanel panel = null;
        int psize = blockerKombinationsForAttacker.size();
        if ((blockerKombinationsForAttacker.size() > 10000)) {
            panel = new ProgressPanel(csa.jportal.config.Configuration.getConfiguration().getMainFrame());
            panel.setText("Finding best (spell) combat strategy!");
            panel.setVisible(true);
            panel.setProgress(0, psize / 100);
        }
        int c = 0;
        for (int i = 0; i < blockerKombinationsForAttacker.size(); i++) {
            CombatFormation blockerFormation = blockerKombinationsForAttacker.elementAt(i);
            blockerFormation.simSorcery(casterLands, ownCreatures);
            if (panel != null) {
                if (c == 100) {
                    c = 0;
                    panel.setProgress(i / 100, psize / 100);
                }
            }
            c++;
        }
        if (panel != null) {
            panel.setVisible(false);
            panel = null;
        }
    }
}
