package consciouscode.bonsai.tags;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.impl.TagFactory;
import org.apache.commons.jelly.tags.swing.BeanFactory;
import org.apache.commons.jelly.tags.swing.SwingTagLibrary;
import org.xml.sax.Attributes;

/**
    A Jelly tag library that extends the normal JellySwing tags to use Bonsai
    channels, actions, and components.
*/
public class BonsaiTagLibrary extends SwingTagLibrary {

    private static class ConstrainedComponentFactory extends BeanFactory implements TagFactory {

        public ConstrainedComponentFactory(Class<?> beanClass) {
            super(beanClass);
        }

        public Tag createTag(String name, Attributes attributes) throws Exception {
            return new ConstrainedComponentTag(this);
        }
    }

    public BonsaiTagLibrary() {
        registerTag("addChild", AddChildTag.class);
        registerTag("channel", ChannelTag.class);
        registerTag("column", ColumnTag.class);
        registerTag("comboBox", ComboBoxTag.class);
        registerTag("frame", FrameTag.class);
        registerTag("label", LabelTag.class);
        registerTag("listTable", ListTableTag.class);
        registerTag("panel", PanelTag.class);
        registerTag("passwordField", PasswordFieldTag.class);
        registerTag("propertyChannel", PropertyChannelTag.class);
        registerTag("setChannel", SetChannelTag.class);
        registerTag("showWindow", ShowWindowTag.class);
        registerTag("tableAlign", TableAlignTag.class);
        registerTag("tableLayout", TableLayoutTag.class);
        registerTag("textArea", TextAreaTag.class);
        registerTag("textField", TextFieldTag.class);
        registerTag("trigger", TriggerTag.class);
    }

    /** XXX Nasty override. */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void registerBeanFactory(String name, Class beanClass) {
        getFactoryMap().put(name, new ConstrainedComponentFactory(beanClass));
    }
}
