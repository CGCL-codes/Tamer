package org.riverock.portlet.test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import org.exolab.castor.xml.Marshaller;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.portlet.tools.SiteUtils;

public class TestGetDbStructure {

    public static void main(String[] s) throws Exception {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance("HSQLDB");
        DbSchemaType schema = DatabaseManager.getDbStructure(db_);
        FileOutputStream fos = new FileOutputStream(SiteUtils.getTempDir() + "hypersonic-schema.xml");
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, "utf-8"));
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding("utf-8");
        marsh.marshal(schema);
        fos.flush();
        fos.close();
        fos = null;
    }
}
