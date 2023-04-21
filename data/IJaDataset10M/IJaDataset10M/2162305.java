package net.sourceforge.myvd.chain.jdbcLdapImpl;

import com.octetstring.jdbcLdap.backend.DirectoryRetrieveResults;
import com.octetstring.jdbcLdap.backend.DirectoryUpdate;
import com.octetstring.jdbcLdap.jndi.JndiLdapConnection;
import com.octetstring.jdbcLdap.jndi.SQLNamingException;
import com.octetstring.jdbcLdap.sql.statements.*;
import com.octetstring.jdbcLdap.sql.*;
import java.sql.*;
import java.util.ArrayList;
import net.sourceforge.myvd.chain.InterceptorChain;
import net.sourceforge.myvd.chain.ModifyInterceptorChain;
import net.sourceforge.myvd.types.DistinguishedName;
import com.novell.ldap.*;

/**
 *Contains logic for updating records in the directory
 *@author Marc Boorshtein, OctetString
 */
public class Update implements ChainedImpl, DirectoryUpdate {

    InterceptorChain chain;

    public int doUpdateJldap(JdbcLdapUpdate update) throws SQLException {
        DirectoryRetrieveResults res = (DirectoryRetrieveResults) update.getCon().getImplClasses().get(JndiLdapConnection.IMPL_RETRIEVE_RESULTS);
        LDAPEntry seres;
        StringBuffer buf = new StringBuffer();
        SqlStore store = update.getSqlStore();
        int count = 0;
        ArrayList<LDAPModification> mods;
        String[] fields, vals;
        mods = new ArrayList<LDAPModification>(store.getFields().length);
        fields = store.getFields();
        vals = update.getVals();
        String name;
        for (int i = 0, m = fields.length; i < m; i++) {
            mods.add(new LDAPModification(LDAPModification.REPLACE, new LDAPAttribute(fields[i].trim(), vals[i])));
        }
        try {
            if (update.getSearchScope() != 0) {
                LDAPSearchResults enumer = res.searchUpInsJldap(update);
                while (enumer.hasMore()) {
                    seres = enumer.next();
                    buf.setLength(0);
                    name = seres.getDN();
                    ModifyInterceptorChain modChain = this.chain.createModifyChain();
                    modChain.nextModify(new DistinguishedName(name), mods, new LDAPConstraints());
                    count++;
                }
            } else {
                ModifyInterceptorChain modChain = this.chain.createModifyChain();
                modChain.nextModify(new DistinguishedName(update.getBaseContext()), mods, new LDAPConstraints());
                count++;
            }
            return count;
        } catch (LDAPException ne) {
            throw new SQLNamingException(ne);
        }
    }

    public InterceptorChain getChain() {
        return chain;
    }

    public void setChain(InterceptorChain chain) {
        this.chain = chain;
    }
}
