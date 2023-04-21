package papertoolkit.pen.gesture.shapecontext;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * <p>
 * 
 * </p>
 * <p>
 * <span class="BSDLicense"> This software is distributed under the <a
 * href="http://hci.stanford.edu/research/copyright.txt">BSD License</a>. </span>
 * </p>
 * 
 * @author Avi Robinson-Mosher
 */
public class Gesture {

    ArrayList<ShapeContext> contexts = new ArrayList<ShapeContext>();

    String name;

    boolean rotationInvariant = false;

    boolean timeSensitive = false;

    public Gesture(String name) {
        this.name = name;
    }

    public void addGesture(ShapeContext context) {
        contexts.add(context);
    }

    public double averageMatch(ShapeContext context) {
        double distance = 0;
        for (ShapeContext gesture : contexts) {
            distance += ShapeHistogram.shapeContextMetric(context, gesture, rotationInvariant, timeSensitive, false);
        }
        return distance / contexts.size();
    }

    public double bestMatch(ShapeContext context) {
        double distance = Double.MAX_VALUE;
        for (ShapeContext gesture : contexts) {
            distance = Math.min(distance, ShapeHistogram.shapeContextMetric(context, gesture, rotationInvariant, timeSensitive, false));
        }
        return distance;
    }

    public void determineClassParameters() {
        int N = contexts.size();
        double dNoRotationNoTime = 0;
        for (int i = 0; i < contexts.size(); i++) {
            ShapeContext context = contexts.get(i);
            for (int j = 0; j < contexts.size(); j++) {
                if (i == j) continue;
                dNoRotationNoTime += ShapeHistogram.shapeContextMetric(context, contexts.get(j), false, false, false);
            }
        }
        dNoRotationNoTime /= N * (N - 1);
        double dRotationNoTime = 0;
        for (int i = 0; i < contexts.size(); i++) {
            ShapeContext context = contexts.get(i);
            for (int j = 0; j < contexts.size(); j++) {
                if (i == j) continue;
                dRotationNoTime += ShapeHistogram.shapeContextMetric(context, contexts.get(j), true, false, false);
            }
        }
        dRotationNoTime /= N * (N - 1);
        double dNoRotationTime = 0;
        for (int i = 0; i < contexts.size(); i++) {
            ShapeContext context = contexts.get(i);
            for (int j = 0; j < contexts.size(); j++) {
                if (i == j) continue;
                dNoRotationTime += ShapeHistogram.shapeContextMetric(context, contexts.get(j), false, true, false);
            }
        }
        dNoRotationTime /= N * (N - 1);
        double dRotationTime = 0;
        for (int i = 0; i < contexts.size(); i++) {
            ShapeContext context = contexts.get(i);
            for (int j = 0; j < contexts.size(); j++) {
                if (i == j) continue;
                dRotationTime += ShapeHistogram.shapeContextMetric(context, contexts.get(j), true, true, false);
            }
        }
        dRotationTime /= N * (N - 1);
        System.out.println("Class " + name + ": NN: " + dNoRotationNoTime + " YN: " + dRotationNoTime + " NY: " + dNoRotationTime + " YY: " + dRotationTime);
        System.out.println("Rotation score: " + dNoRotationNoTime / dRotationNoTime);
    }

    public void knnMatch(ShapeContext context, int k, double[] distance, String[] clazz, boolean verbose) {
        for (int c = 0; c < contexts.size(); c++) {
            ShapeContext gesture = contexts.get(c);
            double d = ShapeHistogram.shapeContextMetric(context, gesture, rotationInvariant, timeSensitive, verbose);
            for (int i = 0; i < k; i++) {
                if (d < distance[i]) {
                    for (int j = k - 1; j > i; j--) {
                        distance[j] = distance[j - 1];
                        clazz[j] = clazz[j - 1];
                    }
                    distance[i] = d;
                    clazz[i] = name;
                    break;
                }
            }
        }
    }

    public void quillWrite(Writer writer) throws IOException {
        String stripTestName = name.replace("TEST", "");
        writer.write("name\t" + stripTestName + "\n");
        boolean directionInvariant = false;
        boolean orientationInvariant = false;
        boolean sizeInvariant = false;
        writer.write("directionInvariant\t" + directionInvariant + "\n");
        writer.write("orientationInvariant\t" + orientationInvariant + "\n");
        writer.write("sizeInvariant\t" + sizeInvariant + "\n");
        writer.write("gestures\t" + contexts.size() + "\n");
        for (int i = 0; i < contexts.size(); i++) {
            contexts.get(i).quillWrite(writer);
        }
        writer.write("endcategory\n");
    }

    public int size() {
        return contexts.size();
    }
}
