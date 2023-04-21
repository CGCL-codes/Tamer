package PolicyAlgebra.Type;

/** 
 * This class implements the ability to associate a Multi Level
 * Security level / clearance and a set of categories with a
 * named atom; It also provides to check for domination between
 * two (sub)classes of this kind.
 */
public abstract class MlsCapable extends Commentable {

    private MlsLevel level;

    /** 
	 * Constructor, automatically adds "" as comment. 
	 * 
	 * @param name The name for this commentable.
	 */
    public MlsCapable(String name) {
        this(name, "");
    }

    /** 
	 * Constructor. 
	 * 
	 * @param name The name for this commentable.
	 * @param comment The commetn to attach to the commentable.
	 */
    public MlsCapable(String name, String comment) {
        this(name, new Type(), comment);
    }

    /** 
	 * Constructor. 
	 * 
	 * @param name The name for this commentable.
	 * @param type The type to attach to the commentable.
	 */
    public MlsCapable(String name, Type type) {
        this(name, type, "");
    }

    /** 
	 * Constructor. 
	 * 
	 * @param name The name for this commentable.
	 * @param type The type to attach to the commentable.
	 * @param comment The comment to attach to the commentable.
	 */
    public MlsCapable(String name, Type type, String comment) {
        this(name, type, comment, new MlsLevel());
    }

    /** 
	 * Constructor. 
	 * 
	 * @param name The name for this commentable.
	 * @param type The type to attach to the commentable.
	 * @param comment The comment to attach to the commentable.
	 * @param level The MLS portion of this object.
	 */
    public MlsCapable(String name, Type type, String comment, MlsLevel level) {
        super(name, type, comment);
        this.level = level;
    }

    /** 
	 * Accessor for the MLS level. 
	 * 
	 * @return The MLS level attached to this object.
	 */
    public MlsLevel getLevel() {
        return level;
    }

    /** 
	 * Custom string transformation method. 
	 * 
	 * @return The string representation of this MLS capable named atom.
	 */
    public String toString() {
        return super.toString() + level;
    }
}
