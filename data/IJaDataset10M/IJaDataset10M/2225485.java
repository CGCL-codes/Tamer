package unit;

public class AlumnoUnit {

    public static void main(String[] args) {
        System.out.println("INICIO ");
        System.out.println("Resultado Alumno por curso " + new ws.AlumnoSOA().getByCurso(1));
        System.out.println("Resultado Alumno por id " + new ws.AlumnoSOA().getById(3));
        System.out.println("Resultado Alumno por id nulo " + new ws.AlumnoSOA().getById(0));
        System.out.println("Resultado Alumno por id inexistente " + new ws.AlumnoSOA().getById(-1));
        System.out.println("Resultado Alumno por nombre " + new ws.AlumnoSOA().getByNombre("Tamara Gonzalez"));
        System.out.println("Resultado Alumno por rut " + new ws.AlumnoSOA().getByRut("16336287"));
        System.out.println("Resultado Alumno todos " + new ws.AlumnoSOA().getAll());
        System.out.println("FIN ");
    }
}
