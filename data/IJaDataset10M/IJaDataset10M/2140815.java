package charismata.resource;

import java.util.List;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import charismata.domain.CharismataPackage;
import charismata.domain.CharismataResource;
import charismata.util.ResourceUtil;
import charismata.web.zk.BaseController;
import charismata.web.zk.MainController;
import charismata.web.zk.ZKUtil;

public class NewResourceController extends BaseController implements AfterCompose {

    ResourceType resourceType;

    public void initEditor() {
        String resourceTypeName = Executions.getCurrent().getParameter("resourceTypeName");
        resourceType = ResourceTypeList.getResourceType(resourceTypeName);
        resourceTypeName = resourceType.getResourceTypeLabel();
        Label label = (Label) getComponent("resourceTypeName");
        label.setValue(resourceTypeName + " Name");
        resourceType.initNewResourcePanel(this);
    }

    public void onCreateResource() {
        String author = ZKUtil.getUsername(this);
        Textbox resourceNameTB = (Textbox) getComponent("resourceName");
        Textbox packageNameTB = (Textbox) getComponent("packageName");
        String resourceName = resourceNameTB.getText();
        String packageName = packageNameTB.getText();
        if ("".equals(resourceName)) {
            try {
                Messagebox.show(resourceType.getResourceTypeLabel() + " Name cannot be empty");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resourceNameTB.focus();
            return;
        }
        resourceType.saveNewResource(packageName, resourceName, author, ZKUtil.getMainController(this));
        CharismataResource resource = ResourceUtil.loadResource(resourceType.getResourceTypeName(), resourceName, packageName);
        MainController main = ZKUtil.getMainController(this);
        resourceType.loadResourceEditorPanel(main, resource);
    }

    public void afterCompose() {
        initEditor();
        Treechildren packageTree = (Treechildren) getComponent("newResource_treeChildren");
        PackageTreeListener gstl = new PackageTreeListener();
        List<CharismataPackage> pkgList = ResourceUtil.listPackages();
        for (CharismataPackage pkg : pkgList) {
            if (!"".equals(pkg.getPackageName())) {
                Treeitem ti = new Treeitem();
                Treerow tr = new Treerow();
                Treecell tcName = new Treecell(pkg.getPackageName());
                tcName.addEventListener("onClick", gstl);
                tcName.setParent(tr);
                tr.setParent(ti);
                ti.setParent(packageTree);
            }
        }
    }
}
