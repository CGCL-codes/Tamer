package Personagens;

import java.util.Random;
import Base.Armadura;
import Base.Armas;

public class Gladiador {

    double capacidadeDeCarga;

    double pesoCarregado;

    int forca;

    int agilidade;

    int constituicao;

    double vida;

    double esquiva;

    double estamina;

    double regeneracaoDeestamina;

    double regeneracaoDeVida;

    Armas arma;

    Armadura armadura;

    double dano;

    int nivel;

    public Random rnd;

    float defesa;

    public Gladiador(int forca, int agilidade, int constituicao, Armas arma, Armadura armadura) {
        vida = constituicao * 5;
        regeneracaoDeVida = constituicao * 2.5;
        capacidadeDeCarga = forca * 5;
        pesoCarregado = arma.getPeso() + armadura.getPesoAtual();
        dano = forca + Ataque(rnd, arma);
        regeneracaoDeestamina = agilidade * 2.5;
        estamina = agilidade * 5;
        esquiva = agilidade * 2 - ((int) pesoCarregado * 0.1);
        defesa = armadura.getDefesaAtual();
    }

    public double Ataque(Random rnd, Armas arma) {
        double danoMedio = (arma.getDanoMimino() + rnd.nextInt((int) arma.getDanoMaximo())) / 2;
        return danoMedio;
    }
}
