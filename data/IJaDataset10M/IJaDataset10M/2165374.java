package nctools.rgb.editors;

import nctools.editors.IColorEditorFactory;
import nctools.rgb.colors.ColorRGB5;
import nctools.rgb.editors.internal.ColorRGBIntValueEditor;
import org.eclipse.swt.widgets.Composite;

public class ColorRGB5ValueEditorFactory implements IColorEditorFactory {

    /**to assure instantiation via Class.newInstance()*/
    public ColorRGB5ValueEditorFactory() {
    }

    @Override
    public ColorRGBIntValueEditor<ColorRGB5> createEditor(Composite parent, int style) {
        return new ColorRGBIntValueEditor<ColorRGB5>(ColorRGB5.class, parent, style);
    }
}
