package org.pyre.foundry.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import org.pyre.foundry.global.entity.Application;
import org.pyre.foundry.global.entity.ApplicationParameter;
import org.pyre.foundry.global.entity.SchemaSpace;

public class ApplicationConfigure {

    private List<ApplicationMenuDefine> menus;

    private SchemaSpace schemaSpace;

    private String name;

    private String version;

    private List<ApplicationParameter> parameters;

    private String fileName;

    private String webRoot;

    private String contextPath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMenus(List<ApplicationMenuDefine> menus) {
        this.menus = menus;
    }

    public List<ApplicationMenuDefine> getMenus() {
        return menus;
    }

    public void setSchemaSpace(SchemaSpace schemaSpace) {
        this.schemaSpace = schemaSpace;
    }

    public SchemaSpace getSchemaSpace() {
        return schemaSpace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setParameters(List<ApplicationParameter> parameters) {
        this.parameters = parameters;
    }

    public List<ApplicationParameter> getParameters() {
        return parameters;
    }

    public static ApplicationConfigure load(String fileName) throws JSONException, IOException {
        InputStream reader = new FileInputStream(fileName);
        ApplicationConfigure conf = load(reader);
        conf.fileName = fileName;
        return conf;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public static ApplicationConfigure load(InputStream inputStream) throws JSONException, IOException {
        JSON json = new JSON();
        ApplicationConfigure conf = json.parse(inputStream, ApplicationConfigure.class);
        return conf;
    }

    public Application toApplication() {
        Application app = new Application(getName(), fileName);
        app.setVersion(getVersion());
        return app;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return contextPath;
    }
}
