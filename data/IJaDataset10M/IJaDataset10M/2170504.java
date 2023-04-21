package net.ramponi.perfmeter;

public interface GraphModel {

    public String[] getHeader();

    public String[] getFooter();

    public int getCapacity();

    public int getSamples();

    public int getSeries();

    public String getCategory();

    public long getTotal();

    public GraphModelManager getManager();

    public String getSerieLegend(int serieIndex);

    public float[] getSerie(int serieIndex);

    public float getMaxSample(int serieIndex);

    public float getMax();

    public float getScale(int from, int to);

    public void addTableModelListener(GraphModelListener l);

    public void removeTableModelListener(GraphModelListener l);
}
