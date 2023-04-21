package modelibra.designer.metamodel;

import modelibra.designer.metadomain.MetaDomain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;

/**
 * MetaModel specific entity.
 * 
 * @author Dzenan Ridjanovic
 * @version 2008-05-19
 */
public class MetaModel extends GenMetaModel {

    private static final long serialVersionUID = 1208025838300L;

    private static Log log = LogFactory.getLog(MetaModel.class);

    /**
	 * Constructs metaModel within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public MetaModel(IDomainModel model) {
        super(model);
    }

    /**
	 * Constructs metaModel within its parent(s).
	 * 
	 * @param MetaDomain
	 *            metaDomain
	 */
    public MetaModel(MetaDomain metaDomain) {
        super(metaDomain);
    }
}
