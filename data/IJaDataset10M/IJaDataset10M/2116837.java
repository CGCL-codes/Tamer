package org.riverock.module.web.context;

import org.riverock.module.web.dispatcher.ModuleRequestDispatcher;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:39:54
 *         $Id: ModuleContext.java,v 1.2 2006/06/05 19:19:10 serg_main Exp $
 */
public interface ModuleContext {

    public ModuleRequestDispatcher getRequestDispatcher(String url);
}
