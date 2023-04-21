package cn.edu.buaa.nlsde.grid.Chart;

public class AxisBuild {

    public String metric;

    public float xAxis;

    public float yAxis;

    public String collectTime;

    public AxisBuild() {
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getMetric() {
        return this.metric;
    }

    public float getxAxis() {
        return this.xAxis;
    }

    public float getyAxis() {
        return this.yAxis;
    }

    public String getCollectTime() {
        return this.collectTime;
    }
}
