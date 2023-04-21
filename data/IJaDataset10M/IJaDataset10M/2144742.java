package ua.orion.cpu.core.employees.services;

import org.apache.tapestry5.ioc.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.employees.EmployeeSymbols;

/**
 *
 * @author molodec
 */
public class EmployeesIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(EmployeeSymbols.EMPLOYEES_LIB, "ua.orion.cpu.core.employees"));
    }
}
