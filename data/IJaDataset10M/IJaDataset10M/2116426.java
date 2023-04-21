package oxygen.tool.wlfacade;

import java.util.Map;
import javax.naming.Binding;
import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import oxygen.tool.facade.FacadeBenignException;
import oxygen.util.OxyTable;

/**
 * This tree is weak
 * Whenever U go, always type the whole thing?
 */
public class WLJNDITree implements WLTree {

    public Binding cmo;

    public Binding prevcmo;

    private WLFacadeContextHelper wlctx;

    private InitialContext ictx;

    public WLJNDITree(InitialContext _ictx, WLFacadeContextHelper _wlctx) throws Exception {
        wlctx = _wlctx;
        ictx = _ictx;
    }

    public Object pointer() {
        return cmo;
    }

    public Object go(String stringrep) throws Exception {
        String s = stringrep;
        while (s.endsWith("/")) {
            s = s.substring(0, s.length() - 1);
        }
        if (s.startsWith("//")) {
            while (s.startsWith("/")) {
                s = s.substring(1);
            }
            s = "/" + s;
        }
        if (cmo != null) {
            if (s.equals("..")) {
                CompositeName cn = new CompositeName(cmo.getName());
                s = (cn.getPrefix(cn.size() - 1)).toString();
            } else {
                if (!(s.startsWith("/"))) {
                    s = cmo.getName() + "/" + s;
                }
            }
        }
        Object o = ictx.lookup(s);
        Binding b = new Binding(s, o);
        setPointer(b);
        return pointer();
    }

    public void setPointer(Object o) throws Exception {
        prevcmo = cmo;
        cmo = (Binding) o;
    }

    public OxyTable list(Map matchMap) throws Exception {
        String key = "";
        if (cmo != null) {
            key = cmo.getName();
        }
        String[] headers = new String[] { "Key", "Value" };
        Object[] row = new Object[headers.length];
        OxyTable tabl = new OxyTable(headers);
        try {
            NamingEnumeration enum0 = ictx.listBindings(key);
            while (enum0.hasMore()) {
                Binding b = (Binding) enum0.next();
                row[0] = b.getName();
                row[1] = b.getObject();
                tabl.addRow(row);
            }
            enum0.close();
        } catch (CannotProceedException cpe) {
        }
        tabl.sort();
        return tabl;
    }

    public OxyTable find(String stringrep) throws Exception {
        throw new FacadeBenignException("Find is not supported on JNDI trees");
    }

    public String getNodeStringRep(Object o) throws Exception {
        Binding b = (Binding) o;
        return b.getName();
    }

    public String getPrompt() throws Exception {
        String s = "[wl:jndi: ] > ";
        if (cmo != null) {
            s = "[wl:jndi: " + cmo.getName() + " > ";
        }
        return s;
    }
}
