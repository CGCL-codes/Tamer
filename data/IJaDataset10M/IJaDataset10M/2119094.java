package becta.viewer.filemanagement;

import iwb.IWBBridge;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import becta.viewer.accessibility.AccessibilityColor;
import becta.viewer.drawing.BaseElement;
import becta.viewer.drawing.ElementColour;
import becta.viewer.drawing.ElementStroke;
import becta.viewer.drawing.FlashElement;
import becta.viewer.drawing.Group;
import becta.viewer.drawing.ImageElement;
import becta.viewer.drawing.Link;
import becta.viewer.drawing.LinkableElement;
import becta.viewer.drawing.MovieElement;
import becta.viewer.drawing.OvalElement;
import becta.viewer.drawing.ParagrapghLayout;
import becta.viewer.drawing.PolylineElement;
import becta.viewer.drawing.RectangleElement;
import becta.viewer.drawing.StrokeLineShape;
import becta.viewer.drawing.TextElement;
import becta.viewer.drawing.TextLinkPoint;
import becta.viewer.framework.Viewer;

/**
 * This class holds the document for the viewer
 */
public class Document {

    /**
	 * Holds the viewport of document
	 */
    private Rectangle2D viewport;

    /**
	 * Holds the viewbox of document
	 */
    private Rectangle2D viewbox;

    /**
	 * Holds the index of page showing
	 */
    private int activePageIndex = 0;

    /**
	 * Holds the list of pages in document
	 */
    private ArrayList<Page> pages;

    /**
	 * Will be true in case of any changes in the content
	 */
    private boolean isDirty = false;

    /**
	 * default viewport or not
	 */
    private boolean isDefaultViewport = false;

    /**
	 * Internal class to the character and style index to create the  
	 * collection of styles
	 */
    class Cursor {

        int index;

        /**
		 * Character index
		 */
        int charIndex;
    }

    /**
	 * Class to keep each style 
	 */
    class Style implements Cloneable {

        String text;

        boolean paragraph;

        int text_align;

        int font;

        int fill;

        int backcolour;

        int underlineStyle;

        boolean list;

        int textliststyle;

        int listfill;

        int listtype;

        int liststyletype;

        ArrayList<Style> styles;

        int start;

        int end;

        Style parent;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /**
	 * Converts iwb font style to java font style
	 * @return
	 */
    private static Float convertFontStyleValue(int style) {
        return style == IWBBridge.IWB_FONT_STYLE_NORMAL ? TextAttribute.POSTURE_REGULAR : TextAttribute.POSTURE_OBLIQUE;
    }

    /**
	 * Converts iwb font stretch to java font stretch
	 * @return
	 */
    private static Float convertFontStretchValue(int style) {
        switch(style) {
            case IWBBridge.IWB_FONT_STRETCH_ULTRA_CONDENSED:
            case IWBBridge.IWB_FONT_STRETCH_EXTRA_CONDENSED:
            case IWBBridge.IWB_FONT_STRETCH_CONDENSED:
            case IWBBridge.IWB_FONT_STRETCH_NARROWER:
                return TextAttribute.WIDTH_CONDENSED;
            case IWBBridge.IWB_FONT_STRETCH_SEMI_CONDENSED:
                return TextAttribute.WIDTH_SEMI_CONDENSED;
            case IWBBridge.IWB_FONT_STRETCH_SEMI_EXPANDED:
                return TextAttribute.WIDTH_SEMI_EXTENDED;
            case IWBBridge.IWB_FONT_STRETCH_ULTRA_EXPANDED:
            case IWBBridge.IWB_FONT_STRETCH_EXTRA_EXPANDED:
            case IWBBridge.IWB_FONT_STRETCH_EXPANDED:
            case IWBBridge.IWB_FONT_STRETCH_WIDER:
                return TextAttribute.WIDTH_EXTENDED;
            default:
                return TextAttribute.WIDTH_REGULAR;
        }
    }

    /**
	 * Converts iwb font weight to java font weight
	 * @return
	 */
    private static Float convertFontWeightValue(int weight) {
        switch(weight) {
            case IWBBridge.IWB_FONT_WEIGHT_UNDEFINED:
                return TextAttribute.WEIGHT_REGULAR;
            case IWBBridge.IWB_FONT_WEIGHT_NORMAL:
                return TextAttribute.WEIGHT_REGULAR;
            case IWBBridge.IWB_FONT_WEIGHT_BOLD:
                return TextAttribute.WEIGHT_BOLD;
            case IWBBridge.IWB_FONT_WEIGHT_BOLDER:
                return TextAttribute.WEIGHT_HEAVY;
            case IWBBridge.IWB_FONT_WEIGHT_LIGHTER:
                return TextAttribute.WEIGHT_LIGHT;
            case IWBBridge.IWB_FONT_WEIGHT_INHERIT:
                return TextAttribute.WEIGHT_REGULAR;
            case IWBBridge.IWB_FONT_WEIGHT_100:
                return TextAttribute.WEIGHT_EXTRA_LIGHT;
            case IWBBridge.IWB_FONT_WEIGHT_200:
                return TextAttribute.WEIGHT_LIGHT;
            case IWBBridge.IWB_FONT_WEIGHT_300:
                return TextAttribute.WEIGHT_DEMILIGHT;
            case IWBBridge.IWB_FONT_WEIGHT_400:
                return TextAttribute.WEIGHT_REGULAR;
            case IWBBridge.IWB_FONT_WEIGHT_500:
                return TextAttribute.WEIGHT_SEMIBOLD;
            case IWBBridge.IWB_FONT_WEIGHT_600:
                return TextAttribute.WEIGHT_LIGHT;
            case IWBBridge.IWB_FONT_WEIGHT_700:
                return TextAttribute.WEIGHT_DEMIBOLD;
            case IWBBridge.IWB_FONT_WEIGHT_800:
                return TextAttribute.WEIGHT_EXTRABOLD;
            case IWBBridge.IWB_FONT_WEIGHT_900:
                return TextAttribute.WEIGHT_ULTRABOLD;
            default:
                return TextAttribute.WEIGHT_REGULAR;
        }
    }

    private static StrokeLineShape getStrokeLineShape(int value) {
        switch(value) {
            case IWBBridge.IWB_LINESHAPE_NONE:
                return StrokeLineShape.NONE;
            case IWBBridge.IWB_LINESHAPE_ARROW:
                return StrokeLineShape.ARROW;
            case IWBBridge.IWB_LINESHAPE_CIRCLE:
                return StrokeLineShape.CIRCLE;
            case IWBBridge.IWB_LINESHAPE_LINE:
                return StrokeLineShape.LINE;
            default:
                return StrokeLineShape.NONE;
        }
    }

    /**
	 * Converts iwb line cap to equivalent java one
	 * @param linecap
	 * @return
	 */
    private static int getLineCap(int linecap, ElementStroke parent) {
        switch(linecap) {
            case IWBBridge.IWB_LINECAP_CIRCLE:
                return BasicStroke.CAP_ROUND;
            case IWBBridge.IWB_LINECAP_BUTT:
                return BasicStroke.CAP_BUTT;
            case IWBBridge.IWB_LINECAP_SQUARE:
                return BasicStroke.CAP_SQUARE;
            case IWBBridge.IWB_LINECAP_UNDEFINED:
            case IWBBridge.IWB_LINECAP_INHERIT:
                return parent != null ? parent.getEndCap() : BasicStroke.CAP_BUTT;
        }
        return BasicStroke.CAP_BUTT;
    }

    /**
	 * Converts iwb line join to equivalent java one
	 * @param linecap
	 * @return
	 */
    private static int getLineJoin(int linejoin, ElementStroke parent) {
        switch(linejoin) {
            case IWBBridge.IWB_LINEJOIN_BEVEL:
                return BasicStroke.JOIN_BEVEL;
            case IWBBridge.IWB_LINEJOIN_MITER:
                return BasicStroke.JOIN_MITER;
            case IWBBridge.IWB_LINEJOIN_ROUND:
                return BasicStroke.JOIN_ROUND;
            case IWBBridge.IWB_LINEJOIN_UNDEFINED:
            case IWBBridge.IWB_LINECAP_INHERIT:
                return parent != null ? parent.getLineJoin() : BasicStroke.JOIN_ROUND;
        }
        return BasicStroke.JOIN_ROUND;
    }

    /**
	 * To create a string from the string ID returned from the API
	 * @param id
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    private static String getString(int id) throws UnsupportedEncodingException {
        return new String(IWBBridge.IWB_StringGetUTF8(id), "UTF-8");
    }

    /**
	 * Converts the string to UTF-format
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    private static byte[] toUTF(String text) throws UnsupportedEncodingException {
        text += '\000';
        return text.getBytes("UTF-8");
    }

    /**
	 * Get the colour from the id
	 * @param id
	 * @param parentColour
	 * @return
	 */
    private static ElementColour getColor(int id, ElementColour parentColour) {
        ElementColour color;
        if (id == IWBBridge.IWB_NULL) color = parentColour; else {
            color = new ElementColour(parentColour, IWBBridge.IWB_ColourGetRed(id), IWBBridge.IWB_ColourGetGreen(id), IWBBridge.IWB_ColourGetBlue(id), IWBBridge.IWB_ColourGetAlpha(id));
        }
        return color;
    }

    /**
	 * Gets stroke
	 * @param id
	 * @param parentStroke
	 * @return
	 */
    private static ElementStroke getStroke(int id, ElementStroke parentStroke) {
        ElementStroke stroke = null;
        if (id == IWBBridge.IWB_NULL) stroke = parentStroke; else {
            int colourId = IWBBridge.IWB_StrokeGetColour(id);
            float width = IWBBridge.IWB_StrokeGetWidth(id);
            int linecap = IWBBridge.IWB_StrokeGetLineCap(id);
            int join = IWBBridge.IWB_StrokeGetLineJoin(id);
            int arrayId = IWBBridge.IWB_StrokeGetDashes(id);
            ElementColour c = getColor(colourId, parentStroke != null ? parentStroke.getStrokeColour() : null);
            if (width == IWBBridge.IWB_VALUE_UNDEFINED) width = parentStroke == null ? 1 : parentStroke.getLineWidth();
            float[] dashes = null;
            if (arrayId != IWBBridge.IWB_NULL) {
                dashes = new float[IWBBridge.IWB_FloatArrayGetCount(arrayId)];
                for (int i = 0; i < dashes.length; i++) {
                    dashes[i] = IWBBridge.IWB_FloatArrayGetAtIndex(arrayId, i);
                }
            } else if (parentStroke != null) dashes = parentStroke.getDashArray();
            stroke = new ElementStroke(c, width, getLineCap(linecap, parentStroke), getLineJoin(join, parentStroke), dashes);
        }
        return stroke;
    }

    /**
	 * Method to create the elements
	 * @param workingpath
	 * @param doc
	 * @param page
	 * @param parent
	 * @param pColour
	 * @param pStroke
	 * @param pTransforms
	 * @param list
	 * @param limit
	 * @throws Exception
	 */
    private static void createElements(String workingpath, Document doc, Page page, int parent, ElementColour pColour, ElementStroke pStroke, ArrayList<AffineTransform> pTransforms, List<BaseElement> list, int limit) throws Exception {
        int elementList = IWBBridge.IWB_ContainerGetChildren(parent);
        int elementCount = IWBBridge.IWB_ArrayGetCount(elementList);
        if (limit >= 0 && limit < elementCount) elementCount = limit;
        BaseElement element = null;
        for (int j = 0; j < elementCount; ++j) {
            int elementID = IWBBridge.IWB_ArrayGetAtIndex(elementList, j);
            switch(IWBBridge.IWB_ElementGetType(elementID)) {
                case IWBBridge.IWB_Oval_type_id:
                    element = doc.createOvalElement(elementID, pColour, pStroke, pTransforms);
                    break;
                case IWBBridge.IWB_Line_type_id:
                    element = doc.createLineElement(elementID, pColour, pStroke, pTransforms);
                    break;
                case IWBBridge.IWB_Rectangle_type_id:
                    element = doc.createRectangleElement(page, elementID, pColour, pStroke, pTransforms);
                    break;
                case IWBBridge.IWB_Polyline_type_id:
                    element = doc.createPolygonElement(elementID, pColour, pStroke, pTransforms);
                    break;
                case IWBBridge.IWB_Image_type_id:
                    element = doc.createImageElement(elementID, pColour, pTransforms);
                    break;
                case IWBBridge.IWB_Video_type_id:
                    element = doc.createMovieElement(elementID, pColour, pTransforms);
                    break;
                case IWBBridge.IWB_Flash_type_id:
                    element = doc.createFlashElement(elementID, pColour, pTransforms);
                    break;
                case IWBBridge.IWB_Switch_type_id:
                    {
                        createElements(workingpath, doc, page, elementID, pColour, pStroke, pTransforms, list, 1);
                        break;
                    }
                case IWBBridge.IWB_StyleGroup_type_id:
                    {
                        ElementColour gColor = getColor(IWBBridge.IWB_StyleGroupGetColour(elementID), pColour);
                        ElementStroke gStroke = getStroke(IWBBridge.IWB_StyleGroupGetStroke(elementID), pStroke);
                        ArrayList<AffineTransform> gTransforms = null;
                        if (IWBBridge.IWB_StyleGroupGetTransformations(elementID) != IWBBridge.IWB_NULL) {
                            gTransforms = new ArrayList<AffineTransform>();
                            int transListID = IWBBridge.IWB_StyleGroupGetTransformations(elementID);
                            if (pTransforms != null) gTransforms.addAll(pTransforms);
                            int transCount = IWBBridge.IWB_ArrayGetCount(transListID);
                            for (int i = 0; i < transCount; ++i) {
                                int transID = IWBBridge.IWB_ArrayGetAtIndex(transListID, i);
                                int transType = IWBBridge.IWB_TransformationGetType(transID);
                                if (transType == IWBBridge.IWB_TRANSFORM_ROTATION) {
                                    AffineTransform trans = AffineTransform.getRotateInstance(Math.toRadians(IWBBridge.IWB_TransformationGetRotation(transID)), IWBBridge.IWB_TransformationGetRotationPointX(transID), IWBBridge.IWB_TransformationGetRotationPointY(transID));
                                    gTransforms.add(trans);
                                } else if (transType == IWBBridge.IWB_TRANSFORM_TRANSLATION) {
                                    AffineTransform trans = AffineTransform.getTranslateInstance(IWBBridge.IWB_TransformationGetDeltaX(transID), IWBBridge.IWB_TransformationGetDeltaY(transID));
                                    gTransforms.add(trans);
                                }
                            }
                        }
                        createElements(workingpath, doc, page, elementID, gColor, gStroke, gTransforms, list, -1);
                        break;
                    }
                case IWBBridge.IWB_Link_type_id:
                    {
                        String url = getString(IWBBridge.IWB_LinkGetURL(elementID));
                        int linkType = IWBBridge.IWB_LinkGetType(elementID);
                        ArrayList<BaseElement> children = new ArrayList<BaseElement>();
                        createElements(workingpath, doc, page, elementID, pColour, pStroke, pTransforms, children, -1);
                        boolean internal = linkType != IWBBridge.IWB_LINK_EXTERNAL;
                        if (internal && url.startsWith("audio/")) {
                            if (internal) url = workingpath + File.separator + url;
                            for (BaseElement baseElement : children) {
                                list.add(baseElement);
                                baseElement.setAudio(url);
                            }
                        } else {
                            for (BaseElement baseElement : children) {
                                list.add(baseElement);
                                if (baseElement instanceof LinkableElement) {
                                    ArrayList<Link> links = new ArrayList<Link>(1);
                                    Link link = new Link(url, baseElement.getShape(), internal);
                                    links.add(link);
                                    ((LinkableElement) baseElement).setLinks(links);
                                }
                            }
                        }
                        element = null;
                        break;
                    }
                case IWBBridge.IWB_TextArea_type_id:
                    element = doc.createTextElement(elementID, pColour, pTransforms, true);
                    break;
                case IWBBridge.IWB_TextBox_type_id:
                    element = doc.createTextElement(elementID, pColour, pTransforms, false);
                    break;
            }
            if (element != null) list.add(element);
        }
    }

    private static void checkStatus(int status, String error) throws Exception {
        if (status != IWBBridge.IWB_Success) {
            throw new Exception("File load error");
        }
    }

    /**
	 * Static method for loading a document
	 * This method will load the document and returns the instance of loaded document
	 * @param fileName File to be loaded
	 * @return instance of loaded document
	 */
    public static Document load(String workingpath, String fileName) throws Exception {
        if (fileName.startsWith("file://")) fileName = fileName.replaceFirst("file://", "");
        int status = IWBBridge.IWB_LibraryInitialize(IWBBridge.IWB_StringFromUTF8(toUTF(workingpath)));
        checkStatus(status, "Library initialization");
        status = IWBBridge.IWB_DocumentLoad(IWBBridge.IWB_StringFromUTF8(toUTF(fileName)));
        checkStatus(status, "Document load");
        int pageList = IWBBridge.IWB_GetPages();
        int pageCount = IWBBridge.IWB_ArrayGetCount(pageList);
        Document document = new Document();
        document.pages = new ArrayList<Page>(pageCount);
        int locationID = IWBBridge.IWB_ViewGetClientLocation();
        int sizeID = IWBBridge.IWB_ViewGetClientSize();
        document.viewbox = new Rectangle2D.Float(IWBBridge.IWB_PointGetX(locationID), IWBBridge.IWB_PointGetY(locationID), IWBBridge.IWB_SizeGetWidth(sizeID), IWBBridge.IWB_SizeGetHeight(sizeID));
        sizeID = IWBBridge.IWB_ViewGetPixelSize();
        document.viewport = sizeID == IWBBridge.IWB_NULL ? null : new Rectangle2D.Float(0, 0, IWBBridge.IWB_SizeGetWidth(sizeID), IWBBridge.IWB_SizeGetHeight(sizeID));
        Dimension desktopSize = Viewer.getViewer().getDesktop().getSize();
        if (document.viewport == null || document.viewport.getWidth() > desktopSize.getWidth() || document.viewport.getHeight() > desktopSize.getHeight()) {
            document.isDefaultViewport = true;
            document.viewport = new Rectangle(new Point(0, 0), document.calculateDefaultViewportSize());
        }
        for (int i = 0; i < pageCount; ++i) {
            int pageID = IWBBridge.IWB_ArrayGetAtIndex(pageList, i);
            Page page = new Page();
            int id = IWBBridge.IWB_ElementGetIdentifier(pageID);
            if (id != IWBBridge.IWB_NULL) {
                page.setIdentifier(getString(id));
            }
            int bgColour = IWBBridge.IWB_PageGetBackgroundColour(pageID);
            if (bgColour != IWBBridge.IWB_NULL) {
                page.setBackgroundColour(getColor(bgColour, null).getColour());
            }
            int bgImageGroupID = IWBBridge.IWB_PageGetBackgroundImage(pageID);
            if (bgImageGroupID != IWBBridge.IWB_NULL) {
                int imageList = IWBBridge.IWB_ContainerGetChildren(bgImageGroupID);
                if (imageList != IWBBridge.IWB_NULL) {
                    int imageCount = IWBBridge.IWB_ArrayGetCount(imageList);
                    if (imageCount > 0) {
                        ImageElement image = document.createImageElement(IWBBridge.IWB_ArrayGetAtIndex(imageList, 0), null, null);
                        int posture = IWBBridge.IWB_PageGetBackgroundImagePosture(pageID);
                        page.setBackgroundImage(image, posture);
                    }
                }
            }
            createElements(workingpath, document, page, pageID, null, null, null, page.getElements(), -1);
            int groupArray = IWBBridge.IWB_PageGetGroup(pageID);
            int groupCount = IWBBridge.IWB_ArrayGetCount(groupArray);
            for (int j = 0; j < groupCount; j++) {
                int groupID = IWBBridge.IWB_ArrayGetAtIndex(groupArray, j);
                int children = IWBBridge.IWB_GroupGetChildren(groupID);
                Group groupList = new Group(children);
                int elementCount = IWBBridge.IWB_ArrayGetCount(children);
                for (int k = 0; k < elementCount; k++) {
                    String elementID = getString(IWBBridge.IWB_ElementGetIdentifier(IWBBridge.IWB_ArrayGetAtIndex(children, k)));
                    for (BaseElement e : page.getElements()) {
                        if (elementID.equals(e.getIdentifier())) {
                            groupList.add(e);
                            e.setGroup(groupList);
                            break;
                        }
                    }
                }
            }
            document.pages.add(page);
        }
        checkStatus(IWBBridge.IWB_LibraryTerminate(), "Library terminated");
        return document;
    }

    /**
	 * Calculate the default viewport size considering available screen size and aspect ratio
	 * @return
	 */
    public Dimension calculateDefaultViewportSize() {
        Dimension viewportSize;
        double aspectRatio = viewbox.getWidth() / viewbox.getHeight();
        Dimension desktopSize = Viewer.getViewer().getDesktop().getSize();
        double height = desktopSize.width / aspectRatio;
        if (height <= desktopSize.height) {
            viewportSize = new Dimension(desktopSize.width, (int) height);
        } else {
            double width = desktopSize.height * aspectRatio;
            viewportSize = new Dimension((int) width, desktopSize.height);
        }
        return viewportSize;
    }

    private AffineTransform getViewPortTransform() {
        double xScale = (double) viewport.getWidth() / viewbox.getWidth();
        double yScale = (double) viewport.getHeight() / viewbox.getHeight();
        AffineTransform transform = AffineTransform.getScaleInstance(xScale, yScale);
        transform.concatenate(AffineTransform.getTranslateInstance(-viewbox.getX(), -viewbox.getY()));
        return transform;
    }

    public List<Page> getPages() {
        return pages;
    }

    /**
	 * Get the page currently shown
	 * @return
	 */
    public Page getCurrentPage() {
        return pages.get(activePageIndex);
    }

    /**
	 * Set the page to be shown
	 * @param pageno
	 */
    public void setActivePageIndex(int pageno) {
        activePageIndex = pageno - 1;
    }

    public int getActivePageIndex() {
        return activePageIndex + 1;
    }

    /**
	 * Get the number of pages in this document
	 * @return
	 */
    public int getPageCount() {
        return pages != null ? pages.size() : 0;
    }

    /**
	 * Returns if the document contains flash objects
	 * @return
	 */
    public boolean hasFlashContent() {
        for (int i = 0; i < getPageCount(); i++) {
            Page page = pages.get(i);
            if (page.hasFlash()) return true;
        }
        return false;
    }

    public void setDirty() {
        isDirty = true;
    }

    /**
	 * Used to know whether the document is modified or not
	 * @return Boolean value indicating whether the document is modified or not
	 */
    public boolean isDirty() {
        return isDirty;
    }

    /**
	 * Gets the viewport
	 * @return the view port
	 */
    public Rectangle2D getViewport() {
        return viewport;
    }

    /**
	 * Gets the viewbox
	 * @return the viewbox
	 */
    public Rectangle2D getViewbox() {
        return viewbox;
    }

    public boolean isDefaultViewport() {
        return isDefaultViewport;
    }

    /**
	 * Clear all annotations in the document
	 */
    public void clearAnnotations() {
        for (Page page : pages) {
            page.clearAnnotations();
        }
    }

    /**
	 * Clones current document
	 */
    @Override
    public Document clone() {
        Document clone = new Document();
        clone.viewport = new Rectangle2D.Double(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
        clone.viewbox = new Rectangle2D.Double(viewbox.getX(), viewbox.getY(), viewbox.getWidth(), viewbox.getHeight());
        clone.activePageIndex = activePageIndex;
        clone.isDefaultViewport = isDefaultViewport;
        clone.isDirty = isDirty;
        ArrayList<Page> clonedPages = new ArrayList<Page>(pages.size());
        for (Page page : pages) {
            clonedPages.add(page.clone());
        }
        clone.pages = clonedPages;
        return clone;
    }

    private RectangleElement createRectangleElement(Page page, int rectID, ElementColour fill, ElementStroke stroke, ArrayList<AffineTransform> transforms) throws UnsupportedEncodingException {
        RectangleElement element = new RectangleElement();
        if (IWBBridge.IWB_RectangleGetRevealer(rectID) == IWBBridge.IWB_TRUE && page.getRevealer() == null) {
            page.setRevealer(element);
            element.setArea(new Rectangle2D.Double(IWBBridge.IWB_ElementGetLocationX(rectID), IWBBridge.IWB_ElementGetLocationY(rectID), IWBBridge.IWB_ElementGetWidth(rectID), IWBBridge.IWB_ElementGetHeight(rectID)));
            element = null;
        } else {
            setElementProperties(element, fill, transforms, rectID);
            element.setStroke(getStroke(IWBBridge.IWB_ShapeGetStroke(rectID), stroke));
            element.setArea(new Rectangle2D.Double(IWBBridge.IWB_ElementGetLocationX(rectID), IWBBridge.IWB_ElementGetLocationY(rectID), IWBBridge.IWB_ElementGetWidth(rectID), IWBBridge.IWB_ElementGetHeight(rectID)));
        }
        return element;
    }

    private OvalElement createOvalElement(int ovalId, ElementColour fill, ElementStroke stroke, ArrayList<AffineTransform> transforms) throws UnsupportedEncodingException {
        OvalElement element = new OvalElement();
        setElementProperties(element, fill, transforms, ovalId);
        element.setArea(new Rectangle2D.Double(IWBBridge.IWB_ElementGetLocationX(ovalId), IWBBridge.IWB_ElementGetLocationY(ovalId), IWBBridge.IWB_ElementGetWidth(ovalId), IWBBridge.IWB_ElementGetHeight(ovalId)));
        element.setStroke(getStroke(IWBBridge.IWB_ShapeGetStroke(ovalId), stroke));
        return element;
    }

    private PolylineElement createLineElement(int id, ElementColour fill, ElementStroke stroke, ArrayList<AffineTransform> transforms) throws UnsupportedEncodingException {
        int startPt = IWBBridge.IWB_LineGetStart(id);
        int endPt = IWBBridge.IWB_LineGetEnd(id);
        Point2D startP = new Point2D.Double(IWBBridge.IWB_PointGetX(startPt), IWBBridge.IWB_PointGetY(startPt));
        Point2D endP = new Point2D.Double(IWBBridge.IWB_PointGetX(endPt), IWBBridge.IWB_PointGetY(endPt));
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        points.add(startP);
        points.add(endP);
        PolylineElement element = new PolylineElement(points, false);
        element.setStartLineShape(getStrokeLineShape(IWBBridge.IWB_LineGetStartShape(id)));
        element.setEndLineShape(getStrokeLineShape(IWBBridge.IWB_LineGetEndShape(id)));
        element.setStroke(getStroke(IWBBridge.IWB_ShapeGetStroke(id), stroke));
        setElementProperties(element, fill, transforms, id);
        return element;
    }

    private PolylineElement createPolygonElement(int id, ElementColour fill, ElementStroke stroke, ArrayList<AffineTransform> transforms) throws UnsupportedEncodingException {
        int pointsID = IWBBridge.IWB_PolylinePointsGet(id);
        int pointsCount = IWBBridge.IWB_ArrayGetCount(pointsID);
        ArrayList<Point2D> points = new ArrayList<Point2D>(pointsCount);
        for (int i = 0; i < pointsCount; ++i) {
            int pointID = IWBBridge.IWB_ArrayGetAtIndex(pointsID, i);
            points.add(new Point2D.Double(IWBBridge.IWB_PointGetX(pointID), IWBBridge.IWB_PointGetY(pointID)));
        }
        PolylineElement element = new PolylineElement(points, IWBBridge.IWB_PolylineIsClosed(id) == IWBBridge.IWB_TRISTATE_ON);
        element.setStartLineShape(getStrokeLineShape(IWBBridge.IWB_LineGetStartShape(id)));
        element.setEndLineShape(getStrokeLineShape(IWBBridge.IWB_LineGetEndShape(id)));
        setElementProperties(element, fill, transforms, id);
        element.setStroke(getStroke(IWBBridge.IWB_ShapeGetStroke(id), stroke));
        element.setHighlight(IWBBridge.IWB_PolylineIsHighlight(id) == IWBBridge.IWB_TRUE);
        return element;
    }

    private ImageElement createImageElement(int id, ElementColour fill, ArrayList<AffineTransform> transforms) throws UnsupportedEncodingException {
        int fileID = IWBBridge.IWB_MediaGetFile(id);
        String filename = getString(IWBBridge.IWB_FileGetPath(fileID));
        File imageFile = new File(filename);
        ImageElement element = new ImageElement(imageFile.getAbsolutePath(), IWBBridge.IWB_ElementGetLocationX(id), IWBBridge.IWB_ElementGetLocationY(id), IWBBridge.IWB_ElementGetWidth(id), IWBBridge.IWB_ElementGetHeight(id));
        setElementProperties(element, fill, transforms, id);
        element.setFlip(IWBBridge.IWB_ImageGetFlip(id));
        element.setOpacity(IWBBridge.IWB_ImageGetOpacity(id));
        return element;
    }

    private void setElementProperties(BaseElement element, ElementColour fill, ArrayList<AffineTransform> transforms, int id) throws UnsupportedEncodingException {
        element.setReplicatable(IWBBridge.IWB_ElementGetReplicate(id) == IWBBridge.IWB_TRISTATE_ON);
        element.setLocked(IWBBridge.IWB_ElementGetLocked(id) == IWBBridge.IWB_TRISTATE_ON);
        if (IWBBridge.IWB_ElementGetIdentifier(id) != IWBBridge.IWB_NULL) element.setIdentifier(getString(IWBBridge.IWB_ElementGetIdentifier(id)));
        ElementColour ec = getColor(IWBBridge.IWB_ElementGetColour(id), fill);
        if (ec != null) element.setFillColour(ec.getColour());
        int transListID = IWBBridge.IWB_ElementGetTransformations(id);
        if (transforms != null) {
            for (AffineTransform affineTransform : transforms) {
                element.addTransformation(affineTransform);
            }
        }
        if (transListID != IWBBridge.IWB_NULL) {
            int transCount = IWBBridge.IWB_ArrayGetCount(transListID);
            for (int i = 0; i < transCount; ++i) {
                int transID = IWBBridge.IWB_ArrayGetAtIndex(transListID, i);
                int transType = IWBBridge.IWB_TransformationGetType(transID);
                if (transType == IWBBridge.IWB_TRANSFORM_ROTATION) {
                    AffineTransform trans = AffineTransform.getRotateInstance(Math.toRadians(IWBBridge.IWB_TransformationGetRotation(transID)), IWBBridge.IWB_TransformationGetRotationPointX(transID), IWBBridge.IWB_TransformationGetRotationPointY(transID));
                    element.addTransformation(trans);
                } else if (transType == IWBBridge.IWB_TRANSFORM_TRANSLATION) {
                    AffineTransform trans = AffineTransform.getTranslateInstance(IWBBridge.IWB_TransformationGetDeltaX(transID), IWBBridge.IWB_TransformationGetDeltaY(transID));
                    element.addTransformation(trans);
                }
            }
        }
        element.addTransformation(getViewPortTransform());
    }

    private MovieElement createMovieElement(int id, ElementColour fill, ArrayList<AffineTransform> transforms) {
        MovieElement element = null;
        try {
            int fileID = IWBBridge.IWB_MediaGetFile(id);
            String filename = getString(IWBBridge.IWB_FileGetPath(fileID));
            File file = new File(filename);
            element = new MovieElement();
            element.setURL(file.getAbsolutePath());
            setElementProperties(element, fill, transforms, id);
            element.setArea(new Rectangle2D.Float(IWBBridge.IWB_ElementGetLocationX(id), IWBBridge.IWB_ElementGetLocationY(id), IWBBridge.IWB_ElementGetWidth(id), IWBBridge.IWB_ElementGetHeight(id)));
        } catch (Exception e) {
            Viewer.logException(e);
        }
        return element;
    }

    private FlashElement createFlashElement(int id, ElementColour fill, ArrayList<AffineTransform> transforms) {
        FlashElement element = null;
        try {
            int fileID = IWBBridge.IWB_MediaGetFile(id);
            String filename = getString(IWBBridge.IWB_FileGetPath(fileID));
            File file = new File(filename);
            element = new FlashElement();
            element.setURL(file.getAbsolutePath());
            setElementProperties(element, fill, transforms, id);
            element.setArea(new Rectangle2D.Double(IWBBridge.IWB_ElementGetLocationX(id), IWBBridge.IWB_ElementGetLocationY(id), IWBBridge.IWB_ElementGetWidth(id), IWBBridge.IWB_ElementGetHeight(id)));
        } catch (Exception e) {
            Viewer.logException(e);
        }
        return element;
    }

    private static void applyStyle(Style s, int propRef) {
        int id = IWBBridge.IWB_TextStyleGetBackgroundColour(propRef);
        if (id != IWBBridge.IWB_NULL) s.backcolour = id;
        id = IWBBridge.IWB_TextStyleGetForegroundColour(propRef);
        if (id != IWBBridge.IWB_NULL) s.fill = id;
        id = IWBBridge.IWB_TextStyleGetFont(propRef);
        if (id != IWBBridge.IWB_NULL) s.font = id;
        id = IWBBridge.IWB_TextStyleGetAlignment(propRef);
        if (id != IWBBridge.IWB_NULL) s.text_align = id;
        s.underlineStyle = IWBBridge.IWB_TextStyleGetDecoration(propRef);
        int listStyleID = IWBBridge.IWB_TextGetTextListStyle(propRef);
        s.list = listStyleID != IWBBridge.IWB_NULL;
        if (s.list) {
            id = IWBBridge.IWB_TextListStyleGetColour(listStyleID);
            if (id != IWBBridge.IWB_NULL) s.listfill = id;
            id = IWBBridge.IWB_TextListStyleGetType(listStyleID);
            if (id != IWBBridge.IWB_NULL) s.listtype = id;
            id = IWBBridge.IWB_TextListStyleGetStyleType(listStyleID);
            if (id != IWBBridge.IWB_NULL) s.liststyletype = id;
        }
        if (IWBBridge.IWB_TextStyleGetAlignment(propRef) != IWBBridge.IWB_TEXTALIGN_UNDEFINED) s.text_align = IWBBridge.IWB_TextStyleGetAlignment(propRef);
    }

    private Cursor readStyle(String text, Style style, ArrayList<TextLinkPoint> links, int charMax, Cursor cur, int propListID) throws CloneNotSupportedException, UnsupportedEncodingException {
        int propCount = IWBBridge.IWB_ArrayGetCount(propListID);
        for (; cur.index < propCount; cur.index++) {
            int propID = IWBBridge.IWB_ArrayGetAtIndex(propListID, cur.index);
            int propRef = IWBBridge.IWB_TextGetPropertyReference(propID);
            int start = IWBBridge.IWB_TextPropertyGetStart(propID);
            int end = IWBBridge.IWB_TextPropertyGetLength(propID);
            if (start < charMax) {
                if (cur.charIndex < start) {
                    Style s = (Style) style.clone();
                    s.list = false;
                    s.styles = null;
                    s.paragraph = false;
                    s.text = text.substring(cur.charIndex, start);
                    s.parent = style;
                    style.styles.add(s);
                }
                cur.charIndex = start;
                if (IWBBridge.IWB_TextGetPropertyType(propID) == IWBBridge.IWB_TEXTSTYLE) {
                    cur.index++;
                    Style s = (Style) style.clone();
                    s.list = false;
                    s.paragraph = false;
                    applyStyle(s, propRef);
                    if (s.list || s.text_align != style.text_align) {
                        s.paragraph = true;
                        s.styles = new ArrayList<Style>();
                    }
                    s.parent = style;
                    style.styles.add(s);
                    cur = readStyle(text, s, links, end, cur, propListID);
                    if (cur.charIndex < end) {
                        Style rem = (Style) s.clone();
                        rem.text = text.substring(cur.charIndex, end);
                        rem.parent = s;
                        s.styles.add(rem);
                        rem.styles = null;
                        rem.paragraph = false;
                        rem.list = false;
                        cur.charIndex = end;
                    }
                    if (!s.paragraph) {
                        s.styles = null;
                        s.list = false;
                        s.paragraph = false;
                    }
                } else if (IWBBridge.IWB_TextGetPropertyType(propID) == IWBBridge.IWB_TEXTLINEBREAK) {
                    style.styles.add(null);
                } else {
                    String url = getString(IWBBridge.IWB_TextLinkGetURL(propRef));
                    boolean internal = IWBBridge.IWB_TextLinkGetType(propRef) != IWBBridge.IWB_LINK_EXTERNAL;
                    links.add(new TextLinkPoint(url, start, end, internal));
                }
            } else {
                cur.index--;
                break;
            }
        }
        return cur;
    }

    private TextElement createTextElement(int id, ElementColour fill, ArrayList<AffineTransform> transforms, boolean textarea) throws CloneNotSupportedException, UnsupportedEncodingException {
        String data = getString(IWBBridge.IWB_TextGetString(id));
        int propListID = IWBBridge.IWB_TextGetProperties(id);
        int propCount = IWBBridge.IWB_ArrayGetCount(propListID);
        int styleID = IWBBridge.IWB_TextGetStyle(id);
        ParagrapghLayout root = new ParagrapghLayout();
        if (styleID == IWBBridge.IWB_NULL && (propListID == IWBBridge.IWB_NULL || propCount <= 0)) {
            if (data.length() > 0) {
                AttributedString attr = new AttributedString(data);
                attr.addAttribute(TextAttribute.FOREGROUND, AccessibilityColor.controlText, 0, data.length());
                attr.addAttribute(TextAttribute.SIZE, 12f, 0, data.length());
                attr.addAttribute(TextAttribute.FAMILY, "arial", 0, data.length());
                root.getItems().add(attr);
            }
        } else {
            Cursor cur = new Cursor();
            cur.index = 0;
            cur.charIndex = 0;
            ArrayList<TextLinkPoint> links = new ArrayList<TextLinkPoint>();
            Style s = new Style();
            if (styleID != IWBBridge.IWB_NULL) {
                applyStyle(s, styleID);
            }
            s.styles = new ArrayList<Style>();
            cur = readStyle(data, s, links, data.length(), cur, propListID);
            if (cur.charIndex < data.length()) {
                Style last = new Style();
                if (styleID != IWBBridge.IWB_NULL) {
                    applyStyle(last, styleID);
                }
                last.text = data.substring(cur.charIndex);
                last.parent = s;
                s.styles.add(last);
            }
            if (fill == null) fill = new ElementColour(null, Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 1);
            createLayouts(root, s, fill);
            root = (ParagrapghLayout) root.getItems().get(0);
            for (TextLinkPoint link : links) {
                applyLink(root, link, 0);
            }
        }
        TextElement element = new TextElement(root, textarea);
        if (textarea) element.setArea(new Rectangle2D.Double(IWBBridge.IWB_ElementGetLocationX(id), IWBBridge.IWB_ElementGetLocationY(id), IWBBridge.IWB_ElementGetWidth(id), IWBBridge.IWB_ElementGetHeight(id))); else element.setArea(new Rectangle2D.Double(IWBBridge.IWB_ElementGetLocationX(id), IWBBridge.IWB_ElementGetLocationY(id), 0, 0));
        setElementProperties(element, fill, transforms, id);
        return element;
    }

    private static int applyLink(ParagrapghLayout layout, TextLinkPoint link, int index) {
        for (Object obj : layout.getItems()) {
            if (obj != null) {
                if (obj instanceof AttributedString) {
                    AttributedString attribute = (AttributedString) obj;
                    int length = attribute.getIterator().getEndIndex();
                    ;
                    if (link.endIndex >= index && link.startIndex <= index + length) {
                        int startIndex = index < link.startIndex ? link.startIndex : index;
                        int endIndex = index + length < link.endIndex ? index + length : link.endIndex;
                        layout.addLink(attribute, new TextLinkPoint(link.url, startIndex - index, endIndex - index, link.isInternal));
                        attribute.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, startIndex - index, endIndex - index);
                    }
                    index += length;
                } else if (obj instanceof ParagrapghLayout) {
                    index = applyLink((ParagrapghLayout) obj, link, index);
                }
            }
            if (link.endIndex <= index) break;
        }
        return index;
    }

    private int createLayouts(ParagrapghLayout parent, Style s, ElementColour pColour) throws UnsupportedEncodingException {
        ParagrapghLayout root = new ParagrapghLayout(s.text_align, s.list, s.listtype, s.liststyletype);
        if (root.isList() && s.listfill != IWBBridge.IWB_NULL) {
            root.setListFillColour(getColor(s.listfill, pColour).getTextColour());
        }
        int i = 0;
        for (i = 0; i < s.styles.size(); i++) {
            String text = "";
            int j;
            for (j = i; j < s.styles.size() && s.styles.get(j) != null && !s.styles.get(j).paragraph; j++) {
                if (s.styles.get(j).text != null) {
                    s.styles.get(j).start = text.length();
                    text += s.styles.get(j).text;
                    s.styles.get(j).end = text.length();
                }
            }
            if (text.length() > 0) {
                AttributedString attr = new AttributedString(text);
                attr.addAttribute(TextAttribute.FOREGROUND, pColour.getTextColour(), 0, text.length());
                attr.addAttribute(TextAttribute.SIZE, 12f, 0, text.length());
                attr.addAttribute(TextAttribute.FAMILY, "arial", 0, text.length());
                s.start = 0;
                s.end = text.length();
                applyStyletoText(attr, s, pColour);
                for (int k = i; k < j; k++) {
                    Style style = s.styles.get(k);
                    if (style != null & style.text != null) {
                        applyStyletoText(attr, style, pColour);
                    }
                }
                root.getItems().add(attr);
            }
            i = j;
            if (j < s.styles.size()) {
                if (s.styles.get(j) == null) {
                    root.getItems().add(null);
                } else if (s.styles.get(j).paragraph) {
                    createLayouts(root, s.styles.get(j), pColour);
                }
            }
        }
        parent.getItems().add(root);
        return i;
    }

    private int getUnderlineStyle(Style style) {
        if (style != null) {
            if (style.underlineStyle != IWBBridge.IWB_TEXTDECORATION_INHERIT) return style.underlineStyle; else return getUnderlineStyle(style.parent);
        } else return IWBBridge.IWB_TEXTDECORATION_UNDEFINED;
    }

    private int getFontStretchValue(Style style) {
        if (style != null) {
            int value = IWBBridge.IWB_FontGetStretch(style.font);
            if (value != IWBBridge.IWB_FONT_STRETCH_INHERIT) {
                return value;
            } else {
                return getFontStretchValue(style.parent);
            }
        } else return IWBBridge.IWB_FONT_STRETCH_UNDEFINED;
    }

    private int getFontStyleValue(Style style) {
        if (style != null) {
            int value = IWBBridge.IWB_FontGetStyle(style.font);
            if (value != IWBBridge.IWB_FONT_STYLE_INHERIT) {
                return value;
            } else {
                return getFontStyleValue(style.parent);
            }
        } else return IWBBridge.IWB_FONT_STYLE_UNDEFINED;
    }

    private int getFontWeightValue(Style style) {
        if (style != null) {
            int value = IWBBridge.IWB_FontGetWeight(style.font);
            if (value != IWBBridge.IWB_FONT_WEIGHT_INHERIT) {
                return value;
            } else {
                return getFontWeightValue(style.parent);
            }
        } else return IWBBridge.IWB_FONT_WEIGHT_UNDEFINED;
    }

    private void applyStyletoText(AttributedString attr, Style style, ElementColour pColour) throws UnsupportedEncodingException {
        if (style.font != IWBBridge.IWB_NULL) {
            int value = IWBBridge.IWB_FontGetName(style.font);
            if (value != IWBBridge.IWB_NULL) attr.addAttribute(TextAttribute.FAMILY, getString(value), style.start, style.end);
            float sizevalue = IWBBridge.IWB_FontGetSize(style.font);
            if (sizevalue != IWBBridge.IWB_VALUE_UNDEFINED) attr.addAttribute(TextAttribute.SIZE, new Float(sizevalue), style.start, style.end);
            value = getFontStretchValue(style);
            if (value != IWBBridge.IWB_FONT_STRETCH_UNDEFINED) attr.addAttribute(TextAttribute.WIDTH, convertFontStretchValue(value), style.start, style.end);
            value = getFontStyleValue(style);
            if (value != IWBBridge.IWB_FONT_STYLE_UNDEFINED) attr.addAttribute(TextAttribute.POSTURE, convertFontStyleValue(value), style.start, style.end);
            value = getFontWeightValue(style);
            if (value != IWBBridge.IWB_FONT_WEIGHT_UNDEFINED) attr.addAttribute(TextAttribute.WEIGHT, convertFontWeightValue(value), style.start, style.end);
        }
        if (style.fill != IWBBridge.IWB_NULL) {
            attr.addAttribute(TextAttribute.FOREGROUND, getColor(style.fill, pColour).getTextColour(), style.start, style.end);
        }
        if (style.backcolour != IWBBridge.IWB_NULL) {
            attr.addAttribute(TextAttribute.BACKGROUND, getColor(style.backcolour, pColour).getColour(), style.start, style.end);
        }
        int underlinestyle = getUnderlineStyle(style);
        if (underlinestyle == IWBBridge.IWB_TEXTDECORATION_UNDERLINE) {
            attr.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, style.start, style.end);
        } else if (underlinestyle == IWBBridge.IWB_TEXTDECORATION_NONE) {
            attr.addAttribute(TextAttribute.UNDERLINE, -1, style.start, style.end);
        }
    }

    /**
	 * Saves a copy of the document
	 * @param filename
	 * @return
	 */
    public void saveAs(String workingpath, String filename, String newFilename) throws Exception {
        int status = IWBBridge.IWB_LibraryInitialize(IWBBridge.IWB_StringFromUTF8(toUTF(workingpath)));
        checkStatus(status, "Library initialization");
        status = IWBBridge.IWB_DocumentLoad(IWBBridge.IWB_StringFromUTF8(toUTF(filename)));
        checkStatus(status, "File load");
        status = IWBBridge.IWB_DocumentSave(IWBBridge.IWB_StringFromUTF8(toUTF(newFilename)));
        checkStatus(status, "File Save");
        status = IWBBridge.IWB_LibraryTerminate();
        checkStatus(status, "Library Uninitialization");
    }
}
