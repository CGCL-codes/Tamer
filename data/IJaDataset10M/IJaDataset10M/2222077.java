package eln.util;

import java.io.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import eln.*;
import eln.editors.GenericEditorWrapper;
import eln.server.NotebookServerProxy;
import eln.server.NotebookServerException;

public class NetNBELoader extends NBELoader {

    static int kBlockSize = 1024;

    private NotebookServerProxy mServer;

    public NetNBELoader(NotebookServerProxy nsp) {
        super();
        mServer = nsp;
    }

    public synchronized void loadEditors() {
        Class editorClass = null;
        Vector filenames = null;
        try {
            filenames = mServer.getEditorClassNames();
        } catch (NotebookServerException nse) {
            System.err.println("Error getting Editor names: " + nse);
        }
        Enumeration fileEnum = filenames.elements();
        while (fileEnum.hasMoreElements()) {
            String edName = (String) fileEnum.nextElement();
            try {
                editorClass = Class.forName(edName);
                Class[] classInterfaces = editorClass.getInterfaces();
                if (classInterfaces.length == 0) {
                    editorClass = new GenericEditorWrapper().getClass();
                    mEditorTable.put(edName, editorClass);
                }
                for (int j = 0; j < classInterfaces.length; j++) {
                    if (classInterfaces[j].getName().endsWith("INBEditor")) {
                        mEditorTable.put(edName, editorClass);
                        break;
                    } else {
                        editorClass = new GenericEditorWrapper().getClass();
                        mEditorTable.put(edName, editorClass);
                    }
                }
            } catch (ClassNotFoundException noClass) {
                System.err.print("Could not load class " + edName + " ");
                if (edName.startsWith(NBELoader.ELNEDITORPACKAGE)) {
                    edName = edName.substring(NBELoader.ELNEDITORPACKAGE.length());
                    try {
                        editorClass = Class.forName(edName);
                        Class[] classInterfaces = editorClass.getInterfaces();
                        if (classInterfaces.length == 0) {
                            editorClass = new GenericEditorWrapper().getClass();
                            mEditorTable.put(edName, editorClass);
                        }
                        for (int j = 0; j < classInterfaces.length; j++) {
                            if (classInterfaces[j].getName().endsWith("INBEditor")) {
                                mEditorTable.put(edName, editorClass);
                                break;
                            } else {
                                editorClass = new GenericEditorWrapper().getClass();
                                mEditorTable.put(edName, editorClass);
                            }
                        }
                        System.err.println("but successfully loaded " + edName);
                    } catch (ClassNotFoundException noClassShort) {
                        System.err.println(" and could not load class " + edName + ".");
                    }
                }
            }
        }
    }
}
