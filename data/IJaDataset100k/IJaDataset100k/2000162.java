package de.l3s.sr.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class RestletUtils {

    /**
     * Returns the resource as String
     *     
     * @param client
     *                client Restlet.
     * @param reference
     *                the resource's URI.
     * @return String
     * 				  the Representation as String
     * @throws IOException
     */
    public static String getAsString(Client client, Reference reference) throws IOException {
        String result = "";
        Request r = new Request(Method.GET, reference);
        Preference<MediaType> mt = new Preference<MediaType>();
        mt.setMetadata(MediaType.APPLICATION_RDF_XML);
        List<Preference<MediaType>> lmt = new LinkedList<Preference<MediaType>>();
        lmt.add(mt);
        r.getClientInfo().setAcceptedMediaTypes(lmt);
        Response response = client.handle(r);
        if (response.getStatus().isSuccess()) {
            if (response.isEntityAvailable()) {
                result = response.getEntity().getText();
            }
        }
        return result;
    }

    public static InputStream getAsInputStream(Client client, Reference reference) {
        String result = "";
        try {
            result = getAsString(client, reference);
        } catch (IOException e) {
            System.out.println("getAsInputStream: Could not get Response as String");
            System.out.println(e.getStackTrace());
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(result.getBytes());
        return bais;
    }
}
