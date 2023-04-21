package be.lassi.domain;

import org.apache.commons.lang.builder.HashCodeBuilder;
import be.lassi.base.Dirty;
import be.lassi.base.NamedObject;
import be.lassi.util.equals.EqualsTester;

/**
 * Named wrapper arround <code>Level</code> object.
 */
public class LevelObject extends NamedObject {

    /**
     * The channel level.
     */
    private final Level level = new Level();

    /**
     * Constructs a new level object.
     *
     * @param dirty the dirty indicator
     */
    public LevelObject(final Dirty dirty, final int id, final String name) {
        super(dirty, id, name);
    }

    /**
     * Gets the integer level value.
     *
     * @return int the integer level value
     */
    public int getIntValue() {
        return level.getIntValue();
    }

    /**
     * Gets the level.
     *
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets the level value.
     *
     * @return float the level value
     */
    public float getValue() {
        return level.getValue();
    }

    /**
     * Sets the level value (integer value).
     *
     * @param value the level value to set
     */
    public void setIntValue(final int value) {
        level.setIntValue(value);
        doNotMarkDirty();
    }

    /**
     * Sets the level value.
     *
     * @param value the level value to set
     */
    public void setValue(final float value) {
        level.setValue(value);
        doNotMarkDirty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = this == object;
        if (!result) {
            EqualsTester tester = EqualsTester.get(this, object);
            if (tester.isEquals()) {
                LevelObject other = (LevelObject) object;
                tester.test(super.equals(other));
                tester.test(getIntValue(), other.getIntValue());
            }
            result = tester.isEquals();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder(-2095369307, -1697413873);
        b.append(super.hashCode());
        return b.toHashCode();
    }
}
