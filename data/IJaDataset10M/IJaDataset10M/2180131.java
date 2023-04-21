package be.vanvlerken.bert.logmonitor.logging;

/**
 * Provides an encode method that is able to encode the LogEntry fields to a specific
 * string output format
 */
public interface ILogEntryEncoder {

    public String encodeLogEntry(ILogEntry logEntry);
}
