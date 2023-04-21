package fr.ign.cogit.appli.geopensim.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import org.apache.log4j.Logger;
import fr.ign.cogit.appli.geopensim.feature.basic.BasicBatiment;
import fr.ign.cogit.appli.geopensim.feature.meso.ZoneElementaireUrbaine;
import fr.ign.cogit.appli.geopensim.feature.micro.Batiment;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IPopulation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;

/**
 * @author Julien Perret
 * 
 */
public class BuildingBlockCreation {

    static Logger logger = Logger.getLogger(BuildingBlockCreation.class);

    /**
   * @param args
   */
    public static void main(String[] args) {
        JFrame frame = new JFrame("BuildingBlockCreation");
        Container p = frame.getContentPane();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JButton b1 = new JButton("Add network file");
        p.add(b1);
        JButton b2 = new JButton("Add building file");
        p.add(b2);
        JList list1 = new JList();
        p.add(list1);
        JList list2 = new JList();
        p.add(list2);
        b1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
            }
        });
        frame.pack();
        frame.setVisible(true);
        IPopulation<IFeature> inputBuildings = null;
    }

    /**
   * @param buildingBlock
   * @param inputBuildingPopulation
   * @param buildingsPopulation
   */
    @SuppressWarnings("unchecked")
    private static void addBuildings(ZoneElementaireUrbaine buildingBlock, IPopulation<IFeature> inputBuildingPopulation, IPopulation<Batiment> buildingsPopulation, int date) {
        Collection<IFeature> buildingsInBuildingBlockDate1 = inputBuildingPopulation.select(buildingBlock.getGeometrie());
        Collection<Batiment> buildings = new ArrayList<Batiment>(0);
        for (IFeature buildingFeature : buildingsInBuildingBlockDate1) {
            IGeometry geometry = null;
            if (buildingBlock.getGeometrie().contains(buildingFeature.getGeom())) {
                geometry = buildingFeature.getGeom();
            } else {
                geometry = buildingFeature.getGeom().intersection(buildingBlock.getGeom());
            }
            if (geometry.area() > 1) {
                String nature = (String) buildingFeature.getAttribute((date == 1989) ? "TYPE" : "CATEGORIE");
                if (geometry.isPolygon()) {
                    BasicBatiment building = new BasicBatiment();
                    building.setGeom(geometry);
                    building.setDateSourceSaisie(buildingBlock.getDateSourceSaisie());
                    building.setNature(nature);
                    buildings.add(building);
                    buildingsPopulation.add(building);
                } else {
                    if (geometry.isMultiSurface()) {
                        for (GM_Polygon polygon : ((GM_MultiSurface<GM_Polygon>) geometry).getList()) {
                            if (polygon.area() > 1) {
                                BasicBatiment building = new BasicBatiment();
                                building.setGeom(polygon);
                                building.setDateSourceSaisie(buildingBlock.getDateSourceSaisie());
                                building.setNature(nature);
                                buildingsPopulation.add(building);
                            }
                        }
                    }
                }
            }
        }
        buildingBlock.construireGroupes(buildings);
    }
}
