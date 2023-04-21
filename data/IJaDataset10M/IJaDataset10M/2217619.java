package moduledefault.partition.kfold;

import moduledefault.evaluationmetric.accuracy.*;
import javax.swing.JDialog;

/**
 *
 * @author evaristowb
 */
public class FacadeKFoldPartion {

    private static KFoldPartionModule kFoldPartionModule;

    public FacadeKFoldPartion() {
    }

    /**
     * returns the kFold partion module
     * @return
     */
    public static KFoldPartionModule getHoudoutPartionModule() {
        return kFoldPartionModule;
    }

    /**
     * sets the Houdout partion module
     * @param holdoutPartionModule
     */
    public static void setHoudoutPartionModule(KFoldPartionModule holdoutPartionModule) {
        FacadeKFoldPartion.kFoldPartionModule = holdoutPartionModule;
    }

    /**
     * returns the kFold painel configuration
     * @return
     */
    public static JDialog getJDialogConfig() {
        return kFoldPartionModule.getJDialogConfig();
    }

    /**
     * returns the k
     * @return
     */
    public static int getK() {
        return kFoldPartionModule.getK();
    }

    /**
     * sets the k
     * @param k
     */
    public static void setK(int k) {
        kFoldPartionModule.setK(k);
    }
}
