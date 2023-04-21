package org.cleartk.timeml.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.simple.SimpleFeatureExtractor;
import com.google.common.base.Joiner;

/**
 * <br>
 * Copyright (c) 2011, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Steven Bethard
 */
public class TimeWordsExtractor implements SimpleFeatureExtractor {

    private Map<String, Set<String>> groupedWords;

    public TimeWordsExtractor() {
        this.groupedWords = new HashMap<String, Set<String>>();
        this.groupedWords.put("Now", new HashSet<String>(Arrays.asList("now", "current", "currently")));
        this.groupedWords.put("TimeOfDay", new HashSet<String>(Arrays.asList("morning", "noon", "afternoon", "evening", "night", "midnight")));
        this.groupedWords.put("Day", new HashSet<String>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "today", "tomorrow", "yesterday")));
        this.groupedWords.put("Month", new HashSet<String>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")));
        this.groupedWords.put("Season", new HashSet<String>(Arrays.asList("spring", "summer", "fall", "autumn", "winter")));
        this.groupedWords.put("TimeDuration", new HashSet<String>(Arrays.asList("minute", "minutes", "second", "seconds", "hour", "hours")));
        this.groupedWords.put("DateDuration", new HashSet<String>(Arrays.asList("day", "days", "week", "weeks", "month", "months", "quarter", "quarters", "season", "seasons", "year", "years", "decade", "decades", "century", "centuries")));
    }

    @Override
    public List<Feature> extract(JCas view, Annotation focusAnnotation) {
        List<String> types = new ArrayList<String>();
        String[] words = focusAnnotation.getCoveredText().split("\\W+");
        for (String word : words) {
            for (String group : this.groupedWords.keySet()) {
                if (this.groupedWords.get(group).contains(word)) {
                    types.add(group);
                }
            }
            if (word.matches("^\\d{4}$")) {
                types.add("Year");
            }
        }
        if (types.isEmpty()) {
            for (String word : words) {
                for (String group : this.groupedWords.keySet()) {
                    if (this.groupedWords.get(group).contains(word.toLowerCase())) {
                        types.add(group + "Lower");
                    }
                }
            }
        }
        if (types.isEmpty()) {
            types.add("None");
        }
        return Arrays.asList(new Feature("TimeType", Joiner.on('_').join(types)));
    }
}
