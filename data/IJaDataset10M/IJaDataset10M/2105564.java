package actions;

import org.apache.ibatis.services.*;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.BeansException;
import com.opensymphony.xwork2.Preparable;

/**
 * Provide a base class for Actions that utilize a ServiceClient.
 * <p/>
 * The service to invoke is passed as a parameter
 * and then obtained from spring.
 * The Action passes in a data transfer object with any parameters,
 * and executes the service.
 * Any outcome of the service is saved back to the data transfer object.
 */
public class ServiceAction extends Support implements BeanNameAware, BeanFactoryAware, Preparable {

    public String input() throws Exception {
        run(getSelectService(), getData());
        return input;
    }

    public String execute() throws Exception {
        run(getService(), getData());
        return SUCCESS;
    }

    public void prepare() throws Exception {
        run(getPrepareService(), getData());
    }

    private String beanName;

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    private ServiceConfig config;

    public ServiceConfig getConfig() {
        return config;
    }

    public void setConfig(ServiceConfig config) {
        this.config = config;
    }

    private BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    protected void run(Service service, ServiceData data) throws Exception {
        if (service != null) service.execute(data);
    }

    protected Object getBean(String id) {
        Object object;
        try {
            object = getBeanFactory().getBean(id);
        } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {
            object = null;
        }
        return object;
    }

    private String getSuffix(String id) {
        if (id.endsWith("List")) return "List";
        if (id.endsWith("Save")) return "Save";
        if (id.endsWith("Select")) return "Select";
        return null;
    }

    protected ServiceClient tryBaseService(String id) {
        String suffix = getSuffix(id);
        if (suffix == null) return null;
        boolean isStatement;
        if (suffix.equals("Save")) {
            String prefix = id.substring(0, id.length() - suffix.length());
            isStatement = config.isStatement(prefix + ServiceClient.insert_suffix);
            isStatement = isStatement && config.isStatement(prefix + ServiceClient.update_suffix);
        } else isStatement = config.isStatement(id);
        if (!isStatement) return null;
        ServiceClient baseService = (ServiceClient) getBean("Service" + suffix);
        baseService.setBeanName(id);
        return baseService;
    }

    public ServiceClient getServiceClient(String id) {
        ServiceClient service = (ServiceClient) getBean(id);
        if (service == null) service = tryBaseService(id);
        return service;
    }

    private ServiceClient prepareService;

    public ServiceDataMapper getPrepareService() {
        if (prepareService == null) {
            String id = getService_id() + ServiceClient.prepare_suffix;
            prepareService = getServiceClient(id);
        }
        return prepareService;
    }

    public void setPrepareService(ServiceClient prepareService) {
        this.prepareService = prepareService;
    }

    private String service_id;

    public String getService_id() {
        if (service_id == null) {
            String bean = getBeanName();
            service_id = bean.substring(1 + bean.lastIndexOf("."));
        }
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    private ServiceClient service;

    public ServiceClient getService() {
        if (service == null) service = getServiceClient(getService_id());
        return service;
    }

    public void setService(ServiceClient service) {
        this.service = service;
    }

    private ServiceClient selectService;

    public ServiceClient getSelectService() {
        if (selectService == null) {
            String id = getService_id() + ServiceClient.select_suffix;
            selectService = getServiceClient(id);
        }
        return selectService;
    }

    public void setSelectService(ServiceClient service) {
        this.selectService = service;
    }
}
