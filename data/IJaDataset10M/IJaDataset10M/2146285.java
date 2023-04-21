package freestyleLearning.homeCore.learningUnitsManager.data.xmlBindingSubclasses;

import java.util.Iterator;
import javax.xml.bind.IdentifiableElement;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.Validator;
import freestyleLearning.homeCore.learningUnitsManager.data.xmlBinding.LearningUnitDescriptor;

public class FSLLearningUnitDescriptor extends LearningUnitDescriptor implements Comparable {

    public boolean getFolder() {
        if (hasFolder()) {
            return super.getFolder();
        }
        return false;
    }

    /** Returns the Learning Unit as a string reprasentation for this object. */
    public String toString() {
        return getTitle();
    }

    public int compareTo(Object o) throws ClassCastException {
        if (!(o instanceof FSLLearningUnitDescriptor)) throw new ClassCastException("Incompatible Types");
        FSLLearningUnitDescriptor object = (FSLLearningUnitDescriptor) o;
        if (!object.getId().equals(this.getId())) throw new ClassCastException("incompatible ID �s");
        System.out.println("vgl. " + this.getVersion() + " mit " + object.getVersion());
        return (this.getVersion().compareTo(object.getVersion()));
    }
}
