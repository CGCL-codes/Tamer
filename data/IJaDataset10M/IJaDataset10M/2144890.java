package com.g2d.studio.scene.script;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import com.cell.CUtil;
import com.cell.rpg.scene.script.Scriptable;
import com.cell.rpg.scene.script.anno.EventType;
import com.cell.rpg.scene.script.trigger.Event;
import com.g2d.awt.util.AbstractOptionDialog;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.Studio;

@SuppressWarnings("serial")
public class SelectEventDialog extends AbstractOptionDialog<Class<? extends Event>> {

    SelectEventCombobox list;

    JButton btn_ok = new JButton();

    public SelectEventDialog(Component owner, Class<? extends Scriptable> trigger_unit_type) {
        super(owner);
        super.setSize(400, 200);
        this.list = new SelectEventCombobox(trigger_unit_type, SelectEventCombobox.last_selected);
        JPanel pan = new JPanel();
        pan.add(list);
        this.add(pan, BorderLayout.CENTER);
    }

    @Override
    protected boolean checkOK() {
        return true;
    }

    @Override
    protected Class<? extends Event> getUserObject(ActionEvent e) {
        return list.getValue();
    }

    public static class SelectEventCombobox extends JComboBox implements PropertyCellEdit<Class<? extends Event>> {

        private static final long serialVersionUID = 1L;

        private static Class<? extends Event> last_selected = null;

        ObjectPropertyEdit panel;

        public SelectEventCombobox(Class<? extends Scriptable> trigger_unit_type, Class<? extends Event> default_value) {
            super(getList(trigger_unit_type));
            try {
                if (default_value != null) {
                    setSelectedItem(default_value);
                }
            } catch (Exception err) {
            }
        }

        public Component getComponent(ObjectPropertyEdit panel) {
            this.panel = panel;
            return this;
        }

        public Class<? extends Event> getValue() {
            Object item = getSelectedItem();
            if (item != null) {
                last_selected = ((EventTypeItem) item).type;
                return ((EventTypeItem) item).type;
            }
            return null;
        }

        static Vector<EventTypeItem> getList(Class<? extends Scriptable> trigger_unit_type) {
            Vector<EventTypeItem> ret = new Vector<EventTypeItem>();
            if (trigger_unit_type != null) {
                for (Class<? extends Event> evt : Studio.getInstance().getSceneScriptManager().getEvents(trigger_unit_type)) {
                    ret.add(new EventTypeItem(evt));
                }
            } else {
                for (Class<? extends Event> evt : Studio.getInstance().getSceneScriptManager().getEvents()) {
                    ret.add(new EventTypeItem(evt));
                }
            }
            Collections.sort(ret);
            return ret;
        }

        static class EventTypeItem implements Comparable<EventTypeItem> {

            Class<? extends Event> type;

            public EventTypeItem(Class<? extends Event> type) {
                this.type = type;
            }

            @Override
            public String toString() {
                EventType tp = type.getAnnotation(EventType.class);
                return tp.comment();
            }

            @Override
            public int compareTo(EventTypeItem o) {
                return CUtil.getStringCompare().compare(o.type.getName(), this.type.getName());
            }
        }
    }
}
