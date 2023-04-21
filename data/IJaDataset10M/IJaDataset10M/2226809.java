package uchicago.src.sim.analysis;

/**
 * Interface for data sources for gui created charts.
 *
 * @version $Revision: 1.4 $ $Date: 2004/11/03 19:51:00 $
 */
public interface GuiChartDataSource {

    /**
   * Returns the name of this data source prefixed by its source. For example,
   * MyModel.getData
   */
    public String getFullName();

    /**
   * Returns a copy of this GuiChartDataSource.
   */
    public GuiChartDataSource copy();

    /**
   * Returns the name of this data source.
   */
    public String getShortName();

    /**
   * Returns an XML representation of this GuiChartDataSource.
   */
    public String toXML();
}
