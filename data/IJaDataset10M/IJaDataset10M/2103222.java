package pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.geographicElement;

import org.apache.log4j.Logger;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.record.identification.GeographicElement;
import fi.mmm.yhteinen.swing.core.YApplicationEvent;
import fi.mmm.yhteinen.swing.core.YController;
import fi.mmm.yhteinen.swing.core.YIComponent;

/**
 * The controller for geographic element view and model.
 * 
 * @author Ant�nio Silva, Jos� Pedro Dias, David Duque
 * @version $Revision: 9175 $
 * @since 1.0
 */
public class GeographicElementController extends YController implements IMapCoordenatesListener {

    /**
	 * The geographic element view component
	 */
    private GeographicElementView geographicElementView = new GeographicElementView();

    /**
	 * The geographic element model component
	 */
    private GeographicElementModel geographicElementModel = new GeographicElementModel();

    /** logger for this class */
    private Logger logger = Logger.getLogger(GeographicElementController.class);

    /**
	 * Constructor for htmlview
	 * 
	 */
    public GeographicElementController() {
        this.setUpMVC(geographicElementModel, geographicElementView);
        this.register(Constants.translateEvent);
        this.register(Constants.helpF1Pressed);
        this.register(Constants.Refresh);
    }

    /**
	 * Handling of Global events.
	 * 
	 * @param ev
	 */
    public void receivedApplicationEvent(YApplicationEvent ev) {
        if (ev.getName() == Constants.translateEvent) {
            geographicElementView.translate();
            this.copyToView("type");
        } else if (ev.getName() == Constants.helpF1Pressed) {
            if (geographicElementView.isShowing()) {
                sendApplicationEvent(new YApplicationEvent(Constants.openBrowser, Constants.confluence + "2162763"));
            }
        } else if (ev.getName() == Constants.Refresh) {
            if (ev.getValue() instanceof GeographicElement) {
                refreshView(ev.getValue());
                sendApplicationEvent(new YApplicationEvent(Constants.changeScreen, this));
                YController.sendApplicationEvent(new YApplicationEvent(Constants.changeTreeNodeEvent, ev.getValue()));
            }
        }
    }

    /**
	 * Executed when user has changed any field in a view. 
	 * @param comp 
	 */
    public void viewChanged(YIComponent comp) {
        logger.debug("Geographic element info view changed");
        this.getParent().viewChanged(this);
    }

    /**
	 * This method is executed, if view is dirty when tab of this view will be selected.
	 * 
	 * @param refreshObject
	 */
    public void refreshView(Object refreshObject) {
        logger.debug("Refreshing geographic element");
        if (refreshObject instanceof GeographicElement) {
            geographicElementModel.setGeographicElement((GeographicElement) refreshObject);
            resetViewChanges();
            geographicElementView.addVerifiers(refreshObject);
            geographicElementView.updateBoxState();
            geographicElementView.setMapCoordenates(true);
        }
    }

    /**
	 * called when combo box changes
	 */
    public void typeChanged() {
        geographicElementView.updateBoxState();
    }

    @Override
    public void coodenatesSelected(double north, double south, double east, double west) {
        GeographicElement ge = geographicElementModel.getGeographicElement();
        ge.setNorthBoundLatitude(north);
        ge.setWestBoundLongitude(west);
        ge.setEastBoundLongitude(east);
        ge.setSouthBoundLatitude(south);
        geographicElementModel.setGeographicElement(ge);
        geographicElementView.setMapCoordenates(false);
        this.viewChanged(this);
    }
}
