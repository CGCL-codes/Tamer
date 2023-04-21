package megamek.common.verifier;

import gd.xml.ParseException;
import gd.xml.tiny.ParsedXML;
import gd.xml.tiny.TinyParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import megamek.common.Entity;
import megamek.common.GunEmplacement;
import megamek.common.Mech;
import megamek.common.MechFileParser;
import megamek.common.MechSummary;
import megamek.common.MechSummaryCache;
import megamek.common.Tank;

public class EntityVerifier implements MechSummaryCache.Listener {

    public static final String CONFIG_FILENAME = "data/mechfiles/UnitVerifierOptions.xml";

    public static final String BASE_NODE = "entityverifier";

    public static final String BASE_MECH_NODE = "mech";

    public static final String BASE_TANK_NODE = "tank";

    private static MechSummaryCache mechSummaryCache = null;

    public TestXMLOption mechOption = new TestXMLOption();

    public TestXMLOption tankOption = new TestXMLOption();

    public EntityVerifier(File config) {
        ParsedXML root = null;
        try {
            root = TinyParser.parseXML(new FileInputStream(config));
            for (Enumeration<?> e = root.elements(); e.hasMoreElements(); ) {
                ParsedXML child = (ParsedXML) e.nextElement();
                if (child.getTypeName().equals("tag") && child.getName().equals(BASE_NODE)) {
                    readOptions(child);
                }
            }
        } catch (ParseException e) {
            System.err.println("EntityVerifier: Failure parsing config file:");
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.println("EntityVerifier: Configfile not found:");
            System.err.println(e.getMessage());
        }
    }

    public boolean checkEntity(Entity entity, String fileString, boolean verbose) {
        return checkEntity(entity, fileString, verbose, false);
    }

    public boolean checkEntity(Entity entity, String fileString, boolean verbose, boolean ignoreAmmo) {
        boolean retVal = false;
        TestEntity testEntity = null;
        if (entity instanceof Mech) {
            testEntity = new TestMech((Mech) entity, mechOption, fileString);
        } else if ((entity instanceof Tank) && !(entity instanceof GunEmplacement)) {
            testEntity = new TestTank((Tank) entity, tankOption, fileString);
        } else {
            System.err.println("UnknownType: " + entity.getDisplayName());
            System.err.println("Found in: " + fileString);
            return false;
        }
        if (verbose) {
            System.out.print(testEntity.printEntity());
            StringBuffer buff = new StringBuffer();
            System.out.println("BV: " + entity.calculateBattleValue() + "    Cost: " + entity.getCost(false));
            if (testEntity.correctEntity(buff, ignoreAmmo)) {
                System.out.println("---Entity is valid---");
            } else {
                System.out.println("---Entity INVALID---");
            }
        } else {
            StringBuffer buff = new StringBuffer();
            if (testEntity.correctEntity(buff, ignoreAmmo)) {
                retVal = true;
            } else {
                System.out.println(testEntity.getName());
                System.out.println("Found in: " + testEntity.fileString);
                System.out.println(buff);
            }
        }
        return retVal;
    }

    public Entity loadEntity(File f, String entityName) {
        Entity entity = null;
        try {
            entity = new MechFileParser(f, entityName).getEntity();
        } catch (megamek.common.loaders.EntityLoadingException e) {
            System.out.println("Exception: " + e.toString());
        }
        return entity;
    }

    public void doneLoading() {
        MechSummary[] ms = mechSummaryCache.getAllMechs();
        System.out.println("\n");
        System.out.println("Mech Options:");
        System.out.println(mechOption.printOptions());
        System.out.println("\nTank Options:");
        System.out.println(tankOption.printOptions());
        int failures = 0;
        for (int i = 0; i < ms.length; i++) {
            if (ms[i].getUnitType().equals("Mek") || ms[i].getUnitType().equals("Tank")) {
                Entity entity = loadEntity(ms[i].getSourceFile(), ms[i].getEntryName());
                if (entity == null) {
                    continue;
                }
                if (!checkEntity(entity, ms[i].getSourceFile().toString(), false)) {
                    failures++;
                }
            }
        }
        System.out.println("Total Failures: " + failures);
    }

    private void readOptions(ParsedXML node) {
        for (Enumeration<?> e = node.elements(); e.hasMoreElements(); ) {
            ParsedXML child = (ParsedXML) e.nextElement();
            if (child.getName().equals(BASE_TANK_NODE)) {
                tankOption.readXMLOptions(child);
            } else if (child.getName().equals(BASE_MECH_NODE)) {
                mechOption.readXMLOptions(child);
            }
        }
    }

    public static void main(String[] args) {
        File config = new File(CONFIG_FILENAME);
        File f = null;
        String entityName = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-file")) {
                if (args.length <= i) {
                    System.out.println("Missing argument filename!");
                    return;
                }
                i++;
                f = new File(args[i]);
                if (!f.exists()) {
                    System.out.println("Can't find: " + args[i] + "!");
                    return;
                }
                if (args[i].endsWith(".zip")) {
                    if (args.length <= i + 1) {
                        System.out.println("Missing Entity Name!");
                        return;
                    }
                    i++;
                    entityName = args[i];
                }
            } else {
                System.err.println("Error: Invalid argument.\n");
                System.err.println("Usage:\n\tEntityVerifier [-file <FILENAME>]");
                return;
            }
        }
        if (f != null) {
            Entity entity = null;
            try {
                entity = new MechFileParser(f, entityName).getEntity();
            } catch (megamek.common.loaders.EntityLoadingException e) {
                System.err.println("Exception: " + e.toString());
                System.err.println("Exception: " + e.getMessage());
                return;
            }
            new EntityVerifier(config).checkEntity(entity, f.toString(), true);
        } else {
            EntityVerifier ev = new EntityVerifier(config);
            mechSummaryCache = MechSummaryCache.getInstance();
            mechSummaryCache.addListener(ev);
        }
    }
}
