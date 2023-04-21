package com.brekeke.hiway.ticket.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.brekeke.hiway.common.Constants;
import com.brekeke.hiway.common.KeyUtility;
import com.brekeke.hiway.common.TableNameUntility;
import com.brekeke.hiway.common.TicketRsConverter;
import com.brekeke.hiway.common.TypesUtility;
import com.brekeke.hiway.ticket.dao.CancelledDataCertifcateNumDAO;
import com.brekeke.hiway.ticket.dao.DisticketReportDAO;
import com.brekeke.hiway.ticket.dao.ECancelledDataDAO;
import com.brekeke.hiway.ticket.dao.EdutyDAO;
import com.brekeke.hiway.ticket.dao.ReceiptTypeDAO;
import com.brekeke.hiway.ticket.dao.XCancelledDataDAO;
import com.brekeke.hiway.ticket.dao.XdutyDAO;
import com.brekeke.hiway.ticket.dto.DisticketReasonCategoryDto;
import com.brekeke.hiway.ticket.dto.DisticketReportDto;
import com.brekeke.hiway.ticket.dto.ECancelledDto;
import com.brekeke.hiway.ticket.dto.EdutyDto;
import com.brekeke.hiway.ticket.dto.ReceiptTypeDto;
import com.brekeke.hiway.ticket.dto.TableDto;
import com.brekeke.hiway.ticket.dto.XCancelledDto;
import com.brekeke.hiway.ticket.dto.XdutyDto;
import com.brekeke.hiway.ticket.entity.BasePojo;
import com.brekeke.hiway.ticket.entity.CancelledDataCertifcateNum;
import com.brekeke.hiway.ticket.entity.DisticketReasonCategory;
import com.brekeke.hiway.ticket.entity.DisticketReport;
import com.brekeke.hiway.ticket.entity.ECancelledData;
import com.brekeke.hiway.ticket.entity.Eduty;
import com.brekeke.hiway.ticket.entity.ReceiptType;
import com.brekeke.hiway.ticket.entity.XCancelledData;
import com.brekeke.hiway.ticket.entity.Xduty;
import com.brekeke.hiway.ticket.service.DisticketTicketService;
import com.brekeke.hiway.util.DateFormatUtility;

/**
 * 废票备查账业务逻辑
 * @author LEPING.LI
 * @version 1.0.0
 */
public class DisticketTicketServiceImpl implements DisticketTicketService {

    private static final long serialVersionUID = -7285719649405798478L;

    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(DisticketTicketServiceImpl.class);

    private XCancelledDataDAO xcancelledDataDAO;

    @SuppressWarnings("unused")
    private ECancelledDataDAO ecancelledDataDAO;

    @SuppressWarnings("unused")
    private EdutyDAO edutyDAO;

    private XdutyDAO xdutyDAO;

    private ReceiptTypeDAO receiptTypeDAO;

    private DisticketReportDAO disticketReportDAO;

    private CancelledDataCertifcateNumDAO cancelledDataCertifcateNumDAO;

    /**
	 * 注入出口值班DAO对象
	 * @param xdutyDAO
	 */
    public void setXdutyDAO(XdutyDAO xdutyDAO) {
        this.xdutyDAO = xdutyDAO;
    }

    /**
	 * 注入入口废票DAO
	 * @param ecancelledDataDAO
	 */
    public void setEcancelledDataDAO(ECancelledDataDAO ecancelledDataDAO) {
        this.ecancelledDataDAO = ecancelledDataDAO;
    }

    /**
	 * 注入入口值班DAO
	 * @param edutyDAO
	 */
    public void setEdutyDAO(EdutyDAO edutyDAO) {
        this.edutyDAO = edutyDAO;
    }

    /**
	 * 注入出口废票DAO对象
	 * @param cancelledDataDAO
	 */
    public void setXcancelledDataDAO(XCancelledDataDAO xcancelledDataDAO) {
        this.xcancelledDataDAO = xcancelledDataDAO;
    }

    /**
	 * 注入票据类型DAO对象
	 * @param receiptTypeDAO
	 */
    public void setReceiptTypeDAO(ReceiptTypeDAO receiptTypeDAO) {
        this.receiptTypeDAO = receiptTypeDAO;
    }

    /**
	 * 注入废票报表数据DAO
	 * @param disticketReportDAO
	 */
    public void setDisticketReportDAO(DisticketReportDAO disticketReportDAO) {
        this.disticketReportDAO = disticketReportDAO;
    }

    public void setCancelledDataCertifcateNumDAO(CancelledDataCertifcateNumDAO cancelledDataCertifcateNumDAO) {
        this.cancelledDataCertifcateNumDAO = cancelledDataCertifcateNumDAO;
    }

    /**
	 * 根据日期查询出口废票[默认票据类型为非定额]
	 * @param xdt:需要包括的参数：
	 * 1代号 2 日期[按月查询] 3 票据类型 4 废票原因
	 * 业务分析：先查询站级所有废票，再处理相关业务分类
	 * 票据类型通过查询出口值班表获取
	 * @return List<BasePojo> 存放出口废票和入口废票
	 */
    public List<BasePojo> findXcancelledData(XCancelledDto xdt) {
        List<BasePojo> resultlist = new ArrayList<BasePojo>();
        if ("1".equals(xdt.getTtype())) {
            ECancelledDto ecd = new ECancelledDto();
            ecd.setStartDate(xdt.getStartDate());
            ecd.setEndDate(xdt.getEndDate());
            List<ECancelledData> elist = ecancelledDataDAO.serachEcancelledByDate(ecd);
            for (ECancelledData cd : elist) {
                Integer receipt_num = cd.getReceiptNum();
                EdutyDto edt = new EdutyDto();
                edt.setReceipt_num(receipt_num);
                Eduty ed = edutyDAO.serachEdutyByReceiptNum(edt);
                if (ed != null && ed.getTsCode().equals(xdt.getTscode())) {
                    Date date = cd.getCancelltime();
                    if (date != null) {
                        cd.setFlag1(DateFormatUtility.formatToString(date, "yyyy-MM-dd"));
                    }
                    resultlist.add(cd);
                }
            }
        } else {
            List<XCancelledData> xlist = xcancelledDataDAO.searchXcancelledData(xdt);
            for (XCancelledData xc : xlist) {
                Integer receipt_num = xc.getReceiptNum();
                if (receipt_num != null) {
                    XdutyDto xd = new XdutyDto();
                    xd.setReceipt_num(receipt_num);
                    Xduty xduty = xdutyDAO.searchXCancelledByReceiptNum(xd);
                    if (xduty != null && xdt.getTscode().equals(xduty.getTsCode())) {
                        Date date = xc.getCancelledtime();
                        if (date != null) {
                            xc.setFlag1(DateFormatUtility.formatToString(date, "yyyy-MM-dd"));
                        }
                        resultlist.add(xc);
                    }
                }
            }
        }
        return resultlist;
    }

    /**
     * 生成废票备查账
     * 业务：1.查询该年的指定的类型的所有废票记录
     * 2.计算摘要，完成统计等工作
     * 3.将其插入报表数据表
     * @param xdt:参数使用的是出口废票参数
     * 包含参数：1 日期  2 票据类型ID  3 站代号  4.站名称 
     * 
     * 表明的定义：disticketreport+站代号(六位)+年(四位)+两位月份
     * 例：dicticketreport_000906_200901 
     */
    public void generateDisticketLedger(XCancelledDto xdt) {
        ReceiptTypeDto rtd = new ReceiptTypeDto();
        rtd.setType(xdt.getTtype());
        ReceiptType rt = receiptTypeDAO.searchByTID(rtd);
        List<DisticketReport> disticketReportList = new ArrayList<DisticketReport>();
        String tablename = TableNameUntility.getDisticketTableName(new TableDto(null, xdt.getTscode(), xdt.getStartDate()));
        DisticketReportDto disticketReportDto = new DisticketReportDto();
        disticketReportDto.setTablename(tablename);
        disticketReportDto.setYear(DateFormatUtility.getCurrentYear(xdt.getStartDate()));
        disticketReportDto.setMonth(DateFormatUtility.getCurrentMonth(xdt.getStartDate()));
        disticketReportDto.setTicketType(xdt.getTtype());
        disticketReportDto.setTscode(xdt.getTscode());
        disticketReportDAO.createDisticketTable(disticketReportDto);
        if ("通行券".equals(rt.getTicketname())) {
            ECancelledDto ecd = new ECancelledDto();
            ecd.setStartDate(xdt.getStartDate());
            ecd.setEndDate(xdt.getEndDate());
            ecd.setTscode(xdt.getTscode());
            List<ECancelledData> dislist = ecancelledDataDAO.serachEcancelledForReport(ecd);
            Integer month = 0;
            Integer ticnumber = 0;
            Integer cnt = 0;
            for (ECancelledData cd : dislist) {
                DisticketReport dr = TicketRsConverter.converterToDisticketReport(cd);
                dr.setTictype(xdt.getTtype());
                dr.setTwnumber(1);
                dr.setTscode(xdt.getTscode());
                dr.setTicid(cd.getReceiptNum().toString());
                dr.setTdocket(cd.getMonitorname() + "|票箱" + cd.getFlag1());
                dr.setFlag05(tablename);
                disticketReportList.add(dr);
                ++ticnumber;
                ++cnt;
                if (month != 0) {
                    if (month != dr.getMonth()) {
                        DisticketReport dr1 = new DisticketReport();
                        dr1.setId(KeyUtility.genkey());
                        dr1.setTdocket("本月合计");
                        dr1.setTwnumber(1);
                        dr1.setTicnumber(ticnumber);
                        dr1.setFlag05(tablename);
                        disticketReportList.add(dr1);
                        ++ticnumber;
                        ++cnt;
                    }
                }
                month = dr.getMonth();
                if ((cnt + 1) % 19 == 0) {
                    DisticketReport dr1 = new DisticketReport();
                    dr1.setId(KeyUtility.genkey());
                    dr1.setTdocket("过次页");
                    dr1.setTicnumber(ticnumber);
                    dr1.setFlag05(tablename);
                    disticketReportList.add(dr1);
                    ++ticnumber;
                    ++cnt;
                }
                if (cnt % 19 == 0) {
                    DisticketReport dr2 = new DisticketReport();
                    dr2.setId(KeyUtility.genkey());
                    dr2.setTdocket("承前页");
                    dr2.setTicnumber(ticnumber);
                    dr2.setFlag05(tablename);
                    disticketReportList.add(dr2);
                    ++ticnumber;
                    ++cnt;
                }
            }
            xdt.setReportDataCount(disticketReportList.size());
            disticketReportDAO.insertBatchDisticket(disticketReportList, tablename);
        } else {
            if ("非定额收据".equals(rt.getTicketname())) {
                xdt.setManualflag(0);
            } else {
                xdt.setManualflag(1);
            }
            List<XCancelledData> dislist = xcancelledDataDAO.searchXcancelledForReport(xdt);
            Integer month = 0;
            Integer ticnumber = 0;
            Integer cnt = 0;
            for (XCancelledData cd : dislist) {
                DisticketReport dr = TicketRsConverter.converterToDisticketReport(cd);
                dr.setTictype(xdt.getTtype());
                dr.setTdocket(cd.getMonitorname() + "|票箱" + cd.getFlag1());
                dr.setTscode(xdt.getTscode());
                dr.setTicnumber(1);
                dr.setFlag05(tablename);
                disticketReportList.add(dr);
                ++ticnumber;
                ++cnt;
                if (month != 0) {
                    if (month != dr.getMonth()) {
                        DisticketReport dr1 = new DisticketReport();
                        dr1.setId(KeyUtility.genkey());
                        dr1.setTdocket("本月合计");
                        dr1.setTicnumber(ticnumber);
                        dr1.setFlag05(tablename);
                        disticketReportList.add(dr1);
                        ++ticnumber;
                        ++cnt;
                    }
                }
                month = dr.getMonth();
                if ((cnt + 1) % 19 == 0) {
                    DisticketReport dr1 = new DisticketReport();
                    dr1.setId(KeyUtility.genkey());
                    dr1.setTdocket("过次页");
                    dr1.setTicnumber(ticnumber);
                    dr1.setFlag05(tablename);
                    disticketReportList.add(dr1);
                    ++ticnumber;
                    ++cnt;
                }
                if (cnt % 19 == 0) {
                    DisticketReport dr2 = new DisticketReport();
                    dr2.setId(KeyUtility.genkey());
                    dr2.setTdocket("承前页");
                    dr2.setTicnumber(ticnumber);
                    dr2.setFlag05(tablename);
                    disticketReportList.add(dr2);
                    ++ticnumber;
                    ++cnt;
                }
            }
            xdt.setReportDataCount(disticketReportList.size());
            disticketReportDAO.insertBatchDisticket(disticketReportList, tablename);
        }
    }

    /**
	 * 查询废票报表数据
	 * @param xdt:包含参数：
	 * 1 日期  2 票据类型ID  3 站代号  4.站名称
	 * 表明的定义：disticketreport+站代号(六位)+年(四位)+票据类型(ID四拉,不够前补0)
     * 例：dicticketreport00090620090001 
	 */
    public List<DisticketReport> findDisticketLedger(XCancelledDto xdt) {
        Calendar cal = Calendar.getInstance();
        String tablename = "disticketreport" + xdt.getTscode() + cal.get(Calendar.YEAR) + TypesUtility.converter4Type(xdt.getTtype());
        DisticketReportDto drt = new DisticketReportDto();
        drt.setTablename(tablename);
        return disticketReportDAO.searchDisticketList(drt);
    }

    /**
     * 根据日期和凭证号查询废票记录
     * @param xdt
     * @return
     */
    public List<BasePojo> findCanncelledByCountFlag(XCancelledDto xdt) {
        List<BasePojo> result = new ArrayList<BasePojo>();
        ReceiptTypeDto rtd = new ReceiptTypeDto();
        rtd.setType(xdt.getTtype());
        ReceiptType rt = receiptTypeDAO.searchByTID(rtd);
        if ("通行券".equals(rt.getTicketname())) {
            ECancelledDto ecd = new ECancelledDto();
            ecd.setStartDate(xdt.getStartDate());
            ecd.setEndDate(xdt.getEndDate());
            ecd.setCertificateNumberFlag(xdt.getCertificateNumberFlag());
            ecd.setPage(xdt.getPage());
            ecd.setStartResult(xdt.getStartResult());
            ecd.setEndResult(xdt.getEndResult());
            List<ECancelledData> ecdlist = ecancelledDataDAO.serachPagingEcancelledByCountFlag(ecd);
            for (ECancelledData cd : ecdlist) {
                Integer receipt_num = cd.getReceiptNum();
                EdutyDto ed = new EdutyDto();
                ed.setReceipt_num(receipt_num);
                Eduty eduty = edutyDAO.serachEdutyByReceiptNum(ed);
                if (eduty != null && eduty.getTsCode().equals(xdt.getTscode())) {
                    result.add(cd);
                }
            }
        } else {
            Integer manualflag = 0;
            if (2 == xdt.getTtype()) {
                manualflag = 0;
            } else {
                manualflag = 1;
                xdt.setChargeperticket(Integer.parseInt(getChargeperticket(rt.getTicketname())));
            }
            xdt.setManualflag(manualflag);
            List<XCancelledData> xclist = xcancelledDataDAO.searchPagingXcancelledByCountFlag(xdt);
            for (XCancelledData cd : xclist) {
                Integer receipt_num = cd.getReceiptNum();
                XdutyDto xd = new XdutyDto();
                xd.setReceipt_num(receipt_num);
                Xduty xduty = xdutyDAO.searchXCancelledByReceiptNum(xd);
                if (xduty != null && xduty.getTsCode().equals(xdt.getTscode())) {
                    result.add(cd);
                }
            }
        }
        return result;
    }

    /**
     * 修改废票凭证号
     * @param xdt
     * @return
     */
    public void modifyDisticketByID(CancelledDataCertifcateNum cancelledDataCertifcateNum) {
        CancelledDataCertifcateNum certifcateNum = cancelledDataCertifcateNumDAO.searchCancelledDataCertifcateNumByID(cancelledDataCertifcateNum);
        if (certifcateNum == null) {
            cancelledDataCertifcateNumDAO.insertCancelledDataCertifcateNumByID(cancelledDataCertifcateNum);
        } else {
            cancelledDataCertifcateNumDAO.updateCancelledDataCertifcateNumByID(cancelledDataCertifcateNum);
        }
    }

    /**
     * 查询废票凭证号
     * @param xdt
     * @return
     */
    public BasePojo findDisticketByID(XCancelledDto xdt) {
        BasePojo returnBasePojo;
        ReceiptTypeDto rtd = new ReceiptTypeDto();
        rtd.setType(xdt.getTtype());
        ReceiptType rt = receiptTypeDAO.searchByTID(rtd);
        if ("通行券".equals(rt.getTicketname())) {
            ECancelledData ecd = new ECancelledData();
            ecd.setDutyid(xdt.getId());
            returnBasePojo = ecancelledDataDAO.serachEcancelledById(ecd);
        } else {
            XCancelledData ecd = new XCancelledData();
            ecd.setDutyid(xdt.getId());
            returnBasePojo = xcancelledDataDAO.searchXcancelledById(ecd);
        }
        return returnBasePojo;
    }

    private String getChargeperticket(String ticketName) {
        StringBuffer charge = new StringBuffer();
        for (int i = 0; i < ticketName.length(); i++) {
            char tmp = ticketName.charAt(i);
            if (tmp >= '0' && tmp <= '9') {
                charge.append(tmp);
            } else {
                break;
            }
        }
        return charge.toString();
    }

    public CancelledDataCertifcateNum findcancelledDataCertifcateNumByID(CancelledDataCertifcateNum cancelledDataCertifcateNum) {
        return cancelledDataCertifcateNumDAO.searchCancelledDataCertifcateNumByID(cancelledDataCertifcateNum);
    }

    public List<String> findAllReasonCategory() {
        List<String> returnReasonCategoryList = new ArrayList<String>();
        for (DisticketReasonCategory reasonCategory : Constants.DISTICKET_REASON_CATEGORY_LIST) {
            returnReasonCategoryList.add(reasonCategory.getReasonCategory());
        }
        return returnReasonCategoryList;
    }

    /**
     * 按照原因分类，查询废票原因。<br>
     * 当原因分类是其他类型时，就返回所有一般类型的。
     * @param reasonCategory
     * @return List<String>
     */
    private List<String> findReasonListByCategory(String category) {
        List<String> returnReasonList = new ArrayList<String>();
        if (Constants.DISTICKET_REASON_CATEGORY_OTHER_REASON.equals(category)) {
            for (DisticketReasonCategory reasonCategory : Constants.DISTICKET_REASON_CATEGORY_LIST) {
                if (!reasonCategory.getReasonCategory().equals(category)) {
                    for (String string : reasonCategory.getReasons()) {
                        returnReasonList.add(string);
                    }
                }
            }
        } else {
            for (DisticketReasonCategory reasonCategory : Constants.DISTICKET_REASON_CATEGORY_LIST) {
                if (reasonCategory.getReasonCategory().equals(category)) {
                    returnReasonList = reasonCategory.getReasons();
                }
            }
        }
        return returnReasonList;
    }

    /**
     * 按照原因分类，查询废票原因
     * @param reasonCategory
     * @return List<String>
     */
    public List<String> findReasonListByCategoryFromDB(DisticketReasonCategoryDto disticketReasonCategoryDto) {
        List<String> returnReasonList = new ArrayList<String>();
        if (Constants.DISTICKET_REASON_CATEGORY_OTHER_REASON.equals(disticketReasonCategoryDto.getReason())) {
            List<BasePojo> allDisticketList = findDisticketByReasonCategory(disticketReasonCategoryDto);
            for (BasePojo disticket : allDisticketList) {
                String disticketReason = "";
                if (disticket instanceof ECancelledData) {
                    disticketReason = ((ECancelledData) disticket).getReason();
                } else if (disticket instanceof XCancelledData) {
                    disticketReason = ((XCancelledData) disticket).getReason();
                }
                if (!returnReasonList.contains(disticketReason)) {
                    returnReasonList.add(disticketReason);
                }
            }
        } else {
            for (DisticketReasonCategory reasonCategory : Constants.DISTICKET_REASON_CATEGORY_LIST) {
                if (reasonCategory.getReasonCategory().equals(disticketReasonCategoryDto.getReason())) {
                    returnReasonList = reasonCategory.getReasons();
                }
            }
        }
        return returnReasonList;
    }

    public List<DisticketReasonCategory> findAllReasonCategoryNum(DisticketReasonCategoryDto disticketReasonCategoryDto) {
        List<DisticketReasonCategory> reasonCategoryNumList = new ArrayList<DisticketReasonCategory>();
        List<String> reasonCategoryList = findAllReasonCategory();
        List<BasePojo> allDisticketList = findAllDisticket(disticketReasonCategoryDto);
        int disticketNotOtherReasonNum = 0;
        for (String reasonCategory : reasonCategoryList) {
            if (!Constants.DISTICKET_REASON_CATEGORY_OTHER_REASON.equals(reasonCategory)) {
                int disticketNum = 0;
                for (BasePojo disticket : allDisticketList) {
                    String disticketReason = "";
                    if (disticket instanceof ECancelledData) {
                        disticketReason = ((ECancelledData) disticket).getReason();
                    } else if (disticket instanceof XCancelledData) {
                        disticketReason = ((XCancelledData) disticket).getReason();
                    }
                    List<String> reasonList = findReasonListByCategory(reasonCategory);
                    if (reasonList.contains(disticketReason)) {
                        disticketNum++;
                    }
                }
                disticketNotOtherReasonNum = disticketNotOtherReasonNum + disticketNum;
                DisticketReasonCategory disticketReasonCategory = new DisticketReasonCategory();
                disticketReasonCategory.setReasonCategory(reasonCategory);
                disticketReasonCategory.setReasonCategoryNum(disticketNum);
                reasonCategoryNumList.add(disticketReasonCategory);
            }
        }
        for (String reasonCategory : reasonCategoryList) {
            if (Constants.DISTICKET_REASON_CATEGORY_OTHER_REASON.equals(reasonCategory)) {
                int disticketNum = allDisticketList.size() - disticketNotOtherReasonNum;
                DisticketReasonCategory disticketReasonCategory = new DisticketReasonCategory();
                disticketReasonCategory.setReasonCategory(reasonCategory);
                disticketReasonCategory.setReasonCategoryNum(disticketNum);
                reasonCategoryNumList.add(disticketReasonCategory);
                break;
            }
        }
        DisticketReasonCategory disticketReasonCategory = new DisticketReasonCategory();
        disticketReasonCategory.setReasonCategory(Constants.DISTICKET_REASON_CATEGORY_All);
        disticketReasonCategory.setReasonCategoryNum(allDisticketList.size());
        reasonCategoryNumList.add(disticketReasonCategory);
        return reasonCategoryNumList;
    }

    /**
     * 按照废票原因分类，查询废票记录
     * @param disticketReasonCategoryDto
     * @return List<BasePojo>
     */
    public List<BasePojo> findDisticketByReasonCategory(DisticketReasonCategoryDto disticketReasonCategoryDto) {
        List<BasePojo> result = new ArrayList<BasePojo>();
        ReceiptTypeDto rtd = new ReceiptTypeDto();
        rtd.setType(disticketReasonCategoryDto.getTtype());
        ReceiptType rt = receiptTypeDAO.searchByTID(rtd);
        disticketReasonCategoryDto.setReasonList(findReasonListByCategory(disticketReasonCategoryDto.getReason()));
        if ("通行券".equals(rt.getTicketname())) {
            List<ECancelledData> ecdlist = ecancelledDataDAO.serachEcancelledByReasonCategory(disticketReasonCategoryDto);
            for (ECancelledData cd : ecdlist) {
                Integer receipt_num = cd.getReceiptNum();
                EdutyDto ed = new EdutyDto();
                ed.setReceipt_num(receipt_num);
                Eduty eduty = edutyDAO.serachEdutyByReceiptNum(ed);
                if (eduty != null && eduty.getTsCode().equals(disticketReasonCategoryDto.getTscode())) {
                    result.add(cd);
                }
            }
        } else {
            Integer manualflag = 0;
            if (2 == disticketReasonCategoryDto.getTtype()) {
                manualflag = 0;
            } else {
                manualflag = 1;
                disticketReasonCategoryDto.setChargeperticket(Integer.parseInt(getChargeperticket(rt.getTicketname())));
            }
            disticketReasonCategoryDto.setManualflag(manualflag);
            List<XCancelledData> xclist = xcancelledDataDAO.searchXcancelledByReasonCategory(disticketReasonCategoryDto);
            for (XCancelledData cd : xclist) {
                Integer receipt_num = cd.getReceiptNum();
                XdutyDto xd = new XdutyDto();
                xd.setReceipt_num(receipt_num);
                Xduty xduty = xdutyDAO.searchXCancelledByReceiptNum(xd);
                if (xduty != null && xduty.getTsCode().equals(disticketReasonCategoryDto.getTscode())) {
                    result.add(cd);
                }
            }
        }
        return result;
    }

    /**
     * 查询所有废票记录 (不分页的)
     * @param 
     * @return List<String>
     */
    public List<BasePojo> findAllDisticket(DisticketReasonCategoryDto disticketReasonCategoryDto) {
        List<BasePojo> result = new ArrayList<BasePojo>();
        ReceiptTypeDto rtd = new ReceiptTypeDto();
        rtd.setType(disticketReasonCategoryDto.getTtype());
        ReceiptType rt = receiptTypeDAO.searchByTID(rtd);
        if ("通行券".equals(rt.getTicketname())) {
            ECancelledDto ecd = new ECancelledDto();
            ecd.setStartDate(disticketReasonCategoryDto.getStartDate());
            ecd.setEndDate(disticketReasonCategoryDto.getEndDate());
            ecd.setCertificateNumberFlag(2);
            ecd.setPage(disticketReasonCategoryDto.getPage());
            ecd.setStartResult(disticketReasonCategoryDto.getStartResult());
            ecd.setEndResult(disticketReasonCategoryDto.getEndResult());
            List<ECancelledData> ecdlist = ecancelledDataDAO.serachEcancelledByCountFlag(ecd);
            for (ECancelledData cd : ecdlist) {
                Integer receipt_num = cd.getReceiptNum();
                EdutyDto ed = new EdutyDto();
                ed.setReceipt_num(receipt_num);
                Eduty eduty = edutyDAO.serachEdutyByReceiptNum(ed);
                if (eduty != null && eduty.getTsCode().equals(disticketReasonCategoryDto.getTscode())) {
                    result.add(cd);
                }
            }
        } else {
            XCancelledDto xcd = new XCancelledDto();
            xcd.setStartDate(disticketReasonCategoryDto.getStartDate());
            xcd.setEndDate(disticketReasonCategoryDto.getEndDate());
            xcd.setCertificateNumberFlag(2);
            xcd.setPage(disticketReasonCategoryDto.getPage());
            xcd.setStartResult(disticketReasonCategoryDto.getStartResult());
            xcd.setEndResult(disticketReasonCategoryDto.getEndResult());
            Integer manualflag = 0;
            if (2 == disticketReasonCategoryDto.getTtype()) {
                manualflag = 0;
            } else {
                manualflag = 1;
                xcd.setChargeperticket(Integer.parseInt(getChargeperticket(rt.getTicketname())));
            }
            xcd.setManualflag(manualflag);
            List<XCancelledData> xclist = xcancelledDataDAO.searchXcancelledByCountFlag(xcd);
            for (XCancelledData cd : xclist) {
                Integer receipt_num = cd.getReceiptNum();
                XdutyDto xd = new XdutyDto();
                xd.setReceipt_num(receipt_num);
                Xduty xduty = xdutyDAO.searchXCancelledByReceiptNum(xd);
                if (xduty != null && xduty.getTsCode().equals(disticketReasonCategoryDto.getTscode())) {
                    result.add(cd);
                }
            }
        }
        return result;
    }

    public List<DisticketReasonCategory> initReasonCategoryNum() {
        List<DisticketReasonCategory> reasonCategoryNumList = new ArrayList<DisticketReasonCategory>();
        List<String> reasonCategoryList = findAllReasonCategory();
        for (String reasonCategory : reasonCategoryList) {
            DisticketReasonCategory disticketReasonCategory = new DisticketReasonCategory();
            disticketReasonCategory.setReasonCategory(reasonCategory);
            disticketReasonCategory.setReasonCategoryNum(0);
            reasonCategoryNumList.add(disticketReasonCategory);
        }
        DisticketReasonCategory disticketReasonCategory = new DisticketReasonCategory();
        disticketReasonCategory.setReasonCategory(Constants.DISTICKET_REASON_CATEGORY_All);
        disticketReasonCategory.setReasonCategoryNum(0);
        reasonCategoryNumList.add(disticketReasonCategory);
        return reasonCategoryNumList;
    }
}
