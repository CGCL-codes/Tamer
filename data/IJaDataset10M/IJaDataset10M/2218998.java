package com.lee;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xmlpull.v1.*;
import android.content.*;
import android.os.Environment;
import android.util.*;

public class Chatterbot {

    private String sInput = new String("");

    private String sResponse = new String("");

    private String sPrevInput = new String("");

    private String sPrevResponse = new String("");

    private String sEvent = new String("");

    private String sPrevEvent = new String("");

    private String sInputBackup = new String("");

    private String sSubject = new String("");

    private String sKeyWord = new String("");

    private String slearningInput = new String("");

    private String slearningOutput = new String("");

    private boolean bQuitProgram = false;

    private boolean isTeachingMode = false;

    private int teachstep = 0;

    final int maxInput = 4;

    final int maxResp = 6;

    final String delim = "?!.;,";

    public String KnowledgeBase[][][];

    private XmlContent xmlContent;

    String sd = Environment.getExternalStorageDirectory().getAbsolutePath();

    private static String transposList[][] = { { "나는", "너는" }, { "들", "을" }, { "있다", "계시다" }, { "내 것", "너의 것" }, { "나의", "너의" }, { "내가", "네가" }, { "나", "너" }, { "나에게", "너에게" }, { "했다", "하였다" }, { "안했다", "하지않았다" }, { "나도", "너도" }, { "아빠", "아버지" }, { "엄마", "어머니" }, { "꿈들", "꿈" }, { "스스로", "너스스로" } };

    private Vector<String> respList = new Vector<String>(maxResp);

    private Context mContext;

    public Chatterbot(Context amContext) {
        mContext = amContext;
        xmlContent = new XmlContent(amContext);
    }

    public void get_input(String str) {
        save_prev_input();
        sInput = str;
        preprocess_input();
    }

    public String respond() {
        respList.clear();
        if (!isTeachingMode) {
            save_prev_response();
            set_event("BOT UNDERSTAND**");
            if (sInput.contains("가르치기")) {
                teach_conversation1();
                isTeachingMode = true;
            } else {
                if (null_input()) {
                    handle_event("NULL INPUT**");
                } else if (null_input_repetition()) {
                    handle_event("NULL INPUT REPETITION**");
                } else if (user_repeat()) {
                    handle_user_repetition();
                } else {
                    find_match();
                }
                if (user_want_to_quit()) {
                    bQuitProgram = true;
                }
                if (!bot_understand()) {
                    handle_event("BOT DON'T UNDERSTAND**");
                }
            }
            if (respList.size() > 0) {
                select_response();
                preprocess_response();
                if (bot_repeat()) {
                    handle_repetition();
                }
            }
        } else {
            if (teachstep == 0) {
                teach_conversation2();
                teachstep++;
            } else {
                teach_conversation3();
                teachstep++;
            }
        }
        return print_response();
    }

    private void teach_conversation1() {
        sResponse = "무엇을 가르치시겠습니까 질문을 말해주세요";
    }

    private void teach_conversation2() {
        slearningInput = sInput;
        sResponse = "이 질문에 해당하는 대답을 입력해주세요";
    }

    private void teach_conversation3() {
        slearningOutput = sInput;
        sResponse = "기억했습니다. ";
        isTeachingMode = false;
        teachstep = 0;
        xmlContent.addContentItem(sd + "/script.xml", slearningInput, slearningOutput);
    }

    public boolean quit() {
        return bQuitProgram;
    }

    public void find_match() {
        respList.clear();
        String bestKeyWord = "";
        Vector<Integer> index_vector = new Vector<Integer>(maxResp);
        for (int i = 0; i < KnowledgeBase.length; ++i) {
            String[] keyWordList = KnowledgeBase[i][0];
            for (int j = 0; j < keyWordList.length; ++j) {
                String keyWord = keyWordList[j];
                if (keyWord == null) break;
                char firstChar = keyWord.charAt(0);
                char lastChar = keyWord.charAt(keyWord.length() - 1);
                keyWord = trimLR(keyWord, "_");
                keyWord = " " + keyWord + " ";
                int keyPos = sInput.indexOf(keyWord);
                if (keyPos != -1) {
                    if (wrong_location(keyWord, firstChar, lastChar, keyPos)) {
                        continue;
                    }
                    if (keyWord.length() > bestKeyWord.length()) {
                        bestKeyWord = keyWord;
                        index_vector.clear();
                        index_vector.add(i);
                    } else if (keyWord.length() == bestKeyWord.length()) {
                        index_vector.add(i);
                    }
                }
            }
        }
        if (index_vector.size() > 0) {
            sKeyWord = bestKeyWord;
            Collections.shuffle(index_vector);
            int respIndex = index_vector.elementAt(0);
            int respSize = KnowledgeBase[respIndex][1].length;
            for (int j = 0; j < respSize; ++j) {
                if (KnowledgeBase[respIndex][1][j] == null) break;
                respList.add(KnowledgeBase[respIndex][1][j]);
            }
        }
    }

    public void preprocess_response() {
        if (sResponse.indexOf("*") != -1) {
            find_subject();
            sSubject = transpose(sSubject);
            sSubject = sSubject.trim();
            sResponse = sResponse.replace("*", " " + sSubject);
        }
    }

    public void find_subject() {
        sSubject = "";
        int pos = sInput.indexOf(sKeyWord);
        if (pos != -1) {
            sSubject = sInput.substring(pos + sKeyWord.length() - 1, sInput.length());
        }
    }

    public static String transpose(String str) {
        boolean bTransposed = false;
        for (int i = 0; i < transposList.length; ++i) {
            String first = transposList[i][1];
            first = " " + first + " ";
            String second = transposList[i][0];
            second = " " + second + " ";
            String backup = str;
            str = str.replace(first, second);
            if (str != backup) {
                bTransposed = true;
            }
        }
        if (!bTransposed) {
            for (int i = 0; i < transposList.length; ++i) {
                String first = transposList[i][0];
                first = " " + first + " ";
                String second = transposList[i][1];
                second = " " + second + " ";
                str = str.replace(first, second);
            }
        }
        return str;
    }

    private boolean wrong_location(String keyword, char firstChar, char lastChar, int pos) {
        boolean bWrongPos = false;
        pos += keyword.length();
        if ((firstChar == '_' && lastChar == '_' && sInput != keyword) || (firstChar != '_' && lastChar == '_' && pos != sInput.length()) || (firstChar == '_' && lastChar != '_' && pos == sInput.length())) {
            bWrongPos = true;
        }
        return bWrongPos;
    }

    public void handle_repetition() {
        if (respList.size() > 0) {
            respList.removeElementAt(0);
        }
        if (no_response()) {
            save_input();
            set_input(sEvent);
            find_match();
            restore_input();
        }
        select_response();
    }

    public void handle_user_repetition() {
        if (same_input()) {
            handle_event("REPETITION T1**");
        } else if (similar_input()) {
            handle_event("REPETITION T2**");
        }
    }

    public void handle_event(String str) {
        save_prev_event();
        set_event(str);
        save_input();
        str = " " + str + " ";
        set_input(str);
        if (!same_event()) {
            find_match();
        }
        restore_input();
    }

    public String signon() {
        handle_event("SIGNON**");
        select_response();
        return print_response();
    }

    public void select_response() {
        Collections.shuffle(respList);
        sResponse = respList.elementAt(0);
    }

    public void save_prev_input() {
        sPrevInput = sInput;
    }

    public void save_prev_response() {
        sPrevResponse = sResponse;
    }

    public void save_prev_event() {
        sPrevEvent = sEvent;
    }

    public void set_event(String str) {
        sEvent = str;
    }

    public void save_input() {
        sInputBackup = sInput;
    }

    public void set_input(String str) {
        sInput = str;
    }

    public void restore_input() {
        sInput = sInputBackup;
    }

    public String print_response() {
        if (sResponse.length() > 0) {
            return sResponse;
        }
        return "";
    }

    public void preprocess_input() {
        sInput = cleanString(sInput);
        sInput = sInput.toUpperCase();
        sInput = " " + sInput + " ";
    }

    public boolean bot_repeat() {
        return (sPrevResponse.length() > 0 && sResponse == sPrevResponse);
    }

    public boolean user_repeat() {
        return (sPrevInput.length() > 0 && ((sInput == sPrevInput) || (sInput.indexOf(sPrevInput) != -1) || (sPrevInput.indexOf(sInput) != -1)));
    }

    public boolean bot_understand() {
        return respList.size() > 0;
    }

    public boolean null_input() {
        return (sInput.length() == 0 && sPrevInput.length() != 0);
    }

    public boolean null_input_repetition() {
        return (sInput.length() == 0 && sPrevInput.length() == 0);
    }

    public boolean user_want_to_quit() {
        return sInput.indexOf("BYE") != -1;
    }

    public boolean same_event() {
        return (sEvent.length() > 0 && sEvent == sPrevEvent);
    }

    public boolean no_response() {
        return respList.size() == 0;
    }

    public boolean same_input() {
        return (sInput.length() > 0 && sInput == sPrevInput);
    }

    public boolean similar_input() {
        return (sInput.length() > 0 && (sInput.indexOf(sPrevInput) != -1 || sPrevInput.indexOf(sInput) != -1));
    }

    boolean isPunc(char ch) {
        return delim.indexOf(ch) != -1;
    }

    String cleanString(String str) {
        StringBuffer temp = new StringBuffer(str.length());
        char prevChar = 0;
        for (int i = 0; i < str.length(); ++i) {
            if ((str.charAt(i) == ' ' && prevChar == ' ') || !isPunc(str.charAt(i))) {
                temp.append(str.charAt(i));
                prevChar = str.charAt(i);
            } else if (prevChar != ' ' && isPunc(str.charAt(i))) {
                temp.append(' ');
                prevChar = ' ';
            }
        }
        return temp.toString();
    }

    String trimLR(String str, String delim) {
        StringBuffer temp = new StringBuffer(str);
        int index1 = temp.indexOf(delim);
        int index2 = temp.lastIndexOf(delim);
        if (index1 != -1) {
            temp.deleteCharAt(index1);
            index2--;
        }
        if (index2 > -1) {
            temp.deleteCharAt(index2);
        }
        return temp.toString();
    }
}
