package net.flysource.client.gui.smartlist.edit;

import net.flysource.client.gui.components.FSDialog;
import net.flysource.client.gui.components.FSTextField;
import net.flysource.client.gui.smartlist.SmartListCondition;
import net.flysource.client.util.FSLabels;
import net.flysource.client.util.Toolbox;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import fi.mmm.yhteinen.swing.core.YIComponent;
import fi.mmm.yhteinen.swing.core.component.YButton;
import fi.mmm.yhteinen.swing.core.component.list.YComboBox;
import fi.mmm.yhteinen.swing.core.tools.YUIToolkit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SmartListView extends FSDialog {

    private YButton okButton;

    private YButton cancelButton;

    private FSTextField nameField = new FSTextField();

    private YComboBox fieldCombo = new YComboBox();

    private YComboBox conditionCombo = new YComboBox();

    private FSTextField valueField = new FSTextField();

    public SmartListView() {
        super(Toolbox.getMainFrame(), FSLabels.getLabel("smartList.title"));
        initLayout();
        initMVC();
    }

    private void initMVC() {
        this.getYProperty().put(YIComponent.MVC_NAME, "smartList");
        okButton.getYProperty().put(YIComponent.MVC_NAME, "okButton");
        cancelButton.getYProperty().put(YIComponent.MVC_NAME, "cancelButton");
        nameField.getYProperty().put(YIComponent.MVC_NAME, "name");
        valueField.getYProperty().put(YIComponent.MVC_NAME, "value");
        conditionCombo.getYProperty().put(YIComponent.MVC_NAME, "condition");
        fieldCombo.getYProperty().put(YIComponent.MVC_NAME, "field");
        YUIToolkit.guessViewComponents(this);
    }

    private void initLayout() {
        JPanel panel = new JPanel(new FormLayout("4dlu,pref,4dlu", "4dlu,pref,4dlu,pref,2dlu, pref, 4dlu, pref, 8dlu, pref, 4dlu,pref"));
        setModal(true);
        setResizable(false);
        panel.add(new JLabel(FSLabels.getLabel("smartList.instruction")), "2,2");
        panel.add(new JLabel(FSLabels.getLabel("smartList.name")), "2,4");
        nameField.setColumns(20);
        nameField.setMaxLength(40);
        panel.add(nameField, "2,6");
        panel.add(getConditionListPanel(), "2,8");
        okButton = new YButton(FSLabels.getLabel(FSLabels.getLabel("ok")));
        cancelButton = new YButton(FSLabels.getLabel(FSLabels.getLabel("cancel")));
        panel.add(ButtonBarFactory.buildOKCancelBar(okButton, cancelButton), "2,10");
        JLabel copyBlurb = new JLabel(FSLabels.getLabel("smartList.info"));
        copyBlurb.setForeground(Color.BLUE);
        panel.add(copyBlurb, "2,12");
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(Toolbox.getMainFrame());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getRootPane().registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().setDefaultButton(okButton);
        nameField.requestFocus();
    }

    private JPanel getConditionListPanel() {
        int row = 2;
        CellConstraints cc = new CellConstraints();
        JPanel panel = new JPanel(new FormLayout("120px, 4dlu, 120px, 4dlu, pref, 4dlu, ", "2dlu, pref, 2dlu, pref"));
        panel.add(new JLabel(FSLabels.getLabel("smartList.matchCondition")), cc.xywh(1, 2, 3, 1));
        fieldCombo = new YComboBox(true);
        fieldCombo.setComboModel(SmartListCondition.getFieldNames(), false);
        conditionCombo = new YComboBox();
        conditionCombo.setComboModel(SmartListCondition.getConditions(), false);
        valueField.setColumns(20);
        row += 2;
        panel.add(fieldCombo, cc.xy(1, row));
        panel.add(conditionCombo, cc.xy(3, row));
        panel.add(valueField, cc.xy(5, row));
        return panel;
    }
}
