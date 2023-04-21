package sratworld.base;

import sratworld.meta.event.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/** Testcases for Usecases for the SRaT World

    @Author: Henrik B�rbak Christensen 2007
*/
public class TestUseCases {

    Actor hero;

    Actor foe;

    Area plains, camp;

    Event move;

    AttackEvent attackEvent;

    @Before
    public void setup() {
        camp = new AreaTestImpl("camp", null);
        plains = new AreaTestImpl("cold-plains", camp);
        hero = new ActorTestImpl("Amazon", plains);
        hero.getAttributes().putInt("hero", 1);
        hero.getAttributes().putInt("row", 1);
        hero.getAttributes().putInt("col", 1);
        foe = new ActorTestImpl("Beast", plains);
        attackEvent = new AttackEvent();
        move = new MoveEvent();
    }

    /** Move hero North East */
    @Test
    public void moveNEAllowed() {
        move.triggeredExecute(hero, '9');
        assertEquals(0, hero.getAttributes().getInt("row"));
        assertEquals(2, hero.getAttributes().getInt("col"));
    }

    /** Move hero North East but make NE tile unpassable */
    @Test
    public void moveNENotAllowed() {
        Tile t = plains.getTile(0, 2);
        t.getAttributes().putInt("is-passable", 0);
        move.triggeredExecute(hero, '9');
        assertEquals(1, hero.getAttributes().getInt("row"));
        assertEquals(1, hero.getAttributes().getInt("col"));
    }

    /** Pick up an sword from NE tile */
    @Test
    public void pickUpSword() {
        List<Item> inventory = hero.getInventory();
        assertEquals(2, inventory.size());
        Event pickup = new PickupEvent();
        pickup.triggeredExecute(hero, '9');
        Tile t = plains.getTile(0, 2);
        assertEquals(0, t.getAttributes().getInt("has-item"));
        inventory = hero.getInventory();
        assertEquals(2 + 1, inventory.size());
        Item item = inventory.get(2);
        assertEquals("bronze-sword", item.getAttributes().getString("name"));
    }

    /** Try to pick up a chest from SE tile */
    @Test
    public void pickUpChest() {
        Event pickup = new PickupEvent();
        pickup.triggeredExecute(hero, '3');
        Tile t = plains.getTile(2, 2);
        assertEquals(1, t.getAttributes().getInt("has-item"));
        List<Item> inventory = hero.getInventory();
        assertEquals(2, inventory.size());
    }

    /** Try to open a chest from SE tile */
    @Test
    public void openChest() {
        Event open = new OpenEvent();
        open.triggeredExecute(hero, '3');
        Tile t = plains.getTile(2, 2);
        assertEquals(1, t.getAttributes().getInt("has-item"));
        Item i = t.getAttributes().getItem("item");
        assertEquals("health-potion", i.getAttributes().getString("name"));
    }

    /** Try to move through a portal */
    @Test
    public void portalMove() {
        Area a = (Area) hero.getAttributes().getObject("area");
        assertEquals("cold-plains", a.getName());
        move.triggeredExecute(hero, '4');
        a = (Area) hero.getAttributes().getObject("area");
        assertEquals("camp", a.getName());
        assertEquals(1, hero.getAttributes().getInt("row"));
        assertEquals(1, hero.getAttributes().getInt("col"));
    }

    /** As there is a trader on a same tile as the hero, it
      is legal to sell items. Sell my carried healthpotion
      and see hero's amount of gold grow by the selling value
      of it.
   */
    @Test
    public void tradeItem() {
        Event sell = new SellEvent();
        Actor trader = new ActorTestImpl("Trader", plains);
        Tile t = plains.getTile(1, 1);
        trader.getAttributes().putInt("row", 1);
        trader.getAttributes().putInt("col", 1);
        trader.getAttributes().putInt("is-trader", 1);
        t.getAttributes().putObject("trader", trader);
        assertEquals(50, hero.getAttributes().getInt("gold"));
        sell.triggeredExecute(hero, '1');
        assertEquals(50 + 20, hero.getAttributes().getInt("gold"));
        List<Item> inventory = hero.getInventory();
        assertEquals(inventory.size(), 1);
    }

    /** hero gains a level */
    @Test
    public void gainLevel() {
        assertEquals(3, hero.getAttributes().getInt("level"));
        AttributeListener expObserver = new AttributeListenerTestImpl() {

            public void intUpdate(String key, int value) {
                if (key.equals("experience")) {
                    if (value > 500) {
                        hero.getAttributes().putInt("level", 4);
                    }
                }
            }
        };
        hero.getAttributes().addAttributeListener(expObserver);
        hero.getAttributes().putInt("experience", 450);
        assertEquals(3, hero.getAttributes().getInt("level"));
        hero.getAttributes().putInt("experience", 550);
        assertEquals(4, hero.getAttributes().getInt("level"));
    }

    /** Use case: AttackFoe */
    @Test
    public void usecaseAttackFoe() {
        assertTrue(true);
    }
}
