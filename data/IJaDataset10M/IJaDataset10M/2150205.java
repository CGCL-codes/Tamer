package net.sf.refactorit.refactorings.promotetemptofield.ui;

import net.sf.refactorit.classmodel.BinModifier;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.refactorings.promotetemptofield.FieldInitialization;
import net.sf.refactorit.refactorings.promotetemptofield.PromoteTempToField;
import net.sf.refactorit.source.format.BinModifierFormatter;
import net.sf.refactorit.ui.DialogManager;
import net.sf.refactorit.ui.dialog.RitDialog;
import net.sf.refactorit.ui.help.HelpViewer;
import net.sf.refactorit.utils.SwingUtil;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author  RISTO A
 * @author  juri
 */
public class UserInputDialog extends UserInput.View {

    RitDialog dialog;

    UserInput userInput;

    JTextField newName = new JTextField();

    private JCheckBox finalModifier = new JCheckBox("Final") {

        {
            setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(0, 0, 4, 0)));
            setMnemonic('l');
        }
    };

    private JCheckBox staticModifier = new JCheckBox("Static") {

        {
            setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(0, 0, 4, 0)));
            setMnemonic('s');
        }
    };

    private JPanel initLocationsPane = new JPanel() {

        {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createTitledBorder("Initialize field in:"));
        }
    };

    private JButton ok = new JButton("Ok");

    private JButton cancel = new JButton("Cancel");

    private JButton help = new JButton("Help");

    private ButtonGroup initLocationsGroup = new ButtonGroup();

    private ButtonGroup accessModifiersGroup = new ButtonGroup();

    public boolean show(PromoteTempToField p) {
        userInput = new UserInput(p.getVariable(), this);
        JPanel contentPane = SwingUtil.combineInNorth(new JComponent[] { createHelpPanel(), createNewNamePanel(), createModifiersPanel(), initLocationsPane, createButtonPanel() });
        dialog = RitDialog.create(IDEController.getInstance().createProjectContext());
        dialog.setTitle("Convert Temp To Field");
        dialog.setContentPane(contentPane);
        HelpViewer.attachHelpToDialog(dialog, help, "refact.convertTempToField");
        SwingUtil.initCommonDialogKeystrokes(dialog, ok, cancel, help);
        newName.selectAll();
        newName.grabFocus();
        dialog.show();
        userInput.initializeRefactoring(p, newName.getText(), finalModifier.isSelected(), staticModifier.isSelected());
        return userInput.wasOkPressed();
    }

    private JPanel createHelpPanel() {
        return DialogManager.getHelpPanel("Set field options below");
    }

    private JPanel createModifiersPanel() {
        JPanel result = SwingUtil.combineInNorth(new JComponent[] { finalModifier, staticModifier, createAccessModifiersPanel() });
        result.setBorder(BorderFactory.createTitledBorder("Modifiers:"));
        return result;
    }

    private JPanel createAccessModifiersPanel() {
        JRadioButton aPrivate = createForAccessModifier(BinModifier.PRIVATE, 't');
        JRadioButton aPackagePrivate = createForAccessModifier(BinModifier.PACKAGE_PRIVATE, 'a');
        JRadioButton aProtected = createForAccessModifier(BinModifier.PROTECTED, 'r');
        JRadioButton aPublic = createForAccessModifier(BinModifier.PUBLIC, 'u');
        JPanel result = SwingUtil.combineInNorth(new JComponent[] { aPrivate, aPackagePrivate, aProtected, aPublic });
        return result;
    }

    private JRadioButton createForAccessModifier(final int accessModifier, char mnemonic) {
        JRadioButton result = new JRadioButton(new BinModifierFormatter(accessModifier, true).print());
        ActionListener userInputUpdater = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                userInput.setAccessModifiers(accessModifier);
            }
        };
        result.addActionListener(userInputUpdater);
        result.setSelected(userInput.getAccessModifiers() == accessModifier);
        result.setMnemonic(mnemonic);
        accessModifiersGroup.add(result);
        return result;
    }

    private JPanel createNewNamePanel() {
        newName.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                userInput.notifyNameChanged(newName.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                userInput.notifyNameChanged(newName.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                userInput.notifyNameChanged(newName.getText());
            }
        });
        JPanel result = SwingUtil.combineInNorth(new JComponent[] { newName });
        result.setBorder(BorderFactory.createTitledBorder("Field name:"));
        return result;
    }

    public void setName(String name) {
        newName.setText(name);
    }

    public void setFinalModifierEnabled(boolean b) {
        finalModifier.setEnabled(b);
    }

    public void setFinalModifierChecked(boolean b) {
        finalModifier.setSelected(b);
    }

    public void setStaticModifierEnabled(boolean b) {
        staticModifier.setEnabled(b);
    }

    public void setStaticModifierChecked(boolean b) {
        staticModifier.setSelected(b);
    }

    public void setOkButtonEnabled(boolean b) {
        ok.setEnabled(b);
    }

    private JPanel createButtonPanel() {
        JPanel result = new JPanel();
        result.add(ok);
        result.add(cancel);
        result.add(help);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                userInput.okPressed();
                dialog.dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        return result;
    }

    public void addInitializeLocation(final FieldInitialization initLocation, boolean enabled, boolean selected, char mnemonic) {
        JRadioButton radioButton = new JRadioButton(initLocation.getDisplayName());
        radioButton.setEnabled(enabled);
        radioButton.setSelected(selected);
        radioButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                userInput.setInitializeLocation(initLocation);
            }
        });
        radioButton.setMnemonic(mnemonic);
        initLocationsPane.add(radioButton);
        initLocationsGroup.add(radioButton);
    }
}
