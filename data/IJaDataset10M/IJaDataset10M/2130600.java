package com.mapbased.sfw.util;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import com.mapbased.sfw.common.Config;
import com.mapbased.sfw.util.logging.ESLogger;
import com.mapbased.sfw.util.logging.Loggers;

public class HdfsUtil {

    private static ESLogger log = Loggers.getLogger(HdfsUtil.class);

    public static String localIP = "127.0.0.1";

    static {
        try {
            localIP = NetworkUtils.getFirstNonLoopbackAddress(NetworkUtils.StackType.IPv4).getHostAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static volatile FileSystem fs;

    private static FileSystem localfs;

    private static Configuration conf = new Configuration();

    static {
        String key = "fs.default.name";
        String value = Config.get().get("hdfs.root", "hdfs://hadoop8:9000");
        if (conf.get(key) == null || conf.get(key).startsWith("file")) {
            log.debug("hdfs root from class:{}==>{}", conf.get(key), value);
            conf.set(key, value);
        }
        conf.setBoolean("fs.file.impl.disable.cache", true);
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);
        try {
            localfs = FileSystem.getLocal(conf);
        } catch (IOException ex) {
            System.exit(-1);
        }
    }

    /**
	 * Must lazy create because it have a sutdown hook
	 * 
	 * @return
	 */
    public static FileSystem getFS() {
        if (fs == null) {
            synchronized (HdfsUtil.class) {
                if (fs != null) {
                    return fs;
                }
                try {
                    fs = FileSystem.get(conf);
                } catch (IOException e) {
                    log.error("Error while creating hdfs FileSystem", e);
                }
            }
        }
        return fs;
    }

    public static void copyLocalFile2Hdfs(boolean delSrc, boolean overwrite, String localPath, String hdfsPath) throws IOException {
        FileUtil.copy(localfs, new Path(localPath), getFS(), new Path(hdfsPath), delSrc, overwrite, conf);
    }

    public static void copyHdfsFile2Local(boolean delSrc, String hdfsPath, String localPath) throws IOException {
        Path src = new Path(hdfsPath);
        File dest = new File(localPath);
        if (getFS().isFile(src) && dest.isDirectory()) {
            dest = new File(dest, src.getName());
        }
        FileUtil.copy(getFS(), src, dest, delSrc, conf);
    }

    public static String getMsDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());
    }

    /**
	 * used for rebuild with mapreduce write ip of this machine on which this
	 * map task running on to hdfs so that job client can clean the work dir
	 * after the whole job finished
	 * 
	 * @param hdfsPath
	 * @throws IOException
	 */
    public static void writeHostIP2Hdfs(String hdfsDir) throws IOException {
        Path hdfsPath = new Path(hdfsDir + "/" + localIP);
        if (getFS().exists(hdfsPath)) {
            return;
        }
        int tryCount = 0;
        IOException e = null;
        while (tryCount++ < 3) {
            try {
                getFS().create(hdfsPath);
                return;
            } catch (IOException e1) {
                e = e1;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        throw e;
    }
}
