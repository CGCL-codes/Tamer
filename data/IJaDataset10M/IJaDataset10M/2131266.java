package org.sqlanyware.sqlwclient.utils.lang;

import java.io.File;
import java.sql.Driver;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JarStreamClassLoaderTest extends TestCase {

    private static final Logger LOGGER = Logger.getLogger(JarStreamClassLoaderTest.class);

    public void testClassLoader() {
        try {
            final Thread currentThread = Thread.currentThread();
            final ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();
            final File file = new File("test.jar");
            final byte[] code = FileUtils.readFileToByteArray(file);
            final List<byte[]> codeList = new LinkedList<byte[]>();
            codeList.add(code);
            final JarStreamsClassLoader cl = new JarStreamsClassLoader(currentThreadClassLoader, codeList);
            final Class<?> clazz = cl.loadClass("org.sqlanyware.another.test.Class1");
            assertNotNull("The result must no be null", clazz);
        } catch (final Throwable throwable) {
            LOGGER.error(StringUtils.EMPTY, throwable);
            fail(throwable.getLocalizedMessage());
        }
    }

    public void testFilteredClassLoader() {
        try {
            final Thread currentThread = Thread.currentThread();
            final ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();
            final File file = new File("mysql-connector-java-5.1.6-bin.jar");
            final byte[] code = FileUtils.readFileToByteArray(file);
            final List<byte[]> codeList = new LinkedList<byte[]>();
            codeList.add(code);
            final JarStreamsClassLoader cl = new JarStreamsClassLoader(currentThreadClassLoader, codeList);
            cl.listClasses(new IClassFilter() {

                public boolean match(final Class<?> clazz) {
                    final Class<Driver> driverClazz = Driver.class;
                    return driverClazz.isAssignableFrom(clazz);
                }
            });
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            LOGGER.error(StringUtils.EMPTY, throwable);
            fail(throwable.getLocalizedMessage());
        }
    }

    public void testMultiFilteredClassLoader() {
        try {
            final File file = new File("db2-drivers.bun");
            final byte[] code = FileUtils.readFileToByteArray(file);
            final List<ZipChunker.Entry> entries = ZipChunker.getChunks(code);
            final List<byte[]> codes = new LinkedList<byte[]>();
            for (final ZipChunker.Entry entry : entries) {
                codes.add(entry.content);
            }
            final JarStreamsClassLoader cl = new JarStreamsClassLoader(ClassLoader.getSystemClassLoader(), codes);
            cl.listClasses(new IClassFilter() {

                public boolean match(final Class<?> clazz) {
                    final Class<Driver> driverClazz = Driver.class;
                    return driverClazz.isAssignableFrom(clazz);
                }
            });
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            LOGGER.error(StringUtils.EMPTY, throwable);
            fail(throwable.getLocalizedMessage());
        }
    }
}
