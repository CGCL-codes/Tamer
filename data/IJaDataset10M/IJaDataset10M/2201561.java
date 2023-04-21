package net.sf.parser4j.gen4gen.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import net.sf.parser4j.kernelgenerator.service.GeneratorException;
import net.sf.parser4j.parser.service.ParserException;
import org.apache.log4j.Logger;

/**
 * 
 * @author luc peuvrier
 * 
 */
public final class MainGenDefinitiveParserDataFromBaseTxtParserData {

    private static final Logger LOGGER = Logger.getLogger(MainGenDefinitiveParserDataFromBaseTxtParserData.class);

    private final Gen4GenSupport gen4GenSupport = new Gen4GenSupport();

    private MainGenDefinitiveParserDataFromBaseTxtParserData() {
        super();
    }

    public static void main(final String[] args) {
        try {
            final MainGenDefinitiveParserDataFromBaseTxtParserData mainGen4GenDefinitiveParserDataFromBaseTxtParserData = new MainGenDefinitiveParserDataFromBaseTxtParserData();
            mainGen4GenDefinitiveParserDataFromBaseTxtParserData.run();
        } catch (Exception exception) {
            LOGGER.error("error while generate for generator", exception);
        }
    }

    private void run() throws UnsupportedEncodingException, MalformedURLException, GeneratorException, ParserException, IOException {
        gen4GenSupport.generateDefinitiveParserDataFromBaseTxtParserData();
    }
}
