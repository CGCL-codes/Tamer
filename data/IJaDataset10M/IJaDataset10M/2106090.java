package de.juwimm.cms.content.modules;

import javax.swing.JPanel;
import de.juwimm.cms.content.panel.ContentBorder;
import de.juwimm.cms.content.panel.ContentBorderIterationAtomPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: ModuleFactoryIterationImpl.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class ModuleFactoryIterationImpl extends ModuleFactoryStandardImpl implements ModuleFactory {

    public JPanel getPanelForModule(Module module) {
        ContentBorder cb = new ContentBorderIterationAtomPanel();
        cb.setContentModulePanel(module.viewPanelUI());
        cb.setLabel(module.getLabel());
        return (JPanel) cb;
    }
}
