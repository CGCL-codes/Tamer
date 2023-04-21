package setupmapframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.data.JFileDataStoreChooser;

public class SetupFrame {

    private JMapPane mappane = new JMapPane();

    private MapContext mapcontext = new MapContext();

    private ZoomInAction zoomIn = new ZoomInAction(mappane, true);

    private ZoomOutAction zoomOut = new ZoomOutAction(mappane, true);

    private PanAction move = new PanAction(mappane, true);

    private InfoAction info = new InfoAction(mappane, true);

    private int u = 0;

    private Color[] coleurs = { Color.BLUE, Color.MAGENTA, Color.PINK, Color.YELLOW, Color.ORANGE, Color.green, Color.blue, Color.GRAY, Color.black };

    public SetupFrame() {
        JFrame frm = new JFrame();
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(800, 600);
        frm.setTitle("Objectwiz");
        JPanel pfond = new JPanel(new BorderLayout());
        frm.setContentPane(pfond);
        frm.setJMenuBar(buildMenu());
        frm.getContentPane().add(BorderLayout.CENTER, buildMap());
        frm.getContentPane().add(BorderLayout.NORTH, buildTool());
        frm.setVisible(true);
    }

    private JMenuBar buildMenu() {
        JMenuBar menu = new JMenuBar();
        JMenu mfichier = new JMenu("Fichier");
        JMenuItem iquitter = new JMenuItem("Quitter");
        iquitter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mfichier.add(iquitter);
        JMenuItem ajouter_couche = new JMenuItem("Importer un Fichier");
        ajouter_couche.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    File file = JFileDataStoreChooser.showOpenFile("shp", null);
                    URL str = file.toURI().toURL();
                    StyleBuilder sb = new StyleBuilder();
                    LineSymbolizer ls2 = sb.createLineSymbolizer(coleurs[u], 1);
                    if (u < 6) u++; else u = 0;
                    Style roadsStyle = sb.createStyle();
                    roadsStyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ls2));
                    ShapefileDataStore store = new ShapefileDataStore(str);
                    String name = store.getTypeNames()[0];
                    FeatureSource source = store.getFeatureSource(name);
                    MapLayer layer = new MapLayer(source, roadsStyle);
                    layer.setTitle("x.shp");
                    layer.setVisible(true);
                    layer.setQuery(Query.ALL);
                    mapcontext.addLayer(layer);
                    mappane.setDisplayArea(mapcontext.getLayerBounds());
                } catch (IOException ex) {
                    Logger.getLogger(SetupFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        mfichier.add(ajouter_couche);
        mfichier.add(iquitter);
        menu.add(mfichier);
        return menu;
    }

    private JPanel buildMap() {
        mappane.setBackground(Color.WHITE);
        try {
            mapcontext.setTitle("Projet");
            MapLayer maplayer;
            URL shapeURL = SetupFrame.class.getResource("natural.shp");
            ShapefileDataStore store = new ShapefileDataStore(shapeURL);
            String name = store.getTypeNames()[0];
            FeatureSource source = store.getFeatureSource(name);
            StyleBuilder sb = new StyleBuilder();
            PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(253, 241, 187), new Color(163, 151, 97), 1);
            Style solstyle = sb.createStyle();
            solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
            maplayer = new MapLayer(source, solstyle);
            maplayer.setTitle("occ_sol.shp");
            maplayer.setVisible(true);
            maplayer.setQuery(Query.ALL);
            mapcontext.addLayer(maplayer);
            shapeURL = SetupFrame.class.getResource("buildings.shp");
            store = new ShapefileDataStore(shapeURL);
            name = store.getTypeNames()[0];
            source = store.getFeatureSource(name);
            sb = new StyleBuilder();
            LineSymbolizer ls2 = sb.createLineSymbolizer(Color.RED, 1);
            Style roadsStyle = sb.createStyle();
            roadsStyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ls2));
            maplayer = new MapLayer(source, roadsStyle);
            maplayer.setTitle("reseau_route.shp");
            maplayer.setVisible(true);
            maplayer.setQuery(Query.ALL);
            mapcontext.addLayer(maplayer);
            mappane.setMapContext(mapcontext);
            StreamingRenderer render = new StreamingRenderer();
            mappane.setRenderer(render);
            mappane.setDisplayArea(mapcontext.getLayerBounds());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mappane;
    }

    private JPanel buildTool() {
        JPanel outil = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoomIn.actionPerformed(e);
            }
        });
        outil.add(plus);
        JButton moins = new JButton("-");
        moins.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoomOut.actionPerformed(e);
            }
        });
        outil.add(moins);
        JButton deplacer = new JButton("deplacer");
        deplacer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                move.actionPerformed(e);
            }
        });
        outil.add(deplacer);
        JButton getInfo = new JButton("coordonnées");
        getInfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                info.actionPerformed(e);
            }
        });
        outil.add(getInfo);
        return outil;
    }
}
