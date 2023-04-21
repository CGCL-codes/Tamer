package com.edujinilabs.dwt.client.tests.core;

import com.edujinilabs.dwt.client.core.RegExp;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Short description of CoreRegExpTest01.
 * 
 * For terms and usage, please see the LICENSE file provided alongwith or
 * contact <a
 * href='mailto:copyright@edujini-labs.com'>copyright@edujini-labs.com</a>
 * <br/> <a href='http://www.edujini-labs.com'>http://www.edujini-labs.com</a>
 * 
 * @author Gaurav Vaish
 * @author $Author: mastergaurav $
 * @copyright (C) 2008 - 2008, Edujini Labs Pvt. Ltd. (www.edujini-labs.com)
 * @svn.url $URL: http://dwt.svn.sourceforge.net/svnroot/dwt/trunk/dwt/eclipse/test/com/edujinilabs/dwt/client/tests/core/CoreRegExpTest01.java $
 * @version $Revision: 45 $
 */
public class CoreRegExpTest01 implements EntryPoint {

    public void onModuleLoad() {
        Element div = DOM.createDiv();
        addText(div, "RegExp Tests...");
        addBr(div);
        RegExp e = RegExp.getInstance("^on[A-Z]");
        addText(div, "Result of testing: " + e.test("onClick"));
        addBr(div);
        e = RegExp.getInstance("^on([A-Z].+)");
        addText(div, "Result of executing: " + e.exec("onClick"));
        addBr(div);
        RootPanel.getBodyElement().appendChild(div);
    }

    public void addBr(Element e) {
        e.appendChild(DOM.createElement("br"));
    }

    public native void addText(Element element, String text);
}
