package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelPositioned;

import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Simple bean which holds a list of objects which is manipulated by the
 * position panel
 */
public class PositionedPanelBean implements Serializable {

    private List people;

    public PositionedPanelBean() {
        people = new ArrayList(7);
        people.add(new PostionedPanelPerson("Mary Smith"));
        people.add(new PostionedPanelPerson("James Johnson"));
        people.add(new PostionedPanelPerson("Patricia Williams"));
        people.add(new PostionedPanelPerson("John Jones"));
        people.add(new PostionedPanelPerson("Linda Brown"));
        people.add(new PostionedPanelPerson("Robert Davis"));
        people.add(new PostionedPanelPerson("Barbara Miller"));
        resetRank();
    }

    private void resetRank() {
        for (int i = 0; i < people.size(); i++) {
            ((PostionedPanelPerson) people.get(i)).setRank(i + 1);
        }
    }

    public void changed(PanelPositionedEvent evt) {
        resetRank();
        if (evt.getOldIndex() >= 0) {
            ((PostionedPanelPerson) people.get(evt.getIndex())).getEffect().setFired(false);
        }
    }

    public List getPeople() {
        return people;
    }

    public void setPeople(List people) {
        this.people = people;
    }
}
