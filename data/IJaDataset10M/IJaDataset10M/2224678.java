package prequips.ui.ms;

public interface AnalysisElementListTableViewFilter {

    public String getAttributeName();

    public String getFilterName();

    public void setAttributeName(String name);

    public void setFilterName(String name);

    public boolean isNegationActive();

    public void setNegationActive(boolean negationActive);

    public boolean isInherited();

    public void setInherited(boolean inherited);

    public void addFilterListener(AnalysisElementListTableViewFilterListener listener);

    public void removeFilterListener(AnalysisElementListTableViewFilterListener listener);

    public void fireFilterChanged(AnalysisElementListTableViewFilterEvent.Id id);

    public MetaViewerFilter getMetaViewerFilter();

    public void setMetaViewerFilter(MetaViewerFilter metaFilter);

    public AnalysisElementListTableViewFilter copy();
}
