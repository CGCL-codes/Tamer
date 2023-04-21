package es.cea;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

public class ColeccionesTest {

    @Test
    public void pruebaLista() {
        List<Alumno> alumnos = new ArrayList<Alumno>();
        Alumno alumno1 = new Alumno("1234", "juan");
        Alumno alumno2 = new Alumno("2345", "pepe");
        Alumno alumno3 = new Alumno("3456", "luis");
        Alumno alumno1bis = new Alumno("1234", "da igual el nombre, solo dni");
        alumnos.add(alumno1);
        alumnos.add(alumno2);
        alumnos.add(alumno3);
        assert (alumnos.size() == 3) : "Deberia ser el tama�o de la lista=3";
        alumnos.add(alumno1bis);
        assert (alumnos.size() == 4) : "el alumno repetido tambien se deberia incluir";
    }

    @Test
    public void pruebaLista2() {
        Set<Alumno> alumnos = new HashSet<Alumno>();
        Alumno alumno1 = new Alumno("1234", "juan");
        Alumno alumno2 = new Alumno("2345", "pepe");
        Alumno alumno3 = new Alumno("3456", "luis");
        Alumno alumno1bis = new Alumno("1234", "da igual el nombre, solo dni");
        alumnos.add(alumno1);
        alumnos.add(alumno2);
        alumnos.add(alumno3);
        assert (alumnos.size() == 3) : "Deberia ser el tama�o de la lista=3";
        alumnos.add(alumno1bis);
        assert (alumnos.size() == 4) : "el alumno repetido tambien se deberia incluir";
    }
}
