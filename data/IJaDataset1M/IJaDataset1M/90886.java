package uk.ac.lkl.expresser.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.TextBox;

public class TiedNumberNameEditor extends TextBox {

    private TiedNumberPanel tiedNumberPanel;

    public TiedNumberNameEditor(TiedNumberPanel tiedNumberPanel) {
        super();
        this.tiedNumberPanel = tiedNumberPanel;
        TiedNumber tiedNumber = tiedNumberPanel.getTiedNumber();
        if (tiedNumber.isNamed()) {
            setText(tiedNumber.getName());
        }
        ChangeHandler changeHandler = new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                stopEditing();
            }
        };
        addChangeHandler(changeHandler);
        setStylePrimaryName("expresser-tied-number-name-editor");
        MouseDownHandler mouseDownHandler = new MouseDownHandler() {

            @Override
            public void onMouseDown(MouseDownEvent event) {
                event.stopPropagation();
            }
        };
        addMouseDownHandler(mouseDownHandler);
    }

    /**
     * @param tiedNumberPanel
     */
    public void stopEditing() {
        if (tiedNumberPanel.isAttached()) {
            tiedNumberPanel.stopEditing(getText());
        }
    }
}
