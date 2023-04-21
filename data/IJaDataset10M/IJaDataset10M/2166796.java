package listnet.process;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import listnet.data.Document;
import listnet.data.MSDocument;
import listnet.data.Sample;

/**
 * The Class MSNormalizer.
 */
public final class MSNormalizer implements Normalizer {

    /** 一个ArrayList，保存特征向量纬度个MaxMin，每个MaxMin实例保存这一维度的权重的最大值和最小值. */
    private ArrayList<MaxMin> maxmin = new ArrayList<MSNormalizer.MaxMin>();

    private MSNormalizer() {
    }

    public MSNormalizer(ArrayList<MaxMin> mm) {
        this.maxmin = mm;
    }

    /**
	 * Gets the normalizer.
	 *
	 * @param f 训练文本文件
	 * @return the normalizer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public static Normalizer getNormalizer(File f) throws IOException {
        MSNormalizer normalizer = new MSNormalizer();
        DataReader reader = new MSDataReader(f);
        Sample smaple = reader.getNextSample();
        if (smaple == null) return null;
        int featureSize = smaple.getDocuments().get(0).getFeatures().size();
        for (int i = 0; i < featureSize; i++) {
            normalizer.maxmin.add(new MaxMin(0.0, Double.MAX_VALUE));
        }
        do {
            List<Document> docs = smaple.getDocuments();
            for (Document doc : docs) {
                for (int i = 0; i < featureSize; i++) {
                    if (doc.getFeatures().size() == 7) {
                        System.out.println("d");
                    }
                    double val = doc.getFeatures().get(i);
                    if (val > normalizer.maxmin.get(i).getMax()) normalizer.maxmin.get(i).setMax(doc.getFeatures().get(i));
                    if (val < normalizer.maxmin.get(i).getMin()) normalizer.maxmin.get(i).setMin(val);
                }
            }
        } while ((smaple = reader.getNextSample()) != null);
        return normalizer;
    }

    @Override
    public Document normalize(Document doc) {
        int featureSize = this.maxmin.size();
        ArrayList<Double> oldfeatures = doc.getFeatures();
        ArrayList<Double> features = new ArrayList<Double>(featureSize);
        for (int i = 0; i < featureSize; i++) {
            double max = maxmin.get(i).getMax();
            double min = maxmin.get(i).getMin();
            if (max == min) features.add(new Double(0.0)); else features.add(new Double(oldfeatures.get(i) - min) / (max - min));
        }
        return new MSDocument(doc.getQid(), doc.getRelevance(), features);
    }

    @Override
    public Sample normalize(Sample sample) {
        Sample res = new Sample(sample.getQid());
        List<Document> docs = sample.getDocuments();
        for (Document doc : docs) {
            res.add(normalize(doc));
        }
        return res;
    }

    @Override
    public ArrayList<MaxMin> getNorParameters() {
        return this.maxmin;
    }

    public void debug() {
        System.out.println("MSNormalizer : ");
        for (int i = 0; i < maxmin.size(); i++) {
            System.out.println("max[" + i + "] = " + maxmin.get(i).getMax() + " min[" + i + "] = " + maxmin.get(i).getMin());
        }
    }
}
