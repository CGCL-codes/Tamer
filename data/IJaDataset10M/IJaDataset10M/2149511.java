package archmapper.main.model.stylemapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Represents a class type that belongs to a component
 * or connector type from an architectural style. Each class
 * type can have a super class, interfaces, and restrictions
 * for the implementation. If a class type is optional, it needs
 * not be part of the implementation, but can be.
 * 
 * @author mg
 *
 */
public class ClassType extends ImplementationArtifactType {

    public enum ClassTypeRestriction {

        noSuperclass
    }

    ;

    private List<String> interfaces = new ArrayList<String>();

    private String superClass;

    private Boolean singleton = false;

    private List<ClassTypeRestriction> restrictions = new ArrayList<ClassTypeRestriction>();

    /**
	 * @return the interfaces
	 */
    @XmlElement(name = "interface")
    public List<String> getInterfaces() {
        return interfaces;
    }

    /**
	 * @param interfaces
	 *            the interfaces to set
	 */
    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    /**
	 * @return the restrictions
	 */
    @XmlAttribute(name = "restriction")
    public List<ClassTypeRestriction> getRestrictions() {
        return restrictions;
    }

    /**
	 * @param restrictions
	 *            the restrictions to set
	 */
    public void setRestrictions(List<ClassTypeRestriction> restrictions) {
        this.restrictions = restrictions;
    }

    /**
	 * @return the superClass
	 */
    @XmlAttribute
    public String getSuperClass() {
        return superClass;
    }

    /**
	 * @param superClass
	 *            the superClass to set
	 */
    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    /**
	 * @return true, if a class of this type is a singleton
	 */
    @XmlAttribute
    public Boolean isSingleton() {
        return singleton;
    }

    /**
	 * @param singleton
	 */
    public void setSingleton(Boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public List<String> getSuperInterfaces() {
        return getInterfaces();
    }
}
