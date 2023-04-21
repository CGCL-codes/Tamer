package tico.imageGallery;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import tico.components.TMenuItem;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

/**
 * Implementation of the image gallery menu
 * 
 * @author Patricia M. Jaray
 * @version 1.0 May 20, 2008
 */
public class TImageGalleryMenuBar {

    private TIGActionSet actionSet;

    private static TIGDataBase dataBase;

    /**
	 * Creates a <code>JMenu</code> with the options of the image gallery
	 * 
	 * @param editor The boards' editor
	 * 
	 * @return The generated <code>JMenu</code> for the <code>editor</code> menu bar
	 */
    public JMenu createImageGalleryMenu(TEditor editor) {
        createDataBase();
        this.actionSet = new TIGActionSet(editor, dataBase);
        JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.GALLERY_MENU"));
        TMenuItem menuItem;
        JMenu submenu;
        menuItem = new TMenuItem(actionSet.getAction(TIGActionSet.GALLERY_KEY_ACTION));
        menu.add(menuItem);
        menu.add(new JSeparator());
        menuItem = new TMenuItem(actionSet.getAction(TIGActionSet.GALLERY_NEW_IMAGE_ACTION));
        menuItem.setText(TLanguage.getString("TEditorMenuBar.IMAGES_NEW"));
        menu.add(menuItem);
        menuItem = new TMenuItem(actionSet.getAction(TIGActionSet.GALLERY_MANAGE_ACTION));
        menuItem.setText(TLanguage.getString("TEditorMenuBar.IMAGES_MODIFY"));
        menu.add(menuItem);
        menuItem = new TMenuItem(actionSet.getAction(TIGActionSet.GALLERY_DELETE_ACTION));
        menuItem.setText(TLanguage.getString("TEditorMenuBar.IMAGES_DELETE"));
        menu.add(menuItem);
        menu.add(new JSeparator());
        menuItem = new TMenuItem(actionSet.getAction(TIGActionSet.GALLERY_IMPORT_DB_ACTION));
        menuItem.setText(TLanguage.getString("TEditorMenuBar.LOAD_DB"));
        menu.add(menuItem);
        menuItem = new TMenuItem(actionSet.getAction(TIGActionSet.GALLERY_EXPORT_DB_ACTION));
        menuItem.setText(TLanguage.getString("TEditorMenuBar.EXPORT_DB"));
        menu.add(menuItem);
        return menu;
    }

    public static void createDataBase() {
        dataBase = new TIGDataBase();
        TIGDataBase.conectDB();
        TIGDataBase.closeDB();
    }
}
