package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import util.ConnPool;
import util.DBUtil;
import bean.Agents;

public class AgentsDao2 {

    ConnPool pool = new ConnPool();

    /**********************************************************
* 
*	Code to populate Agents Table in AgentsInternalFrame
*	sqlStatement string used to get all agents, or agents from search results
* 
**********************************************************/
    public static void PopulateAgentsTable(DefaultTableModel table, String sqlStatement) throws ClassNotFoundException, SQLException {
        try {
            ConnPool pool = new ConnPool();
            Connection c = pool.getConnection();
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStatement);
            while (rs.next()) {
                Agents newAgent = new Agents();
                newAgent.setAGENTID(Integer.parseInt(rs.getString(1)));
                newAgent.setAGTFIRSTNAME(rs.getString(2));
                newAgent.setAGTMIDDLEINITIAL(rs.getString(3));
                newAgent.setAGTLASTNAME(rs.getString(4));
                newAgent.setAGTBUSPHONE(rs.getString(5));
                newAgent.setAGTEMAIL(rs.getString(6));
                newAgent.setAGTPOSITION(rs.getString(7));
                newAgent.setAGENCYID(Integer.parseInt(rs.getString(8)));
                newAgent.setAGNCYCITY(rs.getString(10));
                newAgent.setAGTSTATUS(rs.getString(9));
                Vector<Agents> addAgent = new Vector<Agents>();
                addAgent.add(newAgent);
                table.addRow(addAgent);
            }
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**********************************************************
 * 
 *	Add button calls this code to add new agent to database
 * 
**********************************************************/
    public static void btnAddNewAgent(Agents newAgent) {
        int count = 0;
        int id = -1;
        while (count < 3) {
            id = DBUtil.getNextIdNumber("AGENTS", "AGENTID");
            if (id > 0) break;
            count++;
        }
        try {
            ConnPool pool = new ConnPool();
            Connection c = pool.getConnection();
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            stmt.executeUpdate("INSERT INTO AGENTS (AGENTID,AGTFIRSTNAME,AGTMIDDLEINITIAL," + "AGTLASTNAME,AGTBUSPHONE,AGTEMAIL,AGTPOSITION,AGENCYID,AGTSTATUS)" + "VALUES ('" + id + "','" + newAgent.getAGTFIRSTNAME() + "','" + newAgent.getAGTMIDDLEINITIAL() + "','" + newAgent.getAGTLASTNAME() + "','" + newAgent.getAGTBUSPHONE() + "','" + newAgent.getAGTEMAIL() + "','" + newAgent.getAGTPOSITION() + "','" + newAgent.getAGENCYID() + "','Active')");
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**********************************************************
 * 
 *	Save button code used to save any updated information 
 *	change on the agents form 
 * 
**********************************************************/
    public static void btnSaveAgent(Agents newAgent) {
        try {
            ConnPool pool = new ConnPool();
            Connection c = pool.getConnection();
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            stmt.executeUpdate("UPDATE AGENTS SET AGTFIRSTNAME='" + newAgent.getAGTFIRSTNAME() + "',AGTMIDDLEINITIAL='" + newAgent.getAGTMIDDLEINITIAL() + "',AGTLASTNAME='" + newAgent.getAGTLASTNAME() + "',AGTBUSPHONE='" + newAgent.getAGTBUSPHONE() + "',AGTEMAIL='" + newAgent.getAGTEMAIL() + "',AGTPOSITION='" + newAgent.getAGTPOSITION() + "',AGENCYID='" + newAgent.getAGENCYID() + "' WHERE AGENTID = " + newAgent.getAGENTID());
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**********************************************************
 * 
 *	code used to change the status of an agent from 
 *	active to in-active and vice-versa 
 * 
**********************************************************/
    public static void StatusChange(int agentID, String status) {
        try {
            ConnPool pool = new ConnPool();
            Connection c = pool.getConnection();
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            stmt.executeUpdate("UPDATE AGENTS SET AGTSTATUS='" + status + "' WHERE AGENTID = " + agentID);
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**********************************************************
 * 
 *	code used to count the number of customers related to the  
 *	selected agent - to be used when dynamically generating 
 *	comboboxes for changing a customer's agent
 * 
**********************************************************/
    public static Integer custCount(Integer agtID) {
        int i = 0;
        try {
            ConnPool pool = new ConnPool();
            Connection c = pool.getConnection();
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CUSTOMERS WHERE AGENTID=" + agtID);
            while (rs.next()) {
                i++;
            }
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**********************************************************
* 
*	code used to populate the narrow argument combobox
*	with the option related to the hardcoded item 
*	selected in the narrowField combobox
* 
**********************************************************/
    public static void narrowField(int caseCall, JComboBox cmbNarrowArgument) {
        cmbNarrowArgument.removeAllItems();
        String sqlStatement = "";
        switch(caseCall) {
            case 1:
                sqlStatement = "select distinct AGTFIRSTNAME from Agents";
                break;
            case 2:
                sqlStatement = "select distinct AGTLASTNAME from Agents";
                break;
            case 3:
                sqlStatement = "select distinct AGTPOSITION from Agents";
                break;
            case 4:
                sqlStatement = "select distinct AGNCYCITY from Agencies";
                break;
            case 5:
                sqlStatement = "select distinct AGTSTATUS from Agents";
                break;
            default:
                sqlStatement = null;
                break;
        }
        try {
            ConnPool pool = new ConnPool();
            Connection c = pool.getConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStatement);
            while (rs.next()) {
                cmbNarrowArgument.addItem(rs.getString(1));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**********************************************************
* 
*	code used to pass back the appropriate sqlstatment
*	when an item in the narrowArgument combobox is selected
* 
**********************************************************/
    public static String NarrowArgument(int caseCall) {
        String sqlStatement = "";
        switch(caseCall) {
            case 1:
                sqlStatement = "AGTFIRSTNAME ";
                break;
            case 2:
                sqlStatement = "AGTLASTNAME";
                break;
            case 3:
                sqlStatement = "AGTPOSITION";
                break;
            case 4:
                sqlStatement = "AGNCYCITY";
                break;
            case 5:
                sqlStatement = "AGTSTATUS";
                break;
            default:
                sqlStatement = null;
                break;
        }
        return (sqlStatement);
    }
}
