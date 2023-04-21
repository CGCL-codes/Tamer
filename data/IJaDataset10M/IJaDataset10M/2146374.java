package edu.ucdavis.genomics.metabolomics.binbase.cluster.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.jcraft.jsch.Session;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.ClusterUtil;
import edu.ucdavis.genomics.metabolomics.exception.ConfigurationException;
import edu.ucdavis.genomics.metabolomics.exception.NotSupportedException;
import edu.ucdavis.genomics.metabolomics.util.io.Copy;
import edu.ucdavis.genomics.metabolomics.util.io.dest.FileDestination;
import edu.ucdavis.genomics.metabolomics.util.io.source.ResourceSource;
import edu.ucdavis.genomics.metabolomics.util.io.source.Source;

/**
 * provides some general usefull methods to make it easier to implement your own
 * clusters
 * 
 * @author wohlgemuth
 */
public abstract class AbstractUtil extends ClusterUtil {

    private Logger logger = Logger.getLogger(getClass());

    /**
	 * we keep a session open to the cluster
	 */
    private Session session;

    private String password;

    private String username;

    private String servername;

    protected AbstractUtil(String servername, String username, String password) {
        this.servername = servername;
        this.username = username;
        this.password = password;
    }

    /**
	 * returns the local path where we can find the arcive used for the
	 * deployment
	 * 
	 * @return
	 * @throws IOException
	 * @throws ConfigurationException
	 */
    protected File getArchive() throws IOException, ConfigurationException {
        logger.info("loading file from archive...");
        ResourceSource source = new ResourceSource("/clusterservice.zip");
        logger.info("checking if it exists at the base...");
        if (source.exist()) {
            logger.info("found in classpath");
            FileDestination dest = new FileDestination();
            File output = File.createTempFile("clusterservice", "zip");
            output.deleteOnExit();
            dest.setIdentifier(output);
            Copy.copy(source.getStream(), dest.getOutputStream());
            return output;
        } else {
            logger.info("archive file not found at base!");
        }
        source = new ResourceSource("./clusterservice.zip");
        logger.info("checking if it exists in the current directoy...");
        if (source.exist()) {
            logger.info("found in classpath");
            FileDestination dest = new FileDestination();
            File output = File.createTempFile("clusterservice", "zip");
            output.deleteOnExit();
            dest.setIdentifier(output);
            Copy.copy(source.getStream(), dest.getOutputStream());
            return output;
        } else {
            logger.info("archive file not found at the current directory!");
        }
        logger.info("looking for local file...");
        File file = new File("target/clusterservice.zip");
        if (file.exists()) {
            logger.info("using local file!");
            return file;
        } else {
            logger.info("target file not found - " + file.getAbsolutePath());
        }
        logger.info("looking for file in current directory");
        file = new File("clusterservice.zip");
        if (file.exists()) {
            logger.info("using local file!");
            return file;
        } else {
            logger.info("target file not found - " + file.getAbsolutePath());
        }
        if (System.getProperty("ant.basedir") != null) {
            logger.info("checking in basedir since installation is called from antfile - " + System.getProperty("ant.basedir"));
            file = new File(System.getProperty("ant.basedir") + File.separator + "clusterservice.zip");
            if (file.exists()) {
                logger.info("using local file!");
                return file;
            } else {
                logger.info("target file not found - " + file.getAbsolutePath());
            }
        }
        throw new FileNotFoundException("sorry could not find the archive in the jar file!");
    }

    @Override
    public String executeCommand(String command) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SSHUtil.executeCommand(getSession(), command, out);
        out.flush();
        String ret = out.toString();
        out.close();
        return ret;
    }

    protected synchronized Session getSession() throws Exception {
        if (session == null) {
            logger.info("create new session");
            session = SSHUtil.createSession(servername, username, password);
        }
        if (session.isConnected() == false) {
            logger.info("reconnect to session");
            session = SSHUtil.createSession(servername, username, password);
        }
        return session;
    }

    @Override
    protected void finalize() throws Throwable {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
        super.finalize();
    }

    @Override
    public List<JobInformation> getQueueCurrentUser() throws Exception {
        return getQueueByUser(this.username);
    }

    @Override
    public String killJobOfCurrentUser() throws Exception {
        return this.killJobs(this.username);
    }

    @Override
    public List<JobInformation> getPendingQueueByCurrentUser() throws Exception {
        return getPendingQueueByUser(this.username);
    }

    @Override
    public List<ClusterInformation> getClusterInformationCurrentUser() throws Exception {
        return getClusterInformationByUser(this.username);
    }

    /**
	 * returns the dir where all the libraries are
	 * 
	 * @return
	 */
    protected String getClusterLibDir() {
        return "cluster/ClusterService/lib/";
    }

    /**
	 * returns all libraries registered at the cluster
	 * 
	 * @author wohlgemuth
	 * @version Jul 27, 2006
	 * @return
	 */
    public String[] getRegisteredLibraries() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SSHUtil.executeCommand(getSession(), "ls " + getClusterLibDir(), out);
        SSHUtil.executeCommand(getSession(), "ls " + getClusterRuntimeDir(), out);
        out.flush();
        String[] libs = out.toString().split("\n");
        out.close();
        return libs;
    }

    @Override
    public void setClusterProperties(Properties p) throws Exception {
        Source source = SSHUtil.scpFrom(getSession(), getClusterConfigDir() + "configuration-service.xml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(source.getStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.err.println(line);
        }
    }

    @Override
    public void storeDataOnCluster(String filePathOnCluster, Source source) throws Exception {
        SSHUtil.scpToFile(source, getSession(), filePathOnCluster);
    }

    @Override
    public void destroy() throws Exception {
        getSession().disconnect();
    }

    @Override
    public Source downloadDataFromCluster(String fileOnCluster) throws Exception {
        throw new NotSupportedException();
    }

    protected String getClusterConfigDir() {
        return "cluster/ClusterService/config/";
    }

    @Override
    public void unregisterConfiguration(String name) throws Exception {
        name = name.replaceAll("\\\\", "/");
        String temp[] = name.split("/");
        name = temp[temp.length - 1];
        name = getClusterRuntimeDir() + name;
        executeCommand("rm -f " + name);
    }

    protected String getClusterRuntimeDir() {
        return "cluster/ClusterService/runtime/";
    }

    public String getPassword() {
        return password;
    }

    public final String initializeCluster(String ip) throws Exception {
        return this.initializeCluster(ip.trim(), this.getArchive());
    }

    /**
	 * does the actual intialisation
	 * 
	 * @param string
	 * @param archive
	 * @return
	 * @throws Exception
	 */
    protected abstract String initializeCluster(String string, File archive) throws Exception;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }
}
