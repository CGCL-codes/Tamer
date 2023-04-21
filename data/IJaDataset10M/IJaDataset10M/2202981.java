package utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Scanner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import common.OutputRecord;

public class PrintAlignmentsInBEDwithMapfile {

    private static Configuration conf = null;

    static HashMap<String, String> hashmap = new HashMap<String, String>();

    public static void printFile(Path thePath, FileWriter fw) throws IOException {
        SequenceFile.Reader theReader = new SequenceFile.Reader(FileSystem.get(conf), thePath, conf);
        IntWritable key = new IntWritable();
        BytesWritable value = new BytesWritable();
        OutputRecord outrec = new OutputRecord();
        String realkey;
        while (theReader.next(key, value)) {
            outrec.fromBytes(value);
            realkey = new String(outrec.name);
            if (hashmap.containsKey(realkey)) {
                System.out.println("found key = " + realkey);
                outrec.name = hashmap.get(realkey).getBytes();
            }
            fw.write(outrec.toString());
            fw.write("\n");
        }
    }

    public static void processLine(String aLine) {
        Scanner scanner = new Scanner(aLine);
        scanner.useDelimiter(" ");
        if (scanner.hasNext()) {
            String name = scanner.next();
            String value = scanner.next();
            hashmap.put(name.trim(), value.trim());
            hashmap.put(name.trim() + "_L", value.trim());
            hashmap.put(name.trim() + "_R", value.trim());
            System.out.println(name.trim() + "->" + value.trim());
        }
    }

    public static void main(String[] args) throws IOException {
        String filename = null;
        String mapfile = null;
        String outfile = "results_cloud.bed";
        if (filename == null) {
            if (args.length != 3) {
                System.err.println("Usage: PrintAlignmentsInBED seqfile/folder mapfile outfile");
                System.exit(-1);
            }
            filename = args[0];
            mapfile = args[1];
            outfile = args[2];
        }
        Path thePath = new Path(filename);
        conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(filename), conf);
        if (!fs.exists(thePath)) {
            throw new IOException(thePath + " not found");
        }
        Scanner scanner = new Scanner(new FileReader(mapfile));
        try {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
        FileWriter fw = new FileWriter(outfile);
        FileStatus status = fs.getFileStatus(thePath);
        if (status.isDir()) {
            FileStatus[] files = fs.listStatus(thePath);
            for (FileStatus file : files) {
                String str = file.getPath().getName();
                if (str.startsWith(".")) {
                } else if (!file.isDir()) {
                    printFile(file.getPath(), fw);
                }
            }
        } else {
            printFile(thePath, fw);
        }
        fw.close();
    }
}
