package org.sempere.commons.faces.bean;

import javax.faces.model.*;
import java.util.*;

/**
 * Interface that defines methods to build a selection list (collection of SelectItem instances) from an object.
 *
 * @author bsempere
 */
public interface Selectable {

    /**
	 * Returns a SelectItem list with codes for labels
	 *
	 * @return Collection<SelectItem>
	 */
    Collection<SelectItem> getSelectItemsWithCodesForLabels();

    /**
	 * Returns a SelectItem list with short labels
	 *
	 * @return Collection<SelectItem>
	 */
    Collection<SelectItem> getSelectItemsWithShortLabels();

    /**
	 * Returns a SelectItem list with long labels
	 *
	 * @return Collection<SelectItem>
	 */
    Collection<SelectItem> getSelectItemsWithLongLabels();
}
