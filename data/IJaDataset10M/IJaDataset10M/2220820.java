package org.gruposp2p.aula.gwt.client.event;

import org.gruposp2p.aula.gwt.client.model.Score;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ScoreChangeEvent extends GwtEvent {

    private static final GwtEvent.Type TYPE = new GwtEvent.Type();

    private Score score;

    public static GwtEvent.Type getType() {
        return TYPE;
    }

    @Override
    public Type getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EventHandler handler) {
        ((ScoreChangeHandler) handler).processScore(this);
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Score getScore() {
        return score;
    }
}
