package org.axis2m.spring.conf;

import org.axis2m.spring.Axis2MSpringTestBase;
import org.axis2m.spring.beans.OperationBean;
import org.axis2m.spring.beans.ParameterBean;
import org.axis2m.spring.beans.ServiceBean;

public abstract class AxisNamespaceHandlerTest extends Axis2MSpringTestBase {

    ServiceBean service;

    public void testAxisNamespaceHandlerForService() {
        assertNotNull("service object can not be null", service);
        assertEquals("Service name should be - testServiceName ", "testServiceName", service.getServiceName());
        assertEquals("type  should be - oxm ", "oxm", service.getType());
        assertEquals("tns should be - http://my.com ", "http://my.com", service.getTargetNameSpace());
        assertEquals("Session should be ", "application", service.getSessionScope());
        assertEquals("description should be - testService description", "testService description", service.getServiceDescription());
        assertNotNull("serviceBean object can not be null", service.getServiceBean());
        assertEquals("parameter count  should be 2 ", 2, service.getParametersList().size());
        assertEquals("1st parameter name  should be - paraA ", "paraA", ((ParameterBean) service.getParametersList().get(0)).getName());
        assertEquals("1st parameter value  should be - valueA ", "ValueA", ((ParameterBean) service.getParametersList().get(0)).getValue());
        assertEquals("2nd parameter name  should be - paraB ", "paraB", ((ParameterBean) service.getParametersList().get(1)).getName());
        assertEquals("2nd parameter value  should be - valueB ", "ValueB", ((ParameterBean) service.getParametersList().get(1)).getValue());
        assertEquals("moduleA,ModuleB", service.getModulesStr());
        assertEquals("Operations count  should be 2 ", 2, service.getOperations().size());
        assertEquals("1st Operations name  should be - op1 ", "op1", ((OperationBean) service.getOperations().get(0)).getName());
        assertEquals("2nd Operations name  should be - op2 ", "op2", ((OperationBean) service.getOperations().get(1)).getName());
        assertEquals("excludeOperationsStr should same as op3,op4", "op3,op4", service.getExcludeOperationsStr());
        assertNotNull(service.getMessageReceivers());
        assertEquals("MessageReceivers name should same", "testServiceName", service.getServiceName());
        System.out.println(service);
        System.out.println(" Service Name  " + service.getServiceName());
        System.out.println(" type " + service.getType());
        System.out.println(" tns  " + service.getTargetNameSpace());
        System.out.println(" session  " + service.getSessionScope());
        System.out.println(" serviceBean " + service.getServiceBean().getClass().getName());
        System.out.println(" para 1  " + service.getParametersList().get(0).toString());
        System.out.println(" para 2  " + service.getParametersList().get(1).toString());
        System.out.println(" modulesStr " + service.getModulesStr());
        System.out.println(" opearations 1 " + service.getOperations().get(0).getName());
        System.out.println(" opearations 2 " + service.getOperations().get(1).getNamespace());
        System.out.println(" excludeOperationsStr " + service.getExcludeOperationsStr());
        System.out.println(" parseMessageReceiver " + service.getMessageReceivers().get(0).getClazz());
        System.out.println(" Description " + service.getServiceDescription());
        System.out.println(" Description " + service.getServiceDescription());
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }
}
