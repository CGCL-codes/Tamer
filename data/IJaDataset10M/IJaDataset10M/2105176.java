package com.javaeedev.dao.impl;

import java.io.Serializable;
import com.javaeedev.domain.AbstractPost;
import com.javaeedev.domain.AbstractRankRecord;
import com.javaeedev.domain.Article;
import com.javaeedev.domain.ArticleDifficultyRecord;
import com.javaeedev.domain.ArticleRankRecord;
import com.javaeedev.domain.Reply;
import com.javaeedev.domain.ReplyRankRecord;
import com.javaeedev.domain.Resource;
import com.javaeedev.domain.ResourceRankRecord;
import com.javaeedev.domain.Topic;
import com.javaeedev.domain.TopicRankRecord;
import com.javaeedev.domain.User;
import com.javaeedev.hibernate.HibernateUtil;

/**
 * Create rank for Article, Topic, Reply.
 * 
 * @author Xuefeng
 * 
 * @spring.bean id="rankDao"
 */
public class RankDaoImpl extends GenericDaoImpl<AbstractRankRecord> {

    public RankDaoImpl() {
        super(AbstractRankRecord.class);
    }

    private static final String UPDATE_ARTICLE = "update Article as p set p.rankTotal=p.rankTotal+?, p.rankCount=p.rankCount+1 where p.id=? and " + "(select count(*) from ArticleRankRecord as r where r.article=? and r.user=?)=0";

    private static final String UPDATE_TOPIC = "update Topic as p set p.rankTotal=p.rankTotal+?, p.rankCount=p.rankCount+1 where p.id=? and " + "(select count(*) from TopicRankRecord as r where r.topic=? and r.user=?)=0";

    private static final String UPDATE_REPLY = "update Reply as p set p.rankTotal=p.rankTotal+?, p.rankCount=p.rankCount+1 where p.id=? and " + "(select count(*) from ReplyRankRecord as r where r.reply=? and r.user=?)=0";

    private static final String UPDATE_RESOURCE = "update Resource as p set p.rankTotal=p.rankTotal+?, p.rankCount=p.rankCount+1 where p.id=? and " + "(select count(*) from ResourceRankRecord as r where r.resource=? and r.user=?)=0";

    /**
     * Create a rank on a post.
     * 
     * @param post Post to rank. Example, Article, Topic, Reply, Resource, etc.
     * @param user User who rank this post.
     * @param rank 1-5.
     * @return True if rank ok.
     */
    public boolean createRank(AbstractPost post, User user, int rank) {
        String update = null;
        AbstractRankRecord record = null;
        if (post instanceof Article) {
            update = UPDATE_ARTICLE;
            record = new ArticleRankRecord((Article) post, user, rank);
        } else if (post instanceof Topic) {
            update = UPDATE_TOPIC;
            record = new TopicRankRecord((Topic) post, user, rank);
        } else if (post instanceof Reply) {
            update = UPDATE_REPLY;
            record = new ReplyRankRecord((Reply) post, user, rank);
        } else if (post instanceof Resource) {
            update = UPDATE_RESOURCE;
            record = new ResourceRankRecord((Resource) post, user, rank);
        } else throw new UnsupportedOperationException("Unsupported post type: " + post.getClass().getSimpleName());
        if (1 == executeUpdate(update, new Object[] { new Integer(rank), post.getId(), post, user })) {
            HibernateUtil.getCurrentSession().save(record);
            return true;
        }
        return false;
    }

    public boolean createDifficulty(Article article, User user, int difficulty) {
        if (1 == executeUpdate("update Article as a set a.difficultyTotal=a.difficultyTotal+?, a.difficultyCount=a.difficultyCount+1 where a.id=? and (select count(r.id) from ArticleDifficultyRecord as r where r.article=? and r.user=?)=0", new Object[] { new Integer(difficulty), article.getId(), article, user })) {
            HibernateUtil.getCurrentSession().save(new ArticleDifficultyRecord(article, user, difficulty));
            return true;
        }
        return false;
    }

    public void deleteArticleRank(Article article) {
        executeUpdate("delete from ArticleRankRecord as r where r.article=?", new Object[] { article });
    }

    public void deleteResourceRank(Resource resource) {
        executeUpdate("delete from ResourceRankRecord as r where r.resource=?", new Object[] { resource });
    }

    public void deleteTopicRank(Topic topic) {
        executeUpdate("delete from TopicRankRecord as r where r.topic=?", new Object[] { topic });
    }

    public void deleteReplyRank(Reply reply) {
        executeUpdate("delete from ReplyRankRecord as r where r.reply=?", new Object[] { reply });
    }

    @Override
    public void create(AbstractRankRecord t) {
        throw new UnsupportedOperationException("Not support!");
    }

    @Override
    public void delete(AbstractRankRecord t) {
        throw new UnsupportedOperationException("Not support!");
    }

    @Override
    public AbstractRankRecord get(Serializable id) {
        throw new UnsupportedOperationException("Not support!");
    }

    @Override
    public AbstractRankRecord query(Serializable id) {
        throw new UnsupportedOperationException("Not support!");
    }

    @Override
    public void update(AbstractRankRecord t) {
        throw new UnsupportedOperationException("Not support!");
    }
}
