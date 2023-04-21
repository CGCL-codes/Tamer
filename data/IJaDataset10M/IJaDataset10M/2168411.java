package ua.orion.cpu.core.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.OrionCPUSymbols;

public class OrionCpuIOCModule {

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(OrionCPUSymbols.TEST_DATA, "false");
    }

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo("OrionCpu", "ua.orion.cpu.core"));
    }
}
