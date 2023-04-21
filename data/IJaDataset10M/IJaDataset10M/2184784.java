package jhomenet.gui.tab;

import java.util.List;
import java.awt.Component;
import javax.swing.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import org.jscience.physics.units.Unit;
import jhomenet.binding.*;
import jhomenet.gui.*;
import jhomenet.hw.*;
import jhomenet.hw.sensor.*;
import jhomenet.hw.pollers.PollingService;
import jhomenet.hw.management.HardwareManager;

/**
 * ID: $Id$
 * 
 * @author David Irwin
 */
public abstract class SensorInfoTab<H extends Sensor> extends AbstractInfoTab<H> {

    /***
	 * Serial version ID information - used for the serialization process.
	 */
    private static final long serialVersionUID = 00001;

    /***
	 * Keep a reference to the logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(SensorInfoTab.class.getName());

    /**
	 * Holds the edited sensor and vends ValueModels that adapt sensor properties.
	 */
    protected CustomPresentationModel presentationModel = null;

    /**
	 * Text fields.
	 */
    protected JTextField desc_tf, config_tf;

    /**
	 * Used for formatting dates.
	 */
    protected DateFormat formatter;

    /**
	 * Comboboxes.
	 */
    protected JComboBox polling_cb;

    /**
	 * Buttons
	 */
    protected JButton apply_b, reset_b;

    /**
	 * Default constructor. 
	 */
    public SensorInfoTab(AbstractWindow parentWindow) {
        super(parentWindow);
        formatter = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
    }

    /**
	 * Creates, binds and configures the UI components. Changes are
	 * committed to the value models on apply and are flushed on reset. 
	 */
    private void initDefaultComponents(H hardware) {
        desc_tf = BasicComponentFactory.createTextField(presentationModel.getBufferedModel(HomenetHardware.PROPERTYNAME_SETUPDESC), false);
        desc_tf.setColumns(27);
        desc_tf.setToolTipText("Hardware setup description");
        config_tf = BasicComponentFactory.createTextField(presentationModel.getBufferedModel(HomenetHardware.PROPERTYNAME_CONFIGURATION), false);
        config_tf.setColumns(27);
        config_tf.setToolTipText("Hardware configuration");
        polling_cb = new JComboBox();
        polling_cb.setModel(new ComboBoxAdapter(presentationModel.getPollingSelectionInList().getListModel(), presentationModel.getBufferedModel(Sensor.PROPERTYNAME_POLLINGTYPE)));
        apply_b = new JButton(new ApplyAction());
        apply_b.setToolTipText("Apply any changes made to the hardware");
        reset_b = new JButton(new ResetAction());
        reset_b.setToolTipText("Reset any changes made to the hardware");
        apply_b.setEnabled(false);
        reset_b.setEnabled(false);
        PropertyConnector.connect(presentationModel, "buffering", apply_b, "enabled");
        PropertyConnector.connect(presentationModel, "buffering", reset_b, "enabled");
        initAdditionalComponents();
    }

    /**
	 * Initialize any additional GUI components.
	 */
    protected abstract void initAdditionalComponents();

    /**
	 * Initialize the tab's event handling as required.
	 */
    private void initEventHandling() {
    }

    /**
	 * Build a specific sensor type information tab. Currently, this consists
	 * of either a value or state sensor.
	 *
	 * @param hardware
	 * @return A <code>JPanel</code> with the GUI elements
	 */
    protected abstract JPanel buildSpecificAdditionalPanel(H hardware);

    /**
	 * @see jhomenet.gui.tab.AbstractInfoTab#buildAdditionalPanel(jhomenet.hw.Hardware)
	 */
    @Override
    protected JPanel buildAdditionalPanel(H hardware) {
        presentationModel = new CustomPresentationModel(hardware);
        initDefaultComponents(hardware);
        initEventHandling();
        logger.debug("Building additional panel");
        return buildSpecificAdditionalPanel(hardware);
    }

    /**
	 * Create and build the button panel.
	 *
	 * @return A reference to the newly created button panel
	 */
    protected JPanel buildButtonPanel() {
        return ButtonBarFactory.buildRightAlignedBar(apply_b, reset_b);
    }

    /**
	 * Define a custom presentation model that extends the default presentation model. The
	 * custom presentation model contains additional selection in list information.
	 */
    protected final class CustomPresentationModel extends PresentationModel {

        /***
		 * Serial version ID information - used for the serialization process.
		 */
        private static final long serialVersionUID = 00001;

        /**
		 * Holds the bean's list model plus a selection.
		 */
        private final SelectionInList pollingSelectionInList;

        private final SelectionInList unitsSelectionInList;

        private CustomPresentationModel(H hardware) {
            super(hardware);
            pollingSelectionInList = new SelectionInList(hardware.getPollingTypeListModel());
            List<Unit> unitList = HardwareManager.instance().getCompatibleUnits(hardware.getClass().getName());
            unitsSelectionInList = new SelectionInList(unitList);
        }

        public SelectionInList getPollingSelectionInList() {
            return pollingSelectionInList;
        }

        public SelectionInList getUnitsSelectionInList() {
            return unitsSelectionInList;
        }
    }

    /**
	 * 
	 */
    private class PollingTypeListCellRenderer extends DefaultListCellRenderer {

        /***
		 * Serial version ID information - used for the serialization process.
		 */
        private static final long serialVersionUID = 00001;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null && value instanceof PollingService.PollingTypes) {
                PollingService.PollingTypes pollingType = (PollingService.PollingTypes) value;
                setText(" " + pollingType.toString());
            }
            return component;
        }
    }

    /** 
	 * Commits the Trigger used to buffer the editor contents.
	 */
    private final class ApplyAction extends AbstractAction {

        /***
		 * Serial version ID information - used for the serialization process.
		 */
        private static final long serialVersionUID = 00001;

        /**
		 * Default constuctor.
		 */
        private ApplyAction() {
            super("Apply");
        }

        /**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent e) {
            presentationModel.triggerCommit();
        }
    }

    /** 
	 * Flushed the Trigger used to buffer the editor contents.
	 */
    private final class ResetAction extends AbstractAction {

        /***
		 * Serial version ID information - used for the serialization process.
		 */
        private static final long serialVersionUID = 00001;

        /**
		 * Default constructor.
		 */
        private ResetAction() {
            super("Reset");
        }

        /**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent e) {
            presentationModel.triggerFlush();
        }
    }
}
