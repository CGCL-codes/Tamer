package uk.ac.ncl.cs.instantsoap;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: louis
 * Date: 28-Nov-2006
 * Time: 21:01:42
 * To change this template use File | Settings | File Templates.
 */
public class TerminalEmulator {

    public static void main(String[] arguments) {
        InterproRequestProcessor processor = new InterproRequestProcessor();
        FileInputStream data;
        if (arguments.length != 2) {
            System.out.println("Usage: ./run.sh sequenceFileName resultFileName");
            return;
        }
        FileOutputStream resultFileOutputStream = null;
        File resultFile = new File(arguments[1]);
        try {
            resultFile.createNewFile();
            resultFileOutputStream = new FileOutputStream(resultFile);
        } catch (IOException e) {
            System.out.println("File " + arguments[1] + " can not be created");
            return;
        }
        BufferedOutputStream resultBufferedOutputStream = new BufferedOutputStream(resultFileOutputStream);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(processor.processRequest(new FileInputStream(arguments[0])));
            byte[] bytes = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(bytes);
            resultBufferedOutputStream.write(bytes);
            resultBufferedOutputStream.flush();
            resultBufferedOutputStream.close();
        } catch (IOException e) {
            System.out.println("File " + arguments[0] + " can not be created");
            resultFile.delete();
        } catch (OperationUnavailableException e) {
            System.out.println(e);
            resultFile.delete();
        } catch (InvalidJobSpecificationException e) {
            System.out.println(e);
            resultFile.delete();
        } catch (JobExecutionException e) {
            System.out.println(e);
            resultFile.delete();
        }
    }
}
