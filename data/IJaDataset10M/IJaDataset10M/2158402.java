package org.sf.xrime.algorithms.BC;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.sf.xrime.algorithms.utils.GraphAlgorithmMapReduceBase;
import org.sf.xrime.model.vertex.LabeledAdjBiSetVertex;

;

public class BCForwardMapper extends GraphAlgorithmMapReduceBase implements Mapper<Text, LabeledAdjBiSetVertex, Text, LabeledAdjBiSetVertex> {

    @Override
    public void map(Text key, LabeledAdjBiSetVertex value, OutputCollector<Text, LabeledAdjBiSetVertex> collector, Reporter reporter) throws IOException {
        collector.collect(key, value);
    }
}
