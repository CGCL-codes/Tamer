package com.student.util.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel���������ļ��Ľṹ
 */
public class ExcelStruct {

    private List<ImportCellDesc> onceImportCells;

    private List<ImportCellDesc> repeatImportCells;

    private String endCode;

    /**
	 * xml�ж����У����
	 */
    private Map<String, String> sysValidatorMap = new HashMap<String, String>();

    private Map<String, List<String>> cellValidatorMap = new HashMap<String, List<String>>();

    public ExcelStruct() {
        this.onceImportCells = new ArrayList<ImportCellDesc>();
        this.repeatImportCells = new ArrayList<ImportCellDesc>();
    }

    ;

    public ExcelStruct(List<ImportCellDesc> onceImportCells, List<ImportCellDesc> repeatImportCells, String endCode) {
        this.onceImportCells = onceImportCells;
        this.repeatImportCells = repeatImportCells;
        this.endCode = endCode;
    }

    public List<ImportCellDesc> getOnceImportCells() {
        return onceImportCells;
    }

    public void setOnceImportCells(List<ImportCellDesc> onceImportCells) {
        this.onceImportCells = onceImportCells;
    }

    public List<ImportCellDesc> getRepeatImportCells() {
        return repeatImportCells;
    }

    public void setRepeatImportCells(List<ImportCellDesc> repeatImportCells) {
        this.repeatImportCells = repeatImportCells;
    }

    public String getEndCode() {
        return endCode;
    }

    public void setEndCode(String endCode) {
        this.endCode = endCode;
    }

    public Map<String, String> getSysValidatorMap() {
        return sysValidatorMap;
    }

    public void setSysValidatorMap(Map<String, String> sysValidatorMap) {
        this.sysValidatorMap = sysValidatorMap;
    }

    public Map<String, List<String>> getCellValidatorMap() {
        return cellValidatorMap;
    }

    public void setCellValidatorMap(Map<String, List<String>> cellValidatorMap) {
        this.cellValidatorMap = cellValidatorMap;
    }

    /**
	 * ���У��������
	 * @param name	У�������
	 * @param value	У��������ȫ��
	 */
    public void addSysValidator(String name, String value) {
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(value)) {
            return;
        }
        if (this.sysValidatorMap == null) {
            this.sysValidatorMap = new HashMap<String, String>();
        }
        this.sysValidatorMap.put(name, value);
    }

    /**
	 * ��Ԫ�����У����
	 * @param cellname	��Ԫ�����
	 * @param validator	У�������
	 */
    public void addCellValidator(String cellname, String validator) {
        if (StringUtil.isEmpty(cellname) || StringUtil.isEmpty(validator)) {
            return;
        }
        if (this.cellValidatorMap == null) {
            this.cellValidatorMap = new HashMap<String, List<String>>();
        }
        cellname = cellname.toUpperCase();
        List<String> validatorList = this.cellValidatorMap.get(cellname);
        if (validatorList == null) {
            validatorList = new ArrayList<String>();
            this.cellValidatorMap.put(cellname, validatorList);
        }
        if (sysValidatorMap != null && sysValidatorMap.containsKey(validator)) {
            validatorList.add(sysValidatorMap.get(validator));
        }
    }
}
