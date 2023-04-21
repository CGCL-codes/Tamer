package ajedrezLogica;

import algoritmoAjedrez.manejadorEngine;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import reporteTablero.*;

/**
 *
 * @author Billy
 */
public class EventoJuego implements MouseListener, MouseMotionListener {

    JLabel PiezaLabel;

    JPanel chessBoard;

    public Pieza unaPieza, otraPieza;

    Point UbicacionPadre;

    int xAdjustment, yAdjustment, antx, anty;

    Integer desde, hasta;

    Color ColorSeleccion = Color.RED, coloranterior;

    java.util.List<Integer> pospintados;

    java.util.List<Color> colpintados;

    ManejadordeTablero adminTablero;

    manejadorPiezas adminPiezas;

    JLayeredPane layeredPane;

    Dimension boardSize;

    Pieza[] piezasVivas;

    java.util.List<PiezaPosicion> listadeMovimientos;

    java.util.ArrayList<RegistroJugadas> listadeNotaciones;

    String coordAnterior;

    int columna, fila;

    JPanel panExtra;

    JLabel etiqExtra;

    JTable jtTablaJugadas, jtTablaComentarios;

    JTextPane jtpComentarios, jtpComentar, jtpPGN;

    JButton btnAgregarComentario;

    boolean exito = false;

    boolean hecomidoPieza = false;

    boolean mecomiPiezaBlanca = false;

    manejadorEngine admMaquina;

    int profundidad;

    int contadordeJugadas = 0, contadorComentarios = 0;

    ArrayList<PeonPasado> lstcomerPeonPasadoBlancas, lstcomerPeonPasadoNegras;

    modelodeTabla modelo;

    /** Creates a new instance of Evento */
    public EventoJuego(JPanel cBoard, JLayeredPane layeredPane, Dimension boardSize, JTable jtTablaJugadas, JTextPane jtpComentar, JTable jtTablaComentarios, JButton btnAgregarComentario, JTextPane jtpComentarios, JTextPane jtpPGN) {
        this.chessBoard = cBoard;
        this.jtTablaJugadas = jtTablaJugadas;
        this.layeredPane = layeredPane;
        this.boardSize = boardSize;
        this.jtTablaComentarios = jtTablaComentarios;
        this.jtpComentar = jtpComentar;
        this.jtpComentarios = jtpComentarios;
        this.jtpPGN = jtpPGN;
        this.btnAgregarComentario = btnAgregarComentario;
        empezardeCero();
        piezasVivas = adminPiezas.dametodasPiezas();
        listadeMovimientos = new ArrayList<PiezaPosicion>();
        listadeNotaciones = new ArrayList<RegistroJugadas>();
        modelo = new modelodeTabla();
        this.jtTablaComentarios.setModel(modelo);
        modelo.addColumn("N");
        modelo.addColumn("Comentarios");
        jtTablaComentarios.getColumnModel().getColumn(0).setPreferredWidth(15);
        admMaquina = new manejadorEngine();
        profundidad = 5;
    }

    public void empezardeCero() {
        adminTablero = new ManejadordeTablero(chessBoard);
        adminTablero.CrearTablero();
        adminPiezas = new manejadorPiezas(chessBoard, adminTablero, this);
        adminPiezas.IniciarPiezas();
    }

    public int dameContadorJugadas() {
        return contadordeJugadas;
    }

    public void mouseDragged(MouseEvent me) {
        if (PiezaLabel == null) return;
        PiezaLabel.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
    }

    public void mouseReleased(MouseEvent e) {
        Component c = null;
        if (PiezaLabel == null) return;
        PiezaLabel.setVisible(false);
        if (e.getX() > 0 && e.getX() <= boardSize.width && e.getY() > 0 && e.getY() <= boardSize.height) {
            c = chessBoard.findComponentAt(e.getX(), e.getY());
        } else {
            c = chessBoard.findComponentAt(UbicacionPadre.x, UbicacionPadre.y);
        }
        if (unaPieza.getColorPiezas() == 'N' && contadordeJugadas % 2 == 0) {
            System.out.println("Juegan negras");
            c = chessBoard.findComponentAt(UbicacionPadre.x, UbicacionPadre.y);
            JPanel pnl = (JPanel) c;
            pnl.add(PiezaLabel);
        }
        if (unaPieza.getColorPiezas() == 'B' && contadordeJugadas % 2 == 1) {
            System.out.println("Juegan blancas");
            c = chessBoard.findComponentAt(UbicacionPadre.x, UbicacionPadre.y);
            JPanel pnl = (JPanel) c;
            pnl.add(PiezaLabel);
        }
        if (c instanceof JLabel) {
            if (movimientoCorrecto(getComponentPosition(c)) == true) {
                Container parent = c.getParent();
                parent.remove(0);
                parent.add(PiezaLabel);
                JLabel lbl = (JLabel) c;
                Pieza laPieza = adminPiezas.damePieza(lbl.getName());
                laPieza.dameIcono().setVisible(false);
                laPieza.setVivo(false);
                listadeMovimientos.add(new PiezaPosicion(unaPieza, coordAnterior, laPieza));
                contadordeJugadas++;
                exito = true;
                hecomidoPieza = true;
            } else {
                c = chessBoard.findComponentAt(UbicacionPadre.x, UbicacionPadre.y);
                JPanel pnl = (JPanel) c;
                pnl.add(PiezaLabel);
            }
        } else {
            if (movimientoCorrecto(getComponentPositionPanel(c)) == true) {
                Container parent = (Container) c;
                parent.add(PiezaLabel);
                listadeMovimientos.add(new PiezaPosicion(unaPieza, coordAnterior, null));
                contadordeJugadas++;
                exito = true;
            } else {
                c = chessBoard.findComponentAt(UbicacionPadre.x, UbicacionPadre.y);
                JPanel pnl = (JPanel) c;
                pnl.add(PiezaLabel);
            }
        }
        String strHasta, strDesde;
        PiezaLabel.setVisible(true);
        adminTablero.PintarCasillasalSeleccionar(antx, anty, coloranterior);
        hasta = getComponentPosition(PiezaLabel);
        strHasta = adminTablero.dameCoordenadasSegunPosicion(hasta);
        strDesde = adminTablero.dameCoordenadasSegunPosicion(desde);
        if (exito == true) {
            completarJugada(strDesde, strHasta, unaPieza, false);
            exito = true;
            this.admMaquina.moverEtiqueta(desde, hasta);
            System.out.println("YO. desde:" + desde + " hasta:" + hasta);
            this.admMaquina.ponNegroAMover();
            this.admMaquina.hacerMovimientoMaquina();
            desde = this.admMaquina.dameJugadaDesde();
            hasta = this.admMaquina.dameJugadaHasta();
            strHasta = adminTablero.dameCoordenadasSegunPosicion(hasta);
            strDesde = adminTablero.dameCoordenadasSegunPosicion(desde);
            boolean tomePieza = false;
            if (adminPiezas.HayPiezaenCasilla(strHasta)) tomePieza = true;
            Pieza unaPieza = this.hacerMovimientoMaquina(strDesde, strHasta);
            this.completarJugada(strDesde, strHasta, unaPieza, tomePieza);
            exito = false;
            contadordeJugadas++;
        }
    }

    public void completarJugada(String strDesde, String strHasta, Pieza unaPieza, boolean comido) {
        Pieza otraPieza;
        otraPieza = this.calculoMovimientosOtraPieza(unaPieza);
        unaPieza.setPosicion(strHasta);
        boolean resultadoPromocion;
        resultadoPromocion = promocionarPeon();
        if (unaPieza instanceof Peon) {
            this.hecomidoPieza = comerPorPeonPasado(this.hecomidoPieza);
            marcarPeonPasadoparaNegras(unaPieza);
            marcarPeonPasadoparaBlancas(unaPieza);
        }
        int tipoEnroque = adminPiezas.ProbarEnroque(unaPieza);
        String Notacion = NotacionPrecisada(unaPieza, otraPieza, strDesde, strHasta);
        switch(tipoEnroque) {
            case 1:
            case 3:
                Notacion = "0-0";
                break;
            case 2:
            case 4:
                Notacion = "0-0-0";
                break;
        }
        columna = 2;
        fila = (listadeMovimientos.size() + 1) / 2;
        if (unaPieza.getColorPiezas() == 'B') columna = 1;
        if (fila > 0 && exito == true) {
            if (hecomidoPieza == true || comido == true) {
                if (unaPieza instanceof Peon) Notacion = coordAnterior.charAt(0) + "x" + Notacion; else Notacion = Notacion.charAt(0) + "x" + Notacion.substring(1);
                this.hecomidoPieza = false;
            }
            if (resultadoPromocion) {
                Notacion += "=D";
            }
            if (columna == 1) {
                DefaultTableModel dtm = (DefaultTableModel) jtTablaJugadas.getModel();
                Object[] filaNotacion = new Object[3];
                filaNotacion[0] = fila;
                filaNotacion[1] = Notacion;
                filaNotacion[2] = "";
                dtm.addRow(filaNotacion);
                jtTablaJugadas.setModel(dtm);
            } else {
                jtTablaJugadas.setValueAt(fila, fila - 1, 0);
                jtTablaJugadas.setValueAt(Notacion, fila - 1, columna);
            }
            listadeNotaciones.add(new RegistroJugadas(Notacion, unaPieza, fila - 1, columna, ""));
            InsertarunComentario("");
        }
    }

    public Pieza hacerMovimientoMaquina(String casilladesde, String casillahasta) {
        System.out.println("***************************************");
        coordAnterior = casilladesde;
        System.out.println("casilla desde: " + casilladesde);
        System.out.println("casilla hasta: " + casillahasta);
        String nombrePieza = adminPiezas.damePiezadeCasilla(casilladesde);
        System.out.println("nombrePieza: " + nombrePieza);
        JLabel nombreEtiquetaPieza = adminPiezas.dameEtiqueta(nombrePieza);
        nombreEtiquetaPieza.setVisible(false);
        if (adminPiezas.HayPiezaBlancaenCasilla(casillahasta) == true) {
            System.out.println("Me estoy comiendo una pieza blanca: ");
            String nombrePiezaComida = adminPiezas.damePiezadeCasilla(casillahasta);
            JLabel nombreEtiquetaPiezaComida = adminPiezas.dameEtiqueta(nombrePiezaComida);
            nombreEtiquetaPiezaComida.setVisible(false);
            adminPiezas.IdentificarPiezaElegida(nombreEtiquetaPiezaComida).setVivo(false);
        }
        int nuevapos = adminTablero.damePosicionSegunCoordenadas(casillahasta);
        panExtra = (JPanel) chessBoard.getComponent(nuevapos);
        unaPieza = adminPiezas.IdentificarPiezaElegida(nombreEtiquetaPieza);
        unaPieza.setPosicion(casillahasta);
        panExtra.add(nombreEtiquetaPieza);
        nombreEtiquetaPieza.setVisible(true);
        listadeMovimientos.add(new PiezaPosicion(unaPieza, casilladesde, null));
        return unaPieza;
    }

    public int dameNumeroJugadas() {
        return listadeMovimientos.size();
    }

    public void ActualizarComentarios(int row, int tipoCaso) {
        if (tipoCaso == 0) {
            listadeNotaciones.get(row).setComentario(this.jtpComentarios.getText());
            modelo.setValueAt(this.jtpComentarios.getText(), row, 1);
            this.jtTablaComentarios.setModel(modelo);
            imprimirComentarios();
        } else if (tipoCaso == 1) {
            this.jtpComentarios.setText(listadeNotaciones.get(row).dameComentario());
        }
    }

    public void InsertarunComentario(String comentario) {
        String indice;
        if (unaPieza.getColorPiezas() == 'N') indice = "..." + fila; else indice = "" + fila;
        Object[] fil = new Object[2];
        fil[0] = (Object) indice;
        fil[1] = (Object) comentario;
        modelo.addRow(fil);
        this.jtTablaComentarios.setModel(modelo);
    }

    public void BotonAgregarComentario() {
        if (jtpComentar.getText().length() != 0) {
            modelo.removeRow(modelo.getRowCount() - 1);
            InsertarunComentario(jtpComentar.getText());
            contadorComentarios++;
            listadeNotaciones.get(this.contadordeJugadas - 1).setComentario(jtpComentar.getText());
            jtpComentar.setText("");
            this.btnAgregarComentario.setEnabled(false);
        }
    }

    public void imprimirComentarios() {
        for (int i = 0; i < listadeNotaciones.size(); i++) {
        }
    }

    public void BotonPGN() {
        StringBuffer archivoPGN = new StringBuffer("");
        int contador = 1;
        String jugadaBlanca, jugadaNegra;
        String comentarioBlanco, comentarioNegro;
        for (int i = 0; i < listadeNotaciones.size(); i += 2) {
            jugadaBlanca = listadeNotaciones.get(i).dameNotacionPNG();
            archivoPGN.append(contador).append(". ").append(jugadaBlanca);
            comentarioBlanco = listadeNotaciones.get(i).dameComentario();
            if (comentarioBlanco.equals("") == false) {
                archivoPGN.append(" [").append(comentarioBlanco).append("]");
            }
            if (i + 1 < listadeNotaciones.size()) {
                jugadaNegra = listadeNotaciones.get(i + 1).dameNotacionPNG();
                archivoPGN.append(" ").append(jugadaNegra).append(" ");
                comentarioNegro = listadeNotaciones.get(i + 1).dameComentario();
                if (comentarioNegro.equals("") == false) {
                    archivoPGN.append("[").append(comentarioNegro).append("] ");
                }
            }
            contador++;
        }
        this.jtpPGN.setText(archivoPGN.toString());
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        this.btnAgregarComentario.setEnabled(true);
        PiezaLabel = null;
        antx = e.getX();
        anty = e.getY();
        Component c = chessBoard.findComponentAt(antx, anty);
        if (c instanceof JPanel) return;
        UbicacionPadre = c.getParent().getLocation();
        desde = getComponentPosition(c);
        xAdjustment = UbicacionPadre.x - e.getX();
        yAdjustment = UbicacionPadre.y - e.getY();
        PiezaLabel = (JLabel) c;
        coloranterior = c.getParent().getBackground();
        c.getParent().setBackground(ColorSeleccion);
        PiezaLabel.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        PiezaLabel.setSize(PiezaLabel.getWidth(), PiezaLabel.getHeight());
        layeredPane.add(PiezaLabel, JLayeredPane.DRAG_LAYER);
        unaPieza = adminPiezas.IdentificarPiezaElegida(PiezaLabel);
        coordAnterior = unaPieza.getPosicion();
        adminTablero.CalcularCasillasalSeleccionar(unaPieza, adminPiezas);
        pospintados = adminTablero.damePosPintados();
        colpintados = adminTablero.dameColPintados();
    }

    private Integer getComponentPosition(Component c) {
        Point UbicacionPadre = c.getParent().getLocation();
        int column = UbicacionPadre.x / c.getParent().getWidth();
        int row = UbicacionPadre.y / c.getParent().getHeight();
        int pos = row * 8 + column;
        return new Integer(pos);
    }

    private Integer getComponentPositionPanel(Component c) {
        Point UbicacionPadre = c.getLocation();
        int column = UbicacionPadre.x / c.getWidth();
        int row = UbicacionPadre.y / c.getHeight();
        int pos = row * 8 + column;
        return new Integer(pos);
    }

    public boolean movimientoCorrecto(int posicion) {
        for (int i = 0; i < pospintados.size(); i++) if (pospintados.get(i) == posicion) return true;
        return false;
    }

    public boolean promocionarPeon() {
        boolean exitoPromocion = false;
        if (unaPieza instanceof Peon && unaPieza.getPosicion().charAt(1) == '1') {
            exitoPromocion = true;
            unaPieza.setPromocion(true);
            unaPieza.dameIcono().setVisible(false);
            unaPieza.setVivo(false);
            int pos = adminTablero.damePosicionSegunCoordenadas(unaPieza.getPosicion());
            panExtra = (JPanel) chessBoard.getComponent(pos);
            adminPiezas.damePieza("NegroDama2").setPosicion(unaPieza.getPosicion());
            etiqExtra = adminPiezas.dameEtiqueta("NegroDama2");
            panExtra.add(etiqExtra);
            etiqExtra.setVisible(true);
        }
        if (unaPieza instanceof Peon && unaPieza.getPosicion().charAt(1) == '8') {
            exitoPromocion = true;
            unaPieza.setPromocion(true);
            unaPieza.dameIcono().setVisible(false);
            unaPieza.setVivo(false);
            int pos = adminTablero.damePosicionSegunCoordenadas(unaPieza.getPosicion());
            panExtra = (JPanel) chessBoard.getComponent(pos);
            adminPiezas.damePieza("BlancoDama2").setPosicion(unaPieza.getPosicion());
            etiqExtra = adminPiezas.dameEtiqueta("BlancoDama2");
            panExtra.add(etiqExtra);
            etiqExtra.setVisible(true);
        }
        return exitoPromocion;
    }

    private void undoEnroqueCortoNegro() {
        int pAct = adminTablero.damePosicionSegunCoordenadas("f8");
        adminTablero.RepintarPosiciones(pAct);
        int pAnt = adminTablero.damePosicionSegunCoordenadas("h8");
        JPanel pan2 = (JPanel) chessBoard.getComponent(pAnt);
        adminTablero.RepintarPosiciones(pAnt);
        adminPiezas.damePieza("NegroTorre2").setPosicion("h8");
        JLabel etiq2 = adminPiezas.dameEtiqueta("NegroTorre2");
        pan2.add(etiq2);
        etiq2.setVisible(true);
    }

    private void undoEnroqueCortoBlanco() {
        int pAct = adminTablero.damePosicionSegunCoordenadas("f1");
        adminTablero.RepintarPosiciones(pAct);
        int pAnt = adminTablero.damePosicionSegunCoordenadas("h1");
        JPanel pan2 = (JPanel) chessBoard.getComponent(pAnt);
        adminTablero.RepintarPosiciones(pAnt);
        adminPiezas.damePieza("BlancoTorre2").setPosicion("h1");
        JLabel etiq2 = adminPiezas.dameEtiqueta("BlancoTorre2");
        pan2.add(etiq2);
        etiq2.setVisible(true);
    }

    private void undoEnroqueLargoNegro() {
        int pAct = adminTablero.damePosicionSegunCoordenadas("d8");
        adminTablero.RepintarPosiciones(pAct);
        int pAnt = adminTablero.damePosicionSegunCoordenadas("a8");
        JPanel pan2 = (JPanel) chessBoard.getComponent(pAnt);
        adminTablero.RepintarPosiciones(pAnt);
        adminPiezas.damePieza("NegroTorre1").setPosicion("a8");
        JLabel etiq2 = adminPiezas.dameEtiqueta("NegroTorre1");
        pan2.add(etiq2);
        etiq2.setVisible(true);
    }

    private void undoEnroqueLargoBlanco() {
        int pAct = adminTablero.damePosicionSegunCoordenadas("d1");
        adminTablero.RepintarPosiciones(pAct);
        int pAnt = adminTablero.damePosicionSegunCoordenadas("a1");
        JPanel pan2 = (JPanel) chessBoard.getComponent(pAnt);
        adminTablero.RepintarPosiciones(pAnt);
        adminPiezas.damePieza("BlancoTorre1").setPosicion("a1");
        JLabel etiq2 = adminPiezas.dameEtiqueta("BlancoTorre1");
        pan2.add(etiq2);
        etiq2.setVisible(true);
    }

    public void RetrocederJugada(PiezaPosicion jugada) {
        int posActual;
        String coordActual = jugada.damePieza().getPosicion();
        String coordAnterior = jugada.dameCoordenadaAnterior();
        if (jugada.damePieza().getNombre().equals("NegroRey") && coordActual.equals("g8") && coordAnterior.equals("e8")) undoEnroqueCortoNegro();
        if (jugada.damePieza().getNombre().equals("NegroRey") && coordActual.equals("c8") && coordAnterior.equals("e8")) undoEnroqueLargoNegro();
        if (jugada.damePieza().getNombre().equals("BlancoRey") && coordActual.equals("g1") && coordAnterior.equals("e1")) undoEnroqueCortoBlanco();
        if (jugada.damePieza().getNombre().equals("BlancoRey") && coordActual.equals("c1") && coordAnterior.equals("e1")) undoEnroqueLargoBlanco();
        if (jugada.damePieza().getPromocion() == true && jugada.damePieza().getNombre().charAt(0) == 'N') {
            panExtra.remove(etiqExtra);
            adminPiezas.damePieza("NegroDama2").setVivo(false);
            jugada.damePieza().setVivo(true);
            posActual = adminTablero.damePosicionSegunCoordenadas(coordActual);
            adminTablero.RepintarPosiciones(posActual);
            jugada.damePieza().setPromocion(false);
        }
        if (jugada.damePieza().getPromocion() == true && jugada.damePieza().getNombre().charAt(0) == 'B') {
            panExtra.remove(etiqExtra);
            adminPiezas.damePieza("BlancoDama2").setVivo(false);
            jugada.damePieza().setVivo(true);
            posActual = adminTablero.damePosicionSegunCoordenadas(coordActual);
            adminTablero.RepintarPosiciones(posActual);
            jugada.damePieza().setPromocion(false);
        }
        posActual = adminTablero.damePosicionSegunCoordenadas(coordActual);
        adminTablero.RepintarPosiciones(posActual);
        if (jugada.hayPiezaComida() && jugada.dameEstadoPeonPasado() == false) {
            JPanel pan = (JPanel) chessBoard.getComponent(posActual);
            JLabel etiq = adminPiezas.dameEtiqueta(jugada.damePiezaComida().getNombre());
            pan.add(etiq);
            etiq.setVisible(true);
        }
        if (jugada.hayPiezaComida() && jugada.dameEstadoPeonPasado() == true) {
            posActual = adminTablero.damePosicionSegunCoordenadas(listadeMovimientos.get(this.contadordeJugadas - 2).damePieza().getPosicion());
            JPanel pan = (JPanel) chessBoard.getComponent(posActual);
            JLabel etiq = adminPiezas.dameEtiqueta(jugada.damePiezaComida().getNombre());
            pan.add(etiq);
            etiq.setVisible(true);
        }
        int posAnterior = adminTablero.damePosicionSegunCoordenadas(coordAnterior);
        JPanel pan = (JPanel) chessBoard.getComponent(posAnterior);
        adminTablero.RepintarPosiciones(posAnterior);
        jugada.damePieza().setPosicion(jugada.dameCoordenadaAnterior());
        JLabel etiq = jugada.damePieza().dameIcono();
        pan.add(etiq);
        etiq.setVisible(true);
        if (this.contadordeJugadas >= 2) {
            this.marcarPeonPasadoparaBlancas(this.listadeMovimientos.get(this.contadordeJugadas - 2).damePieza());
            this.marcarPeonPasadoparaNegras(this.listadeMovimientos.get(this.contadordeJugadas - 2).damePieza());
        }
    }

    public void BotonVolver() {
        if (listadeMovimientos.size() > 0) {
            int ultimomov = listadeMovimientos.size() - 1;
            PiezaPosicion pza = (PiezaPosicion) listadeMovimientos.get(ultimomov);
            RegistroJugadas ultimaNotacion = (RegistroJugadas) listadeNotaciones.get(ultimomov);
            if (pza.hayPiezaComida() == true) pza.damePiezaComida().setVivo(true);
            RetrocederJugada(pza);
            RetrocederNotacion(ultimaNotacion);
            --contadordeJugadas;
            listadeMovimientos.remove(pza);
            listadeNotaciones.remove(ultimaNotacion);
            modelo.removeRow(modelo.getRowCount() - 1);
        }
    }

    public void BotonReporte() {
        ArrayList<jugadaXML> arrJugadasXML = dameListaJugadasXML();
        hacerFO impreso = new hacerFO(adminPiezas.dameListaReporte(), arrJugadasXML, this.dameDatosPartida());
        fo2Pdf.crearPdf();
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("rundll32 url.dll,FileProtocolHandler reporteAjedrez.pdf");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void imprimirNotaciones() {
        for (int i = 0; i < listadeNotaciones.size(); i++) System.out.println(listadeNotaciones.get(i).dameNotacion());
    }

    void RetrocederNotacion(RegistroJugadas ultimaNotacion) {
        if (ultimaNotacion.unaPieza.getColorPiezas() == 'B') {
            DefaultTableModel dtm = (DefaultTableModel) jtTablaJugadas.getModel();
            int contadorFilas = dtm.getRowCount();
            dtm.removeRow(contadorFilas - 1);
            jtTablaJugadas.setModel(dtm);
        } else {
            jtTablaJugadas.setValueAt("", ultimaNotacion.dameFila(), 2);
        }
    }

    public boolean comerPorPeonPasado(boolean haComido) {
        if (unaPieza instanceof Peon) switch(unaPieza.getColorPiezas()) {
            case 'B':
                if (EnviarMarcacionesdePeonesBlancas() != null) {
                    for (int i = 0; i < lstcomerPeonPasadoBlancas.size(); i++) if (unaPieza.getPosicion().equals(this.lstcomerPeonPasadoBlancas.get(i).puntodeToma)) {
                        Pieza piezaAcomer = listadeMovimientos.get(contadordeJugadas - 2).damePieza();
                        piezaAcomer.dameIcono().setVisible(false);
                        piezaAcomer.setVivo(false);
                        listadeMovimientos.get(contadordeJugadas - 1).setPiezaComida(piezaAcomer);
                        listadeMovimientos.get(contadordeJugadas - 1).setEstadoPeonPasado(true);
                        return true;
                    }
                }
                break;
            case 'N':
                if (EnviarMarcacionesdePeonesNegras() != null) {
                    for (int i = 0; i < lstcomerPeonPasadoNegras.size(); i++) if (unaPieza.getPosicion().equals(this.lstcomerPeonPasadoNegras.get(i).puntodeToma)) {
                        Pieza piezaAcomer = listadeMovimientos.get(contadordeJugadas - 2).damePieza();
                        piezaAcomer.dameIcono().setVisible(false);
                        piezaAcomer.setVivo(false);
                        listadeMovimientos.get(contadordeJugadas - 1).setPiezaComida(piezaAcomer);
                        listadeMovimientos.get(contadordeJugadas - 1).setEstadoPeonPasado(true);
                        return true;
                    }
                }
        }
        return haComido;
    }

    public ArrayList<PeonPasado> EnviarMarcacionesdePeonesBlancas() {
        return this.lstcomerPeonPasadoBlancas;
    }

    public ArrayList<PeonPasado> EnviarMarcacionesdePeonesNegras() {
        return lstcomerPeonPasadoNegras;
    }

    public void marcarPeonPasadoparaBlancas(Pieza unaPieza) {
        char vertical, horizontal;
        if (exito == true) lstcomerPeonPasadoBlancas = new ArrayList<PeonPasado>();
        if (unaPieza instanceof Peon && unaPieza.getColorPiezas() == 'N') {
            horizontal = unaPieza.getPosicion().charAt(1);
            switch(horizontal) {
                case '5':
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical >= 'b') {
                        vertical--;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'B')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '6';
                            PeonPasado pp = new PeonPasado("" + vertical + horizontal, pos);
                            lstcomerPeonPasadoBlancas.add(pp);
                        }
                    }
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical <= 'g') {
                        vertical++;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'B')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '6';
                            PeonPasado pp = new PeonPasado(unaPieza.aString(vertical, horizontal), pos);
                            lstcomerPeonPasadoBlancas.add(pp);
                        }
                    }
                case '6':
                    horizontal = '6';
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical >= 'b') {
                        vertical--;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'B')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '7';
                            PeonPasado pp = new PeonPasado(unaPieza.aString(vertical, horizontal), pos);
                            lstcomerPeonPasadoBlancas.add(pp);
                        }
                    }
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical <= 'g') {
                        vertical++;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'B')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '7';
                            PeonPasado pp = new PeonPasado(unaPieza.aString(vertical, horizontal), unaPieza.getPosicion());
                            lstcomerPeonPasadoBlancas.add(pp);
                        }
                    }
            }
        }
    }

    public void marcarPeonPasadoparaNegras(Pieza unaPieza) {
        char vertical, horizontal;
        if (exito == true) lstcomerPeonPasadoNegras = new ArrayList<PeonPasado>();
        if (unaPieza instanceof Peon && unaPieza.getColorPiezas() == 'B') {
            horizontal = unaPieza.getPosicion().charAt(1);
            switch(horizontal) {
                case '4':
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical >= 'b') {
                        vertical--;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'N')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '3';
                            PeonPasado pp = new PeonPasado("" + vertical + horizontal, pos);
                            lstcomerPeonPasadoNegras.add(pp);
                        }
                    }
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical <= 'g') {
                        vertical++;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'N')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '3';
                            PeonPasado pp = new PeonPasado(unaPieza.aString(vertical, horizontal), pos);
                            lstcomerPeonPasadoNegras.add(pp);
                        }
                    }
                case '3':
                    horizontal = '3';
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical >= 'b') {
                        vertical--;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'N')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '2';
                            PeonPasado pp = new PeonPasado("" + vertical + horizontal, pos);
                            lstcomerPeonPasadoNegras.add(pp);
                        }
                    }
                    vertical = unaPieza.getPosicion().charAt(0);
                    if (vertical <= 'g') {
                        vertical++;
                        if (adminPiezas.HayPeonenCasilla(unaPieza.aString(vertical, horizontal), 'N')) {
                            String pos = "" + unaPieza.getPosicion().charAt(0) + '2';
                            PeonPasado pp = new PeonPasado("" + vertical + horizontal, pos);
                            lstcomerPeonPasadoNegras.add(pp);
                        }
                    }
                    break;
            }
        }
    }

    Pieza calculoMovimientosOtraPieza(Pieza unaPieza) {
        Pieza otraPieza = null;
        if (unaPieza.getNombre().equals("BlancoCaballo1") == true) {
            otraPieza = adminPiezas.damePieza("BlancoCaballo2");
            otraPieza.CalcularMovimientos(adminPiezas);
        } else if (unaPieza.getNombre().equals("BlancoCaballo2") == true) {
            otraPieza = adminPiezas.damePieza("BlancoCaballo1");
            otraPieza.CalcularMovimientos(adminPiezas);
        } else if (unaPieza.getNombre().equals("NegroCaballo1") == true) {
            otraPieza = adminPiezas.damePieza("NegroCaballo2");
            otraPieza.CalcularMovimientos(adminPiezas);
        } else if (unaPieza.getNombre().equals("NegroCaballo2") == true) {
            otraPieza = adminPiezas.damePieza("NegroCaballo1");
            otraPieza.CalcularMovimientos(adminPiezas);
        } else if (unaPieza.getNombre().equals("BlancoTorre1") == true) {
            otraPieza = adminPiezas.damePieza("BlancoTorre2");
            otraPieza.CalcularMovimientos(adminPiezas);
        } else if (unaPieza.getNombre().equals("BlancoTorre2") == true) {
            otraPieza = adminPiezas.damePieza("BlancoTorre1");
            otraPieza.CalcularMovimientos(adminPiezas);
        } else if (unaPieza.getNombre().equals("NegroTorre1") == true) {
            otraPieza = adminPiezas.damePieza("NegroTorre2");
            otraPieza.CalcularMovimientos(adminPiezas);
        } else if (unaPieza.getNombre().equals("NegroTorre2") == true) {
            otraPieza = adminPiezas.damePieza("NegroTorre1");
            otraPieza.CalcularMovimientos(adminPiezas);
        }
        return otraPieza;
    }

    public String NotacionPrecisada(Pieza unaPieza, Pieza otraPieza, String strDesde, String strHasta) {
        String Notacion = unaPieza.getNotacion();
        if (unaPieza.getNombre().equals("BlancoCaballo1") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
        } else if (unaPieza.getNombre().equals("BlancoCaballo2") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
        } else if (unaPieza.getNombre().equals("NegroCaballo1") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
            System.out.println("NegroCaballo1::" + Notacion);
        } else if (unaPieza.getNombre().equals("NegroCaballo2") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
            System.out.println("NegroCaballo2::" + Notacion);
        } else if (unaPieza.getNombre().equals("BlancoTorre1") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
        } else if (unaPieza.getNombre().equals("BlancoTorre2") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
        } else if (unaPieza.getNombre().equals("NegroTorre1") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
        } else if (unaPieza.getNombre().equals("NegroTorre2") == true) {
            Notacion = notacionPrecisadaxPieza(otraPieza, Notacion, strDesde, strHasta);
        }
        return Notacion;
    }

    String notacionPrecisadaxPieza(Pieza otraPieza, String Notacion, String strDesde, String strHasta) {
        String parte1, parte2;
        parte1 = Notacion.charAt(0) + "";
        if (otraPieza.estaNotacion(strHasta) == true && otraPieza.getVivo() == true) {
            if (otraPieza.getPosicion().charAt(0) != strDesde.charAt(0)) {
                parte2 = strDesde.charAt(0) + "";
            } else {
                parte2 = strDesde.charAt(1) + "";
            }
            Notacion = parte1 + parte2 + Notacion.substring(1);
        }
        return Notacion;
    }

    private String[] dameDatosPartida() {
        String[] arrDatosPartida = new String[8];
        arrDatosPartida[0] = "Reporte de Partida";
        arrDatosPartida[1] = "Billy Colonia - billycolonia@gmail.com";
        arrDatosPartida[2] = "xxx";
        arrDatosPartida[3] = "xxx";
        arrDatosPartida[4] = "xxx";
        arrDatosPartida[5] = "xxx";
        arrDatosPartida[6] = "";
        arrDatosPartida[7] = "1-0";
        return arrDatosPartida;
    }

    ArrayList<jugadaXML> dameListaJugadasXML() {
        int numOrden = 0, cuentaNodosListaNotaciones = 0;
        int tamanoListaNotaciones = this.listadeNotaciones.size();
        jugadaXML unaJugadaXML;
        RegistroJugadas regJugadasBlancas, regJugadasNegras;
        ArrayList<jugadaXML> arrJugadasXML = new ArrayList<jugadaXML>();
        while (cuentaNodosListaNotaciones < tamanoListaNotaciones) {
            unaJugadaXML = new jugadaXML();
            unaJugadaXML.setOrden(numOrden + 1);
            regJugadasBlancas = this.listadeNotaciones.get(cuentaNodosListaNotaciones);
            unaJugadaXML.setJugBlanco(regJugadasBlancas.dameNotacionPNG());
            unaJugadaXML.setcommBlanco(regJugadasBlancas.dameComentario());
            cuentaNodosListaNotaciones++;
            if (cuentaNodosListaNotaciones >= tamanoListaNotaciones) {
                unaJugadaXML.setJugNegro("");
                unaJugadaXML.setcommNegro("");
            } else {
                regJugadasNegras = this.listadeNotaciones.get(cuentaNodosListaNotaciones);
                if (regJugadasNegras == null) {
                    unaJugadaXML.setJugNegro("");
                    unaJugadaXML.setcommNegro("");
                } else {
                    unaJugadaXML.setJugNegro(regJugadasNegras.dameNotacionPNG());
                    unaJugadaXML.setcommNegro(regJugadasNegras.dameComentario());
                }
            }
            cuentaNodosListaNotaciones++;
            arrJugadasXML.add(unaJugadaXML);
            numOrden++;
        }
        return arrJugadasXML;
    }
}
