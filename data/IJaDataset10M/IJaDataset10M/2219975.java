package org.ddsteps.examples.petclinic.pet;

import junit.framework.Test;
import org.ddsteps.examples.petclinic.PetclinicWebBrowserTestCase;
import org.ddsteps.examples.petclinic.pet.executors.ExecuteAddPet;
import org.ddsteps.examples.petclinic.pet.navigators.NavigateToAddPet;
import org.ddsteps.examples.petclinic.pet.validators.ValidatePetForm;
import org.ddsteps.examples.petclinic.pet.validators.ValidatePetOnOwnerPage;
import org.ddsteps.examples.petclinic.pet.validators.ValidatePetRow;
import org.ddsteps.junit.suite.DDStepsSuiteFactory;
import org.ddsteps.testcase.support.DDStepsFixtureSpringTestCase;

/**
 * @author adam
 * @version $Id: PetFTest.java,v 1.2 2006/11/28 19:53:43 adamskogman Exp $
 */
public class PetFTest extends PetclinicWebBrowserTestCase {

    public static Test suite() {
        return DDStepsSuiteFactory.createSuite(PetFTest.class);
    }

    protected NavigateToAddPet nav;

    protected ValidatePetForm valForm;

    protected ExecuteAddPet exeAddPet;

    protected ValidatePetOnOwnerPage valOwnerPage;

    protected ValidatePetRow valRow;

    /**
	 * @throws Exception
	 */
    public void testAddPet_Ok() throws Exception {
        nav.runStep();
        valForm.runStep();
        exeAddPet.runStep();
        valOwnerPage.runStep();
        valRow.runStep();
    }

    /**
	 * @return Returns the exeAddPet.
	 */
    public ExecuteAddPet getExeAddPet() {
        return exeAddPet;
    }

    /**
	 * @return Returns the nav.
	 */
    public NavigateToAddPet getNav() {
        return nav;
    }

    /**
	 * @return Returns the valForm.
	 */
    public ValidatePetForm getValForm() {
        return valForm;
    }

    /**
	 * @return Returns the valOwnerPage.
	 */
    public ValidatePetOnOwnerPage getValOwnerPage() {
        return valOwnerPage;
    }

    /**
	 * @return Returns the valRow.
	 */
    public ValidatePetRow getValRow() {
        return valRow;
    }

    /**
	 * @param exeAddPet
	 *            The exeAddPet to set.
	 */
    public void setExeAddPet(ExecuteAddPet exeAddPet) {
        this.exeAddPet = exeAddPet;
    }

    /**
	 * @param nav
	 *            The nav to set.
	 */
    public void setNav(NavigateToAddPet nav) {
        this.nav = nav;
    }

    /**
	 * @param valForm
	 *            The valForm to set.
	 */
    public void setValForm(ValidatePetForm valForm) {
        this.valForm = valForm;
    }

    /**
	 * @param valOwnerPage
	 *            The valOwnerPage to set.
	 */
    public void setValOwnerPage(ValidatePetOnOwnerPage valOwnerPage) {
        this.valOwnerPage = valOwnerPage;
    }

    /**
	 * @param valRow
	 *            The valRow to set.
	 */
    public void setValRow(ValidatePetRow valRow) {
        this.valRow = valRow;
    }
}
