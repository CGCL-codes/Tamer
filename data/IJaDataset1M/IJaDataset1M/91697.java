package org.openacs.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.context.FacesContext;
import org.openacs.ScriptLocal;
import org.openacs.utils.Ejb;

/**
 *
 * @author Administrator
 */
public class ScriptJsfBean {

    /** Creates a new instance of ScriptJsfBean */
    public ScriptJsfBean() {
        String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("scriptname");
        if (name != null) {
            this.name = name;
            try {
                ScriptLocal s = Ejb.lookupScriptBean().findByPrimaryKey(name);
                description = s.getDescription();
                text = new String(s.getScript());
            } catch (FinderException ex) {
            }
        }
    }

    private Object[] arrayScriptNames = null;

    public Object[] getAll() throws FinderException {
        if (arrayScriptNames != null) {
            return arrayScriptNames;
        } else {
            return arrayScriptNames = Ejb.lookupScriptBean().findAll().toArray();
        }
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String text = "";

    /**
     * Get the value of text
     *
     * @return the value of text
     */
    public String getText() {
        return text;
    }

    /**
     * Set the value of text
     *
     * @param text new value of text
     */
    public void setText(String text) {
        this.text = text;
    }

    public boolean isNew() {
        return name == null || name.equals("");
    }

    public String Save() {
        try {
            ScriptLocal s = Ejb.lookupScriptBean().findByPrimaryKey(name);
            s.setDescription(description);
            s.setScript(text.getBytes());
        } catch (FinderException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String Create() {
        try {
            ScriptLocal s = Ejb.lookupScriptBean().create(name);
            s.setDescription(description);
            s.setScript(text.getBytes());
        } catch (CreateException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String Delete() {
        try {
            Ejb.lookupScriptBean().findByPrimaryKey(name).remove();
        } catch (FinderException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EJBException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoveException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        name = text = description = null;
        return null;
    }
}
