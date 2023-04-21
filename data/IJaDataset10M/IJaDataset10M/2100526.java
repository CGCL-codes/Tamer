package org.langkiss.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom
 * 
 */
public class FileUtil {

    public String getCharsetSensitiveFileAsString(String file) {
        CharsetDetector det = new CharsetDetector();
        String charset = det.detect(file);
        FileUtil util = new FileUtil();
        Logger.getLogger(this.getClass().getName()).log(Level.FINER, "Reading Cards from file ''{0}''...", file);
        if (charset == null) {
            charset = "UTF8";
        }
        String fileContent = null;
        try {
            fileContent = util.getEncodedFileAsString(new File(file), charset);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, ex);
            return null;
        }
        return fileContent;
    }

    public String getEncodedFileAsString(File file, String charset) throws Exception {
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader defaultReader = new InputStreamReader(fis);
            String defaultEncoding = defaultReader.getEncoding();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "The default encondig for file ''{0}'' is ''{1}''. Using URF8...", new Object[] { file.getName(), defaultEncoding });
            InputStreamReader isr = new InputStreamReader(fis, charset);
            reader = new BufferedReader(isr);
            String s = null;
            while ((s = reader.readLine()) != null) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }
                buffer.append(s);
            }
        } catch (SecurityException e) {
            throw new Exception("Failed to load File '" + file.toString() + "'. Not sufficient Rigths. " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Failed to load File '" + file.toString() + "'. " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ioe) {
                }
            }
        }
        String ret = buffer.toString();
        return ret;
    }

    public void printUTF8File(String file, String content) throws Exception {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF8");
            writer = new PrintWriter(new BufferedWriter(out));
            writer.print(content);
            writer.close();
            writer = null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ioe) {
                }
            }
        }
    }

    /**
     * 
     * @param file absolute path to file
     * @param line
     * @throws Exception 
     */
    public void appendLineToFile(String file, String line) throws Exception {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            writer.println(line);
            writer.close();
            writer = null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ioe) {
                }
            }
        }
    }

    public String getFileAsString(File file) throws Exception {
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = reader.readLine()) != null) {
                buffer.append(s).append("\n");
            }
        } catch (SecurityException e) {
            throw new Exception("Failed to load File '" + file.toString() + "'. \n" + "Not sufficient Rigths.\n" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Failed to load File '" + file.toString() + "'. \n" + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ioe) {
                }
            }
        }
        String ret = buffer.toString();
        if (ret.endsWith("\n")) {
            ret = ret.substring(0, ret.lastIndexOf("\n"));
        }
        return ret;
    }

    /**
     * Gets the content of a file in the classpath as String.
     * @param fileInClasspath The file to read. Example: /com/mycompany/test/myfile
     * @return The file content as String
     */
    public String getRessourceAsString(String fileInClasspath) throws Exception {
        byte[] buffer = new byte[4096];
        BufferedInputStream in = null;
        java.io.ByteArrayOutputStream w = null;
        try {
            in = new BufferedInputStream(this.getClass().getResourceAsStream(fileInClasspath));
            w = new ByteArrayOutputStream();
            int x = 0;
            while ((x = in.read(buffer)) != -1) {
                w.write(buffer, 0, x);
            }
            in.close();
            in = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return w.toString();
    }

    public void printFile(String file, String content) throws Exception {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.print(content);
            writer.close();
            writer = null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ioe) {
                }
            }
        }
    }

    /**
     * Does a binary file copy
     * @param from Absolute path
     * @param to Absolute path
     */
    public void copyFile(String from, String to) throws Exception {
        long byteCount = 0;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new DataInputStream(new FileInputStream(from)));
            out = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(to)));
            byte[] bbuf = new byte[4096];
            int length = -1;
            while ((length = in.read(bbuf)) != -1) {
                out.write(bbuf, 0, length);
                byteCount = byteCount + length;
            }
            in.close();
            in = null;
            out.close();
            out = null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Failed to copy (binary) file from ''{0}'' to ''{1}''.{2}", new Object[] { from, to, e.getMessage() });
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Gets the content of a file as String.
     * The internal implementation uses a character array.
     *
     * @param file The file to read
     * @return The file content as String.
     */
    public String getFileContent(File file) throws Exception {
        StringBuilder buf = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            char[] cbuf = new char[4096];
            while (in.read(cbuf) != -1) {
                buf.append(cbuf);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ioe) {
                }
            }
        }
        char[] cArray = { (char) 0 };
        String emptyChar = new String(cArray);
        String ret = null;
        int index = buf.indexOf(emptyChar);
        if (index != -1) {
            ret = buf.substring(0, index);
        } else {
            ret = buf.toString();
        }
        return ret;
    }
}
