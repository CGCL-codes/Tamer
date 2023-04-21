package org.fao.fenix.birt.sds;

import org.fao.fenix.domain.info.content.NumericCommodity;

public class CropProductionSds {

    public NumericCommodity[] getCropProductionList() {
        NumericCommodity n1 = new NumericCommodity();
        n1.setCommodityCode("casa");
        n1.setValue(34.21);
        NumericCommodity n2 = new NumericCommodity();
        n2.setCommodityCode("prato");
        n2.setValue(32.1);
        NumericCommodity[] cropArray = new NumericCommodity[2];
        cropArray[0] = n1;
        cropArray[1] = n2;
        return cropArray;
    }
}
