package com.apelon.dts.client.events;

import java.util.EventObject;
import com.apelon.dts.client.term.Term;
import com.apelon.dts.client.association.TermAssociation;

/**
 * An event which indicates that a term action occurred.
 * <P>
 * A term action is considered to occur in either of the following situation:
 * <ul>
 * <li>add/modify/delete a Term
 * <li>add/modify/delete a Term Association
 * <li>add/modify/delete a Term Property
 * </ul>
 *
 * @author  Apelon Inc.
 * @version DTS 3.4
 * @since DTS 3.4
 */
public class TermEvent extends DataChangeEvent {

    private TermAttributeChange attributeChange;

    /**
   * Constructs a TermEvent object. Initializes the term attribute change.
   *
   * @param source Should be an object that is a valid source of Term events.
   * @param term The term which has changed. In the case of modifiy, this should be the term after change.
   * @param eventType If adding a new term, the eventType should be {@link #EVENT_TYPE_NEW}
   *                  If modifying a term, the eventType should be {@link #EVENT_TYPE_MODIFY}
   *                  If deleting a term, the eventType should be {@link #EVENT_TYPE_DELETE}
   * @param attributeChange If the term is modified, attributeChange contains the attributes changes information;
   *                        If the term is added or is deleted, attributeChange should be null.
   *
   * @see TermListener
   * @see DTSEventMulticaster
   * @see TermAttributeChange
   * @see DataChangeEvent
   *
   */
    public TermEvent(java.lang.Object source, Term term, int eventType, TermAttributeChange attributeChange) {
        super(source, term, eventType);
        this.attributeChange = attributeChange;
    }

    /**
   * Get the Term Attribute Change when a term is modified.
   *
   * @return TermAttributeChange It contains the term attribute changes information
   *                                when a term is updated.
   */
    public TermAttributeChange getAttributeChange() {
        return attributeChange;
    }

    /**
   * Get the term which has changed. This Term may only contains basic term information
   * like name, id, and namespaceid. To get more term information you have to set
   * corresponding TermAttributeSetDescriptor and send a query request to server to get them.
   *
   * @return Term If a term is added, return the term which is added;
   *              If a term is updated, return the term after updating;
   *              If a term is deleted, return the term which is deleted.
   */
    public Term getTerm() {
        return (Term) getChangedData();
    }

    /**
   * A method to check if a term is affected by this term action.
   *
   * @param term the term to be checked.
   * @return boolean true - if the term is affected by this term action.
   *                 false - if the term is not affected by this term action.
   */
    public boolean isTermAffected(Term term) {
        boolean isAffected = false;
        Term relatedTerm = null;
        Term relatedTerm2 = null;
        if (getTerm() != null && getTerm().getId() == term.getId() && getTerm().getNamespaceId() == term.getNamespaceId()) {
            return true;
        }
        if (getEventType() == TermEvent.EVENT_TYPE_MODIFY) {
            if (attributeChange != null) {
                int changeType = attributeChange.getChangeType();
                int attrType = attributeChange.getAttributeType();
                if (changeType == TermAttributeChange.NEW) {
                    if (attrType == TermAttributeChange.ASSOCIATION) {
                        TermAssociation termAssoc = (TermAssociation) attributeChange.getNewAttribute();
                        if (termAssoc != null) {
                            relatedTerm = termAssoc.getToTerm();
                        }
                    }
                } else if (changeType == TermAttributeChange.DELETE) {
                    if (attrType == TermAttributeChange.ASSOCIATION) {
                        TermAssociation termAssoc = (TermAssociation) attributeChange.getOldAttribute();
                        if (termAssoc != null) {
                            relatedTerm = termAssoc.getToTerm();
                        }
                    }
                } else if (changeType == TermAttributeChange.MODIFY) {
                    if (attrType == TermAttributeChange.ASSOCIATION) {
                        TermAssociation termAssoc = (TermAssociation) attributeChange.getNewAttribute();
                        TermAssociation oldTermAssoc = (TermAssociation) attributeChange.getOldAttribute();
                        if (termAssoc != null) {
                            relatedTerm = termAssoc.getToTerm();
                        }
                        if (oldTermAssoc != null) {
                            relatedTerm2 = oldTermAssoc.getToTerm();
                        }
                    }
                }
                if (relatedTerm != null && relatedTerm.getId() == term.getId() && relatedTerm.getNamespaceId() == term.getNamespaceId()) {
                    isAffected = true;
                }
                if (relatedTerm2 != null && relatedTerm2.getId() == term.getId() && relatedTerm2.getNamespaceId() == term.getNamespaceId()) {
                    isAffected = true;
                }
            }
        }
        return isAffected;
    }
}
