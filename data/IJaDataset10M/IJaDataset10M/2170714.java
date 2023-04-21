package edu.mit.lcs.haystack.ozone.standard.widgets.slide;

import edu.mit.lcs.haystack.rdf.*;
import edu.mit.lcs.haystack.Constants;

/**
 * @version 	1.0
 * @author		David Huynh
 */
public final class SlideConstants {

    public static final String s_namespace = Constants.s_slide_namespace;

    public static final int ALIGN_TOP = 0;

    public static final int ALIGN_BOTTOM = 1;

    public static final int ALIGN_CENTER = 2;

    public static final int ALIGN_LEFT = 0;

    public static final int ALIGN_RIGHT = 1;

    public static final Resource s_children = new Resource(s_namespace + "children");

    public static final Resource s_child = new Resource(s_namespace + "child");

    public static final Resource s_fontFamily = new Resource(s_namespace + "fontFamily");

    public static final Resource s_fontSize = new Resource(s_namespace + "fontSize");

    public static final Resource s_fontBold = new Resource(s_namespace + "fontBold");

    public static final Resource s_fontItalic = new Resource(s_namespace + "fontItalic");

    public static final Resource s_color = new Resource(s_namespace + "color");

    public static final Resource s_bgcolor = new Resource(s_namespace + "bgcolor");

    public static final Resource s_linkColor = new Resource(s_namespace + "linkColor");

    public static final Resource s_linkHoverColor = new Resource(s_namespace + "linkHoverColor");

    public static final Resource s_width = new Resource(s_namespace + "width");

    public static final Resource s_height = new Resource(s_namespace + "height");

    public static final Resource s_minWidth = new Resource(s_namespace + "minWidth");

    public static final Resource s_minHeight = new Resource(s_namespace + "minHeight");

    public static final Resource s_maxWidth = new Resource(s_namespace + "maxWidth");

    public static final Resource s_maxHeight = new Resource(s_namespace + "maxHeight");

    public static final Resource s_onLoad = new Resource(s_namespace + "onLoad");

    public static final Resource s_text = new Resource(s_namespace + "text");

    public static final Resource s_defaultText = new Resource(s_namespace + "defaultText");

    public static final Resource s_maxLines = new Resource(s_namespace + "maxLines");

    public static final Resource s_wrap = new Resource(s_namespace + "wrap");

    public static final Resource s_textDropShadow = new Resource(s_namespace + "textDropShadow");

    public static final Resource s_editable = new Resource(s_namespace + "editable");

    public static final Resource s_source = new Resource(s_namespace + "source");

    public static final Resource s_scaleToFit = new Resource(s_namespace + "scaleToFit");

    public static final Resource s_textAlign = new Resource(s_namespace + "textAlign");

    public static final Resource s_baseLineOffset = new Resource(s_namespace + "baseLineOffset");

    public static final Resource s_scale = new Resource(s_namespace + "scale");

    public static final Resource s_pack = new Resource(s_namespace + "pack");

    public static final Resource s_alignX = new Resource(s_namespace + "alignX");

    public static final Resource s_alignY = new Resource(s_namespace + "alignY");

    public static final Resource s_fillParent = new Resource(s_namespace + "fillParent");

    public static final Resource s_fillParentWidth = new Resource(s_namespace + "fillParentWidth");

    public static final Resource s_fillParentHeight = new Resource(s_namespace + "fillParentHeight");

    public static final Resource s_cropChild = new Resource(s_namespace + "cropChild");

    public static final Resource s_cropChildWidth = new Resource(s_namespace + "cropChildWidth");

    public static final Resource s_cropChildHeight = new Resource(s_namespace + "cropChildHeight");

    public static final Resource s_stretchChild = new Resource(s_namespace + "stretchChild");

    public static final Resource s_stretchChildWidth = new Resource(s_namespace + "stretchChildWidth");

    public static final Resource s_stretchChildHeight = new Resource(s_namespace + "stretchChildHeight");

    public static final Resource s_background = new Resource(s_namespace + "background");

    public static final Resource s_backgroundRepeat = new Resource(s_namespace + "backgroundRepeat");

    public static final Resource s_backgroundAlignX = new Resource(s_namespace + "backgroundAlignX");

    public static final Resource s_backgroundAlignY = new Resource(s_namespace + "backgroundAlignY");

    public static final Resource s_margin = new Resource(s_namespace + "margin");

    public static final Resource s_marginX = new Resource(s_namespace + "marginX");

    public static final Resource s_marginY = new Resource(s_namespace + "marginY");

    public static final Resource s_marginLeft = new Resource(s_namespace + "marginLeft");

    public static final Resource s_marginRight = new Resource(s_namespace + "marginRight");

    public static final Resource s_marginTop = new Resource(s_namespace + "marginTop");

    public static final Resource s_marginBottom = new Resource(s_namespace + "marginBottom");

    public static final Resource s_clearance = new Resource(s_namespace + "clearance");

    public static final Resource s_clearanceX = new Resource(s_namespace + "clearanceX");

    public static final Resource s_clearanceY = new Resource(s_namespace + "clearanceY");

    public static final Resource s_clearanceLeft = new Resource(s_namespace + "clearanceLeft");

    public static final Resource s_clearanceRight = new Resource(s_namespace + "clearanceRight");

    public static final Resource s_clearanceTop = new Resource(s_namespace + "clearanceTop");

    public static final Resource s_clearanceBottom = new Resource(s_namespace + "clearanceBottom");

    public static final Resource s_borderWidth = new Resource(s_namespace + "borderWidth");

    public static final Resource s_borderColor = new Resource(s_namespace + "borderColor");

    public static final Resource s_borderXWidth = new Resource(s_namespace + "borderXWidth");

    public static final Resource s_borderXColor = new Resource(s_namespace + "borderXColor");

    public static final Resource s_borderYWidth = new Resource(s_namespace + "borderYWidth");

    public static final Resource s_borderYColor = new Resource(s_namespace + "borderYColor");

    public static final Resource s_borderLeftWidth = new Resource(s_namespace + "borderLeftWidth");

    public static final Resource s_borderLeftColor = new Resource(s_namespace + "borderLeftColor");

    public static final Resource s_borderRightWidth = new Resource(s_namespace + "borderRightWidth");

    public static final Resource s_borderRightColor = new Resource(s_namespace + "borderRightColor");

    public static final Resource s_borderTopWidth = new Resource(s_namespace + "borderTopWidth");

    public static final Resource s_borderTopColor = new Resource(s_namespace + "borderTopColor");

    public static final Resource s_borderBottomWidth = new Resource(s_namespace + "borderBottomWidth");

    public static final Resource s_borderBottomColor = new Resource(s_namespace + "borderBottomColor");

    public static final Resource s_dropShadow = new Resource(s_namespace + "dropShadow");

    public static final Resource s_borderShadow = new Resource(s_namespace + "borderShadow");

    public static final Resource s_borderXShadow = new Resource(s_namespace + "borderXShadow");

    public static final Resource s_borderYShadow = new Resource(s_namespace + "borderYShadow");

    public static final Resource s_borderTopShadow = new Resource(s_namespace + "borderTopShadow");

    public static final Resource s_borderBottomShadow = new Resource(s_namespace + "borderBottomShadow");

    public static final Resource s_borderLeftShadow = new Resource(s_namespace + "borderLeftShadow");

    public static final Resource s_borderRightShadow = new Resource(s_namespace + "borderRightShadow");

    public static final Resource s_backgroundHighlight = new Resource(s_namespace + "backgroundHighlight");

    public static final Resource s_highlightBorder = new Resource(s_namespace + "highlightBorder");
}
