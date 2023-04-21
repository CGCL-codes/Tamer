package org.gaea.ui.graphic.options;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.gaea.ui.component.BottomPanel;
import org.gaea.ui.graphic.options.panel.IOptionPanelListener;
import org.gaea.ui.graphic.options.panel.OptionPanel;
import org.gaea.ui.graphic.options.panel.OptionPanelEvent;
import org.gaea.ui.graphic.options.panel.OptionPanelGeneral;
import org.gaea.ui.graphic.options.panel.OptionPanelLogin;
import org.gaea.ui.language.Messages;
import org.gaea.ui.utilities.UtilityUI;

/**
 * Holds the dialog to show the options. There are different constructors to
 * load different kind of options.
 * 
 * @author jsgoupil
 */
public class OptionDialog extends JDialog {

    /**
	 * Auto Generated
	 */
    private static final long serialVersionUID = 5088821763133032794L;

    /**
	 * Holds the center panel.
	 */
    private OptionPanel[] _panel;

    /**
	 * This panel allows us to switch easily between all _panel.
	 */
    private JPanel _cardPanel;

    /**
	 * List on the left
	 */
    private OptionList _list;

    /**
	 * All components which are dirty
	 */
    private Vector<JComponent> _dirtyComponent;

    /**
	 * Bottom panel containing buttons
	 */
    private BottomPanel _bottomPanel;

    private ListPropertyListener _listPropertyListener;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("org.gaea.ui.lookandfeel.GaeaLookAndFeel");
        } catch (InstantiationException e) {
        } catch (ClassNotFoundException e) {
        } catch (UnsupportedLookAndFeelException e) {
        } catch (IllegalAccessException e) {
        }
        OptionDialog dialog = new OptionDialog(null);
        dialog.setVisible(true);
    }

    /**
	 * Loads all the options and select the default one.
	 * 
	 * @param parent
	 */
    public OptionDialog(JFrame parent) {
        this(parent, 0);
    }

    /**
	 * Loads all the options and select the initialSelected panel.
	 * 
	 * @param parent
	 * 
	 * @param initialSelected
	 *            Index of the panel
	 */
    public OptionDialog(JFrame parent, int initialSelected) {
        super(parent);
        _dirtyComponent = new Vector<JComponent>();
        _cardPanel = new JPanel(new CardLayout());
        _cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 10));
        initPanel(_cardPanel);
        _listPropertyListener = new ListPropertyListener();
        changeTab(-1, initialSelected);
        _list = new OptionList(_panel, initialSelected);
        _list.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 2));
        _list.addPropertyChangeListener(OptionList.SELECTION_CHANGED, _listPropertyListener);
        _bottomPanel = new BottomPanel(new String[] { Messages.getString("Common.OK"), Messages.getString("Common.Cancel"), Messages.getString("Common.Apply") });
        _bottomPanel.getButton(0).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pressOK();
            }
        });
        _bottomPanel.getButton(1).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pressCancel();
            }
        });
        _bottomPanel.getButton(2).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pressApply();
            }
        });
        _bottomPanel.getButton(2).setEnabled(false);
        setLayout(new BorderLayout());
        getContentPane().add(_list, BorderLayout.WEST);
        getContentPane().add(_cardPanel, BorderLayout.CENTER);
        getContentPane().add(_bottomPanel, BorderLayout.SOUTH);
        UtilityUI.closeOnEscape(this);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                boolean goClose = checkDirty(_list.getSelectedIndex());
                if (goClose) {
                    OptionDialog.this.dispose();
                }
            }
        });
        setTitle(Messages.getString("Common.Options"));
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        int height = 380;
        setSize(new Dimension(700, height));
        setMinimumSize(new Dimension(630, 300));
        setLocationRelativeTo(parent);
    }

    /**
	 * Inits all the panels to be loaded. Add more here.
	 * 
	 * @param panel
	 */
    private void initPanel(JPanel panel) {
        _panel = new OptionPanel[2];
        _panel[0] = new OptionPanelGeneral();
        _panel[1] = new OptionPanelLogin();
        OptionPanelListener listener = new OptionPanelListener();
        for (OptionPanel optionPanel : _panel) {
            optionPanel.addOptionPanelListener(listener);
            panel.add(optionPanel, optionPanel.getWindowName());
        }
    }

    /**
	 * Switching tabs
	 * 
	 * @param tabIndex
	 */
    private void changeTab(final int tabIndexOld, final int tabIndexNew) {
        boolean changeTab = checkDirty(tabIndexOld);
        if (changeTab) {
            CardLayout cl = (CardLayout) (_cardPanel.getLayout());
            cl.show(_cardPanel, _panel[tabIndexNew].getWindowName());
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    _panel[tabIndexNew].revalidate();
                    if (tabIndexOld >= 0) {
                        _panel[tabIndexOld].hidePanel();
                    }
                    _panel[tabIndexNew].displayPanel();
                }
            });
        }
    }

    /**
	 * Checks if the tab is dirty (has been modified). If so, we display a
	 * confirmation if we allow the user to change the tab.
	 * 
	 * @param tabIndexOld
	 * @return true if no dirty, clicked yes or no. False if clicked cancel.
	 */
    private boolean checkDirty(final int tabIndexOld) {
        boolean changeAccepted = true;
        if (_dirtyComponent.size() > 0) {
            int optionSelected = JOptionPane.showConfirmDialog(this, Messages.getString("OptionDialog.WantToSave"), Messages.getString("OptionDialog.WantToSaveTitle"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (optionSelected == JOptionPane.YES_OPTION) {
                applySpecificPanel(tabIndexOld);
            } else if (optionSelected == JOptionPane.NO_OPTION) {
                _dirtyComponent.removeAllElements();
                _bottomPanel.getButton(2).setEnabled(false);
            } else if (optionSelected == JOptionPane.CANCEL_OPTION) {
                changeAccepted = false;
                _list.removePropertyChangeListener(OptionList.SELECTION_CHANGED, _listPropertyListener);
                _list.setSelectedIndex(tabIndexOld);
                _list.addPropertyChangeListener(OptionList.SELECTION_CHANGED, _listPropertyListener);
            }
        }
        return changeAccepted;
    }

    /**
	 * Ok has been pressed.
	 * 
	 * @return success
	 */
    private boolean pressOK() {
        if (pressApply()) {
            dispose();
        }
        return true;
    }

    /**
	 * Cancel has been pressed.
	 * 
	 * @return success
	 */
    private boolean pressCancel() {
        dispose();
        return true;
    }

    /**
	 * Apply a specific index of panel.
	 * 
	 * @param index
	 * @return successp
	 */
    private boolean applySpecificPanel(int index) {
        if (index >= 0 && index < _panel.length) {
            if (_panel[index].apply()) {
                _dirtyComponent.removeAllElements();
                _bottomPanel.getButton(2).setEnabled(false);
                return true;
            }
            return false;
        }
        return false;
    }

    /**
	 * Apply has been pressed. The button becomes disabled on success
	 * 
	 * @return success
	 */
    private boolean pressApply() {
        return applySpecificPanel(_list.getSelectedIndex());
    }

    /**
	 * OptionList listener.
	 * 
	 * @author jsgoupil
	 */
    class ListPropertyListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            changeTab(((Integer) evt.getOldValue()).intValue(), ((Integer) evt.getNewValue()).intValue());
        }
    }

    /**
	 * Class dealing with errors and apply button. When a field is changed and
	 * is considerated as dirty, we add it to our _dirtyComponent. If it is not
	 * longer dirty, we remove it. If we have 1 or more component dirty, the
	 * apply buttons should be enabled.
	 * 
	 * @author jsgoupil
	 */
    class OptionPanelListener implements IOptionPanelListener {

        public void fieldChanged(OptionPanelEvent e) {
            if (e.isDirty()) {
                if (!_dirtyComponent.contains(e.getComponent())) {
                    _dirtyComponent.add(e.getComponent());
                }
            } else {
                _dirtyComponent.remove(e.getComponent());
            }
            if (_dirtyComponent.size() == 0) {
                _bottomPanel.getButton(2).setEnabled(false);
            } else {
                _bottomPanel.getButton(2).setEnabled(true);
            }
        }

        public void raiseError(OptionPanelEvent evt) {
            _bottomPanel.showError(evt.getErrorMessage(), true);
        }

        public void hideError() {
            _bottomPanel.hideError(true);
        }
    }
}
