package org.nakedobjects.plugins.dndviewer.viewer.basic;

import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.plugins.dndviewer.Content;
import org.nakedobjects.plugins.dndviewer.ObjectContent;
import org.nakedobjects.plugins.dndviewer.Toolkit;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Text;
import org.nakedobjects.plugins.dndviewer.viewer.view.text.TitleText;

class EmptyFieldTitleText extends TitleText {

    private final Content content;

    public EmptyFieldTitleText(final View view, final Text style) {
        super(view, style, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
        content = view.getContent();
    }

    @Override
    protected String title() {
        return ((ObjectContent) content).getSpecification().getSingularName();
    }
}
