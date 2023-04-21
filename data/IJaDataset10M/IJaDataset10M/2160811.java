package openschool.test;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({ FormationEditorTest.class, UsersDAOTest.class, UserControllerTest.class })
public class TestSuite {
}
