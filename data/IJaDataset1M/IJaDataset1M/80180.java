package com.google.typography.font.tools.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Font.Builder;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMap;
import com.google.typography.font.sfntly.table.core.CMap.CMapFormat;
import com.google.typography.font.sfntly.table.core.CMapFormat4;
import com.google.typography.font.sfntly.table.core.CMapTable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Raph Levien
 */
public class RenumberingCMapTableSubsetter extends TableSubsetterImpl {

    public RenumberingCMapTableSubsetter() {
        super(Tag.cmap);
    }

    private static CMapFormat4 getCMapFormat4(Font font) {
        CMapTable cmapTable = font.getTable(Tag.cmap);
        for (CMap cmap : cmapTable) {
            if (cmap.format() == CMapFormat.Format4.value()) {
                return (CMapFormat4) cmap;
            }
        }
        return null;
    }

    static Map<Integer, Integer> computeMapping(Subsetter subsetter, Font font) {
        CMapFormat4 cmap4 = getCMapFormat4(font);
        if (cmap4 == null) {
            throw new RuntimeException("CMap format 4 table in source font not found");
        }
        Map<Integer, Integer> inverseMapping = subsetter.getInverseMapping();
        Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();
        for (Integer unicode : cmap4) {
            int glyph = cmap4.glyphId(unicode);
            if (inverseMapping.containsKey(glyph)) {
                mapping.put(unicode, inverseMapping.get(glyph));
            }
        }
        return mapping;
    }

    @Override
    public boolean subset(Subsetter subsetter, Font font, Builder fontBuilder) throws IOException {
        CMapTableBuilder cmapBuilder = new CMapTableBuilder(fontBuilder, computeMapping(subsetter, font));
        cmapBuilder.build();
        return true;
    }
}
