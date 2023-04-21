package saadadb.vo.formator;

import saadadb.collection.Category;
import saadadb.collection.SaadaInstance;
import saadadb.database.Database;

public class OtherAPToFITSFormator extends FITSFormator {

    /**@version $Id: OtherAPToFITSFormator.java 118 2012-01-06 14:33:51Z laurent.mistahl $
	 * Constructor.
	 * @throws Exception 
	 */
    public OtherAPToFITSFormator(int category) throws Exception {
        super("native " + Category.explain(category), "Saada Result service", "web:SimpleQueryResponse", "Query Result on SaadaDB " + Database.getName());
    }

    /**
	 * @param result_filename
	 * @throws Exception
	 */
    public OtherAPToFITSFormator(int category, String resource_name) throws Exception {
        super(resource_name, "native " + Category.explain(category), "Saada Result service", "web:SimpleQueryResponse", "Query Result on SaadaDB " + Database.getName());
    }

    /**
	 * @param result_filename
	 * @throws Exception
	 */
    public OtherAPToFITSFormator(int category, String resource_name, String result_filename) throws Exception {
        super(resource_name, "native " + Category.explain(category), "Saada Result service", "web:SimpleQueryResponse", "Query Result on SaadaDB " + Database.getName(), result_filename);
    }

    /**
	 * @param oid
	 * @throws Exception
	 */
    protected void writeDMData(SaadaInstance obj) throws Exception {
        writeNativeValues(obj);
    }
}
