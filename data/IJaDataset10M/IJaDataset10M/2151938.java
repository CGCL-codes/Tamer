package request_files;

import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.NVPair;

public class Upload_Files {

    public static void upload(String[] files_to_upload, String username, String password, String IDnumber) {
        try {
            HTTPConnection connect = new HTTPConnection("https", "creme96.nrl.navy.mil", -1);
            NVPair[] form_data = new NVPair[] { new NVPair("version", "cm"), new NVPair("username", username), new NVPair("password", password) };
            HTTPResponse rsp = connect.Post("/cgi-shl/cm/main_menu.pl", form_data);
            NVPair[] util_data = new NVPair[] { new NVPair("runutilities", "1"), new NVPair("version", "cm"), new NVPair("id", IDnumber), new NVPair("form", "menu") };
            rsp = connect.Post("/cgi-shl/cm/run_or_edit.pl", util_data);
            NVPair[] upload = new NVPair[] { new NVPair("util", "upload"), new NVPair("version", "cm"), new NVPair("id", IDnumber), new NVPair("form", "utility") };
            rsp = connect.Post("/cgi-shl/cm/utilities.pl", upload);
            for (int z = 0; z < files_to_upload.length; z++) {
                NVPair[] upload_data = new NVPair[] { new NVPair("upfiles", files_to_upload[z]), new NVPair("version", "cm"), new NVPair("id", IDnumber) };
                rsp = connect.Post("/cgi-shl/cm/move_files.pl", upload_data);
                String temp = rsp.getText();
                if (temp.contains("Errors during transfer")) {
                    temp = temp.substring(temp.indexOf("Errors during transfer"), temp.indexOf("</ol"));
                    temp = temp.replaceAll("</b><ol>", "");
                    temp = temp.replaceAll("<li>", "");
                    System.out.println("ERROR!!!!!: " + temp);
                }
                if (temp.contains("Files transferred")) {
                    temp = temp.substring(temp.indexOf("Files transferred"), temp.indexOf("</ol"));
                    temp = temp.replaceAll("</b><ol>", "");
                    temp = temp.replaceAll("<li>", "");
                    System.out.println(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        String[] whattopass = { "sample_flux3.rqf", "sample_flux4.rqf", "sample_flux5.rqf" };
        upload(whattopass, "josh8571", "josh8571", "3015");
    }
}
