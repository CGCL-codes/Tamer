package course.quiz.question;

import java.util.Comparator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.DomainModel;
import org.modelibra.Entities;
import org.modelibra.IDomainModel;
import org.modelibra.ISelector;
import org.modelibra.Oid;
import org.modelibra.PropertySelector;
import course.quiz.member.Member;
import course.quiz.test.Test;

/**
 * Question generated entities. This class should not be changed manually. Use a
 * subclass to add specific code.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-03
 */
public abstract class GenQuestions extends Entities<Question> {

    private static final long serialVersionUID = 1176743111751L;

    private static Log log = LogFactory.getLog(GenQuestions.class);

    private Test test;

    private Member member;

    /**
	 * Constructs questions within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public GenQuestions(IDomainModel model) {
        super(model);
    }

    /**
	 * Constructs questions for the test parent.
	 * 
	 * @param test
	 *            test
	 */
    public GenQuestions(Test test) {
        this(test.getModel());
        setTest(test);
    }

    /**
	 * Constructs questions for the member parent.
	 * 
	 * @param member
	 *            member
	 */
    public GenQuestions(Member member) {
        this(member.getModel());
        setMember(member);
    }

    /**
	 * Retrieves the question with a given oid. Null if not found.
	 * 
	 * @param oid
	 *            oid
	 * @return question
	 */
    public Question getQuestion(Oid oid) {
        return retrieveByOid(oid);
    }

    /**
	 * Retrieves the question with a given oid unique number. Null if not found.
	 * 
	 * @param oidUniqueNumber
	 *            oid unique number
	 * @return question
	 */
    public Question getQuestion(Long oidUniqueNumber) {
        return getQuestion(new Oid(oidUniqueNumber));
    }

    /**
	 * Retrieves the first question whose property with a property code is equal
	 * to a property object. Null if not found.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return question
	 */
    public Question getQuestion(String propertyCode, Object property) {
        return retrieveByProperty(propertyCode, property);
    }

    /**
	 * Selects questions whose property with a property code is equal to a
	 * property object. Returns empty entities if no selection.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return questions
	 */
    public Questions getQuestions(String propertyCode, Object property) {
        return (Questions) selectByProperty(propertyCode, property);
    }

    /**
	 * Gets questions ordered by a property.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered questions
	 */
    public Questions getQuestions(String propertyCode, boolean ascending) {
        return (Questions) orderByProperty(propertyCode, ascending);
    }

    /**
	 * Gets questions selected by a selector. Returns empty questions if there
	 * are no questions that satisfy the selector.
	 * 
	 * @param selector
	 *            selector
	 * @return selected questions
	 */
    public Questions getQuestions(ISelector selector) {
        return (Questions) selectBySelector(selector);
    }

    /**
	 * Gets questions ordered by a composite comparator.
	 * 
	 * @param comparator
	 *            comparator
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered questions
	 */
    public Questions getQuestions(Comparator comparator, boolean ascending) {
        return (Questions) orderByComparator(comparator, ascending);
    }

    /**
	 * Gets text questions.
	 * 
	 * @param text
	 *            text
	 * @return text questions
	 */
    public Questions getTextQuestions(String text) {
        PropertySelector propertySelector = new PropertySelector("text");
        propertySelector.defineEqual(text);
        return getQuestions(propertySelector);
    }

    /**
	 * Gets number question.
	 * 
	 * @param number
	 *            number
	 * @return number question
	 */
    public Question getNumberQuestion(Integer number) {
        PropertySelector propertySelector = new PropertySelector("number");
        propertySelector.defineEqual(number);
        List<Question> list = getQuestions(propertySelector).getList();
        if (list.size() > 0) return list.iterator().next(); else return null;
    }

    /**
	 * Gets member questions.
	 * 
	 * @param member
	 *            member oid unique number
	 * @return member questions
	 */
    public Questions getMemberQuestions(Long member) {
        PropertySelector propertySelector = new PropertySelector("memberOid");
        propertySelector.defineEqual(member);
        return getQuestions(propertySelector);
    }

    /**
	 * Gets member questions.
	 * 
	 * @param member
	 *            member oid
	 * @return member questions
	 */
    public Questions getMemberQuestions(Oid member) {
        return getMemberQuestions(member.getUniqueNumber());
    }

    /**
	 * Gets member questions.
	 * 
	 * @param member
	 *            member
	 * @return member questions
	 */
    public Questions getMemberQuestions(Member member) {
        return getMemberQuestions(member.getOid());
    }

    /**
	 * Gets questions ordered by number.
	 * 
	 * @param ascending
	 *            <code>true</code> if ascending
	 * @return ordered questions
	 */
    public Questions getQuestionsOrderedByNumber(boolean ascending) {
        return getQuestions("number", ascending);
    }

    /**
	 * Gets questions ordered by text.
	 * 
	 * @param ascending
	 *            <code>true</code> if ascending
	 * @return ordered questions
	 */
    public Questions getQuestionsOrderedByText(boolean ascending) {
        return getQuestions("text", ascending);
    }

    /**
	 * Sets test.
	 * 
	 * @param test
	 *            test
	 */
    public void setTest(Test test) {
        this.test = test;
    }

    /**
	 * Gets test.
	 * 
	 * @return test
	 */
    public Test getTest() {
        return test;
    }

    /**
	 * Sets member.
	 * 
	 * @param member
	 *            member
	 */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
	 * Gets member.
	 * 
	 * @return member
	 */
    public Member getMember() {
        return member;
    }

    protected boolean postAdd(Question question) {
        if (!isPost()) {
            return true;
        }
        boolean post = true;
        if (super.postAdd(question)) {
            DomainModel model = (DomainModel) getModel();
            if (model.isInitialized()) {
                Member member = getMember();
                if (member == null) {
                    Member questionMember = question.getMember();
                    if (questionMember != null) {
                        if (!questionMember.getQuestions().contain(question)) {
                            questionMember.getQuestions().setPropagateToSource(false);
                            post = questionMember.getQuestions().add(question);
                            questionMember.getQuestions().setPropagateToSource(true);
                        }
                    }
                }
            }
        } else {
            post = false;
        }
        return post;
    }

    protected boolean postRemove(Question question) {
        if (!isPost()) {
            return true;
        }
        boolean post = true;
        if (super.postRemove(question)) {
            Member member = getMember();
            if (member == null) {
                Member questionMember = question.getMember();
                if (questionMember != null) {
                    if (questionMember.getQuestions().contain(question)) {
                        questionMember.getQuestions().setPropagateToSource(false);
                        post = questionMember.getQuestions().remove(question);
                        questionMember.getQuestions().setPropagateToSource(true);
                    }
                }
            }
        } else {
            post = false;
        }
        return post;
    }

    protected boolean postUpdate(Question beforeQuestion, Question afterQuestion) {
        if (!isPost()) {
            return true;
        }
        boolean post = true;
        if (super.postUpdate(beforeQuestion, afterQuestion)) {
            Member member = getMember();
            if (member == null) {
                Member beforeQuestionMember = beforeQuestion.getMember();
                Member afterQuestionMember = afterQuestion.getMember();
                if (beforeQuestionMember == null && afterQuestionMember != null) {
                    if (!afterQuestionMember.getQuestions().contain(afterQuestion)) {
                        afterQuestionMember.getQuestions().setPropagateToSource(false);
                        post = afterQuestionMember.getQuestions().add(afterQuestion);
                        afterQuestionMember.getQuestions().setPropagateToSource(true);
                    }
                } else if (beforeQuestionMember != null && afterQuestionMember == null) {
                    if (beforeQuestionMember.getQuestions().contain(beforeQuestion)) {
                        beforeQuestionMember.getQuestions().setPropagateToSource(false);
                        post = beforeQuestionMember.getQuestions().remove(beforeQuestion);
                        beforeQuestionMember.getQuestions().setPropagateToSource(true);
                    }
                } else if (beforeQuestionMember != null && afterQuestionMember != null && beforeQuestionMember != afterQuestionMember) {
                    if (beforeQuestionMember.getQuestions().contain(beforeQuestion)) {
                        beforeQuestionMember.getQuestions().setPropagateToSource(false);
                        post = beforeQuestionMember.getQuestions().remove(beforeQuestion);
                        beforeQuestionMember.getQuestions().setPropagateToSource(true);
                    }
                    if (!afterQuestionMember.getQuestions().contain(afterQuestion)) {
                        afterQuestionMember.getQuestions().setPropagateToSource(false);
                        post = afterQuestionMember.getQuestions().add(afterQuestion);
                        afterQuestionMember.getQuestions().setPropagateToSource(true);
                    }
                }
            }
        } else {
            post = false;
        }
        return post;
    }

    /**
	 * Creates question.
	 * 
	 * @param testParent
	 *            test parent
	 * @param memberParent
	 *            member parent
	 * @param text
	 *            text
	 * @return question
	 */
    public Question createQuestion(Test testParent, Member memberParent, String text) {
        Question question = new Question(getModel());
        question.setTest(testParent);
        question.setMember(memberParent);
        question.setText(text);
        if (!add(question)) {
            question = null;
        }
        return question;
    }
}
