package is.iclt.icenlp.core.tritagger;

import is.iclt.icenlp.core.utils.Tag;
import is.iclt.icenlp.core.tokenizer.TokenTags;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * A hash of states.
 * <br> Used by TriTagger.
 * @author Hrafn Loftsson
 */
public class StateMap {

    HashMap myStateIDs;

    private final int maxNumberStates = 1310727;

    String[] myStates;

    public StateMap() {
        myStateIDs = new HashMap();
        myStates = new String[maxNumberStates];
    }

    public int getStateId(String tag) {
        Integer value = (Integer) myStateIDs.get(tag);
        if (value == null) return -1; else return value.intValue();
    }

    public int getStateId(String tag1, String tag2) {
        return getStateId(tag1 + " " + tag2);
    }

    public String getState(int num) {
        return myStates[num];
    }

    private String[] split(String str) {
        String strs[];
        strs = str.split(" ");
        if (strs.length != 2) {
            System.out.println("Expected two tags in string " + str);
            System.exit(1);
        }
        return strs;
    }

    public String getSecondTag(int num) {
        String strs[];
        String stateStr = getState(num);
        strs = split(stateStr);
        return strs[1];
    }

    public String getFirstTag(int num) {
        String strs[];
        String stateStr = getState(num);
        strs = split(stateStr);
        return strs[0];
    }

    private void quit(int states) {
        System.out.println("Found " + Integer.toString(states) + " states but maximum is: " + Integer.toString(maxNumberStates));
        System.exit(0);
    }

    public int mapTagsBigrams(ArrayList tokens) {
        myStateIDs.clear();
        int counter = 0;
        for (int i = 0; i < tokens.size(); i++) {
            TokenTags tok = (TokenTags) tokens.get(i);
            ArrayList tags = tok.getTags();
            for (int j = 0; j < tags.size(); j++) {
                Tag tag = (Tag) tags.get(j);
                String tagStr = tag.getTagStr();
                if (!myStateIDs.containsKey(tagStr)) {
                    myStateIDs.put(tagStr, new Integer(counter));
                    if (counter == maxNumberStates) quit(counter); else myStates[counter] = tagStr;
                    counter++;
                }
            }
        }
        return counter;
    }

    public int mapTagsTrigrams(ArrayList tokens) {
        myStateIDs.clear();
        int counter = 0;
        for (int i = 1; i < tokens.size(); i++) {
            TokenTags tok = (TokenTags) tokens.get(i);
            TokenTags prevTok = (TokenTags) tokens.get(i - 1);
            ArrayList tags = tok.getTags();
            ArrayList prevTags = prevTok.getTags();
            for (int j = 0; j < tags.size(); j++) {
                Tag tag = (Tag) tags.get(j);
                String currTagStr = tag.getTagStr();
                for (int k = 0; k < prevTags.size(); k++) {
                    Tag prevTag = (Tag) prevTags.get(k);
                    String prevTagStr = prevTag.getTagStr();
                    String key = prevTagStr + " " + currTagStr;
                    if (!myStateIDs.containsKey(key)) {
                        myStateIDs.put(key, new Integer(counter));
                        if (counter == maxNumberStates) quit(counter + 1); else myStates[counter] = key;
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
}
