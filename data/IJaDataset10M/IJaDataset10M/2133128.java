package net.sourceforge.sonja.examples;

import java.beans.PropertyEditorManager;
import net.sourceforge.sonja.Encoder;
import net.sourceforge.sonja.StringEncoder;
import net.sourceforge.sonja.strategy.BeansStrategy;

public class BeanStrategyExample {

    public Object getProperty() {
        return "Raw property value";
    }

    public static void main(String args[]) {
        final Encoder encoder = new Encoder();
        final StringEncoder wrapper = new StringEncoder(encoder);
        final BeanStrategyExample example = new BeanStrategyExample();
        System.out.println("Encoded with no customization: " + wrapper.encode(example));
        PropertyEditorManager.registerEditor(BeanStrategyExample.class, ExampleEditor.class);
        encoder.setEncodingStrategies(new BeansStrategy());
        System.out.println("Encoded with beans strategy on type: " + wrapper.encode(example));
    }
}
