package uncertain.ocm.mbean;

public interface ObjectRegistryImplWrapperMBean {

    public String getConstructorName(String class_name) throws ClassNotFoundException;

    public boolean canCreateInstance(String class_name) throws ClassNotFoundException;

    public int getInstanceMappingCount();
}
