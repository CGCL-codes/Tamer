package bioweka.core.properties.enums;

import bioweka.core.properties.AbstractSingleOptionProperty;
import weka.core.SelectedTag;
import weka.core.Tag;

/**
 * Abstract base class for properties that rely on enumerations and the 
 * {@link weka.core.SelectedTag} class.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractSelectedTagProperty extends AbstractSingleOptionProperty {

    /**
     * Initializes the property with its name.
     * @param name the name for the property
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     */
    public AbstractSelectedTagProperty(String name) {
        super(name);
    }

    /**
     * Gets the selected tag.
     * @return the list of possible tags and the currently selected tag
     */
    public final SelectedTag getSelectedTag() {
        return new SelectedTag(getTag().getID(), tags());
    }

    /**
     * Sets the selected tag.
     * @param selectedTag  the list of possible tags and the currently selected 
     * tag.
     * @throws Exception if <code>selectedTag</code> is invalid.
     */
    public final void setSelectedTag(SelectedTag selectedTag) throws Exception {
        setTag(selectedTag.getSelectedTag());
    }

    /**
     * Returns the possible tags.
     * @return a list of <code>Tag</code> objects.
     */
    protected abstract Tag[] tags();

    /**
     * Returns the default tag.
     * @return the initial tag
     */
    protected abstract Tag defaultTag();

    /**
     * Sets the selected tag.
     * @param tag the selected tag
     * @throws Exception if <code>tag</code> is invalid.
     */
    protected abstract void setTag(Tag tag) throws Exception;

    /**
     * Gets the selected tag.
     * @return the selected tag
     */
    protected abstract Tag getTag();

    /**
     * {@inheritDoc}
     */
    protected final String defaultOptionValue() {
        return defaultTag().getReadable();
    }

    /**
     * {@inheritDoc}
     */
    protected final String optionSynopsis() {
        StringBuffer sb = new StringBuffer();
        Tag[] tags = tags();
        sb.append("[");
        for (int i = 0; i < tags.length; i++) {
            sb.append(Integer.toString(tags[i].getID()) + ":" + tags[i].getReadable());
            if (i < tags.length - 1) {
                sb.append("|");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    protected final String getOptionValue() {
        return getTag().getReadable();
    }

    /**
     * {@inheritDoc}
     */
    protected final void setOptionValue(String value) throws Exception {
        Tag[] tags = tags();
        try {
            int id = Integer.parseInt(value);
            for (int i = 0; i < tags.length; i++) {
                if (id == tags[i].getID()) {
                    setTag(tags[i]);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            for (int i = 0; i < tags.length; i++) {
                if (tags[i].getReadable().equalsIgnoreCase(value)) {
                    setTag(tags[i]);
                    return;
                }
            }
        }
        throw new IllegalArgumentException(value + " is not a valid tag.");
    }
}
