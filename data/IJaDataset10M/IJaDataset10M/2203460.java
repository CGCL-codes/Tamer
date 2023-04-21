package com.itextpdf.tool.xml.css.apply;

import java.io.IOException;
import java.util.Map;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.net.ImageRetrieve;
import com.itextpdf.tool.xml.net.exc.NoImageException;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;

/**
 * @author itextpdf.com
 *
 */
public class ListStyleTypeCssApplier {

    private final CssUtils utils = CssUtils.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(ListStyleTypeCssApplier.class);

    /**
	 *
	 */
    public ListStyleTypeCssApplier() {
    }

    /**
	 * The ListCssApplier has the capabilities to change the type of the given {@link List} dependable on the css. This
	 * means: <strong>Always replace your list with the returned one and add content to the list after
	 * applying!</strong><br />
	 * note: not implemented list-style-type:armenian, georgian, decimal-leading-zero.
	 *
	 * @param list the list to style
	 * @param t the tag
	 * @param htmlPipelineContext the context
	 * @return the changed {@link List}
	 */
    public List apply(final List list, final Tag t, final ImageProvider htmlPipelineContext) {
        float fontSize = FontSizeTranslator.getInstance().getFontSize(t);
        List lst = list;
        Map<String, String> css = t.getCSS();
        String styleType = css.get(CSS.Property.LIST_STYLE_TYPE);
        BaseColor color = HtmlUtilities.decodeColor(css.get(CSS.Property.COLOR));
        if (null == color) color = BaseColor.BLACK;
        if (null != styleType) {
            if (styleType.equalsIgnoreCase(CSS.Value.NONE)) {
                lst.setLettered(false);
                lst.setNumbered(false);
                lst.setListSymbol("");
            } else if (CSS.Value.DECIMAL.equalsIgnoreCase(styleType)) {
                lst = new List(List.ORDERED);
                synchronizeSymbol(fontSize, lst, color);
            } else if (CSS.Value.DISC.equalsIgnoreCase(styleType)) {
                lst = new ZapfDingbatsList(108);
                shrinkSymbol(lst, fontSize, color);
            } else if (CSS.Value.SQUARE.equalsIgnoreCase(styleType)) {
                lst = new ZapfDingbatsList(110);
                shrinkSymbol(lst, fontSize, color);
            } else if (CSS.Value.CIRCLE.equalsIgnoreCase(styleType)) {
                lst = new ZapfDingbatsList(109);
                shrinkSymbol(lst, fontSize, color);
            } else if (CSS.Value.LOWER_ROMAN.equals(styleType)) {
                lst = new RomanList(true, 0);
                synchronizeSymbol(fontSize, lst, color);
                lst.setAutoindent(true);
            } else if (CSS.Value.UPPER_ROMAN.equals(styleType)) {
                lst = new RomanList(false, 0);
                lst.setAutoindent(true);
                synchronizeSymbol(fontSize, lst, color);
            } else if (CSS.Value.LOWER_GREEK.equals(styleType)) {
                lst = new GreekList(true, 0);
                synchronizeSymbol(fontSize, lst, color);
                lst.setAutoindent(true);
            } else if (CSS.Value.UPPER_GREEK.equals(styleType)) {
                lst = new GreekList(false, 0);
                synchronizeSymbol(fontSize, lst, color);
                lst.setAutoindent(true);
            } else if (CSS.Value.LOWER_ALPHA.equals(styleType) || CSS.Value.LOWER_LATIN.equals(styleType)) {
                lst = new List(List.ORDERED, List.ALPHABETICAL);
                synchronizeSymbol(fontSize, lst, color);
                lst.setLowercase(true);
                lst.setAutoindent(true);
            } else if (CSS.Value.UPPER_ALPHA.equals(styleType) || CSS.Value.UPPER_LATIN.equals(styleType)) {
                lst = new List(List.ORDERED, List.ALPHABETICAL);
                synchronizeSymbol(fontSize, lst, color);
                lst.setLowercase(false);
                lst.setAutoindent(true);
            }
        } else if (t.getName().equalsIgnoreCase(HTML.Tag.OL)) {
            lst = new List(List.ORDERED);
            synchronizeSymbol(fontSize, lst, color);
            lst.setAutoindent(true);
        } else if (t.getName().equalsIgnoreCase(HTML.Tag.UL)) {
            lst = new List(List.UNORDERED);
            shrinkSymbol(lst, fontSize, color);
        }
        if (null != css.get(CSS.Property.LIST_STYLE_IMAGE) && !css.get(CSS.Property.LIST_STYLE_IMAGE).equalsIgnoreCase(CSS.Value.NONE)) {
            lst = new List();
            String url = utils.extractUrl(css.get(CSS.Property.LIST_STYLE_IMAGE));
            Image img = null;
            try {
                if (htmlPipelineContext == null) {
                    img = new ImageRetrieve().retrieveImage(url);
                } else {
                    try {
                        img = new ImageRetrieve(htmlPipelineContext).retrieveImage(url);
                    } catch (NoImageException e) {
                        if (LOG.isLogging(Level.TRACE)) {
                            LOG.trace(String.format(LocaleMessages.getInstance().getMessage("css.applier.list.noimage")));
                        }
                        img = new ImageRetrieve().retrieveImage(url);
                    }
                }
                lst.setListSymbol(new Chunk(img, 0, 0, false));
                lst.setSymbolIndent(img.getWidth());
                if (LOG.isLogging(Level.TRACE)) {
                    LOG.trace(String.format(LocaleMessages.getInstance().getMessage("html.tag.list"), url));
                }
            } catch (IOException e) {
                if (LOG.isLogging(Level.ERROR)) {
                    LOG.error(String.format(LocaleMessages.getInstance().getMessage("html.tag.list.failed"), url), e);
                }
                lst = new List(List.UNORDERED);
            } catch (NoImageException e) {
                if (LOG.isLogging(Level.ERROR)) {
                    LOG.error(e.getLocalizedMessage(), e);
                }
                lst = new List(List.UNORDERED);
            }
            lst.setAutoindent(false);
        }
        lst.setAlignindent(false);
        float leftIndent = 0;
        if (null != css.get(CSS.Property.LIST_STYLE_POSITION) && css.get(CSS.Property.LIST_STYLE_POSITION).equalsIgnoreCase(CSS.Value.INSIDE)) {
            leftIndent += 30;
        } else {
            leftIndent += 15;
        }
        leftIndent += css.get(CSS.Property.MARGIN_LEFT) != null ? utils.parseValueToPt(css.get(CSS.Property.MARGIN_LEFT), fontSize) : 0;
        leftIndent += css.get(CSS.Property.PADDING_LEFT) != null ? utils.parseValueToPt(css.get(CSS.Property.PADDING_LEFT), fontSize) : 0;
        lst.setIndentationLeft(leftIndent);
        return lst;
    }

    private void synchronizeSymbol(final float fontSize, final List lst, final BaseColor color) {
        Font font = lst.getSymbol().getFont();
        font.setSize(fontSize);
        font.setColor(color);
        lst.setSymbolIndent(fontSize);
    }

    private void shrinkSymbol(final List lst, final float fontSize, final BaseColor color) {
        lst.setSymbolIndent(12);
        Chunk symbol = lst.getSymbol();
        symbol.setTextRise(2);
        Font font = symbol.getFont();
        font.setSize(7);
        font.setColor(color);
    }

    /**
	 * Utility method applying style to a list when no ImageProvider is available.
	 * 
	 * @param e the list
	 * @param t the tag
	 * @see ListStyleTypeCssApplier#apply(List, Tag, ImageProvider)
	 * @return styled element
	 */
    public Element apply(final List e, final Tag t) {
        return apply(e, t, null);
    }
}
