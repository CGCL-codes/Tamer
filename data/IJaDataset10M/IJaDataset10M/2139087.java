package org.one.stone.soup.server.http;

import java.net.Socket;
import org.one.stone.soup.server.http.routers.HttpRouter;
import org.one.stone.soup.xml.XmlElement;

public abstract class HttpInversionClient extends HttpRouter {

    public HttpInversionClient(String name, FileServer fileServer) {
        super(name, fileServer);
    }

    public abstract boolean requestControl(Socket socket, XmlElement httpHeader);
}
