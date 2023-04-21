package org.jowidgets.examples.common.demo;

import java.util.Date;
import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ICalendar;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IPopupDialog;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.powo.JoFrame;

public class DemoChooserFrame extends JoFrame {

    private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

    private IPopupDialog popupDialog;

    private Date date;

    public DemoChooserFrame() {
        super("Chooser demo");
        addComponentListener(new IComponentListener() {

            @Override
            public void sizeChanged() {
                System.out.println(getSize());
            }

            @Override
            public void positionChanged() {
                System.out.println(getPosition());
            }
        });
        setLayout(new MigLayoutDescriptor("[grow]", "[][][]"));
        final IButton button1 = add(BPF.button("Open calendar popup..."), "wrap, sg bg");
        button1.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final IMouseButtonEvent event) {
                popupDialog = createChildWindow(BPF.popupDialog().setBorder(false));
                final Position buttonPos = button1.getPosition();
                popupDialog.setPosition(Toolkit.toScreen(buttonPos, DemoChooserFrame.this));
                setContent(popupDialog);
                popupDialog.pack();
                popupDialog.setVisible(true);
            }
        });
        final IButton button2 = add(BPF.button("Open / Close caledar popup"), "wrap, sg bg");
        button2.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final IMouseButtonEvent event) {
                popupDialog = createChildWindow(BPF.popupDialog().setAutoDispose(false).setBorder(false));
                final Dimension buttonSize = button2.getSize();
                final Position buttonPos = button2.getPosition();
                popupDialog.setBackgroundColor(Colors.WHITE);
                setContent(popupDialog);
                popupDialog.setPosition(Toolkit.toScreen(new Position(buttonPos.getX(), buttonPos.getY() + buttonSize.getHeight()), DemoChooserFrame.this));
                popupDialog.pack();
                popupDialog.setVisible(true);
            }

            @Override
            public void mouseReleased(final IMouseButtonEvent mouseEvent) {
                if (popupDialog != null) {
                    popupDialog.dispose();
                }
            }
        });
        final IButton button3 = add(BPF.button("Open calendar dialog..."), "wrap, sg bg");
        button3.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final IMouseButtonEvent event) {
                final IFrame dialog = createChildWindow(BPF.dialog("Calendar dialog"));
                final Position buttonPos = button3.getPosition();
                dialog.setPosition(Toolkit.toScreen(buttonPos, DemoChooserFrame.this));
                final ICalendar calendar = setContent(dialog);
                dialog.pack();
                dialog.setMinSize(dialog.computeDecoratedSize(calendar.getMinSize()));
                dialog.setVisible(true);
            }
        });
    }

    private ICalendar setContent(final IContainer container) {
        container.setLayout(MigLayoutFactory.growingInnerCellLayout());
        final ICalendar calendar = container.add(BPF.calendar().setDate(this.date), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
        calendar.addInputListener(new IInputListener() {

            @Override
            public void inputChanged() {
                System.out.println(calendar.getDate());
                date = calendar.getDate();
            }
        });
        return calendar;
    }
}
