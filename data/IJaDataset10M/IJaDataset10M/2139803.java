package net.sf.zorobot.util;

import java.util.*;
import java.text.*;
import net.sf.zorobot.ext.db.KanjiDAO;

public class SentenceAnalyzer {

    public static String col[] = { "04", "07", "03", "10", "12", "06", "05", "09", "13", "14", "02", "15" };

    public static DecimalFormat pf = new DecimalFormat("##0%");

    public static String[] analyze(KanjiDAO myData, MorphologicalAnalyzer myExt, String a, String b, double thres, int type) {
        ArrayList chasenResult = myExt.extractWord(a);
        String wordA[] = new String[chasenResult.size()];
        String owordA[] = new String[chasenResult.size()];
        boolean isParticleA[] = new boolean[chasenResult.size()];
        for (int i = 0; i < wordA.length; i++) {
            ExtractedWord ew = (ExtractedWord) chasenResult.get(i);
            wordA[i] = ew.word;
            owordA[i] = ew.original;
            isParticleA[i] = ew.isParticle;
        }
        String spaceA[] = new String[chasenResult.size() + 1];
        int end = 0;
        for (int i = 0; i < wordA.length; i++) {
            ExtractedWord wrd = ((ExtractedWord) chasenResult.get(i));
            spaceA[i] = a.substring(end, wrd.startIdx);
            end = wrd.endIdx;
        }
        spaceA[wordA.length] = a.substring(end);
        ArrayList buffA = new ArrayList(wordA.length * 5 - 4);
        for (int j = 0; j < wordA.length; j++) {
            if (!isParticleA[j]) {
                for (int k = minimum(j + 6, wordA.length); k > j; k--) {
                    if (!isParticleA[k - 1] || (isParticleA[k - 1] && k - 1 > j)) {
                        StringBuilder sb = new StringBuilder();
                        for (int p = j; p < k - 1; p++) {
                            sb.append(owordA[p]);
                        }
                        sb.append(owordA[k - 1]);
                        String str = sb.toString();
                        buffA.add(new SourceSegment(j, k, str, StringUtility.wordLetterPairs(str)));
                        if (!owordA[k - 1].equals(wordA[k - 1])) {
                            sb = new StringBuilder();
                            for (int p = j; p < k - 1; p++) {
                                sb.append(owordA[p]);
                            }
                            sb.append(wordA[k - 1]);
                            str = sb.toString();
                            buffA.add(new SourceSegment(j, k, str, StringUtility.wordLetterPairs(str)));
                        }
                    }
                }
            }
        }
        String wordB[] = b.split("[^\\&\\$A-Za-z0-9]+");
        if (wordB.length > 0) {
            if (wordB[0].equals("")) {
                String temp[] = wordB;
                wordB = new String[temp.length - 1];
                for (int i = 0; i < temp.length - 1; i++) {
                    wordB[i] = temp[i + 1];
                }
            }
        }
        String spaceB[] = b.split("[\\&\\$A-Za-z0-9]+");
        TreeSet segments = new TreeSet();
        int countB = 0;
        ArrayList buffB = new ArrayList(wordB.length * 5 - 4);
        for (int j = 0; j < wordB.length; j++) {
            for (int k = j + 1; k <= minimum(j + 5, wordB.length); k++) {
                StringBuffer sb = new StringBuffer();
                for (int p = j; p < k; p++) {
                    if (p > j) sb.append(" ");
                    sb.append(wordB[p]);
                }
                buffB.add(StringUtility.wordLetterPairs(sb.toString()));
            }
        }
        for (int i = 0; i < buffA.size(); i++) {
            SourceSegment sg = (SourceSegment) buffA.get(i);
            ArrayList meanA = null;
            meanA = myData.getWordAndMeaningFromEdict(sg.segment);
            meanA.addAll(myData.getWordAndMeaningFromCustomdict(sg.segment));
            boolean importantCase = false;
            if (meanA.size() > 0) MeaningExpander.expand(sg.segment, meanA); else {
                meanA = SimpleDict.analyze(sg.segment, myData);
                importantCase = true;
            }
            for (int m = 0; m < meanA.size(); m++) {
                String meaningA = ((String[]) (meanA.get(m)))[2].replace('\'', ' ');
                ArrayList pairs1 = StringUtility.wordLetterPairs(meaningA);
                int countPos = 0;
                for (int j = 0; j < wordB.length; j++) {
                    for (int k = j + 1; k <= minimum(j + 5, wordB.length); k++) {
                        ArrayList pairs2 = (ArrayList) ((ArrayList) buffB.get(countPos++)).clone();
                        double score = StringUtility.compareStrings(pairs1, pairs2, importantCase) - 0.00001 * (25 - sg.end + sg.start) - 0.0000001 * (5 - k + j) - 0.000000001 * m;
                        int mlen = pairs1.size();
                        if (mlen > pairs2.size()) mlen = pairs2.size();
                        if (mlen <= 3) {
                            score = Math.pow(score, 1 + 0.75 * (4 - mlen));
                        }
                        if (k - j == 1) {
                            if (meaningA.length() == wordB[j].length()) {
                                char[] aaa = meaningA.toCharArray();
                                char[] bbb = wordB[j].toCharArray();
                                int diff = 0;
                                for (int n = 0; n < aaa.length; n++) {
                                    if (aaa[n] >= 'a') aaa[n] -= 0x0020;
                                    if (bbb[n] >= 'a') bbb[n] -= 0x0020;
                                    if (aaa[n] != bbb[n]) diff++;
                                }
                                if (diff == 1) {
                                    if (myData.isMeaningInEdict(wordB[j])) {
                                        score *= 0.6;
                                    }
                                }
                            }
                        }
                        if (score >= thres) segments.add(new ExSegment(j, k, sg.start, sg.end, score, (String[]) meanA.get(m)));
                    }
                }
            }
        }
        Iterator iter = segments.iterator();
        int flagA[] = new int[wordA.length];
        for (int i = 0; i < wordA.length; i++) flagA[i] = -1;
        int flagB[] = new int[wordB.length];
        for (int i = 0; i < wordB.length; i++) flagB[i] = -1;
        int counter = 0;
        ArrayList segList = new ArrayList();
        while (iter.hasNext()) {
            if (counter >= wordA.length) break;
            ExSegment e = (ExSegment) iter.next();
            boolean canTake = true;
            for (int i = e.startSourceIdx; i < e.endSourceIdx; i++) {
                if (flagA[i] >= 0) {
                    canTake = false;
                    break;
                }
            }
            if (canTake) {
                for (int i = e.startIdx; i < e.endIdx; i++) {
                    if (flagB[i] >= 0) {
                        canTake = false;
                        break;
                    }
                }
                if (canTake) {
                    segList.add(e);
                    for (int i = e.startSourceIdx; i < e.endSourceIdx; i++) flagA[i] = counter;
                    for (int i = e.startIdx; i < e.endIdx; i++) flagB[i] = counter;
                    counter++;
                }
            }
            StringBuffer ss = new StringBuffer();
            for (int u = e.startIdx; u < e.endIdx; u++) {
                if (u > e.startIdx) ss.append(" ");
                ss.append(wordB[u]);
            }
        }
        int renumber[] = new int[wordB.length];
        for (int i = 0; i < wordB.length; i++) {
            renumber[i] = -1;
        }
        Hashtable ht = new Hashtable();
        int renumberCounter = 0;
        for (int i = 0; i < wordA.length; i++) {
            if (flagA[i] >= 0) {
                ExSegment es = (ExSegment) segList.get(flagA[i]);
                StringBuilder rebuild = new StringBuilder();
                for (int j = es.startSourceIdx; j < es.endSourceIdx; j++) {
                    rebuild.append(owordA[j]);
                }
                String rebuilt = rebuild.toString();
                Integer in = (Integer) ht.get(rebuilt);
                if (in == null) {
                    if (renumber[flagA[i]] < 0) {
                        renumber[flagA[i]] = renumberCounter++;
                        ht.put(rebuilt, new Integer(renumber[flagA[i]]));
                    }
                } else {
                    if (renumber[flagA[i]] < 0) {
                        renumber[flagA[i]] = in.intValue();
                    }
                }
            }
        }
        if (type == 0) {
            int prevFlag = -1;
            StringBuffer sc = new StringBuffer();
            for (int i = 0; i < spaceA.length; i++) {
                sc.append(spaceA[i]);
                if (i < wordA.length) {
                    if (prevFlag != flagA[i]) {
                        if (flagA[i] < 0) sc.append("\017"); else sc.append("\003" + col[renumber[flagA[i]] % 12]);
                    }
                    sc.append(owordA[i]);
                    prevFlag = flagA[i];
                    if (i < spaceA.length - 1 && !spaceA[i + 1].matches("[ ]+")) {
                        if (prevFlag != -1) {
                            sc.append("\017");
                            prevFlag = -1;
                        }
                    }
                }
            }
            prevFlag = -1;
            StringBuffer sd = new StringBuffer();
            for (int i = 0; i < spaceB.length; i++) {
                sd.append(spaceB[i]);
                if (i < wordB.length) {
                    if (prevFlag != flagB[i]) {
                        if (flagB[i] < 0) sd.append("\017"); else sd.append("\003" + col[renumber[flagB[i]] % 12]);
                    }
                    sd.append(wordB[i]);
                    prevFlag = flagB[i];
                    if (i < spaceB.length - 1 && !spaceB[i + 1].matches("[ ]+")) {
                        if (prevFlag != -1) {
                            sd.append("\017");
                            prevFlag = -1;
                        }
                    }
                }
            }
            sc.append("  [");
            sc.append(sd.toString());
            sc.append("]");
            String res[] = new String[1];
            res[0] = sc.toString();
            return res;
        } else {
            int oldFlag = -1;
            int iCounter = 0;
            String ret[] = new String[segList.size()];
            for (int i = 0; i < flagA.length; i++) {
                if (flagA[i] != oldFlag) {
                    if (flagA[i] >= 0) {
                        ExSegment seg = (ExSegment) segList.get(flagA[i]);
                        StringBuilder sb = new StringBuilder();
                        sb.append(iCounter + 1);
                        sb.append(". ");
                        for (int j = seg.startSourceIdx; j < seg.endSourceIdx - 1; j++) {
                            if (j > seg.startSourceIdx) sb.append(" ");
                            sb.append(owordA[j]);
                        }
                        if (seg.endSourceIdx - 1 > seg.startSourceIdx) sb.append(" ");
                        sb.append(wordA[seg.endSourceIdx - 1]);
                        sb.append(": ");
                        sb.append(seg.originalMeaning[2]);
                        if (seg.score >= 0.6) sb.append(" ---> "); else sb.append(" -?-> ");
                        for (int j = seg.startIdx; j < seg.endIdx; j++) {
                            if (j > seg.startIdx) sb.append(" ");
                            sb.append(wordB[j]);
                        }
                        sb.append(" (");
                        sb.append(pf.format(seg.score));
                        sb.append(")");
                        ret[iCounter] = sb.toString();
                        iCounter++;
                    }
                    oldFlag = flagA[i];
                }
            }
            return ret;
        }
    }

    public static ArrayList analyzeForDb(KanjiDAO myData, MorphologicalAnalyzer myExt, String a, String b, double thres, int type) {
        ArrayList chasenResult = myExt.extractWord(a);
        String wordA[] = new String[chasenResult.size()];
        String owordA[] = new String[chasenResult.size()];
        boolean isParticleA[] = new boolean[chasenResult.size()];
        if (wordA.length == 0) return chasenResult;
        try {
            for (int i = 0; i < wordA.length; i++) {
                ExtractedWord ew = (ExtractedWord) chasenResult.get(i);
                wordA[i] = ew.word;
                owordA[i] = ew.original;
                isParticleA[i] = ew.isParticle;
            }
            String spaceA[] = new String[chasenResult.size() + 1];
            int end = 0;
            for (int i = 0; i < wordA.length; i++) {
                ExtractedWord wrd = ((ExtractedWord) chasenResult.get(i));
                spaceA[i] = a.substring(end, wrd.startIdx);
                end = wrd.endIdx;
            }
            spaceA[wordA.length] = a.substring(end);
            ArrayList buffA = new ArrayList(wordA.length * 5 - 4);
            for (int j = 0; j < wordA.length; j++) {
                if (!isParticleA[j]) {
                    for (int k = j + 1; k <= minimum(j + 5, wordA.length); k++) {
                        if (!isParticleA[k - 1]) {
                            StringBuilder sb = new StringBuilder();
                            for (int p = j; p < k - 1; p++) {
                                sb.append(owordA[p]);
                            }
                            sb.append(wordA[k - 1]);
                            String str = sb.toString();
                            buffA.add(new SourceSegment(j, k, str, StringUtility.wordLetterPairs(str)));
                        }
                    }
                }
            }
            String wordB[] = b.split("[^A-Za-z0-9]+");
            if (wordB.length > 0) {
                if (wordB[0].equals("")) {
                    String temp[] = wordB;
                    wordB = new String[temp.length - 1];
                    for (int i = 0; i < temp.length - 1; i++) {
                        wordB[i] = temp[i + 1];
                    }
                }
            }
            String spaceB[] = b.split("[A-Za-z0-9]+");
            TreeSet segments = new TreeSet();
            int countB = 0;
            ArrayList buffB = new ArrayList(wordB.length * 5);
            for (int j = 0; j < wordB.length; j++) {
                for (int k = j + 1; k <= minimum(j + 5, wordB.length); k++) {
                    StringBuffer sb = new StringBuffer();
                    for (int p = j; p < k; p++) {
                        if (p > j) sb.append(" ");
                        sb.append(wordB[p]);
                    }
                    buffB.add(StringUtility.wordLetterPairs(sb.toString()));
                }
            }
            for (int i = 0; i < buffA.size(); i++) {
                SourceSegment sg = (SourceSegment) buffA.get(i);
                ArrayList meanA = myData.getWordAndMeaningFromEdict(sg.segment);
                boolean importantCase = false;
                if (meanA.size() > 0) MeaningExpander.expand(sg.segment, meanA); else {
                    meanA = SimpleDict.analyze(sg.segment, myData);
                    importantCase = true;
                }
                for (int m = 0; m < meanA.size(); m++) {
                    String meaningA = ((String[]) (meanA.get(m)))[2];
                    ArrayList pairs1 = StringUtility.wordLetterPairs(meaningA);
                    int countPos = 0;
                    for (int j = 0; j < wordB.length; j++) {
                        for (int k = j + 1; k <= minimum(j + 5, wordB.length); k++) {
                            double score = StringUtility.compareStrings(pairs1, (ArrayList) ((ArrayList) buffB.get(countPos++)).clone(), importantCase) - 0.0001 * (25 - sg.end + sg.start) - 0.000001 * (5 - k + j) - 0.00000001 * m;
                            if (k - j == 1) {
                                if (meaningA.length() == wordB[j].length()) {
                                    char[] aaa = meaningA.toCharArray();
                                    char[] bbb = wordB[j].toCharArray();
                                    int diff = 0;
                                    for (int n = 0; n < aaa.length; n++) {
                                        if (aaa[n] >= 'a') aaa[n] -= 0x0020;
                                        if (bbb[n] >= 'a') bbb[n] -= 0x0020;
                                        if (aaa[n] != bbb[n]) diff++;
                                    }
                                    if (diff == 1) {
                                        if (myData.isMeaningInEdict(wordB[j])) {
                                            score *= 0.6;
                                        }
                                    }
                                }
                            }
                            if (score >= thres) segments.add(new ExSegment(j, k, sg.start, sg.end, score, (String[]) meanA.get(m)));
                        }
                    }
                }
            }
            Iterator iter = segments.iterator();
            int flagA[] = new int[wordA.length];
            for (int i = 0; i < wordA.length; i++) flagA[i] = -1;
            int flagB[] = new int[wordB.length];
            for (int i = 0; i < wordB.length; i++) flagB[i] = -1;
            int counter = 0;
            ArrayList segList = new ArrayList();
            while (iter.hasNext()) {
                if (counter >= wordA.length) break;
                ExSegment e = (ExSegment) iter.next();
                boolean canTake = true;
                for (int i = e.startSourceIdx; i < e.endSourceIdx; i++) {
                    if (flagA[i] >= 0) {
                        canTake = false;
                        break;
                    }
                }
                if (canTake) {
                    for (int i = e.startIdx; i < e.endIdx; i++) {
                        if (flagB[i] >= 0) {
                            canTake = false;
                            break;
                        }
                    }
                    if (canTake) {
                        segList.add(e);
                        for (int i = e.startSourceIdx; i < e.endSourceIdx; i++) flagA[i] = counter;
                        for (int i = e.startIdx; i < e.endIdx; i++) flagB[i] = counter;
                        counter++;
                    }
                }
                StringBuffer ss = new StringBuffer();
                for (int u = e.startIdx; u < e.endIdx; u++) {
                    if (u > e.startIdx) ss.append(" ");
                    ss.append(wordB[u]);
                }
            }
            int astidx[] = new int[wordA.length + 1];
            int aenidx[] = new int[wordA.length + 1];
            int bstidx[] = new int[wordB.length + 1];
            int benidx[] = new int[wordB.length + 1];
            int ps = 0;
            for (int i = 0; i <= wordA.length; i++) {
                if (i < spaceA.length) ps += spaceA[i].length();
                astidx[i] = ps;
                if (i < owordA.length) ps += owordA[i].length();
                aenidx[i] = ps;
            }
            ps = 0;
            for (int i = 0; i <= wordB.length; i++) {
                if (i < spaceB.length) ps += spaceB[i].length();
                bstidx[i] = ps;
                if (i < wordB.length) ps += wordB[i].length();
                benidx[i] = ps;
            }
            Iterator ite = segList.iterator();
            while (ite.hasNext()) {
                ExSegment ex = (ExSegment) ite.next();
                ex.startIdx = bstidx[ex.startIdx];
                ex.endIdx = benidx[ex.endIdx - 1];
                ex.startSourceIdx = astidx[ex.startSourceIdx];
                ex.endSourceIdx = aenidx[ex.endSourceIdx - 1];
            }
            return segList;
        } catch (Exception eee) {
            return new ArrayList();
        }
    }

    public static int minimum(int a, int b) {
        if (a <= b) return a;
        return b;
    }
}
