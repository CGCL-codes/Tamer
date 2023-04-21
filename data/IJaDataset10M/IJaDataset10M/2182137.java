package eu.medsea.mimeutil.detector.eclipse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import eu.medsea.mimeutil.detector.MimeTypeDetector;
import eu.medsea.mimeutil.detector.MimeTypeDetectorRegistry;

public class MimeTypeDetectorExtensionPointProcessor {

    private static Log log = LogFactory.getLog(MimeTypeDetectorExtensionPointProcessor.class);

    /**
	 * Process all extension to the extension-point defined by {@link #getExtensionPointID()}
	 */
    public void processElement(IExtension extension, IConfigurationElement element) throws Exception {
        MimeTypeDetector detector = (MimeTypeDetector) element.createExecutableExtension("class");
        String detectorID = element.getAttribute("detectorID");
        String orderHintStr = element.getAttribute("orderHint");
        int orderHint = 7000;
        if (orderHintStr != null) orderHint = Integer.parseInt(orderHintStr);
        detector.setDetectorID(detectorID);
        detector.setOrderHint(orderHint);
        MimeTypeDetectorRegistry.sharedInstance().addMimeTypeDetector(detector);
    }

    public synchronized void process() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        if (registry != null) {
            IExtensionPoint extensionPoint = registry.getExtensionPoint(getExtensionPointID());
            if (extensionPoint == null) {
                throw new IllegalStateException("Unable to resolve extension-point: " + getExtensionPointID());
            }
            IExtension[] extensions = extensionPoint.getExtensions();
            for (int i = 0; i < extensions.length; i++) {
                IExtension extension = extensions[i];
                IConfigurationElement[] elements = extension.getConfigurationElements();
                for (int j = 0; j < elements.length; j++) {
                    IConfigurationElement element = elements[j];
                    try {
                        processElement(extension, element);
                    } catch (Throwable e) {
                        log.error("Error processing extension element. The element is located in an extension in bundle: " + extension.getNamespaceIdentifier(), e);
                    }
                }
            }
        }
    }

    public String getExtensionPointID() {
        return "eu.medsea.mimeutil.mimeTypeDetectorFactory";
    }
}
