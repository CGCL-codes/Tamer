package org.deved.antlride.core.formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.deved.antlride.core.formatter.AntlrFormatterOptions.BracesStyle;
import org.deved.antlride.core.formatter.AntlrFormatterOptions.IndentStyle;

public class AntlrFormatterPreferences {

    public static String FORMATTER_PROFILES = ("formatter.profiles");

    public static String FORMATTER_ACTIVE_PROFILE = ("formatter.profiles.active");

    private static interface PreferenceValue {

        Object value();
    }

    private abstract static class AbstractFormatterPreference implements AntlrFormatterPreference {

        private final String optionName;

        protected final Object value;

        public AbstractFormatterPreference(String optionName, Object value) {
            this.optionName = optionName;
            this.value = value;
        }

        public boolean booleanValue() {
            throw new IllegalStateException("not a boolean!");
        }

        public int intValue() {
            throw new IllegalStateException("not a int!");
        }

        public String stringValue() {
            throw new IllegalStateException("not a string!");
        }

        public String getName() {
            return optionName;
        }

        public boolean isBoolean() {
            return false;
        }

        public boolean isInt() {
            return false;
        }

        public boolean isString() {
            return false;
        }
    }

    /** Indent Section */
    public static enum Indent implements AntlrFormatterPreference, PreferenceValue {

        TAB_CHAR(stringPreference("formatter.tabulation.char", IndentStyle.SPACE.name())), TAB_SIZE(intPreference("formatter.tabulation.size", 2)), INDENTATION_SIZE(intPreference("formatter.indentation.size", 2)), RULE(booleanPreference("indent.rule", true)), OPTIONS(booleanPreference("indent.options", true)), TOKENS(booleanPreference("indent.tokens", true)), BLOCKS(booleanPreference("indent.blocks", true)), ALIGN_TOKENS_IN_COLUMNS(booleanPreference("indent.align.tokensInColumns", true)), ALIGN_OPTIONS_IN_COLUMNS(booleanPreference("indent.align.optionsInColumns", true)), RULE_OPTIONS(booleanPreference("indent.rule.options", true)), BLOCK_OPTIONS(booleanPreference("indent.block.options", false)), REWRITE_OPERATOR(booleanPreference("indent.rewriteOperator", true));

        private AbstractFormatterPreference delegator;

        private Indent(AbstractFormatterPreference delegator) {
            this.delegator = delegator;
        }

        public boolean booleanValue() {
            return delegator.booleanValue();
        }

        public int intValue() {
            return delegator.intValue();
        }

        public String stringValue() {
            return delegator.stringValue();
        }

        public String getName() {
            return delegator.getName();
        }

        public boolean isBoolean() {
            return delegator.isBoolean();
        }

        public boolean isInt() {
            return delegator.isInt();
        }

        public boolean isString() {
            return delegator.isString();
        }

        public Object value() {
            return delegator.value;
        }
    }

    /** Blank Lines Section */
    public static enum BlankLines implements AntlrFormatterPreference, PreferenceValue {

        NEW_LINE(stringPreference("new.line", "\n")), LINES_BEFORE_GRAMMAR_DECLARATION(intPreference("line.file.before.grammar.decl", 0)), LINES_AFTER_GRAMMAR_DECLARATION(intPreference("line.file.after.grammar.decl", 1)), LINES_BEFORE_OPTIONS(intPreference("line.before.options", 1)), LINES_AFTER_OPTIONS(intPreference("line.after.options", 1)), LINES_BEFORE_TOKENS(intPreference("line.before.tokens", 1)), LINES_AFTER_TOKENS(intPreference("line.after.tokens", 1)), LINES_BEFORE_ACTION(intPreference("line.before.action", 1)), LINES_AFTER_ACTION(intPreference("line.after.action", 1)), LINES_BEFORE_RULE(intPreference("line.before.rule", 1)), LINES_BEFORE_FIRST_RULE(intPreference("line.before.first.rule", 1)), LINES_AFTER_RULE(intPreference("line.after.rule", 1)), LINES_BEFORE_SCOPE(intPreference("line.before.scope", 1)), LINES_AFTER_SCOPE(intPreference("line.after.scope", 1)), LINES_BEFORE_RULE_OPTIONS(intPreference("line.before.rule.options", 1)), LINES_AFTER_RULE_OPTIONS(intPreference("line.after.rule.options", 0)), LINES_BEFORE_RULE_SCOPE(intPreference("line.before.rule.scope", 1)), LINES_AFTER_RULE_SCOPE(intPreference("line.after.rule.scope", 0)), LINES_BEFORE_RULE_ACTION(intPreference("line.before.rule.action", 1)), LINES_AFTER_RULE_ACTION(intPreference("line.after.rule.action", 0));

        private AbstractFormatterPreference delegator;

        private BlankLines(AbstractFormatterPreference delegator) {
            this.delegator = delegator;
        }

        public boolean booleanValue() {
            return delegator.booleanValue();
        }

        public int intValue() {
            return delegator.intValue();
        }

        public String stringValue() {
            return delegator.stringValue();
        }

        public String getName() {
            return delegator.getName();
        }

        public boolean isBoolean() {
            return delegator.isBoolean();
        }

        public boolean isInt() {
            return delegator.isInt();
        }

        public boolean isString() {
            return delegator.isString();
        }

        public Object value() {
            return delegator.value;
        }
    }

    /** Braces Section */
    public static enum Braces implements AntlrFormatterPreference, PreferenceValue {

        OPTIONS(stringPreference("braces.options", BracesStyle.SAME_LINE.name())), TOKENS(stringPreference("braces.tokens", BracesStyle.SAME_LINE.name())), ACTIONS(stringPreference("braces.actions", BracesStyle.SAME_LINE.name())), SCOPES(stringPreference("braces.scopes", BracesStyle.SAME_LINE.name())), RULE_OPTIONS(stringPreference("braces.rule.options", BracesStyle.SAME_LINE.name())), RULE_ACTIONS(stringPreference("braces.rule.actions", BracesStyle.SAME_LINE.name())), RULE_SCOPES(stringPreference("braces.rule.scopes", BracesStyle.SAME_LINE.name()));

        private AbstractFormatterPreference delegator;

        private Braces(AbstractFormatterPreference delegator) {
            this.delegator = delegator;
        }

        public boolean booleanValue() {
            return delegator.booleanValue();
        }

        public int intValue() {
            return delegator.intValue();
        }

        public String stringValue() {
            return delegator.stringValue();
        }

        public String getName() {
            return delegator.getName();
        }

        public boolean isBoolean() {
            return delegator.isBoolean();
        }

        public boolean isInt() {
            return delegator.isInt();
        }

        public boolean isString() {
            return delegator.isString();
        }

        public Object value() {
            return delegator.value;
        }
    }

    /** White Spaces Section */
    public static enum WhiteSpaces implements AntlrFormatterPreference, PreferenceValue {

        BEFORE_AFTER_OPTION(booleanPreference("ws.before-after.option", true)), BEFORE_AFTER_TOKEN(booleanPreference("ws.before-after.tokenName", true)), BEFORE_AFTER_ASSIGN(booleanPreference("ws.before-after.assign", false)), BEFORE_AFTER_BLOCK_PARENTHESIS(booleanPreference("ws.before-after.blockParenthesis", false));

        private AbstractFormatterPreference delegator;

        private WhiteSpaces(AbstractFormatterPreference delegator) {
            this.delegator = delegator;
        }

        public boolean booleanValue() {
            return delegator.booleanValue();
        }

        public int intValue() {
            return delegator.intValue();
        }

        public String stringValue() {
            return delegator.stringValue();
        }

        public String getName() {
            return delegator.getName();
        }

        public boolean isBoolean() {
            return delegator.isBoolean();
        }

        public boolean isInt() {
            return delegator.isInt();
        }

        public boolean isString() {
            return delegator.isString();
        }

        public Object value() {
            return delegator.value;
        }
    }

    /** New Lines Section */
    public static enum ControlStatements implements AntlrFormatterPreference, PreferenceValue {

        NL_AFTER_RULE_MODIFIER(booleanPreference("nl.after.rule.modifier", true)), NL_BEFORE_RULE_COLON(booleanPreference("nl.before.rule.colon", true)), NL_BEFORE_RULE_ARGS(booleanPreference("nl.before.rule.args", false)), NL_BEFORE_RULE_RETURNS(booleanPreference("nl.before.rule.returns", false)), NL_BEFORE_RULE_THROWS(booleanPreference("nl.before.rule.throws", false)), NL_AFTER_REWRITE_OPERATOR(booleanPreference("nl.after.rewriteOperator", true)), NL_BEFORE_RULE_END(booleanPreference("nl.after.rule.end", true)), EMPTY_RULE_ON_ONE_LINE(booleanPreference("empty.rule.onOneLine", true));

        private AbstractFormatterPreference delegator;

        private ControlStatements(AbstractFormatterPreference delegator) {
            this.delegator = delegator;
        }

        public boolean booleanValue() {
            return delegator.booleanValue();
        }

        public int intValue() {
            return delegator.intValue();
        }

        public String stringValue() {
            return delegator.stringValue();
        }

        public String getName() {
            return delegator.getName();
        }

        public boolean isBoolean() {
            return delegator.isBoolean();
        }

        public boolean isInt() {
            return delegator.isInt();
        }

        public boolean isString() {
            return delegator.isString();
        }

        public Object value() {
            return delegator.value;
        }
    }

    public static Map<AntlrFormatterPreference, Object> toMap() {
        AntlrFormatterPreference[] preferences = toList();
        Map<AntlrFormatterPreference, Object> map = new HashMap<AntlrFormatterPreference, Object>();
        for (AntlrFormatterPreference preference : preferences) {
            map.put(preference, ((PreferenceValue) preference).value());
        }
        return map;
    }

    public static AntlrFormatterPreference[] toList() {
        List<AntlrFormatterPreference> all = new ArrayList<AntlrFormatterPreference>();
        all.addAll(Arrays.asList(Indent.values()));
        all.addAll(Arrays.asList(Braces.values()));
        all.addAll(Arrays.asList(BlankLines.values()));
        all.addAll(Arrays.asList(WhiteSpaces.values()));
        all.addAll(Arrays.asList(ControlStatements.values()));
        return all.toArray(new AntlrFormatterPreference[all.size()]);
    }

    public static String[] toStringArray() {
        AntlrFormatterPreference[] all = toList();
        String[] names = new String[all.length];
        for (int i = 0; i < all.length; i++) {
            names[i] = all[i].getName();
        }
        return names;
    }

    private static AbstractFormatterPreference intPreference(String name, int value) {
        return new AbstractFormatterPreference(name, value) {

            @Override
            public boolean isInt() {
                return true;
            }

            @Override
            public int intValue() {
                return (Integer) value;
            }
        };
    }

    private static AbstractFormatterPreference booleanPreference(String name, boolean value) {
        return new AbstractFormatterPreference(name, value) {

            @Override
            public boolean isBoolean() {
                return true;
            }

            @Override
            public boolean booleanValue() {
                return (Boolean) value;
            }
        };
    }

    private static AbstractFormatterPreference stringPreference(String name, String value) {
        return new AbstractFormatterPreference(name, value) {

            @Override
            public boolean isString() {
                return true;
            }

            @Override
            public String stringValue() {
                return (String) value;
            }
        };
    }
}
