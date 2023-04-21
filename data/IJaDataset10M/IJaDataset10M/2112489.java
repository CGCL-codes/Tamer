package com.openbravo.pos.base;

import net.xeoh.plugins.base.PluginManager;
import com.openbravo.data.loader.I_Session;

/**
 *
 * @author adrianromero
 */
public abstract class BeanFactoryDataSingle implements BeanFactoryApp {

    /** Creates a new instance of BeanFactoryData */
    public BeanFactoryDataSingle() {
    }

    public abstract void init(I_Session s, PluginManager pmf);

    public void init(AppView app) throws BeanFactoryException {
        init(app.getSession(), app.getPluginManager());
    }

    public Object getBean() {
        return this;
    }
}
