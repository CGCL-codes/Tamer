package info.jonclark.corpus.management.test;

import info.jonclark.corpus.management.documents.InputDocument;
import info.jonclark.corpus.management.documents.OutputDocument;
import info.jonclark.corpus.management.etc.CorpusManException;
import info.jonclark.corpus.management.iterators.interfaces.ParallelCorpusTransformIterator;
import info.jonclark.corpus.management.runs.ParallelCorpusTransformRun;
import info.jonclark.log.LogUtils;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ParallelTransformTest implements ParallelCorpusTransformRun {

    private static final Logger log = LogUtils.getLogger();

    public ParallelTransformTest(Properties props, String runName, String corpusName) {
    }

    public void processCorpus(ParallelCorpusTransformIterator iterator) throws CorpusManException {
        try {
            while (iterator.hasNext()) {
                iterator.next();
                System.out.print(".");
                InputDocument ein = iterator.getInputDocumentE();
                OutputDocument eout = iterator.getOutputDocumentE();
                InputDocument jin = iterator.getInputDocumentF();
                OutputDocument jout = iterator.getOutputDocumentF();
                String line;
                while ((line = ein.readLine()) != null) eout.println(line);
                eout.close();
                while ((line = jin.readLine()) != null) jout.println(line);
                jout.close();
            }
            iterator.finish();
        } catch (IOException e) {
            throw new CorpusManException(e);
        }
    }
}
