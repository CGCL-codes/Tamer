package aprendizajePiezas;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author  Billy
 */
public class manejadorPiezas extends javax.swing.JInternalFrame {

    Pieza[] piezasVivas;

    Pieza unaPieza;

    JLabel lblTorre1, lblTorre2;

    JPanel panelTorre;

    JPanel chessBoard;

    boolean yamoviTorreEnroqueLargo = false;

    boolean yamoviTorreEnroqueCorto = false;

    boolean yamoviTorreEnroqueLargoBlanco = false;

    boolean yamoviTorreEnroqueCortoBlanco = false;

    boolean yamoviRey = false;

    boolean yaEnroque = false;

    Color ColorClaro = Color.ORANGE;

    Color ColorOscuro = Color.GRAY;

    ManejadordeTablero mdt;

    Evento evt;

    boolean bestoyenLectura;

    int tipodeEnroque = 0;

    Map nombreaIndicePieza = new HashMap();

    char datoAdicional = '0';

    boolean estoyJugando = false;

    String tipoPieza;

    /**
     * Creates new form manejadorPiezas
     */
    public manejadorPiezas() {
    }

    public manejadorPiezas(JPanel cb, ManejadordeTablero mdt, Evento evt, String tipoPieza) {
        initComponents();
        chessBoard = cb;
        this.mdt = mdt;
        this.evt = evt;
        this.bestoyenLectura = false;
        this.datoAdicional = '0';
        this.estoyJugando = false;
        this.tipoPieza = tipoPieza;
    }

    public boolean getEstoyJugando() {
        return estoyJugando;
    }

    public boolean getEstoyenLectura() {
        return bestoyenLectura;
    }

    public void setEstoyenLectura(boolean bandera) {
        bestoyenLectura = bandera;
    }

    public Evento dameEvento() {
        return evt;
    }

    public void IniciarPiezas(String tipoPieza) {
        JLabel piece2;
        JPanel panel2;
        if (tipoPieza.equals("Alfil") == true) {
            piezasVivas = new Pieza[4];
            piece2 = new JLabel(new ImageIcon("imgPiezas/BB.gif"));
            piece2.setName("NegroAlfil1");
            panel2 = (JPanel) chessBoard.getComponent(2);
            panel2.add(piece2);
            piezasVivas[0] = new Alfil("NegroAlfil1", "c8", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BB.gif"));
            piece2.setName("NegroAlfil2");
            panel2 = (JPanel) chessBoard.getComponent(5);
            panel2.add(piece2);
            piezasVivas[1] = new Alfil("NegroAlfil2", "f8", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WB.gif"));
            piece2.setName("BlancoAlfil1");
            panel2 = (JPanel) chessBoard.getComponent(58);
            panel2.add(piece2);
            piezasVivas[2] = new Alfil("BlancoAlfil1", "c1", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WB.gif"));
            piece2.setName("BlancoAlfil2");
            panel2 = (JPanel) chessBoard.getComponent(61);
            panel2.add(piece2);
            piezasVivas[3] = new Alfil("BlancoAlfil2", "f1", piece2);
        } else if (tipoPieza.equals("Torre") == true) {
            piezasVivas = new Pieza[4];
            lblTorre2 = new JLabel(new ImageIcon("imgPiezas/BR.gif"));
            lblTorre2.setName("NegroTorre1");
            panel2 = (JPanel) chessBoard.getComponent(0);
            panel2.add(lblTorre2);
            piezasVivas[0] = new Torre("NegroTorre1", "a8", lblTorre2);
            lblTorre1 = new JLabel(new ImageIcon("imgPiezas/BR.gif"));
            lblTorre1.setName("NegroTorre2");
            panelTorre = (JPanel) chessBoard.getComponent(7);
            panelTorre.add(lblTorre1);
            piezasVivas[1] = new Torre("NegroTorre2", "h8", lblTorre1);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WR.gif"));
            piece2.setName("BlancoTorre1");
            panel2 = (JPanel) chessBoard.getComponent(56);
            panel2.add(piece2);
            piezasVivas[2] = new Torre("BlancoTorre1", "a1", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WR.gif"));
            piece2.setName("BlancoTorre2");
            panel2 = (JPanel) chessBoard.getComponent(63);
            panel2.add(piece2);
            piezasVivas[3] = new Torre("BlancoTorre2", "h1", piece2);
        } else if (tipoPieza.equals("Caballo") == true) {
            piezasVivas = new Pieza[4];
            piece2 = new JLabel(new ImageIcon("imgPiezas/BN.gif"));
            piece2.setName("NegroCaballo1");
            panel2 = (JPanel) chessBoard.getComponent(1);
            panel2.add(piece2);
            piezasVivas[0] = new Caballo("NegroCaballo1", "b8", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BN.gif"));
            piece2.setName("NegroCaballo2");
            panel2 = (JPanel) chessBoard.getComponent(6);
            panel2.add(piece2);
            piezasVivas[1] = new Caballo("NegroCaballo2", "g8", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WN.gif"));
            piece2.setName("BlancoCaballo1");
            panel2 = (JPanel) chessBoard.getComponent(57);
            panel2.add(piece2);
            piezasVivas[2] = new Caballo("BlancoCaballo1", "b1", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WN.gif"));
            piece2.setName("BlancoCaballo2");
            panel2 = (JPanel) chessBoard.getComponent(62);
            panel2.add(piece2);
            piezasVivas[3] = new Caballo("BlancoCaballo2", "g1", piece2);
        } else if (tipoPieza.equals("Rey") == true) {
            piezasVivas = new Pieza[2];
            piece2 = new JLabel(new ImageIcon("imgPiezas/BK.gif"));
            piece2.setName("NegroRey");
            panel2 = (JPanel) chessBoard.getComponent(4);
            panel2.add(piece2);
            piezasVivas[0] = new Rey("NegroRey", "e8", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WK.gif"));
            piece2.setName("BlancoRey");
            panel2 = (JPanel) chessBoard.getComponent(60);
            panel2.add(piece2);
            piezasVivas[1] = new Rey("BlancoRey", "e1", piece2);
        } else if (tipoPieza.equals("Dama") == true) {
            piezasVivas = new Pieza[2];
            piece2 = new JLabel(new ImageIcon("imgPiezas/BQ.gif"));
            piece2.setName("NegroDama");
            panel2 = (JPanel) chessBoard.getComponent(3);
            panel2.add(piece2);
            piezasVivas[0] = new Dama("NegroDama", "d8", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WQ.gif"));
            piece2.setName("BlancoDama");
            panel2 = (JPanel) chessBoard.getComponent(59);
            panel2.add(piece2);
            piezasVivas[1] = new Dama("BlancoDama", "d1", piece2);
        } else if (tipoPieza.equals("Peon") == true) {
            piezasVivas = new Pieza[18];
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon1");
            panel2 = (JPanel) chessBoard.getComponent(8);
            panel2.add(piece2);
            piezasVivas[0] = new Peon("NegroPeon1", "a7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon2");
            panel2 = (JPanel) chessBoard.getComponent(9);
            panel2.add(piece2);
            piezasVivas[1] = new Peon("NegroPeon2", "b7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon3");
            panel2 = (JPanel) chessBoard.getComponent(10);
            panel2.add(piece2);
            piezasVivas[2] = new Peon("NegroPeon3", "c7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon4");
            panel2 = (JPanel) chessBoard.getComponent(11);
            panel2.add(piece2);
            piezasVivas[3] = new Peon("NegroPeon4", "d7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon5");
            panel2 = (JPanel) chessBoard.getComponent(12);
            panel2.add(piece2);
            piezasVivas[4] = new Peon("NegroPeon5", "e7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon6");
            panel2 = (JPanel) chessBoard.getComponent(13);
            panel2.add(piece2);
            piezasVivas[5] = new Peon("NegroPeon6", "f7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon7");
            panel2 = (JPanel) chessBoard.getComponent(14);
            panel2.add(piece2);
            piezasVivas[6] = new Peon("NegroPeon7", "g7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BP.gif"));
            piece2.setName("NegroPeon8");
            panel2 = (JPanel) chessBoard.getComponent(15);
            panel2.add(piece2);
            piezasVivas[7] = new Peon("NegroPeon8", "h7", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/BQ.gif"));
            piece2.setName("NegroDama2");
            piezasVivas[8] = new Dama("NegroDama2", "", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon1");
            panel2 = (JPanel) chessBoard.getComponent(48);
            panel2.add(piece2);
            piezasVivas[9] = new Peon("BlancoPeon1", "a2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon2");
            panel2 = (JPanel) chessBoard.getComponent(49);
            panel2.add(piece2);
            piezasVivas[10] = new Peon("BlancoPeon2", "b2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon3");
            panel2 = (JPanel) chessBoard.getComponent(50);
            panel2.add(piece2);
            piezasVivas[11] = new Peon("BlancoPeon3", "c2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon4");
            panel2 = (JPanel) chessBoard.getComponent(51);
            panel2.add(piece2);
            piezasVivas[12] = new Peon("BlancoPeon4", "d2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon5");
            panel2 = (JPanel) chessBoard.getComponent(52);
            panel2.add(piece2);
            piezasVivas[13] = new Peon("BlancoPeon5", "e2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon6");
            panel2 = (JPanel) chessBoard.getComponent(53);
            panel2.add(piece2);
            piezasVivas[14] = new Peon("BlancoPeon6", "f2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon7");
            panel2 = (JPanel) chessBoard.getComponent(54);
            panel2.add(piece2);
            piezasVivas[15] = new Peon("BlancoPeon7", "g2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WP.gif"));
            piece2.setName("BlancoPeon8");
            panel2 = (JPanel) chessBoard.getComponent(55);
            panel2.add(piece2);
            piezasVivas[16] = new Peon("BlancoPeon8", "h2", piece2);
            piece2 = new JLabel(new ImageIcon("imgPiezas/WQ.gif"));
            piece2.setName("BlancoDama2");
            piezasVivas[17] = new Dama("BlancoDama2", "", piece2);
        }
        for (int n = 0; n < piezasVivas.length; n++) {
            nombreaIndicePieza.put(piezasVivas[n].getNombre(), new Integer(n));
        }
    }

    public void resetPosiciones() {
        if (this.tipoPieza.equals("Alfil") == true) {
            piezasVivas[0].setPosicion("c8");
            piezasVivas[1].setPosicion("f8");
            piezasVivas[2].setPosicion("c1");
            piezasVivas[3].setPosicion("f1");
        } else if (this.tipoPieza.equals("Caballo") == true) {
            piezasVivas[0].setPosicion("b8");
            piezasVivas[1].setPosicion("g8");
            piezasVivas[2].setPosicion("b1");
            piezasVivas[3].setPosicion("g1");
        } else if (this.tipoPieza.equals("Torre") == true) {
            piezasVivas[0].setPosicion("a8");
            piezasVivas[1].setPosicion("h8");
            piezasVivas[2].setPosicion("a1");
            piezasVivas[3].setPosicion("h1");
        } else if (this.tipoPieza.equals("Dama") == true) {
            piezasVivas[0].setPosicion("d8");
            piezasVivas[1].setPosicion("d1");
        } else if (this.tipoPieza.equals("Rey") == true) {
            piezasVivas[0].setPosicion("e8");
            piezasVivas[1].setPosicion("e1");
        } else if (this.tipoPieza.equals("Peon") == true) {
            piezasVivas[0].setPosicion("a7");
            piezasVivas[1].setPosicion("b7");
            piezasVivas[2].setPosicion("c7");
            piezasVivas[3].setPosicion("d7");
            piezasVivas[4].setPosicion("e7");
            piezasVivas[5].setPosicion("f7");
            piezasVivas[6].setPosicion("g7");
            piezasVivas[7].setPosicion("h7");
            piezasVivas[8].setPosicion("a2");
            piezasVivas[9].setPosicion("b2");
            piezasVivas[10].setPosicion("c2");
            piezasVivas[11].setPosicion("d2");
            piezasVivas[12].setPosicion("e2");
            piezasVivas[13].setPosicion("f2");
            piezasVivas[14].setPosicion("g2");
            piezasVivas[15].setPosicion("h2");
        }
        for (int i = 0; i < piezasVivas.length; i++) {
            piezasVivas[i].setVivo(true);
        }
    }

    Pieza[] dametodasPiezas() {
        return piezasVivas;
    }

    public void ImprimirPosiblesJugadas() {
        int n, p, tam;
        for (n = 0; n < piezasVivas.length; n++) {
            if ((n != 16 && n != 33) && piezasVivas[n].getVivo() == true) {
                System.out.println("Nombre::: " + n + ":: " + piezasVivas[n].getNombre());
                piezasVivas[n].CalcularMovimientos(this);
                tam = piezasVivas[n].dameMovimientosPosibles().size();
                System.out.println("tam::: " + tam);
                System.out.println("Posibles Posiciones::: ");
                if (tam > 0) for (p = 0; p < tam; p++) {
                    java.util.List miLista = piezasVivas[n].dameMovimientosPosibles();
                    System.out.println(miLista.get(p));
                }
            }
        }
    }

    public void setDatoAdicional(char dato) {
        this.datoAdicional = dato;
    }

    public String damePiezaCoincidePosiblePosicion(String notacion, char tipoColor, char tipoPieza) {
        String piezaEncontrada = "XX";
        int n, p, tam;
        for (n = 0; n < piezasVivas.length; n++) {
            if (piezasVivas[n].getVivo() == true) {
                System.out.println("Nombre::: " + n + ":: " + piezasVivas[n].getNombre());
                piezasVivas[n].CalcularMovimientos(this);
                tam = piezasVivas[n].dameMovimientosPosibles().size();
                System.out.println("tam::: " + tam);
                System.out.println("Dato Adicional::: " + datoAdicional);
                if (tam > 0) for (p = 0; p < tam; p++) {
                    java.util.List miLista = piezasVivas[n].dameMovimientosPosibles();
                    String dato = (String) miLista.get(p);
                    if (dato.equals(notacion) && datoAdicional == '0') {
                        if (tipoColor == 'B' && piezasVivas[n].getNombre().charAt(6) == tipoPieza) return piezasVivas[n].getNombre(); else if (tipoColor == 'N' && piezasVivas[n].getNombre().charAt(5) == tipoPieza) return piezasVivas[n].getNombre();
                    }
                    if (dato.equals(notacion) && datoAdicional != '0') {
                        if (tipoColor == 'B' && piezasVivas[n].getNombre().charAt(6) == tipoPieza && piezasVivas[n].getPosicion().charAt(0) == datoAdicional) return piezasVivas[n].getNombre(); else if (tipoColor == 'N' && piezasVivas[n].getNombre().charAt(5) == tipoPieza && piezasVivas[n].getPosicion().charAt(0) == datoAdicional) return piezasVivas[n].getNombre(); else if (tipoColor == 'B' && piezasVivas[n].getNombre().charAt(6) == tipoPieza && piezasVivas[n].getPosicion().charAt(1) == datoAdicional) return piezasVivas[n].getNombre(); else if (tipoColor == 'N' && piezasVivas[n].getNombre().charAt(5) == tipoPieza && piezasVivas[n].getPosicion().charAt(1) == datoAdicional) return piezasVivas[n].getNombre();
                    }
                }
            }
        }
        return piezaEncontrada;
    }

    public Pieza damePieza(String nombrePieza) {
        int numeroPieza = (Integer) nombreaIndicePieza.get(nombrePieza);
        Pieza piezaDevuelta = piezasVivas[numeroPieza];
        return piezaDevuelta;
    }

    JLabel dameEtiquetaTorre2() {
        return lblTorre2;
    }

    JLabel dameEtiquetaTorre1() {
        return lblTorre1;
    }

    public JLabel dameEtiqueta(String nombrePieza) {
        for (int i = 0; i < piezasVivas.length; i++) {
            if (piezasVivas[i].dameIcono().getName().equals(nombrePieza) == true) return piezasVivas[i].dameIcono();
        }
        return null;
    }

    public Pieza IdentificarPiezaElegida(JLabel PiezaLabel, String tipoPieza) {
        unaPieza = null;
        if (tipoPieza.equals("Torre")) {
            if (PiezaLabel.getName().equals("NegroTorre1")) unaPieza = piezasVivas[0]; else if (PiezaLabel.getName().equals("NegroTorre2")) unaPieza = piezasVivas[1]; else if (PiezaLabel.getName().equals("BlancoTorre1")) unaPieza = piezasVivas[2]; else if (PiezaLabel.getName().equals("BlancoTorre2")) unaPieza = piezasVivas[3];
        } else if (tipoPieza.equals("Alfil")) {
            if (PiezaLabel.getName().equals("NegroAlfil1")) unaPieza = piezasVivas[0]; else if (PiezaLabel.getName().equals("NegroAlfil2")) unaPieza = piezasVivas[1]; else if (PiezaLabel.getName().equals("BlancoAlfil1")) unaPieza = piezasVivas[2]; else if (PiezaLabel.getName().equals("BlancoAlfil2")) unaPieza = piezasVivas[3];
        } else if (tipoPieza.equals("Caballo")) {
            if (PiezaLabel.getName().equals("NegroCaballo1")) unaPieza = piezasVivas[0]; else if (PiezaLabel.getName().equals("NegroCaballo2")) unaPieza = piezasVivas[1]; else if (PiezaLabel.getName().equals("BlancoCaballo1")) unaPieza = piezasVivas[2]; else if (PiezaLabel.getName().equals("BlancoCaballo2")) unaPieza = piezasVivas[3];
        } else if (tipoPieza.equals("Dama")) {
            if (PiezaLabel.getName().equals("NegroDama")) unaPieza = piezasVivas[0]; else if (PiezaLabel.getName().equals("BlancoDama")) unaPieza = piezasVivas[1];
        } else if (tipoPieza.equals("Rey")) {
            if (PiezaLabel.getName().equals("NegroRey")) unaPieza = piezasVivas[0]; else if (PiezaLabel.getName().equals("BlancoRey")) unaPieza = piezasVivas[1];
        } else if (tipoPieza.equals("Peon")) {
            if (PiezaLabel.getName().equals("NegroPeon1")) unaPieza = piezasVivas[0]; else if (PiezaLabel.getName().equals("NegroPeon2")) unaPieza = piezasVivas[1]; else if (PiezaLabel.getName().equals("NegroPeon3")) unaPieza = piezasVivas[2]; else if (PiezaLabel.getName().equals("NegroPeon4")) unaPieza = piezasVivas[3]; else if (PiezaLabel.getName().equals("NegroPeon5")) unaPieza = piezasVivas[4]; else if (PiezaLabel.getName().equals("NegroPeon6")) unaPieza = piezasVivas[5]; else if (PiezaLabel.getName().equals("NegroPeon7")) unaPieza = piezasVivas[6]; else if (PiezaLabel.getName().equals("NegroPeon8")) unaPieza = piezasVivas[7]; else if (PiezaLabel.getName().equals("NegroDama2")) unaPieza = piezasVivas[8]; else if (PiezaLabel.getName().equals("BlancoPeon1")) unaPieza = piezasVivas[9]; else if (PiezaLabel.getName().equals("BlancoPeon2")) unaPieza = piezasVivas[10]; else if (PiezaLabel.getName().equals("BlancoPeon3")) unaPieza = piezasVivas[11]; else if (PiezaLabel.getName().equals("BlancoPeon4")) unaPieza = piezasVivas[12]; else if (PiezaLabel.getName().equals("BlancoPeon5")) unaPieza = piezasVivas[13]; else if (PiezaLabel.getName().equals("BlancoPeon6")) unaPieza = piezasVivas[14]; else if (PiezaLabel.getName().equals("BlancoPeon7")) unaPieza = piezasVivas[15]; else if (PiezaLabel.getName().equals("BlancoPeon8")) unaPieza = piezasVivas[16]; else if (PiezaLabel.getName().equals("BlancoDama2")) unaPieza = piezasVivas[17];
        }
        return unaPieza;
    }

    public void hacerEnroqueCortoNegro() {
        chessBoard.getComponent(7).setBackground(Color.BLACK);
        chessBoard.getComponent(7).setBackground(ColorOscuro);
        JPanel pan = (JPanel) chessBoard.getComponent(5);
        pan.add(dameEtiqueta("NegroTorre2"));
        piezasVivas[7].setPosicion("f8");
        yaEnroque = true;
        tipodeEnroque = 1;
    }

    public void hacerEnroqueLargoNegro() {
        chessBoard.getComponent(0).setBackground(Color.BLACK);
        chessBoard.getComponent(0).setBackground(ColorClaro);
        JPanel pan = (JPanel) chessBoard.getComponent(3);
        lblTorre2 = dameEtiquetaTorre2();
        pan.add(lblTorre2);
        piezasVivas[0].setPosicion("d8");
        yaEnroque = true;
        tipodeEnroque = 2;
    }

    public void hacerEnroqueCortoBlanco() {
        chessBoard.getComponent(63).setBackground(Color.BLACK);
        chessBoard.getComponent(63).setBackground(ColorClaro);
        JPanel pan = (JPanel) chessBoard.getComponent(61);
        pan.add(dameEtiqueta("BlancoTorre2"));
        this.damePieza("BlancoTorre2").setPosicion("f1");
        yaEnroque = true;
        tipodeEnroque = 4;
    }

    public void hacerEnroqueLargoBlanco() {
        chessBoard.getComponent(56).setBackground(Color.BLACK);
        chessBoard.getComponent(56).setBackground(ColorOscuro);
        JPanel pan = (JPanel) chessBoard.getComponent(59);
        pan.add(dameEtiqueta("BlancoTorre1"));
        this.damePieza("BlancoTorre1").setPosicion("d1");
        yaEnroque = true;
        tipodeEnroque = 3;
    }

    public int ProbarEnroque(Pieza unaPieza) {
        int tipodeEnroque = 0;
        if (unaPieza instanceof Rey) {
            if (unaPieza.getPosicion().equals("g8") == true && yamoviTorreEnroqueCorto(unaPieza) == false) {
                hacerEnroqueCortoNegro();
                tipodeEnroque = 3;
            }
            if (unaPieza.getPosicion().equals("c8") == true && yamoviTorreEnroqueLargo(unaPieza) == false) {
                hacerEnroqueLargoNegro();
                tipodeEnroque = 4;
            }
            if (unaPieza.getPosicion().equals("c1") == true && yamoviTorreEnroqueLargoBlanco(unaPieza) == false) {
                hacerEnroqueLargoBlanco();
                tipodeEnroque = 2;
            }
            if (unaPieza.getPosicion().equals("g1") == true && yamoviTorreEnroqueCortoBlanco(unaPieza) == false) {
                hacerEnroqueCortoBlanco();
                tipodeEnroque = 1;
            }
        }
        return tipodeEnroque;
    }

    public boolean enroqueEfectuado() {
        return yaEnroque;
    }

    public boolean yamoviTorreEnroqueCorto(Pieza unaPieza) {
        if (unaPieza.getNombre().equals("NegroTorre2") && piezasVivas[7].getPosicion().equals("h8") == false) {
            yamoviTorreEnroqueCorto = true;
        }
        return yamoviTorreEnroqueCorto;
    }

    public boolean yamoviTorreEnroqueLargo(Pieza unaPieza) {
        if (unaPieza.getNombre().equals("NegroTorre1") && piezasVivas[0].getPosicion().equals("a8") == false) {
            System.out.println("******yamoviTorreEnroqueLargo**********");
            yamoviTorreEnroqueLargo = true;
        }
        return yamoviTorreEnroqueLargo;
    }

    public boolean yamoviTorreEnroqueCortoBlanco(Pieza unaPieza) {
        if (unaPieza.getNombre().equals("BlancoTorre2") && piezasVivas[24].getPosicion().equals("h1") == false) {
            yamoviTorreEnroqueCortoBlanco = true;
        }
        return yamoviTorreEnroqueCortoBlanco;
    }

    public boolean yamoviTorreEnroqueLargoBlanco(Pieza unaPieza) {
        if (unaPieza.getNombre().equals("BlancoTorre1") && piezasVivas[17].getPosicion().equals("a1") == false) {
            yamoviTorreEnroqueLargoBlanco = true;
        }
        return yamoviTorreEnroqueLargoBlanco;
    }

    /***************************************************************/
    public boolean yamoviRey(Pieza unaPieza) {
        if (unaPieza.getNombre().equals("NegroRey") && (piezasVivas[4].getPosicion().equals("d8") == true || piezasVivas[4].getPosicion().equals("f8") == true || piezasVivas[4].getPosicion().equals("d7") == true || piezasVivas[4].getPosicion().equals("e7") == true || piezasVivas[4].getPosicion().equals("f7") == true)) {
            yamoviRey = true;
        }
        return yamoviRey;
    }

    public boolean HayPiezaenCasilla(String casilla) {
        for (int n = 0; n < piezasVivas.length; n++) {
            if (piezasVivas[n].getPosicion().equals(casilla) && piezasVivas[n].getVivo() == true) return true;
        }
        return false;
    }

    public boolean HayPiezaBlancaenCasilla(String casilla) {
        for (int n = 0; n < piezasVivas.length; n++) {
            if (piezasVivas[n].getPosicion().equals(casilla) && piezasVivas[n].getVivo() == true && piezasVivas[n].getNombre().charAt(0) == 'B') return true;
        }
        return false;
    }

    public boolean HayPeonenCasilla(String casilla, char colorPieza) {
        for (int n = 0; n < piezasVivas.length; n++) {
            if (piezasVivas[n].getPosicion().equals(casilla) && piezasVivas[n].getVivo() == true && piezasVivas[n] instanceof Peon && piezasVivas[n].getColorPiezas() == colorPieza) return true;
        }
        return false;
    }

    public String damePiezadeCasilla(String casilla) {
        for (int n = 0; n < piezasVivas.length; n++) {
            if (piezasVivas[n].getPosicion().equals(casilla) && piezasVivas[n].getVivo() == true) return piezasVivas[n].getNombre();
        }
        return "XX";
    }

    public ArrayList<String> dameListaReporte() {
        ArrayList<String> datosdePartida = new ArrayList<String>();
        String pieza = "", elegido = "XX";
        for (int n = 0; n < 64; n++) {
            pieza = damePiezadeCasilla(mdt.dameCoordenadasSegunPosicion(n));
            if (pieza.equals("XX")) elegido = "XX"; else {
                if (pieza.equals("NegroTorre1") || pieza.equals("NegroTorre2")) elegido = "BR"; else if (pieza.equals("NegroCaballo1") || pieza.equals("NegroCaballo2")) elegido = "BN"; else if (pieza.equals("NegroAlfil1") || pieza.equals("NegroAlfil2")) elegido = "BB"; else if (pieza.equals("NegroDama") || pieza.equals("NegroDama2")) elegido = "BQ"; else if (pieza.equals("NegroRey")) elegido = "BK"; else if (pieza.equals("NegroPeon1")) elegido = "BP"; else if (pieza.equals("NegroPeon2")) elegido = "BP"; else if (pieza.equals("NegroPeon3")) elegido = "BP"; else if (pieza.equals("NegroPeon4")) elegido = "BP"; else if (pieza.equals("NegroPeon5")) elegido = "BP"; else if (pieza.equals("NegroPeon6")) elegido = "BP"; else if (pieza.equals("NegroPeon7")) elegido = "BP"; else if (pieza.equals("NegroPeon8")) elegido = "BP"; else if (pieza.equals("BlancoTorre1") || pieza.equals("BlancoTorre2")) elegido = "WR"; else if (pieza.equals("BlancoCaballo1") || pieza.equals("BlancoCaballo2")) elegido = "WN"; else if (pieza.equals("BlancoAlfil1") || pieza.equals("BlancoAlfil2")) elegido = "WB"; else if (pieza.equals("BlancoDama") || pieza.equals("BlancoDama2")) elegido = "WQ"; else if (pieza.equals("BlancoRey")) elegido = "WK"; else if (pieza.equals("BlancoPeon1")) elegido = "WP"; else if (pieza.equals("BlancoPeon2")) elegido = "WP"; else if (pieza.equals("BlancoPeon3")) elegido = "WP"; else if (pieza.equals("BlancoPeon4")) elegido = "WP"; else if (pieza.equals("BlancoPeon5")) elegido = "WP"; else if (pieza.equals("BlancoPeon6")) elegido = "WP"; else if (pieza.equals("BlancoPeon7")) elegido = "WP"; else if (pieza.equals("BlancoPeon8")) elegido = "WP";
            }
            datosdePartida.add(elegido);
        }
        return datosdePartida;
    }

    public char getColorPieza(String casilla) {
        for (int n = 0; n < piezasVivas.length; n++) {
            if (piezasVivas[n].getPosicion().equals(casilla) && piezasVivas[n].getVivo() == true) return piezasVivas[n].getColorPiezas();
        }
        return '0';
    }

    public void ImprimeColorPiezas() {
        for (int n = 0; n < piezasVivas.length; n++) {
        }
    }

    private void initComponents() {
        getContentPane().setLayout(null);
        pack();
    }
}
