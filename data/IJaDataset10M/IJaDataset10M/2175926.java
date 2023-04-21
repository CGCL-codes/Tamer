package purej.service.router;

/**
 * ���� ����� ���丮
 * 
 * @author Administrator
 * 
 */
public class ServiceRouterFactory {

    public ServiceRouter getServiceRouter() {
        return new ServiceRouterImpl();
    }
}
