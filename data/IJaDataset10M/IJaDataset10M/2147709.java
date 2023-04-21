package controle;

import componentes.Aviao;
import interfaces.PainelFase;

/**
 * 
 * Classe AviaoThread.java que representa
 * 
 * @desc Projeto Jogo do Avião
 * @author Simone Carneiro Streitenberger 11888 CCO2003
 * @author Thiago Nascimento Comicio 11889 CCO2003
 * @professor Edison de Jesus
 * @disciplina Computação Gráfica 2 - CCO812
 * 
 */
public class AviaoThread extends Thread {

    private Aviao aviao;

    private PainelFase painel;

    private boolean pause;

    private double deslocamento;

    private int destrua;

    /**
	 * Construtor da classe AviaoThread
	 * 
	 * @param aviao
	 * @param painel
	 */
    public AviaoThread(Aviao aviao, PainelFase painel) {
        this.aviao = aviao;
        this.painel = painel;
        this.pause = false;
        destrua = 0;
    }

    public void run() {
        double j, i = 0, t = 0;
        int y, x;
        for (; ; ) {
            if (!pause) {
                j = Math.sin(i - t);
                y = (int) Math.round(j * 45.0);
                x = (int) Math.round(i * 10.0);
                aviao.setPosicao(x, y);
                painel.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i += deslocamento;
                if (x >= (painel.getWidth() + 150)) {
                    i = 0;
                    t = 1.5 * Math.PI * Math.random();
                }
            }
            if (destrua == 1) {
                break;
            }
        }
    }

    /**
	 * 
	 * Método que retorna o valor do atributo aviao.
	 * 
	 * @return O atributo aviao.
	 * 
	 */
    public Aviao getAviao() {
        return aviao;
    }

    /**
	 * 
	 * Método que seta o atributo aviao para um novo valor.
	 * 
	 * @param aviao
	 *            O atributo aviao � setado.
	 * 
	 */
    public void setAviao(Aviao aviao) {
        this.aviao = aviao;
    }

    /**
	 * 
	 * Método que retorna o valor do atributo painel.
	 * 
	 * @return O atributo painel.
	 * 
	 */
    public PainelFase getPainel() {
        return painel;
    }

    /**
	 * 
	 * Método que seta o atributo painel para um novo valor.
	 * 
	 * @param painel
	 *            O atributo painel � setado.
	 * 
	 */
    public void setPainel(PainelFase painel) {
        this.painel = painel;
    }

    /**
	 * 
	 * Método que retorna o valor do atributo pause.
	 * 
	 * @return O atributo pause.
	 * 
	 */
    public boolean isPause() {
        return pause;
    }

    /**
	 * 
	 * Método que seta o atributo pause para um novo valor.
	 * 
	 * @param pause
	 *            O atributo pause � setado.
	 * 
	 */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
	 * 
	 * Método que retorna o valor do atributo deslocamento.
	 * 
	 * @return O atributo deslocamento.
	 * 
	 */
    public double getDeslocamento() {
        return deslocamento;
    }

    /**
	 * 
	 * Método que seta o atributo deslocamento para um novo valor.
	 * 
	 * @param deslocamento
	 *            O atributo deslocamento � setado.
	 * 
	 */
    public void setDeslocamento(double deslocamento) {
        this.deslocamento = deslocamento;
    }

    public void destrua() {
        this.destrua = 1;
    }
}
