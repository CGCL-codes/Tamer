package net.sf.sql2java.generator;

import java.util.Properties;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import net.sf.sql2java.common.configuration.ConfigurationHelper;
import net.sf.sql2java.common.configuration.Configurer;
import net.sf.sql2java.generator.merger.AbstractMerger;
import net.sf.sql2java.generator.template.pack.TemplatePackConfigurer;
import net.sf.sql2java.generator.writer.CodeWriter;
import net.sf.sql2java.utils.ClassUtils;

public class GeneratorConfigurer extends Configurer<Generator> {

    protected final Logger LOG = Logger.getLogger(GeneratorConfigurer.class);

    private TemplatePackConfigurer packageConfigurer;

    public GeneratorConfigurer() {
        packageConfigurer = new TemplatePackConfigurer();
    }

    @Override
    public void configure(Generator subject) throws ConfigurationException {
        if (getConfiguration() == null) throw new ConfigurationException("No configuration has been set");
        packageConfigurer.configure(subject.getPackStore());
        CodeWriter writer = createWriter();
        subject.setWriter(writer);
        AbstractMerger merger = createMerger();
        subject.setMerger(merger);
    }

    protected CodeWriter createWriter() throws ConfigurationException {
        String classname = getConfiguration().getString("generator.writer[@class]");
        try {
            Object obj = ClassUtils.executeConstructor(classname);
            if (!(obj instanceof CodeWriter)) throw new ConfigurationException("Invalid writer class, It's not a CodeWriter");
            CodeWriter writer = (CodeWriter) obj;
            Properties properties = ConfigurationHelper.readProperties(getConfiguration().subset("generator.writer.properties"));
            writer.setProperties(properties);
            return writer;
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    protected AbstractMerger createMerger() throws ConfigurationException {
        String classname = getConfiguration().getString("generator.merger[@class]");
        try {
            Object obj = ClassUtils.executeConstructor(classname);
            if (!(obj instanceof AbstractMerger)) throw new ConfigurationException("Invalid merger class, It's not an AbstractMerger");
            AbstractMerger result = (AbstractMerger) obj;
            Properties properties = ConfigurationHelper.readProperties(getConfiguration().subset("generator.merger.properties"));
            result.setProperties(properties);
            return result;
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        super.setConfiguration(configuration);
        packageConfigurer.setConfiguration(configuration);
    }
}
