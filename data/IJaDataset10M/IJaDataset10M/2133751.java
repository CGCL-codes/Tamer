package org.javagui.swt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.javagui.IDataProvider;
import org.javagui.IEventTask;
import org.javagui.annotation.Event;
import org.javagui.annotation.Provider;
import org.javagui.swt.widgets.TableViewer;
import org.javagui.util.BeanUtils;
import org.javagui.util.Message;

/**
 * @author dranson	Email: dranson@163.com
 * @date 2008-6-11
 */
public class TableEventTask implements IEventTask {

    /**
	 * 数据规则辅助对象
	 */
    private IDataProvider dataProvider;

    /**
	 * 表格
	 */
    private TableViewer table;

    /**
	 * 消息显示标签
	 */
    private Label lblMessage;

    /**
	 * 表格数据行集合
	 */
    private List<Object> list;

    /**
	 * 列索引
	 */
    private int col;

    /**
	 * 数据映射表
	 */
    private Map<Integer, String> map;

    /**
	 * 构造函数
	 * @param dataProvider
	 * @param table
	 * @param lblMessage
	 * @param col
	 */
    public TableEventTask(IDataProvider dataProvider, TableViewer table, int col, Label lblMessage) {
        this.dataProvider = dataProvider;
        this.table = table;
        this.lblMessage = lblMessage;
        this.col = col;
        map = new HashMap<Integer, String>();
    }

    public void addKeyCode(int keyCode, String dialog) {
        if (dialog.length() > 0) map.put(keyCode, dialog);
    }

    @SuppressWarnings("unchecked")
    public void keyEvent(Object keyEvent) {
        KeyEvent e = (KeyEvent) keyEvent;
        Set<Integer> sets = map.keySet();
        for (Integer keyCode : sets) {
            if (e.keyCode == keyCode) {
                int row = table.getTable().getSelectionIndex();
                Object value = BeanUtils.invoke(BeanUtils.newInstance(map.get(keyCode), table.getTable().getShell()), "open");
                if (value != null) {
                    if (list == null) list = (List<Object>) table.getInput();
                    Object element = list.get(row);
                    BeanUtils.setPropertyValue(element, table.getProperty(col), value);
                    table.update(element, null);
                }
                SWTUtils.editElement(table, row, col);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public String validate(Object keyEvent) {
        KeyEvent e = (KeyEvent) keyEvent;
        String error = null;
        if (list == null) list = (List<Object>) table.getInput();
        if (dataProvider != null) {
            String property = table.getProperty(col);
            Text text = (Text) table.getCellEditor(col).getControl();
            String value = text.getText().trim();
            int row = table.getTable().getSelectionIndex();
            if (!table.getCellEditor(col).isActivated()) {
                row = list.size() - 1;
                value = BeanUtils.toString(BeanUtils.getPropertyValue(list.get(row), property));
            }
            Class<?> clazz = list.get(row).getClass();
            Provider provider = BeanUtils.getPropertyAnnotation(Provider.class, clazz, property);
            if (provider == null) return null;
            boolean nullable = true;
            nullable = provider.nullable();
            if (!nullable && (value.length() == 0 || value == null)) {
                error = Message.getString(clazz, property) + Message.getString("nullable");
            } else {
                if (provider.primary()) {
                    for (int i = 0; i < list.size(); i++) {
                        if (value.equals(BeanUtils.toString(BeanUtils.getPropertyValue(list.get(i), property))) && i != row) {
                            error = Message.getString(clazz, property) + Message.getString("primary");
                            break;
                        }
                    }
                }
                if (error == null || error.length() == 0) {
                    if (!text.isFocusControl()) value = BeanUtils.toString(BeanUtils.getPropertyValue(list.get(row), property));
                    error = dataProvider.isValid(list.get(row), value);
                }
            }
            if (error == null || error.length() == 0) {
                text.setBackground(SWTUtils.getDefaultColor());
                if (lblMessage != null && lblMessage.getForeground().equals(SWTUtils.getErrorColor())) {
                    lblMessage.setText("");
                    lblMessage.setForeground(SWTUtils.getMessageColor());
                }
                if (value != null && value.length() > 0 && table.getCellEditor(col).isActivated()) {
                    Event annot = BeanUtils.getPropertyAnnotation(Event.class, clazz, property);
                    if (annot != null && annot.type() == Event.TYPE_VALUE) {
                        int var = Integer.parseInt(value);
                        if (var > annot.value()) {
                            table.getCellEditor(col).deactivate();
                            BeanUtils.setPropertyValue(list.get(row), property, value);
                            BeanUtils.invoke(BeanUtils.newInstance(annot.dialog(), text.getShell(), list.get(row)), "open");
                        }
                    }
                }
                if (e != null) focusMove(e);
            } else {
                text.setBackground(SWTUtils.getErrorColor());
                SWTUtils.editElement(table, row, col);
                if (lblMessage == null) {
                    Control control = table.getCellEditor(col).getControl();
                    ToolTip tip = new ToolTip(control.getShell(), SWT.BALLOON);
                    Point point = control.toDisplay(control.getSize());
                    tip.setLocation(point);
                    tip.setMessage(error);
                    tip.setVisible(true);
                } else {
                    lblMessage.setText(error);
                    lblMessage.setForeground(SWTUtils.getErrorColor());
                }
            }
        } else {
            if (e != null) focusMove(e);
        }
        return error;
    }

    public void focusGained() {
        if (dataProvider != null && dataProvider.getMessage() != null) {
            if (!lblMessage.getForeground().equals(SWTUtils.getErrorColor())) {
                lblMessage.setText(dataProvider.getMessage());
                lblMessage.setForeground(SWTUtils.getMessageColor());
            }
        }
    }

    public void focusLost() {
        if (dataProvider != null && dataProvider.getMessage() != null) {
            if (!lblMessage.getForeground().equals(SWTUtils.getErrorColor())) lblMessage.setText("");
        }
    }

    private void focusMove(KeyEvent e) {
        int count = table.getTable().getColumnCount();
        int row = table.getTable().getSelectionIndex();
        if (e.character == SWT.CR && e.keyCode != SWT.CR) e.keyCode = SWT.CR;
        switch(e.keyCode) {
            case SWT.ARROW_LEFT:
                boolean faild = true;
                for (int i = col - 1; i > 0; i--) {
                    String property = table.getProperty(i);
                    if (table.getCellModifier().canModify(table.getElementAt(row), property)) {
                        SWTUtils.editElement(table, row, i);
                        faild = false;
                        break;
                    }
                }
                if (faild && row - 1 >= 0) {
                    for (int i = count - 1; i > col; i--) {
                        String property = table.getProperty(i);
                        if (table.getCellModifier().canModify(table.getElementAt(row), property)) {
                            SWTUtils.editElement(table, row - 1, i);
                            break;
                        }
                    }
                }
                break;
            case SWT.CR:
                faild = true;
                for (int i = col + 1; i < count; i++) {
                    String property = table.getProperty(i);
                    if (table.getCellModifier().canModify(table.getElementAt(row), property)) {
                        SWTUtils.editElement(table, row, i);
                        faild = false;
                        break;
                    }
                }
                if (faild) {
                    if (list.size() <= row + 1) {
                        if (SWTUtils.checkRow(table, col)) {
                            if (table.getDefaultElement() == null) {
                                Object obj = BeanUtils.copy(list.get(row));
                                Text text = (Text) table.getCellEditor(col).getControl();
                                String property = table.getProperty(col);
                                String value = text.getText().trim();
                                BeanUtils.setPropertyValue(obj, property, value);
                                table.add(obj);
                            } else {
                                table.add(BeanUtils.copy(table.getDefaultElement()));
                            }
                        }
                    }
                    for (int i = 1; i < col; i++) {
                        String property = table.getProperty(i);
                        if (table.getCellModifier().canModify(table.getElementAt(row), property)) {
                            SWTUtils.editElement(table, row + 1, i);
                            break;
                        }
                    }
                }
                break;
            case SWT.ARROW_UP:
                faild = true;
                for (int i = col - 1; i > 0; i--) {
                    boolean nullable = true;
                    String property = table.getProperty(i);
                    Provider annot = BeanUtils.getPropertyAnnotation(Provider.class, list.get(row).getClass(), property);
                    if (annot != null) nullable = annot.nullable();
                    String value = BeanUtils.toString(BeanUtils.getPropertyValue(list.get(row), property));
                    if (table.getCellModifier().canModify(table.getElementAt(row), property) && !nullable && (value == null || value.length() == 0)) {
                        SWTUtils.editElement(table, row, i);
                        faild = false;
                        break;
                    }
                }
                if (faild && row - 1 >= 0) {
                    for (int i = count - 1; i > col; i--) {
                        boolean nullable = true;
                        String property = table.getProperty(i);
                        Provider annot = BeanUtils.getPropertyAnnotation(Provider.class, list.get(row).getClass(), property);
                        if (annot != null) nullable = annot.nullable();
                        String value = BeanUtils.toString(BeanUtils.getPropertyValue(list.get(row - 1), property));
                        if (table.getCellModifier().canModify(table.getElementAt(row), property) && !nullable && (value == null || value.length() == 0)) {
                            SWTUtils.editElement(table, row - 1, i);
                            break;
                        }
                    }
                }
                break;
            case SWT.ARROW_DOWN:
                faild = true;
                for (int i = col + 1; i < count; i++) {
                    boolean nullable = true;
                    String property = table.getProperty(i);
                    Provider annot = BeanUtils.getPropertyAnnotation(Provider.class, list.get(row).getClass(), property);
                    if (annot != null) nullable = annot.nullable();
                    String value = BeanUtils.toString(BeanUtils.getPropertyValue(list.get(row), property));
                    if (table.getCellModifier().canModify(table.getElementAt(row), property) && !nullable && (value == null || value.length() == 0)) {
                        SWTUtils.editElement(table, row, i);
                        faild = false;
                        break;
                    }
                }
                if (faild) {
                    if (list.size() <= row + 1) {
                        if (SWTUtils.checkRow(table, col)) {
                            if (table.getDefaultElement() == null) {
                                Object obj = BeanUtils.copy(list.get(row));
                                Text text = (Text) table.getCellEditor(col).getControl();
                                String value = text.getText().trim();
                                BeanUtils.setPropertyValue(obj, table.getProperty(col), value);
                                table.add(obj);
                                for (int i = 1; i < col; i++) {
                                    String property = table.getProperty(i);
                                    if (table.getCellModifier().canModify(table.getElementAt(row), property)) {
                                        SWTUtils.editElement(table, row + 1, i);
                                        break;
                                    }
                                }
                            } else {
                                table.add(BeanUtils.copy(table.getDefaultElement()));
                            }
                        }
                    }
                    for (int i = 1; i < col; i++) {
                        boolean nullable = true;
                        String property = table.getProperty(i);
                        Provider annot = BeanUtils.getPropertyAnnotation(Provider.class, list.get(row).getClass(), property);
                        if (annot != null) nullable = annot.nullable();
                        String value = BeanUtils.toString(BeanUtils.getPropertyValue(list.get(row + 1), property));
                        if (table.getCellModifier().canModify(table.getElementAt(row), property) && !nullable && (value == null || value.length() == 0)) {
                            SWTUtils.editElement(table, row + 1, i);
                            break;
                        }
                    }
                }
                break;
            case SWT.HOME:
                SWTUtils.editElement(table, row, 1);
                break;
            case SWT.END:
                SWTUtils.editElement(table, row, count - 1);
                break;
        }
    }
}
