package com.g2d.studio.quest;

import java.awt.Component;
import javax.swing.JLabel;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.Studio;
import com.g2d.studio.swing.G2DListSelectDialog;

@SuppressWarnings("serial")
public class QuestSelectCellEdit extends G2DListSelectDialog<QuestNode> implements PropertyCellEdit<Integer> {

    JLabel cell_edit_component = new JLabel();

    /**
	 * @param not_transient 是否排除所有临时任务
	 */
    public QuestSelectCellEdit(Component owner, boolean not_transient, Integer dv) {
        super(owner, new QuestList(not_transient), Studio.getInstance().getQuestManager().getQuest(dv));
    }

    @Override
    public Component getComponent(ObjectPropertyEdit panel) {
        QuestNode node = getSelectedObject();
        cell_edit_component.setText(node + "");
        return cell_edit_component;
    }

    @Override
    public Integer getValue() {
        QuestNode node = getSelectedObject();
        if (node != null) {
            return node.getIntID();
        }
        return -1;
    }
}
