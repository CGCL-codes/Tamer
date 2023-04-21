package gov.lanl.xmltape.identifier.index.test;

import gov.lanl.identifier.Identifier;
import gov.lanl.identifier.IndexException;
import gov.lanl.xmltape.identifier.index.jdbImpl.IdentifierIndex;

public class IdentifierIndexReaderTest {

    public static void main(String[] args) {
        try {
            IdentifierIndex idx = new IdentifierIndex();
            idx.open(true);
            long s = System.currentTimeMillis();
            Identifier id = idx.getIdentifier("info:doi/10.1007/s10610-004-5886-2");
            long d = System.currentTimeMillis();
            System.out.println(id.getRecordId() + " took " + (d - s) + " ms - " + id.getDatestamp());
            s = System.currentTimeMillis();
            id = idx.getIdentifier("info:doi/10.1007/s10610-004-5886-2");
            d = System.currentTimeMillis();
            System.out.println(id.getRecordId() + " took " + (d - s) + " ms - " + id.getDatestamp());
            s = System.currentTimeMillis();
            id = idx.getIdentifier("info:doi/10.1007/s10610-004-5886-2");
            d = System.currentTimeMillis();
            System.out.println(id.getRecordId() + " took " + (d - s) + " ms - " + id.getDatestamp());
            idx.close();
        } catch (IndexException e) {
            e.printStackTrace();
        }
    }
}
