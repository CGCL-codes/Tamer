package org.hip.vif.bom;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.GettingException;
import org.hip.kernel.exc.VException;
import org.hip.kernel.workflow.WorkflowException;
import org.hip.vif.exc.BOMChangeValueException;
import org.hip.vif.exc.ExternIDNotUniqueException;

/**
 * This interface defines the behaviour of the Group domain object.
 * A discussion group collects all Questions and Contributions to answer
 * a certain problem complex.
 * 
 * Created on 19.07.2002
 * @author Benno Luthiger
 * @see org.hip.vif.bom.Question
 * @see org.hip.vif.bom.Completion
 */
public interface Group extends DomainObject {

    /**
	 * Insert a new entry and save the data.
	 * 
	 * @param inGroupID java.lang.String
	 * @param inName java.lang.String
	 * @param inReviewers java.lang.String
	 * @param inGuestDepth java.lang.String
	 * @param inMinGoupSize java.lang.String
	 * @param inIsPrivate boolean
	 * @return Long The ID of the new group entry.
	 * @exception org.hip.vif.bom.impl.BOMChangeValueException 
	 * @exception org.hip.vif.bom.impl.ExternIDNotUniqueException 
	 */
    Long ucNew(String inGroupID, String inName, String inReviewers, String inGuestDepth, String inMinGoupSize, boolean inIsPrivate) throws BOMChangeValueException, ExternIDNotUniqueException;

    /**
	 * Update a discussion group entry .
	 * 
	 * @param inGroupID java.lang.String
	 * @param inName java.lang.String
	 * @param inReviewers java.lang.String
	 * @param inGuestDepth java.lang.String
	 * @param inMinGoupSize java.lang.String
	 * @param inLanguage java.lang.String
	 * @param inIsPrivate boolean
 	 * @exception VException 
	 * @exception WorkflowException
	 */
    void ucSave(String inGroupID, String inName, String inReviewers, String inGuestDepth, String inMinGoupSize, String inLanguage, boolean inIsPrivate) throws VException, WorkflowException;

    /**
	 * Returns the number of root questions attached to this group.
	 * 
	 * @return int
	 */
    int rootCount() throws SQLException, VException;

    /**
	 * Returns a collection of possible transitions contingent on this
	 * group's state.
	 * 
	 * @return Collection<String> of transitions
	 * @throws VException
	 */
    Collection<String> getTransitions() throws VException;

    /**
	 * Returns the correct type of closing transition according to
	 * this group's state.
	 * 
	 * @return
	 * @throws VException
	 */
    String getCloseTransition() throws VException;

    /**
	 * Returns the correct type of reactivating transition according to
	 * this group's state.
	 * 
	 * @return String
	 * @throws VException
	 */
    String getReactivateTransition() throws VException;

    /**
	 * Returns the number of participants registered to this group.
	 * 
	 * @return int The number of registered participants.
	 * @throws VException
	 */
    int getNumberOfRegistered() throws VException;

    /**
	 * Returns the minimal number of participants needed for this group to be active.
	 * 
	 * @return int The minimal group size
	 * @throws GettingException
	 */
    int getMinGroupSize() throws VException;

    /**
	 * Checks the group's activation state against the specified value.
	 * If the number exceeds the minimal group size, the group is activated.
	 * 
	 * @param inRegistered Number of registered members of this group.
	 * @param inLanguage String
	 * @throws GettingException
	 * @throws WorkflowException
	 */
    void checkActivationState(int inRegistered, String inLanguage) throws GettingException, WorkflowException;

    /**
	 * Returns a array of the mail addresses of this group's participants.
	 * 
	 * @return InternetAddress[]
	 * @throws VException
	 * @throws SQLException
	 * @throws AddressException
	 */
    InternetAddress[] getParticipantsMail() throws VException, SQLException, AddressException;

    /**
	 * Checks whether this group in the state just created.
	 * 
	 * @return boolean <code>true</code> if the group has just been created.
	 * @throws GettingException
	 */
    public boolean isCreated() throws GettingException;

    /**
	 * Checks whether this group is active.
	 * 
	 * @return boolean True if this group is active.
	 * @throws GettingException
	 */
    boolean isActive() throws GettingException;

    /**
	 * Checks whether the specified actor is participant in this group.
	 * 
	 * @param inActorID Long
	 * @return boolean True if the actor is participant.
	 * @throws VException
	 */
    boolean isParticipant(Long inActorID) throws VException;

    /**
	 * Returnt true if this group is of type where the contributions need
	 * to be reviewed (i.e. the number of reviewers is set > 0).
	 * 
	 * @return boolean True if this group is private.
	 * @throws VException
	 */
    boolean needsReview() throws VException;

    /**
	 * Returns true if this group is private.
	 * 
	 * @return boolean True if this group is private.
	 * @throws VException
	 */
    boolean isPrivate() throws VException;

    /**
	 * Returns the group's visibility, i.e. the depth a unauthenticated
	 * user (that is a guest) can see questions.
	 * 0 means the whole group is invisible for guests,
	 * 1 means only the group's root question is visible,
	 * 2 means only the root question and the first level of follow up questions is visible,
	 * ...
	 * 
	 * @return BigDecimal
	 * @throws GettingException
	 */
    BigDecimal getGuestDepth() throws GettingException;

    /**
	 * The group is deletable if it is either created or closed.
	 * 
	 * @return boolean <code>true</code> if the group is deletable.
	 */
    public boolean isDeletable() throws VException;
}
