package net.infordata.ifw2.web.grds;

import java.util.List;
import net.infordata.ifw2.web.bnds.IForm;

public interface IPojoEditableGridModel<C> extends IPojoGridModel<C>, IEditableGridModel {

    /**
   * Depending on the implementation, the returned collection may or may not contain
   * the inserting pending pojo and may or may not contain a clone of the edited object instead
   * of the original one. 
   * @return A read-only snapshot list reflecting any sort order or filter applied, it is 
   *   unmodifiable but can change if "back-end" collection or data changes.<br>
   */
    @Override
    public List<C> getDataView();

    /**
   * @param pojo
   * @return true if there is a pending edit operation and if the given pojo instance
   *   is the edited pojo or a temporary clone of it.
   */
    public boolean isEditing(C pojo);

    /**
   * @param pojo
   * @return true if there is a pending insert operation and the given pojo instance
   *   is the one behing inserted.
   */
    public boolean isInserting(C pojo);

    /**
   * @param newPojo - replaces the currently inserted pojo.
   * @return itself
   */
    public IForm replaceInsertedPojo(C newPojo);
}
