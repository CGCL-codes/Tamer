package cat.pastrywiki.test;

import java.io.IOException;
import easypastry.cast.CastHandler;
import easypastry.core.PastryConnection;
import easypastry.core.PastryKernel;
import easypastry.dht.DHTException;
import easypastry.dht.DHTHandler;
import easypastry.sample.AppCastContent;
import easypastry.sample.AppCastListener;
import cat.pastrywiki.www.Wiki;
import cat.pastrywiki.exception.WikiException;
import cat.pastrywiki.services.WikiService;

public class Main {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Wiki w = new Wiki();
        w.save("www.google.es", "pagina per buscar");
        System.out.println(w.getValue());
        System.out.println(w.getId());
        Wiki w2 = new Wiki();
        w2.save("www.meristation.es", "pagina de videojocs");
        try {
            PastryKernel.init("./etc/easypastry-config.xml");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (DHTException e1) {
            e1.printStackTrace();
        }
        PastryConnection conn = PastryKernel.getPastryConnection();
        WikiService ws = new WikiService();
        WikiService ws2 = new WikiService();
        try {
            ws.putWiki(w);
            ws2.putWiki(w2);
            Wiki wprint = ws.getWiki("www.google.es");
            System.out.println(wprint.getValue());
            Wiki wprint2 = ws.getWiki("www.meristation.es");
            System.out.println(wprint2.getValue());
        } catch (WikiException e) {
            e.printStackTrace();
        }
    }
}
