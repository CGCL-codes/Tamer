package net.pandoragames.far.ui.swing.component.listener;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import net.pandoragames.far.ui.model.OperationType;
import net.pandoragames.far.ui.model.ProgressListener;
import net.pandoragames.far.ui.model.Resetable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the {@link net.pandoragames.far.ui.model.ProgressListener ProgressListener}
 * inteface for the file search. Allows other components to register in 
 * order to get informed about the state of currently running searches.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class OperationCallBackListener implements ProgressListener {

    private enum ABILITY {

        ENABLE, DISABLE, RESET
    }

    private Log logger;

    private List<ComponentContainer> startComponents = new ArrayList<ComponentContainer>();

    private List<ComponentContainer> endComponents = new ArrayList<ComponentContainer>();

    /**
	 * Default constructor.
	 */
    public OperationCallBackListener() {
        logger = LogFactory.getLog(this.getClass());
    }

    /**
	 * {@inheritDoc}
	 */
    public void operationStarted(OperationType type) {
        for (ComponentContainer comp : startComponents) {
            if ((comp.type == type) || (comp.type == OperationType.ANY)) {
                try {
                    SwingUtilities.invokeAndWait(new EnDisableComponent(comp.component, comp.endisFlag));
                } catch (Exception itx) {
                    logger.error(itx.getClass().getName() + " notifying " + comp.component.getName() + " (" + comp.component.getClass().getName() + "): " + itx.getMessage(), itx);
                }
            }
        }
    }

    /**
	 * This method does nothing
	 */
    public void operationProgressed(int count, int total, OperationType type) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void operationTerminated(OperationType type) {
        for (ComponentContainer comp : endComponents) {
            if ((comp.type == type) || (comp.type == OperationType.ANY)) {
                SwingUtilities.invokeLater(new EnDisableComponent(comp.component, comp.endisFlag));
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void operationAborted(OperationType type) {
        for (ComponentContainer comp : startComponents) {
            if ((comp.type == type) || (comp.type == OperationType.ANY)) {
                if (comp.endisFlag == ABILITY.ENABLE) {
                    SwingUtilities.invokeLater(new EnDisableComponent(comp.component, ABILITY.DISABLE));
                } else if (comp.endisFlag == ABILITY.DISABLE) {
                    SwingUtilities.invokeLater(new EnDisableComponent(comp.component, ABILITY.ENABLE));
                }
            }
        }
    }

    /**
	 * Adds a component that will be enabled on start and disabled on termination.
	 * @param com to be enabled while the operation is in progress.
	 * @param type operation type that will trigger the behaviour
	 */
    public void addComponentStartEnabled(Component com, OperationType type) {
        if (com != null) {
            startComponents.add(new ComponentContainer(com, type, ABILITY.ENABLE));
            endComponents.add(new ComponentContainer(com, type, ABILITY.DISABLE));
        }
    }

    /**
	 * Adds a component that will be disabled on start.
	 * @param com to be disabled on operation start
	 * @param type operation type that will trigger the behaviour
	 */
    public void addComponentStartDisabled(Component com, OperationType type) {
        if (com != null) startComponents.add(new ComponentContainer(com, type, ABILITY.DISABLE));
    }

    /**
	 * Adds a component that will be reseted on start.
	 * The component must implement the interface
	 * {@link net.pandoragames.far.ui.model.Resetable Resetable},
	 * otherwise the call will be ignored.
	 * @param com to be reseted on operation start
	 * @param type operation type that will trigger the behaviour
	 */
    public void addComponentStartReseted(Component com, OperationType type) {
        if (com != null) {
            if (com instanceof Resetable) {
                startComponents.add(new ComponentContainer(com, type, ABILITY.RESET));
            } else {
                logger.error("Attempt to add " + com.getClass().getName() + " for reset on start, but it does not implement " + Resetable.class.getName());
            }
        }
    }

    /**
	 * Adds a component that will be enabled on (successfull) termination, but not
	 * altered otherwise.
	 * @param com to be enabled on operation termination.
	 * @param type operation type that will trigger the behaviour
	 */
    public void addComponentTerminationEnabled(Component com, OperationType type) {
        if (com != null) endComponents.add(new ComponentContainer(com, type, ABILITY.ENABLE));
    }

    /**
	 * Class to be run in swing worker thread.
	 * @author Olivier Wehner at 28.02.2008
 * <!-- copyright note --> 
	 */
    class EnDisableComponent implements Runnable {

        private Component component;

        private ABILITY endisFlag;

        public EnDisableComponent(Component comp, ABILITY enable) {
            component = comp;
            endisFlag = enable;
        }

        public void run() {
            if (ABILITY.RESET == endisFlag) {
                ((Resetable) component).reset();
            } else {
                component.setEnabled(endisFlag == ABILITY.ENABLE);
            }
        }
    }

    class ComponentContainer {

        Component component;

        OperationType type;

        ABILITY endisFlag;

        /**
		 * Define component behaviour.
		 * @param comp component
		 * @param form type of for to react for. constant of class OperationForm
		 * @param endisable enable, disable or reset
		 */
        ComponentContainer(Component comp, OperationType form, ABILITY endisable) {
            component = comp;
            type = form;
            endisFlag = endisable;
        }
    }
}
