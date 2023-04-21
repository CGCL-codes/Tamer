package br.dcc.ufrj.comp2.jogo;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public abstract class ObjetosExplosivos extends ObjetosJogo {

    ArrayList<BufferedImage> imagens = new ArrayList<BufferedImage>(10);

    Graphics g;

    public void carregaImagensExplosao() {
        for (int i = 0; i < 10; i++) {
            try {
                imagens.add(ImageIO.read(new File("imagens/explosaof" + i + ".png")));
            } catch (IOException e) {
                System.out.println("Arquivo n�o encontrado!");
            }
        }
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("explos�o!");
            this.imagem = this.imagens.get(i);
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
