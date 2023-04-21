package com.genITeam.ria.dao;

import com.genITeam.ria.exception.NGFException;
import com.genITeam.ria.vo.Posts;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.*;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

public class TheadPostDAO {

    public TheadPostDAO() {
        BasicConfigurator.configure();
    }

    Logger logger = Logger.getLogger(TheadPostDAO.class);

    /**
	 * getChilds Get the Childs of Posts
	 * 
	 * @param int ,
	 *            Session
	 * 
	 * @return Posts
	 * @throws NGFException
	 */
    public Posts getChilds(int postId, Session session) throws NGFException {
        Posts post = null;
        try {
            logger.info("Start getChilds");
            System.out.println("in dao " + postId);
            post = (Posts) session.createCriteria(Posts.class).add(Expression.eq("postId", new Integer(postId))).uniqueResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
        return post;
    }

    /**
	 * getPost Get the Posts of the Selected Thread
	 * 
	 * @param int ,
	 *            Session
	 * 
	 * @return Posts
	 * @throws NGFException
	 */
    public Posts getPost(int postId, Session session) throws NGFException {
        Posts postVo = null;
        try {
            logger.info("Start getPost");
            System.out.println("in dao " + postId);
            postVo = (Posts) session.createCriteria(Posts.class).add(Expression.eq("postId", new Integer(postId))).uniqueResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
        return postVo;
    }

    /**
	 * deleteThread Delete the selected thread 
	 * 
	 * @param Posts ,
	 *            Session
	 * 
	 * @return none
	 * @throws NGFException
	 */
    public void deleteThread(Posts posts, Session session) throws NGFException {
        Posts postVo = null;
        try {
            logger.info("Start deleteThread");
            System.out.println("in DAO deleteThread  thread id = " + posts.getPostId());
            postVo = (Posts) session.createCriteria(Posts.class).add(Expression.eq("postId", new Integer(posts.getPostId()))).add(Expression.eq("postType", new Character('T'))).uniqueResult();
            session.delete(postVo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
    }

    /**
	 * deletePost Delete the selected posts 
	 * 
	 * @param Posts ,
	 *            Session
	 * 
	 * @return none
	 * @throws NGFException
	 */
    public void deletePost(Posts posts, Session session) throws NGFException {
        Posts postVo = null;
        try {
            logger.info("Start deletePost");
            postVo = (Posts) session.createCriteria(Posts.class).add(Expression.eq("postId", new Integer(posts.getPostId()))).add(Expression.eq("postType", new Character('P'))).uniqueResult();
            session.delete(postVo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
    }

    /**
	 * getForums Return the List of forums 
	 * 
	 * @param int ,
	 *            Session
	 * 
	 * @return Posts
	 * @throws NGFException
	 */
    public Posts getForums(int postId, Session session) throws NGFException {
        Posts post = null;
        try {
            logger.info("Start getForums");
            System.out.println("in dao " + postId);
            post = (Posts) session.createCriteria(Posts.class).add(Expression.eq("postId", new Integer(postId))).add(Expression.eq("postType", "F")).uniqueResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
        return post;
    }

    /**
	 * getRoomList Return the List of all Chat Forum  
	 * 
	 * @param int ,
	 *            Session
	 * 
	 * @return ArrayList
	 * @throws NGFException
	 */
    public ArrayList getRoomList(int postId, Session session) throws NGFException {
        ArrayList a = new ArrayList();
        try {
            logger.info("Start getRoomList");
            System.out.println("in dao " + postId);
            List l = session.createCriteria(Posts.class).add(Expression.eq("postType", "T")).list();
            a.addAll(l);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.toString());
            throw new NGFException(e.getMessage());
        }
        return a;
    }
}
