package net.sourceforge.arcamplayer.gui.visuals.components.selection.songs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import net.sourceforge.arcamplayer.gui.controllers.MainController;
import net.sourceforge.arcamplayer.library.collection.Song;
import net.sourceforge.arcamplayer.library.collection.SongInfo;
import net.sourceforge.arcamplayer.library.collection.StaticList;

/**
 * <p>Diálogo de selección de canciones del almacén.</p>
 * @author David Arranz Oveja, Pelayo Campa González-Nuevo
 */
public class SongsSelectionDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = -1840553824588294652L;

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * componente para listas.
	 */
    private JList jList;

    /**
	 * panel para deplazamiento.
	 */
    private JScrollPane jScroller;

    /**
	 * panel para la lista.
	 */
    private JPanel jPanelForList;

    /**
	 * botón de cancelar.
	 */
    private JButton jButtonCancel;

    /**
	 * botón de aceptar.
	 */
    private JButton jButtonOk;

    /**
	 * canciones seleccionadas.
	 */
    private ArrayList<Song> selectedSongs = new ArrayList<Song>();

    /**
	 * lista de canciones en el almacén.
	 */
    private List<Song> pool;

    /**
	 * referencia al controlador principal.
	 */
    private MainController controller;

    /**
	 * referencia a la lista estática.
	 */
    private StaticList list;

    /**
	 * <p>Constructor parametrizado. Recibe el JFrame propietario del diálogo y el controlador
	 * principal de la interfaz.</p>
	 * @param frame propiertario
	 * @param controller controlador principal.
	 */
    public SongsSelectionDialog(JFrame frame, MainController controller) {
        super(frame, true);
        this.controller = controller;
    }

    /**
	 * <p>Muestra el diálogo para una lista estática recibiendo también la lista de canciones 
	 * de almacén.</p>
	 * @param pool lista de canciones del almacén.
	 * @param list lista estática.
	 */
    public void showDialog(final List<Song> pool, StaticList list) {
        this.list = list;
        if ((this.pool = pool) == null) {
            dispose();
        }
        initGUI();
        setLocationRelativeTo(null);
        setEnabled(true);
        setVisible(true);
    }

    /**
	 * <p>Inicializa los componentes visuales y compone el diálogo.</p>
	 */
    private void initGUI() {
        try {
            {
                GridBagLayout thisLayout = new GridBagLayout();
                thisLayout.rowWeights = new double[] { 0.1, 0.0 };
                thisLayout.rowHeights = new int[] { 7, 7 };
                thisLayout.columnWeights = new double[] { 0.1, 0.0, 0.0 };
                thisLayout.columnWidths = new int[] { 7, 7, 7 };
                getContentPane().setLayout(thisLayout);
                this.setMaximumSize(new java.awt.Dimension(550, 450));
                this.setTitle("Selección de canciones");
                {
                    jButtonOk = new JButton();
                    getContentPane().add(jButtonOk, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 6, 6, 0), 0, 0));
                    jButtonOk.setText("Aceptar");
                    jButtonOk.addActionListener(new OkButtonActionListener());
                }
                {
                    jButtonCancel = new JButton();
                    getContentPane().add(jButtonCancel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 6, 6), 0, 0));
                    jButtonCancel.setText("Cancelar");
                    jButtonCancel.addActionListener(new CancelButtonActionListener());
                }
                {
                    jPanelForList = new JPanel();
                    BorderLayout jPanelForListLayout = new BorderLayout();
                    getContentPane().add(jPanelForList, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    jPanelForList.setBorder(BorderFactory.createTitledBorder("Almacén de la colección"));
                    jPanelForList.setLayout(jPanelForListLayout);
                    {
                        jScroller = new JScrollPane();
                        jPanelForList.add(jScroller, BorderLayout.CENTER);
                        {
                            ArrayList<String> strList = songsToStrings();
                            ListModel jList1Model = new DefaultComboBoxModel(strList.toArray());
                            jList = new JList();
                            jScroller.setViewportView(jList);
                            jList.setModel(jList1Model);
                        }
                    }
                }
            }
            pack();
            this.setSize(382, 245);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * <p>Genera una lista de cadenas que representarán a cada canción
	 * mostrando título, artista, album y año de la lista de canciones del 
	 * almacén.</p>
	 * @return lista de cadenas representando las canciones.
	 */
    private ArrayList<String> songsToStrings() {
        ArrayList<String> strList = new ArrayList<String>();
        for (int i = 0; i < pool.size(); i++) {
            SongInfo si = pool.get(i).getSongInfo();
            String s = si.getTitle() + " - " + si.getArtist() + "  [" + si.getAlbum() + "]  (" + si.getYear() + ")";
            strList.add(s);
        }
        return strList;
    }

    /**
	 * <p>Observador del botón cancelar.</p>
	 * @author David Arranz Oveja, Pelayo Campa González-Nuevo
	 */
    public class CancelButtonActionListener implements ActionListener {

        /**
		 * Método de notificación de la acción.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            setEnabled(false);
            dispose();
        }
    }

    /**
	 * <p>Observador del botón aceptar.</p>
	 * @author David Arranz Oveja, Pelayo Campa González-Nuevo
	 */
    public class OkButtonActionListener implements ActionListener {

        /**
		 * Médoto notificador de la acción.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] indices = jList.getSelectedIndices();
            selectedSongs = new ArrayList<Song>();
            for (int index : indices) {
                selectedSongs.add(pool.get(index));
            }
            controller.requestAddSongsToList(list, selectedSongs);
            dispose();
        }
    }
}
