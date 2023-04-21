package com.peterhi.client.ui;

import com.peterhi.Role;
import com.peterhi.client.App;
import com.peterhi.client.Privileged;
import com.peterhi.client.StateMachine;
import com.peterhi.client.actions.EllipseAction;
import com.peterhi.client.actions.EraseAllAction;
import com.peterhi.client.actions.LineAction;
import com.peterhi.client.actions.PointerAction;
import com.peterhi.client.actions.PolygonAction;
import com.peterhi.client.actions.PolylineAction;
import com.peterhi.client.actions.RectangleAction;
import com.peterhi.client.actions.TextAction;
import com.peterhi.client.ui.constants.Images;
import com.peterhi.client.ui.constants.Strings;
import com.peterhi.client.views.ActionView;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

/**
 *
 * @author YUN TAO
 */
public class ActionPanel extends Composite implements StateMachine, Privileged {

    private ExpandBar expandBar;

    private Button cmdPointer;

    private Button cmdRectangle;

    private Button cmdEllipse;

    private Button cmdLine;

    private Button cmdPolygon;

    private Button cmdPolyline;

    private Button cmdText;

    private Button cmdErase;

    private List<Button> list = new ArrayList<Button>();

    public static ActionPanel get() {
        return App.getApp().getWindow().getView(ActionView.class).get();
    }

    public void toggleDefault() {
        for (Button item : list) {
            if (item == cmdPointer) {
                item.setSelection(true);
            } else {
                item.setSelection(false);
            }
        }
        DrawPad.get().setEditor(null);
    }

    public ActionPanel(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());
        expandBar = new ExpandBar(this, SWT.VERTICAL);
        Composite composite = new Composite(expandBar, SWT.NONE);
        GridLayout lay = new GridLayout();
        lay.numColumns = 4;
        lay.makeColumnsEqualWidth = true;
        composite.setLayout(lay);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        SelectionListener selectionListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DrawPad.get().hideInPlaceEdit();
                for (Button item : list) {
                    if (item == e.widget && !item.getSelection()) {
                        item.setSelection(true);
                    }
                    if (item != e.widget) {
                        item.setSelection(false);
                    }
                }
            }
        };
        cmdPointer = new Button(composite, SWT.TOGGLE);
        cmdPointer.setSelection(true);
        cmdPointer.setLayoutData(data);
        cmdPointer.addSelectionListener(selectionListener);
        cmdRectangle = new Button(composite, SWT.TOGGLE);
        cmdRectangle.setLayoutData(data);
        cmdRectangle.addSelectionListener(selectionListener);
        cmdEllipse = new Button(composite, SWT.TOGGLE);
        cmdEllipse.setLayoutData(data);
        cmdEllipse.addSelectionListener(selectionListener);
        cmdLine = new Button(composite, SWT.TOGGLE);
        cmdLine.setLayoutData(data);
        cmdLine.addSelectionListener(selectionListener);
        cmdPolygon = new Button(composite, SWT.TOGGLE);
        cmdPolygon.setLayoutData(data);
        cmdPolygon.addSelectionListener(selectionListener);
        cmdPolyline = new Button(composite, SWT.TOGGLE);
        cmdPolyline.setLayoutData(data);
        cmdPolyline.addSelectionListener(selectionListener);
        cmdText = new Button(composite, SWT.TOGGLE);
        cmdText.setLayoutData(data);
        cmdText.addSelectionListener(selectionListener);
        cmdErase = new Button(composite, SWT.PUSH);
        cmdErase.setLayoutData(data);
        list.add(cmdPointer);
        list.add(cmdRectangle);
        list.add(cmdEllipse);
        list.add(cmdLine);
        list.add(cmdPolygon);
        list.add(cmdPolyline);
        list.add(cmdText);
        cmdPointer.setText(Strings.action_pointer_text);
        cmdRectangle.setText(Strings.action_rectangle_text);
        cmdEllipse.setText(Strings.action_oval_text);
        cmdLine.setText(Strings.action_line_text);
        cmdPolygon.setText(Strings.action_polygon_text);
        cmdPolyline.setText(Strings.action_polyline_text);
        cmdText.setText(Strings.action_text_text);
        cmdErase.setText(Strings.action_erase_text);
        cmdPointer.setImage(Images.pointer16);
        cmdRectangle.setImage(Images.rect16);
        cmdEllipse.setImage(Images.oval16);
        cmdLine.setImage(Images.line16);
        cmdPolygon.setImage(Images.polygon16);
        cmdPolyline.setImage(Images.polyline16);
        cmdText.setImage(Images.text16);
        cmdErase.setImage(Images.erase16);
        cmdPointer.addSelectionListener(new PointerAction());
        cmdRectangle.addSelectionListener(new RectangleAction());
        cmdEllipse.addSelectionListener(new EllipseAction());
        cmdLine.addSelectionListener(new LineAction());
        cmdPolygon.addSelectionListener(new PolygonAction());
        cmdPolyline.addSelectionListener(new PolylineAction());
        cmdText.addSelectionListener(new TextAction());
        cmdErase.addSelectionListener(new EraseAllAction());
        composite.pack();
        ExpandItem item = new ExpandItem(expandBar, SWT.NONE, 0);
        item.setExpanded(true);
        item.setText(Strings.action_whiteboard_title);
        item.setHeight(composite.getSize().y);
        item.setControl(composite);
    }

    public void transition(int state, Object data) {
        if (state != StateMachine.State.CHANNEL) {
            cmdRectangle.setEnabled(false);
            cmdEllipse.setEnabled(false);
            cmdLine.setEnabled(false);
            cmdPolygon.setEnabled(false);
            cmdPolyline.setEnabled(false);
            cmdText.setEnabled(false);
            cmdErase.setEnabled(false);
        }
    }

    public void notify(int substate, Object data) {
    }

    public void grant(int role) {
        if (App.getApp().getState() == StateMachine.State.CHANNEL) {
            cmdRectangle.setEnabled(role >= Role.TEACHER);
            cmdEllipse.setEnabled(role >= Role.TEACHER);
            cmdLine.setEnabled(role >= Role.TEACHER);
            cmdPolygon.setEnabled(role >= Role.TEACHER);
            cmdPolyline.setEnabled(role >= Role.TEACHER);
            cmdText.setEnabled(role >= Role.TEACHER);
            cmdErase.setEnabled(role >= Role.TEACHER);
        }
    }
}
