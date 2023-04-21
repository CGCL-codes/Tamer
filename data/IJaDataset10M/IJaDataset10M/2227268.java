package org.antdepo.tasks;

import org.antdepo.Constants;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Property accessor for the document order of properties.
 * <p/>
 *
 * @version $Revision: 215 $
 * @ant.task name="document-property"
 */
public class DocumentProperty extends ResourceProperty {

    /**
     * reference to field
     */
    private String field;

    /**
     * Setter to field
     *
     * @param f a <code>PropField</code> value
     */
    public void setField(final PropField f) {
        field = f.getValue();
    }

    /**
     * reference to the document's name
     */
    private String docname;

    /**
     * Setter to the doc field
     *
     * @param name the name of the document
     */
    public void setDoc(final String name) {
        docname = name;
    }

    /**
     * Executes the task
     */
    public void execute() {
        validate();
        if (field.equals(Constants.PROPERTY_RES_NAME_FIELD)) {
            if (null == getInputproperty()) throw new BuildException("inputproperty attribute not set");
            setAntResultpropertyNotNull(getName(getInputproperty()));
            return;
        }
        if (field.equals(Constants.PROPERTY_DOC_NAMES_FIELD)) {
            if (null == getName()) throw new BuildException("name attribute not set");
            setAntResultpropertyNotNull(getNames(getName()));
            return;
        }
        if (null == docname) throw new BuildException("doc attribute not set");
        if (field.equals(Constants.PROPERTY_RES_PROXCNSTRT_FIELD)) {
            setAntResultpropertyNotNull(getProximity(docname));
        } else if (field.equals(Constants.PROPERTY_RES_DIRCNSTRT_FIELD)) {
            setAntResultpropertyNotNull(getDirection(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_EXISTS_FIELD)) {
            setAntResultpropertyNotNull(getExists(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_FILETYPE_FIELD)) {
            setAntResultpropertyNotNull(getFiletype(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_MODDATE_FIELD)) {
            setAntResultpropertyNotNull(getModdate(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_OUTPUTDIR_FIELD)) {
            setAntResultpropertyNotNull(getOutputdir(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_TEMPLATE_FIELD)) {
            setAntResultpropertyNotNull(getTemplate(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_TEMPLATEDIR_FIELD)) {
            setAntResultpropertyNotNull(getTemplatedir(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_TEMPLATETYPE_FIELD)) {
            setAntResultpropertyNotNull(getTemplatetype(docname));
        } else if (field.equals(Constants.PROPERTY_DOC_MANAGEMENT_VIEW_FIELD)) {
            setAntResultpropertyNotNull(getManagementView(docname));
        }
    }

    private String getManagementView(final String docname) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(docname + ".").append(Constants.PROPERTY_DOC_MANAGEMENT_VIEW_FIELD);
        return getProject().getProperty(sb.toString());
    }

    private String getProximity(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_RES_PROXCNSTRT_FIELD);
        return getProject().getProperty(sb.toString());
    }

    private String getDirection(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_RES_DIRCNSTRT_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Returns the exists
     *
     * @param name the entityName
     * @return
     */
    private String getExists(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_DOC_EXISTS_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Returns the filetype
     *
     * @param name then entityName
     * @return
     */
    private String getFiletype(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_DOC_FILETYPE_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Returns the moddate
     *
     * @param name then entityName
     * @return
     */
    private String getModdate(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_DOC_MODDATE_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Returns the outputdir
     *
     * @param name then entityName
     * @return
     */
    private String getOutputdir(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_DOC_OUTPUTDIR_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Returns the template
     *
     * @param name then entityName
     * @return
     */
    private String getTemplate(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_DOC_TEMPLATE_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Returns the templatedir
     *
     * @param name then entityName
     * @return
     */
    private String getTemplatedir(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_DOC_TEMPLATEDIR_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Returns the templatetype
     *
     * @param name then entityName
     * @return
     */
    private String getTemplatetype(final String name) {
        final StringBuffer sb = new StringBuffer();
        sb.append("document.").append(name + ".").append(Constants.PROPERTY_DOC_TEMPLATETYPE_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Return the list of documents
     *
     * @param entityName the entityName
     * @return
     */
    private String getNames(final String entityName) {
        final StringBuffer sb = new StringBuffer();
        sb.append("documents.").append(entityName + ".").append(Constants.PROPERTY_DOC_NAMES_FIELD);
        return getProject().getProperty(sb.toString());
    }

    /**
     * Overriden method to return name given a property
     *
     * @param inputproperty
     * @return
     */
    protected String getName(final String inputproperty) {
        return inputproperty.split(SPLIT_PAT)[1];
    }

    public void setType(final String type) {
        throw new BuildException("type attribute not supported by this task");
    }

    /**
     * Validates the task input
     */
    protected void validate() {
        if (null == getResultproperty()) throw new BuildException("resultproperty attribute not set");
        if (null == field) throw new BuildException("field attribute not set");
    }

    /**
     * enumeration of property fields
     */
    public static class PropField extends EnumeratedAttribute {

        public String[] getValues() {
            return new String[] { Constants.PROPERTY_RES_NAME_FIELD, Constants.PROPERTY_RES_DIRCNSTRT_FIELD, Constants.PROPERTY_RES_PROXCNSTRT_FIELD, Constants.PROPERTY_DOC_EXISTS_FIELD, Constants.PROPERTY_DOC_FILETYPE_FIELD, Constants.PROPERTY_DOC_MODDATE_FIELD, Constants.PROPERTY_DOC_OUTPUTDIR_FIELD, Constants.PROPERTY_DOC_TEMPLATE_FIELD, Constants.PROPERTY_DOC_TEMPLATEDIR_FIELD, Constants.PROPERTY_DOC_TEMPLATETYPE_FIELD, Constants.PROPERTY_DOC_NAMES_FIELD, Constants.PROPERTY_DOC_MANAGEMENT_VIEW_FIELD };
        }
    }
}
