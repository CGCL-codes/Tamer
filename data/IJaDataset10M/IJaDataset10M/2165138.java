package eu.annocultor.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Ignore;
import eu.annocultor.api.Reporter;
import eu.annocultor.data.destinations.AbstractGraph;
import eu.annocultor.triple.Property;
import eu.annocultor.triple.Triple;

@Ignore
public class TestNamedGraph extends AbstractGraph {

    TestNamedGraph() {
        super(null);
    }

    @Override
    public int getVolume() {
        return 1;
    }

    List<Triple> triples = new ArrayList<Triple>();

    public void add(Triple triple) throws Exception {
        triples.add(triple);
    }

    public Triple getLastAddedTriple(int offset) {
        return triples.get(triples.size() - offset);
    }

    public void addNamedGraphAddListener(NamedGraphAddListener addListener) {
    }

    public List<Triple> getTriples() {
        return triples;
    }

    public File getWorkingFile() throws IOException {
        return null;
    }

    public Reporter getReporter() {
        return null;
    }

    public void startRdf() throws Exception {
    }

    public void setFinalDir(File baseDir) {
    }

    public long size() {
        return triples.size();
    }

    public File getFinalFile(int volume) throws IOException {
        return null;
    }

    public String getReport(Property property) throws Exception {
        return null;
    }

    @Override
    public boolean writingHappened() throws Exception {
        return false;
    }

    public void endRdf() throws Exception {
    }

    @Override
    public Set<Property> getProperties() {
        return null;
    }
}
