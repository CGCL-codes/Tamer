package es.eucm.eadventure.editor.control.tools.general;

import es.eucm.eadventure.common.data.chapter.NextScene;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;

public class ChangeTransitionTimeTool extends Tool {

    private int newTime;

    private int oldTime;

    private NextScene nextScene;

    public ChangeTransitionTimeTool(NextScene nextScene, int value) {
        this.oldTime = nextScene.getTransitionTime();
        this.newTime = value;
        this.nextScene = nextScene;
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean combine(Tool other) {
        if (other instanceof ChangeTransitionTimeTool) {
            ChangeTransitionTimeTool cttt = (ChangeTransitionTimeTool) other;
            if (cttt.nextScene == nextScene) {
                this.timeStamp = cttt.timeStamp;
                this.newTime = cttt.newTime;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doTool() {
        if (oldTime == newTime) return false;
        nextScene.setTransitionTime(newTime);
        return true;
    }

    @Override
    public boolean redoTool() {
        nextScene.setTransitionTime(newTime);
        Controller.getInstance().updatePanel();
        return true;
    }

    @Override
    public boolean undoTool() {
        nextScene.setTransitionTime(oldTime);
        Controller.getInstance().updatePanel();
        return true;
    }
}
