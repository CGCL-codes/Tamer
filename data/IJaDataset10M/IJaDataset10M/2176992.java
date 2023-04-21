package alfresco.module.sword;

/**
 *
 * @author clayton
 */
public class AmsetServiceDocument implements IServiceDocument {

    IService service = new AmsetService();

    public IService getService() {
        return service;
    }

    public void setService(IService service) {
        this.service = service;
    }
}
