package com.modelmetrics.utility.describe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.modelmetrics.common.poi.ExcelSupport;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PicklistEntry;

public class DataTemplateExcelBuilderDelegate {

    private Collection<String> requiredFields = new ArrayList<String>();

    public void handleBuild(Set<Field> fields, LayoutsSummary summary, ExcelSupport excelSupport, HSSFWorkbook workbook, String sheetName) {
        Map<String, Collection<String>> fieldNamesToRecordTypeNames = summary.getFieldNamesToRecordTypeNames();
        excelSupport.addSheet(sheetName);
        addRequiredFields(summary);
        HSSFRow fieldNameRow = excelSupport.addRow();
        excelSupport.decorateRowWithBoldCellBlueBackground(0, fieldNameRow, "FIELD NAME");
        HSSFRow fieldTypeRow = excelSupport.addRow();
        excelSupport.decorateRowWithBoldCellBlueBackground(0, fieldTypeRow, "FIELD TYPE");
        HSSFRow dependenciesRow = excelSupport.addRow();
        excelSupport.decorateRowWithBoldCellBlueBackground(0, dependenciesRow, "DEPENDENCIES");
        for (int i = 0; i < 7; i++) excelSupport.addRow();
        HSSFRow picklistValuesRow = excelSupport.addRow();
        excelSupport.decorateRowWithBoldCellBlueBackground(0, picklistValuesRow, "PICKLIST VALUES");
        HSSFRow associatedRecordTypesRow = excelSupport.addRow();
        excelSupport.decorateRowWithBoldCellBlueBackground(0, associatedRecordTypesRow, "ASSOCIATED RECORD TYPES");
        int column = 1;
        for (Iterator iter = fields.iterator(); iter.hasNext(); ) {
            Field element = (Field) iter.next();
            if (!element.getType().getValue().equals("id")) {
                if (!element.getLabel().equals("Created By ID") && !element.getLabel().equals("Created Date") && !element.getLabel().equals("Last Modified By ID") && !element.getLabel().equals("Last Modified Date") && !element.getLabel().equals("System Modstamp") && !element.getLabel().equals("Last Activity") && !element.getLabel().equals("Master Record ID") && !element.isCalculated()) {
                    if (requiredFields.contains(element.getName()) || element.getLabel().equals("Owner ID")) excelSupport.decorateRowWithBoldCellYellowBackground(column, fieldNameRow, element.getLabel()); else excelSupport.decorateRowWithBoldCell(column, fieldNameRow, element.getLabel());
                    String dataType = element.getType().getValue();
                    if (dataType.equals("string")) dataType = "Text";
                    String type;
                    if (dataType.equals("currency") || dataType.equals("double")) type = "Number(" + (element.getPrecision() - element.getScale()) + ", " + element.getScale() + ")"; else type = dataType + "(" + element.getLength() + ")";
                    if (element.getExternalId() != null && element.getExternalId().booleanValue()) type += " (ext id)";
                    if (type.equals("boolean(0)")) type = "Checkbox";
                    if (type.equals("date(0)")) type = "Date";
                    if (type.contains("picklist(")) type = "Picklist";
                    excelSupport.decorateRowWithCell(column, fieldTypeRow, type);
                    excelSupport.decorateRowWithCell(column, dependenciesRow, element.getControllerName());
                    excelSupport.decorateRowWithMultilineTextCell(column, picklistValuesRow, this.getValues(element.getPicklistValues()));
                    if (type.equals("Checkbox")) excelSupport.decorateRowWithMultilineTextCell(column, picklistValuesRow, "True\nFalse");
                    if (element.getLabel().equals("Record Type ID")) {
                        System.out.println("RecTypes" + summary.getRecordTypes());
                        excelSupport.decorateRowWithMultilineTextCell(column, picklistValuesRow, summary.getRecordTypes());
                    }
                    Collection<String> recordTypes = fieldNamesToRecordTypeNames.get(element.getName());
                    String recordTypeNames = "";
                    if (recordTypes != null) {
                        for (Iterator iterator = recordTypes.iterator(); iterator.hasNext(); ) recordTypeNames = recordTypeNames + (String) iterator.next() + "\n";
                        excelSupport.decorateRowWithMultilineTextCell(column, associatedRecordTypesRow, recordTypeNames);
                    }
                    column++;
                }
            }
        }
        excelSupport.setColumnWidthAuto(column);
    }

    private String getValues(PicklistEntry[] target) {
        StringBuffer ret = new StringBuffer();
        if (target != null) {
            for (int i = 0; i < target.length; i++) {
                if (ret.length() > 0) {
                    ret.append("\n");
                }
                ret.append(target[i].getValue());
            }
        }
        return ret.toString();
    }

    private String getValues(String[] target) {
        StringBuffer ret = new StringBuffer();
        if (target != null) {
            for (int i = 0; i < target.length; i++) {
                if (ret.length() > 0) {
                    ret.append(", ");
                }
                ret.append(target[i]);
            }
        }
        return ret.toString();
    }

    public void addRequiredFields(LayoutsSummary summary) {
        for (Iterator iter = summary.getRows().iterator(); iter.hasNext(); ) {
            LayoutsFieldVOV2 element = (LayoutsFieldVOV2) iter.next();
            for (Iterator iterator = element.getLayouts().iterator(); iterator.hasNext(); ) {
                FieldItemVO layoutElement = (FieldItemVO) iterator.next();
                if (layoutElement.isRequired()) {
                    requiredFields.add(element.getField().getName());
                    break;
                }
            }
        }
    }
}
