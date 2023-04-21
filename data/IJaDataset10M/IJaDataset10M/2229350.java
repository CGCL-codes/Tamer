package org.tranche.configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.tranche.flatfile.DataBlockUtil;
import org.tranche.flatfile.DataDirectoryConfiguration;
import org.tranche.flatfile.FlatFileTrancheServer;
import org.tranche.hash.BigHash;
import org.tranche.hash.span.HashSpan;
import org.tranche.flatfile.ServerConfiguration;
import org.tranche.security.SecurityUtil;
import org.tranche.security.Signature;
import org.tranche.users.User;
import org.tranche.users.UserZipFile;
import org.tranche.util.DevUtil;
import org.tranche.util.IOUtil;
import org.tranche.commons.RandomUtil;
import org.tranche.util.TempFileUtil;
import org.tranche.util.TestUtil;
import org.tranche.util.TrancheTestCase;

/**
 * @author Jayson Falkner - jfalkner@umich.edu
 * @author Bryan Smith - bryanesmith@gmail.com
 */
public class ConfigurationTest extends TrancheTestCase {

    public void testReadWriteVersion1() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testReadWriteVersion1()");
        OutputStream out = null;
        InputStream in = null;
        File tmp = null;
        try {
            Configuration c1 = new Configuration();
            c1.setFlags(Configuration.VERSION_ONE);
            c1.addUser(DevUtil.getDevUser());
            c1.addUser(DevUtil.getFFTSUser());
            assertEquals("Expecting certain number of users.", 2, c1.getUsers().size());
            final int hashSpanCount = RandomUtil.getInt(5) + 1;
            Set<HashSpan> hashSpanSet = new HashSet();
            for (int i = 0; i < hashSpanCount; i++) {
                hashSpanSet.add(DevUtil.createRandomHashSpan());
            }
            c1.setHashSpans(hashSpanSet);
            assertEquals("Should know number of hash span(s).", hashSpanCount, c1.getHashSpans().size());
            final int ddcCount = RandomUtil.getInt(5) + 1;
            for (int i = 0; i < ddcCount; i++) {
                int size = 1024 * 1024 * (RandomUtil.getInt(10) + 1);
                DataDirectoryConfiguration ddc = new DataDirectoryConfiguration("/path/to/data/dir" + i, size);
                c1.addDataDirectory(ddc);
            }
            assertEquals("Should know number of DDC(s).", ddcCount, c1.getDataDirectories().size());
            tmp = TempFileUtil.createTemporaryFile();
            out = new FileOutputStream(tmp);
            ConfigurationUtil.write(c1, out);
            IOUtil.safeClose(out);
            in = new FileInputStream(tmp);
            Configuration c2 = ConfigurationUtil.read(in);
            assertEquals("Should have same flag values.", c1.getFlags(), c2.getFlags());
            assertEquals("Should have same number of users.", c1.getUsers().size(), c2.getUsers().size());
            for (User u : c1.getUsers()) {
                assertTrue("Should have user.", c2.getUsers().contains(u));
            }
            for (User u : c2.getUsers()) {
                assertTrue("Should have user.", c1.getUsers().contains(u));
            }
            assertEquals("Should have same number of users.", c1.getDataDirectories().size(), c2.getDataDirectories().size());
            for (DataDirectoryConfiguration ddc : c1.getDataDirectories()) {
                assertTrue("Should have DDC.", c2.getDataDirectories().contains(ddc));
            }
            for (DataDirectoryConfiguration ddc : c2.getDataDirectories()) {
                assertTrue("Should have DDC.", c1.getDataDirectories().contains(ddc));
            }
            assertEquals("Should have same number of hash spans.", c1.getHashSpans(), c2.getHashSpans());
            for (HashSpan hs : c1.getHashSpans()) {
                assertTrue("Should have hash span.", c2.getHashSpans().contains(hs));
            }
            for (HashSpan hs : c2.getHashSpans()) {
                assertTrue("Should have hash span.", c1.getHashSpans().contains(hs));
            }
        } finally {
            IOUtil.safeClose(out);
            IOUtil.safeClose(in);
            IOUtil.safeDelete(tmp);
        }
    }

    public void testReadWriteVersion2WithoutTargetHashSpans() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testReadWriteVersion2WithoutTargetHashSpans()");
        OutputStream out = null;
        InputStream in = null;
        File tmp = null;
        try {
            Configuration c1 = new Configuration();
            c1.setFlags(Configuration.VERSION_TWO);
            c1.addUser(DevUtil.getDevUser());
            c1.addUser(DevUtil.getFFTSUser());
            c1.addUser(DevUtil.getRoutingTrancheServerUser());
            assertEquals("Expecting certain number of users.", 3, c1.getUsers().size());
            final int hashSpanCount = RandomUtil.getInt(5) + 1;
            Set<HashSpan> hashSpanSet = new HashSet();
            for (int i = 0; i < hashSpanCount; i++) {
                hashSpanSet.add(DevUtil.createRandomHashSpan());
            }
            c1.setHashSpans(hashSpanSet);
            assertEquals("Should know number of hash span(s).", hashSpanCount, c1.getHashSpans().size());
            assertEquals("Should know number of hash span(s).", 0, c1.getTargetHashSpans().size());
            final int ddcCount = RandomUtil.getInt(5) + 1;
            for (int i = 0; i < ddcCount; i++) {
                int size = 1024 * 1024 * (RandomUtil.getInt(10) + 1);
                DataDirectoryConfiguration ddc = new DataDirectoryConfiguration("/path/to/data/dir" + i, size);
                c1.addDataDirectory(ddc);
            }
            assertEquals("Should know number of DDC(s).", ddcCount, c1.getDataDirectories().size());
            tmp = TempFileUtil.createTemporaryFile();
            out = new FileOutputStream(tmp);
            ConfigurationUtil.write(c1, out);
            IOUtil.safeClose(out);
            in = new FileInputStream(tmp);
            Configuration c2 = ConfigurationUtil.read(in);
            assertEquals("Should have same flag values.", c1.getFlags(), c2.getFlags());
            assertEquals("Should have same number of users.", c1.getUsers().size(), c2.getUsers().size());
            for (User u : c1.getUsers()) {
                assertTrue("Should have user.", c2.getUsers().contains(u));
            }
            for (User u : c2.getUsers()) {
                assertTrue("Should have user.", c1.getUsers().contains(u));
            }
            assertEquals("Should have same number of users.", c1.getDataDirectories().size(), c2.getDataDirectories().size());
            for (DataDirectoryConfiguration ddc : c1.getDataDirectories()) {
                assertTrue("Should have DDC.", c2.getDataDirectories().contains(ddc));
            }
            for (DataDirectoryConfiguration ddc : c2.getDataDirectories()) {
                assertTrue("Should have DDC.", c1.getDataDirectories().contains(ddc));
            }
            assertEquals("Should have same number of hash spans.", c1.getHashSpans(), c2.getHashSpans());
            for (HashSpan hs : c1.getHashSpans()) {
                assertTrue("Should have hash span.", c2.getHashSpans().contains(hs));
            }
            for (HashSpan hs : c2.getHashSpans()) {
                assertTrue("Should have hash span.", c1.getHashSpans().contains(hs));
            }
            assertEquals("Should not have any target hash spans.", 0, c2.getTargetHashSpans().size());
        } finally {
            IOUtil.safeClose(out);
            IOUtil.safeClose(in);
            IOUtil.safeDelete(tmp);
        }
    }

    public void testReadWriteVersion2WithTargetHashSpans() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testReadWriteVersion2WithTargetHashSpans()");
        OutputStream out = null;
        InputStream in = null;
        File tmp = null;
        try {
            Configuration c1 = new Configuration();
            c1.setFlags(Configuration.VERSION_TWO);
            c1.addUser(DevUtil.getDevUser());
            c1.addUser(DevUtil.getFFTSUser());
            c1.addUser(DevUtil.getRoutingTrancheServerUser());
            assertEquals("Expecting certain number of users.", 3, c1.getUsers().size());
            final int hashSpanCount = RandomUtil.getInt(5) + 1;
            Set<HashSpan> hashSpanSet = new HashSet();
            for (int i = 0; i < hashSpanCount; i++) {
                hashSpanSet.add(DevUtil.createRandomHashSpan());
            }
            c1.setHashSpans(hashSpanSet);
            assertEquals("Should know number of hash span(s).", hashSpanCount, c1.getHashSpans().size());
            final int targetHashSpanCount = RandomUtil.getInt(5) + 1;
            Set<HashSpan> targetHashSpanSet = new HashSet();
            for (int i = 0; i < targetHashSpanCount; i++) {
                targetHashSpanSet.add(DevUtil.createRandomHashSpan());
            }
            c1.setTargetHashSpans(targetHashSpanSet);
            assertEquals("Should know number of hash span(s).", targetHashSpanCount, c1.getTargetHashSpans().size());
            final int ddcCount = RandomUtil.getInt(5) + 1;
            for (int i = 0; i < ddcCount; i++) {
                int size = 1024 * 1024 * (RandomUtil.getInt(10) + 1);
                DataDirectoryConfiguration ddc = new DataDirectoryConfiguration("/path/to/data/dir" + i, size);
                c1.addDataDirectory(ddc);
            }
            assertEquals("Should know number of DDC(s).", ddcCount, c1.getDataDirectories().size());
            tmp = TempFileUtil.createTemporaryFile();
            out = new FileOutputStream(tmp);
            ConfigurationUtil.write(c1, out);
            IOUtil.safeClose(out);
            in = new FileInputStream(tmp);
            Configuration c2 = ConfigurationUtil.read(in);
            assertEquals("Should have same flag values.", c1.getFlags(), c2.getFlags());
            assertEquals("Should have same number of users.", c1.getUsers().size(), c2.getUsers().size());
            for (User u : c1.getUsers()) {
                assertTrue("Should have user.", c2.getUsers().contains(u));
            }
            for (User u : c2.getUsers()) {
                assertTrue("Should have user.", c1.getUsers().contains(u));
            }
            assertEquals("Should have same number of users.", c1.getDataDirectories().size(), c2.getDataDirectories().size());
            for (DataDirectoryConfiguration ddc : c1.getDataDirectories()) {
                assertTrue("Should have DDC.", c2.getDataDirectories().contains(ddc));
            }
            for (DataDirectoryConfiguration ddc : c2.getDataDirectories()) {
                assertTrue("Should have DDC.", c1.getDataDirectories().contains(ddc));
            }
            assertEquals("Should have same number of hash spans.", c1.getHashSpans(), c2.getHashSpans());
            for (HashSpan hs : c1.getHashSpans()) {
                assertTrue("Should have hash span.", c2.getHashSpans().contains(hs));
            }
            for (HashSpan hs : c2.getHashSpans()) {
                assertTrue("Should have hash span.", c1.getHashSpans().contains(hs));
            }
            assertEquals("Should have same number of target hash spans.", c1.getTargetHashSpans(), c2.getTargetHashSpans());
            for (HashSpan hs : c1.getTargetHashSpans()) {
                assertTrue("Should have target hash span.", c2.getTargetHashSpans().contains(hs));
            }
            for (HashSpan hs : c2.getTargetHashSpans()) {
                assertTrue("Should have target hash span.", c1.getTargetHashSpans().contains(hs));
            }
        } finally {
            IOUtil.safeClose(out);
            IOUtil.safeClose(in);
            IOUtil.safeDelete(tmp);
        }
    }

    /**
     * <p>Test deep-cloning functionality.</p>
     * <p>Note that this does not test deep cloning of every possible class member. However, there are already tests for cloning some class members (e.g., ServerConfiguration, HashSpan, etc.). See the respective tests for particular class member.</p>
     * @throws java.lang.Exception
     */
    public void testClone() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testClone()");
        final DataDirectoryConfiguration ddc1 = new DataDirectoryConfiguration("/path/to/data/dir", 10 * 1024 * 1024);
        Configuration c1 = new Configuration();
        c1.setFlags(Configuration.VERSION_ONE);
        c1.addUser(DevUtil.getDevUser());
        c1.addDataDirectory(ddc1);
        Configuration c2 = new Configuration();
        c2.setFlags(Configuration.VERSION_ONE);
        c2.addUser(DevUtil.getDevUser());
        c2.addDataDirectory(new DataDirectoryConfiguration("/path/to/a/differenet/data/dir/than/first", 5 * 1024 * 1024));
        assertFalse("The two configs must be different.", c1.equals(c2));
        assertEquals("Should have same flags.", c1.getFlags(), c2.getFlags());
        assertEquals("Should have same user.", c1.getUsers(), c2.getUsers());
        assertFalse("Should have different data directories.", c1.getDataDirectories().equals(c2.getDataDirectories()));
        Configuration c1Clone = c1.clone();
        Configuration c2Clone = c2.clone();
        assertNotSame("Should not have same reference.", c1, c1Clone);
        assertNotSame("Should not have same reference.", c2, c2Clone);
        assertEquals("Should have same info.", c1.getFlags(), c1Clone.getFlags());
        assertEquals("Should have same info.", c1.getUsers(), c1Clone.getUsers());
        assertEquals("Should have same info.", c1.getDataDirectories(), c1Clone.getDataDirectories());
        assertEquals("Should have same info.", c2.getFlags(), c2Clone.getFlags());
        assertEquals("Should have same info.", c2.getUsers(), c2Clone.getUsers());
        assertEquals("Should have same info.", c2.getDataDirectories(), c2Clone.getDataDirectories());
        c1Clone.setDataDirectories(c2.getDataDirectories());
        assertEquals("Should have same info.", c2.getDataDirectories(), c1Clone.getDataDirectories());
        assertFalse("Should have different data directories.", c1.getDataDirectories().equals(c1Clone.getDataDirectories()));
        Set<DataDirectoryConfiguration> ddc1Set = new HashSet();
        ddc1Set.add(ddc1);
        assertEquals("Original shouldn't have changed.", ddc1Set, c1.getDataDirectories());
    }

    public void testReadWriteInMemory() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testReadWriteInMemory()");
        testReadWrite(false);
    }

    public void testReadWriteToDisk() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testReadWriteToDisk()");
        testReadWrite(true);
    }

    public void testReadWrite(boolean toDisk) throws Exception {
        File dir = TempFileUtil.createTemporaryDirectory();
        try {
            int flags = Configuration.VERSION_ONE;
            HashSet<User> users = new HashSet();
            for (int i = 0; i < RandomUtil.getInt(20) + 1; i++) {
                users.add(DevUtil.makeNewUserWithRandomFlags());
            }
            HashSet<DataDirectoryConfiguration> ddcs = new HashSet();
            for (int i = 0; i < RandomUtil.getInt(10) + 1; i++) {
                ddcs.add(DevUtil.makeNewDataDirectoryConfiguration());
            }
            HashSet<ServerConfiguration> scs = new HashSet();
            for (int i = 0; i < RandomUtil.getInt(5) + 1; i++) {
                scs.add(DevUtil.makeNewServerConfiguration(0));
            }
            Set<BigHash> stickyProjects = new HashSet();
            for (int i = 0; i < RandomUtil.getInt(30) + 1; i++) {
                stickyProjects.add(DevUtil.getRandomBigHash(RandomUtil.getInt(DataBlockUtil.getMaxChunkSize())));
            }
            HashSet<HashSpan> hashSpans = new HashSet();
            for (int i = 0; i < RandomUtil.getInt(5) + 1; i++) {
                hashSpans.add(DevUtil.makeRandomHashSpan());
            }
            Map<String, String> keys = new HashMap<String, String>();
            for (int i = 0; i < RandomUtil.getInt(20) + 1; i++) {
                keys.put(RandomUtil.getString(RandomUtil.getInt(50) + 1), RandomUtil.getString(RandomUtil.getInt(50) + 1));
            }
            Configuration config = new Configuration();
            config.setFlags(flags);
            config.setServerConfigs(scs);
            config.setDataDirectories(ddcs);
            config.setUsers(users);
            config.setStickyProjects(stickyProjects);
            config.setHashSpans(hashSpans);
            for (String key : keys.keySet()) {
                config.setValue(key, keys.get(key));
            }
            for (User user1 : users) {
                boolean found = false;
                for (User user2 : config.getUsers()) {
                    if (user2.equals(user1)) {
                        found = true;
                        break;
                    }
                }
                assertTrue("Expect all users to be in configuration.", found);
            }
            for (HashSpan hashSpan1 : hashSpans) {
                boolean found = false;
                for (HashSpan hashSpan2 : config.getHashSpans()) {
                    if (hashSpan2.equals(hashSpan1)) {
                        found = true;
                        break;
                    }
                }
                assertTrue("Expect all hash spans to be in configuration.", found);
            }
            for (BigHash hash1 : stickyProjects) {
                boolean found = false;
                for (BigHash hash2 : config.getStickyProjects()) {
                    if (hash2.equals(hash1)) {
                        found = true;
                        break;
                    }
                }
                assertTrue("Expect all sticky projects to be in configuration.", found);
            }
            for (DataDirectoryConfiguration ddc1 : ddcs) {
                boolean found = false;
                for (DataDirectoryConfiguration ddc2 : config.getDataDirectories()) {
                    if (ddc2.equals(ddc1)) {
                        found = true;
                        break;
                    }
                }
                assertTrue("Expect all data directories to be in configuration.", found);
            }
            for (ServerConfiguration sc1 : scs) {
                boolean found = false;
                for (ServerConfiguration sc2 : config.getServerConfigs()) {
                    if (sc2.equals(sc1)) {
                        found = true;
                        break;
                    }
                }
                assertTrue("Expect all server configs to be in configuration.", found);
            }
            for (String key1 : keys.keySet()) {
                assertTrue("Expect config to say it has all keys.", config.hasKey(key1));
                boolean found = false;
                for (String key2 : config.getValueKeys()) {
                    if (key2.equals(key1) && config.getValue(key2).equals(keys.get(key1))) {
                        found = true;
                        break;
                    }
                }
                assertTrue("Expect config to contain all keys.", found);
            }
            FileOutputStream out = null;
            FileInputStream in = null;
            ByteArrayOutputStream baos = null;
            ByteArrayInputStream bais = null;
            try {
                Configuration config2 = null;
                if (toDisk) {
                    File configFile = TempFileUtil.createTemporaryFile();
                    out = new FileOutputStream(configFile);
                    ConfigurationUtil.write(config, out);
                    in = new FileInputStream(configFile);
                    config2 = ConfigurationUtil.read(in);
                } else {
                    baos = new ByteArrayOutputStream();
                    ConfigurationUtil.write(config, baos);
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    config2 = ConfigurationUtil.read(bais);
                }
                assertEquals("Same flags expected.", flags, config2.getFlags());
                assertEquals("Same users list size expected", users.size(), config2.getUsers().size());
                assertEquals("Same data directory list size expected", ddcs.size(), config2.getDataDirectories().size());
                assertEquals("Same server config list size expected", scs.size(), config2.getServerConfigs().size());
                assertEquals("Same sticky projects list size expected.", stickyProjects.size(), config2.getStickyProjects().size());
                assertEquals("Same hash span list size expected.", hashSpans.size(), config2.getHashSpans().size());
                assertEquals("Should be an equal number of name value pairs.", keys.keySet().size(), config2.getValueKeys().size());
                assertEquals("Should be an equal number of name value pairs.", keys.keySet().size(), config2.numberKeyValuePairs());
                for (User user1 : users) {
                    boolean found = false;
                    for (User user2 : config2.getUsers()) {
                        if (user2.equals(user1)) {
                            found = true;
                            break;
                        }
                    }
                    assertTrue("Expect all users to be in configuration.", found);
                }
                for (HashSpan hashSpan1 : hashSpans) {
                    boolean found = false;
                    for (HashSpan hashSpan2 : config2.getHashSpans()) {
                        if (hashSpan2.equals(hashSpan1)) {
                            found = true;
                            break;
                        }
                    }
                    assertTrue("Expect all hash spans to be in configuration.", found);
                }
                for (BigHash hash1 : stickyProjects) {
                    boolean found = false;
                    for (BigHash hash2 : config2.getStickyProjects()) {
                        if (hash2.equals(hash1)) {
                            found = true;
                            break;
                        }
                    }
                    assertTrue("Expect all sticky projects to be in configuration.", found);
                }
                for (DataDirectoryConfiguration ddc1 : ddcs) {
                    boolean found = false;
                    for (DataDirectoryConfiguration ddc2 : config2.getDataDirectories()) {
                        if (ddc2.equals(ddc1)) {
                            found = true;
                            break;
                        }
                    }
                    assertTrue("Expect all data directories to be in configuration.", found);
                }
                for (ServerConfiguration sc1 : scs) {
                    boolean found = false;
                    for (ServerConfiguration sc2 : config2.getServerConfigs()) {
                        if (sc2.equals(sc1)) {
                            found = true;
                            break;
                        }
                    }
                    assertTrue("Expect all server configs to be in configuration.", found);
                }
                for (String key1 : keys.keySet()) {
                    assertTrue("Expected configuration to have key.", config2.hasKey(key1));
                    boolean found = false;
                    for (String key2 : config2.getValueKeys()) {
                        if (key1.equals(key2) && config2.getValue(key2).equals(keys.get(key1))) {
                            found = true;
                            break;
                        }
                    }
                    assertTrue("Expected to find all added keys with the same value.", found);
                }
            } finally {
                IOUtil.safeClose(baos);
                IOUtil.safeClose(bais);
            }
        } finally {
            IOUtil.recursiveDeleteWithWarning(dir);
        }
    }

    /**
     * Test switching around the directory.
     */
    public void testChangeDataDirectory() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testChangeDataDirectory()");
        File dir = TempFileUtil.createTemporaryDirectory();
        FlatFileTrancheServer ffts = new FlatFileTrancheServer(dir);
        try {
            UserZipFile uzf = DevUtil.getDevUser();
            ffts.getConfiguration().getUsers().add(uzf);
            Configuration config = new Configuration();
            long size = (long) (Long.MAX_VALUE * Math.random());
            String name = dir.getAbsolutePath() + File.separatorChar + "foo";
            DataDirectoryConfiguration newDataConfig = new DataDirectoryConfiguration(name, size);
            config.getDataDirectories().add(newDataConfig);
            config.getUsers().add(uzf);
            IOUtil.setConfiguration(ffts, config, uzf.getCertificate(), uzf.getPrivateKey());
            byte[] nonce = IOUtil.getNonce(ffts);
            Signature sig = new Signature(SecurityUtil.sign(new ByteArrayInputStream(nonce), uzf.getPrivateKey()), SecurityUtil.getSignatureAlgorithm(uzf.getPrivateKey()), uzf.getCertificate());
            Configuration updateConfig = ffts.getConfiguration(sig, nonce);
            assertEquals(updateConfig.getDataDirectories().size(), 1);
        } finally {
            IOUtil.safeClose(ffts);
            IOUtil.recursiveDeleteWithWarning(dir);
        }
    }

    /**
     * <p>Resets memory and makes sure effects don't require shutdown.</p>
     * <p>The test will progress as follows:</p>
     * <ul>
     *   <li>Start test w/ three directories. Make sure has data/meta.</li>
     *   <li>Remove one directory and add another. Make sure appropriate data/meta.</li>
     *   <li>Go down to no directories, shouldn't have any data/meta.</li>
     *   <li>Add all four, should have all data/meta.</li>
     * </ul>
     */
    public void testSetConfigurationImmediateEffects() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testSetConfigurationImmediateEffects()");
        File tempDir = TempFileUtil.createTemporaryDirectory();
        try {
            File dir1 = new File(tempDir, "dir1"), dir2 = new File(tempDir, "dir2"), dir3 = new File(tempDir, "dir3"), dir4 = new File(tempDir, "dir4");
            assertTrue("Should be able to make " + dir1.getAbsolutePath(), dir1.mkdirs());
            assertTrue("Should be able to make " + dir2.getAbsolutePath(), dir2.mkdirs());
            assertTrue("Should be able to make " + dir3.getAbsolutePath(), dir3.mkdirs());
            assertTrue("Should be able to make " + dir4.getAbsolutePath(), dir4.mkdirs());
            DataDirectoryConfiguration ddc1 = new DataDirectoryConfiguration(dir1.getAbsolutePath(), Long.MAX_VALUE), ddc2 = new DataDirectoryConfiguration(dir2.getAbsolutePath(), Long.MAX_VALUE), ddc3 = new DataDirectoryConfiguration(dir3.getAbsolutePath(), Long.MAX_VALUE), ddc4 = new DataDirectoryConfiguration(dir4.getAbsolutePath(), Long.MAX_VALUE);
            Set<BigHash> dataHashes1 = new HashSet(), metaHashes1 = new HashSet(), dataHashes2 = new HashSet(), metaHashes2 = new HashSet(), dataHashes3 = new HashSet(), metaHashes3 = new HashSet(), dataHashes4 = new HashSet(), metaHashes4 = new HashSet();
            FlatFileTrancheServer ffserver = null;
            try {
                ffserver = new FlatFileTrancheServer(tempDir);
                ffserver.getConfiguration().getUsers().add(DevUtil.getDevUser());
                Configuration c = ffserver.getConfiguration();
                c.getDataDirectories().clear();
                c.getDataDirectories().add(ddc1);
                IOUtil.setConfiguration(ffserver, c, DevUtil.getDevAuthority(), DevUtil.getDevPrivateKey());
                dataHashes1 = addSomeDataToServer(false, ffserver);
                metaHashes1 = addSomeDataToServer(true, ffserver);
                for (BigHash h : dataHashes1) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : metaHashes1) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (DataDirectoryConfiguration ddc : c.getDataDirectories()) {
                    assertTrue("DataDirectoryConfiguration should have files: " + ddc.getDirectory(), ddc.getDirectoryFile().list().length > 0);
                }
                assertEquals("Expecting 1 ddcs.", 1, c.getDataDirectories().size());
                c.getDataDirectories().add(ddc2);
                IOUtil.setConfiguration(ffserver, c, DevUtil.getDevAuthority(), DevUtil.getDevPrivateKey());
                dataHashes2 = addSomeDataToServer(false, ffserver);
                metaHashes2 = addSomeDataToServer(true, ffserver);
                for (BigHash h : dataHashes1) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : dataHashes2) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : metaHashes1) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (BigHash h : metaHashes2) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (DataDirectoryConfiguration ddc : c.getDataDirectories()) {
                    assertTrue("DataDirectoryConfiguration should have files: " + ddc.getDirectory(), ddc.getDirectoryFile().list().length > 0);
                }
                assertEquals("Expecting 2 ddcs.", 2, c.getDataDirectories().size());
                c.getDataDirectories().add(ddc3);
                IOUtil.setConfiguration(ffserver, c, DevUtil.getDevAuthority(), DevUtil.getDevPrivateKey());
                dataHashes3 = addSomeDataToServer(false, ffserver);
                metaHashes3 = addSomeDataToServer(true, ffserver);
                for (BigHash h : dataHashes1) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : dataHashes2) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : dataHashes3) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : metaHashes1) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (BigHash h : metaHashes2) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (BigHash h : metaHashes3) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (DataDirectoryConfiguration ddc : c.getDataDirectories()) {
                    assertTrue("DataDirectoryConfiguration should have files: " + ddc.getDirectory(), ddc.getDirectoryFile().list().length > 0);
                }
                assertEquals("Expecting 3 ddcs.", 3, c.getDataDirectories().size());
                c.getDataDirectories().add(ddc4);
                IOUtil.setConfiguration(ffserver, c, DevUtil.getDevAuthority(), DevUtil.getDevPrivateKey());
                dataHashes4 = addSomeDataToServer(false, ffserver);
                metaHashes4 = addSomeDataToServer(true, ffserver);
                for (BigHash h : dataHashes1) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : dataHashes2) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : dataHashes3) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : dataHashes4) {
                    assertTrue("Not found.", IOUtil.hasData(ffserver, h));
                }
                for (BigHash h : metaHashes1) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (BigHash h : metaHashes2) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (BigHash h : metaHashes3) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (BigHash h : metaHashes4) {
                    assertTrue("Not found.", IOUtil.hasMetaData(ffserver, h));
                }
                for (DataDirectoryConfiguration ddc : c.getDataDirectories()) {
                    assertTrue("DataDirectoryConfiguration should have files: " + ddc.getDirectory(), ddc.getDirectoryFile().list().length > 0);
                }
                assertEquals("Expecting 4 ddcs.", 4, c.getDataDirectories().size());
            } finally {
                IOUtil.safeClose(ffserver);
                IOUtil.recursiveDelete(tempDir);
            }
        } finally {
            IOUtil.recursiveDeleteWithWarning(tempDir);
        }
    }

    private static Set<BigHash> addSomeDataToServer(boolean isMetaData, FlatFileTrancheServer ffserver) throws Exception {
        Set<BigHash> hashes = new HashSet();
        int count = 1 + RandomUtil.getInt(9);
        byte[] data;
        BigHash hash;
        for (int i = 0; i < count; i++) {
            if (!isMetaData) {
                int size = RandomUtil.getInt(1024 * 1024 - 1024) + 1024;
                data = new byte[size];
                RandomUtil.getBytes(data);
            } else {
                data = DevUtil.createRandomMetaDataChunk();
            }
            hash = new BigHash(data);
            hashes.add(hash);
            if (isMetaData) {
                IOUtil.setMetaData(ffserver, DevUtil.getDevAuthority(), DevUtil.getDevPrivateKey(), false, hash, data);
            } else {
                IOUtil.setData(ffserver, DevUtil.getDevAuthority(), DevUtil.getDevPrivateKey(), hash, data);
            }
        }
        return hashes;
    }

    /**
     * Ensures that a user added to config can both act as an authority and a user.
     */
    public static void testSignedUsersAndAuthorities() throws Exception {
        TestUtil.printTitle("ConfigurationTest:testSignedUsersAndAuthorities()");
        FlatFileTrancheServer ffserver = null;
        try {
            ffserver = new FlatFileTrancheServer(TempFileUtil.createTemporaryDirectory());
            UserZipFile uzfAdmin = DevUtil.createUser("admin", "adminpass", TempFileUtil.createTempFileWithName(".zip.encrypted").getAbsolutePath(), true, false);
            UserZipFile uzfSigned = DevUtil.createSignedUser("writer", "secret", TempFileUtil.createTemporaryFile(".zip.encrypted").getAbsolutePath(), uzfAdmin.getCertificate(), uzfAdmin.getPrivateKey(), false, false);
            uzfSigned.setFlags(User.CAN_SET_DATA | User.CAN_SET_META_DATA | User.CAN_GET_CONFIGURATION);
            ffserver.getConfiguration().getUsers().add(uzfSigned);
            ffserver.saveConfiguration();
            try {
                IOUtil.setConfiguration(ffserver, ffserver.getConfiguration(), uzfSigned.getCertificate(), uzfSigned.getPrivateKey());
                fail("Should throw an exception -- user not an admin");
            } catch (Exception expecting) {
            }
            try {
                IOUtil.setConfiguration(ffserver, ffserver.getConfiguration(), uzfAdmin.getCertificate(), uzfAdmin.getPrivateKey());
                fail("Admin not added yet, shouldn't be able to set config");
            } catch (Exception expecting) {
            }
            ffserver.getConfiguration().getUsers().add(uzfAdmin);
            ffserver.saveConfiguration();
            try {
                IOUtil.setConfiguration(ffserver, ffserver.getConfiguration(), uzfSigned.getCertificate(), uzfSigned.getPrivateKey());
                fail("Should throw an exception -- user not an admin");
            } catch (Exception expecting) {
            }
            IOUtil.setConfiguration(ffserver, ffserver.getConfiguration(), uzfAdmin.getCertificate(), uzfAdmin.getPrivateKey());
        } finally {
            IOUtil.safeClose(ffserver);
        }
    }

    public void testSetValueClobbersPrevious() {
        TestUtil.printTitle("ConfigurationTest:testSetValueClobbersPrevious()");
        Configuration c = new Configuration();
        int valuesCount = c.getValueKeys().size();
        c.setValue("Bryan", "One");
        c.setValue("Bryan", "Two");
        assertEquals("Expecting new count to be one greater.", valuesCount + 1, c.getValueKeys().size());
        assertEquals("Expecting certain value.", "Two", c.getValue("Bryan"));
    }
}
