package org.expasy.jpl.matching.annotation.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationAnnotatedSpectrum;
import org.expasy.jpl.matching.exception.JPLMSPParseException;
import org.expasy.jpl.utils.builder.JPLBuilder;
import org.expasy.jpl.utils.builder.JPLBuilderException;
import org.expasy.jpl.utils.parser.JPLParseException;
import org.expasy.jpl.utils.parser.JPLFileParser;

/**
 * This class parse files and transmit informations to the builder.
 * The builder then will have to call build() to create the 
 * {@code JPLAnnotatedSpectrum}.
 *  
 * @see JPLAnnotSpectrumBuilder.
 * 
 * @author nikitin
 *
 */
public abstract class JPLAbstractAnnotatedMSnFileParser implements JPLFileParser, JPLBuilder<List<JPLFragmentationAnnotatedSpectrum>> {

    static Log logger = LogFactory.getLog(JPLMSPFileParser.class);

    protected JPLAbstractAnnotatedMSnEntryParser parser;

    protected static String ENTRY_DELIMITOR = "(\n\\s*\n)+";

    protected List<JPLFragmentationAnnotatedSpectrum> spectra;

    protected boolean isFetchAnnotation = true;

    /**
	 * Default constructor
	 */
    protected JPLAbstractAnnotatedMSnFileParser(boolean isFetchAnnotation) {
        this.isFetchAnnotation = isFetchAnnotation;
    }

    public abstract JPLAbstractAnnotatedMSnEntryParser createEntryParserAnnotEnabled();

    public abstract JPLAbstractAnnotatedMSnEntryParser createEntryParserAnnotDisabled();

    public void parse(String filename) throws JPLParseException, IOException {
        parse(new File(filename));
    }

    /**
	 * This method extract informations from file and transmit them
	 * to the builder.
	 * 
	 * @param file the file to parse
	 * @throws JPLMSPParseException 
	 * @throws IOException 
	 */
    public void parse(File file) throws JPLParseException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int offset = 0;
        int i = 0;
        if (logger.isDebugEnabled()) {
            logger.debug("Total memory : " + Runtime.getRuntime().maxMemory());
        }
        Scanner scanner = new Scanner(br);
        scanner.useDelimiter(ENTRY_DELIMITOR);
        spectra = new ArrayList<JPLFragmentationAnnotatedSpectrum>();
        if (isFetchAnnotation) {
            parser = createEntryParserAnnotEnabled();
        } else {
            parser = createEntryParserAnnotDisabled();
        }
        while (scanner.hasNext()) {
            String currentEntry = scanner.next();
            spectra.add(parseAndBuildSpectrum(currentEntry));
            if (logger.isDebugEnabled()) {
                if (++i % 1000 == 0) {
                    logger.debug(i + " : free memory = " + Runtime.getRuntime().freeMemory());
                }
            }
            offset += currentEntry.length();
        }
        br.close();
        if (logger.isDebugEnabled()) {
            logger.debug("close file.");
        }
    }

    private JPLFragmentationAnnotatedSpectrum parseAndBuildSpectrum(String block) throws JPLParseException {
        parser.parse(block);
        return parser.build();
    }

    public List<JPLFragmentationAnnotatedSpectrum> build() throws JPLBuilderException {
        return spectra;
    }
}
