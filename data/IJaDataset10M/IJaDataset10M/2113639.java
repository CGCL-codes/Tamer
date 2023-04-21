package com.timenes.clips.platform.controller;

import com.timenes.clips.platform.Platform;
import com.timenes.clips.platform.utils.List;
import com.timenes.clips.platform.view.View;
import com.timenes.clips.platform.view.DragAndDropListener;
import com.timenes.clips.platform.view.KeyListener;
import com.timenes.clips.platform.view.Screen;
import com.timenes.clips.platform.view.ScreenListener;

/**
 * Generic handler for screen input events
 * @author helge@timenes.com
 * 
 */
public class ScreenController implements KeyListener, ScreenListener, DragAndDropListener {

    private Screen screen;

    private List<Controller> rootControllers;

    private Controller selectedController;

    private Controller clipboardController;

    public ScreenController() {
        screen = Platform.getViewFactory().createScreen();
        screen.addKeyListener(this);
        screen.addScreenListener(this);
        screen.addDragAndDropListener(this);
        screen.addScreenListener(this);
        rootControllers = Platform.getUtilsFactory().createList(Controller.class);
    }

    public Screen getScreen() {
        return screen;
    }

    private void arrowKeyPressed(boolean downOrRight) {
        if (selectedController == null) {
            selectedController = rootControllers.get(0);
            screen.setSelectedView(selectedController.getView());
            return;
        } else {
            List<Controller> siblings = null;
            ContainerController container = null;
            if (rootControllers.contains(selectedController)) {
                siblings = rootControllers;
            } else {
                container = (ContainerController) selectedController.getContainer();
                siblings = container.getChildControllers();
            }
            int index = siblings.indexOf(selectedController);
            if (downOrRight) {
                if (siblings.size() > index + 1) {
                    selectedController = siblings.get(index + 1);
                    screen.setSelectedView(selectedController.getView());
                } else {
                    screen.setSelectedView(container.getView());
                }
            } else {
                if (index > 0) {
                    selectedController = siblings.get(index - 1);
                    screen.setSelectedView(selectedController.getView());
                } else {
                    selectedController = container;
                    screen.setSelectedView(selectedController.getView());
                }
            }
        }
    }

    @Override
    public void arrowDownPressed() {
        arrowKeyPressed(true);
    }

    @Override
    public void arrowLeftPressed() {
        arrowKeyPressed(false);
    }

    @Override
    public void arrowRightPressed() {
        arrowKeyPressed(true);
    }

    @Override
    public void arrowUpPressed() {
        arrowKeyPressed(false);
    }

    @Override
    public void viewDropped(View droppedView, View dropTarget) {
        System.out.println(this + ".viewDropped(" + droppedView + ", " + dropTarget + ")");
        Controller droppedController = getControllerFor(droppedView);
        Controller dropTargetController = getControllerFor(dropTarget);
        if (dropTargetController.isDropTargetFor(droppedController)) {
            MoveCommand cmd = new MoveCommand(droppedController, (ContainerController) dropTargetController);
            System.out.println(getClass().getSimpleName() + ": executing move command: " + cmd);
            EditStack.getInstance().execute(cmd);
        }
    }

    @Override
    public void copySequencePressed() {
        if (selectedController != null) {
            clipboardController = selectedController.copy();
        }
    }

    @Override
    public void cutSequencePressed() {
        if (selectedController != null && selectedController.getContainer() != null) {
            clipboardController = selectedController.copy();
            DeleteCommand cmd = new DeleteCommand(selectedController);
            EditStack.getInstance().execute(cmd);
        }
    }

    @Override
    public void enterPressed() {
    }

    private Controller getControllerFor(View c) {
        for (int i = 0; i < rootControllers.size(); i++) {
            Controller ctrl = rootControllers.get(i);
            if (ctrl.getView() == c) return ctrl;
            if (ctrl instanceof ContainerController) {
                ContainerController container = (ContainerController) ctrl;
                Controller recursiveChild = getControllerFor(c, (ContainerController) container);
                if (recursiveChild != null) return recursiveChild;
            }
        }
        return null;
    }

    private Controller getControllerFor(View c, ContainerController container) {
        for (int i = 0; i < rootControllers.size(); i++) {
            if (container.getView() == c) return container;
            for (int j = 0; j < container.getChildControllers().size(); j++) {
                Controller child = container.getChildControllers().get(j);
                if (child.getView() == c) {
                    return child;
                }
                if (child instanceof ContainerController) {
                    Controller recursiveChild = getControllerFor(c, (ContainerController) child);
                    if (recursiveChild != null) return recursiveChild;
                }
            }
        }
        return null;
    }

    @Override
    public void keyPressed(char key) {
    }

    @Override
    public void pasteSequencePressed() {
        if (clipboardController != null && selectedController != null) {
            if (selectedController instanceof ContainerController) {
                ContainerController container = (ContainerController) selectedController;
                if (container.isCompatibleChild(clipboardController)) {
                    AddCommand cmd = new AddCommand(container, clipboardController.copy());
                    EditStack.getInstance().execute(cmd);
                }
            }
        }
    }

    @Override
    public void redoSequencePressed() {
        EditStack.getInstance().redo();
    }

    @Override
    public void saveSequencePressed() {
    }

    @Override
    public void screenSizeChanged(int width, int height) {
        System.out.println(getClass().getSimpleName() + ".screenSizeChanged(" + width + ", " + height + ")");
        for (int i = 0; i < rootControllers.size(); i++) {
            rootControllers.get(i).setSize(width, height);
        }
    }

    @Override
    public void undoSequencePressed() {
        EditStack.getInstance().undo();
    }

    @Override
    public void editSequencePressed() {
    }

    @Override
    public void playPuseSequencePressed() {
    }

    public void add(Controller rootController) {
        rootControllers.add(rootController);
    }
}
