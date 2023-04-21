package org.sam.jspacewars.servidor.tareas;

public abstract class TareaAbs implements Tarea {

    private final long duracion;

    public TareaAbs(long duracion) {
        if (duracion < 0) throw new IllegalArgumentException("Duracion < 0");
        this.duracion = duracion;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final long getDuracion() {
        return duracion;
    }

    /**
	 * Metodo que muestra por consola las llamadas para test.
	 * 
	 * @see org.sam.jspacewars.servidor.tareas.Tarea#realizar(long, long)
	 */
    protected final void realizarTest(long startTime, long stopTime) {
        long desfase = startTime;
        long tTrabajo = Math.min(this.getDuracion() - startTime, stopTime - startTime);
        long tEspera = stopTime - tTrabajo - desfase;
        System.out.format("   %-22s [ %2d, %2d ] [ %2d : %2d nanosegundos : %2d ]\n", toString(), startTime, stopTime, desfase, tTrabajo, tEspera);
    }
}
