package org.espers.beatrice.server.sei;

import javax.jws.*;
import javax.jws.soap.*;

@WebService(name = "BeatriceServer", targetNamespace = "http://beatrice.espers.com/", serviceName = "MapBlockService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class ServiceServer {

    @WebMethod(operationName = "getMapBlock", action = "urn:getMapBlock")
    @WebResult(partName = "result")
    public MapBlock getMapBlock(@WebParam(partName = "blockname", targetNamespace = "http://beatrice.espers.com") String name) {
        MapBlock mb = new MapBlock();
        mb.setX(1);
        mb.setY(2);
        mb.setWidth(1);
        mb.setHeight(2);
        mb.setZoomLevel(8);
        mb.setData(new byte[] { 0, 0, 0 });
        System.out.println("Hit !");
        return mb;
    }
}
