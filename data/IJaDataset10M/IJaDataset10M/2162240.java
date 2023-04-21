package cn.edu.thss.iise.beehivez.server.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.importing.pnml.PnmlImport;
import cn.edu.thss.iise.beehivez.server.basicprocess.CTree;
import cn.edu.thss.iise.beehivez.server.basicprocess.CTreeGenerator;
import cn.edu.thss.iise.beehivez.server.basicprocess.mymodel.MyPetriNet;
import cn.edu.thss.iise.beehivez.server.filelogger.FileLogger;
import cn.edu.thss.iise.beehivez.server.metric.BTSSimilarity_Wang;
import cn.edu.thss.iise.beehivez.server.metric.CausalFootprintSimilarity;

public class SimilarityTest {

    public String filepath;

    public SimilarityTest(String path) {
        filepath = path;
    }

    public void BPS_WangTest() {
        FileLogger.deleteLogFile("similarity1.csv");
        File folder = new File(filepath);
        File[] ProcessList = folder.listFiles();
        for (int i = 0; i < ProcessList.length; i++) {
            for (int j = i + 1; j < ProcessList.length; j++) {
                File Process1 = ProcessList[i];
                File Process2 = ProcessList[j];
                FileInputStream pnml1 = null;
                FileInputStream pnml2 = null;
                PetriNet petrinet1 = null;
                PetriNet petrinet2 = null;
                try {
                    pnml1 = new FileInputStream(Process1.getAbsolutePath());
                    pnml2 = new FileInputStream(Process2.getAbsolutePath());
                    PnmlImport pnmlimport = new PnmlImport();
                    petrinet1 = pnmlimport.read(pnml1);
                    petrinet2 = pnmlimport.read(pnml2);
                    BTSSimilarity_Wang bts = new BTSSimilarity_Wang();
                    long startTime = System.nanoTime();
                    double similarity = bts.similarity(petrinet1, petrinet2);
                    long endTime = System.nanoTime();
                    long usedTime = endTime - startTime;
                    String result = Process1.getName() + "," + Process2.getName() + "," + "," + similarity + "," + usedTime;
                    FileLogger.writeLog("similarity1.csv", result);
                    pnml1.close();
                    pnml2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void FootprintTest() {
        File folder = new File(filepath);
        File[] ProcessList = folder.listFiles();
        FileLogger.deleteLogFile("similarity2.csv");
        for (int i = 0; i < ProcessList.length; i++) {
            for (int j = i + 1; j < ProcessList.length; j++) {
                File Process1 = ProcessList[i];
                File Process2 = ProcessList[j];
                FileInputStream pnml1 = null;
                FileInputStream pnml2 = null;
                PetriNet petrinet1 = null;
                PetriNet petrinet2 = null;
                try {
                    pnml1 = new FileInputStream(Process1.getAbsolutePath());
                    pnml2 = new FileInputStream(Process2.getAbsolutePath());
                    PnmlImport pnmlimport = new PnmlImport();
                    petrinet1 = pnmlimport.read(pnml1);
                    petrinet2 = pnmlimport.read(pnml2);
                    CausalFootprintSimilarity footprint = new CausalFootprintSimilarity();
                    long startTime = System.nanoTime();
                    double similarity = footprint.similarity(petrinet1, petrinet2);
                    long endTime = System.nanoTime();
                    long usedTime = endTime - startTime;
                    String result = Process1.getName() + "," + Process2.getName() + "," + similarity + "," + usedTime;
                    FileLogger.writeLog("similarity2.csv", result);
                    pnml1.close();
                    pnml2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SimilarityTest test = new SimilarityTest("a/pnml92");
        test.BPS_WangTest();
        test.FootprintTest();
    }
}
