package defaultApplication;

import model.facade.FacadeHdd;
import model.facade.FacadePreferences;
import model.facade.FacadeEigenaar;
import view.ViewHoofdScherm;

/**
 * @author Chris, Bart, Wannes
 *
 */
public class Application {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        FacadeHdd facadeHdd = new FacadeHdd();
        FacadeEigenaar facadeStudent = new FacadeEigenaar();
        FacadePreferences facadePreferences = new FacadePreferences();
        ViewHoofdScherm HoofdScherm = new ViewHoofdScherm(facadeHdd, facadeStudent, facadePreferences);
        HoofdScherm.setVisible(true);
    }
}
