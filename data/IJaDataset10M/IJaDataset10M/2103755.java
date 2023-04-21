package ar.com.fdvs.dgarcia.spaceconf;

import java.util.Iterator;
import ar.com.fdvs.dgarcia.sistema.Sistema;
import ar.com.fdvs.dgarcia.spaces.DefaultSpaceController;
import ar.com.fdvs.dgarcia.spaces.SpaceController;

/**
 * Esta clase registra la configuracion de instancias a traves del modelo de espacios. Mediante
 * espacios se pueden definir areas en las cuales las intancias interactuen entre s�, sin estar
 * fuertemente acopladas.
 * 
 * @author D. Garcia
 */
public class SpaceConfiguration {

    /**
	 * Singleton
	 */
    private static SpaceConfiguration instance;

    /**
	 * Controlador base para manejar las relaciones espaciales
	 */
    private final SpaceController spaceController = new DefaultSpaceController();

    /**
	 * Resuelve a una instancia del tipo pedido que se encuentre en el mismo espacio que la
	 * instancia pasada
	 * 
	 * @param <T>
	 *            Tipo del objeto pedido
	 * @param contained
	 *            Objeto ya contenido en un espacio que se utilizara para determinar el espacio en
	 *            el que se debe buscar
	 * @param expectedType
	 *            Tipo de la instancia pedida
	 * @return Una instancia cuya clase es la pedida o la primera instancia que sea assignable al
	 *         tipo pedido o null si no hay ninguna en el espacio indicado
	 */
    public <T> T findInSameSpaceAs(Object contained, Class<T> expectedType) {
        Object container = this.getSpaceController().getContainerOf(contained);
        return this.findInside(container, expectedType);
    }

    /**
	 * Busca dentro del espacio indicado una instancia del tipo pedido
	 * 
	 * @param <T>
	 *            Tipo de la instancia buscada
	 * @param container
	 *            Espacio contenedor de instancias dentro de la que se quiere buscar.
	 * @param expectedType
	 * @return Una instancia cuya clase es la pedida o la primera instancia que sea assignable al
	 *         tipo pedido o null si no hay ninguna en el espacio indicado
	 */
    @SuppressWarnings("unchecked")
    public <T> T findInside(Object container, Class<T> expectedType) {
        Object candidate = null;
        synchronized (this.getSpaceController()) {
            Iterator<Object> containedInstances = this.getSpaceController().getContainedIn(container);
            while (containedInstances.hasNext()) {
                Object containedInstance = containedInstances.next();
                Class<? extends Object> instanceType = containedInstance.getClass();
                if (instanceType.equals(expectedType)) {
                    return (T) containedInstance;
                }
                if (expectedType.isAssignableFrom(instanceType) && (candidate == null)) {
                    candidate = containedInstance;
                }
            }
        }
        return (T) candidate;
    }

    /**
	 * Devuelve el controlador de espacios utilizado por esta instancia
	 * 
	 * @return the spaceController
	 */
    private SpaceController getSpaceController() {
        return this.spaceController;
    }

    /**
	 * Agrega la instancia pasada en el mismo espacio que la indicada como referente. La instancia
	 * usada de referencia debe estar registrada en un espacio
	 * 
	 * @param containedReferent
	 *            Instancia contenida en otra que se utiliza de referencia para agregar la nueva
	 *            instancia
	 * @param newContained
	 *            Instancia a agregar como contenida dentro de otra
	 */
    public void putInSameSpaceAs(Object containedReferent, Object newContained) {
        Object container = this.getSpaceController().getContainerOf(containedReferent);
        if (container == null) {
            throw new IllegalArgumentException("Instance [" + containedReferent + "] doesn't have a containment space");
        }
        this.putInside(container, newContained);
    }

    /**
	 * Establece una relacion de contencion en la que se define un objeto que actua como espacio de
	 * otro
	 * 
	 * @param container
	 *            Objeto contenedor que definira un area
	 * @param contained
	 *            Objeto contenido que interactuara con otros en el mismo espacio
	 */
    public void putInside(Object container, Object contained) {
        synchronized (this.getSpaceController()) {
            this.getSpaceController().setContainerOf(contained, container);
        }
    }

    /**
	 * Elimina la relacion de contencion entre las instancias pasadas
	 * 
	 * @param container
	 *            Contenedor actual de la instancia contenida
	 * @param contained
	 *            Instancia contenida que dejara de serlo
	 */
    public void removeFrom(Object container, Object contained) {
        synchronized (this.getSpaceController()) {
            this.getSpaceController().removeContainedFrom(container, contained);
        }
    }

    /**
	 * Configura la instancia singleton para que incluya al sistema como un espacio. De esta manera
	 * se define recursivamente el tope del sistema
	 * 
	 * @param createdInstance
	 *            Singleton recien creado
	 */
    private static void configureSingleton(SpaceConfiguration createdInstance) {
        createdInstance.putInside(Sistema.class, Sistema.class);
    }

    /**
	 * Devuelve la instancia que define las configuraciones generales aplicables a todo el sistema.
	 * A partir de esta instancia se pueden definir sub espacios con sub configuraciones
	 * 
	 * @return La instancia singleton de esta clase
	 */
    public static SpaceConfiguration getInstance() {
        if (instance == null) {
            synchronized (SpaceConfiguration.class) {
                if (instance != null) {
                    return instance;
                }
                instance = new SpaceConfiguration();
                configureSingleton(instance);
            }
        }
        return instance;
    }
}
