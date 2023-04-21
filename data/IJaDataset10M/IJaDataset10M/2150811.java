package com.reactiveplot.programs.editor.nodeeditors;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import com.reactiveplot.library.editor.nodes.SequenceNode;
import com.reactiveplot.library.events.SetStateEvent;
import com.reactiveplot.library.scripts.Script;
import com.reactiveplot.programs.editor.AbstractScriptEditor;
import com.reactiveplot.programs.editor.nodeeditors.GenericItemEditor.EditableItem;

public class SetStateEditor extends JComponent implements MiniEditor, GenericNodeEditor {

    private static final long serialVersionUID = -6389931600860024443L;

    SequenceNode sequenceNode = null;

    SetStateEvent setStateEvent = null;

    GenericItemEditor setStateItemEditor = null;

    Script script = null;

    AbstractScriptEditor topLevelEditor = null;

    public SetStateEditor(SequenceNode sequenceNode, SetStateEvent s, Script script, AbstractScriptEditor topLevelEditor) {
        this.sequenceNode = sequenceNode;
        this.setStateEvent = s;
        this.script = script;
        this.topLevelEditor = topLevelEditor;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        createSetStateItemEditor();
    }

    void createSetStateItemEditor() {
        if (setStateItemEditor != null) this.remove(setStateItemEditor);
        ArrayList<EditableItem> items = new ArrayList<EditableItem>();
        items.add(EditableItem.CHARACTER_ID);
        items.add(EditableItem.FREE_FORM);
        items.add(EditableItem.LABEL);
        items.add(EditableItem.BOOLEAN);
        setStateItemEditor = new GenericItemEditor("Set", items, this, script);
        ArrayList<String> values = new ArrayList<String>();
        values.add(setStateEvent.getTarget());
        values.add(setStateEvent.getStateID());
        values.add("to");
        Boolean b = setStateEvent.getRemoveTheState();
        values.add(b.toString());
        setStateItemEditor.setValues(values);
        this.add(setStateItemEditor);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void updateViewForChangedNode() {
        createSetStateItemEditor();
    }

    @Override
    public void updateModelWithButtonPush(int itemNumber, String buttonText, GenericItemEditor itemEditor) {
    }

    @Override
    public void updateModelWithNewValuesFromItemEditor(List<String> values, GenericItemEditor itemEditor) {
        setStateEvent.setTargetID(values.get(0));
        setStateEvent.setTargetState(values.get(1));
        Boolean b = new Boolean(values.get(3));
        setStateEvent.setRemoveStateStatus(b);
        topLevelEditor.updateForChangedScriptText();
    }
}
