package jsesh.mdc.model.utilities;

import java.util.List;
import jsesh.mdc.model.BasicItemList;
import jsesh.mdc.model.InnerGroup;
import jsesh.mdc.model.SubCadrat;

/**
 * This Expert is able to build an InnerGroup from a list of top-level elements
 * (discarding unsuitable ones).
 * <p>
 * If the list is composed of exactly one element which contains only one
 * InnerGroup, then the systems stops right away.
 * 
 * @author rosmord
 * 
 */
public class InnerGroupBuilder {

    InnerGroup innerGroup;

    /**
	 * @param list
	 * @return
	 */
    public void buildHorizontalElement(List list) {
        innerGroup = null;
        if (list.size() == 1) {
            InnerGroupExtractor extractor = new InnerGroupExtractor();
            extractor.extract(list);
            if (!extractor.foundOtherElements() && extractor.getInnerGroups().size() == 1) {
                innerGroup = (InnerGroup) extractor.getInnerGroups().get(0);
            }
        }
        if (innerGroup == null) {
            BasicItemListGrouper basicItemListGrouper = new BasicItemListGrouper();
            BasicItemList basicItemList = basicItemListGrouper.extractBasicItemList(list);
            if (basicItemList.getNumberOfChildren() != 0) innerGroup = new SubCadrat(basicItemList);
        }
    }

    /**
	 * @return
	 */
    public InnerGroup getGroup() {
        return innerGroup;
    }
}
