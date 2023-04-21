package edu.uic.cs.gpu.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import edu.uic.cs.gpu.Config;
import edu.uic.cs.gpu.Main;
import edu.uic.cs.gpu.util.Measure;

public class PubChem extends Main {

    public static String type = "";

    public static int choice = 2;

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        int option = Integer.parseInt(args[1]);
        if (option == 0) {
            PubChem ptc = new PubChem();
            ptc.init(args[0], 1, true);
            Measure m1 = ptc.run();
            System.out.println("------------------------------------------");
            ptc = new PubChem();
            ptc.init(args[0], 3, true);
            Measure m2 = ptc.run();
        } else if (option == 1) PubChem.test_gamma(args[0]); else if (option == 2) PubChem.test_support(args[0]); else if (option == 3) PubChem.test_feature(args[0]);
    }

    public static void test_gamma(String t) throws Exception {
        System.out.println("test_accuracy_ratio");
        int times = Config.test_times;
        Measure[][] result = new Measure[10][2];
        for (int i = 0; i < 9; i++) {
            Config.positive_raito = i * 0.1 + 0.1;
            for (int j = 0; j < times; j++) {
                Config.random_seed = (long) (Math.random() * Long.MAX_VALUE);
                System.out.println("!!!Baseline " + i);
                PubChem ptc = new PubChem();
                ptc.init(t, 1, false);
                Measure m1 = ptc.run();
                if (result[i][0] == null) result[i][0] = m1; else result[i][0].addMeasure(m1);
                System.out.println("------------------------------");
                System.out.println("!!!GPU " + i);
                ptc = new PubChem();
                ptc.init(t, 2, true);
                Measure m2 = ptc.run_with_hsic_pruning();
                if (result[i][1] == null) result[i][1] = m2; else result[i][1].addMeasure(m2);
                System.out.println("------------------------------");
                System.out.println("------------------------------");
            }
        }
        System.out.println("--------------------f score------------------------------");
        System.out.println("gamma \t baseline \t gpu");
        for (int i = 0; i < 9; i++) {
            double p = (double) result[i][0].A / (result[i][0].A + result[i][0].C);
            double r = (double) result[i][0].A / (result[i][0].A + result[i][0].B);
            double f1 = 2 * p * r / (p + r);
            p = (double) result[i][1].A / (result[i][1].A + result[i][1].C);
            r = (double) result[i][1].A / (result[i][1].A + result[i][1].B);
            double f2 = 2 * p * r / (p + r);
            System.out.println("0." + (i + 1) + "\t" + f1 + "\t" + f2);
        }
        System.out.println("--------------------accuracy------------------------------");
        System.out.println("gamma \t baseline \t gpu");
        for (int i = 0; i < 9; i++) {
            System.out.println("0." + (i + 1) + "\t" + result[i][0].accuracy / times + "\t" + result[i][1].accuracy / times);
        }
        System.out.println("--------------------running time------------------------------");
        System.out.println("gamma \t baseline \t gpu");
        for (int i = 0; i < 9; i++) {
            System.out.println("0." + (i + 1) + "\t" + result[i][0].time / times + "\t" + result[i][1].time / times);
        }
    }

    public static void test_support(String t) throws Exception {
        System.out.println("test_support");
        int times = Config.test_times;
        int start = 4;
        int end = 20;
        int incre = 4;
        int num = (end - start) / incre + 1;
        Measure[][] result = new Measure[num][2];
        for (int i = 0; i < num; i++) {
            Config.minimum_support = (start + i * incre) + "%";
            for (int j = 0; j < times; j++) {
                Config.random_seed = (long) (Math.random() * Long.MAX_VALUE);
                PubChem ptc = new PubChem();
                ptc.init(t, 1, false);
                Measure m1 = ptc.run();
                if (result[i][0] == null) result[i][0] = m1; else result[i][0].addMeasure(m1);
                System.out.println("------------------------------");
                ptc = new PubChem();
                ptc.init(t, 2, true);
                Measure m2 = ptc.run();
                if (result[i][1] == null) result[i][1] = m2; else result[i][1].addMeasure(m2);
                System.out.println("------------------------------");
                System.out.println("------------------------------");
            }
        }
        System.out.println("--------------------f score------------------------------");
        System.out.println("support \t baseline \t gpu");
        for (int i = 0; i < num; i++) {
            double p = (double) result[i][0].A / (result[i][0].A + result[i][0].C);
            double r = (double) result[i][0].A / (result[i][0].A + result[i][0].B);
            double f1 = 2 * p * r / (p + r);
            p = (double) result[i][1].A / (result[i][1].A + result[i][1].C);
            r = (double) result[i][1].A / (result[i][1].A + result[i][1].B);
            double f2 = 2 * p * r / (p + r);
            System.out.println((start + i * incre) + "" + "\t" + f1 + "\t" + f2);
        }
        System.out.println("--------------------accuracy------------------------------");
        System.out.println("gamma \t baseline \t gpu");
        for (int i = 0; i < num; i++) {
            System.out.println((start + i * incre) + "" + "\t" + result[i][0].accuracy / times + "\t" + result[i][1].accuracy / times);
        }
        System.out.println("--------------------running time------------------------------");
        System.out.println("gamma \t baseline \t gpu");
        for (int i = 0; i < num; i++) {
            System.out.println((start + i * incre) + "" + "\t" + result[i][0].time / times + "\t" + result[i][1].time / times);
        }
    }

    public static void test_feature(String t) throws Exception {
        System.out.println("test_accuracy_ratio");
        int times = Config.test_times;
        int start = 20;
        int end = 40;
        int incre = 5;
        int num = (end - start) / incre + 1;
        Measure[][] result = new Measure[num][2];
        for (int i = 0; i < num; i++) {
            Config.feature_top_t = (start + i * incre);
            for (int j = 0; j < times; j++) {
                Config.random_seed = (long) (Math.random() * Long.MAX_VALUE);
                PubChem ptc = new PubChem();
                ptc.init(t, 1, false);
                Measure m1 = ptc.run();
                if (result[i][0] == null) result[i][0] = m1; else result[i][0].addMeasure(m1);
                ptc = new PubChem();
                ptc.init(t, 2, true);
                Measure m2 = ptc.run();
                if (result[i][1] == null) result[i][1] = m2; else result[i][1].addMeasure(m2);
            }
        }
        System.out.println("--------------------f score------------------------------");
        System.out.println("features \t baseline \t gpu");
        for (int i = 0; i < num; i++) {
            double p = (double) result[i][0].A / (result[i][0].A + result[i][0].C);
            double r = (double) result[i][0].A / (result[i][0].A + result[i][0].B);
            double f1 = 2 * p * r / (p + r);
            p = (double) result[i][1].A / (result[i][1].A + result[i][1].C);
            r = (double) result[i][1].A / (result[i][1].A + result[i][1].B);
            double f2 = 2 * p * r / (p + r);
            System.out.println((start + i * incre) + "\t" + f1 + "\t" + f2);
        }
        System.out.println("--------------------accuracy------------------------------");
        System.out.println("features \t baseline \t gpu");
        for (int i = 0; i < num; i++) {
            System.out.println((start + i * incre) + "\t" + result[i][0].accuracy / times + "\t" + result[i][1].accuracy / times);
        }
        System.out.println("--------------------running time------------------------------");
        System.out.println("features \t baseline \t gpu");
        for (int i = 0; i < num; i++) {
            System.out.println((start + i * incre) + "\t" + result[i][0].time / times + "\t" + result[i][1].time / times);
        }
    }

    public ArrayList<String> getPositiveSet() {
        String fileName = Config.chem_positive.replace("name", type);
        return getSetFromFile(fileName);
    }

    public ArrayList<String> getNegativeSet() {
        String fileName = Config.chem_negative.replace("name", type);
        return getSetFromFile(fileName);
    }

    public void init(String t, int c, boolean w) {
        type = t;
        setChoice(c);
        Config.if_use_weights = w;
        Config.gSpan_file_parser = "de.parmol.parsers.LineGraphParser";
        Config.chem_graph_file = Config.chem_graph_file.replace("name", type);
        graph_file = Config.chem_graph_file;
    }

    public void init(String[] args) {
        type = args[0];
        setChoice(Integer.parseInt(args[1]));
        Config.gSpan_file_parser = "de.parmol.parsers.LineGraphParser";
        Config.chem_graph_file = Config.chem_graph_file.replace("name", type);
        graph_file = Config.chem_graph_file;
    }

    private ArrayList<String> getSetFromFile(String s) {
        File file = new File(s);
        ArrayList<String> result = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String oneLine;
            while ((oneLine = br.readLine()) != null) {
                if (oneLine.trim().equals("")) continue;
                result.add(oneLine.split("\t")[0]);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }
}
