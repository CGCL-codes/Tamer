package com.google.htmm.arraybased.refactored;

import java.io.FileNotFoundException;
import java.io.IOException;

public class HTMMDriver {

    public static void main(String[] args) throws IOException {
        if (args.length != 9) {
            System.err.print("Usage: " + args[0] + " topics words alpha beta iters train_file out_file working_dir\n");
            System.exit(1);
        }
        int topics = Integer.parseInt(args[1]);
        int words = Integer.parseInt(args[2]);
        double alpha = Double.parseDouble(args[3]);
        double beta = Double.parseDouble(args[4]);
        int iters = Integer.parseInt(args[5]);
        String train = args[6];
        String out_file = args[7];
        String working_dir = args[8];
        EM em = new EM();
        em.init(topics, words, alpha, beta, iters, train, working_dir, 0);
        em.infer();
        em.SaveAll(out_file);
    }
}
