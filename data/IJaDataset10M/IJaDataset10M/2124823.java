package com.j2biz.compote.plugins.admin.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.j2biz.compote.pojos.Extension;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class ExtensionForm extends ActionForm {

    private String id = null;

    private String name = null;

    private String className = null;

    private String extPointId = null;

    /**
     * @return Returns the name.
     */
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

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setId(null);
        setName(null);
        setClassName(null);
        setExtPointId(null);
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (StringUtils.isEmpty(name)) errors.add("name", new ActionError("error.name.required"));
        if (StringUtils.isEmpty(className)) errors.add("className", new ActionError("error.className.required"));
        if (StringUtils.isEmpty(extPointId)) errors.add("extensionPoint", new ActionError("error.extensionPoint.required"));
        return errors;
    }

    /**
     * @param sysRole
     */
    public void initFromExtension(Extension extension) {
        setId(String.valueOf(extension.getId()));
        setName(extension.getName());
        setClassName(extension.getClassName());
        setExtPointId(String.valueOf(extension.getExtensionPoint().getId()));
    }

    /**
     * @return Returns the fileContent.
     */
    public String getExtPointId() {
        return extPointId;
    }

    /**
     * @param fileContent
     *            The fileContent to set.
     */
    public void setExtPointId(String fileContent) {
        this.extPointId = fileContent;
    }

    /**
     * @return Returns the path.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param path
     *            The path to set.
     */
    public void setClassName(String path) {
        this.className = path;
    }
}
