package svm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.StringTokenizer;

class SvmScale {

    private String line = null;

    private double lower = -1.0;

    private double upper = 1.0;

    private double y_lower;

    private double y_upper;

    private boolean y_scaling = false;

    private double[] feature_max;

    private double[] feature_min;

    private double y_max = -Double.MAX_VALUE;

    private double y_min = Double.MAX_VALUE;

    private int max_index;

    private static void exit_with_help() {
        System.out.print("Usage: svm-scale [options] data_filename\n" + "options:\n" + "-l lower : x scaling lower limit (default -1)\n" + "-u upper : x scaling upper limit (default +1)\n" + "-y y_lower y_upper : y scaling limits (default: no y scaling)\n" + "-s save_filename : save scaling parameters to save_filename\n" + "-r restore_filename : restore scaling parameters from restore_filename\n");
        System.exit(1);
    }

    private BufferedReader rewind(BufferedReader fp, String filename) throws IOException {
        fp.close();
        return new BufferedReader(new FileReader(filename));
    }

    private void output_target(double value) {
        if (y_scaling) {
            if (value == y_min) value = y_lower; else if (value == y_max) value = y_upper; else value = y_lower + (y_upper - y_lower) * (value - y_min) / (y_max - y_min);
        }
        System.out.print(value + " ");
    }

    private void output(int index, double value) {
        if (feature_max[index] == feature_min[index]) return;
        if (value == feature_min[index]) value = lower; else if (value == feature_max[index]) value = upper; else value = lower + (upper - lower) * (value - feature_min[index]) / (feature_max[index] - feature_min[index]);
        if (value != 0) System.out.print(index + ":" + value + " ");
    }

    private String readline(BufferedReader fp) throws IOException {
        line = fp.readLine();
        return line;
    }

    private void run(String[] argv) throws IOException {
        int i, index;
        BufferedReader fp = null;
        String save_filename = null;
        String restore_filename = null;
        String data_filename = null;
        for (i = 0; i < argv.length; i++) {
            if (argv[i].charAt(0) != '-') break;
            ++i;
            switch(argv[i - 1].charAt(1)) {
                case 'l':
                    lower = Double.parseDouble(argv[i]);
                    break;
                case 'u':
                    upper = Double.parseDouble(argv[i]);
                    break;
                case 'y':
                    y_lower = Double.parseDouble(argv[i]);
                    ++i;
                    y_upper = Double.parseDouble(argv[i]);
                    y_scaling = true;
                    break;
                case 's':
                    save_filename = argv[i];
                    break;
                case 'r':
                    restore_filename = argv[i];
                    break;
                default:
                    System.err.println("unknown option");
                    exit_with_help();
            }
        }
        if (!(upper > lower) || (y_scaling && !(y_upper > y_lower))) {
            System.err.println("inconsistent lower/upper specification");
            System.exit(1);
        }
        if (argv.length != i + 1) exit_with_help();
        data_filename = argv[i];
        try {
            fp = new BufferedReader(new FileReader(data_filename));
        } catch (Exception e) {
            System.err.println("can't open file " + data_filename);
            System.exit(1);
        }
        max_index = 0;
        while (readline(fp) != null) {
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            st.nextToken();
            while (st.hasMoreTokens()) {
                index = Integer.parseInt(st.nextToken());
                max_index = Math.max(max_index, index);
                st.nextToken();
            }
        }
        try {
            feature_max = new double[(max_index + 1)];
            feature_min = new double[(max_index + 1)];
        } catch (OutOfMemoryError e) {
            System.err.println("can't allocate enough memory");
            System.exit(1);
        }
        for (i = 0; i <= max_index; i++) {
            feature_max[i] = -Double.MAX_VALUE;
            feature_min[i] = Double.MAX_VALUE;
        }
        fp = rewind(fp, data_filename);
        while (readline(fp) != null) {
            int next_index = 1;
            double target;
            double value;
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            target = Double.parseDouble(st.nextToken());
            y_max = Math.max(y_max, target);
            y_min = Math.min(y_min, target);
            while (st.hasMoreTokens()) {
                index = Integer.parseInt(st.nextToken());
                value = Double.parseDouble(st.nextToken());
                for (i = next_index; i < index; i++) {
                    feature_max[i] = Math.max(feature_max[i], 0);
                    feature_min[i] = Math.min(feature_min[i], 0);
                }
                feature_max[index] = Math.max(feature_max[index], value);
                feature_min[index] = Math.min(feature_min[index], value);
                next_index = index + 1;
            }
            for (i = next_index; i <= max_index; i++) {
                feature_max[i] = Math.max(feature_max[i], 0);
                feature_min[i] = Math.min(feature_min[i], 0);
            }
        }
        fp = rewind(fp, data_filename);
        if (restore_filename != null) {
            BufferedReader fp_restore = null;
            try {
                fp_restore = new BufferedReader(new FileReader(restore_filename));
            } catch (Exception e) {
                System.err.println("can't open file " + restore_filename);
                System.exit(1);
            }
            int idx, c;
            double fmin, fmax;
            fp_restore.mark(2);
            if ((c = fp_restore.read()) == 'y') {
                fp_restore.readLine();
                StringTokenizer st = new StringTokenizer(fp_restore.readLine());
                y_lower = Double.parseDouble(st.nextToken());
                y_upper = Double.parseDouble(st.nextToken());
                st = new StringTokenizer(fp_restore.readLine());
                y_min = Double.parseDouble(st.nextToken());
                y_max = Double.parseDouble(st.nextToken());
                y_scaling = true;
            } else fp_restore.reset();
            if (fp_restore.read() == 'x') {
                fp_restore.readLine();
                StringTokenizer st = new StringTokenizer(fp_restore.readLine());
                lower = Double.parseDouble(st.nextToken());
                upper = Double.parseDouble(st.nextToken());
                String restore_line = null;
                while ((restore_line = fp_restore.readLine()) != null) {
                    StringTokenizer st2 = new StringTokenizer(restore_line);
                    idx = Integer.parseInt(st2.nextToken());
                    fmin = Double.parseDouble(st2.nextToken());
                    fmax = Double.parseDouble(st2.nextToken());
                    if (idx <= max_index) {
                        feature_min[idx] = fmin;
                        feature_max[idx] = fmax;
                    }
                }
            }
            fp_restore.close();
        }
        if (save_filename != null) {
            Formatter formatter = new Formatter(new StringBuilder());
            BufferedWriter fp_save = null;
            try {
                fp_save = new BufferedWriter(new FileWriter(save_filename));
            } catch (IOException e) {
                System.err.println("can't open file " + save_filename);
                System.exit(1);
            }
            if (y_scaling) {
                formatter.format("y\n");
                formatter.format("%.16g %.16g\n", y_lower, y_upper);
                formatter.format("%.16g %.16g\n", y_min, y_max);
            }
            formatter.format("x\n");
            formatter.format("%.16g %.16g\n", lower, upper);
            for (i = 1; i <= max_index; i++) {
                if (feature_min[i] != feature_max[i]) formatter.format("%d %.16g %.16g\n", i, feature_min[i], feature_max[i]);
            }
            fp_save.write(formatter.toString());
            fp_save.close();
        }
        while (readline(fp) != null) {
            int next_index = 1;
            double target;
            double value;
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            target = Double.parseDouble(st.nextToken());
            output_target(target);
            while (st.hasMoreElements()) {
                index = Integer.parseInt(st.nextToken());
                value = Double.parseDouble(st.nextToken());
                for (i = next_index; i < index; i++) output(i, 0);
                output(index, value);
                next_index = index + 1;
            }
            for (i = next_index; i <= max_index; i++) output(i, 0);
            System.out.print("\n");
        }
        fp.close();
    }

    public static void main(String argv[]) throws IOException {
        SvmScale s = new SvmScale();
        s.run(argv);
    }
}
