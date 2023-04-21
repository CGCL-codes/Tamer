package de.mpiwg.vspace.metamodel.impl;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.Text;
import de.mpiwg.vspace.metamodel.extension.IPropertyRetriever;
import de.mpiwg.vspace.metamodel.extension.PropertyExtensionProvider;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Text</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.TextImpl#getTextFilePath <em>Text File Path</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.impl.TextImpl#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TextImpl extends MediaImpl implements Text {

    /**
	 * The default value of the '{@link #getTextFilePath() <em>Text File Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTextFilePath()
	 * @generated
	 * @ordered
	 */
    protected static final String TEXT_FILE_PATH_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTextFilePath() <em>Text File Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTextFilePath()
	 * @generated
	 * @ordered
	 */
    protected String textFilePath = TEXT_FILE_PATH_EDEFAULT;

    /**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated NOT
	 * @ordered
	 */
    protected static final String TEXT_EDEFAULT = "No text";

    /**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
    protected String text = TEXT_EDEFAULT;

    protected Map<Integer, Class> originalTypes;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected TextImpl() {
        super();
        originalTypes = new HashMap<Integer, Class>();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ExhibitionPackage.Literals.TEXT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTextFilePath() {
        return textFilePath;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTextFilePath(String newTextFilePath) {
        String oldTextFilePath = textFilePath;
        textFilePath = newTextFilePath;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.TEXT__TEXT_FILE_PATH, oldTextFilePath, textFilePath));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String getText() {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.TEXT__TEXT);
        if (retriever == null) return text;
        return retriever.getProperty(this, ExhibitionPackage.Literals.TEXT__TEXT).toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setText(String newText) {
        IPropertyRetriever retriever = PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.TEXT__TEXT);
        String oldText = text;
        if (retriever == null) {
            text = newText;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.TEXT__TEXT, oldText, text));
        } else {
            Class type = originalTypes.get(newText.hashCode());
            if (type != null) originalTypes.remove(newText.hashCode());
            retriever.setProperty(this, ExhibitionPackage.Literals.TEXT__TEXT, newText, type);
            text = "";
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExhibitionPackage.TEXT__TEXT, oldText, PropertyExtensionProvider.INSTANCE.getPropertyRetriever(ExhibitionPackage.Literals.TEXT__TEXT).toString()));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ExhibitionPackage.TEXT__TEXT_FILE_PATH:
                return getTextFilePath();
            case ExhibitionPackage.TEXT__TEXT:
                return getText();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ExhibitionPackage.TEXT__TEXT_FILE_PATH:
                setTextFilePath((String) newValue);
                return;
            case ExhibitionPackage.TEXT__TEXT:
                originalTypes.put(newValue.toString().hashCode(), newValue.getClass());
                setText(newValue.toString());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case ExhibitionPackage.TEXT__TEXT_FILE_PATH:
                setTextFilePath(TEXT_FILE_PATH_EDEFAULT);
                return;
            case ExhibitionPackage.TEXT__TEXT:
                setText(TEXT_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case ExhibitionPackage.TEXT__TEXT_FILE_PATH:
                return TEXT_FILE_PATH_EDEFAULT == null ? textFilePath != null : !TEXT_FILE_PATH_EDEFAULT.equals(textFilePath);
            case ExhibitionPackage.TEXT__TEXT:
                return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (textFilePath: ");
        result.append(textFilePath);
        result.append(", text: ");
        result.append(text);
        result.append(')');
        return result.toString();
    }
}
