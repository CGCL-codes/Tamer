package pl.clareo.coroutines.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

final class CoroutineInstrumentator implements ClassFileTransformer {

    public static ClassVisitor createTracer(String classInternalName, ClassVisitor cv) throws FileNotFoundException {
        File outputFile = new File(printPath, classInternalName + ".trace");
        outputFile.getParentFile().mkdirs();
        PrintWriter writer = new PrintWriter(outputFile);
        return new TraceClassVisitor(cv, writer);
    }

    public static void dumpClass(String classInternalName, byte[] classContents) throws IOException {
        String fileName = classInternalName + ".class";
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("Writing class file " + fileName);
        }
        File classFile = new File(classgenPath, fileName);
        if (classFile.exists()) {
            classFile.delete();
        } else {
            classFile.getParentFile().mkdirs();
        }
        FileOutputStream writer = new FileOutputStream(classFile);
        BufferedOutputStream buffer = null;
        try {
            buffer = new BufferedOutputStream(writer);
            buffer.write(classContents);
        } finally {
            if (buffer != null) {
                buffer.close();
            }
        }
    }

    public static String methodNodeListToString(List<MethodNode> mns) {
        if (mns.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<MethodNode> mnsIt = mns.iterator();
        MethodNode mn = mnsIt.next();
        sb.append(mn.name).append(mn.desc);
        while (mnsIt.hasNext()) {
            mn = mnsIt.next();
            sb.append(", ");
            sb.append(mn.name).append(mn.desc);
        }
        return sb.toString();
    }

    public static void verifyClass(String className, ClassReader classReader, boolean dump) throws CoroutineGenerationException {
        try {
            String filename = className + ".ver";
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Running verification process. If verification fails (or printing is turned on) file " + filename + " will contain detailed information");
            }
            File outputFile = new File(printPath, filename);
            PrintWriter writer = new PrintWriter(outputFile);
            CheckClassAdapter.verify(classReader, dump, writer);
        } catch (FileNotFoundException e) {
            throw new CoroutineGenerationException("Verification could not be dumped to disk", e);
        }
    }

    private String[] coroutineEnabledClassnames;

    private boolean[] debugMode;

    private boolean detectCoroutineClasses;

    private boolean generateBinaryOutput;

    private boolean generateDebugCode;

    private boolean[] outputBinMode;

    private boolean overrideFrames;

    private boolean[] overrideFramesMode;

    private boolean printCode;

    private boolean[] printMode;

    private boolean runVerification;

    private boolean[] verifyMode;

    CoroutineInstrumentator() {
        this(false, false, false, false, false);
    }

    CoroutineInstrumentator(boolean generateDebugCode, boolean printCode, boolean verify, boolean outputBin, boolean overrideFrames) {
        detectCoroutineClasses = true;
        this.generateDebugCode = generateDebugCode;
        this.printCode = printCode;
        this.runVerification = verify;
        this.generateBinaryOutput = outputBin;
        this.overrideFrames = overrideFrames;
    }

    CoroutineInstrumentator(String[] coroutineEnabledClassnames) {
        this(coroutineEnabledClassnames, false, false, false, false, false);
    }

    CoroutineInstrumentator(String[] coroutineEnabledClassnames, boolean generateDebugCode, boolean printCode, boolean verify, boolean outputBin, boolean overrideFrames) {
        int classesLength = coroutineEnabledClassnames.length;
        Arrays.sort(coroutineEnabledClassnames);
        this.coroutineEnabledClassnames = coroutineEnabledClassnames;
        this.debugMode = new boolean[classesLength];
        this.printMode = new boolean[classesLength];
        this.verifyMode = new boolean[classesLength];
        this.outputBinMode = new boolean[classesLength];
        this.overrideFramesMode = new boolean[classesLength];
        if (generateDebugCode) {
            for (int i = 0; i < classesLength; i++) {
                debugMode[i] = true;
            }
        }
        if (printCode) {
            for (int i = 0; i < classesLength; i++) {
                printMode[i] = true;
            }
        }
        if (verify) {
            for (int i = 0; i < classesLength; i++) {
                verifyMode[i] = true;
            }
        }
        if (outputBin) {
            for (int i = 0; i < classesLength; i++) {
                outputBinMode[i] = true;
            }
        }
        if (overrideFrames) {
            for (int i = 0; i < classesLength; i++) {
                overrideFramesMode[i] = true;
            }
        }
        for (int i = 0; i < classesLength; i++) {
            String classname = coroutineEnabledClassnames[i];
            int indexOfOptionSeparator = classname.lastIndexOf('-');
            if (indexOfOptionSeparator != -1) {
                String[] options = classname.substring(indexOfOptionSeparator + 1).split(",");
                classname = classname.substring(0, indexOfOptionSeparator);
                for (String option : options) {
                    if (option.equals("debug")) {
                        debugMode[i] = true;
                    } else if (option.equals("overrideframes")) {
                        overrideFramesMode[i] = true;
                    } else if (option.equals("print")) {
                        printMode[i] = true;
                    } else if (option.equals("outputbin")) {
                        outputBinMode[i] = true;
                    } else if (option.equals("verify")) {
                        verifyMode[i] = true;
                    }
                }
            }
            this.coroutineEnabledClassnames[i] = classname.replace('.', '/');
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classBeingRedefined != null) {
            return null;
        }
        if (className.startsWith("java/") || className.startsWith("javax/") || className.startsWith("sun/")) {
            return null;
        }
        List<MethodNode> coroutineMethodsInCurrentClass;
        boolean debug = generateDebugCode;
        boolean print = printCode;
        boolean verify = runVerification;
        boolean outputBin = generateBinaryOutput;
        boolean asmComputeFrames = overrideFrames;
        if (!detectCoroutineClasses) {
            int classnameIndex = Arrays.binarySearch(coroutineEnabledClassnames, className);
            if (classnameIndex < 0) {
                String packageName = className;
                int indexOfSlash;
                while ((indexOfSlash = packageName.lastIndexOf('/')) != -1) {
                    packageName = packageName.substring(0, indexOfSlash);
                    classnameIndex = Arrays.binarySearch(coroutineEnabledClassnames, packageName);
                    if (classnameIndex >= 0) {
                        break;
                    }
                }
                if (classnameIndex < 0) return null;
            }
            debug = debugMode[classnameIndex];
            print = printMode[classnameIndex];
            verify = verifyMode[classnameIndex];
            outputBin = outputBinMode[classnameIndex];
            asmComputeFrames = overrideFramesMode[classnameIndex];
        }
        boolean log = logger.isLoggable(Level.FINEST);
        if (log) {
            logger.finest(className + ": Analyzing");
        }
        ClassReader asmClassReader = new ClassReader(classfileBuffer);
        ClassNode cn = new ClassNode();
        asmClassReader.accept(cn, debug ? 0 : ClassReader.SKIP_DEBUG);
        ClassAnalyzer analyzer = new ClassAnalyzer(cn);
        analyzer.analyze();
        coroutineMethodsInCurrentClass = analyzer.getCoroutineMethods();
        if (coroutineMethodsInCurrentClass.isEmpty()) {
            return null;
        }
        if (log) {
            logger.finest(className + ": Instrumenting coroutines " + methodNodeListToString(coroutineMethodsInCurrentClass));
        }
        ClassWriter asmClassWriter = new ClassWriter((asmComputeFrames ? ClassWriter.COMPUTE_FRAMES : 0) | ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = asmClassWriter;
        byte[] instrumentedClassContents;
        try {
            if (print) {
                try {
                    cv = createTracer(className, cv);
                } catch (FileNotFoundException e) {
                    throw new CoroutineGenerationException("Unable to write trace file ", e);
                }
            }
            new ClassTransformer(cn, coroutineMethodsInCurrentClass, debug).transform();
            cn.accept(cv);
            instrumentedClassContents = asmClassWriter.toByteArray();
            if (verify) {
                verifyClass(className, new ClassReader(instrumentedClassContents), print);
            }
            if (outputBin) {
                dumpClass(className + "Instrumented", instrumentedClassContents);
            }
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, "Verification failed", e);
            return null;
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Verification failed", e);
            return null;
        } catch (CoroutineGenerationException e) {
            logger.warning(e.getMessage());
            return null;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Coroutine generation ended abruptly. This may be a bug in the package itself. Details below:", t);
            return null;
        }
        return instrumentedClassContents;
    }

    private static final String classgenPath = System.getProperty("pl.clareo.coroutines.ClassgenPath", ".");

    private static final Logger logger = Logger.getLogger("pl.clareo.coroutines.CoroutineInstrumentator");

    private static final String printPath = System.getProperty("pl.clareo.coroutines.PrintPath", ".");
}
