package ag.ion.noa.filter;

import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.bion.officelayer.filter.IFilter;

/**
 * Abstract base filter.
 * 
 * @author Andreas Br�ker
 * @version $Revision: 11619 $
 * @date 09.07.2006
 */
public abstract class AbstractFilter implements IFilter {

    /**
	 * Returns definition of the filter. Returns null if the filter is not
	 * available for the submitted document.
	 * 
	 * @param document
	 *            document to be exported
	 * 
	 * @return definition of the filter or null if the filter is not available
	 *         for the submitted document
	 */
    public final String getFilterDefinition(IDocument document) {
        if (document != null) return getFilterDefinition(document.getDocumentType());
        return null;
    }

    /**
	 * Returns information whether the submitted document is supported by the
	 * filter.
	 * 
	 * @param document
	 *            document to be used
	 * 
	 * @return information whether the submitted document is supported by the
	 *         filter
	 * 
	 * @author Andreas Br�ker
	 * @date 08.07.2006
	 */
    public final boolean isSupported(IDocument document) {
        if (document != null) return isSupported(document.getDocumentType());
        return false;
    }

    /**
	 * Returns information whether the submitted document type is supported by
	 * the filter.
	 * 
	 * @param documentType
	 *            document type to be used
	 * 
	 * @return information whether the submitted document type is supported by
	 *         the filter
	 * 
	 * @author Markus Kr�ger
	 * @date 13.03.2008
	 */
    public final boolean isSupported(String documentType) {
        if (getFilterDefinition(documentType) == null) return false;
        return true;
    }

    /**
	 * Returns information whether the filter constructs a document which can
	 * not be interpreted again.
	 * 
	 * @return information whether the filter constructs a document which can
	 *         not be interpreted again
	 * 
	 * @author Andreas Br�ker
	 * @date 08.07.2006
	 */
    public boolean isExternalFilter() {
        return false;
    }

    /**
	 * Returns file extension of the filter. Returns null if the document is not
	 * supported by the filter.
	 * 
	 * @param document
	 *            document to be used
	 * 
	 * @return file extension of the filter
	 * 
	 * @author Markus Kr�ger
	 * @date 03.04.2007
	 */
    public final String getFileExtension(IDocument document) {
        if (document != null) return getFileExtension(document.getDocumentType());
        return null;
    }

    /**
	 * Returns name of the filter. Returns null if the submitted document is not
	 * supported by the filter.
	 * 
	 * @param document
	 *            document to be used
	 * 
	 * @return name of the filter
	 * 
	 * @author Andreas Br�ker
	 * @date 14.07.2006
	 */
    public final String getName(IDocument document) {
        if (document != null) return getName(document.getDocumentType());
        return null;
    }

    /**
	 * Returns name of the filter. Returns null if the submitted document type
	 * is not supported by the filter.
	 * 
	 * @param documentType
	 *            document type to be used
	 * 
	 * @return name of the filter
	 * 
	 * @author Markus Kr�ger
	 * @date 13.03.2008
	 */
    public String getName(String documentType) {
        return getFilterDefinition(documentType);
    }
}
