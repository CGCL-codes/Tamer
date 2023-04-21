package de.mpiwg.vspace.metamodel.transformed.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import de.mpiwg.vspace.metamodel.transformed.ExhibitionMap;
import de.mpiwg.vspace.metamodel.transformed.ExhibitionModuleLink;
import de.mpiwg.vspace.metamodel.transformed.Image;
import de.mpiwg.vspace.metamodel.transformed.SceneLink;
import de.mpiwg.vspace.metamodel.transformed.TransformedPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exhibition Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ExhibitionMapImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ExhibitionMapImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ExhibitionMapImpl#getMapImage <em>Map Image</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ExhibitionMapImpl#getLinksToScenes <em>Links To Scenes</em>}</li>
 *   <li>{@link de.mpiwg.vspace.metamodel.transformed.impl.ExhibitionMapImpl#getLinksToExhibitionModules <em>Links To Exhibition Modules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExhibitionMapImpl extends EObjectImpl implements ExhibitionMap {

    /**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected static final String TITLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected String title = TITLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
	 * The cached value of the '{@link #getMapImage() <em>Map Image</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapImage()
	 * @generated
	 * @ordered
	 */
    protected Image mapImage;

    /**
	 * The cached value of the '{@link #getLinksToScenes() <em>Links To Scenes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinksToScenes()
	 * @generated
	 * @ordered
	 */
    protected EList<SceneLink> linksToScenes;

    /**
	 * The cached value of the '{@link #getLinksToExhibitionModules() <em>Links To Exhibition Modules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinksToExhibitionModules()
	 * @generated
	 * @ordered
	 */
    protected EList<ExhibitionModuleLink> linksToExhibitionModules;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ExhibitionMapImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TransformedPackage.Literals.EXHIBITION_MAP;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTitle(String newTitle) {
        String oldTitle = title;
        title = newTitle;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.EXHIBITION_MAP__TITLE, oldTitle, title));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
        String oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.EXHIBITION_MAP__DESCRIPTION, oldDescription, description));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Image getMapImage() {
        return mapImage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMapImage(Image newMapImage, NotificationChain msgs) {
        Image oldMapImage = mapImage;
        mapImage = newMapImage;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TransformedPackage.EXHIBITION_MAP__MAP_IMAGE, oldMapImage, newMapImage);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMapImage(Image newMapImage) {
        if (newMapImage != mapImage) {
            NotificationChain msgs = null;
            if (mapImage != null) msgs = ((InternalEObject) mapImage).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TransformedPackage.EXHIBITION_MAP__MAP_IMAGE, null, msgs);
            if (newMapImage != null) msgs = ((InternalEObject) newMapImage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TransformedPackage.EXHIBITION_MAP__MAP_IMAGE, null, msgs);
            msgs = basicSetMapImage(newMapImage, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TransformedPackage.EXHIBITION_MAP__MAP_IMAGE, newMapImage, newMapImage));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<SceneLink> getLinksToScenes() {
        if (linksToScenes == null) {
            linksToScenes = new EObjectContainmentEList<SceneLink>(SceneLink.class, this, TransformedPackage.EXHIBITION_MAP__LINKS_TO_SCENES);
        }
        return linksToScenes;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<ExhibitionModuleLink> getLinksToExhibitionModules() {
        if (linksToExhibitionModules == null) {
            linksToExhibitionModules = new EObjectContainmentEList<ExhibitionModuleLink>(ExhibitionModuleLink.class, this, TransformedPackage.EXHIBITION_MAP__LINKS_TO_EXHIBITION_MODULES);
        }
        return linksToExhibitionModules;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TransformedPackage.EXHIBITION_MAP__MAP_IMAGE:
                return basicSetMapImage(null, msgs);
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_SCENES:
                return ((InternalEList<?>) getLinksToScenes()).basicRemove(otherEnd, msgs);
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_EXHIBITION_MODULES:
                return ((InternalEList<?>) getLinksToExhibitionModules()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TransformedPackage.EXHIBITION_MAP__TITLE:
                return getTitle();
            case TransformedPackage.EXHIBITION_MAP__DESCRIPTION:
                return getDescription();
            case TransformedPackage.EXHIBITION_MAP__MAP_IMAGE:
                return getMapImage();
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_SCENES:
                return getLinksToScenes();
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_EXHIBITION_MODULES:
                return getLinksToExhibitionModules();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case TransformedPackage.EXHIBITION_MAP__TITLE:
                setTitle((String) newValue);
                return;
            case TransformedPackage.EXHIBITION_MAP__DESCRIPTION:
                setDescription((String) newValue);
                return;
            case TransformedPackage.EXHIBITION_MAP__MAP_IMAGE:
                setMapImage((Image) newValue);
                return;
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_SCENES:
                getLinksToScenes().clear();
                getLinksToScenes().addAll((Collection<? extends SceneLink>) newValue);
                return;
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_EXHIBITION_MODULES:
                getLinksToExhibitionModules().clear();
                getLinksToExhibitionModules().addAll((Collection<? extends ExhibitionModuleLink>) newValue);
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
            case TransformedPackage.EXHIBITION_MAP__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case TransformedPackage.EXHIBITION_MAP__DESCRIPTION:
                setDescription(DESCRIPTION_EDEFAULT);
                return;
            case TransformedPackage.EXHIBITION_MAP__MAP_IMAGE:
                setMapImage((Image) null);
                return;
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_SCENES:
                getLinksToScenes().clear();
                return;
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_EXHIBITION_MODULES:
                getLinksToExhibitionModules().clear();
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
            case TransformedPackage.EXHIBITION_MAP__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case TransformedPackage.EXHIBITION_MAP__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
            case TransformedPackage.EXHIBITION_MAP__MAP_IMAGE:
                return mapImage != null;
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_SCENES:
                return linksToScenes != null && !linksToScenes.isEmpty();
            case TransformedPackage.EXHIBITION_MAP__LINKS_TO_EXHIBITION_MODULES:
                return linksToExhibitionModules != null && !linksToExhibitionModules.isEmpty();
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
        result.append(" (title: ");
        result.append(title);
        result.append(", description: ");
        result.append(description);
        result.append(')');
        return result.toString();
    }
}
