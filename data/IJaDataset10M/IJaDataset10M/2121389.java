package com.openthinks.woms.report.service;

import java.math.BigDecimal;
import java.util.Collection;
import com.openthinks.woms.report.ColorReport;
import com.openthinks.woms.report.ColorReportItem;
import com.openthinks.woms.report.dao.ColorReportDao;

public class ColorReportServiceImpl implements ColorReportService {

    private ColorReportDao colorReportDao;

    public void setColorReportDao(ColorReportDao colorReportDao) {
        this.colorReportDao = colorReportDao;
    }

    @Override
    public ColorReport analyzeColor(String accountId, String brand) throws Exception {
        ColorReport colorReport = new ColorReport();
        Collection<ColorReportItem> items = colorReportDao.countColor(accountId, brand);
        colorReport.setColorReportItems(items);
        int amountSum = 0;
        int quantitySum = 0;
        for (ColorReportItem item : items) {
            amountSum += item.getAmount();
            quantitySum += item.getQuantity();
        }
        BigDecimal hundred = new BigDecimal("100");
        for (ColorReportItem item : items) {
            item.setQuantityPercentage(new BigDecimal(item.getQuantity()).divide(new BigDecimal(quantitySum), 3, BigDecimal.ROUND_HALF_UP).multiply(hundred).doubleValue());
            item.setAmountPercentage(new BigDecimal(item.getAmount()).divide(new BigDecimal(amountSum), 3, BigDecimal.ROUND_HALF_UP).multiply(hundred).doubleValue());
        }
        return colorReport;
    }
}
