package org.sucri.floxs.ext;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Jul 19, 2007
 * Time: 8:59:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JsComponent extends JsFunction {

    public String construct();

    String getVarName();

    void setVarName(String s);
}
