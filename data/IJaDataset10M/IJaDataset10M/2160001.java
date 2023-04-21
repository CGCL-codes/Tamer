package ch.unibe.im2.inkanno.imageExport;

import java.awt.Color;
import ch.unibe.eindermu.utils.GraphicsBackup;
import ch.unibe.im2.inkanno.InkAnnoAnnotationStructure;
import ch.unibe.inkml.InkTraceView;
import ch.unibe.inkml.InkTraceViewContainer;
import ch.unibe.inkml.InkTraceViewLeaf;

public abstract class ImagePixelAccurateDrawer extends RegisteredImageExportDrawer {

    protected static final int DRAWING_NR = 254;

    protected static final int FORMULA_NR = 160;

    protected static final Color NOISE_COLOR = new Color(255, 255, 0);

    protected int blockCounter = 0;

    protected int drawingCounter = 0;

    protected int formulaCounter = 0;

    protected CT cT;

    protected int columnCounter = 1;

    protected int diagramCounter = 31;

    protected int listCounter = 63;

    protected int tableCounter = 127;

    protected enum CT {

        TEXTBLOCK, DRAWING, FORMULA, NOISE, TEXTLINE, LIST, DIAGRAM, TABLE
    }

    public void go(InkTraceView view) {
        GraphicsBackup gb = new GraphicsBackup(getGraphics());
        getGraphics().setBackground(new Color(255, 255, 255));
        super.go(view);
        gb.reset();
    }

    @Override
    public void visitHook(final InkTraceViewContainer container) {
        GraphicsBackup gb = new GraphicsBackup(getGraphics());
        handle(container, new Handler() {

            public void goOn() {
                container.delegateVisitor(ImagePixelAccurateDrawer.this);
            }
        });
        gb.reset();
    }

    protected abstract void handle(InkTraceView container, Handler handler);

    public void visitHook(final InkTraceViewLeaf leaf) {
        GraphicsBackup gb = new GraphicsBackup(getGraphics());
        handle(leaf, new Handler() {

            public void goOn() {
                paintLeaf(leaf);
            }
        });
        gb.reset();
    }

    public boolean pass(InkTraceView view) {
        if (super.pass(view)) {
            if (!view.containsAnnotation(InkAnnoAnnotationStructure.TYPE) || !view.getAnnotation(InkAnnoAnnotationStructure.TYPE).equals("Marking")) {
                return true;
            }
        }
        return false;
    }

    protected boolean parentEquals(InkTraceView view, String string) {
        return view.getParent().containsAnnotation(InkAnnoAnnotationStructure.TYPE) && view.getParent().getAnnotation(InkAnnoAnnotationStructure.TYPE).equalsIgnoreCase(string);
    }

    abstract class Handler {

        public void goOn(CT ct) {
            CT b = cT;
            cT = ct;
            goOn();
            cT = b;
        }

        public abstract void goOn();
    }
}
