package de.flingelli.scrum.gui.latex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import de.flingelli.latex.EDocumentClass;
import de.flingelli.latex.ELanguage;
import de.flingelli.latex.EPaperSize;
import de.flingelli.latex.ESide;
import de.flingelli.scrum.Global;
import de.flingelli.scrum.datastructure.gui.WindowType;
import de.flingelli.scrum.gui.listener.ScrumComponentListener;
import de.flingelli.scrum.language.JastTranslation;
import de.flingelli.scrum.observer.GuiPropertyChangeSupport;
import de.flingelli.scrum.observer.ProductPropertyChangeSupport;

/**
 * 
 * @author Markus Flingelli
 * 
 */
@SuppressWarnings("serial")
public class LaTeXReportDialog extends JDialog implements PropertyChangeListener {

    private static final int BUTTON_WIDTH = 120;

    private static final int BUTTON_HEIGHT = 23;

    private static final int ROW_X_COLUMN_1 = 10;

    private static final int ROW_X_COLUMN_2 = 170;

    private static final int COMPONENT_WIDTH = 150;

    private static final int COMPONENT_HEIGHT = 20;

    private static final String PREFIX = "de.flingelli.scrum.gui.latex.OptionPanel-";

    private JLabel documentClassLabel;

    private JComboBox documentClass;

    private JLabel languageLabel;

    private JComboBox language;

    private JLabel sideLabel;

    private JComboBox side;

    private JButton cancelButton;

    private JButton okButton;

    private boolean isCancel = true;

    private JLabel paperSizeLabel;

    private JComboBox paperSize;

    private GuiPropertyChangeSupport mGuiPropertyChangeSupport = null;

    public LaTeXReportDialog() {
        setModal(true);
        setResizable(false);
        ProductPropertyChangeSupport.getInstance().addPropertyChangeListener(this);
        mGuiPropertyChangeSupport = GuiPropertyChangeSupport.getInstance(Global.getOptionsFileName());
        addComponentListener(new ScrumComponentListener(WindowType.LATEX_DIALOG));
        getContentPane().setLayout(null);
        init();
        calculateLocation();
        initializeComboboxes();
        changeLanguage();
    }

    private void calculateLocation() {
        int locationx = mGuiPropertyChangeSupport.getFrameTopLeftX(WindowType.LATEX_DIALOG);
        int locationy = mGuiPropertyChangeSupport.getFrameTopLeftY(WindowType.LATEX_DIALOG);
        setLocation(locationx, locationy);
    }

    private void init() {
        setSize(350, 210);
        setPreferredSize(getSize());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        documentClassLabel = new JLabel();
        documentClassLabel.setBounds(ROW_X_COLUMN_1, 10, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(documentClassLabel);
        documentClass = new JComboBox();
        documentClass.setBounds(ROW_X_COLUMN_2, 10, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(documentClass);
        languageLabel = new JLabel();
        languageLabel.setBounds(ROW_X_COLUMN_1, 40, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(languageLabel);
        language = new JComboBox();
        language.setBounds(ROW_X_COLUMN_2, 40, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(language);
        sideLabel = new JLabel();
        sideLabel.setBounds(ROW_X_COLUMN_1, 70, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(sideLabel);
        side = new JComboBox();
        side.setBounds(ROW_X_COLUMN_2, 70, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(side);
        okButton = new JButton();
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                isCancel = false;
                setVisible(false);
            }
        });
        okButton.setBounds(ROW_X_COLUMN_1, COMPONENT_WIDTH, BUTTON_WIDTH, BUTTON_HEIGHT);
        getContentPane().add(okButton);
        cancelButton = new JButton();
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        cancelButton.setBounds(200, COMPONENT_WIDTH, BUTTON_WIDTH, BUTTON_HEIGHT);
        getContentPane().add(cancelButton);
        paperSizeLabel = new JLabel();
        paperSizeLabel.setBounds(ROW_X_COLUMN_1, 100, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(paperSizeLabel);
        paperSize = new JComboBox();
        paperSize.setBounds(ROW_X_COLUMN_2, 100, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        getContentPane().add(paperSize);
    }

    private void initializeComboboxes() {
        initDocumentClassComboBox();
        initLanguageComboBox();
        initSideComoboBox();
        initPaperSizeComboBox();
    }

    private void initPaperSizeComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (EPaperSize value : EPaperSize.values()) {
            model.addElement(value);
        }
        paperSize.setModel(model);
    }

    private void initSideComoboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (ESide value : ESide.values()) {
            model.addElement(value);
        }
        side.setModel(model);
    }

    private void initLanguageComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (ELanguage lang : ELanguage.values()) {
            model.addElement(lang);
        }
        language.setModel(model);
    }

    private void initDocumentClassComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (EDocumentClass docClass : EDocumentClass.values()) {
            model.addElement(docClass);
        }
        documentClass.setModel(model);
    }

    private void changeLanguage() {
        JastTranslation translation = JastTranslation.getInstance();
        documentClassLabel.setText(translation.getValue(PREFIX + "documentClass"));
        languageLabel.setText(translation.getValue(PREFIX + "language"));
        sideLabel.setText(translation.getValue(PREFIX + "side"));
        paperSizeLabel.setText(translation.getValue(PREFIX + "paperSize"));
        okButton.setText(translation.getValue(PREFIX + "ok"));
        cancelButton.setText(translation.getValue(PREFIX + "cancel"));
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("language_changed")) {
            changeLanguage();
        }
    }

    public boolean isCancel() {
        return isCancel;
    }

    public EDocumentClass getDocumentClass() {
        return (EDocumentClass) documentClass.getSelectedItem();
    }

    public ELanguage getLanguage() {
        return (ELanguage) language.getSelectedItem();
    }

    public ESide getSide() {
        return (ESide) side.getSelectedItem();
    }

    public EPaperSize getPaperSize() {
        return (EPaperSize) paperSize.getSelectedItem();
    }
}
