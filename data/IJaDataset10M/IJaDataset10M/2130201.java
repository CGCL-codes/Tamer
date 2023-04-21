package sg.edu.nus.iss.se07.da;

import java.io.*;
import java.util.*;
import sg.edu.nus.iss.se07.bc.Discount;
import sg.edu.nus.iss.se07.common.io.FileUtil;
import sg.edu.nus.iss.se07.utils.SystemSetting;

public class DiscountDA {

    private BufferedReader bRead;

    private String filename;

    public BufferedReader getRead() {
        return bRead;
    }

    private static File getFile() throws IOException {
        String dir = FileUtil.getBaseDirectory("");
        if (!dir.endsWith("\\")) {
            dir = dir + "\\";
        }
        dir = dir + SystemSetting.getProperty("data.folder");
        File f = new File(dir);
        return f;
    }

    public void read() {
        try {
            filename = getFile().getPath().concat("\\Discounts.dat");
            FileReader fread = new FileReader(filename);
            bRead = new BufferedReader(fread);
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
    }

    public void save(Iterator<Discount> i) {
        try {
            FileWriter w = new FileWriter(filename);
            PrintWriter pr = new PrintWriter(w);
            while (i.hasNext()) {
                Discount d = i.next();
                pr.print(d.getDiscountCode() + ",");
                pr.print(d.getDescription() + ",");
                pr.print(d.getStartDate() + ",");
                pr.print(d.getPeriod() + ",");
                pr.print(d.getPercentage() + ",");
                if (i.hasNext()) {
                    pr.println(d.getStatus());
                } else {
                    pr.print(d.getStatus());
                }
            }
            pr.close();
        } catch (IOException e) {
        }
    }
}
