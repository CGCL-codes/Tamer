package at.jku.rdfstats.hist.builder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import at.jku.rdfstats.ParseException;
import at.jku.rdfstats.RDFStatsConfiguration;
import at.jku.rdfstats.hist.GenericSingleBinHistogram;
import at.jku.rdfstats.hist.Histogram;
import com.hp.hpl.jena.graph.Node;

/**
 * @author dorgon
 *
 */
public class GenericSingleBinHistogramBuilder extends AbstractHistogramBuilder<Object> {

    /**
	 * @param conf
	 * @param typeUri
	 * @param prefSize
	 */
    public GenericSingleBinHistogramBuilder(RDFStatsConfiguration conf, String typeUri, int prefSize) {
        super(conf, typeUri, prefSize);
        values = new HashMap<Object, Integer>();
    }

    public void addNodeValue(Node val) throws HistogramBuilderException {
        try {
            Object o = GenericSingleBinHistogram.parseNodeValueImpl(val);
            addValue(o, getValueLength(val));
        } catch (ParseException e) {
            throw new HistogramBuilderException("Error parsing node value: " + e.getMessage(), e);
        }
    }

    @Override
    protected Histogram<Object> generateHistogram() {
        int total = 0;
        for (Object val : values.keySet()) total += values.get(val);
        int distinctValues = values.size();
        int[] lengths = getValueLengths(total);
        return (Histogram<Object>) new GenericSingleBinHistogram(typeUri, total, distinctValues, lengths, this.getClass());
    }

    public void writeData(ByteArrayOutputStream stream, Histogram<Object> hist) {
        HistogramCodec.writeInt(stream, hist.getBinData()[0]);
        HistogramCodec.writeInt(stream, hist.getDistinctValues());
        HistogramCodec.writeIntArray(stream, hist.getValueLengths());
    }

    public Histogram<Object> readData(ByteArrayInputStream stream) {
        int total = HistogramCodec.readInt(stream);
        int distinctValues = HistogramCodec.readInt(stream);
        int[] valueLengths = HistogramCodec.readIntArray(stream, 3);
        return new GenericSingleBinHistogram(typeUri, total, distinctValues, valueLengths, this.getClass());
    }
}
