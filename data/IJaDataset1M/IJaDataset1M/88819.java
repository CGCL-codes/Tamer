package org.gvsig.georeferencing.ui.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Clase para los selectores de color del cuadro de opciones.
 * 
 * 30/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ColorSelector extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JButton button = null;

    private JPanel colorPanel = null;

    private Color color = null;

    private String text = null;

    private JColorChooser colorChooser = null;

    private JDialog dialog = null;

    public ColorSelector(Color color, String text) {
        this.color = color;
        this.text = text;
        init();
    }

    /**
	 * Asigna el color
	 * @param c
	 */
    public void setColor(Color c) {
        this.color = c;
        if (colorPanel != null) colorPanel.setBackground(color);
        if (colorChooser != null) colorChooser.setColor(color);
    }

    /**
	 * Obtiene el color
	 * @return
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * Acciones de inicializaci�n del panel
	 */
    public void init() {
        GridBagLayout gl = new GridBagLayout();
        setLayout(gl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 4);
        add(getButton(), gbc);
        gbc.gridx = 1;
        add(getPanel(), gbc);
    }

    /**
	 * Obtiene el selector de color para el fondo
	 * @return JButton
	 */
    public JButton getButton() {
        if (button == null) {
            button = new JButton(text);
            button.addActionListener(this);
        }
        return button;
    }

    /**
	 * Obtiene el selector de color para el fondo
	 * @return JButton
	 */
    public JPanel getPanel() {
        if (colorPanel == null) {
            colorPanel = new JPanel();
            colorPanel.setPreferredSize(new Dimension(50, 20));
            colorPanel.setSize(new Dimension(50, 20));
            colorPanel.setBackground(color);
        }
        return colorPanel;
    }

    /**
	 * Eventos del bot�n y del dialogo de selecci�n de color
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getButton()) {
            if (colorChooser == null) colorChooser = new JColorChooser();
            dialog = JColorChooser.createDialog(button, RasterToolsUtil.getText(this, "select_color"), true, colorChooser, this, null);
            colorChooser.setColor(color);
            dialog.setVisible(true);
            return;
        }
        color = colorChooser.getColor();
        getPanel().setBackground(color);
    }
}
