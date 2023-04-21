package edu.colorado.emml.util.latex;

public class CSVToLatexTable {

    public static void main(String[] args) {
        String csv = "adaboost-500\t0.052374482\t0\t0.023716006\t0.002400778\t0.013871353\t0\t\t0.09236262\n" + "adaboost-bin-500\t0.063387691\t0\t0.013791091\t0.000383186\t0.022087396\t0\t\t0.099649364\n" + "ann-0.5-32-1000\t0\t0\t0.060852099\t0.035395209\t0\t0.003594664\t\t0.099841972\n" + "ann-0.5-16-500\t0.039297889\t0\t0\t0.017934075\t0.009392052\t0.034082947\t\t0.100706962\n" + "ann-0.9-16-500\t0.002233745\t0.082306904\t0\t0\t0.00667243\t0.016380791\t\t0.10759387\n" + "ann-0.5-32-500\t3.14E-06\t0.074722846\t0\t0.009807069\t0.026761155\t0\t\t0.111294212\n" + "knn-1\t0\t0\t0.075946549\t0.064640585\t0.008350051\t0.097191726\t\t0.246128911";
        csv = csv.replaceAll("\t\t", "\t");
        csv = csv.replaceAll("\t", "&");
        csv = csv.replaceAll("\n", "\\\\\\\\\n");
        System.out.println("csv = \n" + csv);
    }
}
