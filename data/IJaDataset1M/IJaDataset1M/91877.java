package ajedrezLogica;

import BE.jugador;
import BE.partida;
import BE.torneo;
import BL.partidaBL;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import prototipo.frmElegirJugadores;
import prototipo.frmElegirTorneo;
import prototipo.frmPonerResultados;

/**
 *
 * @author  Billy
 */
public class frmIngresoPartidas extends javax.swing.JInternalFrame {

    Dimension boardSize;

    Evento adminEventos;

    frmPonerResultados fpr, fprObjeto;

    jugador jugadorBlancas, jugadorNegras;

    JDesktopPane jDesktopPane1;

    torneo unTorneo;

    partida unaPartida;

    int elegido = 0;

    partidaBL admPartidaBL;

    int idDivision, ronda, fila, claseVentana;

    JTable jtResultados;

    /** Creates new form frmIngresoPartidas */
    public frmIngresoPartidas(JDesktopPane jDesktopPane1) {
        iniciarVentanaPartida(jDesktopPane1);
        this.claseVentana = 1;
    }

    public frmIngresoPartidas(JDesktopPane jDesktopPane1, int idDivision, int ronda, int fila, frmPonerResultados fprObjeto) {
        iniciarVentanaPartida(jDesktopPane1);
        this.idDivision = idDivision;
        this.ronda = ronda;
        this.fila = fila;
        this.fprObjeto = fprObjeto;
        this.claseVentana = 2;
    }

    public int dameTipoVentanaIngreso() {
        return this.claseVentana;
    }

    void iniciarVentanaPartida(JDesktopPane jDesktopPane1) {
        initComponents();
        this.jDesktopPane1 = jDesktopPane1;
        boardSize = new Dimension(400, 400);
        adminEventos = new Evento(chessBoard2, layeredPane2, boardSize, jtTablaJugadas, jtpComentar, jtTablaComentarios, btnAgregarComentario, jtpComentarios, jtpPGN, this);
        layeredPane2.addMouseListener(adminEventos);
        layeredPane2.addMouseMotionListener(adminEventos);
        jtTablaJugadas.setEnabled(false);
        unaPartida = new partida();
        ButtonGroup grupodeBotones = new ButtonGroup();
        grupodeBotones.add(rbtBlancas);
        grupodeBotones.add(rbtNegras);
        grupodeBotones.add(rbtTablas);
        rbtBlancas.setSelected(true);
        admPartidaBL = new partidaBL();
        Date now = new Date();
        this.jdcFechaPartida.setDate(now);
    }

    private void initComponents() {
        layeredPane2 = new javax.swing.JLayeredPane();
        chessBoard2 = new javax.swing.JPanel();
        pnlOrdenadas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnlAbscisas = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jtpJugadasyComentarios = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtTablaJugadas = new javax.swing.JTable();
        lblComentarios = new javax.swing.JLabel();
        btnAgregarComentario = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtpComentar = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtpComentarios = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtTablaComentarios = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        lblIdPartida = new javax.swing.JLabel();
        txtIdPartida = new javax.swing.JTextField();
        txtJugadorBlancas = new javax.swing.JTextField();
        lblJugadorBlancas = new javax.swing.JLabel();
        lblJugadorNegras = new javax.swing.JLabel();
        txtJugadorNegras = new javax.swing.JTextField();
        lblNombreTorneo = new javax.swing.JLabel();
        txtNombreTorneo = new javax.swing.JTextField();
        txtCiudad = new javax.swing.JTextField();
        jdcFechaPartida = new com.toedter.calendar.JDateChooser();
        lblCiudad = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rbtBlancas = new javax.swing.JRadioButton();
        rbtNegras = new javax.swing.JRadioButton();
        rbtTablas = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnJugadorNegras = new javax.swing.JButton();
        btnJugadorBlancas = new javax.swing.JButton();
        btnElegirTorneo = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtpPGN = new javax.swing.JTextPane();
        btnPGN = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnRetroceder = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        txtComentarios = new javax.swing.JTextField();
        setClosable(true);
        setIconifiable(true);
        setTitle("Ingreso de Partidas");
        layeredPane2.setForeground(new java.awt.Color(255, 255, 255));
        layeredPane2.setMaximumSize(new java.awt.Dimension(400, 400));
        layeredPane2.setMinimumSize(new java.awt.Dimension(400, 400));
        chessBoard2.setLayout(new java.awt.GridLayout(8, 8));
        chessBoard2.setBackground(new java.awt.Color(153, 153, 153));
        chessBoard2.setMaximumSize(new java.awt.Dimension(400, 400));
        chessBoard2.setMinimumSize(new java.awt.Dimension(400, 400));
        chessBoard2.setPreferredSize(new java.awt.Dimension(400, 400));
        chessBoard2.setBounds(0, 0, 400, 400);
        layeredPane2.add(chessBoard2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlOrdenadas.setLayout(new java.awt.GridLayout(8, 1));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("8");
        pnlOrdenadas.add(jLabel1);
        jLabel3.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("7");
        pnlOrdenadas.add(jLabel3);
        jLabel2.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("6");
        pnlOrdenadas.add(jLabel2);
        jLabel5.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("5");
        pnlOrdenadas.add(jLabel5);
        jLabel4.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("4");
        pnlOrdenadas.add(jLabel4);
        jLabel6.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("3");
        pnlOrdenadas.add(jLabel6);
        jLabel7.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("2");
        pnlOrdenadas.add(jLabel7);
        jLabel8.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("1");
        pnlOrdenadas.add(jLabel8);
        pnlAbscisas.setLayout(new java.awt.GridLayout(1, 8));
        pnlAbscisas.setMinimumSize(new java.awt.Dimension(70, 18));
        jLabel9.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("A");
        pnlAbscisas.add(jLabel9);
        jLabel11.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("B");
        pnlAbscisas.add(jLabel11);
        jLabel13.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("C");
        pnlAbscisas.add(jLabel13);
        jLabel14.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("D");
        pnlAbscisas.add(jLabel14);
        jLabel16.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("E");
        pnlAbscisas.add(jLabel16);
        jLabel15.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("F");
        pnlAbscisas.add(jLabel15);
        jLabel12.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("G");
        pnlAbscisas.add(jLabel12);
        jLabel10.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("H");
        pnlAbscisas.add(jLabel10);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setName("pnlJugadas");
        jtTablaJugadas.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Njug", "Blancas", "Negras" }) {

            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.String.class };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtTablaJugadas);
        lblComentarios.setText("Comentarios:");
        btnAgregarComentario.setText("Agregar Comentario");
        btnAgregarComentario.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarComentarioActionPerformed(evt);
            }
        });
        jScrollPane4.setViewportView(jtpComentar);
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE).addComponent(lblComentarios).addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE).addComponent(btnAgregarComentario, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(17, 17, 17).addComponent(lblComentarios).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAgregarComentario).addContainerGap()));
        jtpJugadasyComentarios.addTab("Jugadas", jPanel4);
        jPanel5.setName("pnlComentarios");
        jScrollPane3.setViewportView(jtpComentarios);
        jtTablaComentarios.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null }, { null, null } }, new String[] { "Njug", "Comentario" }) {

            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jtTablaComentarios.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtTablaComentariosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtTablaComentarios);
        btnEditar.setText("Ingresar Comentario");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE).addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15, 15, 15).addComponent(btnEditar).addGap(14, 14, 14).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(22, Short.MAX_VALUE)));
        jtpJugadasyComentarios.addTab("Comentar", jPanel5);
        lblIdPartida.setText("Id Partida:");
        txtIdPartida.setEditable(false);
        lblJugadorBlancas.setText("Jugador de piezas Blancas:");
        lblJugadorNegras.setText("Jugador de piezas Negras: ");
        lblNombreTorneo.setText("Nombre de Torneo:");
        lblCiudad.setText("Ciudad:");
        lblFecha.setText("Fecha:");
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultado Final (*)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
        rbtBlancas.setText("Ganan Blancas");
        rbtBlancas.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbtBlancas.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rbtNegras.setText("Ganan Negras");
        rbtNegras.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbtNegras.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rbtTablas.setText("Tablas");
        rbtTablas.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbtTablas.setMargin(new java.awt.Insets(0, 0, 0, 0));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(rbtTablas).addComponent(rbtNegras)).addGap(148, 148, 148)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(rbtBlancas).addContainerGap(137, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(rbtBlancas).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rbtNegras).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rbtTablas)));
        jPanel1.getAccessibleContext().setAccessibleName("Resultado Final ");
        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel17.setText("(*)");
        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel18.setText("(*)");
        btnJugadorNegras.setText("jButton1");
        btnJugadorNegras.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugadorNegrasActionPerformed(evt);
            }
        });
        btnJugadorBlancas.setText("jButton2");
        btnJugadorBlancas.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugadorBlancasActionPerformed(evt);
            }
        });
        btnElegirTorneo.setText("jButton3");
        btnElegirTorneo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElegirTorneoActionPerformed(evt);
            }
        });
        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel19.setText("(*)");
        jLabel20.setText("Datos Obligatorios");
        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblJugadorBlancas).addComponent(lblJugadorNegras).addComponent(lblNombreTorneo).addComponent(lblCiudad).addComponent(lblFecha).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(txtNombreTorneo, javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtJugadorNegras, javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtJugadorBlancas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jLabel17).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnJugadorBlancas, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnElegirTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jLabel18).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnJugadorNegras, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jdcFechaPartida, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(lblIdPartida).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtIdPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(txtCiudad, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)).addGap(113, 113, 113)).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jLabel19).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel20))).addContainerGap()));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblIdPartida).addComponent(txtIdPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(16, 16, 16).addComponent(lblJugadorBlancas).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtJugadorBlancas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel17).addComponent(btnJugadorBlancas)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblJugadorNegras).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtJugadorNegras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel18).addComponent(btnJugadorNegras)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblNombreTorneo).addGap(3, 3, 3).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtNombreTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnElegirTorneo)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblCiudad).addGap(4, 4, 4).addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblFecha).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jdcFechaPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(16, 16, 16).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(12, 12, 12).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel19).addComponent(jLabel20)).addContainerGap()));
        jtpJugadasyComentarios.addTab("Datos", jPanel6);
        jScrollPane5.setViewportView(jtpPGN);
        btnPGN.setText("Generar archivo PGN");
        btnPGN.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPGNActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE).addComponent(btnPGN)).addContainerGap()));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE).addComponent(btnPGN).addGap(21, 21, 21)));
        jtpJugadasyComentarios.addTab("PGN", jPanel7);
        jtpJugadasyComentarios.getAccessibleContext().setAccessibleName("Jugadas");
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jtpJugadasyComentarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE).addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(jtpJugadasyComentarios, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnCancelar).addComponent(btnGrabar)).addContainerGap()));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnRetroceder.setText("Retroceder");
        btnRetroceder.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetrocederActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(btnRetroceder, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(btnRetroceder).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        btnReporte.setText("Generar Reporte");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(pnlOrdenadas, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(layeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(btnReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtComentarios, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)).addComponent(pnlAbscisas, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(15, 15, 15).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(37, 37, 37).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(pnlOrdenadas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addComponent(layeredPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pnlAbscisas, javax.swing.GroupLayout.PREFERRED_SIZE, 17, Short.MAX_VALUE).addGap(14, 14, 14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(txtComentarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnReporte)).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addGap(23, 23, 23)));
        pack();
    }

    void RegistrarPartida() {
        String resultado = "1-0";
        if (this.unTorneo != null) {
            unaPartida.setIdTorneo(this.unTorneo.getIdTorneo());
        }
        if (this.jugadorBlancas != null) {
            unaPartida.setIdJugadorBlancas(this.jugadorBlancas.getIdJugador());
        }
        if (this.jugadorNegras != null) {
            unaPartida.setIdJugadorNegras(this.jugadorNegras.getIdJugador());
        }
        if (this.jdcFechaPartida != null) {
            unaPartida.setFechaJuego(this.jdcFechaPartida.getCalendar());
        }
        unaPartida.setEstadoRegistrado("V");
        adminEventos.BotonPGN();
        unaPartida.setMovimimentos(this.jtpPGN.getText());
        if (this.rbtBlancas.isSelected() == true) resultado = "1-0"; else if (this.rbtNegras.isSelected() == true) resultado = "0-1"; else if (this.rbtTablas.isSelected() == true) resultado = "1/2-1/2";
        unaPartida.setResultado(resultado);
        String nombreBlancas, nombreNegras;
        if (this.jugadorBlancas != null) {
            nombreBlancas = this.jugadorBlancas.getApellidoPaterno() + ", " + this.jugadorBlancas.getNombres();
            unaPartida.setNombreJugadorBlanco(nombreBlancas);
            unaPartida.setEloBlancas(this.jugadorBlancas.getRating() + "");
        }
        if (this.jugadorNegras != null) {
            nombreNegras = this.jugadorNegras.getApellidoPaterno() + ", " + this.jugadorNegras.getNombres();
            unaPartida.setNombreJugadorNegro(nombreNegras);
            unaPartida.setEloNegras(this.jugadorNegras.getRating() + "");
        }
        unaPartida.setApertura(0);
        if (this.txtJugadorBlancas.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Elija al jugador de piezas blancas, Por favor", "Informacion", JOptionPane.WARNING_MESSAGE);
        } else if (this.txtJugadorNegras.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Elija al jugador de piezas negras, Por favor", "Informacion", JOptionPane.WARNING_MESSAGE);
        } else if (this.txtNombreTorneo.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Elija el nombre del torneo, Por favor", "Informacion", JOptionPane.WARNING_MESSAGE);
        } else if (this.jtpPGN.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "No se ha realizado ninguna jugada.", "Informacion", JOptionPane.WARNING_MESSAGE);
        } else {
            if (admPartidaBL.crear(unaPartida)) {
                if (this.claseVentana == 2) {
                    this.fpr = new frmPonerResultados(this.idDivision, this.ronda);
                    this.fpr.actualizarPartidadePareo(this.jugadorBlancas, this.jugadorNegras, ultimoIdPartida());
                    Object[][] data = this.fprObjeto.dameData();
                    data[this.fila][4] = "SI";
                    this.fprObjeto.pintarVentana(data);
                }
                JOptionPane.showMessageDialog(this, "Los datos han sido guardados correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            } else JOptionPane.showMessageDialog(this, "Ha Ocurrido un error. No se han grabado los datos", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            int numJugadas = adminEventos.dameNumeroJugadas();
            for (int i = 0; i < numJugadas; i++) {
                adminEventos.BotonVolver();
            }
            this.LimpiarCampos();
        }
    }

    void LimpiarCampos() {
        this.txtNombreTorneo.setText("");
        this.txtJugadorBlancas.setText("");
        this.txtJugadorNegras.setText("");
        this.txtCiudad.setText("");
    }

    public int dameIdDivision() {
        return this.idDivision;
    }

    public String dametxtJugadorBlancas() {
        return this.txtJugadorBlancas.getText();
    }

    public String dametxtJugadorNegras() {
        return this.txtJugadorNegras.getText();
    }

    public String dametxtNombreTorneo() {
        return this.txtNombreTorneo.getText();
    }

    public String dametxtCiudad() {
        return this.txtCiudad.getText();
    }

    public String dametxtFecha() {
        int ano, mes, dia;
        Calendar miCalendario = this.jdcFechaPartida.getCalendar();
        ano = miCalendario.get(Calendar.YEAR);
        mes = miCalendario.get(Calendar.MONTH) + 1;
        dia = miCalendario.get(Calendar.DATE);
        String fecha = dia + "/" + mes + "/" + ano;
        return fecha;
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {
        this.RegistrarPartida();
    }

    private void btnElegirTorneoActionPerformed(java.awt.event.ActionEvent evt) {
        frmElegirTorneo fet = new frmElegirTorneo(this);
        this.jDesktopPane1.add(fet);
        fet.setVisible(true);
    }

    private void btnJugadorNegrasActionPerformed(java.awt.event.ActionEvent evt) {
        frmElegirJugadores fej = new frmElegirJugadores(this, "negro");
        this.jDesktopPane1.add(fej);
        fej.setVisible(true);
    }

    private void btnJugadorBlancasActionPerformed(java.awt.event.ActionEvent evt) {
        frmElegirJugadores fej = new frmElegirJugadores(this, "blanco");
        this.jDesktopPane1.add(fej);
        fej.setVisible(true);
    }

    public JTabbedPane damePanelPestanas() {
        return this.jtpJugadasyComentarios;
    }

    public void dameJugadorBlancas(jugador unJugador) {
        this.jugadorBlancas = unJugador;
        String nombres = unJugador.getApellidoPaterno() + ", " + unJugador.getNombres() + " (" + unJugador.getRating() + ")";
        ;
        this.txtJugadorBlancas.setText(nombres);
    }

    public void dameTorneo(torneo unTorneo) {
        this.unTorneo = unTorneo;
        this.txtNombreTorneo.setText(unTorneo.getnombreTorneo());
        this.txtCiudad.setText(unTorneo.getCiudad());
    }

    public void dameJugadorNegras(jugador unJugador) {
        this.jugadorNegras = unJugador;
        String nombres = unJugador.getApellidoPaterno() + ", " + unJugador.getNombres() + " (" + unJugador.getRating() + ")";
        this.txtJugadorNegras.setText(nombres);
    }

    public void dameResultados(String resultado) {
        if (resultado.equals("1-0") == true) this.rbtBlancas.setSelected(true);
        if (resultado.equals("1/2-1/2") == true) this.rbtTablas.setSelected(true);
        if (resultado.equals("0-1") == true) this.rbtNegras.setSelected(true);
    }

    public String dameResultados() {
        String resultado = "1-0";
        if (this.rbtBlancas.isSelected() == true) resultado = "1-0"; else if (this.rbtNegras.isSelected() == true) resultado = "0-1"; else if (this.rbtTablas.isSelected() == true) resultado = "1/2-1/2";
        return resultado;
    }

    public int dameIdPartida() {
        int idUltimaPartida;
        idUltimaPartida = this.admPartidaBL.getUltimoIdPartida();
        this.txtIdPartida.setText(++idUltimaPartida + "");
        return idUltimaPartida;
    }

    public int ultimoIdPartida() {
        int idUltimaPartida;
        idUltimaPartida = this.admPartidaBL.getUltimoIdPartida();
        return idUltimaPartida;
    }

    private void jtTablaComentariosMouseClicked(java.awt.event.MouseEvent evt) {
        int row = this.jtTablaComentarios.getSelectedRow();
        adminEventos.ActualizarComentarios(row, 1);
    }

    private void btnPGNActionPerformed(java.awt.event.ActionEvent evt) {
        adminEventos.BotonPGN();
    }

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        int row = this.jtTablaComentarios.getSelectedRow();
        if (row > 0) {
            adminEventos.ActualizarComentarios(row, 0);
            this.jtpComentarios.setText("");
        }
    }

    private void btnAgregarComentarioActionPerformed(java.awt.event.ActionEvent evt) {
        adminEventos.BotonAgregarComentario();
    }

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {
        adminEventos.BotonReporte();
    }

    private void btnRetrocederActionPerformed(java.awt.event.ActionEvent evt) {
        adminEventos.BotonVolver();
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private javax.swing.JButton btnAgregarComentario;

    private javax.swing.JButton btnCancelar;

    private javax.swing.JButton btnEditar;

    private javax.swing.JButton btnElegirTorneo;

    private javax.swing.JButton btnGrabar;

    private javax.swing.JButton btnJugadorBlancas;

    private javax.swing.JButton btnJugadorNegras;

    private javax.swing.JButton btnPGN;

    private javax.swing.JButton btnReporte;

    private javax.swing.JButton btnRetroceder;

    private javax.swing.JPanel chessBoard2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JScrollPane jScrollPane5;

    private com.toedter.calendar.JDateChooser jdcFechaPartida;

    private javax.swing.JTable jtTablaComentarios;

    private javax.swing.JTable jtTablaJugadas;

    private javax.swing.JTextPane jtpComentar;

    private javax.swing.JTextPane jtpComentarios;

    private javax.swing.JTabbedPane jtpJugadasyComentarios;

    private javax.swing.JTextPane jtpPGN;

    private javax.swing.JLayeredPane layeredPane2;

    private javax.swing.JLabel lblCiudad;

    private javax.swing.JLabel lblComentarios;

    private javax.swing.JLabel lblFecha;

    private javax.swing.JLabel lblIdPartida;

    private javax.swing.JLabel lblJugadorBlancas;

    private javax.swing.JLabel lblJugadorNegras;

    private javax.swing.JLabel lblNombreTorneo;

    private javax.swing.JPanel pnlAbscisas;

    private javax.swing.JPanel pnlOrdenadas;

    private javax.swing.JRadioButton rbtBlancas;

    private javax.swing.JRadioButton rbtNegras;

    private javax.swing.JRadioButton rbtTablas;

    private javax.swing.JTextField txtCiudad;

    private javax.swing.JTextField txtComentarios;

    private javax.swing.JTextField txtIdPartida;

    private javax.swing.JTextField txtJugadorBlancas;

    private javax.swing.JTextField txtJugadorNegras;

    private javax.swing.JTextField txtNombreTorneo;
}
