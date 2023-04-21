package org.phramer.v1.decoder.table;

import info.olteanu.utils.*;
import info.olteanu.utils.chron.*;
import info.olteanu.utils.lang.*;
import java.io.*;
import java.util.*;
import org.phramer.v1.decoder.math.*;
import org.phramer.v1.decoder.phrase.*;

public class MemoryTranslationTable extends TranslationTable {

    private final HashMap<Object, TableLine[]> hash;

    public MemoryTranslationTable(InputStream input, String encodingTextFile, int type, EFProcessorIf processor, int tableLimit, int maxPhraseLength, double tableThreshold, double[] thresholdWeights, boolean storeDetailed) throws IOException {
        Chronometer c = new Chronometer(true);
        MutableInt size = new MutableInt(0);
        MutableInt sizePruning = new MutableInt(0);
        hash = TranslationTableTools.readTranslationTable(type, input, encodingTextFile, MathTools.numberToLog(tableThreshold), thresholdWeights, tableLimit, maxPhraseLength, processor, this, storeDetailed, true, size, sizePruning);
        System.err.println("Translation table loaded in " + StringTools.formatDouble(c.getValue() * 0.001, "0.0#") + " secs. Kept " + sizePruning.value + "/" + size.value + " = " + StringTools.formatDouble(100.0 * sizePruning.value / size.value, "0.0") + "%");
    }

    public MemoryTranslationTable(HashMap<Object, TableLine[]> hash, int tableLimit, double tableThreshold, double[] thresholdWeights) {
        double logTableThreshold = MathTools.numberToLog(tableThreshold);
        MutableInt sizePruning = new MutableInt(0);
        this.hash = TranslationTableTools.reweight(hash, thresholdWeights);
        TranslationTableTools.sortAndPruneArray(this.hash, tableLimit, true, logTableThreshold, sizePruning);
    }

    /** Retrieves all translations for a certain phrase into a list <br>
	 * size() must be greater than 0
	 */
    public TableLine[] getAllTranslations(Phrase phrase) {
        return hash.get(phrase.getKey());
    }

    public boolean readjustWeights(int tableLimit, double tableThreshold, double[] thresholdWeights) {
        return false;
    }

    /** It is reentrant */
    public boolean allowConcurrency() {
        return true;
    }
}
