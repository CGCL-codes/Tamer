package de.fuberlin.wiwiss.r2r;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;

/**
 * This implementation of the Output interface serializes the target dataset in
 * TURTLE format. The mapped output is written immediately.
 * 
 * @author grindcrank
 * 
 */
public class TurtleOutput implements Output {

    private final OutputStream outputStream;

    private final Writer writer;

    public TurtleOutput(String filename) throws IOException {
        super();
        writer = new BufferedWriter(new FileWriter(filename));
        outputStream = null;
    }

    public TurtleOutput(OutputStream output) {
        super();
        writer = null;
        outputStream = output;
    }

    public TurtleOutput(Writer writer) {
        super();
        this.writer = writer;
        outputStream = null;
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
        if (outputStream != null) {
            outputStream.flush();
            writer.close();
        }
    }

    public Model getOutputModel() {
        return ModelFactory.createDefaultModel();
    }

    public void write(Model output) {
        if (writer != null) output.write(writer, FileUtils.langTurtle); else output.write(outputStream, FileUtils.langTurtle);
    }
}
