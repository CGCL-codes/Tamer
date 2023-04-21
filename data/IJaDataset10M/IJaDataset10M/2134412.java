package game.model;

import game.GameFileOperator;
import game.classifier.GameClassifierContainer;
import game.configuration.ClassWithConfigBean;
import game.data.GameData;
import game.data.GameDataContainer;
import game.data.RapidGameData;
import game.models.ModelBox;
import game.models.ModelFactory;
import java.io.File;
import java.util.Arrays;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.UndefinedParameterError;
import configuration.CfgTemplate;
import configuration.ConfigurationFactory;

public class GameModelFileOperator extends GameFileOperator {

    private InputPort train = getInputPorts().createPort("train", ExampleSet.class);

    private OutputPort modelOuput = getOutputPorts().createPort("model");

    private OutputPort trainOut = getOutputPorts().createPort("train");

    public GameModelFileOperator(OperatorDescription description) {
        super(description);
        getTransformer().addPassThroughRule(train, trainOut);
        getTransformer().addGenerationRule(modelOuput, GameModelContainer.class);
    }

    @Override
    public void doWork() {
        File file;
        try {
            file = getParameterAsFile("file");
            CfgTemplate cfg = (CfgTemplate) ConfigurationFactory.getConfigurationObject(file.getAbsolutePath());
            ExampleSet set = train.getData(ExampleSet.class);
            GameData data = new RapidGameData(set);
            ModelBox modelBox = ModelFactory.createModelBox(cfg, data, true);
            for (int i = 0; i < 150; i++) {
                double[] d = new double[4];
                for (int j = 0; j < 4; j++) d[j] = data.getVector(i)[j];
                System.out.println(Arrays.toString(modelBox.getOutput(d)));
            }
            modelOuput.deliver(new GameModelContainer(set, modelBox));
        } catch (UndefinedParameterError e) {
            e.printStackTrace();
        } catch (OperatorException e) {
            e.printStackTrace();
        }
    }
}
