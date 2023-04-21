package es.gavab.jmh.instancefile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Patxi Gortázar
 * 
 */
public class InstanceFileBestKnownValueProvider {

    private File valuesFile;

    public InstanceFileBestKnownValueProvider(File values) {
        this.valuesFile = values;
    }

    public Number getBestKnownValue(String id) {
        if (!valuesFile.exists()) {
            throw new Error("File does not exist: " + valuesFile.getAbsolutePath());
        }
        List<String> sections = splitId(id);
        String fileName = sections.get(sections.size() - 1);
        String fileNameWithoutExt = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(valuesFile)));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(id) || line.contains(fileName) || line.contains(fileNameWithoutExt)) {
                    try {
                        return parseBestKnown(line);
                    } catch (NumberFormatException e) {
                        throw new InstancesRepositoryException("Line could not be parsed: " + line, e);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new InstancesRepositoryException("File not found: " + valuesFile, e);
        } catch (IOException e) {
            throw new InstancesRepositoryException(e);
        }
        return null;
    }

    protected Number parseBestKnown(String line) {
        StringTokenizer st = new StringTokenizer(line);
        String instanceName = st.nextToken();
        String bestKnownValue = st.nextToken();
        return Double.parseDouble(bestKnownValue);
    }

    private List<String> splitId(String id) {
        StringTokenizer st = new StringTokenizer(id, InstancesRepository.INSTANCE_ID_PATH_SEPARATOR);
        List<String> parts = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            parts.add(st.nextToken());
        }
        return parts;
    }
}
