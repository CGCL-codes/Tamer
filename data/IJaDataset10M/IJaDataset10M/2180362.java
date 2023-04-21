package org.mftech.dawn.codegen.util;

import org.eclipse.gmf.gmfgraph.Canvas;

public class Utils {

    public static String packagePath = "";

    public static String packageName = "";

    public static String diagramPluginID = "";

    public static String diagramPackage = "";

    public static String timestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getPackagePath() {
        return packagePath;
    }

    public static String getPackageName() {
        return packageName;
    }

    public static String getDiagramPluginID() {
        return diagramPluginID;
    }

    public static String setGlobals(Canvas e) {
        return "";
    }

    public static String setPackage(String pName) {
        diagramPluginID = pName + ".diagram";
        packageName = pName.replace("class", "clazz");
        packagePath = pName.replace(".", "/");
        return "";
    }

    public static String setDiagramPackage(String pName) {
        diagramPackage = pName;
        return "";
    }

    public static String getDiagramPackage() {
        return diagramPackage;
    }

    public static String getRootPackage() {
        String ret = diagramPackage.replace(".diagram", "");
        System.out.println("ROOT PAKAGE: " + ret);
        return ret;
    }

    public static String getDiagramPackagePath() {
        return diagramPackage.replace(".", "/");
    }

    public static String getUniqueIdentifierName(String uniqueIdentifier) {
        String ret = uniqueIdentifier.substring(uniqueIdentifier.lastIndexOf(".") + 1, uniqueIdentifier.length());
        return ret;
    }

    public static String toModelName(String uniqueIdentifier) {
        String ret = uniqueIdentifier.substring(uniqueIdentifier.lastIndexOf(".") + 1, uniqueIdentifier.length());
        ret = ret.substring(0, ret.lastIndexOf("_"));
        return ret;
    }

    public static String toModelNameFromGetterName(String uniqueIdentifier) {
        String ret = uniqueIdentifier.replace("getFigure", "");
        return ret;
    }
}
