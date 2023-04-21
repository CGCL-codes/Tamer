package cz.vse.gebz.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import cz.vse.gebz.AtribalniVyrok;
import cz.vse.gebz.Atribut;
import cz.vse.gebz.GebzPackage;
import cz.vse.gebz.IdentifikovatelnyObjekt;
import cz.vse.gebz.PojmenovanyObjekt;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Atribut</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.vse.gebz.impl.AtributImpl#getId <em>Id</em>}</li>
 *   <li>{@link cz.vse.gebz.impl.AtributImpl#getKomentar <em>Komentar</em>}</li>
 *   <li>{@link cz.vse.gebz.impl.AtributImpl#getNazev <em>Nazev</em>}</li>
 *   <li>{@link cz.vse.gebz.impl.AtributImpl#getVyroky <em>Vyroky</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AtributImpl extends ObjektBazeImpl implements Atribut {

    /**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected String id = ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getKomentar() <em>Komentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKomentar()
	 * @generated
	 * @ordered
	 */
    protected static final String KOMENTAR_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getKomentar() <em>Komentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKomentar()
	 * @generated
	 * @ordered
	 */
    protected String komentar = KOMENTAR_EDEFAULT;

    /**
	 * The default value of the '{@link #getNazev() <em>Nazev</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNazev()
	 * @generated
	 * @ordered
	 */
    protected static final String NAZEV_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNazev() <em>Nazev</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNazev()
	 * @generated
	 * @ordered
	 */
    protected String nazev = NAZEV_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AtributImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return GebzPackage.Literals.ATRIBUT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getId() {
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GebzPackage.ATRIBUT__ID, oldId, id));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getKomentar() {
        return komentar;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setKomentar(String newKomentar) {
        String oldKomentar = komentar;
        komentar = newKomentar;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GebzPackage.ATRIBUT__KOMENTAR, oldKomentar, komentar));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getNazev() {
        return nazev;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNazev(String newNazev) {
        String oldNazev = nazev;
        nazev = newNazev;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GebzPackage.ATRIBUT__NAZEV, oldNazev, nazev));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<AtribalniVyrok> getVyroky() {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case GebzPackage.ATRIBUT__ID:
                return getId();
            case GebzPackage.ATRIBUT__KOMENTAR:
                return getKomentar();
            case GebzPackage.ATRIBUT__NAZEV:
                return getNazev();
            case GebzPackage.ATRIBUT__VYROKY:
                return getVyroky();
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
            case GebzPackage.ATRIBUT__ID:
                setId((String) newValue);
                return;
            case GebzPackage.ATRIBUT__KOMENTAR:
                setKomentar((String) newValue);
                return;
            case GebzPackage.ATRIBUT__NAZEV:
                setNazev((String) newValue);
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
            case GebzPackage.ATRIBUT__ID:
                setId(ID_EDEFAULT);
                return;
            case GebzPackage.ATRIBUT__KOMENTAR:
                setKomentar(KOMENTAR_EDEFAULT);
                return;
            case GebzPackage.ATRIBUT__NAZEV:
                setNazev(NAZEV_EDEFAULT);
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
            case GebzPackage.ATRIBUT__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case GebzPackage.ATRIBUT__KOMENTAR:
                return KOMENTAR_EDEFAULT == null ? komentar != null : !KOMENTAR_EDEFAULT.equals(komentar);
            case GebzPackage.ATRIBUT__NAZEV:
                return NAZEV_EDEFAULT == null ? nazev != null : !NAZEV_EDEFAULT.equals(nazev);
            case GebzPackage.ATRIBUT__VYROKY:
                return !getVyroky().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == IdentifikovatelnyObjekt.class) {
            switch(derivedFeatureID) {
                case GebzPackage.ATRIBUT__ID:
                    return GebzPackage.IDENTIFIKOVATELNY_OBJEKT__ID;
                case GebzPackage.ATRIBUT__KOMENTAR:
                    return GebzPackage.IDENTIFIKOVATELNY_OBJEKT__KOMENTAR;
                default:
                    return -1;
            }
        }
        if (baseClass == PojmenovanyObjekt.class) {
            switch(derivedFeatureID) {
                case GebzPackage.ATRIBUT__NAZEV:
                    return GebzPackage.POJMENOVANY_OBJEKT__NAZEV;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == IdentifikovatelnyObjekt.class) {
            switch(baseFeatureID) {
                case GebzPackage.IDENTIFIKOVATELNY_OBJEKT__ID:
                    return GebzPackage.ATRIBUT__ID;
                case GebzPackage.IDENTIFIKOVATELNY_OBJEKT__KOMENTAR:
                    return GebzPackage.ATRIBUT__KOMENTAR;
                default:
                    return -1;
            }
        }
        if (baseClass == PojmenovanyObjekt.class) {
            switch(baseFeatureID) {
                case GebzPackage.POJMENOVANY_OBJEKT__NAZEV:
                    return GebzPackage.ATRIBUT__NAZEV;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (id: ");
        result.append(id);
        result.append(", komentar: ");
        result.append(komentar);
        result.append(", nazev: ");
        result.append(nazev);
        result.append(')');
        return result.toString();
    }
}
