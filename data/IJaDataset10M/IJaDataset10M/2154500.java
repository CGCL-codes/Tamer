package org.phramer.v1.decoder.mert.item;

import info.olteanu.utils.*;
import java.io.*;
import java.util.*;
import org.phramer.*;
import org.phramer.mert.evaluation.*;
import org.phramer.mert.item.*;
import org.phramer.v1.decoder.mert.reader.*;

public class ReferenceLoaderMT implements ReferenceLoader {

    public static boolean DEBUG__NSENT_LIMITED = false;

    public static int DEBUG__NSETNT_LIMIT = 3;

    public static boolean DEBUG__HYPO_LIMITED = false;

    public static int DEBUG__HYPO_LIMIT = 30;

    public ReferenceWithHypotheses[] getEmptyReferenceWithHypotheses(Item[][] references) {
        ReferenceWithHypotheses[] out = new ReferenceWithHypotheses[references.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = new ReferenceWithHypotheses();
            out[i].references = references[i];
            out[i].hypotheses = new Vector<Hypothesis>();
        }
        return out;
    }

    public Item[][] readReference_R1(String fileReference) throws IOException {
        Vector<Item[]> out = new Vector<Item[]>();
        BufferedReader inputFile = new BufferedReader(new InputStreamReader(new FileInputStream(fileReference)));
        String lineFile;
        int cnt = 0;
        while ((lineFile = inputFile.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(lineFile);
            ESentence[] f = new ESentence[1];
            String[] k = new String[st.countTokens()];
            for (int i = 0; i < k.length; i++) k[i] = st.nextToken().intern();
            f[0] = new ESentence(k, k);
            out.add(f);
            cnt++;
            if (DEBUG__NSENT_LIMITED) if (cnt == DEBUG__NSETNT_LIMIT) break;
        }
        inputFile.close();
        return out.toArray(new Item[out.size()][]);
    }

    public Item[][] readReference_Rn(String[] fileReference) throws IOException {
        Vector<Item[]> out = new Vector<Item[]>();
        BufferedReader[] inputFile = new BufferedReader[fileReference.length];
        for (int i = 0; i < inputFile.length; i++) inputFile[i] = new BufferedReader(new InputStreamReader(new FileInputStream(fileReference[i])));
        String lineFile0;
        String[] lf = new String[fileReference.length];
        while ((lineFile0 = inputFile[0].readLine()) != null) {
            lf[0] = lineFile0;
            for (int i = 1; i < inputFile.length; i++) {
                lf[i] = inputFile[i].readLine();
                if (lf[i] == null) throw new IOException("File " + fileReference[i] + " has fewer lines than expected");
            }
            ESentence[] f = new ESentence[inputFile.length];
            for (int q = 0; q < inputFile.length; q++) {
                StringTokenizer st = new StringTokenizer(lf[q]);
                String[] k = new String[st.countTokens()];
                for (int i = 0; i < k.length; i++) k[i] = st.nextToken().intern();
                f[q] = new ESentence(k, k);
            }
            out.add(f);
        }
        for (int i = 1; i < inputFile.length; i++) if (inputFile[i].readLine() != null) throw new IOException("File " + fileReference[i] + " has more lines than expected");
        for (int i = 0; i < inputFile.length; i++) inputFile[i].close();
        return out.toArray(new Item[out.size()][]);
    }

    public static ReferenceWithHypotheses[] readHypotheses(Item[][] reference, String rescoreFilePrefix, String rescoreFileSufix, RescoreReader rescoreReader, String nbestFilePrefix, String nbestFileSufix, NBestReader nbestReader, Evaluator evaluator) throws IOException, PhramerException {
        ReferenceWithHypotheses[] out = new ReferenceWithHypotheses[reference.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = new ReferenceWithHypotheses();
            out[i].references = reference[i];
            out[i].hypotheses = new Vector<Hypothesis>();
            BufferedReader inputFileRescore = new BufferedReader(new InputStreamReader(new FileInputStream(rescoreFilePrefix + StringTools.adjustLengthForNumber(i, 4) + rescoreFileSufix)));
            BufferedReader inputFileNBest = new BufferedReader(new InputStreamReader(new FileInputStream(nbestFilePrefix + StringTools.adjustLengthForNumber(i, 4) + nbestFileSufix)));
            String lineFileR, lineFileN;
            int cnt = 0;
            while ((lineFileR = inputFileRescore.readLine()) != null) {
                lineFileN = inputFileNBest.readLine();
                double[] p = rescoreReader.readLine(lineFileR);
                String[] hypoInNBest = nbestReader.readLine(lineFileN);
                if (p == null || hypoInNBest == null) break;
                Hypothesis e = new Hypothesis();
                e.h = new ESentence(hypoInNBest, hypoInNBest);
                e.p = p;
                e.error = evaluator.getErrorStatistics(out[i].references, e.h);
                out[i].hypotheses.add(e);
                cnt++;
                if (DEBUG__HYPO_LIMITED) if (cnt == DEBUG__HYPO_LIMIT) break;
            }
            inputFileRescore.close();
            inputFileNBest.close();
        }
        return out;
    }
}
