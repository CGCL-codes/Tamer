package sourceagile.planning.client.tasks;

import sourceagile.client.ProjectInitialization;
import sourceagile.shared.entities.entry.ClassFile;
import com.google.gwt.user.client.ui.ListBox;

public class FoldersList extends ListBox {

    public FoldersList() {
        this.setWidth("250px");
        String folderName = "";
        this.addItem(folderName, folderName);
        for (ClassFile classFile : ProjectInitialization.projectEntries.values()) {
            if (classFile.getFeature() != null) {
                String featureFolder = classFile.getFeature().getFeatureFolder();
                if (!featureFolder.equals(folderName)) {
                    folderName = classFile.getFeature().getFeatureFolder();
                    this.addItem(folderName, classFile.getFilePath());
                }
            }
        }
    }
}
