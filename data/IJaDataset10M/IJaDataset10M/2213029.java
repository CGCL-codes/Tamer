package com.cosylab.vdct.graphics;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;

/**
 * Insert the type's description here.
 * Creation date: (4.2.2001 15:04:32)
 * @author Matej Sekoranja
 */
public interface GUIMenuInterface {

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:12:21)
 */
    void moveOrigin(int direction);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:12:21)
 */
    void baseView();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:07:46)
 */
    void copy();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:07:40)
 */
    void cut();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:08:30)
 */
    void delete();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:08:14)
 */
    void group(String groupName);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:10:27)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void importDB(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:10:27)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void importTemplateDB(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:10:27)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void importFields(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:10:27)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void importBorder(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:10:38)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void importDBD(File file) throws IOException;

    void loadRdbGroup(JFrame guiContext);

    void saveRdbGroup(JFrame guiContext, boolean dialog);

    /**
 * Insert the method's description here.
 * Creation date: (29.4.2001 11:37:15)
 * @return boolean
 */
    boolean isModified();

    boolean isMacroPortsIDChanged();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:11:41)
 */
    void levelUp();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:04:49)
 */
    void newCmd();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:10:15)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void openDB(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:10:50)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void openDBD(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:07:54)
 */
    void paste();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:07:54)
 */
    void pasteAtPosition(int pX, int pY);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:07:19)
 */
    void print();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:07:33)
 */
    void redo();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:08:06)
 */
    void rename();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:08:06)
 */
    void rename(String oldName, String newName);

    void morph();

    void morph(String name, String newType);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:48:15)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void save(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:11:04)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void saveAsGroup(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:11:04)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void saveAsTemplate(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:48:15)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void export(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:11:04)
 * @param file java.io.File
 * @exception java.io.IOException The exception description.
 */
    void exportAsGroup(File file) throws IOException;

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:08:39)
 */
    void selectAll();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:11:32)
 * @param state boolean
 */
    void setFlatView(boolean state);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:48:02)
 * @param scale double
 */
    void setScale(double scale);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:13:01)
 * @param state boolean
 */
    void showGrid(boolean state);

    /**
 * Insert the method's description here.
 * Creation date: (27.4.2001 19:54:15)
 * @param state boolean
 */
    void showNavigator(boolean state);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:57:42)
 */
    void smartZoom();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:13:22)
 * @param state boolean
 */
    void snapToGrid(boolean state);

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:07:26)
 */
    void undo();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:08:21)
 */
    void ungroup();

    /**
 * Insert the method's description here.
 * Creation date: (4.2.2001 15:08:21)
 */
    void updateGroupLabel();

    void systemCopy();

    void systemPaste();

    void reset();

    void close();

    void closeAll();
}
