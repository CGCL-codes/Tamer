package sisc.nativefun;

import sisc.data.Value;
import sisc.util.Util;

/**
 * This class serves only as an example, and should never be subclassed
 * Remove the abstract modifier when copying/pasting.
 *  
 * @author scgmille
 *
 */
public abstract class NativeModuleTemplate extends Util {

    public static final int IDS = -1;

    /**
     * The Simple procedures are purely functional procedures
     * which do not need to access interpreter registers to execute
     */
    public static class Simple extends IndexedFixableProcedure {

        public Simple() {
        }

        Simple(int id) {
            super(id);
        }
    }

    /**
     * The Complex procedures either have a side effect, or
     * require the interpreter to execute
     */
    public static class Complex extends CommonIndexedProcedure {

        public Complex() {
        }

        Complex(int id) {
            super(id);
        }
    }

    /**
     * The Index 
     */
    public static class Index extends IndexedLibraryAdapter {

        public Index() {
        }

        public Value construct(Object context, int id) {
            if (context == null || context == Simple.class) {
                return new Simple(id);
            } else return new Complex(id);
        }
    }
}
