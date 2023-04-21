package com.ojt.export.xlspoi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.export.GroupExportListener;
import java.util.List;

/**
 * Compl�te la feuille r�sultat
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

    private static final String COMPETITOR_GRADE = "%%COMPETITOR_GRADE%%";

    private static final String COMPETITOR_WEIGHT = "%%COMPETITOR_WEIGHT%%";

    private static final String COMPETITOR_CLUB = "%%COMPETITOR_CLUB%%";

    private static final String COMPETITOR_ORDER = "%%COMPETITOR_ORDER%%";

    private int groupRowIndex = -1;

    private int groupColIndex = -1;

    private int competitorRowIndex = -1;

    private int currentRow;

    private final HSSFSheet resultsSheet;

    private CompetitorGroup firstGroup = null;

    private Competitor firstCompetitor = null;

    private final List<String> competitorDifferentCellProperties = null;

    private final List<String> groupDifferentCellProperties = null;

    private HSSFCell groupCell;

    public ResultsSheetCompleter(final HSSFSheet resultsSheet) {
        super();
        this.resultsSheet = resultsSheet;
    }

    public void init(final CompetitionDescriptor competitionDescriptor) {
        for (int row = 0; row < MAX_ROWS; row++) {
            final HSSFRow xlsRow = resultsSheet.getRow(row);
            if (xlsRow != null) {
                for (int col = 0; col < MAX_COLS; col++) {
                    final HSSFCell cell = xlsRow.getCell(col);
                    if (cell != null) {
                        final String originalText = XlsPoiUtilities.getCellValueAsString(cell);
                        if ((originalText != null) && !originalText.trim().isEmpty()) {
                            final String modifiedText = completeWithCompetitionDescriptor(originalText, competitionDescriptor);
                            if (!modifiedText.equals(originalText)) {
                                cell.setCellValue(new HSSFRichTextString(modifiedText));
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
            }
        }
        currentRow = Math.max(groupRowIndex, competitorRowIndex) + 1;
        if ((groupRowIndex > -1) && (competitorRowIndex > -1)) {
            groupCell = resultsSheet.getRow(groupRowIndex).getCell(groupColIndex);
        }
    }

    public void appendGroup(final CompetitorGroup group, final int groupNumber, final boolean isFirstGroup) {
        final HSSFRow row = resultsSheet.createRow(currentRow);
        if (!isFirstGroup) {
            final HSSFCell currentGroupCell = row.createCell(groupColIndex);
            currentGroupCell.setCellStyle(groupCell.getCellStyle());
            currentGroupCell.setCellValue(new HSSFRichTextString(completeWithGroupInfos(XlsPoiUtilities.getCellValueAsString(groupCell), group, groupNumber)));
            currentRow++;
        }
        for (int i = (isFirstGroup ? 1 : 0); i < group.getCompetitors().size(); i++) {
            final HSSFRow competitorRow = resultsSheet.createRow(currentRow);
            for (int col = 0; col < 3; col++) {
                final HSSFCell cell = resultsSheet.getRow(competitorRowIndex).getCell(col);
                final HSSFCell newCell = competitorRow.createCell(col);
                newCell.setCellStyle(cell.getCellStyle());
                final String cellText = XlsPoiUtilities.getCellValueAsString(cell);
                if ((cellText != null) && !cellText.trim().isEmpty()) {
                    newCell.setCellValue(new HSSFRichTextString(completeWithCompetitorInfos(cellText, group.getCompetitors().get(i))));
                }
            }
            currentRow++;
        }
        currentRow += 2;
    }

    public void cleanSheet() {
        if (groupCell != null) {
            groupCell.setCellValue(new HSSFRichTextString(completeWithGroupInfos(XlsPoiUtilities.getCellValueAsString(groupCell), firstGroup, 1)));
            for (int col = 0; col < 3; col++) {
                final HSSFCell cell = resultsSheet.getRow(competitorRowIndex).getCell(col);
                final String cellText = XlsPoiUtilities.getCellValueAsString(cell);
                if ((cellText != null) && !cellText.trim().isEmpty()) {
                    cell.setCellValue(new HSSFRichTextString(completeWithCompetitorInfos(cellText, firstCompetitor)));
                }
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

    private String completeWithCompetitorInfos(final String cellText, final Competitor competitor) {
        String text = cellText;
        if (text.contains(COMPETITOR_NAME)) {
            text = text.replaceAll(COMPETITOR_NAME, competitor.getDisplayName());
        }
        if (text.contains(COMPETITOR_GRADE)) {
            text = text.replaceAll(COMPETITOR_GRADE, competitor.getGradeBelt() == null ? "" : competitor.getGradeBelt().abbreviation);
        }
        if (text.contains(COMPETITOR_WEIGHT)) {
            text = text.replaceAll(COMPETITOR_WEIGHT, String.valueOf(competitor.getWeightAsFloat()) + " kg");
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
