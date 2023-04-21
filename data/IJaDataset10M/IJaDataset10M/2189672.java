package org.xanot;

import org.xanot.annotation.ParentReference;
import org.xanot.annotation.Repeatable;
import org.xanot.annotation.TagInstance;
import org.xanot.annotation.XmlAttribute;
import org.xanot.annotation.XmlCollectionProperty;
import org.xanot.annotation.XmlSingleProperty;
import java.util.ArrayList;

@TagInstance(path = "child")
@Repeatable
public class Child {

    private Parent parent = null;

    private String name = null;

    private String description = null;

    private ArrayList<Child> childs = new ArrayList<Child>();

    /**
	 * @return Returns the parent.
	 */
    @ParentReference
    public Parent getParent() {
        return parent;
    }

    /**
	 * @param parent
	 *            The parent to set.
	 */
    public void setParent(Parent parent) {
        this.parent = parent;
    }

    /**
	 * @return Returns the name.
	 */
    @XmlAttribute(name = "tagname")
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the description.
	 */
    @XmlSingleProperty(name = "desc")
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 *            The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return Returns the childs.
	 */
    @XmlCollectionProperty(methodName = "addChild", xmltag = "child", element = Child.class)
    public ArrayList<Child> getChilds() {
        return childs;
    }

    /**
	 * @param childs
	 *            The childs to set.
	 */
    public void setChilds(ArrayList<Child> childs) {
        this.childs = childs;
    }

    /**
	 * 
	 * 
	 * @param child
	 * 
	 */
    public void addChild(Child child) {
        childs.add(child);
    }
}
