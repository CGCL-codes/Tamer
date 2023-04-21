package net.emotivecloud.vrmm.dm;

import org.apache.commons.configuration.PropertiesConfiguration;
import es.bsc.brein.jsdl.DataStaging;

public class Tester {

    public static void main(String[] args) {
        DataManagement data = new DataManagement();
        data.setLocalLocation("/tmp");
        try {
            System.out.println("Change permissions");
            DataStaging file2 = new DataStaging();
            file2.setFileName("filetest.txt");
            file2.setTargetURL("s3://breintest2/filetest.txt");
            data.uploadFile("newjobid", file2);
            DataStaging file = new DataStaging();
            file.setFileName("finalfile.txt");
            file.setSourceURL("s3://breintest2/filetest.txt");
            data.downloadFile("newjobid2", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
