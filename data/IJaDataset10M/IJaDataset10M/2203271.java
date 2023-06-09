package opennlp.tools.formats;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import opennlp.tools.cmdline.ArgumentParser;
import opennlp.tools.cmdline.ObjectStreamFactory;
import opennlp.tools.cmdline.TerminateToolException;
import opennlp.tools.cmdline.params.DetokenizerParameter;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.tokenize.DetokenizationDictionary;
import opennlp.tools.tokenize.Detokenizer;
import opennlp.tools.tokenize.DictionaryDetokenizer;
import opennlp.tools.util.ObjectStream;

/**
 * <b>Note:</b> Do not use this class, internal use only!
 */
public class NameToSentenceSampleStreamFactory implements ObjectStreamFactory<SentenceSample> {

    interface Parameters extends NameSampleStreamFactory.Parameters, DetokenizerParameter {
    }

    public String getUsage() {
        return ArgumentParser.createUsage(Parameters.class);
    }

    public boolean validateArguments(String[] args) {
        return ArgumentParser.validateArguments(args, Parameters.class);
    }

    public ObjectStream<SentenceSample> create(String[] args) {
        Parameters params = ArgumentParser.parse(args, Parameters.class);
        ObjectStream<NameSample> nameSampleStream = new NameSampleStreamFactory().create(params);
        Detokenizer detokenizer;
        try {
            detokenizer = new DictionaryDetokenizer(new DetokenizationDictionary(new FileInputStream(new File(params.getDetokenizer()))));
        } catch (IOException e) {
            System.err.println("Error while loading detokenizer dict: " + e.getMessage());
            throw new TerminateToolException(-1);
        }
        return new NameToSentenceSampleStream(detokenizer, nameSampleStream, 30);
    }
}
