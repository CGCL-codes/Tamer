package de.mpiwg.vspace.metamodel.transformed.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import de.mpiwg.vspace.metamodel.transformed.Image;
import de.mpiwg.vspace.metamodel.transformed.ImageType;
import de.mpiwg.vspace.metamodel.transformed.TransformedPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Image</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ImageImpl#getImagePath <em>Image Path</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ImageImpl#getWidth <em>Width</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ImageImpl#getHeight <em>Height</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ImageImpl#getType <em>Type</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ImageImpl#getFilename <em>Filename</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ImageImpl#getFileextension <em>Fileextension</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImageImpl extends MediaImpl implements Image {

    /**
	 * The default value of the '{@link #getImagePath() <em>Image Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImagePath()
	 * @generated
	 * @ordered
	 */
    protected static final String IMAGE_PATH_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getImagePath() <em>Image Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImagePath()
	 * @generated
	 * @ordered
	 */
    protected String imagePath = IMAGE_PATH_EDEFAULT;

    /**
	 * The default value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
    protected static final int WIDTH_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
    protected int width = WIDTH_EDEFAULT;

    /**
	 * The default value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
    protected static final int HEIGHT_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
    protected int height = HEIGHT_EDEFAULT;

    /**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected static final ImageType TYPE_EDEFAULT = ImageType.LOCAL;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected ImageType type = TYPE_EDEFAULT;

    /**
	 * The default value of the '{@link #getFilename() <em>Filename</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilename()
	 * @generated
	 * @ordered
	 */
    protected static final String FILENAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getFilename() <em>Filename</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilename()
	 * @generated
	 * @ordered
	 */
    protected String filename = FILENAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getFileextension() <em>Fileextension</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileextension()
	 * @generated
	 * @ordered
	 */
    protected static final String FILEEXTENSION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getFileextension() <em>Fileextension</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileextension()
	 * @generated
	 * @ordered
	 */
    protected String fileextension = FILEEXTENSION_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ImageImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TransformedPackage.Literals.IMAGE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getImagePath() {
        return imagePath;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setImagePath(String newImagePath) {
        String oldImagePath = imagePath;
        imagePath = newImagePath;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.IMAGE__IMAGE_PATH, oldImagePath, imagePath));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setWidth(int newWidth) {
        int oldWidth = width;
        width = newWidth;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.IMAGE__WIDTH, oldWidth, width));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHeight(int newHeight) {
        int oldHeight = height;
        height = newHeight;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.IMAGE__HEIGHT, oldHeight, height));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ImageType getType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(ImageType newType) {
        ImageType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.IMAGE__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFilename(String newFilename) {
        String oldFilename = filename;
        filename = newFilename;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.IMAGE__FILENAME, oldFilename, filename));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getFileextension() {
        return fileextension;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFileextension(String newFileextension) {
        String oldFileextension = fileextension;
        fileextension = newFileextension;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.IMAGE__FILEEXTENSION, oldFileextension, fileextension));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TransformedPackage.IMAGE__IMAGE_PATH:
                return getImagePath();
            case TransformedPackage.IMAGE__WIDTH:
                return getWidth();
            case TransformedPackage.IMAGE__HEIGHT:
                return getHeight();
            case TransformedPackage.IMAGE__TYPE:
                return getType();
            case TransformedPackage.IMAGE__FILENAME:
                return getFilename();
            case TransformedPackage.IMAGE__FILEEXTENSION:
                return getFileextension();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case TransformedPackage.IMAGE__IMAGE_PATH:
                setImagePath((String) newValue);
                return;
            case TransformedPackage.IMAGE__WIDTH:
                setWidth((Integer) newValue);
                return;
            case TransformedPackage.IMAGE__HEIGHT:
                setHeight((Integer) newValue);
                return;
            case TransformedPackage.IMAGE__TYPE:
                setType((ImageType) newValue);
                return;
            case TransformedPackage.IMAGE__FILENAME:
                setFilename((String) newValue);
                return;
            case TransformedPackage.IMAGE__FILEEXTENSION:
                setFileextension((String) newValue);
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
            case TransformedPackage.IMAGE__IMAGE_PATH:
                setImagePath(IMAGE_PATH_EDEFAULT);
                return;
            case TransformedPackage.IMAGE__WIDTH:
                setWidth(WIDTH_EDEFAULT);
                return;
            case TransformedPackage.IMAGE__HEIGHT:
                setHeight(HEIGHT_EDEFAULT);
                return;
            case TransformedPackage.IMAGE__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case TransformedPackage.IMAGE__FILENAME:
                setFilename(FILENAME_EDEFAULT);
                return;
            case TransformedPackage.IMAGE__FILEEXTENSION:
                setFileextension(FILEEXTENSION_EDEFAULT);
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
            case TransformedPackage.IMAGE__IMAGE_PATH:
                return IMAGE_PATH_EDEFAULT == null ? imagePath != null : !IMAGE_PATH_EDEFAULT.equals(imagePath);
            case TransformedPackage.IMAGE__WIDTH:
                return width != WIDTH_EDEFAULT;
            case TransformedPackage.IMAGE__HEIGHT:
                return height != HEIGHT_EDEFAULT;
            case TransformedPackage.IMAGE__TYPE:
                return type != TYPE_EDEFAULT;
            case TransformedPackage.IMAGE__FILENAME:
                return FILENAME_EDEFAULT == null ? filename != null : !FILENAME_EDEFAULT.equals(filename);
            case TransformedPackage.IMAGE__FILEEXTENSION:
                return FILEEXTENSION_EDEFAULT == null ? fileextension != null : !FILEEXTENSION_EDEFAULT.equals(fileextension);
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
        result.append(" (imagePath: ");
        result.append(imagePath);
        result.append(", width: ");
        result.append(width);
        result.append(", height: ");
        result.append(height);
        result.append(", type: ");
        result.append(type);
        result.append(", filename: ");
        result.append(filename);
        result.append(", fileextension: ");
        result.append(fileextension);
        result.append(')');
        return result.toString();
    }
}
