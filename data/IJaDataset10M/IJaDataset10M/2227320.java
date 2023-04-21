package archmapper.main.model.stylemapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * Abstract super class for FileType, ClassType and InterfaceType. All of these
 * are implementation artifact types.
 * 
 * @author mg
 *
 */
public abstract class ImplementationArtifactType {

    protected Boolean optional = false;

    protected String typeName;

    @XmlAttribute
    public Boolean isOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    @XmlID
    @XmlAttribute(required = true)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
	 * Returns the super-interfaces of this implementation artifact type.
	 * By default, returns an empty list. For File and Interface types, returns
	 * the interfaces.
	 * 
	 * @return Qualified names of the interfaces, or an empty list.
	 */
    public List<String> getSuperInterfaces() {
        return new ArrayList<String>();
    }
}
