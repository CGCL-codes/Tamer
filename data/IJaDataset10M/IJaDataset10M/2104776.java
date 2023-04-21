package wa.tosec;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class TosecFileParserTest {

    @Test
    public void testParseOk1() {
        String testFilename;
        TosecFile tosecFile;
        TosecFileParser parser = new TosecFileParser();
        try {
            testFilename = "Castle Hassle (1983)(Roklan).xex";
            tosecFile = parser.parse(testFilename);
            assertTrue("Extender unexpected!", tosecFile.getExtender().equals("xex"));
            assertTrue("Name unexpected!", tosecFile.getTitle().equals("Castle Hassle"));
            assertTrue("Year unexpected!", tosecFile.getYear().equals("1983"));
            assertTrue("Publisher unexpected!", tosecFile.getPublisher().equals("Roklan"));
            testFilename = "Xagon (1983)(Troll)[cr MACE][k-file].atz";
            tosecFile = parser.parse(testFilename);
            assertTrue("Extender unexpected!", tosecFile.getExtender().equals("atz"));
            assertTrue("Name unexpected!", tosecFile.getTitle().equals("Xagon"));
            assertTrue("Year unexpected!", tosecFile.getYear().equals("1983"));
            assertTrue("Publisher unexpected!", tosecFile.getPublisher().equals("Troll"));
            assertTrue("Cracker unexpected!", tosecFile.getDumpInformation(DumpInformationType.CRACKED).getInformation().equals("MACE"));
        } catch (TosecException e) {
            Assert.fail("TosecExcpetion!");
        }
    }

    @Test
    public void testParseAndGenerate1() {
        TosecFile tosecFile;
        TosecFileParser parser = new TosecFileParser();
        String generatedFilename;
        java.util.List<String> testData = new ArrayList<String>();
        testData.add("Pong (1992)(Mirage)(Pl)[t trainer](Disk 1 of 2)(disk label name)[OSb].atr");
        testData.add("Robbo (DeMO) (1989)(L.K. Avalon)(Pl)[t][k-file].atz");
        testData.add("Robbo (Demo) (1989)(L.K. Avalon)(Pl)[t][k-file].atz");
        testData.add("Robbo (1989)(L.K. Avalon)(Pl)[t2][k-file].atz");
        testData.add("Pong (1992)(Mirage)(Pl).atr");
        testData.add("Game (1985)(Lucasfilm)(Disk 1 of 2).atz");
        testData.add("Ballblazer (1985)(Lucasfilm)[a][Regulation Certified][k-file].atz");
        testData.add("Electronic Organ (19xx)(-)[OSb].atz");
        testData.add("221B Baker Street v1.0 (1987)(Datasoft)[cr The Bounty](Side A)(program).atr");
        testData.add("Adams Adventure #03 - Mission Impossible v3.9-306 (1981)(Adventure International).xex");
        testData.add("Boulder Dash Construction Kit (1986)(First Star Software)(Side A).atz");
        testData.add("Castle Hassle (1983)(Roklan).xex");
        testData.add("Caverns of Mars II (1981)(Greg Christensen)[compressed][k-file].atz");
        testData.add("Demon Attack (1982)(Imagic)[OSb].xex");
        testData.add("Flight Simulator II v1.05 (1984)(SubLogic)(Side A).atz");
        testData.add("Gateway to Apshai (1983)(Epyx)[a].xex");
        testData.add("Infantry Squad (1987)(Rassilon)[a](Side A)[Basic].atz");
        testData.add("Kayos (1981)(Computer Magic).xex");
        testData.add("Kingdom (19xx)(-)[bas2boot].atz");
        testData.add("Marauder (1982)(Sierra On-Line)[Stage 1][k-file].atz");
        testData.add("Moogles (1983)(Sirius)[k-file].atz");
        testData.add("One Life (1993)(P&S Software).atr");
        testData.add("Pitstop II (1984)(Epyx).xex");
        testData.add("Sea Dragon v1.0 (1982)(Adventure International).xex");
        testData.add("Snowball (1983)(Level 9 Computing)[a][c blub][b playable][k-file].atz");
        testData.add("Super Yahtsee (1988)(D. Smyth).atz");
        testData.add("Swiat Olkiego (1994)(Stan-Bit)(Pl)[t].xex");
        testData.add("Trix (1992)(L.K. Avalon)(Pl)[compressed][k-file].atz");
        testData.add("Warsaw Tetris, The (1989)(Lukszo-Konatkowski)[m Gizmo - Grendel].xex");
        testData.add("Xagon (1983)(Troll)[cr MACE][k-file].atz");
        testData.add("Zone Ranger (1984)(Activision).xex");
        try {
            for (String testFilename : testData) {
                tosecFile = parser.parse(testFilename);
                generatedFilename = tosecFile.generateTosecFilename();
                assertTrue("Must be equal: " + testFilename + " and " + generatedFilename, testFilename.equalsIgnoreCase(generatedFilename));
            }
        } catch (TosecException e) {
            Assert.fail("TosecExcpetion! with " + e);
        }
    }

    @Test
    public void testParseAndGenerate2() {
        TosecFile tosecFile;
        TosecFileParser parser = new TosecFileParser();
        String generatedFilename;
        java.util.List<String> testData = new ArrayList<String>();
        testData.add("Name (1984)(Comp)[a2][cr3][m test].xex");
        testData.add("Pong (1992)(Mirage)(Pl)[cr2](Side A)(program)[k-file].atr");
        testData.add("Pong (1992)(Mirage)(Pl)[t trainer].atr");
        testData.add("Pong (1992)(Mirage)(Pl)[t trainer](Disk 1 of 2)(disk label name)[OSb].atr");
        testData.add("Zone Ranger (1984)(Activision)[a].xex");
        testData.add("Name (1984)(Comp)[a2][cr3][m test].xex");
        try {
            for (String testFilename : testData) {
                tosecFile = parser.parse(testFilename);
                generatedFilename = tosecFile.generateTosecFilename();
                assertTrue("Must be equal: " + testFilename + " and " + generatedFilename, testFilename.equalsIgnoreCase(generatedFilename));
            }
        } catch (TosecException e) {
            Assert.fail("TosecExcpetion! with " + e);
        }
    }

    @Test
    public void testParseError() {
        List<String> testData = new ArrayList<String>();
        testData.add("Xagon )1983).atr");
        testData.add("Xagon )1983).atr");
        testData.add("()()()()())()(.atz");
        testData.add("(1982)(Cosmi)[noATR].xex");
        testData.add("Demon z");
        testData.add("Gorgon (1983)( [cr Yogi][k-file].atz");
        testData.add("Congo Bongo (198)[a2][k-file].atz");
        TosecFileParser parser = new TosecFileParser();
        for (String brokenTosecFilename : testData) {
            TosecException e = null;
            try {
                parser.parse(brokenTosecFilename);
            } catch (TosecException exp) {
                e = exp;
            }
            if (e == null) Assert.fail("ParseException epxected!:" + brokenTosecFilename);
        }
    }

    @Test
    public void testParsedDumpInformation() {
        TosecFile tFile = null;
        TosecFileParser parser = new TosecFileParser();
        List<String> testDataList = new ArrayList<String>();
        testDataList.add("Name (1999)(Company)[a].atz");
        testDataList.add("Name (1999)(Company)[a1].atz");
        testDataList.add("Name (1999)(Company)[a2].atz");
        for (String testData : testDataList) {
            tFile = parser.parse(testData);
            DumpInformation dumpInfo = tFile.getDumpInformation(DumpInformationType.ALTERNATE_VERSION);
            assertNotNull("Was null but expeceted Alternate DumpInfo", dumpInfo);
            assertTrue("Expected empty information", "".equals(dumpInfo.getInformation()));
        }
        testDataList.removeAll(testDataList);
        testDataList.add("Name (1999)(Company)[a3 test].atz");
        testDataList.add("Name (1999)(Company)[a2 test].atz");
        testDataList.add("Name (1999)(Company)[a test].atz");
        for (String testData : testDataList) {
            tFile = parser.parse(testData);
            DumpInformation dumpInfo = tFile.getDumpInformation(DumpInformationType.ALTERNATE_VERSION);
            assertNotNull("Got null but expected Alternate DumpInfo", dumpInfo);
            assertTrue(dumpInfo.getInformation().equalsIgnoreCase("test"));
        }
        testDataList.removeAll(testDataList);
        testDataList.add("Name (1999)(Company)[cr3 test test].atz");
        testDataList.add("Name (1999)(Company)[cr2 test test].atz");
        testDataList.add("Name (1999)(Company)[cr test test].atz");
        for (String testData : testDataList) {
            tFile = parser.parse(testData);
            DumpInformation dumpInfo = tFile.getDumpInformation(DumpInformationType.CRACKED);
            assertNotNull("Got null but expected Alternate DumpInfo", dumpInfo);
            assertTrue(dumpInfo.getInformation().equalsIgnoreCase("test test"));
        }
    }

    @Test
    public void testParsedMoreDumpInformation() {
        TosecFile tFile = null;
        TosecFileParser parser = new TosecFileParser();
        List<String> testDataList = new ArrayList<String>();
        testDataList.add("Name (1999)(Company)[k-file].atz");
        testDataList.add("Name (1999)(Company)[cr MEGA](Side A)(program)[k-file].atz");
        testDataList.add("Name (1999)(Company)[cr MEGA][t Specialist][k-file].xex");
        for (String testData : testDataList) {
            tFile = parser.parse(testData);
            assertTrue("Should contain [k-file]!", tFile.getMoreDumpInformationList().get(0).getInformation().equals("k-file"));
        }
        testDataList.removeAll(testDataList);
        testDataList.add("Name (1999)(Company)[k-file][blubber lub].atz");
        testDataList.add("Name (1999)(Company)[cr MEGA][k-file][blubber lub].atz");
        testDataList.add("Name (1999)(Company)[cr MEGA][t Specialist][k-file][blubber lub].xex");
        for (String testData : testDataList) {
            tFile = parser.parse(testData);
            assertTrue("Should contain [k-file]!", tFile.getMoreDumpInformationList().get(0).getInformation().equals("k-file"));
            assertTrue("Should contain [blubber lub]!", tFile.getMoreDumpInformationList().get(1).getInformation().equals("blubber lub"));
        }
    }
}
