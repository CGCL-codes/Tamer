package common.devbot.descriptor;

import java.util.List;
import common.devbot.descriptor.description.Description;
import common.devbot.descriptor.description.impl.ObjectDescription;

/**
 * @author T Decoster.
 *
 */
public interface Descriptor {

    ObjectDescription processDescription(Object source);

    ObjectDescription processAddComposite(ObjectDescription desc, Object source);
}
