package com.softserveinc.edu.training.webserver.requests;

import java.util.Scanner;

/**
 * class for parsing a header form request.
 * 
 * @author marko
 * 
 * @version v1.0
 * 
 */
public class HeaderParser {

    private RequestHeader header;

    private Scanner clientInputStream;

    public void parse() throws WrongFormatException {
        try {
            header = new RequestHeader();
            while (clientInputStream.hasNext()) {
                String line = clientInputStream.nextLine();
                if (line.equals("")) {
                    break;
                }
                Scanner tmpSc = new Scanner(line);
                String key = tmpSc.next();
                key = key.substring(0, key.length() - 1);
                String[] parameters = tmpSc.nextLine().substring(1).split(",");
                header.addParameter(key, parameters);
            }
        } catch (Exception e) {
            throw new WrongFormatException(WrongFormatException.UNKNOWN_FORMAT);
        }
    }

    public RequestHeader getHeader() {
        return this.header;
    }

    public void setClientInputStream(Scanner clientInputStream) {
        this.clientInputStream = clientInputStream;
    }
}
