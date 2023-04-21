package axb.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import axb.data.ASubmit;
import axb.data.VectorIterator;
import axb.ui.AXBPanel;

public class ExecuteFunctions {

    public static void click(ASubmit asubmit) {
        if (asubmit.by.equals("id")) {
            AXBPanel.browser.clickById(asubmit.key);
        } else if (asubmit.by.equals("value")) {
            AXBPanel.browser.clickByValue(asubmit.tag, asubmit.value, Integer.parseInt(asubmit.num));
        } else if (asubmit.by.equals("class")) {
            AXBPanel.browser.clickByClass(asubmit.tag, asubmit.value, Integer.parseInt(asubmit.num));
        } else if (asubmit.by.equals("name")) {
            AXBPanel.browser.clickByName(asubmit.value, Integer.parseInt(asubmit.num));
        } else if (asubmit.by.equals("tag")) {
            AXBPanel.browser.clickByTagName(asubmit.key, asubmit.tag, Integer.parseInt(asubmit.num));
        }
        System.out.println("click by " + asubmit.by + ": " + asubmit.value);
    }

    public static void input(ASubmit asubmit) {
        if (asubmit.by.equals("id")) {
            AXBPanel.browser.setValueById(asubmit.key, asubmit.value);
            System.out.println("input: " + asubmit.value);
        } else if (asubmit.by.equals("name")) {
            String[] in = asubmit.num.split("\\,");
            ArrayList<Integer> inn = new ArrayList<Integer>();
            for (int i = 0; i < in.length; i++) {
                if (in[i].indexOf("-") == -1) {
                    inn.add(Integer.parseInt(in[i]));
                } else {
                    for (int j = Integer.parseInt(in[i].split("-")[0]); j <= Integer.parseInt(in[i].split("-")[1]); j++) {
                        inn.add(j);
                    }
                }
            }
            for (int i = 0; i < inn.size(); i++) {
                int inpIndex = (Integer) inn.get(i);
                AXBPanel.browser.setValueByName(asubmit.key, inpIndex, asubmit.value);
                System.out.println("input by name" + asubmit.key + " (" + i + ") with value: " + asubmit.value);
            }
        }
    }

    public static void gourl(ASubmit asubmit) {
        AXBPanel.browser.setUrl(asubmit.value);
        System.out.println("gourl: " + asubmit.value);
    }

    public static Vector<?> fetch(ASubmit asubmit) {
        String fBody = AXBPanel.browser.getBody();
        Vector<Vector> fetchResult = new Vector<Vector>();
        Vector<String> singleResult = new Vector<String>();
        Pattern prstatus = Pattern.compile(asubmit.value);
        Matcher mrstatus = prstatus.matcher(fBody);
        int groups = countSubString(asubmit.value, "(") - countSubString(asubmit.value, "\\(");
        while (mrstatus.find()) {
            singleResult = new Vector<String>();
            for (int i = 1; i <= groups; i++) {
                singleResult.addElement(mrstatus.group(i));
            }
            fetchResult.addElement(singleResult);
        }
        System.out.println("fetchresult.size=" + fetchResult.size());
        return fetchResult;
    }

    private static int countSubString(String mstring, String substring) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = mstring.indexOf(substring, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex++;
            }
        }
        return count;
    }

    public static void clickfetch(ASubmit asubmit, Vector<?> fetchresult) {
        String groupvalue = "";
        String[] s;
        groupvalue = (String) ((Vector<?>) fetchresult.elementAt(1)).elementAt(asubmit.fetchGroupNum);
        if (asubmit.exeFectchIndex.equals("all")) {
            for (int i = 0; i < fetchresult.size(); i++) {
                groupvalue = (String) ((Vector<?>) fetchresult.elementAt(i)).elementAt(asubmit.fetchGroupNum);
                if (asubmit.by.equals("id")) {
                    AXBPanel.browser.clickById(groupvalue);
                } else if (asubmit.by.equals("value")) {
                    AXBPanel.browser.clickByValue(asubmit.tag, groupvalue, 0);
                } else if (asubmit.by.equals("class")) {
                    AXBPanel.browser.clickByClass(asubmit.tag, groupvalue, 0);
                } else if (asubmit.by.equals("name")) {
                    AXBPanel.browser.clickByName(groupvalue, 0);
                } else if (asubmit.by.equals("tag")) {
                    AXBPanel.browser.clickByTagName(groupvalue, asubmit.tag, 0);
                }
                System.out.println("click by " + asubmit.by + ": " + groupvalue);
            }
        } else {
            s = asubmit.exeFectchIndex.split("\\,");
            for (int i = 0; i < s.length; i++) {
                if (s[i].indexOf("-") == -1) {
                    int eleIndex = Integer.parseInt(s[i]);
                    groupvalue = (String) ((Vector<?>) fetchresult.elementAt(eleIndex)).elementAt(asubmit.fetchGroupNum);
                    System.out.println(groupvalue);
                    if (asubmit.by.equals("id")) {
                        AXBPanel.browser.clickById(groupvalue);
                    } else if (asubmit.by.equals("value")) {
                        AXBPanel.browser.clickByValue(asubmit.tag, groupvalue, 0);
                    } else if (asubmit.by.equals("class")) {
                        AXBPanel.browser.clickByClass(asubmit.tag, groupvalue, 0);
                    } else if (asubmit.by.equals("name")) {
                        AXBPanel.browser.clickByName(groupvalue, 0);
                    } else if (asubmit.by.equals("tag")) {
                        AXBPanel.browser.clickByTagName(groupvalue, asubmit.tag, 0);
                    }
                } else {
                    for (int j = Integer.parseInt(s[i].split("-")[0]); j <= Integer.parseInt(s[i].split("-")[1]); j++) {
                        groupvalue = (String) ((Vector<?>) fetchresult.elementAt(j)).elementAt(asubmit.fetchGroupNum);
                        System.out.println(groupvalue);
                        if (asubmit.by.equals("id")) {
                            AXBPanel.browser.clickById(groupvalue);
                        } else if (asubmit.by.equals("value")) {
                            AXBPanel.browser.clickByValue(asubmit.tag, groupvalue, 0);
                        } else if (asubmit.by.equals("class")) {
                            AXBPanel.browser.clickByClass(asubmit.tag, groupvalue, 0);
                        } else if (asubmit.by.equals("name")) {
                            AXBPanel.browser.clickByName(groupvalue, 0);
                        } else if (asubmit.by.equals("tag")) {
                            AXBPanel.browser.clickByTagName(groupvalue, asubmit.tag, 0);
                        }
                    }
                }
            }
        }
    }

    public static void inputfetch(ASubmit asubmit, Vector<?> fetchresult) {
        String groupvalue = "";
        groupvalue = (String) ((Vector<?>) fetchresult.elementAt(0)).elementAt(asubmit.fetchGroupNum);
        if (asubmit.by.equals("id")) {
            AXBPanel.browser.setValueById(asubmit.key, groupvalue);
        } else if (asubmit.by.equals("name")) {
            if (asubmit.exeFectchIndex.equals("all")) {
                for (int i = 0; i < fetchresult.size(); i++) {
                    groupvalue = (String) ((Vector<?>) fetchresult.elementAt(i)).elementAt(asubmit.fetchGroupNum);
                    AXBPanel.browser.setValueByName(asubmit.key, i, groupvalue);
                    System.out.println("input by name--" + asubmit.key + " with value: " + groupvalue);
                }
            } else {
                String[] fe = asubmit.exeFectchIndex.split("\\,");
                String[] in = asubmit.num.split("\\,");
                ArrayList<Integer> fen = new ArrayList<Integer>();
                ArrayList<Integer> inn = new ArrayList<Integer>();
                for (int i = 0; i < fe.length; i++) {
                    if (fe[i].indexOf("-") == -1) {
                        fen.add(Integer.parseInt(fe[i]));
                    } else {
                        for (int j = Integer.parseInt(fe[i].split("-")[0]); j <= Integer.parseInt(fe[i].split("-")[1]); j++) {
                            fen.add(j);
                        }
                    }
                }
                for (int i = 0; i < in.length; i++) {
                    if (in[i].indexOf("-") == -1) {
                        inn.add(Integer.parseInt(in[i]));
                    } else {
                        for (int j = Integer.parseInt(in[i].split("-")[0]); j <= Integer.parseInt(in[i].split("-")[1]); j++) {
                            inn.add(j);
                        }
                    }
                }
                for (int i = 0; i < fen.size(); i++) {
                    if (i < inn.size()) {
                        int eleIndex = (Integer) fen.get(i);
                        int inpIndex = (Integer) inn.get(i);
                        groupvalue = (String) ((Vector<?>) fetchresult.elementAt(eleIndex)).elementAt(asubmit.fetchGroupNum);
                        AXBPanel.browser.setValueByName(asubmit.key, inpIndex, groupvalue);
                    } else {
                        break;
                    }
                }
            }
        }
    }
}
