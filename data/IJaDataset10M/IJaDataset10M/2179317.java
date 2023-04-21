package cw.coursemanagementmodul.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;
import cw.boardingschoolmanagement.gui.component.CWButtonPanel;
import cw.boardingschoolmanagement.gui.component.CWComponentFactory;
import cw.boardingschoolmanagement.gui.component.CWView;
import cw.coursemanagementmodul.pojo.Course;

/**
 *
 * @author André Salmhofer
 */
public class EditCourseView extends CWView {

    private EditCoursePresentationModel model;

    private CWComponentFactory.CWComponentContainer componentContainer;

    private JLabel nameLabel;

    private JLabel beginLabel;

    private JLabel endLabel;

    private JLabel valueLabel;

    private JTextField nameTextField;

    private JTextField valueTextField;

    private JButton saveButton;

    private JButton saveAndCloseButton;

    private JButton cancelButton;

    private JDateChooser beginDate;

    private JDateChooser endDate;

    public EditCourseView(EditCoursePresentationModel model) {
        this.model = model;
        initComponents();
        buildView();
        initEventHandling();
    }

    private void initComponents() {
        nameLabel = CWComponentFactory.createLabel("Kursname");
        beginLabel = CWComponentFactory.createLabel("Beginn");
        endLabel = CWComponentFactory.createLabel("Ende");
        valueLabel = CWComponentFactory.createLabel("Betrag");
        nameTextField = CWComponentFactory.createTextField(model.getBufferedModel(Course.PROPERTYNAME_NAME), false);
        valueTextField = CWComponentFactory.createCurrencyTextField(model.getBufferedModel(Course.PROPERTYNAME_PRICE));
        saveButton = CWComponentFactory.createButton(model.getSaveButtonAction());
        saveAndCloseButton = CWComponentFactory.createButton(model.getSaveCancelButtonAction());
        cancelButton = CWComponentFactory.createButton(model.getCancelButtonAction());
        saveButton.setToolTipText(CWComponentFactory.createToolTip("Speichern", "Hier wird der Kurs gespeichert!", "cw/coursemanagementmodul/images/save.png"));
        saveAndCloseButton.setToolTipText(CWComponentFactory.createToolTip("Speichern u. Schließen", "Hier wird der Kurs gespeichert und anschließend in die Kursuebersichts gewechselt!", "cw/coursemanagementmodul/images/save_cancel.png"));
        cancelButton.setToolTipText(CWComponentFactory.createToolTip("Abbrechen", "Hier kehren Sie zur Kursuebersicht zurueck!", "cw/coursemanagementmodul/images/cancel.png"));
        beginDate = CWComponentFactory.createDateChooser(model.getBufferedModel(Course.PROPERTYNAME_BEGINDATE));
        endDate = CWComponentFactory.createDateChooser(model.getBufferedModel(Course.PROPERTYNAME_ENDDATE));
        componentContainer = CWComponentFactory.createComponentContainer().addComponent(beginDate).addComponent(beginLabel).addComponent(cancelButton).addComponent(endDate).addComponent(endLabel).addComponent(nameLabel).addComponent(nameTextField).addComponent(saveAndCloseButton).addComponent(saveButton).addComponent(valueLabel).addComponent(valueTextField);
    }

    private void initEventHandling() {
    }

    private void buildView() {
        this.setHeaderInfo(model.getHeaderInfo());
        CWButtonPanel buttonPanel = this.getButtonPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(saveAndCloseButton);
        buttonPanel.add(cancelButton);
        FormLayout layout = new FormLayout("pref, 4dlu, pref, 150dlu, pref", "pref, 4dlu, pref, 4dlu, pref, 4dlu, pref");
        CellConstraints cc = new CellConstraints();
        this.getContentPanel().setLayout(layout);
        this.getContentPanel().add(nameLabel, cc.xy(1, 1));
        this.getContentPanel().add(nameTextField, cc.xyw(3, 1, 3));
        this.getContentPanel().add(beginLabel, cc.xy(1, 3));
        this.getContentPanel().add(beginDate, cc.xy(3, 3));
        this.getContentPanel().add(endLabel, cc.xy(1, 5));
        this.getContentPanel().add(endDate, cc.xy(3, 5));
        this.getContentPanel().add(valueLabel, cc.xy(1, 7));
        this.getContentPanel().add(valueTextField, cc.xy(3, 7));
    }

    @Override
    public void dispose() {
        componentContainer.dispose();
        model.dispose();
    }
}
