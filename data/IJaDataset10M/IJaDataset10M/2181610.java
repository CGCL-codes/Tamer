package org.jquery4jsf.custom.bookmark;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jquery4jsf.javascript.JSAttribute;
import org.jquery4jsf.javascript.JSDocumentElement;
import org.jquery4jsf.javascript.JSElement;
import org.jquery4jsf.javascript.function.JSFunction;
import org.jquery4jsf.renderkit.RendererUtilities;
import org.jquery4jsf.renderkit.html.HTML;
import org.jquery4jsf.renderkit.html.HtmlRendererUtilities;
import org.jquery4jsf.utilities.MessageFactory;

public class BookMarkRenderer extends BookMarkBaseRenderer {

    protected String encodeOptionComponent(StringBuffer options, BookMark bookMark, FacesContext context) {
        options.append(" {\n");
        encodeOptionComponentByType(options, bookMark.getUrl(), "url", null);
        encodeOptionComponentByType(options, bookMark.getTitle(), "title", null);
        String icons = bookMark.getIcons() != null ? bookMark.getIcons() : RendererUtilities.getResourceURL(context, "/bookmaker/bookmarks.png");
        encodeOptionComponentByType(options, icons, "icons", null);
        encodeOptionComponentArrayByType(options, bookMark.getSites(), "sites");
        encodeOptionComponentByType(options, bookMark.getIconSize(), "iconSize", null);
        encodeOptionComponentByType(options, bookMark.getIconCols(), "iconCols", null);
        encodeOptionComponentByType(options, bookMark.getTarget(), "target", null);
        encodeOptionComponentByType(options, bookMark.isCompact(), "compact", null);
        encodeOptionComponentByType(options, bookMark.getHint(), "hint", null);
        encodeOptionComponentByType(options, bookMark.isPopup(), "popup", null);
        encodeOptionComponentByType(options, bookMark.getPopupText(), "popupText", null);
        encodeOptionComponentByType(options, bookMark.isAddFavorite(), "addFavorite", null);
        encodeOptionComponentByType(options, bookMark.getFavoriteText(), "favoriteText", null);
        encodeOptionComponentByType(options, bookMark.getFavoriteIcon(), "favoriteIcon", null);
        encodeOptionComponentByType(options, bookMark.isAddEmail(), "addEmail", null);
        encodeOptionComponentByType(options, bookMark.getEmailText(), "emailText", null);
        encodeOptionComponentByType(options, bookMark.getEmailIcon(), "emailIcon", null);
        encodeOptionComponentByType(options, bookMark.getEmailSubject(), "emailSubject", null);
        encodeOptionComponentByType(options, bookMark.getEmailBody(), "emailBody", null);
        encodeOptionComponentByType(options, bookMark.getManualBookmark(), "manualBookmark", null);
        if (options.toString().endsWith(", \n")) {
            String stringa = options.substring(0, options.length() - 3);
            options = new StringBuffer(stringa);
        }
        boolean noParams = false;
        if (options.toString().endsWith(" {\n")) {
            String stringa = options.substring(0, options.length() - 3);
            options = new StringBuffer(stringa);
            noParams = true;
        }
        if (!noParams) {
            options.append(" }");
        }
        return options.toString();
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) throw new NullPointerException(MessageFactory.getMessage("com.sun.faces.NULL_PARAMETERS_ERROR"));
        if (!component.isRendered()) return;
        BookMark bookMark = null;
        if (component instanceof BookMark) bookMark = (BookMark) component;
        encodeResources(bookMark);
        encodeBookMarkScript(context, bookMark);
        encodeBookMarkMarket(context, bookMark);
    }

    private void encodeBookMarkMarket(FacesContext context, BookMark bookMark) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement(HTML.TAG_SPAN, bookMark);
        responseWriter.writeAttribute("id", bookMark.getClientId(context), "id");
        HtmlRendererUtilities.writeHtmlAttributes(responseWriter, bookMark, HTML.HTML_STD_ATTR);
        responseWriter.endElement(HTML.TAG_SPAN);
    }

    protected void encodeBookMarkScript(FacesContext context, BookMark bookMark) throws IOException {
        JSDocumentElement documentElement = JSDocumentElement.getInstance();
        JSFunction function = new JSFunction();
        function.addJSElement(getJSElement(context, bookMark));
        documentElement.addFunctionToReady(function);
    }

    public JSElement getJSElement(FacesContext context, UIComponent component) {
        BookMark bookMark = null;
        if (component instanceof BookMark) bookMark = (BookMark) component;
        JSElement element = new JSElement(bookMark.getClientId(context));
        JSAttribute jsBookMark = new JSAttribute("bookmark", false);
        StringBuffer sbOption = new StringBuffer();
        jsBookMark.addValue(encodeOptionComponent(sbOption, bookMark, context));
        element.addAttribute(jsBookMark);
        return element;
    }
}
