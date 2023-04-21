package javamath.server.proxycas;

import java.io.*;
import java.net.*;
import java.util.*;
import javamath.util.ServletWriter;

/**
* Servlet based proxy for the RMI CAServer. This can be created on any
* server from  a java standalone or on the webserver from an applet.
*/
public class ProxyCAServer {

    String webBase;

    int i;

    String magicCookie;

    /**
	* proxyCASURL is a string like
	* http://javamathserver.domain.net/ProxyCAS
	* which is the URL to the directory javamath/src/server/proxystore
	* on the server.
	*/
    public ProxyCAServer(String proxyCASURL) {
        webBase = proxyCASURL;
        try {
            URL servlet = new URL(webBase + "/servlet/CACookie");
            Serializable objs[] = {};
            ObjectInputStream in = ServletWriter.postObjects(servlet, objs);
            magicCookie = (String) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        i = 0;
    }

    /** 
	* Returns a computation session with the compute engine named
	* <i>cas</i>. The valid values of <i>cas</i> are determined
	* by which engines have been installed into the server.
	* 
	* The current implementation comes with interfaces to "GAP" and "Maple".
	*/
    public ProxyCASession createSession(String cas) {
        String sessionId = magicCookie + i++;
        Serializable objs[] = { sessionId, cas };
        ProxyCASession result = null;
        try {
            URL servlet = new URL(webBase + "/servlet/CAServerServlet");
            ObjectInputStream in = ServletWriter.postObjects(servlet, objs);
            result = new ProxyCASession(webBase, sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
