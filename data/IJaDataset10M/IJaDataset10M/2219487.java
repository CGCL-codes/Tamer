package es.aeat.eett.workbench.toolBarViews;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import es.aeat.eett.workbench.core.CorePlugin;
import es.aeat.eett.workbench.core.WorkbenchEvent;
import es.aeat.eett.workbench.core.WorkbenchListener;
import es.aeat.eett.workbench.core.FocusEvent;

/**
 * JToggleButton que escucha FocusEvent events.
 * Comportamientos si llega un FocusEvent informando que se ha mostarado
 * un componente el boton comprueba si es componente mostrado corresponde al
 * gestionado por el. En caso afirmativo selecciona el boton.
 * 
 * @author f00992
 */
public class FocusButton extends JToggleButton implements WorkbenchListener {

    private static final long serialVersionUID = 896356566545453774L;

    /**
	 * @param icon
	 */
    public FocusButton(Icon icon) {
        super(icon);
    }

    /**
	 * @param icon
	 * @param selected
	 */
    public FocusButton(Icon icon, boolean selected) {
        super(icon, selected);
    }

    /**
	 * Obtiene el id del plugin gestionado por este botton.
	 * @return
	 */
    String getPluginId() {
        String atCommand = getModel().getActionCommand();
        int posIni = atCommand.indexOf(' ');
        if (posIni < 0) {
            posIni = 0;
        }
        return atCommand.substring(posIni).trim();
    }

    /**
     * Si FocusEvent informa que se muestra el componente (plugin)
     * gestionado por este boton cambia su estado a this.setSelected(true)
     * @param event
     */
    private void updateSelection(FocusEvent event) {
        if (event != null && event.getActiveComponent() != null) {
            JComponent c = event.getActiveComponent();
            String pluginID = (String) c.getClientProperty(CorePlugin.KEY_PROPERTY_UNIQUE_ID);
            if (pluginID != null && pluginID.equals(getPluginId())) {
                if (!isSelected()) setSelected(true);
            }
        }
    }

    public void viewFocusChanged(FocusEvent event) {
        updateSelection(event);
    }

    public void viewStructureChanged(WorkbenchEvent wEvent) {
    }
}
