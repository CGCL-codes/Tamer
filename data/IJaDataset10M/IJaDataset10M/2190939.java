package eu.annocultor.api;

/**
 * Converter status for debugging and monitoring.
 * 
 * @author Borys Omelayenko
 */
public class ConverterStatus {

    private long partsQueueLength;

    public ConverterStatus(long partsQueueLength) {
        this.partsQueueLength = partsQueueLength;
    }

    public long getPartsQueueLength() {
        return partsQueueLength;
    }
}
