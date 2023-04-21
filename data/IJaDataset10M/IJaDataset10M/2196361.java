package ramon.eventos;

import org.apache.commons.lang.builder.EqualsBuilder;
import ramon.EstadoValidacion;
import ramon.gen.GeneradorSimple;

public class Telefono extends GeneradorSimple<Telefono> {

    private String numero;

    public Telefono(String numero) {
        this.numero = numero;
    }

    public Telefono() {
    }

    @Override
    public void destroy(Telefono tel) {
        this.valor = tel.numero;
    }

    @Override
    protected Telefono generate(EstadoValidacion val) {
        if (valor != null) if (valor.contains("-")) {
            val.addError("El número no puede tener guiones");
            return null;
        } else {
            numero = valor;
            return this;
        }
        return null;
    }

    public String toString() {
        return "numero = " + numero;
    }

    @Override
    public boolean equals(Object otro) {
        return EqualsBuilder.reflectionEquals(this, otro, false, getClass());
    }
}
