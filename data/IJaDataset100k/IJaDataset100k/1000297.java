package com.vlee.bean.inventory;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.Serializable;
import java.math.BigDecimal;
import com.vlee.util.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;

public class StockAgingForm extends java.lang.Object implements Serializable {

    Integer pcCenter = null;

    Timestamp endDate = null;

    Vector vecRow = null;

    TreeMap treeLevel1 = null;

    public String groupByCategory1 = "2";

    public String groupByCategory2 = "3";

    public StockAgingForm() {
        this.pcCenter = new Integer(0);
        this.endDate = TimeFormat.getTimestamp();
        this.vecRow = new Vector();
        this.treeLevel1 = new TreeMap();
    }

    public String getGroupByCategory1() {
        return this.groupByCategory1;
    }

    public void setGroupByCategory1(String buf) {
        this.groupByCategory1 = buf;
    }

    public String getGroupByCategory2() {
        return this.groupByCategory2;
    }

    public void setGroupByCategory2(String buf) {
        this.groupByCategory2 = buf;
    }

    public String getPCCenter(String buf) {
        return this.pcCenter.toString();
    }

    public void setPCCenter(Integer buf) {
        this.pcCenter = buf;
    }

    public void setEndDate(Timestamp buf) {
        buf = TimeFormat.set(buf, Calendar.DATE, 1);
        buf = TimeFormat.add(buf, 0, 1, 0);
        buf = TimeFormat.add(buf, 0, 0, -1);
        this.endDate = buf;
    }

    public String getEndDate(String buf) {
        return TimeFormat.strDisplayDate(this.endDate);
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void retrieveData() {
        Timestamp theEndDate = TimeFormat.set(this.endDate, Calendar.DATE, 1);
        theEndDate = TimeFormat.add(this.endDate, 0, 1, 0);
        theEndDate = TimeFormat.add(theEndDate, 0, 0, -1);
        this.vecRow = StockDeltaNut.getAgingReport(this.pcCenter, theEndDate);
        for (int cnt1 = 0; cnt1 < vecRow.size(); cnt1++) {
            Row oneRow = (Row) vecRow.get(cnt1);
            oneRow.calculateBinBalance();
        }
        multiLevelCategorization();
    }

    public void multiLevelCategorization() {
        this.treeLevel1.clear();
        for (int cnt1 = 0; cnt1 < this.vecRow.size(); cnt1++) {
            Row oneRow = (Row) vecRow.get(cnt1);
            String thisCat1 = oneRow.getCategoryCode(this.groupByCategory1);
            String thisCat2 = oneRow.getCategoryCode(this.groupByCategory2);
            PerLevel1 perLevel1 = (PerLevel1) this.treeLevel1.get(thisCat1);
            if (perLevel1 == null) {
                perLevel1 = new PerLevel1();
                this.treeLevel1.put(thisCat1, perLevel1);
                perLevel1.categoryCode = thisCat1;
                QueryObject query = new QueryObject(new String[] { CategoryTreeBean.CAT_LEVEL + " ='" + this.groupByCategory1 + "' ", CategoryTreeBean.CODE + " ='" + thisCat1 + "' " });
                query.setOrder(" ORDER BY " + CategoryTreeBean.SORT + ", " + CategoryTreeBean.CODE + ", " + CategoryTreeBean.NAME);
                Vector vecCategory = new Vector(CategoryTreeNut.getObjects(query));
                if (vecCategory.size() > 0) {
                    CategoryTreeObject catTreeObj = (CategoryTreeObject) vecCategory.get(0);
                    perLevel1.categoryName = catTreeObj.name;
                }
            }
            PerLevel1.PerLevel2 perLevel2 = (PerLevel1.PerLevel2) perLevel1.treeLevel2.get(thisCat2);
            if (perLevel2 == null) {
                perLevel2 = new PerLevel1.PerLevel2();
                perLevel1.treeLevel2.put(thisCat2, perLevel2);
                perLevel2.categoryCode = thisCat2;
                QueryObject query = new QueryObject(new String[] { CategoryTreeBean.CAT_LEVEL + " ='" + this.groupByCategory2 + "' ", CategoryTreeBean.CODE + " ='" + thisCat2 + "' " });
                query.setOrder(" ORDER BY " + CategoryTreeBean.SORT + ", " + CategoryTreeBean.CODE + ", " + CategoryTreeBean.NAME);
                Vector vecCategory = new Vector(CategoryTreeNut.getObjects(query));
                if (vecCategory.size() > 0) {
                    CategoryTreeObject catTreeObj = (CategoryTreeObject) vecCategory.get(0);
                    perLevel2.categoryName = catTreeObj.name;
                }
            }
            perLevel2.treeRow.put(oneRow.itemObj.code, oneRow);
        }
    }

    public TreeMap getResult() {
        return this.treeLevel1;
    }

    public static class Row {

        public ItemObject itemObj;

        public BigDecimal balQty;

        public BigDecimal totalQty;

        public BigDecimal balAmt;

        public TreeMap agingTree;

        public Row() {
            this.itemObj = new ItemObject();
            this.balQty = new BigDecimal(0);
            this.totalQty = new BigDecimal(0);
            this.balAmt = new BigDecimal(0);
            this.agingTree = new TreeMap();
        }

        public BigDecimal getUnitCost() {
            BigDecimal unitPrice = this.balAmt.divide(this.balQty, 12, BigDecimal.ROUND_HALF_EVEN);
            return unitPrice;
        }

        public void calculateBinBalance() {
            Vector vecAging = new Vector(this.agingTree.values());
            BigDecimal balance = this.balQty;
            for (int cnt1 = vecAging.size() - 1; cnt1 >= 0; cnt1--) {
                AgingBin agingBin = (AgingBin) vecAging.get(cnt1);
                if (balance.compareTo(agingBin.binQty) >= 0) {
                    agingBin.balQty = agingBin.binQty;
                    balance = balance.subtract(agingBin.binQty);
                } else {
                    agingBin.balQty = balance;
                    balance = new BigDecimal(0);
                }
                agingBin.calculateBalanceAmount();
            }
        }

        public String getBalQtyByMonth(Timestamp dateBalance, int mthStart, int mthEnd) {
            Timestamp tsStart = TimeFormat.set(dateBalance, Calendar.DATE, 1);
            tsStart = TimeFormat.add(tsStart, 0, mthStart, 0);
            tsStart = TimeFormat.set(tsStart, Calendar.DATE, 1);
            Timestamp tsEnd = TimeFormat.set(dateBalance, Calendar.DATE, 1);
            tsEnd = TimeFormat.set(tsEnd, Calendar.DATE, 1);
            tsEnd = TimeFormat.add(tsEnd, 0, mthEnd + 1, 0);
            tsEnd = TimeFormat.set(tsEnd, Calendar.DATE, 1);
            tsEnd = TimeFormat.add(tsEnd, 0, 0, -1);
            BigDecimal totalQty = new BigDecimal(0);
            Vector vecAge = new Vector(this.agingTree.values());
            for (int cnt1 = 0; cnt1 < vecAge.size(); cnt1++) {
                AgingBin ageBin = (AgingBin) vecAge.get(cnt1);
                if (ageBin.dateStart.compareTo(tsStart) >= 0 && ageBin.dateEnd.compareTo(tsEnd) < 0) {
                    totalQty = totalQty.add(ageBin.balQty);
                }
            }
            String strQty = "";
            if (totalQty.signum() != 0) {
                strQty = CurrencyFormat.strInt(totalQty);
            }
            return strQty;
        }

        public String getBalAmtByMonth(Timestamp dateBalance, int mthStart, int mthEnd) {
            Timestamp tsStart = TimeFormat.set(dateBalance, Calendar.DATE, 1);
            tsStart = TimeFormat.add(tsStart, 0, mthStart, 0);
            tsStart = TimeFormat.set(tsStart, Calendar.DATE, 1);
            Timestamp tsEnd = TimeFormat.set(dateBalance, Calendar.DATE, 1);
            tsEnd = TimeFormat.set(tsEnd, Calendar.DATE, 1);
            tsEnd = TimeFormat.add(tsEnd, 0, mthEnd + 1, 0);
            tsEnd = TimeFormat.set(tsEnd, Calendar.DATE, 1);
            tsEnd = TimeFormat.add(tsEnd, 0, 0, -1);
            BigDecimal totalAmt = new BigDecimal(0);
            Vector vecAge = new Vector(this.agingTree.values());
            for (int cnt1 = 0; cnt1 < vecAge.size(); cnt1++) {
                AgingBin ageBin = (AgingBin) vecAge.get(cnt1);
                if (ageBin.dateStart.compareTo(tsStart) >= 0 && ageBin.dateEnd.compareTo(tsEnd) < 0) {
                    totalAmt = totalAmt.add(ageBin.balAmt);
                }
            }
            String strCost = "";
            if (totalAmt.signum() != 0) {
                strCost = CurrencyFormat.strCcy(totalAmt);
            }
            return strCost;
        }

        public BigDecimal getRemainQty() {
            BigDecimal totalQty = new BigDecimal(0);
            Vector vecAge = new Vector(this.agingTree.values());
            for (int cnt1 = 0; cnt1 < vecAge.size(); cnt1++) {
                AgingBin ageBin = (AgingBin) vecAge.get(cnt1);
                totalQty = totalQty.add(ageBin.balQty);
            }
            return this.balQty.subtract(totalQty);
        }

        public String getBinQtyByMonth(Timestamp dateBalance, int mthStart, int mthEnd) {
            Timestamp tsStart = TimeFormat.set(dateBalance, Calendar.DATE, 1);
            tsStart = TimeFormat.add(tsStart, 0, mthStart, 0);
            tsStart = TimeFormat.set(tsStart, Calendar.DATE, 1);
            Timestamp tsEnd = TimeFormat.set(dateBalance, Calendar.DATE, 1);
            tsEnd = TimeFormat.set(tsEnd, Calendar.DATE, 1);
            tsEnd = TimeFormat.add(tsEnd, 0, mthEnd + 1, 0);
            tsEnd = TimeFormat.set(tsEnd, Calendar.DATE, 1);
            tsEnd = TimeFormat.add(tsEnd, 0, 0, -1);
            BigDecimal totalQty = new BigDecimal(0);
            Vector vecAge = new Vector(this.agingTree.values());
            for (int cnt1 = 0; cnt1 < vecAge.size(); cnt1++) {
                AgingBin ageBin = (AgingBin) vecAge.get(cnt1);
                if (ageBin.dateStart.compareTo(tsStart) >= 0 && ageBin.dateEnd.compareTo(tsEnd) < 0) {
                    totalQty = totalQty.add(ageBin.binQty);
                }
            }
            String strQty = "";
            if (totalQty.signum() != 0) {
                strQty = CurrencyFormat.strInt(totalQty);
            }
            return strQty;
        }

        public String getKey(String buf1, String buf2) {
            String theKey = "";
            if (buf1.equals("1")) {
                theKey += this.itemObj.category1;
            } else if (buf1.equals("2")) {
                theKey += this.itemObj.category2;
            } else if (buf1.equals("3")) {
                theKey += this.itemObj.category3;
            } else if (buf1.equals("4")) {
                theKey += this.itemObj.category4;
            } else if (buf1.equals("5")) {
                theKey += this.itemObj.category5;
            }
            theKey += "-";
            if (buf2.equals("1")) {
                theKey += this.itemObj.category1;
            } else if (buf2.equals("2")) {
                theKey += this.itemObj.category2;
            } else if (buf2.equals("3")) {
                theKey += this.itemObj.category3;
            } else if (buf2.equals("4")) {
                theKey += this.itemObj.category4;
            } else if (buf2.equals("5")) {
                theKey += this.itemObj.category5;
            }
            theKey += "-" + this.itemObj.code;
            return theKey;
        }

        public String getCategoryCode(String level) {
            String theKey = "";
            if (level.equals("1")) {
                theKey = this.itemObj.category1;
            } else if (level.equals("2")) {
                theKey = this.itemObj.category2;
            } else if (level.equals("3")) {
                theKey = this.itemObj.category3;
            } else if (level.equals("4")) {
                theKey = this.itemObj.category4;
            } else if (level.equals("5")) {
                theKey = this.itemObj.category5;
            }
            return theKey;
        }
    }

    public static class AgingBin {

        public String dateTag;

        public Timestamp dateStart;

        public Timestamp dateEnd;

        public BigDecimal binQty;

        public BigDecimal binAmt;

        public BigDecimal balQty;

        public BigDecimal balAmt;

        public AgingBin() {
            this.dateTag = "";
            this.dateStart = TimeFormat.createTimestamp("0001-01-01");
            this.dateEnd = this.dateStart;
            this.binQty = new BigDecimal(0);
            this.binAmt = new BigDecimal(0);
            this.balQty = new BigDecimal(0);
            this.balAmt = new BigDecimal(0);
        }

        public void calculateBalanceAmount() {
            if (this.binQty.signum() != 0) {
                this.balAmt = this.binAmt.multiply(this.balQty).divide(this.binQty, 12, BigDecimal.ROUND_HALF_EVEN);
            }
        }
    }

    public static class PerLevel1 {

        public String categoryCode;

        public String categoryName;

        public TreeMap treeLevel2;

        public PerLevel1() {
            this.categoryCode = "";
            this.categoryName = "";
            this.treeLevel2 = new TreeMap();
        }

        public static class PerLevel2 {

            public String categoryCode;

            public String categoryName;

            public TreeMap treeRow;

            public AmountQty amtQty1;

            public AmountQty amtQty2;

            public AmountQty amtQty3;

            public AmountQty amtQty4;

            public AmountQty amtQty5;

            public AmountQty amtQty6;

            public AmountQty amtQty7;

            public AmountQty amtQty8;

            public AmountQty amtQty9;

            public AmountQty amtQty10;

            public AmountQty amtQty11;

            public PerLevel2() {
                this.categoryCode = "";
                this.categoryName = "";
                this.treeRow = new TreeMap();
                this.amtQty1 = new AmountQty();
                this.amtQty2 = new AmountQty();
                this.amtQty3 = new AmountQty();
                this.amtQty4 = new AmountQty();
                this.amtQty5 = new AmountQty();
                this.amtQty6 = new AmountQty();
                this.amtQty7 = new AmountQty();
                this.amtQty8 = new AmountQty();
                this.amtQty9 = new AmountQty();
                this.amtQty10 = new AmountQty();
                this.amtQty11 = new AmountQty();
            }

            public void getTotalAmtQty(Row oneRow, Timestamp endDate) {
                String strTmp = "";
                BigDecimal decStr = new BigDecimal(0);
                strTmp = oneRow.getBalQtyByMonth(endDate, 0, 2);
                try {
                    decStr = new BigDecimal(strTmp);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty1.qty = amtQty1.qty.add(decStr);
                String strTmp1 = "";
                BigDecimal decStr1 = new BigDecimal(0);
                strTmp1 = oneRow.getBalAmtByMonth(endDate, 0, 2);
                ;
                try {
                    decStr1 = new BigDecimal(strTmp1);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty1.amount = amtQty1.amount.add(decStr1);
                String strTmp2 = "";
                BigDecimal decStr2 = new BigDecimal(0);
                strTmp2 = oneRow.getBalQtyByMonth(endDate, -1, 0);
                try {
                    decStr2 = new BigDecimal(strTmp2);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty2.qty = amtQty2.qty.add(decStr2);
                String strTmp3 = "";
                BigDecimal decStr3 = new BigDecimal(0);
                strTmp3 = oneRow.getBalAmtByMonth(endDate, -1, 0);
                try {
                    decStr3 = new BigDecimal(strTmp3);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty2.amount = amtQty2.amount.add(decStr3);
                String strTmp4 = "";
                BigDecimal decStr4 = new BigDecimal(0);
                strTmp4 = oneRow.getBalQtyByMonth(endDate, -2, -1);
                try {
                    decStr4 = new BigDecimal(strTmp4);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty3.qty = amtQty3.qty.add(decStr4);
                String strTmp5 = "";
                BigDecimal decStr5 = new BigDecimal(0);
                strTmp5 = oneRow.getBalAmtByMonth(endDate, -2, -1);
                try {
                    decStr5 = new BigDecimal(strTmp5);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty3.amount = amtQty3.amount.add(decStr5);
                String strTmp6 = "";
                BigDecimal decStr6 = new BigDecimal(0);
                strTmp6 = oneRow.getBalQtyByMonth(endDate, -3, -2);
                try {
                    decStr6 = new BigDecimal(strTmp6);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty4.qty = amtQty4.qty.add(decStr6);
                String strTmp7 = "";
                BigDecimal decStr7 = new BigDecimal(0);
                strTmp7 = oneRow.getBalAmtByMonth(endDate, -3, -2);
                try {
                    decStr7 = new BigDecimal(strTmp7);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty4.amount = amtQty4.amount.add(decStr7);
                String strTmp8 = "";
                BigDecimal decStr8 = new BigDecimal(0);
                strTmp8 = oneRow.getBalQtyByMonth(endDate, -4, -3);
                try {
                    decStr8 = new BigDecimal(strTmp8);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty5.qty = amtQty5.qty.add(decStr8);
                String strTmp9 = "";
                BigDecimal decStr9 = new BigDecimal(0);
                strTmp9 = oneRow.getBalAmtByMonth(endDate, -4, -3);
                try {
                    decStr9 = new BigDecimal(strTmp9);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty5.amount = amtQty5.amount.add(decStr9);
                String strTmp10 = "";
                BigDecimal decStr10 = new BigDecimal(0);
                strTmp10 = oneRow.getBalQtyByMonth(endDate, -5, -4);
                try {
                    decStr10 = new BigDecimal(strTmp10);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty6.qty = amtQty6.qty.add(decStr10);
                String strTmp11 = "";
                BigDecimal decStr11 = new BigDecimal(0);
                strTmp11 = oneRow.getBalAmtByMonth(endDate, -5, -4);
                try {
                    decStr11 = new BigDecimal(strTmp11);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty6.amount = amtQty6.amount.add(decStr11);
                String strTmp12 = "";
                BigDecimal decStr12 = new BigDecimal(0);
                strTmp12 = oneRow.getBalQtyByMonth(endDate, -6, -5);
                try {
                    decStr12 = new BigDecimal(strTmp12);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty7.qty = amtQty7.qty.add(decStr12);
                String strTmp13 = "";
                BigDecimal decStr13 = new BigDecimal(0);
                strTmp13 = oneRow.getBalAmtByMonth(endDate, -6, -5);
                try {
                    decStr13 = new BigDecimal(strTmp13);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty7.amount = amtQty7.amount.add(decStr13);
                String strTmp14 = "";
                BigDecimal decStr14 = new BigDecimal(0);
                strTmp14 = oneRow.getBalQtyByMonth(endDate, -12, -6);
                try {
                    decStr14 = new BigDecimal(strTmp14);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty8.qty = amtQty8.qty.add(decStr14);
                String strTmp15 = "";
                BigDecimal decStr15 = new BigDecimal(0);
                strTmp15 = oneRow.getBalQtyByMonth(endDate, -12, -6);
                try {
                    decStr15 = new BigDecimal(strTmp15);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty8.qty = amtQty8.qty.add(decStr15);
                String strTmp16 = "";
                BigDecimal decStr16 = new BigDecimal(0);
                strTmp16 = oneRow.getBalAmtByMonth(endDate, -12, -6);
                try {
                    decStr16 = new BigDecimal(strTmp16);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty8.amount = amtQty8.amount.add(decStr16);
                String strTmp17 = "";
                BigDecimal decStr17 = new BigDecimal(0);
                strTmp17 = oneRow.getBalQtyByMonth(endDate, -24, -12);
                try {
                    decStr17 = new BigDecimal(strTmp17);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty9.qty = amtQty9.qty.add(decStr17);
                String strTmp18 = "";
                BigDecimal decStr18 = new BigDecimal(0);
                strTmp18 = oneRow.getBalAmtByMonth(endDate, -24, -12);
                try {
                    decStr18 = new BigDecimal(strTmp18);
                } catch (Exception ex) {
                    System.out.println("Exception" + ex.toString());
                }
                amtQty9.amount = amtQty9.amount.add(decStr18);
                BigDecimal decTmp2 = oneRow.getRemainQty();
                amtQty10.remainQty = amtQty10.remainQty.add(decTmp2);
                BigDecimal decTmp3 = oneRow.getRemainQty().multiply(oneRow.getUnitCost());
                amtQty10.remainAmt = amtQty10.remainAmt.add(decTmp3);
                BigDecimal decTmp = oneRow.balQty;
                amtQty11.remainQty = amtQty11.remainQty.add(decTmp);
                BigDecimal decTmp1 = oneRow.balQty.multiply(oneRow.getUnitCost());
                amtQty11.remainAmt = amtQty11.remainAmt.add(decTmp1);
            }
        }
    }

    public static class AmountQty {

        public BigDecimal amount;

        public BigDecimal qty;

        public BigDecimal remainAmt;

        public BigDecimal remainQty;

        public AmountQty() {
            this.amount = new BigDecimal(0);
            this.qty = new BigDecimal(0);
            this.remainAmt = new BigDecimal(0);
            this.remainQty = new BigDecimal(0);
        }
    }
}
