package tester;

/**
 * Created by IntelliJ IDEA.
 * User: aleck
 * Date: 2007-10-23
 * Time: 9:43:21
 * To change this template use File | Settings | File Templates.
 */
public class AllTesterThread extends BaseTesterThread {

    private final PersonManager persons;

    private final ProblemManager problems;

    public AllTesterThread(PersonManager persons, ProblemManager problems) {
        this.persons = persons;
        this.problems = problems;
    }

    public void run() {
        for (Person person : persons.personList) {
            for (Problem problem : problems.problemList) {
                PersonProblemTester spt = new PersonProblemTester(person, problem, Config.getWorkDir());
                spt.runTest();
            }
        }
        MessageOutput.println(finishedMessage);
    }
}
