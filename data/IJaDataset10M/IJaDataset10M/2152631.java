package application.example.mgmtp;

import net.sourceforge.nconfigurations.BuilderException;
import net.sourceforge.nconfigurations.Configuration;
import net.sourceforge.nconfigurations.KeyFactory;
import net.sourceforge.nconfigurations.contrib.mgmtp.ComMgmtpConfigConfigurationConverters;
import net.sourceforge.nconfigurations.contrib.mgmtp.MgmTypedPropertiesConfigurationBuilder;
import net.sourceforge.nconfigurations.convert.ConvertException;
import net.sourceforge.nconfigurations.convert.ConvertStrategy;
import net.sourceforge.nconfigurations.convert.Converter;
import net.sourceforge.nconfigurations.convert.Converters;
import net.sourceforge.nconfigurations.util.ClasspathResource;
import net.sourceforge.nconfigurations.util.Resource;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import java.util.Locale;

/**
 * Proof of concept test to show how the mgm configuration builders can be
 * easily extended.
 *
 * <p>This class is intentionaly in a different package than the mgm
 * configuration builders.
 * 
 * @author Petr Novotník
 * @since 1.0
 */
public class CustomSetupTest {

    public static enum Priority {

        HIGH, NORMAL, LOW
    }

    private static class CustomConvertStrategy implements ConvertStrategy {

        private final Converter<Priority> PRIO_CONV = Converters.enumNameConverter(Priority.class);

        public Object toObject(final String key, final String value, final String type) throws ConvertException {
            if (type != null && type.toLowerCase(Locale.ENGLISH).equals("priority")) {
                return PRIO_CONV.stringToValue(value);
            } else {
                return ComMgmtpConfigConfigurationConverters.NEARLY_EXACT.toObject(key, value, type);
            }
        }
    }

    private static class CustomConfigurationBuilder extends MgmTypedPropertiesConfigurationBuilder {

        private static final ConvertStrategy CONV_STRATEGY = new CustomConvertStrategy();

        CustomConfigurationBuilder(final Resource resource) {
            super(resource);
        }

        @Override
        public ConvertStrategy getConvertStrategy() {
            return CONV_STRATEGY;
        }
    }

    @Test
    public void testExpected() throws BuilderException {
        final Resource doc = new ClasspathResource(getClass().getPackage().getName().replace('.', '/') + "/custom-setup-test.tproperties.xml");
        final Configuration cfg = new CustomConfigurationBuilder(doc).buildConfiguration("foo");
        assertEquals(cfg.getValue(KeyFactory.keyFrom("bar.one", String.class)), "foo-bar-one");
        assertEquals(cfg.getValue(KeyFactory.keyFrom("bar.two", Integer.class)), Integer.valueOf(42));
        assertEquals(cfg.getValue(KeyFactory.keyFrom("bar.warn.prio", Priority.class)), Priority.NORMAL);
        assertEquals(cfg.getValue(KeyFactory.keyFrom("bar.error.prio", Priority.class)), Priority.HIGH);
        assertEquals(cfg.getValue(KeyFactory.keyFrom("bar.info.prio", Priority.class)), Priority.LOW);
    }
}
