package karto;

import karto.datamodels.Map;
import karto.datamodels.Messages;
import karto.datamodels.RCDataModel;
import karto.datamodels.inout.InOutArcInfo;
import karto.datamodels.inout.InOutGraphML;
import karto.datamodels.inout.InOutSpeckmann;
import karto.views.RCMainframe;
import java.io.File;
import java.util.ResourceBundle;

/**
 * Application's starting point.
 * @author dupuis
 */
public class MainClass {

    /**
     * For testing purpose only : Write whatever you want in your mainX()
     * function and call it here. Warning : Exceptions are not handled.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        mainGui();
    }

    /**
     * Testing Speckmann import.
     * @param args
     */
    public static void mainSpeckmann(String[] args) {
        InOutSpeckmann importSpeckmann = new InOutSpeckmann();
        Map m = importSpeckmann.read(new File("data/SOMEWHERE.txt"));
        RCDataModel rcModel = new RCDataModel();
        rcModel.fillDataModelFromGraph(m.getGraph());
        System.out.println(rcModel.toString());
        rcModel.hasGoodNeighbors();
        rcModel.isProperEdgeLabeling();
    }

    /**
     * Testing Arc INFO data import.
     * @param args
     */
    public static void mainArcInfo(String[] args) {
        InOutArcInfo arcInfo = new InOutArcInfo();
        Map m = arcInfo.read(new File("src/karto/data/africa_adm0.shp"));
        InOutGraphML inOutGraphML = new InOutGraphML();
        inOutGraphML.write(m, new File("testGraphWrite.graphml"));
        m.toString();
    }

    /**
     * Testing Arc INFO data import.
     * @param args
     */
    public static void mainImportGraphML(String[] args) {
        InOutGraphML inOutGraphML = new InOutGraphML();
        Map m = inOutGraphML.read(new File("testGraphWrite.graphML"));
        RCDataModel rcModel = new RCDataModel();
        rcModel.fillDataModelFromGraph(m.getGraph());
        System.out.println(rcModel.toString());
        rcModel.hasGoodNeighbors();
        rcModel.isProperEdgeLabeling();
    }

    public static void mainGui() {
        Messages.BUNDLE_NAME = "karto.datamodels.MessagesDE";
        Messages.RESOURCE_BUNDLE = ResourceBundle.getBundle(Messages.BUNDLE_NAME);
        RCMainframe RCMainframe = new RCMainframe();
        RCMainframe.setVisible(true);
    }
}
