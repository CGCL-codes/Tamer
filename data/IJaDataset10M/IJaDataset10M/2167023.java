package test;

import java.io.*;
import java.util.regex.Pattern;

public class Grep {

    public static void main(String[] args) {
        String dir = args[0];
        String regex = args[1];
        String fileFilterRegex = args.length > 2 ? args[2] : null;
        File searchRoot = new File(dir);
        FileFilter fileFilter = null;
        visit(searchRoot, fileFilter, Pattern.compile(regex), new PrintReceiver());
    }

    private static void visit(File searchRoot, FileFilter fileFilter, Pattern regex, Receiver receiver) {
        for (File child : searchRoot.listFiles()) {
            if (fileFilter == null || fileFilter.accept(child)) {
                if (child.isDirectory()) visit(child, fileFilter, regex, receiver); else scanFile(child, regex, receiver);
            }
        }
    }

    private static void scanFile(File child, Pattern regex, Receiver receiver) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(child), "iso-8859-1"));
            String line;
            while ((line = reader.readLine()) != null) if (line != null && regex.matcher(line).find()) receiver.receive(child.getPath(), line);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    interface Receiver {

        void receive(String file, String line);
    }

    private static class PrintReceiver implements Receiver {

        @Override
        public void receive(String file, String line) {
            System.out.println(file + ": " + line);
        }
    }
}
