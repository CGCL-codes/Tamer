package ui;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.JScrollPane;
import datatypes.HostInformation;
import ui.grafic.WizzardGrafic;

public class ScenarioWizzard_Pane extends JScrollPane {

    static int scenario_ID = 0;

    static int miniworld_width = 40;

    static int miniworld_height = 40;

    static int range = 10;

    static String name = "name";

    static String subname = "untername";

    static String description = "beschreibung";

    static HashMap<Integer, HostInformation> generatedContent = null;

    static CheckList_Universal ActiveHostList = null;

    static CheckList_Universal ServiceHostList = null;

    static WizzardGrafic ActiveHostGrafic = null;

    static WizzardGrafic ServiceHostGrafic = null;

    static Vector<Vector<String>> ActionExecutionList = null;
}
