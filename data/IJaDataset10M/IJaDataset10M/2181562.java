package com.wwwc.util.web;

public class MyStringFormat {

    private String inputString = "";

    ;

    private String outputString = "";

    ;

    private int stringLength = 0;

    public static void main(String args[]) {
        String s1 = args[0];
        int i = Integer.parseInt(args[1]);
        char c = args[2].charAt(0);
        MyStringFormat mf = new MyStringFormat();
        mf.leftIndent(s1, i, c);
        System.out.println(mf.getString());
        mf.rightIndent(s1, i, c);
        System.out.println(mf.getString());
    }

    public void setString(String input_string, int string_length) {
        inputString = input_string;
        stringLength = string_length;
    }

    public String getString() {
        return outputString;
    }

    public String leftIndent(String input_string, int output_length, char c) {
        inputString = input_string;
        stringLength = output_length;
        outputString = "";
        char add_char = c;
        char temp[] = new char[stringLength];
        if (inputString.length() >= stringLength) {
            outputString = inputString;
        } else {
            for (int i = 0; i < stringLength; i++) {
                if (i < inputString.length()) {
                    temp[i] = inputString.charAt(i);
                } else {
                    temp[i] = add_char;
                }
                outputString += temp[i];
            }
        }
        return outputString;
    }

    public String leftIndent(int input_int, int output_length, char c) {
        inputString = Integer.toString(input_int);
        stringLength = output_length;
        outputString = "";
        char add_char = c;
        char temp[] = new char[stringLength];
        if (inputString.length() >= stringLength) {
            outputString = inputString;
        } else {
            for (int i = 0; i < stringLength; i++) {
                if (i < inputString.length()) {
                    temp[i] = inputString.charAt(i);
                } else {
                    temp[i] = add_char;
                }
                outputString += temp[i];
            }
        }
        return outputString;
    }

    public String rightIndent(String input_string, int output_length, char c) {
        inputString = input_string;
        stringLength = output_length;
        outputString = "";
        char add_char = c;
        char temp[] = new char[stringLength];
        if (inputString.length() >= stringLength) {
            outputString = inputString;
        } else {
            for (int i = 0; i < stringLength; i++) {
                if (i < (stringLength - inputString.length())) {
                    temp[i] = add_char;
                } else {
                    temp[i] = inputString.charAt(i - (stringLength - inputString.length()));
                }
                outputString += temp[i];
            }
        }
        return outputString;
    }

    public String rightIndent(String input_string, int output_length, String space) {
        inputString = input_string;
        stringLength = output_length;
        outputString = "";
        String add_str = space;
        String temp = "";
        if (inputString.length() >= stringLength) {
            outputString = inputString;
        } else {
            for (int i = 0; i < stringLength; i++) {
                if (i < (stringLength - inputString.length())) {
                    temp += add_str;
                }
            }
            outputString += temp;
        }
        return outputString;
    }

    public String rightIndent(int int_string, int string_length, char c) {
        inputString = Integer.toString(int_string);
        stringLength = string_length;
        outputString = "";
        if (inputString.length() >= stringLength) {
            outputString = inputString;
        } else {
            char add_char = c;
            char temp[] = new char[stringLength];
            for (int i = 0; i < stringLength; i++) {
                if (i < (stringLength - inputString.length())) {
                    temp[i] = add_char;
                } else {
                    temp[i] = inputString.charAt(i - (stringLength - inputString.length()));
                }
                outputString += temp[i];
            }
        }
        return outputString;
    }

    public String rightIndent(long long_string, int string_length, char c) {
        inputString = Long.toString(long_string);
        stringLength = string_length;
        outputString = "";
        if (inputString.length() >= stringLength) {
            outputString = inputString;
        } else {
            char add_char = c;
            char temp[] = new char[stringLength];
            for (int i = 0; i < stringLength; i++) {
                if (i < (stringLength - inputString.length())) {
                    temp[i] = add_char;
                } else {
                    temp[i] = inputString.charAt(i - (stringLength - inputString.length()));
                }
                outputString += temp[i];
            }
        }
        return outputString;
    }

    public String rightIndent(double long_string, int string_length, char c) {
        inputString = Double.toString(long_string);
        stringLength = string_length;
        outputString = "";
        if (inputString.length() >= stringLength) {
            outputString = inputString;
        } else {
            char add_char = c;
            char temp[] = new char[stringLength];
            for (int i = 0; i < stringLength; i++) {
                if (i < (stringLength - inputString.length())) {
                    temp[i] = add_char;
                } else {
                    temp[i] = inputString.charAt(i - (stringLength - inputString.length()));
                }
                outputString += temp[i];
            }
        }
        return outputString;
    }
}
