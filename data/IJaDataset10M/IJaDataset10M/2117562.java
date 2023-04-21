package jest.classfile;

/**
  * AccessConstants is a class containing static fields defining the various =
  * access attibutes used in the Java language and JVM.
  */
public class AccessConstants {

    /**    Declared public; may be accessed from outside its package. */
    public static int PUBLIC = 0x0001;

    /**    Declared private; usable only within the defining class. */
    public static int PRIVATE = 0x0002;

    /**    Declared protected; may be accessed within subclasses. */
    public static int PROTECTED = 0x0004;

    /**    Declared static. */
    public static int STATIC = 0x0008;

    /**    Declared final; no further assignment after initialization. */
    public static int FINAL = 0x0010;

    /**    Declared synchronized; invocation is wrapped in a monitor lock. */
    public static int SYNCHRONIZED = 0x0020;

    /**    Declared volatile; cannot be cached. */
    public static int VOLATILE = 0x0040;

    /**    Declared transient; not written or read by a persistent object manager.*/
    public static int TRANSIENT = 0x0080;

    /**    Declared native; implemented in a language other than Java. */
    public static int NATIVE = 0x0100;

    /**    Declared abstract; no implementation is provided. */
    public static int ABSTRACT = 0x0400;

    /**    Declared strictfp; floating-point mode is FP-strict. */
    public static int STRICT = 0x0800;
}
