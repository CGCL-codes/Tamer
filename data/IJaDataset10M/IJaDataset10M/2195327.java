package jaxlib.swing.plaf.basic;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: CustomHtmlEditorKit.java 2271 2007-03-16 08:48:23Z joerg_wassmer $
 */
@SuppressWarnings("serial")
final class CustomHtmlEditorKit extends HTMLEditorKit {

    private CustomHtmlStyleSheet styleSheet;

    private final CustomHtmlEditorKit.HTMLFactoryImpl viewFactory;

    CustomHtmlEditorKit() {
        super();
        this.viewFactory = new HTMLFactoryImpl();
    }

    @Override
    public CustomHtmlStyleSheet getStyleSheet() {
        if (this.styleSheet == null) {
            this.styleSheet = new CustomHtmlStyleSheet();
            super.setStyleSheet(styleSheet);
        }
        return this.styleSheet;
    }

    @Override
    public ViewFactory getViewFactory() {
        return this.viewFactory;
    }

    @Override
    public void setStyleSheet(StyleSheet styleSheet) {
        this.styleSheet = (CustomHtmlStyleSheet) styleSheet;
        super.setStyleSheet(styleSheet);
    }

    private final class HTMLFactoryImpl extends HTMLEditorKit.HTMLFactory {

        HTMLFactoryImpl() {
            super();
        }

        @Override
        public View create(Element e) {
            AttributeSet attrs = e.getAttributes();
            Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
            Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
            if (o instanceof HTML.Tag) {
                HTML.Tag kind = (HTML.Tag) o;
                if (kind == HTML.Tag.OL) return new CustomHtmlListView(e, CustomHtmlEditorKit.this.getStyleSheet());
            }
            return super.create(e);
        }
    }
}
