package com.i3sp.www.config;

import com.i3sp.jndi.ldap.BeanTranslator;
import com.i3sp.jndi.ldap.BeanTranslatorData;
import java.util.ArrayList;
import java.util.HashMap;

public class VirtualServerConfigFactory extends BeanTranslator {

    private static BeanTranslatorData data;

    public static ArrayList getObjectClasses() {
        ArrayList ocs = new ArrayList();
        ocs.add("top");
        ocs.add("comi3SPVirtualServerConfig");
        return ocs;
    }

    public static HashMap getRequired() {
        HashMap required = new HashMap();
        required.put("name", "cn");
        return required;
    }

    public static HashMap getOptional() {
        HashMap optional = new HashMap();
        return optional;
    }

    public static ArrayList getMultivalued() {
        ArrayList multi = new ArrayList();
        return multi;
    }

    static {
        data = new BeanTranslatorData(getRequired(), getOptional());
        data.setMultivalued(getMultivalued());
        data.setObjectClasses(getObjectClasses());
        data.setClass_(com.i3sp.www.config.VirtualServerConfig.class);
    }

    public VirtualServerConfigFactory() {
        super(data);
    }
}
