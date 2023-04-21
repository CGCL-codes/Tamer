package bma.bricks.otter.ui;

import bma.bricks.module.DependModuleList;
import bma.bricks.module.Module;
import bma.bricks.module.ModuleInfo;
import bma.bricks.module.ModuleManager;
import bma.bricks.module.ModulePhase;
import bma.bricks.module.common.MCDomain;
import bma.bricks.otter.ui.progress.ProgressUIDomain;
import bma.bricks.otter.ui.publet.PubletUIDomain;
import bma.bricks.otter.ui.publish.PublishUIDomain;
import bma.bricks.otter.ui.pubtemplate.PublishTemplateUIDomain;
import bma.bricks.otter.ui.site.SiteUIDomain;

public class OtterUIDomain extends MCDomain {

    @Override
    public void configPhaseControl(ModulePhase phase, Module thisModule, ModuleManager manager) {
    }

    @Override
    public void buildDependModule(DependModuleList info) {
        info.addDepend(ProgressUIDomain.class);
        info.addDepend(PublishUIDomain.class);
        info.addDepend(PubletUIDomain.class);
        info.addDepend(SiteUIDomain.class);
        info.addDepend(PublishTemplateUIDomain.class);
    }

    @Override
    public void buildModuleInfo(ModuleInfo info, ModuleManager manager) {
        info.setTitle("�������ģ��");
    }
}
