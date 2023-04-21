package ocp.service.impl;

import java.util.HashMap;
import java.util.Vector;
import ocp.service.ContextConsumer;
import ocp.service.ContextProducer;
import ocp.context.Context;
import ocp.management.OntologyManagement;

/**
 * Gestión de usuarios(producers y consumers) de contextos
 * @author Paco Lopez Marmol
 * 
 */
public class ContextManagement {

    private static ContextManagement instance;

    private OntologyManagement om;

    private HashMap<String, Vector<ContextConsumer>> listeners;

    private HashMap<String, Vector<ContextConsumer>> contextTypeListeners;

    private Vector<ContextProducer> producers;

    private Vector<ContextConsumer> consumers;

    /**
     * Constructor. Inicializa los datos. Actualmente en memoria.
     * (Deberia usar persistencia.)
     * @param om OntologyManagement
     */
    private ContextManagement(OntologyManagement om) {
        this.om = om;
        listeners = new HashMap<String, Vector<ContextConsumer>>();
        contextTypeListeners = new HashMap<String, Vector<ContextConsumer>>();
        producers = new Vector<ContextProducer>();
        consumers = new Vector<ContextConsumer>();
    }

    /**
     * Crea un singleton 
     * @return ContextManagement
     */
    public static ContextManagement getInstance(OntologyManagement om) {
        if (instance == null) {
            instance = new ContextManagement(om);
        }
        return instance;
    }

    /**
     * Subscribe un oyente(ContextConsumer) a un tipo de contexto
     * @param tipo tipo del contexto (ej. "Medicamento" para contextos de tipo "Medicamento-12132")
     * @param cl Oyente que implementa la interfaz ContextConsumer
     */
    public void registerToContextType(String type, ContextConsumer cl) {
        synchronized (contextTypeListeners) {
            Vector<ContextConsumer> listenersId = contextTypeListeners.get(type);
            if (listenersId == null) {
                listenersId = new Vector<ContextConsumer>();
                contextTypeListeners.put(type, listenersId);
            }
            if (!listenersId.contains(cl)) listenersId.add(cl);
        }
    }

    /**
     * Subscribe un oyente(ContextConsumer) a un contexto
     * @param id identificador del contexto
     * @param cl Oyente que implementa la interfaz ContextConsumer
     */
    public void register(String id, ContextConsumer cl) {
        synchronized (listeners) {
            Vector<ContextConsumer> listenersId = listeners.get(id);
            if (listenersId == null) {
                listenersId = new Vector<ContextConsumer>();
                listeners.put(id, listenersId);
            }
            if (!listenersId.contains(cl)) listenersId.add(cl);
        }
    }

    /**
     * Quita la subscripción de un oyente(ContextConsumer) a un contexto
     * @param id identificador del contexto
     * @param cl Oyente que implementa la interfaz ContextConsumer
     */
    public void unregister(String id, ContextConsumer cl) {
        synchronized (listeners) {
            Vector<ContextConsumer> listenersId = listeners.get(id);
            if (listenersId == null) {
                listenersId = new Vector<ContextConsumer>();
                listeners.put(id, listenersId);
                return;
            }
            listenersId.remove(cl);
        }
    }

    /**
     * Escribe un contexto en el repositorio de contextos y notifica a los
     * oyentes de ese contexto. Si el contexto es nuevo notifica a consumers.
     * @param context El contexto a escribir
     */
    public void write(Context context) {
        String id = context.getId();
        boolean nuevo;
        nuevo = false;
        if (om.writeContext(context)) {
            nuevo = true;
            synchronized (consumers) {
                for (ContextConsumer consumer : consumers) {
                    consumer.notifyNewContext(context);
                }
            }
        }
        if (!nuevo) {
            synchronized (listeners) {
                Vector<ContextConsumer> listenersId = listeners.get(id);
                if (listenersId != null) {
                    for (ContextConsumer listener : listenersId) {
                        listener.notifyContextChange(context);
                    }
                }
            }
            int c = id.indexOf("-");
            String contextType = id.substring(0, c);
            synchronized (contextTypeListeners) {
                Vector<ContextConsumer> l = contextTypeListeners.get(contextType);
                if (l != null) {
                    for (ContextConsumer listener : l) {
                        listener.notifyContextChange(context);
                    }
                }
            }
        }
    }

    /**
     * Obtiene un contexto mediante su id
     * @param id el identificador del contexto
     * @return el contexto con identificador id o null si el contexto no existe
     */
    public Context read(String id) {
        Context c;
        c = om.readContext(id);
        return c;
    }

    /**
     * Obtiene el número de contextos almacenados en el historico de un 
     * contexto concreto.
     * @param id Identificador del contexto
     * @return Número de contextos almacenados
     */
    public int getHistoricalContextLenght(String id) {
        return om.getCountHistorical(id);
    }

    /** 
     * Lee los últimos valores del historico de un contexto
     * @param id Identificador del contexto
     * @param nitems Número de contextos a leer
     * @return Un vector de contextos ordenado de menos reciente a más reciente
     */
    public Vector<Context> readHistoricalContextLast(String id, int nitems) {
        return om.readHistoricalContextLast(id, nitems);
    }

    public boolean consumerAdd(ContextConsumer cs) {
        synchronized (consumers) {
            return consumers.add(cs);
        }
    }

    /**
     * Un ContextConsumer desaparece del sistema
     * @param cs el ContextConsumer que desaparece
     * @return true si todo va bien
     */
    public boolean consumerDel(ContextConsumer cs) {
        synchronized (listeners) {
            for (Vector<ContextConsumer> listenersId : listeners.values()) {
                if (listenersId != null) {
                    listenersId.remove(cs);
                }
            }
        }
        synchronized (contextTypeListeners) {
            for (Vector<ContextConsumer> l : contextTypeListeners.values()) {
                if (l != null) {
                    l.remove(cs);
                }
            }
        }
        synchronized (consumers) {
            return consumers.remove(cs);
        }
    }

    public boolean producerAdd(ContextProducer cp) {
        synchronized (producers) {
            return producers.add(cp);
        }
    }

    public boolean producerDel(ContextProducer cp) {
        synchronized (producers) {
            return producers.remove(cp);
        }
    }

    /**
     * El servicio debe terminar. Avisa a los usuarios de los servicios
     */
    void terminate() {
        synchronized (producers) {
            for (ContextProducer producer : producers) {
                producer.deactivate();
            }
        }
        synchronized (consumers) {
            for (ContextConsumer consumer : consumers) {
                consumer.deactivate();
            }
        }
    }
}
