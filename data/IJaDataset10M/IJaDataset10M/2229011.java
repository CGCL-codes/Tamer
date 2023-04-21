package br.furb.furbot;

import br.furb.furbot.suporte.LoadImage;
import javax.swing.ImageIcon;

/**
 * 
 * @author Adilson Vahldick 
 */
public class Parede extends ObjetoDoMundoAdapter {

    public Parede() {
    }

    public String toString() {
        return "Parede";
    }

    public ImageIcon buildImage() {
        return LoadImage.getInstance().getIcon("imagens/wall.png");
    }

    public void executar() throws Exception {
    }
}
