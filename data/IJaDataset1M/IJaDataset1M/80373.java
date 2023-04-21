package net.sf.opentranquera.services;

/**
 * @author Nicolas 
 */
public class TestServiceLocator {

    public static void main(String[] args) throws ServiceNotFoundException {
        BarService bar = (BarService) ServiceLocator.getInstance().getService(Services.BAR);
        System.out.println(bar);
        bar.fooMethod();
        Object errorService = ServiceLocator.getInstance().getService("noservice");
        System.out.println(errorService);
    }
}
