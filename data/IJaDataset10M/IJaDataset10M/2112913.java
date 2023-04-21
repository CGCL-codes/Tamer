package org.ximtec.igesture.core.composite;

import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.sigtec.ink.Note;
import org.sigtec.ink.NoteTool;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.ximtec.igesture.core.Gesture;
import org.ximtec.igesture.core.GestureSample;
import org.ximtec.igesture.core.GestureSample3D;
import org.ximtec.igesture.util.Constant;
import org.ximtec.igesture.util.XMLParser;
import org.ximtec.igesture.util.additions3d.Note3D;

/**
 * @author Bjorn Puype, bpuype@gmail.com
 *
 */
public class ConstraintTool {

    private static final Map<String, String> deviceMapping = new HashMap<String, String>();

    static {
        XMLParser parser = new XMLParser() {

            @Override
            public void execute(ArrayList<NodeList> nodeLists) {
                String name = ((Node) nodeLists.get(0).item(0)).getNodeValue();
                String type = ((Node) nodeLists.get(1).item(0)).getNodeValue();
                deviceMapping.put(name, type);
            }
        };
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add(Constant.XML_NODE_NAME);
        nodes.add(Constant.XML_NODE_TYPE);
        try {
            URL path = ConstraintTool.class.getClassLoader().getResource(Constant.XML_DEVICEMAPPINGS);
            parser.parse(path.getFile(), Constant.XML_DEVICEMAPPINGS_NODE, nodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");

    static {
        df.setLenient(false);
    }

    public static String getGestureType(String deviceType) {
        return deviceMapping.get(deviceType);
    }

    public static boolean isBoundsDiagonalValid(List<Note> notes, double minDistance, double maxDistance) {
        boolean valid = true;
        Rectangle2D bounds = NoteTool.getBounds2D(notes);
        double w = bounds.getWidth();
        double h = bounds.getHeight();
        double d = Math.sqrt(Math.pow(w, 2) + Math.pow(h, 2));
        if (d < minDistance || d > maxDistance) valid = false;
        return valid;
    }

    public static long calculateTimeInMillis(Calendar time) {
        long t = 0;
        t += time.get(Calendar.MILLISECOND);
        t += time.get(Calendar.SECOND) * 1000;
        t += time.get(Calendar.MINUTE) * 60 * 1000;
        t += time.get(Calendar.HOUR) * 24 * 60 * 1000;
        return t;
    }

    public static void permute(int level, String permuted, boolean used[], String original, Set<String> results) {
        int length = original.length();
        if (level == length) {
            results.add(permuted);
        } else {
            for (int i = 0; i < length; i++) {
                if (!used[i]) {
                    used[i] = true;
                    permute(level + 1, permuted + original.charAt(i), used, original, results);
                    used[i] = false;
                }
            }
        }
    }

    /**
	 * Get the start or end timestamp of a gesture.
	 * @param gesture	the gesture from which to get the timestamp
	 * @param start		true to get the start timestamp, false to get the end timestamp
	 * @return	timestamp
	 */
    public static long getTimeStamp(Gesture<?> gesture, boolean start) {
        long timestamp = 0;
        if (gesture instanceof GestureSample) {
            Note note = ((GestureSample) gesture).getGesture();
            if (start) timestamp = note.getStartPoint().getTimestamp(); else timestamp = note.getEndPoint().getTimestamp();
        } else if (gesture instanceof GestureSample3D) {
            Note3D record = ((GestureSample3D) gesture).getGesture();
            if (start) timestamp = record.getStartPoint().getTimeStamp(); else timestamp = record.getEndPoint().getTimeStamp();
        } else ;
        return timestamp;
    }

    public static SimpleDateFormat getDateFormatter() {
        return df;
    }
}
