package PINFinal;

import java.util.Scanner;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class FileHelper {

    public static ArrayList<String> readFile(String path) {
        ArrayList<String> text = new ArrayList<String>();
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(path));
            while (in.hasNextLine()) text.add(in.nextLine());
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null) in.close();
        }
        if (text.size() > 0) for (int i = (text.size() - 1); text.get(i).length() < 1 && i > 0; i--) text.remove(i);
        return text;
    }

    public static void writeFile(String path, ArrayList<String> text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(path);
            if (text != null) for (String s : text) out.println(s);
        } catch (IOException e) {
        } finally {
            out.close();
        }
    }

    public static void deleteFile(String path) {
        File f = new File(path);
        f.delete();
    }
}
