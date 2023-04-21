package net.simpleframework.organization.component.jobchartselect;

import java.util.Collection;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.IDictionaryHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IJobChartSelectHandle extends IDictionaryHandle {

    Collection<? extends IJobChart> getJobCharts(ComponentParameter compParameter, IDepartment department);

    Collection<? extends AbstractTreeNode> getTreenodes(ComponentParameter compParameter, AbstractTreeBean treeBean, AbstractTreeNode treeNode);
}
