package wyq.infrastructure;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileTool extends WorkSpaceTool {

    /**
     * @author wyq
     * 
     */
    public interface TextLineAccepter {

        public abstract boolean accept(String line);
    }

    public void copyFile(File srcFile, File tarFile) throws IOException {
        BufferedInputStream in = getBufferedInputStream(srcFile);
        BufferedOutputStream out = getBufferedOutputStream(tarFile);
        int buff = -1;
        while ((buff = in.read()) != -1) {
            out.write(buff);
        }
        in.close();
        out.flush();
        out.close();
    }

    public void copyFile(String srcFileName, String tarFileName) throws IOException {
        copyFile(getFileFromWorkspace(srcFileName), getFileFromWorkspace(tarFileName));
    }

    public void copyResFileToWorkspace(Class<?> rootClz, String rscFileName) throws IOException {
        URL res = rootClz.getResource(rscFileName);
        if (res == null) {
            throw new FileNotFoundException();
        }
        copyFile(getFile(res.getFile()), getFileFromWorkspace(rscFileName));
    }

    public void copyResFileToWorkspace(String srcFileName) throws IOException {
        copyResFileToWorkspace(FileTool.class, srcFileName);
    }

    public BufferedReader getBufferedReader(File txtFile) throws FileNotFoundException {
        return new BufferedReader(new FileReader(txtFile));
    }

    public BufferedInputStream getBufferedInputStream(File byteFile) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(byteFile));
    }

    public BufferedWriter getBufferedWriter(File txtFile) throws IOException {
        return new BufferedWriter(new FileWriter(txtFile));
    }

    public BufferedOutputStream getBufferedOutputStream(File byteFile) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(byteFile));
    }

    public void writeTxtFile(String content, String file) throws IOException {
        Writer writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(content);
        bw.flush();
        writer.flush();
        bw.close();
        writer.close();
    }

    public String[] readTxtFileLines(String file) throws IOException {
        return readTxtFileLines(file, new TextLineAccepter() {

            @Override
            public boolean accept(String line) {
                return true;
            }
        });
    }

    public String[] readTxtFileLines(String file, final String... ignorePrefix) throws IOException {
        return readTxtFileLines(file, new TextLineAccepter() {

            @Override
            public boolean accept(String line) {
                if (ignorePrefix == null || ignorePrefix.length == 0) {
                    return true;
                } else {
                    for (String ignPfx : ignorePrefix) {
                        if (line.startsWith(ignPfx)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        });
    }

    public String[] readTxtFileLines(String file, TextLineAccepter accepter) throws IOException {
        List<String> lines = new ArrayList<String>();
        File f = new File(file);
        Reader reader = new FileReader(f);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        while ((line = br.readLine()) != null) {
            if (accepter == null || accepter.accept(line)) {
                lines.add(line);
            }
        }
        br.close();
        reader.close();
        return lines.toArray(new String[0]);
    }

    public void openExplorer(String path) {
        Runtime r = Runtime.getRuntime();
        try {
            r.exec("explorer.exe " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
