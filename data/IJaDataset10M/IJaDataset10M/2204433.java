package uk.ac.ebi.intact.core.persister.util;

import uk.ac.ebi.intact.context.IntactContext;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: PersisterConfig.java 8692 2007-06-15 17:02:01Z baranda $
 */
public class PersisterConfig {

    private static final String DRY_RUN_ATT = PersisterConfig.class + ".DRY_RUN";

    private PersisterConfig() {
    }

    public static boolean isDryRun(IntactContext context) {
        return (Boolean) context.getSession().getAttribute(DRY_RUN_ATT);
    }

    public static void setDryRun(IntactContext context, boolean isDryRun) {
        context.getSession().setAttribute(DRY_RUN_ATT, isDryRun);
    }
}
