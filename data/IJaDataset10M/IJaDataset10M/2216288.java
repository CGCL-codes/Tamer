package basis.demos;

import java.awt.*;

public class MiscDemo extends Demo {

    public MiscDemo() {
        setLayout(new GridLayout(2, 2));
        add(new BeanDemo());
        add(new FrameDemo());
        add(new GridBagLayoutDemo());
        add(new NullLayoutDemo());
    }
}
