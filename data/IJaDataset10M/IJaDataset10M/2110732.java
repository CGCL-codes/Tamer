package test.AcceptanceTests;

import junit.framework.*;

public class AllAcceptanceTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("MovieSystem");
        suite.addTest(new TestSuite(LoginTest.class));
        suite.addTest(new TestSuite(MovieTest.class));
        suite.addTest(new TestSuite(AddRecommendation.class));
        suite.addTest(new TestSuite(AddUserTest.class));
        System.out.println("Good Job!");
        return suite;
    }
}
