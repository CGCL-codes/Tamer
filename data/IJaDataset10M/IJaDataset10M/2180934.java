package net.kodeninja.UPnP.internal.description;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPAdvertiseOperation;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;

public class ServiceDescriptionURI extends UPnPURIParser implements URIHandler {

    public ServiceDescriptionURI(UPnP host) {
        super(host);
    }

    public HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket, HTTPPacket<? extends HTTPBody> Packet) {
        if (parseURI(Packet.getHeader().getLocation().getPath()) == false) return null;
        if ((serviceId == null) || (uriAction.equals("description.xml") == false)) return null;
        UPnPAdvertiseOperation advertiser = host.getAdvertisterByRootUUID(deviceId);
        if (advertiser == null) return null;
        HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1, HTTPResponseCode.HTTP_200_OK);
        HTTPPacket<HTTPBody> packet = new HTTPPacket<HTTPBody>(header, advertiser.getServiceDescriptionBody(serviceId));
        header.setParameter("Content-Language", "en");
        return packet;
    }
}
