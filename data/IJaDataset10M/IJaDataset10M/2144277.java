package g4p.tool;

import g4p.tool.gui.GuiDesigner;
import java.io.File;
import javax.swing.JFrame;
import processing.app.Base;
import processing.app.Sketch;
import processing.app.tools.Tool;

/**
 * 
 * @author Peter Lager
 *
 */
public class G4PTool implements Tool, TFileConstants {

    private processing.app.Editor editor;

    private GuiDesigner dframe;

    private boolean g4p_error_shown = false;

    public String getMenuTitle() {
        return "GUI builder";
    }

    /**
	 * Get version string of this tool
	 * @return revision number string
	 */
    public static String getVersion() {
        return "##tool.prettyVersion##";
    }

    /**
	 * Get compatible version string of this tool
	 * @return revision number string
	 */
    public static String getCompatibleVersionNo() {
        String n[] = "##tool.prettyVersion##".split("[\\.]");
        return n[0] + "." + n[1];
    }

    /**
	 * Get version number of this tool as an integer with the form  <br>
	 * MMmmii <br>
	 * M = Major revision <br>
	 * m = minor revision <br>
	 * i = internal revision <br>
	 * @return version number as int
	 */
    public static int getVersionNo() {
        String n[] = "##tool.prettyVersion##".split("[\\.]");
        int[] vnp = new int[3];
        for (int i = 0; i < n.length; i++) {
            try {
                vnp[i] = Integer.parseInt(n[i]);
            } catch (Exception excp) {
            }
        }
        return ((vnp[0] * 100) + vnp[1]) * 100 + vnp[2];
    }

    /**
	 * Called once first time the tool is called
	 */
    public void init(processing.app.Editor theEditor) {
        this.editor = theEditor;
    }

    /**
	 * This is executed every time we launch the tool using the menu item in Processing IDE
	 * 
	 */
    public void run() {
        Base base = editor.getBase();
        Sketch sketch = editor.getSketch();
        File sketchFolder = sketch.getFolder();
        File sketchbookFolder = base.getSketchbookFolder();
        if (!g4p_error_shown && !g4pJarExists(base.getSketchbookLibrariesFolder())) {
            Base.showWarning("GUI Builder error", "Although you can use this tool the sketch created will not \nwork because the G4P library needs to be installed.\nSee G4P at http://www.lagers.org.uk/g4p/", null);
            g4p_error_shown = true;
        }
        if (dframe == null) {
            if (!guiTabExists(sketch)) {
                sketch.addFile(new File(sketchbookFolder, G4P_TOOL_DATA_FOLDER + SEP + PDE_TAB_NAME));
            }
            sketch.prepareDataFolder();
            File configFolder = new File(sketchFolder, CONFIG_FOLDER);
            if (!configFolder.exists()) {
                configFolder.mkdir();
            }
            dframe = new GuiDesigner(editor);
            System.out.println("===================================================");
            System.out.println("   ##tool.name## V##tool.prettyVersion## created by ##author.name##");
            System.out.println("===================================================");
        }
        dframe.setVisible(true);
        dframe.setExtendedState(JFrame.NORMAL);
        dframe.toFront();
    }

    /**
	 * See if the G4P library has been installed in the SketchBook libraries folder correctly
	 * @param sketchbookLibrariesFolder
	 * @return true if found else false
	 */
    private boolean g4pJarExists(File sketchbookLibrariesFolder) {
        File f = new File(sketchbookLibrariesFolder, G4P_LIB);
        return f.exists();
    }

    /**
	 * See if the gui.pde tab has been created already if not
	 * @param sketch
	 * @return
	 */
    private boolean guiTabExists(Sketch sketch) {
        File f = new File(sketch.getFolder(), PDE_TAB_NAME);
        return f.exists();
    }
}
