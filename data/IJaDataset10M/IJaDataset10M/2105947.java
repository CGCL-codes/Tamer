package DataManager;

import model.*;
import java.sql.*;
import java.util.*;

public class SuccessfulBidDataManager {

    private static ArrayList<Bid> successfulBidList = new ArrayList<Bid>();

    /**
     * This method clears the previous objects in successfulBidList
     */
    public static void refresh() {
        successfulBidList.clear();
        readFromDataStore();
    }

    /**
     * This method connects to the database and retrieves data that from the successfulbid table,
     * and adds bids into the successfulBidList
     */
    private static void readFromDataStore() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = (Connection) ConnectionManager.getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement("SELECT * FROM SUCCESSFULBID");
            rs = (ResultSet) pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString(1);
                String courseCode = rs.getString(2);
                String sectionCode = rs.getString(3);
                double amount = rs.getDouble(4);
                Bid bid = new Bid(userId, courseCode, sectionCode, amount, true);
                successfulBidList.add(bid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
    }

    /**
     * Method adds bid into the database successfulbid table
     * @param userId
     * @param courseCode
     * @param sectionCode
     * @param amt
     */
    private static void addToDataStore(String userId, String courseCode, String sectionCode, double amt) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = (Connection) ConnectionManager.getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement("INSERT INTO SUCCESSFULBID (USERID, COURSECODE, SECTIONCODE, AMOUNT) VALUES(?,?,?,?)");
            pstmt.setString(1, userId);
            pstmt.setString(2, courseCode);
            pstmt.setString(3, sectionCode);
            pstmt.setDouble(4, amt);
            String bider = "SuccessfulBid Datamanger was called";
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
    }

    /**
     * Method deletes successfulbid from student sucessfulbid table
     * @param studentId
     * @param courseCode
     */
    private static void deleteFromDataStore(String studentId, String courseCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = (Connection) ConnectionManager.getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement("DELETE FROM SUCCESSFULBID WHERE USERID = '" + studentId + "' AND COURSECODE = '" + courseCode + "'");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn, pstmt, rs);
        }
    }

    /**
     * Returns all the successful bids
     * @return successfulBidList the ArrayList of successful bids
     */
    public static ArrayList<Bid> retrieveAll() {
        return successfulBidList;
    }

    /**
     * Method retrieves the successful bids of a particular student
     * @param userId
     * @return successfulBidList the ArrayList of successful bids
     */
    public static ArrayList<Bid> getStudentSuccessfulBids(String userId) {
        ArrayList<Bid> userBids = new ArrayList<Bid>();
        for (Bid bid : successfulBidList) {
            if (bid.getUserId().equals(userId)) {
                userBids.add(bid);
            }
        }
        return userBids;
    }

    /**
     * Method retrieves the successful bids of a particular course and section
     * @param courseCode
     * @param sectionCode
     * @return successfulBidList the ArrayList of successful bids
     */
    public static ArrayList<Bid> getSuccessfulBidsByCourseAndSection(String courseCode, String sectionCode) {
        ArrayList<Bid> returnBid = new ArrayList<Bid>();
        for (Bid bid : successfulBidList) {
            if (bid.getCourseCode().equals(courseCode) && bid.getSectionCode().equals(sectionCode)) {
                returnBid.add(bid);
            }
        }
        return returnBid;
    }

    /**
     * Method adds a successful bid into the database
     * @param userId
     * @param courseCode
     * @param sectionCode
     * @param amt
     */
    public static void addSuccessfulBid(String userId, String courseCode, String sectionCode, double amt) {
        addToDataStore(userId, courseCode, sectionCode, amt);
        refresh();
    }

    /**
     * Method edits the successful bid
     * @param studentId
     * @param courseCode
     * @param sectionCode
     * @param amt
     */
    public static void editSuccessfulBid(String studentId, String courseCode, String sectionCode, double amt) {
        deleteFromDataStore(studentId, courseCode);
        addToDataStore(studentId, courseCode, sectionCode, amt);
        refresh();
    }

    /**
     * Method drops a success bid
     * @param student
     * @param courseCode
     * @param sectionCode
     * @param bidAmt
     */
    public static void dropSuccessfulBid(Student student, String courseCode, String sectionCode, double bidAmt) {
        deleteFromDataStore(student.getUserId(), courseCode);
        student.setEdollar(student.getEdollar() + bidAmt);
        StudentDataManager.editStudent(student, student.getEdollar());
        refresh();
        Section section = SectionDataManager.getSection(courseCode, sectionCode);
        int vacancy = SectionDataManager.getSectionVacancy(section.getCourseCode(), section.getSectionCode());
        section.setVacancy(vacancy);
        SectionDataManager.getSectionMinBid(section.getCourseCode(), section.getSectionCode());
        refresh();
    }
}
