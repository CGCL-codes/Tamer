package person.jws.ldap.exp;

import java.util.Enumeration;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import person.jws.ldap.ContextManager;
import person.jws.ldap.PropManager;

public class Exp {

    private static DirContext ctx = new ContextManager().getContext();

    private static SearchControls constraints = null;

    private static String basedn = "ou=��-�����׽�Ʈ��,ou=��Ÿ,ou=�����,dc=republic,dc=military,dc=mil";

    private static String filter = "objectclass=*";

    static {
        basedn = PropManager.getProperty("basedn");
        filter = PropManager.getProperty("filter");
        constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
    }

    public static void main(String args[]) {
        String filename = "workbook.xls";
        int rowcnt = 0;
        if (args.length != 0) {
            filename = args[0];
            filename = filename.replaceAll("\\", "/");
        }
        System.out.println("\"" + basedn + "\" is exporting now ");
        try {
            EntityHandler et = new EntityHandler(new ExpExcelHandler(filename));
            NamingEnumeration n = ctx.search(basedn, filter, constraints);
            while (n != null && n.hasMore()) {
                SearchResult sr = (SearchResult) n.next();
                LdapEntity le = new LdapEntity(sr.getName() + "," + basedn);
                Attributes attrs = sr.getAttributes();
                for (NamingEnumeration ne = attrs.getAll(); ne.hasMoreElements(); ) {
                    Attribute attr = (Attribute) ne.next();
                    String attrID = attr.getID();
                    StringBuffer sbAttribute = new StringBuffer();
                    for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                        Object o = vals.nextElement();
                        if (o instanceof String) {
                            sbAttribute.append(o).append("\n");
                        } else {
                            sbAttribute.append("<object>").append("\n");
                        }
                    }
                    le.put(attrID, sbAttribute.toString());
                }
                et.storeEntity(le);
                rowcnt++;
            }
            et.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(rowcnt + " row exported.");
    }
}
