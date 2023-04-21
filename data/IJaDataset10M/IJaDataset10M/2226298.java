package org.opencms.editors.editarea;

import org.opencms.file.CmsResource;
import org.opencms.file.types.CmsResourceTypeJsp;
import org.opencms.file.types.CmsResourceTypeXmlContent;
import org.opencms.file.types.CmsResourceTypeXmlPage;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.editors.CmsSimpleEditor;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Provides helper methods for the usage of the EditArea editor that can be used
 * for syntax highlighting of text based files.<p>
 * 
 * @author Andreas Zahner
 * 
 * @version $Revision: 1.1 $ 
 * 
 * @since 7.0.5
 */
public class CmsEditArea extends CmsSimpleEditor {

    /** Syntax highlight type name: CSS. */
    public static final String HIGHLIGHT_TYPE_CSS = "css";

    /** Syntax highlight type name: HTML. */
    public static final String HIGHLIGHT_TYPE_HTML = "html";

    /** Syntax highlight type name: JavaScript. */
    public static final String HIGHLIGHT_TYPE_JAVASCRIPT = "js";

    /** Syntax highlight type name: JSP. */
    public static final String HIGHLIGHT_TYPE_JSP = "jsp";

    /** Syntax highlight type name: XML. */
    public static final String HIGHLIGHT_TYPE_XML = "xml";

    /** Possible type suffix names. */
    protected static final String[] HIGHLIGHT_TYPES = { HIGHLIGHT_TYPE_CSS, HIGHLIGHT_TYPE_HTML, HIGHLIGHT_TYPE_JAVASCRIPT, HIGHLIGHT_TYPE_JSP, HIGHLIGHT_TYPE_XML };

    /** Possible type suffix names as list. */
    protected static final List HIGHLIGHT_TYPES_LIST = Arrays.asList(HIGHLIGHT_TYPES);

    /** Sub path fragment to the editor resources. */
    protected static final String SUBPATH_EDITAREA = "editors/editarea/";

    /** Path to the editor resources. */
    protected static final String VFS_PATH_EDITOR = VFS_PATH_RESOURCES + SUBPATH_EDITAREA;

    /** Path to the editor distribution resources. */
    protected static final String VFS_PATH_EDITOR_DIST = VFS_PATH_EDITOR + "dist/";

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsEditArea(CmsJspActionElement jsp) {
        super(jsp);
    }

    /**
     * Returns the editor language to use according to the current users workplace settings.<p>
     * 
     * @return the editor language to use
     */
    public String getEditorLanguage() {
        String testLocale = getLocale().toString();
        if (getCms().existsResource(VFS_PATH_EDITOR_DIST + "langs/" + testLocale + ".js")) {
            return testLocale;
        }
        return Locale.ENGLISH.toString();
    }

    /**
     * @see org.opencms.workplace.editors.CmsSimpleEditor#getEditorResourceUri()
     */
    public String getEditorResourceUri() {
        return getSkinUri() + SUBPATH_EDITAREA;
    }

    /**
     * Returns the start syntax highlighting type for the currently edited resource.<p>
     * 
     * @return the start syntax highlighting type
     */
    public String getStartHighlight() {
        CmsResource resource = null;
        try {
            resource = getCms().readResource(getParamResource());
        } catch (CmsException e) {
        }
        if (resource != null) {
            int type = resource.getTypeId();
            if (type == CmsResourceTypeJsp.getStaticTypeId()) {
                return HIGHLIGHT_TYPE_JSP;
            }
            if (CmsResourceTypeXmlContent.isXmlContent(resource) || type == CmsResourceTypeXmlPage.getStaticTypeId()) {
                return HIGHLIGHT_TYPE_XML;
            }
            int dotIndex = getParamResource().lastIndexOf('.');
            if (dotIndex != -1) {
                String suffix = getParamResource().substring(dotIndex + 1);
                if (CmsStringUtil.isNotEmpty(suffix)) {
                    int typeIndex = HIGHLIGHT_TYPES_LIST.indexOf(suffix.toLowerCase());
                    if (typeIndex != -1) {
                        return (String) HIGHLIGHT_TYPES_LIST.get(typeIndex);
                    }
                }
            }
        }
        return HIGHLIGHT_TYPE_HTML;
    }
}
