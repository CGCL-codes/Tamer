package com.turnengine.client.global.error.bean;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Command Stack Trace.
 */
public class CommandStackTrace implements ICommandStackTrace, Comparable<ICommandStackTrace> {

    /** The id. */
    private long id = 0;

    /** The timestamp. */
    private long timestamp = -1;

    /** The command. */
    private String command = "";

    /** The trace. */
    private String trace = "";

    /**
	 * Creates a new Command Stack Trace.
	 */
    public CommandStackTrace() {
    }

    /**
	 * Creates a new Command Stack Trace.
	 * @param id the id
	 * @param timestamp the timestamp
	 * @param command the command
	 * @param trace the trace
	 */
    public CommandStackTrace(long id, long timestamp, String command, String trace) {
        setId(id);
        setTimestamp(timestamp);
        setCommand(command);
        setTrace(trace);
    }

    /**
	 * Creates a new Command Stack Trace.
	 * @param commandStackTrace the command stack trace
	 */
    public CommandStackTrace(CommandStackTrace commandStackTrace) {
        setId(commandStackTrace.getId());
        setTimestamp(commandStackTrace.getTimestamp());
        setCommand(commandStackTrace.getCommand());
        setTrace(commandStackTrace.getTrace());
    }

    /**
	 * Creates a new Command Stack Trace.
	 * @param iCommandStackTrace the i command stack trace
	 */
    public CommandStackTrace(ICommandStackTrace iCommandStackTrace) {
        setId(iCommandStackTrace.getId());
        setTimestamp(iCommandStackTrace.getTimestamp());
        setCommand(iCommandStackTrace.getCommand());
        setTrace(iCommandStackTrace.getTrace());
    }

    /**
	 * Returns the id.
	 * @return the id.
	 */
    public long getId() {
        return id;
    }

    /**
	 * Returns the timestamp.
	 * @return the timestamp.
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * Returns the command.
	 * @return the command.
	 */
    public String getCommand() {
        return command;
    }

    /**
	 * Returns the trace.
	 * @return the trace.
	 */
    public String getTrace() {
        return trace;
    }

    /**
	 * Sets the id.
	 * @param id the id to set.
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * Sets the timestamp.
	 * @param timestamp the timestamp to set.
	 */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
	 * Sets the command.
	 * @param command the command to set.
	 */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
	 * Sets the trace.
	 * @param trace the trace to set.
	 */
    public void setTrace(String trace) {
        this.trace = trace;
    }

    /**
	 * Returns the hash code.
	 * @return the hash code.
	 */
    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder(31, 37);
        hash.append(id);
        hash.append(timestamp);
        hash.append(command);
        hash.append(trace);
        return hash.toHashCode();
    }

    /**
	 * Returns true if this is equal to the given object.
	 * @param object the object to compare.
	 * @return true if this is equal to the given object.
	 */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CommandStackTrace) {
            CommandStackTrace compare = (CommandStackTrace) object;
            EqualsBuilder equals = new EqualsBuilder();
            equals.append(this.id, compare.id);
            equals.append(this.timestamp, compare.timestamp);
            equals.append(this.command, compare.command);
            equals.append(this.trace, compare.trace);
            return equals.isEquals();
        }
        return false;
    }

    /**
	 * Compare this to the given object.
	 * @param compare the object to compare to.
	 * @return the result of the comparison.
	 */
    @Override
    public int compareTo(ICommandStackTrace compare) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(this.id, compare.getId());
        builder.append(this.timestamp, compare.getTimestamp());
        builder.append(this.command, compare.getCommand());
        builder.append(this.trace, compare.getTrace());
        return builder.toComparison();
    }

    /**
	 * Returns this as a string.
	 * @return this as a string.
	 */
    @Override
    public String toString() {
        ToStringBuilder string = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        string.append("id", id);
        string.append("timestamp", timestamp);
        if (command != null) {
            string.append("command", command);
        }
        if (trace != null) {
            string.append("trace", trace);
        }
        return string.toString();
    }
}
