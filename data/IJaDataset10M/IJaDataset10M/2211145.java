package com.atticlabs.zonelayout.swing;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.util.*;

public class RandomZoneLayoutTest extends TestCase {

    protected Random random = new Random();

    public static final int ITERATIONS = 10000;

    public RandomZoneLayoutTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(RandomZoneLayoutTest.class);
    }

    public void testRandomSectionTakes() {
        for (int i = 0; i < ITERATIONS; i++) {
            String row1 = "abcdefghi";
            String row2 = "j.......j";
            ZoneLayoutImpl layout = new ZoneLayoutImpl();
            layout.addRow(row1);
            layout.addRow(row2);
            int jTake = random.nextInt(101);
            layout.getZone("j").setTake(jTake, 0);
            int totalTakes = 0;
            int[] oldTakes = new int[10];
            for (int j = 0; j < row1.length(); j++) {
                int take = random.nextInt(101);
                layout.getZone(row1.substring(j, j + 1)).setTake(take, 0);
                totalTakes += take;
                oldTakes[j] = take;
            }
            layout.compile();
            layout.createSections();
            if (jTake > totalTakes) {
                int newTotalTakes = 0;
                for (Iterator rowIter = layout.columns.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalTakes += section.getTake();
                }
                assertEquals(jTake, newTotalTakes);
                for (int j = 0; j < layout.columns.size(); j++) {
                    Section section = (Section) layout.columns.get(j);
                    double dOldTake = (double) oldTakes[j];
                    double dTotalTakes = (double) totalTakes;
                    double dNewTake = (double) section.getTake();
                    double dNewTotalTakes = (double) newTotalTakes;
                    assertTrue(Math.abs((dOldTake / dTotalTakes) - (dNewTake / dNewTotalTakes)) < .001);
                }
            } else {
                int newTotalTakes = 0;
                for (Iterator rowIter = layout.columns.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalTakes += section.getTake();
                }
                assertEquals(totalTakes, newTotalTakes);
            }
        }
        for (int i = 0; i < ITERATIONS; i++) {
            char[] col1 = "abcdefghi".toCharArray();
            ZoneLayoutImpl layout = new ZoneLayoutImpl();
            layout.addRow("aj");
            layout.addRow("b.");
            layout.addRow("c.");
            layout.addRow("d.");
            layout.addRow("e.");
            layout.addRow("f.");
            layout.addRow("g.");
            layout.addRow("h.");
            layout.addRow("ij");
            int jTake = random.nextInt(101);
            layout.getZone("j").setTake(0, jTake);
            int totalTakes = 0;
            int[] oldTakes = new int[10];
            for (int j = 0; j < col1.length; j++) {
                int take = random.nextInt(101);
                layout.getZone(Character.toString(col1[j])).setTake(0, take);
                totalTakes += take;
                oldTakes[j] = take;
            }
            layout.compile();
            layout.createSections();
            if (jTake > totalTakes) {
                int newTotalTakes = 0;
                for (Iterator rowIter = layout.rows.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalTakes += section.getTake();
                }
                assertEquals(jTake, newTotalTakes);
                for (int j = 0; j < layout.rows.size(); j++) {
                    Section section = (Section) layout.rows.get(j);
                    double dOldTake = (double) oldTakes[j];
                    double dTotalTakes = (double) totalTakes;
                    double dNewTake = (double) section.getTake();
                    double dNewTotalTakes = (double) newTotalTakes;
                    assertTrue(Math.abs((dOldTake / dTotalTakes) - (dNewTake / dNewTotalTakes)) < .001);
                }
            } else {
                int newTotalTakes = 0;
                for (Iterator rowIter = layout.rows.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalTakes += section.getTake();
                }
                assertEquals(totalTakes, newTotalTakes);
            }
        }
    }

    public void testRandomSectionGives() {
        for (int i = 0; i < ITERATIONS; i++) {
            String row1 = "abcdefghi";
            String row2 = "j.......j";
            ZoneLayoutImpl layout = new ZoneLayoutImpl();
            layout.addRow(row1);
            layout.addRow(row2);
            int jGive = random.nextInt(51) * random.nextInt(2);
            layout.getZone("j").setGive(jGive, 0);
            int totalGives = 0;
            int[] oldGives = new int[10];
            for (int j = 0; j < row1.length(); j++) {
                int give = random.nextInt(51) * random.nextInt(2);
                layout.getZone(row1.substring(j, j + 1)).setGive(give, 0);
                totalGives += give;
                oldGives[j] = give;
            }
            layout.compile();
            layout.createSections();
            if (jGive < totalGives) {
                int newTotalGives = 0;
                for (Iterator rowIter = layout.columns.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalGives += section.getGive();
                }
                assertEquals(jGive, newTotalGives);
                if (jGive != 0) {
                    for (int j = 0; j < layout.columns.size(); j++) {
                        Section section = (Section) layout.columns.get(j);
                        double dOldGive = (double) oldGives[j];
                        double dTotalGives = (double) totalGives;
                        double dNewGive = (double) section.getGive();
                        double dNewTotalGives = (double) newTotalGives;
                        assertTrue(Math.abs((dOldGive / dTotalGives) - (dNewGive / dNewTotalGives)) < .02);
                    }
                }
            } else {
                int newTotalGives = 0;
                for (Iterator rowIter = layout.columns.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalGives += section.getGive();
                }
                assertEquals(totalGives, newTotalGives);
            }
        }
        for (int i = 0; i < ITERATIONS; i++) {
            char[] col1 = "abcdefghi".toCharArray();
            ZoneLayoutImpl layout = new ZoneLayoutImpl();
            layout.addRow("aj");
            layout.addRow("b.");
            layout.addRow("c.");
            layout.addRow("d.");
            layout.addRow("e.");
            layout.addRow("f.");
            layout.addRow("g.");
            layout.addRow("h.");
            layout.addRow("ij");
            int jGive = random.nextInt(51) * random.nextInt(2);
            layout.getZone("j").setGive(0, jGive);
            int totalGives = 0;
            int[] oldGives = new int[10];
            for (int j = 0; j < col1.length; j++) {
                int give = random.nextInt(51) * random.nextInt(2);
                layout.getZone(Character.toString(col1[j])).setGive(0, give);
                totalGives += give;
                oldGives[j] = give;
            }
            layout.compile();
            layout.createSections();
            if (jGive < totalGives) {
                int newTotalGives = 0;
                for (Iterator rowIter = layout.rows.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalGives += section.getGive();
                }
                assertEquals(jGive, newTotalGives);
                if (jGive != 0) {
                    for (int j = 0; j < layout.rows.size(); j++) {
                        Section section = (Section) layout.rows.get(j);
                        double dOldGive = (double) oldGives[j];
                        double dTotalGives = (double) totalGives;
                        double dNewGive = (double) section.getGive();
                        double dNewTotalGives = (double) newTotalGives;
                        assertTrue(Math.abs((dOldGive / dTotalGives) - (dNewGive / dNewTotalGives)) < .02);
                    }
                }
            } else {
                int newTotalGives = 0;
                for (Iterator rowIter = layout.rows.iterator(); rowIter.hasNext(); ) {
                    Section section = (Section) rowIter.next();
                    newTotalGives += section.getGive();
                }
                assertEquals(totalGives, newTotalGives);
            }
        }
    }

    public void testRandomAllocateAdditionalSpace() {
        for (int j = 0; j < ITERATIONS; j++) {
            ZoneLayoutImpl layout = new ZoneLayoutImpl();
            int sectionCount = random.nextInt(20) + 1;
            int[] originalSizes = new int[sectionCount];
            int space = random.nextInt(901) + 100;
            List sections = new ArrayList(sectionCount);
            boolean zeroTake = false;
            int totalTake = 0;
            zeroTake = random.nextInt(50) == 0;
            for (int i = 0; i < sectionCount; i++) {
                Row row = new Row(layout, i);
                sections.add(row);
                originalSizes[i] = random.nextInt(101);
                row.setSize(originalSizes[i]);
                if (zeroTake) {
                    row.setTake(0);
                } else {
                    int take = random.nextInt(10001);
                    row.setTake(take);
                    totalTake += take;
                }
            }
            layout.allocateAdditionalSpace(sections, space, new DisplaySizeView(), true);
            assertEquals(sum(originalSizes) + space, totalSize(sections));
            if (zeroTake) {
                int share = space / sectionCount;
                int remainder = space % sectionCount;
                for (int i = 0; i < sections.size(); i++) {
                    Section section = (Section) sections.get(i);
                    assertEquals(share + (remainder - i > 0 ? 1 : 0), section.getSize() - originalSizes[i]);
                }
            } else {
                for (int i = 0; i < sections.size(); i++) {
                    Section section = (Section) sections.get(i);
                    int addedSpace = section.getSize() - originalSizes[i];
                    assertEquals(((double) addedSpace / (double) space), ((double) section.getTake() / (double) totalTake), 0.01);
                }
            }
        }
    }

    private int sum(int[] values) {
        int total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }
        return total;
    }

    private int totalSize(List sections) {
        int total = 0;
        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            total += section.getSize();
        }
        return total;
    }

    public void testRandomRevokeAdditionalSpace() {
        for (int j = 0; j < ITERATIONS; j++) {
            ZoneLayoutImpl layout = new ZoneLayoutImpl();
            int sectionCount = random.nextInt(20) + 1;
            int[] originalSizes = new int[sectionCount];
            int[] minSizes = new int[sectionCount];
            int space = random.nextInt(401) + 100;
            List sections = new ArrayList(sectionCount);
            int totalGive = 0;
            int totalRevokableSpace = 0;
            int totalGivableSpace = 0;
            for (int i = 0; i < sectionCount; i++) {
                Row row = new Row(layout, i);
                sections.add(row);
                originalSizes[i] = random.nextInt(101) + 50;
                minSizes[i] = random.nextInt(51);
                row.setSize(originalSizes[i]);
                row.setMinimumSize(minSizes[i]);
                totalRevokableSpace += originalSizes[i] - minSizes[i];
                int give = random.nextInt(1001);
                if (random.nextInt(10) == 0) {
                    give = 0;
                }
                if (give > 0) {
                    totalGivableSpace += originalSizes[i] - minSizes[i];
                }
                row.setGive(give);
                totalGive += give;
            }
            layout.revokeAdditionalSpace(sections, space, new DisplaySizeView(), new MinimumSizeView());
            for (int i = 0; i < sections.size(); i++) {
                Section section = (Section) sections.get(i);
            }
            if (totalGivableSpace > space) {
                assertEquals(sum(originalSizes) - space, totalSize(sections));
                for (int i = 0; i < sections.size(); i++) {
                    Section section = (Section) sections.get(i);
                    if (section.getGive() == 0) {
                        assertEquals(section.getSize(), originalSizes[i]);
                    }
                    if (section.getGive() * space / totalGive >= originalSizes[i] - minSizes[i]) {
                        assertEquals(section.getSize(), section.getMinimumSize());
                    } else {
                        int maxSize = (int) (originalSizes[i] - (section.getGive() * space / totalGive));
                        assertTrue(section.getSize() <= maxSize);
                    }
                }
            } else {
                assertEquals(sum(originalSizes) - totalGivableSpace, totalSize(sections));
                for (int i = 0; i < sections.size(); i++) {
                    Section section = (Section) sections.get(i);
                    if (section.getGive() > 0) {
                        assertEquals(minSizes[i], section.getSize());
                    } else {
                        assertEquals(originalSizes[i], section.getSize());
                    }
                }
            }
        }
    }
}
