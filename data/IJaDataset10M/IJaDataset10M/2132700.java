package ucl.cs.testingEmulator.core;

import java.io.IOException;
import org.microemu.app.classloader.ChangeCallsClassVisitor;
import org.microemu.app.classloader.ClassPreprocessor;
import org.microemu.app.classloader.InstrumentationConfig;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import ucl.cs.testingEmulator.time.TimeClassAdapter;
import ucl.cs.testingEmulator.time.VirtualClock;

/**
 * @author -Michele Sama- aka -RAX-
 * 
 * University College London
 * Dept. of Computer Science
 * Gower Street
 * London WC1E 6BT
 * United Kingdom
 *
 * Email: M.Sama (at) cs.ucl.ac.uk
 *
 * Group:
 * Software Systems Engineering
 *
 */
public class ASMTry {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        VirtualClock.getSimulatedTime();
        System.currentTimeMillis();
        ClassReader cr = null;
        try {
            cr = new ClassReader(ASMTry.class.getCanonicalName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassWriter cw = new ClassWriter(0);
        ClassAdapter ca = new TimeClassAdapter(cw);
        cr.accept(ca, 0);
        VirtualClock.getSimulatedTime();
        System.currentTimeMillis();
    }
}
