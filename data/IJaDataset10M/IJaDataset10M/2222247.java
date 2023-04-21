package at.fhjoanneum.aim.sdi.project.tab;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import at.fhjoanneum.aim.sdi.project.ldap.LDAPObject;
import at.fhjoanneum.aim.sdi.project.utilities.GlobalProperties;
import at.fhjoanneum.aim.sdi.project.utilities.ServiceLocator;

@SuppressWarnings("serial")
public class AdminModal extends Window {

    private static final Logger log = Logger.getLogger(AdminModal.class);

    private ServiceLocator service = new ServiceLocator();

    private LinkedList<String> root = new LinkedList<String>();

    private String ldapRoot = "OU=Technikum,DC=technikum,DC=fh-joanneum,DC=local";

    private String rootLabel = "LDAP";

    @SuppressWarnings("static-access")
    public AdminModal() {
        root.add(ldapRoot);
        setVariable("root", root, true);
        setVariable("rootLabel", rootLabel, true);
    }

    public void onCreate() {
    }

    public void select() {
        Window win = (Window) Path.getComponent("//admin/adminModal");
        Textbox tbxSelection = (Textbox) Path.getComponent("//admin/adminModal/tbx_ldapSelection");
        Listbox lbxLdapList = (Listbox) Path.getComponent("//admin/adminTab/lbx_ldapUnits");
        if (!tbxSelection.getValue().equals("")) {
            if (tbxSelection.getValue().startsWith("CN")) {
                try {
                    Messagebox.show("Select LDAP Organization Unit (OU) instead of Common Name (CN)", "Error", Messagebox.OK, Messagebox.ERROR);
                } catch (InterruptedException e) {
                    String message = e.getClass().getCanonicalName() + " " + e.getLocalizedMessage();
                    for (StackTraceElement element : e.getStackTrace()) {
                        message += element.toString();
                    }
                    GlobalProperties.getMyLogger().severe(message);
                    log.error(e);
                }
            } else {
                if (checkLdapUnitIfExists(tbxSelection.getValue())) {
                    try {
                        Messagebox.show("LDAP Unit exists already", "Error", Messagebox.OK, Messagebox.ERROR);
                    } catch (InterruptedException e) {
                        String message = e.getClass().getCanonicalName() + " " + e.getLocalizedMessage();
                        for (StackTraceElement element : e.getStackTrace()) {
                            message += element.toString();
                        }
                        GlobalProperties.getMyLogger().severe(message);
                        log.error(e);
                    }
                } else {
                    Listitem newItem = new Listitem();
                    newItem.setLabel(tbxSelection.getValue());
                    newItem.setParent(lbxLdapList);
                    win.setVisible(false);
                }
            }
        }
        tbxSelection.setValue("");
    }

    @SuppressWarnings("unchecked")
    private boolean checkLdapUnitIfExists(String value) {
        Listbox lbxLdapList = (Listbox) Path.getComponent("//admin/adminTab/lbx_ldapUnits");
        List<Listitem> ldapList = (List<Listitem>) lbxLdapList.getItems();
        for (Listitem item : ldapList) {
            if (item.getLabel().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void refreshInfo(Tree tree) {
        Textbox tbx_selection = (Textbox) Path.getComponent("//admin/adminModal/tbx_ldapSelection");
        Treeitem selectedItem = tree.getSelectedItem();
        tbx_selection.setValue(selectedItem.getId());
    }

    public void createSubNode(Treeitem parentNode, boolean callByListener) {
        Collection<LDAPObject> ldapList = new LinkedList<LDAPObject>();
        log.info("PATH: " + parentNode.getId());
        if (parentNode.isOpen()) {
            Treechildren children = parentNode.getTreechildren();
            if (children.getItemCount() <= 0) {
                if (callByListener) {
                    ldapList = service.getLdapService().getLdapUser(parentNode.getId());
                } else {
                    ldapList = service.getLdapService().getLdapUser(ldapRoot);
                }
                for (LDAPObject ldapItem : ldapList) {
                    Treeitem newItem = new Treeitem();
                    String tempPath = "OU=" + ldapItem.getName() + "," + parentNode.getId();
                    if (service.getLdapService().getLdapUser(tempPath).iterator().hasNext()) {
                        Treechildren newChildren = new Treechildren();
                        newChildren.setParent(newItem);
                        newItem.setOpen(false);
                        TreeListener listener = new TreeListener();
                        newItem.addEventListener("onOpen", listener);
                        newItem.setId("OU=" + ldapItem.getName() + "," + parentNode.getId());
                    } else {
                        newItem.setId("CN=" + ldapItem.getName() + "," + parentNode.getId());
                    }
                    newItem.setParent(children);
                    Treerow newRow = new Treerow();
                    newRow.setParent(newItem);
                    Treecell newCell = new Treecell(ldapItem.getName());
                    newCell.setParent(newRow);
                }
            }
        }
    }

    private class TreeListener implements EventListener {

        public void onEvent(Event event) {
            Treeitem treeItem = (Treeitem) event.getTarget();
            if (treeItem.isOpen()) {
                createSubNode(treeItem, true);
            }
        }
    }
}
