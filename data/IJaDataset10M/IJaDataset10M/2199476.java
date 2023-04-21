package reconcile.scorers;

import java.util.HashMap;
import reconcile.data.Annotation;
import reconcile.data.AnnotationSet;
import reconcile.data.Document;
import reconcile.features.FeatureUtils;
import reconcile.features.properties.HeadNoun;
import reconcile.features.properties.Property;
import reconcile.general.Constants;
import reconcile.general.Utils;

public class Matcher {

    public enum MatchStyleEnum {

        MUC, ACE, UW
    }

    public static int numMatchedKey = 0;

    public static int totalKey = 0;

    public static int totalNPsMatched = 0;

    public static int doubleMatches = 0;

    public static void exactMatchAnnotationSets(AnnotationSet gsNps, AnnotationSet nps) {
        for (Annotation a : gsNps) {
            a.setProperty(Property.MATCHED_CE, Integer.parseInt(a.getAttribute("ID")));
        }
        for (Annotation a : nps) {
            a.setProperty(Property.MATCHED_CE, Integer.parseInt(a.getAttribute("ID")));
        }
    }

    public static void matchAnnotationSets(AnnotationSet gsNps, AnnotationSet nps, MatchStyleEnum matchStyle, Document doc) {
        matchAnnotationSets(gsNps, nps, matchStyle, doc, true);
    }

    public static void matchAnnotationSets(AnnotationSet gsNps, AnnotationSet nps, MatchStyleEnum matchStyle, Document doc, boolean outputStats) {
        int numMatched = 0;
        HashMap<Annotation, Annotation> matched = new HashMap<Annotation, Annotation>();
        for (Annotation a : nps.getOrderedAnnots()) {
            Annotation match;
            switch(matchStyle) {
                case MUC:
                    match = matchAnnotationMUCStyle(a, gsNps, doc);
                    break;
                case ACE:
                    match = matchAnnotationACEStyle(a, gsNps, doc);
                    break;
                case UW:
                    match = matchAnnotationUWStyle(a, gsNps, doc);
                    break;
                default:
                    match = matchAnnotationACEStyle(a, gsNps, doc);
                    break;
            }
            if (match != null) {
                numMatched++;
                if (matched.containsKey(match)) {
                    doubleMatches++;
                    Annotation oldMatch = matched.get(match);
                    if (outputStats) {
                        System.out.println("Double match " + doc.getAnnotText(match));
                        System.out.println("Matches: [" + doc.getAnnotText(matched.get(match)) + "]" + "] [" + doc.getAnnotText(a) + "]");
                    }
                    boolean conjOldMatch = FeatureUtils.memberArray("and", doc.getWords(oldMatch));
                    boolean conjNewMatch = FeatureUtils.memberArray("and", doc.getWords(a));
                    if ((oldMatch.getLength() >= a.getLength() && (!conjOldMatch || conjNewMatch)) || (conjNewMatch && !conjOldMatch)) {
                        a.setProperty(Property.MATCHED_CE, -1);
                    } else {
                        oldMatch.setProperty(Property.MATCHED_CE, -1);
                        match.setProperty(Property.MATCHED_CE, Integer.parseInt(a.getAttribute(Constants.CE_ID)));
                        a.setProperty(Property.MATCHED_CE, Integer.parseInt(match.getAttribute(Constants.CE_ID)));
                        matched.put(match, a);
                    }
                    if (outputStats) {
                        System.out.println("Resolved to: [" + doc.getAnnotText(matched.get(match)) + "]");
                    }
                } else {
                    matched.put(match, a);
                    match.setProperty(Property.MATCHED_CE, Integer.parseInt(a.getAttribute(Constants.CE_ID)));
                    a.setProperty(Property.MATCHED_CE, Integer.parseInt(match.getAttribute(Constants.CE_ID)));
                }
            } else {
                a.setProperty(Property.MATCHED_CE, -1);
            }
        }
        numMatchedKey += matched.size();
        int gsNpsSize = gsNps == null ? 0 : gsNps.size();
        totalKey += gsNpsSize;
        totalNPsMatched += numMatched;
        if (outputStats) {
            System.out.println("Matched KEY: " + matched.size() + "/" + gsNpsSize + " CEs. RESPONSE: " + numMatched + "/" + nps.size() + " CEs");
        }
        if (Constants.DEBUG && gsNps != null) {
            for (Annotation a : gsNps) {
                if (!matched.containsKey(a)) {
                    System.err.println("Not matched key: " + doc.getAnnotText(a) + " -- " + a.getAttribute("ID"));
                }
            }
        }
    }

    public static Annotation matchAnnotation(Annotation a, Document doc, AnnotationSet key) {
        Annotation match = null;
        AnnotationSet posannotations = doc.getAnnotationSet(Constants.POS);
        AnnotationSet overlapCoref = key.getOverlapping(a);
        if (overlapCoref != null) {
            for (Annotation cur : overlapCoref) {
                if (cur.compareSpan(a) == 0) {
                    match = cur;
                    break;
                }
                String min = cur.getAttribute("MIN");
                String[] mins;
                if (min == null) {
                    mins = new String[] { doc.getAnnotString(cur) };
                } else {
                    mins = min.split("\\|");
                }
                if (cur.covers(a)) {
                    Annotation headNoun = HeadNoun.getValue(a, doc);
                    String head = doc.getAnnotString(headNoun);
                    if (FeatureUtils.memberArray(head, mins)) {
                        match = cur;
                        break;
                    }
                    String npString = doc.getAnnotString(a).toLowerCase().replaceAll("\n", " ").replaceAll("\\A\\W", "");
                    for (String m : mins) {
                        if (npString.endsWith(m.toLowerCase())) {
                            match = cur;
                            break;
                        }
                    }
                    String headString = doc.getAnnotString(a.getStartOffset(), headNoun.getEndOffset()).toLowerCase().replaceAll("\n", " ").replaceAll("\\A\\W", "");
                    for (String m : mins) {
                        if (headString.endsWith(m.toLowerCase())) {
                            match = cur;
                            break;
                        }
                        if (headNoun.compareSpan(HeadNoun.getValue(cur, doc)) == 0 && npString.contains(m.toLowerCase())) {
                            match = cur;
                            break;
                        }
                    }
                }
                if (match == null && a.getEndOffset() == cur.getEndOffset() || HeadNoun.getValue(a, doc).getEndOffset() == cur.getEndOffset()) {
                    AnnotationSet diff;
                    if (a.covers(cur)) {
                        diff = posannotations.getContained(a.getStartOffset(), cur.getStartOffset());
                    } else {
                        diff = posannotations.getContained(cur.getStartOffset(), a.getStartOffset());
                    }
                    boolean same = true;
                    for (Annotation d : diff) {
                        if (!(d.getType().startsWith("JJ") || d.getType().startsWith("RB") || d.getType().startsWith("CD") || d.getType().equals("DT") || d.getType().matches("\\W+"))) {
                            same = false;
                        }
                    }
                    if (same) {
                        match = cur;
                        break;
                    }
                } else {
                    String npString = doc.getAnnotText(a).toLowerCase().replaceAll("\n", " ").replaceAll("\\A\\W", "");
                    for (String m : mins) {
                        if (npString.endsWith(m.toLowerCase())) {
                            match = cur;
                            break;
                        }
                    }
                    if (match == null) {
                        npString = doc.getAnnotString(a.getStartOffset(), HeadNoun.getValue(a, doc).getEndOffset()).toLowerCase().replaceAll("\n", " ").replaceAll("\\A\\W", "");
                        for (String m : mins) {
                            if (npString.endsWith(m.toLowerCase())) {
                                match = cur;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return match;
    }

    public static Annotation matchAnnotationMUCStyle(Annotation a, AnnotationSet gsNps, Document doc) {
        Annotation match = null;
        if (gsNps != null) {
            AnnotationSet overlapCoref = gsNps.getOverlapping(a);
            if (overlapCoref != null) {
                for (Annotation cur : overlapCoref) {
                    if (cur.compareSpan(a) == 0) {
                        match = cur;
                        break;
                    }
                    String min = cur.getAttribute("MIN");
                    String[] mins;
                    if (min == null || min.length() < 1) {
                        mins = new String[] { doc.getAnnotString(cur) };
                    } else {
                        mins = min.split("\\|");
                    }
                    if (coversAnnot(cur, a, doc.getText())) {
                        if (a.covers(cur)) {
                            match = cur;
                            break;
                        }
                        String respString = doc.getAnnotString(a).toLowerCase().replaceAll("\n", " ").replaceAll("\\A", "");
                        for (String m : mins) {
                            if (respString.contains(m.toLowerCase())) {
                                match = cur;
                                break;
                            }
                        }
                    }
                    if (match == null) {
                        String diff;
                        if (a.getStartOffset() < cur.getStartOffset()) {
                            diff = doc.getAnnotString(a.getStartOffset(), cur.getStartOffset());
                        } else {
                            diff = doc.getAnnotString(cur.getStartOffset(), a.getStartOffset());
                        }
                        boolean same = true;
                        String[] words = FeatureUtils.getWords(diff);
                        if (diff != null && diff.length() > 0) {
                            for (String s : words) {
                                if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                                    same = false;
                                }
                            }
                        }
                        if (same) {
                            if (a.getEndOffset() < cur.getEndOffset()) {
                                diff = doc.getAnnotString(a.getEndOffset(), cur.getEndOffset());
                            } else {
                                diff = doc.getAnnotString(cur.getEndOffset(), a.getEndOffset());
                            }
                            words = FeatureUtils.getWords(diff);
                            if (diff != null && diff.length() > 0) {
                                for (String s : words) {
                                    if (!FeatureUtils.memberArray(s, POSTMODIFIERS)) {
                                        same = false;
                                    }
                                }
                            }
                        }
                        if (same) {
                            match = cur;
                            break;
                        }
                    }
                }
            }
        }
        return match;
    }

    public static Annotation matchAnnotationACEStyle(Annotation a, AnnotationSet gsNps, Document doc) {
        Annotation match = null;
        AnnotationSet overlapCoref = gsNps.getOverlapping(a);
        if (overlapCoref != null) {
            for (Annotation cur : overlapCoref) {
                if (cur.compareSpan(a) == 0) {
                    match = cur;
                    break;
                }
                String preDifference, postDifference;
                if (a.getStartOffset() < cur.getStartOffset()) {
                    preDifference = doc.getAnnotString(a.getStartOffset(), cur.getStartOffset());
                } else {
                    preDifference = doc.getAnnotString(cur.getStartOffset(), a.getStartOffset());
                }
                if (a.getEndOffset() < cur.getEndOffset()) {
                    postDifference = doc.getAnnotString(a.getEndOffset(), cur.getEndOffset());
                } else {
                    postDifference = doc.getAnnotString(cur.getEndOffset(), a.getEndOffset());
                }
                boolean preSame, postSame;
                if (preDifference.matches("\\W*")) {
                    preSame = true;
                } else {
                    preSame = true;
                    String[] words = FeatureUtils.getWords(preDifference);
                    if (preDifference.length() > 0) {
                        for (String s : words) {
                            if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                                preSame = false;
                            }
                        }
                    }
                }
                if (postDifference.matches("\\W*")) {
                    postSame = true;
                } else {
                    postSame = true;
                    String[] words = FeatureUtils.getWords(postDifference);
                    if (postDifference.length() > 0) {
                        for (String s : words) {
                            if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                                postSame = false;
                            }
                        }
                    }
                }
                if (preSame && postSame) {
                    match = cur;
                    break;
                }
                int headStart = Integer.parseInt(cur.getAttribute(Constants.HEAD_START));
                int headEnd = Integer.parseInt(cur.getAttribute(Constants.HEAD_END));
                Annotation head = HeadNoun.getValue(a, doc);
                boolean headMatch = false;
                if (head.getEndOffset() == headEnd) {
                    if (head.getStartOffset() == headStart) {
                        headMatch = true;
                    } else if (head.getStartOffset() >= headStart) {
                        headMatch = true;
                        Annotation gsHead = HeadNoun.getValue(cur, doc);
                        if (gsHead.compareSpan(head) == 0) {
                            headMatch = true;
                        }
                    }
                }
                if (headMatch) {
                    String[] words = FeatureUtils.getWords(preDifference);
                    if (preDifference.length() < 1 || !FeatureUtils.memberArray("and", words)) {
                        if (headEnd == cur.getEndOffset()) {
                            match = cur;
                            break;
                        } else {
                            if (!doc.getAnnotString(headEnd, cur.getEndOffset()).trim().startsWith("and")) {
                                match = cur;
                                break;
                            } else {
                            }
                        }
                    }
                }
            }
        }
        return match;
    }

    public static Annotation matchAnnotationUWStyle(Annotation a, AnnotationSet gsNps, Document doc) {
        Annotation match = null;
        AnnotationSet overlapCoref = gsNps.getOverlapping(a);
        if (overlapCoref != null) {
            for (Annotation cur : overlapCoref) {
                if (cur.compareSpan(a) == 0) {
                    match = cur;
                    break;
                }
                String preDifference, postDifference;
                if (a.getStartOffset() < cur.getStartOffset()) {
                    preDifference = doc.getAnnotString(a.getStartOffset(), cur.getStartOffset());
                } else {
                    preDifference = doc.getAnnotString(cur.getStartOffset(), a.getStartOffset());
                }
                if (a.getEndOffset() < cur.getEndOffset()) {
                    postDifference = doc.getAnnotString(a.getEndOffset(), cur.getEndOffset());
                } else {
                    postDifference = doc.getAnnotString(cur.getEndOffset(), a.getEndOffset());
                }
                boolean preSame, postSame;
                if (preDifference.matches("\\W*")) {
                    preSame = true;
                } else {
                    preSame = true;
                    String[] words = FeatureUtils.getWords(preDifference);
                    if (preDifference.length() > 0) {
                        for (String s : words) {
                            if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                                preSame = false;
                            }
                        }
                    }
                }
                if (postDifference.matches("\\W*")) {
                    postSame = true;
                } else {
                    postSame = true;
                    String[] words = FeatureUtils.getWords(postDifference);
                    if (postDifference.length() > 0) {
                        for (String s : words) {
                            if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                                postSame = false;
                            }
                        }
                    }
                }
                if (preSame && postSame) {
                    match = cur;
                    break;
                }
                String min = cur.getAttribute("min");
                if (min != null && min.length() > 1) {
                    String[] minSpan = min.split(",");
                    int minStart = Integer.parseInt(minSpan[0]);
                    int minEnd = Integer.parseInt(minSpan[1]);
                    if (a.compareSpan(minStart, minEnd) == 0) {
                        match = cur;
                        break;
                    }
                    if (a.getStartOffset() < minStart) {
                        preDifference = doc.getAnnotString(a.getStartOffset(), minStart);
                    } else {
                        preDifference = doc.getAnnotString(minStart, a.getStartOffset());
                    }
                    if (a.getEndOffset() < minEnd) {
                        postDifference = doc.getAnnotString(a.getEndOffset(), minEnd);
                    } else {
                        postDifference = doc.getAnnotString(minEnd, a.getEndOffset());
                    }
                    preSame = false;
                    postSame = false;
                    if (preDifference.matches("\\W*")) {
                        preSame = true;
                    } else {
                        preSame = true;
                        String[] words = FeatureUtils.getWords(preDifference);
                        if (preDifference.length() > 0) {
                            for (String s : words) {
                                if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                                    preSame = false;
                                }
                            }
                        }
                    }
                    if (postDifference.matches("\\W*")) {
                        postSame = true;
                    } else {
                        postSame = true;
                        String[] words = FeatureUtils.getWords(postDifference);
                        if (postDifference.length() > 0) {
                            for (String s : words) {
                                if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                                    postSame = false;
                                }
                            }
                        }
                    }
                    if (preSame && postSame) {
                        match = cur;
                        break;
                    }
                }
                String headTxt = cur.getAttribute("head");
                if (headTxt != null && headTxt.length() > 1) {
                    String[] headSpan = headTxt.split(",");
                    int headStart = Integer.parseInt(headSpan[0]);
                    int headEnd = Integer.parseInt(headSpan[1]);
                    Annotation head = HeadNoun.getValue(a, doc);
                    boolean headMatch = false;
                    if (head.getEndOffset() == headEnd) {
                        if (head.getStartOffset() == headStart) {
                            headMatch = true;
                        } else if (head.getStartOffset() >= headStart) {
                            headMatch = true;
                            Annotation gsHead = HeadNoun.getValue(cur, doc);
                            if (gsHead.compareSpan(head) == 0) {
                                headMatch = true;
                            }
                        }
                    }
                    if (headMatch) {
                        String[] words = FeatureUtils.getWords(preDifference);
                        if (preDifference.length() < 1 || !FeatureUtils.memberArray("and", words)) {
                            if (headEnd == cur.getEndOffset()) {
                                match = cur;
                                break;
                            } else {
                                if (!doc.getAnnotString(headEnd, cur.getEndOffset()).trim().startsWith("and")) {
                                    match = cur;
                                    break;
                                } else {
                                }
                            }
                        }
                    }
                }
            }
        }
        return match;
    }

    private static boolean coversAnnot(Annotation bigger, Annotation smaller, String text) {
        String diff;
        boolean coversStart = true;
        if (bigger.getStartOffset() <= smaller.getStartOffset()) {
            coversStart = true;
        } else {
            diff = Utils.getAnnotText(smaller.getStartOffset(), bigger.getStartOffset(), text);
            String[] words = FeatureUtils.getWords(diff);
            if (diff != null && diff.length() > 0) {
                for (String s : words) {
                    if (!FeatureUtils.memberArray(s, PREMODIFIERS)) {
                        coversStart = false;
                    }
                }
            }
        }
        if (!coversStart) return false;
        boolean coversEnd = true;
        if (smaller.getEndOffset() <= bigger.getEndOffset()) {
            coversEnd = true;
        } else {
            diff = Utils.getAnnotText(bigger.getEndOffset(), smaller.getEndOffset(), text);
            String[] words = FeatureUtils.getWords(diff);
            if (diff != null && diff.length() > 0) {
                for (String s : words) {
                    if (!FeatureUtils.memberArray(s, POSTMODIFIERS)) {
                        coversEnd = false;
                    }
                }
            }
        }
        if (!coversEnd) return false;
        return true;
    }

    public static void nullifyCounters() {
        numMatchedKey = 0;
        totalKey = 0;
        totalNPsMatched = 0;
        doubleMatches = 0;
    }

    private static String[] PREMODIFIERS = { "A", "AN", "THE" };

    private static String[] POSTMODIFIERS = { ";", "'", ",", "." };
}
