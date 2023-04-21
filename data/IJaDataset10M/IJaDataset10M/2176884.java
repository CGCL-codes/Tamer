package TTJ.App;

import TTJ.Interface.GenericMinion;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: meier
 * Date: 29.07.11
 * Time: 13:45
 * To change this template use File | Settings | File Templates.
 */
public class ttj {

    public ttj() {
    }

    public static void main(String[] args) {
        Lair Test = Lair.getInstance();
        GraphicMaster DialogTest = Test.getGraphicMaster();
        DialogTest.LoadAllPlugins();
        DialogTest.GetGraphicMinion(0).StartMainDialog(new Point(10, 10), new Point(1600, 900), "TestDialog");
        DialogTest.GetGraphicMinion(0).DrawAFloatingWindow(new Point(1000, 200), new Point(400, 300), "A little floating Window.");
    }
}
