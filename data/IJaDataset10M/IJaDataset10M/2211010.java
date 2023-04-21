package org.elephantt.webby;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class JaxbResponder implements Responder {

    private final Marshaller marshaller;

    public JaxbResponder(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void respond(HttpServletRequest request, HttpServletResponse response, Object controllerReturnValue) throws IOException {
        JAXBElement element = (JAXBElement) controllerReturnValue;
        response.setContentType("application/xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            marshaller.marshal(element, baos);
            response.getOutputStream().write(baos.toByteArray());
        } catch (JAXBException e) {
            throw new WebbyException("could not marshal to XML", e);
        }
    }
}
