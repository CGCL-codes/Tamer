package edu.mit.csail.pag.amock.hooks;

import edu.mit.csail.pag.amock.representation.*;
import edu.mit.csail.pag.amock.trace.*;
import edu.mit.csail.pag.amock.util.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RecordPrimaryClassInfo implements Serializable {

    public final ClassName className;

    public final Map<TraceField, Integer> fieldSlots = new HashMap<TraceField, Integer>();

    public final Map<TraceMethod, Integer> methodSlots = new HashMap<TraceMethod, Integer>();

    public final List<ProgramObject> slotDefaults = new ArrayList<ProgramObject>();

    public final Set<TraceMethod> benignMethods = new HashSet<TraceMethod>();

    private static final String RPCI_DATA_DUMP_FILE = "src/java/edu/mit/csail/pag/amock/hooks/record-primary-data.xml";

    public RecordPrimaryClassInfo(ClassName className) {
        this.className = className;
    }

    public boolean methodIsBenign(TraceMethod m) {
        return m.isConstructor() || benignMethods.contains(m) || methodSlots.containsKey(m);
    }

    private static Map<ClassName, RecordPrimaryClassInfo> cachedClassInfo;

    private static void initializeCache() {
        if (cachedClassInfo == null) {
            cachedClassInfo = new HashMap<ClassName, RecordPrimaryClassInfo>();
            InputStream is;
            try {
                is = new FileInputStream(RPCI_DATA_DUMP_FILE);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            }
            Deserializer<RecordPrimaryClassInfo> d = new XMLDeserializer<RecordPrimaryClassInfo>(is, RecordPrimaryClassInfo.class);
            d.process(new TraceProcessor<RecordPrimaryClassInfo>() {

                public void processEvent(RecordPrimaryClassInfo rpci) {
                    addEntryToCache(rpci);
                }
            });
        }
    }

    private static void addEntryToCache(RecordPrimaryClassInfo rpci) {
        cachedClassInfo.put(rpci.className, rpci);
    }

    public static boolean isRecordPrimaryClass(ClassName name) {
        initializeCache();
        return cachedClassInfo.containsKey(name);
    }

    public static RecordPrimaryClassInfo getClassInfo(ClassName name) {
        initializeCache();
        return cachedClassInfo.get(name);
    }

    private static void saveData() throws FileNotFoundException {
        initializeCache();
        OutputStream os = new FileOutputStream(RPCI_DATA_DUMP_FILE);
        Serializer<RecordPrimaryClassInfo> s = Serializer.getSerializer(os);
        for (RecordPrimaryClassInfo rpci : cachedClassInfo.values()) {
            s.write(rpci);
        }
        s.close();
    }

    /**
     * Returns a RecordPrimaryClassInfo object for the given class,
     * with all fields and methods listed; this is intended as a
     * skeleton which can be edited to contain the correct data.
     */
    private static RecordPrimaryClassInfo createSampleRPCI(ClassName className) throws ClassNotFoundException {
        RecordPrimaryClassInfo rpci = new RecordPrimaryClassInfo(className);
        Class<?> c = Class.forName(className.asClassForNameArgument());
        rpci.reflectivelyFillFields(c);
        rpci.reflectivelyFillMethods(c);
        rpci.reflectivelyFillDefaults(c);
        return rpci;
    }

    private void reflectivelyFillFields(Class<?> c) {
        for (Field f : c.getFields()) {
            if ((f.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            if ((f.getModifiers() & Modifier.PUBLIC) == 0) {
                continue;
            }
            TraceField tf = TraceField.createFromField(f);
            fieldSlots.put(tf, 0);
        }
    }

    private void reflectivelyFillMethods(Class<?> c) {
        for (Method m : c.getMethods()) {
            if ((m.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            if ((m.getModifiers() & Modifier.PUBLIC) == 0) {
                continue;
            }
            TraceMethod tm = TraceMethod.createFromMethod(m);
            benignMethods.add(tm);
        }
    }

    private void reflectivelyFillDefaults(Class<?> c) {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("usage: java RecordPrimaryClassInfo pack/age/classname");
        }
        initializeCache();
        RecordPrimaryClassInfo rpci = createSampleRPCI(ClassName.fromSlashed(args[0]));
        addEntryToCache(rpci);
        saveData();
    }
}
