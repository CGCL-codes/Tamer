package br.gov.demoiselle.eclipse.util.utility.classwriter;

import java.util.List;

public class ClassRepresentation {

    private String name = null;

    private String fullName = null;

    private String generic = null;

    private String fullGenericName = null;

    private List<ClassRepresentation> fields = null;

    public ClassRepresentation() {
    }

    public ClassRepresentation(String fullName) {
        setFullName(fullName);
    }

    /**
	 * This Method converts this object to string with java code restrictions  
	 * @return Returns a String with this format: "Type<GenericType>"
	 */
    public String toString() {
        StringBuffer result = new StringBuffer();
        if (name != null) {
            result.append(name);
        } else if (fullName != null) {
            result.append(fullName);
        }
        if (generic != null) {
            result.append("<").append(generic).append(">");
        }
        return result.toString();
    }

    /**
	 * Set fullName value and check if name is null, in case positive, set this with
	 * the name of the class (from the last point to the end) 
	 * ex.: br.gov.demoiselle.framework.Example, name = Example
	 * @param fullName
	 */
    public void setFullName(String fullName) {
        if (name == null) {
            if (fullName != null && fullName.lastIndexOf('.') > 0) {
                name = fullName.substring(fullName.lastIndexOf('.') + 1);
            } else {
                name = fullName;
            }
        }
        this.fullName = fullName;
    }

    public String getGeneric() {
        return generic;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }

    public String getFullGenericName() {
        return fullGenericName;
    }

    public void setFullGenericName(String fullGenericName) {
        if (generic == null && fullGenericName != null && fullGenericName.lastIndexOf('.') > 0) {
            generic = fullGenericName.substring(fullGenericName.lastIndexOf('.') + 1);
        }
        this.fullGenericName = fullGenericName;
    }

    public List<ClassRepresentation> getFields() {
        return fields;
    }

    public void setFields(List<ClassRepresentation> fields) {
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }
}
