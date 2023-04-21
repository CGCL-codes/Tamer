package lablayer.model.MathComponents;

import lablayer.model.LabData;

class MinValue implements MathComponent {

    private LabData result;

    private LabData data;

    private String componentName = "Минимальный элемент выборки";

    private String componentId = "12";

    @Override
    public LabData getData() {
        return this.data;
    }

    @Override
    public void calculate() {
        try {
            Double[][] data = (this.data.getData())[0];
            Double[][] result = new Double[data.length][];
            for (int i = 0; i < data.length; i++) {
                Double[] row = data[i];
                Double minValue = row[0];
                for (int j = 1; j < row.length; j++) {
                    if (minValue > row[j]) minValue = row[j];
                }
                result[i] = new Double[1];
                result[i][0] = minValue;
            }
            Double[][][] ret = new Double[1][][];
            ret[0] = result;
            this.result = new LabData(ret);
        } catch (Exception e) {
        }
    }

    @Override
    public void setData(LabData data) {
        try {
            assert data != null : "";
            this.data = data;
        } catch (NullPointerException e) {
        }
    }

    @Override
    public String getComponentId() {
        return this.componentId;
    }

    @Override
    public String getComponentName() {
        return this.componentName;
    }

    @Override
    public LabData getResult() {
        return this.result;
    }

    @Override
    public void setParameter(String params, String values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LabData getInitData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
