package de.schlund.pfixxml.targets;

import java.util.TreeMap;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import de.schlund.pfixxml.resources.FileResource;
import de.schlund.pfixxml.resources.ResourceUtil;
import de.schlund.pfixxml.util.Xml;

/**
 * XMLVirtualTarget.java
 *
 *
 * Created: Mon Jul 23 21:53:06 2001
 *
 * @author <a href="mailto:jtl@schlund.de">Jens Lautenbacher</a>
 *
 *
 */
public class XMLVirtualTarget extends VirtualTarget {

    public XMLVirtualTarget(TargetType type, TargetGenerator gen, String key, Themes themes) throws Exception {
        this.type = type;
        this.generator = gen;
        this.targetkey = key;
        this.themes = themes;
        this.params = new TreeMap<String, Object>();
        this.auxdepmanager = new AuxDependencyManager(this, gen.getAuxDependencyFactory(), gen.getTargetDependencyRelation());
        auxdepmanager.tryInitAuxdepend();
    }

    /**
     * @see de.schlund.pfixxml.targets.TargetImpl#getValueFromDiscCache()
     */
    @Override
    protected Object getValueFromDiscCache() throws TransformerException {
        FileResource thefile = ResourceUtil.getFileResource(getTargetGenerator().getDisccachedir(), getTargetKey());
        if (thefile.exists() && thefile.isFile()) {
            return Xml.parse(generator.getXsltVersion(), thefile);
        } else {
            return null;
        }
    }

    public Document getDOM() throws TargetGenerationException {
        return (Document) this.getValue();
    }
}
