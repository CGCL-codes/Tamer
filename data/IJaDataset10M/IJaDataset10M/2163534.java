package com.novocode.naf.gui;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.app.NAFException;
import com.novocode.naf.model.ModelMap;

/**
 * This interface is implemented by NGWidgets which can create a WindowInstance.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 6, 2004
 * @version $Id: IWindowInstanceWidget.java 294 2005-01-14 21:14:50 +0000 (Fri, 14 Jan 2005) szeiger $
 */
public interface IWindowInstanceWidget {

    public WindowInstance createWindowInstance(NAFApplication app, ModelMap models, WindowInstance parent) throws NAFException;
}
