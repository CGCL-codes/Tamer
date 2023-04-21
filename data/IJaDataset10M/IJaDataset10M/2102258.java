package org.easyrec.plugin.pearson.cli;

import org.easyrec.plugin.pearson.PearsonService;
import org.easyrec.utils.spring.cli.AbstractDependencyInjectionSpringCLI;

public class PearsonServiceCLI extends AbstractDependencyInjectionSpringCLI {

    public static void main(final String[] args) {
        final PearsonServiceCLI pearsonServiceCli = new PearsonServiceCLI();
        pearsonServiceCli.processCommandLineCall(args);
    }

    private PearsonService pearsonService;

    public PearsonService getPearsonService() {
        return pearsonService;
    }

    public void setPearsonService(final PearsonService pearsonService) {
        this.pearsonService = pearsonService;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "spring/content/pearson/AllInOne_Pearson.xml" };
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int processCommandLineCall(final String[] args) {
        if (args.length == 0) pearsonService.perform((Integer) null); else if (args[0].equals("-t") && args[1] != null) try {
            final int tenantId = Integer.parseInt(args[1]);
            pearsonService.perform(tenantId);
        } catch (final Exception e) {
            usage();
            return -2;
        } else {
            usage();
            return -1;
        }
        return 0;
    }

    @Override
    protected void usage() {
        System.out.println("Usage: java -...PearsonServiceCLI [OPTIONS]");
        System.out.println("Options:");
        System.out.println("   -t <TENANT>              specifiy a tenant to generate rules for;");
        System.out.println("                            by default rules for all tenants are generated");
    }
}
