package planning.editor;

import javax.swing.JFrame;
import planning.file.props.ExecuterProp;

/**
 * Just about editing part of the simulator..
 * 
 * @author phoad
 */
public class PlannerEditing {

    public ExecuterProp executerProp;

    public JFrame planningEditorFrame;

    public PlannerEditing(IPlannerController plannerController) {
        PlannerEditorController editorListener = new PlannerEditorController();
        PlannerEditor plannerEditor = new PlannerEditor(editorListener);
        executerProp = new ExecuterProp();
        editorListener.initialize(plannerEditor, plannerController, executerProp);
        planningEditorFrame = new JFrame("Planning Editor");
        planningEditorFrame.setContentPane(plannerEditor);
        planningEditorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        planningEditorFrame.pack();
        planningEditorFrame.setVisible(true);
    }
}
