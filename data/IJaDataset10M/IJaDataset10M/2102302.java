package edu.univalle.lingweb.persistence;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoItemsE2 entity.
 * 
 * @author LingWeb
 */
@Entity
@Table(name = "co_items_e2", schema = "public", uniqueConstraints = {  })
public class CoItemsE2 extends AbstractCoItemsE2 implements java.io.Serializable {

    /** default constructor */
    public CoItemsE2() {
    }

    /** minimal constructor */
    public CoItemsE2(Long itemE2Id, CoMatchingE2 coMatchingE2, String textQuestionItem, String textAnswerItem) {
        super(itemE2Id, coMatchingE2, textQuestionItem, textAnswerItem);
    }

    /** full constructor */
    public CoItemsE2(Long itemE2Id, CoMatchingE2 coMatchingE2, String textQuestionItem, String textAnswerItem, String imageQuestionItem, String imageAnswerItem, String soundQuestionItem, String soundAnswerItem, Set<CoCompleteFeedback2> coCompleteFeedback2s) {
        super(itemE2Id, coMatchingE2, textQuestionItem, textAnswerItem, imageQuestionItem, imageAnswerItem, soundQuestionItem, soundAnswerItem, coCompleteFeedback2s);
    }
}
