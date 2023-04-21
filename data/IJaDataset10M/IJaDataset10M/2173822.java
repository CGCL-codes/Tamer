package uk.co.cocking.getinline2.pipeline.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import uk.co.cocking.getinline2.exceptions.EnvironmentException;
import uk.co.cocking.getinline2.pipeline.transformers.AbstractTransformer;

public class FileLineWriter extends AbstractTransformer<String, String> {

    private final String fileName;

    private LineWriter lineWriter;

    public FileLineWriter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> transform(String line) {
        lineWriter.transform(line);
        return enlist();
    }

    @Override
    public List<String> open() throws EnvironmentException {
        System.out.println("opening file");
        try {
            lineWriter = new LineWriter(new FileWriter(new File(fileName)));
        } catch (IOException e) {
            throw new EnvironmentException("could not open file " + fileName, e);
        }
        return enlist();
    }

    @Override
    public List<String> close() throws EnvironmentException {
        System.out.println("closing file");
        lineWriter.close();
        return enlist();
    }
}
