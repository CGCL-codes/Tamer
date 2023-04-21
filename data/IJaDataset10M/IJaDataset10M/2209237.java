package edu.univalle.lingweb.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoParagraphBaseKnowledge entity.
 * 
 * @author LingWeb
 */
@Entity
@Table(name = "co_paragraph_base_knowledge", schema = "public", uniqueConstraints = {  })
public class CoParagraphBaseKnowledge extends AbstractCoParagraphBaseKnowledge implements java.io.Serializable {

    /** default constructor */
    public CoParagraphBaseKnowledge() {
    }

    /** minimal constructor */
    public CoParagraphBaseKnowledge(Long knowledgeId) {
        super(knowledgeId);
    }

    /** full constructor */
    public CoParagraphBaseKnowledge(Long knowledgeId, MaParagraphForm maParagraphForm, CoLanguage coLanguage, Long rowNumber, String knowledge) {
        super(knowledgeId, maParagraphForm, coLanguage, rowNumber, knowledge);
    }
}
