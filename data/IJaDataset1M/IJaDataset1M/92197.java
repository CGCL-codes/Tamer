package examples.omnidas;

import edu.mit.wi.omnigene.omnidas.*;
import java.io.*;
import java.net.*;

/**
 * This example will retrieve all the types for a specific entry point
 * of a data source name from the WormBase system.
 */
public class TIGRTypesEx {

    private static final String TIGR_URL = "http://www.tigr.org/docs/tigr-scripts/nhgi_scripts/das";

    private static DASQueryFactory factory = null;

    public static void main(String[] args) {
        try {
            DASTypesRequest dasRequest = new DASTypesRequestImpl();
            dasRequest.setDASVersion(2.0f);
            dasRequest.setDASSource(new URL(TIGR_URL));
            DSN dsn = new DSNImpl("arabidopsis");
            dasRequest.setDSN(dsn);
            Segment segment = new SegmentImpl("CHROMOSOME_1", new RangeImpl(1L, 200000L));
            Segment[] segments = { segment };
            dasRequest.setSegments(segments);
            factory = DASQueryFactory.getInstance();
            DASQuery query = factory.getDASQuery(dasRequest);
            query.doDASQuery();
            DASResponse dasResponse = query.getDASResponse();
            InputStream is = dasResponse.getResponse();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String record = null;
            while ((record = br.readLine()) != null) {
                System.out.println(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
