package net.sourceforge.smartconversion.easymap.ui.config;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import net.sourceforge.smartconversion.api.port.Port;
import net.sourceforge.smartconversion.api.port.java.JavaObjectPort;
import net.sourceforge.smartconversion.easymap.ui.exception.NoPanelDefinedForPort;
import net.sourceforge.smartconversion.easymap.ui.portdefinition.JavaObjectPortDefinitionPanel;
import net.sourceforge.smartconversion.easymap.ui.portdefinition.JavaObjectPortDefinitionPanelController;
import net.sourceforge.smartconversion.easymap.ui.portdefinition.PortDefinitionPanel;

/**
 * Provides various standard configuration objects. 
 * 
 * @author Ovidiu Dolha
 */
public abstract class StandardConfiguration {

    /**
   * The standard easymap extensions {@link PortDefinitionPanelConfiguration} which defines
   * default {@link PortDefinitionPanel}s mapped to their {@link Port} type that they can create. 
   */
    public static final PortDefinitionPanelConfiguration STANDARD_PORT_DEFINITION_PANEL_CONFIGURATION = new PortDefinitionPanelConfiguration() {

        @Override
        public Set<Class<? extends Port>> getAvailablePortTypes() {
            Set<Class<? extends Port>> set = new HashSet<Class<? extends Port>>();
            set.add(JavaObjectPort.class);
            return set;
        }

        /**
     * {@inheritDoc}
     */
        @SuppressWarnings("unchecked")
        @Override
        public <T extends Port> PortDefinitionPanel<T> createPortDefinitionPanel(Class<T> portType, JFrame parentFrame) throws NoPanelDefinedForPort {
            if (JavaObjectPort.class.equals(portType)) {
                JavaObjectPortDefinitionPanel panel = new JavaObjectPortDefinitionPanel(parentFrame);
                new JavaObjectPortDefinitionPanelController(panel);
                return (PortDefinitionPanel<T>) panel;
            } else {
                throw new NoPanelDefinedForPort("No panel is configured for the given port type: " + portType);
            }
        }
    };
}
