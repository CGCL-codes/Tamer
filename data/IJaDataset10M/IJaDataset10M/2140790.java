package nl.multimedian.eculture.annocultor.core.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.multimedian.eculture.annocultor.xconverter.api.DataObject;
import nl.multimedian.eculture.annocultor.xconverter.api.Graph;
import nl.multimedian.eculture.annocultor.xconverter.api.LiteralValue;
import nl.multimedian.eculture.annocultor.xconverter.api.Property;
import nl.multimedian.eculture.annocultor.xconverter.api.Triple;

/**
 * Rule to create an RDF literal property with the value specified by a regular
 * expression.
 * 
 * @author Borys Omelayenko
 * 
 */
public class ExtractLiteralValueByPatternRule extends RenameLiteralPropertyRule {

    private Pattern pattern;

    private int group;

    /**
	 * Uses a regular expression to pull a part of a value and to store it in a
	 * separate property.
	 * 
	 * @param group
	 *          group number in the pattern that is stored, e.g. 1, ...
	 */
    public ExtractLiteralValueByPatternRule(Property trgProperty, String pattern, String trgLang, int group, Graph trgGraph) {
        super(trgProperty, trgLang, trgGraph);
        this.pattern = Pattern.compile(pattern);
        this.group = group;
    }

    /**
	 * @inheritDoc
	 */
    @Override
    public void fire(Triple triple, DataObject converter) throws Exception {
        Matcher matcher = pattern.matcher(triple.getValue().getValue());
        if (matcher.find()) {
            super.fire(triple.changeValue(new LiteralValue(matcher.group(group))), converter);
        }
    }
}
