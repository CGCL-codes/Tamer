package net.sf.smbt.osc;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see net.sf.smbt.osc.OscFactory
 * @model kind="package"
 * @generated
 */
public interface OscPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "osc";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://osc/1.0";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "osc";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    OscPackage eINSTANCE = net.sf.smbt.osc.impl.OscPackageImpl.init();

    /**
	 * The meta object id for the '{@link net.sf.smbt.osc.impl.OscAtomImpl <em>Atom</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.osc.impl.OscAtomImpl
	 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscAtom()
	 * @generated
	 */
    int OSC_ATOM = 0;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OSC_ATOM__TYPE = 0;

    /**
	 * The number of structural features of the '<em>Atom</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OSC_ATOM_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.sf.smbt.osc.impl.OscMetaImpl <em>Meta</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.osc.impl.OscMetaImpl
	 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscMeta()
	 * @generated
	 */
    int OSC_META = 1;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OSC_META__VERSION = 0;

    /**
	 * The feature id for the '<em><b>Framing</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OSC_META__FRAMING = 1;

    /**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OSC_META__URI = 2;

    /**
	 * The feature id for the '<em><b>Types</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OSC_META__TYPES = 3;

    /**
	 * The number of structural features of the '<em>Meta</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OSC_META_FEATURE_COUNT = 4;

    /**
	 * The meta object id for the '{@link net.sf.smbt.osc.OscType <em>Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.osc.OscType
	 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscType()
	 * @generated
	 */
    int OSC_TYPE = 2;

    /**
	 * The meta object id for the '{@link net.sf.smbt.osc.OscData <em>Data</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.osc.OscData
	 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscData()
	 * @generated
	 */
    int OSC_DATA = 3;

    /**
	 * The meta object id for the '{@link net.sf.smbt.osc.OscFraming <em>Framing</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.osc.OscFraming
	 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscFraming()
	 * @generated
	 */
    int OSC_FRAMING = 4;

    /**
	 * The meta object id for the '{@link net.sf.smbt.osc.OscVersion <em>Version</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.osc.OscVersion
	 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscVersion()
	 * @generated
	 */
    int OSC_VERSION = 5;

    /**
	 * The meta object id for the '<em>OSC Message</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.illposed.osc.OSCMessage
	 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOSCMessage()
	 * @generated
	 */
    int OSC_MESSAGE = 6;

    /**
	 * Returns the meta object for class '{@link net.sf.smbt.osc.OscAtom <em>Atom</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Atom</em>'.
	 * @see net.sf.smbt.osc.OscAtom
	 * @generated
	 */
    EClass getOscAtom();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.osc.OscAtom#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.sf.smbt.osc.OscAtom#getType()
	 * @see #getOscAtom()
	 * @generated
	 */
    EAttribute getOscAtom_Type();

    /**
	 * Returns the meta object for class '{@link net.sf.smbt.osc.OscMeta <em>Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Meta</em>'.
	 * @see net.sf.smbt.osc.OscMeta
	 * @generated
	 */
    EClass getOscMeta();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.osc.OscMeta#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.sf.smbt.osc.OscMeta#getVersion()
	 * @see #getOscMeta()
	 * @generated
	 */
    EAttribute getOscMeta_Version();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.osc.OscMeta#getFraming <em>Framing</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Framing</em>'.
	 * @see net.sf.smbt.osc.OscMeta#getFraming()
	 * @see #getOscMeta()
	 * @generated
	 */
    EAttribute getOscMeta_Framing();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.osc.OscMeta#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see net.sf.smbt.osc.OscMeta#getUri()
	 * @see #getOscMeta()
	 * @generated
	 */
    EAttribute getOscMeta_Uri();

    /**
	 * Returns the meta object for the attribute list '{@link net.sf.smbt.osc.OscMeta#getTypes <em>Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Types</em>'.
	 * @see net.sf.smbt.osc.OscMeta#getTypes()
	 * @see #getOscMeta()
	 * @generated
	 */
    EAttribute getOscMeta_Types();

    /**
	 * Returns the meta object for enum '{@link net.sf.smbt.osc.OscType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Type</em>'.
	 * @see net.sf.smbt.osc.OscType
	 * @generated
	 */
    EEnum getOscType();

    /**
	 * Returns the meta object for enum '{@link net.sf.smbt.osc.OscData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Data</em>'.
	 * @see net.sf.smbt.osc.OscData
	 * @generated
	 */
    EEnum getOscData();

    /**
	 * Returns the meta object for enum '{@link net.sf.smbt.osc.OscFraming <em>Framing</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Framing</em>'.
	 * @see net.sf.smbt.osc.OscFraming
	 * @generated
	 */
    EEnum getOscFraming();

    /**
	 * Returns the meta object for enum '{@link net.sf.smbt.osc.OscVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Version</em>'.
	 * @see net.sf.smbt.osc.OscVersion
	 * @generated
	 */
    EEnum getOscVersion();

    /**
	 * Returns the meta object for data type '{@link com.illposed.osc.OSCMessage <em>OSC Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>OSC Message</em>'.
	 * @see com.illposed.osc.OSCMessage
	 * @model instanceClass="com.illposed.osc.OSCMessage"
	 * @generated
	 */
    EDataType getOSCMessage();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    OscFactory getOscFactory();

    /**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '{@link net.sf.smbt.osc.impl.OscAtomImpl <em>Atom</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.osc.impl.OscAtomImpl
		 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscAtom()
		 * @generated
		 */
        EClass OSC_ATOM = eINSTANCE.getOscAtom();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OSC_ATOM__TYPE = eINSTANCE.getOscAtom_Type();

        /**
		 * The meta object literal for the '{@link net.sf.smbt.osc.impl.OscMetaImpl <em>Meta</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.osc.impl.OscMetaImpl
		 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscMeta()
		 * @generated
		 */
        EClass OSC_META = eINSTANCE.getOscMeta();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OSC_META__VERSION = eINSTANCE.getOscMeta_Version();

        /**
		 * The meta object literal for the '<em><b>Framing</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OSC_META__FRAMING = eINSTANCE.getOscMeta_Framing();

        /**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OSC_META__URI = eINSTANCE.getOscMeta_Uri();

        /**
		 * The meta object literal for the '<em><b>Types</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OSC_META__TYPES = eINSTANCE.getOscMeta_Types();

        /**
		 * The meta object literal for the '{@link net.sf.smbt.osc.OscType <em>Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.osc.OscType
		 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscType()
		 * @generated
		 */
        EEnum OSC_TYPE = eINSTANCE.getOscType();

        /**
		 * The meta object literal for the '{@link net.sf.smbt.osc.OscData <em>Data</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.osc.OscData
		 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscData()
		 * @generated
		 */
        EEnum OSC_DATA = eINSTANCE.getOscData();

        /**
		 * The meta object literal for the '{@link net.sf.smbt.osc.OscFraming <em>Framing</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.osc.OscFraming
		 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscFraming()
		 * @generated
		 */
        EEnum OSC_FRAMING = eINSTANCE.getOscFraming();

        /**
		 * The meta object literal for the '{@link net.sf.smbt.osc.OscVersion <em>Version</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.osc.OscVersion
		 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOscVersion()
		 * @generated
		 */
        EEnum OSC_VERSION = eINSTANCE.getOscVersion();

        /**
		 * The meta object literal for the '<em>OSC Message</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.illposed.osc.OSCMessage
		 * @see net.sf.smbt.osc.impl.OscPackageImpl#getOSCMessage()
		 * @generated
		 */
        EDataType OSC_MESSAGE = eINSTANCE.getOSCMessage();
    }
}
