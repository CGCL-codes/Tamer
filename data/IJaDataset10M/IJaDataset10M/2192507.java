package multiplayer;

import javax.swing.*;
import multiplayer.*;
import java.awt.*;
import java.awt.event.*;

/** Essa classe  o qradro por onde se da entrada de dados.
  * @version 1.0
  * @author Carlos Alberto Sampaio - 2002
  */
public class Quadriculado extends JLabel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Point ponto = null;

    private Dimension objDim;

    private Dimension quadriSize;

    private int largQuad;

    private int nQuad;

    private int matrizObjeto[][];

    private Color corMouse;

    private int i, j;

    /** Construtor
      * @param numeroQuad numero de celulas
      * @param Lado largura total do quadro
      * @param mObj matriz que indica objetos do mundo
      */
    public Quadriculado(int numeroQuad, int Lado, int mObj[][]) {
        nQuad = numeroQuad;
        quadriSize = new Dimension(Lado, Lado);
        matrizObjeto = mObj;
        largQuad = (int) quadriSize.height / nQuad;
        setBackground(new Color(0.98f, 0.97f, 0.85f));
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        Objeto2 obj;
        for (i = 0; i < (nQuad - 1); i++) for (j = 0; j < (nQuad - 1); j++) if (matrizObjeto[i][j] > 1) {
            obj = new Objeto2(matrizObjeto[i][j]);
            g.fillRect((i * largQuad + 1), (j * largQuad + 1), (obj.larg * largQuad - 1), (obj.comp * largQuad - 1));
        }
        if (ponto != null) {
            g.setColor(corMouse);
            g.fillRect(ponto.x + 1, ponto.y + 1, objDim.width - 1, objDim.height - 1);
        }
        g.setColor(Color.black);
        for (int i = 0; i < nQuad; i++) {
            g.drawLine(i * largQuad, 0, i * largQuad, quadriSize.height);
            g.drawLine(0, i * largQuad, quadriSize.width, i * largQuad);
        }
    }

    public void setObjDim(int largura, int comprimento) {
        int lAux = (int) (largura * largQuad);
        int cAux = (int) (comprimento * largQuad);
        objDim = new Dimension(lAux, cAux);
    }

    public void setMatriz(int mObj[][]) {
        matrizObjeto = mObj;
    }

    public void setPonto(Point pont) {
        ponto = new Point(0, 0);
        for (int i = 0; i < nQuad; i++) {
            if (pont.x == (i * largQuad)) ponto.x = pont.x; else if ((pont.x < (i + 1) * largQuad) && (pont.x > i * largQuad)) ponto.x = (int) i * largQuad;
            if (pont.y == (i * largQuad)) ponto.y = pont.y; else if ((pont.y < (i + 1) * largQuad) && (pont.y > i * largQuad)) ponto.y = (int) i * largQuad;
        }
    }

    public void setPontoNull() {
        ponto = null;
    }

    public void setCorMouse(boolean pode) {
        if (pode) corMouse = Color.blue; else corMouse = Color.red;
    }

    public int getLarg() {
        return largQuad;
    }

    public Dimension getMinimumSize() {
        return quadriSize;
    }

    public Dimension getPreferredSize() {
        return quadriSize;
    }

    public Dimension getMaxSize() {
        return quadriSize;
    }
}
