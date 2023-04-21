package org.adl.datamodels.cmi;

import java.io.Serializable;
import java.util.StringTokenizer;
import org.adl.util.debug.DebugIndicator;

public class CMIRequest implements Serializable {

    static void incrementPatternCount() {
        pattern_counter++;
    }

    static void resetPatternCounter() {
        pattern_counter = 2;
    }

    public CMIRequest(String s, boolean flag) {
        numSub = 10;
        numOfSubCategories = 0;
        numOfSubCatReturned = 0;
        numInd = 10;
        numOfInd = 0;
        numOfIndReturned = 0;
        totalNumberOfTokens = 0;
        tokensRequested = 1;
        tokensProcessed = 0;
        numPat = 10;
        if (DebugIndicator.ON) if (flag) System.out.println("Building CMIRequest for a LMSGetValue(" + s + ") "); else System.out.println("Building CMIRequest for a LMSSetValue(" + s + ") ");
        request = s;
        model = new String("");
        baseCategory = new String("");
        element = new String("");
        setValue = new String("");
        getRequest = flag;
        index = new Integer[numInd];
        for (int i = 0; i < index.length; i++) index[i] = new Integer(-1);
        pattern = new Integer[numPat];
        for (int j = 0; j < pattern.length; j++) pattern[j] = new Integer(-1);
        subcategory = new String[numSub];
        for (int k = 0; k < subcategory.length; k++) subcategory[k] = new String("-1");
        String s1 = new String("");
        String s2 = new String("");
        if (getRequest) {
            setRequest = false;
            parseGetRequest(s);
        } else {
            setRequest = true;
            StringTokenizer stringtokenizer = new StringTokenizer(s, ",", false);
            int l = stringtokenizer.countTokens();
            int i1 = 0;
            String s3 = stringtokenizer.nextToken();
            i1++;
            if (DebugIndicator.ON) System.out.println("Request: [" + s3 + "]");
            if (l == 1) {
                s1 = new String("");
            } else {
                while (i1 < l) {
                    s1 = s1 + stringtokenizer.nextToken();
                    if (++i1 != l) s1 = s1 + ",";
                }
                if (DebugIndicator.ON) System.out.println("Set Value: [" + s1 + "]");
            }
            totalNumberOfTokens++;
            parseSetRequest(s3, s1);
        }
        if (DebugIndicator.ON) showRequest();
    }

    public String getModel() {
        return model;
    }

    public boolean isForASetRequest() {
        return setRequest;
    }

    public boolean isAKeywordRequest() {
        boolean flag = false;
        if (isAChildrenRequest() || isACountRequest() || isAVersionRequest()) flag = true;
        return flag;
    }

    public boolean isAChildrenRequest() {
        boolean flag = false;
        if (element.equals("_children")) flag = true;
        return flag;
    }

    public boolean isACountRequest() {
        boolean flag = false;
        if (element.equals("_count")) flag = true;
        return flag;
    }

    public boolean isAVersionRequest() {
        boolean flag = false;
        if (element.equals("_version")) flag = true;
        return flag;
    }

    public String getRequest() {
        return request;
    }

    public String getBaseCategory() {
        tokensRequested++;
        return baseCategory;
    }

    public String getValue() {
        return setValue;
    }

    public String getElement() {
        return element;
    }

    public int getTotalNumTokens() {
        return totalNumberOfTokens;
    }

    public int getNumSubCat() {
        return numOfSubCategories;
    }

    public int getNumIndices() {
        return numOfInd;
    }

    public Integer getIndex(int i) {
        return index[i];
    }

    public String getSubCategory(int i) {
        String s = new String("-1");
        if (i >= 0 && i < 10) s = subcategory[i];
        return s;
    }

    private void parseGetRequest(String s) {
        StringTokenizer stringtokenizer = new StringTokenizer(s, ".", false);
        totalNumberOfTokens = totalNumberOfTokens + stringtokenizer.countTokens();
        model = stringtokenizer.nextToken();
        tokensProcessed++;
        pattern[0] = new Integer(MODEL);
        baseCategory = stringtokenizer.nextToken();
        tokensProcessed++;
        pattern[1] = new Integer(BASE_CATEGORY);
        if (stringtokenizer.hasMoreTokens()) {
            for (boolean flag = false; !flag; ) if (onLastToken()) {
                String s1 = stringtokenizer.nextToken();
                try {
                    Integer integer = new Integer(s1);
                    int i = findNextIndexLoc();
                    index[i] = integer;
                    numOfInd++;
                    int k = findNextPatternLoc();
                    pattern[k] = new Integer(ARRAY_INDEX);
                    element = subcategory[numOfSubCategories - 1];
                    k = findNextPatternLoc();
                    pattern[k] = new Integer(ELEMENT);
                } catch (NumberFormatException numberformatexception) {
                    element = s1;
                    int j = findNextPatternLoc();
                    pattern[j] = new Integer(ELEMENT);
                }
                tokensProcessed++;
                flag = true;
            } else {
                determineNextToken(stringtokenizer);
            }
        } else {
            element = baseCategory;
        }
    }

    private void parseSetRequest(String s, String s1) {
        if (DebugIndicator.ON) System.out.println("In CMIRequest::parseSetRequest");
        StringTokenizer stringtokenizer = new StringTokenizer(s, ".", false);
        totalNumberOfTokens = totalNumberOfTokens + stringtokenizer.countTokens();
        model = stringtokenizer.nextToken();
        tokensProcessed++;
        pattern[0] = new Integer(MODEL);
        baseCategory = stringtokenizer.nextToken();
        tokensProcessed++;
        pattern[1] = new Integer(BASE_CATEGORY);
        if (stringtokenizer.hasMoreTokens()) {
            for (boolean flag = false; !flag; ) if (onLastToken()) {
                setValue = s1;
                tokensProcessed++;
                int j = findNextPatternLoc();
                pattern[j] = new Integer(VALUE);
                flag = true;
            } else if (totalNumberOfTokens - tokensProcessed == 2) {
                element = stringtokenizer.nextToken();
                tokensProcessed++;
                int k = findNextPatternLoc();
                pattern[k] = new Integer(ELEMENT);
            } else {
                determineNextToken(stringtokenizer);
            }
        } else {
            setValue = s1;
            tokensProcessed++;
            int i = findNextPatternLoc();
            pattern[i] = new Integer(VALUE);
        }
    }

    private void determineNextToken(StringTokenizer stringtokenizer) {
        String s = stringtokenizer.nextToken();
        tokensProcessed++;
        try {
            Integer integer = new Integer(s);
            int i = findNextIndexLoc();
            index[i] = integer;
            numOfInd++;
            int k = findNextPatternLoc();
            pattern[k] = new Integer(ARRAY_INDEX);
        } catch (NumberFormatException numberformatexception) {
            int j = findNextSubLoc();
            subcategory[j] = s;
            numOfSubCategories++;
            int l = findNextPatternLoc();
            pattern[l] = new Integer(SUB_CATEGORY);
        }
    }

    private boolean onLastToken() {
        boolean flag = false;
        if (totalNumberOfTokens - tokensProcessed == 1) flag = true;
        return flag;
    }

    public String getNextToken() {
        String s = new String("");
        try {
            Integer integer = pattern[pattern_counter];
            int i = integer.intValue();
            if (i == SUB_CATEGORY) try {
                s = subcategory[numOfSubCatReturned];
                numOfSubCatReturned++;
            } catch (IndexOutOfBoundsException indexoutofboundsexception1) {
                System.out.println(indexoutofboundsexception1);
            } else if (i == ARRAY_INDEX) try {
                Integer integer1 = index[numOfIndReturned];
                numOfIndReturned++;
                s = integer1.toString();
            } catch (IndexOutOfBoundsException indexoutofboundsexception2) {
                System.out.println(indexoutofboundsexception2);
            } else if (i == ELEMENT) {
                s = element;
                resetPatternCounter();
            } else if (i == VALUE) s = setValue;
            if (i != ELEMENT) incrementPatternCount();
            tokensRequested++;
        } catch (IndexOutOfBoundsException indexoutofboundsexception) {
            System.out.println(indexoutofboundsexception);
        }
        return s;
    }

    public boolean hasMoreTokensToProcess() {
        boolean flag = true;
        if (setRequest) {
            if (totalNumberOfTokens - 1 - tokensRequested == 0) flag = false;
        } else if (totalNumberOfTokens - tokensRequested == 0) flag = false;
        return flag;
    }

    public void done() {
        resetPatternCounter();
        tokensRequested = 1;
        numOfSubCatReturned = 0;
        numOfIndReturned = 0;
    }

    private int findNextPatternLoc() {
        int i = -1;
        byte byte0 = -1;
        for (int j = 0; j <= numPat; j++) {
            if (byte0 != pattern[j].intValue()) continue;
            i = j;
            break;
        }
        return i;
    }

    private int findNextIndexLoc() {
        int i = -1;
        byte byte0 = -1;
        for (int j = 0; j <= numInd; j++) {
            if (byte0 != index[j].intValue()) continue;
            i = j;
            break;
        }
        return i;
    }

    private int findNextSubLoc() {
        int i = -1;
        String s = new String("-1");
        for (int j = 0; j <= numSub; j++) {
            if (!subcategory[j].equalsIgnoreCase(s)) continue;
            i = j;
            break;
        }
        return i;
    }

    public void showRequest() {
        if (DebugIndicator.ON) {
            System.out.println("Base Category: " + baseCategory);
            System.out.println("Element: " + element);
            System.out.println("Model: " + model);
            System.out.println("Number of Indices: " + numOfInd);
            System.out.println("Number of SubCategories: " + numOfSubCategories);
            for (int i = 0; i < numOfSubCategories; i++) System.out.println("Subcategory[" + i + "]: " + subcategory[i]);
            for (int j = 0; j < numOfInd; j++) System.out.println("Index[" + j + "]: " + index[j]);
            for (int k = 0; k < numPat; k++) if (pattern[k].intValue() > -1) System.out.println("Pattern[" + k + "]: " + pattern[k]);
            if (isForASetRequest()) System.out.println("Set Value: " + setValue);
        }
    }

    private String request;

    private String model;

    private String baseCategory;

    private int numSub;

    private String subcategory[];

    private int numOfSubCategories;

    private int numOfSubCatReturned;

    private int numInd;

    private Integer index[];

    private int numOfInd;

    private int numOfIndReturned;

    private String element;

    private String setValue;

    private int totalNumberOfTokens;

    private int tokensRequested;

    private int tokensProcessed;

    private boolean getRequest;

    private boolean setRequest;

    private int numPat;

    private Integer pattern[];

    private static int MODEL = 1;

    private static int BASE_CATEGORY = 2;

    private static int SUB_CATEGORY = 3;

    private static int ARRAY_INDEX = 4;

    private static int ELEMENT = 5;

    private static int VALUE = 6;

    static int pattern_counter = 2;
}
