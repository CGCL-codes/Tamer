package jfan.fan.fuzzy;

public class FuncaoPertinenciaTriangular implements FuncaoPertinencia {

    public double calcularPertinencia(double valor, double inicio, double centro, double fim) {
        if (valor <= inicio || valor >= fim) {
            return 0;
        }
        if (valor == centro) {
            return 1.0;
        }
        if (inicio <= valor && valor < centro) {
            return (valor - inicio) / (centro - inicio);
        }
        return (fim - valor) / (fim - centro);
    }
}
