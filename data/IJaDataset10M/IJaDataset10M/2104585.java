package org.jcouchdb.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jcouchdb.db.LocalDatabaseTestCase;
import org.jcouchdb.document.DesignDocument;
import org.junit.Test;

public class JarBasedCouchDBUpdaterTestCase {

    @Test
    public void test() throws IOException {
        JarBasedCouchDBUpdater couchDBUpdater = new JarBasedCouchDBUpdater();
        couchDBUpdater.setJarFile(new File("test/org/jcouchdb/util/views.jar"));
        couchDBUpdater.setPathInsideJar("test/path/test-views/");
        couchDBUpdater.setDatabase(LocalDatabaseTestCase.createDatabaseForTest());
        List<DesignDocument> docs = couchDBUpdater.readDesignDocuments();
        new CouchDBUpdaterTestCase().testDocsIntegrity(docs);
    }

    @Test
    public void testFindClassPathJar() {
        JarBasedCouchDBUpdater couchDBUpdater = new JarBasedCouchDBUpdater();
        String easyMockRegEx = ".*/easymock[0-9\\.-]*\\.jar";
        couchDBUpdater.setJarFilePattern(easyMockRegEx);
        couchDBUpdater.setPathInsideJar("org/easymock");
        File f = couchDBUpdater.findJarFileOrSourceDirectory();
        assertThat(f.isDirectory(), is(false));
        String path = f.getPath();
        assertThat(path.matches(easyMockRegEx), is(true));
    }

    @Test
    public void testFindClassPathDir() {
        JarBasedCouchDBUpdater couchDBUpdater = new JarBasedCouchDBUpdater();
        couchDBUpdater.setJarFilePattern(".*/jcouchdb[0-9\\.]*jar");
        couchDBUpdater.setPathInsideJar("org/jcouchdb");
        File f = couchDBUpdater.findJarFileOrSourceDirectory();
        assertThat(f.isDirectory(), is(true));
    }
}
