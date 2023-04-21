package org.javagui.swing.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import org.javagui.util.BeanUtils;

/**
 * @author dranson	Email: dranson@163.com
 * @date 2008-6-13
 */
public class ComboBox extends JComboBox {

    private static final long serialVersionUID = -7089150509525521449L;

    /**
	 * 父类对象
	 */
    private Object parentUI;

    /**
	 * 数据模型
	 */
    private Object element;

    /**
	 * 绑定对象属性
	 */
    private String property;

    /**
	 * 下拉框数据键值对应
	 */
    private Map<String, String> map;

    /**
	 * 
	 */
    public ComboBox() {
        super();
        init();
    }

    /**
	 * @param model
	 */
    public ComboBox(ComboBoxModel model) {
        super(model);
        init();
    }

    /**
	 * @param items
	 */
    public ComboBox(Object[] items) {
        super(items);
        init();
    }

    /**
	 * @param items
	 */
    public ComboBox(Vector<?> items) {
        super(items);
        init();
    }

    /**
	 * @param items
	 */
    public ComboBox(Map<String, String> items) {
        super(items.values().toArray());
        this.map = items;
        init();
    }

    private void init() {
        this.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        refresh();
                        transferFocus();
                        break;
                    case KeyEvent.VK_ENTER:
                        transferFocus();
                        break;
                }
            }
        });
        this.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Object item = getItemAt(getSelectedIndex());
                if (map == null) {
                    BeanUtils.setPropertyValue(element, getProperty(), item);
                } else {
                    for (String key : map.keySet()) {
                        if (map.get(key).equals(item.toString())) {
                            BeanUtils.setPropertyValue(element, getProperty(), key);
                            break;
                        }
                    }
                }
            }
        });
    }

    public void setItems(Map<String, String> items) {
        if (items != null) {
            this.map = items;
            this.removeAllItems();
            Object[] values = items.values().toArray();
            for (Object value : values) this.addItem(value);
        }
    }

    /**
	 * @return the parentUI
	 */
    public Object getParentUI() {
        return parentUI;
    }

    /**
	 * @param parentUI the parentUI to set
	 */
    public void setParentUI(Object parentUI) {
        this.parentUI = parentUI;
    }

    /**
	 * @return the element
	 */
    public Object getInput() {
        return element;
    }

    /**
	 * @param element the element to set
	 */
    public void setInput(Object element) {
        this.element = element;
        refresh();
    }

    /**
	 * @return the property
	 */
    public String getProperty() {
        return property;
    }

    /**
	 * @param property the property to set
	 */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
	 * 刷新数据模型
	 */
    public void refresh() {
        if (element != null && getProperty() != null) {
            String value = BeanUtils.toString(BeanUtils.getPropertyValue(element, getProperty()));
            if (value != null) {
                for (int i = 0; i < getItemCount(); i++) {
                    if (map == null) {
                        if (getItemAt(i).toString().equals(value)) {
                            this.setSelectedIndex(i);
                            break;
                        }
                    } else {
                        if (getItemAt(i).toString().equals(map.get(value))) {
                            this.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }
    }
}
