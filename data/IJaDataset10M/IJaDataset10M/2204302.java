package ejemploclaseabstracta;

import ejemploclaseabstracta.Recurso;

/**
 *
 * @author AdminLocal
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Empleado miEmpleado = new Empleado() {

            @Override
            public double calcularNomina() {
                return salarioBase;
            }
        };
        Empleado otro = new Comercial();
        Empleado[] lista = new Empleado[2];
        lista[0] = miEmpleado;
        lista[1] = otro;
        for (Empleado e : lista) {
            System.out.println(e.calcularNomina());
        }
        Recurso[] elementos = new Recurso[3];
        elementos[0] = miEmpleado;
        elementos[1] = otro;
        elementos[2] = new Proveedor();
        for (Recurso r : elementos) {
            if (r instanceof Proveedor) {
                System.out.println(r.getClass().getName());
            }
            System.out.println(r.calcularGasto());
        }
    }
}
