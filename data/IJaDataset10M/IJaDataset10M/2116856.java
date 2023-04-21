package GUI.Forms.TabFactory;

import Entities.Estimate.*;
import Entities.*;
import GUI.Forms.BarrierForm;
import GUI.Forms.Util.FormTitle;
import GUI.Forms.ReferenceLevelForm;
import GUI.Forms.Tabbable;
import GUI.Forms.TargetForm;
import Utility.HelpTexts.Help;
import Utility.HelpTexts.QuperChapters;
import java.util.Vector;

/**
 * A factory which creates tabs with forms for the Estimation carrying Entities.
 * @author pontuslp
 */
public class EstimationContainingTabFactory implements FormTabFactory {

    /**
	 * The different types this factory manages.
	 */
    public static enum Type {

        barrier, referencelevel, target
    }

    Type type;

    Vector<Tabbable> prefilled;

    /**
	 * Tab factory for tabbed form in a new quality.
	 * @param type the type of the tabbables/data
	 */
    public EstimationContainingTabFactory(Type type) {
        this.type = type;
        prefilled = new Vector<Tabbable>();
    }

    /**
	 * Tab factory with precreated tabs for the data in the supplied quality
	 * @param type the type of the tabbables/data
	 * @param quality quality to be prefilled into tabs
	 */
    public EstimationContainingTabFactory(Type type, Quality quality) {
        this.type = type;
        prefilled = new Vector<Tabbable>();
        switch(type) {
            case barrier:
                {
                    for (Barrier barrier : quality.getBarriers()) {
                        prefilled.add(new BarrierForm(barrier));
                    }
                    break;
                }
            case referencelevel:
                {
                    for (ReferenceLevel referenceLevel : quality.getReferenceLevels()) {
                        prefilled.add(new ReferenceLevelForm(referenceLevel));
                    }
                    break;
                }
            case target:
                {
                    for (Target target : quality.getTargets()) {
                        prefilled.add(new TargetForm(target));
                    }
                    break;
                }
        }
    }

    /**
	 * Creates a new Tabbable form.
	 * @return
	 */
    public Tabbable create() {
        switch(type) {
            case barrier:
                {
                    return new BarrierForm();
                }
            case referencelevel:
                {
                    return new ReferenceLevelForm();
                }
            case target:
                {
                    return new TargetForm();
                }
        }
        return null;
    }

    /**
	 * Returns the prefilled Tabbables created in the second constructor.
	 * @return prefilled Tabbables
	 */
    public Vector<Tabbable> getPrefilled() {
        return prefilled;
    }

    /**
	 * Returns the title associated with this Factorys type.
	 */
    public FormTitle getTitle() {
        switch(type) {
            case barrier:
                {
                    return new FormTitle("Estimate Cost Barriers", Help.getShortHTML(QuperChapters.QuperChapter.barrier));
                }
            case referencelevel:
                {
                    return new FormTitle("Identify Reference Levels", Help.getShortHTML(QuperChapters.QuperChapter.ref));
                }
            case target:
                {
                    return new FormTitle("Define Targets", Help.getShortHTML(QuperChapters.QuperChapter.target));
                }
        }
        return null;
    }
}
