package shellkk.qiq.jdm.attributeimportance;

import java.util.List;
import shellkk.qiq.jdm.base.IModelDetail;

public interface AttributeImportanceModelDetail extends IModelDetail {

    public List<RankedAttribute> getRankedAttributes();
}
