package net.ko.creator;

import java.io.File;
import java.util.ArrayList;
import net.ko.utils.KApplication;
import net.ko.utils.KString;
import net.ko.utils.KTemplateFile;

public class KTestClassesCreator {

    private KTemplateFile tplFile;

    private String templateFolder;

    private String classNameTest;

    private String kernelPackage;

    private ArrayList<String> tableNames;

    private String host;

    private String user;

    private String password;

    private String base;

    public String getList(String tableName) {
        KTemplateFile tplList = new KTemplateFile();
        tplList.open(KApplication.getRootPath(KClassCreator.class) + "/" + this.templateFolder + "/testlists.tpl");
        tplList.parseWith("className", "K" + KString.capitalizeFirstLetter(tableName));
        tplList.parseWith("listName", tableName + "s");
        return tplList.getText();
    }

    public String getLists(ArrayList<String> tableNames) {
        String ret = "";
        for (String tableName : tableNames) {
            ret += getList(tableName);
        }
        return ret;
    }

    public String getTemplateFolder() {
        return templateFolder;
    }

    public void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder;
        tplFile.open(KApplication.getRootPath(KClassCreator.class) + "/" + this.templateFolder + "/testclasses.tpl");
    }

    public KTestClassesCreator(String classNameTest, String kernelPackage, String templateFolder) {
        tplFile = new KTemplateFile();
        tableNames = new ArrayList<String>();
        this.classNameTest = classNameTest;
        this.kernelPackage = kernelPackage;
        setTemplateFolder(templateFolder);
    }

    public void addTable(String table) {
        tableNames.add(table);
    }

    public void setTables(KListClasses classes) {
        this.tableNames.clear();
        for (KClassCreator cls : classes.getClasses()) {
            this.tableNames.add(cls.getTableName());
        }
    }

    public void setTable(KClassCreator cls) {
        this.tableNames.clear();
        this.tableNames.add(cls.getTableName());
    }

    public void mkTpl() {
        tplFile.parseWith("packageTest", kernelPackage + ".tests");
        tplFile.parseWith("kernelPackage", kernelPackage);
        tplFile.parseWith("classNameTest", classNameTest);
        tplFile.parseWith("testLists", getLists(tableNames));
        tplFile.parseWith("host", host);
        tplFile.parseWith("user", user);
        tplFile.parseWith("password", password);
        tplFile.parseWith("base", base);
    }

    public void save() {
        String path = KApplication.getRootPath(KClassCreator.class);
        File f = new File(path);
        path = f.getPath().replaceAll(f.getName(), "");
        save(path);
    }

    public void save(String path) {
        tplFile.saveAs(path, kernelPackage + ".tests", classNameTest);
    }

    public void setDbParams(String host, String user, String password, String base) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.base = base;
    }
}
