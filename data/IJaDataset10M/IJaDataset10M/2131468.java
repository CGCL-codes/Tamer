package atv;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class FormatFileTo80Cols {

    public static void format(String f) throws IOException {
        BufferedReader file;
        String inline;
        String newline;
        String newcontent;
        StringBuffer sb = new StringBuffer();
        int end;
        int current;
        int l;
        int value = 0;
        boolean comment;
        boolean double_comment;
        file = new BufferedReader(new FileReader(f));
        for (inline = file.readLine(); inline != null && inline.length() != 0; inline = file.readLine()) {
            current = 0;
            if (inline.charAt(0) == ';') {
                if (inline.length() > 1 && inline.charAt(1) == ';') {
                    double_comment = true;
                    comment = false;
                } else {
                    comment = true;
                    double_comment = false;
                }
            } else {
                comment = false;
                double_comment = false;
            }
            l = inline.length();
            if (l != 0) {
                if (l > 80) {
                    end = 80;
                    while (end < l) {
                        newline = inline.substring(current, end);
                        if (comment) sb = sb.append(newline).append('\n').append(';'); else {
                            if (double_comment) sb = sb.append(newline).append('\n').append(";;"); else sb = sb.append(newline).append('\n');
                        }
                        current = end;
                        end = end + 80;
                    }
                    newline = inline.substring(current);
                    sb = sb.append(newline).append('\n');
                } else {
                    newline = inline.substring(current);
                    sb = sb.append(newline).append('\n');
                }
            }
        }
        file.close();
        PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        newcontent = sb.toString();
        w.println(newcontent);
        w.close();
    }

    public static void format(File f) throws IOException {
        BufferedReader file;
        String inline;
        String newline;
        String newcontent;
        StringBuffer sb = new StringBuffer();
        int end;
        int current;
        int l;
        int value = 0;
        boolean comment;
        boolean double_comment;
        file = new BufferedReader(new FileReader(f));
        for (inline = file.readLine(); inline != null && inline.length() != 0; inline = file.readLine()) {
            current = 0;
            if (inline.charAt(0) == ';') {
                if (inline.charAt(1) == ';') {
                    double_comment = true;
                    comment = false;
                } else {
                    comment = true;
                    double_comment = false;
                }
            } else {
                comment = false;
                double_comment = false;
            }
            l = inline.length();
            if (l != 0) {
                if (l > 80) {
                    end = 80;
                    while (end < l) {
                        newline = inline.substring(current, end);
                        if (comment) sb = sb.append(newline).append('\n').append(';'); else {
                            if (double_comment) sb = sb.append(newline).append('\n').append(";;"); else sb = sb.append(newline).append('\n');
                        }
                        current = end;
                        end = end + 80;
                    }
                    newline = inline.substring(current);
                    sb = sb.append(newline).append('\n');
                } else {
                    newline = inline.substring(current);
                    sb = sb.append(newline).append('\n');
                }
            }
        }
        file.close();
        PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        newcontent = sb.toString();
        w.println(newcontent);
        w.close();
    }

    public static String formatString(String inline) {
        String newline;
        int end = 80;
        int current = 0;
        int l;
        StringBuffer sb = new StringBuffer();
        l = inline.length();
        if (l != 0) {
            if (l > 80) {
                ;
                while (end < l) {
                    newline = inline.substring(current, end);
                    sb = sb.append(newline).append('\n');
                    current = end;
                    end = end + 80;
                }
                newline = inline.substring(current);
                sb = sb.append(newline).append('\n');
            } else {
                newline = inline.substring(current);
                sb = sb.append(newline).append('\n');
            }
        }
        return (sb.toString());
    }
}
