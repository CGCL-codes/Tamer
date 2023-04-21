package com.sri.emo.wizard.branch;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import com.jcorporate.expresso.core.db.DBException;
import com.sri.emo.dbobj.WizDefinition;
import com.sri.emo.wizard.AbstractWizard;
import com.sri.emo.wizard.Wizard;
import com.sri.emo.wizard.WizardMonitor;
import com.sri.emo.wizard.selection.DisplaySummaryPage;
import com.sri.emo.wizard.selection.EmoSelectionWizardFactory;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class EmoBranchingWizardFactory extends EmoSelectionWizardFactory {

    public EmoBranchingWizardFactory() {
    }

    /**
     * Constructs the wizard itself.
     *
     * @param definition the WizDefinition dbobject for this wizard.
     * @param steps      the List of steps that were constructed previously in
     *                   the constructSteps function.
     * @return fully constructed Wizard instance.
     * @throws DBException upon database exception error.
     */
    protected Wizard constructWizard(final WizDefinition definition, final List branchNodes) throws DBException {
        String wizardClassString = definition.getWizardClass();
        Class wizardClass;
        try {
            wizardClass = Thread.currentThread().getContextClassLoader().loadClass(wizardClassString);
            if (!BranchingWizard.class.isAssignableFrom(wizardClass)) {
                throw new IllegalArgumentException("If you do not" + " implement a wizard" + " that derives from SequentialWizard and uses the same " + "constructor then you need to implement your own" + " Wizard Factory implementation");
            }
            Constructor c = wizardClass.getConstructor(new Class[] { WizardMonitor.class, BranchNode[].class });
            AbstractWizard emoWizard = (AbstractWizard) c.newInstance(new Object[] { constructMonitor(), (BranchNode[]) branchNodes.toArray(new BranchNode[branchNodes.size()]) });
            emoWizard.setId(new Integer(definition.getId()));
            emoWizard.setTitle(definition.getWizName());
            emoWizard.setSummary(definition.getSummary());
            setWizardForStepsThatNeedIt(emoWizard, branchNodes);
            return emoWizard;
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Unknown class: " + wizardClassString + " edit your wizard definition to solve this problem");
        } catch (InvocationTargetException ex) {
            throw new DBException("An Exception was thrown constructing the " + "wizard", ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("The wizard specified: " + wizardClassString + " does not have a public constructor" + " like the default.  You may need to implement your own " + "wizard factory.");
        } catch (InstantiationException ex) {
            throw new DBException("There was an error constructing your " + "wizard.", ex);
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("No appropriate constructor" + " found:" + ex.getMessage());
        }
    }

    private void setWizardForStepsThatNeedIt(final Wizard constructedWizard, final List steps) {
        for (Iterator i = steps.iterator(); i.hasNext(); ) {
            BranchNode oneStep = (BranchNode) i.next();
            if (oneStep.getPage() instanceof DisplaySummaryPage) {
                ((DisplaySummaryPage) oneStep.getPage()).setMyOwner(constructedWizard);
            }
        }
    }
}
