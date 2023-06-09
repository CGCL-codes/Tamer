package fr.crnan.videso3d;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.xpath.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import fr.crnan.videso3d.DatabaseManager.Type;
import fr.crnan.videso3d.formats.VidesoTrack;
import fr.crnan.videso3d.formats.images.ImagesController;
import fr.crnan.videso3d.geom.LatLonUtils;
import fr.crnan.videso3d.globes.EarthFlatCautra;
import fr.crnan.videso3d.globes.FlatGlobeCautra;
import fr.crnan.videso3d.graphics.Aerodrome;
import fr.crnan.videso3d.graphics.Balise;
import fr.crnan.videso3d.graphics.Balise2D;
import fr.crnan.videso3d.graphics.DatabaseVidesoObject;
import fr.crnan.videso3d.graphics.Route;
import fr.crnan.videso3d.graphics.Secteur3D;
import fr.crnan.videso3d.graphics.VPolygon;
import fr.crnan.videso3d.graphics.editor.PolygonEditorsManager;
import fr.crnan.videso3d.ihm.components.VidesoGLCanvasKeyListener;
import fr.crnan.videso3d.layers.AltitudeFilterableLayer;
import fr.crnan.videso3d.layers.Balise2DLayer;
import fr.crnan.videso3d.layers.BaliseLayer;
import fr.crnan.videso3d.layers.FrontieresStipLayer;
import fr.crnan.videso3d.layers.LayerManagerLayer;
import fr.crnan.videso3d.layers.LayerSet;
import fr.crnan.videso3d.layers.VAnnotationLayer;
import fr.crnan.videso3d.layers.VerticalScaleBar;
import fr.crnan.videso3d.stip.Stip;
import fr.crnan.videso3d.util.VMeasureTool;
import gov.nasa.worldwind.Factory;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.exception.WWRuntimeException;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Intersection;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Line;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.AirspaceLayer;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.ScalebarLayer;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.airspaces.Airspace;
import gov.nasa.worldwind.render.airspaces.Polygon;
import gov.nasa.worldwind.tracks.TrackPoint;
import gov.nasa.worldwind.util.DataConfigurationFilter;
import gov.nasa.worldwind.util.DataConfigurationUtils;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwind.util.WWXML;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;

/**
 * Extension de WorldWindCanvas prenant en compte la création d'éléments 3D
 * @author Bruno Spyckerelle
 * @version 0.9.5
 */
@SuppressWarnings("serial")
public class VidesoGLCanvas extends WorldWindowGLCanvas implements ClipboardOwner {

    /**
	 * Layer contenant les annotations
	 */
    private VAnnotationLayer annotationLayer;

    /**
	 * Layer pour les frontières
	 */
    private FrontieresStipLayer frontieres;

    /**
	 * Projection 2D
	 */
    private FlatGlobeCautra flatGlobe;

    private Globe roundGlobe;

    private String projection;

    /**
	 * Gestion des images
	 */
    private ImagesController imagesController;

    /**
	 * Outil de mesure (alidade)
	 */
    private VMeasureTool measureTool;

    private VerticalScaleBar scale;

    private DraggerListener dragger;

    private HighlightController highlightController;

    private ScreenSelectListener screenSelectListener;

    private boolean europe = false;

    /**
	 * Initialise les différents objets graphiques
	 */
    public void initialize() {
        Configuration.initializeProxy();
        ScalebarLayer scalebarLayer = new ScalebarLayer();
        scalebarLayer.setUnit(ScalebarLayer.UNIT_NAUTICAL);
        this.getModel().getLayers().add(scalebarLayer);
        Layer latlon = new LatLonGraticuleLayer();
        latlon.setEnabled(false);
        this.getModel().getLayers().add(latlon);
        Balise2DLayer coordinatesLayer = new Balise2DLayer("coordLayer");
        coordinatesLayer.setEnabled(true);
        this.getModel().getLayers().add(coordinatesLayer);
        LayerManagerLayer layerManager = new LayerManagerLayer(this);
        layerManager.setEnabled(false);
        this.getModel().getLayers().add(0, layerManager);
        this.addKeyListener(new VidesoGLCanvasKeyListener(this));
        screenSelectListener = new ScreenSelectListener(this);
        this.dragger = new DraggerListener(this);
        this.addSelectListener(dragger);
        highlightController = new HighlightController(this, SelectEvent.ROLLOVER);
        this.getSceneController().setDeepPickEnabled(true);
        firePropertyChange("step", "", "Ajout des layers installés");
        this.updateWWI();
        if (isFlatGlobe()) {
            this.flatGlobe = (FlatGlobeCautra) this.getModel().getGlobe();
            this.roundGlobe = new Earth();
        } else {
            this.flatGlobe = new EarthFlatCautra();
            this.roundGlobe = this.getModel().getGlobe();
        }
        PolygonEditorsManager.setWWD(this);
        this.getView().setEyePosition(Position.fromDegrees(47, 0, 2500e3));
    }

    public VAnnotationLayer getAnnotationLayer() {
        if (annotationLayer == null) {
            annotationLayer = new VAnnotationLayer(this);
            this.getModel().getLayers().add(annotationLayer);
        }
        return annotationLayer;
    }

    /**
	 * Affiche ou non un Layer<br />
	 * Ajouter le {@link Layer} aux layers du modèle si il n'en fait pas partie
	 * @param layer {@link Layer} à afficher/enlever
	 * @param state {@link Boolean}
	 */
    public void toggleLayer(Layer layer, Boolean state) {
        if (layer != null) {
            this.getModel().getLayers().addIfAbsent(layer);
            layer.setEnabled(state);
        }
    }

    /**
	 * Ajoute un calque à la fin de la liste. Le calque est affiché par défaut. 
	 * @param layer Calque à ajouter
	 */
    public void addLayer(Layer layer) {
        this.toggleLayer(layer, true);
    }

    /**
	 * Supprime un calque
	 * @param layer Calque à supprimer
	 */
    public void removeLayer(Layer layer) {
        this.getModel().getLayers().remove(layer);
    }

    /**
	 * Insère un layer suffisamment haut dans la liste pour être derrière les PlaceNames
	 * @param layer
	 */
    public void insertBeforePlacenames(Layer layer) {
        int position = 0;
        LayerList layers = this.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof PlaceNameLayer) position = layers.indexOf(l);
        }
        layers.add(position, layer);
    }

    public ImagesController getImagesController() {
        if (this.imagesController == null) {
            this.imagesController = new ImagesController(this);
        }
        return this.imagesController;
    }

    public HighlightController getHighlightController() {
        return highlightController;
    }

    /**
	 * @return Objects selected with CTRL+Left click
	 */
    public List<?> getSelectedObjects() {
        return screenSelectListener.getSelectedObjects();
    }

    public void copySelectedObjectsToClipboard() {
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String selection = new String("");
        for (Object o : getSelectedObjects()) {
            if (o instanceof DatabaseVidesoObject) {
                if (((DatabaseVidesoObject) o).getDatabaseType().compareTo(Type.STIP) == 0) {
                    selection += Stip.getString(((DatabaseVidesoObject) o).getType(), ((DatabaseVidesoObject) o).getName());
                    selection += "\n";
                }
            }
        }
        clipBoard.setContents(new StringSelection(selection), this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    /**
	 * Mets à jour les layers installés dans WorldWindInstalled
	 */
    public void updateWWI() {
        File installLocation = null;
        for (java.io.File f : WorldWind.getDataFileStore().getLocations()) {
            if (WorldWind.getDataFileStore().isInstallLocation(f.getPath())) {
                installLocation = f;
                break;
            }
        }
        String[] names = WWIO.listDescendantFilenames(installLocation, new DataConfigurationFilter(), false);
        if (names == null || names.length == 0) return;
        for (String filename : names) {
            Document dataConfig = null;
            try {
                File dataConfigFile = new File(installLocation, filename);
                dataConfig = WWXML.openDocument(dataConfigFile);
                dataConfig = DataConfigurationUtils.convertToStandardDataConfigDocument(dataConfig);
            } catch (WWRuntimeException e) {
                e.printStackTrace();
            }
            if (dataConfig == null) continue;
            AVList params = new AVListImpl();
            XPath xpath = WWXML.makeXPath();
            Element domElement = dataConfig.getDocumentElement();
            String s = WWXML.getText(domElement, "DataCacheName", xpath);
            if (s == null || s.length() == 0) DataConfigurationUtils.getDataConfigCacheName(filename, params);
            String type = DataConfigurationUtils.getDataConfigType(domElement);
            if (type.equalsIgnoreCase("ElevationModel")) {
                if (WWXML.getDouble(domElement, "ExtremeElevations/@min", xpath) == null) params.setValue(AVKey.ELEVATION_MIN, -11000d);
                if (WWXML.getDouble(domElement, "ExtremeElevations/@max", xpath) == null) params.setValue(AVKey.ELEVATION_MAX, 8500d);
            }
            if (DataConfigurationUtils.getDataConfigType(domElement).equalsIgnoreCase("Layer")) {
                Layer layer = null;
                try {
                    Factory factory = (Factory) WorldWind.createConfigurationComponent(AVKey.LAYER_FACTORY);
                    layer = (Layer) factory.createFromConfigSource(domElement, params);
                } catch (Exception e) {
                    String message = Logging.getMessage("generic.CreationFromDataConfigurationFailed", DataConfigurationUtils.getDataConfigDisplayName(domElement));
                    Logging.logger().log(java.util.logging.Level.SEVERE, message, e);
                }
                if (layer == null) return;
                if (!this.getModel().getLayers().contains(layer)) {
                    this.insertBeforePlacenames(layer);
                    layer.setEnabled(false);
                }
            }
        }
    }

    private double maxAltitude = 800.0 * 30.48;

    private double minAltitude = 0.0;

    public void filterLayers(double maxAltitude, double minAltitude) {
        filterLayers(this.getModel().getLayers(), maxAltitude, minAltitude);
        this.maxAltitude = maxAltitude;
        this.minAltitude = minAltitude;
    }

    private void filterLayers(Iterable<Layer> layers, double maxAltitude, double minAltitude) {
        Iterator<Layer> iterator = layers.iterator();
        while (iterator.hasNext()) {
            Layer l = iterator.next();
            if (l instanceof AltitudeFilterableLayer) {
                if (this.minAltitude != minAltitude) {
                    ((AltitudeFilterableLayer) l).setMinimumViewableAltitude(minAltitude);
                }
                if (this.maxAltitude != maxAltitude) {
                    ((AltitudeFilterableLayer) l).setMaximumViewableAltitude(maxAltitude);
                }
            } else if (l instanceof LayerSet) {
                filterLayers((LayerList) l, maxAltitude, minAltitude);
            }
        }
    }

    public VMeasureTool getMeasureTool() {
        if (measureTool == null) {
            measureTool = new VMeasureTool(this);
        }
        return measureTool;
    }

    public void switchMeasureTool(Boolean bool) {
        this.getMeasureTool().setArmed(bool);
        ((Component) this).setCursor(!measureTool.isArmed() ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        if (!bool) {
            this.getMeasureTool().clear();
        }
    }

    /**
	 * Change la projection
	 * @param projection Nom de la projection (parmi {@link FlatGlobeCautra})
	 */
    public void setProjection(String projection) {
        this.projection = projection;
        if (flatGlobe != null) this.flatGlobe.setProjection(projection);
        if (isFlatGlobe()) this.redraw();
    }

    public boolean isFlatGlobe() {
        return this.getModel().getGlobe() instanceof FlatGlobeCautra;
    }

    public String getProjection() {
        return projection;
    }

    /**
	 * Active la vue 2D
	 * @param flat
	 */
    public void enableFlatGlobe(boolean flat) {
        if (isFlatGlobe() == flat) return;
        if (!flat) {
            this.getModel().setGlobe(roundGlobe);
            FlatOrbitView flatOrbitView = (FlatOrbitView) this.getView();
            BasicOrbitView orbitView = new BasicOrbitView();
            orbitView.setCenterPosition(flatOrbitView.getCenterPosition());
            orbitView.setZoom(flatOrbitView.getZoom());
            orbitView.setHeading(flatOrbitView.getHeading());
            orbitView.setPitch(flatOrbitView.getPitch());
            this.setView(orbitView);
            LayerList layers = this.getModel().getLayers();
            for (int i = 0; i < layers.size(); i++) {
                if (layers.get(i) instanceof SkyColorLayer) layers.set(i, new SkyGradientLayer());
            }
        } else {
            this.getModel().setGlobe(flatGlobe);
            flatGlobe.setProjection(this.getProjection());
            BasicOrbitView orbitView = (BasicOrbitView) this.getView();
            FlatOrbitView flatOrbitView = new FlatOrbitView();
            flatOrbitView.setCenterPosition(orbitView.getCenterPosition());
            flatOrbitView.setZoom(orbitView.getZoom());
            flatOrbitView.setHeading(orbitView.getHeading());
            flatOrbitView.setPitch(orbitView.getPitch());
            this.setView(flatOrbitView);
            LayerList layers = this.getModel().getLayers();
            for (int i = 0; i < layers.size(); i++) {
                if (layers.get(i) instanceof SkyGradientLayer) layers.set(i, new SkyColorLayer());
            }
        }
        this.redraw();
    }

    /**
	 * Affiche ou non le fond uni suivant les frontières Stip
	 * @param toggle
	 */
    public void toggleFrontieres(Boolean toggle) {
        if (frontieres == null) {
            frontieres = new FrontieresStipLayer();
            this.insertBeforePlacenames(frontieres);
        }
        if (toggle) {
            if (europe) {
                frontieres.setEurope();
            } else {
                frontieres.setFrance();
            }
        } else {
            frontieres.removeFond();
        }
        this.redrawNow();
    }

    public void setFrontieresEurope(boolean b) {
        europe = b;
    }

    /**
	 * Nombre d'étapes de l'initialisation (utile pour le splashscreen)
	 * @return int
	 */
    public int getNumberInitSteps() {
        return 1;
    }

    /**
	 * Recentre la vue
	 */
    public void resetView() {
        if (this.annotationLayer != null) this.annotationLayer.removeAllAnnotations();
        this.getView().stopMovement();
        this.getView().setEyePosition(Position.fromDegrees(47, 0, 2500e3));
        this.getView().setPitch(Angle.ZERO);
        this.getView().setHeading(Angle.ZERO);
        this.redraw();
    }

    /**
	 * Computes a position included in the upper polygon
	 * @param point ScreenPoint
	 * @param refObject Vpolygon
	 * @return
	 */
    public Position computePositionFromScreenPoint(Point point, VPolygon refObject) {
        Position pos = this.computePositionFromScreenPoint(point, (Airspace) refObject);
        if (!refObject.contains(pos)) {
            Position groundPosition = new Position(pos.getLatitude(), pos.getLongitude(), 0);
            double distance = LatLonUtils.computeDistance(groundPosition, new Position(refObject.getLocations().get(0), 0), this.getModel().getGlobe());
            for (LatLon loc : refObject.getLocations()) {
                double newDistance = LatLonUtils.computeDistance(groundPosition, new Position(loc, 0), this.getModel().getGlobe());
                if (newDistance < distance) {
                    distance = newDistance;
                    groundPosition = new Position(loc, 0);
                }
            }
            pos = new Position(groundPosition, refObject.getAltitudes()[1]);
        }
        return pos;
    }

    public Position computePositionFromScreenPoint(Point point, Airspace refObject) {
        Line ray = this.getView().computeRayFromScreenPoint(point.x, point.y);
        Intersection inters[] = this.getModel().getGlobe().intersect(ray, refObject.getAltitudes()[1]);
        Position pos = this.getModel().getGlobe().computePositionFromPoint(inters[0].getIntersectionPoint());
        return pos;
    }

    public Position centerView(Object object) {
        getView().setValue(AVKey.ELEVATION, 1e11);
        double[] eyePosition = this.computeBestEyePosition(object);
        Position centerPosition = null;
        if (eyePosition.length > 1) {
            centerPosition = Position.fromDegrees(eyePosition[0], eyePosition[1]);
            getView().setHeading(Angle.ZERO);
            getView().setPitch(Angle.ZERO);
            BasicOrbitView bov = (BasicOrbitView) getView();
            bov.addPanToAnimator(centerPosition, bov.getHeading(), bov.getPitch(), eyePosition[2], 2000, true);
            bov.firePropertyChange(AVKey.VIEW, null, bov);
        }
        return centerPosition;
    }

    /**
	 * Calcule l'altitude à laquelle doit se trouver la caméra pour voir correctement l'objet, 
	 * et la position sur laquelle elle doit être centrée. 
	 * @param object
	 * @return un tableau contenant dans l'ordre : la latitude, la longitude et l'altitude et l'inclinaison de la caméra (pitch).
	 */
    @SuppressWarnings("unchecked")
    public double[] computeBestEyePosition(Object object) {
        if (object instanceof Secteur3D) {
            return computeBestEyePosition((Secteur3D) object);
        } else if (object instanceof Balise2D) {
            return computeBestEyePosition((Balise2D) object);
        } else if (object instanceof Aerodrome) {
            Position ref = ((Aerodrome) object).getRefPosition();
            return new double[] { ref.latitude.degrees, ref.longitude.degrees, 50000 };
        } else if (object instanceof VidesoTrack) {
            return computeBestEyePosition((VidesoTrack) object);
        } else if (object instanceof Route) {
            ArrayList<Route> list = new ArrayList<Route>();
            list.add((Route) object);
            return computeBestEyePosition(list);
        } else if (object instanceof List) {
            if (((List<?>) object).get(0) instanceof Route) {
                return computeBestEyePosition((List<? extends Route>) object);
            } else if (((List<?>) object).get(0) instanceof Aerodrome) {
                Position ref = ((Aerodrome) ((List<?>) object).get(0)).getRefPosition();
                return new double[] { ref.latitude.degrees, ref.longitude.degrees, 50000 };
            }
        }
        return null;
    }

    /**
	 * Calcule l'altitude à laquelle doit se trouver l'oeil pour voir correctement la zone.
	 * @param zone
	 * @return 
	 */
    public double[] computeBestEyePosition(Secteur3D zone) {
        List<LatLon> locations = zone.getLocations();
        Angle latMin = locations.get(0).latitude;
        Angle latMax = latMin;
        Angle lonMin = locations.get(0).longitude;
        Angle lonMax = lonMin;
        int step = 1;
        if (locations.size() > 200) step = locations.size() / 200 + 1;
        for (int i = 0; i < locations.size(); i += step) {
            Angle lat = locations.get(i).latitude;
            Angle lon = locations.get(i).longitude;
            if (lat.compareTo(latMax) > 0) {
                latMax = lat;
            }
            if (lat.compareTo(latMin) < 0) {
                latMin = lat;
            }
            if (lon.compareTo(lonMax) > 0) {
                lonMax = lon;
            }
            if (lon.compareTo(lonMin) < 0) {
                lonMin = lon;
            }
        }
        double er = this.getView().getGlobe().getEquatorialRadius();
        double pr = this.getView().getGlobe().getPolarRadius();
        double maxDistance = LatLon.ellipsoidalDistance(new LatLon(latMin, lonMin), new LatLon(latMax, lonMax), er, pr);
        double elevation = computeBestElevation(maxDistance);
        return new double[] { (latMin.degrees + latMax.degrees) / 2, (lonMin.degrees + lonMax.degrees) / 2, Math.min(elevation, 2.5e6) };
    }

    /**
	 * Renvoie l'altitude à laquelle doit se trouver l'oeil pour voir correctement la balise.
	 * @param navFix
	 * @return
	 */
    public double[] computeBestEyePosition(Balise2D navFix) {
        return new double[] { navFix.getPosition().latitude.degrees, navFix.getPosition().longitude.degrees, 2e5 };
    }

    /**
	 * Calcule l'altitude à laquelle doit se trouver l'oeil pour voir correctement la route.
	 * @param segments
	 * @return 
	 */
    @SuppressWarnings("unchecked")
    public double[] computeBestEyePosition(List<? extends Route> segments) {
        LatLon firstLocation = (LatLon) ((Route) segments.get(0)).getLocations().iterator().next();
        LatLon lastLocation = null;
        if (segments.size() > 1) {
            lastLocation = ((Route) segments.get(segments.size() - 1)).getLocations().iterator().next();
        } else {
            Iterator<LatLon> it = (Iterator<LatLon>) ((Route) segments.get(0)).getLocations().iterator();
            while (it.hasNext()) {
                lastLocation = it.next();
            }
        }
        LatLon middleSegmentLocation = ((Route) segments.get(segments.size() / 2)).getLocations().iterator().next();
        double elevation = computeBestElevation(firstLocation, lastLocation);
        return new double[] { middleSegmentLocation.latitude.degrees, middleSegmentLocation.longitude.degrees, Math.min(elevation, 2.5e6) };
    }

    public double[] computeBestEyePosition(VidesoTrack track) {
        LinkedList<TrackPoint> points = new LinkedList<TrackPoint>();
        points.addAll(track.getTrackPoints());
        Position p1 = points.getFirst().getPosition();
        Position p2 = points.getLast().getPosition();
        Position middle = points.get(track.getNumPoints() / 2).getPosition();
        double elevation = computeBestElevation(p1, p2);
        return new double[] { middle.latitude.degrees, middle.longitude.degrees, Math.min(elevation, 6e8) };
    }

    private double computeBestElevation(LatLon firstLocation, LatLon lastLocation) {
        double er = this.getView().getGlobe().getEquatorialRadius();
        double pr = this.getView().getGlobe().getPolarRadius();
        double distance = LatLon.ellipsoidalDistance(firstLocation, lastLocation, er, pr);
        return computeBestElevation(distance);
    }

    public double computeBestElevation(double distance) {
        return -2e-6 * distance * distance + 2.3945 * distance + 175836;
    }

    public DraggerListener getDraggerListener() {
        return this.dragger;
    }

    public void activateVerticalScaleBar(Boolean state) {
        if (scale == null) {
            scale = new VerticalScaleBar(this);
            this.addSelectListener(scale);
        }
        if (state) {
            scale.initializePosition(this.getView().getGlobe().computePositionFromPoint(this.getView().getCenterPoint()));
            this.toggleLayer(scale, true);
            this.addSelectListener(scale);
        } else {
            this.toggleLayer(scale, false);
            this.removeSelectListener(scale);
        }
    }

    public void delete(Object o) {
        if (o instanceof Airspace) {
            this.deleteAirspace((Airspace) o);
        } else if (o instanceof Balise) {
            this.deleteBalise((Balise) o);
        } else if (o instanceof Path) {
            this.deletePath((Path) o);
        }
    }

    public void deleteBalise(Balise balise) {
        if (balise instanceof DatabaseVidesoObject) {
            DatasManager.getController(((DatabaseVidesoObject) balise).getDatabaseType()).hideObject(((DatabaseVidesoObject) balise).getType(), ((DatabaseVidesoObject) balise).getName());
        } else {
            for (Layer l : this.getModel().getLayers()) {
                if (l instanceof BaliseLayer) {
                    ((BaliseLayer) l).removeBalise(balise);
                }
            }
        }
    }

    public void deleteAirspace(Airspace airspace) {
        if (airspace instanceof DatabaseVidesoObject) {
            DatasManager.getController(((DatabaseVidesoObject) airspace).getDatabaseType()).hideObject(((DatabaseVidesoObject) airspace).getType(), ((DatabaseVidesoObject) airspace).getName());
        } else {
            for (Layer l : this.getModel().getLayers()) {
                if (l instanceof AirspaceLayer) {
                    ((AirspaceLayer) l).removeAirspace(airspace);
                }
            }
            if (airspace instanceof Polygon) {
                PolygonEditorsManager.stopEditAirspace((Polygon) airspace);
            }
        }
    }

    public void deletePath(Path p) {
        p.setVisible(false);
    }
}
