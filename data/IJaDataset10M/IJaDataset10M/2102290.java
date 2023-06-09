package polyglot.frontend;

/**
 * Version information for the base compiler.
 */
public class JLVersion extends polyglot.main.Version {

    public String name() {
        return "jl";
    }

    public int major() {
        return 3;
    }

    public int minor() {
        return 2;
    }

    public int patch_level() {
        return 0;
    }

    public String toString() {
        return "3.2.0 (2010-06-03 19:23:58)";
    }
}
