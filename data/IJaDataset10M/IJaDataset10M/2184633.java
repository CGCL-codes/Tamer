package org.fpse.store;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.fpse.forum.Forum;
import org.fpse.forum.Question;
import org.fpse.forum.TopicArea;
import org.fpse.store.impl.PostMeta;
import org.fpse.topic.Post;

/**
 * Created on Dec 13, 2006 4:19:21 PM by Ajay
 */
public interface Store {

    public void store(Forum forum) throws SQLException;

    public void store(TopicArea topicArea) throws SQLException;

    public void store(Question question, boolean saveQuestion) throws SQLException;

    public void store(Set<Post> posts) throws SQLException;

    public List<Question> load(TopicArea ta, Date date) throws SQLException;

    public Set<TopicArea> load(Forum forum) throws SQLException;

    public Question loadQuestion(TopicArea topicArea, String id) throws SQLException;

    public Set<Post> loadPost(Question question) throws SQLException;

    public void makeRead(String user, Post post) throws SQLException;

    public Set<PostMeta> loadUnread(String user, String forum, int max) throws SQLException;

    public void close();

    public void store(Post... posts) throws SQLException;

    public void makeRead(String user, PostMeta meta) throws SQLException;

    public void delete(Post... posts) throws SQLException;

    public String getMaxQuestionId(String name) throws SQLException;
}
