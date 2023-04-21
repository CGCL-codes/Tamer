package edu.kit.aifb.wikipedia.sql;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.kit.aifb.concept.IConceptIndex;
import edu.kit.aifb.concept.IConceptIterator;
import edu.kit.aifb.concept.IConceptVector;
import edu.kit.aifb.concept.IConceptVectorMapper;
import edu.kit.aifb.concept.TroveConceptVector;
import edu.kit.aifb.nlp.Language;

public class LanglinksConceptVectorMapper implements IConceptVectorMapper {

    static Log logger = LogFactory.getLog(LanglinksConceptVectorMapper.class);

    private int[] m_idMapping;

    public LanglinksConceptVectorMapper(Configuration config, IConceptIndex sourceIndex, Language sourceLanguage, IConceptIndex targetIndex, Language targetLanguage, ILanglinksApi llApi) throws Exception {
        logger.info("Initializing langlinks concept vector mapper (" + sourceLanguage + " to " + targetLanguage + ")");
        LanglinksApiLanglinksMap langlinksMap = new LanglinksApiLanglinksMap();
        langlinksMap.setLanglinksApi(llApi);
        m_idMapping = new int[sourceIndex.size()];
        for (int sourceConceptId = 0; sourceConceptId < sourceIndex.size(); sourceConceptId++) {
            if (logger.isDebugEnabled() && sourceConceptId % 100 == 0) {
                logger.debug("Loading mapping ... (" + sourceConceptId + " of " + sourceIndex.size() + ")");
            }
            int sourceArticleId = WikipediaCollection.getArticleId(sourceIndex.getConceptName(sourceConceptId));
            int targetArticleId = langlinksMap.map(sourceArticleId);
            String targetArticleConceptName = WikipediaCollection.getArticleName(targetArticleId);
            int targetConceptId = targetIndex.getConceptId(targetArticleConceptName);
            m_idMapping[sourceConceptId] = targetConceptId;
        }
    }

    public IConceptVector map(IConceptVector cv) {
        logger.debug("Mapping concept vector for document " + cv.getData().getDocName());
        IConceptVector mappedCv = new TroveConceptVector(cv.getData().getDocName(), cv.size());
        IConceptIterator it = cv.iterator();
        while (it.next()) {
            mappedCv.set(m_idMapping[it.getId()], it.getValue());
        }
        return mappedCv;
    }
}
