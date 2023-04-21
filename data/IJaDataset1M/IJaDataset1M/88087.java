package de.objectcode.time4u.test;

import java.io.File;
import de.objectcode.time4u.store.RepositoryParameters;
import de.objectcode.time4u.store.db.HSQLDBRepositoryParameters;

public class HSQLDBTestContext extends TestContext {

    File m_baseDir;

    public HSQLDBTestContext() {
        m_baseDir = new File(getTestDir(), "repositories/hsqldb");
    }

    @Override
    public RepositoryParameters getRepositoryParameters() {
        return new HSQLDBRepositoryParameters(m_baseDir);
    }

    public void clearRepository() {
        File[] files = m_baseDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
