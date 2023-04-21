package ca.sandstorm.luminance.level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for tools in XmlLevels.
 * @author Steven Indzeoski
 *
 */
public abstract class XmlLevelTool {

    private static final Logger _logger = LoggerFactory.getLogger(XmlLevelTool.class);

    private String _type;

    private int _count;

    /**
     * Constructor for XmlLevelTool object.
     * @param type The type of tool (mirror, prism)
     * @param count The number of this tool you get for this level.
     * @precond type is mirror or prism, count > 0.
     * @postcond An XmlLevelTool is created.
     */
    public XmlLevelTool(String type, int count) {
        _logger.debug("XmlLevelTool(" + type + ", " + count + ")");
        if (!isValidType(type)) {
            throw new IllegalArgumentException("Type given (\"" + type + "\") is invalid.");
        } else if (count < 0) {
            throw new IllegalArgumentException("Number of " + type + "s given (" + count + ") is invalid.");
        }
        _type = type;
        _count = count;
    }

    /**
     * Getter method for the type of the tool.
     * @return The type of the tool.
     */
    public String getType() {
        return _type;
    }

    /**
     * Getter method for the count of the tool.
     * @return The count of the tool.
     */
    public int getCount() {
        return _count;
    }

    /**
     * Checks if a type is valid (mirror, prism)
     * @param type The type to check the validity of.
     * @precond type != null
     * @return True is the type is valid, false otherwise.
     */
    public boolean isValidType(String type) {
        if (type.equals(XmlLevelMirror.getId())) {
            return true;
        } else if (type.equals(XmlLevelPrism.getId())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a string representation of XmlLevelTool.
     * @return A string representation of XmlLevelTool.
     */
    @Override
    public String toString() {
        return ("\nType: " + getType() + "\nCount: " + getCount());
    }

    /**
     * Method for creating a deep copy of the XmlLevelTool.
     * @return A deep copy of the XmlLevelTool.
     */
    public abstract XmlLevelTool deepCopy();
}
