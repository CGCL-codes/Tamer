package org.mvz.gwttest.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TestUndo extends Composite {

    private final UndoStack undoStack = new UndoStack(5);

    private VerticalPanel actionsPanel = new VerticalPanel();

    private PushButton undoButton = new PushButton("Undo", new ClickListener() {

        public void onClick(Widget w) {
            undoStack.undoAction();
            enableUndoRedo();
        }
    });

    private PushButton doButton = new PushButton("Add random integer", new ClickListener() {

        public void onClick(Widget w) {
            undoStack.doAction(addRandomIntCommand());
            enableUndoRedo();
        }
    });

    private PushButton redoButton = new PushButton("Redo", new ClickListener() {

        public void onClick(Widget w) {
            undoStack.redoAction();
            enableUndoRedo();
        }
    });

    private void enableUndoRedo() {
        undoButton.setEnabled(undoStack.canUndo());
        redoButton.setEnabled(undoStack.canRedo());
    }

    private int current = 0;

    private TextBox currentTB = new TextBox();

    private UndoStack.Action addRandomIntCommand() {
        return new UndoStack.Action() {

            private int i = Random.nextInt(10);

            private Label label = new Label("add " + i);

            private void highlightLabel() {
                DOM.setStyleAttribute(label.getElement(), "backgroundColor", "#EEE");
            }

            private void removeHighlight() {
                DOM.setStyleAttribute(label.getElement(), "backgroundColor", "");
            }

            public void execCommit() {
                actionsPanel.remove(label);
            }

            public void execDo() {
                add(i);
                if (label.getParent() == null) actionsPanel.add(label);
                highlightLabel();
            }

            public void execUndo() {
                add(-i);
                removeHighlight();
            }

            public void onCleared() {
                actionsPanel.remove(label);
            }
        };
    }

    private void add(int i) {
        current += i;
        currentTB.setText(Integer.toString(current));
    }

    public TestUndo() {
        currentTB.setEnabled(false);
        add(0);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(undoButton);
        hp.add(redoButton);
        hp.add(doButton);
        actionsPanel.add(hp);
        actionsPanel.add(currentTB);
        initWidget(actionsPanel);
    }
}
