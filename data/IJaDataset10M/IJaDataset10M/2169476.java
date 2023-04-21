package net.nothinginteresting.ylib.xameleon;

import net.nothinginteresting.xameleon2.XameleonException;
import net.nothinginteresting.xameleon2.annotations.XameleonAttribute;
import net.nothinginteresting.xameleon2.annotations.XameleonNode;
import net.nothinginteresting.ylib.annotations.YAttribute;
import net.nothinginteresting.ylib.constants.YAlignment;
import net.nothinginteresting.ylib.options.YLabelOptions;
import net.nothinginteresting.ylib.widgets.YContainer;
import net.nothinginteresting.ylib.widgets.YComposite;
import net.nothinginteresting.ylib.widgets.YLabel;

/**
 * @author Dmitriy Gorbenko
 * 
 */
@XameleonNode(tag = "label", namespace = YWidgetNode.NAMESPACE, target = YLabel.class)
public class YLabelNode extends YControlNode<YLabel, YLabelOptions> {

    @YAttribute(name = "text", category = GENERAL)
    @XameleonAttribute(name = "text")
    private String text;

    @YAttribute(name = "alignment", category = GENERAL)
    @XameleonAttribute(name = "alignment")
    private YAlignment alignment;

    @Override
    protected void loadAttributes(YLabel widget) throws XameleonException {
        super.loadAttributes(widget);
        YLabel label = widget;
        if (text != null) label.setText(text);
        if (alignment != null) label.setAlignment(alignment);
    }

    @Override
    protected void saveAttributes(YLabel widget) {
        super.saveAttributes(widget);
        YLabel label = widget;
        text = label.getText();
        alignment = label.getAlignment();
    }

    @Override
    protected YLabel createWidget(YContainer<?> parent) {
        return new YLabel((YComposite) parent);
    }
}
