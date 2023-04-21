package edu.centenary.centenaryController;

import java.util.*;
import java.io.*;

public class Njoin {

    public static ArrayList<TreeNode> bodyParts = new ArrayList<TreeNode>();

    public static void main(String[] args) throws IOException, FileNotFoundException {
        Scanner scan = new Scanner(new File(args[0]));
        while (scan.hasNext()) {
            BodyPart bp = new BodyPart(scan.next(), scan.nextDouble(), scan.nextDouble(), scan.nextDouble());
            bodyParts.add(new TreeNode(bp));
        }
        while (bodyParts.size() > 2) {
            double[][] distmatrix = new double[bodyParts.size()][bodyParts.size()];
            double[] divarray = new double[bodyParts.size()];
            for (int i = 0; i < bodyParts.size(); i++) {
                int j = 0;
                for (TreeNode bp : bodyParts) {
                    distmatrix[i][j] = bp.distance(bodyParts.get(i));
                    divarray[i] += distmatrix[i][j];
                    j++;
                }
            }
            for (int i = 0; i < distmatrix.length; i++) {
                for (int j = 0; j < distmatrix[i].length; j++) {
                    System.out.print(distmatrix[i][j] + " ");
                }
                System.out.println();
            }
            for (int i = 0; i < divarray.length; i++) {
                System.out.println("Divergence " + i + " is " + divarray[i]);
            }
            double[][] mmatrix = new double[bodyParts.size()][bodyParts.size()];
            double minimum = Integer.MAX_VALUE;
            int mincoordi = 0;
            int mincoordj = 0;
            for (int i = 0; i < distmatrix.length; i++) {
                for (int j = 0; j < distmatrix[i].length; j++) {
                    if (i != j) {
                        mmatrix[i][j] = distmatrix[i][j] - (divarray[i] + divarray[j]) / (bodyParts.size() - 2);
                        if (mmatrix[i][j] < minimum) {
                            minimum = mmatrix[i][j];
                            mincoordi = i;
                            mincoordj = j;
                        }
                    }
                }
            }
            System.out.println("Minimum coordinate is " + mincoordi + "," + mincoordj + " with distance " + minimum);
            TreeNode temp = new TreeNode(bodyParts.get(mincoordi), bodyParts.get(mincoordj));
            bodyParts.remove(Math.max(mincoordi, mincoordj));
            bodyParts.remove(Math.min(mincoordi, mincoordj));
            bodyParts.add(temp);
        }
        TreeNode root = new TreeNode(bodyParts.get(0), bodyParts.get(1));
        ArrayList<TreeNode> unsplit = new ArrayList<TreeNode>();
        ArrayList<TreeNode> group = new ArrayList<TreeNode>();
        ArrayList<TreeNode> bum = new ArrayList<TreeNode>();
        unsplit.add(root);
        while (unsplit.size() > 0) {
            TreeNode t = unsplit.remove(0);
            if (t.numChests() == 0) {
                bum.add(t);
            } else if (t.numChests() == 1) {
                group.add(t);
            } else {
                unsplit.add(t.left());
                unsplit.add(t.right());
            }
        }
        for (TreeNode t : group) {
            System.out.println("New Group ");
            System.out.println(t);
            System.out.println("----------------------------");
        }
    }
}
