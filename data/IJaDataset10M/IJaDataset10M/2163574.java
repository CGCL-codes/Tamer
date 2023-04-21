package cn.cublog.drunkedcat.roughset;

public class DataForUse {

    public String title;

    public String xLabel;

    public String yLabel;

    public String description;

    public String markDesc;

    public String markPos;

    public boolean differentColor;

    public boolean percent;

    public double bound;

    public double zero;

    public DrawAttribute[] values;

    public DataForUse() {
        title = "in_title";
        xLabel = "in_xLabel";
        yLabel = "in_yLabel好";
        description = "in_desc";
        markDesc = "mark";
        differentColor = false;
        percent = false;
        bound = 60D;
        zero = 0.0000000000020002D;
        values = new DrawAttribute[12];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new DrawAttribute("x" + i, i * 10.0D);
        }
        markPos = values[0].name;
    }
}
