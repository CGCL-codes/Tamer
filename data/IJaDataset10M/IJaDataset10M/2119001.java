package com.agentfactory.astr.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class IncludesExpanderStream extends InputStream {

    List<InputStream> stack;

    InputStream current;

    String design;

    public IncludesExpanderStream(String url) throws IOException {
        design = url;
        stack = new LinkedList<InputStream>();
        current = IncludesExpanderStream.class.getResourceAsStream("/" + url);
        if (current == null) {
            throw new IOException("Could not open input stream on resource: /" + url);
        }
    }

    @Override
    public int read() throws IOException {
        int input = current.read();
        if (input == '#') {
            StringBuffer buf = new StringBuffer();
            while (!isSkipCharacter(input = current.read())) buf.append((char) input);
            String directive = buf.toString();
            if (directive.equalsIgnoreCase("include")) {
                while (isSkipCharacter(input = current.read())) ;
                if (input != '<') {
                    throw new IOException("Error Reading from file: #include statement missing <");
                }
                buf = new StringBuffer();
                while ((input = current.read()) != '>') buf.append((char) input);
                stack.add(current);
                String url = buf.toString().replace('.', '/') + ".aspeak";
                current = IncludesExpanderStream.class.getResourceAsStream("/" + url);
                if (current == null) {
                    System.out.println("WARNING: Error Instantiating: " + design);
                    System.out.println("            No Such Resource: " + url);
                    throw new IOException("Could not open input stream on resource: /" + url);
                }
                input = current.read();
            }
        }
        if ((input == -1) && !stack.isEmpty()) {
            current = stack.remove(stack.size() - 1);
            return read();
        }
        return input;
    }

    private boolean isSkipCharacter(int input) {
        return input == ' ' || input == '\t';
    }
}
