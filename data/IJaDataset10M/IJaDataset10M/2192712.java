package vista;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import businessDelegate.CasaCentralDelegate;
import vo.PaisVo;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JSelectorPaises extends javax.swing.JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3867833938707163834L;

    private JScrollPane jScrTabPanel;

    private JTable jTablaLista;

    private static JSelectorPaises singleInstance;

    private String[] nombresColumnas = { "C�digo", "Nombre" };

    private String frmCaption = "Selecci�n de Paises";

    private JButton btnCancelar;

    private JButton btnSeleccionar;

    private JTextField txtSeleccion;

    private JTextField txtDescripcion;

    private CasaCentralDelegate bss = null;

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSelectorPaises(JTextField txtSeleccion, JTextField txtDescripcion) {
        super();
        this.txtSeleccion = txtSeleccion;
        this.txtDescripcion = txtDescripcion;
        instanciarDelegate();
        initGUI();
    }

    private void instanciarDelegate() {
        if (this.bss == null) {
            try {
                this.bss = CasaCentralDelegate.getInstancia();
            } catch (Exception e) {
                System.out.println("No pude instanciar el DELEGATE!!!");
            }
        }
    }

    private JSelectorPaises() {
        super();
        initGUI();
        instanciarDelegate();
    }

    public static JSelectorPaises getInstance(JTextField txtSeleccion, JTextField txtDescripcion) {
        if (singleInstance == null) {
            singleInstance = new JSelectorPaises(txtSeleccion, txtDescripcion);
        }
        return singleInstance;
    }

    public static JSelectorPaises getInstance() {
        if (singleInstance == null) {
            singleInstance = new JSelectorPaises();
        }
        return singleInstance;
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension ventana = this.getSize();
            this.setLocation((pantalla.width - ventana.width) / 4, (pantalla.height - ventana.height) / 4);
            getContentPane().setLayout(null);
            this.setTitle(frmCaption);
            this.setSize(300, 380);
            this.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    singleInstance = null;
                }
            });
            {
                jScrTabPanel = new JScrollPane();
                getContentPane().add(jScrTabPanel);
                getContentPane().add(getBtnSeleccionar());
                getContentPane().add(getBtnCancelar());
                jScrTabPanel.setBounds(10, 11, 270, 301);
                {
                    TableModel jTablaListaModel = new AbstractTableModel(new String[][] { { "", "" }, { "", "" } }, nombresColumnas);
                    jTablaLista = new JTable();
                    jScrTabPanel.setViewportView(jTablaLista);
                    jTablaLista.setPreferredSize(new java.awt.Dimension(251, 301));
                    jTablaLista.setModel(jTablaListaModel);
                    jTablaLista.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent e) {
                        }
                    });
                }
            }
            cargarTabla(bss.getListaPaises());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarTabla(List<PaisVo> lista) {
        String[] datos = new String[2];
        PaisVo unDato = null;
        int fila;
        DefaultTableModel jTablaListaModel = new AbstractTableModel();
        jTablaListaModel.addColumn(nombresColumnas[0]);
        jTablaListaModel.addColumn(nombresColumnas[1]);
        for (fila = 0; fila < lista.size(); fila++) {
            unDato = lista.get(fila);
            datos[0] = String.valueOf(unDato.getIdPais());
            datos[1] = unDato.getNombre();
            jTablaListaModel.addRow(datos);
        }
        jTablaLista.setModel(jTablaListaModel);
        TableRowSorter<TableModel> trs = new TableRowSorter<TableModel>(jTablaListaModel);
        jTablaLista.setRowSorter(trs);
        jTablaLista.changeSelection(fila - 1, 0, false, false);
    }

    private JButton getBtnSeleccionar() {
        if (btnSeleccionar == null) {
            btnSeleccionar = new JButton();
            btnSeleccionar.setText("Seleccionar");
            btnSeleccionar.setBounds(193, 313, 87, 23);
            btnSeleccionar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int row = jTablaLista.getSelectedRow();
                    txtSeleccion.setText(jTablaLista.getValueAt(row, 0).toString());
                    txtDescripcion.setText(jTablaLista.getValueAt(row, 1).toString());
                    JSelectorPaises.getInstance().setVisible(false);
                    singleInstance = null;
                }
            });
        }
        return btnSeleccionar;
    }

    private JButton getBtnCancelar() {
        if (btnCancelar == null) {
            btnCancelar = new JButton();
            btnCancelar.setText("Cancelar");
            btnCancelar.setBounds(106, 313, 87, 23);
            btnCancelar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    JSelectorPaises.getInstance().setVisible(false);
                    singleInstance = null;
                }
            });
        }
        return btnCancelar;
    }
}
