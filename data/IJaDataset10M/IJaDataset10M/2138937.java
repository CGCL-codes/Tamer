package org.fao.geonet.arcgis;

import com.esri.sde.sdk.client.SeError;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeSqlConstruct;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to retrieve ISO metadata from an ArcSDE server. The metadata in ArcSDE is scanned for
 * "MD_Metadata" and those that match are included in the result unprocessed, so including any
 * non-ISO ESRI elements they may contain.
 * 
 * @author heikki doeleman
 *
 */
public class ArcSDEMetadataAdapter extends ArcSDEConnection {

    public ArcSDEMetadataAdapter(String server, int instance, String database, String username, String password) {
        super(server, instance, database, username, password);
    }

    private static final String METADATA_TABLE = "SDE.GDB_USERMETADATA";

    private static final String METADATA_COLUMN = "SDE.GDB_USERMETADATA.XML";

    private static final String ISO_METADATA_IDENTIFIER = "MD_Metadata";

    public List<String> retrieveMetadata() throws Exception {
        System.out.println("start retrieve metadata");
        List<String> results = new ArrayList<String>();
        try {
            SeSqlConstruct sqlConstruct = new SeSqlConstruct();
            String[] tables = { METADATA_TABLE };
            sqlConstruct.setTables(tables);
            String[] propertyNames = { METADATA_COLUMN };
            SeQuery query = new SeQuery(seConnection);
            query.prepareQuery(propertyNames, sqlConstruct);
            query.execute();
            boolean allRowsFetched = false;
            while (!allRowsFetched) {
                SeRow row = query.fetch();
                if (row != null) {
                    ByteArrayInputStream bytes = row.getBlob(0);
                    byte[] buff = new byte[bytes.available()];
                    bytes.read(buff);
                    String document = new String(buff);
                    if (document.contains(ISO_METADATA_IDENTIFIER)) {
                        System.out.println("ISO metadata found");
                        results.add(document);
                    }
                } else {
                    allRowsFetched = true;
                }
            }
            query.close();
            System.out.println("cool");
            return results;
        } catch (SeException x) {
            SeError error = x.getSeError();
            String description = error.getExtError() + " " + error.getExtErrMsg() + " " + error.getErrDesc();
            System.out.println(description);
            x.printStackTrace();
            throw new Exception(x);
        }
    }
}
