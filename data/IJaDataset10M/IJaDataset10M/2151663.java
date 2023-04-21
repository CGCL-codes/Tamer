package org.gvsig.rastertools.properties.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.gvsig.raster.datastruct.TransparencyRange;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.properties.control.TranspByPixelEventListener;
import org.gvsig.rastertools.properties.control.TranspByPixelListener;
import com.iver.andami.PluginServices;

/**
 * Panel con los controles para la transparencia por pixel. Incluye los textbox
 * para a�adir un RGB a la lista.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TranspByPixelPanel extends JPanel {

    private static final long serialVersionUID = -1131297200332579683L;

    private boolean controlEnabled = false;

    private JCheckBox activeCheck = null;

    private JList jList = null;

    private JPanel pixelSelector = null;

    private JPanel activeControl = null;

    private JPanel pEast = null;

    private JPanel pCenter = null;

    private JPanel pButtons = null;

    private JButton bAdd = null;

    private JButton bRemove = null;

    private JButton bSelectColor = null;

    private JScrollPane jScrollList = null;

    private TranspByPixelRGBInputPanel pWest = null;

    private TranspByPixelAndOrSelectorPanel pOperation = null;

    private TranspByPixelListener listener = null;

    private ArrayList actionCommandListeners = new ArrayList();

    /**
	 * This is the default constructor
	 */
    public TranspByPixelPanel() {
        initialize();
        getListener();
        setControlEnabled(false);
    }

    /**
	 * This method initializes this
	 */
    private void initialize() {
        setLayout(new BorderLayout());
        add(getActiveControl(), BorderLayout.NORTH);
        add(getPixelSelector(), BorderLayout.CENTER);
    }

    /**
	 * Obtiene el panel con los controles principales para introducir valores RGB
	 * en la lista
	 * @return JPanel
	 */
    private JPanel getPixelSelector() {
        if (pixelSelector == null) {
            pixelSelector = new JPanel();
            pixelSelector.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 11, 5, 11));
            pixelSelector.setLayout(new BorderLayout(5, 5));
            JPanel jpanel1 = new JPanel();
            jpanel1.setLayout(new BorderLayout(5, 5));
            jpanel1.add(getPRGBInput(), BorderLayout.CENTER);
            jpanel1.add(getPCenter(), BorderLayout.EAST);
            pixelSelector.add(jpanel1, BorderLayout.CENTER);
            pixelSelector.add(getPList(), BorderLayout.EAST);
        }
        return pixelSelector;
    }

    /**
	 * Obtiene el panel con el control de activaci�n
	 * @return JPanel
	 */
    private JPanel getActiveControl() {
        if (activeControl == null) {
            activeControl = new JPanel();
            FlowLayout fl = new FlowLayout();
            fl.setAlignment(FlowLayout.LEFT);
            activeControl.setLayout(fl);
            activeControl.add(getActiveCheck());
        }
        return activeControl;
    }

    /**
	 * Obtiene el check que activa y desactiva la transparencia por pixel.
	 * @return JCheckBox
	 */
    public JCheckBox getActiveCheck() {
        if (activeCheck == null) {
            activeCheck = new JCheckBox(PluginServices.getText(this, "activar"));
            activeCheck.setSelected(false);
        }
        return activeCheck;
    }

    /**
	 * This method initializes jPanel
	 * @return
	 */
    public TranspByPixelRGBInputPanel getPRGBInput() {
        if (pWest == null) {
            pWest = new TranspByPixelRGBInputPanel();
        }
        return pWest;
    }

    /**
	 * This method initializes jPanel1
	 * @return javax.swing.JPanel
	 */
    public JPanel getPList() {
        if (pEast == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.weighty = 1.0;
            gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 0);
            pEast = new JPanel();
            pEast.setLayout(new GridBagLayout());
            pEast.add(getJScrollList(), gridBagConstraints3);
        }
        return pEast;
    }

    /**
	 * This method initializes pCenter
	 * @return javax.swing.JPanel
	 */
    private JPanel getPCenter() {
        if (pCenter == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.gridx = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 0;
            pCenter = new JPanel();
            pCenter.setLayout(new GridBagLayout());
            pCenter.add(getPButtons(), gridBagConstraints);
            pCenter.add(getPOperation(), gridBagConstraints1);
        }
        return pCenter;
    }

    /**
	 * Obtiene el bot�n de a�adir elemento de la lista en la transparencia
	 * por pixel.
	 *
	 * @return javax.swing.JButton
	 */
    public JButton getBAdd() {
        if (bAdd == null) {
            bAdd = new JButton(PluginServices.getText(this, "Anadir"));
            bAdd.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
            bAdd.setIcon(RasterToolsUtil.getIcon("forward-icon"));
        }
        return bAdd;
    }

    /**
	 * Obtiene el bot�n de eliminar elemento de la lista en la transparencia
	 * por pixel.
	 * @return javax.swing.JButton
	 */
    public JButton getBRemove() {
        if (bRemove == null) {
            bRemove = new JButton(PluginServices.getText(this, "Quitar"));
            bRemove.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            bRemove.setIcon(RasterToolsUtil.getIcon("backward-icon"));
            bRemove.setEnabled(false);
        }
        return bRemove;
    }

    /**
	 * Obtiene el bot�n de color.
	 * @return JButton
	 */
    public JButton getBSelectColor() {
        if (bSelectColor == null) {
            bSelectColor = new JButton();
            bSelectColor.setIcon(RasterToolsUtil.getIcon("selectrgb-icon"));
            bSelectColor.setPreferredSize(new Dimension(26, 26));
            bSelectColor.setToolTipText(RasterToolsUtil.getText(this, "select_rgb"));
        }
        return bSelectColor;
    }

    /**
	 * Obtiene el panel con los botones
	 * @return javax.swing.JPanel
	 */
    private JPanel getPButtons() {
        if (pButtons == null) {
            pButtons = new JPanel();
            pButtons.setLayout(new java.awt.GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            pButtons.add(getBAdd(), gridBagConstraints);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            pButtons.add(getBRemove(), gridBagConstraints);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridx = 0;
            pButtons.add(getBSelectColor(), gridBagConstraints);
        }
        return pButtons;
    }

    /**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
    public TranspByPixelAndOrSelectorPanel getPOperation() {
        if (pOperation == null) {
            pOperation = new TranspByPixelAndOrSelectorPanel();
            pOperation.setPreferredSize(new java.awt.Dimension(60, 60));
        }
        return pOperation;
    }

    /**
	 * This method initializes jList
	 *
	 * @return javax.swing.JList
	 */
    public JList getJList() {
        if (jList == null) {
            jList = new JList(new DefaultListModel());
            jList.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10));
            jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return jList;
    }

    /**
	 * Inicializa el scroll para el jList
	 * @return
	 */
    public JScrollPane getJScrollList() {
        if (jScrollList == null) {
            jScrollList = new JScrollPane();
            jScrollList.setViewportView(getJList());
            jScrollList.setPreferredSize(new java.awt.Dimension(160, 100));
        }
        return jScrollList;
    }

    /**
	 * Obtiene el ListModel de la lista
	 * @return DefaultListModel
	 */
    public DefaultListModel getListModel() {
        return (DefaultListModel) getJList().getModel();
    }

    /**
	 * Activa o desactiva el control
	 * @param enable True activa el control y false lo desactiva
	 */
    public void setControlEnabled(boolean enabled) {
        this.getActiveCheck().setSelected(enabled);
        getJList().setEnabled(enabled);
        if (enabled) getJList().setBackground(Color.WHITE); else getJList().setBackground(this.getBackground());
        getPRGBInput().setControlEnabled(enabled);
        getBAdd().setEnabled(enabled);
        getBSelectColor().setEnabled(enabled);
        if (enabled) getBRemove().setEnabled(getJList().getModel().getSize() > 0); else getBRemove().setEnabled(false);
        getPOperation().setControlEnabled(enabled);
        controlEnabled = enabled;
    }

    /**
	 * Obtiene true si el control est� activo y false si no lo est�
	 * @return
	 */
    public boolean isControlEnabled() {
        return controlEnabled;
    }

    /**
	 * Asigna el n�mero de bandas activas
	 * @param n N�mero de bandas
	 */
    public void setActiveBands(int n) {
        ((TranspByPixelRGBInputPanel) getPRGBInput()).setActiveBands(n);
    }

    /**
	 * Obtiene el array de entradas de valores a�adidos a la lista
	 * @return ArrayList
	 */
    public ArrayList getEntries() {
        return getListener().getEntries();
    }

    /**
	 * Obtiene la clase manejadora de eventos asociada a esta
	 * @return TransparencyByPixelListener
	 */
    public TranspByPixelListener getListener() {
        if (listener == null) {
            listener = new TranspByPixelListener(this);
        }
        return listener;
    }

    /**
	 * A�ade una entrada a la tabla
	 * @param entry objeto TransparencyRange
	 */
    public void addStringElement(TransparencyRange entry) {
        getEntries().add(entry);
        getListModel().addElement(entry.getStrEntry());
        getBRemove().setEnabled(true);
    }

    /**
	 * Borra las entradas de la tabla
	 */
    public void clear() {
        getPRGBInput().clear();
        getListModel().clear();
        getEntries().clear();
        getBRemove().setEnabled(false);
    }

    /**
	 * A�adir un listener a la lista de eventos
	 * @param listener
	 */
    public void addValueChangedListener(TranspByPixelEventListener listener) {
        if (!actionCommandListeners.contains(listener)) actionCommandListeners.add(listener);
    }

    /**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
    public void removeValueChangedListener(TranspByPixelEventListener listener) {
        actionCommandListeners.remove(listener);
    }

    /**
	 * Dispara todos los manejadores que estan apuntando a este evento
	 */
    public void callValueChanged() {
        Iterator acIterator = actionCommandListeners.iterator();
        while (acIterator.hasNext()) {
            TranspByPixelEventListener listener = (TranspByPixelEventListener) acIterator.next();
            listener.actionPixelListChanged(new EventObject(this));
        }
    }
}
