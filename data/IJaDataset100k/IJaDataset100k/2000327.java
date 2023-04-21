package preprocessing.automatic.Population;

import game.utils.Exceptions.NonExistingAttributeException;
import preprocessing.Parameters.Parameter;
import preprocessing.Parameters.ParameterMutable;
import preprocessing.PreprocessingMethodsList;
import preprocessing.automatic.GUI.GeneticIndividualsViewer.IndividualPanel.GeneVizualization;
import preprocessing.methods.BasePreprocessorConfig;
import preprocessing.methods.Preprocessor;
import preprocessing.storage.PreprocessingStorage;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: Oct 21, 2009
 * Time: 4:56:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Gene implements Cloneable, GeneVizualization {

    private transient Preprocessor method;

    private BasePreprocessorConfig config;

    private String preprocessorName;

    private boolean geneEnabled;

    public Gene(Preprocessor method, BasePreprocessorConfig config) {
        this.method = method;
        this.config = config;
        preprocessorName = method.getPreprocessingMethodName();
        geneEnabled = true;
    }

    public Preprocessor getMethod() {
        return method;
    }

    public BasePreprocessorConfig getConfig() {
        return config;
    }

    public void applyMethod(PreprocessingStorage store, String attributeName, boolean storeIsTestingSet) throws NonExistingAttributeException {
        if (!geneEnabled) {
            return;
        }
        if (storeIsTestingSet && !method.isApplyOnTestingData()) {
            return;
        }
        method.init(store);
        config.cleanIONames();
        if (attributeName.compareTo(APAIndividual.globalString) == 0) {
            config.add2IONames("");
        } else {
            config.add2IONames(attributeName);
        }
        String names = config.getSeparatedIONames().length == 0 ? APAIndividual.globalString : config.getSeparatedIONames()[0];
        System.out.printf("Method %s apllied to %s\n", method.getPreprocessingMethodName(), names);
        method.setConfigurationClass(config);
        method.run();
    }

    public void mutateConfiguration(double mutationPropability) {
        for (int j = 0; j < config.getParametersCount(); j++) {
            if (Math.random() < mutationPropability) {
                continue;
            }
            Parameter param = (Parameter) config.getParameterAt(j);
            if (param instanceof ParameterMutable) {
                ((ParameterMutable) param).randomize();
            }
        }
    }

    public void setGeneEnabled(boolean geneEnabled) {
        this.geneEnabled = geneEnabled;
    }

    public boolean getGeneEnabled() {
        return geneEnabled;
    }

    public Gene clone() throws CloneNotSupportedException {
        Gene g = (Gene) super.clone();
        g.method = this.method;
        g.config = this.config.clone();
        g.geneEnabled = this.geneEnabled;
        return g;
    }

    public void unFreezeAfterDeserialisation() {
        PreprocessingMethodsList list = PreprocessingMethodsList.getInstance();
        method = list.getMethodByName(preprocessorName);
    }

    @Override
    public String getGeneName() {
        return method.getPreprocessingMethodName();
    }

    @Override
    public String getGeneInfo() {
        return method.getPreprocessingMethodTree() + "." + method.getPreprocessingMethodName();
    }
}
