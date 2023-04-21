package com.proyecto.tropero.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.hibernate.criterion.Criterion;
import com.proyecto.tropero.core.application.FuncionesGenericas;
import com.proyecto.tropero.core.application.VerificaCuadroTextoLonguitud;
import com.proyecto.tropero.core.domain.Animal;
import com.proyecto.tropero.core.domain.GrupoAnimal;
import com.proyecto.tropero.core.presenter.ResourceMap;
import com.proyecto.tropero.core.presenter.VentanaGruposAnimalesPresenter;
import com.proyecto.tropero.core.service.model.ServiceLocator;
import com.proyecto.tropero.gui.interfaces.IVentanaGruposAnimales;

/**
 * Esta clase es la encargada de dibujar la ventana de 
 * 
 */
public class VentanaGruposAnimales extends VentanaFunciones implements IVentanaGruposAnimales {

    private static final long serialVersionUID = 1L;

    private javax.swing.JButton jButton13;

    private javax.swing.JButton jButton14;

    private javax.swing.JButton jButton15;

    private javax.swing.JButton jButton16;

    private javax.swing.JButton jButton17;

    private javax.swing.JButton jButton18;

    private javax.swing.JButton jButton19;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JList jList1;

    private javax.swing.JList jList2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private VentanaBusquedaAnimal jPanelBusqueda;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JSpinner jSpinner1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTable jTable2;

    private javax.swing.JTable jTable3;

    private javax.swing.JTextField jTextField6;

    private javax.swing.JButton jButton20;

    private List<GrupoAnimal> listGruposAnimales = new ArrayList<GrupoAnimal>();

    private GrupoAnimal grupoSeleccionado = new GrupoAnimal();

    private VentanaGruposAnimalesPresenter presenter = new VentanaGruposAnimalesPresenter();

    ;

    private ResourceMap resourceMap = new ResourceMap("VentanaGruposAnimales");

    private static VentanaGruposAnimales instance;

    public static VentanaGruposAnimales getInstance() {
        if (null == instance) {
            instance = new VentanaGruposAnimales();
        }
        return instance;
    }

    public static boolean existeInstancia() {
        if (null == instance) {
            return false;
        }
        return true;
    }

    public VentanaGruposAnimales() {
        presenter.setIVentanaGruposAnimales(this);
        presenter.setService(ServiceLocator.getGrupoAnimalService());
        initComponentes();
        initEvents();
        return;
    }

    private void initComponentes() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jSpinner1 = new javax.swing.JSpinner();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        DefaultListModel model = new DefaultListModel();
        jList2.setModel(model);
        jPanelBusqueda = new VentanaBusquedaAnimal(model);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Grupos Existentes"));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Descripci�n", "Cantidad M�xima" }));
        jScrollPane5.setViewportView(jTable2);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE).addContainerGap()));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Grupo"));
        jLabel11.setText("Nombre del Grupo");
        jLabel12.setText("Cantidad M�xima:");
        jTable3.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Nombre", "ID SENASA" }));
        jScrollPane2.setViewportView(jTable3);
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Integrantes en Grupo");
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11).addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE).addContainerGap()));
        jButton15.setIcon(resourceMap.getIcon("jButton15.icon"));
        jButton15.setText("Asignar");
        jButton14.setIcon(resourceMap.getIcon("jButton14.icon"));
        jButton14.setText("Nuevo");
        jButton13.setIcon(resourceMap.getIcon("jButton13.icon"));
        jButton13.setText("Salir");
        jButton18.setIcon(resourceMap.getIcon("jButton18.icon"));
        jButton18.setText("Grabar");
        jButton20.setIcon(resourceMap.getIcon("jButton20.icon"));
        jButton20.setText("Borrar");
        jTable3.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Nombre", "ID SENASA" }));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE).addComponent(jButton13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE).addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE).addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE).addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)).addContainerGap()))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jButton15).addGap(18, 18, 18).addComponent(jButton14).addGap(18, 18, 18).addComponent(jButton18).addGap(18, 18, 18).addComponent(jButton20).addGap(18, 18, 18).addComponent(jButton13)).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jTabbedPane1.addTab("Crear/Modificar", jPanel1);
        jButton16.setIcon(resourceMap.getIcon("jButton16.icon"));
        jButton16.setText("Anterior");
        jButton17.setIcon(resourceMap.getIcon("jButton17.icon"));
        jButton17.setText("Quitar");
        jScrollPane1.setViewportView(jList1);
        jScrollPane3.setViewportView(jList2);
        jButton2.setIcon(resourceMap.getIcon("jButton2.icon"));
        jButton3.setIcon(resourceMap.getIcon("jButton3.icon"));
        jButton19.setIcon(resourceMap.getIcon("jButton9.icon"));
        jButton19.setText("Aplicar");
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("INTEGRANTES EN GRUPO");
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ANIMALES SELECCIONADOS");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanelBusqueda, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jButton3, 0, 0, Short.MAX_VALUE).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE).addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jButton16, jButton17, jButton19 });
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jPanelBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(23, 23, 23).addComponent(jLabel3)).addGroup(jPanel2Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jLabel2))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3).addGap(62, 62, 62))).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton16).addComponent(jButton17).addComponent(jButton19)).addContainerGap()));
        jTabbedPane1.addTab("Asignar a Grupo", jPanel2);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE).addContainerGap()));
        cargarMoldeloTablaGrupos();
        jTabbedPane1.setEnabled(false);
        jButton15.setEnabled(false);
        jSpinner1.setModel(getModeloSpineer());
    }

    private SpinnerModel getModeloSpineer() {
        SpinnerNumberModel model = new SpinnerNumberModel(new Integer(0), new Integer(0), new Integer(99999), new Integer(1));
        return model;
    }

    private void initEvents() {
        jTextField6.setInputVerifier(new VerificaCuadroTextoLonguitud(30));
        jTable2.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                int rowSelect = jTable2.getSelectedRow();
                grupoSeleccionado = listGruposAnimales.get(rowSelect);
                actualizarDatosEnPantalla();
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
        jButton14.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                limpiaPantalla();
            }
        });
        jButton16.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jTabbedPane1.setSelectedIndex(0);
            }
        });
        jButton15.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mostrarPantallaAsigancionGrupo();
            }
        });
        jButton17.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                quitarAnimalesDeLista();
            }
        });
        jButton18.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                grabarModificarGrupo();
            }
        });
        jButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                quitarAnimalesDeGrupo();
            }
        });
        jButton3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                agregarAnimalesAGrupo();
            }
        });
        jButton19.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                grabarModificarGrupo();
            }
        });
        jButton20.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                borrarGrupo();
            }
        });
    }

    protected void borrarGrupo() {
        if (grupoSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un grupo a borrar", "Grupo Animal");
            return;
        }
        try {
            presenter.borrarGrupoAnimal();
            mostrarMensajeExito("El grupo fue borrado exitosamente", "Grupo Animal");
        } catch (SQLException e) {
            mostrarMensajeError(FuncionesGenericas.obtenerMensajeError(e), "Grupo Animal");
        } catch (Exception e) {
            mostrarMensajeError("Error borrando el grupo", "Grupo Animal");
        } finally {
            reiniciarPantalla();
        }
    }

    protected void agregarAnimalesAGrupo() {
        List<Animal> listAnimalesAAgregar = jPanelBusqueda.getListAnimalesSeleccionados();
        List<Animal> listAnimalesOK = new ArrayList<Animal>();
        List<Animal> listAnimalesNOOK = new ArrayList<Animal>();
        if ((listAnimalesAAgregar.size() + grupoSeleccionado.getAnimales().size()) > grupoSeleccionado.getCantMaxAnimales().intValue()) {
            mostrarMensajeError("El grupo tiene asociado " + grupoSeleccionado.getAnimales().size() + " animales. " + "Usted quiere agregar " + listAnimalesAAgregar.size() + ". Dicho n�mero supera al m�ximo permitido de " + +grupoSeleccionado.getCantMaxAnimales() + " animales.", "Grupo Animal");
            return;
        }
        for (int i = 0; i < listAnimalesAAgregar.size(); i++) {
            Animal animalSeleccionado = listAnimalesAAgregar.get(i);
            if (animalSeleccionado.getGrupo() == null) {
                grupoSeleccionado.getAnimales().add(animalSeleccionado);
                listAnimalesOK.add(animalSeleccionado);
            } else {
                int res = JOptionPane.showConfirmDialog(this, "El animal " + animalSeleccionado.getNombre() + " est� asociado al grupo " + animalSeleccionado.getGrupo().getDe_GrupoAnimal() + ", desea asociarlo igual al grupo" + grupoSeleccionado.getDe_GrupoAnimal() + "?", "Mensaje de Confirmaci�n", JOptionPane.YES_NO_CANCEL_OPTION);
                if (res == 0) {
                    grupoSeleccionado.getAnimales().add(animalSeleccionado);
                    listAnimalesOK.add(animalSeleccionado);
                } else {
                    listAnimalesNOOK.add(animalSeleccionado);
                }
            }
        }
        jPanelBusqueda.quitarAnimalesDeLista(listAnimalesNOOK);
        cargarListaAnimalesDentro();
        jPanelBusqueda.quitarAnimalesDeLista(listAnimalesOK);
    }

    protected void quitarAnimalesDeGrupo() {
        List<Animal> animalesSelecionados = new ArrayList<Animal>();
        if (jList1.getSelectedIndex() == -1) {
            mostrarMensajeWarning("Debe seleccionar un animal para quitar del grupo", "Grupo Animal");
            return;
        }
        for (int i = 0; i < jList1.getSelectedIndices().length; i++) {
            animalesSelecionados.add(grupoSeleccionado.getAnimales().get(jList1.getSelectedIndices()[i]));
        }
        grupoSeleccionado.getAnimales().removeAll(animalesSelecionados);
        cargarListaAnimalesDentro();
        jPanelBusqueda.agregarAnimalesALista(animalesSelecionados);
    }

    private void cargarMoldeloTablaGrupos() {
        presenter.cargarGruposAnimales();
        String[] nombreColumnas = { "Descripci�n", "Cantidad M�xima" };
        String[] nombreMetodos = { "getDe_GrupoAnimal", "getCantMaxAnimales" };
        boolean[] listEditables = { false, false };
        genericCargarTablaEditable(this.listGruposAnimales, GrupoAnimal.class.getName(), nombreMetodos, nombreColumnas, jTable2, listEditables);
    }

    protected void grabarModificarGrupo() {
        if (jTextField6.getText().isEmpty()) {
            mostrarMensajeError("Debe ingresar una descripci�n para el grupo", "Grupo Animal");
            return;
        }
        if (grupoSeleccionado == null) {
            grupoSeleccionado = new GrupoAnimal();
            grupoSeleccionado.setId(null);
        }
        grupoSeleccionado.setDe_GrupoAnimal(jTextField6.getText().toString());
        grupoSeleccionado.setCantMaxAnimales(new Long(jSpinner1.getModel().getValue().toString()));
        try {
            presenter.grabarGrupo();
            mostrarMensajeExito("Grupo grabado exitosamente", "Grupo Animal");
            reiniciarPantalla();
        } catch (SQLException e) {
            mostrarMensajeError(FuncionesGenericas.obtenerMensajeError(e), "Grupo Animal");
        } catch (Exception e) {
            mostrarMensajeError("Error grabando el grupo", "Grupo Animal");
        } finally {
            cargarMoldeloTablaGrupos();
            jPanelBusqueda.reiniciaListas();
        }
    }

    private void reiniciarPantalla() {
        limpiaPantalla();
        jTabbedPane1.setSelectedIndex(0);
    }

    protected void quitarAnimalesDeLista() {
        if (jList2.getSelectedIndex() != -1) {
            jPanelBusqueda.quitarAnimalSeleccionado(jList2.getSelectedIndices(), jList2.getSelectedIndices().length);
        }
    }

    protected void mostrarPantallaAsigancionGrupo() {
        if (grupoSeleccionado == null) {
            mostrarMensajeError("Debe seleccionar un grupo", "Grupo Animal");
            return;
        }
        jTabbedPane1.setSelectedIndex(1);
        cargarPantallaAsigancionGrupo();
    }

    private void cargarPantallaAsigancionGrupo() {
        cargarListaAnimalesDentro();
        jPanelBusqueda.quitarAnimalesDeLista(grupoSeleccionado.getAnimales());
    }

    private void cargarListaAnimalesDentro() {
        final String[] ganadosDentro = new String[grupoSeleccionado.getAnimales().size()];
        for (int i = 0; i < grupoSeleccionado.getAnimales().size(); i++) {
            ganadosDentro[i] = grupoSeleccionado.getAnimales().get(i).getNombre() + " - " + grupoSeleccionado.getAnimales().get(i).getIdSenasa();
        }
        jList1.setModel(new javax.swing.AbstractListModel() {

            private static final long serialVersionUID = 1L;

            String[] strings = ganadosDentro;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    protected void limpiaPantalla() {
        cargarTablaAnimalesDentro();
        jTextField6.setText(null);
        jTextField6.requestFocus();
        jSpinner1.setValue(0);
        cambiarTextBoton("Grabar");
        grupoSeleccionado = null;
        inicializaTablas();
        jButton15.setEnabled(false);
    }

    private void inicializaTablas() {
        String[] nombreColumnasTabla1 = { "Nombre", "ID SENASA" };
        genericInicializarTabla(nombreColumnasTabla1, jTable3);
        String[] nombreColumnas = { "Descripci�n", "Cantidad M�xima" };
        String[] nombreMetodos = { "getDe_GrupoAnimal", "getCantMaxAnimales" };
        boolean[] listEditables = { false, false };
        genericCargarTablaEditable(this.listGruposAnimales, GrupoAnimal.class.getName(), nombreMetodos, nombreColumnas, jTable2, listEditables);
    }

    protected void actualizarDatosEnPantalla() {
        jTextField6.setText(grupoSeleccionado.getDe_GrupoAnimal());
        jSpinner1.setValue(grupoSeleccionado.getCantMaxAnimales());
        cambiarTextBoton("Modificar");
        cargarTablaAnimalesDentro();
        jButton15.setEnabled(true);
    }

    private void cargarTablaAnimalesDentro() {
        presenter.cargarGruposAnimales();
        String[] nombreColumnas = { "Nombre", "ID SENASA" };
        String[] nombreMetodos = { "getNombre", "getIdSenasa" };
        boolean[] listEditables = { false, false };
        genericCargarTablaEditable(grupoSeleccionado.getAnimales(), Animal.class.getName(), nombreMetodos, nombreColumnas, jTable3, listEditables);
        jTable3.setRowSelectionAllowed(false);
    }

    private void cambiarTextBoton(String txt) {
        jButton18.setText(txt);
    }

    protected void inicializaListas(JList lista) {
        lista.setModel(new javax.swing.AbstractListModel() {

            private static final long serialVersionUID = 1L;

            public int getSize() {
                return 0;
            }

            public Object getElementAt(int i) {
                return null;
            }
        });
    }

    public List<GrupoAnimal> getListGruposAnimales() {
        return listGruposAnimales;
    }

    public void setListGruposAnimales(List<GrupoAnimal> listGruposAnimales) {
        this.listGruposAnimales = listGruposAnimales;
    }

    public void setActionListenerClose(ActionListener actionListener) {
        jButton13.addActionListener(actionListener);
    }

    public GrupoAnimal getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    @Override
    public Criterion getFiltro() {
        return null;
    }

    public Criterion getFiltroIn() {
        return null;
    }

    @Override
    public void setListDentro(List<Animal> listGanados) {
    }

    @Override
    public void setListaAfuera(List<Animal> listaAnimalesAfuera) {
    }

    public static void reiniciarInstancia() {
        VentanaGruposAnimales.instance = null;
    }

    public void initParaRFID(List<Animal> animalesSelected) {
        jPanelBusqueda.agregarAnimalesALista(animalesSelected);
    }
}
