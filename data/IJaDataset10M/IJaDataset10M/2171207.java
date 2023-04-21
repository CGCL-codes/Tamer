package atp.reporter.data;

import atp.reporter.items.RAlign;
import atp.reporter.product.*;

public interface RDataItemHolder {

    public RDataItem[] getDataItems();

    public RCellMaker getCellMaker();

    public RAlign getAlign();
}
