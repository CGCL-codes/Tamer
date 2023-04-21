package org.gvsig.georeferencing.ui.launcher;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.gvsig.georeferencing.main.Georeferencing;
import org.gvsig.raster.util.RasterToolsUtil;
import com.iver.utiles.swing.JComboBox;

/**
 * Panel de selecci�n de tipo de georreferenciaci�n.
 * 
 * 10/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class TypeSelectionPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private String[] viewNameList = null;

    private JRadioButton withoutMap = null;

    private JRadioButton withMap = null;

    private JComboBox viewList = null;

    /**
	 * Constructor. Asigna la lista de nombres de vistas para el selector. 
	 * @param viewList
	 */
    public TypeSelectionPanel(String[] viewList) {
        this.viewNameList = viewList;
        init();
    }

    /**
	 * Acciones de inicializaci�n del panel
	 */
    public void init() {
        ButtonGroup group = new ButtonGroup();
        group.add(getWithoutMap());
        group.add(getWithMap());
        getWithoutMap().addActionListener(this);
        getWithMap().addActionListener(this);
        GridBagLayout gl = new GridBagLayout();
        setLayout(gl);
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "georef_type"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 8, 0);
        add(getWithoutMap(), gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 8, 0);
        add(getWithMap(), gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 35, 8, 0);
        add(getView(), gbc);
    }

    /**
	 * Obtiene el radio but�n de la selecci�n sin cartograf�a de referencia
	 * @return JRadioButton
	 */
    public JRadioButton getWithoutMap() {
        if (withoutMap == null) withoutMap = new JRadioButton(RasterToolsUtil.getText(this, "without_map"));
        return withoutMap;
    }

    /**
	 * Obtiene el radio but�n de la selecci�n con cartograf�a de referencia
	 * @return JRadioButton
	 */
    public JRadioButton getWithMap() {
        if (withMap == null) {
            withMap = new JRadioButton(RasterToolsUtil.getText(this, "with_map"));
            withMap.setSelected(true);
        }
        return withMap;
    }

    /**
	 * Obtiene el combo con la lista de vistas para la selecci�n de la que contiene
	 * la cartograf�a de referencia.
	 * @return JComboBox
	 */
    public JComboBox getView() {
        if (viewList == null) {
            viewList = new JComboBox();
            for (int i = 0; i < viewNameList.length; i++) viewList.addItem(viewNameList[i]);
        }
        return viewList;
    }

    /**
	 * Eventos de selecci�n en los RadioButton de selecci�n de Tipo
	 */
    public void actionPerformed(ActionEvent e) {
        getView().setEnabled(getWithMap().isSelected());
    }

    /**
	 * Obtiene el grado seleccionado
	 * @return valor del grado seleccionado
	 */
    public String getSelectedView() {
        if (viewList != null && getWithMap().isSelected()) return (String) viewList.getSelectedItem();
        return null;
    }

    /**
	 * Obtiene el tipo de georreferenciaci�n seleccionada
	 * @return constante definida en Georeferencing con el tipo de georreferenciaci�n seleccionada.
	 */
    public int getType() {
        if (getWithoutMap().isSelected()) return Georeferencing.WITHOUT_MAP;
        if (getWithMap().isSelected()) return Georeferencing.WITH_MAP;
        return Georeferencing.UNDEFINED;
    }
}
