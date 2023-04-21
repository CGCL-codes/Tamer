package unit;

public class SubsectorUnit {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("INICIO ");
        System.out.println("Resultado Subsector por id " + new ws.SubsectorSOA().getById(1));
        System.out.println("Resultado Subsector por id nulo " + new ws.SubsectorSOA().getById(0));
        System.out.println("Resultado Subsector por id inexistente " + new ws.SubsectorSOA().getById(1000000000));
        System.out.println("Resultado Subsector por nombre " + new ws.SubsectorSOA().getByNombre("Matematicas"));
        System.out.println("Resultado Subsector por id curso " + new ws.SubsectorSOA().getByCurso(3));
        System.out.println("Resultado Subsector todos " + new ws.SubsectorSOA().getAll());
        System.out.println("FIN ");
    }
}
