package com.apelon.dts.test.interactive;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class MenuTest {

    private int choice;

    protected BufferedReader in;

    protected String[] newItems;

    protected static final String[] mainItems = {};

    protected static final String[] testMainItems = {};

    protected String configName = "menu.xml";

    protected static Map menuParams;

    public MenuTest() {
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    protected String getInput(String msg) throws IOException {
        System.out.println(msg);
        String input = in.readLine();
        if (input == null || input.length() == 0) {
            return null;
        }
        if (input.charAt(0) == '0' && input.length() == 1) {
            return null;
        }
        return input;
    }

    public void askChoice() {
        boolean bContinue = true;
        String[] mainItems = getMainItems();
        String[] testMainItems = getTestMainItems();
        while (bContinue) {
            printMenu(mainItems);
            System.out.println("please enter the choice");
            System.out.println("To exit, please press 0");
            String input = null;
            try {
                input = in.readLine();
                input.trim();
                choice = Integer.parseInt(input);
            } catch (Exception ioe) {
                System.out.println("invalid input: " + input);
                continue;
            }
            if (choice < 0 || choice > mainItems.length) {
                System.out.println("The choice is out of range");
                continue;
            }
            if (choice == 0) {
                return;
            }
            executeTest(testMainItems, choice - 1);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("TestMenu v1.0");
        MenuTest test = new DTSMenuTest();
        test.askChoice();
    }

    private void printMenu(String[] items) {
        findMaxLength(items);
        int j = 0;
        System.out.println();
        System.out.println();
        for (int i = 0; i < newItems.length; i++) {
            System.out.print((i + 1) + " " + newItems[i]);
            printSpace(5);
            if ((i != 0) && (i % 2) == 1) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println();
    }

    private void executeTest(String[] testItems, int choice) {
        String test = testItems[choice];
        Method method = null;
        try {
            method = this.getClass().getMethod(test, null);
            method.invoke(this, null);
        } catch (NoSuchMethodException nsme) {
            System.out.print(test + " is not supported");
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private void findMaxLength(String[] items) {
        int max = 0;
        for (int i = 0; i < items.length; i++) {
            if (max < items[i].length()) {
                max = items[i].length();
            }
        }
        newItems = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            StringBuffer tmp = new StringBuffer(items[i]);
            String itemString = items[i];
            for (int j = 0; j < (max - itemString.length()); j++) {
                tmp.append(" ");
            }
            newItems[i] = tmp.toString();
        }
    }

    private static void printSpace(int num) {
        for (int i = 0; i < num; i++) {
            System.out.print(" ");
        }
    }

    public void setConfigName(String name) {
        configName = name;
    }

    public String getConfigName() {
        return configName;
    }

    public void askConfigName() {
        try {
            System.out.println("enter configuration filename");
            String line = in.readLine();
            if (line == null || line.length() == 1) return;
            configName = line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The config filename is set to " + configName);
    }

    public void showConfigName() {
        System.out.println(configName);
    }

    protected void parseConfig() throws Exception {
    }

    public void reLoadConfig() throws Exception {
        parseConfig();
    }

    private String[] displayItems;

    private String[] testItems;

    public abstract String[] getDisplayItems();

    public abstract String[] getTestItems();

    protected abstract void init();

    public String[] getMainItems() {
        if (displayItems == null) {
            String[] items = getDisplayItems();
            displayItems = new String[mainItems.length + items.length];
            System.arraycopy(items, 0, displayItems, 0, items.length);
            System.arraycopy(mainItems, 0, displayItems, items.length, mainItems.length);
        }
        return displayItems;
    }

    public String[] getTestMainItems() {
        if (testItems == null) {
            String[] items = getTestItems();
            testItems = new String[testMainItems.length + items.length];
            System.arraycopy(items, 0, testItems, 0, items.length);
            System.arraycopy(testMainItems, 0, testItems, items.length, testMainItems.length);
        }
        return testItems;
    }
}
