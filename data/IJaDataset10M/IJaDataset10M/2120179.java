package weblife.object;

import java.util.Vector;
import weblife.datamodel.NewsFeedComment;
import weblife.section.SectionComments;

/**
 * Objekt f�r Lesen und Speichern der Daten der Commentssection
 * @author Knoll
 *
 */
public class ObjectComments extends AbstractWeblifeObject implements ObjectInterface {

    private Vector vector_Comments = new Vector();

    private String commenterName;

    private String commenterThumb;

    private String commentableOwnerType;

    private String commentableOwnerName;

    private String commentTime;

    private String commenterId;

    private String text;

    private String id;

    private String success;

    /**
	 * 
	 * @param methode Methode des Aufrufs
	 * @param param Name des Parameters
	 * @param value Wert des Paramaters 
	 */
    public void parseParams(String methode, String param, String value) {
        if (param.equals("errorcode")) {
            errorcode = value;
            error = true;
        } else if (param.equals("errormessage")) {
            errormessage = value;
            error = true;
        } else if (methode.equals(SectionComments.METHODE_READCOMMENTS)) {
            if (param.equals("id")) id = value; else if (param.equals("commenterName")) commenterName = value; else if (param.equals("commenterThumb")) commenterThumb = value; else if (param.equals("commentTime")) commentTime = value; else if (param.equals("commenterId")) commenterId = value; else if (param.equals("text")) {
                text = value;
                NewsFeedComment comment = new NewsFeedComment(id, commenterName, commenterThumb, commentTime, commenterId, text);
                vector_Comments.addElement(comment);
            }
        } else if (methode.equals(SectionComments.METHODE_WRITECOMMENT)) {
            if (param.equals("success")) this.success = value;
        }
    }

    public Vector getVector_Comments() {
        return vector_Comments;
    }
}
