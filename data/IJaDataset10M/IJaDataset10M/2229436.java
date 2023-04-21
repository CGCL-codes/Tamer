package org.gvsig.rastertools.vectorizacion.vector.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Panel con los controles de generaci�n de lineas de contorno.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ContourLinesPanel extends BasePanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private DataInputContainer distance = null;

    private boolean enabled = true;

    /**
	 *Inicializa componentes gr�ficos y traduce
	 */
    public ContourLinesPanel() {
        translate();
    }

    protected void init() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "contourlines"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridy = 1;
        add(getDistance(), gbc);
        setComponentEnabled(false);
    }

    protected void translate() {
    }

    /**
	 * Obtiene la barra deslizadora con el radio del filtro de paso alto
	 * @return CheckSliderTextContainer
	 */
    public DataInputContainer getDistance() {
        if (distance == null) {
            distance = new DataInputContainer();
            distance.setLabelText(RasterToolsUtil.getText(this, "distance"));
        }
        return distance;
    }

    /**
	 * Gesti�n del evento del check de activaci�n y desactivaci�n
	 */
    public void actionPerformed(ActionEvent e) {
        setComponentEnabled(enabled);
    }

    /**
	 * Activa o desactiva el componente. El estado de activaci�n y desactivaci�n de un
	 * componente depende de los controles que contiene. En este caso activa o desactiva
	 * la barra de incremento.
	 * @param enabled
	 */
    public void setComponentEnabled(boolean enabled) {
        getDistance().setControlEnabled(enabled);
        this.enabled = !enabled;
    }
}
