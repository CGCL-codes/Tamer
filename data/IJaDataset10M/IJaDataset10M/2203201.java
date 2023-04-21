package de.morknet.mrw.metamodel.impl;

import de.morknet.mrw.metamodel.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelrailwayFactoryImpl extends EFactoryImpl implements ModelrailwayFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static ModelrailwayFactory init() {
        try {
            ModelrailwayFactory theModelrailwayFactory = (ModelrailwayFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.morknet.de/Modelrailway");
            if (theModelrailwayFactory != null) {
                return theModelrailwayFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ModelrailwayFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelrailwayFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case ModelrailwayPackage.ANSCHLUSS:
                return createAnschluss();
            case ModelrailwayPackage.GLEISABSCHNITT:
                return createGleisabschnitt();
            case ModelrailwayPackage.MODELL:
                return createModell();
            case ModelrailwayPackage.CONTROLLER:
                return createController();
            case ModelrailwayPackage.GLEISMODUL:
                return createGleismodul();
            case ModelrailwayPackage.LICHT:
                return createLicht();
            case ModelrailwayPackage.AUSFAHRSIGNAL:
                return createAusfahrsignal();
            case ModelrailwayPackage.BAHNHOF:
                return createBahnhof();
            case ModelrailwayPackage.IMPULSMODUL:
                return createImpulsmodul();
            case ModelrailwayPackage.EINFAHRSIGNAL:
                return createEinfahrsignal();
            case ModelrailwayPackage.WEICHE:
                return createWeiche();
            case ModelrailwayPackage.FORMVORSIGNAL:
                return createFormvorsignal();
            case ModelrailwayPackage.GLEIS:
                return createGleis();
            case ModelrailwayPackage.BLOCKSIGNAL:
                return createBlocksignal();
            case ModelrailwayPackage.VORSIGNAL:
                return createVorsignal();
            case ModelrailwayPackage.DKW:
                return createDKW();
            case ModelrailwayPackage.GLEISSPERRSIGNAL:
                return createGleissperrsignal();
            case ModelrailwayPackage.FORMHAUPTSIGNAL:
                return createFormhauptsignal();
            case ModelrailwayPackage.FORMGLEISSPERRSIGNAL:
                return createFormgleissperrsignal();
            case ModelrailwayPackage.STRECKE:
                return createStrecke();
            case ModelrailwayPackage.BELEUCHTUNGSMODUL:
                return createBeleuchtungsmodul();
            case ModelrailwayPackage.LAMPE:
                return createLampe();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Anschluss createAnschluss() {
        AnschlussImpl anschluss = new AnschlussImpl();
        return anschluss;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Gleisabschnitt createGleisabschnitt() {
        GleisabschnittImpl gleisabschnitt = new GleisabschnittImpl();
        return gleisabschnitt;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Modell createModell() {
        ModellImpl modell = new ModellImpl();
        return modell;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Controller createController() {
        ControllerImpl controller = new ControllerImpl();
        return controller;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Gleismodul createGleismodul() {
        GleismodulImpl gleismodul = new GleismodulImpl();
        return gleismodul;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Licht createLicht() {
        LichtImpl licht = new LichtImpl();
        return licht;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Ausfahrsignal createAusfahrsignal() {
        AusfahrsignalImpl ausfahrsignal = new AusfahrsignalImpl();
        return ausfahrsignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Bahnhof createBahnhof() {
        BahnhofImpl bahnhof = new BahnhofImpl();
        return bahnhof;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Impulsmodul createImpulsmodul() {
        ImpulsmodulImpl impulsmodul = new ImpulsmodulImpl();
        return impulsmodul;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Einfahrsignal createEinfahrsignal() {
        EinfahrsignalImpl einfahrsignal = new EinfahrsignalImpl();
        return einfahrsignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Weiche createWeiche() {
        WeicheImpl weiche = new WeicheImpl();
        return weiche;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Formvorsignal createFormvorsignal() {
        FormvorsignalImpl formvorsignal = new FormvorsignalImpl();
        return formvorsignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Gleis createGleis() {
        GleisImpl gleis = new GleisImpl();
        return gleis;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Blocksignal createBlocksignal() {
        BlocksignalImpl blocksignal = new BlocksignalImpl();
        return blocksignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Vorsignal createVorsignal() {
        VorsignalImpl vorsignal = new VorsignalImpl();
        return vorsignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DKW createDKW() {
        DKWImpl dkw = new DKWImpl();
        return dkw;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Gleissperrsignal createGleissperrsignal() {
        GleissperrsignalImpl gleissperrsignal = new GleissperrsignalImpl();
        return gleissperrsignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Formhauptsignal createFormhauptsignal() {
        FormhauptsignalImpl formhauptsignal = new FormhauptsignalImpl();
        return formhauptsignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Formgleissperrsignal createFormgleissperrsignal() {
        FormgleissperrsignalImpl formgleissperrsignal = new FormgleissperrsignalImpl();
        return formgleissperrsignal;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Strecke createStrecke() {
        StreckeImpl strecke = new StreckeImpl();
        return strecke;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Beleuchtungsmodul createBeleuchtungsmodul() {
        BeleuchtungsmodulImpl beleuchtungsmodul = new BeleuchtungsmodulImpl();
        return beleuchtungsmodul;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Lampe createLampe() {
        LampeImpl lampe = new LampeImpl();
        return lampe;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelrailwayPackage getModelrailwayPackage() {
        return (ModelrailwayPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static ModelrailwayPackage getPackage() {
        return ModelrailwayPackage.eINSTANCE;
    }
}
