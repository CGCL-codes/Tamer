package archmapper.main.model.stylemapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract super class for ComponentTypeMapping und ConnectorTypeMapping. Both
 * architecture elements can be implemented and thus share a lot of functionality.
 * 
 * @author mg
 *
 */
@XmlType(propOrder = { "interfaceTypes", "classTypes", "fileTypes" })
public abstract class ImplementableTypeMapping extends StyleElementMappingBase {

    protected Boolean external = false;

    protected List<InterfaceType> interfaceTypes = new ArrayList<InterfaceType>();

    protected List<ClassType> classTypes = new ArrayList<ClassType>();

    protected List<FileType> fileTypes = new ArrayList<FileType>();

    /**
	 * @return the classTypes
	 */
    @XmlElement(name = "classType")
    public List<ClassType> getClassTypes() {
        return classTypes;
    }

    /**
	 * @param classTypes
	 *            the classTypes to set
	 */
    public void setClassTypes(List<ClassType> classTypes) {
        this.classTypes = classTypes;
    }

    /**
	 * @return the fileTypes
	 */
    @XmlElement(name = "fileType")
    public List<FileType> getFileTypes() {
        return fileTypes;
    }

    /**
	 * @param fileTypes
	 *            the fileTypes to set
	 */
    public void setFileTypes(List<FileType> fileTypes) {
        this.fileTypes = fileTypes;
    }

    /**
	 * Returns the ClassType with the given type name.
	 * @param classType
	 * @return
	 */
    public ClassType getClassType(String typeName) {
        for (ClassType type : classTypes) {
            if (type.getTypeName() != null && type.getTypeName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    /**
	 * Returns the FileType with the given type name.
	 * @param classType
	 * @return
	 */
    public FileType getFileType(String typeName) {
        for (FileType type : fileTypes) {
            if (type.getTypeName() != null && type.getTypeName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    /**
	 * @return the external
	 */
    @XmlAttribute
    public Boolean isExternal() {
        return external;
    }

    /**
	 * @param external the external to set
	 */
    public void setExternal(Boolean external) {
        this.external = external;
    }

    @XmlElement(name = "interfaceType")
    public List<InterfaceType> getInterfaceTypes() {
        return interfaceTypes;
    }

    public void setInterfaceTypes(List<InterfaceType> interfaceTypes) {
        this.interfaceTypes = interfaceTypes;
    }

    /**
	 * Returns the interface type with the given type name, or null, if
	 * no such interface type exists.
	 * 
	 * @param typeName
	 * @return
	 */
    public InterfaceType getInterfaceType(String typeName) {
        for (InterfaceType iftype : getInterfaceTypes()) {
            if (iftype.getTypeName().equals(typeName)) {
                return iftype;
            }
        }
        return null;
    }

    public List<ImplementationArtifactType> getAllImplementationArtifactTypes() {
        List<ImplementationArtifactType> types = new ArrayList<ImplementationArtifactType>();
        types.addAll(getClassTypes());
        types.addAll(getInterfaceTypes());
        types.addAll(getFileTypes());
        return types;
    }

    /**
	 * Returns the class type, file type or interface type with the given type name,
	 * or null if no such type exists.
	 * 
	 * @param typeName
	 * @return
	 */
    public ImplementationArtifactType getImplementationArtifactType(String typeName) {
        ImplementationArtifactType type = null;
        type = getClassType(typeName);
        if (type != null) {
            return type;
        }
        type = getInterfaceType(typeName);
        if (type != null) {
            return type;
        }
        type = getFileType(typeName);
        if (type != null) {
            return type;
        }
        return null;
    }
}
