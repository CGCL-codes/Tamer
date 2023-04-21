package si.unimb.isportal07.iiPoll.dbobj;

import java.util.Locale;
import java.util.Vector;
import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBField;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.core.misc.DateTime;
import si.unimb.isportal07.iiCategories.*;

public class Answer extends SecuredDBObject {

    private static final long serialVersionUID = 3690474740435399734L;

    private String thisClass = new String(getClass().getName() + ".");

    public static final String ANSWER_ID = "AnswerID";

    public static final String POLL_ID = "PollID";

    public static final String TEXT = "Text";

    public static final String VOTES_NUMBER = "VotesNr";

    public Answer() throws DBException {
        super();
    }

    public Answer(DBConnection myConnection) throws DBException {
        super(myConnection);
    }

    public void delete() throws DBException {
        super.delete();
    }

    public void add() throws DBException {
        String myName = new String(thisClass + "add()");
        super.add();
    }

    public void add(int Uid) throws DBException {
        String myName = new String(thisClass + "add()");
        super.add();
    }

    public void update() throws DBException {
        String myName = new String(thisClass + "update()");
        super.update();
    }

    public DBObject getThisDBObj() throws DBException {
        return new Answer();
    }

    public void setupFields() throws DBException {
        setTargetTable("isportal07_iiPoll_Answer");
        setDescription(this.getClass().getName() + " table");
        setCharset("utf-8");
        addField(Answer.ANSWER_ID, DBField.AUTOINC_TYPE, 0, false, "Identificator");
        addField(Answer.POLL_ID, DBField.VARCHAR_TYPE, 10, false, "IdentificatorPoll");
        addField(Answer.TEXT, DBField.VARCHAR_TYPE, 100, false, "Text");
        addField(Answer.VOTES_NUMBER, DBField.INT_TYPE, 0, true, "Number_of_votes");
        addKey(Answer.ANSWER_ID);
        setReadOnly(Answer.ANSWER_ID);
        setDefaultValue(Answer.TEXT, "Add answer here");
        addSortKey(Answer.ANSWER_ID, false);
        addDetail(Voting.class.getName(), Answer.ANSWER_ID, Voting.ID_ANSWER);
    }
}
