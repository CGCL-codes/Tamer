package ru.glance.excelmaster;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StyleManager {

    private MultiKeyMap map;

    private HSSFWorkbook wb;

    public HSSFWorkbook getWb() {
        return wb;
    }

    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }

    public StyleManager() {
        map = new MultiKeyMap();
    }

    public void setStyle(HSSFSheet sheet, int rowPos, int cellPos, ProxyStyle style) {
        map.put(sheet, rowPos, cellPos, style);
    }

    public void setStyle(HSSFSheet sheet, int topRow, int leftCell, int bottomRow, int rightCell, ProxyStyle style) {
        for (int rowPos = topRow; rowPos <= bottomRow; rowPos++) {
            for (int cellPos = leftCell; cellPos <= rightCell; cellPos++) {
                map.put(sheet, rowPos, cellPos, style);
            }
        }
    }

    public void mergeStyle(HSSFSheet sheet, int topRow, int leftCell, int bottomRow, int rightCell, ProxyStyle style, String... options) {
        ProxyStyle oldStyle;
        ProxyStyle newStyle;
        for (int rowPos = topRow; rowPos <= bottomRow; rowPos++) {
            for (int cellPos = leftCell; cellPos <= rightCell; cellPos++) {
                oldStyle = (ProxyStyle) map.get(sheet, rowPos, cellPos);
                if (oldStyle == null) {
                    newStyle = new ProxyStyle();
                } else {
                    newStyle = oldStyle.clone();
                }
                newStyle.mergeOptions(style, options);
                map.put(sheet, rowPos, cellPos, newStyle);
            }
        }
    }

    /**
     * ����������� ����������� �����, ������ �������� � ��������� �� �������.
     *
     */
    @SuppressWarnings("unchecked")
    public void apply() {
        HashMap<ProxyStyle, HSSFCellStyle> optMap = new HashMap<ProxyStyle, HSSFCellStyle>();
        for (Entry<MultiKey, ProxyStyle> entry : (Set<Entry<MultiKey, ProxyStyle>>) map.entrySet()) {
            MultiKey key = entry.getKey();
            HSSFSheet sheet = (HSSFSheet) key.getKey(0);
            int rowPos = (Integer) key.getKey(1);
            int cellPos = (Integer) key.getKey(2);
            ProxyStyle pstyle = entry.getValue();
            HSSFCellStyle style = optMap.get(pstyle);
            if (style == null) {
                style = wb.createCellStyle();
                ProxyStyle.mergeOptions(style, pstyle);
                optMap.put(pstyle, style);
            }
            Composer.getCell(sheet, rowPos, cellPos).setCellStyle(style);
        }
    }
}
