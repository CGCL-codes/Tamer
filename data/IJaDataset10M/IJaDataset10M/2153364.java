package org.sbfc.converter.sbml2xpp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.sbfc.api.GeneralConverter;
import org.sbfc.util.ConverterRegistry;
import org.sbfc.util.ProgramOptions;

public class Activator implements BundleActivator {

    private GeneralConverter converter;

    private String command = "sbml2xpp";

    private ConverterRegistry registry;

    public void start(BundleContext context) throws Exception {
        converter = new SBML2XPP();
        context.registerService(GeneralConverter.class.getName(), converter, null);
        registry = (ConverterRegistry) context.getService(context.getServiceReference(ConverterRegistry.class.getName()));
        registry.registerConverter(converter, command);
        ProgramOptions options = (ProgramOptions) context.getService(context.getServiceReference(ProgramOptions.class.getName()));
        options.getOptions().put(command + "_cmdoptions", "");
        options.getOptions().put(command + "_desc", "Converts a SBML file into XPP format.");
    }

    public void stop(BundleContext context) throws Exception {
        registry.unregisterConverter(command);
    }
}
