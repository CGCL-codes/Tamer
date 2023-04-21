package course.quiz;

import org.modelibra.DomainModel;
import org.modelibra.IDomain;
import course.quiz.member.Members;
import course.quiz.test.Tests;

/**
 * Quiz generated model. This class should not be changed manually. Use a
 * subclass to add specific code.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-03
 */
public abstract class GenQuiz extends DomainModel {

    private static final long serialVersionUID = 1176742903870L;

    private Tests tests;

    private Members members;

    /**
	 * Constructs a domain model within the domain.
	 * 
	 * @param domain
	 *            domain
	 */
    public GenQuiz(IDomain domain) {
        super(domain);
        tests = new Tests(this);
        members = new Members(this);
    }

    /**
	 * Gets Test entities.
	 * 
	 * @return Test entities
	 */
    public Tests getTests() {
        return tests;
    }

    /**
	 * Gets Member entities.
	 * 
	 * @return Member entities
	 */
    public Members getMembers() {
        return members;
    }
}
