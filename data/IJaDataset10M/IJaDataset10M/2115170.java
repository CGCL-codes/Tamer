package part_III;

import java.math.BigDecimal;
import temperature.Temperature;
import temperature.TemperatureUnit;
import environment.Direction;
import environment.Position;
import environment.dungeons.CompositeDungeon;
import environment.dungeons.Dungeon;
import environment.dungeons.Level;
import environment.dungeons.NormalSquare;
import environment.dungeons.Rock;
import environment.dungeons.Shaft;
import environment.dungeons.Square;
import environment.dungeons.SquareConstraint;
import environment.dungeons.TeleportableSquare;
import environment.dungeons.TransparentSquare;

/**
 * Deze klasse houdt het uit te voeren hoofdprogramma bij. 
 * Dit dient ter demonstratie van het project Project3-Inheritance.
 * 
 * @author Nathan Bekaert & Philippe de Potter de ten Broeck
 * @version 1.0
 */
public class Part_III {

    public static void main(String[] args) {
        Dungeon<Square> root = new CompositeDungeon<Square>(20, 20, 20);
        Dungeon<Square> subDungeon1 = new Level<Square>(20, 20);
        Dungeon<TeleportableSquare> subDungeon2 = new Shaft<TeleportableSquare>(10, Direction.CEILING);
        ((CompositeDungeon<Square>) root).addSubDungeonAt(subDungeon1, 0, 0, 0);
        ((CompositeDungeon<Square>) root).addSubDungeonAt(subDungeon2, 5, 5, 1);
        subDungeon2.addSquareAt(new NormalSquare(), new Position(0, 0, 0));
        root.addSquareAt(new TransparentSquare(Direction.EAST), new Position(5, 5, 2));
        TeleportableSquare teleportingSquare = new TransparentSquare(new Temperature(BigDecimal.TEN, TemperatureUnit.CELSIUS), BigDecimal.TEN, Direction.CEILING, false);
        teleportingSquare.setTeleportable(true);
        root.addSquareAt(teleportingSquare, new Position(5, 5, 3));
        root.addSquareAt(new NormalSquare(), new Position(5, 5, 0));
        root.addSquareAt(new Rock(), new Position(6, 5, 0));
        root.addSquareAt(new NormalSquare(), new Position(5, 4, 0));
        root.addSquareAt(new TransparentSquare(Direction.SOUTH), new Position(4, 5, 0));
        Square destinationSquare = new NormalSquare();
        root.addSquareAt(destinationSquare, new Position(5, 3, 0));
        teleportingSquare.addAsTeleportationSquare((TeleportableSquare) destinationSquare);
        root.addSquareAt(new NormalSquare(), new Position(4, 4, 0));
        root.addSquareAt(new TransparentSquare(Direction.EAST), new Position(6, 3, 0));
        root.addSquareAt(new NormalSquare(), new Position(6, 4, 0));
        root.addSquareAt(new NormalSquare(), new Position(4, 3, 0));
        TeleportableSquare tele1 = new TransparentSquare(Direction.EAST);
        tele1.setTeleportable(true);
        root.addSquareAt(tele1, new Position(2, 5, 0));
        NormalSquare tele2 = new NormalSquare();
        tele2.setTeleportable(true);
        root.addSquareAt(tele2, new Position(3, 2, 0));
        System.out.println(" A random square in the level is accessible from the teleportable square in the shaft:");
        boolean canAccessWholeRoom = true;
        for (Square squareInRoom : destinationSquare.getRoom()) if (!teleportingSquare.canAccess(squareInRoom)) canAccessWholeRoom = false;
        System.out.println("	" + canAccessWholeRoom);
        System.out.println();
        root.addSquareAt(new Rock(), new Position(2, 3, 0));
        root.addSquareAt(new NormalSquare(new Temperature(new BigDecimal(2000), TemperatureUnit.CELSIUS), BigDecimal.TEN), new Position(3, 4, 0));
        root.addSquareAt(new NormalSquare(new Temperature(BigDecimal.TEN, TemperatureUnit.CELSIUS), new BigDecimal(100)), new Position(7, 4, 0));
        System.out.println("All squares in the dungeon, satisfying: \n	" + isRock().toString());
        System.out.println(root.getAllSquaresSatisfying(isRock()));
        System.out.println("All squares in the dungeon, satisfying: \n	" + isRock().toString() + " \n	" + hasTemperatureOfAtLeast(200).toString());
        System.out.println(root.getAllSquaresSatisfying(isRock(), hasTemperatureOfAtLeast(200)));
        System.out.println("All squares in the dungeon, satisfying: \n	" + isTeleportable().toString());
        System.out.println(root.getAllSquaresSatisfying(isTeleportable()));
    }

    public static SquareConstraint<Square> isRock() {
        return new SquareConstraint<Square>() {

            @Override
            public boolean checkElement(Square square) {
                if (!(square instanceof Rock)) return false;
                return true;
            }

            @Override
            public String toString() {
                return "is Rock";
            }
        };
    }

    public static SquareConstraint<Square> isTeleportable() {
        return new SquareConstraint<Square>() {

            @Override
            public boolean checkElement(Square square) {
                if (!(square instanceof TeleportableSquare)) return false; else if (!((TeleportableSquare) square).isTeleportable()) return false;
                return true;
            }

            @Override
            public String toString() {
                return "is Teleportable";
            }
        };
    }

    public static SquareConstraint<Square> hasTemperatureOfAtLeast(final long valueInC) {
        return new SquareConstraint<Square>() {

            @Override
            public boolean checkElement(Square square) {
                if ((square.getTemperature().getTemperatureValueIn(TemperatureUnit.CELSIUS)).compareTo(new BigDecimal(valueInC)) == -1) return false;
                return true;
            }

            @Override
            public String toString() {
                return "has temperature of at least " + valueInC + "�C";
            }
        };
    }

    public static SquareConstraint<Square> hasTemperatureBelow(final long valueInC) {
        return new SquareConstraint<Square>() {

            @Override
            public boolean checkElement(Square square) {
                if ((square.getTemperature().getTemperatureValueIn(TemperatureUnit.CELSIUS)).compareTo(new BigDecimal(valueInC)) != -1) return false;
                return true;
            }

            @Override
            public String toString() {
                return "has temperature below " + valueInC + "�C";
            }
        };
    }

    public static SquareConstraint<Square> hasHumidityOfAtLeast(final long percentage) {
        return new SquareConstraint<Square>() {

            @Override
            public boolean checkElement(Square square) {
                if (square.getHumidity().compareTo(new BigDecimal(percentage)) == -1) return false;
                return true;
            }

            @Override
            public String toString() {
                return "has humidity of at least " + percentage + "%";
            }
        };
    }
}
