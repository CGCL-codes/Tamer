package kr.ac.ssu.imc.durubi.report.viewer.components.functions;

/**FunctionSet Ŭ������ ��� �޾� �ҹ���ȭ �Լ������� ������ Ŭ����*/
public class DRLOWERCASEFunction extends DRFunctionSet {

    /**
	 * ����
	 * @param md ResultsModel
	 * @param fdm FuncDataModel
	 */
    public DRLOWERCASEFunction(String fn, String ft, int sci) {
        super(fn, ft, sci);
    }

    /**
   	 * FunctionSet�� abstract method processFunc()�� �ҹ���ȭ �Լ��������� ����
   	 */
    public String[] processFunc(String[] inputArray) {
        String[] CookedDatas = new String[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            try {
                CookedDatas[i] = inputArray[i].toLowerCase();
            } catch (Exception e) {
                CookedDatas[i] = inputArray[i];
            }
        }
        return CookedDatas;
    }

    public String processFunc(String inputVal) {
        String CookedData;
        try {
            CookedData = inputVal.toLowerCase();
        } catch (Exception e) {
            CookedData = inputVal;
        }
        return CookedData;
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
