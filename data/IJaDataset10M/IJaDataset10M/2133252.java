package librodeesher;

import java.io.Serializable;

/**
 *
 * @author jorge
 */
class Caracteristica implements Serializable {

    private int temporal = 31;

    private int potencial = 0;

    private int raza = 0;

    private int especial = 0;

    private String abreviatura;

    private Esher esher;

    /** Creates a new instance of Caracteristica */
    public Caracteristica(String ab, Esher tmp_esher) {
        abreviatura = ab;
        esher = tmp_esher;
    }

    public void GuardarPontencial() {
        if (esher.pj.nivel == 1) {
            potencial = CalcularPotencial();
        }
    }

    private int CalcularPotencial() {
        if (temporal >= 100) return 99 + esher.TiradaDados(1, 2);
        if (temporal >= 99) return 98 + esher.TiradaDados(1, 2);
        if (temporal >= 98) return 97 + esher.TiradaDados(1, 3);
        if (temporal >= 97) return 96 + esher.TiradaDados(1, 4);
        if (temporal >= 96) return 95 + esher.TiradaDados(1, 5);
        if (temporal >= 95) return 94 + esher.TiradaDados(1, 6);
        if (temporal >= 94) return 93 + esher.TiradaDados(1, 7);
        if (temporal >= 93) return 92 + esher.TiradaDados(1, 8);
        if (temporal >= 92) return 91 + esher.TiradaDados(1, 9);
        if (temporal >= 85) return 90 + esher.TiradaDados(1, 10);
        if (temporal >= 75) return 80 + esher.TiradaDados(2, 10);
        if (temporal >= 65) return 70 + esher.TiradaDados(3, 10);
        if (temporal >= 55) return 60 + esher.TiradaDados(4, 10);
        if (temporal >= 45) return 50 + esher.TiradaDados(5, 10);
        if (temporal >= 35) return 40 + esher.TiradaDados(6, 10);
        if (temporal >= 25) return 30 + esher.TiradaDados(7, 10);
        if (temporal >= 20) return 20 + esher.TiradaDados(8, 10);
        return potencial;
    }

    void CrearPuntosTemporal(int valor) {
        if (esher.pj.nivel == 1) {
            temporal = valor;
        }
    }

    void CrearPuntosPotencial(int valor) {
        potencial = valor;
    }

    void IncrementarPuntosTemporal(int valor) {
        temporal += valor;
        if (temporal > potencial) temporal = potencial;
        if (temporal < 20) temporal = 20;
    }

    void IncrementarPuntosTemporalSinContemplarPotencial(int valor) {
        temporal += valor;
        if (temporal < 20) temporal = 20;
    }

    /**
     * Devuelve el valor temporal de una caracteristica.
     */
    public int ObtenerPuntosTemporal() {
        return temporal;
    }

    /**
     * Devuelve el coste sobre 660 de que la caracteristicas obtenga ese valor.
     */
    int ObtenerValorTemporal() {
        if (temporal >= 102) return 14;
        if (temporal >= 101) return 12;
        if (temporal >= 100) return 10;
        if (temporal >= 98) return 9;
        if (temporal >= 96) return 8;
        if (temporal >= 94) return 7;
        if (temporal >= 92) return 6;
        if (temporal >= 90) return 5;
        if (temporal >= 85) return 4;
        if (temporal >= 80) return 3;
        if (temporal >= 75) return 2;
        if (temporal >= 70) return 1;
        if (temporal >= 31) return 0;
        if (temporal >= 26) return -1;
        if (temporal >= 21) return -2;
        if (temporal >= 16) return -3;
        if (temporal >= 11) return -4;
        if (temporal >= 10) return -5;
        if (temporal >= 8) return -6;
        if (temporal >= 6) return -7;
        if (temporal >= 4) return -8;
        if (temporal >= 2) return -9;
        return -10;
    }

    int ObtenerValorRaza() {
        return raza;
    }

    void CambiarPuntosRaza(int valor) {
        raza = valor;
    }

    int ObtenerPuntosEspecial() {
        return especial;
    }

    /**
     * Devuelve los puntos obtenidos al calcular el valor potencial de una caracteristica.
     */
    int ObtenerPuntosPotencial() {
        return potencial;
    }

    int Total() {
        return ObtenerValorTemporal() + raza + especial;
    }

    public String DevolverAbreviatura() {
        return abreviatura;
    }

    int ObtenerCosteTemporalCaracteristica(int value) {
        Double d;
        if (value < 91) return value; else d = Math.pow(value - 90, 2) + 90;
        return d.intValue();
    }

    int ObtenerCosteTemporalCaracteristica() {
        Double d;
        if (ObtenerPuntosTemporal() < 91) return ObtenerPuntosTemporal(); else d = Math.pow(ObtenerPuntosTemporal() - 90, 2) + 90;
        return d.intValue();
    }

    void CalcularAumentoCaracteristica() {
        int subir = 0;
        int dado1 = esher.TiradaDado(10);
        int dado2 = esher.TiradaDado(10);
        if (potencial - temporal <= 10) {
            if (dado1 != dado2) {
                subir = Math.min(dado1, dado2);
            } else {
                if (dado1 < 6) subir = -dado1; else subir = dado1 * 2;
            }
        } else {
            if (potencial - temporal <= 20) {
                if (dado1 != dado2) {
                    subir = Math.max(dado1, dado2);
                } else {
                    if (dado1 < 6) subir = -dado1; else subir = dado1 * 2;
                }
            } else {
                if (dado1 != dado2) {
                    subir = dado1 + dado2;
                } else {
                    if (dado1 < 6) subir = -dado1; else subir = dado1 * 2;
                }
            }
        }
        IncrementarPuntosTemporal(subir);
    }

    void CambiarPuntosTemporal(int valor) {
        temporal = valor;
    }
}
