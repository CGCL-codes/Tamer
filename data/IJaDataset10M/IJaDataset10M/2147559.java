package org.gaea.ui.graphic.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.gaea.common.GaeaLogger;
import org.gaea.common.GaeaLoggerFactory;
import org.gaea.common.exception.GaeaException;
import org.gaea.ui.graphic.General;
import org.gaea.ui.language.Messages;
import org.gaea.ui.treatment.Treatment;
import org.gaea.ui.utilities.DataHelper;
import org.gaea.ui.utilities.ErrorUtilities;
import org.gaea.ui.utilities.SpringUtilities;

/**
 * This panel displays all the fields of _object and allows to change them.
 * 
 * @author mdmajor
 */
public class AddInstancePanel extends JPanel implements ActionListener {

    /**
	 * Auto-generated
	 */
    private static final long serialVersionUID = 3887465295346815482L;

    public static final String ADD_OBJECT_IN_TOP_ARRAY = "AddObjectInTopArray";

    private Class _class;

    private Vector<AddInstanceElement> _elementsList;

    /**
	 * Second panel containing everything. Needed so that the components do not
	 * take all the space.
	 */
    private JPanel _containerPanel;

    private PropertyChangeSupport _propertyChangeSupport;

    private static final String SEND_ACTION = "sendNewInstanceAction";

    private static GaeaLogger _logger = GaeaLoggerFactory.getLogger("org.gaea.ui.graphic.data.AddInstancePanel", ErrorUtilities.getErrorHandlers());

    private JButton _sendButton;

    /**
	 * Constructor
	 */
    public AddInstancePanel() {
        synchronized (AddInstancePanel.this) {
            if (_propertyChangeSupport == null) {
                _propertyChangeSupport = new PropertyChangeSupport(this);
            }
        }
        createAndShowGUI();
    }

    /**
	 * Create the GUI and show it.
	 */
    private void createAndShowGUI() {
        _containerPanel = new JPanel();
        _containerPanel.setLayout(new SpringLayout());
        this.add(_containerPanel);
        _elementsList = new Vector<AddInstanceElement>();
        _sendButton = new JButton(Messages.getString("Common.Send"));
        _sendButton.addActionListener(this);
        _sendButton.setActionCommand(SEND_ACTION);
        updatePanel();
    }

    /**
	 * Refreshes the UI components (components associated with the field of the
	 * current _object).
	 */
    private void updatePanel() {
        _containerPanel.removeAll();
        _elementsList.removeAllElements();
        _sendButton.setEnabled(true);
        if (_class == null) {
            return;
        }
        Field[] fieldArray = DataHelper.getFields(_class);
        int countComponent = 0;
        for (int i = 0; i < fieldArray.length; i++) {
            Field field = fieldArray[i];
            if (DataHelper.isUsefulField(field, false)) {
                AddInstanceElement element;
                try {
                    element = new AddInstanceElement(field);
                    _containerPanel.add(element.getLabel());
                    _containerPanel.add(element.getInstanceElement().getPanel());
                    _elementsList.add(element);
                    countComponent++;
                } catch (GaeaException e) {
                    _logger.error(e.getMessage());
                    _containerPanel.add(new JLabel(field.getName() + " (" + field.getType().getCanonicalName() + "): ", JLabel.TRAILING));
                    _containerPanel.add(new JPanel());
                    countComponent++;
                }
            }
        }
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalGlue());
        hBox.add(_sendButton);
        _containerPanel.add(Box.createHorizontalGlue());
        _containerPanel.add(hBox);
        countComponent++;
        _containerPanel.setOpaque(true);
        SpringUtilities.makeCompactGrid(_containerPanel, countComponent, 2, 6, 6, 6, 6);
    }

    /**
	 * Adds the object of this class with the data written in the fields if
	 * everything is alright.
	 */
    public void apply() {
        Vector<String> errorsList = new Vector<String>();
        Object newInstance = null;
        try {
            Constructor constructor = _class.getDeclaredConstructor();
            boolean isAccessible = constructor.isAccessible();
            if (!isAccessible) {
                constructor.setAccessible(true);
            }
            newInstance = constructor.newInstance();
            if (!isAccessible) {
                constructor.setAccessible(false);
            }
        } catch (IllegalAccessException exception) {
            errorsList.add(Messages.getString("AddInstancePanel.NotAllowedCreateInstance", _class.toString()));
        } catch (InstantiationException exception) {
            errorsList.add(Messages.getString("AddInstancePanel.UnableToInstanciate", _class.toString()));
        } catch (NoSuchMethodException exception) {
            errorsList.add(Messages.getString("AddInstancePanel.UnableFindConstructor", _class.toString()));
        } catch (InvocationTargetException exception) {
            errorsList.add(Messages.getString("AddInstancePanel.ExceptionInConstructor", _class.toString()));
        }
        if (newInstance != null) {
            Field[] fieldArray = DataHelper.getFields(_class);
            for (Field field : fieldArray) {
                if (!DataHelper.isUsefulField(field, false)) {
                    continue;
                }
                AddInstanceElement element = getElementForFieldName(field.getName());
                if (element == null) {
                    errorsList.add(Messages.getString("AddInstancePanel.ErrorGetValueUI"));
                    continue;
                }
                errorsList.addAll(element.getInstanceElement().generateValue());
                errorsList.addAll(element.getInstanceElement().saveToInstance(newInstance, field));
            }
        }
        if (errorsList.size() > 0) {
            if (errorsList.size() == 1) {
                _logger.warning(Messages.getString("AddInstancePanel.ErrorPleaseFix"));
            } else {
                _logger.warning(Messages.getString("AddInstancePanel.ErrorsPleaseFix"));
            }
            _logger.warning(Messages.getString("AddInstancePanel.InstanceNotSent"));
            for (String error : errorsList) {
                _logger.warning(error);
            }
        } else {
            if (newInstance != null) {
                try {
                    if (Treatment.getInstance().getData().addInstance(newInstance)) {
                        General.getInstance().setStatusBarText(Messages.getString("AddInstancePanel.InstanceAddedSuccess"));
                        erase();
                        _propertyChangeSupport.firePropertyChange(ADD_OBJECT_IN_TOP_ARRAY, null, newInstance);
                    }
                } catch (GaeaException e) {
                    _logger.warning(Messages.getString("AddInstancePanel.FailedAddInstance", e.getMessage()));
                    General.getInstance().setStatusBarText(Messages.getString("AddInstancePanel.InstanceAddedError"));
                }
            }
        }
    }

    /**
	 * Erases all value fields.
	 */
    public void erase() {
        for (AddInstanceElement element : _elementsList) {
            element.getInstanceElement().resetValue();
        }
    }

    /**
	 * Fetches the <code>AddInstanceElement</code> instance that has the same
	 * name than <code>fieldName</code>
	 * 
	 * @param fieldName
	 *            Name of the field
	 * @return The instance found or null if it has not been found.
	 */
    private AddInstanceElement getElementForFieldName(String fieldName) {
        for (AddInstanceElement element : _elementsList) {
            if (fieldName.equals(element.getField().getName())) {
                return element;
            }
        }
        return null;
    }

    /**
	 * @param object
	 *            the object to set
	 */
    public void setClass(Class object) {
        _class = object;
        updatePanel();
    }

    /**
	 * Disables the components because the data comes from OQL and some
	 * components should not be available.
	 */
    public void setDataFromOQL() {
        _sendButton.setEnabled(false);
    }

    /**
	 * Adds the specified listener to the list
	 * 
	 * @param listener
	 *            Listener to be added.
	 */
    public synchronized void addChangeListener(PropertyChangeListener listener) {
        if (_propertyChangeSupport == null) {
            _propertyChangeSupport = new PropertyChangeSupport(this);
        }
        if (listener == null) {
            _logger.warning(Messages.getString("Common.NullListener"));
        }
        _propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(SEND_ACTION)) {
            if (DataPerspective.getInstance().isDataFromOQL()) {
                _logger.info(Messages.getString("Common.ErrorSendFromOQL"));
                return;
            }
            apply();
        }
    }
}
