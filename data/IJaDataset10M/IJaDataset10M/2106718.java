package SDG;

import swarm.Globals;
import swarm.objectbase.Swarm;
import swarm.activity.Activity;
import swarm.activity.Schedule;
import swarm.activity.ScheduleImpl;
import swarm.simtoolsgui.GUISwarmImpl;
import swarm.space.Object2dDisplay;
import swarm.space.Object2dDisplayImpl;
import swarm.gui.ZoomRasterImpl;
import swarm.gui.ZoomRaster;
import swarm.gui.Colormap;
import swarm.gui.ColormapImpl;
import swarm.defobj.Zone;
import swarm.Selector;
import swarm.objectbase.VarProbe;
import swarm.objectbase.MessageProbe;
import swarm.objectbase.EmptyProbeMapImpl;
import java.util.List;
import java.util.LinkedList;

public class ObserverSwarm extends GUISwarmImpl {

    public int xsize = 100;

    public int ysize = 100;

    public int numUsers = 50;

    public static java.util.ArrayList nameTable = null;

    public static final byte UserTourColor = 0;

    public static final byte UserResistColor = 1;

    public static final byte UserListenColor = 2;

    public static final byte GlenTourColor = 3;

    public static final byte GlenAttackColor = 4;

    public static final byte MarcusIncubateColor = 5;

    public static final byte MarcusResistColor = 6;

    public static final byte MarcusListenColor = 7;

    public static final byte MarcusNativeColor = 8;

    public static final byte AlexTourColor = 9;

    public static final byte AlexTalkColor = 10;

    public static final byte AlexTargetColor = 11;

    Object2dDisplay display;

    ZoomRaster raster;

    SDGOrganization model;

    Colormap colormap;

    Schedule displaySchedule;

    public ObserverSwarm(Zone aZone) {
        super(aZone);
        class ObserverSwarmProbeMap extends EmptyProbeMapImpl {

            private VarProbe probeVariable(String name) {
                return Globals.env.probeLibrary.getProbeForVariable$inClass(name, ObserverSwarm.this.getClass());
            }

            private MessageProbe probeMessage(String name) {
                return Globals.env.probeLibrary.getProbeForMessage$inClass(name, ObserverSwarm.this.getClass());
            }

            private void addVar(String name) {
                addProbe(probeVariable(name));
            }

            private void addMessage(String name) {
                addProbe(probeMessage(name));
            }

            public ObserverSwarmProbeMap(Zone _aZone, Class aClass) {
                super(_aZone, aClass);
                addVar("xsize");
                addVar("ysize");
                addVar("numUsers");
            }
        }
        Globals.env.probeLibrary.setProbeMap$For(new ObserverSwarmProbeMap(aZone, getClass()), getClass());
        Globals.env.createArchivedProbeDisplay(this, "Observer");
    }

    public Object buildObjects() {
        super.buildObjects();
        nameTable = createNameTable();
        getControlPanel().setStateStopped();
        model = new SDGOrganization(getZone(), xsize, ysize, numUsers);
        model.buildObjects();
        colormap = new ColormapImpl(getZone());
        colormap.setColor$ToName(UserTourColor, "white");
        colormap.setColor$ToName(UserResistColor, "gray");
        colormap.setColor$ToName(UserListenColor, "slate gray");
        colormap.setColor$ToName(MarcusIncubateColor, "yellow");
        colormap.setColor$ToName(MarcusResistColor, "red");
        colormap.setColor$ToName(MarcusListenColor, "pale green");
        colormap.setColor$ToName(MarcusNativeColor, "dark green");
        colormap.setColor$ToName(GlenTourColor, "cyan");
        colormap.setColor$ToName(GlenAttackColor, "pink");
        colormap.setColor$ToName(AlexTourColor, "brown");
        colormap.setColor$ToName(AlexTalkColor, "orange");
        colormap.setColor$ToName(AlexTargetColor, "brown");
        raster = new ZoomRasterImpl(getZone(), "raster");
        raster.setColormap(colormap);
        raster.setZoomFactor(3);
        raster.setWidth$Height(model.getWorld().getSizeX(), model.getWorld().getSizeY());
        raster.setWindowTitle("SDG World");
        raster.pack();
        raster.erase();
        try {
            display = new Object2dDisplayImpl(getZone(), raster, model.getWorld(), new Selector(Class.forName("SDG.agent2d.Agent2d"), "drawSelfOn", false));
            raster.setButton$Client$Message(3, display, new Selector(display.getClass(), "makeProbeAtX$Y", true));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return this;
    }

    public void updateDisplay() {
        raster.erase();
        display.display();
        raster.drawSelf();
        Globals.env.probeDisplayManager.update();
        getActionCache().doTkEvents();
    }

    public Object buildActions() {
        super.buildActions();
        model.buildActions();
        displaySchedule = new ScheduleImpl(getZone(), 1);
        try {
            displaySchedule.at$createActionTo$message(0, this, new Selector(getClass(), "updateDisplay", false));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return this;
    }

    public Activity activateIn(Swarm context) {
        super.activateIn(context);
        model.activateIn(this);
        displaySchedule.activateIn(this);
        return getActivity();
    }

    private java.util.ArrayList createNameTable() {
        java.util.ArrayList table = new java.util.ArrayList();
        table.add("Aaron");
        table.add("Alessandro");
        table.add("Andre");
        table.add("Andrew");
        table.add("Barry");
        table.add("Bas");
        table.add("Ben");
        table.add("Benedikt");
        table.add("Carl");
        table.add("Chris L.");
        table.add("Christian");
        table.add("Christina");
        table.add("Daniel");
        table.add("Darren");
        table.add("David A.");
        table.add("David S.");
        table.add("Dawn");
        table.add("Doug D.");
        table.add("Ed");
        table.add("Ferdinando V.");
        table.add("Francois");
        table.add("Fred");
        table.add("Gary A.");
        table.add("Gary M.");
        table.add("Gigi");
        table.add("Georgi");
        table.add("Gert");
        table.add("Ginger");
        table.add("Gustavo");
        table.add("Heath");
        table.add("Ho");
        table.add("Jack");
        table.add("Jacobo");
        table.add("James A.");
        table.add("James G.");
        table.add("Jan");
        table.add("Jason A.");
        table.add("Jason S.");
        table.add("Jay");
        table.add("Jonathan");
        table.add("John G.");
        table.add("John P.");
        table.add("John S.");
        table.add("Jorge");
        table.add("Joshua");
        table.add("Juan");
        table.add("Julie");
        table.add("Katherine");
        table.add("Ken G.");
        table.add("Kenneth H.");
        table.add("Krishnan");
        table.add("Louis");
        table.add("Marc");
        table.add("Marcello");
        table.add("Marie");
        table.add("Marian");
        table.add("Matt");
        table.add("Matteo");
        table.add("Matthew");
        table.add("Michael K.");
        table.add("Michael N.");
        table.add("Mike");
        table.add("Miles");
        table.add("Minh");
        table.add("Murali");
        table.add("Murat");
        table.add("Narjes");
        table.add("Nick");
        table.add("Nicolas");
        table.add("Nigel");
        table.add("Nikitas");
        table.add("Norberto");
        table.add("Owen");
        table.add("Pallamin");
        table.add("Patelli");
        table.add("Paul B.");
        table.add("Paul J.");
        table.add("Peter");
        table.add("Philip");
        table.add("Pietro");
        table.add("Ralf");
        table.add("Rayman");
        table.add("Ravi");
        table.add("Rene");
        table.add("Riccardo");
        table.add("Rick");
        table.add("Rob");
        table.add("Roger");
        table.add("Russell");
        table.add("Sharon");
        table.add("Stephen");
        table.add("Steve J.");
        table.add("Steve R.");
        table.add("Steve S.");
        table.add("Sussane");
        table.add("Sven");
        table.add("Ted");
        table.add("Teresa");
        table.add("Thomas");
        table.add("Tom");
        table.add("Tushar");
        table.add("Uffe");
        table.add("Vickie");
        table.add("Vladimir");
        table.add("Voytek");
        table.add("Wayne");
        table.add("Will");
        table.add("William");
        table.add("Xiaodong");
        table.add("Yannick");
        table.add("Zhang");
        table.add("Zoltan");
        return table;
    }

    public static void main(String[] args) {
        Globals.env.initSwarm("SDG", "0.0", "bug-swarm@swarm.org", args);
        ObserverSwarm observer = new ObserverSwarm(Globals.env.globalZone);
        observer.buildObjects();
        observer.buildActions();
        observer.activateIn(null);
        observer.go();
    }
}
