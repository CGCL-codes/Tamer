package es.rediris.searchy.client;

import java.net.URL;
import de.fmui.spheon.jsoap.Envelope;
import de.fmui.spheon.jsoap.Fault;
import de.fmui.spheon.jsoap.ParamBag;
import de.fmui.spheon.jsoap.RPCCall;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.transport.http.SoapHttpClient;
import es.rediris.searchy.dc.DCVocabulary;
import es.rediris.searchy.dc.DCResource;
import es.rediris.searchy.dc.DC;

/**
 * Text based basic low level Searchy client. It may be used to help
 * admin a Searchy engine as well as example about how to develop a
 * low level Searchyclient
 *
 * @author David F. Barrero
 * @version $Id: SearchyClient.java,v 1.2 2005/03/03 09:33:11 dfbarrero Exp $
 */
public class SearchyClient {

    public static void main(String argv[]) {
        SoapConfig sc;
        DCResource query = new DCResource();
        DC ans;
        String parameter;
        query.add(DCVocabulary.CREATOR, "Unamuno");
        parameter = query.toRDF();
        try {
            sc = new SoapConfig("jsoapconfig.xml");
            sc.setActor("client");
            RPCCall call = new RPCCall(sc, "urn:mace:rediris.es:searchy", "doQuery");
            call.setParameter("mystring", parameter, String.class);
            SoapHttpClient shc = new SoapHttpClient(sc);
            Envelope answer = shc.invokeHTTP(new URL("http://localhost:33333/service"), "", call.getEnvelope());
            if (answer.hasFault()) {
                Fault f = answer.getFault();
                System.out.println("Faultcode:   " + f.getFaultcode());
                System.out.println("Faultstring: " + f.getFaultstring());
                System.out.println("Faultactor:  " + f.getFaultactor());
                return;
            }
            ParamBag p = call.getResults(answer);
            String result = (String) p.getValue(0);
            DC dc = new DC(result, DC.XML);
            System.out.println("Result: \n" + dc.toString());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }
}
