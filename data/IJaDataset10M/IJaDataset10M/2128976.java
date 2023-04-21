package net.sf.jcmdlineparser.strategy;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import net.sf.jcmdlineparser.ParserException;
import net.sf.jcmdlineparser.options.AbstractOption;
import net.sf.jcmdlineparser.options.NoSuchOptionException;
import net.sf.jcmdlineparser.options.OptionUtils;
import net.sf.jcmdlineparser.options.VoidOption;
import net.sf.jcmdlineparser.strategy.escape.StringHandlerImpl;
import net.sf.kerner.commons.AbstractCounter;
import net.sf.kerner.commons.StringUtils;
import net.sf.kerner.commons.logging.Log;

/**
 * <p>
 * TODO description
 * </p>
 * 
 * <p>
 * TODO example
 * </p>
 * 
 * @autor Alexander Kerner
 * 
 */
public class StrategyImpl implements Strategy {

    /**
	 * TODO whitespaces in general?
	 */
    private final Character optionDelimiter = Character.valueOf(' ');

    private static final Log log = new Log(StrategyImpl.class);

    protected volatile boolean escape = false;

    protected volatile char currentKeyValueDelim;

    protected final StrategyRules rules;

    public StrategyImpl(StrategyRules rules) {
        this.rules = rules;
    }

    protected boolean isOptionDelimChar(char c) {
        return Character.isWhitespace(c);
    }

    public String parse(String string, Set<AbstractOption<?>> options) throws ParserException {
        if (string == null || options == null || options.isEmpty() || string.length() < 1) throw new NullPointerException("empty string or no options given");
        log.debug("parsing string: \"" + string + "\", options to process: \"" + options + "\"");
        string = string.trim();
        final StringBuilder unknown = new StringBuilder();
        StringBuilder cache = new StringBuilder();
        final AbstractCounter cnt = new AbstractCounter() {

            public void perform() {
            }
        };
        for (int i = 0; i < string.length(); i++) {
            if (string.length() <= cnt.getCount()) {
                break;
            }
            char c = string.charAt(cnt.getCount());
            if (rules.isKeyValueSeparator(c)) {
                currentKeyValueDelim = c;
                final String key = cache.toString();
                log.debug("key is valid: " + rules.isValidKeyString(key));
                log.debug("key is known: " + OptionUtils.optionKnown(options, key));
                if (rules.isValidKeyString(key) && OptionUtils.optionKnown(options, key)) {
                    log.debug("key found: \"" + key + "\"");
                    AbstractOption<?> o = OptionUtils.getOptionForKey(options, key);
                    if (o instanceof VoidOption) {
                        log.debug("void option, not parsing for value");
                        ((VoidOption) o).setSet(true);
                    } else {
                        final String value = findValue(string.substring(cnt.getCount() + 1));
                        StrategyUtils.handleKeyValuePair(key, removeEscapeCharacters(value), options);
                        log.debug("key value pair sucessfully processed: " + key + currentKeyValueDelim + value);
                        cnt.setCount(cnt.getCount() + value.length() + 1);
                    }
                    cache = new StringBuilder();
                } else {
                    log.debug("key unknown or contains illegal characters");
                    unknown.append(cache);
                    unknown.append(currentKeyValueDelim);
                    cache = new StringBuilder();
                }
            } else cache.append(c);
            cnt.count();
        }
        if (OptionUtils.optionKnown(options, cache.toString())) {
            log.debug("emerceny check for void option sucessfull");
            AbstractOption<?> o = OptionUtils.getOptionForKey(options, cache.toString());
            if (o instanceof VoidOption) {
                log.debug("void option, not parsing for value");
                ((VoidOption) o).setSet(true);
            } else {
                throw new RuntimeException("something badly wrong here! [" + cache.toString() + "]");
            }
        } else unknown.append(cache);
        log.debug("parsing done, unknwon: \"" + unknown.toString().trim() + "\"");
        return unknown.toString().trim();
    }

    public String[] parse(String[] string, Set<AbstractOption<?>> options) throws ParserException {
        final StringBuilder sb = new StringBuilder();
        for (String s : string) {
            sb.append(s);
            sb.append(optionDelimiter);
        }
        return parse(sb.toString(), options).split(optionDelimiter.toString());
    }

    protected String removeEscapeCharacters(String string) {
        log.debug("cleaning string \"" + string + "\" from whitespaces");
        final String result = new StringHandlerImpl(rules).handleString(string);
        log.debug("cleaning done, returning \"" + result.toString() + "\"");
        return result.toString();
    }

    protected String findValue(String string) {
        log.debug("finding value in [" + string + "]");
        final StringBuilder cache = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (rules.isEscapeChar(c)) {
                log.debug("found escape char");
                if (escape) {
                    log.debug("disabeling escaping");
                    escape = false;
                } else {
                    log.debug("enabeling escaping");
                    escape = true;
                }
            }
            if (isOptionDelimChar(c) && escape == false && !rules.isEscapeFlag(string.charAt(i - 1))) {
                log.debug("found whitespace, returning \"" + cache.toString() + "\"");
                return cache.toString();
            } else {
                cache.append(c);
            }
        }
        return cache.toString();
    }
}
