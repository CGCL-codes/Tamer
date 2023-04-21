package sourceleads;

import in.co.daffodil.db.rmi.*;
import java.sql.*;

public class Update extends Thread {

    boolean startThread;

    RmiDaffodilDBDataSource dsSite;

    Connection localCon;

    int lastRecordId;

    public Update(Connection con) {
        lastRecordId = 0;
        localCon = con;
        startThread = true;
        dsSite = new RmiDaffodilDBDataSource();
        dsSite.setCreateDatabase("false");
        dsSite.setHostName("207.234.209.31");
        dsSite.setPortNumber("3457");
        dsSite.setDatabaseName("clientdatabase");
        dsSite.setUser("daffodil");
        dsSite.setPassword("javardbms");
    }

    public void stopThread() {
        startThread = false;
    }

    public void run() {
        try {
            Connection connew = dsSite.getConnection();
            while (startThread) {
                try {
                    Statement st = localCon.createStatement();
                    ResultSet rstemp = st.executeQuery("select max(id) from id");
                    if (rstemp.next()) {
                        lastRecordId = rstemp.getInt(1);
                    }
                    System.out.println("id from local db <>" + lastRecordId);
                    rstemp.close();
                    PreparedStatement psSite = connew.prepareStatement(" select r.email,d.product,r.name, r.phone,d.DateOfDownload,r.wantnews,d.id from Registrants r,(select regno,product,DateOfDownload,id from Downloads where id > " + lastRecordId + ") d where r.regno=d.Regno");
                    PreparedStatement psLeadDetails = localCon.prepareStatement("insert into LeadDetails(Email,Category,name,Telephone,opendate,wantnews,salesStage,PrimaryOwner) values (?,?,?,?,?,?,?,?)");
                    java.sql.Statement stLead = localCon.createStatement();
                    ResultSet rsLeads = null;
                    ResultSet rsnew = psSite.executeQuery();
                    System.out.println("inserting values");
                    while (rsnew.next()) {
                        String tempemail = rsnew.getString(1);
                        String tempProd = rsnew.getString(2);
                        String tempname = rsnew.getString(3);
                        String tempPhone = rsnew.getString(4);
                        java.sql.Date tempDOD = rsnew.getDate(5);
                        String tempWantNews = rsnew.getString(6);
                        int finalId = rsnew.getInt(7);
                        System.out.println("email <> " + tempemail + " : Prod <>" + tempProd + " : Name <>" + tempname + "tempPhone <> " + tempPhone + " : tempDOD <>" + tempDOD + " : tempWantNews <>" + tempWantNews + "finalId" + finalId);
                        rsLeads = stLead.executeQuery("select * from LeadDetails where Email='" + tempemail + "'" + "and Category='" + tempProd + "'" + " and name='" + tempname + "'");
                        if (rsLeads.next()) {
                            continue;
                        }
                        psLeadDetails.setString(1, tempemail);
                        psLeadDetails.setString(2, tempProd);
                        psLeadDetails.setString(3, tempname);
                        psLeadDetails.setString(4, tempPhone);
                        psLeadDetails.setDate(5, tempDOD);
                        psLeadDetails.setString(6, tempWantNews);
                        psLeadDetails.setString(7, "download");
                        psLeadDetails.setInt(8, 1);
                        System.out.println("finalId before insertion=" + finalId);
                        st.execute("insert into id values(" + finalId + ")");
                    }
                    rsLeads.close();
                    stLead.close();
                    System.out.println(lastRecordId);
                } catch (SQLException ex) {
                    System.out.println("no new entries");
                    ex.printStackTrace();
                } finally {
                    try {
                        System.out.println("records updated");
                        Thread.sleep(2 * 60 * 60 * 1000);
                    } catch (InterruptedException ex1) {
                        ex1.printStackTrace();
                    }
                }
            }
            localCon.close();
            connew.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
