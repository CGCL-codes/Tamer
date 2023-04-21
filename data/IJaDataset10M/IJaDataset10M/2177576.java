package net.kano.joustsim.text.convbox;

import javax.swing.SizeRequirements;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.ParagraphView;

public class WordSplittingViewFactory implements ViewFactory {

    private final ViewFactory parent;

    public WordSplittingViewFactory(ViewFactory parent) {
        this.parent = parent;
    }

    public View create(Element elem) {
        AttributeSet attr = elem.getAttributes();
        Object name = attr.getAttribute(StyleConstants.NameAttribute);
        if (name == HTML.Tag.P || name == HTML.Tag.IMPLIED) {
            Boolean val = (Boolean) attr.getAttribute(ConversationDocument.ATTR_SPLIT_WORDS);
            if (val != null && val.booleanValue() == true) {
                return new WordSplittingParagraphView(elem);
            }
        }
        return parent.create(elem);
    }

    private static class WordSplittingParagraphView extends ParagraphView {

        public WordSplittingParagraphView(Element elem) {
            super(elem);
        }

        protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
            SizeRequirements sup = super.calculateMinorAxisRequirements(axis, r);
            sup.minimum = 1;
            return sup;
        }
    }
}
