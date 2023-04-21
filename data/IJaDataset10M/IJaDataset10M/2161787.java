package net.sf.cplab.gazer.converters;

import java.io.IOException;
import java.util.List;
import net.sf.cplab.core.ListConverter;
import net.sf.cplab.core.ProgressListener;
import net.sf.cplab.core.util.Configurable;
import net.sf.cplab.gazer.GazeSample;
import net.sf.cplab.gazer.IdfAsciiSample;
import net.sf.cplab.gazer.raw.IdfAscii;

/**
 * @author jtse
 *
 */
public class IdfAsciiSamplesToGazeSamples implements Configurable, ListConverter {

    public class Config {

        public String trialStart = "trial_start";

        public String trialFinish = "trial_finish";
    }

    private Config config;

    public IdfAsciiSamplesToGazeSamples() {
        config = new Config();
    }

    public Object getConfiguration() {
        return config;
    }

    public void setConfiguration(Object config) {
        this.config = (Config) config;
    }

    public List<GazeSample> convert(List<IdfAsciiSample> list, ProgressListener listener) {
        try {
            return IdfAscii.newGazeListUsingIdfAsciiInputStream(list.get(0).getInputStream(), config.trialStart, config.trialFinish);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> getInputClass() {
        return IdfAsciiSample.class;
    }

    public Class<?> getOutputClass() {
        return GazeSample.class;
    }
}
