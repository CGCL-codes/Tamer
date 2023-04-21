package net.sf.jIPtables.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A rule for the firewall. You can specify the generic rule options, a protocol
 * matcher(-p option), a target (-j option) and additional matcher(-m option)
 * 
 */
public class Rule extends Command implements Cloneable {

    private static final Pattern counterPatten = Pattern.compile("\\[(\\d*):(\\d*)\\]");

    private static final Pattern argumentPattern = Pattern.compile("(!?)[\\s]*([-]{1,2}[-a-zA-Z0-9]*)([\\s|\"]*)([\\w|.|?|=|&|+|\\s|:|,|\\\\|/]*)([\\s|\"]|$)");

    /**
	 * The chainName field can be written only by the rule itself and by other
	 * classes in the same package
	 */
    protected String chainname = "";

    /**
	 * The number of packets
	 */
    private long packets;

    /**
	 * The number of bytes
	 */
    private long bytes;

    /**
	 * Create an empty rule
	 */
    public Rule() {
    }

    /**
	 * @return A rule created from the passed command
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed command is null or is empty
	 */
    public static Rule buildFromCommand(String ruleCommand) {
        if (ruleCommand == null || ruleCommand.isEmpty()) throw new IllegalArgumentException("Invalid rule command");
        Rule rule = new Rule();
        rule.initRule(ruleCommand);
        return rule;
    }

    /**
	 * Initialize the passed rule with the string command
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed ruleCommand is invalid
	 */
    private void initRule(String ruleCommand) {
        if (ruleCommand == null || ruleCommand.isEmpty()) throw new IllegalArgumentException("Invalid rule command");
        parseArguments(ruleCommand);
        parseDataCounters(ruleCommand);
    }

    private void parseArguments(String ruleCommand) {
        Matcher argumentMatcher = argumentPattern.matcher(ruleCommand);
        while (argumentMatcher.find()) {
            String argumentName = argumentMatcher.group(2).trim();
            String argumentValue = argumentMatcher.group(4).trim();
            boolean isNegated = (argumentMatcher.group(1) != null && !argumentMatcher.group(1).isEmpty());
            if (isChainNameArgument(argumentName)) chainname = argumentValue; else setOption(argumentName, argumentValue, isNegated);
        }
    }

    private boolean isChainNameArgument(String argumentName) {
        return "-A".equals(argumentName) || "--append".equals(argumentName);
    }

    private void parseDataCounters(String ruleCommand) {
        Matcher counterMatcher = counterPatten.matcher(ruleCommand);
        if (counterMatcher.find()) {
            setPacketsNum(Long.parseLong(counterMatcher.group(1)));
            setBytesNum(Long.parseLong(counterMatcher.group(2)));
        }
    }

    /**
	 * @return The name of the associated chain
	 */
    public String getChainName() {
        return chainname;
    }

    /**
	 * Set the number of packets that have matched this rule
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed number of packets is lesser of 0
	 * 
	 */
    public void setPacketsNum(long packets) {
        if (packets < 0) throw new IllegalArgumentException("The packets number cannot be less than 0, " + packets + " given");
        this.packets = packets;
    }

    /**
	 * Set the number of bytes that have matched this rule
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed number of bytes is lesser of 0
	 */
    public void setBytesNum(long bytes) {
        if (bytes < 0) throw new IllegalArgumentException("The bytes number cannot be less than 0, " + bytes + " given");
        this.bytes = bytes;
    }

    /**
	 * @return The number of packets that have matched this rule
	 */
    public long getPacketsNum() {
        return packets;
    }

    /**
	 * @return The number of bytes that have matched this rule
	 */
    public long getBytesNum() {
        return bytes;
    }

    @Override
    public String getCommand() {
        return super.getCommand() + getDataCounter();
    }

    private String getDataCounter() {
        if (getPacketsNum() > 0 || getBytesNum() > 0) return " -c " + getPacketsNum() + " " + getBytesNum(); else return "";
    }

    /**
	 * @return A copy of this rule
	 */
    @Override
    public Rule clone() {
        return buildFromCommand(getCommand());
    }

    @Override
    public String toString() {
        return "Rule " + super.toString();
    }
}
