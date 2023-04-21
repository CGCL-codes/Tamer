package models;

import game.configuration.ClassWithConfigBean;
import game.data.FileGameData;
import game.data.GameData;
import game.models.Model;
import game.models.ModelLearnable;
import game.models.Models;
import game.models.single.PolynomialModel;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;
import configuration.ConfigurationFactory;
import configuration.game.trainers.QuasiNewtonConfig;
import configuration.models.single.PolynomialModelConfig;

/**
 * This class performs junit test of the configuration mechansm used by the ame core
 */
public class ModelSaveLoadTest {

    private static final String cfgfilename = "cfg/poly2.properties";

    private static final String datafilename = "data/iris.txt";

    static ModelLearnable model;

    static Models models;

    static ClassWithConfigBean generatedCfg;

    static GameData data;

    static FileInputStream fis;

    static DataInputStream dis;

    static FileOutputStream fos;

    static DataOutputStream dos;

    @BeforeClass
    public static void generateConfigurationOfModelsAndLoadData() {
        PolynomialModelConfig pmc = new PolynomialModelConfig();
        pmc.setTrainerClassName("QuasiNewtonTrainer");
        pmc.setTrainerCfg(new QuasiNewtonConfig());
        pmc.setMaxDegree(5);
        generatedCfg = new ClassWithConfigBean(PolynomialModel.class, pmc);
        ConfigurationFactory.saveConfiguration(generatedCfg, cfgfilename);
        data = new FileGameData(datafilename);
    }

    @Test
    public void testConfigurationOfModels() {
        ClassWithConfigBean conf;
        conf = (ClassWithConfigBean) ConfigurationFactory.getConfigurationObject(cfgfilename);
        for (int i = 0; i < data.getONumber(); i++) {
            PolynomialModelConfig pmc = (PolynomialModelConfig) conf.getCfgBean();
            pmc.setTargetVariable(i);
            Models.getInstance().createNewModel(conf);
        }
    }

    @Test
    public void saveModels() {
        try {
            fos = new FileOutputStream("iris-poly5.net");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dos = new DataOutputStream(fos);
        ArrayList<Model> m = new ArrayList<Model>();
        Model[] mo = Models.getInstance().getModelsArray();
        for (int i = 0; i < mo.length; i++) m.add(mo[i]);
        Models.saveModelsToXMLStream(dos, m);
        try {
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadModels() {
        try {
            fis = new FileInputStream("iris-poly5.net");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dis = new DataInputStream(fis);
        ArrayList<Model> models = Models.loadModelsfromXMLStream(dis);
        for (int i = 0; i < models.size(); i++) Models.getInstance().storeModelAt(models.get(i), i);
    }
}
