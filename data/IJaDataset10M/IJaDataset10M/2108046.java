package br.com.betioli.jme.games.sokobob;

import br.com.betioli.jme.games.sokobob.config.GameConfig;
import br.com.betioli.jme.games.sokobob.i18n.Messages;
import br.com.betioli.jme.games.sokobob.level.Level;
import br.com.betioli.jme.games.sokobob.level.LevelFactory;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class SokoBobCanvas extends GameCanvas implements Runnable {

    /**
     * Variáveis utilizadas para renderizar a tela
     */
    private int xOffset;

    private int yOffset;

    private Graphics g;

    /**
     * Comandos disponíveis durante o jogo
     */
    private Command restartLevelCommand;

    private Command backToMenuCommand;

    /**
     * Carrega o mapa e mantém o status do jogo
     */
    private Level currentLevel;

    /**
     * Indica se o jogo está rodando 
     */
    private volatile boolean running;

    private volatile Thread currentThread;

    /**
     * Variável de controle para impedir que o pressionamento da tecla provoque 
     * mais de um movimento por ação. Ou seja, cada vez que a tecla direcional
     * for pressionada, o ator moverá apenas um espaço.
     */
    private boolean moved;

    /**
     * A chamada explícita ao contrutor da superclasse define se os métodos 
     * keyPressed e keyReleased serão ou não invocados. (suppress key events)
     * false habilita a chamada aos métodos em questão.
     * 
     * Será necessário para definir apenas um movimento a cada vez que a tecla 
     * for pressionada, o qual será controlado através da variável moved.
     */
    public SokoBobCanvas() {
        super(false);
        xOffset = (getWidth() - GameConfig.COLUMNS * GameConfig.TILE_WIDTH) / 2;
        yOffset = (getHeight() - GameConfig.ROWS * GameConfig.TILE_HEIGHT) / 2;
        g = getGraphics();
        restartLevelCommand = new Command(Messages.getMessage("command-restart"), Command.CANCEL, 0);
        backToMenuCommand = new Command(Messages.getMessage("command-menu"), Command.STOP, 0);
        addCommand(restartLevelCommand);
        addCommand(backToMenuCommand);
        currentLevel = loadLevel(0);
    }

    /**
     * Inicia jogo criando uma thread separada
     */
    public void start() {
        if (!running) {
            running = true;
            currentThread = new Thread(this, "SokoBobCanvas");
            currentThread.start();
        }
        currentLevel.getBobSprite().startAnimation();
    }

    /**
     * Loop principal do jogo
     */
    public void run() {
        while (isRunning()) {
            long start = System.currentTimeMillis();
            readInput();
            currentLevel.checkCollision();
            render();
            if (currentLevel.verifyWinCondition()) {
                nextLevel();
                try {
                    Thread.sleep(GameConfig.TIME_STEP * 5);
                } catch (InterruptedException ie) {
                    stop();
                }
                render();
            }
            long end = System.currentTimeMillis();
            int duration = (int) (end - start);
            if (duration < GameConfig.TIME_STEP) {
                try {
                    Thread.sleep(GameConfig.TIME_STEP - duration);
                } catch (InterruptedException ie) {
                    stop();
                }
            }
        }
    }

    public void stop() {
        currentLevel.getBobSprite().stopAnimation();
        running = false;
    }

    /**
     * Libera ação para próximo movimento
     */
    protected void keyReleased(int arg0) {
        super.keyReleased(arg0);
        moved = false;
    }

    /**
     * Lê entrada do teclado do celular. Note que mais de uma tecla pode ser 
     * lida ao mesmo tempo, porém nesta aplicação é considerada apenas uma por 
     * vez.
     */
    private void readInput() {
        if (!moved) {
            getKeyStates();
            int keyStates = getKeyStates();
            if (keyStates > 0) {
                currentLevel.getBobSprite().move(keyStates);
                moved = true;
            }
        }
    }

    /**
     * Desenha a tela
     */
    private void render() {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());
        currentLevel.paint(g, xOffset, yOffset);
        g.setColor(0x000000);
        g.drawRect(xOffset, yOffset, GameConfig.COLUMNS * GameConfig.TILE_WIDTH, GameConfig.ROWS * GameConfig.TILE_HEIGHT);
        flushGraphics();
    }

    public void newGame() {
        currentLevel = loadLevel(0);
        start();
    }

    public void restartLevel() {
        currentLevel = loadLevel(currentLevel.getLevelIndex());
    }

    public void nextLevel() {
        currentLevel = loadLevel(currentLevel.getLevelIndex() + 1);
    }

    private Level loadLevel(int index) {
        if (!LevelFactory.load(index)) {
            SokoBobMIDlet.getInstance().showWinnerAlert();
            stop();
        }
        return LevelFactory.getLevel();
    }

    public boolean isRunning() {
        return running;
    }
}
