package com.safi.asterisk.actionstep;

import com.safi.core.actionstep.DynamicValue;
import com.safi.core.call.CallConsumer1;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Voicemail Main</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.safi.asterisk.actionstep.VoicemailMain#getMailbox <em>Mailbox</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.VoicemailMain#isSkipPasswordCheck <em>Skip Password Check</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.VoicemailMain#isUsePrefix <em>Use Prefix</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.VoicemailMain#getRecordingGain <em>Recording Gain</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.VoicemailMain#getDefaultFolder <em>Default Folder</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.safi.asterisk.actionstep.ActionstepPackage#getVoicemailMain()
 * @model
 * @generated
 */
public interface VoicemailMain extends AsteriskActionStep, CallConsumer1 {

    /**
	 * Returns the value of the '<em><b>Mailbox</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mailbox</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mailbox</em>' containment reference.
	 * @see #setMailbox(DynamicValue)
	 * @see com.safi.asterisk.actionstep.ActionstepPackage#getVoicemailMain_Mailbox()
	 * @model containment="true" ordered="false"
	 *        annotation="DynamicValueAnnotation type='VariableName' isTypeLocked='false' description='the mailbox name' expectedReturnType='Text' helperClass=''"
	 *        annotation="Required criteria='non-null'"
	 * @generated
	 */
    DynamicValue getMailbox();

    /**
	 * Sets the value of the '{@link com.safi.asterisk.actionstep.VoicemailMain#getMailbox <em>Mailbox</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mailbox</em>' containment reference.
	 * @see #getMailbox()
	 * @generated
	 */
    void setMailbox(DynamicValue value);

    /**
	 * Returns the value of the '<em><b>Skip Password Check</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Skip Password Check</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Skip Password Check</em>' attribute.
	 * @see #setSkipPasswordCheck(boolean)
	 * @see com.safi.asterisk.actionstep.ActionstepPackage#getVoicemailMain_SkipPasswordCheck()
	 * @model ordered="false"
	 * @generated
	 */
    boolean isSkipPasswordCheck();

    /**
	 * Sets the value of the '{@link com.safi.asterisk.actionstep.VoicemailMain#isSkipPasswordCheck <em>Skip Password Check</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Skip Password Check</em>' attribute.
	 * @see #isSkipPasswordCheck()
	 * @generated
	 */
    void setSkipPasswordCheck(boolean value);

    /**
	 * Returns the value of the '<em><b>Use Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Prefix</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Prefix</em>' attribute.
	 * @see #setUsePrefix(boolean)
	 * @see com.safi.asterisk.actionstep.ActionstepPackage#getVoicemailMain_UsePrefix()
	 * @model ordered="false"
	 * @generated
	 */
    boolean isUsePrefix();

    /**
	 * Sets the value of the '{@link com.safi.asterisk.actionstep.VoicemailMain#isUsePrefix <em>Use Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Prefix</em>' attribute.
	 * @see #isUsePrefix()
	 * @generated
	 */
    void setUsePrefix(boolean value);

    /**
	 * Returns the value of the '<em><b>Recording Gain</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Recording Gain</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Recording Gain</em>' attribute.
	 * @see #setRecordingGain(int)
	 * @see com.safi.asterisk.actionstep.ActionstepPackage#getVoicemailMain_RecordingGain()
	 * @model ordered="false"
	 * @generated
	 */
    int getRecordingGain();

    /**
	 * Sets the value of the '{@link com.safi.asterisk.actionstep.VoicemailMain#getRecordingGain <em>Recording Gain</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Recording Gain</em>' attribute.
	 * @see #getRecordingGain()
	 * @generated
	 */
    void setRecordingGain(int value);

    /**
	 * Returns the value of the '<em><b>Default Folder</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Folder</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Folder</em>' containment reference.
	 * @see #setDefaultFolder(DynamicValue)
	 * @see com.safi.asterisk.actionstep.ActionstepPackage#getVoicemailMain_DefaultFolder()
	 * @model containment="true" ordered="false"
	 *        annotation="DynamicValueAnnotation type='VariableName' isTypeLocked='false' description='the mailbox folder name to default to. Default is \'INBOX\'' expectedReturnType='Text' helperClass=''"
	 * @generated
	 */
    DynamicValue getDefaultFolder();

    /**
	 * Sets the value of the '{@link com.safi.asterisk.actionstep.VoicemailMain#getDefaultFolder <em>Default Folder</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Folder</em>' containment reference.
	 * @see #getDefaultFolder()
	 * @generated
	 */
    void setDefaultFolder(DynamicValue value);
}
