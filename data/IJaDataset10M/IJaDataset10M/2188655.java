package uk.ac.ebi.intact;

import uk.ac.ebi.intact.context.IntactContext;

/**
 * TODO comment this
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
public class PlaygroundJpa {

    public static void main(String[] args) throws Exception {
        IntactContext context = IntactContext.getCurrentInstance();
        System.out.println(context.getDataContext().getDaoFactory().getInstitutionDao().getAll());
        System.out.println(context.getDataContext().getDaoFactory().getDbInfoDao().getAll());
    }
}
