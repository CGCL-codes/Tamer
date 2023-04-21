package listo.client.dialogs.controllers;

import com.google.inject.Inject;
import listo.client.ContextManager;
import listo.client.Convert;
import listo.utils.swing.AutoCompleter;
import listo.client.dialogs.autocompletion.FoldersCompleter;
import listo.client.model.Context;
import listo.client.model.Folder;
import listo.client.model.ObjectId;
import listo.client.model.Task;
import listo.utils.guice.ManuallyScoped;
import org.apache.commons.lang.StringUtils;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ManuallyScoped("TaskDialogScope")
public class TaskFoldersController extends TextComponentController<Task> implements ComponentController.ModelChangedListener {

    private final FoldersCompleter completer;

    private final Context currentContext;

    @Inject
    public TaskFoldersController(FoldersCompleter completer, ContextManager contextManager) {
        this.completer = completer;
        this.currentContext = contextManager.getCurrentContext();
    }

    public void setDialogComponent(JDialog dialog, JComponent component) {
        super.setDialogComponent(dialog, component);
        new AutoCompleter(completer).decorate(dialog, (JTextComponent) component);
    }

    protected void updateView(boolean active) {
        StringBuilder sb = new StringBuilder();
        if (modelObject.hasFolders()) {
            for (ObjectId folderId : modelObject.getFolders()) {
                Folder folder = currentContext.getFolder(folderId);
                if (sb.length() > 0) sb.append(", ");
                sb.append(folder.getPathName());
            }
        }
        if (sb.length() > 0) {
            textComponent.setForeground(SystemColor.textText);
        } else {
            sb.append(active ? "" : "None");
            textComponent.setForeground(active ? SystemColor.textText : SystemColor.textInactiveText);
        }
        setText(sb.toString());
    }

    protected void extractFromString(String string) {
        if (StringUtils.isEmpty(string)) {
            modelObject.setFolderIds();
            return;
        }
        Set<Folder> folders = new CopyOnWriteArraySet<Folder>();
        for (String folderName : StringUtils.split(string, ", ")) {
            folderName = folderName.trim();
            if (StringUtils.isNotEmpty(folderName) && !StringUtils.contains(folderName, ' ')) {
                Folder folder = currentContext.getFolderByPathName(folderName);
                if (folder != null) {
                    folders.add(folder);
                    continue;
                }
            }
            modelObject.setFolderIds();
            return;
        }
        modelObject.setFolderIds(Convert.toObjectIds(folders.toArray(new Folder[folders.size()])));
    }

    public boolean isComponentValid() {
        return modelObject.hasFolders();
    }

    public void modelChanged(ComponentController... controllers) {
        updateView(false);
        if (controllers[0] instanceof TaskQuickEntryController) {
            fireModelChanged(controllers);
        }
    }
}
