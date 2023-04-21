package org.quickconnectfamily.hybrid;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.quickconnect.QuickConnect;
import org.quickconnectfamily.hybrid.QCAndroid;
import org.quickconnectfamily.hybrid.QCPlugin;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class QCViewsPlugin {

    private static HashMap<String, Class> viewTypeMap;

    public static void init() {
        viewTypeMap = new HashMap<String, Class>();
        viewTypeMap.put("Web", QCWebView.class);
        viewTypeMap.put("Map", QCMapView.class);
        viewTypeMap.put("Container", QCViewGroup.class);
    }

    public static void removeView(QCAndroid theContext, View viewToModify, String id, HashMap settings) {
        if (viewToModify.getClass() == QCViewGroup.class) {
            QCViewsPlugin.removeChildViews(theContext, (QCViewGroup) viewToModify, id);
        }
        QCAndroid instance = QCAndroid.getInstance();
        if (settings.get("parentId") != null || ((String) settings.get("parentId")).length() != 0) {
            QCViewGroup parent = (QCViewGroup) instance.getView((String) settings.get("parentId"));
            parent.removeView(viewToModify);
            HashMap pm = instance.viewStorage.get(settings.get("parentId"));
            ArrayList pl = (ArrayList) pm.get("children");
            pl.remove(id);
        } else {
            instance.layout.removeView(viewToModify);
        }
        instance.viewStorage.remove(id);
    }

    private static void removeChildViews(QCAndroid theContext, QCViewGroup parentView, String jsid) {
        HashMap viewInfo = theContext.viewStorage.get(jsid);
        if (viewInfo.get("children") != null) {
            ArrayList<String> children = (ArrayList<String>) viewInfo.get("children");
            Iterator i = children.iterator();
            while (i.hasNext()) {
                String childId = (String) i.next();
                View childView = theContext.getView(childId);
                if (childView.getClass() == QCViewGroup.class) {
                    QCViewsPlugin.removeChildViews(theContext, (QCViewGroup) childView, childId);
                }
                parentView.removeView(childView);
                theContext.viewStorage.remove(childId);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static View addViewForType(QCAndroid theContext, String aType, HashMap settings) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class aViewClass = viewTypeMap.get(aType);
        Constructor constructor = aViewClass.getConstructor(new Class[] { QCAndroid.class, HashMap.class });
        Object[] args = new Object[] { theContext, settings };
        View theView = (View) constructor.newInstance(args);
        String parentId = (String) settings.get("parentId");
        if (parentId != null && parentId.length() != 0) {
            try {
                QCViewGroup addTo = (QCViewGroup) theContext.getView(parentId);
                HashMap hm = new HashMap();
                hm.put("parent", parentId);
                HashMap pm = theContext.viewStorage.get(parentId);
                if (pm.get("children") == null) {
                    ArrayList<String> children = new ArrayList<String>();
                    children.add((String) settings.get("id"));
                    pm.put("children", children);
                } else {
                    ((ArrayList<String>) pm.get("children")).add((String) settings.get("id"));
                }
                Date now = new Date();
                long id = now.getTime();
                Integer idi = (int) id;
                theView.setId(idi);
                theView.setTag((String) settings.get("id"));
                hm.put("id", theView.getId());
                theContext.viewStorage.put((String) settings.get("id"), hm);
                addTo.addView(theView);
            } catch (Exception e) {
                if (e.getClass() == ClassCastException.class) {
                    System.out.println("Cannot add view to non-ViewGroup view");
                } else {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            HashMap hm = new HashMap();
            hm.put("parent", parentId);
            Date now = new Date();
            long id = now.getTime();
            Integer idi = (int) id;
            theView.setId(idi);
            hm.put("id", theView.getId());
            theContext.viewStorage.put((String) settings.get("id"), hm);
            theContext.addView(theView);
        }
        return theView;
    }
}
