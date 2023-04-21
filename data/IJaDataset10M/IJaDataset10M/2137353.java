package misc;

import containers.*;
import containers.events.*;
import containers.unitfeatures.*;
import java.util.Date;

public class DefaultObjects {

    private static int counter = 0;

    public static World getNextWorld() {
        World world = new World();
        return world;
    }

    public static User getNextUser() {
        int count = getNextCounter();
        User user = new User();
        user.setBanned(false);
        user.setEmail("lucek" + count + "@pnet.pl");
        user.setLogin("lucek" + count);
        user.setName("maciej");
        user.setPassword("haslo");
        Island island = getNextIsland(count);
        user.getIslands().add(island);
        island.setIsMotherIsland(true);
        island.setOwner(user);
        return (user);
    }

    public static Island getNextIsland() {
        return (getNextIsland(getNextCounter()));
    }

    private static Island getNextIsland(int count) {
        Island island = new Island();
        island.setArea(70);
        island.setDescription("Domyslna Wyspa " + count);
        island.setName("Wyspa " + count);
        return (island);
    }

    static int currentMsg = 0;

    public static Message getNextMessage() {
        Message msg = new Message();
        msg.setBody("Body of Message " + currentMsg);
        msg.setDate(new Date());
        msg.setSubject("Subject of Message " + currentMsg);
        currentMsg++;
        return (msg);
    }

    private static int getNextCounter() {
        return (counter++);
    }

    private static MobileFleet getNextMobileFleet(int count) {
        MobileFleet fleet = new MobileFleet();
        fleet.setDescription("Fleet " + count);
        fleet.setName("Fleet Name " + count);
        return (fleet);
    }

    public static MobileFleet getNextMoibleFleet() {
        return getNextMobileFleet(getNextCounter());
    }

    public static World getComplicatedWorld() {
        int USER_COUNT = 10;
        int MSGS_COUNT = 100;
        int UNIT_COUNT_ATTACKER = 50;
        int UNIT_MOVABLE_MOD = 3;
        int UNIT_COUNT_RESOURCE_TRANSPORTER = 10;
        int UNIT_COUNT_RESOURCE_CONSUMER = 5;
        int UNIT_COUNT_WAREHOUSE = 1;
        int ISLANDS_COUNT = 2;
        int FLEET_COUNT = 3;
        World world = DefaultObjects.getNextWorld();
        for (int i = 0; i < USER_COUNT; i++) {
            User user = DefaultObjects.getNextUser();
            world.addUser(user);
            for (int j = 0; j < MSGS_COUNT; j++) {
                Message message = DefaultObjects.getNextMessage();
                message.setUser(user);
                user.addMessage(message);
            }
            for (int k = 0; k < ISLANDS_COUNT; k++) {
                Island island = DefaultObjects.getNextIsland();
                user.addIsland(island);
                for (int l = 0; l < FLEET_COUNT; l++) {
                    boolean movable = true;
                    Fleet fleet = DefaultObjects.getNextMoibleFleet();
                    fleet.setHomeIsland(island);
                    for (int j = 0; j < UNIT_COUNT_ATTACKER; j++) {
                        Unit unit = new Unit();
                        Attacker attacker = new Attacker();
                        attacker.setUnit(unit);
                        attacker.setFirePower(j * 10);
                        addMoveableFeature(unit, j, movable);
                        unit.addFeature(attacker);
                        fleet.addUnit(unit);
                    }
                    for (int j = 0; j < UNIT_COUNT_RESOURCE_TRANSPORTER; j++) {
                        Unit unit = new Unit();
                        ResourcesTransporter f = new ResourcesTransporter();
                        f.setUnit(unit);
                        f.setCapacity(new Resources(500 * j, 100 * j, 33 * j));
                        addMoveableFeature(unit, j, movable);
                        unit.addFeature(f);
                        fleet.addUnit(unit);
                    }
                    for (int j = 0; j < UNIT_COUNT_RESOURCE_CONSUMER; j++) {
                        Unit unit = new Unit();
                        ResourcesConsumer f = new ResourcesConsumer();
                        f.setUnit(unit);
                        f.setConsumption(new Resources(4 * j, 5 * j, 1 * j));
                        addMoveableFeature(unit, j, movable);
                        unit.addFeature(f);
                        fleet.addUnit(unit);
                    }
                    for (int j = 0; j < UNIT_COUNT_WAREHOUSE; j++) {
                        Unit unit = new Unit();
                        Warehouse f = new Warehouse();
                        f.setUnit(unit);
                        f.setCapacity(new Resources(4000 * j, 500 * j, 70 * j));
                        f.setStoredResources(new Resources(3000 * j, 400 * j, 35 * j));
                        unit.addFeature(f);
                        fleet.addUnit(unit);
                    }
                }
            }
        }
        return (world);
    }

    public static void addMoveableFeature(Unit unit, int loopCount, boolean reallyAdd) {
        if (reallyAdd) {
            addMoveableFeature(unit, 1, 1);
        }
    }

    public static void addMoveableFeature(Unit unit, int loopCount, int UNIT_MOVABLE_MOD) {
        if (loopCount % UNIT_MOVABLE_MOD == 0) {
            Movable mov = new Movable();
            mov.setFuelConsumption(loopCount * 3);
            mov.setSpeed(loopCount * 4);
            mov.setUnit(unit);
            unit.addFeature(mov);
        }
    }
}
