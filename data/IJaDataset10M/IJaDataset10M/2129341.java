package de.hpi.eworld.activitygen.model;

import java.util.Observable;
import java.util.Observer;
import javax.swing.SpinnerModel;
import de.hpi.eworld.activitygen.model.PropertyAccessor.AccessorType;
import de.hpi.eworld.model.db.data.EdgeModel;
import de.hpi.eworld.model.db.data.WayModel;
import de.hpi.eworld.networkview.GraphController;
import de.hpi.eworld.networkview.objects.WayView;
import de.hpi.eworld.observer.NotificationType;
import de.hpi.eworld.observer.ObserverNotification;

/**
 * Helper class that collects the number models for the edge attributes.
 * 
 * The models work on the complete list of edges. This makes it possible to
 * modify the attributes of several edges at once and to offer a clean simple
 * user interface.
 * 
 * @author Simon Siemens
 */
public class EdgesPropertyModels implements Observer {

    /**
	 * Sets up the models.
	 */
    public EdgesPropertyModels() {
    }

    /**
	 * Number model for a selection of inhabitantDensities.
	 */
    private SetNumberModel<EdgeModel, Double> inhabitantDensitiesModel = new SetNumberModel<EdgeModel, Double>(new PropertyAccessor<EdgeModel, Double>("inhabitantDensity", EdgeModel.class, Double.class, AccessorType.DOUBLEACCESSORS), 0.0, 0.0, 1000.0, 0.1);

    /**
	 * Number model for a selection of work place densities.
	 */
    private SetNumberModel<EdgeModel, Double> workplaceDensitiesModel = new SetNumberModel<EdgeModel, Double>(new PropertyAccessor<EdgeModel, Double>("workplaceDensity", EdgeModel.class, Double.class, AccessorType.DOUBLEACCESSORS), 0.0, 0.0, 1000.0, 0.1);

    /**
	 * Clears the sets of all set models.
	 */
    private void clearSelection() {
        inhabitantDensitiesModel.clear();
        workplaceDensitiesModel.clear();
    }

    /**
	 * Provides a number model for a selection of inhabitant densities.
	 * 
	 * The number model helps to connect user interface elements (like a
	 * JSpinner) to this model package. It provides the inhabitant densities of
	 * the selected edges, if they agree, or an empty string. And it sets the
	 * inhabitant densities of all selected edges to the same value.
	 * 
	 * @return a number model for the inhabitant densities of the selected edges
	 */
    public SpinnerModel getInhabitantDensitiesModel() {
        return inhabitantDensitiesModel;
    }

    /**
	 * Provides a number model for a selection of work place densities.
	 * 
	 * The number model helps to connect user interface elements (like a
	 * JSpinner) to this model package. It provides the work place densities of
	 * the selected edges, if they agree, or an empty string. And it sets the
	 * work place densities of all selected edges to the same value.
	 * 
	 * @return a number model for the work place densities of the selected edges
	 */
    public SpinnerModel getWorkplaceDensitiesModel() {
        return workplaceDensitiesModel;
    }

    /**
	 * Processes an itemClicked event of the network view on a way view.
	 * 
	 * It de-selects all elements that were selected before. Then it determines
	 * the selected edges and adds them to the lists of the models.
	 * 
	 * @param wayView
	 */
    private void onWayClicked(WayView wayView) {
        clearSelection();
        WayModel way = wayView.getModelElement();
        for (EdgeModel e : way.getForwardEdges()) {
            inhabitantDensitiesModel.add(e);
            workplaceDensitiesModel.add(e);
        }
        for (EdgeModel e : way.getBackwardEdges()) {
            inhabitantDensitiesModel.add(e);
            workplaceDensitiesModel.add(e);
        }
    }

    /**
	 * Processes the click events of the network view.
	 * 
	 * The selected items determine the values of the number models this class
	 * provides.
	 * 
	 * @param o
	 *            the observable object; the {@link GraphController} here.
	 * @param arg
	 *            a {@link ObserverNotification} of the network view
	 */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ObserverNotification) {
            final ObserverNotification notification = (ObserverNotification) arg;
            final NotificationType type = notification.getType();
            switch(type) {
                case itemClicked:
                    Object item = notification.getObj1();
                    if (item instanceof WayView) {
                        onWayClicked((WayView) item);
                    } else {
                        clearSelection();
                    }
                    break;
                case freeSpaceClicked:
                    clearSelection();
                    break;
            }
        }
    }
}
