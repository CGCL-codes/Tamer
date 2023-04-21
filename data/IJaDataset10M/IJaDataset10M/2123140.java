package com.leclercb.taskunifier.gui.components.quickaddtask;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.actions.ActionAddQuickTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTaskMenu;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class QuickAddTaskPanel extends JPanel {

    private JTextField textField;

    private JButton button;

    private JButton buttonTemplate;

    public QuickAddTaskPanel() {
        this.initialize();
    }

    private void initialize() {
        this.setOpaque(false);
        this.textField = new JTextField();
        this.textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    QuickAddTaskPanel.this.addQuickTask();
                }
            }
        });
        this.button = new JButton(ImageUtils.getResourceImage("task.png", 16, 16));
        this.button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                QuickAddTaskPanel.this.addQuickTask();
            }
        });
        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ActionAddTemplateTask action = (ActionAddTemplateTask) e.getSource();
                QuickAddTaskPanel.this.addQuickTask(action.getTemplate());
            }
        };
        this.buttonTemplate = new JButton(new ActionAddTemplateTaskMenu(listener, 16, 16));
        this.buttonTemplate.setText(null);
        this.setLayout(new BorderLayout(3, 3));
        this.add(this.textField, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new BorderLayout(3, 3));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(this.button, BorderLayout.WEST);
        buttonsPanel.add(this.buttonTemplate, BorderLayout.EAST);
        this.add(buttonsPanel, BorderLayout.EAST);
    }

    private void addQuickTask() {
        String title = this.textField.getText();
        ActionAddQuickTask.addQuickTask(title, false);
        this.textField.setText("");
    }

    private void addQuickTask(TaskTemplate template) {
        String title = this.textField.getText();
        ActionAddQuickTask.addQuickTask(template, title, false);
        this.textField.setText("");
    }
}
