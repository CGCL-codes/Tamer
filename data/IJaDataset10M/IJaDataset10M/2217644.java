package org.openacs.web;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.context.FacesContext;
import org.openacs.ConfigurationLocal;
import org.openacs.ConfigurationPK;
import org.openacs.HardwareModelLocal;
import org.openacs.utils.Ejb;
import org.openacs.vendors.Vendor;

/**
 *
 * @author Administrator
 */
public class ConfigJsfBean extends JsfBeanBase {

    /** Creates a new instance of ConfigJsfBean */
    public ConfigJsfBean() {
        Map<String, String> rpm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String hwid = rpm.get("hwid");
        String name = rpm.get("cfgname");
        if (hwid != null) {
            this.hwid = Integer.parseInt(hwid);
        }
        this.name = name;
        if (hwid != null && name != null) {
            try {
                ConfigurationLocal s = Ejb.lookupConfigurationBean().findByPrimaryKey(new ConfigurationPK(this.hwid, name));
                this.filename = s.getFilename();
                this.config = new String(s.getConfig());
                this.name = s.getName();
                this.version = s.getVersion();
            } catch (FinderException ex) {
            }
        }
    }

    private Object[] array = null;

    public Object[] getAll() throws FinderException {
        if (array != null) {
            return array;
        } else {
            return array = Ejb.lookupConfigurationBean().findByHwid(this.hwid).toArray();
        }
    }

    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String config;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer hwid;

    public Integer getHwid() {
        return hwid;
    }

    public void setHwid(Integer hwid) {
        this.hwid = hwid;
    }

    public boolean isNew() {
        return (name == null || name.equals(""));
    }

    public String Save() {
        try {
            ConfigurationLocal s = Ejb.lookupConfigurationBean().findByPrimaryKey(new ConfigurationPK(hwid, name));
            HardwareModelLocal hw = s.getHardware();
            Vendor v = Vendor.getVendor(hw.getOui(), hw.getHclass(), hw.getVersion());
            String[] r = v.CheckConfig(filename, name, version, config);
            if (r.length > 0) {
                setErrorMessage(r);
            }
            s.setFilename(filename);
            s.setVersion(version);
            s.setConfig(config.getBytes());
        } catch (FinderException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String Create() {
        try {
            ConfigurationLocal s = Ejb.lookupConfigurationBean().create(hwid, name);
            s.setFilename(filename);
            s.setVersion(version);
            s.setConfig(config.getBytes());
        } catch (CreateException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String Delete() {
        try {
            Ejb.lookupConfigurationBean().findByPrimaryKey(new ConfigurationPK(hwid, name)).remove();
        } catch (FinderException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EJBException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoveException ex) {
            Logger.getLogger(ScriptJsfBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        name = filename = version = null;
        return null;
    }
}
