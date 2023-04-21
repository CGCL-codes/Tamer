package com.loribel.java.impl;

import java.util.ArrayList;
import java.util.List;
import com.loribel.java.abstraction.GB_JavaMethod;
import com.loribel.java.abstraction.GB_JavaMethods;

/**
 * Default implementation of {@link GB_JavaMethods}.
 */
public class GB_JavaMethodsImpl implements GB_JavaMethods {

    List methods = new ArrayList();

    public void add(GB_JavaMethod a_method) {
        methods.add(a_method);
    }

    public int getSize() {
        return methods.size();
    }

    public GB_JavaMethod[] getAll() {
        return (GB_JavaMethod[]) methods.toArray(new GB_JavaMethod[0]);
    }

    public GB_JavaMethod[] getByName(String a_name) {
        List retour = new ArrayList();
        int len = methods.size();
        GB_JavaMethod l_method;
        for (int i = 0; i < len; i++) {
            l_method = (GB_JavaMethod) methods.get(i);
            if (l_method.getName().equals(a_name)) {
                retour.add(l_method);
            }
        }
        return (GB_JavaMethod[]) retour.toArray(new GB_JavaMethod[0]);
    }
}
