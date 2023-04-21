package JavaOrc.diagram;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @class SlotItem
 *
 * @date 12-16-2004
 * @author krcpa
 * @version 1.0
 *
 */
public class SlotItem extends DesignItem {

    public SlotItem() {
        this(null);
    }

    public SlotItem(String className) {
        super(className);
    }

    public String getType() {
        return "slot";
    }
}
