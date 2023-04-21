package uk.ac.ebi.intact.application.dataConversion.psiUpload.checker;

import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.CellTypeTag;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.HostOrganismTag;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.TissueTag;
import uk.ac.ebi.intact.model.BioSource;
import uk.ac.ebi.intact.util.BioSourceFactory;

/**
 * That class .
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: HostOrganismChecker.java 5204 2006-07-05 01:00:41Z baranda $
 */
public final class HostOrganismChecker extends AbstractOrganismChecker {

    public static BioSource getBioSource(final HostOrganismTag hostOrganism) {
        final String taxid = hostOrganism.getTaxId();
        final CellTypeTag cellType = hostOrganism.getCellType();
        final TissueTag tissue = hostOrganism.getTissue();
        return getBioSource(taxid, cellType, tissue);
    }

    public static void check(final HostOrganismTag hostOrganism, final BioSourceFactory bioSourceFactory) {
        final String taxid = hostOrganism.getTaxId();
        final CellTypeTag cellType = hostOrganism.getCellType();
        final TissueTag tissue = hostOrganism.getTissue();
        check(taxid, cellType, tissue, bioSourceFactory);
    }
}
