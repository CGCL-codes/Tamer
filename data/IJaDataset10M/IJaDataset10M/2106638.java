package ISO2709DataExport;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import newgenlib.marccomponent.marcmodel.Field;
import newgenlib.marccomponent.marcmodel.SubField;

/**
 *
 * @author root
 */
public class JournalInformation {

    newgenlib.marccomponent.conversion.Converter converter = new newgenlib.marccomponent.conversion.Converter();

    private void addJournalInformationToISORecord() {
        try {
            Connection con = reports.utility.database.ConnectionPoolFactory.getInstance().getConnectionPool().getConnection();
            String count = "select count(*) from searchable_cataloguerecord";
            System.out.println("COUNT " + count);
            ResultSet rsx = con.createStatement().executeQuery(count);
            int rescount = 0;
            while (rsx.next()) {
                rescount = rsx.getInt(1);
            }
            int totcount = rescount % 1000;
            int qcount = rescount / 1000;
            if (totcount != 0) {
                qcount++;
            }
            System.out.println("QCOUNT " + qcount);
            rsx.close();
            String sqlx = "BEGIN WORK; declare mtcat cursor for select cataloguerecordid,owner_library_id,wholecataloguerecord from searchable_cataloguerecord ;";
            Statement stat = con.createStatement();
            stat.execute(sqlx);
            ResultSet rs = null;
            newgenlib.marccomponent.marcmodel.CatalogMaterialDescription cmd1 = null;
            String wholecatrec = "";
            System.out.println("qCOUNT" + qcount);
            for (int i = 0; i < qcount; i++) {
                rs = con.createStatement().executeQuery("FETCH FORWARD 1000 FROM mtcat;");
                System.out.println("fetching........." + i);
                while (rs.next()) {
                    try {
                        int catrec = 0;
                        int ownerLibid = 0;
                        String wholecat = "";
                        wholecat = rs.getString("wholecataloguerecord");
                        catrec = rs.getInt("cataloguerecordid");
                        ownerLibid = rs.getInt("owner_library_id");
                        newgenlib.marccomponent.marcmodel.CatalogMaterialDescription cmd = converter.getMarcModelFromMarc(wholecat);
                        Field fld = new Field("915", '0', '1');
                        java.util.ArrayList alist = new java.util.ArrayList();
                        SubField a = new SubField('a', "");
                        fld.appendSubField(a);
                        SubField b = new SubField('b', "");
                        fld.appendSubField(b);
                        SubField c = new SubField('c', "");
                        fld.appendSubField(c);
                        cmd.addField(fld);
                        wholecatrec = converter.marcModelToMARC(cmd);
                        String xmlwholecatrec = converter.marcModelToMarcXML(cmd);
                        String insertcmd = "UPDATE searchable_cataloguerecord SET wholecataloguerecord='" + wholecatrec + "' ,xml_wholerecord='" + xmlwholecatrec + "'" + " where cataloguerecordid =" + catrec + " and owner_library_id=" + ownerLibid;
                        java.sql.PreparedStatement pst = con.prepareStatement("UPDATE searchable_cataloguerecord SET wholecataloguerecord=? ,xml_wholerecord=?  where cataloguerecordid =? and owner_library_id=?");
                        pst.setString(1, wholecatrec);
                        pst.setString(2, xmlwholecatrec);
                        pst.setInt(3, catrec);
                        pst.setInt(4, ownerLibid);
                        pst.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            con.createStatement().execute("CLOSE mtcat; COMMIT WORK;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void count() {
        int dupcnt = 0;
        try {
            Connection con = reports.utility.database.ConnectionPoolFactory.getInstance().getConnectionPool().getConnection();
            String cou = "select cataloguerecordid from searchable_cataloguerecord";
            ResultSet cnt = con.createStatement().executeQuery(cou);
            System.out.println("$$$$$$$$$$");
            while (cnt.next()) {
                int catid = cnt.getInt("cataloguerecordid");
                ResultSet rs = con.createStatement().executeQuery("select count(cataloguerecordid) from cat_volume where cataloguerecordid=" + catid);
                while (rs.next()) {
                    if (rs.getInt(1) > 1) {
                        System.out.println(" ^^^^" + catid);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        JournalInformation ji = new JournalInformation();
        ji.count();
    }
}
