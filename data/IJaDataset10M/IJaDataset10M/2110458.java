package jfigure.geom2D.action;

import jfigure.commons.event.PainterEvent;
import jfigure.commons.mediator.Mediator;
import jfigure.commons.*;
import jfigure.geom2D.*;
import jfigure.geom2D.transformation.ApplicationEditionDialog;
import jfigure.geom2D.transformation.ApplicationProperties;
import jfigure.geom2D.transformation.Translation;

/**
 * Actions sur un vecteur
 */
public class VectorAction {

    /**
     * Cr�ation des actions sur un vecteur
     * 
     * @param mediator Le m�diateur
     * @param c Le vecteur
     * @return La liste des actions sur le vecteur
     */
    public static final DisplayableActionList createActionList(Mediator m, JfigureVector v) {
        DisplayableActionList list = new DisplayableActionList();
        final Mediator mediator = m;
        final JfigureVector vector = v;
        DisplayableAction action = new DisplayableAction(mediator, "Cr�er une translation de vecteur " + vector.getName()) {

            public void invoke() {
                Translation t = new Translation(vector);
                ApplicationProperties props = t.getProperties();
                ApplicationEditionDialog dialog = new ApplicationEditionDialog(mediator);
                dialog.setTitle("Cr�ation d'une translation");
                dialog.initWithApplicationProperties(props);
                dialog.show();
                t.setProperties(props);
                mediator.getPainter().addApplication(t);
                PainterEvent evt = new PainterEvent(mediator.getPainter());
                for (int i = 0; i < mediator.getPainter().getPainterListeners().length; i++) {
                    mediator.getPainter().getPainterListeners()[i].applicationHasBeenAdded(evt);
                }
            }
        };
        action.setIcon(MainPanel.image("Translation"));
        list.addAction(action);
        return list;
    }
}
