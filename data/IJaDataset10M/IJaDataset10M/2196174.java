package com.c4j.cmdtool;

import com.c4j.CMDToolComposition;

/**
 * A simple class that contains a main entry point to start an application.
 */
public final class CMDTool {

    /**
     * A private empty constructor to avoid instantiation of this class.
     */
    private CMDTool() {
    }

    /**
     * The main entry point of an application. Constructs a fragment owning a main facet and calls
     * the main method of one of these facets as specified in the assembly.
     *
     * @param args
     *         the command line arguments given to the application.
     */
    public static void main(final String[] args) {
        final CMDToolComposition application = CMDToolComposition.create("Application");
        final int result = application.getFacet_main().get().main(args);
        if (result != 0) System.exit(result);
    }
}
