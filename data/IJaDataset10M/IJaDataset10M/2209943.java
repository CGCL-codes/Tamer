package tagcloud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import meco.Document;
import mecoDB.DocumentDB;
import tagcloud.datastructure.MecoTag;
import tagcloud.util.DbUtil;
import tagcloud.util.TagFrequency;
import util.StringUtils;
import database.DBAccess;
import database.DBConnections;

public class TagExtraction {

    private DBAccess dbaccess;

    public TagExtraction() {
        dbaccess = new DBAccess(DBConnections.MECO);
    }

    public static void main(String[] args) {
        TagExtraction tagExtraction = new TagExtraction();
        tagExtraction.cleanTables();
        tagExtraction.createTablesForExtractedTags();
    }

    private static final int MAX_TAG_PER_USER = 10;

    /**
	 * considered tags are tags that are term frequency high 
	 */
    private void generateUserTaggings() {
        Random random = new Random();
        TagCloudGeneration cloudGeneration = new TagCloudGeneration(null);
        List<TagFrequency> consideredTags = cloudGeneration.selectTags();
        createTablesForExtractedTags();
        List<String> users = selectUsers();
        int tagCounter = 0;
        int docCounter = 0;
        for (TagFrequency tag : consideredTags) {
            if (tagCounter == MAX_TAG_PER_USER) {
                tagCounter = 0;
                continue;
            }
            tagCounter++;
            for (Integer docId : tag.getTaggedDocuments().keySet()) {
                if (docCounter == 2) {
                    docCounter = 0;
                    break;
                }
                saveTagging(tag.getTagId(), docId, users.get(random.nextInt(users.size())));
                docCounter++;
            }
        }
    }

    private List<String> selectUsers() {
        dbaccess.Query = "select GuidUser from [Data].[User];";
        List<String> users = new ArrayList<String>();
        ResultSet rs = dbaccess.ExecQuery();
        try {
            while (rs != null && rs.next()) {
                users.add(rs.getString("guiduser"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void saveTagging(int tagId, int docId, String userId) {
        dbaccess.insertQuery = "insert into dbo.TagCloudTaggings(tag_id, doc_id, user_id) " + " values (" + tagId + "," + docId + ",'" + userId + "');";
        dbaccess.ExecInsert();
    }

    private void cleanTables() {
        dbaccess.Update = "delete from dbo.TagCloudDocumentTags;" + "delete from dbo.TagCloudTaggings;" + "delete from dbo.TagCloudTags;" + "drop TABLE  dbo.TagCloudDocumentTags;" + "drop TABLE dbo.TagCloudTaggings;drop TABLE dbo.TagCloudTags;";
        dbaccess.ExecUpdate();
    }

    private void extractTagsFromDocuments() {
        List<Document> documentsToParse = readDocuments();
        createTablesForExtractedTags();
        Set<String> savedTags = new HashSet<String>();
        for (Document doc : documentsToParse) {
            for (String tag : (Set<String>) doc.getTermFrequency().keySet()) {
                if (tag.length() > 1) {
                    tag = StringUtils.removeInvalidCharacteres(tag);
                }
                if (!savedTags.contains(tag)) {
                    saveTagWithFrequency(tag, 0);
                    savedTags.add(tag);
                }
                if (doc.getTermFrequency().get(tag) != null) {
                    saveTagDocumentRelation(tag, ((Integer) (doc.getTermFrequency().get(tag))).intValue(), doc.getDocumentId());
                }
            }
        }
        updateFinalTagFrequencies();
        generateUserTaggings();
    }

    public static void updateFinalTagFrequencies() {
        DbUtil.dbAccess.Query = "select tag_id, tag from dbo.TagCloudTags;";
        ResultSet rs = DbUtil.dbAccess.ExecQuery();
        List<Integer> indexes = new ArrayList<Integer>();
        try {
            while (rs != null && rs.next()) {
                indexes.add(rs.getInt("tag_id"));
            }
            for (Integer index : indexes) {
                DbUtil.dbAccess.Update = "update dbo.TagCloudTags set tag_frequency = (select sum(freq) from dbo.TagCloudDocumentTags where tag_id=" + index.intValue() + ")" + "where tag_id = " + index.intValue() + ";";
                DbUtil.dbAccess.ExecUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveTagDocumentRelation(String tag, int tagFreq, int documentId) {
        DbUtil.dbAccess.insertQuery = "insert into dbo.TagCloudDocumentTags " + "  SELECT tag_id,+" + documentId + "," + tagFreq + " from dbo.TagCloudTags where tag ='" + tag + "'";
        DbUtil.dbAccess.ExecInsert();
    }

    public static void saveTagWithFrequency(String tag, int frequency) {
        String newTag = StringUtils.removeInvalidCharacteres(tag).replace(".", " ").trim();
        if (newTag.length() > 50) {
            newTag = newTag.substring(0, 49);
        }
        DbUtil.dbAccess.insertQuery = "insert into dbo.TagCloudTags(tag, tag_frequency) " + " values ('" + newTag + "'," + frequency + ");";
        DbUtil.dbAccess.ExecInsert();
    }

    private void createTablesForExtractedTags() {
        dbaccess.Update = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[TagCloudTags]')" + "AND OBJECTPROPERTY(id, N'IsUserTable') = 1)" + "CREATE TABLE [dbo].[TagCloudTags] ( " + "tag_id int NOT NULL primary key identity(1,1)," + "tag nvarchar(50)," + "tag_frequency int);";
        dbaccess.ExecUpdate();
        dbaccess.Update = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[TagCloudDocumentTags]')" + "AND OBJECTPROPERTY(id, N'IsUserTable') = 1)" + "CREATE TABLE [dbo].[TagCloudDocumentTags] ( id int NOT NULL identity(1,1)," + "tag_id int not null," + "doc_id int not null," + "freq int," + "primary key(id)," + "constraint fkTags foreign key (tag_id) references [dbo].[TagCloudTags](tag_id)," + "constraint fkDocs foreign key (doc_id) references [Data].[Document](IdDocument));";
        dbaccess.ExecUpdate();
        dbaccess.Update = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[TagCloudTaggings]')" + "AND OBJECTPROPERTY(id, N'IsUserTable') = 1)" + "CREATE TABLE [dbo].[TagCloudTaggings] ( id int NOT NULL identity(1,1)," + "tag_id int not null," + "doc_id int not null," + "user_id uniqueidentifier not null," + "partioning int null," + "round int null," + "primary key(id)," + "constraint fkTagsId foreign key (tag_id) references [dbo].[TagCloudTags](tag_id)," + "constraint fkUsers foreign key (user_id) references [Data].[User](guiduser)," + "constraint fkDocsId foreign key (doc_id) references [Data].[Document](IdDocument));";
        dbaccess.ExecUpdate();
    }

    @SuppressWarnings("unchecked")
    private List<Document> readDocuments() {
        try {
            dbaccess.Query = "select top 200 IdDocument, Title, Description, Timestamp, Link, IdLuceneIndex " + " from data.document " + " where Description IS NOT NULL ";
            ResultSet rs = dbaccess.ExecQuery();
            DocumentDB documentDb = new DocumentDB();
            List<Document> documents = new ArrayList<Document>();
            while (rs != null && rs.next()) {
                documents.add(documentDb.getDocument(rs.getInt("IdDocument")));
            }
            return documents;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
