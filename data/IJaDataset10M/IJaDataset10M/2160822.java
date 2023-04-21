package ch.intertec.storybook.toolkit.swing.panel;

import javax.swing.JCheckBox;
import ch.intertec.storybook.model.hbn.entity.AbstractEntity;

/**
 * @author martin
 * 
 */
public abstract class CbPanelDecorator {

    protected CheckBoxPanel panel;

    public CbPanelDecorator() {
    }

    public abstract void decorateBeforeEntity(AbstractEntity entity);

    public abstract void decorateEntity(JCheckBox cb, AbstractEntity entity);

    public abstract void decorateAfterEntity(AbstractEntity entity);

    public CheckBoxPanel getPanel() {
        return panel;
    }

    public void setPanel(CheckBoxPanel panel) {
        this.panel = panel;
    }
}
