package net.sf.xpontus.plugins.perspectives;

import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.properties.PropertiesHolder;
import java.util.List;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PerspectiveHelper {

    private PerspectiveHelper() {
    }

    /**
     *
     * @param mimeType
     * @return
     */
    public static IDocumentContainer createPerspective(String mimeType) {
        PerspectivePluginIF m_perspective = null;
        List<PerspectivePluginIF> availablePerspectives = (List<PerspectivePluginIF>) PropertiesHolder.getPropertyValue(XPontusPropertiesConstantsIF.XPONTUS_PERSPECTIVES);
        for (PerspectivePluginIF currentPerspective : availablePerspectives) {
            String m_mime = currentPerspective.getContentType();
            if (m_mime.equals(mimeType)) {
                m_perspective = currentPerspective;
                break;
            }
        }
        if (m_perspective == null) {
            m_perspective = new DefaultPerspectiveImpl();
        }
        return m_perspective.createDocumentContainer();
    }
}
