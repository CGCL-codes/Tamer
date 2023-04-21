package com.vividsolutions.jump.workbench.ui.cursortool.editing;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.cursortool.AbstractCursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.DrawRectangleFenceTool;
import com.vividsolutions.jump.workbench.ui.cursortool.QuasimodeTool;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

public class SnapVerticesToSelectedVertexTool extends QuasimodeTool {

    private static final Cursor SHIFT_DOWN_CURSOR = AbstractCursorTool.createCursor(IconLoader.icon("SnapVerticesTogetherCursor3.gif").getImage());

    private static final Cursor SHIFT_NOT_DOWN_CURSOR = AbstractCursorTool.createCursor(IconLoader.icon("SnapVerticesTogetherCursor4.gif").getImage());

    public String getName() {
        return AbstractCursorTool.name(this);
    }

    public SnapVerticesToSelectedVertexTool(EnableCheckFactory checkFactory) {
        super(new DrawRectangleFenceTool() {

            public void mouseClicked(final MouseEvent e) {
                if (!check(new EnableCheck() {

                    public String check(JComponent component) {
                        return (!e.isShiftDown()) ? "Shift-click the vertex to snap to." : null;
                    }
                })) {
                    return;
                }
                super.mouseClicked(e);
            }

            public Cursor getCursor() {
                return SHIFT_NOT_DOWN_CURSOR;
            }
        });
        add(new ModifierKeySpec(false, true, false), new SnapVerticesToSelectedVertexClickTool(checkFactory) {

            public Cursor getCursor() {
                return SHIFT_DOWN_CURSOR;
            }
        });
    }

    public Icon getIcon() {
        return IconLoader.icon("SnapVerticesTogether.gif");
    }
}
