package org.acegisecurity.intercept.web;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.ConfigAttributeEditor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import java.beans.PropertyEditorSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Property editor to assist with the setup of a {@link
 * FilterInvocationDefinitionSource}.
 * 
 * <p>
 * The class creates and populates a {@link
 * RegExpBasedFilterInvocationDefinitionMap} or {@link
 * PathBasedFilterInvocationDefinitionMap} (depending on the type of patterns
 * presented).
 * </p>
 * 
 * <p>
 * By default the class treats presented patterns as regular expressions. If
 * the keyword <code>PATTERN_TYPE_APACHE_ANT</code> is present (case
 * sensitive), patterns will be treated as Apache Ant paths rather than
 * regular expressions.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: FilterInvocationDefinitionSourceEditor.java,v 1.4 2006/01/04 21:35:10 luke_t Exp $
 */
public class FilterInvocationDefinitionSourceEditor extends PropertyEditorSupport {

    private static final Log logger = LogFactory.getLog(FilterInvocationDefinitionSourceEditor.class);

    public void setAsText(String s) throws IllegalArgumentException {
        FilterInvocationDefinitionMap source = new RegExpBasedFilterInvocationDefinitionMap();
        if ((s == null) || "".equals(s)) {
        } else {
            if (s.lastIndexOf("PATTERN_TYPE_APACHE_ANT") != -1) {
                source = new PathBasedFilterInvocationDefinitionMap();
                if (logger.isDebugEnabled()) {
                    logger.debug(("Detected PATTERN_TYPE_APACHE_ANT directive; using Apache Ant style path expressions"));
                }
            }
            BufferedReader br = new BufferedReader(new StringReader(s));
            int counter = 0;
            String line;
            while (true) {
                counter++;
                try {
                    line = br.readLine();
                } catch (IOException ioe) {
                    throw new IllegalArgumentException(ioe.getMessage());
                }
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (logger.isDebugEnabled()) {
                    logger.debug("Line " + counter + ": " + line);
                }
                if (line.startsWith("//")) {
                    continue;
                }
                if (line.equals("CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON")) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Line " + counter + ": Instructing mapper to convert URLs to lowercase before comparison");
                    }
                    source.setConvertUrlToLowercaseBeforeComparison(true);
                    continue;
                }
                if (line.lastIndexOf('=') == -1) {
                    continue;
                }
                String[] nameValue = StringUtils.delimitedListToStringArray(line, "=");
                String name = nameValue[0];
                String value = nameValue[1];
                if (!StringUtils.hasLength(name) || !StringUtils.hasLength(value)) {
                    throw new IllegalArgumentException("Failed to parse a valid name/value pair from " + line);
                }
                ConfigAttributeEditor configAttribEd = new ConfigAttributeEditor();
                configAttribEd.setAsText(value);
                ConfigAttributeDefinition attr = (ConfigAttributeDefinition) configAttribEd.getValue();
                source.addSecureUrl(name, attr);
            }
        }
        setValue(source);
    }
}
