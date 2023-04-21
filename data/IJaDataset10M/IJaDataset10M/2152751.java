package tufts.vue;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
	 * FilterStatementEditor
	 * This is the editor to create/modify logical filter statements.
	 *
	 **/
public class FilterStatementEditor extends JPanel {

    private static String[] sConditions = { "contains", "equals", "starts with", "ends with" };

    private static String[] sSources = { "All ", "Label", "Notes", "User Type", "Resource", "User Data" };

    protected SourceItem mAnywhereSource = new SourceItem(sSources[0], 0, null);

    private SourceItem mLabelSource = new SourceItem(sSources[1], 1, null);

    private SourceItem mNotesSource = new SourceItem(sSources[2], 2, null);

    private SourceItem mUserTypeSource = new SourceItem(sSources[3], 3, null);

    private SourceItem[] mDefaultSourceItems = { mAnywhereSource, mLabelSource, mNotesSource, mUserTypeSource };

    /** the horizontal layout fox **/
    Box mBox = null;

    /** the list of match conditions **/
    JComboBox mCondition = null;

    /** the value text edit field. **/
    JTextField mValue = null;

    /** the list of source types **/
    JComboBox mSource = null;

    /** the logical statement **/
    LWCFilter.LogicalStatement mStatement = null;

    /** The popup of map types **/
    JComboBox mMapTypes = null;

    /** the LWCFilter that this statement belongs to **/
    LWCFilter mFilter = null;

    /**
		 * FilterStatementEditor
		 *
		 **/
    public FilterStatementEditor(LWCFilter pFilter, LWCFilter.LogicalStatement pStatement) {
        mBox = Box.createHorizontalBox();
        mCondition = new JComboBox(sConditions);
        mSource = new JComboBox();
        mFilter = pFilter;
        mValue = new JTextField();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(4, 4, 4, 4));
        buildSourcesCombo(mFilter.getMap());
        mBox.add(mSource);
        mBox.add(mCondition);
        add(BorderLayout.WEST, mBox);
        add(BorderLayout.CENTER, mValue);
        setStatement(pStatement);
    }

    /**
		 * getStatement
		 * This returns the logical statement
		 * @return LogicalStatement the statement
		 **/
    public LWCFilter.LogicalStatement getStatement() {
        SourceItem item = (SourceItem) mSource.getSelectedItem();
        mStatement.setSourceType(item.getType());
        mStatement.setSourceID(item.getSourceID());
        mStatement.setValue(mValue.getText());
        mStatement.setCondition(mCondition.getSelectedIndex());
        return mStatement;
    }

    public void setStatement(LWCFilter.LogicalStatement pStatement) {
        mStatement = pStatement;
        mValue.setText(mStatement.getValue());
        mCondition.setSelectedIndex(mStatement.getCondition());
        setSourceCombo(mStatement.getSourceType(), mStatement.getSourceID());
    }

    public void refreshFields(LWMap pMap) {
    }

    private void setSourceCombo(int pSource, String pID) {
        if (pSource < mDefaultSourceItems.length) {
            mSource.setSelectedIndex(pSource);
        } else if (pSource == LWCFilter.USERDATA) {
            System.out.println("!!! FIX:  need to search user meta-data.");
        }
    }

    public void buildSourcesCombo(LWMap pMap) {
        mSource.removeAllItems();
        for (int i = 0; i < mDefaultSourceItems.length; i++) {
            mSource.addItem(mDefaultSourceItems[i]);
        }
    }

    public class SourceItem {

        String mName = null;

        int mSourceType = 0;

        String mSourceID = null;

        public SourceItem(String pName, int pType, String pID) {
            mName = pName;
            mSourceType = pType;
            mSourceID = pID;
        }

        public int getType() {
            return mSourceType;
        }

        public String getSourceID() {
            return mSourceID;
        }

        public String toString() {
            return mName;
        }
    }
}
