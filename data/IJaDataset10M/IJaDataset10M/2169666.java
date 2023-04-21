package br.ufpa.cbcc2007.carros;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Veiculo extends ObjetoJogo {

    private int tipo, velocidade, velocidadeMaxima, energia;

    public Veiculo() {
        tipo = 0;
        velocidade = 0;
        velocidadeMaxima = 0;
        energia = 0;
    }

    public Veiculo(Image i, int t, int x, int y, int veloM, int e) {
        super.image = i;
        super.comprimento = image.getWidth();
        super.altura = image.getHeight();
        super.px = x;
        super.py = y;
        tipo = t;
        velocidade = 0;
        velocidadeMaxima = veloM;
        energia = e;
    }

    public int getTipo() {
        return tipo;
    }

    public void incEnergia(int i) {
        energia += i;
    }

    public void setEnergia(int i) {
        energia = i;
    }

    public int getEnergia() {
        return energia;
    }

    public void incVelocidade(int x) {
        if (velocidade + x <= velocidadeMaxima) velocidade += x; else velocidade = velocidadeMaxima;
    }

    public void setVelocidade(int i) {
        velocidade = i;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidadeMaxima(int i) {
        velocidadeMaxima = i;
    }

    public void meDesenha(Graphics g, int dx, int dy) {
        g.drawImage(super.image, super.px - dx, super.py - dy, Graphics.TOP | Graphics.LEFT);
    }
}
