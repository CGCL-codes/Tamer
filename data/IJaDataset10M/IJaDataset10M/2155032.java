package com.brekeke.hiway.ticket.action;

import java.util.Date;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.brekeke.hiway.common.Constants;
import com.brekeke.hiway.common.KeyUtility;
import com.brekeke.hiway.common.TableNameUntility;
import com.brekeke.hiway.ticket.dto.TableDto;
import com.brekeke.hiway.ticket.dto.TicketDto;
import com.brekeke.hiway.ticket.entity.TicketRS;
import com.brekeke.hiway.ticket.entity.User;
import com.brekeke.hiway.ticket.service.TicketService;
import com.brekeke.hiway.util.DateFormatUtility;
import com.opensymphony.xwork2.ActionContext;

/**
 * The action control ticket receiving and sending  update , delete and insert operations
 * @author LEPING.LI
 * @version 1.0.0
 */
public class TicketRSCommandAction extends SuperAction {

    private static final long serialVersionUID = -4971454821913741239L;

    public Logger log = Logger.getLogger(TicketRSCommandAction.class);

    private String id;

    private String types;

    private String tdate;

    private String tsName;

    private String twWord;

    private String twNumber;

    private String tdocket;

    private String tpersonnel;

    private String ticketbox;

    private String tgetbegin;

    private String tgetend;

    private String tusebegin;

    private String tuseend;

    private String tlivebegin;

    private String tliveend;

    private String tremark;

    private String types1;

    private String tid;

    private String operation;

    private String ticketType;

    private String startDate;

    private String endDate;

    private String result;

    private TicketService ticketService;

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
	 * 修改单条票据收发记录
	 * @return 
	 * @throws Exception
	 */
    public String ajaxModifyTicketRs() throws Exception {
        TicketRS trs = new TicketRS();
        trs.setId(this.id == null || "".equals(this.id) ? null : this.id);
        trs.setTypes(this.types == null || "".equals(this.types) ? null : Integer.parseInt(this.types.trim()));
        trs.setTdate(DateFormatUtility.formatToDate(tdate, "yy-MM-dd"));
        trs.setTsName(this.tsName == null || "".equals(this.tsName.trim()) ? null : this.tsName.trim());
        trs.setTwWord(this.twWord == null || "".equals(this.twWord) ? null : this.twWord.trim());
        trs.setTwNumber(this.twNumber == null || "".equals(this.twNumber) ? 0 : Integer.parseInt(this.twNumber.trim()));
        trs.setTdocket(this.tdocket == null || "".equals(this.tdocket) ? null : this.tdocket.trim());
        trs.setTpersonnel(this.tpersonnel == null || "".equals(this.tpersonnel) ? null : this.tpersonnel.trim());
        trs.setTicketbox(this.ticketbox == null || "".equals(this.ticketbox) ? null : Integer.parseInt(this.ticketbox.trim()));
        trs.setTgetbegin(this.tgetbegin == null || "".equals(this.tgetbegin) ? null : Integer.parseInt(this.tgetbegin.trim()));
        trs.setTgetend(this.tgetend == null || "".equals(this.tgetend) ? null : Integer.parseInt(this.tgetend.trim()));
        trs.setTusebegin(this.tusebegin == null || "".equals(this.tusebegin) ? null : Integer.parseInt(this.tusebegin.trim()));
        trs.setTuseend(this.tuseend == null || "".equals(this.tuseend) ? null : Integer.parseInt(this.tuseend.trim()));
        trs.setTremark(this.tremark == null || "".equals(this.tremark) ? null : this.tremark.trim());
        Map session = ActionContext.getContext().getSession();
        User usr = (User) session.get("usr");
        trs.setTsCode(usr.getFlag2());
        trs.setTunit(usr.getFlag4());
        trs.setOptime(new Date());
        trs.setFlag5(this.types1 == null || "".equals(this.types1) ? null : this.types1);
        trs.setFlag2(usr.getFlag3());
        if ("modify".equals(this.operation)) {
            trs.setFlag3(Constants.TICKET_OPERATION_TYPE_MODIFY);
            ticketService.modifyTicketSelective(trs);
        } else if ("add".equals(this.operation)) {
            trs.setId(KeyUtility.genkey());
            ticketService.addTicket(trs);
            trs.setFlag6(DateFormatUtility.formatToString(trs.getTdate(), "yyyy-MM-dd"));
            Map context = ActionContext.getContext().getContextMap();
            JSONObject obj = JSONObject.fromObject(trs);
            this.result = obj.toString();
        }
        return "success";
    }

    /**
	 * 新增票据收发记录
	 * @return
	 * @throws Exception
	 */
    public String ajaxAddTicketRS() throws Exception {
        TicketRS trs = new TicketRS();
        trs.setId(KeyUtility.genkey());
        trs.setTypes(this.types == null || "".equals(this.types) ? null : Integer.parseInt(this.types.trim()));
        trs.setTdate(DateFormatUtility.formatToDate(tdate, "yy-MM-dd"));
        trs.setTsName(this.tsName == null || "".equals(this.tsName.trim()) ? null : this.tsName.trim());
        trs.setTwWord(this.twWord == null || "".equals(this.twWord) ? null : this.twWord.trim());
        trs.setTwNumber(this.twNumber == null || "".equals(this.twNumber) ? 0 : Integer.parseInt(this.twNumber.trim()));
        trs.setTdocket(this.tdocket == null || "".equals(this.tdocket) ? null : this.tdocket.trim());
        trs.setTpersonnel(this.tpersonnel == null || "".equals(this.tpersonnel) ? null : this.tpersonnel.trim());
        trs.setTicketbox(this.ticketbox == null || "".equals(this.ticketbox) ? null : Integer.parseInt(this.ticketbox.trim()));
        trs.setTgetbegin(this.tgetbegin == null || "".equals(this.tgetbegin) ? null : Integer.parseInt(this.tgetbegin.trim()));
        trs.setTgetend(this.tgetend == null || "".equals(this.tgetend) ? null : Integer.parseInt(this.tgetend.trim()));
        trs.setTusebegin(this.tusebegin == null || "".equals(this.tusebegin) ? null : Integer.parseInt(this.tusebegin.trim()));
        trs.setTuseend(this.tuseend == null || "".equals(this.tuseend) ? null : Integer.parseInt(this.tuseend.trim()));
        trs.setTremark(this.tremark == null || "".equals(this.tremark) ? null : this.tremark.trim());
        trs.setFlag1(this.types1 == null || "".equals(this.types1) ? null : this.types1);
        Map session = ActionContext.getContext().getSession();
        User usr = (User) session.get("usr");
        trs.setTsCode(usr.getFlag2());
        trs.setTunit(usr.getFlag4());
        ticketService.addTicket(trs);
        Map context = ActionContext.getContext().getContextMap();
        JSONObject obj = JSONObject.fromObject(trs);
        this.result = obj.toString();
        return "success";
    }

    /**
	 * 根据ID删除票据类型(普通页面提交方式)
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public String removeTicketRsByID() throws Exception {
        try {
            Map session = ActionContext.getContext().getSession();
            User usr = (User) session.get("usr");
            TicketRS ticket = new TicketRS();
            ticket.setFlag2(usr.getFlag3());
            TableDto tableDto = new TableDto(ticket.getFlag2());
            String tableName = TableNameUntility.getTicketRSListTableName(tableDto);
            TicketDto ticketDto = new TicketDto();
            ticketDto.setTablename(tableName);
            ticketDto.setOrgaLevel(ticket.getFlag2());
            ticketDto.setId(this.tid);
            ticketService.removeTicketByID(ticketDto);
            result = "删除成功";
        } catch (RuntimeException e) {
            result = "删除失败";
        }
        return "success";
    }

    /**
	 * 根据ID删除票据类型(ajax 方式)
	 * @return
	 * @throws Exception
	 */
    public String ajaxRemoveTicketRsByID() throws Exception {
        User user = getUserInSession();
        TicketDto ticketDto = new TicketDto();
        TableDto tableDto = new TableDto(user.getFlag3());
        ticketDto.setId(this.id);
        ticketDto.setOrgaLevel(user.getFlag3());
        String tableName = TableNameUntility.getTicketRSListTableName(tableDto);
        ticketDto.setTablename(tableName);
        ticketService.removeTicketByID(ticketDto);
        return "success";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getTwWord() {
        return twWord;
    }

    public void setTwWord(String twWord) {
        this.twWord = twWord;
    }

    public String getTwNumber() {
        return twNumber;
    }

    public void setTwNumber(String twNumber) {
        this.twNumber = twNumber;
    }

    public String getTdocket() {
        return tdocket;
    }

    public void setTdocket(String tdocket) {
        this.tdocket = tdocket;
    }

    public String getTpersonnel() {
        return tpersonnel;
    }

    public void setTpersonnel(String tpersonnel) {
        this.tpersonnel = tpersonnel;
    }

    public String getTicketbox() {
        return ticketbox;
    }

    public void setTicketbox(String ticketbox) {
        this.ticketbox = ticketbox;
    }

    public String getTgetbegin() {
        return tgetbegin;
    }

    public void setTgetbegin(String tgetbegin) {
        this.tgetbegin = tgetbegin;
    }

    public String getTgetend() {
        return tgetend;
    }

    public void setTgetend(String tgetend) {
        this.tgetend = tgetend;
    }

    public String getTusebegin() {
        return tusebegin;
    }

    public void setTusebegin(String tusebegin) {
        this.tusebegin = tusebegin;
    }

    public String getTuseend() {
        return tuseend;
    }

    public void setTuseend(String tuseend) {
        this.tuseend = tuseend;
    }

    public String getTlivebegin() {
        return tlivebegin;
    }

    public void setTlivebegin(String tlivebegin) {
        this.tlivebegin = tlivebegin;
    }

    public String getTliveend() {
        return tliveend;
    }

    public void setTliveend(String tliveend) {
        this.tliveend = tliveend;
    }

    public String getTremark() {
        return tremark;
    }

    public void setTremark(String tremark) {
        this.tremark = tremark;
    }

    public TicketService getTicketService() {
        return ticketService;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTypes1() {
        return types1;
    }

    public void setTypes1(String types1) {
        this.types1 = types1;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
