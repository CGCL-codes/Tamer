package org.nms.spider.helpers.utils;

import java.util.ArrayList;
import java.util.List;
import org.nms.spider.beans.impl.RegexDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates all regex for a multiple prefix regex filtering. The list of
 * prefixes generates a list of definitions with same postfix and remove policy.
 * 
 * <p>
 * The usual function of this generator is for a list of prefix urls, that must
 * be filtered after a urlprocessor.
 * 
 * </p>
 * 
 * @author daviz
 * 
 */
public class RegexDefinitionGenerator {

    /**
	 * The logger.
	 */
    private static final Logger log = LoggerFactory.getLogger(RegexDefinitionGenerator.class);

    /**
	 * The list of prefixes.
	 */
    private List<String> prefixList;

    /**
	 * The postfix.
	 */
    private String postfix;

    /**
	 * The remove prefix flag.
	 */
    private boolean removePrefix;

    /**
	 * The remove postfix flag.
	 */
    private boolean removePostfix;

    /**
	 * The regex.
	 */
    private String regex = "(.*?)";

    /**
	 * Generates the list of RegexDefinitions, using the list of prefixes, and
	 * setting the postfix and remove policies in each definition.
	 * 
	 * @return The list of RegexDefinitions generated.
	 */
    public List<RegexDefinition> generate() {
        List<RegexDefinition> result = new ArrayList<RegexDefinition>();
        for (String prefix : prefixList) {
            log.debug(String.format("Generating REGEXDEFINITION for %s-%s-%s", prefix, regex, postfix));
            RegexDefinition rd = new RegexDefinition();
            rd.setPrefix(prefix);
            rd.setPostfix(postfix);
            rd.setRemovePostfix(removePostfix);
            rd.setRemovePrefix(removePrefix);
            result.add(rd);
        }
        return result;
    }

    public List<String> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postFix) {
        this.postfix = postFix;
    }

    public boolean isRemovePrefix() {
        return removePrefix;
    }

    public void setRemovePrefix(boolean removePrefix) {
        this.removePrefix = removePrefix;
    }

    public boolean isRemovePostfix() {
        return removePostfix;
    }

    public void setRemovePostfix(boolean removePostFix) {
        this.removePostfix = removePostFix;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
