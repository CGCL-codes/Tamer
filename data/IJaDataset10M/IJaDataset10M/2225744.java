package jgnash.net.rpc;

import java.io.*;
import jgnash.xml.*;
import jgnash.engine.Engine;

/** This class handles the ugly side of sending and receiving objects
 * through a socket
 * <p>
 * $Id: RPCConnection.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 */
public abstract class RPCConnection {

    static final String XMLSTOP = "<!-- end -->";

    public static Object readXMLObject(String objectName, InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuffer data = new StringBuffer();
            while (true) {
                String line = reader.readLine();
                if (line.equals(XMLSTOP)) {
                    break;
                }
                data.append(line);
            }
            XMLInputStream in = new XMLInputStream(new StringReader(data.toString()), Engine.FILE_VERSION);
            Object o = in.readXMLObject(objectName);
            return o;
        } catch (IOException ioe) {
            System.err.println("RPCConnection error: " + ioe);
            return null;
        }
    }

    public static void writeXMLObject(String objectName, XMLObject object, OutputStream stream) {
        try {
            XMLOutputStream out = new XMLOutputStream(stream, Engine.FILE_VERSION);
            PrintWriter pw = out.getWriter();
            out.writeXMLObject(objectName, object);
            pw.println(XMLSTOP);
            stream.flush();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
