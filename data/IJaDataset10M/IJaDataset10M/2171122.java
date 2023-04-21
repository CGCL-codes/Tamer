package threading;

/**
 * Thread especializado para realizar la ejecuci�n de un instante de un usuario determinado.
 * @author alberto
 */
public class ThreadInstante extends Thread {

    /**
	 * El pool al que pertenece el hilo.
	 */
    private ThreadPool pool;

    /**
	 * La tarea que se le asigna al hilo. Normalmente ser� una instancia de URS.
	 */
    private Runnable task;

    /**
	 * Marca que determina si el hilo debe terminar su ejecuci�n o mantenerse a la espera de 
	 * recibir nuevas tareas.
	 */
    private boolean cerrar;

    /**
	 * M�todo para obtener el pool al que pertenece el hilo.
	 * @return Pool de hilos.
	 */
    public ThreadInstante() {
    }

    public ThreadPool getPool() {
        return pool;
    }

    public void setPool(ThreadPool p) {
        pool = p;
    }

    /**
	 * M�todo para obtener la tarea ejecutable que realizar� este hilo.
	 * @return La tarea Runnable.
	 */
    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable t) {
        task = t;
        setName("Hilo instante de " + task.toString());
    }

    /**
	 * M�todo para preguntar si se debe cerrar el hilo cuando termine su tarea.
	 * @return true si el hilo est� marcado para ser destruido, false si se mantendr� a la espera de nueva tarea.
	 */
    public boolean getCerrar() {
        return cerrar;
    }

    private void setCerrar(boolean b) {
        cerrar = b;
    }

    /**
	 * Rutina de ejecuci�n del thread: en caso de tener una tarea la ejecutar�. Si no tiene tarea, si no est� marcado
	 * para destrucci�n esperar� hasta recibir una nueva tarea o hasta que quede marcado para su destrucci�n.
	 */
    public synchronized void run() {
        while (!cerrar) {
            while (task == null && !cerrar) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            if (task != null) {
                setName("instante de " + task.toString());
                task.run();
                task = null;
                pool.devolverHilo(this);
            }
        }
    }

    /**
	 * Asigna una tarea para ejecutar a este hilo y lo despierta.
	 * @param r Tarea Runnable para ejecutar.
	 */
    public synchronized void ejecutar(Runnable r) {
        if (task != null) System.out.println("Error al asignar task a un hilo porque ya ten�a.");
        task = r;
        notify();
    }

    /**
	 * Fuerza la liberaci�n del hilo. Normalmente no debe ser llamada.
	 */
    public synchronized void liberar() {
        if (task != null) {
            System.out.println("Error al liberar un hilo: tiene tarea.");
        }
        pool.devolverHilo(this);
    }

    /**
	 * Marca el hilo como descartado. Cuando termine lo que est� haciendo, se destruir�.
	 */
    synchronized void descartar() {
        setCerrar(true);
        notify();
    }
}
