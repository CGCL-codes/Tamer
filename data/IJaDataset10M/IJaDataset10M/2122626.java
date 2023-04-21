package com.tecacet.jflat;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import jxl.read.biff.BiffException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.junit.Test;
import com.tecacet.jflat.excel.ExcelReader;
import com.tecacet.jflat.om.StockPrice;

public class ExcelReaderTest {

    @Test
    public void testReadAll() throws BiffException, IOException {
        ReaderRowMapper<StockPrice> rowMapper = new BeanReaderRowMapper<StockPrice>(StockPrice.class, new String[] { "date", "openPrice", "closePrice", "volume" }, new String[] { "Date", "Open", "Close", "Volume" });
        DateConverter dateConverter = new DateConverter(null);
        dateConverter.setPattern("yyyy-MM-dd");
        ConvertUtils.register(dateConverter, Date.class);
        ExcelReader reader = new ExcelReader("testdata/prices.xls", rowMapper);
        List<StockPrice> prices = reader.readAll();
        assertEquals(253, prices.size());
        StockPrice price = prices.get(0);
        assertEquals(1550.87, price.getOpenPrice(), 0.0001);
        assertEquals(1577.03, price.getClosePrice(), 2);
        assertEquals(1521220000, price.getVolume());
        reader.readWithCallback(new FlatFileReaderCallback() {

            @Override
            public void processRow(int rowIndex, String[] tokens, Object bean) {
            }
        });
    }
}
