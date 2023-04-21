package com.freeture.frmwk.utils;

import java.util.StringTokenizer;

public class JavaTools {

    /**
	 * Renvoie le nom court (sans le nom de package) de la classe pour n'importe
     * quel objet
	 * @return java.lang.String
	 * @param o java.lang.Object
	 */
    public static final String getClassName(Object o) {
        String name = null;
        if (o != null) {
            String fullName = o.getClass().getName();
            StringTokenizer st = new StringTokenizer(fullName, ".");
            while (st.hasMoreTokens()) {
                name = st.nextToken();
            }
        }
        return name;
    }

    /**
	 * Renvoie le nom du package pour n'importe quel objet
	 * @return java.lang.String
	 * @param o java.lang.Object
	 */
    public static final String getPackageName(Object o) {
        String fullName = o.getClass().getName();
        return fullName.substring(0, fullName.indexOf(getClassName(o)));
    }
}
