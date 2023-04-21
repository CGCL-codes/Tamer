package org.chess.quasimodo.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.chess.quasimodo.domain.EngineModel;
import org.chess.quasimodo.engine.model.IdOptions;
import org.chess.quasimodo.engine.model.Option;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.util.ViewUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Scope("prototype")
@Component("enginePropertiesDialog")
public class EnginePropertiesDialog extends AbstractDialogForm<EngineModel> {

    /**
	 * Serial Id.
	 */
    private static final long serialVersionUID = 5933049722274088737L;

    @Autowired
    EventPublisherAdapter eventPublisher;

    private JPanel contentPanel = new JPanel();

    private JScrollPane scrollPane;

    private JPanel rightPanel;

    private JLabel nameLabelValue;

    private JLabel autorLabelValue;

    private JTextField bookFileTextField;

    /**
	 * Create the dialog.
	 */
    public EnginePropertiesDialog() {
        initialize();
    }

    private void initialize() {
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Engine Properties");
        setBounds(100, 100, 513, 542);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            scrollPane = new JScrollPane();
            scrollPane.getVerticalScrollBar().setUnitIncrement(15);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        {
            rightPanel = new JPanel();
            rightPanel.setPreferredSize(new Dimension(350, 450));
            {
                JLabel nameLabel = new JLabel("Name:");
                nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
                nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                nameLabel.setPreferredSize(new Dimension(160, 20));
                rightPanel.add(nameLabel);
            }
            {
                nameLabelValue = new JLabel();
                nameLabelValue.setPreferredSize(new Dimension(300, 20));
                rightPanel.add(nameLabelValue);
            }
            {
                JLabel authorLabel = new JLabel("Author:");
                authorLabel.setPreferredSize(new Dimension(160, 20));
                authorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                authorLabel.setHorizontalAlignment(SwingConstants.LEFT);
                rightPanel.add(authorLabel);
            }
            {
                autorLabelValue = new JLabel();
                autorLabelValue.setPreferredSize(new Dimension(300, 20));
                rightPanel.add(autorLabelValue);
            }
            scrollPane.setViewportView(rightPanel);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setPreferredSize(new Dimension(10, 40));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton saveButton = new JButton("Save");
                saveButton.setBounds(183, 8, 67, 23);
                saveButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        saveButtonActionPerformed(e);
                    }
                });
                buttonPane.setLayout(null);
                saveButton.setMinimumSize(new Dimension(67, 23));
                saveButton.setMaximumSize(new Dimension(67, 23));
                saveButton.setPreferredSize(new Dimension(67, 23));
                saveButton.setActionCommand("Save");
                buttonPane.add(saveButton);
                getRootPane().setDefaultButton(saveButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setBounds(262, 8, 67, 23);
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                cancelButton.setActionCommand("Cancel");
                cancelButton.setMinimumSize(new Dimension(67, 23));
                cancelButton.setMaximumSize(new Dimension(67, 23));
                cancelButton.setPreferredSize(new Dimension(67, 23));
                cancelButton.setMargin(new Insets(0, 0, 0, 0));
                buttonPane.add(cancelButton);
            }
        }
        setLocationRelativeTo(null);
    }

    private void addOption(Option option) {
        addOptionLabel(option);
        addOptionValue(option);
    }

    private void addOptionLabel(Option option) {
        JLabel label = new JLabel(option.name);
        label.setPreferredSize(new Dimension(250, 20));
        rightPanel.add(label);
    }

    private void addOptionValue(Option option) {
        JComponent component;
        if (option.isTypeSpin()) {
            component = new JSpinner();
            ((JSpinner) component).setValue(option.value != null ? Integer.parseInt(option.value) : Integer.parseInt(option.defaultValue));
        } else if (option.isTypeCheck()) {
            component = new JCheckBox();
            ((JCheckBox) component).setSelected(option.value != null ? Boolean.parseBoolean(option.value) : Boolean.parseBoolean(option.defaultValue));
        } else if (option.isTypeCombo()) {
            List<String> items = new ArrayList<String>();
            for (String s : option.varLine.split("\\s")) {
                if (!"var".equals(s)) {
                    items.add(s);
                }
            }
            component = new JComboBox(items.toArray());
            ((JComboBox) component).setSelectedItem(option.defaultValue);
        } else {
            component = new JTextField(option.defaultValue);
        }
        component.setName(option.name);
        component.setPreferredSize(new Dimension(200, 20));
        rightPanel.add(component);
    }

    @Override
    public void commit() {
        IdOptions idOptions = model.getIdOptions();
        nameLabelValue.setText(idOptions.getName());
        autorLabelValue.setText(idOptions.getAuthor());
        rightPanel.setPreferredSize(new Dimension(rightPanel.getWidth(), 100 + 24 * (idOptions.getOptions().size() + 2) + 2));
        for (Option option : idOptions.getOptions()) {
            addOption(option);
        }
        JLabel label = new JLabel("Custom properties");
        label.setPreferredSize(new Dimension(450, 20));
        rightPanel.add(label);
        JLabel bookLabel = new JLabel("Use openingBook file");
        bookLabel.setPreferredSize(new Dimension(250, 20));
        rightPanel.add(bookLabel);
        rightPanel.add(getBookFileTextField());
        final JButton btnChooseBook = new JButton("...");
        btnChooseBook.setPreferredSize(new Dimension(15, 15));
        btnChooseBook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(btnChooseBook);
                if (result == JFileChooser.APPROVE_OPTION) {
                    bookFileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        rightPanel.add(btnChooseBook);
    }

    private JTextField getBookFileTextField() {
        if (bookFileTextField == null) {
            bookFileTextField = new JTextField(model.getBookFilePath());
            bookFileTextField.setName("bookFilePath");
            bookFileTextField.setPreferredSize(new Dimension(180, 20));
        }
        return bookFileTextField;
    }

    @Override
    public void updateModel() {
        IdOptions idOptions = model.getIdOptions();
        for (Option option : idOptions.getOptions()) {
            setOptionValue(ViewUtils.getComponent(rightPanel, option.name), option);
        }
        model.setBookFilePath(bookFileTextField.getText());
    }

    private void setOptionValue(java.awt.Component component, Option option) {
        System.out.println("component: " + component);
        if (option.isTypeSpin()) {
            option.value = ((JSpinner) component).getValue().toString();
        } else if (option.isTypeCheck()) {
            option.value = String.valueOf(((JCheckBox) component).getSelectedObjects() != null);
        } else if (option.isTypeCombo()) {
            option.value = ((JComboBox) component).getSelectedItem().toString();
        } else {
            option.value = ((JTextField) component).getText().trim();
        }
    }

    protected void saveButtonActionPerformed(ActionEvent e) {
        updateModel();
        eventPublisher.publishCommandEvent(e.getSource(), this, CommandEvent.Command.SAVE_ENGINE_OPTIONS);
        dispose();
    }

    protected void cancelButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    @Override
    protected Validator getValidator() {
        return null;
    }
}
