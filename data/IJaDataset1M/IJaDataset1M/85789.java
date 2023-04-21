package ag.ion.noa.filter;

import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.bion.officelayer.filter.IFilter;

/**
 * Filter for the StarWriter 4.0 format.
 * 
 * @author Andreas Br�ker
 * @version $Revision: 11619 $
 * @date 09.07.2006
 */
public class StarOffice40Filter extends AbstractFilter implements IFilter {

    /** Global filter for StarWriter 4.0. */
    public static final IFilter FILTER = new StarOffice40Filter();

    /**
	 * Returns definition of the filter. Returns null if the filter is not
	 * available for the submitted document type.
	 * 
	 * @param documentType
	 *            document type to be used
	 * 
	 * @return definition of the filter or null if the filter is not available
	 *         for the submitted document type
	 * 
	 * @author Markus Kr�ger
	 * @date 13.03.2008
	 */
    public String getFilterDefinition(String documentType) {
        if (documentType.equals(IDocument.WRITER)) {
            return "StarWriter 4.0";
        } else if (documentType.equals(IDocument.GLOBAL)) {
            return "StarWriter 4.0/GlobalDocument";
        } else if (documentType.equals(IDocument.WEB)) {
            return "StarWriter 4.0 (StarWriter/Web)";
        } else if (documentType.equals(IDocument.CALC)) {
            return "StarCalc 4.0";
        } else if (documentType.equals(IDocument.IMPRESS)) {
            return "StarImpress 4.0";
        } else if (documentType.equals(IDocument.MATH)) {
            return "StarMath 4.0";
        }
        return null;
    }

    /**
	 * Returns file extension of the filter. Returns null if the document type
	 * is not supported by the filter.
	 * 
	 * @param documentType
	 *            document type to be used
	 * 
	 * @return file extension of the filter
	 * 
	 * @author Markus Kr�ger
	 * @date 03.04.2007
	 */
    public String getFileExtension(String documentType) {
        if (documentType == null) return null;
        if (documentType.equals(IDocument.WRITER)) {
            return "sdw";
        } else if (documentType.equals(IDocument.GLOBAL)) {
            return "sgl";
        } else if (documentType.equals(IDocument.WEB)) {
            return "html";
        } else if (documentType.equals(IDocument.CALC)) {
            return "sdc";
        } else if (documentType.equals(IDocument.IMPRESS)) {
            return "sdd";
        } else if (documentType.equals(IDocument.MATH)) {
            return "smf";
        }
        return null;
    }
}
