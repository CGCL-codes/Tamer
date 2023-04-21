package xmitools2.util.processor;

import xmitools2.model.XmiFactory;

/**
 * Creates an UML-processor.
 * 
 * @author rlechner
 */
public final class UmlProcessorFactory {

    /**
     * Creates an UML-processor for MagicDraw models.
     * 
     * @param exporterName value of <code>XMI.exporter</code>
     * @param exporterVersion value of <code>XMI.exporterVersion</code>
     * @param factory XMI model element factory
     */
    public static UmlProcessor create(String exporterName, String exporterVersion, XmiFactory factory) {
        if (exporterName.equals("MagicDraw UML")) {
            return new MagicDrawProcessor(exporterVersion, factory);
        }
        return null;
    }
}
