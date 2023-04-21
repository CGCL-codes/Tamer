package net.sourceforge.myvd.chain;

import java.util.ArrayList;
import java.util.HashMap;
import net.sourceforge.myvd.core.InsertChain;
import net.sourceforge.myvd.inserts.Insert;
import net.sourceforge.myvd.router.Router;
import net.sourceforge.myvd.types.DistinguishedName;
import net.sourceforge.myvd.types.Password;
import com.novell.ldap.LDAPConstraints;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;

public class ModifyInterceptorChain extends InterceptorChain {

    public ModifyInterceptorChain(DistinguishedName dn, Password pass, int startPos, InsertChain chain, HashMap<Object, Object> session, HashMap<Object, Object> request, Router router) {
        super(dn, pass, startPos, chain, session, request, router);
    }

    public ModifyInterceptorChain(DistinguishedName dn, Password pass, int startPos, InsertChain chain, HashMap<Object, Object> session, HashMap<Object, Object> request) {
        super(dn, pass, startPos, chain, session, request);
    }

    public void nextModify(DistinguishedName dn, ArrayList<LDAPModification> mods, LDAPConstraints constraints) throws LDAPException {
        Insert next = this.getNext();
        if (next != null) {
            next.modify(this, dn, mods, constraints);
        } else {
            if (router != null) {
                router.modify(this, dn, mods, constraints);
            }
        }
    }
}
