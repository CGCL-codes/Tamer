package org.germinus.telcoblocks.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.germinus.telcoblocks.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.germinus.telcoblocks.TelcoblocksPackage
 * @generated
 */
public class TelcoblocksAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static TelcoblocksPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TelcoblocksAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = TelcoblocksPackage.eINSTANCE;
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
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TelcoblocksSwitch<Adapter> modelSwitch = new TelcoblocksSwitch<Adapter>() {

        @Override
        public Adapter caseNodo(Nodo object) {
            return createNodoAdapter();
        }

        @Override
        public Adapter caseEnlace(Enlace object) {
            return createEnlaceAdapter();
        }

        @Override
        public Adapter caseRED(RED object) {
            return createREDAdapter();
        }

        @Override
        public Adapter caseNodoRED(NodoRED object) {
            return createNodoREDAdapter();
        }

        @Override
        public Adapter caseTerminal(Terminal object) {
            return createTerminalAdapter();
        }

        @Override
        public Adapter caseTelefono_VOIP(Telefono_VOIP object) {
            return createTelefono_VOIPAdapter();
        }

        @Override
        public Adapter caseSoftPhone_VOIP(SoftPhone_VOIP object) {
            return createSoftPhone_VOIPAdapter();
        }

        @Override
        public Adapter caseServidor(Servidor object) {
            return createServidorAdapter();
        }

        @Override
        public Adapter caseServidor_APP_SIP(Servidor_APP_SIP object) {
            return createServidor_APP_SIPAdapter();
        }

        @Override
        public Adapter caseServidor_VOIP(Servidor_VOIP object) {
            return createServidor_VOIPAdapter();
        }

        @Override
        public Adapter caseCentralita_VOIP(Centralita_VOIP object) {
            return createCentralita_VOIPAdapter();
        }

        @Override
        public Adapter caseMediaServer(MediaServer object) {
            return createMediaServerAdapter();
        }

        @Override
        public Adapter caseSERVICIOS(SERVICIOS object) {
            return createSERVICIOSAdapter();
        }

        @Override
        public Adapter caseNodoSERVICIOS(NodoSERVICIOS object) {
            return createNodoSERVICIOSAdapter();
        }

        @Override
        public Adapter caseEvento(Evento object) {
            return createEventoAdapter();
        }

        @Override
        public Adapter caseIM(IM object) {
            return createIMAdapter();
        }

        @Override
        public Adapter caseTelefono(Telefono object) {
            return createTelefonoAdapter();
        }

        @Override
        public Adapter caseWeb(Web object) {
            return createWebAdapter();
        }

        @Override
        public Adapter caseServicio(Servicio object) {
            return createServicioAdapter();
        }

        @Override
        public Adapter caseServicioGen(ServicioGen object) {
            return createServicioGenAdapter();
        }

        @Override
        public Adapter caseLlamadas(Llamadas object) {
            return createLlamadasAdapter();
        }

        @Override
        public Adapter caseClickToDial(ClickToDial object) {
            return createClickToDialAdapter();
        }

        @Override
        public Adapter casePersonalizacion(Personalizacion object) {
            return createPersonalizacionAdapter();
        }

        @Override
        public Adapter caseBC(BC object) {
            return createBCAdapter();
        }

        @Override
        public Adapter caseFacturacion(Facturacion object) {
            return createFacturacionAdapter();
        }

        @Override
        public Adapter caseAnuncios(Anuncios object) {
            return createAnunciosAdapter();
        }

        @Override
        public Adapter caseDesvio(Desvio object) {
            return createDesvioAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Nodo <em>Nodo</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Nodo
	 * @generated
	 */
    public Adapter createNodoAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Enlace <em>Enlace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Enlace
	 * @generated
	 */
    public Adapter createEnlaceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.RED <em>RED</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.RED
	 * @generated
	 */
    public Adapter createREDAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.NodoRED <em>Nodo RED</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.NodoRED
	 * @generated
	 */
    public Adapter createNodoREDAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Terminal <em>Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Terminal
	 * @generated
	 */
    public Adapter createTerminalAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Telefono_VOIP <em>Telefono VOIP</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Telefono_VOIP
	 * @generated
	 */
    public Adapter createTelefono_VOIPAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.SoftPhone_VOIP <em>Soft Phone VOIP</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.SoftPhone_VOIP
	 * @generated
	 */
    public Adapter createSoftPhone_VOIPAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Servidor <em>Servidor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Servidor
	 * @generated
	 */
    public Adapter createServidorAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Servidor_APP_SIP <em>Servidor APP SIP</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Servidor_APP_SIP
	 * @generated
	 */
    public Adapter createServidor_APP_SIPAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Servidor_VOIP <em>Servidor VOIP</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Servidor_VOIP
	 * @generated
	 */
    public Adapter createServidor_VOIPAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Centralita_VOIP <em>Centralita VOIP</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Centralita_VOIP
	 * @generated
	 */
    public Adapter createCentralita_VOIPAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.MediaServer <em>Media Server</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.MediaServer
	 * @generated
	 */
    public Adapter createMediaServerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.SERVICIOS <em>SERVICIOS</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.SERVICIOS
	 * @generated
	 */
    public Adapter createSERVICIOSAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.NodoSERVICIOS <em>Nodo SERVICIOS</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.NodoSERVICIOS
	 * @generated
	 */
    public Adapter createNodoSERVICIOSAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Evento <em>Evento</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Evento
	 * @generated
	 */
    public Adapter createEventoAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.IM <em>IM</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.IM
	 * @generated
	 */
    public Adapter createIMAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Telefono <em>Telefono</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Telefono
	 * @generated
	 */
    public Adapter createTelefonoAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Web <em>Web</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Web
	 * @generated
	 */
    public Adapter createWebAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Servicio <em>Servicio</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Servicio
	 * @generated
	 */
    public Adapter createServicioAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.ServicioGen <em>Servicio Gen</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.ServicioGen
	 * @generated
	 */
    public Adapter createServicioGenAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Llamadas <em>Llamadas</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Llamadas
	 * @generated
	 */
    public Adapter createLlamadasAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.ClickToDial <em>Click To Dial</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.ClickToDial
	 * @generated
	 */
    public Adapter createClickToDialAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Personalizacion <em>Personalizacion</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Personalizacion
	 * @generated
	 */
    public Adapter createPersonalizacionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.BC <em>BC</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.BC
	 * @generated
	 */
    public Adapter createBCAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Facturacion <em>Facturacion</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Facturacion
	 * @generated
	 */
    public Adapter createFacturacionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Anuncios <em>Anuncios</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Anuncios
	 * @generated
	 */
    public Adapter createAnunciosAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.germinus.telcoblocks.Desvio <em>Desvio</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.germinus.telcoblocks.Desvio
	 * @generated
	 */
    public Adapter createDesvioAdapter() {
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
