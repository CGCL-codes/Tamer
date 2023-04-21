package com.stromberglabs.visual.ip.creator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.semanticmetadata.lire.imageanalysis.sift.Extractor;
import net.semanticmetadata.lire.imageanalysis.sift.Feature;
import com.stromberglabs.cluster.Clusterable;
import com.stromberglabs.visual.ip.sift.SIFTInterestPoint;

public class SIFTInterestPointCreator implements InterestPointCreator {

    public List<Clusterable> getPoints(String image) throws IOException {
        BufferedImage img = ImageIO.read(new File(image));
        return getPoints(img);
    }

    public List<Clusterable> getPoints(BufferedImage img) throws IOException {
        Extractor extractor = new Extractor();
        List<Feature> features = extractor.computeSiftFeatures(img);
        List<Clusterable> points = new ArrayList<Clusterable>(features.size());
        for (Feature feature : features) {
            points.add(new SIFTInterestPoint(feature.descriptor));
        }
        return points;
    }
}
