package org.beanopen.io.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import org.junit.Test;

public class OSCommand {

    @Test
    public void execute() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /c dir");
        InputStreamReader ir = new InputStreamReader(process.getInputStream(), "GB2312");
        LineNumberReader input = new LineNumberReader(ir);
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line + line.split(" +").length);
        }
    }
}
