package org.apache.hadoop.filecache;

import org.apache.commons.logging.*;
import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.fs.*;
import java.net.URI;

/**
 * Distribute application-specific large, read-only files efficiently.
 * 
 * <p><code>DistributedCache</code> is a facility provided by the Map-Reduce
 * framework to cache files (text, archives, jars etc.) needed by applications.
 * </p>
 * 
 * <p>Applications specify the files, via urls (hdfs:// or http://) to be cached 
 * via the {@link org.apache.hadoop.mapred.JobConf}.
 * The <code>DistributedCache</code> assumes that the
 * files specified via hdfs:// urls are already present on the 
 * {@link FileSystem} at the path specified by the url.</p>
 * 
 * <p>The framework will copy the necessary files on to the slave node before 
 * any tasks for the job are executed on that node. Its efficiency stems from 
 * the fact that the files are only copied once per job and the ability to 
 * cache archives which are un-archived on the slaves.</p> 
 *
 * <p><code>DistributedCache</code> can be used to distribute simple, read-only
 * data/text files and/or more complex types such as archives, jars etc. 
 * Archives (zip, tar and tgz/tar.gz files) are un-archived at the slave nodes. 
 * Jars may be optionally added to the classpath of the tasks, a rudimentary 
 * software distribution mechanism.  Files have execution permissions.
 * Optionally users can also direct it to symlink the distributed cache file(s)
 * into the working directory of the task.</p>
 * 
 * <p><code>DistributedCache</code> tracks modification timestamps of the cache 
 * files. Clearly the cache files should not be modified by the application 
 * or externally while the job is executing.</p>
 * 
 * <p>Here is an illustrative example on how to use the 
 * <code>DistributedCache</code>:</p>
 * <p><blockquote><pre>
 *     // Setting up the cache for the application
 *     
 *     1. Copy the requisite files to the <code>FileSystem</code>:
 *     
 *     $ bin/hadoop fs -copyFromLocal lookup.dat /myapp/lookup.dat  
 *     $ bin/hadoop fs -copyFromLocal map.zip /myapp/map.zip  
 *     $ bin/hadoop fs -copyFromLocal mylib.jar /myapp/mylib.jar
 *     $ bin/hadoop fs -copyFromLocal mytar.tar /myapp/mytar.tar
 *     $ bin/hadoop fs -copyFromLocal mytgz.tgz /myapp/mytgz.tgz
 *     $ bin/hadoop fs -copyFromLocal mytargz.tar.gz /myapp/mytargz.tar.gz
 *     
 *     2. Setup the application's <code>JobConf</code>:
 *     
 *     JobConf job = new JobConf();
 *     DistributedCache.addCacheFile(new URI("/myapp/lookup.dat#lookup.dat"), 
 *                                   job);
 *     DistributedCache.addCacheArchive(new URI("/myapp/map.zip", job);
 *     DistributedCache.addFileToClassPath(new Path("/myapp/mylib.jar"), job);
 *     DistributedCache.addCacheArchive(new URI("/myapp/mytar.tar", job);
 *     DistributedCache.addCacheArchive(new URI("/myapp/mytgz.tgz", job);
 *     DistributedCache.addCacheArchive(new URI("/myapp/mytargz.tar.gz", job);
 *     
 *     3. Use the cached files in the {@link org.apache.hadoop.mapred.Mapper}
 *     or {@link org.apache.hadoop.mapred.Reducer}:
 *     
 *     public static class MapClass extends MapReduceBase  
 *     implements Mapper&lt;K, V, K, V&gt; {
 *     
 *       private Path[] localArchives;
 *       private Path[] localFiles;
 *       
 *       public void configure(JobConf job) {
 *         // Get the cached archives/files
 *         localArchives = DistributedCache.getLocalCacheArchives(job);
 *         localFiles = DistributedCache.getLocalCacheFiles(job);
 *       }
 *       
 *       public void map(K key, V value, 
 *                       OutputCollector&lt;K, V&gt; output, Reporter reporter) 
 *       throws IOException {
 *         // Use data from the cached archives/files here
 *         // ...
 *         // ...
 *         output.collect(k, v);
 *       }
 *     }
 *     
 * </pre></blockquote></p>
 * 
 * @see org.apache.hadoop.mapred.JobConf
 * @see org.apache.hadoop.mapred.JobClient
 */
public class DistributedCache {

    private static TreeMap<String, CacheStatus> cachedArchives = new TreeMap<String, CacheStatus>();

    private static final long DEFAULT_CACHE_SIZE = 1048576L;

    private static final Log LOG = LogFactory.getLog(DistributedCache.class);

    /**
   * Get the locally cached file or archive; it could either be 
   * previously cached (and valid) or copy it from the {@link FileSystem} now.
   * 
   * @param cache the cache to be localized, this should be specified as 
   * new URI(hdfs://hostname:port/absolute_path_to_file#LINKNAME). If no schema 
   * or hostname:port is provided the file is assumed to be in the filesystem
   * being used in the Configuration
   * @param conf The Confguration file which contains the filesystem
   * @param baseDir The base cache Dir where you wnat to localize the files/archives
   * @param fileStatus The file status on the dfs.
   * @param isArchive if the cache is an archive or a file. In case it is an
   *  archive with a .zip or .jar or .tar or .tgz or .tar.gz extension it will
   *  be unzipped/unjarred/untarred automatically 
   *  and the directory where the archive is unzipped/unjarred/untarred is
   *  returned as the Path.
   *  In case of a file, the path to the file is returned
   * @param confFileStamp this is the hdfs file modification timestamp to verify that the 
   * file to be cached hasn't changed since the job started
   * @param currentWorkDir this is the directory where you would want to create symlinks 
   * for the locally cached files/archives
   * @return the path to directory where the archives are unjarred in case of archives,
   * the path to the file where the file is copied locally 
   * @throws IOException
   */
    public static Path getLocalCache(URI cache, Configuration conf, Path baseDir, FileStatus fileStatus, boolean isArchive, long confFileStamp, Path currentWorkDir) throws IOException {
        return getLocalCache(cache, conf, baseDir, fileStatus, isArchive, confFileStamp, currentWorkDir, true);
    }

    /**
   * Get the locally cached file or archive; it could either be 
   * previously cached (and valid) or copy it from the {@link FileSystem} now.
   * 
   * @param cache the cache to be localized, this should be specified as 
   * new URI(hdfs://hostname:port/absolute_path_to_file#LINKNAME). If no schema 
   * or hostname:port is provided the file is assumed to be in the filesystem
   * being used in the Configuration
   * @param conf The Confguration file which contains the filesystem
   * @param baseDir The base cache Dir where you wnat to localize the files/archives
   * @param fileStatus The file status on the dfs.
   * @param isArchive if the cache is an archive or a file. In case it is an
   *  archive with a .zip or .jar or .tar or .tgz or .tar.gz extension it will
   *  be unzipped/unjarred/untarred automatically 
   *  and the directory where the archive is unzipped/unjarred/untarred is
   *  returned as the Path.
   *  In case of a file, the path to the file is returned
   * @param confFileStamp this is the hdfs file modification timestamp to verify that the 
   * file to be cached hasn't changed since the job started
   * @param currentWorkDir this is the directory where you would want to create symlinks 
   * for the locally cached files/archives
   * @param honorSymLinkConf if this is false, then the symlinks are not
   * created even if conf says so (this is required for an optimization in task
   * launches
   * @return the path to directory where the archives are unjarred in case of archives,
   * the path to the file where the file is copied locally 
   * @throws IOException
   */
    public static Path getLocalCache(URI cache, Configuration conf, Path baseDir, FileStatus fileStatus, boolean isArchive, long confFileStamp, Path currentWorkDir, boolean honorSymLinkConf) throws IOException {
        String cacheId = makeRelative(cache, conf);
        CacheStatus lcacheStatus;
        Path localizedPath;
        synchronized (cachedArchives) {
            lcacheStatus = cachedArchives.get(cacheId);
            if (lcacheStatus == null) {
                lcacheStatus = new CacheStatus(new Path(baseDir, new Path(cacheId)));
                cachedArchives.put(cacheId, lcacheStatus);
            }
            synchronized (lcacheStatus) {
                localizedPath = localizeCache(conf, cache, confFileStamp, lcacheStatus, fileStatus, isArchive, currentWorkDir, honorSymLinkConf);
                lcacheStatus.refcount++;
            }
        }
        long size = FileUtil.getDU(new File(baseDir.toString()));
        long allowedSize = conf.getLong("local.cache.size", DEFAULT_CACHE_SIZE);
        if (allowedSize < size) {
            deleteCache(conf);
        }
        return localizedPath;
    }

    /**
   * Get the locally cached file or archive; it could either be 
   * previously cached (and valid) or copy it from the {@link FileSystem} now.
   * 
   * @param cache the cache to be localized, this should be specified as 
   * new URI(hdfs://hostname:port/absolute_path_to_file#LINKNAME). If no schema 
   * or hostname:port is provided the file is assumed to be in the filesystem
   * being used in the Configuration
   * @param conf The Confguration file which contains the filesystem
   * @param baseDir The base cache Dir where you wnat to localize the files/archives
   * @param isArchive if the cache is an archive or a file. In case it is an 
   *  archive with a .zip or .jar or .tar or .tgz or .tar.gz extension it will 
   *  be unzipped/unjarred/untarred automatically 
   *  and the directory where the archive is unzipped/unjarred/untarred 
   *  is returned as the Path.
   *  In case of a file, the path to the file is returned
   * @param confFileStamp this is the hdfs file modification timestamp to verify that the 
   * file to be cached hasn't changed since the job started
   * @param currentWorkDir this is the directory where you would want to create symlinks 
   * for the locally cached files/archives
   * @return the path to directory where the archives are unjarred in case of archives,
   * the path to the file where the file is copied locally 
   * @throws IOException

   */
    public static Path getLocalCache(URI cache, Configuration conf, Path baseDir, boolean isArchive, long confFileStamp, Path currentWorkDir) throws IOException {
        return getLocalCache(cache, conf, baseDir, null, isArchive, confFileStamp, currentWorkDir);
    }

    /**
   * This is the opposite of getlocalcache. When you are done with
   * using the cache, you need to release the cache
   * @param cache The cache URI to be released
   * @param conf configuration which contains the filesystem the cache 
   * is contained in.
   * @throws IOException
   */
    public static void releaseCache(URI cache, Configuration conf) throws IOException {
        String cacheId = makeRelative(cache, conf);
        synchronized (cachedArchives) {
            CacheStatus lcacheStatus = cachedArchives.get(cacheId);
            if (lcacheStatus == null) return;
            synchronized (lcacheStatus) {
                lcacheStatus.refcount--;
            }
        }
    }

    private static void deleteCache(Configuration conf) throws IOException {
        synchronized (cachedArchives) {
            for (Iterator it = cachedArchives.keySet().iterator(); it.hasNext(); ) {
                String cacheId = (String) it.next();
                CacheStatus lcacheStatus = cachedArchives.get(cacheId);
                synchronized (lcacheStatus) {
                    if (lcacheStatus.refcount == 0) {
                        FileSystem.getLocal(conf).delete(lcacheStatus.localLoadPath, true);
                        it.remove();
                    }
                }
            }
        }
    }

    public static String makeRelative(URI cache, Configuration conf) throws IOException {
        String host = cache.getHost();
        if (host == null) {
            host = cache.getScheme();
        }
        if (host == null) {
            URI defaultUri = FileSystem.get(conf).getUri();
            host = defaultUri.getHost();
            if (host == null) {
                host = defaultUri.getScheme();
            }
        }
        String path = host + cache.getPath();
        path = path.replace(":/", "/");
        return path;
    }

    private static Path cacheFilePath(Path p) {
        return new Path(p, p.getName());
    }

    private static Path localizeCache(Configuration conf, URI cache, long confFileStamp, CacheStatus cacheStatus, FileStatus fileStatus, boolean isArchive, Path currentWorkDir, boolean honorSymLinkConf) throws IOException {
        boolean doSymlink = honorSymLinkConf && getSymlink(conf);
        if (cache.getFragment() == null) {
            doSymlink = false;
        }
        FileSystem fs = getFileSystem(cache, conf);
        String link = currentWorkDir.toString() + Path.SEPARATOR + cache.getFragment();
        File flink = new File(link);
        if (ifExistsAndFresh(conf, fs, cache, confFileStamp, cacheStatus, fileStatus)) {
            if (isArchive) {
                if (doSymlink) {
                    if (!flink.exists()) FileUtil.symLink(cacheStatus.localLoadPath.toString(), link);
                }
                return cacheStatus.localLoadPath;
            } else {
                if (doSymlink) {
                    if (!flink.exists()) FileUtil.symLink(cacheFilePath(cacheStatus.localLoadPath).toString(), link);
                }
                return cacheFilePath(cacheStatus.localLoadPath);
            }
        } else {
            if (cacheStatus.refcount > 1 && (cacheStatus.currentStatus == true)) throw new IOException("Cache " + cacheStatus.localLoadPath.toString() + " is in use and cannot be refreshed");
            FileSystem localFs = FileSystem.getLocal(conf);
            localFs.delete(cacheStatus.localLoadPath, true);
            Path parchive = new Path(cacheStatus.localLoadPath, new Path(cacheStatus.localLoadPath.getName()));
            if (!localFs.mkdirs(cacheStatus.localLoadPath)) {
                throw new IOException("Mkdirs failed to create directory " + cacheStatus.localLoadPath.toString());
            }
            String cacheId = cache.getPath();
            fs.copyToLocalFile(new Path(cacheId), parchive);
            if (isArchive) {
                String tmpArchive = parchive.toString().toLowerCase();
                File srcFile = new File(parchive.toString());
                File destDir = new File(parchive.getParent().toString());
                if (tmpArchive.endsWith(".jar")) {
                    RunJar.unJar(srcFile, destDir);
                } else if (tmpArchive.endsWith(".zip")) {
                    FileUtil.unZip(srcFile, destDir);
                } else if (isTarFile(tmpArchive)) {
                    FileUtil.unTar(srcFile, destDir);
                }
            }
            try {
                FileUtil.chmod(parchive.toString(), "+x");
            } catch (InterruptedException e) {
                LOG.warn("Exception in chmod" + e.toString());
            }
            cacheStatus.currentStatus = true;
            cacheStatus.mtime = getTimestamp(conf, cache);
        }
        if (isArchive) {
            if (doSymlink) {
                if (!flink.exists()) FileUtil.symLink(cacheStatus.localLoadPath.toString(), link);
            }
            return cacheStatus.localLoadPath;
        } else {
            if (doSymlink) {
                if (!flink.exists()) FileUtil.symLink(cacheFilePath(cacheStatus.localLoadPath).toString(), link);
            }
            return cacheFilePath(cacheStatus.localLoadPath);
        }
    }

    private static boolean isTarFile(String filename) {
        return (filename.endsWith(".tgz") || filename.endsWith(".tar.gz") || filename.endsWith(".tar"));
    }

    private static boolean ifExistsAndFresh(Configuration conf, FileSystem fs, URI cache, long confFileStamp, CacheStatus lcacheStatus, FileStatus fileStatus) throws IOException {
        if (lcacheStatus.currentStatus == false) {
            return false;
        } else {
            long dfsFileStamp;
            if (fileStatus != null) {
                dfsFileStamp = fileStatus.getModificationTime();
            } else {
                dfsFileStamp = getTimestamp(conf, cache);
            }
            if (dfsFileStamp != confFileStamp) {
                LOG.fatal("File: " + cache + " has changed on HDFS since job started");
                throw new IOException("File: " + cache + " has changed on HDFS since job started");
            }
            if (dfsFileStamp != lcacheStatus.mtime) {
                return false;
            }
        }
        return true;
    }

    /**
   * Returns mtime of a given cache file on hdfs.
   * @param conf configuration
   * @param cache cache file 
   * @return mtime of a given cache file on hdfs
   * @throws IOException
   */
    public static long getTimestamp(Configuration conf, URI cache) throws IOException {
        FileSystem fileSystem = FileSystem.get(cache, conf);
        Path filePath = new Path(cache.getPath());
        return fileSystem.getFileStatus(filePath).getModificationTime();
    }

    /**
   * This method create symlinks for all files in a given dir in another directory
   * @param conf the configuration
   * @param jobCacheDir the target directory for creating symlinks
   * @param workDir the directory in which the symlinks are created
   * @throws IOException
   */
    public static void createAllSymlink(Configuration conf, File jobCacheDir, File workDir) throws IOException {
        if ((jobCacheDir == null || !jobCacheDir.isDirectory()) || workDir == null || (!workDir.isDirectory())) {
            return;
        }
        boolean createSymlink = getSymlink(conf);
        if (createSymlink) {
            File[] list = jobCacheDir.listFiles();
            for (int i = 0; i < list.length; i++) {
                FileUtil.symLink(list[i].getAbsolutePath(), new File(workDir, list[i].getName()).toString());
            }
        }
    }

    private static String getFileSysName(URI url) {
        String fsname = url.getScheme();
        if ("hdfs".equals(fsname)) {
            String host = url.getHost();
            int port = url.getPort();
            return (port == (-1)) ? host : (host + ":" + port);
        } else {
            return null;
        }
    }

    private static FileSystem getFileSystem(URI cache, Configuration conf) throws IOException {
        String fileSysName = getFileSysName(cache);
        if (fileSysName != null) return FileSystem.getNamed(fileSysName, conf); else return FileSystem.get(conf);
    }

    /**
   * Set the configuration with the given set of archives
   * @param archives The list of archives that need to be localized
   * @param conf Configuration which will be changed
   */
    public static void setCacheArchives(URI[] archives, Configuration conf) {
        String sarchives = StringUtils.uriToString(archives);
        conf.set("mapred.cache.archives", sarchives);
    }

    /**
   * Set the configuration with the given set of files
   * @param files The list of files that need to be localized
   * @param conf Configuration which will be changed
   */
    public static void setCacheFiles(URI[] files, Configuration conf) {
        String sfiles = StringUtils.uriToString(files);
        conf.set("mapred.cache.files", sfiles);
    }

    /**
   * Get cache archives set in the Configuration
   * @param conf The configuration which contains the archives
   * @return A URI array of the caches set in the Configuration
   * @throws IOException
   */
    public static URI[] getCacheArchives(Configuration conf) throws IOException {
        return StringUtils.stringToURI(conf.getStrings("mapred.cache.archives"));
    }

    /**
   * Get cache files set in the Configuration
   * @param conf The configuration which contains the files
   * @return A URI array of the files set in the Configuration
   * @throws IOException
   */
    public static URI[] getCacheFiles(Configuration conf) throws IOException {
        return StringUtils.stringToURI(conf.getStrings("mapred.cache.files"));
    }

    /**
   * Return the path array of the localized caches
   * @param conf Configuration that contains the localized archives
   * @return A path array of localized caches
   * @throws IOException
   */
    public static Path[] getLocalCacheArchives(Configuration conf) throws IOException {
        return StringUtils.stringToPath(conf.getStrings("mapred.cache.localArchives"));
    }

    /**
   * Return the path array of the localized files
   * @param conf Configuration that contains the localized files
   * @return A path array of localized files
   * @throws IOException
   */
    public static Path[] getLocalCacheFiles(Configuration conf) throws IOException {
        return StringUtils.stringToPath(conf.getStrings("mapred.cache.localFiles"));
    }

    /**
   * Get the timestamps of the archives
   * @param conf The configuration which stored the timestamps
   * @return a string array of timestamps 
   * @throws IOException
   */
    public static String[] getArchiveTimestamps(Configuration conf) {
        return conf.getStrings("mapred.cache.archives.timestamps");
    }

    /**
   * Get the timestamps of the files
   * @param conf The configuration which stored the timestamps
   * @return a string array of timestamps 
   * @throws IOException
   */
    public static String[] getFileTimestamps(Configuration conf) {
        return conf.getStrings("mapred.cache.files.timestamps");
    }

    /**
   * This is to check the timestamp of the archives to be localized
   * @param conf Configuration which stores the timestamp's
   * @param timestamps comma separated list of timestamps of archives.
   * The order should be the same as the order in which the archives are added.
   */
    public static void setArchiveTimestamps(Configuration conf, String timestamps) {
        conf.set("mapred.cache.archives.timestamps", timestamps);
    }

    /**
   * This is to check the timestamp of the files to be localized
   * @param conf Configuration which stores the timestamp's
   * @param timestamps comma separated list of timestamps of files.
   * The order should be the same as the order in which the files are added.
   */
    public static void setFileTimestamps(Configuration conf, String timestamps) {
        conf.set("mapred.cache.files.timestamps", timestamps);
    }

    /**
   * Set the conf to contain the location for localized archives 
   * @param conf The conf to modify to contain the localized caches
   * @param str a comma separated list of local archives
   */
    public static void setLocalArchives(Configuration conf, String str) {
        conf.set("mapred.cache.localArchives", str);
    }

    /**
   * Set the conf to contain the location for localized files 
   * @param conf The conf to modify to contain the localized caches
   * @param str a comma separated list of local files
   */
    public static void setLocalFiles(Configuration conf, String str) {
        conf.set("mapred.cache.localFiles", str);
    }

    /**
   * Add a archives to be localized to the conf
   * @param uri The uri of the cache to be localized
   * @param conf Configuration to add the cache to
   */
    public static void addCacheArchive(URI uri, Configuration conf) {
        String archives = conf.get("mapred.cache.archives");
        conf.set("mapred.cache.archives", archives == null ? uri.toString() : archives + "," + uri.toString());
    }

    /**
   * Add a file to be localized to the conf
   * @param uri The uri of the cache to be localized
   * @param conf Configuration to add the cache to
   */
    public static void addCacheFile(URI uri, Configuration conf) {
        String files = conf.get("mapred.cache.files");
        conf.set("mapred.cache.files", files == null ? uri.toString() : files + "," + uri.toString());
    }

    /**
   * Add an file path to the current set of classpath entries It adds the file
   * to cache as well.
   * 
   * @param file Path of the file to be added
   * @param conf Configuration that contains the classpath setting
   */
    public static void addFileToClassPath(Path file, Configuration conf) throws IOException {
        String classpath = conf.get("mapred.job.classpath.files");
        conf.set("mapred.job.classpath.files", classpath == null ? file.toString() : classpath + System.getProperty("path.separator") + file.toString());
        FileSystem fs = FileSystem.get(conf);
        URI uri = fs.makeQualified(file).toUri();
        addCacheFile(uri, conf);
    }

    /**
   * Get the file entries in classpath as an array of Path
   * 
   * @param conf Configuration that contains the classpath setting
   */
    public static Path[] getFileClassPaths(Configuration conf) {
        String classpath = conf.get("mapred.job.classpath.files");
        if (classpath == null) return null;
        ArrayList list = Collections.list(new StringTokenizer(classpath, System.getProperty("path.separator")));
        Path[] paths = new Path[list.size()];
        for (int i = 0; i < list.size(); i++) {
            paths[i] = new Path((String) list.get(i));
        }
        return paths;
    }

    /**
   * Add an archive path to the current set of classpath entries. It adds the
   * archive to cache as well.
   * 
   * @param archive Path of the archive to be added
   * @param conf Configuration that contains the classpath setting
   */
    public static void addArchiveToClassPath(Path archive, Configuration conf) throws IOException {
        String classpath = conf.get("mapred.job.classpath.archives");
        conf.set("mapred.job.classpath.archives", classpath == null ? archive.toString() : classpath + System.getProperty("path.separator") + archive.toString());
        FileSystem fs = FileSystem.get(conf);
        URI uri = fs.makeQualified(archive).toUri();
        addCacheArchive(uri, conf);
    }

    /**
   * Get the archive entries in classpath as an array of Path
   * 
   * @param conf Configuration that contains the classpath setting
   */
    public static Path[] getArchiveClassPaths(Configuration conf) {
        String classpath = conf.get("mapred.job.classpath.archives");
        if (classpath == null) return null;
        ArrayList list = Collections.list(new StringTokenizer(classpath, System.getProperty("path.separator")));
        Path[] paths = new Path[list.size()];
        for (int i = 0; i < list.size(); i++) {
            paths[i] = new Path((String) list.get(i));
        }
        return paths;
    }

    /**
   * This method allows you to create symlinks in the current working directory
   * of the task to all the cache files/archives
   * @param conf the jobconf 
   */
    public static void createSymlink(Configuration conf) {
        conf.set("mapred.create.symlink", "yes");
    }

    /**
   * This method checks to see if symlinks are to be create for the 
   * localized cache files in the current working directory 
   * @param conf the jobconf
   * @return true if symlinks are to be created- else return false
   */
    public static boolean getSymlink(Configuration conf) {
        String result = conf.get("mapred.create.symlink");
        if ("yes".equals(result)) {
            return true;
        }
        return false;
    }

    /**
   * This method checks if there is a conflict in the fragment names 
   * of the uris. Also makes sure that each uri has a fragment. It 
   * is only to be called if you want to create symlinks for 
   * the various archives and files.
   * @param uriFiles The uri array of urifiles
   * @param uriArchives the uri array of uri archives
   */
    public static boolean checkURIs(URI[] uriFiles, URI[] uriArchives) {
        if ((uriFiles == null) && (uriArchives == null)) {
            return true;
        }
        if (uriFiles != null) {
            for (int i = 0; i < uriFiles.length; i++) {
                String frag1 = uriFiles[i].getFragment();
                if (frag1 == null) return false;
                for (int j = i + 1; j < uriFiles.length; j++) {
                    String frag2 = uriFiles[j].getFragment();
                    if (frag2 == null) return false;
                    if (frag1.equalsIgnoreCase(frag2)) return false;
                }
                if (uriArchives != null) {
                    for (int j = 0; j < uriArchives.length; j++) {
                        String frag2 = uriArchives[j].getFragment();
                        if (frag2 == null) {
                            return false;
                        }
                        if (frag1.equalsIgnoreCase(frag2)) return false;
                        for (int k = j + 1; k < uriArchives.length; k++) {
                            String frag3 = uriArchives[k].getFragment();
                            if (frag3 == null) return false;
                            if (frag2.equalsIgnoreCase(frag3)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static class CacheStatus {

        boolean currentStatus;

        Path localLoadPath;

        int refcount;

        long mtime;

        public CacheStatus(Path localLoadPath) {
            super();
            this.currentStatus = false;
            this.localLoadPath = localLoadPath;
            this.refcount = 0;
            this.mtime = -1;
        }
    }

    /**
   * Clear the entire contents of the cache and delete the backing files. This
   * should only be used when the server is reinitializing, because the users
   * are going to lose their files.
   */
    public static void purgeCache(Configuration conf) throws IOException {
        synchronized (cachedArchives) {
            FileSystem localFs = FileSystem.getLocal(conf);
            for (Map.Entry<String, CacheStatus> f : cachedArchives.entrySet()) {
                try {
                    localFs.delete(f.getValue().localLoadPath, true);
                } catch (IOException ie) {
                    LOG.debug("Error cleaning up cache", ie);
                }
            }
            cachedArchives.clear();
        }
    }
}
