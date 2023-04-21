package nctools.rgb.editors;

import nctools.editors.IColorEditorFactory;
import nctools.editors.internal.FloatColorColorScaleEditor;
import nctools.rgb.colors.ColorHSLf;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Mike
 *
 */
public class ColorHSLfColorScaleEditorFactory implements IColorEditorFactory {

    /**to assure instantiation via Class.newInstance()*/
    public ColorHSLfColorScaleEditorFactory() {
    }

    @Override
    public FloatColorColorScaleEditor<ColorHSLf> createEditor(Composite parent, int style) {
        return new FloatColorColorScaleEditor<ColorHSLf>(ColorHSLf.class, parent, style, ColorHSLf.COMPONENT_INDEX_H, new ColorHSLf(0.0f, 1.0f, 0.5f));
    }
}
