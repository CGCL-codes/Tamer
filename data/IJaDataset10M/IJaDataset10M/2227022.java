package edu.ucla.stat.SOCR.analyses.ri;

import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.analyses.model.*;
import edu.ucla.stat.SOCR.analyses.ri.*;

public class RIAnalysisType {

    Data data = null;

    short analysisType = -1;

    boolean useRI = false;

    Result result = null;

    public RIAnalysisType() {
    }

    public static String lookup(short type) throws WrongAnalysisException {
        String analysisClass = "edu.ucla.stat.SOCR.analyses.ri.";
        System.out.println("RIAnalysisType  lookup analysisClass = " + analysisClass);
        switch(type) {
            case AnalysisType.SIMPLE_LINEAR_REGRESSION:
                analysisClass = analysisClass + "LinearModel";
                break;
            case AnalysisType.MULTI_LINEAR_REGRESSION:
                analysisClass = analysisClass + "LinearModel";
                break;
            case AnalysisType.ANOVA_ONE_WAY:
                analysisClass = analysisClass + "AnovaOneWay";
                break;
            case AnalysisType.ANOVA_TWO_WAY:
                analysisClass = analysisClass + "AnovaTwoWay";
                break;
            case AnalysisType.ONE_T:
                analysisClass = analysisClass + "OneT";
                break;
            case AnalysisType.TWO_INDEPENDENT_T:
                analysisClass = analysisClass + "TwoIndependentT";
                break;
            case AnalysisType.TWO_INDEPENDENT_WILCOXON:
                analysisClass = analysisClass + "TwoIndependentWilcoxon";
                break;
            case AnalysisType.TWO_PAIRED_T:
                analysisClass = analysisClass + "TwoPairedT";
                break;
            case AnalysisType.TWO_PAIRED_SIGNED_RANK:
                analysisClass = analysisClass + "TwoPairedSignedRank";
                break;
            case AnalysisType.TWO_PAIRED_SIGN_TEST:
                analysisClass = analysisClass + "TwoPairedSignTest";
                break;
            default:
                analysisClass = null;
        }
        System.out.println("In AnalysisType after switch analysisClass = " + analysisClass);
        if (analysisClass == null) {
            System.out.println("RIAnalysisType WrongAnalysisException");
            throw new WrongAnalysisException("The analysis requestged does not exist.");
        }
        return analysisClass;
    }

    public Result getResult() throws Exception {
        if (useRI) {
            ;
        } else {
            ;
        }
        return null;
    }

    public String getResultXML() {
        return null;
    }
}
