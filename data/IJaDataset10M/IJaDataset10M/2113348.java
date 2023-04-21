package fr.emn.easymol.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/** The interface to store all the constants used in <code>EasyMol</code>.<br><br>
*   It holds screen settings, dimensions of windows, paths and name of icons files,
*   title of windows and menu labels, tooltips for the buttons, name of the actions, error messages
*   and the the GPL disclaimer.
**/
public interface GUIConstants {

    /**Used to get the defaults settings of the screen. **/
    public final Dimension DESKTOP = Toolkit.getDefaultToolkit().getScreenSize();

    /**The height of the screen. **/
    public final int SCREEN_HEIGHT = (int) DESKTOP.getHeight();

    /** The width of the screen. **/
    public final int SCREEN_WIDTH = (int) DESKTOP.getWidth();

    /** A Point representing the coordinates of the upper left corner of the screen. **/
    public final Point DESKTOP_UPPER_LEFT_CORNER = new Point(0, 0);

    /** A Point representing the coordinates of the upper right corner of the screen. **/
    public final Point DESKTOP_UPPER_RIGHT_CORNER = new Point((int) DESKTOP.getWidth() / 2, 0);

    /** A Point representing the coordinates of the lower left corner of the screen. **/
    public final Point DESKTOP_LOWER_LEFT_CORNER = new Point(0, (int) DESKTOP.getHeight() / 2);

    /** A Point representing the coordinates of the lower right corner of the screen. **/
    public final Point DESKTOP_LOWER_RIGHT_CORNER = new Point((int) DESKTOP.getWidth() / 2, (int) DESKTOP.getHeight() / 2);

    /** The width of the <code>VisualisationWindow</code>. **/
    public static final int WIDTH_VISU = 800;

    /** The width of the <code>NavigationWindow</code>. **/
    public static final int WIDTH_NAVIGATION = 400;

    /** The height of the <code>ToolBarWindow</code>. **/
    public static final int HEIGHT_TB = 100;

    /** The height of the <code>ConsoleWindow</code>. **/
    public final int HEIGHT_CONSOLE = 150;

    /** The height of the <code>VisualisationWindow</code>. **/
    public final int HEIGHT_VISU = 480;

    /** The height of the <code>NavigationWindow</code>. **/
    public final int HEIGHT_NAVIGATION = 400;

    /** The path to the buttons icons. **/
    public final String PATH_BUTTONS_PICS = "./pix/Buttons/";

    /** The path to the splash screen image. **/
    public final String PATH_SPLASH_SCREEN = "./pix/Splash/";

    /** The path to the GPL License. **/
    public final String PATH_GPL = "./COPYING";

    /** Name of the new pixmap.. **/
    public final String PIC_NEW = "new.gif";

    /** Name of the load pixmap.. **/
    public final String PIC_LOAD = "load.gif";

    /** Name of the save pixmap..  **/
    public final String PIC_SAVE = "save.gif";

    /**Name of the saveAs pixmap..  **/
    public final String PIC_SAVE_AS = "saveas.gif";

    /** Name of the closeAll pixmap..  **/
    public final String PIC_CLOSE_ALL = "closeAll.gif";

    /** Name of the saveAll pixmap..  **/
    public final String PIC_SAVE_ALL = "saveAll.gif";

    /** Name of the quit pixmap..  **/
    public final String PIC_QUIT = "quit.gif";

    /** Name of the cut pixmap.. **/
    public final String PIC_CUT = "cut.gif";

    /** Name of the copy pixmap.  **/
    public final String PIC_COPY = "copy.gif";

    /** Name of the paste pixmap.  **/
    public final String PIC_PASTE = "paste.gif";

    /** Name of the undo pixmap.  **/
    public final String PIC_UNDO = "undo.gif";

    /** Name of the redo pixmap.  **/
    public final String PIC_REDO = "redo.gif";

    /** Name of the carbon pixmap. **/
    public final String PIC_CARBON = "carbon.gif";

    /** Name of the oxygen pixmap. **/
    public final String PIC_OXYGEN = "oxygen.gif";

    /** Name of the hydrogen pixmap. **/
    public final String PIC_HYDROGEN = "hydrogen.gif";

    /** Name of the nitrogen pixmap. **/
    public final String PIC_NITROGEN = "nitrogen.gif";

    /** Name of the remove atom pixmap. **/
    public final String PIC_ATOM_REMOVE = "removeAtom.gif";

    /** Name of the single bound pixmap. **/
    public final String PIC_BOND_SG = "singleLink.gif";

    /** Name of the double bound pixmap. **/
    public final String PIC_BOND_DB = "doubleLink.gif";

    /** Name of the triple bound pixmap. **/
    public final String PIC_BOND_TR = "tripleLink.gif";

    /** Name of the remove link pixmap. **/
    public final String PIC_BOND_REMOVE = "removeLink.gif";

    /** Name of the render pixmap. **/
    public final String PIC_RENDER = "render.gif";

    /** Name of the arrange pixmap. **/
    public final String PIC_REARRANGE = "arrange.gif";

    /** The title of the main window. **/
    public final String TITLE_TB_WIN = "EasyMol";

    /** The title of the <code>ConsoleWindow</code>. **/
    public final String TITLE_CONSOLE = "Console";

    /** The title of the <code>NavigationWindow</code>. **/
    public static final String TITLE_NAVIGATION = "3D Navigation";

    /** The first part of the title of a <code>VisualisationWindow</code>. **/
    public static final String TITLE_VISU_ONE = "Molecule: ";

    /** The second part of the title of a <code>VisualisationWindow</code>. **/
    public static final String TITLE_VISU_TWO = " in file: ";

    /** The title of the new molecule dialog frame. **/
    public final String TITLE_NEW_DIALOG = "New Molecule";

    /** The title of the new molecule dialog name text label. **/
    public final String TITLE_NEW_DIALOG_MOLECULE_NAME_LABEL = "New Molecule";

    /** The title of the new molecule dialog root atom label. **/
    public final String TITLE_NEW_DIALOG_ROOT_ATOM_LABEL = "What will be the first atom of your molecule?";

    /** The title of the about window. **/
    public final String TITLE_ABOUT = "About Easymol";

    /** The title of the GPL window. **/
    public final String TITLE_GPL = "The GPL License";

    /** The title of the file menu. **/
    public final String TITLE_MENU_FILE = "File";

    /** The title of the edit menu. **/
    public final String TITLE_MENU_EDIT = "Edit";

    /** The title of the insert menu. **/
    public final String TITLE_MENU_INSERT = "Insert";

    /** The title of the windows menu. **/
    public final String TITLE_MENU_WINDOWS = "Windows";

    /** The title of the help menu. **/
    public final String TITLE_MENU_HELP = "Help";

    /** The title of the console item in the windows menu. **/
    public final String TITLE_MENU_ITEM_CONSOLE = "Console";

    /** The title of the about item in the help menu. **/
    public final String TITLE_MENU_ITEM_ABOUT = "About";

    /** The title of the help item in the help menu. **/
    public final String TITLE_MENU_ITEM_HELP = "Help";

    /** The title of the gpl button. **/
    public final String TITLE_BUTTON_GPL = "GPL License...";

    /** The title of the close button on the about panel. **/
    public final String TITLE_BUTTON_ABOUT_CLOSE = "Close";

    /** The title of the clear button of the console. **/
    public final String TITLE_BUTTON_CONSOLE_CLEAR = "Clear";

    /** The title of the "2D View Only" button of the <code>VisualisationWindow</code>. **/
    public final String TITLE_BUTTON_2D_ONLY = "2D View Only";

    /** The title of the "3D View Only" button of the <code>VisualisationWindow</code>. **/
    public final String TITLE_BUTTON_3D_ONLY = "3D View Only";

    /** The title of the "2D And 3D Views" button of the <code>VisualisationWindow</code>. **/
    public final String TITLE_BUTTON_2D_AND_3D = "2D and 3D";

    /**Tooltip for the "New" action. **/
    public final String TOOLTIP_NEW = "Create a new molecule";

    /**Tooltip for the "Load" action. **/
    public final String TOOLTIP_LOAD = "Load a molecule from file";

    /**Tooltip for the "Save" action. **/
    public final String TOOLTIP_SAVE = "Save a molecule to disk";

    /**Tooltip for the "Add Carbon Atom" action. **/
    public final String TOOLTIP_CARBON = "Add a Carbon Atom";

    /**Tooltip for the "Add Hydrogen Atom" action. **/
    public final String TOOLTIP_HYDROGEN = "Add an Hydrogen Atom";

    /**Tooltip for the "Add Nitrogen Atom" action. **/
    public final String TOOLTIP_NITROGEN = "Add a Nitrogen Atom";

    /**Tooltip for the "Add Oxygen Atom" action. **/
    public final String TOOLTIP_OXYGEN = "Add an Oxygen Atom";

    /**Tooltip for the "Remove Atom" action. **/
    public final String TOOLTIP_ATOM_REMOVE = "Remove the selected atom";

    /**Tooltip for the "Add Single Bond" action. **/
    public final String TOOLTIP_BOND_SG = "Add a Single Bond";

    /**Tooltip for the "Add Double Bond" action. **/
    public final String TOOLTIP_BOND_DB = "Add a Double Bond";

    /**Tooltip for the "Add Triple Bond" action. **/
    public final String TOOLTIP_BOND_TR = "Add a Triple Bond";

    /**Tooltip for the "Remove Bond" action. **/
    public final String TOOLTIP_BOND_REMOVE = "Remove the bond between the selected atoms";

    /**Tooltip for the "Render" action. **/
    public final String TOOLTIP_RENDER = "Render or refresh the 3D view";

    /**Tooltip for the "Rearrange" action. **/
    public final String TOOLTIP_REARRANGE = "Rearrange the Lewis representation of the molecule";

    /**Tooltip for the "Save As" action. **/
    public final String TOOLTIP_SAVE_AS = "Save a molecule to disk with a different name";

    /**Tooltip for the "Save All" action. **/
    public final String TOOLTIP_SAVE_ALL = "Save all the molecules to disk";

    /**Tooltip for the "Close All" action. **/
    public final String TOOLTIP_CLOSE_ALL = "Close all files. Save if needed";

    /**Tooltip for the "Quit" action. **/
    public final String TOOLTIP_QUIT = "Exit EasyMol. Save if needed";

    /**Name for the "New" action. **/
    public final String NAME_NEW = "New";

    /**Name for the "Load" action. **/
    public final String NAME_LOAD = "Load...";

    /**Name for the "Save" action. **/
    public final String NAME_SAVE = "Save";

    /**Name for the "Save As" action. **/
    public final String NAME_SAVE_AS = "Save As...";

    /**Name for the "Save All" action. **/
    public final String NAME_SAVE_ALL = "Save All Files";

    /**Name for the "Close All" action. **/
    public final String NAME_CLOSE_ALL = "Close All Files";

    /**Name for the "Quit" action. **/
    public final String NAME_QUIT = "Quit";

    /**Name for the "Cut" action. **/
    public final String NAME_CUT = "Cut";

    /**Name for the "Copy" action. **/
    public final String NAME_COPY = "Copy";

    /**Name for the "Paste" action. **/
    public final String NAME_PASTE = "Paste";

    /**Name for the "Undo" action. **/
    public final String NAME_UNDO = "Undo";

    /**Name for the "Redo" action. **/
    public final String NAME_REDO = "Redo";

    /**Name for the "Add Carbon Atom" action. **/
    public final String NAME_CARBON = "Carbon";

    /**Name for the "Add Oxygen Atom" action. **/
    public final String NAME_OXYGEN = "Oxygen";

    /**Name for the "Add Hydrogen Atom" action. **/
    public final String NAME_HYDROGEN = "Hydrogen";

    /**Name for the "Add Nitrogen Atom" action. **/
    public final String NAME_NITROGEN = "Nitrogen";

    /**Name for the "Remove Atom Action" action. **/
    public final String NAME_ATOM_REMOVE = "Remove atom";

    /**Name for the "Add Single Bound" action. **/
    public final String NAME_BOND_SG = "Single bond";

    /**Name for the "Add Double Bound" action. **/
    public final String NAME_BOND_DB = "Double bond";

    /**Name for the "Add Triple Bound" action. **/
    public final String NAME_BOND_TR = "Triple bond";

    /**Name for the "Remove Bond Action" action. **/
    public final String NAME_BOND_REMOVE = "Remove bond";

    /**Name for the "Render" action. **/
    public final String NAME_RENDER = "Render";

    /**Name for the "Rearrange" action. **/
    public final String NAME_REARRANGE = "Rearrange";

    /**Name for the label of the carbon choice in the new window dialog. **/
    public final String NAME_NEW_DIALOG_CARBON_ATOM = "Carbon";

    /**Name for the label of the oxygen choice in the new window dialog. **/
    public final String NAME_NEW_DIALOG_OXYGEN_ATOM = "Oxygen";

    /**Name for the label of the nitrogen choice in the new window dialog. **/
    public final String NAME_NEW_DIALOG_NITROGEN_ATOM = "Nitrogen";

    /** When there is no name registered on a <code>VisualisationWindow</code>, this constant is used. **/
    public final String NAME_UNNAMED = "(no name)";

    /** The filename of the splash screen. **/
    public final String NAME_SPLASH = "3DSplashScreen.jpg";

    /** Message when save file type is incorrect. **/
    public final String ERR_INCORRECT_SAVE_FILE_TYPE = "Please give your file a .xml or .XML extension";

    /** Message when there was a problem writing to disk. **/
    public final String ERR_SAVING = "There was a problem writing the molecule to disk. Directory does not exist";

    /** Message when there was a problem loading from disk. **/
    public final String ERR_LOADING = "There was a problem loading the file from disk. File may be corrupted";

    /** Message when load file type is incorrect. **/
    public final String ERR_INCORRECT_LOAD_FILE_TYPE = "This is not a valid file. Please select an XML file.";

    /** Message when there is no molecule available. **/
    public final String ERR_NO_MOLECULE_AVAILABLE = "There is no molecule available. Please load or create one.";

    /**Message when "save" is performed on an empty molecule. It doesn't have to be saved. **/
    public final String ERR_EMPTY_MOLECULE = "There is no need to save an empty molecule.";

    /** Message when the GPL license couldn't be loaded. **/
    public final String ERR_GPL_LOAD = "There was a problem loading the GPL text file";

    /**Info that some files were unnamed hence could not be saved. **/
    public final String INFO_UNNAMED_FILES_PRESENT = "One or more files could not be saved because they are unnamed.";

    /**Info that there are no changes to save. **/
    public final String INFO_NO_CHANGES_TO_SAVE = "No changes need to be saved in file ";

    /**Info that one instance of <code>ToolBarWindow</code>. **/
    public final String INFO_ALREADY_ONE_TBWIN = "There is already an instance of ToolBarWindow created";

    /**Info that proposes the user to save changes. **/
    public final String INFO_MOLECULE_MODIFIED = "The molecule appears to have been modified.\nSave changes?";

    /** Info message displayed in the console. **/
    public final String INFO_WELCOME = "Welcome to EasyMol";

    /** Message when write was successful. **/
    public final String SUCCESS_SAVE = "Successfully written the ";

    /** The second part of the message output when save was successful. **/
    public final String SUCCESS_SAVE_LOCATION = " molecule to disk in file ";

    /** Message when load was successful. **/
    public final String SUCCESS_LOAD = "Successfully loaded the ";

    /** The second part of the message output when load was successful.**/
    public final String SUCCESS_LOAD_LOCATION = " molecule from file ";

    /** The free software disclaimer. **/
    public final String DISCLAIMER = "<HTML>This is EasyMol v0.2b, the 2D/3D molecule viewer.<br>Copyright (C) 2002 Alexandre Vaughan,Micha�l L�ger, Thomas Nagy et Vincent Rubiolo.<br><br> This program is free software; You can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.<br><br> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.<br><br> You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA. <HTML>";
}
