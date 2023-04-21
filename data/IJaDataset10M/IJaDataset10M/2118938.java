package resources;

import lib.GestionExcepciones;
import com.rational.test.ft.object.interfaces.*;
import com.rational.test.ft.object.interfaces.SAP.*;
import com.rational.test.ft.object.interfaces.siebel.*;
import com.rational.test.ft.script.*;
import com.rational.test.ft.vp.IFtVerificationPoint;

/**
 * Script Name   : <b>Popup</b><br>
 * Generated     : <b>2007/10/14 19:22:36</b><br>
 * Description   : Helper class for script<br>
 * Original Host : Windows XP x86 5.1 build 2600 Service Pack 2 <br>
 * 
 * @since  octubre 14, 2007
 * @author SE01687
 */
public abstract class PopupHelper extends lib.GestionExcepciones {

    /**
	 * htmlBrowser: with default state.
	 *		.class : Html.HtmlBrowser
	 * 		.browserName : MS Internet Explorer
	 */
    protected BrowserTestObject browser_htmlBrowser() {
        return new BrowserTestObject(getMappedTestObject("browser_htmlBrowser"));
    }

    /**
	 * htmlBrowser: with specific test context and state.
	 *		.class : Html.HtmlBrowser
	 * 		.browserName : MS Internet Explorer
	 */
    protected BrowserTestObject browser_htmlBrowser(TestObject anchor, long flags) {
        return new BrowserTestObject(getMappedTestObject("browser_htmlBrowser"), anchor, flags);
    }

    /**
	 * Back: with default state.
	 *		.class : Html.HtmlBrowser.ToolbarButton
	 * 		.name : Back
	 * 		.classIndex : 0
	 */
    protected GuiTestObject button_back() {
        return new GuiTestObject(getMappedTestObject("button_back"));
    }

    /**
	 * Back: with specific test context and state.
	 *		.class : Html.HtmlBrowser.ToolbarButton
	 * 		.name : Back
	 * 		.classIndex : 0
	 */
    protected GuiTestObject button_back(TestObject anchor, long flags) {
        return new GuiTestObject(getMappedTestObject("button_back"), anchor, flags);
    }

    /**
	 * _Popup: with default state.
	 *		.text : - Popup
	 * 		.id : 
	 * 		.href : file:///C:/bk/CruiseControl/projects/gwanted/checkout/gwanted/build/www/samples/ ...
	 * 		.title : 
	 * 		.class : Html.A
	 * 		.name : 
	 * 		.classIndex : 20
	 */
    protected GuiTestObject link__Popup() {
        return new GuiTestObject(getMappedTestObject("link__Popup"));
    }

    /**
	 * _Popup: with specific test context and state.
	 *		.text : - Popup
	 * 		.id : 
	 * 		.href : file:///C:/bk/CruiseControl/projects/gwanted/checkout/gwanted/build/www/samples/ ...
	 * 		.title : 
	 * 		.class : Html.A
	 * 		.name : 
	 * 		.classIndex : 20
	 */
    protected GuiTestObject link__Popup(TestObject anchor, long flags) {
        return new GuiTestObject(getMappedTestObject("link__Popup"), anchor, flags);
    }

    protected PopupHelper() {
        setScriptName("Popup");
    }
}
