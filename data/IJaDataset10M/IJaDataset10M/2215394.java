package org.qcmylyn.core.comment;

import static org.qcmylyn.core.QcMylynCorePlugin.log;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.qcmylyn.core.QcMylynCorePlugin;
import org.qcmylyn.core.messages.Messages;

/**
 * The comment template registry.
 * 
 * @author tszadel
 */
public final class CommentTemplateRegistry {

    private static final String DATE_FORMAT_PATTERN = "dateFormatPattern";

    /** The extension point name. */
    private static final String EXTENSION_POINT = "comment.templates";

    private static final String ID = "id";

    /** The instance. */
    private static CommentTemplateRegistry instance = null;

    private static final String NAME = "name";

    private static final String PARSER = "parser";

    /**
     * Returns the instance ; creates it if not exists.
     * 
     * @return The instance.
     */
    public static synchronized CommentTemplateRegistry getInstance() {
        if (instance == null) {
            instance = new CommentTemplateRegistry();
        }
        return instance;
    }

    private final Map<String, CommentTemplate> templates = new TreeMap<String, CommentTemplate>();

    /**
     * Constructor.
     */
    private CommentTemplateRegistry() {
        readExtension(Platform.getExtensionRegistry());
    }

    /**
     * Returns a templated identified by its id.
     * 
     * @param pId The id of the template.
     * @return The template or null if not found.
     */
    public CommentTemplate getTemplate(final String pId) {
        if (pId == null) {
            return null;
        }
        return templates.get(pId);
    }

    /**
     * Returns all the templates.
     * 
     * @return The templates.
     */
    public Collection<CommentTemplate> getTemplates() {
        return Collections.unmodifiableCollection(templates.values());
    }

    /**
     * Read the layer extensions within a registry.
     * 
     * @param pRegistry the extension registry
     */
    private void readExtension(final IExtensionRegistry pRegistry) {
        log.info("Reading comment extension");
        IExtensionPoint lPoint = pRegistry.getExtensionPoint(QcMylynCorePlugin.PLUGIN_ID, EXTENSION_POINT);
        if (lPoint != null) {
            IExtension[] lExtensions = lPoint.getExtensions();
            for (IExtension lExtension : lExtensions) {
                for (IConfigurationElement lConfig : lExtension.getConfigurationElements()) {
                    String lName = lConfig.getAttribute(NAME);
                    if (log.isDebugEnabled()) {
                        log.debug("Found template: " + lName);
                    }
                    try {
                        if (templates.containsKey(lName)) {
                            log.warn("Template [" + lName + "] already defined");
                        } else {
                            IQcCommentParser lParser = (IQcCommentParser) lConfig.createExecutableExtension(PARSER);
                            String lId = lConfig.getAttribute(ID);
                            lParser.setDateFormat(lConfig.getAttribute(DATE_FORMAT_PATTERN));
                            templates.put(lId, new CommentTemplate(lId, lName, lParser));
                        }
                    } catch (CoreException e) {
                        log.warn("Invalid parser class" + lConfig.getAttribute(PARSER), e);
                    } catch (PatternSyntaxException e) {
                        log.warn(e.getMessage(), e);
                    }
                }
            }
        }
    }
}
