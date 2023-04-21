package org.jfree.report.modules.misc.tablemodel;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;

/**
 * The module definition for the table model utility classes module.
 *
 * @author Thomas Morgner
 */
public class TableModelModule extends AbstractModule {

    /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws ModuleInitializeException if an error occured.
   */
    public TableModelModule() throws ModuleInitializeException {
        loadModuleInfo();
    }

    /**
   * Initializes the module. Use this method to perform all initial setup operations. This
   * method is called only once in a modules lifetime. If the initializing cannot be
   * completed, throw a ModuleInitializeException to indicate the error,. The module will
   * not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
    public void initialize(SubSystem subSystem) throws ModuleInitializeException {
    }
}
