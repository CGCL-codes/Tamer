package com.jguigen.XML;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.adapters.DOMAdapter;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

public class Profiler {

    public Profiler() {
        try {
            IniFile ini = new IniFile("test.ini");
            ini.putProfile("Database", "name", "Mydatabase");
            ini.putProfile("Database", "connection", "COM.ibm.db2.jdbc.app.DB2Driver");
            ini.putProfile("Database", "user", "jim");
            ini.putProfile("Database", "password", "snarch");
            ini.putProfile("Files", "directory", "d:\\temp");
            ini.putProfile("Files", "file", "mydata.txt");
        } catch (IOException e) {
        } catch (JDOMException e) {
            System.out.println(e.getMessage());
        }
        try {
            IniFile ini = new IniFile("test.ini");
            String name = ini.getProfile("database", "name");
            System.out.println("Name=" + name);
        } catch (IOException e) {
        } catch (JDOMException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] argv) {
        new Profiler();
    }
}
