package org.hip.vif.core.bom.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.hip.kernel.bom.BOMNotFoundException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.DomainObjectHomeImpl;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.ModifierStrategy;
import org.hip.kernel.bom.impl.PreparedUpdateStatement;
import org.hip.kernel.exc.VException;
import org.hip.kernel.workflow.WorkflowException;
import org.hip.vif.core.bom.GroupAdminHome;
import org.hip.vif.core.bom.GroupHome;
import org.hip.vif.core.bom.ParticipantHome;
import org.hip.vif.core.bom.VIFGroupWorkflow;
import org.hip.vif.core.exc.BOMChangeValueException;

/**
 * This domain object home implements the ParticipantHome interface.
 * 
 * Created on 01.11.2002
 * @author Benno Luthiger
 * @see org.hip.vif.core.bom.ParticipantHome
 */
public class ParticipantHomeImpl extends DomainObjectHomeImpl implements ParticipantHome {

    private static final String OBJECT_CLASS_NAME = "org.hip.vif.core.bom.impl.ParticipantImpl";

    private static final String XML_OBJECT_DEF = "<?xml version='1.0' encoding='ISO-8859-1'?>	\n" + "<objectDef objectName='GroupAdmin' parent='org.hip.kernel.bom.DomainObject' version='1.0'>	\n" + "	<keyDefs>	\n" + "		<keyDef>	\n" + "			<keyItemDef seq='0' keyPropertyName='" + KEY_MEMBER_ID + "'/>	\n" + "			<keyItemDef seq='1' keyPropertyName='" + KEY_GROUP_ID + "'/>	\n" + "		</keyDef>	\n" + "	</keyDefs>	\n" + "	<propertyDefs>	\n" + "		<propertyDef propertyName='" + KEY_MEMBER_ID + "' valueType='Long' propertyType='simple'>	\n" + "			<mappingDef tableName='tblParticipant' columnName='MemberID'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_GROUP_ID + "' valueType='Long' propertyType='simple'>	\n" + "			<mappingDef tableName='tblParticipant' columnName='GroupID'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_SUSPEND_FROM + "' valueType='Timestamp' propertyType='simple'>	\n" + "			<mappingDef tableName='tblParticipant' columnName='dtSuspendFrom'/>	\n" + "		</propertyDef>	\n" + "		<propertyDef propertyName='" + KEY_SUSPEND_TO + "' valueType='Timestamp' propertyType='simple'>	\n" + "			<mappingDef tableName='tblParticipant' columnName='dtSuspendTo'/>	\n" + "		</propertyDef>	\n" + "	</propertyDefs>	\n" + "</objectDef>";

    /**
	 * @see org.hip.kernel.bom.GeneralDomainObjectHome#getObjectClassName()
	 */
    public String getObjectClassName() {
        return OBJECT_CLASS_NAME;
    }

    /**
	 * @see org.hip.kernel.bom.impl.AbstractDomainObjectHome#getObjectDefString()
	 */
    protected String getObjectDefString() {
        return XML_OBJECT_DEF;
    }

    public Collection<String> saveRegisterings(Long inActorID, Collection<String> inGroupIDs) throws BOMChangeValueException {
        Collection<String> outDeletedAdmins = new Vector<String>();
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_MEMBER_ID, inActorID);
            lKey.setValue(GroupHome.KEY_PRIVATE, GroupHome.IS_PUBLIC, "=", BinaryBooleanOperator.AND);
            lKey.setValue(createSubscribable(), BinaryBooleanOperator.AND);
            QueryResult lExisting = ((JoinParticipantToGroupHome) BOMHelper.getJoinParticipantToGroupHome()).select(lKey);
            while (lExisting.hasMoreElements()) {
                Long lGroupID = new Long(lExisting.nextAsDomainObject().get(KEY_GROUP_ID).toString());
                String lGroupIDString = lGroupID.toString();
                if (inGroupIDs.contains(lGroupIDString)) {
                    inGroupIDs.remove(lGroupIDString);
                } else {
                    if (((GroupAdminHome) BOMHelper.getGroupAdminHome()).isGroupAdmin(inActorID, lGroupID)) {
                        outDeletedAdmins.add(lGroupIDString);
                    } else {
                        lKey = new KeyObjectImpl();
                        lKey.setValue(KEY_MEMBER_ID, inActorID);
                        lKey.setValue(KEY_GROUP_ID, lGroupID);
                        delete(lKey, true);
                    }
                }
            }
            for (Iterator<String> lNewEntries = inGroupIDs.iterator(); lNewEntries.hasNext(); ) {
                create(inActorID, new Long(lNewEntries.next()));
            }
            return outDeletedAdmins;
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (WorkflowException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    private KeyObject createSubscribable() throws VException {
        Integer[] lStates = VIFGroupWorkflow.ENLISTABLE_STATES;
        KeyObject outKey = new KeyObjectImpl();
        for (int i = 0; i < lStates.length; i++) {
            outKey.setValue(GroupHome.KEY_STATE, lStates[i], "=", BinaryBooleanOperator.OR);
        }
        return outKey;
    }

    /**
	 * Returns an Collection of ids of discussion groups the specified user has registered for.
	 * 
	 * @param inActorID java.lang.Long The unique identification of the member.
	 * @return Collection<String> of ids of discussion groups the specified user has registered for
	 * @throws org.hip.vif.core.exc.bom.impl.BOMChangeValueException 
	 */
    public Collection<String> getRegisterings(Long inActorID) throws BOMChangeValueException {
        Collection<String> outGroupIDs = new Vector<String>();
        try {
            QueryResult lEntries = getGroups(inActorID);
            while (lEntries.hasMoreElements()) {
                GeneralDomainObject lEntry = lEntries.nextAsDomainObject();
                outGroupIDs.add(lEntry.get(KEY_GROUP_ID).toString());
            }
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
        return outGroupIDs;
    }

    private QueryResult getGroups(Long inActorID) throws BOMChangeValueException {
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_MEMBER_ID, inActorID);
            return select(lKey);
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Makes the specified member an active participant in the specified group.
	 * 
	 * @param inMemberID String
	 * @param inGroupID String
	 * @return DomainObject
	 * @throws VException
	 * @throws WorkflowException
	 * @throws SQLException
	 */
    public DomainObject create(String inMemberID, String inGroupID) throws VException, WorkflowException, SQLException {
        Long lMemberID = new Long(inMemberID);
        Long lGroupID = new Long(inGroupID);
        create(lMemberID, lGroupID);
        return find(lMemberID, lGroupID);
    }

    /**
	 * Makes the specified member an active participant in the specified group
	 * only if she is not registered yet.
	 * 
	 * @param inMemberID String
	 * @param inGroupID String
	 * @return boolean
	 * @throws VException
	 * @throws WorkflowException
	 * @throws SQLException
	 */
    public boolean createChecked(String inMemberID, String inGroupID) throws VException, WorkflowException, SQLException {
        return createChecked(new Long(inMemberID), new Long(inGroupID));
    }

    public boolean createChecked(Long inMemberID, Long inGroupID) throws VException, WorkflowException, SQLException {
        try {
            find(inMemberID, inGroupID);
            return false;
        } catch (BOMNotFoundException exc) {
            create(inMemberID, inGroupID);
            return true;
        }
    }

    private void create(Long inMemberID, Long inGroupID) throws VException, WorkflowException, SQLException {
        DomainObject lParticipant = create();
        lParticipant.set(KEY_MEMBER_ID, inMemberID);
        lParticipant.set(KEY_GROUP_ID, inGroupID);
        Timestamp lInit = new Timestamp(1000);
        lParticipant.set(KEY_SUSPEND_FROM, lInit);
        lParticipant.set(KEY_SUSPEND_TO, lInit);
        lParticipant.insert(true);
        BOMHelper.getGroupHome().getGroup(inGroupID.toString()).checkActivationState(getParticipantsOfGroup(inGroupID));
    }

    private DomainObject find(Long inMemberID, Long inGroupID) throws VException, SQLException {
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(ParticipantHome.KEY_MEMBER_ID, inMemberID);
        lKey.setValue(ParticipantHome.KEY_GROUP_ID, inGroupID);
        return findByKey(lKey);
    }

    /**
	 * Checks whether the specified actor is participant, i.e. is registered
	 * in at least one group.
	 * 
	 * @param inActorID Long
	 * @return boolean, true if participant.
	 * @throws BOMChangeValueException
	 */
    public boolean isParticipant(Long inActorID) throws BOMChangeValueException {
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_MEMBER_ID, inActorID);
            return getCount(lKey) > 0;
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Checks whether the specified actor is participant of the specified group.
	 * 
	 * @param inGroupID Long
	 * @param inActorID Long
	 * @return boolean True, if the actor is not participant of the specified group.
	 * @throws BOMChangeValueException
	 */
    public boolean isParticipantOfGroup(Long inGroupID, Long inActorID) throws BOMChangeValueException {
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_GROUP_ID, inGroupID);
            lKey.setValue(KEY_MEMBER_ID, inActorID);
            return getCount(lKey) > 0;
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Returns the number of participants of the specified group.
	 * 
	 * @param inGroupID Long
	 * @return int
	 * @throws BOMChangeValueException
	 */
    public int getParticipantsOfGroup(Long inGroupID) throws BOMChangeValueException {
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_GROUP_ID, inGroupID);
            return getCount(lKey);
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Removes the specified participant from the specified group.
	 * 
	 * @param inGroupID Long
	 * @param inMemberID Long
	 * @throws BOMChangeValueException
	 */
    public void removeParticipant(Long inGroupID, Long inMemberID) throws BOMChangeValueException {
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(KEY_GROUP_ID, inGroupID);
            lKey.setValue(KEY_MEMBER_ID, inMemberID);
            delete(lKey, true);
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Suspends the specified actor's participations in his or her discussion groups for the
	 * specified time period. 
	 * 
	 * @param inActorID Long 
	 * @param inFrom Timestamp
	 * @param inTo Timestamp
	 * @throws BOMChangeValueException
	 */
    public void suspendParticipation(Long inActorID, Timestamp inFrom, Timestamp inTo) throws BOMChangeValueException {
        try {
            KeyObject lChange = new KeyObjectImpl();
            lChange.setValue(ParticipantHome.KEY_SUSPEND_FROM, inFrom);
            lChange.setValue(ParticipantHome.KEY_SUSPEND_TO, inTo);
            KeyObject lWhere = new KeyObjectImpl();
            lWhere.setValue(ParticipantHome.KEY_MEMBER_ID, inActorID);
            PreparedUpdateStatement lStatement = new PreparedUpdateStatement(this, lChange, lWhere);
            lStatement.executeUpdate();
            lStatement.commit();
        } catch (VException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        } catch (SQLException exc) {
            throw new BOMChangeValueException(exc.getMessage());
        }
    }

    /**
	 * Get the specified actor's suspend from and suspend to dates.
	 * The method returns a Collection containing the maximum date for SuspendFrom and SuspendTo.
	 * 
	 * @param ininActorID Long
	 * @return Collection
	 * @throws VException 
	 * @throws SQLException
	 */
    public Collection<Object> getActorSuspend(Long inActorID) throws VException, SQLException {
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(ParticipantHome.KEY_MEMBER_ID, inActorID);
        ModifierStrategy lStrategy = new ModifierStrategy(new String[] { ParticipantHome.KEY_SUSPEND_FROM, ParticipantHome.KEY_SUSPEND_TO }, ModifierStrategy.MAX);
        return getModified(lStrategy, lKey);
    }
}
