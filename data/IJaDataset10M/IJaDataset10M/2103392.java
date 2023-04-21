package br.com.srv.commons.enumeration;

import java.util.ArrayList;

public class EmptyEnumeration extends BaseEnumeration {

    private static EmptyEnumeration instance = null;

    public static synchronized EmptyEnumeration getInstance() {
        if (instance == null) {
            instance = new EmptyEnumeration();
        }
        return instance;
    }

    private EmptyEnumeration() {
        this.setData(generateData());
    }

    private ArrayList generateData() {
        return new ArrayList(0);
    }
}
