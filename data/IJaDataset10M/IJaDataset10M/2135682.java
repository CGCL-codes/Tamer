package capitulo3.highlevel.game.pacman;

import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;
import capitulo3.highlevel.game.pacman.sprites.Bolinha;
import capitulo3.highlevel.game.pacman.sprites.FantasmaSprite;
import capitulo3.highlevel.game.pacman.sprites.MovimentoSprite;
import capitulo3.highlevel.game.pacman.sprites.PacManSprite;
import capitulo3.highlevel.game.pacman.sprites.PoderSprite;

/**
 * Esta � a classe principal do jogo. Ela possui m�todos para criar  todos os
 * elementos gr�ficos, organizar sua disposi��o na tela e verificar eventos
 * do jogo. O construtor inicializa os elementos  e quando � chamado o m�todo
 * start inicia-se a thread principal, que ficar� em um ciclo respondendo aos
 * movimentos e atualizando o estado do jogo.
 */
public class PacManCanvas extends GameCanvas implements Runnable {

    private boolean executando;

    private PacManSprite pacMan;

    private FantasmaSprite[] fantasma;

    private Bolinha bolinhas;

    private PoderSprite[] poderes;

    private TiledLayer parede;

    private final int largura = getWidth();

    private final int altura = getHeight();

    private LayerManager layerManager;

    private int qtdBolinhas = 56;

    private int tempoPoder = 0;

    private int pontos = 0;

    /**
     * Chama os m�todos respons�veis pela cria��o e inicializa��o de todos os
     * elementos da tela.
     * @throws IOException
     */
    public PacManCanvas() throws IOException {
        super(true);
        pacMan = criarPacMan();
        inicializarSprite(pacMan, 72, 72, 'd');
        fantasma = new FantasmaSprite[2];
        fantasma[0] = criarFantasma();
        inicializarSprite(fantasma[0], 8, 112, 'c');
        fantasma[1] = criarFantasma();
        inicializarSprite(fantasma[1], 112, 8, 'e');
        bolinhas = new Bolinha();
        poderes = new PoderSprite[4];
        inicializarPoderes();
        parede = criarFase();
        layerManager = new LayerManager();
        layerManager.insert(parede, 0);
        layerManager.insert(pacMan, 1);
        layerManager.insert(fantasma[0], 2);
        layerManager.insert(fantasma[1], 3);
        for (int i = 0; i < poderes.length; i++) {
            layerManager.insert(poderes[i], (i + 4));
        }
    }

    /**
     * Obt�m a imagem referente ao Pacman e chama seu construtor.
     * @return PacManSprite
     * @throws IOException
     */
    private PacManSprite criarPacMan() throws IOException {
        Image imagem = Image.createImage("/imagens/pacman.png");
        return new PacManSprite(imagem, 16, 16);
    }

    /**
     * Obt�m a imagem referente ao Fantasma e chama seu construtor.
     * @return FantasmaSprite
     * @throws IOException
     */
    private FantasmaSprite criarFantasma() throws IOException {
        Image imagem = Image.createImage("/imagens/fantasma.png");
        return new FantasmaSprite(imagem, 16, 16);
    }

    /**
     * Define a dire��o e posi��o do sprite na tela.
     * @param sprite Sprite (PacMan ou Fantasma) que ser� posicionado
     * @param pH Coordenada horizontal
     * @param pV Coordenada vertical
     * @param direcao c = cima, b = baixo, d = direita, e = esquerda
     */
    private void inicializarSprite(MovimentoSprite sprite, int pH, int pV, char direcao) {
        sprite.setPosition(pH, pV);
        sprite.setDirecao(direcao);
    }

    /**
     * Inicializa os poderes dentro da array e define o posicionamendo de cada
     * um na tela.
     * @throws IOException
     */
    private void inicializarPoderes() throws IOException {
        Image imagem = Image.createImage("/imagens/poder.png");
        for (int i = 0; i < poderes.length; i++) {
            poderes[i] = new PoderSprite(imagem, 11, 11);
        }
        final int[] x = { 16, 16, 144, 144 };
        final int[] y = { 16, 144, 16, 144 };
        for (int i = 0; i < poderes.length; i++) {
            poderes[i].setRefPixelPosition(x[i], y[i]);
        }
    }

    /**
     * Cria a TiledLayer que possui o layout da estrutura da fase.
     * @return TiledLayer
     * @throws IOException
     */
    private TiledLayer criarFase() throws IOException {
        Image imagem = Image.createImage("/imagens/parede.png");
        TiledLayer fase = new TiledLayer(10, 10, imagem, 16, 16);
        final int[] mapa = { 1, 7, 7, 7, 7, 7, 7, 7, 7, 2, 10, 9, 0, 0, 5, 6, 0, 0, 8, 20, 10, 13, 17, 0, 10, 20, 0, 17, 14, 20, 10, 0, 0, 0, 0, 0, 0, 0, 0, 20, 10, 3, 7, 7, 0, 0, 7, 7, 4, 20, 10, 13, 17, 17, 0, 0, 17, 17, 14, 20, 10, 0, 0, 0, 0, 0, 0, 0, 0, 20, 10, 3, 7, 0, 10, 20, 0, 7, 4, 20, 10, 19, 0, 0, 15, 16, 0, 0, 18, 20, 11, 17, 17, 17, 17, 17, 17, 17, 17, 12 };
        for (int i = 0; i < mapa.length; i++) {
            int coluna = i % 10;
            int linha = (i - coluna) / 10;
            fase.setCell(coluna, linha, mapa[i]);
        }
        return fase;
    }

    /**
     * Inicia a thread de execu��o do jogo.
     */
    public void start() {
        executando = true;
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Neste m�todo fica o c�digo a ser executado pela thread principal  do
     * jogo, que rodar� continuamente at� que seja atribuido o valor  false �
     * vari�vel "executando". A cada itera��o � atualizado o  estado do jogo
     * de acordo com as instru��es do usu�rio e de suas  regras de
     * funcionamento internas.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        Graphics g = getGraphics();
        final int tempo = 200;
        while (executando) {
            long inicio = System.currentTimeMillis();
            movimenta();
            verificaPosicaoParede();
            verificaBolinha();
            verificaPoder();
            verificaFantasma();
            if (qtdBolinhas == 0) {
                reinicializaFase();
            }
            renderiza(g);
            long fim = System.currentTimeMillis();
            int duracao = (int) (fim - inicio);
            if (duracao < tempo) {
                try {
                    Thread.sleep(tempo - duracao);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        Font fonte = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(fonte);
        g.setColor(0x000000);
        String s = Integer.toString(pontos);
        g.drawString(s, largura / 2, altura / 2, Graphics.BASELINE | Graphics.HCENTER);
        flushGraphics();
        pacMan = null;
        fantasma = null;
        bolinhas = null;
        poderes = null;
        parede = null;
        layerManager = null;
    }

    /**
     * Obt�m o estado das teclas direcionais e chama o m�todo correspondente
     * ao movimento.
     */
    private void movimenta() {
        int keyStates = getKeyStates();
        if ((keyStates & LEFT_PRESSED) != 0) {
            pacMan.esquerda();
        } else if ((keyStates & RIGHT_PRESSED) != 0) {
            pacMan.direita();
        } else if ((keyStates & UP_PRESSED) != 0) {
            pacMan.cima();
        } else if ((keyStates & DOWN_PRESSED) != 0) {
            pacMan.baixo();
        }
        for (int f = 0; f < fantasma.length; f++) {
            if (fantasma[f] != null) {
                fantasma[f].andar();
            }
        }
    }

    /**
     * Verifica se o PacMan ou fantasma colidiu com alguma parede e
     * anula o movimento.
     */
    private void verificaPosicaoParede() {
        if (pacMan.collidesWith(parede, true)) {
            pacMan.voltar();
        }
        for (int f = 0; f < fantasma.length; f++) {
            if ((fantasma[f] != null) && fantasma[f].collidesWith(parede, true)) {
                fantasma[f].voltar();
            }
        }
    }

    /**
     * Verifica se o PacMan comeu alguma bolinha.
     */
    private void verificaBolinha() {
        for (int i = 0; i < bolinhas.estado.length; i++) {
            if ((bolinhas.estado[i]) && pacMan.collidesWith(bolinhas.imagem, bolinhas.x[i], bolinhas.y[i], true)) {
                bolinhas.remove(i);
                qtdBolinhas--;
                pontos++;
            }
        }
    }

    /**
     * Determina o estado de invencibilidade do PacMan.
     */
    private void verificaPoder() {
        for (int i = 0; i < poderes.length; i++) {
            if ((poderes[i] != null) && pacMan.collidesWith(poderes[i], true)) {
                layerManager.remove(poderes[i]);
                poderes[i] = null;
                qtdBolinhas--;
                pontos += 5;
                for (int f = 0; f < fantasma.length; f++) {
                    if ((fantasma[f] != null) && (tempoPoder == 0)) {
                        fantasma[f].nextFrame();
                    }
                }
                tempoPoder = 100;
            }
        }
        if (tempoPoder > 0) {
            tempoPoder--;
            if (tempoPoder == 1) {
                for (int f = 0; f < fantasma.length; f++) {
                    if (fantasma[f] != null) {
                        fantasma[f].nextFrame();
                    }
                }
            }
        } else {
            for (int f = 0; f < fantasma.length; f++) {
                if (fantasma[f] == null) {
                    try {
                        fantasma[f] = criarFantasma();
                        if (f == 0) {
                            inicializarSprite(fantasma[f], 8, 112, 'c');
                        }
                        if (f == 1) {
                            inicializarSprite(fantasma[f], 112, 8, 'e');
                        }
                        layerManager.insert(fantasma[f], 2);
                    } catch (IOException e) {
                        System.out.println("Erro ao recriar fantasma");
                    }
                }
            }
        }
    }

    /**
     * Verifica se PacMan colidiu com algum fantasma.
     */
    private void verificaFantasma() {
        for (int f = 0; f < fantasma.length; f++) {
            if ((fantasma[f] != null) && pacMan.collidesWith(fantasma[f], true)) {
                if (tempoPoder == 0) {
                    executando = false;
                } else {
                    layerManager.remove(fantasma[f]);
                    fantasma[f] = null;
                    pontos += 20;
                }
            }
        }
    }

    /**
     * Recria todas as bolinhas da fase.
     */
    private void reinicializaFase() {
        try {
            qtdBolinhas = 56;
            inicializarPoderes();
            for (int i = 0; i < poderes.length; i++) {
                layerManager.insert(poderes[i], (i + 4));
            }
            bolinhas.reinicializa();
        } catch (IOException e) {
            System.out.println("Erro ao recriar bolinhas");
        }
    }

    /**
     * Coordena a renderiza��o da tela.
     * @param g Objeto gr�fico onde ser� renderizado o buffer da GameCanvas
     */
    private void renderiza(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, largura, altura);
        int x = (largura - 160) / 2;
        int y = (altura - 160) / 2;
        bolinhas.paint(g, x, y);
        layerManager.paint(g, x, y);
        flushGraphics();
    }

    /**
     * Sinaliza a �ltima execu��o do ciclo principal do jogo.
     */
    public void stop() {
        executando = false;
    }
}
