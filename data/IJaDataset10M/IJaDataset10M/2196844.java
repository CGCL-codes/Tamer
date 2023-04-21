package co.edu.unal.ungrid.services.server.db.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import co.edu.unal.ungrid.services.client.applet.document.DocumentType;
import co.edu.unal.ungrid.services.server.db.base.Record;
import co.edu.unal.ungrid.services.server.db.instance.DatabaseLoader;
import co.edu.unal.ungrid.services.server.db.select.AbstractQuery;

/**
 * @author Administrator
 *
 */
public class DocTypeDb {

    private DocTypeDb() {
    }

    public static class DocType {

        public DocType(long nType, final String sDescription, final String sFolder) {
            super();
            this.nType = nType;
            this.sDescription = sDescription;
            this.sFolder = sFolder;
        }

        public long getsType() {
            return nType;
        }

        public void setsType(long nType) {
            this.nType = nType;
        }

        public String getDescription() {
            return sDescription;
        }

        public void setDescription(String sDescription) {
            this.sDescription = sDescription;
        }

        public String getFolder() {
            return sFolder;
        }

        public void setFolder(String sFolder) {
            this.sFolder = sFolder;
        }

        public static DocType fromRecord(final Record r) {
            return new DocType(r.getLong(TYPE), r.getString(DESCRIPTION), r.getString(FOLDER));
        }

        private long nType;

        private String sDescription;

        private String sFolder;
    }

    private static class DocTypeSelect extends AbstractQuery {

        public DocTypeSelect() {
            setQuery(SELECT_ALL_FROM + TABLE);
        }

        public DocTypeSelect(long nSvcIdx) {
            setQuery(SELECT_ALL_FROM + TABLE + WHERE + TYPE + EQUALS + nSvcIdx);
        }
    }

    public static ArrayList<Record> getAllTypes() {
        ArrayList<Record> ra = new ArrayList<Record>();
        try {
            ResultSet rs = SqlUtilities.stdQuery(new DocTypeSelect());
            if (rs != null) {
                while (rs.next()) {
                    ra.add(new Record(rs));
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ra;
    }

    public static DocType getType(long nSvcIdx) {
        DocType dt = null;
        Record r = SqlUtilities.stdGetRecord(new DocTypeSelect(nSvcIdx));
        if (r != null) {
            dt = DocType.fromRecord(r);
        }
        return dt;
    }

    public static void testAllTypes(String[] args) {
        ArrayList<Record> ra = getAllTypes();
        for (Record r : ra) {
            System.out.println(r);
        }
        System.out.println("SUBTRACTION=" + DocumentType.SUBTRACTION.ordinal());
        System.out.println("SEGMENTATION=" + DocumentType.SEGMENTATION.ordinal());
        System.out.println("ATLAS=" + DocumentType.ATLAS.ordinal());
        System.out.println("PFOLDING=" + DocumentType.PFOLDING.ordinal());
    }

    public static void main(String[] args) {
        DatabaseLoader.getInstance(DatabaseLoader.STANDARD_MODE);
        testAllTypes(args);
    }

    public static final String TABLE = "doc_type";

    public static final String TYPE = "type";

    public static final String DESCRIPTION = "description";

    public static final String FOLDER = "folder";

    public static final int SUBTRACTION = DocumentType.SUBTRACTION.ordinal();

    public static final int SEGMENTATION = DocumentType.SEGMENTATION.ordinal();

    public static final int ATLAS = DocumentType.ATLAS.ordinal();

    public static final int PFOLDING = DocumentType.PFOLDING.ordinal();

    public static final int BIMLER = DocumentType.BIMLER.ordinal();
}
