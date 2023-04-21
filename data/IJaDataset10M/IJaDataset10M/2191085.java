package freestyleLearning.homeCore.learningUnitsManager.data.xmlBindingSubclasses;

import java.util.Iterator;
import freestyleLearning.homeCore.learningUnitsManager.data.xmlBinding.LearningUnitsDescriptor;

public class FSLLearningUnitsDescriptor extends LearningUnitsDescriptor {

    private boolean modified;

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public FSLLearningUnitDescriptor getDescriptorById(String id) {
        FSLLearningUnitDescriptor tmpLearningUnitDescriptor;
        Iterator iterator = this.getLearningUnitsDescriptors().iterator();
        while (iterator.hasNext()) {
            tmpLearningUnitDescriptor = (FSLLearningUnitDescriptor) iterator.next();
            if (tmpLearningUnitDescriptor.getId().equals(id)) return tmpLearningUnitDescriptor;
        }
        return null;
    }
}
