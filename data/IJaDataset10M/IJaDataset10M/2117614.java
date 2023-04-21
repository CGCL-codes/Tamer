package net.sf.opennemesis.motor.estructuras.utilerias;

/**
 *
 * @author fictog
 */
public class DisciplinaMagica extends Disciplina {

    public final String TIPO = "corporal";

    public DisciplinaMagica(String nombre) throws IllegalArgumentException, NullPointerException {
        if (nombre == null) throw new NullPointerException("DisciplinaMagica.java");
        if (nombre.equals("")) throw new IllegalArgumentException("DisciplinaMagica.java");
        this.nombre = nombre;
    }
}
