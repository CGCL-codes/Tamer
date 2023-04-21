package com.googlecode.jcompare.filesys;

import com.googlecode.jcompare.model.ElementProvider;
import com.googlecode.jcompare.model.Item;
import com.googlecode.jcompare.model.Item.State;
import com.googlecode.jcompare.model.ItemState;
import com.googlecode.jcompare.model.StockItemStates;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public class FilesysElementProvider implements ElementProvider {

    public List<String> getNodeChildren(String path, Object data) {
        File pathFile = new File(path);
        String[] nameList = pathFile.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                File newFile = new File(dir + File.separator + name);
                if (newFile.isDirectory()) {
                    String[] childList = newFile.list();
                    if (childList.length > 0) {
                        return true;
                    }
                }
                return false;
            }
        });
        ArrayList<String> list = new ArrayList<String>();
        for (String name : nameList) {
            list.add(name);
        }
        return list;
    }

    public List<String> getLeafChildren(String path, Object data) {
        File pathFile = new File(path);
        String[] nameList = pathFile.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                File newFile = new File(dir + File.separator + name);
                if (newFile.isFile()) {
                    return true;
                } else {
                    String[] childList = newFile.list();
                    if (childList.length == 0) {
                        return true;
                    }
                }
                return false;
            }
        });
        ArrayList<String> list = new ArrayList<String>();
        for (String name : nameList) {
            list.add(name);
        }
        return list;
    }

    public Object getData(String path, String key) {
        return new File(getPath(path, key));
    }

    public String getPath(String path, String key) {
        return path + File.separator + key;
    }

    public ItemState getState(List<State> leftStateList, List<State> rightStateList, Object leftData, Object rightData) {
        ItemState itemState;
        if (leftStateList.isEmpty() && rightStateList.isEmpty()) {
            itemState = getState(leftData.toString(), rightData.toString(), leftData, rightData);
        } else {
            itemState = new ItemState();
            itemState.setLeftState(determineStatus(leftStateList));
            itemState.setRightState(determineStatus(rightStateList));
        }
        return itemState;
    }

    public State determineStatus(List<State> stateList) {
        boolean uncheckedStatus = stateExists(stateList, StockItemStates.STATE_UNCHECKED);
        if (uncheckedStatus) {
            return StockItemStates.STATE_UNCHECKED;
        }
        boolean newStatus = stateExists(stateList, newState);
        boolean oldStatus = stateExists(stateList, oldState);
        boolean sameStatus = stateExists(stateList, sameState);
        State state;
        if (newStatus && oldStatus) {
            state = newOldState;
        } else {
            if (newStatus) {
                state = newState;
            } else if (oldStatus) {
                state = oldState;
            } else if (sameStatus) {
                state = sameState;
            } else {
                state = StockItemStates.STATE_UNCHECKED;
            }
        }
        return state;
    }

    public boolean stateExists(List<State> stateList, State state) {
        boolean status = false;
        for (State stateItem : stateList) {
            if (stateItem == state) {
                status = true;
            }
        }
        return status;
    }

    public static class NewState implements Item.State {

        @Override
        public String toString() {
            return "NewState";
        }
    }

    public static class OldState implements Item.State {

        @Override
        public String toString() {
            return "OldState";
        }
    }

    public static class NewOldState implements Item.State {

        @Override
        public String toString() {
            return "NewOldState";
        }
    }

    public static class SameState implements Item.State {

        @Override
        public String toString() {
            return "SameState";
        }
    }

    private final Item.State newState = new NewState();

    private final Item.State oldState = new OldState();

    private final Item.State newOldState = new NewOldState();

    private final Item.State sameState = new SameState();

    public ItemState getState(String leftPath, String rightPath, Object leftData, Object rightData) {
        File leftFile = (File) leftData;
        File rightFile = (File) rightData;
        long leftModified = leftFile.lastModified();
        long rightModified = rightFile.lastModified();
        ItemState itemState = new ItemState();
        if (leftModified > rightModified) {
            itemState.setLeftState(newState);
            itemState.setRightState(oldState);
        } else if (leftModified < rightModified) {
            itemState.setLeftState(oldState);
            itemState.setRightState(newState);
        } else {
            itemState.setLeftState(sameState);
            itemState.setRightState(sameState);
        }
        return itemState;
    }
}
