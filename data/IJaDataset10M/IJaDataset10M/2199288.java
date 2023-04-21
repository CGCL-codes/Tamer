package preprocessing.PreprocessingWizzard;

import preprocessing.methods.Preprocessor;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: Jun 13, 2008
 * Time: 12:45:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface WizardCallback {

    JFrame getFramePtr();

    public enum MethodTypes4W {

        LoadMethods, MissingValuesTreatmentMethods, DataReductionMethods
    }

    public void allowNext(boolean allow);

    public void allowBack(boolean allow);

    public void setErrorStatusText(String text);

    public void setOKStatusText(String text);

    public Preprocessor[] getListOfMethods(MethodTypes4W methodType);

    public Preprocessor getMethod(String name);
}
