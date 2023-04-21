package cz.vse.gebz.util;

import cz.vse.gebz.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see cz.vse.gebz.GebzPackage
 * @generated
 */
public class GebzAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static GebzPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public GebzAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = GebzPackage.eINSTANCE;
        }
    }

    /**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected GebzSwitch<Adapter> modelSwitch = new GebzSwitch<Adapter>() {

        @Override
        public Adapter caseVyrok(Vyrok object) {
            return createVyrokAdapter();
        }

        @Override
        public Adapter caseIdentifikovatelnyObjekt(IdentifikovatelnyObjekt object) {
            return createIdentifikovatelnyObjektAdapter();
        }

        @Override
        public Adapter casePojmenovanyObjekt(PojmenovanyObjekt object) {
            return createPojmenovanyObjektAdapter();
        }

        @Override
        public Adapter caseObjektBaze(ObjektBaze object) {
            return createObjektBazeAdapter();
        }

        @Override
        public Adapter caseAbstraktniSpoj(AbstraktniSpoj object) {
            return createAbstraktniSpojAdapter();
        }

        @Override
        public Adapter caseBazeZnalosti(BazeZnalosti object) {
            return createBazeZnalostiAdapter();
        }

        @Override
        public Adapter caseAtribut(Atribut object) {
            return createAtributAdapter();
        }

        @Override
        public Adapter caseAtribalniVyrok(AtribalniVyrok object) {
            return createAtribalniVyrokAdapter();
        }

        @Override
        public Adapter caseBinarniAtribut(BinarniAtribut object) {
            return createBinarniAtributAdapter();
        }

        @Override
        public Adapter caseKonstantniAtribut(KonstantniAtribut object) {
            return createKonstantniAtributAdapter();
        }

        @Override
        public Adapter caseNominalniAtribut(NominalniAtribut object) {
            return createNominalniAtributAdapter();
        }

        @Override
        public Adapter caseNominalniAtribalniVyrok(NominalniAtribalniVyrok object) {
            return createNominalniAtribalniVyrokAdapter();
        }

        @Override
        public Adapter caseNumerickyAtribut(NumerickyAtribut object) {
            return createNumerickyAtributAdapter();
        }

        @Override
        public Adapter caseNumerickyAtribalniVyrok(NumerickyAtribalniVyrok object) {
            return createNumerickyAtribalniVyrokAdapter();
        }

        @Override
        public Adapter caseVahovaFunkce(VahovaFunkce object) {
            return createVahovaFunkceAdapter();
        }

        @Override
        public Adapter caseAbstraktniLogickySpoj(AbstraktniLogickySpoj object) {
            return createAbstraktniLogickySpojAdapter();
        }

        @Override
        public Adapter casePravidlo(Pravidlo object) {
            return createPravidloAdapter();
        }

        @Override
        public Adapter caseSpoj(Spoj object) {
            return createSpojAdapter();
        }

        @Override
        public Adapter caseLogickePravidlo(LogickePravidlo object) {
            return createLogickePravidloAdapter();
        }

        @Override
        public Adapter caseKompozicionalniPravidlo(KompozicionalniPravidlo object) {
            return createKompozicionalniPravidloAdapter();
        }

        @Override
        public Adapter caseKontext(Kontext object) {
            return createKontextAdapter();
        }

        @Override
        public Adapter caseOperace(Operace object) {
            return createOperaceAdapter();
        }

        @Override
        public Adapter caseDisjunkce(Disjunkce object) {
            return createDisjunkceAdapter();
        }

        @Override
        public Adapter caseKonjunkce(Konjunkce object) {
            return createKonjunkceAdapter();
        }

        @Override
        public Adapter caseVaha(Vaha object) {
            return createVahaAdapter();
        }

        @Override
        public Adapter casePopisBaze(PopisBaze object) {
            return createPopisBazeAdapter();
        }

        @Override
        public Adapter caseRozhodovaciMechanizmus(RozhodovaciMechanizmus object) {
            return createRozhodovaciMechanizmusAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Vyrok <em>Vyrok</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Vyrok
	 * @generated
	 */
    public Adapter createVyrokAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.IdentifikovatelnyObjekt <em>Identifikovatelny Objekt</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.IdentifikovatelnyObjekt
	 * @generated
	 */
    public Adapter createIdentifikovatelnyObjektAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.PojmenovanyObjekt <em>Pojmenovany Objekt</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.PojmenovanyObjekt
	 * @generated
	 */
    public Adapter createPojmenovanyObjektAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.ObjektBaze <em>Objekt Baze</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.ObjektBaze
	 * @generated
	 */
    public Adapter createObjektBazeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.AbstraktniSpoj <em>Abstraktni Spoj</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.AbstraktniSpoj
	 * @generated
	 */
    public Adapter createAbstraktniSpojAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.RozhodovaciMechanizmus <em>Rozhodovaci Mechanizmus</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.RozhodovaciMechanizmus
	 * @generated
	 */
    public Adapter createRozhodovaciMechanizmusAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Vaha <em>Vaha</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Vaha
	 * @generated
	 */
    public Adapter createVahaAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Pravidlo <em>Pravidlo</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Pravidlo
	 * @generated
	 */
    public Adapter createPravidloAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Spoj <em>Spoj</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Spoj
	 * @generated
	 */
    public Adapter createSpojAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Operace <em>Operace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Operace
	 * @generated
	 */
    public Adapter createOperaceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.AtribalniVyrok <em>Atribalni Vyrok</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.AtribalniVyrok
	 * @generated
	 */
    public Adapter createAtribalniVyrokAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.NominalniAtribalniVyrok <em>Nominalni Atribalni Vyrok</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.NominalniAtribalniVyrok
	 * @generated
	 */
    public Adapter createNominalniAtribalniVyrokAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Atribut <em>Atribut</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Atribut
	 * @generated
	 */
    public Adapter createAtributAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.BazeZnalosti <em>Baze Znalosti</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.BazeZnalosti
	 * @generated
	 */
    public Adapter createBazeZnalostiAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.PopisBaze <em>Popis Baze</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.PopisBaze
	 * @generated
	 */
    public Adapter createPopisBazeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.BinarniAtribut <em>Binarni Atribut</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.BinarniAtribut
	 * @generated
	 */
    public Adapter createBinarniAtributAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Disjunkce <em>Disjunkce</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Disjunkce
	 * @generated
	 */
    public Adapter createDisjunkceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Konjunkce <em>Konjunkce</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Konjunkce
	 * @generated
	 */
    public Adapter createKonjunkceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.NominalniAtribut <em>Nominalni Atribut</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.NominalniAtribut
	 * @generated
	 */
    public Adapter createNominalniAtributAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.NumerickyAtribalniVyrok <em>Numericky Atribalni Vyrok</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.NumerickyAtribalniVyrok
	 * @generated
	 */
    public Adapter createNumerickyAtribalniVyrokAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.NumerickyAtribut <em>Numericky Atribut</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.NumerickyAtribut
	 * @generated
	 */
    public Adapter createNumerickyAtributAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.VahovaFunkce <em>Vahova Funkce</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.VahovaFunkce
	 * @generated
	 */
    public Adapter createVahovaFunkceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.AbstraktniLogickySpoj <em>Abstraktni Logicky Spoj</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.AbstraktniLogickySpoj
	 * @generated
	 */
    public Adapter createAbstraktniLogickySpojAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.LogickePravidlo <em>Logicke Pravidlo</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.LogickePravidlo
	 * @generated
	 */
    public Adapter createLogickePravidloAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.Kontext <em>Kontext</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.Kontext
	 * @generated
	 */
    public Adapter createKontextAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.KompozicionalniPravidlo <em>Kompozicionalni Pravidlo</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.KompozicionalniPravidlo
	 * @generated
	 */
    public Adapter createKompozicionalniPravidloAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link cz.vse.gebz.KonstantniAtribut <em>Konstantni Atribut</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.vse.gebz.KonstantniAtribut
	 * @generated
	 */
    public Adapter createKonstantniAtributAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
