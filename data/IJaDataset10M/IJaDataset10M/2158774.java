package com.centraview.support.supportlist;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Logger;
import com.centraview.common.CVDal;
import com.centraview.common.DDNameValue;
import com.centraview.common.DateMember;
import com.centraview.common.EJBUtil;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.support.faq.FAQList;
import com.centraview.support.faq.FAQListElement;
import com.centraview.support.faq.QuestionList;
import com.centraview.support.faq.QuestionListElement;
import com.centraview.support.knowledgebase.KnowledgebaseList;
import com.centraview.support.knowledgebase.KnowledgebaseListElement;
import com.centraview.support.thread.ThreadList;
import com.centraview.support.thread.ThreadListElement;
import com.centraview.support.ticket.TicketList;
import com.centraview.support.ticket.TicketListElement;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class SupportListEJB implements SessionBean {

    private static Logger logger = Logger.getLogger(SupportListEJB.class);

    protected SessionContext ctx;

    private String dataSource = "MySqlDS";

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(SessionContext ctx) {
        this.ctx = ctx;
    }

    public TicketList getTicketList(int individualId, HashMap hashmap) {
        Integer intStart = (Integer) hashmap.get("startATparam");
        Integer intEnd = (Integer) hashmap.get("EndAtparam");
        String strSearch = (String) hashmap.get("searchString");
        String strSortMem = (String) hashmap.get("sortmem");
        Character chrSortType = (Character) hashmap.get("sortType");
        char charSort = chrSortType.charValue();
        int intStartParam = intStart.intValue();
        int intEndParam = intEnd.intValue();
        int beginIndex = Math.max(intStartParam - 100, 1);
        int endindex = intEndParam + 100;
        TicketList ticketList = new TicketList();
        ticketList.setSortMember(strSortMem);
        CVDal cvdl = new CVDal(dataSource);
        Collection colList = null;
        String sortOn = strSortMem;
        if (sortOn != null && sortOn.equals("AssignedTo")) {
            sortOn = "assignindv";
        }
        if (sortOn != null && sortOn.equals("Number")) {
            sortOn = "ticketid";
        }
        if (strSearch != null && strSearch.startsWith("ADVANCE:")) {
            strSearch = strSearch.substring(8);
            String str = "create TEMPORARY TABLE ticketlistSearch " + strSearch;
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery(str);
            cvdl.executeUpdate();
            cvdl.clearParameters();
            String strQuery = "";
            String sortType = "ASC";
            if (charSort == 'A') {
                sortType = "ASC";
            } else {
                sortType = "DESC";
            }
            strQuery = "select tick.ticketid as ticketid,tick.subject as subject,en.name as entity, tick.entityid as entityid,tick.individualid " + "as individualid,tick.created as dateopened,tick.assignedto as assignindv,supstat.name as status, " + "tick.dateclosed as dateclosed,concat(indv.FirstName,' ',indv.LastName) as assignedto from ticket tick " + "left outer join entity en on en.entityid=tick.entityid " + "left outer join individual indv on indv.individualid=tick.assignedto,supportstatus supstat,ticketlistSearch tsearch " + "where tick.status=supstat.statusid and tsearch.ticketid=tick.ticketid	order by '" + sortOn + "' " + sortType;
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery(strQuery);
            colList = cvdl.executeQuery();
            cvdl.clearParameters();
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("DROP TABLE ticketlistSearch");
            cvdl.executeUpdate();
            ticketList.setTotalNoOfRecords(colList.size());
        } else {
            String sortType = "ASC";
            if (charSort == 'A') {
                sortType = "ASC";
            } else {
                sortType = "DESC";
            }
            String query = "SELECT tick.ticketid AS ticketid, tick.subject AS subject, en.name AS entity, " + "tick.entityid AS entityid,tick.individualid AS individualid,tick.created AS " + " dateopened, tick.assignedto AS assignindv, supstat.name AS status, tick.dateclosed " + " AS dateclosed, concat(indv.FirstName,' ',indv.LastName) AS assignedto FROM ticket " + " tick LEFT OUTER JOIN entity en ON en.entityid=tick.entityid LEFT OUTER JOIN individual " + " indv ON indv.individualid=tick.assignedto LEFT OUTER JOIN supportstatus supstat ON  " + " tick.status=supstat.statusid WHERE tick.owner = ? UNION SELECT tick.ticketid AS " + " ticketid,tick.subject AS subject,en.name AS entity, tick.entityid AS entityid," + " tick.individualid AS individualid,tick.created AS dateopened, tick.assignedto AS " + " assignindv,supstat.name AS status,tick.dateclosed AS dateclosed, " + " concat(indv.FirstName,' ',indv.LastName) AS assignedto FROM ticket tick, " + " recordauthorisation b LEFT OUTER JOIN entity en ON en.entityid=tick.entityid " + " LEFT OUTER JOIN individual indv ON indv.individualid=tick.assignedto LEFT OUTER " + " JOIN supportstatus supstat ON  tick.status=supstat.statusid WHERE tick.ticketid = " + " b.recordid and b.recordtypeid = 39 and b.privilegelevel < 40 and b.privilegelevel > 0 " + " and b.individualID = ? UNION SELECT tick.ticketid AS ticketid,tick.subject AS subject," + " en.name AS entity, tick.entityid AS entityid,tick.individualid AS individualid," + " tick.created AS dateopened, tick.assignedto AS assignindv,supstat.name AS status," + " tick.dateclosed AS dateclosed,concat(indv.FirstName,' ',indv.LastName) AS assignedto " + " FROM ticket AS tick INNER JOIN publicrecords pub ON tick.ticketid = pub.recordid LEFT " + " OUTER JOIN entity en ON en.entityid=tick.entityid LEFT OUTER JOIN individual indv ON " + " indv.individualid=tick.assignedto LEFT OUTER JOIN supportstatus supstat ON " + " tick.status=supstat.statusid WHERE pub.moduleid=39 order by '" + sortOn + "' " + sortType;
            cvdl.setSqlQuery(query);
            cvdl.setInt(1, individualId);
            cvdl.setInt(2, individualId);
            colList = cvdl.executeQuery();
            cvdl.setSqlQueryToNull();
            cvdl.setSql("support.ticket.allticketcount");
            Collection count = cvdl.executeQuery();
            Iterator itCount = count.iterator();
            HashMap hmx = (HashMap) itCount.next();
            Integer endCount = (Integer) hmx.get("allticketcount");
            cvdl.clearParameters();
            int totalCount = endCount.intValue();
            ticketList.setTotalNoOfRecords(totalCount);
        }
        cvdl.destroy();
        if (colList.size() > 0) {
            Iterator it = colList.iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                HashMap hm = (HashMap) it.next();
                int ticketID = ((Long) hm.get("ticketid")).intValue();
                try {
                    IntMember intTicketID = new IntMember("TicketID", ticketID, 10, "", 'T', false, 10);
                    StringMember strSubject = null;
                    if ((hm.get("subject") != null)) {
                        strSubject = new StringMember("Subject", (String) hm.get("subject"), 10, "", 'T', true);
                    } else {
                        strSubject = new StringMember("Subject", null, 10, "", 'T', true);
                    }
                    StringMember strStatus = new StringMember("Status", (String) hm.get("status"), 10, "", 'T', false);
                    StringMember strAssignedTo = new StringMember("AssignedTo", (String) hm.get("assignedto"), 10, "", 'T', true);
                    StringMember strEntity = new StringMember("Entity", (String) hm.get("entity"), 10, "", 'T', true);
                    IntMember intIndividualID = null;
                    IntMember intEntityID = null;
                    if ((hm.get("assignindv") != null)) {
                        intIndividualID = new IntMember("IndividualID", ((Long) hm.get("assignindv")).intValue(), 10, "", 'T', false, 10);
                    } else {
                        intIndividualID = new IntMember("IndividualID", 0, 10, "", 'T', false, 10);
                    }
                    if ((hm.get("entityid") != null)) {
                        intEntityID = new IntMember("EntityID", ((Long) hm.get("entityid")).intValue(), 10, "", 'T', false, 10);
                    } else {
                        intEntityID = new IntMember("EntityID", 0, 10, "", 'T', false, 10);
                    }
                    Timestamp dtDateOpened = null;
                    DateMember dmDateOpened = null;
                    String timezoneid = "EST";
                    if ((hm.get("dateopened") != null)) {
                        dtDateOpened = (Timestamp) hm.get("dateopened");
                        dmDateOpened = new DateMember("DateOpened", dtDateOpened, 10, "", 'T', false, 1, timezoneid);
                    } else {
                        dmDateOpened = new DateMember("DateOpened", null, 10, "", 'T', false, 1, timezoneid);
                    }
                    Timestamp dtDateClosed = null;
                    DateMember dmDateClosed = null;
                    if ((hm.get("dateclosed") != null)) {
                        dtDateClosed = (Timestamp) hm.get("dateclosed");
                        dmDateClosed = new DateMember("DateClosed", dtDateClosed, 10, "", 'T', false, 1, timezoneid);
                    } else {
                        dmDateClosed = new DateMember("DateClosed", null, 10, "", 'T', false, 1, timezoneid);
                    }
                    TicketListElement ticketlistelement = new TicketListElement(ticketID);
                    ticketlistelement.put("TicketID", intTicketID);
                    ticketlistelement.put("Subject", strSubject);
                    ticketlistelement.put("DateOpened", dmDateOpened);
                    ticketlistelement.put("Status", strStatus);
                    ticketlistelement.put("DateClosed", dmDateClosed);
                    ticketlistelement.put("AssignedTo", strAssignedTo);
                    ticketlistelement.put("Entity", strEntity);
                    ticketlistelement.put("IndividualID", intIndividualID);
                    ticketlistelement.put("EntityID", intEntityID);
                    ticketlistelement.put("Number", intTicketID);
                    StringBuffer stringbuffer = new StringBuffer("00000000000");
                    stringbuffer.setLength(11);
                    String s3 = (new Integer(i)).toString();
                    stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
                    String s4 = stringbuffer.toString();
                    ticketList.put(s4, ticketlistelement);
                } catch (Exception e) {
                    logger.debug(" [Exception] SupportListEJB.getTicketList " + e);
                }
            }
        }
        ticketList.setListType("Ticket");
        ticketList.setBeginIndex(beginIndex);
        ticketList.setEndIndex(ticketList.size());
        return ticketList;
    }

    public FAQList getFAQList(int userID, HashMap hashmap) {
        Integer intStart = (Integer) hashmap.get("startATparam");
        Integer intEnd = (Integer) hashmap.get("EndAtparam");
        String strSearch = (String) hashmap.get("searchString");
        String strSortMem = (String) hashmap.get("sortmem");
        Character chrSortType = (Character) hashmap.get("sortType");
        char charSort = chrSortType.charValue();
        int intStartParam = intStart.intValue();
        int intEndParam = intEnd.intValue();
        int beginIndex = Math.max(intStartParam - 100, 1);
        int endindex = intEndParam + 100;
        FAQList faqList = new FAQList();
        faqList.setSortMember(strSortMem);
        CVDal cvdl = new CVDal(dataSource);
        Collection colList = null;
        if (strSearch != null && strSearch.startsWith("ADVANCE:")) {
            strSearch = strSearch.substring(8);
            String str = "create TEMPORARY TABLE faqlistSearch " + strSearch;
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery(str);
            cvdl.executeUpdate();
            cvdl.clearParameters();
            String strQuery = "";
            String sortType = "ASC";
            if (charSort == 'A') {
                sortType = "ASC";
            } else {
                sortType = "DESC";
            }
            strQuery = "Select fq.faqid as faqid,fq.title as title ,fq.created as created ,fq.updated as updated from faq fq,faqlistSearch fsearch where fsearch.faqid=fq.faqid order by '" + strSortMem + "' " + sortType;
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery(strQuery);
            colList = cvdl.executeQuery();
            cvdl.clearParameters();
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("DROP TABLE faqlistSearch");
            cvdl.executeUpdate();
            faqList.setTotalNoOfRecords(colList.size());
        } else {
            String sortType = "ASC";
            if (charSort == 'A') sortType = "ASC"; else sortType = "DESC";
            cvdl.setDynamicQuery("faq.allfaq", sortType, strSortMem, beginIndex, endindex);
            cvdl.setInt(1, userID);
            cvdl.setInt(2, userID);
            colList = cvdl.executeQuery();
            cvdl.clearParameters();
            cvdl.setSql("support.faq.allfaqcount");
            Collection count = cvdl.executeQuery();
            Iterator itCount = count.iterator();
            HashMap hmx = (HashMap) itCount.next();
            Integer endCount = (Integer) hmx.get("allcountfaq");
            cvdl.clearParameters();
            int totalCount = endCount.intValue();
            faqList.setTotalNoOfRecords(totalCount);
        }
        if (colList != null) {
            Iterator it = colList.iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                HashMap hm = (HashMap) it.next();
                int faqID = ((Long) hm.get("faqid")).intValue();
                try {
                    IntMember intFaqID = new IntMember("FaqID", faqID, 10, "", 'T', false, 10);
                    StringMember strTitle = null;
                    if ((hm.get("title") != null)) strTitle = new StringMember("Title", (String) hm.get("title"), 10, "/centraview/ViewNoteHandler.do?typeOfContact=entity&rowId=1", 'T', true); else strTitle = new StringMember("Title", null, 10, "/centraview/ViewNoteHandler.do?typeOfContact=entity&rowId=1", 'T', true);
                    Timestamp dtCreated = null;
                    DateMember dmCreated = null;
                    String timezoneid = "EST";
                    if ((hm.get("created") != null)) {
                        dtCreated = (Timestamp) hm.get("created");
                        dmCreated = new DateMember("Created", dtCreated, 10, "", 'T', false, 1, timezoneid);
                    } else {
                        dmCreated = new DateMember("Created", null, 10, "", 'T', false, 1, timezoneid);
                    }
                    Timestamp dtUpdated = null;
                    DateMember dmUpdated = null;
                    if ((hm.get("updated") != null)) {
                        dtUpdated = (Timestamp) hm.get("updated");
                        dmUpdated = new DateMember("Updated", dtUpdated, 10, "", 'T', false, 1, timezoneid);
                    } else {
                        dmUpdated = new DateMember("Updated", null, 10, "", 'T', false, 1, timezoneid);
                    }
                    FAQListElement faqlistelement = new FAQListElement(faqID);
                    faqlistelement.put("FaqID", intFaqID);
                    faqlistelement.put("Title", strTitle);
                    faqlistelement.put("Created", dmCreated);
                    faqlistelement.put("Updated", dmUpdated);
                    StringBuffer stringbuffer = new StringBuffer("00000000000");
                    stringbuffer.setLength(11);
                    String s3 = (new Integer(i)).toString();
                    stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
                    String s4 = stringbuffer.toString();
                    faqList.put(s4, faqlistelement);
                } catch (Exception e) {
                    logger.debug(" [Exception] SupportListEJB.getFAQList " + e);
                }
            }
        }
        faqList.setListType("FAQ");
        faqList.setBeginIndex(beginIndex);
        faqList.setEndIndex(faqList.size());
        return faqList;
    }

    public KnowledgebaseList getKnowledgebaseList(int userID, int curCategoryID, HashMap hashmap) {
        Integer intStart = (Integer) hashmap.get("startATparam");
        Integer intEnd = (Integer) hashmap.get("EndAtparam");
        String strSearch = (String) hashmap.get("searchString");
        String strSortMem = (String) hashmap.get("sortmem");
        Character chrSortType = (Character) hashmap.get("sortType");
        Integer intCategoryID = (Integer) hashmap.get("curCategoryID");
        Boolean tempCustomerViewFlag = (Boolean) hashmap.get("customerViewFlag");
        boolean customerViewFlag = tempCustomerViewFlag.booleanValue();
        char charSort = chrSortType.charValue();
        int intStartParam = intStart.intValue();
        int intEndParam = intEnd.intValue();
        int categoryID = intCategoryID.intValue();
        String strOriginal = strSearch;
        int beginIndex = 0;
        KnowledgebaseList kblist = new KnowledgebaseList();
        kblist.setSortMember(strSortMem);
        Vector vec = new Vector();
        CVDal cvdl = new CVDal(dataSource);
        Collection colList = null;
        try {
            if (customerViewFlag) {
                cvdl.setSqlQueryToNull();
                String str = "select 'KBELEMENT' as Catkb,kb.kbid ID,kb.title Name, " + "kb.created DateCreated,kb.updated DateUpdated,kb.category  " + "from knowledgebase kb where category=?  and kb.publishToCustomerView='YES' union  " + "select 'CATEGORY' as Catkb,cat.catid ID,cat.title Name, " + "cat.created DateCreated,cat.Modified DateUpdated,cat.parent  " + "from individual indv,category cat where cat.parent=? and cat.publishToCustomerView='YES' ;";
                cvdl.setSqlQuery(str);
                cvdl.setInt(1, categoryID);
                cvdl.setInt(2, categoryID);
                colList = cvdl.executeQuery();
                cvdl.clearParameters();
            } else {
                if (strSearch != null && strSearch.startsWith("ADVANCE:")) {
                    strSearch = strSearch.substring(8);
                    String str = "create TEMPORARY TABLE kblistSearch " + strSearch;
                    cvdl.setSqlQueryToNull();
                    cvdl.setSqlQuery(str);
                    cvdl.executeUpdate();
                    cvdl.clearParameters();
                    String strQuery = "";
                    String sortType = "ASC";
                    if (charSort == 'A') {
                        sortType = "ASC";
                    } else {
                        sortType = "DESC";
                    }
                    strQuery = "SELECT 'KBELEMENT' AS Catkb, kb.kbid AS ID, kb.title AS Name, " + "kb.created AS DateCreated, kb.updated AS DateUpdated, kb.category " + "FROM knowledgebase kb, kblistSearch kbsearch WHERE kbsearch.kbid=kb.kbid " + "ORDER BY '" + strSortMem + "' " + sortType;
                    cvdl.setSqlQueryToNull();
                    cvdl.setSqlQuery(strQuery);
                    colList = cvdl.executeQuery();
                    cvdl.clearParameters();
                    cvdl.setSqlQueryToNull();
                    cvdl.setSqlQuery("DROP TABLE kblistSearch");
                    cvdl.executeUpdate();
                } else {
                    String sortType = "ASC";
                    if (charSort != 'A') {
                        sortType = "DESC";
                    }
                    cvdl.setDynamicQuery("kb.allkb", sortType, strSortMem);
                    cvdl.setInt(1, categoryID);
                    cvdl.setInt(2, userID);
                    cvdl.setInt(3, categoryID);
                    cvdl.setInt(4, categoryID);
                    cvdl.setInt(5, userID);
                    cvdl.setInt(6, categoryID);
                    colList = cvdl.executeQuery();
                    cvdl.clearParameters();
                }
            }
        } catch (Exception e) {
            logger.debug(" [Exception] SupportListEJB.getKnowledgebaseList " + e);
        } finally {
            cvdl.destroy();
            cvdl = null;
        }
        Iterator it = colList.iterator();
        int i = 0;
        if (colList.size() > 0) {
            while (it.hasNext()) {
                i++;
                HashMap hm = (HashMap) it.next();
                int kbElementID = ((Long) hm.get("ID")).intValue();
                try {
                    IntMember intKbElementID = new IntMember("ID", kbElementID, 10, "", 'T', false, 10);
                    StringMember strName = new StringMember("Name", (String) hm.get("Name"), 10, "", 'T', true);
                    StringMember strCatKB = new StringMember("CatKB", (String) hm.get("Catkb"), 10, "", 'T', false);
                    Timestamp dtCreated = null;
                    Timestamp dtUpdated = null;
                    DateMember dmCreated = null;
                    DateMember dmUpdated = null;
                    String timezoneid = "EST";
                    if ((hm.get("DateCreated") != null)) {
                        dtCreated = (Timestamp) hm.get("DateCreated");
                        dmCreated = new DateMember("DateCreated", dtCreated, 10, "", 'T', false, 1, timezoneid);
                    } else {
                        dmCreated = new DateMember("DateCreated", null, 10, "", 'T', false, 1, timezoneid);
                    }
                    if ((hm.get("DateUpdated") != null)) {
                        dtUpdated = (Timestamp) hm.get("DateUpdated");
                        dmUpdated = new DateMember("DateUpdated", dtUpdated, 10, "", 'T', false, 1, timezoneid);
                    } else {
                        dmUpdated = new DateMember("DateUpdated", null, 10, "", 'T', false, 1, timezoneid);
                    }
                    KnowledgebaseListElement kblistElement = new KnowledgebaseListElement(kbElementID);
                    kblistElement.put("CatKBID", intKbElementID);
                    kblistElement.put("Name", strName);
                    kblistElement.put("DateCreated", dmCreated);
                    kblistElement.put("DateUpdated", dmUpdated);
                    kblistElement.put("CatKB", strCatKB);
                    StringBuffer stringbuffer = new StringBuffer("00000000000");
                    stringbuffer.setLength(11);
                    String s3 = (new Integer(i)).toString();
                    stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
                    String s4 = stringbuffer.toString();
                    kblist.put(s4, kblistElement);
                } catch (Exception e) {
                    logger.debug(" [Exception] SupportListEJB.getKnowldegebaseList " + e);
                    e.printStackTrace();
                }
            }
        }
        Vector rootPath = getCategoryRootPath(userID, categoryID);
        kblist.setCustomerViewFlag(customerViewFlag);
        kblist.setCategoryStructure(rootPath);
        kblist.setTotalNoOfRecords(kblist.size());
        kblist.setListType("Knowledgebase");
        kblist.setBeginIndex(beginIndex);
        kblist.setEndIndex(kblist.size());
        return (kblist);
    }

    public ThreadList getThreadList(int userID, int curTicketID) {
        ThreadList threadList = new ThreadList();
        CVDal cvdl = new CVDal(dataSource);
        Collection colList = null;
        cvdl.setSql("support.ticket.getthreadforticket");
        cvdl.setInt(1, curTicketID);
        colList = cvdl.executeQuery();
        cvdl.clearParameters();
        cvdl.destroy();
        if (colList.size() > 0) {
            Iterator it = colList.iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                HashMap hm = (HashMap) it.next();
                int threadID = ((Long) hm.get("threadid")).intValue();
                try {
                    IntMember intThreadID = new IntMember("ThreadID", threadID, 10, "", 'T', false, 10);
                    StringMember strTitle = null;
                    String str = (String) hm.get("title");
                    String subStr = null;
                    if (str != null) {
                        if (str.length() >= 255) subStr = str.substring(0, 255); else subStr = str;
                        strTitle = new StringMember("Title", subStr, 10, "", 'T', true);
                    } else {
                        strTitle = new StringMember("Title", null, 10, "", 'T', true);
                    }
                    StringMember strDesc = null;
                    str = (String) hm.get("detail");
                    if (str != null) {
                        if (str.length() >= 255) {
                            subStr = str.substring(0, 252);
                            subStr = subStr.concat("...");
                        } else {
                            subStr = str;
                        }
                        strDesc = new StringMember("Description", subStr, 10, "", 'T', false);
                    } else {
                        strDesc = new StringMember("Description", null, 10, "", 'T', false);
                    }
                    StringMember strPriority = new StringMember("Priority", (String) hm.get("priorityname"), 10, "", 'T', false);
                    StringMember strCreatedBy = new StringMember("CreatedBy", (String) hm.get("createdby"), 10, "", 'T', true);
                    IntMember intIndividualID = null;
                    IntMember intEntityID = null;
                    if ((hm.get("creator") != null)) intIndividualID = new IntMember("IndividualID", ((Long) hm.get("creator")).intValue(), 10, "", 'T', false, 10); else intIndividualID = new IntMember("IndividualID", 0, 10, "", 'T', false, 10);
                    if ((hm.get("entityid") != null)) intEntityID = new IntMember("EntityID", ((Long) hm.get("entityid")).intValue(), 10, "", 'T', false, 10); else intEntityID = new IntMember("EntityID", 0, 10, "", 'T', false, 10);
                    Timestamp dtCreated = null;
                    DateMember dmCreated = null;
                    String timezoneid = "EST";
                    if ((hm.get("created") != null)) {
                        dtCreated = (Timestamp) hm.get("created");
                        dmCreated = new DateMember("Created", dtCreated, 10, "", 'T', false, 1, timezoneid);
                    } else {
                        dmCreated = new DateMember("Created", null, 10, "", 'T', false, 1, timezoneid);
                    }
                    ThreadListElement threadlistelement = new ThreadListElement(threadID);
                    threadlistelement.put("ThreadID", intThreadID);
                    threadlistelement.put("Description", strDesc);
                    threadlistelement.put("Title", strTitle);
                    threadlistelement.put("Created", dmCreated);
                    threadlistelement.put("Priority", strPriority);
                    threadlistelement.put("CreatedBy", strCreatedBy);
                    threadlistelement.put("IndividualID", intIndividualID);
                    threadlistelement.put("EntityID", intEntityID);
                    StringBuffer stringbuffer = new StringBuffer("00000000000");
                    stringbuffer.setLength(11);
                    String s3 = (new Integer(i)).toString();
                    stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
                    String s4 = stringbuffer.toString();
                    threadList.put(s4, threadlistelement);
                } catch (Exception e) {
                    logger.debug(" [Exception] SupportListEJB.getThreadList " + e);
                    e.printStackTrace();
                }
            }
        }
        return threadList;
    }

    /**
   * This method returns All The Root(Parent) Categories of This categoryID in
   * the form of DDNameValue object packaged in a Vector
   * 
   * @param userID
   * @param categoryID
   * @return vector (vecDDNameValue)
   */
    public Vector getCategoryRootPath(int userID, int categoryID) {
        CVDal dl = new CVDal(dataSource);
        Vector vecDDNameValue = new Vector();
        int catID = categoryID;
        dl.clearParameters();
        dl.setSql("kb.getcurcategoryname");
        dl.setInt(1, categoryID);
        Collection colID = dl.executeQuery();
        if (colID.size() > 0) {
            Iterator itCur = colID.iterator();
            HashMap hmCur = (HashMap) itCur.next();
            DDNameValue curnameValueObj = new DDNameValue(catID, (String) hmCur.get("title"));
            vecDDNameValue.addElement(curnameValueObj);
            while (catID != 0) {
                dl.clearParameters();
                dl.setSql("kb.getparentinfo");
                dl.setInt(1, catID);
                Collection colParID = dl.executeQuery();
                Iterator iter = colParID.iterator();
                int parID = 0;
                String parName = "";
                String system = "";
                while (iter.hasNext()) {
                    HashMap hm = (HashMap) iter.next();
                    parID = ((Long) hm.get("parentid")).intValue();
                    parName = (String) hm.get("parenttitle");
                }
                catID = parID;
                if (parID != 0) {
                    DDNameValue thisnameValueObj = new DDNameValue(parID, parName);
                    vecDDNameValue.addElement(thisnameValueObj);
                }
            }
        }
        dl.destroy();
        return vecDDNameValue;
    }

    public QuestionList getQuestionList(int userID, int curFaqID) {
        QuestionList questionList = new QuestionList();
        CVDal cvdl = new CVDal(dataSource);
        Collection colList = null;
        cvdl.setSql("support.faq.getquestionforfaq");
        cvdl.setInt(1, curFaqID);
        colList = cvdl.executeQuery();
        cvdl.clearParameters();
        cvdl.destroy();
        if (colList.size() > 0) {
            Iterator it = colList.iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                HashMap hm = (HashMap) it.next();
                int questionID = ((Long) hm.get("questionid")).intValue();
                try {
                    IntMember intQuestionID = new IntMember("QuestionID", questionID, 10, "", 'T', false, 10);
                    StringMember strQuestion = null;
                    if ((hm.get("question") != null)) strQuestion = new StringMember("Question", (String) hm.get("question"), 10, "", 'T', true); else strQuestion = new StringMember("Question", null, 10, "", 'T', true);
                    StringMember strAnswer = null;
                    if ((hm.get("answer") != null)) strAnswer = new StringMember("Answer", (String) hm.get("answer"), 10, "", 'T', true); else strAnswer = new StringMember("Answer", null, 10, "", 'T', true);
                    QuestionListElement questionListElement = new QuestionListElement(questionID);
                    questionListElement.put("QuestionID", intQuestionID);
                    questionListElement.put("Question", strQuestion);
                    questionListElement.put("Answer", strAnswer);
                    StringBuffer stringbuffer = new StringBuffer("00000000000");
                    stringbuffer.setLength(11);
                    String s3 = (new Integer(i)).toString();
                    stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
                    String s4 = stringbuffer.toString();
                    questionList.put(s4, questionListElement);
                } catch (Exception e) {
                    logger.debug(" [Exception] SupportListEJB.getQuestionList " + e);
                    e.printStackTrace();
                }
            }
        }
        return questionList;
    }

    /**
   * @author Kevin McAllister <kevin@centraview.com>This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
    public void setDataSource(String ds) {
        this.dataSource = ds;
    }

    /**
   * Returns a ValueListVO representing a list of Ticket records, based on
   * the <code>parameters</code> argument which limits results.
   */
    public ValueListVO getTicketValueList(int individualID, ValueListParameters parameters) {
        ArrayList list = new ArrayList();
        boolean permissionSwitch = individualID < 1 ? false : true;
        boolean applyFilter = false;
        String filter = parameters.getFilter();
        CVDal cvdl = new CVDal(this.dataSource);
        try {
            if (filter != null && filter.length() > 0) {
                String str = "CREATE TABLE ticketlistfilter " + filter;
                cvdl.setSqlQuery(str);
                cvdl.executeUpdate();
                cvdl.setSqlQueryToNull();
                applyFilter = true;
            }
            int numberOfRecords = 0;
            if (applyFilter) {
                numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "ticketlistfilter", individualID, 39, "ticket", "ticketid", "owner", null, permissionSwitch);
            } else if (permissionSwitch) {
                numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 39, "ticket", "ticketid", "owner", null, permissionSwitch);
            }
            parameters.setTotalRecords(numberOfRecords);
            String query = this.buildTicketListQuery(applyFilter, permissionSwitch, parameters);
            cvdl.setSqlQuery("CREATE TEMPORARY TABLE ticketlist " + query);
            cvdl.executeUpdate();
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("SELECT * FROM ticketlist");
            list = cvdl.executeQueryList(1);
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("DROP TABLE ticketlist");
            cvdl.executeUpdate();
            if (applyFilter) {
                cvdl.setSqlQueryToNull();
                cvdl.setSqlQuery("DROP TABLE ticketlistfilter");
                cvdl.executeUpdate();
            }
            if (applyFilter || permissionSwitch) {
                cvdl.setSqlQueryToNull();
                cvdl.setSqlQuery("DROP TABLE listfilter");
                cvdl.executeUpdate();
            }
        } finally {
            cvdl.destroy();
            cvdl = null;
        }
        return new ValueListVO(list, parameters);
    }

    private String buildTicketListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters) {
        String select = " SELECT t.ticketid AS Number, t.subject AS Subject, en.name as Entity " + ", t.entityid, t.individualid, t.created as DateOpened," + " t.assignedto AS assignedID, supportstatus.name AS Status," + " t.dateclosed AS DateClosed, concat(i.FirstName,' ',i.LastName) AS AssignedTo ";
        String joinConditions = " LEFT OUTER JOIN entity en ON en.entityid=t.entityid " + " LEFT OUTER JOIN individual i ON i.individualid=t.assignedto " + " LEFT OUTER JOIN supportstatus ON t.status=supportstatus.statusid ";
        StringBuffer from = new StringBuffer(" FROM ticket t ");
        StringBuffer where = new StringBuffer(" WHERE ");
        String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
        String limit = parameters.getLimitParam();
        if (applyFilter || permissionSwitch) {
            from.append(", listfilter AS lf ");
            where.append(" t.ticketid = lf.ticketid ");
        }
        StringBuffer query = new StringBuffer();
        query.append(select);
        query.append(from);
        query.append(joinConditions);
        query.append(where);
        query.append(orderBy);
        query.append(limit);
        return query.toString();
    }

    /**
	  * Returns a ValueListVO representing a list of FAQ records, based on
	  * the <code>parameters</code> argument which limits results.
	  */
    public ValueListVO getFAQValueList(int individualID, ValueListParameters parameters) {
        ArrayList list = new ArrayList();
        boolean permissionSwitch = individualID < 1 ? false : true;
        boolean applyFilter = false;
        String filter = parameters.getFilter();
        CVDal cvdl = new CVDal(this.dataSource);
        if (filter != null && filter.length() > 0) {
            String str = "CREATE TABLE faqlistfilter " + filter;
            cvdl.setSqlQuery(str);
            cvdl.executeUpdate();
            cvdl.setSqlQueryToNull();
            applyFilter = true;
        }
        int numberOfRecords = 0;
        if (applyFilter) numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "faqlistfilter", individualID, 40, "faq", "faqid", "owner", null, permissionSwitch); else if (permissionSwitch) numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 40, "faq", "faqid", "owner", null, permissionSwitch);
        parameters.setTotalRecords(numberOfRecords);
        String query = this.buildFAQListQuery(applyFilter, permissionSwitch, parameters);
        cvdl.setSqlQuery(query);
        list = cvdl.executeQueryList(1);
        cvdl.setSqlQueryToNull();
        if (applyFilter) {
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("DROP TABLE faqlistfilter");
            cvdl.executeUpdate();
        }
        if (applyFilter || permissionSwitch) {
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("DROP TABLE listfilter");
            cvdl.executeUpdate();
        }
        cvdl.destroy();
        cvdl = null;
        return new ValueListVO(list, parameters);
    }

    private String buildFAQListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters) {
        String select = "SELECT f.faqid, f.title, f.created, f.updated ";
        StringBuffer from = new StringBuffer("FROM faq f ");
        StringBuffer where = new StringBuffer("WHERE 1 = 1 ");
        String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
        String limit = parameters.getLimitParam();
        if (applyFilter || permissionSwitch) {
            from.append(", listfilter AS lf ");
            where.append("AND f.faqid = lf.faqid ");
        }
        StringBuffer query = new StringBuffer();
        query.append(select);
        query.append(from);
        query.append(where);
        query.append(orderBy);
        query.append(limit);
        return query.toString();
    }

    /**
	  * Returns a ValueListVO representing a list of Knowledgebase records, based on
	  * the <code>parameters</code> argument which limits results.
	  */
    public ValueListVO getKnowledgeBaseValueList(int individualID, ValueListParameters parameters) {
        ArrayList list = new ArrayList();
        boolean permissionSwitch = individualID < 1 ? false : true;
        boolean applyFilter = false;
        String filter = parameters.getFilter();
        CVDal cvdl = new CVDal(this.dataSource);
        if (filter != null && filter.length() > 0) {
            String str = "CREATE TABLE knowledgebaselistfilter " + filter;
            cvdl.setSqlQuery(str);
            cvdl.executeUpdate();
            cvdl.setSqlQueryToNull();
            applyFilter = true;
        }
        int numberOfRecords = 0;
        if (applyFilter) numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "knowledgebaselistfilter", individualID, 41, "knowledgebase", "kbid", "owner", null, permissionSwitch); else if (permissionSwitch) numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 41, "knowledgebase", "kbid", "owner", null, permissionSwitch);
        parameters.setTotalRecords(numberOfRecords);
        String query = this.buildKnowledgeBaseListQuery(applyFilter, permissionSwitch, parameters);
        cvdl.setSqlQuery(query);
        list = cvdl.executeQueryList(1);
        cvdl.setSqlQueryToNull();
        if (applyFilter) {
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("DROP TABLE knowledgebaselistfilter");
            cvdl.executeUpdate();
        }
        if (applyFilter || permissionSwitch) {
            cvdl.setSqlQueryToNull();
            cvdl.setSqlQuery("DROP TABLE listfilter");
            cvdl.executeUpdate();
        }
        cvdl.destroy();
        cvdl = null;
        return new ValueListVO(list, parameters);
    }

    private String buildKnowledgeBaseListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters) {
        String select = "SELECT kb.kbid, 'KBELEMENT' AS Catkb, kb.title, kb.created, kb.updated, kb.category ";
        StringBuffer from = new StringBuffer("FROM knowledgebase kb ");
        StringBuffer where = new StringBuffer("WHERE kb.category = " + parameters.getCategoryID() + " ");
        if (applyFilter || permissionSwitch) {
            from.append(", listfilter AS lf ");
            where.append("AND kb.kbid = lf.kbid ");
        }
        StringBuffer query = new StringBuffer();
        query.append(select);
        query.append(from);
        query.append(where);
        query.append("UNION ");
        select = "SELECT c.catid, 'CATEGORY' AS Catkb, c.title, c.created, c.modified, c.parent ";
        from = new StringBuffer("FROM category c ");
        where = new StringBuffer("WHERE c.parent = " + parameters.getCategoryID() + " ");
        String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
        String limit = parameters.getLimitParam();
        query.append(select);
        query.append(from);
        query.append(where);
        query.append(orderBy);
        query.append(limit);
        return query.toString();
    }
}
