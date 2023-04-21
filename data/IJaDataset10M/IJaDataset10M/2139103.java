package org.fao.fenix.domain.info.pattern.cropproduction;

import javax.persistence.Entity;
import org.fao.fenix.domain.info.dataset.NumericCommodityDataset;
import org.fao.fenix.domain.upload.parsing.ColumnNames;

/** */
@Entity
@ColumnNames(columnNames = { "Area_Code", "Area_lbl", "Commodity_Code", "Commodity_lbl", "Indicator_Code", "Indicator_label", "TimeSeries_code", "TimeSeries_lbl", "value", "mu_label", "precision" })
public class CropProductionDataset extends NumericCommodityDataset {
}
