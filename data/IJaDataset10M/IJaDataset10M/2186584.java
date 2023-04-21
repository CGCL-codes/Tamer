package threading;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Pool de threads con lista de tareas pendientes. Si no hay threads disponibles, se intenta crear uno nuevo.
 * Si no puede crearse por exceder el m�ximo, se encola la tarea a ejecutar hasta que un thread vuelva al pool.
 * @author alberto
 *
 */
public class ThreadPool {

    /**Threads disponibles para uso */
    ArrayList<Thread> hilosLibres;

    /**Tareas pendientes de ser ejecutadas*/
    LinkedList<Runnable> tareasPendientes;

    /** The maximum number of threads at any moment. */
    int max_num_threads;

    /**M�ximo n�mero de hilos libres que puede haber*/
    int maxHilosLibres;

    /**M�ximo n�mero de tareas que se pueden encolar.*/
    int maxTareasPendientes;

    /**N�mero de threads en uso*/
    int numHilosUso;

    /**N�mero de hilos disponibles*/
    int numHilosLibres;

    /**
	 * Esta constructora inicia todos los par�metros del pool de hilos, pero no crea
	 * ninguno. Se crear�n bajo demanda.
	 * @param maxnumthreads L�mite de threads permitidos
	 * @param maxpool Tama�o del pool
	 * @param maxtasks Tama�o de la lista de tasks pendientes
	 */
    public ThreadPool(int maxnumthreads, int maxpool, int maxtasks) {
        max_num_threads = maxnumthreads;
        maxHilosLibres = maxpool;
        maxTareasPendientes = maxtasks;
        numHilosUso = 0;
        numHilosLibres = 0;
        hilosLibres = new ArrayList<Thread>(Math.min(maxpool, 25));
        tareasPendientes = new LinkedList<Runnable>();
    }

    /**
	 * M�todo para obtener un Thread libre. Si es necesario, lo crear� y lo pondr�
	 * en marcha, quedando �ste a la espera de que se le asigne una tarea.
	 * @return El thread listo para recibir una tarea, o null si hubo alg�n problema.
	 */
    public synchronized Thread obtenerThread() {
        if (numHilosUso >= max_num_threads) {
            return null;
        }
        Thread hilo;
        if (numHilosLibres == 0) {
            hilo = new ThreadInstante();
            ((ThreadInstante) (hilo)).setPool(this);
            hilo.start();
        } else {
            numHilosLibres--;
            hilo = (Thread) hilosLibres.remove(numHilosLibres);
        }
        numHilosUso++;
        return hilo;
    }

    /**
	 * M�todo para intentar ejecutar una tarea. Este m�todo intentar� obtener un hilo
	 * disponible. Si lo consigue, le encargar� la realizaci�n de la tarea. Si no lo 
	 * consigue, intenta encolar la tarea como pendiente, para ejecutarse cuando haya
	 * un thread disponible. Si la cola de tareas pendientes est� llena, la tarea se
	 * descartar� y no ser� realizada.
	 * @param t Tarea a realizar en un hilo independiente.
	 * @return true si se puede asegurar la ejecuci�n de la tarea, false si se tiene que descartar.
	 */
    public synchronized boolean ejecutarTarea(Runnable t) {
        ThreadInstante hilo = (ThreadInstante) obtenerThread();
        if (hilo != null) {
            hilo.ejecutar(t);
            return true;
        } else if (tareasPendientes.size() < maxTareasPendientes) {
            tareasPendientes.add(t);
            return true;
        } else return false;
    }

    /**
	 * M�todo por el cual se devuelve un hilo al pool porque ya ha terminado su tarea.
	 * Antes de declararlo como disponible se mira si hay tareas pendientes de realizarse,
	 * y se le asigna la primera. Si no lo hay, en caso de que no haya hueco en el pool,
	 * simplemente se marca para su destrucci�n.
	 * @param h El hilo disponible.
	 */
    protected synchronized void devolverHilo(Thread h) {
        if (tareasPendientes.size() > 0) {
            Runnable t = tareasPendientes.removeFirst();
            ((ThreadInstante) (h)).ejecutar(t);
            return;
        }
        numHilosUso--;
        if (numHilosLibres < maxHilosLibres) {
            hilosLibres.add(h);
            numHilosLibres++;
        } else ((ThreadInstante) (h)).descartar();
        notify();
    }

    public synchronized void waitForExecutor() {
        while (numHilosUso >= max_num_threads) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
