package org.nightlabs.jfire.base.ui.prop.search;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.nightlabs.jdo.search.SearchFilterItem;
import org.nightlabs.jfire.prop.StructField;
import org.nightlabs.jfire.prop.id.StructFieldID;
import org.nightlabs.jfire.prop.search.TextPropSearchFilterItem;

/**
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 */
public class TextStructFieldSearchItemEditorHelper extends PropertySetStructFieldSearchItemEditorHelper {

    public static class Factory implements PropertySetSearchFilterItemEditorHelperFactory<TextStructFieldSearchItemEditorHelper> {

        public TextStructFieldSearchItemEditorHelper createHelper() {
            return new TextStructFieldSearchItemEditorHelper();
        }
    }

    private Composite helperComposite;

    private Combo comboMatchType;

    private Text textNeedle;

    /**
	 * 
	 */
    public TextStructFieldSearchItemEditorHelper() {
        super();
    }

    /**
	 * @param personStructField
	 */
    public TextStructFieldSearchItemEditorHelper(StructField structField) {
        super(structField);
    }

    protected class MatchTypeOrderEntry {

        int matchType;

        String displayName;

        public MatchTypeOrderEntry(int matchType, String displayName) {
            this.matchType = matchType;
            this.displayName = displayName;
        }
    }

    private MatchTypeOrderEntry[] matchTypeOrder = new MatchTypeOrderEntry[7];

    private MatchTypeOrderEntry setMatchTypeOrderEntry(int idx, int matchType) {
        String displayName = SearchFilterItem.getLocalisedMatchType(matchType);
        MatchTypeOrderEntry result = new MatchTypeOrderEntry(matchType, displayName);
        matchTypeOrder[idx] = result;
        return result;
    }

    /**
	 * @see org.nightlabs.jfire.base.ui.prop.search.PropertySetSearchFilterItemEditorHelper#getControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public Control getControl(Composite parent) {
        if (helperComposite == null) {
            helperComposite = new Composite(parent, SWT.NONE);
            GridLayout wrapperLayout = new GridLayout();
            wrapperLayout.numColumns = 2;
            wrapperLayout.makeColumnsEqualWidth = true;
            helperComposite.setLayout(wrapperLayout);
            helperComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
            comboMatchType = new Combo(helperComposite, SWT.READ_ONLY);
            comboMatchType.add(setMatchTypeOrderEntry(0, SearchFilterItem.MATCHTYPE_CONTAINS).displayName);
            comboMatchType.add(setMatchTypeOrderEntry(1, SearchFilterItem.MATCHTYPE_NOTCONTAINS).displayName);
            comboMatchType.add(setMatchTypeOrderEntry(2, SearchFilterItem.MATCHTYPE_BEGINSWITH).displayName);
            comboMatchType.add(setMatchTypeOrderEntry(3, SearchFilterItem.MATCHTYPE_ENDSWITH).displayName);
            comboMatchType.add(setMatchTypeOrderEntry(4, SearchFilterItem.MATCHTYPE_EQUALS).displayName);
            comboMatchType.add(setMatchTypeOrderEntry(5, SearchFilterItem.MATCHTYPE_MATCHES).displayName);
            comboMatchType.add(setMatchTypeOrderEntry(6, SearchFilterItem.MATCHTYPE_NOTEQUALS).displayName);
            GridData gdCombo = new GridData();
            gdCombo.grabExcessHorizontalSpace = true;
            gdCombo.horizontalAlignment = GridData.FILL;
            comboMatchType.setLayoutData(gdCombo);
            comboMatchType.select(SearchFilterItem.MATCHTYPE_DEFAULT - 1);
            textNeedle = new Text(helperComposite, SWT.BORDER);
            GridData gd = new GridData();
            gd.grabExcessHorizontalSpace = true;
            gd.horizontalAlignment = GridData.FILL;
            textNeedle.setLayoutData(gd);
        }
        return helperComposite;
    }

    /**
	 * @see org.nightlabs.jfire.base.ui.prop.search.PropertySetSearchFilterItemEditorHelper#getSearchFilterItem()
	 */
    @Override
    public SearchFilterItem getSearchFilterItem() {
        StructFieldID id = StructFieldID.create(personStructField.getStructBlockOrganisationID(), personStructField.getStructBlockID(), personStructField.getStructFieldOrganisationID(), personStructField.getStructFieldID());
        int matchType = matchTypeOrder[comboMatchType.getSelectionIndex()].matchType;
        String needle = textNeedle.getText();
        TextPropSearchFilterItem result = new TextPropSearchFilterItem(id, matchType, needle);
        return result;
    }

    /**
	 * @see org.nightlabs.jfire.base.ui.prop.search.PropertySetSearchFilterItemEditorHelper#close()
	 */
    public void close() {
    }
}
