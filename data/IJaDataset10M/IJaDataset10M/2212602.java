package syntelos.compile;

/**
 * Used by {@link Compile} to efficiently identify and allocate {@link
 * Transform} classes given source and target type names (filename
 * extensions).
 * 
 * The {@link Registry$Builtins Builtins} registry implements a
 * convention for the <code>"syntelos"</code> namespace via {@link
 * java.lang.Class#forName(java.lang.String)}.
 * 
 * A store based {@link Registry} would employ the {@link
 * Registry$Builtins Builtins} registry in a parent scope.
 * 
 * @since 1.5
 */
public interface Registry {

    /**
     * Base class for source type registries as available via {@link
     * Registry$Builtins Builtins}.
     * 
     * Provides {@link #Camel(java.lang.String)} function for working
     * on the reference file type string to produce a component of a
     * class name, for example the builtin transformers are ultimately
     * available via the convention
     * <code>"syntelos."+sourceType+".compile."+Camel(sourceType)+"To"+Camel(targetType)</code>.
     * 
     * Subclasses may choose to employ the superclass map in any way
     * that's convenient.
     */
    public abstract class Abstract extends alto.io.u.Objmap implements Registry {

        public static final java.lang.String Camel(java.lang.String name) {
            if (null != name && 1 < name.length()) {
                java.lang.String a = name.substring(0, 1).toUpperCase();
                java.lang.String b = name.substring(1).toLowerCase();
                return alto.io.u.Chbuf.cat(a, b);
            } else throw new alto.sys.Error.Argument("Null or empty or short name '" + name + "'.");
        }

        private static final alto.io.u.Objmap CompilesToClass = new alto.io.u.Objmap();

        /**
         * Static initializer for this and sub classes to configure
         * filename extensions that target the "class" type (JVM JCF
         * binary).
         */
        protected static final void SInitCompilesToClass(String type) {
            CompilesToClass.put(type, Boolean.TRUE);
        }

        static {
            SInitCompilesToClass("java");
            SInitCompilesToClass("sp");
        }

        /**
         * @param type Filename extension, for example "java"
         * @return Whether this type has been configured here to
         * target the "class" type.
         */
        public static final boolean CompilesToClass(String type) {
            return (null != CompilesToClass.get(type));
        }

        public Abstract() {
            super();
        }

        public String targetFor(String sourceType) {
            if (null == sourceType) return null; else if (CompilesToClass(sourceType)) return "class"; else return null;
        }
    }

    /**
     * Identify builtin registries using the convention
     * <code>"syntelos."+type+".compile.Registry"</code> via
     * {@link java.lang.Class#forName(java.lang.String)}.  A "builtin"
     * registry class is loaded and instantiated once and then cached
     * in memory.
     */
    public final class Builtins extends Abstract implements Registry {

        public static final String Name = "builtins";

        public Builtins() {
            super();
        }

        public String getName() {
            return Name;
        }

        protected Registry registryFor(String type) {
            Registry registry = (Registry) this.get(type);
            if (null == registry) {
                String classname = "syntelos." + type + ".compile.Registry";
                try {
                    registry = (Registry) Class.forName(classname).newInstance();
                    this.put(type, registry);
                    return registry;
                } catch (Throwable any) {
                    return null;
                }
            } else return registry;
        }

        /**
         * Lookup a "builtin" (source type) registry and ask it for a
         * {@link Transform} class.
         */
        public Class classFor(String srcType, String tgtType) {
            Registry registry = this.registryFor(srcType);
            if (null == registry) {
                registry = this.registryFor(tgtType);
                if (null == registry) return null;
            }
            return registry.classFor(srcType, tgtType);
        }
    }

    /**
     * @return Short registry class identifier, for example,
     * "builtins" or "store".
     */
    public String getName();

    /**
     * @param  sourceType A source file name extension string
     * 
     * @return The default target type for a source type
     */
    public String targetFor(String sourceType);

    /**
     * A type is a filename extension without the implied leading
     * dot.  Examples include "java" and "class" and "sp".  See also
     * the file type documentation in {@link
     * syntelos.sys.Reference}.
     * 
     * @param sourceType Transform source type, never null.
     * @param targetType Transform target type, never null.
     * 
     * @return {@link Transformer} class for this transformation, or
     * null for not found
     */
    public Class classFor(String sourceType, String targetType);
}
