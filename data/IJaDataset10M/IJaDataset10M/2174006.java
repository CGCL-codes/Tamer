package org.fluidityproject.example.controller;

import java.util.ArrayList;
import java.util.List;
import org.fluidityproject.js.BaseJSController;

/**
 * An example Javascript controller. Any javascript files registered in the
 * getJsFiles method will be compiled together and served by this controller
 * whenever a request comes to the js folder with a file name of "compiledjs.js"
 * 
 * You can modify what the js folder is by overriding the getJsRoot method. By
 * default it is the "js/" relative to you app root.
 * 
 * You can modity what the file name is by overriding the getJsFileName method;
 * 
 * @author itaylor
 * 
 */
public class ExampleJSController extends BaseJSController {

    private static final long serialVersionUID = 1L;

    @Override
    public List<String> getJsFiles() {
        ArrayList<String> jsFiles = new ArrayList<String>();
        jsFiles.add("prototype.js");
        jsFiles.add("AjaxExt.js");
        jsFiles.add("$N.js");
        jsFiles.add("fluidityRequester.js");
        return jsFiles;
    }
}
