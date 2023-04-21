package test;

import java.io.*;
import java.util.*;

/**
 *
 * @author Deva
 */
public class Parser {

    final String SPACE = " ";

    RandomAccessFile sam;

    RandomAccessFile src;

    RandomAccessFile pdf;

    ReadFile reader;

    Render render;

    Hashtable varDesc = null;

    Hashtable varStore = null;

    int srcTempLineNum;

    int i = 1;

    public Parser() throws Exception {
        sam = new RandomAccessFile("E:\\java works\\alata\\sample.txt", "r");
        src = new RandomAccessFile("E:\\java works\\alata\\source.txt", "r");
        pdf = new RandomAccessFile("E:\\java works\\alata\\pdf.txt", "r");
        reader = new ReadFile(sam);
        varDesc = new Hashtable();
        varStore = new Hashtable();
    }

    public static void main(String a[]) throws Exception {
        new Parser().parse(1);
    }

    public void parse(int in) throws Exception {
        this.i = in;
        String command = "";
        while (!command.equals("ENDCODE")) {
            command = getCommand();
            commandHandler(command);
        }
    }

    public String getCommand() throws Exception {
        String line = "";
        while (true) {
            srcTempLineNum++;
            line = src.readLine().trim();
            if (!line.startsWith("#") && line.length() > 0) {
                break;
            }
        }
        return line;
    }

    public void commandHandler(String cmd) throws Exception {
        String command = "";
        String strTemp = "";
        String[] strArrTemp;
        if (cmd.indexOf(SPACE) != -1) {
            command = cmd.substring(0, cmd.indexOf(SPACE));
        } else {
            command = cmd;
        }
        switch(cmdList.getSrcCmdVal(command)) {
            case 1:
                varDesc.put(cmd.substring(cmd.indexOf(SPACE) + 1).trim(), "STRING");
                varStore.put(cmd.substring(cmd.indexOf(SPACE) + 1).trim(), new String(""));
                break;
            case 2:
                varDesc.put(cmd.substring(cmd.indexOf(SPACE) + 1, cmd.indexOf("[")).trim(), "STRINGARRAY");
                varStore.put(cmd.substring(cmd.indexOf(SPACE) + 1, cmd.indexOf("[")).trim(), new String[Integer.parseInt(cmd.substring(cmd.indexOf("[") + 1, cmd.indexOf("]")))]);
                break;
            case 3:
                varDesc.put(cmd.substring(cmd.indexOf(SPACE) + 1).trim(), "INT");
                varStore.put(cmd.substring(cmd.indexOf(SPACE) + 1).trim(), 0);
                break;
            case 4:
                varDesc.put(cmd.substring(cmd.indexOf(SPACE) + 1, cmd.indexOf("[")).trim(), "INTARRAY");
                varStore.put(cmd.substring(cmd.indexOf(SPACE) + 1, cmd.indexOf("[")).trim(), new Integer[Integer.parseInt(cmd.substring(cmd.indexOf("[") + 1, cmd.indexOf("]")))]);
                break;
            case 5:
                varDesc.put(cmd.substring(cmd.indexOf(SPACE) + 1).trim(), "DOUBLE");
                varStore.put(cmd.substring(cmd.indexOf(SPACE) + 1).trim(), new Double(0));
                break;
            case 6:
                varDesc.put(cmd.substring(cmd.indexOf(SPACE) + 1, cmd.indexOf("[")).trim(), "DOUBLEARRAY");
                varStore.put(cmd.substring(cmd.indexOf(SPACE) + 1, cmd.indexOf("[")).trim(), new Double[Integer.parseInt(cmd.substring(cmd.indexOf("[") + 1, cmd.indexOf("]")))]);
                break;
            case 7:
                strTemp = cmd.substring(cmd.indexOf(SPACE) + 1).trim();
                if (strTemp.equals("SPACE")) {
                    reader.skipSpace();
                } else if (strTemp.substring(strTemp.length() - 1).equals("L")) {
                    reader.skipLine(Integer.parseInt(strTemp.substring(0, strTemp.indexOf("L"))));
                } else if (strTemp.substring(strTemp.length() - 1).equals("C")) {
                    reader.skipChar(Integer.parseInt(strTemp.substring(0, strTemp.indexOf("C"))));
                } else if (strTemp.charAt(0) == '\"') {
                    reader.skipString(strTemp);
                } else {
                    System.out.println("Unknownn Command at line num " + srcTempLineNum);
                }
                break;
            case 8:
                strArrTemp = new String[4];
                int type = 0;
                boolean isArray = false;
                strTemp = cmd.substring(cmd.indexOf(SPACE) + 1).trim();
                System.out.println(strTemp);
                if (strTemp.startsWith("?") && strTemp.endsWith("\"")) {
                    strArrTemp[0] = strTemp.substring(strTemp.indexOf("\"") + 1, strTemp.indexOf("\"", strTemp.indexOf("\"") + 1));
                    strTemp = strTemp.substring(strTemp.indexOf("\"", strTemp.indexOf("\"") + 1) + 1).trim();
                    strArrTemp[1] = strTemp.substring(0, strTemp.indexOf(SPACE));
                    strTemp = strTemp.substring(strTemp.indexOf(SPACE) + 1).trim();
                    if (strArrTemp[1].contains("[")) {
                        strArrTemp[2] = strArrTemp[1].substring(strArrTemp[1].indexOf("[") + 1, strArrTemp[1].indexOf("]"));
                        strArrTemp[1] = strArrTemp[1].substring(0, strArrTemp[1].indexOf("["));
                        isArray = true;
                    }
                    strArrTemp[3] = strTemp.substring(strTemp.indexOf("\"") + 1, strTemp.lastIndexOf("\""));
                    type = 1;
                } else if (strTemp.startsWith("?") && strTemp.endsWith("*")) {
                    strArrTemp[0] = strTemp.substring(strTemp.indexOf("\"") + 1, strTemp.indexOf("\"", strTemp.indexOf("\"") + 1));
                    strTemp = strTemp.substring(strTemp.indexOf("\"", strTemp.indexOf("\"") + 1) + 1).trim();
                    strArrTemp[1] = strTemp.substring(0, strTemp.indexOf(SPACE));
                    strTemp = strTemp.substring(strTemp.indexOf(SPACE) + 1).trim();
                    if (strArrTemp[1].contains("[")) {
                        strArrTemp[2] = strArrTemp[1].substring(strArrTemp[1].indexOf("[") + 1, strArrTemp[1].indexOf("]"));
                        strArrTemp[1] = strArrTemp[1].substring(0, strArrTemp[1].indexOf("["));
                        isArray = true;
                    }
                    type = 2;
                } else if (strTemp.startsWith("?")) {
                    strArrTemp[0] = strTemp.substring(strTemp.indexOf("\"") + 1, strTemp.indexOf("\"", strTemp.indexOf("\"") + 1));
                    strTemp = strTemp.substring(strTemp.indexOf("\"", strTemp.indexOf("\"") + 1) + 1).trim();
                    strArrTemp[1] = strTemp.substring(0, strTemp.indexOf(SPACE));
                    strTemp = strTemp.substring(strTemp.indexOf(SPACE) + 1).trim();
                    if (strArrTemp[1].contains("[")) {
                        strArrTemp[2] = strArrTemp[1].substring(strArrTemp[1].indexOf("[") + 1, strArrTemp[1].indexOf("]"));
                        strArrTemp[1] = strArrTemp[1].substring(0, strArrTemp[1].indexOf("["));
                        isArray = true;
                    }
                    strArrTemp[3] = strTemp.trim();
                    type = 3;
                } else if (strTemp.endsWith("\"")) {
                    strArrTemp[0] = "";
                    strArrTemp[1] = strTemp.substring(0, strTemp.indexOf(SPACE));
                    strTemp = strTemp.substring(strTemp.indexOf(SPACE) + 1).trim();
                    if (strArrTemp[1].contains("[")) {
                        strArrTemp[2] = strArrTemp[1].substring(strArrTemp[1].indexOf("[") + 1, strArrTemp[1].indexOf("]"));
                        strArrTemp[1] = strArrTemp[1].substring(0, strArrTemp[1].indexOf("["));
                        isArray = true;
                    }
                    strArrTemp[3] = strTemp.substring(strTemp.indexOf("\"") + 1, strTemp.lastIndexOf("\""));
                    type = 4;
                } else if (strTemp.endsWith("*")) {
                    strArrTemp[1] = strTemp.substring(0, strTemp.indexOf(SPACE));
                    strTemp = strTemp.substring(strTemp.indexOf(SPACE) + 1).trim();
                    if (strArrTemp[1].contains("[")) {
                        strArrTemp[2] = strArrTemp[1].substring(strArrTemp[1].indexOf("[") + 1, strArrTemp[1].indexOf("]"));
                        strArrTemp[1] = strArrTemp[1].substring(0, strArrTemp[1].indexOf("["));
                        isArray = true;
                    }
                    type = 5;
                } else {
                    strArrTemp[1] = strTemp.substring(0, strTemp.indexOf(SPACE));
                    strTemp = strTemp.substring(strTemp.indexOf(SPACE) + 1).trim();
                    if (strArrTemp[1].contains("[")) {
                        strArrTemp[2] = strArrTemp[1].substring(strArrTemp[1].indexOf("[") + 1, strArrTemp[1].indexOf("]"));
                        strArrTemp[1] = strArrTemp[1].substring(0, strArrTemp[1].indexOf("["));
                        isArray = true;
                    }
                    strArrTemp[3] = strTemp.trim();
                    type = 6;
                }
                if (type == 1) {
                    if (varDesc.get(strArrTemp[1]).equals("STRING")) {
                        varStore.put(strArrTemp[1], reader.readBound(strArrTemp[0], strArrTemp[3], type));
                    } else if (varDesc.get(strArrTemp[1]).equals("STRINGARRAY")) {
                        if (isArray) {
                            String[] temp = (String[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = reader.readBound(strArrTemp[0], strArrTemp[3], type);
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("INT")) {
                        varStore.put(strArrTemp[1], Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("INTARRAY")) {
                        if (isArray) {
                            Integer[] temp = (Integer[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLE")) {
                        varStore.put(strArrTemp[1], Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLEARRAY")) {
                        if (isArray) {
                            Double[] temp = (Double[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    }
                } else if (type == 2) {
                    if (varDesc.get(strArrTemp[1]).equals("STRING")) {
                        varStore.put(strArrTemp[1], reader.readBound(strArrTemp[0], strArrTemp[3], type));
                    } else if (varDesc.get(strArrTemp[1]).equals("STRINGARRAY")) {
                        if (isArray) {
                            String[] temp = (String[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = reader.readBound(strArrTemp[0], strArrTemp[3], type);
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("INT")) {
                        varStore.put(strArrTemp[1], Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("INTARRAY")) {
                        if (isArray) {
                            Integer[] temp = (Integer[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLE")) {
                        varStore.put(strArrTemp[1], Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLEARRAY")) {
                        if (isArray) {
                            Double[] temp = (Double[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    }
                } else if (type == 3) {
                    if (varDesc.get(strArrTemp[1]).equals("STRING")) {
                        varStore.put(strArrTemp[1], reader.readBound(strArrTemp[0], strArrTemp[3], type));
                    } else if (varDesc.get(strArrTemp[1]).equals("STRINGARRAY")) {
                        if (isArray) {
                            String[] temp = (String[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = reader.readBound(strArrTemp[0], strArrTemp[3], type);
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("INT")) {
                        varStore.put(strArrTemp[1], Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("INTARRAY")) {
                        if (isArray) {
                            Integer[] temp = (Integer[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLE")) {
                        varStore.put(strArrTemp[1], Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLEARRAY")) {
                        if (isArray) {
                            Double[] temp = (Double[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    }
                } else if (type == 4) {
                    if (varDesc.get(strArrTemp[1]).equals("STRING")) {
                        varStore.put(strArrTemp[1], reader.readBound(strArrTemp[0], strArrTemp[3], type));
                    } else if (varDesc.get(strArrTemp[1]).equals("STRINGARRAY")) {
                        if (isArray) {
                            String[] temp = (String[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = reader.readBound(strArrTemp[0], strArrTemp[3], type);
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("INT")) {
                        varStore.put(strArrTemp[1], Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("INTARRAY")) {
                        if (isArray) {
                            Integer[] temp = (Integer[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLE")) {
                        varStore.put(strArrTemp[1], Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLEARRAY")) {
                        if (isArray) {
                            Double[] temp = (Double[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    }
                } else if (type == 5) {
                    if (varDesc.get(strArrTemp[1]).equals("STRING")) {
                        varStore.put(strArrTemp[1], reader.readBound(strArrTemp[0], strArrTemp[3], type));
                    } else if (varDesc.get(strArrTemp[1]).equals("STRINGARRAY")) {
                        if (isArray) {
                            String[] temp = (String[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = reader.readBound(strArrTemp[0], strArrTemp[3], type);
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("INT")) {
                        varStore.put(strArrTemp[1], Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("INTARRAY")) {
                        if (isArray) {
                            Integer[] temp = (Integer[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLE")) {
                        varStore.put(strArrTemp[1], Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLEARRAY")) {
                        if (isArray) {
                            Double[] temp = (Double[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    }
                } else if (type == 6) {
                    if (varDesc.get(strArrTemp[1]).equals("STRING")) {
                        varStore.put(strArrTemp[1], reader.readBound(strArrTemp[0], strArrTemp[3], type));
                    } else if (varDesc.get(strArrTemp[1]).equals("STRINGARRAY")) {
                        if (isArray) {
                            String[] temp = (String[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = reader.readBound(strArrTemp[0], strArrTemp[3], type);
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("INT")) {
                        varStore.put(strArrTemp[1], Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("INTARRAY")) {
                        if (isArray) {
                            Integer[] temp = (Integer[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Integer.parseInt(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLE")) {
                        varStore.put(strArrTemp[1], Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type)));
                    } else if (varDesc.get(strArrTemp[1]).equals("DOUBLEARRAY")) {
                        if (isArray) {
                            Double[] temp = (Double[]) varStore.get(strArrTemp[1]);
                            temp[Integer.parseInt(strArrTemp[2])] = Double.parseDouble(reader.readBound(strArrTemp[0], strArrTemp[3], type));
                            varStore.put(strArrTemp[1], temp);
                        }
                    }
                } else {
                }
                break;
            case 9:
                break;
        }
    }
}
