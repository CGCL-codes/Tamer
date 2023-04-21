package home.projects.recipes.chapthree;

import java.util.List;
import java.util.Map;

public class SequenceGeneratorImpl implements ISequenceGenerator {

    private PrefixGenerator prefixGenerator;

    private String prefix;

    private String suffix;

    private int step;

    private int start;

    private int current;

    private List<String> list;

    private Map<String, String> map;

    public SequenceGeneratorImpl() {
        this("", "", 0, 1);
    }

    public SequenceGeneratorImpl(int start) {
        this("", "", start, 1);
    }

    public SequenceGeneratorImpl(String prefix, String suffix, int start, int step) {
        setPrefix(prefix);
        setSuffix(suffix);
        setStep(step);
        this.start = start;
        this.current = start;
    }

    public String next() {
        synchronized (this) {
            StringBuilder buf = new StringBuilder();
            buf.append(prefixGenerator.getPrefix()).append('-').append(current + step).append('-').append(suffix);
            current += step;
            return buf.toString();
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Mandatory
    public void setPrefixGenerator(PrefixGenerator prefixGenerator) {
        this.prefixGenerator = prefixGenerator;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
