package tester;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: aleck
 * Date: 2007-10-11
 * Time: 10:48:35
 * To change this template use File | Settings | File Templates.
 */
public class ExeRunner {

    public static class ExecuteResult {

        private InputStream inputStream;

        private long timeUsed;

        public long getTimeUsed() {
            return timeUsed;
        }

        public void setTimeUsed(long timeUsed) {
            this.timeUsed = timeUsed;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public ExecuteResult(InputStream inputStream, long timeUsed) {
            this.inputStream = inputStream;
            this.timeUsed = timeUsed;
        }
    }

    public static void transferStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] b = new byte[1024];
        int len;
        len = inputStream.read(b);
        while (len >= 0) {
            outputStream.write(b, 0, len);
            len = inputStream.read(b);
        }
        outputStream.flush();
    }

    public static void transferAnsiStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
        String line = br.readLine();
        while (line != null) {
            bw.write(line);
            bw.write("\n");
            line = br.readLine();
        }
        bw.flush();
    }

    private static boolean isProcessAlive(Process process) {
        try {
            process.exitValue();
        } catch (IllegalThreadStateException e) {
            return true;
        }
        return false;
    }

    public static ExecuteResult executeCmd(File directory, long timeout, String... command) throws IOException {
        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add("cmd");
        cmd.add("/c");
        cmd.addAll(Arrays.asList(command));
        try {
            return ExeRunner.execute(directory, timeout, null, (String[]) cmd.toArray());
        } catch (InternalErrorException e) {
            return new ExecuteResult(null, 0);
        }
    }

    public static ExecuteResult execute(File directory, long timeout, InputStream inputStream, String... command) throws IOException, InternalErrorException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        if (directory != null) processBuilder.directory(directory);
        Process process = null;
        List<byte[]> store = new ArrayList<byte[]>();
        long timeUsed = 0;
        try {
            process = processBuilder.start();
            if (inputStream != null) transferAnsiStream(inputStream, process.getOutputStream());
            InputStream processInputStream = process.getInputStream();
            long timeStart = System.currentTimeMillis();
            long dueTime = timeStart + timeout;
            byte[] b = new byte[1024];
            while (isProcessAlive(process)) {
                if (processInputStream.available() > 0) {
                    int len = processInputStream.read(b);
                    byte[] toStore = new byte[len];
                    System.arraycopy(b, 0, toStore, 0, len);
                    store.add(toStore);
                }
                if (timeout != -1 && System.currentTimeMillis() > dueTime) break; else Thread.sleep(20);
            }
            if (isProcessAlive(process)) process.destroy();
            while (processInputStream.available() > 0) {
                int len = processInputStream.read(b);
                byte[] toStore = new byte[len];
                System.arraycopy(b, 0, toStore, 0, len);
                store.add(toStore);
            }
            timeUsed = System.currentTimeMillis() - timeStart;
            return new ExecuteResult(new ByteArrayInputStream(flattenArrayList(store)), timeUsed);
        } catch (InterruptedException e) {
            throw new InternalErrorException("Executing interrupted.");
        }
    }

    private static byte[] flattenArrayList(List<byte[]> list) {
        int totLen = 0;
        for (byte[] b : list) {
            totLen += b.length;
        }
        byte[] result = new byte[totLen];
        int offset = 0;
        for (byte[] b : list) {
            System.arraycopy(b, 0, result, offset, b.length);
            offset += b.length;
        }
        return result;
    }

    public static ExecuteResult executeNoIO(File directory, long timeout, String... command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        if (directory != null) processBuilder.directory(directory);
        Process process = null;
        long timeUsed = 0;
        try {
            process = processBuilder.start();
            InputStream processInputStream = process.getInputStream();
            InputStream processErrorStream = process.getErrorStream();
            long timeStart = System.currentTimeMillis();
            long dueTime = timeStart + timeout;
            byte[] b = new byte[1024];
            while (isProcessAlive(process)) {
                while (processInputStream.available() > 0) {
                    processInputStream.read(b);
                }
                while (processErrorStream.available() > 0) {
                    processErrorStream.read(b);
                }
                if (timeout != -1 && System.currentTimeMillis() > dueTime) break; else Thread.sleep(20);
            }
            while (processInputStream.available() > 0) {
                processInputStream.read(b);
            }
            while (processInputStream.available() > 0) {
                processInputStream.read(b);
            }
            if (isProcessAlive(process)) process.destroy();
            timeUsed = System.currentTimeMillis() - timeStart;
            return new ExecuteResult(null, timeUsed);
        } catch (InterruptedException e) {
            System.out.println("Interrupted when executing...");
            e.printStackTrace();
            return new ExecuteResult(null, timeUsed);
        }
    }
}
