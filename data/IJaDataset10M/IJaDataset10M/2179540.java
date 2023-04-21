package logic;

import util.*;
import static util.VectorUtil.*;

/**
 * Erzeugt zufällige Blöcke einer vorgegebnene Größe.
 */
public class RandomBlockFactory implements BlockFactory {

    private final int DIM = 3;

    private int[][] cubes;

    private int length = 5;

    private TypeIDSource source;

    /**
     * Erzeugt ein RandomBlockFactory-Objekt, das {@link TypeIDSource.dummy}
     * verwendet.
     * 
     */
    public RandomBlockFactory() {
        this(TypeIDSource.dummySource);
    }

    /**
     * Erzeugt ein RandomBlockFactory-Objekt.
     * 
     * @param source
     *            Die Quelle der Würfeltypen.
     */
    public RandomBlockFactory(TypeIDSource source) {
        this.source = source;
    }

    /**
     * Setzt die Länge der erzeugten Blöcke sofern {@code length} mindestens 1
     * ist.
     * 
     * @param length
     *            die angeforderte Länge
     */
    public void setLength(int length) {
        if (length > 0) {
            this.length = length;
        }
    }

    public int getLength() {
        return length;
    }

    /**
     * Erzeuge einen Block zufällig.
     * 
     * @return Block mit zufälligen Würfeln und zufälligem Würfeltyp
     */
    public Block createBlock() {
        cubes = new int[length][];
        cubes[0] = new int[DIM];
        for (int i = 1; i < length; i++) createCube(i);
        return new Block(cubes, GlobalRandom.getRandom().nextInt(source.getNumberOfTypes()) + 1);
    }

    private static final int[][] adjacentDeltas = new int[6][3];

    static {
        Direction[] directions = Direction.values();
        for (int i = 0; i < adjacentDeltas.length; i++) {
            adjacentDeltas[i] = directions[i].getVector();
        }
    }

    private void createCube(int index) {
        boolean newCubeAdded = false;
        DeckOfIntCards deck = new DeckOfIntCards(index);
        DeckOfIntCards adjacentDeck = new DeckOfIntCards(adjacentDeltas.length);
        int[] coordinates = new int[DIM];
        while (!newCubeAdded) {
            int[] cube = cubes[deck.nextInt()];
            boolean coordinatesFound = false;
            while (!adjacentDeck.isEmpty() && !coordinatesFound) {
                int[] adjacentDelta = adjacentDeltas[adjacentDeck.nextInt()];
                assign(coordinates, cube);
                add(coordinates, adjacentDelta);
                boolean coordinatesShouldBeAdded = true;
                for (int i = 0; i < index; i++) {
                    boolean allCoordinatesMatch = true;
                    for (int j = 0; j < DIM; j++) {
                        if (coordinates[j] != cubes[i][j]) {
                            allCoordinatesMatch = false;
                            break;
                        }
                    }
                    if (allCoordinatesMatch) {
                        coordinatesShouldBeAdded = false;
                        break;
                    }
                }
                if (coordinatesShouldBeAdded) {
                    coordinatesFound = true;
                }
            }
            if (coordinatesFound) {
                cubes[index] = coordinates;
                newCubeAdded = true;
            } else {
                adjacentDeck.reset();
            }
        }
    }
}
