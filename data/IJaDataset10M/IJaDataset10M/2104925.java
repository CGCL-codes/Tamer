package Modelo.IA;

import Modelo.Criatura;

/**
 * Sirve para representar las condciones que tienen que ver con los atributos
 * de la criatura.
 * @author Drakart
 */
public class CondicionA implements Condicion {

    private comparador comp_;

    private operando operando_;

    public enum comparador {

        MINIMO, MUY_BAJO, BAJO, MEDIO, ALTO, MUY_ALTO, MAXIMO;

        boolean eval(float porcentaje) {
            switch(this) {
                case MINIMO:
                    return porcentaje == 0;
                case MUY_BAJO:
                    return porcentaje >= 0 && porcentaje <= 0.2f;
                case BAJO:
                    return porcentaje <= 0.4f && porcentaje > 0.2f;
                case MEDIO:
                    return porcentaje <= 0.6f;
                case ALTO:
                    return porcentaje <= 0.8f;
                case MUY_ALTO:
                    return porcentaje <= 1;
                case MAXIMO:
                    return porcentaje == 1;
            }
            throw new AssertionError("Unknown op: " + this);
        }
    }

    public enum operando {

        VIDA, HAMBRE, AGUANTE_CLIMA, AGRESIVIDAD;

        float eval(Criatura c) {
            switch(this) {
                case VIDA:
                    return c.getVida_() / c.getVidaMax_();
                case HAMBRE:
                    return c.getHambre_() / c.getHambreMax_();
                case AGUANTE_CLIMA:
                    return c.getAguanteClimatico_() / c.getAguanteClimaticoMax_();
                case AGRESIVIDAD:
                    return c.getAgresividad_() / c.getAgresividadMax_();
            }
            throw new AssertionError("Unknown op: " + this);
        }
    }

    public boolean evaluar(Criatura c) {
        return comp_.eval(operando_.eval(c));
    }

    public CondicionA(operando op, comparador comp) {
        this.comp_ = comp;
        this.operando_ = op;
    }
}
