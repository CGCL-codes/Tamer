package com.pk.platform.business.charge.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.pk.platform.business.charge.dao.IIncomeExpenseDao;
import com.pk.platform.business.charge.service.IIncomeExpenseService;
import com.pk.platform.business.charge.vo.IncomeExpenseVO;
import com.pk.platform.domain.charge.IncomeExpense;
import com.pk.platform.domain.charge.IncomeExpenseIndex;
import com.pk.platform.domain.charge.IncomeExpenseLock;
import com.pk.platform.domain.common.Kindergarten;
import com.pk.platform.util.DateConverter;
import com.pk.platform.util.constant.Constant;
import com.pk.platform.util.page.ListPage;
import com.pk.platform.util.page.Pager;

public class IncomeExpenseServiceImpl implements IIncomeExpenseService {

    private IIncomeExpenseDao incomeExpenseDao;

    public ListPage getReportList(IncomeExpenseVO ivo, Pager pager) {
        return incomeExpenseDao.getReportList(ivo, pager);
    }

    public IncomeExpenseVO getTotalAmount(IncomeExpenseVO ivo) {
        return incomeExpenseDao.getTotalAmount(ivo);
    }

    public List<Map<String, Object>> queryYearAgo(String kgId, String thisYear) {
        List<Map<String, Object>> list = incomeExpenseDao.queryYearAgo(kgId, thisYear);
        String currentYear = DateConverter.getCurrentYearStr();
        if (!currentYear.equals(thisYear)) {
            Map<String, Object> currYearMap = new HashMap<String, Object>();
            currYearMap.put("year", currentYear);
            list.add(0, currYearMap);
        }
        return list;
    }

    public List<Map<String, Object>> queryLockMonth(String year, String kgId) {
        return incomeExpenseDao.queryLockMonth(year, kgId);
    }

    public void addIncomeExpense(IncomeExpense ie) {
        incomeExpenseDao.add(ie);
        incomeExpenseDao.addOperateLog("新增其他收入与支出", Constant.SUCCESSFUL, ie.getKindergarten());
    }

    public void delIncomeExpense(String[] ids) {
        for (String id : ids) {
            IncomeExpense ie = this.queryIncomeExpenseById(id);
            incomeExpenseDao.addOperateLog("删除其他收入与支出", Constant.SUCCESSFUL, ie.getKindergarten());
            incomeExpenseDao.delete(IncomeExpense.class, id);
        }
    }

    public void updateIncomeExpense(IncomeExpense ie) {
        IncomeExpense incomeExpense = this.queryIncomeExpenseById(ie.getId());
        incomeExpense.setIncomeExpenseIndex(ie.getIncomeExpenseIndex());
        incomeExpense.setChargeTime(ie.getChargeTime());
        incomeExpense.setAmount(ie.getAmount());
        incomeExpense.setRemark(ie.getRemark());
        incomeExpenseDao.addOperateLog("修改其他收入与支出", Constant.SUCCESSFUL, incomeExpense.getKindergarten());
    }

    public IncomeExpense queryIncomeExpenseById(String id) {
        return incomeExpenseDao.get(IncomeExpense.class, id);
    }

    public void addLock(IncomeExpenseLock iel) {
        if (!incomeExpenseDao.existLock(iel.getYear(), iel.getMonth(), iel.getKindergarten().getId())) {
            incomeExpenseDao.add(iel);
            Kindergarten kinder = incomeExpenseDao.get(Kindergarten.class, iel.getKindergarten().getId());
            incomeExpenseDao.addOperateLog("上锁" + iel.getYear() + iel.getMonth(), Constant.SUCCESSFUL, kinder);
        }
    }

    public void delLock(String year, String month, String kgId) {
        incomeExpenseDao.delLock(year, month, kgId);
        Kindergarten kinder = incomeExpenseDao.get(Kindergarten.class, kgId);
        incomeExpenseDao.addOperateLog("解锁" + year + month, Constant.SUCCESSFUL, kinder);
    }

    public List<IncomeExpenseIndex> queryIncomeExpenseIndexList(String type) {
        return incomeExpenseDao.queryIncomeExpenseIndexList(type);
    }

    public InputStream getIndexExcelInputStream(IncomeExpenseVO ivo) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        putDataOnOutputStream(out, ivo);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void putDataOnOutputStream(OutputStream os, IncomeExpenseVO ivo) {
        WritableWorkbook workbook;
        try {
            List<Map<String, Object>> resultList = incomeExpenseDao.getList(ivo);
            workbook = Workbook.createWorkbook(os);
            WritableFont wf1 = new WritableFont(WritableFont.TIMES, 13, WritableFont.BOLD);
            WritableCellFormat wcf1 = new WritableCellFormat(wf1);
            wcf1.setAlignment(Alignment.CENTRE);
            wcf1.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf1.setWrap(true);
            WritableFont wf2 = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
            WritableCellFormat wcf2 = new WritableCellFormat(wf2);
            wcf1.setAlignment(Alignment.CENTRE);
            wcf1.setVerticalAlignment(VerticalAlignment.CENTRE);
            WritableCellFormat wcf3 = new WritableCellFormat();
            WritableSheet sheet = workbook.createSheet("其他收入与支出报表", 0);
            jxl.write.Label lb = null;
            jxl.write.Number nb = null;
            sheet.setColumnView(0, 20);
            sheet.setColumnView(1, 40);
            sheet.setColumnView(2, 30);
            sheet.setColumnView(3, 15);
            sheet.setColumnView(4, 15);
            Map map;
            sheet.mergeCells(0, 0, 4, 1);
            lb = new jxl.write.Label(0, 0, "其他收入与支出（每月汇总表）", wcf1);
            sheet.addCell(lb);
            lb = new jxl.write.Label(0, 2, "日期", wcf2);
            sheet.addCell(lb);
            lb = new jxl.write.Label(1, 2, "备注", wcf2);
            sheet.addCell(lb);
            lb = new jxl.write.Label(2, 2, "指标", wcf2);
            sheet.addCell(lb);
            lb = new jxl.write.Label(3, 2, "其他收入", wcf2);
            sheet.addCell(lb);
            lb = new jxl.write.Label(4, 2, "支出", wcf2);
            sheet.addCell(lb);
            for (int i = 0; i < resultList.size(); i++) {
                map = resultList.get(i);
                lb = new jxl.write.Label(0, i + 3, map.get("chargeTime") == null ? "" : map.get("chargeTime").toString(), wcf3);
                sheet.addCell(lb);
                lb = new jxl.write.Label(1, i + 3, map.get("remark") == null ? "" : map.get("remark").toString());
                sheet.addCell(lb);
                lb = new jxl.write.Label(2, i + 3, map.get("indexName") == null ? "" : map.get("indexName").toString());
                sheet.addCell(lb);
                if ((map.get("amount") != null) && (Double.parseDouble(map.get("amount").toString()) > 0)) {
                    nb = new jxl.write.Number(3, i + 3, Double.parseDouble(map.get("amount").toString()));
                    sheet.addCell(nb);
                }
                if ((map.get("amount2") != null) && (Double.parseDouble(map.get("amount2").toString()) > 0)) {
                    nb = new jxl.write.Number(4, i + 3, Double.parseDouble(map.get("amount2").toString()));
                    sheet.addCell(nb);
                }
            }
            lb = new jxl.write.Label(0, resultList.size() + 3, "合计：");
            sheet.addCell(lb);
            IncomeExpenseVO ievo = incomeExpenseDao.getTotalAmount(ivo);
            nb = new jxl.write.Number(3, resultList.size() + 3, ievo.getAmountStart());
            sheet.addCell(nb);
            nb = new jxl.write.Number(4, resultList.size() + 3, ievo.getAmountEnd());
            sheet.addCell(nb);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existLock(String year, String month, String kgId) {
        return incomeExpenseDao.existLock(year, month, kgId);
    }

    public IIncomeExpenseDao getIncomeExpenseDao() {
        return incomeExpenseDao;
    }

    public void setIncomeExpenseDao(IIncomeExpenseDao incomeExpenseDao) {
        this.incomeExpenseDao = incomeExpenseDao;
    }
}
