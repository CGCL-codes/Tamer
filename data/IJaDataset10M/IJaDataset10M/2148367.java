package tagcloud.evaluation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Document;
import model.MecoUser;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import tagcloud.TagExtraction;
import tagcloud.datastructure.MecoTag;
import com.icesoft.faces.context.effects.JavascriptContext;
import database.DBAccess;
import database.DocumentDB;
import database.MecoUserDB;

/**
 * @author Martin Leginus
 */
@Name("userTaggingEvaluationAction")
@Scope(ScopeType.SESSION)
public class UserTaggingEvaluation {

    private String name;

    private String description;

    private String userName;

    private boolean generatedTagsEvaluationRender = true, userTagsEvaluationRender = false;

    public void openDocumentLink(String documentLink) {
        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "window.open('" + documentLink + "', 'Document Window');");
    }

    public MecoUser getMecoUser() {
        MecoUser mecoUser = null;
        if (this.getUserName() != null && !this.getUserName().isEmpty()) {
            mecoUser = MecoUserDB.getInstance().getUserByName(this.getUserName());
        }
        return mecoUser;
    }

    private List<Document> documents;

    public void signUpUser() {
        MecoUserDB mecoUserDB = MecoUserDB.getInstance();
        MecoUser user = mecoUserDB.getUserByName(this.getUserName());
        if (user == null) {
            mecoUserDB.createUser(user);
            FacesMessage facesMessage = new FacesMessage("User created and logged!");
            FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
        } else {
            FacesMessage facesMessage = new FacesMessage("User logged!");
            FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
        }
        loadDocuments();
        createEvaluationTable();
    }

    private void loadDocuments() {
        createEvaluationTable();
        documents = DocumentDB.getInstance().getDocuments(false, true);
    }

    public void saveGeneratedTagsEvaluation() {
        MecoUser mecoUser = this.getMecoUser();
        System.out.println("Evaluation form was created");
        for (Document doc : documents) {
            for (MecoTag tag : doc.getTagsOfDocument()) {
                saveUserTaggingRelation(doc, tag, mecoUser);
            }
        }
        FacesMessage facesMessage = new FacesMessage("First round of evaluation is finished! Please proceed with your assignment of tags.");
        FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
        loadDocuments();
        this.setGeneratedTagsEvaluationRender(false);
        this.setUserTagsEvaluationRender(true);
    }

    public void saveUserTagsEvaluation() {
        MecoUser mecoUser = this.getMecoUser();
        System.out.println("Evaluation form was created - users");
        for (Document doc : documents) {
            for (String tag : doc.getUserTagsOfDocument()) {
                saveUserTaggingRelation(doc, tag, mecoUser);
            }
        }
        FacesMessage facesMessage = new FacesMessage("Evaluation is finished! Thank you for you time and help.");
        FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
        this.setUserTagsEvaluationRender(false);
    }

    private void saveUserTaggingRelation(Document doc, MecoTag tag, MecoUser mecoUser) {
        if (tag != null && doc.areTagsOfDocumentEvaluated()) {
            DBAccess.getInstance().insertQuery = "insert into [dbo].[UserTaggingEvaluation](idDocument,idTag,guiduser) values (" + doc.getDocumentId() + ",'" + tag.getTagId() + "','" + mecoUser.getGuid() + "')";
            DBAccess.getInstance().ExecInsert();
        }
    }

    private void saveUserTaggingRelation(Document doc, String tag, MecoUser mecoUser) {
        int idTag = TagExtraction.saveUserTag(tag);
        DBAccess.getInstance().insertQuery = "insert into [dbo].[UserTaggingEvaluation](idDocument,idTag,isUserTag,guiduser) values (" + doc.getDocumentId() + ",'" + idTag + "','1','" + mecoUser.getGuid() + "')";
        DBAccess.getInstance().ExecInsert();
    }

    public void removeAllTags(Document document) {
        if (!document.areTagsOfDocumentEvaluated()) {
            document.setTagsOfDocumentEvaluated(true);
        }
        for (Document doc : documents) {
            if (doc.getDocumentId() == document.getDocumentId()) {
                doc.setTagsOfDocument(Collections.EMPTY_LIST);
            }
        }
        FacesMessage facesMessage = new FacesMessage("All tags were removed.");
        FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
    }

    public void removeTag(MecoTag removedTag, Document document) {
        if (!document.areTagsOfDocumentEvaluated()) {
            document.setTagsOfDocumentEvaluated(true);
        }
        System.out.println(removedTag.getTagLabel());
        List<MecoTag> tagsOfDocument = document.getTagsOfDocument();
        tagsOfDocument.remove(removedTag);
        for (Document doc : documents) {
            if (doc.getDocumentId() == document.getDocumentId()) {
                doc.setTagsOfDocument(tagsOfDocument);
            }
        }
        FacesMessage facesMessage = new FacesMessage("Tag" + removedTag.getTagLabel() + " was removed.");
        FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
    }

    public void removeUserTag(String removedTag, Document document) {
        System.out.println(removedTag);
        List<String> tagsOfDocument = document.getUserTagsOfDocument();
        tagsOfDocument.remove(removedTag);
        for (Document doc : documents) {
            if (doc.getDocumentId() == document.getDocumentId()) {
                doc.setUserTagsOfDocument(tagsOfDocument);
            }
        }
        FacesMessage facesMessage = new FacesMessage("Tag" + removedTag + " was removed.");
        FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
    }

    private void createEvaluationTable() {
        String query = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[UserTaggingEvaluation]')" + "AND OBJECTPROPERTY(id, N'IsUserTable') = 1)" + "CREATE TABLE [dbo].[UserTaggingEvaluation] ( " + "id int NOT NULL primary key identity ," + "idDocument int not null," + "idTag int not null," + "isUserTag bit default 0," + "guiduser uniqueidentifier not null);";
        PreparedStatement ps = DBAccess.getInstance().prepareStatement(query);
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        query = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[UserTagEvaluation]')" + "AND OBJECTPROPERTY(id, N'IsUserTable') = 1)" + "CREATE TABLE [dbo].[UserTagEvaluation] ( " + "idTag int NOT NULL primary key identity ," + "tag varchar(25) not null);";
        ps = DBAccess.getInstance().prepareStatement(query);
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
    }

    public void addTags(Document selectedDocument) {
        System.out.println("Add tag method was called");
        FacesMessage facesMessage;
        String[] userTags = selectedDocument.getUserTags().split(",");
        List<String> tmp = selectedDocument.getUserTagsOfDocument();
        for (String userTag : userTags) {
            if (userTag.length() > 2) {
                System.out.println("This tag was added " + userTag);
                tmp.add(userTag);
                facesMessage = new FacesMessage("Tag " + userTag + " was added.");
            } else {
                facesMessage = new FacesMessage(" Entered tag is incorrect.");
            }
            FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
        }
        selectedDocument.setUserTagsOfDocument(tmp);
        selectedDocument.setUserTags("");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isGeneratedTagsEvaluationRender() {
        return generatedTagsEvaluationRender;
    }

    public void setGeneratedTagsEvaluationRender(boolean generatedTagsEvaluation) {
        this.generatedTagsEvaluationRender = generatedTagsEvaluation;
    }

    public boolean isUserTagsEvaluationRender() {
        return userTagsEvaluationRender;
    }

    public void setUserTagsEvaluationRender(boolean userTagsEvaluation) {
        this.userTagsEvaluationRender = userTagsEvaluation;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
