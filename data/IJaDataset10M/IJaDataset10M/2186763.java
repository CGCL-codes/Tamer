package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class LifeDestroy extends GraphicalElement {

    private final ParticipantBox participant;

    private final Component comp;

    public LifeDestroy(double startingY, ParticipantBox participant, Component comp) {
        super(startingY);
        this.participant = participant;
        this.comp = comp;
    }

    @Override
    protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
        final StringBounder stringBounder = ug.getStringBounder();
        ug.translate(getStartingX(stringBounder), getStartingY());
        comp.drawU(ug, null, context);
    }

    @Override
    public double getPreferredHeight(StringBounder stringBounder) {
        return comp.getPreferredHeight(stringBounder);
    }

    @Override
    public double getPreferredWidth(StringBounder stringBounder) {
        return comp.getPreferredWidth(stringBounder);
    }

    @Override
    public double getStartingX(StringBounder stringBounder) {
        return participant.getCenterX(stringBounder) - getPreferredWidth(stringBounder) / 2.0;
    }
}
