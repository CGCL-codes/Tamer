package lablayer.model.MathComponents;

import lablayer.model.LabData;

/**
 * Component #26
 * 
 * Median of array
 */
class Median implements MathComponent {

    private LabData result;

    private LabData data;

    private String componentName = "Медиана";

    private String componentId = "26";

    public Median() {
        super();
        this.result = new LabData();
    }

    public void calculate() {
        Double[][][] data = this.data.getData();
        Double[] row = null;
        if (data.length >= 1) {
            if (data[0].length >= 1) {
                row = data[0][0];
            }
        }
        try {
            if (row != null) {
                int test = row.length / 2;
                if ((test * 2) != row.length) {
                    this.result.setData(row[((row.length + 1) / 2) - 1]);
                } else {
                    this.result.setData((row[(row.length / 2) - 1] + row[(row.length / 2)]) / 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getComponentId() {
        return componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public LabData getData() {
        return data;
    }

    public void setData(LabData data) {
        this.data = data;
    }

    public LabData getResult() {
        return result;
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
