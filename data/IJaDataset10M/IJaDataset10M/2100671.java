package ru.yep.forum.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import ru.yep.forum.events.NewTopicEvent;
import ru.yep.forum.utils.ForumUtil.URLs;

/**
 * 
 * Topic and parts manager and consistency controller
 * @author Oleg Orlov
 */
public class Topics {

    public static final int ID_ROOT_PART = -1;

    public static final int ID_BOARD_PART = 0;

    private static final Topics instance = new Topics();

    public static Topics getDefault() {
        return instance;
    }

    /** (int)partId -> part*/
    private Map partsByIdMap = new HashMap();

    /** (int)partId -> part*/
    private Map subPartsByParentIdMap = new HashMap();

    /** (int)topicId -> topic */
    private Map topicsByIdMap = new HashMap();

    /** [(int)topicId->ArrayList]  */
    private ConcurrentHashMap lastCommentsMap = new ConcurrentHashMap();

    /** Integer-> Topic[]     */
    private Map topicsByPartIdMap = new ConcurrentHashMap();

    private int lastestTopicId = 0;

    private final Part ROOT_PART = new Part(-1, "Главный", "", -2) {

        public int getDeep() {
            return 0;
        }

        public String toString() {
            return "Root part";
        }

        public boolean isChildOf(Part superPart) {
            return false;
        }
    };

    String getURI(Topic topic) {
        return URLs.TOPIC_PAGE + "?id=" + topic.id;
    }

    String getURI(Comment comment) {
        return URLs.TOPIC_PAGE + "?id=" + comment.getTopic().getId() + "#comment" + comment.getId();
    }

    String getURI(Part part) {
        return URLs.PART_PAGE + "?id=" + part.getId();
    }

    public Topic[] getAllTopics() {
        return (Topic[]) topicsByIdMap.values().toArray(new Topic[0]);
    }

    public Topic getTopicById(int topicId) {
        return (Topic) topicsByIdMap.get(Integer.valueOf(topicId));
    }

    public Topic getTopicById(String topic_id) {
        Topic topic = (Topic) topicsByIdMap.get(Integer.valueOf(topic_id));
        return topic;
    }

    public void init() {
        try {
            Topics.getDefault().updateTopics();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateTopics() throws SQLException {
        Statement partStatement = Engine.getDefault().getDbHelper().createStatement();
        ResultSet partRs = partStatement.executeQuery("select * from part");
        partsByIdMap.clear();
        partsByIdMap.put(Integer.valueOf(ID_ROOT_PART), ROOT_PART);
        topicsByPartIdMap.clear();
        Part boardPart = new Part(ID_BOARD_PART, "ДОСКА ОБЪЯВЛЕНИЙ", "ВАЖНОЕ", ID_ROOT_PART);
        partsByIdMap.put(Integer.valueOf(ID_BOARD_PART), boardPart);
        while (partRs.next()) {
            int id = partRs.getInt("id");
            String name = partRs.getString("name");
            String description = partRs.getString("description");
            String parentId = partRs.getString("path");
            Part part = new Part(id, name, description, parentId != null ? Integer.parseInt(parentId) : ID_ROOT_PART);
            partsByIdMap.put(Integer.valueOf(id), part);
        }
        Statement statement = Engine.getDefault().getDbHelper().createStatement();
        ResultSet rs = statement.executeQuery("SELECT t.*, COUNT(c.id) as comcount FROM themes t LEFT JOIN comments c ON t.id = c.theme_id GROUP BY c.theme_id order by t.id");
        topicsByIdMap.clear();
        lastestTopicId = 0;
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String part_id = rs.getString("part_id");
            int user_id = rs.getInt("user_id");
            String desc = rs.getString("description");
            Date sqlDate = rs.getTimestamp("timestamp");
            int commentsCount = rs.getInt("comcount");
            int viewsCount = rs.getInt("views");
            Topic topic = new Topic(id, name, part_id, user_id, desc, sqlDate, rs.getInt("severity"), commentsCount, viewsCount);
            topicsByIdMap.put(Integer.valueOf(id), topic);
            ArrayList topics = (ArrayList) topicsByPartIdMap.get(Integer.valueOf(part_id));
            if (topics == null) {
                topics = new ArrayList();
                topicsByPartIdMap.put(Integer.valueOf(part_id), topics);
            }
            topics.add(topic);
        }
        linkParts();
        updateLastestTopicId();
        statement = Engine.getDefault().getDbHelper().createStatement();
        rs = statement.executeQuery("SELECT t.id,c.* FROM themes t, comments c where t.id=c.theme_id order by t.id,c.id DESC");
        ArrayList commentsList = new ArrayList();
        Topic currentTopic = null;
        lastCommentsMap.clear();
        while (rs.next()) {
            int topicId = rs.getInt("t.id");
            if (currentTopic == null || topicId != currentTopic.getId()) {
                currentTopic = getTopicById(topicId);
                commentsList = new ArrayList();
                lastCommentsMap.put(Integer.valueOf(currentTopic.getId()), commentsList);
            }
            if (commentsList.size() > 3) continue;
            int commentId = rs.getInt("c.id");
            String themeId = rs.getString("theme_id");
            String userId = rs.getString("user_id");
            Date sqlDate = rs.getTimestamp("timestamp");
            String text = rs.getString("text");
            User user = Users.getDefault().getUser(Integer.valueOf(userId).intValue());
            Comment comment = new Comment(commentId, themeId, user, sqlDate, text);
            commentsList.add(comment);
        }
    }

    private void updateLastestTopicId() throws SQLException {
        Statement statement = Engine.getDefault().getDbHelper().createStatement();
        ResultSet rs = statement.executeQuery("SELECT MAX(id) as maxid from themes");
        lastestTopicId = 0;
        while (rs.next()) {
            int id = rs.getInt("maxid");
            lastestTopicId = id;
        }
    }

    private void linkParts() {
        subPartsByParentIdMap.clear();
        for (Iterator itPart = partsByIdMap.values().iterator(); itPart.hasNext(); ) {
            Part part = (Part) itPart.next();
            if (part == ROOT_PART) continue;
            Part parentPart = (Part) partsByIdMap.get(Integer.valueOf(part.getParentPartId()));
            if (parentPart == null) parentPart = ROOT_PART;
            HashSet subParts = (HashSet) subPartsByParentIdMap.get(Integer.valueOf(parentPart.getId()));
            if (subParts == null) {
                subParts = new HashSet();
                subPartsByParentIdMap.put(Integer.valueOf(parentPart.getId()), subParts);
            }
            subParts.add(part);
        }
    }

    /**
     * @return last comments for topic sorted by age
     */
    public Comment[] getLastComments(Topic topic) {
        ArrayList commentsList = (ArrayList) lastCommentsMap.get(Integer.valueOf(topic.getId()));
        if (commentsList == null || commentsList.size() == 0) return new Comment[0]; else return (Comment[]) commentsList.toArray(new Comment[0]);
    }

    public Topic addNewTopic(String name, String part_id, User user, int severity, String desc, String message) throws SQLException {
        Statement statement = Engine.getDefault().getDbHelper().createStatement();
        String sql = "insert into themes (name,part_id,user_id,severity,description,timestamp)" + " values('" + name + "','" + part_id.trim() + "','" + user.getId() + "','" + severity + "','" + desc + "',NOW())";
        statement.executeUpdate(sql);
        updateLastestTopicId();
        String sqlComment = "insert into comments (theme_id,user_id,text,timestamp)" + " values('" + lastestTopicId + "','" + user.getId() + "','" + message + "',NOW())";
        statement.executeUpdate(sqlComment);
        updateTopics();
        final Topic newTopic = (Topic) topicsByIdMap.get(Integer.valueOf(lastestTopicId));
        EventSystem.getDefault().publishEvent(new NewTopicEvent(newTopic, user));
        return newTopic;
    }

    public Topic addNewTopic(String name, String part_id, User user, int severity) throws SQLException {
        Statement statement = Engine.getDefault().getDbHelper().createStatement();
        String sql = "insert into themes (name,part_id,user_id,severity,timestamp)" + " values('" + name + "','" + part_id.trim() + "','" + user.getId() + "','" + severity + "',NOW())";
        statement.executeUpdate(sql);
        updateTopics();
        final Topic newTopic = (Topic) topicsByIdMap.get(Integer.valueOf(lastestTopicId));
        return newTopic;
    }

    public String getPartName(String part_id) {
        Part part = (Part) partsByIdMap.get(Integer.valueOf(part_id));
        return part.description;
    }

    public Part getPart(String part_id) {
        Part part = (Part) partsByIdMap.get(Integer.valueOf(part_id));
        return part;
    }

    public void addNewPart(String name, String description, Part superPart) throws SQLException {
        Statement statement = Engine.getDefault().getDbHelper().createStatement();
        String sql = "insert into part (name,description,path)" + " values('" + name + "','" + description + "','" + (superPart != null ? superPart.id : Topics.ID_ROOT_PART) + "')";
        statement.executeUpdate(sql);
        updateTopics();
    }

    private static final Comparator topicComparator = new Comparator() {

        public int compare(Object arg0, Object arg1) {
            Topic t0 = (Topic) arg0;
            Topic t1 = (Topic) arg1;
            return t0.timeStamp.before(t1.timeStamp) ? -1 : 0;
        }
    };

    /**
     * @return top level parts without mainBoardPart 
     */
    public Part[] getTopLevelParts() {
        HashSet topParts = (HashSet) subPartsByParentIdMap.get(Integer.valueOf(ID_ROOT_PART));
        for (Iterator itPart = topParts.iterator(); itPart.hasNext(); ) {
            Part name = (Part) itPart.next();
            if (name.getId() == ID_BOARD_PART) itPart.remove();
        }
        return (Part[]) topParts.toArray(new Part[0]);
    }

    public Part[] getSubParts(Part parent) {
        HashSet subParts = (HashSet) subPartsByParentIdMap.get(Integer.valueOf(parent.id));
        if (subParts == null) return new Part[0];
        return (Part[]) subParts.toArray(new Part[0]);
    }

    public Part[] getAllPartsExcluding(Part part) {
        Part[] topParts = getTopLevelParts();
        Part topLevelParent = getTopParent(part);
        ArrayList result = new ArrayList();
        for (int i = 0; i < topParts.length; i++) {
            if (topParts[i] == topLevelParent) continue;
            result.addAll(Arrays.asList(getAllSubParts(topParts[i])));
        }
        return (Part[]) result.toArray(new Part[0]);
    }

    /**
     * @return all parts sorted by inheritance including ROOT
     */
    public Part[] getSortedParts() {
        ArrayList allParts = new ArrayList();
        allParts.add(ROOT_PART);
        allParts.addAll(Arrays.asList(getAllSubParts(ROOT_PART)));
        return (Part[]) allParts.toArray(new Part[0]);
    }

    public Part[] getAllSubParts(Part part) {
        ArrayList result = new ArrayList();
        Part[] subParts = part.getSubParts();
        for (int i = 0; i < subParts.length; i++) {
            result.add(subParts[i]);
            Part[] allSubParts = getAllSubParts(subParts[i]);
            result.addAll(Arrays.asList(allSubParts));
        }
        return (Part[]) result.toArray(new Part[0]);
    }

    /** walks trough parent's hierarchy to root_part 
     * @return topLevel parent */
    private Part getTopParent(Part part) {
        int deep = 0;
        Part parentPart;
        for (parentPart = getParentPart(part); parentPart.getParentPartId() != ID_ROOT_PART; deep++) {
            parentPart = getParentPart(parentPart);
        }
        return parentPart;
    }

    /** @return parent of part*/
    public Part getParentPart(Part part) {
        Part parentPart = (Part) partsByIdMap.get(Integer.valueOf(part.getParentPartId()));
        return parentPart;
    }

    public Part[] getAllParts() {
        return (Part[]) partsByIdMap.values().toArray(new Part[0]);
    }

    public Part getPart(int partId) {
        return (Part) partsByIdMap.get(Integer.valueOf(partId));
    }

    private static final Comparator TOPICS_RELATIVE_COMPARATOR = new Comparator() {

        public int compare(Object arg0, Object arg1) {
            Topic topic0 = (Topic) arg0;
            Topic topic1 = (Topic) arg1;
            long result = topic1.getLastUpdate() - topic0.getLastUpdate();
            if (result != 0) return result > 0 ? 1 : -1;
            return topic1.getId() - topic0.getId();
        }
    };

    public Topic[] getTopics(Part part) {
        ArrayList topicsList = (ArrayList) topicsByPartIdMap.get(Integer.valueOf(part.id));
        if (topicsList == null) return new Topic[0];
        Topic[] result = (Topic[]) topicsList.toArray(new Topic[0]);
        Arrays.sort(result, topicComparator);
        return result;
    }

    public Topic[] getTopicsSorted(Part part) {
        TreeSet sortedSet = new TreeSet(TOPICS_RELATIVE_COMPARATOR);
        ArrayList topicsList = (ArrayList) topicsByPartIdMap.get(Integer.valueOf(part.id));
        if (topicsList == null) return new Topic[0];
        sortedSet.addAll(topicsList);
        Topic[] result = (Topic[]) sortedSet.toArray(new Topic[0]);
        return result;
    }

    public void movePartTo(Part part, Part toPart) throws SQLException {
        Statement statement = Engine.getDefault().getDbHelper().createStatement();
        String sql = "update part set path='" + toPart.getId() + "' where id='" + part.getId() + "'";
        statement.execute(sql);
        part.setParentPartId(toPart.getId());
        linkParts();
    }

    private final Map topicViewsMap = new ConcurrentHashMap();

    public int getViewsCount(Topic topic) {
        Integer viewsCount = (Integer) topicViewsMap.get(Integer.valueOf(topic.getId()));
        if (viewsCount == null) return 0;
        return viewsCount.intValue();
    }

    public void topicViewed(Topic topic) throws SQLException {
        int newViewsCount = topic.getViewsCount() + 1;
        Statement statement = Engine.getDefault().getDbHelper().createStatement();
        String sql = "update themes set views =" + newViewsCount + " where id=" + topic.getId();
        statement.executeUpdate(sql);
        topic.setViewsCount(newViewsCount);
    }
}
