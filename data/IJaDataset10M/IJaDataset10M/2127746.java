package uima.producers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * @author pon3
 * Reads TREC zip files and creates newsitem and parsedcontent annotations
 */
public class TRECReader extends CollectionReader_ImplBase {

    protected ArrayList mFiles;

    protected int current = 0;

    protected int currentZipFile = 0;

    public static String INPUT_DIRECTORY = "inputDirectory";

    public int totalFiles;

    /**
	 * Stores the list of files contained in a directory in 
	 * the list passed in
	 * @param d the name of the directory
	 * @param mFiles list of files to add
	 * @return list of files
	 */
    private ArrayList getFiles(File d, ArrayList mFiles) {
        File[] files = d.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) mFiles.add(files[i]); else mFiles = getFiles(files[i], mFiles);
        }
        return mFiles;
    }

    public void initialize() throws ResourceInitializationException {
        totalFiles = 0;
        File directory = new File((String) getConfigParameterValue(INPUT_DIRECTORY));
        mFiles = new ArrayList();
        mFiles = getFiles(directory, mFiles);
        for (Iterator itr = mFiles.iterator(); itr.hasNext(); ) {
            File file = (File) itr.next();
            try {
                ZipFile zipFile = new ZipFile(file);
                totalFiles += zipFile.size();
                zipFile.close();
            } catch (ZipException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected ZipInputStream in = null;

    public void getNext(CAS arg0) throws IOException, CollectionException {
        try {
            JCas jcas;
            jcas = arg0.getJCas();
            if (in == null) {
                File file = (File) mFiles.get(currentZipFile);
                currentZipFile++;
                in = new ZipInputStream(new FileInputStream(file));
            }
            if (in.getNextEntry() == null) {
                File file = (File) mFiles.get(currentZipFile);
                currentZipFile++;
                in = new ZipInputStream(new FileInputStream(file));
                in.getNextEntry();
            }
            byte b[] = new byte[1024];
            int numRead = in.read(b);
            String content = new String(b, 0, numRead);
            while (numRead != -1) {
                numRead = in.read(b);
                if (numRead != -1) {
                    String newContent = new String(b, 0, numRead);
                    content += newContent;
                }
            }
            jcas.setDocumentText(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CASRuntimeException e) {
            e.printStackTrace();
        } catch (CASException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        current++;
    }

    public boolean hasNext() throws IOException, CollectionException {
        return current < totalFiles;
    }

    public Progress[] getProgress() {
        return new Progress[] { new ProgressImpl(current, totalFiles, Progress.ENTITIES) };
    }

    public void close() throws IOException {
        return;
    }
}
