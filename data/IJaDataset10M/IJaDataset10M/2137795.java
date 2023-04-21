package com.gusto.engine.semsim.measures.impl;

import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;
import com.gusto.engine.semsim.measures.ValueSimilarity;
import com.gusto.engine.semsim.measures.exception.SimilarityException;
import com.gusto.engine.semsim.measures.utils.DateDiff;
import com.gusto.engine.semsim.measures.utils.DateDiff.Diff;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;

public class DateIntervalValueSimilarity implements ValueSimilarity {

    private Logger log = Logger.getLogger(DateIntervalValueSimilarity.class);

    private Map<String, Double> intervals;

    public void setIntervals(Map<String, Double> intervals) {
        this.intervals = intervals;
    }

    private String unit;

    public void setUnit(String unit) throws SimilarityException {
        if (unit.equals("day") || unit.equals("month") || unit.equals("year")) {
            this.unit = unit;
        } else {
            throw new SimilarityException("unit '" + unit + "' must be one of (day|month|year)");
        }
    }

    public String getId() {
        String id = "[" + getClass().getName() + "|" + unit + "|";
        id += intervals + "]";
        return id;
    }

    public Double getSimilarity(Literal value1, Literal value2) throws SimilarityException {
        try {
            Date date1 = ((XSDDateTime) value1.getValue()).asCalendar().getTime();
            Date date2 = ((XSDDateTime) value2.getValue()).asCalendar().getTime();
            DateDiff dd = new DateDiff();
            Diff d = dd.getDiff(date1, date2);
            Double diff = null;
            if (unit != null) {
                if (unit.equals("day")) {
                    diff = new Double(d.years * 365 + d.months * 30 + d.days);
                } else if (unit.equals("month")) {
                    diff = new Double(d.years * 12 + d.months);
                } else if (unit.equals("year")) {
                    diff = new Double(d.years);
                }
                log.debug("Difference is '" + diff + "' in '" + unit + "'");
            }
            try {
                for (String key : intervals.keySet()) {
                    Double from = Double.parseDouble(key.substring(0, key.indexOf("-")));
                    Double to = Double.parseDouble(key.substring(key.indexOf("-") + 1));
                    boolean inInterval = (diff >= from) && (diff < to);
                    if (inInterval) {
                        log.debug("Difference '" + diff + "' in interval '" + from + "'-'" + to + "'");
                        log.info("Similarity = '" + intervals.get(key) + "'");
                        return intervals.get(key);
                    }
                }
            } catch (Exception ex) {
                throw new SimilarityException("Error in configuration");
            }
            return 0.0;
        } catch (Exception ex) {
            throw new SimilarityException("This similarity is only applicable on Date values");
        }
    }
}
