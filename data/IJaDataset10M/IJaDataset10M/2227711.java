package org.jactr.eclipse.ui.editor.link;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.jactr.eclipse.ui.editor.command.GoTo;
import org.jactr.io.antlr3.misc.DetailedCommonTree;

public class ACTRHyperlink implements IHyperlink {

    private final IRegion _target;

    private final DetailedCommonTree _destination;

    public ACTRHyperlink(IRegion location, DetailedCommonTree destination) {
        _target = location;
        _destination = destination;
    }

    public IRegion getHyperlinkRegion() {
        return _target;
    }

    public String getHyperlinkText() {
        return null;
    }

    public String getTypeLabel() {
        return null;
    }

    public void open() {
        try {
            GoTo.goTo(_destination);
        } catch (Exception e) {
        }
    }
}
