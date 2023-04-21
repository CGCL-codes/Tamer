package org.mbari.vars.knowledgebase.model;

import vars.knowledgebase.IHistory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mbari.vars.dao.IDataObject;
import vars.IUserAccount;
import vars.knowledgebase.IConceptDelegate;

/**
 * A <code>History</code> object represents a single modification to the
 * <code>KnowledgeBase<code>. Since it is necessary to act upon
 * <code>History</code> objects with only a String representation of the
 * <code>History</code>, this class contains code for converting a
 * <code>History</code> to and from a String representation.
 *
 * @author Monterey Bay Aquarium Research Institute 2003
 * @created August 17, 2004
 * @stereotype description
 */
public class History implements IDataObject, Cloneable, java.io.Serializable, IHistory {

    /**
     *
     */
    private static final long serialVersionUID = 7812520655911398045L;

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(History.class);

    private static final Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * DB detail that has leaked into this layer.
     */
    private static final int MAX_USERNAME_LEN = 50;

    /**
	 * @uml.property  name="_toStringBuf"
	 */
    private transient StringBuffer _toStringBuf;

    /**
	 * @uml.property  name="action"
	 */
    private String action;

    /**
	 * The time of approval of this <code>History</code>.
	 * @uml.property  name="approvalDate"
	 */
    private Date approvalDate;

    /**
	 * @uml.property  name="approverName"
	 */
    private String approverName;

    /**
	 * @uml.property  name="comment"
	 */
    private String comment;

    /**
	 * Castor needs this link
	 * @uml.property  name="conceptDelegate"
	 * @uml.associationEnd  inverse="historySet:org.mbari.vars.knowledgebase.model.ConceptDelegate"
	 */
    private ConceptDelegate conceptDelegate;

    /**
	 * The time of creation for this <code>History</code>.
	 * @uml.property  name="creationDate"
	 */
    private Date creationDate;

    /**
	 * @uml.property  name="field"
	 */
    private String field;

    /**
	 * The Rdb database HistoryID of this <code>History</code>
	 * @uml.property  name="id"
	 */
    private Long id;

    /**
	 * @uml.property  name="newValue"
	 */
    private String newValue;

    /**
	 * @uml.property  name="oldValue"
	 */
    private String oldValue;

    /**
	 * @uml.property  name="rejected"
	 */
    private boolean rejected;

    /**
	 * The timeStamp value. Used for Long Transactions in Castor.
	 * @uml.property  name="timeStamp"
	 */
    private transient long timeStamp = 0;

    /**
	 * @uml.property  name="toStringBuf"
	 */
    private transient StringBuffer toStringBuf;

    /**
	 * The name of the user that created this <code>History</code>.
	 * @uml.property  name="creatorName"
	 */
    private String creatorName;

    /**
     * Default Public Contructor required by Castor. Warning: This call returns
     * a <code>Histroy</code> object in an unusable state. Use the
     * <code>History</code> factory method <code>create()</code> to create a
     * <code>History</code>.
     */
    public History() {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        creationDate = calendar.getTime();
        approvalDate = NOT_APPROVED;
    }

    /**
     * Constructs ...
     *
     *
     * @param history
     */
    public History(History history) {
        setAction(history.getAction());
        setCreationDate(new Date());
        setField(history.getField());
        setOldValue(history.getOldValue());
        setNewValue(history.getNewValue());
        setComment(history.getComment());
    }

    /**
     * Creates a new <code>History</code> object using the specified
     * description and information from the current <code>User</code>.
     *
     *
     * @param userAccount
     * @param action
     *            The action of this <code>History</code>.
     * @param fieldName
     *            The field name on which the action occurs.
     * @param value
     *            The field name's value for the action.
     * @param prevValue
     *            The field name's value before the action.
     */
    public History(IUserAccount userAccount, String action, String fieldName, String oldValue, String newValue) {
        this();
        if (userAccount == null) {
            creatorName = "Unknown";
        } else {
            creatorName = userAccount.getUserName();
        }
        setAction(action);
        setField(fieldName);
        setOldValue(oldValue);
        setNewValue(newValue);
    }

    /**
     * Used by the hashCode and compareTo
     */
    private String keyRepresentation() {
        if (_toStringBuf == null) {
            _toStringBuf = new StringBuffer();
        } else {
            _toStringBuf.delete(0, _toStringBuf.length());
        }
        if (id == null || id == 0) {
            _toStringBuf.append("Buisness Key [");
            _toStringBuf.append((creationDate != null) ? creationDate.getTime() : Long.MIN_VALUE).append(" ");
            _toStringBuf.append(creatorName).append(" ");
            _toStringBuf.append(action).append(" ");
            _toStringBuf.append(field).append(" ");
            _toStringBuf.append(oldValue).append(" >> ");
            _toStringBuf.append(newValue).append("]");
        } else {
            _toStringBuf.append("Database Key [").append(id).append("]");
        }
        return _toStringBuf.toString();
    }

    /**
     * Approves this <code>History</code> using the current GMT time.
     *
     * @param userAccount The UserAccount being used to approve the change
     * represented by the History object.
     *
     * @throws IllegalStateException
     * @deprecated used ApproveHistoryTask instead.
     */
    public void approve(IUserAccount userAccount) throws IllegalStateException {
        if (isApproved()) {
            if (log.isInfoEnabled()) {
                log.info("Trying to approve a history that's already been approved");
            }
        }
        if (!userAccount.isAdmin()) {
            throw new IllegalStateException(userAccount.getUserName() + " does not have admin rights");
        }
        calendar.setTime(new Date());
        this.approvalDate = calendar.getTime();
    }

    /**
     * Create and return a deep copy of this <code>History</code>.
     *
     * @return A deep copy of this <code>History</code>.
     * @exception CloneNotSupportedException
     *                Description of the Exception
     */
    public Object clone() throws CloneNotSupportedException {
        History history = (History) super.clone();
        history.setCreatorName(this.creatorName);
        history.setCreationDate(this.creationDate);
        history.setApprovalDate(this.approvalDate);
        return history;
    }

    /**
     * Description of the Method
     *
     * @param object
     *            Description of the Parameter
     * @return Description of the Return Value
     *
     * @throws ClassCastException
     */
    public int compareTo(final Object object) throws ClassCastException {
        if (!(object instanceof History)) {
            throw new ClassCastException("History.compareTo");
        }
        int c = 0;
        if (this != object) {
            History that = (History) object;
            String thatString = that.keyRepresentation();
            String thisString = this.keyRepresentation();
            c = thisString.compareTo(thatString);
        }
        return c;
    }

    /**
     * Creates a new <code>History</code> object using the specified
     * description and information from the current <code>User</code>.
     *
     * @param action
     *            The <code>Action</code> of this <code>History</code>.
     * @param fieldName
     *            The field name on which the action occurs.
     * @param value
     *            The field name's value for the action.
     * @return A new <code>History</code> object.
     *
     * @deprecated use HistoryFactory instead
     */
    public static History create(String action, String fieldName, String value) {
        return create(null, action, fieldName, value, null);
    }

    /**
     * Creates a new <code>History</code> object using the specified
     * description and information from the current <code>User</code>.
     *
     * @param action
     *            The <code>Action</code> of this <code>History</code>.
     * @param fieldName
     *            The field name on which the action occurs.
     * @param value
     *            The field name's value for the action.
     * @param prevValue
     *            The field name's value before the action.
     * @return A new <code>History</code> object.
     *
     * @deprecated use HistoryFactory instead
     */
    public static History create(String action, String fieldName, String value, String prevValue) {
        return new History(null, action, fieldName, value, prevValue);
    }

    /**
     * Method description
     *
     *
     * @param userAccount
     * @param action
     * @param fieldName
     * @param newValue
     * @param oldValue
     *
     * @return
     * @deprecated use HistoryFactory instead
     */
    public static History create(IUserAccount userAccount, String action, String fieldName, String newValue, String oldValue) {
        return new History(userAccount, action, fieldName, newValue, oldValue);
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @param object
     *
     * @return
     */
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (object == this) {
            isEqual = true;
        }
        if ((object != null) || (object.getClass() == this.getClass())) {
            isEqual = this.compareTo(object) == 0;
        }
        return isEqual;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    public int hashCode() {
        return keyRepresentation().hashCode();
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public long jdoGetTimeStamp() {
        return timeStamp;
    }

    /**
     * Description of the Method
     *
     * @param p1
     *            Description of the Parameter
     */
    public void jdoSetTimeStamp(long p1) {
        timeStamp = p1;
    }

    /**
     * Rejects this <code>History</code>. Rejection appends a rejection
     * string to the <code>History</code> description and then approvs the
     * history. Whomever calls this method should ensure the necessary steps are
     * taken to "undo" previous <code>History</code> action if appropriate.
     *
     * @deprecated use setRejected instead
     */
    public void reject() {
        setRejected(true);
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return
     */
    public String toString() {
        if (toStringBuf == null) {
            toStringBuf = new StringBuffer();
        } else {
            toStringBuf.delete(0, toStringBuf.length());
        }
        toStringBuf.append("HISTORY [");
        toStringBuf.append((creationDate != null) ? creationDate.getTime() : Long.MIN_VALUE).append(" ");
        toStringBuf.append(creatorName).append(" ");
        toStringBuf.append(action).append(" ");
        toStringBuf.append(field).append(" ");
        toStringBuf.append(oldValue).append(" >> ");
        toStringBuf.append(newValue).append("]");
        return toStringBuf.toString();
    }

    public String stringValue() {
        StringBuffer sb = new StringBuffer("[").append(DATE_FORMAT.format(creationDate));
        sb.append(" by ").append(creatorName).append("] ").append(action).append(" ").append(field);
        final String newVal = (newValue == null) ? "" : newValue;
        final String oldVal = (oldValue == null) ? "" : oldValue;
        if (ACTION_ADD.equals(action)) {
            sb.append(" '").append(newVal).append("'");
        } else if (ACTION_DELETE.equals(action)) {
            sb.append(" '").append(oldVal).append("'");
        } else if (ACTION_REPLACE.equals(action)) {
            sb.append(" '").append(oldVal).append("' with '").append(newVal).append("'");
        }
        return sb.toString();
    }

    /**
	 * Gets the action of this <code>History</code> as a String.
	 * @return  The action of this <code>History</code>.
	 * @uml.property  name="action"
	 */
    public String getAction() {
        return action;
    }

    /**
	 * Gets the approval date of this <code>History</code>.
	 * @return  The approval date of this <code>History</code>.
	 * @uml.property  name="approvalDate"
	 */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
	 * Method description
	 * @return
	 * @uml.property  name="comment"
	 */
    public String getComment() {
        return comment;
    }

    /**
     * Not for Developer use. This is required for Castor/DAO
     * @return  The conceptDelegate value
     * @uml.property  name="conceptDelegate"
     */
    public IConceptDelegate getConceptDelegate() {
        return this.conceptDelegate;
    }

    /**
     * Gets the creation date of this <code>History</code>.
     *
     * @return The creation date of this <code>History</code>.
     */
    public Date getCreationDate() {
        return new Date(creationDate.getTime());
    }

    /**
	 * Method description
	 * @return
	 * @uml.property  name="field"
	 */
    public String getField() {
        return field;
    }

    /**
     * @return  The Rdb database HistoryID of this <code>History</code>.
     * @uml.property  name="id"
     */
    public Long getId() {
        return id;
    }

    /**
	 * Method description
	 * @return
	 * @uml.property  name="newValue"
	 */
    public String getNewValue() {
        return newValue;
    }

    /**
	 * Method description
	 * @return
	 * @uml.property  name="oldValue"
	 */
    public String getOldValue() {
        return oldValue;
    }

    /**
	 * Gets the previous value of the field name from the description of this <code>History</code>. This value represents the value of the field name before the action occurred.
	 * @return  The previous value of the field name from the description of this  <code>History</code>.
	 * @uml.property  name="creatorName"
	 */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * Determines whether the action of this <code>History</code> is an add.
     *
     * @return <code>true</code> if the action of this <code>History</code>
     *         is an add.
     */
    public boolean isAdd() {
        return ACTION_ADD.equalsIgnoreCase(action);
    }

    /**
     * Gets whether this <code>History</code> has been approved.
     *
     * NOTE: Changes that are not approved are stored in the database with an
     * approvalDtg == 0 (ie 1/1/1970)
     *
     * @return <code>true</code> if this <code>History</code> has been
     *         approved.
     */
    public boolean isApproved() {
        return approvalDate != null;
    }

    /**
     * Determines whether the action of this <code>History</code> is a delete.
     *
     * @return <code>true</code> if the action of this <code>History</code>
     *         is a delete.
     */
    public boolean isDelete() {
        return ACTION_DELETE.equals(action);
    }

    /**
     * Determines whether this <code>History</code> was rejected.
     *
     * @return <code>true</code> if this <code>History</code> was rejected.
     */
    public boolean isRejected() {
        return rejected;
    }

    /**
	 * @return
	 * @uml.property  name="rejected"
	 */
    public boolean getRejected() {
        return rejected;
    }

    /**
     * Determines whether the action of this <code>History</code> is a
     * replace.
     *
     * @return <code>true</code> if the action of this <code>History</code>
     *         is an replace.
     */
    public boolean isReplace() {
        return ACTION_REPLACE.equalsIgnoreCase(action);
    }

    /**
	 * Method description
	 * @param  action
	 * @uml.property  name="action"
	 */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Sets the approval Date this <code>History</code>. DO NOT CALL THIS DIRECTLY! USE <code>approve()</code> INSTEAD.
     * @param approvalDate  The approval Data of this <code>History</code>.
     * @thows  IllegalArgumentException If the approval Date is <code>null</code>.
     * @uml.property  name="approvalDate"
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    /**
	 * Method description
	 * @param  comment
	 * @uml.property  name="comment"
	 */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Not for Developer use. This is required for Castor/DAO
     * @param conceptDelegate  The new conceptDelegate value
     * @uml.property  name="conceptDelegate"
     */
    public void setConceptDelegate(IConceptDelegate conceptDelegate) {
        this.conceptDelegate = (ConceptDelegate) conceptDelegate;
    }

    /**
     * Sets the Date the user created this <code>History</code>. Necessary for Castor. Developers should use appropriate constructor.
     * @param creationDate  The Date the user created this <code>History</code>.
     * @thows  IllegalArgumentException If the creation Date is <code>null</code>.
     * @uml.property  name="creationDate"
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
	 * Method description
	 * @param  field
	 * @uml.property  name="field"
	 */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Not for Developer use. Sets the database primary key. Required for IDAO interface.
     * @param id  The new id value
     * @uml.property  name="id"
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Method description
	 * @param  newValue
	 * @uml.property  name="newValue"
	 */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    /**
	 * Method description
	 * @param  oldValue
	 * @uml.property  name="oldValue"
	 */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    /**
	 * Method description
	 * @param  rejected
	 * @uml.property  name="rejected"
	 */
    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    /**
     * Sets the name of the user that created this <code>History<code>.
     * @param creatorName                      The name of the user that created this <code>History<code>.
     * @uml.property  name="creatorName"
     */
    public void setCreatorName(String userName) {
        if ((userName != null) && (MAX_USERNAME_LEN < userName.length())) {
            int len = userName.length();
            if (log.isWarnEnabled()) {
                log.warn("History user name truncated from " + len + " to " + MAX_USERNAME_LEN + " characters");
            }
            this.creatorName = userName.substring(0, MAX_USERNAME_LEN);
        } else {
            this.creatorName = userName;
        }
    }

    /**
	 * @param approverName  the approverName to set
	 * @uml.property  name="approverName"
	 */
    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    /**
	 * @return  the approverName
	 * @uml.property  name="approverName"
	 */
    public String getApproverName() {
        return approverName;
    }
}
