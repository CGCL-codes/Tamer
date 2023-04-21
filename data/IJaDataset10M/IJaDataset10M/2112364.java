package kr.ac.ssu.imc.whitehole.report.designer.items.rdfunctions;

import java.math.*;

public class RDMINROWFunction extends RDFunctionSet {

    /**
   * ����
   * @param md ResultsModel
   * @param fdm FuncDataModel
   */
    public RDMINROWFunction(String fn, String ft, int sci) {
        super(fn, ft, sci);
    }

    public String[] processFunc(String[] inputArray) {
        String[] CookedDatas = new String[inputArray.length];
        double minVal = new Double(inputArray[0]).doubleValue();
        for (int i = 0; i < inputArray.length; i++) {
            try {
                Double arg = new Double(inputArray[i]);
                minVal = Math.min(minVal, arg.doubleValue());
            } catch (Exception e) {
                CookedDatas[i] = inputArray[i];
            }
        }
        for (int i = 0; i < CookedDatas.length; i++) {
            CookedDatas[i] = new BigDecimal(minVal).toString();
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
        String[] result = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            RDMINFunction maxFunc = new RDMINFunction(" ", " ", -1);
            String[] maxValA = maxFunc.processFunc(input[i]);
            result[i] = maxValA[0];
        }
        return result;
    }
}
