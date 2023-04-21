package ua.orion.cpu.core.orgunits.services;

import org.apache.tapestry5.ioc.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.orgunits.OrgUnitsSymbols;

public class OrgUnitsIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(OrgUnitsSymbols.ORG_UNITS_LIB, "ua.orion.cpu.core.orgunits"));
    }
}
