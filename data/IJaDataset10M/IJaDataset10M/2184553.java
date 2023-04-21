package external.libsvm;

public class svm_model implements java.io.Serializable {

    svm_parameter param;

    int nr_class;

    int l;

    svm_node[][] SV;

    double[][] sv_coef;

    double[] rho;

    double[] probA;

    double[] probB;

    int[] label;

    int[] nSV;
}

;
