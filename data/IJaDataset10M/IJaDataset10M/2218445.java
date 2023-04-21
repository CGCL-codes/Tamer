package net.kodeninja.http.service.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.packet.extra.HTTPBodylessPacket;
import net.kodeninja.http.packet.extra.HTTPChunkedPacket;
import net.kodeninja.http.packet.extra.HTTPErrorResponse;
import net.kodeninja.http.packet.extra.HTTPGZipPacket;
import net.kodeninja.http.packet.extra.HTTPRangeResponsePacket;
import net.kodeninja.http.service.HTTPSocket;

public class PageRequestHandler implements PacketHandler {

    private boolean chunkedAllowed = true;

    private boolean compressionAllowed = true;

    private boolean rangeAllowed = true;

    public void enableCompression(boolean c) {
        compressionAllowed = c;
    }

    public void enableChunked(boolean c) {
        chunkedAllowed = c;
    }

    public void enableByteRange(boolean br) {
        rangeAllowed = br;
    }

    protected Set<URIHandler> handlers = Collections.synchronizedSet(new HashSet<URIHandler>());

    public boolean process(HTTPSocket Socket, HTTPPacket<HTTPBody> Packet) throws IOException {
        if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST) return false;
        boolean sendBody = true;
        if (Packet.getHeader().getMethod().equalsIgnoreCase("HEAD")) sendBody = false;
        HTTPPacket<? extends HTTPBody> response = null;
        Iterator<URIHandler> it = handlers.iterator();
        while (it.hasNext()) if ((response = it.next().process(Socket, Packet)) != null) break;
        if (response == null) response = new HTTPErrorResponse(HTTPResponseCode.HTTP_404_NOT_FOUND, Packet.getHeader().getVersion()); else {
            if ((response.getBody() != null) && (response.getHeader().getVersion().equals(HTTPVersion.HTTP1_1))) {
                boolean sendAsChunked = response.getBody().forceChunked();
                String encoding = Packet.getHeader().getParameter("Accept-Encoding");
                if ((compressionAllowed == true) && (response.getBody().forceCompression()) && (response.getBody().getContentLength() > 1024) && (response.getBody().getContentLength() < 1048576) && (encoding != null) && (encoding.toLowerCase().contains("gzip"))) {
                    response = new HTTPGZipPacket(response);
                    sendAsChunked = true;
                } else if ((response.getBody().getContentLength() < 0) || (response.getBody().getContentLength() > 524288)) sendAsChunked = true;
                if ((sendAsChunked) && (chunkedAllowed)) {
                    response = new HTTPChunkedPacket(response);
                    response.getHeader().setParameter("Transfer-Encoding", "chunked");
                } else sendAsChunked = false;
                if ((rangeAllowed) && (sendAsChunked == false)) {
                    String rangeRequest = Packet.getHeader().getParameter("range");
                    if ((rangeRequest != null) && (response.getBody() != null) && (response.getBody().getContentLength() > 0)) {
                        response = new HTTPRangeResponsePacket(response, rangeRequest);
                        response.getHeader().setResponseCode(response.getHeader().getVersion(), HTTPResponseCode.HTTP_206_PARTIAL_CONTENT);
                    }
                }
            }
        }
        if (rangeAllowed) response.getHeader().setParameter("Accept-Ranges", "bytes");
        if (sendBody == false) response = new HTTPBodylessPacket(response);
        Socket.sendPacket(response);
        String conn = response.getHeader().getParameter("connection");
        if ((conn != null) && (conn.equals("close"))) Socket.close();
        return true;
    }

    public void addURIHandler(URIHandler uriHandler) {
        handlers.add(uriHandler);
    }

    public void removeURIHandler(URIHandler uriHandler) {
        handlers.remove(uriHandler);
    }
}
