package util.namegen;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: serhiy
 * Created on Oct 16, 2006, 7:12:46 AM
 */
public class TemplateNameGenerator implements INameGenerator {

    protected INameGenerator generator;

    private final Map<String, INameGenerator> generators = new HashMap<String, INameGenerator>();

    private Properties props;

    private final Pattern pattern = Pattern.compile("<(.*?)>");

    @Override
    public void initialize(Properties props, String prefix) throws Exception {
        String generatorName = props.getProperty(prefix + ".Generator");
        if (generatorName != null) {
            generator = FilterGenerator.load(props, generatorName);
            generator.initialize(props, "NameGenerator." + generatorName);
        } else {
            generator = new ListNameGenerator();
            generator.initialize(props, prefix);
        }
        this.props = props;
    }

    @Override
    public void reset() {
        generator.reset();
        for (INameGenerator generator : generators.values()) generator.reset();
    }

    @Override
    public String next() throws Exception {
        Matcher matcher = pattern.matcher(generator.next());
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            INameGenerator generator = getGenerator(key, generators, props);
            String replacement = generator.next();
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static INameGenerator getGenerator(String key, Map<String, INameGenerator> generators, Properties props) throws Exception {
        INameGenerator generator = generators.get(key);
        if (generator == null) {
            generator = FilterGenerator.load(props, key);
            generators.put(key, generator);
            generator.initialize(props, "NameGenerator." + key);
            generator.reset();
        }
        return generator;
    }
}
