package br.ufmg.lcc.eid.model;

import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.model.IPersistenceObject;
import br.ufmg.lcc.arangi.model.StandardBusinessObject;
import br.ufmg.lcc.eid.commons.EidConstantes;
import br.ufmg.lcc.eid.dto.Source;

public class SourceBO extends StandardBusinessObject {

    /**
	 * Returns source object based on source and key
	 * @throws BasicException 
	 */
    public Source getSourceByNameAndKey(IPersistenceObject dao, String sourceName, String sourceId) throws BasicException {
        Source result = null;
        result = (Source) dao.executeNamedQuerySingleResult("findSourceByNameAndKey", new Object[] { sourceName, sourceId });
        return result;
    }

    /**
	 * 
	 * @throws BasicException
	 */
    public void verifySource(IPersistenceObject dao) throws BasicException {
        Source source = (Source) dao.executeNamedQuerySingleResult("findSourceByNameAndKey", new Object[] { EidConstantes.Source.SOURCE_NAME, EidConstantes.Source.SOURCE_KEY });
        if (source == null) {
            source = new Source();
            source.setSourceName(EidConstantes.Source.SOURCE_NAME);
            source.setSourceKey(EidConstantes.Source.SOURCE_KEY);
            dao.save(source);
        }
    }
}
