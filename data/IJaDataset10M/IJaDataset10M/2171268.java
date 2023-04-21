package solidbase.core;

import solidstack.io.SourceLocation;

/**
 * Represents a single segment in the upgrade file.
 *
 * @author Ren� M. de Bloois
 * @since Apr 1, 2006 7:18:15 PM
 */
public class UpgradeSegment {

    /**
	 * The possible types of a segment.
	 */
    public enum Type {

        /**
		 * A setup segment is used to create or maintain the DBVERSION and DBVERSIONLOG tables.
		 */
        SETUP, /**
		 * An upgrade segment is used to upgrade the database.
		 */
        UPGRADE, /**
		 * A switch segment is used to switch between upgrade paths (branches).
		 */
        SWITCH, /**
		 * A downgrade segment is used during development to downgrade the database. For example, this makes it possible
		 * to switch back to a previous stable branch.
		 */
        DOWNGRADE
    }

    /**
	 * The type of this segment.
	 */
    protected Type type;

    /**
	 * The source version of this segment.
	 */
    protected String source;

    /**
	 * The target version of this segment.
	 */
    protected String target;

    /**
	 * Is this segment open (unfinished).
	 */
    protected boolean open;

    /**
	 * The file location of this segment.
	 */
    protected SourceLocation location;

    /**
	 * Constructs a new segment.
	 *
	 * @param type The type of the segment.
	 * @param source The source version of this segment.
	 * @param target The target version of this segment.
	 * @param open Is this segment open (unfinished).
	 */
    protected UpgradeSegment(Type type, String source, String target, boolean open) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.open = open;
    }

    /**
	 * Is this segment a switch.
	 *
	 * @return True if this segment is a switch, false otherwise.
	 */
    protected boolean isSwitch() {
        return this.type == Type.SWITCH;
    }

    /**
	 * Gets the source version.
	 *
	 * @return The source version.
	 */
    public String getSource() {
        return this.source;
    }

    /**
	 * Gets the target version.
	 *
	 * @return The target version.
	 */
    public String getTarget() {
        return this.target;
    }

    /**
	 * Sets the file location for this segment.
	 *
	 * @param location The file location for this segment.
	 */
    protected void setLocation(SourceLocation location) {
        this.location = location;
    }

    /**
	 * Gets the line number in the file for this segment.
	 *
	 * @return The line number in the file for this segment.
	 */
    protected int getLineNumber() {
        return this.location.getLineNumber();
    }

    /**
	 * Gets the line number in the file for this segment.
	 *
	 * @return The line number in the file for this segment.
	 */
    protected SourceLocation getLocation() {
        return this.location;
    }

    /**
	 * Is this segment open.
	 *
	 * @return True if this segment is open (unfinished), false otherwise.
	 */
    protected boolean isOpen() {
        return this.open;
    }

    /**
	 * Is this an init segment.
	 *
	 * @return True if this segment is an init segment, false otherwise.
	 */
    protected boolean isSetup() {
        return this.type == Type.SETUP;
    }

    /**
	 * Is this a downgrade segment.
	 *
	 * @return True if this segment is a downgrade segment, false otherwise.
	 */
    protected boolean isDowngrade() {
        return this.type == Type.DOWNGRADE;
    }

    /**
	 * Is this segment is normal upgrade segment?
	 *
	 * @return True if this segment is a normal upgrade segment, false otherwise.
	 */
    protected boolean isUpgrade() {
        return this.type == Type.UPGRADE;
    }

    @Override
    public String toString() {
        return "UpgradeSegment(source:" + this.source + ", target:" + this.target + ", open:" + this.open + ")";
    }

    /**
	 * Returns the type of this segment.
	 *
	 * @return The type of this segment.
	 */
    public Type getType() {
        return this.type;
    }
}
