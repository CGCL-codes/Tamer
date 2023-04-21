package nctools.rgb.editors;

import nctools.editors.IColorEditorFactory;
import nctools.editors.internal.IntColorScaleEditor;
import nctools.rgb.colors.ColorRGB5;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Mike
 *
 */
public class ColorRGB5ScaleEditorFactory implements IColorEditorFactory {

    /**to assure instantiation via Class.newInstance()*/
    public ColorRGB5ScaleEditorFactory() {
    }

    @Override
    public IntColorScaleEditor<ColorRGB5> createEditor(Composite parent, int style) {
        return new IntColorScaleEditor<ColorRGB5>(ColorRGB5.class, parent, style);
    }
}
