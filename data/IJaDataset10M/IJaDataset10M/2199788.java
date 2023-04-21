package org.webcastellum.definition.container;

import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.webcastellum.WordDictionary;
import org.webcastellum.definition.SimpleDefinition;
import org.webcastellum.definition.TotalExcludeDefinition;
import org.webcastellum.exception.IllegalRuleDefinitionFormatException;
import org.webcastellum.rules.loader.RuleFileLoader;

public final class TotalExcludeDefinitionContainer extends SimpleDefinitionContainer {

    /**
	 * Serial Version UID
	 */
    private static final long serialVersionUID = 1L;

    public TotalExcludeDefinitionContainer(final RuleFileLoader ruleFileLoader) {
        super(ruleFileLoader);
    }

    protected SimpleDefinition doCreateSimpleDefinition(final boolean enabled, final String name, final String description, final WordDictionary servletPathOrRequestURIPrefilter, final Pattern servletPathOrRequestURIPattern) {
        return new TotalExcludeDefinition(enabled, name, description, servletPathOrRequestURIPrefilter, servletPathOrRequestURIPattern);
    }

    protected void doParseSimpleDefinitionDetailsAndRemoveKeys(final SimpleDefinition definition, final Properties properties) throws PatternSyntaxException, IllegalRuleDefinitionFormatException {
    }

    public final boolean isTotalExclude(final String servletPath, final String requestURI) {
        return getMatchingSimpleDefinition(servletPath, requestURI) != null;
    }
}
