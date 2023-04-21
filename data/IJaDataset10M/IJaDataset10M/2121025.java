package com.loribel.tools.ac;

import com.loribel.commons.business.GB_BOMetaDataFactoryTools;
import com.loribel.commons.exception.GB_LoadException;
import com.loribel.tools.ac.bo.generated.GB_ACBOInitializer;

/**
 * Initializer.
 *
 * @author Gr�gory Borelli
 */
public abstract class GB_ACInitializer {

    public static void initAll() throws GB_LoadException {
        initBOFactory();
        initOther();
    }

    private static void initBOFactory() throws GB_LoadException {
        GB_BOMetaDataFactoryTools.getFactory().addResource(GB_ACInitializer.class, AA.TOOLS_AC_BO_XML);
        GB_ACBOInitializer.initBOFactory();
        GB_ACBOInitializer.initBODataFactory();
    }

    private static void initOther() {
    }
}
