package com.fuckingbrit.q3astats;

import java.io.*;

/**
  * The CorePreParser is a very simple implementation of the PreParser
  * interface. The basic point of this Class is to rip out as many lines of the
  * log file as we can prior to parsing it against the hit/kill messages. A
  * typical qconsole.log file will contain many log lines for loading in the
  * pak files etc. We want to strip them. This is intended as a first draft of
  * a much more efficient parser.
  *
  * @author Michael Jervis
  * @version 1.0
  */
public class CorePreParser implements PreParser {

    /**
      * The strip method is the core function of the pre-parser.
      * 
      * This pre-parser is very stupid. All it knows is that if a line starts
      * with:<br><pr>
      *     ..., ---, "  ", or "Parsing " 
      * </pre>
      * Then we don't want it and doesn't keep it.
      *
      * @param reader The raw un-parsed log file.
      * @param output The parsed version of the log file will be written out here.
      */
    public void strip(java.io.BufferedReader reader, java.io.ByteArrayOutputStream output) throws QuakeParseException {
        String line;
        int in = 0;
        int out = 0;
        try {
            line = reader.readLine();
            while (line != null) {
                in++;
                if (!(line.startsWith("...") || line.startsWith("---") || line.startsWith("  ") || line.startsWith("Parsing "))) {
                    line = line + "\n";
                    output.write(line.getBytes(), 0, line.length());
                    out++;
                }
                line = reader.readLine();
            }
            System.out.println("Preparser read: " + String.valueOf(in) + " lines and output: " + String.valueOf(out) + " lines");
        } catch (IOException e) {
            throw new QuakeParseException("Preparsing of file with CorePreParser failed: " + e.getMessage());
        }
    }
}
