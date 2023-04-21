package ejemploclaseabstracta;

/**
 *
 * @author AdminLocal
 */
class Cuenta {

    String nombre = "";

    double cantidad = 100;
}

public class Comercial extends Empleado {

    Cuenta[] cuentas;

    public Comercial() {
        super();
        cuentas = new Cuenta[1];
        cuentas[0] = new Cuenta();
    }

    @Override
    public double calcularNomina() {
        double total = salarioBase;
        for (Cuenta c : cuentas) {
            total += c.cantidad;
        }
        return total;
    }
}
