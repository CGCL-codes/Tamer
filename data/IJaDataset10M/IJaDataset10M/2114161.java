package com.google.gwt.gwtai.applet.client;

import java.util.HashMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * GWT-side utility class.
 * 
 * @author Adrian Buerki <a.buerki@gmail.com>
 */
public class AppletJSUtil {

    static {
        CallbackUtil.defineBridgeMethod();
    }

    /**
	 * Constructs an <code>HTML</code> element which contains an applet tag. The HTML specification
	 * states that the <code>applet</code> tag is deprecated. But the browser support for the <code>object</code>
	 * and <code>embed</code> tag is currently inconsistent. So using the <code>applet</code> tag is the only 
	 * consistent way to deploy a Java Applet across browsers on all platforms.
	 *
	 * @param applet The <code>Applet</code> to take the information from.
	 * @return An <code>HTML</code> element which contains an applet tag.
	 */
    public static Widget createAppletWidget(Applet applet) {
        String htmlCode = createAppletHTML(applet);
        if (htmlCode != null) {
            return new HTML(htmlCode);
        }
        return null;
    }

    /**
	 * Constructs an applet tag. The HTML specification states that the <code>applet</code> tag is deprecated.
	 * But the browser support for the <code>object</code> and <code>embed</code> tag is currently
	 * inconsistent. So using the <code>applet</code> tag is the only consistent way to deploy a Java Applet
	 * across browsers on all platforms.
	 *
	 * @param applet The <code>Applet</code> to take the information from.
	 * @return The applet tag.
	 */
    public static String createAppletHTML(Applet applet) {
        if (applet instanceof AppletAccomplice) {
            AppletAccomplice aapplet = (AppletAccomplice) applet;
            String htmlCode = "<applet mayscript='true'" + " code='" + aapplet.getCode() + "' width='" + aapplet.getWidth() + "' height='" + aapplet.getHeight() + "' name='" + aapplet.getName() + "' id='" + aapplet.getName() + "' alt='Java Runtime Environment is not working on your system'";
            if (null != aapplet.getCodebase()) {
                htmlCode += "codebase='" + aapplet.getCodebase() + "'";
            }
            if (null != aapplet.getArchive()) {
                htmlCode += "archive='" + aapplet.getArchive() + "'";
            }
            if (null != aapplet.getAlign()) {
                htmlCode += "align='" + aapplet.getAlign() + "'";
            }
            htmlCode += ">";
            HashMap<String, String> parameters = aapplet.getParameters();
            if (parameters != null && !parameters.isEmpty()) {
                for (String name : parameters.keySet()) {
                    htmlCode += createParamTag(name, parameters.get(name));
                }
            }
            htmlCode += createParamTag("java_version", aapplet.getJavaVersion());
            htmlCode += createParamTag("java_arguments", aapplet.getJavaArguments());
            htmlCode += createParamTag("separate_jvm", aapplet.hasSeparateJVM());
            htmlCode += createParamTag("image", aapplet.getLoadingImage());
            htmlCode += createParamTag("applet_name", aapplet.getName());
            htmlCode += "</applet>";
            return htmlCode;
        }
        return null;
    }

    public static Widget createAppletWidget(Applet applet, String forceJavaVersion) {
        if (applet instanceof AppletAccomplice) {
            AppletAccomplice aapplet = (AppletAccomplice) applet;
            String codebase = GWT.getModuleBaseURL();
            String htmlCode = "<p style='text-align: center;'><script src='http://java.com/js/deployJava.js'></script>" + "<script>deployJava.runApplet({codebase:'" + codebase + "', ";
            if (null != aapplet.getArchive()) {
                htmlCode += "archive:'" + aapplet.getArchive() + "', ";
            }
            htmlCode += "code:'" + aapplet.getCode() + "', width:'" + aapplet.getWidth() + "', Height:'" + aapplet.getHeight() + "'}, null, '" + forceJavaVersion + "');" + "</script></p>";
            return new HTML(htmlCode);
        }
        return null;
    }

    /**
	 * Registers an instance of your <code>AppletCallback</code>
	 * implementation to listen for callbacks coming from the given
	 * <code>Applet</code>. The actual <code>Applet</code> implementation
	 * can check whether an <code>AppletCallback</code> handler is installed,
	 * if so it can notify the given <code>AppletCallback</code> instance
	 * about updates or events.
	 * 
	 * Example:
	 * 
	 * First, register a listening <code>AppletCallback</code> object:
	 * <code>AppletJSUtil.registerAppletCallback(stopWatchApplet,
	 * new StopWatchCallback(panelLaps));</code>
	 * 
	 * Then callback to GWT from your <code>Applet</code> implementation
	 * (Java):
	 * <code>AppletUtil.callback(StopWatchAppletImpl.this, _swLabel.getText());</code>
	 * 
	 * @param applet -
	 *            The <code>Applet</code> instance to listen to.
	 * @param appletCallback -
	 *            The <code>AppletCallback</code> instance to notify once a
	 *            callback is coming.
	 */
    public static void registerAppletCallback(Applet applet, AppletCallback<? extends Object> appletCallback) {
        if (applet instanceof AppletAccomplice) {
            AppletAccomplice aapplet = (AppletAccomplice) applet;
            CallbackUtil.registerCallback(aapplet.getName(), appletCallback);
        }
    }

    /**
	 * Calls the method with the given name on the <code>Applet</code>
	 * instance.
	 * 
	 * @param applet -
	 *            The <code>Applet</code> instance to call the method on.
	 * @param methodName -
	 *            The name of the method to call.
	 */
    public static void call(Applet applet, String methodName) {
        if (applet instanceof AppletAccomplice) {
            String id = ((AppletAccomplice) applet).getId();
            Element elem = DOM.getElementById(id);
            call(elem, methodName);
        }
    }

    /**
	 * Calls the method with the given name on the <code>Applet</code>
	 * instance.
	 * 
	 * @param applet -
	 *            The <code>Applet</code> instance to call the method on.
	 * @param methodName -
	 *            The name of the method to call.
	 * @param args -
	 *            The method arguments.
	 */
    public static void call(Applet applet, String methodName, Object[] args) {
        if (applet instanceof AppletAccomplice) {
            String id = ((AppletAccomplice) applet).getId();
            Element elem = DOM.getElementById(id);
            call(elem, methodName, args);
        }
    }

    /**
	 * Helper-method to create a param tag.
	 */
    private static String createParamTag(String name, Object value) {
        if (value != null) {
            return "<param name='" + name + "' value='" + value + "'>";
        }
        return "";
    }

    /**
	 * Helper-method to do the actual calling.
	 */
    private static native Object call(Element elem, String methodName);

    /**
	 * Helper-method to do the actual calling.
	 */
    private static native Object call(Element elem, String methodName, Object args);
}
