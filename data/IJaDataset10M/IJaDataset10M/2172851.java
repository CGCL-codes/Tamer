package test.org.mandarax.reference;

import org.mandarax.kernel.InferenceEngine;
import org.mandarax.kernel.KnowledgeBase;
import org.mandarax.kernel.Query;

/**
 * Combination of sample 1 and 2. The basic structure is taken from 1,
 * but we use functions like in 2.
 * @see test.org.mandarax.reference.TestInferenceEngine1
 * @see test.org.mandarax.reference.TestInferenceEngine2
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.1
 */
public class TestInferenceEngine5 extends TestInferenceEngine {

    /**
     * Constructor.
     * @param aKnowledgeBase a new, uninitialized knowledge base that will be used
     * @param anInferenceEngine the inference engine that will be tested
     */
    public TestInferenceEngine5(KnowledgeBase aKnowledgeBase, InferenceEngine anInferenceEngine) {
        super(aKnowledgeBase, anInferenceEngine);
    }

    /**
     * Add facts and rules to the knowledge base.
     * @param knowledge org.mandarax.kernel.KnowledgeBase
     */
    public void feedKnowledgeBase(KnowledgeBase knowledge) {
        knowledge.removeAll();
        knowledge.add(Person.getRule("isGrandFatherOf", "x", "z", "isFatherOf", "y", "z", "isFatherOf", "x", "y"));
        knowledge.add(Person.getRule("isOncleOf", "x", "z", "isFatherOf", "y", "z", "isBrotherOf", "x", "y"));
        knowledge.add(Person.getRule("isOncleOf", "x", "z", "isGrandFatherOf", "y", "z", "isBrotherOf", "x", "y"));
        knowledge.add(Person.getRule("isGrandFatherOf", "z", "x", "isFatherOf", "y", "x", "isFatherOf", "z", "y"));
        knowledge.add(Person.getRule("isFatherOf", "x", "y", "equals", "x", "father(y)"));
        knowledge.add(Person.getRule("isBrotherOf", "x", "y", "equals", "z", "father(y)", "equals", "z", "father(x)", "equalsNot", "x", "y"));
        knowledge.add(Person.getFact("equals", "Klaus", "father(Jens)"));
        knowledge.add(Person.getFact("equals", "Otto", "father(Klaus)"));
        knowledge.add(Person.getFact("equals", "Otto", "father(Lutz)"));
        knowledge.add(Person.getFact("equals", "Jens", "father(Max)"));
        String[] names1 = { "Max", "Jens", "Klaus", "Otto", "Lutz" };
        String[] names2 = { "Max", "Jens", "Klaus", "Otto", "Lutz" };
        Person p1, p2 = null;
        for (int i = 0; i < names1.length; i++) {
            p1 = Person.get(names1[i]);
            for (int j = 0; j < i; j++) {
                p2 = Person.get(names2[j]);
                if (p1.equalsNot(p2)) {
                    knowledge.add(Person.getFact("equalsNot", names1[i], names2[j]));
                }
            }
        }
    }

    /**
     * Get a description of this test case.
     * This is used by the <code>org.mandarax.demo</code>
     * package to display the test cases.
     * @return a brief description of the test case
     */
    public String getDescription() {
        return "Combination of sample 1 and 2.\nThe basic structure is taken from 1,\nbut we use functions like in 2.";
    }

    /**
     * Get the name of the person we are looking for.
     * More precisely, this is the name of the person expected to
     * replace the query variable as a result of the query.
     * @return the name of the person
     */
    String getPersonName() {
        return "Lutz";
    }

    /**
     * Get the query.
     * @return a query
     */
    public Query getQuery() {
        return Person.getQuery("isOncleOf", QUERY_VARIABLE, "Max");
    }
}
