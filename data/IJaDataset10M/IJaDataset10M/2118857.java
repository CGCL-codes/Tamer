package org.minions.stigma.game.map.test;

import org.minions.stigma.game.map.Map;
import org.minions.stigma.game.map.MapType;
import org.minions.stigma.game.map.Tileset;
import org.minions.utils.collections.CollectionFactory;
import org.minions.utils.collections.CollectionFactory.CollectionType;

public class MapTest {

    private final int loopCount = 1000;

    private final int[] mapSizes = new int[] { 25, 50, 100, 200, 500, 1000, 5000, 10000 };

    private final short tileCount = 100;

    public void runTest() {
        Tileset t = RandomTilesetGenerator.generateTileset((short) 1, tileCount);
        for (int size : mapSizes) {
            long start = System.currentTimeMillis();
            MapType m = RandomMapTypeGenerator.generateMapType(t, (short) 0, size, size);
            System.out.println("\nMapType for size " + size + "x" + size + " generated in " + (System.currentTimeMillis() - start) + " ms");
            long average = 0;
            for (int i = 0; i < loopCount; ++i) {
                start = System.currentTimeMillis();
                new Map((short) 0, m, t);
                average += (System.currentTimeMillis() - start);
                if (i % 100 == 0) System.out.println((i / 10 + 10) + "%");
            }
            average /= loopCount;
            System.out.println("Map for size " + size + "x" + size + " average generation time: " + average + " ms (" + (average / 1000.0) + " s)");
        }
    }

    public static void main(String[] args) {
        CollectionFactory.init(CollectionType.Normal);
        MapTest t = new MapTest();
        t.runTest();
    }
}
