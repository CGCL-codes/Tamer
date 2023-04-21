package ejb.bprocess.util;

/**
 *
 * @author  Administrator
 */
public class BudgetsHandler {

    ejb.bprocess.util.Utility utility = null;

    /** Creates a new instance of BudgetsHandler */
    public BudgetsHandler() {
        utility = ejb.bprocess.util.Utility.getInstance();
    }

    public java.lang.String rollbackingReorderedCopies(java.util.Vector vecBudTrcopy, String copyid, javax.ejb.SessionContext context, String entryId, String libid) {
        String status = "Y";
        try {
            for (int i = 0; i < vecBudTrcopy.size(); i += 3) {
                ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey libKey = new ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey();
                libKey.library_Id = new Integer(vecBudTrcopy.get(i + 1).toString());
                libKey.budget_Id = vecBudTrcopy.get(i).toString();
                ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET locaBudget = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByPrimaryKey(libKey);
                java.math.BigDecimal orderPrice = new java.math.BigDecimal(vecBudTrcopy.get(i + 2).toString());
                if (locaBudget.getBalance_Amt().doubleValue() >= orderPrice.doubleValue()) {
                    double bal = locaBudget.getBalance_Amt().doubleValue();
                    double comiit = locaBudget.getCommitted_Amt().doubleValue();
                    bal = bal - orderPrice.doubleValue();
                    comiit = comiit + orderPrice.doubleValue();
                    locaBudget.setBalance_Amt(new java.math.BigDecimal(bal));
                    locaBudget.setCommitted_Amt(new java.math.BigDecimal(comiit));
                    int budgettaid = ejb.bprocess.util.Utility.getInstance().getBudgetTransactionID(locaBudget.getLibrary_Id().intValue());
                    ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).create(locaBudget.getLibrary_Id(), new Integer(budgettaid), ejb.bprocess.util.Utility.getInstance().getTimestamp(), orderPrice, "D", locaBudget.getBudget_Id(), "C", ejb.bprocess.util.Utility.getInstance().getTimestamp(), "", entryId, ejb.bprocess.util.Utility.getInstance().getTimestamp());
                    ((ejb.objectmodel.acquisitions.LocalACQ_REQUEST_AM_COPY_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACQ_REQUEST_AM_COPY_BUDGET_TRANSACTION")).create(new Integer(copyid), new Integer(libid), new Integer(budgettaid), locaBudget.getLibrary_Id());
                } else {
                    status = "N";
                    context.setRollbackOnly();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.setRollbackOnly();
            status = "N";
        }
        return status;
    }

    public java.lang.String rollbackAcquisitionBudgets(java.util.Vector vecCopyId, String libId, javax.ejb.SessionContext context) {
        String response = "N";
        try {
            java.util.Vector vec = new java.util.Vector(1, 1);
            for (int i = 0; i < vecCopyId.size(); i++) {
                Integer copyid = new Integer(vecCopyId.get(i).toString().trim());
                Object obj[] = ((ejb.objectmodel.acquisitions.LocalACQ_REQUEST_AM_COPY_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACQ_REQUEST_AM_COPY_BUDGET_TRANSACTION")).findByLibraryIdAndCopyId(new Integer(libId), copyid).toArray();
                for (int j = 0; j < obj.length; j++) {
                    ejb.objectmodel.acquisitions.LocalACQ_REQUEST_AM_COPY_BUDGET_TRANSACTION localAMBudget = (ejb.objectmodel.acquisitions.LocalACQ_REQUEST_AM_COPY_BUDGET_TRANSACTION) obj[j];
                    ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey budgetKey = new ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey();
                    budgetKey.budget_Ta_Id = localAMBudget.getBudget_ta_id();
                    budgetKey.library_Id = localAMBudget.getBudget_ta_library_id();
                    ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION budgetTr = ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).findByPrimaryKey(budgetKey);
                    double taAmt = budgetTr.getTa_Amt().doubleValue();
                    ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey libBudgetKey = new ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey();
                    libBudgetKey.library_Id = budgetTr.getLibrary_Id();
                    libBudgetKey.budget_Id = budgetTr.getBudget_Id();
                    ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET localLibBudget = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByPrimaryKey(libBudgetKey);
                    double commitAmt = localLibBudget.getCommitted_Amt().doubleValue();
                    double balanec = localLibBudget.getBalance_Amt().doubleValue();
                    double commit = 0.0d;
                    double remaincommit = 0.0d;
                    remaincommit = commitAmt - taAmt;
                    if (remaincommit < 0) {
                        balanec = balanec + commitAmt;
                        commit = 0.0d;
                    } else {
                        commit = remaincommit;
                        balanec = balanec + taAmt;
                    }
                    localLibBudget.setBalance_Amt(new java.math.BigDecimal(balanec));
                    localLibBudget.setCommitted_Amt(new java.math.BigDecimal(commit));
                    localAMBudget.remove();
                    budgetTr.remove();
                }
            }
            response = "Y";
        } catch (Exception e) {
            e.printStackTrace();
            context.setRollbackOnly();
            response = "N";
        }
        return response;
    }

    public java.util.Vector getBudgetUpdation(java.util.Vector vec, String commitOrExp, String transactionMode, String invoice, String paySlipNum, String entryId) {
        java.util.Vector vecBudgeTaId = new java.util.Vector(1, 1);
        try {
            for (int k = 0; k < vec.size(); k++) {
                java.util.Vector vecBudget = (java.util.Vector) vec.elementAt(k);
                String budgetId = vecBudget.get(1).toString().trim();
                Integer libIdInt = new Integer(vecBudget.get(0).toString().trim());
                java.math.BigDecimal amountBigDecimal = new java.math.BigDecimal(vecBudget.elementAt(2).toString().trim());
                java.math.BigDecimal transactionAmount = new java.math.BigDecimal(amountBigDecimal.toString());
                ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey aKey = new ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey();
                aKey.library_Id = libIdInt;
                aKey.budget_Id = budgetId.trim();
                ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET local = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByPrimaryKey(aKey);
                int kp = local.getBalance_Amt().compareTo(amountBigDecimal);
                if (kp >= 0) {
                    if (commitOrExp.trim().equals("COMMIT")) {
                        local.setBalance_Amt(local.getBalance_Amt().add(amountBigDecimal.negate()));
                        System.out.println("balance is  " + local.getBalance_Amt().add(amountBigDecimal.negate()));
                        amountBigDecimal = amountBigDecimal.add(local.getCommitted_Amt());
                        local.setCommitted_Amt(amountBigDecimal);
                        java.sql.Timestamp budgetTaDate = utility.getTimestampWithoutTime();
                        Integer budgetTaId = new Integer(utility.getBudgetTransactionID(libIdInt.intValue()));
                        ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).create(libIdInt, budgetTaId, budgetTaDate, transactionAmount, "D", budgetId, "C", budgetTaDate, paySlipNum, entryId, budgetTaDate);
                        vecBudgeTaId.addElement(budgetTaId.toString().trim());
                    } else {
                    }
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vecBudgeTaId;
    }

    public boolean rollbackBindingBudgets(String libraryId, String orderno) {
        boolean valid = true;
        try {
            java.util.Vector vec = new java.util.Vector(1, 1);
            Object obj[] = new Object[0];
            boolean find = true;
            try {
                obj = ((ejb.objectmodel.sm.LocalSM_BINDER_ORDER_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_BINDER_ORDER_BUDGET")).findByOrderNoLibId(new Integer(orderno), new Integer(libraryId)).toArray();
                find = true;
            } catch (Exception ee) {
                ee.printStackTrace();
                find = false;
            }
            if (find) {
                for (int i = 0; i < obj.length; i++) {
                    ejb.objectmodel.sm.LocalSM_BINDER_ORDER_BUDGET budgets = (ejb.objectmodel.sm.LocalSM_BINDER_ORDER_BUDGET) obj[i];
                    vec.addElement(budgets.getBudget_Ta_Id().toString());
                }
                if (vec.size() > 0) {
                    try {
                        for (int i = 0; i < obj.length; i++) {
                            ejb.objectmodel.sm.LocalSM_BINDER_ORDER_BUDGET budgets = (ejb.objectmodel.sm.LocalSM_BINDER_ORDER_BUDGET) obj[i];
                            budgets.remove();
                            valid = true;
                        }
                    } catch (Exception bexp) {
                        valid = false;
                    }
                    if (valid) {
                        valid = this.rollBackingBudgets(vec, libraryId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    public boolean rollBackingBudgets(java.util.Vector vec, String libraryId) {
        boolean valid = false;
        try {
            Integer libId = new Integer(libraryId);
            for (int i = 0; i < vec.size(); i++) {
                Integer budtaid = new Integer(vec.get(i).toString().trim());
                ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey budKey = new ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey();
                budKey.library_Id = libId;
                budKey.budget_Ta_Id = budtaid;
                ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION localBud = ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).findByPrimaryKey(budKey);
                String budId = localBud.getBudget_Id();
                double tamt = localBud.getTa_Amt().doubleValue();
                ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey libBudgetKey = new ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey();
                libBudgetKey.library_Id = libId;
                libBudgetKey.budget_Id = budId;
                ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET libBudget = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByPrimaryKey(libBudgetKey);
                double camt = libBudget.getCommitted_Amt().doubleValue();
                camt = camt - tamt;
                if (camt > 0) {
                    double bamt = libBudget.getBalance_Amt().doubleValue();
                    bamt = +tamt;
                    libBudget.setBalance_Amt(new java.math.BigDecimal(bamt));
                    libBudget.setCommitted_Amt(new java.math.BigDecimal(camt));
                }
                localBud.remove();
                valid = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    public String updateExpenditureBindingBudget(java.util.Vector vec) {
        String xmlRes = "error";
        try {
            for (int i = 0; i < vec.size(); i += 2) {
                ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey budgetKey = new ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey();
                budgetKey.library_Id = new Integer(vec.get(i).toString().trim());
                budgetKey.budget_Ta_Id = new Integer(vec.get(i + 1).toString().trim());
                ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION localBudget = ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).findByPrimaryKey(budgetKey);
                localBudget.getBudget_Id();
                java.math.BigDecimal amount = localBudget.getTa_Amt();
                double amtDouble = amount.doubleValue();
                ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey libBudgetKey = new ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey();
                libBudgetKey.library_Id = localBudget.getLibrary_Id();
                libBudgetKey.budget_Id = localBudget.getBudget_Id();
                ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET libBudget = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByPrimaryKey(libBudgetKey);
                double commit = libBudget.getCommitted_Amt().doubleValue();
                double balance = libBudget.getBalance_Amt().doubleValue();
                double commit1 = 0.0;
                if (amtDouble <= (commit + balance)) {
                    double remaining = commit - amtDouble;
                    double expend = libBudget.getExpenditure_Amt().doubleValue();
                    if (remaining >= 0) {
                        commit1 = remaining;
                    } else {
                        balance = balance + remaining;
                        commit1 = 0.0;
                    }
                    libBudget.setBalance_Amt(new java.math.BigDecimal(balance));
                    libBudget.setCommitted_Amt(new java.math.BigDecimal(commit1));
                    expend = expend + amtDouble;
                    libBudget.setExpenditure_Amt(new java.math.BigDecimal(expend));
                    localBudget.setCommitted_Or_Expenditure("E");
                    localBudget.setTa_Date(ejb.bprocess.util.Utility.getInstance().getTimestamp());
                    xmlRes = "success";
                } else {
                    xmlRes = "error";
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlRes;
    }

    public String updateCommitBinderBudgets(java.util.Vector vec, String commitOrExp, String transactionMode, String orderno, String paySlipNum, String entryId, String libraryId, String subscriptionId) {
        String xml = "SUCCESS";
        try {
            java.util.Vector vecBudgetTaId = getBudgetUpdation(vec, commitOrExp, transactionMode, "", paySlipNum, entryId);
            if (vecBudgetTaId != null && vecBudgetTaId.size() > 0) {
                for (int k = 0; k < vec.size(); k++) {
                    Integer budgetTaId = new Integer(vecBudgetTaId.get(k).toString().trim());
                    java.util.Vector vecBudget = (java.util.Vector) vec.elementAt(k);
                    String budgetId = vecBudget.get(1).toString().trim();
                    Integer libIdInt = new Integer(vecBudget.get(0).toString().trim());
                    java.math.BigDecimal amountBigDecimal = new java.math.BigDecimal(vecBudget.elementAt(2).toString().trim());
                    ((ejb.objectmodel.sm.LocalSM_BINDER_ORDER_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_BINDER_ORDER_BUDGET")).createBindOrderBudget(budgetTaId, new Integer(libraryId), new Integer(orderno));
                }
            } else {
                xml = "ERROR";
            }
        } catch (Exception e) {
            e.printStackTrace();
            xml = "ERROR";
        }
        return xml;
    }

    public String updateCommitSerialBudgets(java.util.Vector vec, String commitOrExp, String transactionMode, String invoice, String paySlipNum, String entryId, String libraryId, String subscriptionId) {
        String xml = "";
        try {
            java.util.Vector vecBudgetTaId = getBudgetUpdation(vec, commitOrExp, transactionMode, invoice, paySlipNum, entryId);
            if (vecBudgetTaId != null) {
                for (int k = 0; k < vec.size(); k++) {
                    Integer budgetTaId = new Integer(vecBudgetTaId.get(k).toString().trim());
                    java.util.Vector vecBudget = (java.util.Vector) vec.elementAt(k);
                    String budgetId = vecBudget.get(1).toString().trim();
                    Integer libIdInt = new Integer(vecBudget.get(0).toString().trim());
                    java.math.BigDecimal amountBigDecimal = new java.math.BigDecimal(vecBudget.elementAt(2).toString().trim());
                    ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET localSubBudget = null;
                    Object[] objbudget = ((ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_SUBSCRIPTION_BUDGET")).findBudgetExistanceInLibrary(new Integer(subscriptionId), libIdInt, budgetId, new Integer(libraryId)).toArray();
                    Integer allocationId = new Integer(0);
                    Integer allocationTaId = new Integer(utility.getAllocationTaId(libIdInt.intValue()));
                    for (int d = 0; d < objbudget.length; d++) {
                        localSubBudget = (ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET) objbudget[d];
                        localSubBudget.setAmount(amountBigDecimal);
                        allocationId = localSubBudget.getAllocation_Id();
                    }
                    ((ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET_TRHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_SUBSCRIPTION_BUDGET_TR")).createUsingAllFields(allocationId, allocationTaId, budgetTaId, libIdInt, null, null, null, null, libIdInt);
                }
                xml = "successfull";
            } else {
                xml = "insufficientbalance";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public String getUpadateBudgetsExpenditure(String xmlstr) {
        String xml = "";
        try {
            org.jdom.input.SAXBuilder sax1 = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = sax1.build(xmlstr);
            org.jdom.Element root = doc.getRootElement();
            String subLibId = null;
            String entryId = null;
            String libId = null;
            String subId = null;
            if (root.getChildText("SubscriptionId") != null) {
                subId = root.getChildText("SubscriptionId");
            }
            if (root.getChildText("LibraryId") != null) {
                libId = root.getChildText("LibraryId");
            }
            if (root.getChildText("SubscriptionLibraryId") != null) {
                subLibId = root.getChildText("SubscriptionLibraryId");
            }
            java.util.Hashtable hashBudgetTaID = new java.util.Hashtable();
            hashBudgetTaID = getUpdateSerialBudgetExpenditure(subLibId, subId, libId);
            org.jdom.Element budgetAcc = root.getChild("BudgetAccounts");
            java.util.List listBudgets = budgetAcc.getChildren("Budget");
            for (int i = 0; i < listBudgets.size(); i++) {
                org.jdom.Element budget = (org.jdom.Element) listBudgets.get(i);
                String budgetId = null;
                java.math.BigDecimal invoiceAmt = null;
                java.math.BigDecimal orderAmt = null;
                if (budget.getChild("BudgetId") != null) {
                    budgetId = budget.getChildText("BudgetId");
                }
                if (budget.getChild("InvoiceAmount") != null) {
                    invoiceAmt = new java.math.BigDecimal(budget.getChildText("InvoiceAmount"));
                }
                if (budget.getChild("OrderedAmount") != null) {
                    orderAmt = new java.math.BigDecimal(budget.getChildText("OrderedAmount"));
                }
                ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET localAccBud = null;
                localAccBud = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByBudgetLibraryId(new Integer(libId), budgetId);
                java.math.BigDecimal allocatedAmt = localAccBud.getBudget_Allocated_Amt();
                java.math.BigDecimal expendAmt = localAccBud.getExpenditure_Amt();
                java.math.BigDecimal balance = localAccBud.getBalance_Amt();
                System.out.println("balance is " + balance);
                balance = balance.subtract(invoiceAmt.subtract(orderAmt));
                if (balance.signum() >= 0) {
                    java.util.Vector vecTaId = (java.util.Vector) hashBudgetTaID.get(budgetId);
                    System.out.println("vector for traancactionId " + vecTaId.size());
                    for (int j = 0; j < vecTaId.size(); j = j + 2) {
                        Integer budgetTaId = new Integer(vecTaId.elementAt(j).toString());
                        ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey budTrans = new ejb.objectmodel.administration.ACC_BUDGET_TRANSACTIONKey();
                        budTrans.budget_Ta_Id = budgetTaId;
                        budTrans.library_Id = new Integer(libId);
                        ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION localAccTrans = null;
                        localAccTrans = ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).findByPrimaryKey(budTrans);
                        updateAccLibraryBudgetTransaction(localAccTrans, orderAmt, invoiceAmt);
                    }
                    expendAmt = expendAmt.add(invoiceAmt);
                    localAccBud.setExpenditure_Amt(expendAmt);
                    java.math.BigDecimal commitAmt = localAccBud.getCommitted_Amt().subtract(orderAmt);
                    localAccBud.setCommitted_Amt(commitAmt);
                    localAccBud.setBalance_Amt(balance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public java.util.Hashtable getUpdateSerialBudgetExpenditure(String subLibId, String subId, String libId) {
        java.util.Hashtable hashRet = new java.util.Hashtable();
        try {
            Object[] obj = new Object[0];
            Object[] obj4 = new Object[0];
            obj = ((ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_SUBSCRIPTION_BUDGET")).findbysublibidsubidlibid(new Integer(subLibId), new Integer(subId), new Integer(libId)).toArray();
            System.out.println("size of entity sm budget is" + obj.length);
            for (int i = 0; i < obj.length; i++) {
                ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET local = (ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET) obj[i];
                String budgetId = local.getBudget_Id();
                System.out.println("budgetid=" + budgetId);
                if (!(budgetId == null || budgetId.equals(""))) {
                    Integer budgetLibraryId = local.getBudget_Library_Id();
                    java.lang.Integer lid = local.getLibrary_Id();
                    java.lang.Integer alid = local.getAllocation_Id();
                    obj4 = ((ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET_TRHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_SUBSCRIPTION_BUDGET_TR")).findByAllocIdLibId(alid, lid).toArray();
                    java.lang.Integer budTaId = new java.lang.Integer(0);
                    java.lang.Integer budTaLibId = new java.lang.Integer(0);
                    System.out.println("obj4.length=" + obj4.length);
                    java.util.Vector vecBudgetTaIds = new java.util.Vector();
                    for (int j = 0; j < obj4.length; j++) {
                        ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET_TR local1 = (ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_BUDGET_TR) obj4[j];
                        budTaId = local1.getBudget_Ta_Id();
                        budTaLibId = local1.getBudget_Ta_Library_Id();
                        vecBudgetTaIds.addElement(budTaId);
                        vecBudgetTaIds.addElement(budTaLibId);
                    }
                    hashRet.put(budgetId, vecBudgetTaIds);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashRet;
    }

    public void updateAccLibraryBudgetTransaction(ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION localAccTrans, java.math.BigDecimal orderAmt, java.math.BigDecimal invoiceAmt) {
        int value = orderAmt.compareTo(invoiceAmt);
        localAccTrans.setCommitted_Or_Expenditure("E");
        localAccTrans.setTa_Type("D");
        localAccTrans.setTa_Amt(invoiceAmt);
        if (value == 0) {
        } else if (value > 0) {
        } else if (value < 0) {
            int taId = ejb.bprocess.util.Utility.getInstance().getBudgetTransactionID(localAccTrans.getLibrary_Id().intValue());
            java.sql.Timestamp todayDt = ejb.bprocess.util.Utility.getInstance().getTimestampWithoutTime();
            java.math.BigDecimal taAmt = invoiceAmt.subtract(orderAmt);
            try {
                ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).create(localAccTrans.getLibrary_Id(), new Integer(taId), todayDt, taAmt, "D", localAccTrans.getBudget_Id(), "E", todayDt, null, "1", todayDt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String updateSerialExpenditureDetails(java.lang.String xmlStr) {
        ejb.bprocess.util.NewGenXMLGenerator newGenXMLGenerator = ejb.bprocess.util.NewGenXMLGenerator.getInstance();
        org.jdom.Element printcopies = null;
        org.jdom.Element root = newGenXMLGenerator.getRootElementFromXMLDocument(xmlStr);
        org.jdom.Element root1 = new org.jdom.Element("Response");
        org.jdom.Element success = new org.jdom.Element("Success");
        Integer libraryId = new Integer(root.getChildText("LibraryID").toString());
        String entryId = root.getChildText("EntryID");
        String vendor = root.getChildText("Vendor");
        String invoiceNo = root.getChildText("InvoiceNo");
        java.sql.Timestamp invoiceDate = utility.getTimestamp(root.getChildText("invoiceDate"));
        String invoiceda = utility.getFormattedDate(invoiceDate);
        String orderNo = root.getChildText("OrderNo");
        java.sql.Timestamp sysDate = utility.getInstance().getTimestamp();
        String sys = utility.getFormattedDate(sysDate);
        int totalsubcount = new Integer(root.getChildText("Count").toString()).intValue();
        Object objectinvoiceDetails[] = root.getChildren("InvoiceNumberDetails").toArray();
        int receivesubcount = objectinvoiceDetails.length;
        try {
            for (int i = 0; i < objectinvoiceDetails.length; i++) {
                org.jdom.Element eleinvoiceDetails = (org.jdom.Element) objectinvoiceDetails[i];
                Integer subScriptionId = new Integer(eleinvoiceDetails.getChildText("SubscriptionId").toString());
                java.math.BigDecimal orderAmt = new java.math.BigDecimal(eleinvoiceDetails.getChildText("OrderAmount").toString());
                java.math.BigDecimal invoiceAmt = new java.math.BigDecimal(eleinvoiceDetails.getChildText("InvoiceAmount").toString());
                String printStatus = "A";
                String emailStatus = "A";
                java.sql.Timestamp paySlipDate = sysDate;
                String payslipNo = "";
                String paySlipContent = "";
                ((ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_INVOICEHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_SUBSCRIPTION_INVOICE")).createInvoice(libraryId, subScriptionId, invoiceNo.toString(), payslipNo, invoiceDate, paySlipDate, new java.math.BigDecimal(invoiceAmt + ""), "A", paySlipContent, "", "", entryId, sysDate);
                ejb.objectmodel.sm.SM_SUBSCRIPTIONKey pkey = new ejb.objectmodel.sm.SM_SUBSCRIPTIONKey();
                pkey.library_Id = libraryId;
                pkey.subscription_Id = subScriptionId;
                Object objSub = ((ejb.objectmodel.sm.LocalSM_SUBSCRIPTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_SUBSCRIPTION")).findByPrimaryKey(pkey);
                ejb.objectmodel.sm.LocalSM_SUBSCRIPTION localsubscription = (ejb.objectmodel.sm.LocalSM_SUBSCRIPTION) objSub;
                localsubscription.setAmount(invoiceAmt);
                localsubscription.setStatus("B");
                localsubscription.setEntry_Id(entryId);
                Object[] objLibsSub = ((ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_FAMILYHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("SM_SUBSCRIPTION_FAMILY")).findBySubLibIdSubId(libraryId, subScriptionId).toArray();
                for (int j = 0; j < objLibsSub.length; j++) {
                    ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_FAMILY libSub = (ejb.objectmodel.sm.LocalSM_SUBSCRIPTION_FAMILY) objLibsSub[j];
                    Integer libId = libSub.getLibrary_Id();
                    Integer noofCopies = libSub.getNo_Of_Copies();
                    java.util.Hashtable hashBudTaId = new java.util.Hashtable();
                    hashBudTaId = getUpdateSerialBudgetExpenditure(libraryId.toString(), subScriptionId.toString(), libId.toString());
                    java.util.Enumeration enumBudId = hashBudTaId.keys();
                    while (enumBudId.hasMoreElements()) {
                        String budgetId = enumBudId.nextElement().toString();
                        java.util.Vector vecTaId = (java.util.Vector) hashBudTaId.get(budgetId);
                        String res = updateAccLibraryBudgetTransaction2(vecTaId, orderAmt, invoiceAmt, budgetId, libId.toString());
                        System.out.println("respobnse after updating budgets " + res);
                    }
                    success.setText("Y");
                }
            }
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            success.setText("N");
        }
        root1.addContent(success);
        org.jdom.Document doc = new org.jdom.Document(root1);
        xmlStr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        System.out.println("xml String in receive Invoice is " + xmlStr);
        return xmlStr;
    }

    public String updateAccLibraryBudgetTransaction2(java.util.Vector vecBudgetTaId, java.math.BigDecimal orderAmount, java.math.BigDecimal invoiceAmt, java.lang.String budgetId, java.lang.String budgetLibId) {
        String res = "error";
        try {
            ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey accBudgetLibraryKey = new ejb.objectmodel.administration.ACC_LIBRARY_BUDGETKey();
            accBudgetLibraryKey.library_Id = new Integer(budgetLibId);
            accBudgetLibraryKey.budget_Id = budgetId;
            ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGET localLibBud = null;
            localLibBud = ((ejb.objectmodel.administration.LocalACC_LIBRARY_BUDGETHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_LIBRARY_BUDGET")).findByPrimaryKey(accBudgetLibraryKey);
            System.out.println("vector for taid received is " + vecBudgetTaId.size());
            for (int i = 0; i < vecBudgetTaId.size(); i = i + 3) {
                Integer taId = new Integer(vecBudgetTaId.elementAt(i).toString());
                Integer taLibId = new Integer(vecBudgetTaId.elementAt(i + 1).toString());
                java.math.BigDecimal orderAmtForThisBudget = (java.math.BigDecimal) vecBudgetTaId.elementAt(i + 2);
                java.math.BigDecimal percent = new java.math.BigDecimal(String.valueOf(1));
                try {
                    percent = orderAmtForThisBudget.divide(orderAmount, java.math.BigDecimal.ROUND_UNNECESSARY);
                } catch (Exception e) {
                    System.out.println("exception is " + e);
                }
                System.out.println("percentage is " + percent);
                Object objbudgetTransaction[] = new Object[0];
                objbudgetTransaction = ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).findByBudgetTransactions(taLibId, taId).toArray();
                for (int m = 0; m < objbudgetTransaction.length; m++) {
                    ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION localBudgetTransaction = (ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTION) objbudgetTransaction[m];
                    java.math.BigDecimal commitedAmt = localLibBud.getCommitted_Amt();
                    java.math.BigDecimal expenditureAmt = localLibBud.getExpenditure_Amt();
                    java.math.BigDecimal balanceAmt = localLibBud.getBalance_Amt();
                    java.math.BigDecimal invoiceAmtForThisBudget = percent.multiply(invoiceAmt);
                    System.out.println("invoice amt in case : orderamt gretaer than invoice " + invoiceAmtForThisBudget);
                    int value = orderAmount.compareTo(invoiceAmt);
                    System.out.println("comparision of order amt with invoice amt " + value);
                    if (value == 0) {
                        commitedAmt = commitedAmt.subtract(orderAmtForThisBudget);
                        expenditureAmt = expenditureAmt.add(orderAmtForThisBudget);
                        localLibBud.setCommitted_Amt(commitedAmt);
                        localLibBud.setExpenditure_Amt(expenditureAmt);
                        localBudgetTransaction.setCommitted_Or_Expenditure("E");
                    } else if (value > 0) {
                        java.math.BigDecimal remainingAmt = orderAmount.subtract(invoiceAmtForThisBudget);
                        commitedAmt = commitedAmt.subtract(orderAmtForThisBudget);
                        expenditureAmt = expenditureAmt.add(invoiceAmtForThisBudget);
                        balanceAmt = balanceAmt.add(remainingAmt);
                        System.out.println("commited amt is " + commitedAmt);
                        System.out.println("expend amt is " + expenditureAmt);
                        System.out.println("balance is " + balanceAmt);
                        System.out.println("ramaining amount" + remainingAmt);
                        localLibBud.setBalance_Amt(balanceAmt);
                        localLibBud.setCommitted_Amt(commitedAmt);
                        localLibBud.setExpenditure_Amt(expenditureAmt);
                        localBudgetTransaction.setCommitted_Or_Expenditure("E");
                        localBudgetTransaction.setTa_Amt(invoiceAmtForThisBudget);
                    } else if (value < 0) {
                        java.math.BigDecimal additionalAmt = invoiceAmtForThisBudget.subtract(orderAmtForThisBudget);
                        commitedAmt = commitedAmt.subtract(orderAmtForThisBudget);
                        expenditureAmt = expenditureAmt.add(invoiceAmtForThisBudget);
                        balanceAmt = balanceAmt.subtract(additionalAmt);
                        System.out.println("commited amt is " + commitedAmt);
                        System.out.println("expend amt is " + expenditureAmt);
                        System.out.println("balance is " + balanceAmt);
                        System.out.println("additionalAmt amount " + additionalAmt);
                        localLibBud.setBalance_Amt(balanceAmt);
                        localLibBud.setCommitted_Amt(commitedAmt);
                        localLibBud.setExpenditure_Amt(expenditureAmt);
                        localBudgetTransaction.setCommitted_Or_Expenditure("E");
                        int taIdNext = ejb.bprocess.util.Utility.getInstance().getBudgetTransactionID(localBudgetTransaction.getLibrary_Id().intValue());
                        java.sql.Timestamp todayDt = ejb.bprocess.util.Utility.getInstance().getTimestampWithoutTime();
                        java.math.BigDecimal taAmt = additionalAmt;
                        ((ejb.objectmodel.administration.LocalACC_BUDGET_TRANSACTIONHome) ejb.bprocess.util.HomeFactory.getInstance().getHome("ACC_BUDGET_TRANSACTION")).create(localBudgetTransaction.getLibrary_Id(), new Integer(taIdNext), todayDt, taAmt, "D", localBudgetTransaction.getBudget_Id(), "E", todayDt, null, "1", todayDt);
                    }
                    System.out.println("commited amt is " + commitedAmt);
                    System.out.println("expend amt is " + expenditureAmt);
                    System.out.println("balance is " + balanceAmt);
                }
            }
            res = "success";
        } catch (Exception e) {
            e.printStackTrace();
            res = "error";
        }
        return res;
    }

    private javax.ejb.SessionContext context;
}
