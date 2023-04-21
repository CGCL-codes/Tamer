package net.nothinginteresting.ylib.widgets;

import net.nothinginteresting.ylib.constants.YAlignment;
import net.nothinginteresting.ylib.options.YAbstractLabelOptions;
import net.nothinginteresting.ylib.options.YWidgetOptions;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;

/**
 * <dt><b>Styles:</b></dt> <dd>SEPARATOR, HORIZONTAL, VERTICAL</dd> <dd>
 * SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd> <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * 
 */
public abstract class YAbstractLabel extends YControl {

    public YAbstractLabel(YComposite parent, YAbstractLabelOptions options) {
        super(parent, options);
    }

    public YAbstractLabel(YComposite parent) {
        this(parent, new YAbstractLabelOptions());
    }

    @Override
    protected Label createWidget(YWidgetOptions options) {
        return new Label(getParent().getWidget(), options.getStyle());
    }

    @Override
    public Label getWidget() {
        return (Label) super.getWidget();
    }

    public String getText() {
        return getWidget().getText();
    }

    public void setText(String text) {
        getWidget().setText(text);
    }

    public void setAlignment(YAlignment alignment) {
        getWidget().setAlignment(alignment.getOption().getValue());
    }

    public YAlignment getAlignment() {
        return YAlignment.valueOf(getWidget().getAlignment());
    }

    /**
	 * TODO YImage
	 * 
	 * @return
	 * @see org.eclipse.swt.widgets.Label#getImage()
	 */
    public Image getImage() {
        return getWidget().getImage();
    }

    /**
	 * TODO YImage
	 * 
	 * @param image
	 * @see org.eclipse.swt.widgets.Label#setImage(org.eclipse.swt.graphics.Image)
	 */
    public void setImage(Image image) {
        getWidget().setImage(image);
    }
}
