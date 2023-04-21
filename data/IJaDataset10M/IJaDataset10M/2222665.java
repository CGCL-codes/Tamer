package edu.colorado.emml.classifiers.ind;

import weka.core.Instances;
import java.io.File;
import edu.colorado.emml.classifiers.BatchClassifier;
import edu.colorado.emml.classifiers.ClassifierDescription;
import edu.colorado.emml.classifiers.Parameter;
import edu.colorado.emml.classifiers.PredictionResult;
import edu.colorado.emml.util.FileUtils;

/**
 * Created by: Sam
 * Mar 17, 2008 at 1:03:52 PM
 */
public class IND extends BatchClassifier {

    private Type type;

    public static enum Type {

        cart, cart0, c4, smml, mml, bayes
    }

    public static final String IND_BIN_DIR = "/cygdrive/c/reid/svn-working-copy/trunk/ensembles/contrib/cygwind-0.00.02/bin";

    public IND() {
        this(Type.cart);
    }

    public IND(Type type) {
        this.type = type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public PredictionResult buildAndClassify(Instances train, Instances test) throws Exception {
        String path = "research/IND/data";
        new File("C:/" + path).mkdirs();
        final File attrFile = new File("C:/" + path + "/data.rawattr");
        INDFile.saveAttributeSkeleton(train, attrFile);
        FileUtils.copyTextFile(attrFile, new File(attrFile.getParentFile(), "data.attr"));
        INDFile.saveIND(train, new File("C:/" + path + "/data.bld"));
        INDFile.saveIND(train, new File("C:/" + path + "/data.tst"));
        CygwinUtil.execBatchScript("cd /cygdrive/c/" + path + "\n" + "PATH=$PATH:" + IND_BIN_DIR + "\n" + "mktree -e -s " + type + " data");
        File treeFile = new File("C:/" + path + "data.tree");
        INDClassifier indClassifier = new INDClassifier(treeFile, attrFile);
        PredictionResult predictionResult = new PredictionResult(indClassifier.classify(train), indClassifier.classify(test));
        predictionResult.setDescription(getClassifierDescription().toString());
        return predictionResult;
    }

    public ClassifierDescription getClassifierDescription() {
        return new ClassifierDescription(getClass(), new Parameter("type", type.toString()));
    }

    public void setClassifierDescription(ClassifierDescription description) {
        setType(getTypeForString(description.getString("type")));
    }

    private Type getTypeForString(String string) {
        for (Type x : Type.values()) {
            if (x.toString().equals(string)) {
                return x;
            }
        }
        throw new RuntimeException("Type not found for string: " + string);
    }
}
