package org.judo.generate.mvc;

import java.util.ArrayList;

public class ViewResult extends Context {

    public ParseResults parse;

    public String contollerName;

    public String actionName;

    public String pageTemplate = "default.template";

    public String html = "";

    public ArrayList<String> listContextVars = new ArrayList<String>();

    public ArrayList<FormInfo> forms = new ArrayList<FormInfo>();

    public ArrayList<DatatableInfo> dataTables = new ArrayList<DatatableInfo>();

    public ArrayList<String> methodAnnotations = new ArrayList<String>();

    public ArrayList<String> imports = new ArrayList<String>();

    public String title;

    public boolean genConroller = true;

    public String scope = "request";

    public String path;

    public String menuName;

    public String menuDisplay;
}
