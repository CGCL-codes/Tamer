package uk.ac.ebi.intact.dataexchange.imex;

import uk.ac.ebi.intact.dataexchange.psimi.xml.converter.ExportProfile;
import uk.ac.ebi.intact.dataexchange.psimi.xml.converter.ConverterContext;

/**
 * Provides default configuration for exporting data to IMEx partners.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 0.1
 */
public class ImexExportProfile implements ExportProfile {

    public void configure(ConverterContext context) {
        context.setGenerateExpandedXml(true);
        context.getInteractorConfig().setExcludeInteractorAliases(true);
        context.getInteractorConfig().setExcludePolymerSequence(true);
    }
}
