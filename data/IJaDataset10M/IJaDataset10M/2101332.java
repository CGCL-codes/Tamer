package com.fastaop.aspectloader.discovery;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import com.fastaop.aspectloader.Filetype;
import com.fastaop.util.FastAopUtil;

/**
 * JARScanner.
 */
public class JARScanner implements IScanner {

    private final File jarFile;

    private final Filetype type;

    /**
	 * Constructor.
	 * 
	 * @param jarFile
	 *            the jar file
	 * @param type
	 *            the type string like .java
	 */
    public JARScanner(File jarFile, Filetype type) {
        this.jarFile = jarFile;
        this.type = type;
    }

    /**
	 * Scans the content of the jar file and pass the output to the callback (if
	 * the filename is matching one of the fileNames).
	 * 
	 * @param fileNames -
	 *            the list of file names we are interested in
	 */
    public void scan(IScannerCallback callback) {
        this.scan(null, callback);
    }

    /**
	 * Scans the content of the jar file and pass the output to the callback (if
	 * the filename is matching one of the fileNames).
	 * 
	 * @param fileNames -
	 *            the list of file names we are interested in
	 * @param callback
	 *            the call back
	 */
    public void scan(List<String> fileNames, IScannerCallback callback) {
        try {
            final InputStream in = getInputStream(jarFile);
            String innerJarName = getInnerFileNameOrNull();
            this.scanInternal(in, fileNames, callback, innerJarName);
        } catch (IOException e) {
            throw new IllegalArgumentException("The jar file (" + this.jarFile.getAbsolutePath() + ") cant be read", e);
        }
    }

    private String getInnerFileNameOrNull() {
        String innerJarName = null;
        if (hasOuterJarFileName(jarFile.getAbsolutePath())) {
            innerJarName = getInnerJarFileName(jarFile.getAbsolutePath());
        }
        return innerJarName;
    }

    /**
	 * {@inheritDoc}
	 */
    public InputStream scanSingleFile(String fileNameInjar) {
        final InputStream in = getInputStream(jarFile);
        String innerJarName = getInnerFileNameOrNull();
        try {
            return scanSingleFileInternal(in, fileNameInjar, innerJarName);
        } catch (IOException e) {
            throw new IllegalArgumentException("The jar file (" + this.jarFile.getAbsolutePath() + ") cant be read", e);
        }
    }

    private InputStream scanSingleFileInternal(InputStream in, String fileNameInjar, String innerJarName) throws IOException {
        JarInputStream jin = new JarInputStream(in);
        final byte[] buffer = new byte[1024];
        ZipEntry entry = jin.getNextEntry();
        while (entry != null) {
            String fileName = entry.getName();
            if (fileName.endsWith(this.type.getType()) && fileName.equals(fileNameInjar)) {
                InputStream back = createClassInputStream(jin, buffer);
                jin.close();
                return back;
            } else if (innerJarName != null && fileName.endsWith(innerJarName)) {
                return scanSingleFileInternal(createClassInputStream(jin, buffer), fileNameInjar, innerJarName);
            }
            entry = jin.getNextEntry();
        }
        jin.close();
        return null;
    }

    private void scanInternal(InputStream in, List<String> rsfNames, IScannerCallback callback, String innerJarName) throws IOException {
        final Set<String> transformedSet = FastAopUtil.transformClassNamesToFileNames(rsfNames, type);
        JarInputStream jin = new JarInputStream(in);
        final byte[] buffer = new byte[1024];
        ZipEntry entry = jin.getNextEntry();
        while (entry != null) {
            String fileName = entry.getName();
            if ((transformedSet == null && fileName.endsWith(this.type.getType())) || (transformedSet != null && transformedSet.contains(fileName))) {
                callback.scan(createClassInputStream(jin, buffer));
            } else if (innerJarName != null && fileName.endsWith(innerJarName)) {
                scanInternal(createClassInputStream(jin, buffer), rsfNames, callback, null);
            }
            entry = jin.getNextEntry();
        }
        jin.close();
    }

    private InputStream createClassInputStream(JarInputStream jin, final byte[] buffer) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        while ((len = jin.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        jin.closeEntry();
        final InputStream deflatedInput = new ByteArrayInputStream(out.toByteArray());
        return deflatedInput;
    }

    private static InputStream getInputStream(File jarFile) {
        InputStream in = null;
        try {
            if (hasOuterJarFileName(jarFile.getAbsolutePath())) {
                File outerFile = getOuterJarFileName(jarFile.getAbsolutePath());
                if (!outerFile.exists()) {
                    throw new IllegalArgumentException("The jar file (" + outerFile.getAbsolutePath() + ") is not existing");
                }
                in = new FileInputStream(outerFile);
            } else {
                if (!jarFile.exists()) {
                    throw new IllegalArgumentException("The jar file (" + jarFile.getAbsolutePath() + ") is not existing");
                }
                in = new FileInputStream(jarFile);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The jar file (" + jarFile.getAbsolutePath() + ") cant be read");
        }
        return in;
    }

    /**
	 * Identifies the outer file name is a jar file is nested e.g.
	 * <code>AIT_servinv_tool_JUnit_Test.jar!\SC_Prx_Contract.jar</code>, the
	 * outer file name is <code>IT_servinv_tool_JUnit_Test.jar<code>
	 * 
	 * @param jarFile
	 *            the complete file name (outer& inner)
	 * @return the outer file name
	 */
    public static File getOuterJarFileName(String jarFile) {
        if (jarFile.indexOf("!") != -1) {
            int startIndex = jarFile.indexOf("file:");
            if (startIndex != -1) {
                startIndex = startIndex + "file:".length();
            } else {
                startIndex = 0;
            }
            return new File(jarFile.substring(startIndex, jarFile.indexOf("!")));
        } else {
            throw new IllegalArgumentException("No nested jar file name found in (" + jarFile + ")");
        }
    }

    /**
	 * Identifies the inner file name is a jar file is nested e.g.
	 * <code>AIT_servinv_tool_JUnit_Test.jar!\SC_Prx_Contract.jar</code>, the
	 * inner file name is <code>SC_Prx_Contract.jar<code>
	 * 
	 * @param jarFile
	 *            the complete file name (outer& inner)
	 * @return the outer file name
	 */
    public static String getInnerJarFileName(String jarFile) {
        if (jarFile.indexOf("!\\") != -1) {
            String substring = jarFile.substring(jarFile.indexOf("!\\") + "!\\".length(), jarFile.length());
            return substring;
        } else {
            throw new IllegalArgumentException("No nested outer jar file name found in (" + jarFile + ")");
        }
    }

    private static boolean hasOuterJarFileName(String jarFile) {
        return (jarFile.indexOf("!") != -1);
    }
}
