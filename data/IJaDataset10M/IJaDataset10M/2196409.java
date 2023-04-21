package de.maramuse.soundcomp.parser;

import de.maramuse.soundcomp.parser.SCParser.ParserVal;
import de.maramuse.soundcomp.process.ProcessElement;

/**
 * @author Jan Schmidt-Reinisch
 * 
 */
public class Envelope extends ParserVal implements TemplateProvider, NamelessSource {

    Envelope(String s) {
        super(SCParser.GENVELOPE, s);
    }

    private static final ProcessElement template = new de.maramuse.soundcomp.generator.Envelope();

    @Override
    public ProcessElement getTemplate() {
        return template;
    }
}
