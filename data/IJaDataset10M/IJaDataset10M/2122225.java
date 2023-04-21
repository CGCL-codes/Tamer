package evaluation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import database.DBAccess;

public class TaggingGeneration {

    private static DBAccess dbAccess;

    private static final int P_CORE = 25;

    private static Set<Integer> usedDocuments;

    private static List<TagFrequency> tags;

    public TaggingGeneration() {
        dbAccess = new DBAccess();
    }

    public static void main(String[] args) {
        TaggingGeneration tagCloud = new TaggingGeneration();
        tagCloud.generateTagCloud();
    }

    public void generateTagCloud() {
    }

    public static TagFrequency getTagByLabel(String tagLabel) {
        return null;
    }

    public static List<TagFrequency> selectTags() {
        tags = new ArrayList<TagFrequency>();
        usedDocuments = new HashSet<Integer>();
        dbAccess = new DBAccess();
        dbAccess.Query = "select tag_id, tag, tag_frequency from dbo.TagCloudTags where tag_frequency >=" + P_CORE + ";";
        ResultSet rs = dbAccess.ExecQuery();
        try {
            while (rs != null && rs.next()) {
                tags.add(new TagFrequency(rs.getString("tag").toLowerCase(), rs.getInt("tag_frequency"), rs.getInt("tag_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Tag cloud will be generated from " + tags.size() + " tags");
        getTaggedDocuments(tags);
        return tags;
    }

    private List<TagFrequency> selectUserTags(String userId) {
        List<TagFrequency> userTags = new ArrayList<TagFrequency>();
        dbAccess = new DBAccess();
        dbAccess.Query = "select tag_id, tag, tag_frequency from dbo.TagCloudTags where tag_id in (select tag_id from dbo.TagCloudTaggings where user_id='" + userId + "');";
        ResultSet rs = dbAccess.ExecQuery();
        try {
            while (rs != null && rs.next()) {
                userTags.add(new TagFrequency(rs.getString("tag").toLowerCase(), rs.getInt("tag_frequency"), rs.getInt("tag_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getTaggedDocuments(userTags);
        return userTags;
    }

    /**
	 * @param tags
	 */
    private static void getTaggedDocuments(List<TagFrequency> tags) {
        for (TagFrequency tag : tags) {
            dbAccess.Query = "select doc_id, freq from dbo.TagCloudDocumentTags where tag_id =" + tag.getTagId() + ";";
            ResultSet rs = dbAccess.ExecQuery();
            HashMap<Integer, Integer> taggedDocuments = new HashMap<Integer, Integer>();
            try {
                while (rs != null && rs.next()) {
                    taggedDocuments.put(rs.getInt("doc_id"), rs.getInt("freq"));
                    if (!usedDocuments.contains(rs.getInt("doc_id"))) {
                        usedDocuments.add(rs.getInt("doc_id"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tag.setTaggedDocuments(taggedDocuments);
        }
    }

    private List<String> selectUsers() {
        dbAccess.Query = "select GuidUser from [Data].[User];";
        List<String> users = new ArrayList<String>();
        ResultSet rs = dbAccess.ExecQuery();
        try {
            while (rs != null && rs.next()) {
                users.add(rs.getString("guiduser"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
