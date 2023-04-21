package de.uni_leipzig.lots.webfrontend.formbeans;

import de.uni_leipzig.lots.webfrontend.formbeans.property.PersistentLongIdProperty;
import org.jetbrains.annotations.Nullable;

/**
 * @author Alexander Kiel
 * @version $Id: DraftCreateForm.java,v 1.8 2007/10/23 06:29:43 mai99bxd Exp $
 */
public class DraftCreateForm extends AutoValidateFormSupport {

    protected PersistentLongIdProperty trainingPaperFolderId = new PersistentLongIdProperty();

    public DraftCreateForm() {
        registerProperty("id", trainingPaperFolderId);
        trainingPaperFolderId.setRequired(true);
    }

    @Nullable
    public String getTrainingPaperFolderId() {
        return trainingPaperFolderId.getValue();
    }

    public void setTrainingPaperFolderId(String trainingPaperFolderId) {
        this.trainingPaperFolderId.setValue(trainingPaperFolderId);
    }
}
