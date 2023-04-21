package net.sf.omd.core.internal;

import java.util.ArrayList;

/**
 * This class represents Local variable array.
 * @author SASIKALA
 *
 */
public class LocalVariableArray {

    ArrayList<Class<? extends Object>> localArrayClassList;

    public LocalVariableArray(String methodSign) {
        localArrayClassList = new ArrayList<Class<? extends Object>>();
        parseMethodDesc(methodSign);
    }

    private void parseMethodDesc(String methodSign) {
    }
}
