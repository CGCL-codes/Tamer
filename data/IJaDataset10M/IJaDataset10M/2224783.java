package org.musicnotation.gef.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.musicnotation.gef.Engraver;

public class StaffGroupFigure extends Figure {

    public StaffGroupFigure() {
        final FlowLayout layout = new FlowLayout();
        layout.setHorizontal(false);
        layout.setMinorSpacing((int) Engraver.LINE_DISTANCE);
        setLayoutManager(layout);
    }

    @Override
    public Insets getInsets() {
        return new Insets((int) Engraver.LINE_DISTANCE, 0, (int) Engraver.LINE_DISTANCE, (int) Engraver.LINE_DISTANCE);
    }
}
