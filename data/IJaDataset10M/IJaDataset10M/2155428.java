package org.sf.pkb.util;

import java.util.HashMap;

public class FontUtil {

    /**
 * Font PT, PX, EM, % convertor
 */
    private static String[][] m_font_size_table = new String[][] { { "6pt", "8px", "0.5em", "50%" }, { "7pt", "9px", "0.55em", "55%" }, { "7.5pt", "10px", "0.625em", "62.5%" }, { "8pt", "11px", "0.7em", "70%" }, { "9pt", "12px", "0.75em", "75%" }, { "10pt", "13px", "0.8em", "80%" }, { "10.5pt", "14px", "0.875em", "87.5%" }, { "11pt", "15px", "0.95em", "95%" }, { "12pt", "16px", "1em", "100%" }, { "13pt", "17px", "1.05em", "105%" }, { "13.5pt", "18px", "1.125em", "112.5%" }, { "14pt", "19px", "1.2em", "120%" }, { "14.5pt", "20px", "1.25em", "125%" }, { "15pt", "21px", "1.3em", "130%" }, { "16pt", "22px", "1.4em", "140%" }, { "17pt", "23px", "1.45em", "145%" }, { "18pt", "24px", "1.5em", "150%" }, { "20pt", "26px", "1.6em", "160%" }, { "22pt", "29px", "1.8em", "180%" }, { "24pt", "32px", "2em", "200%" }, { "26pt", "35px", "2.2em", "220%" }, { "27pt", "36px", "2.25em", "225%" }, { "28pt", "37px", "2.3em", "230%" }, { "29pt", "38px", "2.35em", "235%" }, { "30pt", "40px", "2.45em", "245%" }, { "32pt", "42px", "2.55em", "255%" }, { "34pt", "45px", "2.75em", "275%" }, { "36pt", "48px", "3em", "300%" } };

    private static HashMap<String, String[]> m_convert_map = null;

    public final int UNIT_PT = 0;

    public final int UNIT_PX = 1;

    public final int UNIT_EM = 2;

    public final int UNIT_PERCENTAGE = 3;

    public String convert(String src, int unit) {
        if (m_convert_map == null) {
            m_convert_map = new HashMap<String, String[]>();
            for (int i = 0; i < m_font_size_table.length; i++) {
                for (int j = 0; j < m_font_size_table[i].length; j++) {
                    m_convert_map.put(m_font_size_table[i][j], m_font_size_table[i]);
                }
            }
        }
        try {
            String[] values = (String[]) m_convert_map.get(src);
            if (values != null && (unit <= 3) && (unit >= 0)) {
                return values[unit];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return src;
    }
}
