package pt.utl.ist.lucene.treceval.geotime.utiltasks;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermEnum;
import pt.utl.ist.lucene.treceval.geotime.index.Config;
import pt.utl.ist.lucene.treceval.geotime.index.IndexWoeid;
import pt.utl.ist.lucene.treceval.geotime.webservices.CallWebServices;
import pt.utl.ist.lucene.utils.placemaker.BelongTosDocument;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Jorge Machado
 * @date 15/Jan/2010
 * @time 19:27:33
 * @email machadofisher@gmail.com
 */
public class BelongTosPlaceNamesTagger {

    public static String output = Config.ntcirBase + File.separator + "belongtos";

    public static String woeidBelongTosPlacesNames = output + File.separator + "woeidBelongTosPlacesNames.txt";

    public static void main(String[] args) throws IOException {
        int maxbelongtoos = 0;
        new File(output).mkdirs();
        FileWriter writer = new FileWriter(output + File.separator + "woeidBelongTosPlacesNames.txt");
        IndexReader reader = IndexReader.open(IndexWoeid.indexPath);
        TermEnum termEnum = reader.terms();
        int i = 0;
        while (termEnum.next()) {
            if (termEnum.term().field().equals(Config.G_GEO_ALL_WOEID) && termEnum.term().text().compareTo("WOEID-1") > 0) {
                String woeid = termEnum.term().text().substring("WOEID-".length());
                try {
                    writer.write(woeid + ":");
                    BelongTosDocument doc = CallWebServices.belongTos(woeid);
                    if (doc.getBelongTos() == null || doc.getBelongTos().size() == 0) {
                        System.out.println("woeid: " + woeid + " don't have belongtos");
                    } else {
                        if (maxbelongtoos < doc.getBelongTos().size()) maxbelongtoos = doc.getBelongTos().size();
                        for (BelongTosDocument.Place p : doc.getBelongTos()) {
                            writer.write(p.getName() + ";");
                        }
                    }
                } catch (Exception e) {
                    writer.write(woeid + ":");
                    System.out.println(e.toString());
                    e.printStackTrace();
                } finally {
                    writer.write("\n");
                }
                i++;
                if (i % 100 == 0) {
                    writer.flush();
                    System.out.println("Done " + i + " max belongtos:" + maxbelongtoos);
                }
            }
        }
        reader.close();
        writer.flush();
        writer.close();
        System.out.println("max belongtoos = " + maxbelongtoos);
    }
}
