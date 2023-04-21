package oxygen.tool.wlevs;

import java.util.List;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.QueryExp;
import oxygen.tool.facade.trees.JMXFacadeTreeModel;
import oxygen.tool.facade.trees.JMXUtils;

/**
 * 2 trees will be presented here for wlevs: services and applications.
 * services (ServerRuntime, ConfigSession, AppDeployment)
*/
public class WLEvSServicesFacadeTreeModel extends JMXFacadeTreeModel {

    private static String domain = "com.bea.wlevs";

    private static QueryExp kidsQueryExp = new MyQueryExp();

    public WLEvSServicesFacadeTreeModel(MBeanServerConnection mbs0, String name0) throws Exception {
        super(mbs0, name0);
        root = null;
    }

    public String getSeparator() {
        return "/";
    }

    public Object[] getChildren(Object parent) {
        try {
            if (parent != null) {
                return new Object[0];
            }
            ObjectName oname = new ObjectName(domain + ":*");
            Set names = JMXUtils.getAllRegistered(mbs, oname, kidsQueryExp);
            return names.toArray();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public Object getParent(Object child) throws Exception {
        return null;
    }

    public Object getChild(Object parent, String pathname) throws Exception {
        Object child = null;
        if (parent == null) {
            ObjectName oname = new ObjectName(domain + ":Type=" + pathname + ",*");
            child = JMXUtils.checkRegistered(mbs, oname, kidsQueryExp);
        }
        return child;
    }

    public String toPathName(Object child) throws Exception {
        ObjectName oname = (ObjectName) child;
        return (oname == null ? (null) : oname.getKeyProperty("Type"));
    }

    private static class MyQueryExp implements QueryExp {

        public boolean apply(ObjectName oname) {
            List l = JMXUtils.getKeys(oname);
            int lsize = l.size();
            if (lsize == 0 || lsize > 2 || (lsize == 2 && !l.contains("Type")) || (lsize == 2 && !l.contains("Name")) || (lsize == 1 && !l.contains("Type")) || ("Application".equals(oname.getKeyProperty("Type")))) {
                return false;
            }
            return true;
        }

        public void setMBeanServer(MBeanServer mbs) {
        }
    }
}
