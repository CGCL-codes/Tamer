package util;

import armas.Tiro;
import itens.Item;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import naves.Nave;

/**
 * Guarda todas as imagens do jogo, bem como seus caminhos relativos. Para mudar uma imagem ou animação no jogo basta mudar nesta classe
 * @author Gabriel
 * @author Felipe
 */
public final class Resources {

    private Icon iconeMissel;

    private Icon iconeBala;

    private Icon iconeVida;

    private int bgX;

    private int bgY;

    private String imgP1Normal;

    private String imgP1Left;

    private String imgP1Right;

    private String imgInimigoPersegueNormal;

    private String imgInimigoPersegueLeft;

    private String imgInimigoPersegueRight;

    private String imgInimigoGrande;

    private String imgTiroSimples;

    private String imgTiroMissel;

    private int framesTiroMissel;

    private String imgTiroMisselInimigo;

    private int framesTiroMisselInimigo;

    private String imgTiroSimplesInimigo;

    private String imgExplosaoSimples;

    private int framesExplosaoSimples;

    private String imgExplosaoGrande;

    private int framesExplosaoGrande;

    private String imgDir;

    private String imgSpriteExt;

    private String imgItemVida;

    private int framesItemVida;

    private String imgItemMissel;

    private int framesItemMissel;

    private String imgItemBala;

    private int framesItemBala;

    private String imgItemEscudo;

    private int framesItemEscudo;

    private String imgEscudoJogador;

    private int framesEscudoJogador;

    private BufferedImage bg;

    private BufferedImage exGrande[];

    private BufferedImage exSimples[];

    private BufferedImage tiroMissel[];

    private BufferedImage tiroSimples;

    private BufferedImage tiroSimplesInimigo;

    private BufferedImage itemVida[];

    private BufferedImage itemMissel[];

    private BufferedImage itemBala[];

    private BufferedImage jogadorNormal;

    private BufferedImage jogadorLeft;

    private BufferedImage jogadorRight;

    private BufferedImage inimigoPersegueNormal;

    private BufferedImage inimigoPersegueLeft;

    private BufferedImage inimigoPersegueRight;

    private BufferedImage inimigoGrande;

    private BufferedImage tiroMisselInimigo[];

    private BufferedImage msgPerdeu;

    private BufferedImage escudoJogador[];

    private BufferedImage itemEscudo[];

    public Resources() {
        iconeMissel = new ImageIcon(IO.carregarImagem("/imagens/icones/missel1.png"));
        bg = IO.carregarImagem("/imagens/background_grande.png");
        iconeBala = new ImageIcon(IO.carregarImagem("/imagens/icones/bala1.png"));
        iconeVida = new ImageIcon(IO.carregarImagem("/imagens/icones/vida.png"));
        this.bgX = 0;
        this.bgY = 0;
        this.imgDir = "/imagens/";
        this.imgSpriteExt = ".png";
        this.imgP1Normal = "/imagens/player1_normal.png";
        this.imgP1Left = "/imagens/player1_left.png";
        this.imgP1Right = "/imagens/player1_right.png";
        this.imgInimigoPersegueNormal = "/imagens/inimigo1_normal.png";
        this.imgInimigoPersegueLeft = "/imagens/inimigo1_left.png";
        this.imgInimigoPersegueRight = "/imagens/inimigo1_right.png";
        this.imgTiroSimples = "/imagens/tiroSimples.png";
        this.imgTiroMissel = "/imagens/sprites/arma1/a";
        this.framesTiroMissel = 2;
        this.imgExplosaoGrande = "/imagens/sprites/ex/";
        this.framesExplosaoGrande = 19;
        this.imgExplosaoSimples = "/imagens/sprites/exSimples/";
        this.framesExplosaoSimples = 10;
        this.imgTiroSimplesInimigo = "/imagens/tiroSimples.png";
        exGrande = new BufferedImage[this.framesExplosaoGrande];
        for (int i = 0; i < this.framesExplosaoGrande; i++) {
            String filename = this.imgExplosaoGrande + i + ".png";
            exGrande[i] = IO.carregarImagem(filename);
        }
        exSimples = new BufferedImage[this.framesExplosaoSimples];
        for (int i = 0; i < this.framesExplosaoSimples; i++) {
            String filename = this.imgExplosaoSimples + i + ".png";
            exSimples[i] = IO.carregarImagem(filename);
        }
        tiroMissel = new BufferedImage[this.framesTiroMissel];
        for (int i = 0; i < this.framesTiroMissel; i++) {
            String filename = this.imgTiroMissel + i + ".png";
            tiroMissel[i] = IO.carregarImagem(filename);
        }
        framesTiroMisselInimigo = 2;
        imgTiroMisselInimigo = "/imagens/sprites/misselInimigo/a";
        tiroMisselInimigo = new BufferedImage[this.framesTiroMisselInimigo];
        for (int i = 0; i < this.framesTiroMisselInimigo; i++) {
            String filename = this.imgTiroMisselInimigo + i + ".png";
            tiroMisselInimigo[i] = IO.carregarImagem(filename);
        }
        tiroSimples = IO.carregarImagem(this.imgTiroSimples);
        tiroSimplesInimigo = IO.carregarImagem(this.imgTiroSimplesInimigo);
        framesItemVida = 10;
        imgItemVida = "/imagens/sprites/itemVida/";
        itemVida = new BufferedImage[this.framesItemVida];
        for (int i = 0; i < this.framesItemVida; i++) {
            String filename = this.imgItemVida + i + ".png";
            itemVida[i] = IO.carregarImagem(filename);
        }
        framesItemMissel = 9;
        imgItemMissel = "/imagens/sprites/itemMissel/";
        itemMissel = new BufferedImage[this.framesItemMissel];
        for (int i = 0; i < this.framesItemMissel; i++) {
            String filename = this.imgItemMissel + i + ".png";
            itemMissel[i] = IO.carregarImagem(filename);
        }
        framesItemBala = 12;
        imgItemBala = "/imagens/sprites/itemBala/";
        itemBala = new BufferedImage[this.framesItemBala];
        for (int i = 0; i < this.framesItemBala; i++) {
            String filename = this.imgItemBala + i + ".png";
            itemBala[i] = IO.carregarImagem(filename);
        }
        this.imgInimigoGrande = "/imagens/inimigoGrande/normal.png";
        this.inimigoGrande = IO.carregarImagem(imgInimigoGrande);
        this.jogadorNormal = IO.carregarImagem(imgP1Normal);
        this.jogadorLeft = IO.carregarImagem(imgP1Left);
        this.jogadorRight = IO.carregarImagem(imgP1Right);
        this.inimigoPersegueNormal = IO.carregarImagem(imgInimigoPersegueNormal);
        this.inimigoPersegueLeft = IO.carregarImagem(imgInimigoPersegueLeft);
        this.inimigoPersegueRight = IO.carregarImagem(imgInimigoPersegueRight);
        this.msgPerdeu = IO.carregarImagem("/imagens/perdeu.png");
        this.imgEscudoJogador = "/imagens/sprites/escudo/";
        this.framesEscudoJogador = 7;
        this.escudoJogador = new BufferedImage[this.framesEscudoJogador];
        for (int i = 0; i < this.framesEscudoJogador; i++) {
            escudoJogador[i] = IO.carregarImagem(this.imgEscudoJogador + i + ".png");
        }
        this.imgItemEscudo = "/imagens/sprites/itemEscudo/";
        this.framesItemEscudo = 15;
        itemEscudo = new BufferedImage[this.framesItemEscudo];
        for (int i = 0; i < this.framesItemEscudo; i++) {
            this.itemEscudo[i] = IO.carregarImagem(this.imgItemEscudo + i + ".png");
        }
    }

    public static boolean checkColision(Nave n, Tiro t) {
        return (((t.getPosX() >= n.getX() - Math.abs(t.getIncTiroX()) && t.getPosX() <= n.getX() + n.getTamanhoX() + Math.abs(t.getIncTiroX())) || (t.getPosX() + t.getTamanhoX() >= n.getX() - Math.abs(t.getIncTiroX()) && t.getPosX() + t.getTamanhoX() <= n.getX() + n.getTamanhoX() + Math.abs(t.getIncTiroX()))) && ((t.getPosY() >= n.getY() && t.getPosY() <= n.getY() + n.getTamanhoY()) || (t.getPosY() + t.getTamanhoY() >= n.getY() && t.getPosY() + t.getTamanhoY() <= n.getY() + n.getTamanhoY()))) && (t.getSource() != n);
    }

    public static boolean checkColision_NaveGrande(Nave n, Tiro t) {
        boolean a = (t.getPosX() > n.getX() && t.getPosX() < n.getX() + n.getTamanhoX()) || (t.getPosX() + t.getTamanhoX() > n.getX() && t.getPosX() + t.getTamanhoX() < n.getX() + n.getTamanhoX());
        boolean b = (t.getPosY() > n.getY() + 101 && t.getPosY() < n.getY() + 181);
        boolean c = (t.getPosX() > n.getX() + 200 && t.getPosX() < n.getX() + n.getTamanhoX()) || (t.getPosX() + t.getTamanhoX() > n.getX() + 200 && t.getPosX() + t.getTamanhoX() < n.getX() + n.getTamanhoX());
        boolean d = (t.getPosY() > n.getY() && t.getPosY() < n.getY() + n.getTamanhoY());
        return (a && b) || (c && d);
    }

    public static boolean checkColision_Item(Nave n, Item i) {
        boolean a = (i.getPosX() > n.getX() && i.getPosX() < n.getX() + n.getTamanhoX()) || (i.getPosX() + i.getTamanhoX() > n.getX() + 200 && i.getPosX() + i.getTamanhoX() < n.getX() + n.getTamanhoX());
        boolean b = (i.getPosY() > n.getY() && i.getPosY() < n.getY() + n.getTamanhoY());
        return a && b;
    }

    public BufferedImage getBg() {
        return bg;
    }

    public Icon getIconeBala() {
        return iconeBala;
    }

    public Icon getIconeMissel() {
        return iconeMissel;
    }

    public Icon getIconeVida() {
        return iconeVida;
    }

    public int getBgX() {
        return bgX;
    }

    public void setBgX(int bgX) {
        if (this.bgX == -360) {
            this.bgX = 0;
        } else {
            this.bgX = bgX;
        }
    }

    public int getBgY() {
        return bgY;
    }

    public void setBgY(int bgY) {
        this.bgY = bgY;
    }

    public int getFramesExplosaoSimples() {
        return framesExplosaoSimples;
    }

    public int getFramesTiroMissel() {
        return framesTiroMissel;
    }

    public String getImgDir() {
        return imgDir;
    }

    public String getImgExplosaoSimples() {
        return imgExplosaoSimples;
    }

    public String getImgInimigoPersegueLeft() {
        return imgInimigoPersegueLeft;
    }

    public String getImgInimigoPersegueNormal() {
        return imgInimigoPersegueNormal;
    }

    public String getImgInimigoPersegueRight() {
        return imgInimigoPersegueRight;
    }

    public String getImgP1Left() {
        return imgP1Left;
    }

    public String getImgP1Normal() {
        return imgP1Normal;
    }

    public String getImgP1Right() {
        return imgP1Right;
    }

    public String getImgSpriteExt() {
        return imgSpriteExt;
    }

    public String getImgTiroMissel() {
        return imgTiroMissel;
    }

    public String getImgTiroSimples() {
        return imgTiroSimples;
    }

    public String getImgTiroSimplesInimigo() {
        return imgTiroSimplesInimigo;
    }

    public BufferedImage[] getExGrande() {
        return this.exGrande;
    }

    public BufferedImage[] getExSimples() {
        return this.exSimples;
    }

    public BufferedImage[] getTiroMissel() {
        return this.tiroMissel;
    }

    public BufferedImage getTiroSimples() {
        return this.tiroSimples;
    }

    public BufferedImage getTiroSimplesInimigo() {
        return this.tiroSimplesInimigo;
    }

    public int getFramesExplosaoGrande() {
        return this.framesExplosaoGrande;
    }

    public BufferedImage[] getItemVida() {
        return this.itemVida;
    }

    public int getFramesItemVida() {
        return this.framesItemVida;
    }

    public int getFramesItemBala() {
        return this.framesItemBala;
    }

    public int getFramesItemMissel() {
        return this.framesItemMissel;
    }

    public BufferedImage[] getItemBala() {
        return this.itemBala;
    }

    public BufferedImage[] getItemMissel() {
        return this.itemMissel;
    }

    public BufferedImage getInimigoGrande() {
        return inimigoGrande;
    }

    public BufferedImage getInimigoPersegueLeft() {
        return inimigoPersegueLeft;
    }

    public BufferedImage getInimigoPersegueNormal() {
        return inimigoPersegueNormal;
    }

    public BufferedImage getInimigoPersegueRight() {
        return inimigoPersegueRight;
    }

    public BufferedImage getJogadorLeft() {
        return jogadorLeft;
    }

    public BufferedImage getJogadorNormal() {
        return jogadorNormal;
    }

    public BufferedImage getJogadorRight() {
        return jogadorRight;
    }

    public int getFramesTiroMisselInimigo() {
        return framesTiroMisselInimigo;
    }

    public BufferedImage[] getTiroMisselInimigo() {
        return tiroMisselInimigo;
    }

    public BufferedImage getMsgPerdeu() {
        return this.msgPerdeu;
    }

    public BufferedImage[] getEscudoJogador() {
        return escudoJogador;
    }

    public int getFramesEscudoJogador() {
        return framesEscudoJogador;
    }

    public int getFramesItemEscudo() {
        return this.framesItemEscudo;
    }

    public BufferedImage[] getItemEscudo() {
        return this.itemEscudo;
    }
}
