package com.ojt.export.openoffice;

import java.util.LinkedList;
import java.util.List;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.export.GroupExportListener;
import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;

/**
 * Compl�te la feuille r�sultat
 *
 * @author R�mi "DwarfConan" Guitreau 
 * @since 21 oct. 2009 : Cr�ation
 */
public class ResultsSheetCompleter implements GroupExportListener {

    private static final int MAX_ROWS = 10;

    private static final int MAX_COLS = 4;

    private static final String DATE = "%%DATE%%";

    private static final String PERCENT_STEP = "%%PERCENT_STEP%%";

    private static final String MAX_WEIGHT = "%%MAX_WEIGHT%%";

    private static final String MIN_WEIGHT = "%%MIN_WEIGHT%%";

    private static final String COMPETITOR_NUMBER = "%%COMPETITOR_NUMBER%%";

    private static final String GROUP_NUMBER = "%%GROUP_NUMBER%%";

    private static final String LOCATION = "%%LOCATION%%";

    private static final String COMPETITION_NAME = "%%COMPETITION_NAME%%";

    private static final String COMPETITOR_NAME = "%%COMPETITOR_NAME%%";

    private static final String COMPETITOR_CLUB = "%%COMPETITOR_CLUB%%";

    private static final String COMPETITOR_ORDER = "%%COMPETITOR_ORDER%%";

    private int groupRowIndex = -1;

    private int groupColIndex = -1;

    private int competitorRowIndex = -1;

    private int currentRow;

    private final XSpreadsheet resultsSheet;

    private CompetitorGroup firstGroup = null;

    private Competitor firstCompetitor = null;

    private List<String> competitorDifferentCellProperties = null;

    private List<String> groupDifferentCellProperties = null;

    private XCell groupCell;

    public ResultsSheetCompleter(final XSpreadsheet resultsSheet) {
        super();
        this.resultsSheet = resultsSheet;
    }

    public void init(final CompetitionDescriptor competitionDescriptor) throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException, RowsExceededException, WriteException, UnknownPropertyException, PropertyVetoException, IllegalArgumentException {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                final XCell cell = resultsSheet.getCellByPosition(col, row);
                final XText xCellText = (com.sun.star.text.XText) UnoRuntime.queryInterface(XText.class, cell);
                if ((xCellText.getText() != null) && !xCellText.getString().trim().isEmpty()) {
                    final String originalText = xCellText.getString();
                    String modifiedText = completeWithCompetitionDescriptor(originalText, competitionDescriptor);
                    if (!modifiedText.equals(originalText)) {
                        cell.setFormula(modifiedText);
                    } else {
                        if (isGroupRow(originalText)) {
                            groupRowIndex = row;
                            groupColIndex = col;
                        } else if (isCompetitorRow(originalText)) {
                            competitorRowIndex = row;
                        }
                    }
                }
            }
        }
        currentRow = Math.max(groupRowIndex, competitorRowIndex) + 1;
        if (groupRowIndex > -1 && competitorRowIndex > -1) {
            groupCell = resultsSheet.getCellByPosition(groupColIndex, groupRowIndex);
        }
    }

    public void appendGroup(final CompetitorGroup group, final int groupNumber, final boolean isFirstGroup) throws IndexOutOfBoundsException, UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        if (!isFirstGroup) {
            final XCell currentGroupCell = resultsSheet.getCellByPosition(groupColIndex, currentRow);
            if (groupDifferentCellProperties == null) {
                groupDifferentCellProperties = copyCellTypeAndGetDifferents(groupCell, currentGroupCell);
            } else {
                copyOnlyDifferentsCellType(groupCell, currentGroupCell, groupDifferentCellProperties);
            }
            currentGroupCell.setFormula(completeWithGroupInfos(((XText) UnoRuntime.queryInterface(XText.class, groupCell)).getString(), group, groupNumber));
            currentRow++;
        }
        for (int i = (isFirstGroup ? 1 : 0); i < group.getCompetitors().size(); i++) {
            for (int col = 0; col < 3; col++) {
                final XCell cell = resultsSheet.getCellByPosition(col, competitorRowIndex);
                final XCell newCell = resultsSheet.getCellByPosition(col, currentRow);
                if (competitorDifferentCellProperties == null) {
                    competitorDifferentCellProperties = copyCellTypeAndGetDifferents(cell, newCell);
                } else {
                    copyOnlyDifferentsCellType(cell, newCell, competitorDifferentCellProperties);
                }
                final String cellText = ((XText) UnoRuntime.queryInterface(XText.class, cell)).getString();
                if ((cellText != null) && !cellText.trim().isEmpty()) {
                    newCell.setFormula(completeWithCompetitorInfos(cellText, group.getCompetitors().get(i)));
                }
            }
            currentRow++;
        }
        currentRow += 2;
    }

    public void cleanSheet() throws IndexOutOfBoundsException {
        groupCell.setFormula(completeWithGroupInfos(((XText) UnoRuntime.queryInterface(XText.class, groupCell)).getString(), firstGroup, 1));
        for (int col = 0; col < 3; col++) {
            final XCell cell = resultsSheet.getCellByPosition(col, competitorRowIndex);
            final String cellText = ((XText) UnoRuntime.queryInterface(XText.class, cell)).getString();
            if ((cellText != null) && !cellText.trim().isEmpty()) {
                cell.setFormula(completeWithCompetitorInfos(cellText, firstCompetitor));
            }
        }
    }

    @Override
    public void competitorExported(final Competitor competitor) {
    }

    @Override
    public void exportFinished() {
    }

    @Override
    public void groupExportBegin(final String groupName, final CompetitorGroup group, final int groupNumber) {
        boolean isFirstGroup = false;
        if (firstGroup == null) {
            isFirstGroup = true;
            firstGroup = group;
            firstCompetitor = group.getCompetitors().get(0);
        }
        try {
            appendGroup(group, groupNumber, isFirstGroup);
        } catch (final Exception ex) {
            throw new RuntimeException("Erreur lors de l'ajout du groupe aux r�sultats", ex);
        }
    }

    private List<String> copyCellTypeAndGetDifferents(final XCell cell1, final XCell cell2) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        final List<String> differentsProperties = new LinkedList<String>();
        final XPropertySet set1 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell1);
        final XPropertySet set2 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell2);
        for (Property prop : set1.getPropertySetInfo().getProperties()) {
            if (!set1.getPropertyValue(prop.Name).equals(set2.getPropertyValue(prop.Name))) {
                set2.setPropertyValue(prop.Name, set1.getPropertyValue(prop.Name));
                differentsProperties.add(prop.Name);
            }
        }
        return differentsProperties;
    }

    private void copyOnlyDifferentsCellType(final XCell cell1, final XCell cell2, final List<String> differentsProperties) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        final XPropertySet set1 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell1);
        final XPropertySet set2 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell2);
        for (final String prop : differentsProperties) {
            set2.setPropertyValue(prop, set1.getPropertyValue(prop));
        }
    }

    private String completeWithCompetitorInfos(final String cellText, final Competitor competitor) {
        String text = cellText;
        if (text.contains(COMPETITOR_NAME)) {
            text = text.replaceAll(COMPETITOR_NAME, competitor.getDisplayName());
        }
        if (text.contains(COMPETITOR_CLUB)) {
            text = text.replaceAll(COMPETITOR_CLUB, competitor.getClub().toString());
        }
        if (text.contains(COMPETITOR_ORDER)) {
            text = text.replaceAll(COMPETITOR_ORDER, "");
        }
        return text;
    }

    private String completeWithGroupInfos(final String cellText, final CompetitorGroup group, final int groupNb) {
        String text = cellText;
        if (text.contains(GROUP_NUMBER)) {
            text = text.replaceAll(GROUP_NUMBER, String.valueOf(groupNb));
        }
        if (text.contains(COMPETITOR_NUMBER)) {
            text = text.replaceAll(COMPETITOR_NUMBER, String.valueOf(group.getCompetitorsNumber()));
        }
        if (text.contains(MIN_WEIGHT)) {
            text = text.replaceAll(MIN_WEIGHT, String.valueOf(group.getMinGroupWeight()));
        }
        if (text.contains(MAX_WEIGHT)) {
            text = text.replaceAll(MAX_WEIGHT, String.valueOf(group.getMaxGroupWeight()));
        }
        if (text.contains(PERCENT_STEP)) {
            text = text.replaceAll(PERCENT_STEP, String.valueOf(group.getWeightStepInPercent()));
        }
        return text;
    }

    private boolean isGroupRow(final String cellText) {
        return cellText.contains(GROUP_NUMBER);
    }

    private boolean isCompetitorRow(final String cellText) {
        return cellText.contains(COMPETITOR_NAME);
    }

    private String completeWithCompetitionDescriptor(final String cellText, final CompetitionDescriptor competitionDescriptor) {
        String text = cellText;
        if (text.contains(COMPETITION_NAME)) {
            text = text.replaceAll(COMPETITION_NAME, competitionDescriptor.getCompetitionName());
        }
        if (text.contains(LOCATION)) {
            text = text.replaceAll(LOCATION, competitionDescriptor.getLocation());
        }
        if (text.contains(DATE)) {
            text = text.replaceAll(DATE, competitionDescriptor.getDate());
        }
        return text;
    }
}
