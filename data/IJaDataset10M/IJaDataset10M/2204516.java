package com.mockturtlesolutions.snifflib.extensions;

import java.io.*;
import java.net.URLClassLoader;
import java.net.URL;
import java.util.zip.*;
import java.util.Vector;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Date;
import java.util.Set;
import java.util.Enumeration;

public class ModuleList extends AbstractModuleList {

    public ModuleList() {
        super();
    }

    /**
	Returns a Vector of the module names.  These should be the fully qualified URLs
	*/
    public Vector getModuleNames() {
        Set names = this.modules.keySet();
        Iterator iter = names.iterator();
        Vector out = new Vector();
        while (iter.hasNext()) {
            out.add((String) iter.next());
        }
        return (out);
    }

    public void addModule(String modulename) {
        if (this.modules.containsKey(modulename)) {
            throw new RuntimeException("Already have a class file for add-on module " + modulename + ".  Check your add-on jar files for dubplicate classes or noming conflicts or contact the developer of the module/add-on.");
        }
        this.modules.put(modulename, new DefaultModuleItem());
    }

    public void removeModule(String modulename) {
        this.modules.remove(modulename);
    }

    public String getAuthor(String modulename) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        return (mod.getAuthor());
    }

    public String getDescription(String modulename) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        return (mod.getDescription());
    }

    public String getVersion(String modulename) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        return (mod.getVersion());
    }

    public String getLastModified(String modulename) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        return (mod.getLastModified());
    }

    public String getCopyRight(String modulename) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        return (mod.getCopyRight());
    }

    public String getShortName(String modulename) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        return (mod.getShortName());
    }

    public void setDescription(String modulename, String x) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        mod.setDescription(x);
    }

    public void setAuthor(String modulename, String x) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        mod.setAuthor(x);
    }

    public void setVersion(String modulename, String x) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        mod.setVersion(x);
    }

    public void setLastModified(String modulename, String x) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        mod.setLastModified(x);
    }

    public void setCopyRight(String modulename, String x) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        mod.setCopyRight(x);
    }

    public void setShortName(String modulename, String x) {
        ModuleItem mod = (ModuleItem) this.modules.get(modulename);
        mod.setShortName(x);
    }
}
