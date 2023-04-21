package org.jeevang.hotwire.factory;

import org.jeevang.hotwire.exceptions.InitializationException;
import org.jeevang.hotwire.interfaces.VMController;
import org.jeevang.hotwire.log.Logger;
import org.jeevang.hotwire.system.Config;
import java.io.IOException;

/**
 * -------------------------------------------------------------------------------
 *- @(#) VMFactory.java    0.1 Oct 19, 2004                                             -
 *-                                                                              
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the SUN PUBLIC LICENSE
 * as published by Sun Microsystems.
 *
 * You should have recieved a copy of SUN PUBLIC LICENSE along with
 * this source code. If not, you can view it at www.sun.com
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *                           -
 *-                                                                              
 *-                                                                              
 *- @author Jeevan Gheevarghese Joseph
 *-                                                                              
 *-                                                                              
 *- Revision History                                                             
 *-                                                                              
 *- Version        Date            Author          Description                   
 *- ============================================================                 
 *- 0.1          Oct 19, 2004      Jeevan G Joseph       First draft             
 *-                                                                               
 *--------------------------------------------------------------------------------
 */
public class VMFactory {

    /**
     * Method loads the classname from a property file.
     * This allows us to instantiate an arbitrary implementation at runtime.
     * @return Any valid implementation for org.jeevang.hotwire.interfaces.VMController
     * @throws InitializationException
     */
    public static VMController getVMController() throws InitializationException {
        try {
            return (VMController) Class.forName(Config.getProperty("VMControllerClass")).newInstance();
        } catch (InstantiationException e) {
            Logger.getInstance().fine("InstantiationException when trying to create a VMController");
            throw new InitializationException("InstantiationException when trying to create a VMController", e);
        } catch (IllegalAccessException e) {
            Logger.getInstance().fine("IllegalAccessException when trying to create a VMController");
            throw new InitializationException("IllegalAccessException when trying to create a VMController", e);
        } catch (ClassNotFoundException e) {
            Logger.getInstance().fine("ClassNotFoundException when trying to create a VMController");
            throw new InitializationException("ClassNotFoundException when trying to create a VMController", e);
        } catch (IOException e) {
            Logger.getInstance().fine("IOException when trying to create a VMController");
            throw new InitializationException("IOException when trying to create a VMController", e);
        }
    }
}
