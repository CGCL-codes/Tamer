package org.vspirit.doveide.project.manager;

import org.netbeans.spi.project.support.GenericSources;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.vspirit.doveide.project.builder.DoveBuilder;
import org.vspirit.doveide.project.manager.ui.DoveProjectInformation;
import org.vspirit.doveide.project.manager.ui.DoveProjectLogicView;
import org.vspirit.doveide.project.manager.ui.actions.DoveProjectCustomizerProvider;
import org.vspirit.doveide.project.manager.ui.actions.DoveProjectRootNodeActionProvider;
import org.vspirit.doveide.project.manager.ui.actions.DoveProjectRootNodeOperation;
import org.vspirit.doveide.project.manager.ui.actions.DoveProjectSubNodeActionProvider;
import org.vspirit.doveide.project.manager.ui.actions.DoveProjectSubNodeOperation;

/**
 * 创建与项目相关的唯一Lookup,这个Lookup将关联以下对象
 * DoveProjectLogicView 项目视图
 * DoveRootNodeActionProvider 项目根结点的下拉菜单操作定义
 * DoveRootNodeOperation  项目根节点的下拉菜单的操作实现
 * DoveSubNodeActionProvider 项目的子节点的下拉菜单操作定义
 * DoveSubNodeOperation  项目根节点的下拉菜单的操作实现
 * GenericSources.genericOnly(project) 项目中的所有文件的集合
 * DoveProjectInformation  项目信息
 * @author codekitten
 */
public class DoveProjectLookupFactory {

    public static Lookup createLookup(DoveProject project) {
        return Lookups.fixed(new Object[] { project, new DoveProjectLogicView(project), new DoveProjectRootNodeActionProvider(project), new DoveProjectRootNodeOperation(project), new DoveProjectSubNodeActionProvider(project), new DoveProjectSubNodeOperation(project), GenericSources.genericOnly(project), new DoveProjectInformation(project), new DoveProjectCustomizerProvider(project), new DoveBuilder(project.getProjectDirectory().getPath()) });
    }
}
