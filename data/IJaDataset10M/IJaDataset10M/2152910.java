package hudson.zipscript.parser.util.translator;

import hudson.zipscript.parser.Configurable;
import java.util.List;
import java.util.Locale;

public interface Translator extends Configurable {

    public List translate(List elementsOrText, Locale to) throws Exception;

    public String getBaseLocaleKey();
}
