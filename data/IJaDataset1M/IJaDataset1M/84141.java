package org.hmaciel.rph.test;

import java.util.LinkedList;
import java.util.List;
import org.hmaciel.rph.ejb.utils.ENClass;
import org.hmaciel.rph.helper.FieldConstant;
import org.hmaciel.rph.helper.persisters.LdapENPersister;

/**
 * @author pclavijo
 * @deprecated
 */
public class pruebaENPersister {

    /**
	 * @author pclavijo
	 * @deprecated
	 */
    public static void main(String[] args) {
        LdapENPersister ldapPersister = LdapENPersister.getInstance();
        ENClass en = new ENClass();
        List<String> use = new LinkedList<String>();
        use.add("Pablo");
        use.add("Andres");
        use.add("Clavijo");
        en.setUse(use);
        ldapPersister.persist(en, FieldConstant.DN_BASE, null, 2);
    }
}
