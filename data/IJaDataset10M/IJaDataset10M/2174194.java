package icapture.com.xmlWriter;

import java.io.*;
import java.util.*;

public class XMLWriter {

    private LinkedList elementList;

    private boolean elementStarted;

    private String fileName;

    private PrintStream printer;

    private String result;

    public XMLWriter() {
        this.printer = null;
        this.result = "";
        this.elementList = new LinkedList();
        this.elementStarted = false;
        this.fileName = null;
    }

    public XMLWriter(String filename) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(new File(filename));
        } catch (Exception e) {
        }
        this.printer = new PrintStream(output);
        this.elementList = new LinkedList();
        this.elementStarted = false;
        this.fileName = new String(filename);
    }

    public void startElement(String name) {
        this.finishStart(true);
        this.printText("<" + name);
        this.elementList.addLast(new String(name));
        this.elementStarted = true;
    }

    public void finishStart(boolean newLine) {
        if (this.elementStarted) {
            this.elementStarted = false;
            this.printText(">");
            if (newLine) {
                this.printText("\n");
            }
        }
    }

    public void endElement() {
        this.endElement(false);
    }

    public void endElement(boolean newLine) {
        this.finishStart(newLine);
        if (this.elementList.size() > 0) {
            String name = (String) elementList.removeLast();
            this.printText("</" + name + ">");
        } else {
            XMLUtil.vassert(false);
        }
        if (newLine) {
            this.printText("\n");
        }
    }

    public boolean addAtribute(String name, int value) {
        if (this.elementList.size() > 0) {
            this.printText(" " + name + "=" + value);
            return true;
        }
        return false;
    }

    public boolean addAtribute(String name, String value) {
        if ((this.elementList.size() > 0) && (value != "&nbsp;") && (value != "")) {
            this.printText(" " + name + "=\'" + value + "\'");
            return true;
        }
        return false;
    }

    public boolean addAtributeText(String name, String value) {
        if ((this.elementList.size() > 0) && (value != "&nbsp;") && (value != "")) {
            this.printText(" " + name + "=" + value);
            return true;
        }
        return false;
    }

    public boolean addAtribute(String name, Long value) {
        return addAtribute(name, value.longValue());
    }

    public boolean addAtribute(String name, long value) {
        if (this.elementList.size() > 0) {
            this.printText(" " + name + "=" + value);
            return true;
        }
        return false;
    }

    public boolean addAtribute(String name, Double value) {
        return addAtribute(name, value.doubleValue());
    }

    public boolean addAtribute(String name, double value) {
        if (this.elementList.size() > 0) {
            this.printText(" " + name + "=" + value);
            return true;
        }
        return false;
    }

    public boolean addAtribute(String name, boolean value) {
        if (this.elementList.size() > 0) {
            this.printText(" " + name + "=" + value);
            return true;
        }
        return false;
    }

    public boolean addAtribute(String name) {
        if (this.elementList.size() > 0) {
            this.printText(" " + name);
            return true;
        }
        return false;
    }

    public void addString(String value, boolean newLine) {
        this.printText(value);
        if (newLine) {
            this.printText("\n");
        }
    }

    public void newLine() {
        this.printText("\n");
    }

    public void addString(String value, String style, boolean newLine) {
        this.printText("<" + style + ">");
        this.addString(value, false);
        this.printText("</" + style + ">");
        if (newLine) {
            this.printText("\n");
        }
    }

    private void printText(String text) {
        if (this.printer != null) {
            printer.print(text);
        } else {
            this.result = this.result + text;
        }
    }

    public int close() {
        int rc = 0;
        while (elementList.size() > 0) {
            rc++;
            this.endElement();
        }
        if (this.printer != null) {
            this.printer.close();
            this.printer = null;
        }
        return rc;
    }

    public String readFromFile(String fileName) {
        String DataLine = "";
        try {
            File inFile = new File(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
            DataLine = br.readLine();
            br.close();
        } catch (FileNotFoundException ex) {
            return (null);
        } catch (IOException ex) {
            return (null);
        }
        return (DataLine);
    }

    public List getResult() {
        List result = null;
        if (this.fileName == null) {
            result = new ArrayList();
            result.add(this.result);
        } else {
            result = readFile(this.fileName);
            File file = new File(this.fileName);
            XMLUtil.vassert(file != null);
            XMLUtil.vassert(file.delete());
        }
        return result;
    }

    public static List readFile(String fileName) {
        if (fileName == null) {
            return null;
        }
        List result = new ArrayList();
        BufferedReader input = null;
        try {
            FileReader reader = new FileReader(fileName);
            String line = null;
            input = new BufferedReader(reader);
            while ((line = input.readLine()) != null) {
                result.add(line);
            }
            input.close();
            reader.close();
        } catch (Exception e) {
            System.err.println("File input error");
        }
        return result;
    }
}
