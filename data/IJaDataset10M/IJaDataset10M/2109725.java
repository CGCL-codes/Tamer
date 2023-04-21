package org.paquitosoft.lml.model.facade;

/**
 *  This class is used to create instances of library public facade.
 * 
 * @author paquitosoft
 */
public class LMLFacadeFactory {

    /**
     * This method returns a brand new instance of LML facade.
     * 
     * @return LML facade
     */
    public static ILMLFacade createLMLFacade() {
        return new LMLFacadeImpl();
    }
}
