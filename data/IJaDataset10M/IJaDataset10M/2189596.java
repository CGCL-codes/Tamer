package org.deft.common;

import java.util.ArrayList;
import java.util.List;
import net.sf.vex.dom.Element;
import net.sf.vex.editor.VexEditor;
import net.sf.vex.layout.Box;
import net.sf.vex.swt.VexWidget;
import net.sf.vex.widget.IBoxFilter;
import org.deft.vextoolkit.common.ResourceHelper;
import org.deft.vextoolkit.layout.CodeTextBox;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class VexRuler extends Canvas {

    private VexEditor vexEditor;

    public VexRuler(VexEditor editor, Composite parent, int style) {
        super(parent, style);
        this.addPaintListener(painter);
        this.vexEditor = editor;
    }

    private PaintListener painter = new PaintListener() {

        public void paintControl(PaintEvent e) {
            VexWidget widget = vexEditor.getVexWidget();
            Element rootElement = widget.getDocument().getRootElement();
            List<Element> coderefs = getCodeRefElements(rootElement);
            for (Element cr : coderefs) {
                int offset = cr.getStartOffset();
                Box box = widget.findInnermostBox(new IBoxFilter() {

                    public boolean matches(Box box) {
                        return box instanceof CodeTextBox;
                    }
                }, offset + 1);
                if (box != null) {
                    Point p = widget.getBoxPosition(box);
                    e.gc.drawImage(ResourceHelper.getImage("warning.gif"), 0, p.y);
                }
            }
        }
    };

    private List<Element> getCodeRefElements(Element rootElement) {
        List<Element> result = new ArrayList<Element>();
        if (rootElement.getName().equals("coderef")) {
            result.add(rootElement);
        } else {
            for (Element child : rootElement.getChildElements()) {
                result.addAll(getCodeRefElements(child));
            }
        }
        return result;
    }
}
