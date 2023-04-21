package org.apache.tools.ant.taskdefs.optional.dependencies;

import org.apache.tools.ant.Project;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import java.util.Arrays;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

/**
 * Created by IntelliJ IDEA.
 * User: rgold
 * Date: May 8, 2006
 * Time: 3:35:39 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MavenRepositoryBase extends HttpRepository {

    public MavenRepositoryBase(URL repositoryUrl) {
        super(repositoryUrl);
    }

    protected boolean isValidFile(Dependencies task, File localFile, String group, String type, String artifact, String version) {
        try {
            URL md5File = new URL(getUrl(), getRelativeDependencyPath(group, type, artifact, version) + ".md5");
            InputStream is = task.read(md5File);
            if (is == null) {
                task.log("No digest found at " + md5File.toExternalForm() + "; accepting file without validation", Project.MSG_WARN);
                return true;
            } else {
                task.log("Checking MD5 digest: " + md5File.toExternalForm(), Project.MSG_VERBOSE);
                byte[] expectedDigest = readExpectedDigest(is);
                byte[] actualDigest = getMd5Digest(localFile);
                task.log("Actual digest = " + toHexString(actualDigest) + "; expected digest = " + toHexString(expectedDigest), Project.MSG_VERBOSE);
                if (Arrays.equals(expectedDigest, actualDigest)) return true;
                task.log("MD5 validation error while loading " + getDependencyName(artifact, version, type) + " from " + getUrl(), Project.MSG_ERR);
                return false;
            }
        } catch (IOException e) {
            task.log("Error reading digest: " + e.toString() + ". Accepting download of " + getDependencyName(artifact, version, type) + " without validation", Project.MSG_WARN);
            return true;
        } catch (NoSuchAlgorithmException e) {
            task.log("MD5 algorithm not supported by this JVM. Accepting download of " + getDependencyName(artifact, version, type) + " without validation", Project.MSG_WARN);
            return true;
        }
    }

    private static byte[] readExpectedDigest(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String digestText = br.readLine();
        br.close();
        int size = 0;
        while (size < digestText.length() && Character.isLetterOrDigit(digestText.charAt(size))) size++;
        digestText = digestText.substring(0, size);
        return asByteArray(digestText);
    }

    private static byte[] asByteArray(String s) {
        byte[] result = new byte[s.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return result;
    }

    private static byte[] getMd5Digest(File localFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("md5");
        InputStream fileStream = new FileInputStream(localFile);
        int count = 0;
        byte[] buffer = new byte[2048];
        while (count >= 0) {
            md.update(buffer, 0, count);
            count = fileStream.read(buffer);
        }
        fileStream.close();
        return md.digest();
    }

    private static String toHexString(byte[] digest) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            int b = digest[i];
            if (b < 0) b = 0xff & (b + 256);
            if (b < 16) sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    protected String getDependencyName(String artifact, String version, String type) {
        return artifact + '-' + version + "." + type;
    }

    public void getDependency(Dependencies task, File targetFile, String group, String type, String artifact, String version) throws MalformedURLException {
        if (targetFile.exists()) {
            task.log("Found " + targetFile.getName() + " in local repository. Will not download it.", Project.MSG_VERBOSE);
        } else {
            super.getDependency(task, targetFile, group, type, artifact, version);
        }
    }
}
