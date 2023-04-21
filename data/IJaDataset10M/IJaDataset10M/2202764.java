package se.inera.ifv.medcert.domain;

import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Pär Wenåker
 *
 */
public class StaticUrlGenerator {

    public static String generate(String uri) throws IOException {
        String baseUri = uri.substring(0, uri.lastIndexOf('/'));
        String file = uri.substring(uri.lastIndexOf('/'));
        ClassPathResource cpr = new ClassPathResource("/filter.properties");
        Properties props = new Properties();
        props.load(cpr.getInputStream());
        return baseUri + "/CAFE" + props.getProperty("build.time") + file;
    }
}
