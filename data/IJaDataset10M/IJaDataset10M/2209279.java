package com.i3sp.address;

import com.i3sp.jndi.ldap.BeanTranslator;
import com.i3sp.jndi.ldap.BeanTranslatorData;
import java.util.ArrayList;
import java.util.HashMap;

public class AddressPreferencesFactory extends BeanTranslator {

    private static BeanTranslatorData data;

    public static ArrayList getObjectClasses() {
        ArrayList ocs = new ArrayList();
        ocs.add("top");
        ocs.add("comi3SPAddressPreferences");
        return ocs;
    }

    public static HashMap getRequired() {
        HashMap required = new HashMap();
        required.put("name", "cn");
        return required;
    }

    public static HashMap getOptional() {
        HashMap optional = new HashMap();
        optional.put("categories", "comi3SPCategories");
        return optional;
    }

    public static ArrayList getMultivalued() {
        ArrayList multi = new ArrayList();
        multi.add("comi3SPCategories");
        return multi;
    }

    static {
        data = new BeanTranslatorData(getRequired(), getOptional());
        data.setMultivalued(getMultivalued());
        data.setObjectClasses(getObjectClasses());
        data.setClass_(com.i3sp.address.AddressPreferences.class);
    }

    public AddressPreferencesFactory() {
        super(data);
    }
}
