package cummingsm.unitTests;

import java.util.List;
import concreateAPI.ConcreateRCreature;
import concreateAPI.ConcreateRLivingSpace;
import creatureAI_API.supportInterfaces.RCreature;
import cummingsm.Creature;
import cummingsm.CreatureCore;
import cummingsm.LivableLocations.GridSpace;
import cummingsm.interfaces.Item;
import cummingsm.interfaces.LivableLocation;
import cummingsm.items.HealthPack;
import junit.framework.TestCase;

public class CreatureTests extends TestCase {

    private CreatureCore cc;

    private CreatureCore cc2;

    private LivableLocation ll;

    private Creature creatureWin;

    private Creature creatureLoose;

    protected void setUp() throws Exception {
        super.setUp();
        cc = new CreatureCore(CreatureCore.CreatureTypes.Zombie);
        cc.setBaseAttackPower(6);
        cc.setBaseLife(40);
        cc.setBaseDefencePower(2);
        cc.setRace("BobWinner");
        cc2 = new CreatureCore(CreatureCore.CreatureTypes.Zombie);
        cc2.setBaseAttackPower(10);
        cc2.setBaseLife(30);
        cc2.setBaseDefencePower(0);
        cc2.setRace("ZedLoser");
        creatureWin = new Creature(cc);
        creatureLoose = new Creature(cc2);
        ll = new GridSpace(null);
        ll.addCreature(creatureWin);
        ll.addCreature(creatureLoose);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOneNorthMovement() {
    }

    public void testHealthpackCreatureRemovial() {
        HealthPack hp = new HealthPack(-10, 5.0);
        assertEquals(40.0, creatureWin.getCurrentHealth());
        assertEquals(30.0, creatureLoose.getCurrentHealth());
        creatureWin.addItem(hp);
        assertTrue(creatureWin.contains(hp));
        assertTrue(creatureWin.useItem(hp.getName()));
        assertFalse(creatureWin.contains(hp));
        creatureWin.attack(creatureLoose);
        assertEquals(24.0, creatureLoose.getCurrentHealth());
        creatureLoose.addItem(hp);
        assertTrue(creatureLoose.contains(hp));
        for (Item s : creatureLoose.getItemList()) {
            assertEquals(hp.getName(), s.toString());
        }
        assertTrue(creatureLoose.useItem(hp.getName()));
        assertFalse(creatureLoose.contains(hp));
        assertEquals(29.0, creatureLoose.getCurrentHealth());
    }

    public void testHealingItem() {
        HealthPack hp = new HealthPack(-11, 5.0);
        assertEquals(40.0, creatureWin.getCurrentHealth());
        assertEquals(30.0, creatureLoose.getCurrentHealth());
        creatureLoose.accept(hp);
        assertEquals(30.0, creatureLoose.getCurrentHealth());
        creatureWin.attack(creatureLoose);
        assertEquals(24.0, creatureLoose.getCurrentHealth());
        creatureLoose.accept(hp);
        assertEquals(29.0, creatureLoose.getCurrentHealth());
        creatureLoose.accept(hp);
        assertEquals(30.0, creatureLoose.getCurrentHealth());
    }

    public void testCreatureAttackAndDeath() {
        assertTrue(ll.contains(creatureWin));
        assertTrue(ll.contains(creatureLoose));
        assertEquals(40.0, creatureWin.getCurrentHealth());
        assertEquals(30.0, creatureLoose.getCurrentHealth());
        assertFalse(creatureWin.isDead());
        assertFalse(creatureLoose.isDead());
        creatureWin.attack(creatureLoose);
        assertEquals(40.0, creatureWin.getCurrentHealth());
        assertEquals(24.0, creatureLoose.getCurrentHealth());
        creatureWin.attack(creatureLoose);
        creatureWin.attack(creatureLoose);
        creatureWin.attack(creatureLoose);
        assertEquals(6.0, creatureLoose.getCurrentHealth());
        assertFalse(creatureLoose.isDead());
        assertFalse(creatureWin.isDead());
        creatureWin.attack(creatureLoose);
        assertEquals(0.0, creatureLoose.getCurrentHealth());
        assertTrue(creatureLoose.isDead());
        assertFalse(creatureWin.isDead());
        assertTrue(ll.contains(creatureWin));
        ConcreateRLivingSpace crls = new ConcreateRLivingSpace(ll);
        List<RCreature> RCreatureList = crls.getCreaturesInRoom();
        ConcreateRCreature crc = new ConcreateRCreature(creatureLoose);
        int idToMatch = crc.getCreatureID();
        for (RCreature RCreature : RCreatureList) {
            if (RCreature.getCreatureID() == idToMatch) {
                fail();
            }
        }
    }
}
