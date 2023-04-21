package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import edu.ncsu.csc.itrust.beans.MessageBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.MessageDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ViewMyMessagesActionTest extends TestCase {

    private ViewMyMessagesAction action;

    private ViewMyMessagesAction action2;

    private DAOFactory factory;

    private long mId = 2L;

    private long hcpId = 9000000000L;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.standardData();
        this.factory = TestDAOFactory.getTestInstance();
        this.action = new ViewMyMessagesAction(this.factory, this.mId);
        this.action2 = new ViewMyMessagesAction(this.factory, this.hcpId);
    }

    public void testGetAllMyMessages() throws SQLException {
        List<MessageBean> mbList = action.getAllMyMessages();
        assertEquals(1, mbList.size());
    }

    public void testGetPatientName() throws iTrustException {
        assertEquals("Andy Programmer", action.getName(this.mId));
    }

    public void testGetPersonnelName() throws iTrustException {
        assertEquals("Kelly Doctor", action.getPersonnelName(this.hcpId));
    }

    public void testGetAllMyMessagesTimeAscending() throws SQLException {
        List<MessageBean> mbList = action2.getAllMyMessagesTimeAscending();
        assertEquals(14, mbList.size());
        assertTrue(mbList.get(0).getSentDate().before(mbList.get(1).getSentDate()));
    }

    public void testGetAllMyMessagesNameAscending() throws SQLException {
        List<MessageBean> mbList = action2.getAllMyMessagesNameAscending();
        List<MessageBean> mbList2 = action.getAllMyMessagesNameAscending();
        assertEquals(14, mbList.size());
        assertEquals(1, mbList2.size());
        try {
            assertTrue(action2.getName(mbList.get(0).getFrom()).compareTo(action2.getName(mbList.get(13).getFrom())) >= 0);
        } catch (iTrustException e) {
            e.printStackTrace();
        }
    }

    public void testGetAllMyMessagesNameDescending() throws SQLException {
        List<MessageBean> mbList = action2.getAllMyMessagesNameDescending();
        List<MessageBean> mbList2 = action.getAllMyMessagesNameDescending();
        assertEquals(14, mbList.size());
        assertEquals(1, mbList2.size());
        try {
            assertTrue(action2.getName(mbList.get(13).getFrom()).compareTo(action2.getName(mbList.get(0).getFrom())) >= 0);
        } catch (iTrustException e) {
            e.printStackTrace();
        }
    }

    public void testGetAllMySentMessages() throws SQLException {
        List<MessageBean> mbList = action2.getAllMySentMessages();
        assertEquals(2, mbList.size());
    }

    public void testGetAllMyMessagesFromTimeAscending() throws SQLException {
        List<MessageBean> mbList = action2.getAllMySentMessagesTimeAscending();
        assertEquals(2, mbList.size());
        assertTrue(mbList.get(0).getSentDate().before(mbList.get(1).getSentDate()));
    }

    public void testGetAllMyMessagesFromNameAscending() throws SQLException {
        List<MessageBean> mbList = action2.getAllMySentMessagesNameAscending();
        List<MessageBean> mbList2 = action.getAllMySentMessagesNameAscending();
        assertEquals(2, mbList.size());
        assertEquals(3, mbList2.size());
        try {
            assertTrue(action2.getName(mbList.get(0).getFrom()).compareTo(action2.getName(mbList.get(1).getFrom())) >= 0);
        } catch (iTrustException e) {
            e.printStackTrace();
        }
    }

    public void testGetAllMyMessagesFromNameDescending() throws SQLException {
        List<MessageBean> mbList = action2.getAllMySentMessagesNameDescending();
        List<MessageBean> mbList2 = action.getAllMySentMessagesNameDescending();
        assertEquals(2, mbList.size());
        assertEquals(3, mbList2.size());
        try {
            assertTrue(action2.getName(mbList.get(1).getFrom()).compareTo(action2.getName(mbList.get(0).getFrom())) >= 0);
        } catch (iTrustException e) {
            e.printStackTrace();
        }
    }

    public void testUpdateRead() throws iTrustException, SQLException, FormValidationException {
        List<MessageBean> mbList = action.getAllMyMessages();
        assertEquals(0, mbList.get(0).getRead());
        action.setRead(mbList.get(0));
        mbList = action.getAllMyMessages();
        assertEquals(1, mbList.get(0).getRead());
    }

    public void testAddMessage() throws SQLException {
        MessageDAO test = new MessageDAO(factory);
        List<MessageBean> mbList = action.getAllMyMessages();
        test.addMessage(mbList.get(0));
        mbList = action.getAllMyMessages();
        assertEquals(2, mbList.size());
    }

    public void testValidateAndCreateFilter() {
        String test = action.validateAndCreateFilter("Random Person,Appointment,Appointment,Lab,01/01/2010,01/31/2010");
        assertEquals(test, "Random Person,Appointment,Appointment,Lab,01/01/2010,01/31/2010");
    }

    public void testFilterMessages() throws SQLException, iTrustException, ParseException {
        List<MessageBean> mbList = action2.getAllMyMessages();
        mbList = action2.filterMessages(mbList, "Random Person,Appointment,Appointment,Lab,01/01/2010,01/31/2010");
        assertEquals(1, mbList.size());
    }

    public void testGetUnreadCount() throws SQLException {
        assertEquals(1, action.getUnreadCount());
        assertEquals(12, action2.getUnreadCount());
    }

    public void testLinkedToReferral() throws DBException {
        assertEquals(0, action.linkedToReferral(1));
    }
}
