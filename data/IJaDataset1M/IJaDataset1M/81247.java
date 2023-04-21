package mx.ipn.presentacion.telefonista.ui;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_Principal {

    public QAction action_Salir;

    public QAction actionDeshacer;

    public QAction actionRehacer;

    public QAction actionCortar;

    public QAction actionCopiar;

    public QAction actionPegar;

    public QAction actionBorrar;

    public QAction actionBuscarCambiar;

    public QAction actionRegistrarCl;

    public QAction actionModificarCliente;

    public QAction actionRegistrarDireccion;

    public QAction actionModificarDireccion;

    public QAction actionAgregarServicio;

    public QAction actionBitacora;

    public QAction actionRegistrar_Modificar_Direcci_n;

    public QAction actionRegistrar_Cliente;

    public QAction actionAsociar_Cliente_Direcci_n;

    public QAction actionModificar_Cliente;

    public QAction actionBuscar_en_la_Bit_cora;

    public QAction actionChat;

    public QAction actionMapa;

    public QWidget centralwidget;

    public QVBoxLayout vboxLayout;

    public QDockWidget dockWidgetBitacora;

    public QWidget dockWidgetContents_2;

    public QVBoxLayout vboxLayout1;

    public QTableWidget tableWidgetBitacora;

    public QDockWidget dockWidgetAgregarServicio;

    public QWidget dockWidgetContents;

    public QVBoxLayout vboxLayout2;

    public QHBoxLayout hboxLayout;

    public QLabel label_2;

    public QLineEdit lineEditTelefono;

    public QLabel label_13;

    public QComboBox comboBoxNombre;

    public QHBoxLayout hboxLayout1;

    public QLabel label_4;

    public QComboBox comboBoxOrigen;

    public QHBoxLayout hboxLayout2;

    public QLabel label_12;

    public QComboBox comboBoxDestino;

    public QHBoxLayout hboxLayout3;

    public QLabel label_10;

    public QTextEdit textEditObservaciones;

    public QHBoxLayout hboxLayout4;

    public QHBoxLayout hboxLayout5;

    public QLabel label_11;

    public QComboBox comboBoxTipoServicio;

    public QHBoxLayout hboxLayout6;

    public QHBoxLayout hboxLayout7;

    public QCheckBox checkBoxReservado;

    public QDateTimeEdit dateTimeEditReservado;

    public QSpacerItem spacerItem;

    public QHBoxLayout hboxLayout8;

    public QHBoxLayout hboxLayout9;

    public QLabel label;

    public QComboBox comboBoxFormaDePago;

    public QCheckBox checkBoxFactura;

    public QHBoxLayout hboxLayout10;

    public QSpacerItem spacerItem1;

    public QPushButton pushButtonAgregar;

    public QMenuBar menubar;

    public QMenu menu_Archivo;

    public QMenu menuEdici_n;

    public QMenu menuAcciones;

    public QMenu menuVentana;

    public QStatusBar statusbar;

    public Ui_Principal() {
        super();
    }

    public void setupUi(QMainWindow Principal) {
        Principal.setObjectName("Principal");
        Principal.resize(new QSize(1016, 674).expandedTo(Principal.minimumSizeHint()));
        Principal.setDockOptions(com.trolltech.qt.gui.QMainWindow.DockOption.createQFlags(com.trolltech.qt.gui.QMainWindow.DockOption.AllowNestedDocks, com.trolltech.qt.gui.QMainWindow.DockOption.AllowTabbedDocks, com.trolltech.qt.gui.QMainWindow.DockOption.AnimatedDocks));
        action_Salir = new QAction(Principal);
        action_Salir.setObjectName("action_Salir");
        actionDeshacer = new QAction(Principal);
        actionDeshacer.setObjectName("actionDeshacer");
        actionDeshacer.setEnabled(false);
        actionRehacer = new QAction(Principal);
        actionRehacer.setObjectName("actionRehacer");
        actionRehacer.setEnabled(false);
        actionCortar = new QAction(Principal);
        actionCortar.setObjectName("actionCortar");
        actionCopiar = new QAction(Principal);
        actionCopiar.setObjectName("actionCopiar");
        actionPegar = new QAction(Principal);
        actionPegar.setObjectName("actionPegar");
        actionPegar.setEnabled(false);
        actionBorrar = new QAction(Principal);
        actionBorrar.setObjectName("actionBorrar");
        actionBuscarCambiar = new QAction(Principal);
        actionBuscarCambiar.setObjectName("actionBuscarCambiar");
        actionRegistrarCl = new QAction(Principal);
        actionRegistrarCl.setObjectName("actionRegistrarCl");
        actionModificarCliente = new QAction(Principal);
        actionModificarCliente.setObjectName("actionModificarCliente");
        actionRegistrarDireccion = new QAction(Principal);
        actionRegistrarDireccion.setObjectName("actionRegistrarDireccion");
        actionModificarDireccion = new QAction(Principal);
        actionModificarDireccion.setObjectName("actionModificarDireccion");
        actionAgregarServicio = new QAction(Principal);
        actionAgregarServicio.setObjectName("actionAgregarServicio");
        actionBitacora = new QAction(Principal);
        actionBitacora.setObjectName("actionBitacora");
        actionRegistrar_Modificar_Direcci_n = new QAction(Principal);
        actionRegistrar_Modificar_Direcci_n.setObjectName("actionRegistrar_Modificar_Direcci_n");
        actionRegistrar_Cliente = new QAction(Principal);
        actionRegistrar_Cliente.setObjectName("actionRegistrar_Cliente");
        actionAsociar_Cliente_Direcci_n = new QAction(Principal);
        actionAsociar_Cliente_Direcci_n.setObjectName("actionAsociar_Cliente_Direcci_n");
        actionModificar_Cliente = new QAction(Principal);
        actionModificar_Cliente.setObjectName("actionModificar_Cliente");
        actionBuscar_en_la_Bit_cora = new QAction(Principal);
        actionBuscar_en_la_Bit_cora.setObjectName("actionBuscar_en_la_Bit_cora");
        actionChat = new QAction(Principal);
        actionChat.setObjectName("actionChat");
        actionMapa = new QAction(Principal);
        actionMapa.setObjectName("actionMapa");
        centralwidget = new QWidget(Principal);
        centralwidget.setObjectName("centralwidget");
        vboxLayout = new QVBoxLayout(centralwidget);
        vboxLayout.setObjectName("vboxLayout");
        dockWidgetBitacora = new QDockWidget(centralwidget);
        dockWidgetBitacora.setObjectName("dockWidgetBitacora");
        dockWidgetBitacora.setFloating(false);
        dockWidgetBitacora.setWindowModality(com.trolltech.qt.core.Qt.WindowModality.NonModal);
        dockWidgetContents_2 = new QWidget(dockWidgetBitacora);
        dockWidgetContents_2.setObjectName("dockWidgetContents_2");
        vboxLayout1 = new QVBoxLayout(dockWidgetContents_2);
        vboxLayout1.setObjectName("vboxLayout1");
        tableWidgetBitacora = new QTableWidget(dockWidgetContents_2);
        tableWidgetBitacora.setObjectName("tableWidgetBitacora");
        tableWidgetBitacora.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        tableWidgetBitacora.setEditTriggers(com.trolltech.qt.gui.QAbstractItemView.EditTrigger.createQFlags(com.trolltech.qt.gui.QAbstractItemView.EditTrigger.NoEditTriggers));
        tableWidgetBitacora.setAlternatingRowColors(true);
        tableWidgetBitacora.setSelectionBehavior(com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior.SelectRows);
        tableWidgetBitacora.setGridStyle(com.trolltech.qt.core.Qt.PenStyle.DashDotDotLine);
        vboxLayout1.addWidget(tableWidgetBitacora);
        dockWidgetBitacora.setWidget(dockWidgetContents_2);
        vboxLayout.addWidget(dockWidgetBitacora);
        dockWidgetAgregarServicio = new QDockWidget(centralwidget);
        dockWidgetAgregarServicio.setObjectName("dockWidgetAgregarServicio");
        dockWidgetContents = new QWidget(dockWidgetAgregarServicio);
        dockWidgetContents.setObjectName("dockWidgetContents");
        vboxLayout2 = new QVBoxLayout(dockWidgetContents);
        vboxLayout2.setObjectName("vboxLayout2");
        hboxLayout = new QHBoxLayout();
        hboxLayout.setObjectName("hboxLayout");
        hboxLayout.setContentsMargins(0, 0, 0, 0);
        label_2 = new QLabel(dockWidgetContents);
        label_2.setObjectName("label_2");
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy.setHorizontalStretch((byte) 0);
        sizePolicy.setVerticalStretch((byte) 0);
        sizePolicy.setHeightForWidth(label_2.sizePolicy().hasHeightForWidth());
        label_2.setSizePolicy(sizePolicy);
        hboxLayout.addWidget(label_2);
        lineEditTelefono = new QLineEdit(dockWidgetContents);
        lineEditTelefono.setObjectName("lineEditTelefono");
        QSizePolicy sizePolicy1 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy1.setHorizontalStretch((byte) 0);
        sizePolicy1.setVerticalStretch((byte) 0);
        sizePolicy1.setHeightForWidth(lineEditTelefono.sizePolicy().hasHeightForWidth());
        lineEditTelefono.setSizePolicy(sizePolicy1);
        lineEditTelefono.setMaximumSize(new QSize(110, 23));
        lineEditTelefono.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));
        hboxLayout.addWidget(lineEditTelefono);
        label_13 = new QLabel(dockWidgetContents);
        label_13.setObjectName("label_13");
        QSizePolicy sizePolicy2 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy2.setHorizontalStretch((byte) 0);
        sizePolicy2.setVerticalStretch((byte) 0);
        sizePolicy2.setHeightForWidth(label_13.sizePolicy().hasHeightForWidth());
        label_13.setSizePolicy(sizePolicy2);
        label_13.setMaximumSize(new QSize(60, 16777215));
        hboxLayout.addWidget(label_13);
        comboBoxNombre = new QComboBox(dockWidgetContents);
        comboBoxNombre.setObjectName("comboBoxNombre");
        QSizePolicy sizePolicy3 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy3.setHorizontalStretch((byte) 0);
        sizePolicy3.setVerticalStretch((byte) 0);
        sizePolicy3.setHeightForWidth(comboBoxNombre.sizePolicy().hasHeightForWidth());
        comboBoxNombre.setSizePolicy(sizePolicy3);
        comboBoxNombre.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        comboBoxNombre.setEditable(true);
        hboxLayout.addWidget(comboBoxNombre);
        vboxLayout2.addLayout(hboxLayout);
        hboxLayout1 = new QHBoxLayout();
        hboxLayout1.setObjectName("hboxLayout1");
        hboxLayout1.setContentsMargins(0, 0, 0, 0);
        label_4 = new QLabel(dockWidgetContents);
        label_4.setObjectName("label_4");
        QSizePolicy sizePolicy4 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy4.setHorizontalStretch((byte) 0);
        sizePolicy4.setVerticalStretch((byte) 0);
        sizePolicy4.setHeightForWidth(label_4.sizePolicy().hasHeightForWidth());
        label_4.setSizePolicy(sizePolicy4);
        label_4.setMaximumSize(new QSize(70, 16777215));
        hboxLayout1.addWidget(label_4);
        comboBoxOrigen = new QComboBox(dockWidgetContents);
        comboBoxOrigen.setObjectName("comboBoxOrigen");
        QSizePolicy sizePolicy5 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy5.setHorizontalStretch((byte) 0);
        sizePolicy5.setVerticalStretch((byte) 0);
        sizePolicy5.setHeightForWidth(comboBoxOrigen.sizePolicy().hasHeightForWidth());
        comboBoxOrigen.setSizePolicy(sizePolicy5);
        comboBoxOrigen.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        comboBoxOrigen.setEditable(true);
        comboBoxOrigen.setDuplicatesEnabled(true);
        hboxLayout1.addWidget(comboBoxOrigen);
        vboxLayout2.addLayout(hboxLayout1);
        hboxLayout2 = new QHBoxLayout();
        hboxLayout2.setObjectName("hboxLayout2");
        hboxLayout2.setContentsMargins(0, 0, 0, 0);
        label_12 = new QLabel(dockWidgetContents);
        label_12.setObjectName("label_12");
        QSizePolicy sizePolicy6 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy6.setHorizontalStretch((byte) 0);
        sizePolicy6.setVerticalStretch((byte) 0);
        sizePolicy6.setHeightForWidth(label_12.sizePolicy().hasHeightForWidth());
        label_12.setSizePolicy(sizePolicy6);
        label_12.setMaximumSize(new QSize(70, 16777215));
        hboxLayout2.addWidget(label_12);
        comboBoxDestino = new QComboBox(dockWidgetContents);
        comboBoxDestino.setObjectName("comboBoxDestino");
        QSizePolicy sizePolicy7 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Preferred, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy7.setHorizontalStretch((byte) 0);
        sizePolicy7.setVerticalStretch((byte) 0);
        sizePolicy7.setHeightForWidth(comboBoxDestino.sizePolicy().hasHeightForWidth());
        comboBoxDestino.setSizePolicy(sizePolicy7);
        comboBoxDestino.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        comboBoxDestino.setEditable(true);
        comboBoxDestino.setDuplicatesEnabled(true);
        hboxLayout2.addWidget(comboBoxDestino);
        vboxLayout2.addLayout(hboxLayout2);
        hboxLayout3 = new QHBoxLayout();
        hboxLayout3.setObjectName("hboxLayout3");
        hboxLayout3.setContentsMargins(0, 0, 0, 0);
        label_10 = new QLabel(dockWidgetContents);
        label_10.setObjectName("label_10");
        QSizePolicy sizePolicy8 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy8.setHorizontalStretch((byte) 0);
        sizePolicy8.setVerticalStretch((byte) 0);
        sizePolicy8.setHeightForWidth(label_10.sizePolicy().hasHeightForWidth());
        label_10.setSizePolicy(sizePolicy8);
        hboxLayout3.addWidget(label_10);
        textEditObservaciones = new QTextEdit(dockWidgetContents);
        textEditObservaciones.setObjectName("textEditObservaciones");
        textEditObservaciones.setMaximumSize(new QSize(16777215, 80));
        textEditObservaciones.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        hboxLayout3.addWidget(textEditObservaciones);
        vboxLayout2.addLayout(hboxLayout3);
        hboxLayout4 = new QHBoxLayout();
        hboxLayout4.setObjectName("hboxLayout4");
        hboxLayout4.setContentsMargins(0, 0, 0, 0);
        hboxLayout5 = new QHBoxLayout();
        hboxLayout5.setObjectName("hboxLayout5");
        hboxLayout5.setContentsMargins(0, 0, 0, 0);
        label_11 = new QLabel(dockWidgetContents);
        label_11.setObjectName("label_11");
        QSizePolicy sizePolicy9 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy9.setHorizontalStretch((byte) 0);
        sizePolicy9.setVerticalStretch((byte) 0);
        sizePolicy9.setHeightForWidth(label_11.sizePolicy().hasHeightForWidth());
        label_11.setSizePolicy(sizePolicy9);
        hboxLayout5.addWidget(label_11);
        comboBoxTipoServicio = new QComboBox(dockWidgetContents);
        comboBoxTipoServicio.setObjectName("comboBoxTipoServicio");
        comboBoxTipoServicio.setEnabled(true);
        QSizePolicy sizePolicy10 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy10.setHorizontalStretch((byte) 0);
        sizePolicy10.setVerticalStretch((byte) 0);
        sizePolicy10.setHeightForWidth(comboBoxTipoServicio.sizePolicy().hasHeightForWidth());
        comboBoxTipoServicio.setSizePolicy(sizePolicy10);
        comboBoxTipoServicio.setMinimumSize(new QSize(150, 0));
        comboBoxTipoServicio.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        comboBoxTipoServicio.setEditable(false);
        hboxLayout5.addWidget(comboBoxTipoServicio);
        hboxLayout4.addLayout(hboxLayout5);
        hboxLayout6 = new QHBoxLayout();
        hboxLayout6.setObjectName("hboxLayout6");
        hboxLayout6.setContentsMargins(0, 0, 0, 0);
        hboxLayout7 = new QHBoxLayout();
        hboxLayout7.setObjectName("hboxLayout7");
        hboxLayout7.setContentsMargins(0, 0, 0, 0);
        checkBoxReservado = new QCheckBox(dockWidgetContents);
        checkBoxReservado.setObjectName("checkBoxReservado");
        checkBoxReservado.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        hboxLayout7.addWidget(checkBoxReservado);
        dateTimeEditReservado = new QDateTimeEdit(dockWidgetContents);
        dateTimeEditReservado.setObjectName("dateTimeEditReservado");
        dateTimeEditReservado.setEnabled(false);
        QSizePolicy sizePolicy11 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy11.setHorizontalStretch((byte) 0);
        sizePolicy11.setVerticalStretch((byte) 0);
        sizePolicy11.setHeightForWidth(dateTimeEditReservado.sizePolicy().hasHeightForWidth());
        dateTimeEditReservado.setSizePolicy(sizePolicy11);
        dateTimeEditReservado.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        hboxLayout7.addWidget(dateTimeEditReservado);
        hboxLayout6.addLayout(hboxLayout7);
        hboxLayout4.addLayout(hboxLayout6);
        spacerItem = new QSpacerItem(40, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);
        hboxLayout4.addItem(spacerItem);
        vboxLayout2.addLayout(hboxLayout4);
        hboxLayout8 = new QHBoxLayout();
        hboxLayout8.setObjectName("hboxLayout8");
        hboxLayout8.setContentsMargins(0, 0, 0, 0);
        hboxLayout9 = new QHBoxLayout();
        hboxLayout9.setObjectName("hboxLayout9");
        hboxLayout9.setContentsMargins(0, 0, 0, 0);
        label = new QLabel(dockWidgetContents);
        label.setObjectName("label");
        QSizePolicy sizePolicy12 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Fixed, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy12.setHorizontalStretch((byte) 0);
        sizePolicy12.setVerticalStretch((byte) 0);
        sizePolicy12.setHeightForWidth(label.sizePolicy().hasHeightForWidth());
        label.setSizePolicy(sizePolicy12);
        hboxLayout9.addWidget(label);
        comboBoxFormaDePago = new QComboBox(dockWidgetContents);
        comboBoxFormaDePago.setObjectName("comboBoxFormaDePago");
        QSizePolicy sizePolicy13 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy13.setHorizontalStretch((byte) 0);
        sizePolicy13.setVerticalStretch((byte) 0);
        sizePolicy13.setHeightForWidth(comboBoxFormaDePago.sizePolicy().hasHeightForWidth());
        comboBoxFormaDePago.setSizePolicy(sizePolicy13);
        comboBoxFormaDePago.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        hboxLayout9.addWidget(comboBoxFormaDePago);
        hboxLayout8.addLayout(hboxLayout9);
        checkBoxFactura = new QCheckBox(dockWidgetContents);
        checkBoxFactura.setObjectName("checkBoxFactura");
        checkBoxFactura.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        hboxLayout8.addWidget(checkBoxFactura);
        hboxLayout10 = new QHBoxLayout();
        hboxLayout10.setObjectName("hboxLayout10");
        hboxLayout10.setContentsMargins(0, 0, 0, 0);
        spacerItem1 = new QSpacerItem(40, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);
        hboxLayout10.addItem(spacerItem1);
        pushButtonAgregar = new QPushButton(dockWidgetContents);
        pushButtonAgregar.setObjectName("pushButtonAgregar");
        pushButtonAgregar.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        pushButtonAgregar.setIcon(new QIcon(new QPixmap("classpath:mx/ipn/presentacion/recursos/iconos/plus.png")));
        pushButtonAgregar.setIconSize(new QSize(22, 22));
        hboxLayout10.addWidget(pushButtonAgregar);
        hboxLayout8.addLayout(hboxLayout10);
        vboxLayout2.addLayout(hboxLayout8);
        dockWidgetAgregarServicio.setWidget(dockWidgetContents);
        vboxLayout.addWidget(dockWidgetAgregarServicio);
        Principal.setCentralWidget(centralwidget);
        menubar = new QMenuBar(Principal);
        menubar.setObjectName("menubar");
        menubar.setGeometry(new QRect(0, 0, 1016, 31));
        menu_Archivo = new QMenu(menubar);
        menu_Archivo.setObjectName("menu_Archivo");
        menuEdici_n = new QMenu(menubar);
        menuEdici_n.setObjectName("menuEdici_n");
        menuAcciones = new QMenu(menubar);
        menuAcciones.setObjectName("menuAcciones");
        menuVentana = new QMenu(menubar);
        menuVentana.setObjectName("menuVentana");
        Principal.setMenuBar(menubar);
        statusbar = new QStatusBar(Principal);
        statusbar.setObjectName("statusbar");
        Principal.setStatusBar(statusbar);
        menubar.addAction(menu_Archivo.menuAction());
        menubar.addAction(menuEdici_n.menuAction());
        menubar.addAction(menuAcciones.menuAction());
        menubar.addAction(menuVentana.menuAction());
        menu_Archivo.addAction(action_Salir);
        menuEdici_n.addAction(actionDeshacer);
        menuEdici_n.addAction(actionRehacer);
        menuEdici_n.addSeparator();
        menuEdici_n.addAction(actionCortar);
        menuEdici_n.addAction(actionCopiar);
        menuEdici_n.addAction(actionPegar);
        menuEdici_n.addSeparator();
        menuEdici_n.addAction(actionBorrar);
        menuEdici_n.addSeparator();
        menuEdici_n.addAction(actionBuscarCambiar);
        menuAcciones.addSeparator();
        menuAcciones.addAction(actionRegistrar_Modificar_Direcci_n);
        menuAcciones.addSeparator();
        menuAcciones.addAction(actionRegistrar_Cliente);
        menuAcciones.addAction(actionModificar_Cliente);
        menuAcciones.addSeparator();
        menuAcciones.addAction(actionAsociar_Cliente_Direcci_n);
        menuAcciones.addSeparator();
        menuAcciones.addAction(actionBuscar_en_la_Bit_cora);
        menuVentana.addAction(actionAgregarServicio);
        menuVentana.addAction(actionBitacora);
        menuVentana.addSeparator();
        menuVentana.addAction(actionChat);
        menuVentana.addAction(actionMapa);
        retranslateUi(Principal);
        checkBoxReservado.toggled.connect(dateTimeEditReservado, "setEnabled(boolean)");
        Principal.connectSlotsByName();
    }

    void retranslateUi(QMainWindow Principal) {
        Principal.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("Principal", "SAIST - Telefonista"));
        action_Salir.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "&Salir"));
        actionDeshacer.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Deshacer"));
        actionRehacer.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Rehacer"));
        actionCortar.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Cortar"));
        actionCopiar.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Copiar"));
        actionPegar.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Pegar"));
        actionBorrar.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Borrar"));
        actionBuscarCambiar.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Buscar/Filtrar"));
        actionRegistrarCl.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Registrar Cliente"));
        actionModificarCliente.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Modificar Cliente"));
        actionRegistrarDireccion.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Registrar/Modificar Dirección"));
        actionModificarDireccion.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Modificar Dirección"));
        actionAgregarServicio.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Agregar Servicio"));
        actionBitacora.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Bitácora"));
        actionRegistrar_Modificar_Direcci_n.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Registrar/Modificar Dirección"));
        actionRegistrar_Cliente.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Registrar Cliente"));
        actionAsociar_Cliente_Direcci_n.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Asociar Cliente - Dirección"));
        actionModificar_Cliente.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Modificar Cliente"));
        actionBuscar_en_la_Bit_cora.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Buscar en la Bitácora"));
        actionChat.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Chat"));
        actionMapa.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Mapa"));
        dockWidgetBitacora.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Bitácora de Servicios"));
        tableWidgetBitacora.clear();
        tableWidgetBitacora.setColumnCount(17);
        QTableWidgetItem __colItem = new QTableWidgetItem();
        __colItem.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Número"));
        tableWidgetBitacora.setHorizontalHeaderItem(0, __colItem);
        QTableWidgetItem __colItem1 = new QTableWidgetItem();
        __colItem1.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Tipo"));
        tableWidgetBitacora.setHorizontalHeaderItem(1, __colItem1);
        QTableWidgetItem __colItem2 = new QTableWidgetItem();
        __colItem2.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Captura"));
        tableWidgetBitacora.setHorizontalHeaderItem(2, __colItem2);
        QTableWidgetItem __colItem3 = new QTableWidgetItem();
        __colItem3.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Asignación"));
        tableWidgetBitacora.setHorizontalHeaderItem(3, __colItem3);
        QTableWidgetItem __colItem4 = new QTableWidgetItem();
        __colItem4.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Cubierto"));
        tableWidgetBitacora.setHorizontalHeaderItem(4, __colItem4);
        QTableWidgetItem __colItem5 = new QTableWidgetItem();
        __colItem5.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Reservación"));
        tableWidgetBitacora.setHorizontalHeaderItem(5, __colItem5);
        QTableWidgetItem __colItem6 = new QTableWidgetItem();
        __colItem6.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Status"));
        tableWidgetBitacora.setHorizontalHeaderItem(6, __colItem6);
        QTableWidgetItem __colItem7 = new QTableWidgetItem();
        __colItem7.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Cliente"));
        tableWidgetBitacora.setHorizontalHeaderItem(7, __colItem7);
        QTableWidgetItem __colItem8 = new QTableWidgetItem();
        __colItem8.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Teléfono"));
        tableWidgetBitacora.setHorizontalHeaderItem(8, __colItem8);
        QTableWidgetItem __colItem9 = new QTableWidgetItem();
        __colItem9.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Dirección"));
        tableWidgetBitacora.setHorizontalHeaderItem(9, __colItem9);
        QTableWidgetItem __colItem10 = new QTableWidgetItem();
        __colItem10.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Destino"));
        tableWidgetBitacora.setHorizontalHeaderItem(10, __colItem10);
        QTableWidgetItem __colItem11 = new QTableWidgetItem();
        __colItem11.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Forma de Pago"));
        tableWidgetBitacora.setHorizontalHeaderItem(11, __colItem11);
        QTableWidgetItem __colItem12 = new QTableWidgetItem();
        __colItem12.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Factura"));
        tableWidgetBitacora.setHorizontalHeaderItem(12, __colItem12);
        QTableWidgetItem __colItem13 = new QTableWidgetItem();
        __colItem13.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Observaciones"));
        tableWidgetBitacora.setHorizontalHeaderItem(13, __colItem13);
        QTableWidgetItem __colItem14 = new QTableWidgetItem();
        __colItem14.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Telefonista"));
        tableWidgetBitacora.setHorizontalHeaderItem(14, __colItem14);
        QTableWidgetItem __colItem15 = new QTableWidgetItem();
        __colItem15.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Operadora"));
        tableWidgetBitacora.setHorizontalHeaderItem(15, __colItem15);
        QTableWidgetItem __colItem16 = new QTableWidgetItem();
        __colItem16.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Chofer"));
        tableWidgetBitacora.setHorizontalHeaderItem(16, __colItem16);
        tableWidgetBitacora.setRowCount(0);
        tableWidgetBitacora.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Esta es sólo una bitácora rápida de los servicios más próximos, para una vista completa, consulte la opción Búsqueda."));
        dockWidgetAgregarServicio.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Agregar Servicio"));
        label_2.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Teléfono:"));
        lineEditTelefono.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Ingrese el teléfono del cliente que solicita el servicio"));
        lineEditTelefono.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Presione ENTER para obtener la lista de clientes asociados a este teléfono."));
        lineEditTelefono.setWhatsThis(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Teléfono"));
        lineEditTelefono.setInputMask(com.trolltech.qt.core.QCoreApplication.translate("Principal", "99999999; "));
        lineEditTelefono.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "00000000"));
        label_13.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Cliente:"));
        comboBoxNombre.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Seleccione el cliente que solicita el servicio"));
        comboBoxNombre.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Seleccione un cliente para obtener las direcciones asociadas a dicho cliente."));
        label_4.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Origen:"));
        comboBoxOrigen.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Seleccione la dirección origen del cliente"));
        comboBoxOrigen.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Puede especificar una dirección temporal escribiendola directamente."));
        label_12.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Destino:"));
        comboBoxDestino.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Seleccione la dirección destino del cliente"));
        comboBoxDestino.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Puede especificar una dirección temporal escribiendola directamente"));
        label_10.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Observaciones:"));
        textEditObservaciones.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Especifique alguna anotación sobre el servicio si lo requiere"));
        textEditObservaciones.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Este campo es opcional."));
        label_11.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Tipo:"));
        comboBoxTipoServicio.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Seleccione a que tipo de servicio corresponde"));
        comboBoxTipoServicio.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Tome en cuenta la categoría y la subcategoría a la que pertenece el servicio."));
        checkBoxReservado.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Especifique si el servicio será agendado"));
        checkBoxReservado.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Los servicios reservados aparecerán en la bitácora 1 hora antes de su agenda."));
        checkBoxReservado.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Reservado"));
        label.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Forma de Pago:"));
        comboBoxFormaDePago.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Seleccione la forma de pago del cliente"));
        checkBoxFactura.setToolTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Especifique si el cliente solicitó factura del servicio"));
        checkBoxFactura.setStatusTip(com.trolltech.qt.core.QCoreApplication.translate("Principal", "La elaboración de la factura será en el módulo correspondiente."));
        checkBoxFactura.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Requiere Factura"));
        pushButtonAgregar.setText(com.trolltech.qt.core.QCoreApplication.translate("Principal", "Agregar"));
        menu_Archivo.setTitle(com.trolltech.qt.core.QCoreApplication.translate("Principal", "&Archivo"));
        menuEdici_n.setTitle(com.trolltech.qt.core.QCoreApplication.translate("Principal", "&Edición"));
        menuAcciones.setTitle(com.trolltech.qt.core.QCoreApplication.translate("Principal", "A&cciones"));
        menuVentana.setTitle(com.trolltech.qt.core.QCoreApplication.translate("Principal", "&Ventana"));
    }
}
