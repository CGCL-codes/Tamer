package fr.crnan.videso3d.exsa;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import fr.crnan.videso3d.Couple;
import fr.crnan.videso3d.DatabaseManager;
import fr.crnan.videso3d.DatasManager;
import fr.crnan.videso3d.Pallet;
import fr.crnan.videso3d.VidesoController;
import fr.crnan.videso3d.VidesoGLCanvas;
import fr.crnan.videso3d.DatabaseManager.Type;
import fr.crnan.videso3d.geom.LatLonCautra;
import fr.crnan.videso3d.graphics.Cylinder;
import fr.crnan.videso3d.graphics.DatabaseCylinder;
import fr.crnan.videso3d.graphics.DatabaseRadar;
import fr.crnan.videso3d.graphics.DatabaseSimpleStack3D;
import fr.crnan.videso3d.graphics.Radar;
import fr.crnan.videso3d.graphics.SimpleStack3D;
import fr.crnan.videso3d.layers.MosaiqueLayer;
import gov.nasa.worldwind.Restorable;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceShape;
import gov.nasa.worldwind.render.airspaces.AirspaceAttributes;
import gov.nasa.worldwind.render.airspaces.BasicAirspaceAttributes;

/**
 * Contrôle l'affichage des éléments Exsa
 * @author Bruno Spyckerelle
 * @version 0.2.1
 */
public class STRController implements VidesoController {

    /**
	 * Liste des layers Mosaiques
	 */
    private HashMap<String, MosaiqueLayer> mosaiquesLayer = new HashMap<String, MosaiqueLayer>();

    /**
	 * Layer pour les radars et les stacks
	 */
    private RenderableLayer renderableLayer = new RenderableLayer();

    {
        renderableLayer.setName("EXSA");
    }

    /**
	 * Liste des radars et stacks affichés
	 */
    private HashMap<String, Renderable> renderables = new HashMap<String, Renderable>();

    private VidesoGLCanvas wwd;

    private Boolean flat = true;

    public static final int MOSAIQUE = 4;

    public static final int MOSAIQUE_VVF = 0;

    public static final int MOSAIQUE_ZOCC = 1;

    public static final int MOSAIQUE_DYN = 2;

    public static final int MOSAIQUE_CAPA = 3;

    public static final int RADAR = 5;

    public static final int STACK = 6;

    public static final int TMA_F = 7;

    public static final int TMA_F_M = 8;

    public STRController(VidesoGLCanvas wwd) {
        this.wwd = wwd;
        this.wwd.firePropertyChange("step", "", "Création des éléments STR");
    }

    @Override
    public void highlight(int type, String name) {
        this.showObject(type, name);
    }

    @Override
    public void unHighlight(int type, String name) {
    }

    @Override
    public void addLayer(String name, Layer layer) {
    }

    @Override
    public void removeLayer(String name, Layer layer) {
    }

    @Override
    public void removeAllLayers() {
        for (Layer l : mosaiquesLayer.values()) {
            this.wwd.removeLayer(l);
        }
        mosaiquesLayer.clear();
        this.wwd.removeLayer(renderableLayer);
    }

    @Override
    public void toggleLayer(Layer layer, Boolean state) {
        this.wwd.toggleLayer(layer, state);
    }

    @Override
    public void showObject(int type, String name) {
        switch(type) {
            case MOSAIQUE:
                this.toggleLayer(this.createMosaiqueLayer(type, name), true);
                break;
            case MOSAIQUE_CAPA:
                this.toggleLayer(this.createMosaiqueLayer(type, name), true);
                break;
            case MOSAIQUE_DYN:
                this.toggleLayer(this.createMosaiqueLayer(type, name), true);
                break;
            case MOSAIQUE_ZOCC:
                this.toggleLayer(this.createMosaiqueLayer(type, name), true);
                break;
            case MOSAIQUE_VVF:
                this.toggleLayer(this.createMosaiqueLayer(type, name), true);
                break;
            case RADAR:
                if (!renderables.containsKey(type + "-" + name)) {
                    try {
                        Statement st = DatabaseManager.getCurrentExsa();
                        ResultSet rs = st.executeQuery("select * from radrgener, radrtechn where radrgener.name = radrtechn.name and radrgener.name ='" + name + "'");
                        if (rs.next()) {
                            DatabaseRadar radar = new DatabaseRadar(name, LatLon.fromDegrees(rs.getDouble("latitude"), rs.getDouble("longitude")), rs.getInt("portee"), DatabaseManager.Type.EXSA, STRController.RADAR);
                            radar.setAnnotation("<html><b>Radar : " + name + "</b><br /><br />" + "Portée : " + rs.getInt("portee") + "NM<br />" + "Numéro : " + rs.getInt("numero") + "<br />" + "Code pays : " + rs.getInt("codepays") + "<br />" + "Code radar : " + rs.getInt("coderadar") + "<br />" + "Tour d'antenne : " + rs.getInt("vitesse") + "s<br />" + "</html>");
                            renderableLayer.addRenderable(radar);
                            renderables.put(type + "-" + name, radar);
                            this.toggleLayer(renderableLayer, true);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    ((SurfaceShape) this.renderables.get(type + "-" + name)).setVisible(true);
                }
                this.renderableLayer.firePropertyChange(AVKey.LAYER, null, this.renderableLayer);
                break;
            case STACK:
                if (!renderables.containsKey(type + "-" + name)) {
                    try {
                        Statement st = DatabaseManager.getCurrentExsa();
                        ResultSet rs = st.executeQuery("select * from centstack where name='" + name + "'");
                        if (rs.next()) {
                            DatabaseSimpleStack3D stack = new DatabaseSimpleStack3D(name, LatLon.fromDegrees(rs.getDouble("latitude"), rs.getDouble("longitude")), rs.getDouble("rayonint"), rs.getDouble("rayonext"), rs.getInt("flinf"), rs.getInt("flsup"), Type.EXSA, STACK);
                            stack.setAnnotation("<html><b>Stack : " + name + "</b><br /><br />" + "Type : " + rs.getString("type") + "<br />" + "Rayon : " + rs.getInt("rayonint") + " NM<br />" + "Rayon protection : " + rs.getInt("rayonext") + " NM<br />" + "Plafond : FL" + rs.getInt("flsup") + "<br />" + "Plancher : FL" + rs.getInt("flinf") + "<br />" + "</html>");
                            BasicAirspaceAttributes attrs = new BasicAirspaceAttributes();
                            attrs.setDrawOutline(true);
                            attrs.setMaterial(new Material(Color.CYAN));
                            attrs.setOutlineMaterial(new Material(Pallet.makeBrighter(Color.CYAN)));
                            attrs.setOpacity(0.2);
                            attrs.setOutlineOpacity(0.9);
                            attrs.setOutlineWidth(1.0);
                            stack.setAttributes(attrs);
                            BasicAirspaceAttributes attrH = new BasicAirspaceAttributes(attrs);
                            attrH.setMaterial(new Material(Pallet.makeBrighter(attrs.getMaterial().getDiffuse())));
                            stack.setHighlightAttributes(attrH);
                            renderableLayer.addRenderable(stack);
                            renderables.put(type + "-" + name, stack);
                            this.toggleLayer(renderableLayer, true);
                        }
                        st.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    ((SimpleStack3D) this.renderables.get(type + "-" + name)).setVisible(true);
                }
                this.renderableLayer.firePropertyChange(AVKey.LAYER, null, this.renderableLayer);
                break;
            case TMA_F:
                if (!renderables.containsKey(type + "-" + name)) {
                    try {
                        Statement st = DatabaseManager.getCurrentExsa();
                        ResultSet rs = st.executeQuery("select * from centtmaf where name='" + name + "'");
                        if (rs.next()) {
                            DatabaseCylinder tmaFilet = new DatabaseCylinder(name, Type.EXSA, TMA_F, LatLon.fromDegrees(rs.getDouble("latitude"), rs.getDouble("longitude")), 0, rs.getInt("fl"), rs.getDouble("rayon"));
                            tmaFilet.setAnnotation("<html><b>TMA Filet : " + name + "</b><br /><br />" + "Rayon : " + rs.getInt("rayon") + " NM<br />" + "Plafond : FL" + rs.getInt("fl") + "<br />" + "Nom du secteur : " + rs.getString("nomsecteur") + "<br />" + "</html>");
                            BasicAirspaceAttributes attrs = new BasicAirspaceAttributes();
                            attrs.setDrawOutline(true);
                            attrs.setMaterial(new Material(Color.CYAN));
                            attrs.setOutlineMaterial(new Material(Pallet.makeBrighter(Color.CYAN)));
                            attrs.setOpacity(0.2);
                            attrs.setOutlineOpacity(0.9);
                            attrs.setOutlineWidth(1.0);
                            tmaFilet.setAttributes(attrs);
                            renderableLayer.addRenderable(tmaFilet);
                            renderables.put(type + "-" + name, tmaFilet);
                            this.toggleLayer(renderableLayer, true);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    ((Cylinder) this.renderables.get(type + "-" + name)).setVisible(true);
                }
                this.renderableLayer.firePropertyChange(AVKey.LAYER, null, this.renderableLayer);
                break;
            case TMA_F_M:
                this.toggleLayer(this.createMosaiqueLayer(type, name), true);
                break;
            default:
                break;
        }
        DatasManager.getView(Type.EXSA).showObject(type, name);
    }

    /**
	 * Crée le calque mosaîque demandé
	 * @param type Type de mosaîque
	 * @param name Nom de la mosaïque
	 * @return {@link MosaiqueLayer}
	 */
    private MosaiqueLayer createMosaiqueLayer(int type, String name) {
        if (mosaiquesLayer.containsKey(type + "-" + name)) {
            MosaiqueLayer mos = mosaiquesLayer.get(type + "-" + name);
            mos.set3D(!flat);
            return mos;
        } else {
            String annotationTitle = null;
            Boolean grille = true;
            LatLonCautra origine = null;
            Integer width = 0;
            Integer height = 0;
            Integer size = 0;
            int hSens = 0;
            int vSens = 0;
            int numSens = 0;
            List<Couple<Integer, Integer>> squares = null;
            List<Couple<Double, Double>> altitudes = null;
            Boolean numbers = true;
            ShapeAttributes attr = null;
            AirspaceAttributes airspaceAttr = null;
            if (type == MOSAIQUE) {
                try {
                    Statement st = DatabaseManager.getCurrentExsa();
                    ResultSet rs = st.executeQuery("select * from centmosai where type ='" + name + "'");
                    origine = LatLonCautra.fromCautra(rs.getDouble("xcautra"), rs.getDouble("ycautra"));
                    width = rs.getInt("colonnes");
                    height = rs.getInt("lignes");
                    rs.close();
                    st.close();
                    size = 32;
                    hSens = MosaiqueLayer.BOTTOM_UP;
                    vSens = MosaiqueLayer.LEFT_RIGHT;
                    numSens = MosaiqueLayer.VERTICAL_FIRST;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (type == MOSAIQUE_CAPA) {
                try {
                    annotationTitle = "Filtrage capacitif " + name;
                    grille = false;
                    squares = new LinkedList<Couple<Integer, Integer>>();
                    altitudes = new LinkedList<Couple<Double, Double>>();
                    Statement st = DatabaseManager.getCurrentExsa();
                    String typeGrille = name.equals("VISSEC") ? "ADP" : "CCR";
                    ResultSet rs = st.executeQuery("select * from centmosai where type ='" + typeGrille + "'");
                    origine = LatLonCautra.fromCautra(rs.getDouble("xcautra"), rs.getDouble("ycautra"));
                    width = rs.getInt("colonnes");
                    height = rs.getInt("lignes");
                    size = 32;
                    hSens = MosaiqueLayer.BOTTOM_UP;
                    vSens = MosaiqueLayer.LEFT_RIGHT;
                    numSens = MosaiqueLayer.VERTICAL_FIRST;
                    rs = st.executeQuery("select * from ficaafniv where abonne = '" + name + "'");
                    rs.next();
                    for (int i = 1; i <= height * width; i++) {
                        if (rs.getInt("carre") == i) {
                            if (!rs.getBoolean("elimine")) {
                                squares.add(new Couple<Integer, Integer>(i, 0));
                                altitudes.add(new Couple<Double, Double>(rs.getInt("plancher") * 30.48, rs.getInt("plafond") * 30.48));
                            }
                            rs.next();
                        } else {
                            squares.add(new Couple<Integer, Integer>(i, 0));
                            altitudes.add(new Couple<Double, Double>(-10.0, 660 * 30.48));
                        }
                    }
                    numbers = false;
                    airspaceAttr = new BasicAirspaceAttributes();
                    airspaceAttr.setMaterial(Material.YELLOW);
                    airspaceAttr.setOpacity(0.4);
                    attr = new BasicShapeAttributes();
                    attr.setInteriorMaterial(Material.YELLOW);
                    attr.setInteriorOpacity(0.4);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (type == MOSAIQUE_DYN) {
                annotationTitle = "Filtrage dynamique " + name;
                grille = false;
                squares = new LinkedList<Couple<Integer, Integer>>();
                altitudes = new LinkedList<Couple<Double, Double>>();
                try {
                    Statement st = DatabaseManager.getCurrentExsa();
                    ResultSet rs = st.executeQuery("select * from centmosai where type ='CCR'");
                    origine = LatLonCautra.fromCautra(rs.getDouble("xcautra"), rs.getDouble("ycautra"));
                    width = rs.getInt("colonnes");
                    height = rs.getInt("lignes");
                    size = 32;
                    hSens = MosaiqueLayer.BOTTOM_UP;
                    vSens = MosaiqueLayer.LEFT_RIGHT;
                    numSens = MosaiqueLayer.VERTICAL_FIRST;
                    numbers = false;
                    attr = new BasicShapeAttributes();
                    attr.setInteriorMaterial(Material.YELLOW);
                    attr.setInteriorOpacity(0.4);
                    airspaceAttr = new BasicAirspaceAttributes();
                    airspaceAttr.setMaterial(Material.YELLOW);
                    airspaceAttr.setOpacity(0.4);
                    grille = false;
                    rs = st.executeQuery("select * from ficaafnic where abonne = '" + name + "'");
                    while (rs.next()) {
                        squares.add(new Couple<Integer, Integer>(rs.getInt("carre"), 0));
                        altitudes.add(new Couple<Double, Double>(rs.getInt("plancher") * 30.48, rs.getInt("plafond") * 30.48));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (type == MOSAIQUE_ZOCC) {
                annotationTitle = "Zone d'occultation " + name;
                grille = false;
                squares = new LinkedList<Couple<Integer, Integer>>();
                altitudes = new LinkedList<Couple<Double, Double>>();
                try {
                    Statement st = DatabaseManager.getCurrentExsa();
                    ResultSet rs = st.executeQuery("select * from centmosai where type ='CCR'");
                    origine = LatLonCautra.fromCautra(rs.getDouble("xcautra"), rs.getDouble("ycautra"));
                    width = rs.getInt("colonnes");
                    height = rs.getInt("lignes");
                    size = 32;
                    hSens = MosaiqueLayer.BOTTOM_UP;
                    vSens = MosaiqueLayer.LEFT_RIGHT;
                    numSens = MosaiqueLayer.VERTICAL_FIRST;
                    numbers = false;
                    attr = new BasicShapeAttributes();
                    attr.setInteriorMaterial(Material.YELLOW);
                    attr.setInteriorOpacity(0.4);
                    airspaceAttr = new BasicAirspaceAttributes();
                    airspaceAttr.setMaterial(Material.YELLOW);
                    airspaceAttr.setOpacity(0.4);
                    grille = false;
                    rs = st.executeQuery("select * from centsczoc where zone = '" + name + "'");
                    while (rs.next()) {
                        squares.add(new Couple<Integer, Integer>(rs.getInt("carre"), rs.getInt("souscarre")));
                        altitudes.add(new Couple<Double, Double>(0.0, rs.getInt("plafond") * 30.48));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (type == TMA_F_M) {
                annotationTitle = "TMA Filet " + name;
                grille = false;
                squares = new LinkedList<Couple<Integer, Integer>>();
                altitudes = new LinkedList<Couple<Double, Double>>();
                try {
                    Statement st = DatabaseManager.getCurrentExsa();
                    ResultSet rs = st.executeQuery("select * from centmosai where type ='CCR'");
                    origine = LatLonCautra.fromCautra(rs.getDouble("xcautra"), rs.getDouble("ycautra"));
                    width = rs.getInt("colonnes");
                    height = rs.getInt("lignes");
                    size = 32;
                    hSens = MosaiqueLayer.BOTTOM_UP;
                    vSens = MosaiqueLayer.LEFT_RIGHT;
                    numSens = MosaiqueLayer.VERTICAL_FIRST;
                    numbers = false;
                    attr = new BasicShapeAttributes();
                    attr.setInteriorMaterial(Material.YELLOW);
                    attr.setInteriorOpacity(0.4);
                    airspaceAttr = new BasicAirspaceAttributes();
                    airspaceAttr.setMaterial(Material.YELLOW);
                    airspaceAttr.setOpacity(0.4);
                    grille = false;
                    rs = st.executeQuery("select * from centsctma where name = '" + name.split(" ")[0] + "'");
                    while (rs.next()) {
                        squares.add(new Couple<Integer, Integer>(rs.getInt("carre"), rs.getInt("souscarre")));
                        altitudes.add(new Couple<Double, Double>(0.0, rs.getInt(name.split(" ")[1]) * 30.48));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (type == MOSAIQUE_VVF) {
                annotationTitle = "VVF " + name;
                grille = false;
                squares = new LinkedList<Couple<Integer, Integer>>();
                altitudes = new LinkedList<Couple<Double, Double>>();
                try {
                    Statement st = DatabaseManager.getCurrentExsa();
                    ResultSet rs = st.executeQuery("select * from centmosai where type ='CCR'");
                    origine = LatLonCautra.fromCautra(rs.getDouble("xcautra"), rs.getDouble("ycautra"));
                    width = rs.getInt("colonnes");
                    height = rs.getInt("lignes");
                    size = 32;
                    hSens = MosaiqueLayer.BOTTOM_UP;
                    vSens = MosaiqueLayer.LEFT_RIGHT;
                    numSens = MosaiqueLayer.VERTICAL_FIRST;
                    numbers = false;
                    attr = new BasicShapeAttributes();
                    attr.setInteriorMaterial(Material.YELLOW);
                    attr.setInteriorOpacity(0.4);
                    attr.setOutlineMaterial(Material.YELLOW);
                    airspaceAttr = new BasicAirspaceAttributes();
                    airspaceAttr.setMaterial(Material.YELLOW);
                    airspaceAttr.setOpacity(0.4);
                    grille = false;
                    rs = st.executeQuery("select * from centscvvf where vvfs LIKE '%" + name + "%'");
                    while (rs.next()) {
                        squares.add(new Couple<Integer, Integer>(rs.getInt("carre"), rs.getInt("souscarre")));
                        String[] vvfs = rs.getString("vvfs").split("\\\\");
                        int numVVF = 0;
                        for (int i = 0; i < vvfs.length; i++) {
                            if (vvfs[i].equals(name)) numVVF = i;
                        }
                        double plancher = new Double(rs.getString("planchers").split("\\\\")[numVVF]) * 30.48;
                        double plafond = new Double(rs.getString("plafonds").split("\\\\")[numVVF]) * 30.48;
                        altitudes.add(new Couple<Double, Double>(plancher, plafond));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            MosaiqueLayer mLayer = new MosaiqueLayer(annotationTitle, grille, origine, width, height, size, hSens, vSens, numSens, squares, altitudes, numbers, attr, airspaceAttr, Type.EXSA, type, name);
            mosaiquesLayer.put(type + "-" + name, mLayer);
            mLayer.setName("Mosaïque " + type + " " + name);
            mLayer.set3D(!flat);
            return mLayer;
        }
    }

    @Override
    public void hideObject(int type, String name) {
        if (type == RADAR) {
            if (renderables.containsKey(type + "-" + name)) {
                ((SurfaceShape) renderables.get(type + "-" + name)).setVisible(false);
                this.renderableLayer.firePropertyChange(AVKey.LAYER, null, this.renderableLayer);
            }
        } else if (type == STACK) {
            if (renderables.containsKey(type + "-" + name)) {
                ((SimpleStack3D) renderables.get(type + "-" + name)).setVisible(false);
                this.renderableLayer.firePropertyChange(AVKey.LAYER, null, this.renderableLayer);
            }
        } else if (type == TMA_F) {
            if (renderables.containsKey(type + "-" + name)) {
                ((Cylinder) renderables.get(type + "-" + name)).setVisible(false);
                this.renderableLayer.firePropertyChange(AVKey.LAYER, null, this.renderableLayer);
            }
        } else {
            this.toggleLayer(this.mosaiquesLayer.get(type + "-" + name), false);
        }
        DatasManager.getView(Type.EXSA).hideObject(type, name);
    }

    @Override
    public void reset() {
    }

    @Override
    public void set2D(Boolean flat) {
        if (this.flat != flat) {
            this.flat = flat;
            for (MosaiqueLayer l : mosaiquesLayer.values()) {
                l.set3D(!flat);
            }
        }
    }

    @Override
    public int string2type(String type) {
        return 0;
    }

    @Override
    public String type2string(int type) {
        return null;
    }

    public static int getNumberInitSteps() {
        return 1;
    }

    @Override
    public Collection<Object> getObjects(int type) {
        return null;
    }

    @Override
    public void setColor(Color color, int type, String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean isColorEditable(int type) {
        return false;
    }

    @Override
    public HashMap<Integer, List<String>> getSelectedObjectsReference() {
        HashMap<Integer, List<String>> objects = new HashMap<Integer, List<String>>();
        for (Entry<String, MosaiqueLayer> e : this.mosaiquesLayer.entrySet()) {
            if (e.getValue().isEnabled()) {
                String[] names = e.getKey().split("-");
                int type = new Integer(names[0]);
                if (!objects.containsKey(type)) {
                    objects.put(type, new ArrayList<String>());
                }
                objects.get(type).add(names[1]);
            }
        }
        for (Entry<String, Renderable> e : renderables.entrySet()) {
            String[] names = e.getKey().split("-");
            int type = new Integer(names[0]);
            boolean visible = false;
            if (type == RADAR) {
                visible = ((Radar) e.getValue()).isVisible();
            } else if (type == STACK) {
                visible = ((SimpleStack3D) e.getValue()).isVisible();
            } else if (type == TMA_F) {
                visible = ((Cylinder) e.getValue()).isVisible();
            }
            if (visible) {
                if (!objects.containsKey(type)) {
                    objects.put(type, new ArrayList<String>());
                }
                objects.get(type).add(names[1]);
            }
        }
        return objects;
    }

    @Override
    public Iterable<Restorable> getSelectedObjects() {
        ArrayList<Restorable> restorables = new ArrayList<Restorable>();
        for (Entry<String, Renderable> e : renderables.entrySet()) {
            String[] names = e.getKey().split("-");
            int type = new Integer(names[0]);
            boolean visible = false;
            if (type == RADAR) {
                visible = ((Radar) e.getValue()).isVisible();
            } else if (type == STACK) {
                visible = ((SimpleStack3D) e.getValue()).isVisible();
            } else if (type == TMA_F) {
                visible = ((Cylinder) e.getValue()).isVisible();
            }
            if (visible) {
                restorables.add((Restorable) e.getValue());
            }
        }
        for (Layer l : mosaiquesLayer.values()) {
            if (l.isEnabled()) restorables.add(l);
        }
        return restorables;
    }

    /**
	 * Not implemented in this controller
	 */
    @Override
    public boolean areLocationsVisible(int type, String name) {
        return false;
    }

    /**
	 * Not implemented in this controller
	 */
    @Override
    public void setLocationsVisible(int type, String name, boolean b) {
    }
}
