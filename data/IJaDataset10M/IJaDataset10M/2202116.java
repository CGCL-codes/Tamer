package cdu.computer.hxl.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cdu.computer.hxl.db.DBCRUDHandler;

/**
 * 
 * @author Administrator
 * 
 */
public class CostService {

    private DBCRUDHandler dbHandler = null;

    public void addCostCategory(Map<String, Object> map) {
        dbHandler.add(map, "cost_category");
    }

    public void addCost(Map<String, Object> map) {
        dbHandler.add(map, "cost");
    }

    public void deleteCost(int rowid) {
        Map<String, Object> whereMap = new HashMap<String, Object>();
        whereMap.put("rowid", rowid);
        dbHandler.delete(whereMap, "cost");
    }

    public void deleteCostcategory(int rowid) {
        Map<String, Object> whereMap = new HashMap<String, Object>();
        try {
            whereMap.put("useid", rowid);
            dbHandler.delete(whereMap, "cost");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        whereMap.clear();
        whereMap.put("rowid", rowid);
        dbHandler.delete(whereMap, "cost_category");
    }

    public List<Map<String, Object>> loadCostCategoryForList(Map<String, Object> whereDataMap) {
        List<Map<String, Object>> result = dbHandler.search(new String[] { "rowid", "categoryname", "remark", "datetime" }, whereDataMap, "cost_category");
        return result;
    }

    public List<Map<String, Object>> loadCostForList(Map<String, Object> whereDataMap) {
        List<Map<String, Object>> result = dbHandler.search(new String[] { "c.rowid", "amount", "cc.categoryname", "b.bankname", "c.remark", "c.date" }, whereDataMap, "cost as c left join cost_category as cc on c.useid =cc.rowid left join bank as b on c.bankid=b.rowid");
        return result;
    }

    public Object[][] loadCostForObject(Map<String, Object> whereDataMap) {
        List<Map<String, Object>> result = loadCostForList(whereDataMap);
        int size = result.size();
        Object[][] o = new Object[size][6];
        for (int i = 0; i < size; i++) {
            Map<String, Object> mm = result.get(i);
            o[i][0] = mm.get("rowid");
            o[i][1] = mm.get("amount");
            o[i][2] = mm.get("categoryname");
            o[i][3] = mm.get("bankname");
            o[i][4] = mm.get("remark");
            o[i][5] = mm.get("date");
        }
        return o;
    }

    public Map<String, Object> statistiCostForallocation() {
        Map<String, Object> statistData = new HashMap<String, Object>();
        List<Map<String, Object>> incomeCategory = dbHandler.search(new String[] { "rowid", "categoryname" }, null, "cost_category");
        int size = incomeCategory.size();
        Map<String, Object> whereDataMap = new HashMap<String, Object>();
        List<Map<String, Object>> sumList = dbHandler.search(new String[] { "count(*) as sum" }, null, "cost");
        int count = 1;
        if (sumList != null && sumList.size() == 1) count = (Integer) sumList.get(0).get("sum");
        for (int i = 0; i < size; i++) {
            Object id = incomeCategory.get(i).get("rowid");
            String catename = (String) incomeCategory.get(i).get("categoryname");
            whereDataMap.clear();
            whereDataMap.put("useid", id);
            sumList = dbHandler.search(new String[] { "count(rowid) as sum" }, whereDataMap, "cost");
            if (sumList != null && sumList.size() == 1) statistData.put(catename, ((Integer) sumList.get(0).get("sum")).intValue() * 1.0 / count * 100);
        }
        return statistData;
    }

    public Map<String, Double> statistiCostForMoneyallocation() {
        Map<String, Double> statistData = new HashMap<String, Double>();
        List<Map<String, Object>> sumMoneyList = dbHandler.search(new String[] { "sum(amount) as summoney" }, null, "cost");
        double sum = 1;
        if (sumMoneyList != null && sumMoneyList.size() == 1) sum = (Double) sumMoneyList.get(0).get("summoney");
        List<Map<String, Object>> cateList = dbHandler.search(new String[] { "rowid", "categoryname" }, null, "cost_category");
        Map<String, Object> whereDataMap = new HashMap<String, Object>();
        int size = cateList.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> m = cateList.get(i);
            whereDataMap.clear();
            whereDataMap.put("useid", m.get("rowid"));
            List<Map<String, Object>> result = dbHandler.search(new String[] { "sum(amount) as money" }, whereDataMap, "cost");
            double count = 0;
            if (result != null && result.size() == 1) {
                Object o = result.get(0).get("money");
                if (o != null) count = (Double) o;
            }
            statistData.put((String) m.get("categoryname"), count / sum * 1.0);
        }
        return statistData;
    }

    public Map<Integer, Double> statistiCostForBalance() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        double sum = 0.0;
        Map<String, Object> whereDataMap = new HashMap<String, Object>();
        whereDataMap.put("date like ", "%" + year + "%");
        List<Map<String, Object>> result = dbHandler.search(new String[] { "amount", "date" }, whereDataMap, "cost");
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        for (int i = 0; i < 12; i++) {
            map.put(i, 0.0);
        }
        int size = result.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> m = result.get(i);
            double amount = (Double) m.get("amount");
            sum += amount;
            String date = String.valueOf(m.get("date"));
            Integer month = Integer.parseInt(date.split("-")[1]);
            map.put(month - 1, map.get(month - 1) + amount);
        }
        map.put(1000, sum);
        return map;
    }

    /**
	 * @return the dbHandler
	 */
    public DBCRUDHandler getDbHandler() {
        return dbHandler;
    }

    /**
	 * @param dbHandler
	 *            the dbHandler to set
	 */
    public void setDbHandler(DBCRUDHandler dbHandler) {
        this.dbHandler = dbHandler;
    }
}
