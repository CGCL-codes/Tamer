package org.jgenesis.swing;

import java.util.Collection;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import org.jgenesis.bean.BeanWrapper;
import org.jgenesis.beanset.BeanManager;
import org.jgenesis.beanset.BeanSet;
import org.jgenesis.beanset.BeanSetEvent;
import org.jgenesis.beanset.BeanSetListener;
import org.jgenesis.beanset.DefaultBeanSetListener;
import org.jgenesis.swing.models.BeanLookupComboBoxModel;

/**
 * @author root
 * @deprecated use JGComboBox
 */
public class JGLookupComboBox extends JComboBox implements AutoEditable {

    private BeanLookupComboBoxModel beanComboBoxModel;

    private int editableMode;

    private BeanSetListener beanListener = new DefaultBeanSetListener() {

        public void itemChanged(BeanSetEvent event) {
            boolean empty = beanComboBoxModel.getTargetBeanSet().isEmpty();
            if ((!empty) != JGLookupComboBox.this.isEnabled()) {
                JGLookupComboBox.this.setEnabled(!empty);
                JGLookupComboBox.this.setFocusable(!empty);
            }
        }

        public void beanSetChanged(BeanSetEvent event) {
            itemChanged(event);
        }

        public void stateChanged(BeanSetEvent event) {
            itemChanged(event);
        }

        public void deleteItem(BeanSetEvent event) {
            itemChanged(event);
        }

        public void beforeCancel(BeanSetEvent event) {
            itemChanged(event);
        }
    };

    /**
	 * 
	 */
    public JGLookupComboBox() {
        init();
    }

    private void init() {
        this.setModel(new BeanLookupComboBoxModel());
        this.setEnabled(false);
    }

    public void setModel(ComboBoxModel aModel) {
        super.setModel(aModel);
        if (aModel instanceof BeanLookupComboBoxModel) {
            this.beanComboBoxModel = (BeanLookupComboBoxModel) aModel;
            if (this.beanComboBoxModel.getTargetBeanSet() != null) this.beanComboBoxModel.getTargetBeanSet().addBeanSetListener(this.beanListener);
        }
    }

    public void setTargetFieldName(String fieldName) {
        this.beanComboBoxModel.setTargetFieldName(fieldName);
    }

    public String getTargetFieldName() {
        return this.beanComboBoxModel.getTargetFieldName();
    }

    public void setSourceFieldName(String fieldName) {
        this.beanComboBoxModel.setFieldNameData(fieldName);
    }

    public String getSourceFieldName() {
        return this.beanComboBoxModel.getFieldNameData();
    }

    public void setFieldNamesDisplay(String fieldName) {
        this.beanComboBoxModel.setFieldNamesDisplay(fieldName);
    }

    public String getFieldNamesDisplay() {
        return this.beanComboBoxModel.getFieldNamesDisplay();
    }

    public void setTargetBeanManager(BeanManager targetBeanManager) {
        if (this.beanComboBoxModel.getTargetBeanSet() != null) this.beanComboBoxModel.getTargetBeanSet().removeBeanSetListener(beanListener);
        this.beanComboBoxModel.setTargetBeanManager(targetBeanManager);
        if (this.beanComboBoxModel.getTargetBeanSet() != null) {
            this.beanComboBoxModel.getTargetBeanSet().addBeanSetListener(beanListener);
            beanListener.itemChanged(null);
        }
    }

    public BeanWrapper getCurrentBean() {
        return this.beanComboBoxModel.getCurrentBean();
    }

    public void setSourceBeanCollection(Collection sourceBeanCollection) {
        this.beanComboBoxModel.setSourceCollection(sourceBeanCollection);
    }

    public Collection getSourceBeanCollection() {
        return this.beanComboBoxModel.getSourceCollection();
    }

    public void setAllowNull(boolean allowNull) {
        this.beanComboBoxModel.setAllowNull(allowNull);
    }

    public boolean isAllowNull() {
        return this.beanComboBoxModel.isAllowNull();
    }

    public void setDelimiter(String delimiter) {
        this.beanComboBoxModel.setDelimiter(delimiter);
    }

    public String getDelimiter() {
        return this.beanComboBoxModel.getDelimiter();
    }

    public int getEditableMode() {
        return this.editableMode;
    }

    public void setEditableMode(int editableMode) {
        if (this.editableMode == editableMode) return;
        if (this.editableMode == AUTO && this.beanComboBoxModel.getTargetBeanSet() != null) this.beanComboBoxModel.getTargetBeanSet().removeBeanSetListener(beanListener);
        switch(editableMode) {
            case AUTO:
                if (this.beanComboBoxModel.getTargetBeanSet() != null) {
                    this.beanComboBoxModel.getTargetBeanSet().addBeanSetListener(beanListener);
                    beanListener.cursorMoved(null);
                }
                break;
            case EDITABLE:
                this.setEditable(true);
                this.setFocusable(true);
                break;
            case UNEDITABLE:
                this.setEditable(false);
                this.setFocusable(false);
        }
        this.editableMode = editableMode;
    }

    public void setDefaultItem(Object defaultItem) {
        this.beanComboBoxModel.setDefaultItem(defaultItem);
    }

    public Object getDefaultItem() {
        return this.beanComboBoxModel.getDefaultItem();
    }
}
