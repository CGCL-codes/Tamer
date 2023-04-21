package jfigure.geom2D.action;

import jfigure.commons.mediator.Mediator;
import jfigure.commons.*;
import jfigure.commons.event.PainterEvent;
import jfigure.geom2D.*;
import jfigure.geom2D.transformation.*;

/**
 * Actions sur un point
 */
public class PointAction {

    /**
     * Cr�ation des actions sur un point
     * 
     * @param mediator Le m�diateur
     * @param pt Le point
     * @return La liste des actions sur le point
     */
    public static final DisplayableActionList createActionList(Mediator m, JfigurePoint pt) {
        DisplayableActionList list = new DisplayableActionList();
        final Mediator mediator = m;
        final JfigurePoint point = pt;
        DisplayableAction action = new DisplayableAction(mediator, "Cr�er une rotation centr�e en " + point.getName()) {

            public void invoke() {
                Rotation r = new Rotation(point);
                ApplicationProperties props = r.getProperties();
                ApplicationEditionDialog dialog = new ApplicationEditionDialog(mediator);
                dialog.setTitle("Cr�ation d'une rotation");
                dialog.initWithApplicationProperties(props);
                dialog.show();
                if (!dialog.isOk()) return;
                r.setProperties(props);
                mediator.getPainter().addApplication(r);
                PainterEvent evt = new PainterEvent(mediator.getPainter());
                for (int i = 0; i < mediator.getPainter().getPainterListeners().length; i++) {
                    mediator.getPainter().getPainterListeners()[i].applicationHasBeenAdded(evt);
                }
            }
        };
        action.setIcon(MainPanel.image("Rotation"));
        list.addAction(action);
        action = new DisplayableAction(mediator, "Cr�er une sym�trie centrale centr�e en " + point.getName()) {

            public void invoke() {
                CentralSymetry r = new CentralSymetry(point);
                ApplicationProperties props = r.getProperties();
                ApplicationEditionDialog dialog = new ApplicationEditionDialog(mediator);
                dialog.setTitle("Cr�ation d'une sym�trie centrale");
                dialog.initWithApplicationProperties(props);
                dialog.pack();
                dialog.setSize(300, 200);
                dialog.show();
                if (!dialog.isOk()) return;
                r.setProperties(props);
                mediator.getPainter().addApplication(r);
                PainterEvent evt = new PainterEvent(mediator.getPainter());
                for (int i = 0; i < mediator.getPainter().getPainterListeners().length; i++) {
                    mediator.getPainter().getPainterListeners()[i].applicationHasBeenAdded(evt);
                }
            }
        };
        action.setIcon(MainPanel.image("SymetrieCentrale"));
        list.addAction(action);
        action = new DisplayableAction(mediator, "Cr�er une homoth�tie centr�e en " + point.getName()) {

            public void invoke() {
                Homothetie h = new Homothetie(point);
                ApplicationProperties props = h.getProperties();
                ApplicationEditionDialog dialog = new ApplicationEditionDialog(mediator);
                dialog.setTitle("Cr�ation d'une homoth�tie");
                dialog.initWithApplicationProperties(props);
                dialog.pack();
                dialog.setSize(300, 200);
                dialog.show();
                if (!dialog.isOk()) return;
                h.setProperties(props);
                mediator.getPainter().addApplication(h);
                PainterEvent evt = new PainterEvent(mediator.getPainter());
                for (int i = 0; i < mediator.getPainter().getPainterListeners().length; i++) {
                    mediator.getPainter().getPainterListeners()[i].applicationHasBeenAdded(evt);
                }
            }
        };
        action.setIcon(MainPanel.image("Homothetie"));
        list.addAction(action);
        action = new DisplayableAction(mediator, "Cr�er un cercle centr� en " + point.getName()) {

            public void invoke() {
                Circle c = new Circle(point, 1.0);
                FigureUtility.showCreationDialog(c, mediator);
            }
        };
        action.setIcon(MainPanel.image("Cercle"));
        list.addAction(action);
        action = new DisplayableAction(mediator, "Cr�er un secteur centr� en " + point.getName()) {

            public void invoke() {
                Sector s = new Sector(point, 1.0);
                FigureUtility.showCreationDialog(s, mediator);
            }
        };
        action.setIcon(MainPanel.image("Secteur"));
        list.addAction(action);
        return list;
    }
}
