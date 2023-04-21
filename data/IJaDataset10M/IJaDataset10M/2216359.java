package com.medcentrex.interfaces;

import com.medcentrex.util.DS;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.Calendar;
import java.sql.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class ChargeListBean {

    private String lastName;

    private String firstName;

    private String mi;

    private String chargeTicketID;

    private String createdatefrom;

    private String createdateto;

    private LoginSession loginSession;

    private String physicianID;

    private String locationID;

    private String ctStatusID;

    private int recnum;

    private int dLrecnum;

    private String ct_id;

    private String doslist_id;

    private CT_DOSListEntityData cT_DOSListData = new CT_DOSListEntityData();

    private Collection ct_doslist = (Collection) new Vector();

    private Collection col = (Collection) new Vector();

    private Collection cptCol = (Collection) new Vector();

    private ChargeTicketEntityData cTData;

    public ChargeListBean() {
        lastName = "";
        firstName = "";
        chargeTicketID = "";
        createdatefrom = "";
        createdateto = "";
        physicianID = "";
        locationID = "";
        ctStatusID = "";
        ct_id = "";
        recnum = 0;
        dLrecnum = 0;
        doslist_id = "";
    }

    public void setLoginSession(LoginSession lLogin) {
        System.out.println("SET LoginSession called");
        loginSession = lLogin;
    }

    public LoginSession getLoginSession() {
        return loginSession;
    }

    public void setCreatedatefrom(String lcreatedatefrom) {
        createdatefrom = lcreatedatefrom;
    }

    public String getCreatedatefrom() {
        if (createdatefrom == null) createdatefrom = "";
        return createdatefrom;
    }

    public void setCreatedateto(String lcreatedateto) {
        createdateto = lcreatedateto;
    }

    public String getCreatedateto() {
        if (createdateto == null) createdateto = "";
        return createdateto;
    }

    public void setChargeTicketID(String lChargeTicketID) {
        chargeTicketID = lChargeTicketID;
    }

    public String getChargeTicketID() {
        if (chargeTicketID == null) chargeTicketID = "";
        return chargeTicketID;
    }

    public void setFirstName(String lfirstname) {
        this.firstName = lfirstname;
    }

    public String getFirstName() {
        if (firstName == null) firstName = "";
        return firstName;
    }

    public void setLastName(String llastname) {
        this.lastName = llastname;
    }

    public String getLastName() {
        if (lastName == null) lastName = "";
        return lastName;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getMi() {
        if (mi == null) mi = "";
        return mi;
    }

    public void setPhysicianID(String physicianID) {
        this.physicianID = physicianID;
    }

    public String getPhysicianID() {
        if (physicianID == null) physicianID = "";
        return physicianID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationID() {
        if (locationID == null) locationID = "";
        return locationID;
    }

    public void setDoslist_id(String doslist_id) {
        this.doslist_id = doslist_id;
    }

    public String getDoslist_id() {
        if (doslist_id == null) doslist_id = "";
        return doslist_id;
    }

    public void setCTStatusID(String ctStatusID) {
        this.ctStatusID = ctStatusID;
    }

    public String getCTStatusID() {
        if (ctStatusID == null) ctStatusID = "";
        return ctStatusID;
    }

    public Collection getCol() {
        if (col == null) col = (Collection) new Vector();
        return col;
    }

    public void setCol(Collection col) {
        this.col = col;
    }

    public Collection getCPTCol() {
        if (cptCol == null) cptCol = (Collection) new Vector();
        return cptCol;
    }

    public void setCPTCol(Collection cptCol) {
        this.cptCol = cptCol;
    }

    public Collection getCt_doslist() {
        if (ct_doslist == null) ct_doslist = new Vector();
        return ct_doslist;
    }

    public void setCt_doslist(Collection ct_doslist) {
        this.ct_doslist = ct_doslist;
    }

    public String getCt_id() {
        if (ct_id == null) ct_id = "";
        return ct_id;
    }

    public void setCt_id(String ct_id) {
        this.ct_id = ct_id;
    }

    public void setRecnum(int recnum) {
        this.recnum = recnum;
    }

    public int getRecnum() {
        return recnum;
    }

    public void setDLRecnum(int dLrecnum) {
        this.dLrecnum = dLrecnum;
    }

    public int getDLRecnum() {
        return dLrecnum;
    }

    public void setCT_DOSListData(CT_DOSListEntityData cT_DOSListData) {
        this.cT_DOSListData = cT_DOSListData;
    }

    public CT_DOSListEntityData getCT_DOSListData() {
        return cT_DOSListData;
    }

    public com.medcentrex.interfaces.ChargeTicketEntityData getCTData() {
        if (this.cTData == null) this.cTData = new ChargeTicketEntityData();
        return this.cTData;
    }

    public void setCTData(com.medcentrex.interfaces.ChargeTicketEntityData cTData) {
        this.cTData = cTData;
    }

    public void clearSearch() {
        lastName = "";
        firstName = "";
        mi = "";
        chargeTicketID = "";
        createdatefrom = "";
        createdateto = "";
        physicianID = "";
        locationID = "";
        ctStatusID = "";
        ct_id = "";
        recnum = 0;
        dLrecnum = 0;
    }

    public java.util.Collection findChargeList(String patient_id, String firstName, String chargeTicketID, String createdatefrom, String createdateto, String lastName, String physician_id, String place_id, String status_id) throws ServiceUnavailableException, RemoteException {
        Connection aConnection = null;
        Statement aStatement = null;
        try {
            InitialContext lContext = new InitialContext();
            aConnection = DS.getConnection(this.getClass());
            aStatement = aConnection.createStatement();
            String aSql = "select     chargeticket.* from chargeticket inner join people on chargeticket.patient_id = people.person_id " + "and customer_id=" + loginSession.getCustomer_ID();
            if (!patient_id.equals("")) aSql += " AND Patient_ID='" + patient_id + "'";
            if (!firstName.equals("")) aSql += " AND people.firstname LIKE '" + firstName + "%'";
            if (!lastName.equals("")) aSql += " AND people.lastname LIKE '" + lastName + "%'";
            if (!chargeTicketID.equals("")) aSql += " AND ChargeTicket_ID= '" + chargeTicketID + "'";
            if (!createdatefrom.equals("")) aSql += " AND CTCreateDate >= '" + createdatefrom + "'";
            if (!createdateto.equals("")) aSql += " AND CTCreateDate <= '" + createdateto + "'";
            if (!physician_id.equals("")) aSql += " AND Physician_ID='" + physician_id + "'";
            if (!place_id.equals("")) aSql += " AND Location_ID='" + place_id + "'";
            if (!status_id.equals("")) aSql += " AND ChargeTicket_Status_ID='" + status_id + "'";
            java.util.Vector chargeList = new java.util.Vector();
            System.out.println(aSql);
            ResultSet aResult = aStatement.executeQuery(aSql);
            while (aResult.next()) {
                ChargeTicketEntityData lData = new ChargeTicketEntityData();
                lData.setChargeTicket_ID(new Integer(aResult.getInt("ChargeTicket_ID")));
                lData.setCustomer_ID(new Integer(aResult.getInt("Customer_ID")));
                lData.setPrimary_ID(new Integer(aResult.getInt("Primary_ID")));
                lData.setSecondary_ID(new Integer(aResult.getInt("Secondary_ID")));
                lData.setTertiary_ID(new Integer(aResult.getInt("Tertiary_ID")));
                lData.setReferring_Physician_ID(aResult.getString("Referring_Physician_ID"));
                lData.setChargeTicket_Status_ID(new Integer(aResult.getInt("ChargeTicket_Status_ID")));
                lData.setPatient_id(new Integer(aResult.getInt("Patient_ID")));
                lData.setLocation_ID(new Integer(aResult.getInt("Location_ID")));
                lData.setPhysician_id(new Integer(aResult.getInt("Physician_ID")));
                lData.setInsuranceCoName(aResult.getString("InsuranceCoName"));
                lData.setReferringName(aResult.getString("ReferringName"));
                lData.setCTCreateDate(aResult.getDate("CTCreateDate"));
                lData.setCTCreateTime(aResult.getTime("CTCreateTime"));
                lData.setServiceDate(aResult.getDate("ServiceDate"));
                chargeList.add(lData);
            }
            return chargeList;
        } catch (NamingException ne) {
            throw new ServiceUnavailableException("JNDI Lookup broken");
        } catch (SQLException se) {
            se.printStackTrace();
            throw new ServiceUnavailableException("findChargeList is broken");
        } finally {
            try {
                if (aStatement != null) {
                    aStatement.close();
                }
            } catch (Exception e) {
            }
            try {
                if (aConnection != null) {
                    DS.closeConnection(aConnection, this.getClass());
                }
            } catch (Exception e) {
            }
        }
    }

    public CT_DOSListEntityData getDOSListEntityData(String doslist_ID) {
        Iterator iterator = ct_doslist.iterator();
        CT_DOSListEntityData temp = new CT_DOSListEntityData();
        while (iterator.hasNext()) {
            temp = (CT_DOSListEntityData) iterator.next();
            if (temp.getCT_DOSList_ID().toString().equals(doslist_ID)) {
                break;
            }
        }
        System.out.println("STEEN Dropping charage=" + temp.getCT_DOSList_ID());
        return temp;
    }
}
