package org.javagui.swing;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.javagui.IDataProvider;
import org.javagui.IEventTask;
import org.javagui.annotation.Event;
import org.javagui.annotation.Provider;
import org.javagui.util.BeanUtils;
import org.javagui.util.Message;

/**
 * @author dranson	Email: dranson@163.com
 * @date 2008-6-11
 */
public class FormEventTask implements IEventTask {

    /**
	 * 数据规则辅助对象
	 */
    private IDataProvider dataProvider;

    /**
	 * 文本框
	 */
    private JTextComponent text;

    /**
	 * 消息显示标签
	 */
    private JLabel lblMessage;

    /**
	 * 数据映射表
	 */
    private Map<Integer, String> map;

    /**
	 * 构造函数
	 * @param dataProvider
	 * @param textViewer
	 * @param lblMessage
	 */
    public FormEventTask(IDataProvider dataProvider, JTextComponent text, JLabel lblMessage) {
        this.dataProvider = dataProvider;
        this.text = text;
        this.lblMessage = lblMessage;
        map = new HashMap<Integer, String>();
    }

    public void addKeyCode(int keyCode, String dialog) {
        if (dialog.length() > 0) map.put(keyCode, dialog);
    }

    public void keyEvent(Object keyEvent) {
        KeyEvent e = (KeyEvent) keyEvent;
        Set<Integer> sets = map.keySet();
        for (Integer keyCode : sets) {
            if (e.getKeyCode() == keyCode) {
                Object value = BeanUtils.invoke(BeanUtils.newInstance(map.get(keyCode), SwingUtilities.getRoot(text)), "open");
                if (value != null) {
                    Object element = BeanUtils.invoke(text, "getInput");
                    BeanUtils.setPropertyValue(element, (String) BeanUtils.invoke(text, "getProperty"), value);
                    BeanUtils.invoke(text, "refresh");
                }
            }
        }
    }

    public String validate(Object keyEvent) {
        String error = null;
        String property = (String) BeanUtils.invoke(text, "getProperty");
        String value = text.getText();
        if (dataProvider == null) {
            BeanUtils.setPropertyValue(BeanUtils.invoke(text, "getInput"), property, value);
            if (BeanUtils.invoke(text, "getParentUI") == null) {
                SwingUtils.refreshControl(text.getParent());
            } else {
                SwingUtils.refreshUI(BeanUtils.invoke(text, "getParentUI"));
            }
        } else {
            Class<?> clazz = BeanUtils.invoke(text, "getInput").getClass();
            Provider provider = BeanUtils.getPropertyAnnotation(Provider.class, clazz, property);
            boolean nullable = true;
            if (provider != null) nullable = provider.nullable();
            if (!nullable && (value == null || value.length() == 0)) {
                error = Message.getString(clazz, property) + Message.getString("nullable");
            } else {
                error = BeanUtils.setPropertyValue(BeanUtils.newInstance(BeanUtils.invoke(text, "getInput").getClass()), property, value);
                if (error == null) {
                    error = dataProvider.isValid(BeanUtils.invoke(text, "getInput"), value);
                } else {
                    error = Message.getString("format");
                }
            }
            if (error == null) {
                BeanUtils.invoke(text, "hideToolTip");
                text.setBackground(SwingUtils.getDefaultColor());
                if (lblMessage != null) {
                    if (lblMessage.getForeground().equals(SwingUtils.getErrorColor())) {
                        lblMessage.setText("");
                        lblMessage.setForeground(SwingUtils.getMessageColor());
                    }
                }
                if (value != null && value.length() > 0) {
                    Event annot = BeanUtils.getPropertyAnnotation(Event.class, clazz, property);
                    if (annot != null && annot.type() == Event.TYPE_VALUE) {
                        int var = Integer.parseInt(value);
                        if (var > annot.value()) {
                            BeanUtils.setPropertyValue(BeanUtils.invoke(text, "getInput"), property, value);
                            BeanUtils.invoke(BeanUtils.newInstance(annot.dialog(), SwingUtilities.getRoot(text), BeanUtils.invoke(text, "getInput")), "open");
                        }
                    }
                }
                BeanUtils.setPropertyValue(BeanUtils.invoke(text, "getInput"), property, value);
                BeanUtils.invoke(text, "refresh");
                if (BeanUtils.invoke(text, "getParentUI") == null) {
                    SwingUtils.refreshControl(text.getParent());
                } else {
                    SwingUtils.refreshUI(BeanUtils.invoke(text, "getParentUI"));
                }
            } else {
                text.setBackground(SwingUtils.getErrorColor());
                if (lblMessage == null) {
                    BeanUtils.invoke(text, "showToolTip", error);
                } else {
                    lblMessage.setText(error);
                    lblMessage.setForeground(SwingUtils.getErrorColor());
                }
                text.requestFocus();
            }
        }
        return error;
    }

    public void focusGained() {
        if (dataProvider != null && dataProvider.getMessage() != null) {
            if (lblMessage == null) {
                if (text.getToolTipText() == null) text.setToolTipText(dataProvider.getMessage());
            } else {
                if (!lblMessage.getForeground().equals(SwingUtils.getErrorColor())) {
                    lblMessage.setText(dataProvider.getMessage());
                    lblMessage.setForeground(SwingUtils.getMessageColor());
                }
            }
        }
    }

    public void focusLost() {
        if (lblMessage != null && lblMessage.getForeground().equals(SwingUtils.getErrorColor())) lblMessage.setText("");
    }
}
