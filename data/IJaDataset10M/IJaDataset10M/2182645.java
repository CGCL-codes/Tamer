package jp.go.aist.six.oval.model.sc;

import jp.go.aist.six.oval.model.OvalObject;

/**
 * The variableValue holds the value to a variable
 * used during the collection of an object.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: VariableValueType.java 2026 2011-09-21 03:10:55Z nakamura5akihito $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class VariableValueType implements OvalObject {

    private String variable_id;

    private String content;

    /**
     * Constructor.
     */
    public VariableValueType() {
    }

    public VariableValueType(final String variable_id, final String content) {
        setVariableID(variable_id);
        setContent(content);
    }

    /**
     */
    public void setVariableID(final String id) {
        this.variable_id = id;
    }

    public String getVariableID() {
        return this.variable_id;
    }

    /**
     */
    public void setContent(final String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "[variable_id=" + getVariableID() + ", " + getContent() + "]";
    }
}
