package se.sics.tasim.is.common;

import java.io.IOException;
import java.util.logging.Logger;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpFields;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.util.ByteArrayISO8859Writer;

public class ViewerPage extends HttpPage {

    private static final Logger log = Logger.getLogger(ViewerPage.class.getName());

    private String viewerClass = "se.sics.tasim.viewer.applet.ViewerApplet";

    private String viewerCodeBase = "/code/";

    private String viewerArchive = "simviewer.jar";

    private int width = 800;

    private int height = 750;

    private int infoPort = 4042;

    private String topPage;

    private String middlePage;

    private String bottomPage;

    public ViewerPage(InfoServer infoServer, SimServer simServer) {
        String serverName = simServer.getServerName();
        topPage = "<html>\r\n<head>\r\n<title>Simulation viewer for " + serverName + "</title>\r\n</head>\r\n<body bgcolor=black>\r\n" + "<object\r\n" + "   classid='clsid:8AD9C840-044E-11D1-B3E9-00805F499D93'\r\n" + "   codebase='http://java.sun.com/products/plugin/autodl/jinstall-1_4_1-windows-i586.cab#Version=1,4,1,0'\r\n" + "   width='" + width + "' height='" + height + "'>\r\n" + "  <param name=code value='" + viewerClass + "'>\r\n" + "  <param name=codebase value='" + viewerCodeBase + "'>\r\n" + "  <param name=archive value='" + viewerArchive + "'>\r\n" + "  <param name=type value='application/x-java-applet;version=1.4.1'>\r\n" + "  <param name='scriptable' value='false'>\r\n" + "  <param name='serverName' value='" + serverName + "'>\r\n";
        middlePage = "\r\n" + "  <COMMENT>\r\n" + "    <embed\r\n" + "       type='application/x-java-applet;version=1.4.1'\r\n" + "       code='" + viewerClass + "'\r\n" + "       codebase='" + viewerCodeBase + "'\r\n" + "       archive='" + viewerArchive + "'\r\n" + "       width='" + width + "'\r\n" + "       height='" + height + "'\r\n" + "       scriptable=false\r\n" + "       serverName='" + serverName + "'\r\n";
        bottomPage = "	pluginspage='http://java.sun.com/products/plugin/index.html#download'\r\n" + "       alt='Your browser understands the &lt;EMBED&gt; tag but isn't running the Java Applet, for some reason.'>\r\n" + "	<noembed>\r\n" + "      Your browser is completely ignoring the Java Applet!\r\n" + "       </noembed>\r\n" + "    </embed>\r\n" + "  </COMMENT>\r\n" + "</object>\r\n" + "<br>\r\n" + "<font size=-1 color=white><em>Note that you can see more information by clicking on the entities during a game.</em></font>" + "</body>\r\n" + "</html>\r\n";
    }

    public void handle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws HttpException, IOException {
        String userName = request.getAuthUser();
        String page = topPage + "<param name=user value='" + userName + "'>\r\n" + "<param name=port value='" + infoPort + "'>\r\n" + middlePage + "user='" + userName + "'\r\n" + "port='" + infoPort + "'\r\n" + bottomPage;
        ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer();
        writer.write(page);
        response.setContentType(HttpFields.__TextHtml);
        response.setContentLength(writer.size());
        writer.writeTo(response.getOutputStream());
        response.commit();
    }
}
