package org.yaoqiang.bpmn.editor.swing.handler;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.yaoqiang.bpmn.editor.action.BPMNModelActions;
import org.yaoqiang.graph.editor.action.EditorActions;
import org.yaoqiang.graph.editor.swing.GraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;

/**
 * BPMNKeyboardHandler
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class BPMNKeyboardHandler extends mxKeyboardHandler {

    public BPMNKeyboardHandler(GraphComponent graphComponent) {
        super(graphComponent);
    }

    protected InputMap getInputMap(int condition) {
        InputMap map = super.getInputMap(condition);
        if (condition == JComponent.WHEN_FOCUSED && map != null) {
            map.put(KeyStroke.getKeyStroke("control S"), "save");
            map.put(KeyStroke.getKeyStroke("control shift S"), "saveAs");
            map.put(KeyStroke.getKeyStroke("control N"), "new");
            map.put(KeyStroke.getKeyStroke("control O"), "open");
            map.put(KeyStroke.getKeyStroke("control Z"), "undo");
            map.put(KeyStroke.getKeyStroke("control Y"), "redo");
        }
        return map;
    }

    /**
	 * Return the mapping between JTree's input map and JGraph's actions.
	 */
    protected ActionMap createActionMap() {
        ActionMap map = super.createActionMap();
        map.put("save", BPMNModelActions.getSaveAction());
        map.put("saveAs", BPMNModelActions.getSaveAsAction());
        map.put("new", BPMNModelActions.getAction(BPMNModelActions.NEW));
        map.put("open", BPMNModelActions.getAction(BPMNModelActions.OPEN));
        map.put("undo", EditorActions.getAction(EditorActions.UNDO));
        map.put("redo", EditorActions.getAction(EditorActions.REDO));
        return map;
    }
}
