package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class ArrowAndNoteBox extends Arrow implements InGroupable {

    private final Arrow arrow;

    private final NoteBox noteBox;

    public ArrowAndNoteBox(StringBounder stringBounder, Arrow arrow, NoteBox noteBox) {
        super(arrow.getStartingY(), arrow.getSkin(), arrow.getArrowComponent());
        this.arrow = arrow;
        this.noteBox = noteBox;
        final double arrowHeight = arrow.getPreferredHeight(stringBounder);
        final double noteHeight = noteBox.getPreferredHeight(stringBounder);
        final double myHeight = getPreferredHeight(stringBounder);
        final double diffHeightArrow = myHeight - arrowHeight;
        final double diffHeightNote = myHeight - noteHeight;
        if (diffHeightArrow > 0) {
            arrow.pushToDown(diffHeightArrow / 2);
        }
        if (diffHeightNote > 0) {
            noteBox.pushToDown(diffHeightNote / 2);
        }
    }

    @Override
    public final double getArrowOnlyWidth(StringBounder stringBounder) {
        return arrow.getPreferredWidth(stringBounder);
    }

    @Override
    public void setMaxX(double m) {
        super.setMaxX(m);
        arrow.setMaxX(m);
    }

    @Override
    protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
        final double atX = ug.getTranslateX();
        final double atY = ug.getTranslateY();
        arrow.drawU(ug, maxX, context);
        ug.setTranslate(atX, atY);
        noteBox.drawU(ug, maxX, context);
        ug.setTranslate(atX, atY);
    }

    @Override
    public double getPreferredHeight(StringBounder stringBounder) {
        return Math.max(arrow.getPreferredHeight(stringBounder), noteBox.getPreferredHeight(stringBounder));
    }

    @Override
    public double getPreferredWidth(StringBounder stringBounder) {
        double w = arrow.getPreferredWidth(stringBounder);
        w = Math.max(w, arrow.getActualWidth(stringBounder));
        return w + noteBox.getPreferredWidth(stringBounder);
    }

    @Override
    public double getActualWidth(StringBounder stringBounder) {
        return getPreferredWidth(stringBounder);
    }

    @Override
    public double getStartingX(StringBounder stringBounder) {
        return Math.min(arrow.getStartingX(stringBounder), noteBox.getStartingX(stringBounder));
    }

    @Override
    public int getDirection(StringBounder stringBounder) {
        return arrow.getDirection(stringBounder);
    }

    @Override
    public double getArrowYStartLevel(StringBounder stringBounder) {
        return arrow.getArrowYStartLevel(stringBounder);
    }

    @Override
    public double getArrowYEndLevel(StringBounder stringBounder) {
        return arrow.getArrowYEndLevel(stringBounder);
    }

    public double getMaxX(StringBounder stringBounder) {
        return getStartingX(stringBounder) + getPreferredWidth(stringBounder);
    }

    public double getMinX(StringBounder stringBounder) {
        return getStartingX(stringBounder);
    }

    public String toString(StringBounder stringBounder) {
        return toString();
    }

    @Override
    public LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position) {
        return arrow.getParticipantAt(stringBounder, position);
    }
}
