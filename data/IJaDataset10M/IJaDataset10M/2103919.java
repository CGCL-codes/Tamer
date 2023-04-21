package com.genITeam.ria.bl;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.genITeam.ria.dao.TheadPostDAO;
import com.genITeam.ria.dao.UserRequestDAO;
import com.genITeam.ria.exception.NGFException;
import com.genITeam.ria.utility.HibernateUtils;
import com.genITeam.ria.vo.FavouriteForum;
import com.genITeam.ria.vo.Member;
import com.genITeam.ria.vo.ModeratorRequest;
import com.genITeam.ria.vo.Posts;
import com.genITeam.ria.vo.ModeratorRequest;

public class UserRequestBL {

    Logger logger = Logger.getLogger(TheadPostDAO.class);

    public ArrayList getUserRequest() throws NGFException {
        UserRequestDAO userRequestDAO = new UserRequestDAO();
        Transaction tx = null;
        ModeratorRequest moderatorRequestVo;
        ArrayList reqList = null;
        try {
            Session session = HibernateUtils.getSessionFactory().getCurrentSession();
            tx = session.getTransaction();
            tx.begin();
            reqList = (ArrayList) userRequestDAO.getUserRequest(session);
            for (int i = 0; i < reqList.size(); i++) {
                moderatorRequestVo = (ModeratorRequest) reqList.get(i);
                int id = moderatorRequestVo.getId();
                Member member = moderatorRequestVo.getMember();
                Posts posts = moderatorRequestVo.getPosts();
                Character status = moderatorRequestVo.getStatus();
                System.out.print(moderatorRequestVo.getId());
                System.out.print("   ");
                System.out.print(member.getId());
                System.out.print("   ");
                System.out.print(posts.getPostId());
                System.out.print("   ");
                System.out.println(status);
            }
        } catch (Exception e) {
            throw new NGFException(e.getMessage());
        }
        return reqList;
    }

    public ModeratorRequest getRecord(int id) {
        ModeratorRequest moderatorRequestVo = null;
        UserRequestDAO userRequestDAO = new UserRequestDAO();
        try {
            Transaction tx = null;
            Session session = HibernateUtils.getSessionFactory().getCurrentSession();
            tx = session.getTransaction();
            tx.begin();
            moderatorRequestVo = userRequestDAO.getRecord(id, session);
        } catch (Exception e) {
            System.out.println(e);
        }
        return moderatorRequestVo;
    }

    public String generateRequestsXml(ArrayList reqList) throws NGFException {
        ModeratorRequest moderatorRequestVo = new ModeratorRequest();
        Member member = new Member();
        Posts posts = new Posts();
        StringBuffer xml = new StringBuffer("");
        try {
            System.out.println("fakhri\n\n\n\n in BL");
            logger.info("Start generateForumMemberxml");
            int size = reqList.size();
            xml = new StringBuffer("<resultset>\n");
            for (int i = 0; i < size; i++) {
                moderatorRequestVo = (ModeratorRequest) reqList.get(i);
                member = (Member) moderatorRequestVo.getMember();
                posts = (Posts) moderatorRequestVo.getPosts();
                xml.append("<result>");
                xml.append("<id>" + moderatorRequestVo.getId() + "</id>");
                xml.append("<memberid>" + member.getUserName() + "</memberid>");
                xml.append("<postid>" + posts.getTitle() + "</postid>");
                xml.append("<description>" + moderatorRequestVo.getDescription() + "</description>");
                xml.append("<status>" + "Open Request" + "</status>");
                xml.append("</result>");
                xml.append("\n");
            }
            xml.append("</resultset>");
            System.out.println(xml.toString());
            logger.info("xml = " + xml.toString());
        } catch (Exception e) {
            logger.error("Exception = " + e.toString());
            throw new NGFException(e.getMessage());
        }
        return xml.toString();
    }

    public void approveRequest(ModeratorRequest moderatorRequestVo) throws NGFException {
        UserRequestDAO userRequestDAO = new UserRequestDAO();
        Transaction tx = null;
        try {
            Session session = HibernateUtils.getSessionFactory().getCurrentSession();
            tx = session.getTransaction();
            tx.begin();
            userRequestDAO.approveRequest(moderatorRequestVo, session);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
    }

    public void submitUserRequest(ModeratorRequest moderatorRequestVo) throws NGFException {
        UserRequestDAO userRequestDAO = new UserRequestDAO();
        System.out.println("in submit user request member id = " + moderatorRequestVo.getMember().getId());
        Transaction tx = null;
        try {
            Session session = HibernateUtils.getSessionFactory().getCurrentSession();
            tx = session.getTransaction();
            tx.begin();
            userRequestDAO.submitUserRequest(moderatorRequestVo, session);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
    }

    public static void main(String args[]) {
        UserRequestBL userRequestBL = new UserRequestBL();
        try {
            ArrayList list = userRequestBL.getUserRequest();
            String xml = userRequestBL.generateRequestsXml(list);
            System.out.println(xml);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
