package guirequest;

import guireportgenerator.MaxId;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Hashtable;
import utility.CapitalChar;
import utility.Splitstring;
import control.Control;
import dbmanager.*;

public class WebPageRegistration {

    String Username;

    String path;

    char NextSpChar = '|';

    static Hashtable<Object, Object> Objects = new Hashtable<Object, Object>();

    public WebPageRegistration(String Username, String path) {
        this.Username = Username;
        this.path = path;
    }

    public String generateMasterName(String request) {
        String result = null;
        try {
            String objectname = null;
            String objstr = null;
            String max_id = new MaxId().getMaxId();
            Hashtable<Object, Object> temphash = new Hashtable<Object, Object>();
            Splitstring split = new Splitstring();
            String requestArr[] = split.splitString(request);
            String type = null;
            for (int i = 0; i < request.length(); i++) {
                if (request.charAt(i) == NextSpChar) {
                    String tempstr = request.substring(i, request.indexOf(String.valueOf("!")));
                    System.out.println("The temp str is " + tempstr);
                    String temparr[] = tempstr.split(":");
                    temphash.put(temparr[0], temparr[1]);
                }
            }
            for (int i = 0; i < requestArr.length; i = i + 2) {
                System.out.println("The arr element is " + requestArr[i]);
                if (requestArr[i].equalsIgnoreCase("tagtype") || requestArr[i].equalsIgnoreCase("type")) {
                    if (requestArr[i + 1].equalsIgnoreCase("webreport")) {
                    } else {
                        type = CapitalChar.makeFirstCharCapital(requestArr[i + 1]);
                    }
                }
            }
            int indexOfPipe = request.indexOf(String.valueOf("|"));
            int indexOfstar = request.indexOf(String.valueOf("*"));
            String oldobjname = request.substring(indexOfstar + 1, indexOfPipe);
            System.out.println("The oldobjname is " + oldobjname);
            String proprequest = request.substring(indexOfPipe, request.length());
            System.out.println("The req is " + proprequest);
            Objects.put(oldobjname, proprequest);
            result = "nm*" + type + max_id + proprequest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void Register() {
        try {
            String request;
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            File file = new File("C://responces.txt");
            file.delete();
            file.createNewFile();
            RandomAccessFile raft = new RandomAccessFile(file, "rw");
            Hashtable<Object, Object> h1 = new Hashtable<Object, Object>();
            Hashtable<Object, Object> h2 = new Hashtable<Object, Object>();
            while ((request = raf.readLine()) != null) {
                String myrequest = this.generateMasterName(request);
                System.out.println("The myrequest is " + myrequest);
                if (Username.startsWith("TO:") == false) {
                    Control control = new Control(Username, myrequest);
                    h1 = control.messageProcessing();
                    StringBuffer response = new StringBuffer();
                    if (h1.containsKey("ErrorUser") == false) {
                        h2 = control.requestProcess();
                        response.append(h2);
                        h1.clear();
                        h2.clear();
                    } else {
                        System.out.println("Unable to Find user");
                        h1.put("ErrorUser", "Unable to Find user or password");
                        response.append(h1);
                    }
                    raft.writeBytes("------------------------------------------------------------\n");
                    raft.writeBytes(myrequest + "\n");
                    raft.writeBytes(response.toString() + "\n");
                    raft.writeBytes("------------------------------------------------------------\n");
                } else {
                    Username = Username.substring(Username.indexOf(':'));
                    System.out.println("control::ClientProcess::run()::Message Delivery Report for user " + Username);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebPageRegistration rf = new WebPageRegistration("+919922918605", "D:\\TextFiles\\Registration Document\\WebReportRegistration.txt");
        System.out.println(rf.generateMasterName("nm*IdField|type:field!0|text:id!1#605"));
    }
}
