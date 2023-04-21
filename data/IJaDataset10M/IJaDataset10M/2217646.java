package edu.xtec.jclic.bags;

import edu.xtec.jclic.Activity;
import edu.xtec.jclic.edit.Editable;
import edu.xtec.jclic.edit.Editor;
import edu.xtec.jclic.fileSystem.FileSystem;
import java.util.HashMap;

/**
 * This class stores a XML {@link orj.jdom.Element} that defines an
 * {@link edu.xtec.jclic.Activity}. It stores also a {@link java.util.HashMap}
 * with references of other objects to this activity, and implements some
 * useful methods to directly retrieve some properites of the related Activity,
 * like its name. ActivityBagElements are usually stored into
 * {@link edu.xtec.jclic.bags.ActivityBag} objects.
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class ActivityBagElement extends Object implements Editable, Cloneable {

    private org.jdom.Element element;

    private HashMap references;

    /** Creates new ActivityBagElement */
    public ActivityBagElement(org.jdom.Element e) {
        setData(e);
    }

    public ActivityBagElement(ActivityBagElement a) {
        this.element = a.getElement();
        this.references = a.getReferences();
    }

    public org.jdom.Element getElement() {
        return this.element;
    }

    public String getName() {
        return FileSystem.stdFn(element.getAttributeValue(Activity.NAME));
    }

    public void setData(org.jdom.Element e) {
        element = e;
        references = null;
    }

    public org.jdom.Element getData() {
        return element;
    }

    public String toString() {
        return getName();
    }

    /** Getter for property dependences.
     * @return Value of property dependences.
     */
    public HashMap getReferences() {
        if (references == null && element != null) {
            references = new HashMap();
            Activity.listReferences(element, references);
        }
        return references;
    }

    public Editor getEditor(Editor parent) {
        return Editor.createEditor(getClass().getName() + "Editor", this, parent);
    }

    public Object clone() throws CloneNotSupportedException {
        ActivityBagElement result = (ActivityBagElement) super.clone();
        result.references = null;
        result.element = (org.jdom.Element) element.clone();
        return result;
    }
}
