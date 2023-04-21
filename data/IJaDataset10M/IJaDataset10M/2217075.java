package edu.centenary.centenaryController;

import java.util.*;
import java.io.*;

public class Kmeans {

    private static int k = 0;

    private static ArrayList<BodyPart> bodyParts = new ArrayList<BodyPart>();

    public Kmeans(int k, ArrayList<BodyPart> bodyParts) {
        this.k = k;
        this.bodyParts = bodyParts;
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        Scanner scan = new Scanner(new File(args[0]));
        while (scan.hasNext()) {
            BodyPart bp = new BodyPart(scan.next(), scan.nextDouble(), scan.nextDouble(), scan.nextDouble());
            bodyParts.add(bp);
        }
        int ch = 0;
        for (BodyPart bp : bodyParts) {
            if (bp.getBodyPart().equals("Chest")) {
                ch++;
            }
        }
        System.out.println("There are " + ch + " Chests");
        double mindistortion = Double.MAX_VALUE;
        int mink = k;
        for (k = ch; k <= ch; k++) {
            BodyPart[] centers = new BodyPart[k];
            int[] r = new int[bodyParts.size()];
            for (int i = 0; i < r.length; i++) {
                r[i] = i;
            }
            for (int i = 0; i < k; i++) {
                int rand = (int) (Math.random() * (r.length - i)) + i;
                int p = r[i];
                r[i] = r[rand];
                r[rand] = p;
                BodyPart bp = bodyParts.get(r[i]);
                centers[i] = new BodyPart("CENTER", bp.getLocationX(), bp.getLocationY(), bp.getLocationZ());
            }
            boolean changed = true;
            while (changed) {
                changed = false;
                int j = 0;
                for (BodyPart bp : bodyParts) {
                    double minimum = bp.distance(centers[0]);
                    int c = bp.getCenter();
                    for (int i = 0; i < k; i++) {
                        if (bp.distance(centers[i]) < minimum) {
                            minimum = bp.distance(centers[i]);
                            bp.setCenter(i);
                            c = bp.getCenter();
                        }
                    }
                    bp.setGroup(c);
                    j++;
                }
                for (int i = 0; i < k; i++) {
                    centers[i] = new BodyPart("CENTER", 0, 0, 0);
                }
                int[] members = new int[k];
                for (BodyPart bp : bodyParts) {
                    int group = bp.getGroup();
                    centers[group].setLocationX(centers[group].getLocationX() + bp.getLocationX());
                    centers[group].setLocationY(centers[group].getLocationY() + bp.getLocationY());
                    centers[group].setLocationZ(centers[group].getLocationZ() + bp.getLocationZ());
                    members[group] += 1;
                }
                for (int i = 0; i < k; i++) {
                    centers[i].setLocationX(centers[i].getLocationX() / members[i]);
                    centers[i].setLocationY(centers[i].getLocationY() / members[i]);
                    centers[i].setLocationZ(centers[i].getLocationZ() / members[i]);
                }
                int i = 0;
                for (BodyPart bp : bodyParts) {
                    if (bp.getDirty() == true) {
                        changed = true;
                        bp.setDirty(false);
                    }
                    i++;
                }
            }
            double distortion = 0;
            for (BodyPart bp : bodyParts) {
                distortion += Math.pow(bp.distance(centers[bp.getGroup()]) / bodyParts.size(), 2);
            }
            if (distortion <= mindistortion) {
                mindistortion = distortion;
                mink = k;
            }
            int ik = 0;
            for (BodyPart bp : bodyParts) {
                System.out.println("Bodypart " + ik + " is part of group " + bp.getGroup());
                ik++;
            }
            for (int j = 0; j < k; j++) {
            }
            System.out.println("Distortion for " + k + " is " + distortion);
        }
        System.out.println("Minimum Distortion is " + mindistortion + " for k " + mink);
    }
}
