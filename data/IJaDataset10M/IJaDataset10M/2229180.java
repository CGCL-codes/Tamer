package com.brekeke.hiway.ticket.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.brekeke.hiway.common.ExceptionUtility;
import com.brekeke.hiway.common.TableNameUntility;
import com.brekeke.hiway.ticket.dto.ReceiptTypeDto;
import com.brekeke.hiway.ticket.dto.TableDto;
import com.brekeke.hiway.ticket.dto.TicketDto;
import com.brekeke.hiway.ticket.dto.UserDto;
import com.brekeke.hiway.ticket.entity.ReceiptType;
import com.brekeke.hiway.ticket.entity.TicketRS;
import com.brekeke.hiway.ticket.entity.User;
import com.brekeke.hiway.ticket.service.ReceiptTypeService;
import com.brekeke.hiway.ticket.service.TicketService;
import com.brekeke.hiway.ticket.service.UserService;
import com.brekeke.hiway.util.DateFormatUtility;
import com.brekeke.hiway.util.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 票据收发记录查询操作实现
 * @author LEPING.LI
 * @version 1.0.0
 */
public class TicketRSSearchAction extends ActionSupport {

    private static final long serialVersionUID = -2576265238258593977L;

    private Logger log = Logger.getLogger(TicketRSSearchAction.class);

    private String tid;

    private String id;

    private String ticketType;

    private String startDate;

    private String endDate;

    private String currentPage;

    private String result;

    private String orgaName;

    private String ticketName;

    private TicketService ticketService;

    private ReceiptTypeService rtypeService;

    private UserService userService;

    /**
	 * 注入相关业务对象实例
	 * @param ***Service
	 */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void setRtypeService(ReceiptTypeService rtypeService) {
        this.rtypeService = rtypeService;
    }

    /**
	 * 根据票据类型和日期查询票据收发记录列表
	 * @return String 
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public String findTicketRSByTypeAndDate() throws Exception {
        Map map = ActionContext.getContext().getContextMap();
        Map session = ActionContext.getContext().getSession();
        User user = (User) session.get("usr");
        try {
            Date sdate = DateFormatUtility.formatMonthFirstTime(this.startDate.substring(0, 4), this.startDate.substring(5, 7));
            Date edate = DateFormatUtility.formatMonthLastTime(this.endDate.substring(0, 4), this.endDate.substring(5, 7));
            TicketDto td = new TicketDto(Integer.parseInt(ticketType), sdate, edate);
            Page page = new Page();
            if (this.currentPage == null || "".equals(this.currentPage)) {
                page.setCurrentPage(0);
                td.setStartResult(0);
                td.setEndResult(page.getPageSize());
            } else {
                page.setCurrentPage(Integer.parseInt(this.currentPage));
                td.setStartResult(page.getFristRow());
                td.setEndResult(page.getFristRow() + page.getPageSize());
            }
            td.setPage(page);
            TicketRS ticket = new TicketRS();
            ticket.setFlag2(user.getFlag3());
            this.orgaName = user.getFlag4();
            String tableName = TableNameUntility.getTicketRSListTableName(new TableDto(ticket.getFlag2()));
            td.setTablename(tableName);
            List<TicketRS> ticketList = ticketService.findTicketListByTypeAndDate(td);
            for (TicketRS ticketRS : ticketList) {
                ticketRS.setFlag1(DateFormatUtility.formatToString(ticketRS.getTdate(), "yyyy-MM-dd"));
            }
            map.put("ticketrslist", ticketList);
            map.put("page", page);
            String level = user.getFlag4();
            ReceiptTypeDto rtd = new ReceiptTypeDto();
            rtd.setLevel(level);
            List<ReceiptType> rtlist = rtypeService.findRtByLevel(rtd);
            map.put("rtlist", rtlist);
            for (ReceiptType rt : rtlist) {
                if (rt.getTypeid().equals(Integer.parseInt(this.ticketType))) {
                    this.ticketName = rt.getTicketname();
                    break;
                }
            }
            String orgaid = user.getFlag1();
            UserDto ud = new UserDto();
            ud.setOrgaid(orgaid);
            List<User> ulist = userService.findUserByOrga(ud);
            map.put("ulist", ulist);
        } catch (Exception e) {
            log.error("ticket search error!" + ExceptionUtility.getExceptionMessage(e));
        }
        return "ticketrslist";
    }

    /**
	 * 首次转向票据收发页面
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public String toTicketRSSerach() throws Exception {
        Map map = ActionContext.getContext().getContextMap();
        Map session = ActionContext.getContext().getSession();
        User user = (User) session.get("usr");
        String level = user.getFlag4();
        ReceiptTypeDto rtd = new ReceiptTypeDto();
        rtd.setLevel(level);
        List<ReceiptType> rtlist = rtypeService.findRtByLevel(rtd);
        map.put("rtlist", rtlist);
        String orgaid = user.getFlag1();
        UserDto ud = new UserDto();
        ud.setOrgaid(orgaid);
        List<User> ulist = userService.findUserByOrga(ud);
        map.put("ulist", ulist);
        return "success";
    }

    /**
	 * 查询ID查询单个票据收发记录
	 * [采用ajax 方式返回TicketRS Josn对象]
	 * 需要参数：TicketRS ID
	 * @return 
	 * @throws Exception
	 */
    public String ajaxFindTicketRsByID() throws Exception {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(this.id);
        TicketRS tr = new TicketRS();
        Map session = ActionContext.getContext().getSession();
        User usr = (User) session.get("usr");
        tr.setFlag2(usr.getFlag3());
        TableDto tableDto = new TableDto(tr.getFlag2());
        String tableName = TableNameUntility.getTicketRSListTableName(tableDto);
        ticketDto.setTablename(tableName);
        TicketRS ts = ticketService.findTicketByID(ticketDto);
        Integer type = ts.getTypes();
        String level = usr.getFlag4();
        ReceiptTypeDto rtd = new ReceiptTypeDto();
        rtd.setLevel(level);
        List<ReceiptType> rtlist = rtypeService.findRtByLevel(rtd);
        for (ReceiptType rt : rtlist) {
            if (type.equals(rt.getTypeid())) {
                ts.setFlag1(rt.getTicketname());
            }
        }
        ts.setFlag2(DateFormatUtility.formatToString(ts.getTdate(), "yyyy-MM-dd"));
        JSONObject obj = JSONObject.fromObject(ts);
        this.result = obj.toString();
        return "success";
    }

    /**
	 * 检测票据的合法性
	 * @return
	 * @throws Exception
	 */
    public String checkTicketRSValid() throws Exception {
        TicketDto td = new TicketDto();
        Map session = ActionContext.getContext().getSession();
        User usr = (User) session.get("usr");
        td.setTtype(Integer.parseInt(this.ticketType));
        td.setOrgaCode(usr.getFlag2());
        Date startDate = DateFormatUtility.formatToDate(this.startDate.substring(0, 4) + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endDate = DateFormatUtility.formatToDate(this.startDate.substring(0, 4) + "-12-31 00:00:00", "yyyy-MM-dd HH:mm:ss");
        td.setStartDate(startDate);
        td.setEndDate(endDate);
        List<TicketRS> badticlist = ticketService.checkTicketValid(td);
        JSONArray obj = JSONArray.fromObject(badticlist);
        this.result = obj.toString();
        return "success";
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgaName() {
        return orgaName;
    }

    public void setOrgaName(String orgaName) {
        this.orgaName = orgaName;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }
}
