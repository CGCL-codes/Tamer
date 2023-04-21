package org.xjy.classify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author xjy
 * @version 1.0
 * 
 * */
public class TrainClassifier {

    /** ѵ����λ�� */
    private String trainSetLocation = "";

    /** ѵ�����ģ��λ�� */
    private String modelLocation = "";

    public TrainClassifier(String trainSetLocation, String modelLocation) {
        this.trainSetLocation = trainSetLocation;
        this.modelLocation = modelLocation;
    }

    public void train() {
        train(trainSetLocation, modelLocation);
    }

    private void train(String trainSetLocation, String modelLocation) {
        File trainSetDir = new File(trainSetLocation);
        File modelFile = new File(modelLocation);
        Utility.createFile(modelFile);
        double maxUrlDepth = 0;
        double maxMarkNum = 0;
        double maxLineBlockLen = 0;
        double maxFigureNum = 0;
        File[] fileList = trainSetDir.listFiles();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<DocModel> modelList = new ArrayList<DocModel>();
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelFile, true)));
            for (int i = 0; i < fileList.length; i++) {
                File path = fileList[i];
                String type = path.getName();
                File[] htmlList = path.listFiles();
                for (File trainFile : htmlList) {
                    reader = new BufferedReader(new FileReader(trainFile));
                    String tempString = null;
                    int line = 0;
                    String url = "";
                    StringBuffer content = new StringBuffer("");
                    while ((tempString = reader.readLine()) != null) {
                        line++;
                        if (line == 1) {
                            url = tempString;
                        } else {
                            content.append(tempString + "\n");
                        }
                    }
                    reader.close();
                    ExtractFeature extractFeature = new ExtractFeature();
                    Vector<Double> feature = extractFeature.getFeature(url, content.toString());
                    maxUrlDepth = feature.get(0) > maxUrlDepth ? feature.get(0) : maxUrlDepth;
                    maxMarkNum = feature.get(1) > maxMarkNum ? feature.get(1) : maxMarkNum;
                    maxLineBlockLen = feature.get(2) > maxLineBlockLen ? feature.get(2) : maxLineBlockLen;
                    maxFigureNum = feature.get(3) > maxFigureNum ? feature.get(3) : maxFigureNum;
                    modelList.add(new DocModel(type, feature));
                }
            }
            for (DocModel model : modelList) {
                writer.append(model.getType() + ":" + Utility.normalizeVec(model.getFeatureVector(), maxUrlDepth, maxMarkNum, maxLineBlockLen, maxFigureNum).toString());
                writer.newLine();
                writer.flush();
            }
            writer.append("maxUrlDepth:" + maxUrlDepth);
            writer.newLine();
            writer.append("maxMarkNum:" + maxMarkNum);
            writer.newLine();
            writer.append("maxLineBlockLen:" + maxLineBlockLen);
            writer.newLine();
            writer.append("maxFigureNum:" + maxFigureNum);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        TrainClassifier trainer = new TrainClassifier("E:\\trainSet", "E:\\model.txt");
        trainer.train();
    }
}
