package de.uniba.kinf.cityguide.controller.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import de.uniba.kinf.cityguide.R;
import de.uniba.kinf.cityguide.controller.IDataController;
import de.uniba.kinf.cityguide.controller.IGeoManagerController;
import de.uniba.kinf.cityguide.controller.IRecommenderController;
import de.uniba.kinf.cityguide.controller.ISystemController;
import de.uniba.kinf.cityguide.controller.data.CGDataController;
import de.uniba.kinf.cityguide.controller.geomanager.CGGeoManagerController;
import de.uniba.kinf.cityguide.controller.recommender.CGRecommenderController;
import de.uniba.kinf.cityguide.controller.recommender.CGRecommenderCore;
import de.uniba.kinf.cityguide.controller.system.bootstrap.dataimport.DataImportController;
import de.uniba.kinf.cityguide.controller.system.bootstrap.dataimport.IDataImportController;
import de.uniba.kinf.cityguide.entities.CGPOI;
import de.uniba.kinf.cityguide.entities.CGRating;
import de.uniba.kinf.cityguide.entities.CGTour;
import de.uniba.kinf.cityguide.gui.ListContextMenu;
import de.uniba.kinf.cityguide.gui.ListMenu;
import de.uniba.kinf.cityguide.gui.MapMenu;
import de.uniba.kinf.cityguide.gui.MenuRegistry;
import de.uniba.kinf.cityguide.gui.MenuRegistry.CGMenuName;

/**
 * 
 * @author Thomas Hieber
 *
 */
public class CGSystemController implements ISystemController {

    private Context ctx;

    private IDataImportController dataImportC;

    private IDataController dataC;

    private IGeoManagerController geoC;

    private IRecommenderController recC;

    private CGTour currentTour;

    private int intentCounter;

    private Location nextTargetLocation = new Location("Next Target");

    private InputStream poiInputFile;

    private InputStream taxInputFile;

    private InputStream entryPointsInputFile;

    private InputStream neighboursFile;

    private MenuRegistry menuRegistry;

    public CGSystemController(Context ctx) {
        poiInputFile = ctx.getResources().openRawResource(R.raw.bamberg);
        taxInputFile = ctx.getResources().openRawResource(R.raw.taxonomy);
        entryPointsInputFile = ctx.getResources().openRawResource(R.raw.entry_points);
        neighboursFile = ctx.getResources().openRawResource(R.raw.neighbours);
        this.ctx = ctx;
        this.dataC = new CGDataController(ctx, taxInputFile, entryPointsInputFile, neighboursFile);
        this.dataImportC = new DataImportController(poiInputFile, entryPointsInputFile, neighboursFile, ctx, this.dataC);
        this.geoC = new CGGeoManagerController(this.dataC);
        this.recC = new CGRecommenderController(this.dataC, this.geoC);
        this.menuRegistry = new MenuRegistry();
    }

    @Override
    public CGTour enlargeTour(Location startLocation, int amount) {
        this.currentTour = this.recC.enlargeTour(startLocation, currentTour, amount);
        makeRanking();
        return this.currentTour;
    }

    @Override
    public CGTour generateTour(Location startLocation, int tourLength) {
        CGTour retVal = new CGTour(tourLength);
        this.currentTour = this.recC.generateTour(startLocation);
        makeRanking();
        return currentTour;
    }

    @Override
    public boolean ratePOI(int poiId, CGRating rating) {
        try {
            this.currentTour.getPoiById(poiId).setRated(true);
        } catch (NullPointerException e) {
            Log.d(CGSystemController.this.toString(), e.getMessage());
        }
        return this.recC.ratePOI(poiId, rating);
    }

    @Override
    public CGTour shortenTour(Location startLocation, int poiID) {
        this.currentTour = this.recC.shortenTour(startLocation, currentTour, poiID);
        makeRanking();
        return this.currentTour;
    }

    @Override
    public void startTour() {
        this.menuRegistry.toggleMenuValue(CGMenuName.MAP_MAIN, MapMenu.GENERATE_TOUR.ordinal(), false);
        this.menuRegistry.toggleMenuValue(CGMenuName.MAP_MAIN, MapMenu.START_TOUR.ordinal(), false);
        this.menuRegistry.toggleMenuValue(CGMenuName.MAP_MAIN, MapMenu.STOP_TOUR.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_MAIN, ListMenu.MAP_VIEW.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_MAIN, ListMenu.ENLARGE.ordinal(), false);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_CONTEXT, ListContextMenu.DETAILS.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_CONTEXT, ListContextMenu.RATE.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_CONTEXT, ListContextMenu.REMOVE.ordinal(), false);
        this.currentTour.setTour_started(true);
    }

    @Override
    public void stop_tour() {
        this.menuRegistry.toggleMenuValue(CGMenuName.MAP_MAIN, MapMenu.GENERATE_TOUR.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.MAP_MAIN, MapMenu.START_TOUR.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.MAP_MAIN, MapMenu.STOP_TOUR.ordinal(), false);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_MAIN, ListMenu.MAP_VIEW.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_MAIN, ListMenu.ENLARGE.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_CONTEXT, ListContextMenu.DETAILS.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_CONTEXT, ListContextMenu.RATE.ordinal(), true);
        this.menuRegistry.toggleMenuValue(CGMenuName.LIST_CONTEXT, ListContextMenu.REMOVE.ordinal(), true);
        this.currentTour.setTour_started(false);
    }

    public int test() {
        return this.dataC.getSectionCountOfCity();
    }

    @Override
    public void initialImport() {
        try {
            this.dataImportC.writeIntoDB();
            Log.i(this.getClass().toString(), "Sucessfully imported POI data");
            this.dataImportC.writeAdjacencyIntoDB();
            Log.i(this.getClass().toString(), "Successfully imported adjacency data");
            this.dataImportC.writeEntryPointsIntoDB();
            Log.i(this.getClass().toString(), "Successfully imported entrypoint information");
            this.dataImportC.writeCategoryOfPoisIntoDB();
            Log.i(this.getClass().toString(), "Successfully imported categories of all pois");
        } catch (ParserConfigurationException e) {
            Log.e(this.getClass().toString(), e.getMessage());
        } catch (SAXException e) {
            Log.e(this.getClass().toString(), e.getMessage());
        } catch (IOException e) {
            Log.e(this.getClass().toString(), e.getMessage());
        }
    }

    @Override
    public boolean unratedPOILeft() {
        for (CGPOI poi : this.currentTour.getPOIs()) {
            if (poi.isRated() == false) {
                return true;
            }
        }
        return false;
    }

    public boolean initialImportNecessary() {
        CGPOI pp = this.dataC.getPOI(1150);
        if (pp.getID() != 1150) {
            Log.d(CGSystemController.class.getCanonicalName(), "data import required");
            return true;
        } else return false;
    }

    public CGTour getCurrentTour() {
        return this.currentTour;
    }

    @Override
    public MenuRegistry getMenuRegistry() {
        return this.menuRegistry;
    }

    public Location getNextTargetLocation() {
        return this.nextTargetLocation;
    }

    @Override
    public void setNextTargetLocation(Location loc) {
        this.nextTargetLocation = loc;
    }

    /**
	 * <p>add a number to the poi according to the poi's number in the tour</p>
	 */
    private void makeRanking() {
        int counter = 1;
        for (CGPOI poi : this.currentTour.getPOIs()) {
            poi.setTourPosition(counter);
            counter++;
        }
    }
}
