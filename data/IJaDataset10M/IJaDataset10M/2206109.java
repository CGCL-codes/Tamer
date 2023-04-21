package com.robrohan.fangorn;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import com.robrohan.tools.AshpoolDB;
import com.robrohan.tools.ImageManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.robrohan.tools.Globals;

/**
 * This is the base class for all the "windows". Sub class this to add a new plugin
 * or window to fangorn
 * 
 * @author rob
 */
public class Enting extends JFrame {

    /** unknown type of window */
    public static final byte TYPE_UNK = 0;

    /** normal window */
    public static final byte TYPE_WINDOW = 1;

    /** the system error window */
    public static final byte TYPE_ERR = 2;

    /** the xml template browser */
    public static final byte TYPE_TEMPLATE_BROWSER = 3;

    /** a simple dialog */
    public static final byte TYPE_DIALOG = 4;

    /** the type of this window (default to dialog) */
    protected byte type = TYPE_WINDOW;

    /** this is so we can use "this" in the onFocus adapter */
    private final Enting __this;

    /**
	 * Create a new window with the passed title (which will be its key btw) and 
	 * set weather it's modal or not
	 */
    public Enting(String title, boolean modal, byte type) {
        super();
        setTitle(title);
        this.type = type;
        __this = this;
        this.setIconImage(ImageManager.getImage("fan.16.treebeard").getImage());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                closeWindow();
            }

            public void windowGainedFocus(WindowEvent evt) {
                Globals.setActiveWindow(__this);
            }

            public void windowActivated(WindowEvent evt) {
                Globals.setActiveWindow(__this);
            }
        });
        this.setFocusCycleRoot(true);
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        Globals.addWindow(title, this);
        Globals.refreshActiveWindowList();
    }

    /** 
	 * Creates a new instance of Enting with just a title 
	 */
    public Enting(String title) {
        this(title, false, Enting.TYPE_WINDOW);
    }

    /**
	 * Creates a new non-modal window
	 */
    public Enting() {
        this("Window", false, Enting.TYPE_WINDOW);
    }

    /**
	 * Restores (or sets) the state of this window
	 * @param title
	 * @param height
	 * @param width
	 * @param x
	 * @param y
	 */
    public void restoreState(String title, int height, int width, int x, int y) {
        this.setTitle(title);
        this.setBounds(x, y, width, height);
    }

    /**
	 * Fangorn loader can pass in a result set to restore the values for this Ent.
	 * The data in the result set will be defined by the TYPE_XXX setting, though
	 * they are coming from a database table and should be pretty uniform. The record
	 * set will be positioned at the proper place for this type so no need to loop
	 * 
	 * @param rs the single record to get restore data from
	 */
    public void restoreState(ResultSet rs) {
        try {
            setTitle(rs.getString("name"));
            setSize(rs.getInt("w"), rs.getInt("h"));
            setLocation(rs.getInt("y"), rs.getInt("x"));
        } catch (SQLException e) {
            Globals.logWarning(e);
        }
    }

    /**
	 * After the restore state is called, some other items happen out side of the
	 * control of this window. When those items are finished this method is called
	 * so any post restore code should go here
	 */
    public void restoreStatePost() {
        ;
    }

    /**
	 * Fangorn can ask for the Enting to save its state. If so this method will be
	 * called
	 * @param projectid
	 */
    public void saveState(int projectid) {
        try {
            String qry = "insert into desktop( projectid, type, name, h, w, x, y, iconify" + " )values( " + " " + projectid + ", " + " " + type + ", " + " '" + this.getTitle() + "', " + " " + getHeight() + ", " + " " + getWidth() + ", " + " " + getX() + ", " + " " + getY() + ", " + " " + false + " " + ");";
            AshpoolDB.executeQuery(qry);
        } catch (Exception e) {
            System.err.println("I can't save myself! " + e.toString());
            e.printStackTrace(System.err);
        }
    }

    /**
	 * Do anything that needs cleaning up on window close
	 */
    public void closeWindow() {
        Globals.removeWindow(getTitle());
        Globals.refreshActiveWindowList();
        destroy();
    }

    /**
	 * do anything to clean up this windows resources if needed
	 */
    public void destroy() {
        System.err.println(getTitle() + " leaving...");
    }

    /**
	 * @return Returns the type.
	 */
    public byte getType() {
        return type;
    }

    /**
	 * @param type The type to set.
	 */
    public void setType(byte type) {
        this.type = type;
    }
}
