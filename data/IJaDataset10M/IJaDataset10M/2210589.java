package sun.net.spi.nameservice;

public interface NameServiceDescriptor {

    /**
     * Create a new instance of the corresponding name service.
     */
    public NameService createNameService() throws Exception;

    /**
     * Returns this service provider's name
     *
     */
    public String getProviderName();

    /**
     * Returns this name service type
     * "dns" "nis" etc
     */
    public String getType();
}
