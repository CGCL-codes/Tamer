package ui.view.swing.component.detail;

import javax.swing.JTextField;
import message.MessageId;
import model.stock.ArticleGroup;
import ui.controller.manager.UIModelManager;
import ui.view.component.ArticleUI;
import ui.view.swing.SwingUI;
import ui.view.swing.util.objectpicker3.ObjectPicker3;

public class ArticleDialog extends StandardDetailDialog implements ArticleUI {

    private JTextField codeField;

    private JTextField nameField;

    private JTextField sizeField;

    private ObjectPicker3 groupPicker;

    public ArticleDialog() {
        super(MessageId.article);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        codeField = new JTextField();
        nameField = new JTextField();
        sizeField = new JTextField();
        groupPicker = new ObjectPicker3();
        centerPanel().add(SwingUI.instance().decorated(codeField, MessageId.code));
        centerPanel().add(SwingUI.instance().decorated(nameField, MessageId.name));
        centerPanel().add(SwingUI.instance().decorated(sizeField, MessageId.size));
        centerPanel().add(SwingUI.instance().decorated(groupPicker, MessageId.articleGroup));
    }

    public ArticleGroup getArticleGroup() {
        return (ArticleGroup) groupPicker.getSelection();
    }

    public String getArticleName() {
        return nameField.getText();
    }

    public void setArticleGroupManager(UIModelManager manager) {
        groupPicker.setUIModelManager(manager);
    }

    public String getCode() {
        return codeField.getText();
    }

    public String getArticleSize() {
        return sizeField.getText();
    }

    public void setArticleGroup(ArticleGroup articleGroup) {
        groupPicker.setSelection(articleGroup);
    }

    public void setArticleName(String name) {
        nameField.setText(name);
    }

    public void setCode(String code) {
        codeField.setText(code);
    }

    public void setArticleSize(String size) {
        sizeField.setText(size);
    }
}
