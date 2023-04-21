package com.kanjiportal.portal.course;

import com.kanjiportal.portal.dao.KanjiDao;
import com.kanjiportal.portal.dao.KnowledgeDao;
import com.kanjiportal.portal.dao.TagDao;
import com.kanjiportal.portal.model.Kanji;
import com.kanjiportal.portal.model.Knowledge;
import com.kanjiportal.portal.model.Tag;
import com.kanjiportal.portal.model.service.KnowledgeParam;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Nickho
 * Date: Feb 1, 2009
 * Time: 7:20:32 PM
 * To change this template use File | Settings | File Templates.
 */
@AutoCreate
@Name("knowledgeImporting")
public class KnowledgeImportingAction implements KnowledgeImporting {

    @In
    private KnowledgeDao knowledgeDao;

    @In
    private KanjiDao kanjiDao;

    @In
    private TagDao tagDao;

    @Logger
    private Log logger;

    @Begin
    public void startImport() {
        logger.info("start import");
    }

    public int importKnowledges(List<KnowledgeParam> list, String username) {
        int updated = 0;
        for (KnowledgeParam knowledgeParam : list) {
            Knowledge knowledge = knowledgeDao.findByKanjiAndTagForUser(knowledgeParam.getKanji(), knowledgeParam.getTag(), username);
            if (knowledge != null) {
                copy(knowledgeParam, knowledge);
                knowledgeDao.update(knowledge);
            } else {
                knowledge = new Knowledge();
                Kanji kanji = kanjiDao.findByKanji(knowledgeParam.getKanji());
                Tag tag = tagDao.findByCode(knowledgeParam.getTag());
                knowledge.setKanji(kanji);
                knowledge.setTag(tag);
                knowledge.setUsername(username);
                copy(knowledgeParam, knowledge);
                knowledgeDao.create(knowledge);
            }
            updated++;
        }
        return updated;
    }

    /**
     * Do a simple copy of a KnowledgeParam to a Knowledge
     *
     * @param knowledgeParam
     * @param knowledge
     */
    private void copy(KnowledgeParam knowledgeParam, Knowledge knowledge) {
        knowledge.setFirstTimeSuccess(knowledgeParam.getFirstTimeSuccess());
        knowledge.setLastTimeSuccess(knowledgeParam.getLastTimeSuccess());
        knowledge.setLastTestSuccess(knowledgeParam.getLastTestSuccess());
    }
}
