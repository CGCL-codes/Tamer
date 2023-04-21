package org.xfap.http;

import org.kxml.*;
import org.kxml.io.*;
import org.kxml.kdom.*;
import java.awt.image.*;
import java.net.*;
import java.text.*;
import java.io.*;
import java.util.*;
import org.xfap.*;
import org.xfap.ams.*;
import org.xfap.util.*;

class DefaultHandler {

    HttpDispatcher dispatcher;

    AgentHttpRequest request;

    AgentHttpResponse response;

    DefaultHandler(HttpDispatcher dispatcher, AgentHttpRequest request, AgentHttpResponse response) {
        this.dispatcher = dispatcher;
        this.request = request;
        this.response = response;
    }

    public void service() throws IOException {
        if (request.agentName.equals("files")) handleFileRequest(); else if (request.agentName.equals("unicode")) handleUnicodeRequest(); else if (request.agentName.equals("robots.txt")) {
            request.pathInfo = "robots.txt";
            handleFileRequest();
        } else forward();
    }

    public void forward() throws IOException {
        StringBuffer content = new StringBuffer("<http-request>");
        content.append("<method>" + Xml.encode(request.getMethod(), Xml.ENCODE_MIN) + "</method>");
        content.append("<url>" + Xml.encode(request.getRequestURI()) + "</url>");
        content.append("<servletPath>" + Xml.encode(request.getServletPath()) + "</servletPath>");
        content.append("<agentPath>" + Xml.encode(request.getAgentPath()) + "</agentPath>");
        content.append("<pathInfo>" + Xml.encode(request.getPathInfo()) + "</pathInfo>");
        if (request.getQueryString() != null) content.append("<query>" + Xml.encode(request.getQueryString(), Xml.ENCODE_MIN) + "</query>");
        if (request.header.size() > 0) {
            content.append("<properties ");
            for (Iterator i = request.header.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                content.append(key + "=\"" + Xml.encode(request.getHeader(key), Xml.ENCODE_QUOT) + "\" ");
            }
            content.append("/>");
        }
        if (request.getMethod().equalsIgnoreCase("POST")) {
            String len = request.getHeader("content-length");
            if (len == null) throw new RuntimeException("content-length required for POST!");
            byte[] data = new byte[Integer.parseInt(len)];
            DataInputStream dis = new DataInputStream(request.getInputStream());
            dis.readFully(data);
            String requestContent = new String(data);
            if (requestContent.trim().length() != 0) content.append("<content>" + Xml.encode(requestContent, Xml.ENCODE_MIN) + "</content>");
        }
        content.append("</http-request>");
        Message message = new Message();
        message.setSender(dispatcher.getAgentIdentifier());
        message.setConversationId(request.getSessionId());
        message.addReceiver(new AgentIdentifier(dispatcher.getPlatform().getAbsoluteName(request.getAgentName()), new Vector()));
        message.setPerformative("request");
        message.setContent(content.toString());
        reply(dispatcher.getPlatform().query(message));
    }

    void reply(Message reply) throws IOException {
        XmlWriter xw = new XmlWriter(response.getWriter());
        try {
            Element content = reply.getContentElement();
            boolean indirect = content.getName().equals("http-response");
            if (indirect) {
                int pi = content.indexOf("properties", 0);
                if (pi != -1) {
                    Element properties = content.getElement(pi);
                    for (int i = 0; i < properties.getAttributeCount(); i++) {
                        Attribute attr = properties.getAttribute(i);
                        response.setHeader(attr.getName(), attr.getValue());
                    }
                }
            }
            if (indirect) content.getElement(content.indexOf("content", 0)).writeChildren(xw); else content.write(xw);
        } catch (Exception e) {
            Log.error("Error in handling http request " + request.getRequestURI(), e);
            xw.writeRaw("<html><head><title>Error</title></head><body>");
            xw.writeRaw("<h1>Error</h1>");
            xw.write("Exception: " + e.toString());
            xw.write("Uri: " + request.getRequestURI());
            xw.writeRaw("<h3>Message Content</h3>");
            String cnt = reply.getContent().toString();
            int i0 = 0;
            while (true) {
                int i = cnt.indexOf('\n', i0);
                if (i == -1) {
                    xw.write(cnt.substring(i0));
                    break;
                }
                xw.write(cnt.substring(i0, i));
                i0 = i + 1;
                xw.writeRaw("<br />");
            }
        }
        xw.flush();
    }

    void handleUnicodeRequest() throws IOException {
        String path = request.getPathInfo();
        String name = path.substring(path.startsWith("/") ? 1 : 0, path.length() - 4);
        int number = (name.startsWith("u_")) ? Integer.parseInt(name.substring(2), 16) : org.xfap.util.Unicode.getUnicodeNumber(name);
        BufferedImage image = org.xfap.util.Unicode.getUnicodeGlyph(number, 0.5);
        org.xfap.util.Imaging.writeImage(image, response.getOutputStream());
    }

    void handleFileRequest() throws IOException {
        String path = request.getPathInfo();
        File file = new File(dispatcher.fileBase, path);
        Log.debug("transferFile full path: " + file);
        if (!file.exists() || file.isDirectory()) throw new RuntimeException("requested file does not exist or is a directory");
        OutputStream out = response.getOutputStream();
        int dot = path.lastIndexOf('.');
        String ext = dot == -1 ? "" : path.substring(dot).toLowerCase();
        String type;
        if (ext.startsWith(".htm")) type = "text/html"; else if (ext.equals(".xml") || ext.equals(".txt")) type = "text/" + ext; else if (ext.equals(".gif") || ext.equals(".jpg") || ext.equals(".png")) type = "image/" + ext; else if (ext.equals(".jar")) type = "application/java-archive"; else type = "binary/" + ext;
        InputStream in = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) break;
            out.write(buffer, 0, read);
        }
        out.flush();
    }
}
