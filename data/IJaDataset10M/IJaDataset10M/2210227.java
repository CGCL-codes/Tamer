package kr.ac.ssu.imc.whitehole.report.viewer.rdobjects.rdfunctions;

import java.math.*;

public class RDMAXFunction extends RDFunctionSet {

    /**
     * ����
     * @param md ResultsModel
     * @param fdm FuncDataModel
     */
    public RDMAXFunction(String fn, String ft, int sci) {
        super(fn, ft, sci);
    }

    public String[] processFunc(String[] inputArray) {
        String[] CookedDatas = new String[inputArray.length];
        double maxVal = new Double(inputArray[0]).doubleValue();
        for (int i = 0; i < inputArray.length; i++) {
            try {
                Double arg = new Double(inputArray[i]);
                maxVal = Math.max(maxVal, arg.doubleValue());
            } catch (Exception e) {
                CookedDatas[i] = inputArray[i];
            }
        }
        for (int i = 0; i < CookedDatas.length; i++) {
            CookedDatas[i] = new BigDecimal(maxVal).toString();
        }
        return CookedDatas;
    }

    /**��ȿ�� ����*/
    public String processFunc(String inputVal) {
        return inputVal;
    }

    /**��ȿ�� ����*/
    public String processFunc(String inputVal1, String inputVal2) {
        return inputVal1;
    }

    /**��ȿ�� ����*/
    public String[] processFunc(String[] inputArray1, String[] inputArray2) {
        return inputArray1;
    }

    /**��ȿ�� ����*/
    public String processFunc(String[] inputArray, String inputVal1, String inputVal2) {
        return inputVal1;
    }

    public String[] processFunc(String[][] input) {
        return input[0];
    }
}
