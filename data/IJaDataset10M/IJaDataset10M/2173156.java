package net.sf.doolin.app.sc.common.stats;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class Stats {

    private final Map<Integer, Map<String, BigDecimal>> values = new HashMap<Integer, Map<String, BigDecimal>>();

    public void addValue(String axis, int year, BigDecimal value) {
        Map<String, BigDecimal> yearValues = this.values.get(year);
        if (yearValues == null) {
            yearValues = new HashMap<String, BigDecimal>();
            this.values.put(year, yearValues);
        }
        yearValues.put(axis, value);
    }

    public String getExport() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(new BufferedWriter(sw));
        try {
            for (Map.Entry<Integer, Map<String, BigDecimal>> axisEntry : this.values.entrySet()) {
                int year = axisEntry.getKey();
                Map<String, BigDecimal> axisValues = axisEntry.getValue();
                for (Map.Entry<String, BigDecimal> valueEntry : axisValues.entrySet()) {
                    String axis = valueEntry.getKey();
                    BigDecimal value = valueEntry.getValue();
                    pw.format(Locale.UK, "%s:%d:%.2f%n", axis, year, value.doubleValue());
                }
            }
        } finally {
            pw.close();
        }
        return sw.toString();
    }

    public Map<Integer, Map<String, BigDecimal>> getRawValues() {
        return this.values;
    }

    public void setExport(String stats) {
        this.values.clear();
        stats = StringUtils.trim(stats);
        if (StringUtils.isNotBlank(stats)) {
            String[] lines = StringUtils.split(stats, "\n\r");
            for (String line : lines) {
                line = StringUtils.trim(line);
                String[] lineTokens = StringUtils.split(line, ":");
                String axis = lineTokens[0];
                int year = Integer.parseInt(lineTokens[1], 10);
                BigDecimal value = new BigDecimal(lineTokens[2]);
                addValue(axis, year, value);
            }
        }
    }
}
