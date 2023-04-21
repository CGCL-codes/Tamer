package org.remus.infomngmt.common.ui.uimodel;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.remus.infomngmnt.common.ui.extension.AbstractTraySection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tray Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getName <em>Name</em>}</li>
 *   <li>{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getImage <em>Image</em>}</li>
 *   <li>{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getDescription <em>Description</em>}</li>
 *   <li>{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getImplementation <em>Implementation</em>}</li>
 *   <li>{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getPreferenceOptions <em>Preference Options</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.remus.infomngmt.common.ui.uimodel.UIModelPackage#getTraySection()
 * @model
 * @generated
 */
public interface TraySection extends EObject {

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.remus.infomngmt.common.ui.uimodel.UIModelPackage#getTraySection_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Image</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Image</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Image</em>' attribute.
	 * @see #setImage(Image)
	 * @see org.remus.infomngmt.common.ui.uimodel.UIModelPackage#getTraySection_Image()
	 * @model dataType="org.remus.infomngmt.common.ui.uimodel.Image" transient="true"
	 * @generated
	 */
    Image getImage();

    /**
	 * Sets the value of the '{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getImage <em>Image</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Image</em>' attribute.
	 * @see #getImage()
	 * @generated
	 */
    void setImage(Image value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.remus.infomngmt.common.ui.uimodel.UIModelPackage#getTraySection_Description()
	 * @model
	 * @generated
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * Returns the value of the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implementation</em>' attribute.
	 * @see #setImplementation(AbstractTraySection)
	 * @see org.remus.infomngmt.common.ui.uimodel.UIModelPackage#getTraySection_Implementation()
	 * @model dataType="org.remus.infomngmt.common.ui.uimodel.AbstractTraySection" transient="true"
	 * @generated
	 */
    AbstractTraySection getImplementation();

    /**
	 * Sets the value of the '{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getImplementation <em>Implementation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Implementation</em>' attribute.
	 * @see #getImplementation()
	 * @generated
	 */
    void setImplementation(AbstractTraySection value);

    /**
	 * Returns the value of the '<em><b>Template Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Template Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Template Id</em>' attribute.
	 * @see #setTemplateId(String)
	 * @see org.remus.infomngmt.common.ui.uimodel.UIModelPackage#getTraySection_TemplateId()
	 * @model
	 * @generated
	 */
    String getTemplateId();

    /**
	 * Sets the value of the '{@link org.remus.infomngmt.common.ui.uimodel.TraySection#getTemplateId <em>Template Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Template Id</em>' attribute.
	 * @see #getTemplateId()
	 * @generated
	 */
    void setTemplateId(String value);

    /**
	 * Returns the value of the '<em><b>Preference Options</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Preference Options</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Preference Options</em>' map.
	 * @see org.remus.infomngmt.common.ui.uimodel.UIModelPackage#getTraySection_PreferenceOptions()
	 * @model mapType="org.remus.infomngmt.common.ui.uimodel.StringToStringMap<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 * @generated
	 */
    EMap<String, String> getPreferenceOptions();
}
