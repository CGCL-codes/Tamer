package org.gdal.osr;

public class osr implements osrConstants {

    public static int GetWellKnownGeogCSAsWKT(String name, String[] argout) {
        return osrJNI.GetWellKnownGeogCSAsWKT(name, argout);
    }

    public static int GetUserInputAsWKT(String name, String[] argout) {
        return osrJNI.GetUserInputAsWKT(name, argout);
    }

    public static String[] GetProjectionMethods() {
        return osrJNI.GetProjectionMethods();
    }

    public static String[] GetProjectionMethodParameterList(String method, String[] username) {
        return osrJNI.GetProjectionMethodParameterList(method, username);
    }

    public static void GetProjectionMethodParamInfo(String method, String param, String[] usrname, String[] type, SWIGTYPE_p_double defaultval) {
        osrJNI.GetProjectionMethodParamInfo(method, param, usrname, type, SWIGTYPE_p_double.getCPtr(defaultval));
    }
}
