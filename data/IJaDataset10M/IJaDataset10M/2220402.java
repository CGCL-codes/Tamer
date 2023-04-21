package edu.xtec.jclic.bags;

import edu.xtec.jclic.Constants;
import edu.xtec.jclic.edit.Editor;
import edu.xtec.jclic.edit.EditorAction;
import edu.xtec.jclic.edit.EditorPanel;
import edu.xtec.jclic.fileSystem.FileSystem;
import edu.xtec.jclic.misc.Utils;
import edu.xtec.jclic.project.JClicProjectEditor;
import edu.xtec.util.Messages;
import edu.xtec.util.Options;
import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class MediaBagElementEditor extends Editor {

    private static boolean actionsCreated;

    private static Icon icon;

    public static EditorAction testMediaBagElementAction;

    /** Creates a new instance of MediaBagElementEditor */
    public MediaBagElementEditor(MediaBagElement el) {
        super(el);
    }

    protected void createChildren() {
    }

    public Class getEditorPanelClass() {
        return MediaBagElementEditorPanel.class;
    }

    public EditorPanel createEditorPanel(Options options) {
        return new MediaBagElementEditorPanel(options);
    }

    public MediaBagElement getMediaBagElement() {
        return (MediaBagElement) getFirstObject(MediaBagElement.class);
    }

    public MediaBag getMediaBag() {
        MediaBag result = null;
        if (getEditorParent() instanceof MediaBagEditor) {
            result = ((MediaBagEditor) getParent()).getMediaBag();
        }
        return result;
    }

    public String getName() {
        return getMediaBagElement().getName();
    }

    private static MediaPreviewPanel previewPanel = null;

    public void testMedia(java.awt.Component parent, Options options) {
        if (previewPanel == null) {
            previewPanel = new MediaPreviewPanel(this, options);
            options.getMessages().showInputDlg(parent, previewPanel, "edit_media_preview_tooltip", null);
            previewPanel.end();
            previewPanel = null;
        }
    }

    public boolean rename(String newName, Editor.EditorListener agent, Messages msg) {
        String oldName = getName();
        String errMsg = null;
        newName = newName.trim();
        Component parent = (agent instanceof Component) ? (Component) agent : null;
        boolean result = false;
        if (oldName.equals(newName)) {
        } else if (newName.length() < 1) errMsg = "edit_media_rename_invalid"; else if (getMediaBag().getElement(newName) != null) errMsg = "edit_media_rename_exists"; else {
            result = getProjectEditor().nameChanged(Utils.T_CODES[Utils.getFileType(getMediaBagElement().getFileName())], oldName, newName);
        }
        if (errMsg != null && msg != null && parent != null) {
            msg.showAlert(parent, errMsg);
        }
        return result;
    }

    public void updateContent(Editor.EditorListener agent) {
        getMediaBagElement().setData(null);
        fireEditorDataChanged(null);
    }

    public boolean changeFileName(String newFileName, Editor.EditorListener agent, Messages msg) {
        boolean result = false;
        String errMsg = null;
        String oldFileName = getMediaBagElement().getFileName();
        FileSystem fs = getMediaBag().getProject().getFileSystem();
        newFileName = fs.stdFn(newFileName.trim());
        Component parent = (agent instanceof Component) ? (Component) agent : null;
        if (oldFileName.equals(newFileName)) {
        } else if (Utils.getFileType(oldFileName) != Utils.getFileType(newFileName)) {
            errMsg = "edit_media_chfile_different";
        } else if (newFileName.length() < 1) errMsg = "edit_media_chfile_invalid"; else if (getMediaBag().getElementByFileName(newFileName) != null) {
            if (msg != null && parent != null) {
                msg.showAlert(parent, new String[] { msg.get("edit_media_exists_1"), newFileName, msg.get("edit_media_exists_2") });
            }
        } else {
            getMediaBagElement().setFileName(newFileName);
            result = true;
            fireEditorDataChanged(agent);
        }
        if (errMsg != null && msg != null && parent != null) {
            msg.showAlert(parent, errMsg);
        }
        return result;
    }

    public String toString() {
        return getName();
    }

    protected void saveData() {
    }

    protected boolean delete(boolean changeSelection) {
        MediaBagEditor mbe = (MediaBagEditor) getEditorParent();
        boolean result = super.delete(changeSelection);
        if (result && mbe != null) {
            result = mbe.getMediaBag().removeElement(getMediaBagElement());
            mbe.fireEditorDataChanged(null);
        }
        return result;
    }

    protected boolean canClone() {
        return false;
    }

    protected void setActionsFlag() {
        allowDelete = true;
        allowCut = false;
        allowCopy = false;
        allowPaste = false;
    }

    public void setActionsOwner() {
        setActionsFlag();
        super.setActionsOwner();
        if (actionsCreated) {
            testMediaBagElementAction.setActionOwner(this);
        }
    }

    public void clearActionsOwner() {
        super.clearActionsOwner();
        if (actionsCreated) {
            testMediaBagElementAction.setActionOwner(null);
        }
    }

    public static Icon getIcon() {
        if (icon == null) icon = edu.xtec.util.ResourceManager.getImageIcon("icons/movie.gif");
        return icon;
    }

    public Icon getIcon(boolean leaf, boolean expanded) {
        return getIcon();
    }

    public boolean canBeParentOf(Editor e) {
        return false;
    }

    public boolean canBeSiblingOf(Editor e) {
        return (e instanceof MediaBagElementEditor);
    }

    public JClicProjectEditor getProjectEditor() {
        return (JClicProjectEditor) getFirstParent(JClicProjectEditor.class);
    }

    public static void createActions(Options opt) {
        createBasicActions(opt);
        if (!actionsCreated) {
            testMediaBagElementAction = new EditorAction("edit_media_preview", "icons/media_view.gif", "edit_media_preview_tooltip", opt) {

                protected void doAction(Editor e) {
                    EditorPanel ep = getEditorPanelSrc();
                    if (ep != null && e instanceof MediaBagElementEditor) {
                        ((MediaBagElementEditor) e).testMedia(ep, ep.getOptions());
                    }
                }
            };
            actionsCreated = true;
        }
    }

    public String getDescription(Options options) {
        MediaBagElement mbe = getMediaBagElement();
        StringBuffer sb = new StringBuffer();
        String fileName = mbe.getFileName();
        if (fileName != null) {
            int type = Utils.getFileType(fileName);
            sb.append(options.getMsg(Utils.TYPE_CODES[type]));
            if (type == Utils.TYPE_IMAGE) {
                if (mbe.animated) sb.append(" ").append(options.getMsg("ftype_animated"));
                try {
                    Image img = mbe.getImage();
                    if (img != null) {
                        sb.append(" (").append(img.getWidth(null)).append("x").append(img.getHeight(null)).append(")");
                    }
                } catch (Exception ex) {
                    sb.append(" - ").append(options.getMsg("ERROR"));
                    System.err.println("Error reading image " + fileName);
                }
            }
        }
        return sb.substring(0);
    }

    public long getFileSize() {
        MediaBagElement mbe = getMediaBagElement();
        long result = -1L;
        if (mbe != null) {
            String fileName = mbe.getFileName();
            if (fileName != null && getMediaBag() != null) {
                try {
                    result = getMediaBag().getProject().getFileSystem().getFileLength(fileName);
                } catch (Exception ex) {
                    System.err.println("ERROR getting the size of " + fileName);
                }
            }
        }
        return result;
    }

    public boolean nameChanged(int type, String oldName, String newName) {
        boolean result = false;
        if ((type & Constants.T_MEDIA) != 0 && oldName.equals(getMediaBagElement().getName())) {
            getMediaBagElement().setName(newName);
            setModified(true);
            result = true;
        }
        return result;
    }

    public Vector listReferences() {
        Vector result = null;
        MediaBag mb = getMediaBag();
        if (mb != null) {
            HashMap hm = new HashMap();
            mb.listReferencesTo(getName(), Constants.MEDIA_OBJECT, hm);
            result = new Vector(hm.keySet());
        }
        allowDelete = (result == null || result.size() == 0);
        deleteAction.setEnabled(allowDelete);
        return result;
    }
}
