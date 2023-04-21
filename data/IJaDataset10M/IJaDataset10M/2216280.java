package com.googlecode.phugushop.drawing;

import org.bushe.swing.event.ObjectEvent;
import com.googlecode.phugushop.domain.Stage;

public class StageEvent extends ObjectEvent {

    private Stage alternateStage;

    public StageEvent(Object source, Stage slide, Stage otherStage) {
        super(source, slide);
        this.alternateStage = otherStage;
    }

    @Override
    public Stage getEventObject() {
        return (Stage) super.getEventObject();
    }

    public Stage getAlternateStage() {
        return alternateStage;
    }

    @Override
    public String toString() {
        return "Source: '" + getSource() + "', Stage='" + getEventObject() + "'";
    }
}
