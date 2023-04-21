package net.sf.amemailchecker;

import net.sf.amemailchecker.app.db.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ DBQueryResolverTest.class, POP3MarkedCacheDAOTest.class, LetterDAOTest.class, LetterPartDAOTest.class, AccountDAOTest.class, FolderDAOTest.class })
public class DataBaseTestSuite {
}
