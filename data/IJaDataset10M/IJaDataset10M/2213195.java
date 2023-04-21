package gov.ornl.nice.niceitem.item.action;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import static gov.ornl.nice.niceitem.item.action.LoginInfoForm.*;
import java.util.concurrent.atomic.AtomicReference;
import gov.ornl.nice.nicedatastructures.form.DataComponent;
import gov.ornl.nice.nicedatastructures.form.Entry;
import java.util.ArrayList;

/** 
 * <!-- begin-UML-doc -->
 * <p>The NiCEJschUIInfo class gathers password information from a LoginInfoForm to provide the password to Jsch. It implements both the Jsch UserInfo and UIKeyboardInteractive interfaces. This class is only configured to work with single request keyboard interactive login and (and it fakes the interactive response at that...).</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class NiCEJschUIInfo implements UIKeyboardInteractive, UserInfo {

    /**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An AtomicReference to the LoginInfoForm from which password information
	 * should be gathered.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
    private AtomicReference<LoginInfoForm> loginInfoFormAtomic;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public NiCEJschUIInfo() {
        loginInfoFormAtomic = new AtomicReference<LoginInfoForm>();
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation sets the LoginInfoForm that should be used by the NiCEJschUIInfo class to gather the password information.</p>
	 * <!-- end-UML-doc -->
	 * @param form
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void setForm(LoginInfoForm form) {
        if (form != null) loginInfoFormAtomic.set(form);
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns true if the NiCEJschUIInfo is ready and false otherwise. The decision is based on the presence or absence of the LoginInfoForm.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if ready, false otherwise.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public boolean isReady() {
        if (loginInfoFormAtomic.get() != null) return true; else return false;
    }

    /** 
	 * (non-Javadoc)
	 * @see UIKeyboardInteractive#promptKeyboardInteractive(String destination, String name, String instruction, ArrayList<String> prompt, boolean... echo)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public ArrayList<String> promptKeyboardInteractive(String destination, String name, String instruction, ArrayList<String> prompt, boolean... echo) {
        ArrayList<String> retList = new ArrayList<String>();
        String[] retArray = promptKeyboardInteractive(destination, name, instruction, (String[]) prompt.toArray(), echo);
        for (String i : retArray) retList.add(i);
        return retList;
    }

    /**
	 * This operation fakes out Jsch's keyboard interactive check and gives it
	 * the stored password.
	 * 
	 * @see UIKeyboardInteractive#promptKeyboardInteractive(String destination,
	 *      String name, String instruction, ArrayList<String> prompt,
	 *      boolean... echo)
	 */
    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
        DataComponent comp = null;
        ArrayList<Entry> entries = null;
        String password = null;
        String[] response = new String[1];
        if (loginInfoFormAtomic.get() != null) {
            comp = (DataComponent) loginInfoFormAtomic.get().getComponent(1);
            entries = comp.retrieveAllEntries();
            password = entries.get(1).getValue();
            response[0] = password;
        }
        return response;
    }

    /** 
	 * (non-Javadoc)
	 * @see UserInfo#getPassphrase()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public String getPassphrase() {
        return null;
    }

    /** 
	 * (non-Javadoc)
	 * @see UserInfo#getPassword()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public String getPassword() {
        DataComponent comp = null;
        ArrayList<Entry> entries = null;
        String password = null;
        if (loginInfoFormAtomic.get() != null) {
            comp = (DataComponent) loginInfoFormAtomic.get().getComponent(1);
            entries = comp.retrieveAllEntries();
            password = entries.get(1).getValue();
            System.out.println("Password requested!");
        }
        return password;
    }

    /** 
	 * (non-Javadoc)
	 * @see UserInfo#promptPassword(String message)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public boolean promptPassword(String message) {
        DataComponent comp = null;
        ArrayList<Entry> entries = null;
        String password = null;
        if (loginInfoFormAtomic.get() != null) {
            comp = (DataComponent) loginInfoFormAtomic.get().getComponent(1);
            entries = comp.retrieveAllEntries();
            password = entries.get(1).getValue();
        }
        if (password == null) return false; else return true;
    }

    /** 
	 * (non-Javadoc)
	 * @see UserInfo#promptPassphrase(String message)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public boolean promptPassphrase(String message) {
        return true;
    }

    /** 
	 * (non-Javadoc)
	 * @see UserInfo#promptYesNo(String message)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public boolean promptYesNo(String message) {
        return false;
    }

    /** 
	 * (non-Javadoc)
	 * @see UserInfo#showMessage(String message)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void showMessage(String message) {
    }
}
