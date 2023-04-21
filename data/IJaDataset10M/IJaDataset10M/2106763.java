package pspdash;

import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.TreePath;
import pspdash.data.DataRepository;
import pspdash.data.StringData;
import pspdash.data.SimpleData;

public class EVTaskListCached extends EVTaskListXMLAbstract {

    public static final String CACHED_OBJECT_TYPE = "pspdash.EVTaskListCached";

    public static final String LOCAL_NAME_ATTR = "LocalName";

    private int objectID;

    private String localName;

    private CachedObject object;

    private String xmlSource = null;

    private EVTaskListCached() {
        super(" ", " ", false);
    }

    public EVTaskListCached(String taskListName, ObjectCache cache) {
        super(taskListName, taskListName, false);
        int slashPos = taskListName.indexOf('/');
        localName = taskListName.substring(slashPos + 1);
        objectID = Integer.parseInt(taskListName.substring(6, slashPos));
        object = cache.getCachedObject(objectID, -1);
        openXML();
    }

    private boolean openXML() {
        if (object == null) {
            createErrorRootNode(localName, resources.getString("Cannot_Open_Schedule_Error"));
            return false;
        }
        String xmlDoc = object.getString("UTF-8");
        String errorMessage = null;
        if (object.olderThanAge(3)) {
            errorMessage = object.getErrorMessage();
            String errorPrefix = (object.olderThanAge(5) ? resources.getString("Out_Of_Date_Error") : " " + resources.getString("Out_Of_Date_Warning")) + " ";
            if (errorMessage == null) errorMessage = errorPrefix; else if (xmlDoc != null) errorMessage = errorPrefix + errorMessage;
        }
        return openXML(xmlDoc, localName, errorMessage);
    }

    public void recalc() {
        if (object != null) object.refresh(0, 1000);
        openXML();
        super.recalc();
    }

    public static boolean validName(String taskListName) {
        return (taskListName != null && taskListName.startsWith("cache:"));
    }

    public static boolean exists(String taskListName, ObjectCache cache) {
        try {
            if (taskListName == null) return false;
            int slashPos = taskListName.indexOf('/');
            if (slashPos < 7) return false;
            int objectID = Integer.parseInt(taskListName.substring(6, slashPos));
            CachedObject object = cache.getCachedObject(objectID, -1);
            return (object != null && CACHED_OBJECT_TYPE.equals(object.getType()));
        } catch (Exception e) {
        }
        return false;
    }

    public static String buildTaskListName(int id, String displayName) {
        return "cache:" + id + "/" + displayName;
    }

    public static String getNameFromXML(String xmlDoc) {
        EVTaskListCached tl = new EVTaskListCached();
        if (tl.openXML(xmlDoc, null)) return ((EVTask) tl.root).name; else return null;
    }
}
