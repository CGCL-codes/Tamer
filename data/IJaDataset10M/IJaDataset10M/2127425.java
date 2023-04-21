package test;

import com.ubu.psg.logging.LogModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.openide.util.Exceptions;

/**
 * Document   : ReadLogging
 * Created on : 26 ก.พ. 2551, 23:07:03
 * @author ATHAPOL
 */
public class ReadLogging {

    public static void main(String[] args) {
        FileReader fr = null;
        Stack<String> log = new Stack<String>();
        try {
            File f = new File("ProgramStructureGraph.log");
            fr = new FileReader(f);
            BufferedReader buffer = new BufferedReader(fr);
            String msg;
            while ((msg = buffer.readLine()) != null) {
                log.push(msg);
            }
            setFormatLog(log);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public static void setFormatLog(Stack<String> log) {
        ArrayList<LogModel> logModels = new ArrayList<LogModel>();
        String msg[];
        while (!log.empty()) {
            msg = log.pop().split(" ");
            LogModel logModel = new LogModel();
            logModel.setLevel(msg[0]);
            logModel.setModule(msg[1]);
            logModel.setDate(msg[2]);
            logModel.setTime(msg[3]);
            String message = "";
            for (int i = 4; i < msg.length; i++) {
                message += msg[i] + " ";
            }
            logModel.setMessage(message);
            logModels.add(logModel);
        }
    }
}
