package net.simpleframework.my.file;

import java.util.ArrayList;
import java.util.Collection;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.component.catalog.AbstractAccountCatalogHandle;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FolderHandle extends AbstractAccountCatalogHandle {

    @Override
    public IFileApplicationModule getApplicationModule() {
        return MyFileUtils.applicationModule;
    }

    @Override
    public Class<? extends IIdBeanAware> getEntityBeanClass() {
        return MyFolder.class;
    }

    @Override
    public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
        if (beanClazz.isAssignableFrom(MyFolder.class)) {
            final ExpressionValue ev2 = new ExpressionValue("catalogid=?", ev.getValues());
            final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter, MyFileUtils.getFileComponentBean(compParameter));
            final FileHandle hdl = (FileHandle) nComponentParameter.getComponentHandle();
            hdl.doDelete(nComponentParameter, ev2);
        }
        super.doDelete(compParameter, ev, beanClazz);
    }

    @Override
    protected String getRootID(final AbstractTreeBean treeBean) {
        return "0";
    }

    @Override
    public Collection<? extends AbstractTreeNode> getCatalogTreenodes(final ComponentParameter compParameter, final AbstractTreeBean treeBean, final AbstractTreeNode treeNode, final boolean dictionary) {
        final Collection<AbstractTreeNode> treeNodes = new ArrayList<AbstractTreeNode>();
        final String imgBase = MyFileUtils.getCssPath(compParameter) + "/images/";
        final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter, treeBean, treeNode, dictionary);
        treeNodes.addAll(coll);
        for (final AbstractTreeNode treeNode2 : treeNodes) {
            if (!dictionary) {
                final MyFolder myFolder = (MyFolder) treeNode2.getDataObject();
                if (myFolder != null) {
                    treeNode2.setPostfixText(WebUtils.enclose(myFolder.getFiles()));
                    treeNode2.setImage(imgBase + "folder.png");
                    treeNode2.setJsClickCallback(MyFileUtils.__my_files_list(getIdParameterName(compParameter), myFolder.getId()));
                } else {
                    final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
                    if (stat != null) {
                        treeNode2.setPostfixText(WebUtils.enclose(stat.getRootFiles()));
                    }
                    treeNode2.setTooltip(LocaleI18n.getMessage("My.folder_c.0"));
                    treeNode2.setImage(imgBase + "folder_root.png");
                    treeNode2.setJsClickCallback(MyFileUtils.__my_files_list(getIdParameterName(compParameter), null));
                }
            } else {
                treeNode2.setImage(imgBase + "folder.png");
            }
        }
        if (treeNode == null && !dictionary) {
            AbstractTreeNode treeNode2 = new TreeNode(treeBean, treeNode, LocaleI18n.getMessage("FolderHandle.1"));
            treeNode2.setJsClickCallback(MyFileUtils.__my_files_list(getIdParameterName(compParameter), MyFileUtils.DELETE_ID));
            treeNode2.setImage(imgBase + "delete.png");
            treeNode2.setId(String.valueOf(MyFileUtils.DELETE_ID));
            treeNodes.add(treeNode2);
            final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
            if (stat != null) {
                treeNode2.setPostfixText(WebUtils.enclose(stat.getDeleteFiles()));
            }
            if (OrgUtils.isManagerMember(compParameter.getSession())) {
                treeNode2 = new TreeNode(treeBean, treeNode, LocaleI18n.getMessage("FolderHandle.3"));
                treeNode2.setJsClickCallback(MyFileUtils.__my_files_list(getIdParameterName(compParameter), MyFileUtils.ALL_ID));
                treeNode2.setImage(imgBase + "admin.png");
                treeNode2.setId(String.valueOf(MyFileUtils.ALL_ID));
                treeNodes.add(treeNode2);
            }
        }
        return treeNodes;
    }

    @Override
    public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
        String jsCallback = StringUtils.blank(super.getJavascriptCallback(compParameter, jsAction, bean));
        if ("delete".equals(jsAction)) {
            jsCallback += "__my_folder_refresh();$Actions['__my_files'].refresh('" + getIdParameterName(compParameter) + "=');";
        }
        return jsCallback;
    }
}
