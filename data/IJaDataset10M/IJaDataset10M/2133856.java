package com.centraview.support.supportlist;

import java.util.HashMap;
import java.util.Vector;
import javax.ejb.EJBLocalObject;
import com.centraview.support.faq.FAQList;
import com.centraview.support.faq.QuestionList;
import com.centraview.support.knowledgebase.KnowledgebaseList;
import com.centraview.support.thread.ThreadList;
import com.centraview.support.ticket.TicketList;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface SupportListLocal extends EJBLocalObject {

    public TicketList getTicketList(int userID, HashMap hashmap);

    public FAQList getFAQList(int userID, HashMap hashmap);

    public KnowledgebaseList getKnowledgebaseList(int userID, int curCategoryID, HashMap hashmap);

    public ThreadList getThreadList(int userID, int curTicketID);

    public QuestionList getQuestionList(int userID, int curFaqID);

    /**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
    public void setDataSource(String ds);

    public ValueListVO getTicketValueList(int individualID, ValueListParameters parameters);

    public ValueListVO getFAQValueList(int individualID, ValueListParameters parameters);

    public ValueListVO getKnowledgeBaseValueList(int individualID, ValueListParameters parameters);

    public Vector getCategoryRootPath(int userID, int categoryID);
}
