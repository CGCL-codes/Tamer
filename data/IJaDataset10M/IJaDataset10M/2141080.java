package mx4j.server;

import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

/**
 * Default implementation of the MBeanMetaData interface.
 *
 * @version $Revision: 1.3 $
 */
class MX4JMBeanMetaData implements MBeanMetaData {

    private Object mbean;

    private ClassLoader classloader;

    private ObjectInstance instance;

    private ObjectName name;

    private MBeanInfo info;

    private boolean dynamic;

    private boolean standard;

    private Class management;

    private MBeanInvoker invoker;

    public Object getMBean() {
        return mbean;
    }

    public void setMBean(Object mbean) {
        this.mbean = mbean;
    }

    public ClassLoader getClassLoader() {
        return classloader;
    }

    public void setClassLoader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public ObjectName getObjectName() {
        return name;
    }

    public void setObjectName(ObjectName name) {
        this.name = name;
    }

    public MBeanInfo getMBeanInfo() {
        return info;
    }

    public void setMBeanInfo(MBeanInfo info) {
        this.info = info;
    }

    public boolean isMBeanDynamic() {
        return dynamic;
    }

    public void setMBeanDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isMBeanStandard() {
        return standard;
    }

    public void setMBeanStandard(boolean standard) {
        this.standard = standard;
    }

    public Class getMBeanInterface() {
        return management;
    }

    public void setMBeanInterface(Class management) {
        this.management = management;
    }

    public MBeanInvoker getMBeanInvoker() {
        return invoker;
    }

    public void setMBeanInvoker(MBeanInvoker invoker) {
        this.invoker = invoker;
    }

    public ObjectInstance getObjectInstance() {
        if (instance == null) {
            instance = new ObjectInstance(getObjectName(), getMBeanInfo().getClassName());
            return instance;
        }
        if (isMBeanDynamic()) {
            String clsName = getMBeanInfo().getClassName();
            if (!instance.getClassName().equals(clsName)) instance = new ObjectInstance(getObjectName(), clsName);
        }
        return instance;
    }
}
