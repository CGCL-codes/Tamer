package ag.ion.noa.filter;

import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.bion.officelayer.filter.IFilter;

/**
 * Filter for the OpenOffice.org 1.0 Template format.
 * 
 * @author Andreas Br�ker
 * @version $Revision: 11619 $
 * @date 09.07.2006
 */
public class OpenOfficeTemplateFilter extends AbstractFilter implements IFilter {

    /** Filter for the OpenOffice.org 1.0 Template format. */
    public static final IFilter FILTER = new OpenOfficeTemplateFilter();

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
            return "writer_StarOffice_XML_Writer_Template";
        } else if (documentType.equals(IDocument.WEB)) {
            return "writer_web_StarOffice_XML_Writer_Web_Template";
        } else if (documentType.equals(IDocument.CALC)) {
            return "calc_StarOffice_XML_Calc_Template";
        } else if (documentType.equals(IDocument.DRAW)) {
            return "draw_StarOffice_XML_Draw_Template";
        } else if (documentType.equals(IDocument.IMPRESS)) {
            return "impress_StarOffice_XML_Impress_Template";
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
            return "stw";
        } else if (documentType.equals(IDocument.WEB)) {
            return "stw";
        } else if (documentType.equals(IDocument.CALC)) {
            return "stc";
        } else if (documentType.equals(IDocument.DRAW)) {
            return "std";
        } else if (documentType.equals(IDocument.IMPRESS)) {
            return "sti";
        }
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
        if (documentType.equals(IDocument.WRITER)) {
            return "OpenOffice.org 1.0 Template Textdocument";
        } else if (documentType.equals(IDocument.WEB)) {
            return "OpenOffice.org 1.0 Template Web";
        } else if (documentType.equals(IDocument.CALC)) {
            return "OpenOffice.org 1.0 Template Spreadsheet";
        } else if (documentType.equals(IDocument.DRAW)) {
            return "OpenOffice.org 1.0 Template Drawing";
        } else if (documentType.equals(IDocument.IMPRESS)) {
            return "OpenOffice.org 1.0 Template Presentation";
        }
        return null;
    }
}
