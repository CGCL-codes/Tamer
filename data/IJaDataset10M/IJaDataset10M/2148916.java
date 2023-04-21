package bma.bricks.otter.ui.site;

import bma.bricks.innersession.InnerSessionDomain;
import bma.bricks.module.DependModuleList;
import bma.bricks.module.Module;
import bma.bricks.module.ModuleInfo;
import bma.bricks.module.ModuleManager;
import bma.bricks.module.ModulePhase;
import bma.bricks.module.common.MCDomain;
import bma.bricks.objectlet.method.ObjectletMethodDomain;
import bma.bricks.objectlet.method.ObjectletMethodManager;
import bma.bricks.otter.model.category.site.SiteCategoryCoreDomain;
import bma.bricks.otter.publish.engine.PublishEngineDomain;
import bma.bricks.otter.ui.category.objectlet.CategoryTreeObjectletMethod;
import bma.bricks.otter.ui.site.objectlet.SiteCategoryObjectlet;

/**
 * ��վ����ģ��<br>
 * 
 * @author ����
 * 
 */
public class SiteUIDomain extends MCDomain {

    @Override
    public void configPhaseControl(ModulePhase phase, Module thisModule, ModuleManager manager) {
        ObjectletMethodManager omm = ObjectletMethodManager.getInstance();
        SiteCategoryObjectlet sc = new SiteCategoryObjectlet();
        omm.addObject(sc);
        CategoryTreeObjectletMethod ct = new CategoryTreeObjectletMethod(sc.getObjectletTarget());
        omm.addObject(ct);
    }

    @Override
    public void buildDependModule(DependModuleList info) {
        info.addDepend(ObjectletMethodDomain.class);
        info.addDepend(PublishEngineDomain.class);
        info.addDepend(SiteCategoryCoreDomain.class);
        info.addDepend(InnerSessionDomain.class);
    }

    @Override
    public void buildModuleInfo(ModuleInfo info, ModuleManager manager) {
        info.setTitle("��վ����ģ��");
    }
}
