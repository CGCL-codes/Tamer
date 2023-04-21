package mikhail_barg.ctools.swt.editors;

import mikhail_barg.ctools.colors.ColorNCSf;
import mikhail_barg.ctools.swt.editors.events.ColorChangedEvent;
import mikhail_barg.ctools.swt.editors.events.ColorChangedListener;
import mikhail_barg.ctools.swt.editors.internal.NCSTriangleEditor;
import mikhail_barg.ctools.swt.editors.internal.StyledColorEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Mike
 *
 */
public class ColorNCSfTriangleEditor extends StyledColorEditor<ColorNCSf> {

    private NCSTriangleEditor m_editor;

    public ColorNCSfTriangleEditor(Composite parent, int style) {
        super(ColorNCSf.class, parent, style);
        getMainWidget().setLayout(new FillLayout());
        m_editor = new NCSTriangleEditor(getMainWidget(), SWT.NONE);
        m_editor.addColorChangedListener(new ColorChangedListener() {

            @Override
            public void colorChanged(ColorChangedEvent event) {
                editorColorChanged();
            }
        });
    }

    private void editorColorChanged() {
        getColor().assign(m_editor.getColor());
        sendColorChangedEvent();
    }

    @Override
    public void setColor(ColorNCSf src) {
        super.setColor(src);
        m_editor.setColor(src);
    }
}
