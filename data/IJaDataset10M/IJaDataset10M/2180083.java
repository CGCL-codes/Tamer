package exemplo5;

import exemplos.SpriteComImagensBoundingBox;
import java.applet.AudioClip;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.javagame.Animacao;

/**
 *
 * @author Leonardo
 */
public class Personagem extends SpriteComImagensBoundingBox implements Runnable {

    public static int ESTADO_PARADO = 0;

    public static int ESTADO_ANDANDOPARAFRENTE = 1;

    public static int ESTADO_ANDANDOPARATRAS = 2;

    public static int ESTADO_CHUTANDO = 3;

    public static float VELOCIDADE = 200;

    public static long TEMPO_CHUTE = 1100;

    private AudioClip somChute;

    /** Creates a new instance of Personagem */
    public Personagem(MeuJogoApp app) {
        this.somChute = app.somChute;
        Animacao anim01 = new Animacao();
        Animacao anim02 = new Animacao();
        Animacao anim03 = new Animacao();
        Animacao anim04 = new Animacao();
        Rectangle2D[] rectsParado = new Rectangle2D[] { new Rectangle(60, 6, 30, 33), new Rectangle(45, 32, 65, 84), new Rectangle(15, 111, 49, 74), new Rectangle(80, 111, 49, 40), new Rectangle(110, 144, 49, 38) };
        Rectangle2D[] rectsAndando = new Rectangle2D[] { new Rectangle(75, 6, 41, 33), new Rectangle(45, 32, 92, 84), new Rectangle(26, 111, 49, 74), new Rectangle(100, 111, 49, 74) };
        Rectangle2D[] rectsChute1 = new Rectangle2D[] { new Rectangle(62, 10, 38, 32), new Rectangle(48, 32, 68, 100), new Rectangle(22, 104, 42, 80), new Rectangle(102, 120, 50, 64) };
        Rectangle2D[] rectsChute2 = new Rectangle2D[] { new Rectangle(64, 6, 34, 32), new Rectangle(56, 32, 60, 98), new Rectangle(24, 104, 42, 80), new Rectangle(102, 120, 50, 64) };
        Rectangle2D[] rectsChute3 = new Rectangle2D[] { new Rectangle(82, 6, 32, 34), new Rectangle(52, 38, 84, 76), new Rectangle(30, 94, 54, 54), new Rectangle(106, 106, 52, 76) };
        Rectangle2D[] rectsChute4 = new Rectangle2D[] { new Rectangle(48, 6, 54, 188), new Rectangle(8, 36, 66, 40), new Rectangle(84, 130, 46, 42) };
        Rectangle2D[] rectsChute5 = new Rectangle2D[] { new Rectangle(18, 14, 84, 168), new Rectangle(76, 58, 58, 68), new Rectangle(110, 94, 68, 42) };
        Rectangle2D[] rectsChute6 = new Rectangle2D[] { new Rectangle(126, 40, 140, 58), new Rectangle(22, 34, 118, 76), new Rectangle(56, 78, 70, 104) };
        anim01.addFrame(app.imagensSakura[0], 100, rectsParado);
        anim01.addFrame(app.imagensSakura[1], 100, rectsParado);
        anim01.addFrame(app.imagensSakura[2], 100, rectsParado);
        anim01.addFrame(app.imagensSakura[3], 100, rectsParado);
        anim01.addFrame(app.imagensSakura[4], 100, rectsParado);
        anim01.addFrame(app.imagensSakura[5], 100, rectsParado);
        anim02.addFrame(app.imagensSakura[6], 80, rectsAndando);
        anim02.addFrame(app.imagensSakura[7], 80, rectsAndando);
        anim02.addFrame(app.imagensSakura[8], 160, rectsAndando);
        anim02.addFrame(app.imagensSakura[9], 240, rectsAndando);
        anim02.addFrame(app.imagensSakura[10], 160, rectsAndando);
        anim02.addFrame(app.imagensSakura[11], 80, rectsAndando);
        anim03.addFrame(app.imagensSakura[11], 80, rectsAndando);
        anim03.addFrame(app.imagensSakura[10], 160, rectsAndando);
        anim03.addFrame(app.imagensSakura[9], 240, rectsAndando);
        anim03.addFrame(app.imagensSakura[8], 160, rectsAndando);
        anim03.addFrame(app.imagensSakura[7], 80, rectsAndando);
        anim03.addFrame(app.imagensSakura[6], 80, rectsAndando);
        anim04.addFrame(app.imagensSakura[12], 100, rectsChute1);
        anim04.addFrame(app.imagensSakura[13], 100, rectsChute2);
        anim04.addFrame(app.imagensSakura[14], 100, rectsChute3);
        anim04.addFrame(app.imagensSakura[15], 100, rectsChute4);
        anim04.addFrame(app.imagensSakura[16], 200, rectsChute5);
        anim04.addFrame(app.imagensSakura[17], 300, rectsChute6);
        anim04.addFrame(app.imagensSakura[16], 200, rectsChute5);
        anim04.addFrame(app.imagensSakura[15], 100, rectsChute4);
        anim04.addFrame(app.imagensSakura[14], 100, rectsChute3);
        anim04.addFrame(app.imagensSakura[13], 100, rectsChute2);
        anim04.addFrame(app.imagensSakura[12], 100, rectsChute1);
        adicionaEstado("Parado", anim01);
        adicionaEstado("AndandoParaFrente", anim02);
        adicionaEstado("AndandoParaTras", anim03);
        adicionaEstado("Chutando", anim04);
    }

    public void andaParaFrente() {
        if (getEstadoAtual() != ESTADO_PARADO && getEstadoAtual() != ESTADO_ANDANDOPARATRAS) return;
        setEstadoAtual(ESTADO_ANDANDOPARAFRENTE);
        setVelX(VELOCIDADE);
    }

    public void andaParaTras() {
        if (getEstadoAtual() != ESTADO_PARADO && getEstadoAtual() != ESTADO_ANDANDOPARAFRENTE) return;
        setEstadoAtual(ESTADO_ANDANDOPARATRAS);
        setVelX(-VELOCIDADE);
    }

    public void para() {
        if (getEstadoAtual() != ESTADO_ANDANDOPARATRAS && getEstadoAtual() != ESTADO_ANDANDOPARAFRENTE) return;
        setEstadoAtual(ESTADO_PARADO);
        setVelX(0);
    }

    public void chuta() {
        if (getEstadoAtual() == ESTADO_CHUTANDO) return;
        setEstadoAtual(ESTADO_CHUTANDO);
        setVelX(0);
        new Thread(this).start();
    }

    public void run() {
        try {
            Thread.sleep(500);
            somChute.play();
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        setEstadoAtual(ESTADO_PARADO);
    }
}
