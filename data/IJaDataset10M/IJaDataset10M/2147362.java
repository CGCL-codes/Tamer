package com.sodad.weka.gui.beans;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.sodad.weka.classifiers.Classifier;
import com.sodad.weka.core.Environment;
import com.sodad.weka.core.EnvironmentHandler;
import com.sodad.weka.gui.GenericObjectEditor;
import com.sodad.weka.gui.PropertySheetPanel;

/**
 * GUI customizer for the classifier wrapper bean
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 7124 $
 */
public class ClassifierCustomizer extends JPanel implements BeanCustomizer, CustomizerClosingListener, CustomizerCloseRequester, EnvironmentHandler {

    /** for serialization */
    private static final long serialVersionUID = -6688000820160821429L;

    static {
        GenericObjectEditor.registerEditors();
    }

    private PropertyChangeSupport m_pcSupport = new PropertyChangeSupport(this);

    private com.sodad.weka.gui.beans.Classifier m_dsClassifier;

    private PropertySheetPanel m_ClassifierEditor = new PropertySheetPanel();

    private JPanel m_incrementalPanel = new JPanel();

    private JCheckBox m_updateIncrementalClassifier = new JCheckBox("Update classifier on incoming instance stream");

    private boolean m_panelVisible = false;

    private JPanel m_holderPanel = new JPanel();

    private JTextField m_executionSlotsText = new JTextField();

    private JCheckBox m_blockOnLastFold = new JCheckBox("Block on last fold of last run");

    private Window m_parentWindow;

    /** Copy of the current classifier in case cancel is selected */
    protected com.sodad.weka.classifiers.Classifier m_backup;

    private Environment m_env = Environment.getSystemWide();

    /**
   *  Listener that wants to know the the modified status of the object that
   * we're customizing
   */
    private ModifyListener m_modifyListener;

    public ClassifierCustomizer() {
        m_ClassifierEditor.setBorder(BorderFactory.createTitledBorder("Classifier options"));
        m_updateIncrementalClassifier.setToolTipText("Train the classifier on " + "each individual incoming streamed instance.");
        m_updateIncrementalClassifier.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_dsClassifier != null) {
                    m_dsClassifier.setUpdateIncrementalClassifier(m_updateIncrementalClassifier.isSelected());
                }
            }
        });
        m_incrementalPanel.add(m_updateIncrementalClassifier);
        m_executionSlotsText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_dsClassifier != null && m_executionSlotsText.getText().length() > 0) {
                    int newSlots = Integer.parseInt(m_executionSlotsText.getText());
                    m_dsClassifier.setExecutionSlots(newSlots);
                }
            }
        });
        m_executionSlotsText.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                if (m_dsClassifier != null && m_executionSlotsText.getText().length() > 0) {
                    int newSlots = Integer.parseInt(m_executionSlotsText.getText());
                    m_dsClassifier.setExecutionSlots(newSlots);
                }
            }
        });
        m_blockOnLastFold.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_dsClassifier != null) {
                    m_dsClassifier.setBlockOnLastFold(m_blockOnLastFold.isSelected());
                }
            }
        });
        JPanel executionSlotsPanel = new JPanel();
        executionSlotsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel executionSlotsLabel = new JLabel("Execution slots");
        executionSlotsPanel.setLayout(new BorderLayout());
        executionSlotsPanel.add(executionSlotsLabel, BorderLayout.WEST);
        executionSlotsPanel.add(m_executionSlotsText, BorderLayout.CENTER);
        m_holderPanel.setBorder(BorderFactory.createTitledBorder("More options"));
        m_holderPanel.setLayout(new BorderLayout());
        m_holderPanel.add(executionSlotsPanel, BorderLayout.NORTH);
        m_holderPanel.add(m_blockOnLastFold, BorderLayout.SOUTH);
        JPanel holder2 = new JPanel();
        holder2.setLayout(new BorderLayout());
        holder2.add(m_holderPanel, BorderLayout.NORTH);
        JButton OKBut = new JButton("OK");
        JButton CancelBut = new JButton("Cancel");
        OKBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_dsClassifier.setClassifierTemplate(m_dsClassifier.getClassifierTemplate());
                if (m_modifyListener != null) {
                    m_modifyListener.setModifiedStatus(ClassifierCustomizer.this, true);
                }
                m_parentWindow.dispose();
            }
        });
        CancelBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_backup != null) {
                    m_dsClassifier.setClassifierTemplate(m_backup);
                }
                if (m_modifyListener != null) {
                    m_modifyListener.setModifiedStatus(ClassifierCustomizer.this, false);
                }
                m_parentWindow.dispose();
            }
        });
        JPanel butHolder = new JPanel();
        butHolder.setLayout(new GridLayout(1, 2));
        butHolder.add(OKBut);
        butHolder.add(CancelBut);
        holder2.add(butHolder, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(m_ClassifierEditor, BorderLayout.CENTER);
        add(holder2, BorderLayout.SOUTH);
    }

    private void checkOnClassifierType() {
        Classifier editedC = m_dsClassifier.getClassifierTemplate();
        if (editedC instanceof com.sodad.weka.classifiers.UpdateableClassifier && m_dsClassifier.hasIncomingStreamInstances()) {
            if (!m_panelVisible) {
                m_holderPanel.add(m_incrementalPanel, BorderLayout.SOUTH);
                m_panelVisible = true;
                m_executionSlotsText.setEnabled(false);
            }
        } else {
            if (m_panelVisible) {
                m_holderPanel.remove(m_incrementalPanel);
                m_executionSlotsText.setEnabled(true);
                m_panelVisible = false;
            }
        }
    }

    /**
   * Set the classifier object to be edited
   *
   * @param object an <code>Object</code> value
   */
    public void setObject(Object object) {
        m_dsClassifier = (com.sodad.weka.gui.beans.Classifier) object;
        try {
            m_backup = (com.sodad.weka.classifiers.Classifier) GenericObjectEditor.makeCopy(m_dsClassifier.getClassifierTemplate());
        } catch (Exception ex) {
        }
        m_ClassifierEditor.setEnvironment(m_env);
        m_ClassifierEditor.setTarget(m_dsClassifier.getClassifierTemplate());
        m_updateIncrementalClassifier.setSelected(m_dsClassifier.getUpdateIncrementalClassifier());
        m_executionSlotsText.setText("" + m_dsClassifier.getExecutionSlots());
        m_blockOnLastFold.setSelected(m_dsClassifier.getBlockOnLastFold());
        checkOnClassifierType();
    }

    public void customizerClosing() {
        if (m_executionSlotsText.getText().length() > 0) {
            int newSlots = Integer.parseInt(m_executionSlotsText.getText());
            m_dsClassifier.setExecutionSlots(newSlots);
        }
    }

    /**
   * Add a property change listener
   *
   * @param pcl a <code>PropertyChangeListener</code> value
   */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        m_pcSupport.addPropertyChangeListener(pcl);
    }

    /**
   * Remove a property change listener
   *
   * @param pcl a <code>PropertyChangeListener</code> value
   */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        m_pcSupport.removePropertyChangeListener(pcl);
    }

    public void setParentWindow(Window parent) {
        m_parentWindow = parent;
    }

    /**
   * Set any environment variables to pass to the PropertySheetPanel
   * 
   * @param env environment variables to pass to the property sheet
   * panel
   */
    public void setEnvironment(Environment env) {
        m_env = env;
    }

    @Override
    public void setModifiedListener(ModifyListener l) {
        m_modifyListener = l;
    }
}
