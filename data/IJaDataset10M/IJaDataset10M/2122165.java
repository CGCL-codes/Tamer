package org.phramer.v1.decoder.lm.ngram;

import java.io.*;
import java.util.*;
import org.phramer.lib.vocabulary.*;
import org.phramer.v1.decoder.lm.wordsequence.*;
import org.phramer.v1.decoder.math.*;

public class VocabularyBackOffLM extends BackOffLM {

    public boolean allowFastCall() {
        return allowFastCall;
    }

    public boolean isVocabularyBasedLM() {
        return true;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public int getN() {
        return n;
    }

    private int n;

    private HashMap<Long, Double>[] logProbability;

    private HashMap<Long, Double>[] logBOW;

    private Vocabulary vocabulary;

    private final boolean allowFastCall;

    public VocabularyBackOffLM(boolean allowFastCall) {
        this.allowFastCall = allowFastCall;
    }

    /** Loads the model from an ARPA file
	 *
	 */
    public void loadArpa(Reader r) throws IOException {
        BufferedReader inputFile = new BufferedReader(r);
        String lineFile;
        if (!readTill(inputFile, "\\data\\")) throw new EOFException("No model found");
        Vector<Integer> list = new Vector<Integer>();
        while ((lineFile = inputFile.readLine()) != null) if (lineFile.startsWith("ngram")) list.add(Integer.parseInt(lineFile.substring(lineFile.indexOf('=') + 1))); else if (lineFile.equals("\\1-grams:")) break;
        logProbability = new HashMap[list.size()];
        logBOW = new HashMap[list.size() - 1];
        vocabulary = new Vocabulary();
        n = list.size();
        for (int i = 0; i < logProbability.length; i++) {
            System.err.println("Loading " + (1 + i) + "-grams");
            if (i > 0) if (!readTill(inputFile, "\\" + (i + 1) + "-grams:")) throw new EOFException("LM file ended too soon");
            logProbability[i] = new HashMap<Long, Double>();
            if (i < logBOW.length) logBOW[i] = new HashMap<Long, Double>();
            HashMap<Long, Double> lProbability = logProbability[i];
            HashMap<Long, Double> lBOW = i < logBOW.length ? logBOW[i] : null;
            int ngramOrder = i + 1;
            int ngramCount = list.get(i);
            String[] str = new String[ngramOrder];
            for (int j = 0; j < ngramCount; j++) {
                lineFile = inputFile.readLine();
                StringTokenizer st = new StringTokenizer(lineFile);
                if (st.countTokens() != ngramOrder + 1 && st.countTokens() != ngramOrder + 2) throw new IOException("Bad n-gram file: " + lineFile);
                double lP10 = Double.parseDouble(st.nextToken());
                for (int k = ngramOrder - 1; k >= 0; k--) str[k] = st.nextToken().intern();
                long wSeq;
                if (ngramOrder == 1) {
                    wSeq = vocabulary.add(str[0]) + SlowIdWordSequence.DELTA;
                } else wSeq = SlowIdWordSequence.getSequenceID(vocabulary, str);
                if (lP10 == -99) lProbability.put(wSeq, Double.NEGATIVE_INFINITY); else lProbability.put(wSeq, MathTools.log10toLog(lP10));
                double lB10 = 0;
                if (st.hasMoreTokens()) {
                    lB10 = Double.parseDouble(st.nextToken());
                    if (lB10 == -99) lBOW.put(wSeq, Double.NEGATIVE_INFINITY); else lBOW.put(wSeq, MathTools.log10toLog(lB10));
                }
            }
        }
        if (!readTill(inputFile, "\\end\\")) throw new IOException("Bad n-gram file: no end marker found");
    }

    /** Loads the model from a proprietary binary file.<br><br>
	 * Format:
	 * <pre>
	 * 1) order (3 for 3-gram) - int32
	 * 2) serialized vocabulary (see Vocabulary)
	 * 3) probability ( 1..order )
	 * 3.a) size (number of instances) - int32
	 * 3.b) probability instances (1..size)
	 * 3.b. i) key ( encodes the n-gram) - long64
	 * 3.b.ii) probability (prob. of that n-gram) - double64
	 * 4) backoff (1..order-1)
	 * ( same as (3) )
	 * </pre>
	 */
    public void loadBinary(InputStream is, int type) throws IOException {
        DataInputStream input = new DataInputStream(new BufferedInputStream(is));
        int order = input.readInt();
        n = order;
        System.err.println("Loading binary " + order + "-gram LM...");
        logProbability = new HashMap[order];
        logBOW = new HashMap[order - 1];
        vocabulary = new Vocabulary();
        vocabulary.loadBinary(input);
        for (int i = 0; i < logProbability.length; i++) {
            int size = input.readInt();
            logProbability[i] = new HashMap<Long, Double>(size);
            for (int j = 0; j < size; j++) {
                long key = input.readLong();
                double value = input.readDouble();
                logProbability[i].put(key, value);
            }
        }
        for (int i = 0; i < logBOW.length; i++) {
            int size = input.readInt();
            logBOW[i] = new HashMap<Long, Double>(size);
            for (int j = 0; j < size; j++) {
                long key = input.readLong();
                double value = input.readDouble();
                logBOW[i].put(key, value);
            }
        }
    }

    /** Saves the model into a proprietary binary file
	 *
	 */
    public void saveBinary(OutputStream os, int type) throws IOException {
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(os));
        output.writeInt(logProbability.length);
        vocabulary.saveBinary(output);
        for (int i = 0; i < logProbability.length; i++) {
            output.writeInt(logProbability[i].size());
            Iterator<Long> iter = logProbability[i].keySet().iterator();
            while (iter.hasNext()) {
                Long key = iter.next();
                double value = logProbability[i].get(key);
                output.writeLong(key);
                output.writeDouble(value);
            }
        }
        for (int i = 0; i < logBOW.length; i++) {
            output.writeInt(logBOW[i].size());
            Iterator<Long> iter = logBOW[i].keySet().iterator();
            while (iter.hasNext()) {
                Long key = iter.next();
                double value = logBOW[i].get(key);
                output.writeLong(key);
                output.writeDouble(value);
            }
        }
        output.close();
    }

    public static boolean readTill(BufferedReader inputFile, String marker) throws IOException {
        String lineFile;
        while ((lineFile = inputFile.readLine()) != null) if (marker.equals(lineFile)) return true;
        return false;
    }

    public double getLogProbabilityWord(WordSequence sequence) {
        if (sequence.length() > n) sequence = sequence.trimToSize(n);
        double value = fastGetLogProbabilityWord(((IdWordSequence) sequence).getSequenceID(), sequence.length());
        return value;
    }

    public double fastGetLogProbabilityWord(long sequenceID, int len) {
        assert len <= 3;
        Double t, b;
        double backoff = 0f;
        t = logProbability[len - 1].get(sequenceID);
        if (t != null) return t.doubleValue();
        if (len == 1) return 2;
        b = logBOW[len - 2].get(FastIdWordSequence.farSequence(sequenceID, len));
        if (b != null) backoff += b;
        sequenceID = FastIdWordSequence.nearSequence(sequenceID);
        t = logProbability[len - 2].get(sequenceID);
        if (t != null) return t.doubleValue() + backoff;
        if (len == 2) return 2;
        b = logBOW[len - 3].get(FastIdWordSequence.farSequence(sequenceID, len - 1));
        if (b != null) backoff += b;
        sequenceID = FastIdWordSequence.nearSequence(sequenceID);
        t = logProbability[len - 3].get(sequenceID);
        if (t != null) return t.doubleValue() + backoff;
        return 2;
    }
}
