package edu.univalle.lingweb.persistence;

import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ToQuestionGroup entity.
 * 
 * @author LingWeb
 */
@Entity
@Table(name = "to_question_group", schema = "public", uniqueConstraints = {  })
public class ToQuestionGroup extends AbstractToQuestionGroup implements java.io.Serializable {

    /** default constructor */
    public ToQuestionGroup() {
    }

    /** minimal constructor */
    public ToQuestionGroup(Long exerciseGroupId, String groupName, Date createDate) {
        super(exerciseGroupId, groupName, createDate);
    }

    /** full constructor */
    public ToQuestionGroup(Long exerciseGroupId, CoQuestion coQuestion, String groupName, String description, Date createDate, Long roomExternalId, Set<CoUserQuestionGroup> coUserQuestionGroups) {
        super(exerciseGroupId, coQuestion, groupName, description, createDate, roomExternalId, coUserQuestionGroups);
    }
}
