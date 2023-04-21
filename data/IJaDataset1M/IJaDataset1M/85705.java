package jp.groupsession.v2.tcd.tcd020;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.StringUtil;
import jp.co.sjts.util.date.UDate;
import jp.co.sjts.util.date.UDateUtil;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstTimecard;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.base.CmnUsrmInfDao;
import jp.groupsession.v2.cmn.model.base.CmnUsrmInfModel;
import jp.groupsession.v2.cmn.model.base.TcdAdmConfModel;
import jp.groupsession.v2.prj.GSConstProject;
import jp.groupsession.v2.prj.dao.ProjectSearchDao;
import jp.groupsession.v2.prj.model.ProjectItemModel;
import jp.groupsession.v2.prj.model.ProjectSearchModel;
import jp.groupsession.v2.tcd.TimecardBiz;
import jp.groupsession.v2.tcd.dao.TcdPriConfModel;
import jp.groupsession.v2.tcd.dao.TcdPrjWorktimeDao;
import jp.groupsession.v2.tcd.dao.TcdTcdataDao;
import jp.groupsession.v2.tcd.model.TcdPrjWorktimeModel;
import jp.groupsession.v2.tcd.model.TcdTcdataModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * �^�C���J�[�h�ҏW�̃r�W�l�X���W�b�N
 * @author JTS
 *
 */
public class Tcd020Biz {

    /** Logging �C���X�^���X */
    private static Log log__ = LogFactory.getLog(Tcd020Biz.class);

    /**
     * <br>����\����ʏ����擾���܂�
     * @param map �A�N�V�����}�b�s���O
     * @param form �A�N�V�����t�H�[��
     * @param req ���N�G�X�g
     * @param res ���X�|���X
     * @param con �R�l�N�V����
     * @return Sch010Form �A�N�V�����t�H�[��
     * @throws SQLException SQL���s����O
     */
    public Tcd020Form getInitData(ActionMapping map, Tcd020Form form, HttpServletRequest req, HttpServletResponse res, Connection con) throws SQLException {
        if (StringUtil.isNullZeroString(form.getEditDay())) {
            if (form.getSelectDay().length == 1) {
                String[] days = form.getSelectDay();
                log__.debug("days[0]==>" + days[0]);
                form.setEditDay(days[0]);
                __doInitOneData(map, form, req, res, con);
            } else {
                __doInitDatas(map, form, req, res, con);
            }
        } else {
            __doInitOneData(map, form, req, res, con);
        }
        return form;
    }

    /**
     * <br>[�@  �\] ����\��(�P��)
     * <br>[��  ��]
     * <br>[��  �l]
     *
     * @param map �}�b�v
     * @param form �t�H�[��
     * @param req ���N�G�X�g
     * @param res ���X�|���X
     * @param con �R�l�N�V����
     * @throws SQLException SQL���s����O
     */
    private void __doInitOneData(ActionMapping map, Tcd020Form form, HttpServletRequest req, HttpServletResponse res, Connection con) throws SQLException {
        HttpSession session = req.getSession();
        BaseUserModel usModel = (BaseUserModel) session.getAttribute(GSConst.SESSION_KEY);
        int sessionUsrSid = usModel.getUsrsid();
        TimecardBiz tBiz = new TimecardBiz();
        TcdAdmConfModel admConf = tBiz.getTcdAdmConfModel(sessionUsrSid, con);
        setTimeCombo(form, sessionUsrSid, con);
        setProjectCombo(form, sessionUsrSid, con);
        int userSid = Integer.parseInt(form.getUsrSid());
        CmnUsrmInfDao uiDao = new CmnUsrmInfDao(con);
        CmnUsrmInfModel uiMdl = uiDao.select(userSid);
        form.setTcd020Name(uiMdl.getUsiSei() + " " + uiMdl.getUsiMei());
        int year = Integer.parseInt(form.getYear());
        int month = Integer.parseInt(form.getMonth());
        int day = Integer.parseInt(form.getEditDay());
        UDate udate = getEditDate(year, month, day, admConf.getTacSimebi());
        String editDate = UDateUtil.getYymdJ(udate);
        form.setTcd020Date(editDate);
        TcdTcdataDao tcdDao = new TcdTcdataDao(con);
        TcdTcdataModel tcdMdl = tcdDao.select(userSid, udate.getYear(), udate.getMonth(), udate.getIntDay());
        String tcd020InHour = null;
        String tcd020InMinute = null;
        String tcd020OutHour = null;
        String tcd020OutMinute = null;
        String tcd020Biko = null;
        String tcd020HolKbn = "0";
        String tcd020HolValue = null;
        String tcd020HolDays = null;
        String tcd020ChkKbn = "";
        String tcd020SouKbn = "";
        String tcd020ShgKbn = "";
        if (tcdMdl != null) {
            Time itime = tcdMdl.getTcdIntime();
            UDate idate = null;
            if (itime != null) {
                idate = TimecardBiz.convertForDspTime(UDate.getInstance(itime.getTime()), admConf, true);
                tcd020InHour = String.valueOf(idate.getIntHour());
                tcd020InMinute = String.valueOf(idate.getIntMinute());
            }
            Time otime = tcdMdl.getTcdOuttime();
            UDate odate = null;
            if (otime != null) {
                odate = TimecardBiz.convertForDspTime(UDate.getInstance(otime.getTime()), admConf, false);
                if (UDate.getInstance(itime.getTime()).compareDateYMDHM(UDate.getInstance(otime.getTime())) == UDate.SMALL) {
                    tcd020OutHour = String.valueOf(odate.getIntHour() + 24);
                } else {
                    tcd020OutHour = String.valueOf(odate.getIntHour());
                }
                tcd020OutMinute = String.valueOf(odate.getIntMinute());
            }
            tcd020Biko = tcdMdl.getTcdBiko();
            tcd020HolKbn = String.valueOf(tcdMdl.getTcdHolkbn());
            tcd020HolValue = NullDefault.getString(tcdMdl.getTcdHolother(), "");
            if (tcdMdl.getTcdHolcnt() != null) {
                tcd020HolDays = tcdMdl.getTcdHolcnt().toString();
            } else {
                tcd020HolDays = "";
            }
            tcd020ChkKbn = tcdMdl.getTcdChkkbn() != null ? String.valueOf(tcdMdl.getTcdChkkbn()) : "";
            tcd020SouKbn = tcdMdl.getTcdSoukbn() != null ? String.valueOf(tcdMdl.getTcdSoukbn()) : "";
            tcd020ShgKbn = tcdMdl.getTcdShgkbn() != null ? String.valueOf(tcdMdl.getTcdShgkbn()) : "";
        } else {
            TcdPriConfModel priMdl = tBiz.getTcdPriConfModel(sessionUsrSid, con);
            tcd020InHour = String.valueOf(priMdl.getTpcInHour());
            tcd020InMinute = String.valueOf(priMdl.getTpcInMin());
            tcd020OutHour = String.valueOf(priMdl.getTpcOutHour());
            tcd020OutMinute = String.valueOf(priMdl.getTpcOutMin());
        }
        form.setTcd020InHour(NullDefault.getString(form.getTcd020InHour(), tcd020InHour));
        form.setTcd020InMinute(NullDefault.getString(form.getTcd020InMinute(), tcd020InMinute));
        form.setTcd020OutHour(NullDefault.getString(form.getTcd020OutHour(), tcd020OutHour));
        form.setTcd020OutMinute(NullDefault.getString(form.getTcd020OutMinute(), tcd020OutMinute));
        form.setTcd020Biko(NullDefault.getString(form.getTcd020Biko(), tcd020Biko));
        form.setTcd020HolKbn(NullDefault.getString(form.getTcd020HolKbn(), tcd020HolKbn));
        form.setTcd020HolValue(NullDefault.getString(form.getTcd020HolValue(), tcd020HolValue));
        form.setTcd020HolDays(NullDefault.getString(form.getTcd020HolDays(), tcd020HolDays));
        form.setTcd020ChkKbn(NullDefault.getString(form.getTcd020ChkKbn(), tcd020ChkKbn));
        form.setTcd020SouKbn(NullDefault.getString(form.getTcd020SouKbn(), tcd020SouKbn));
        form.setTcd020ShgKbn(NullDefault.getString(form.getTcd020ShgKbn(), tcd020ShgKbn));
        form.setTcd020ChkKbnFlag(!"".equals(form.getTcd020ChkKbn()));
        form.setTcd020SouKbnFlag(!"".equals(form.getTcd020SouKbn()));
        form.setTcd020ShgKbnFlag(!"".equals(form.getTcd020ShgKbn()));
        TcdPrjWorktimeDao tcdPrjDao = new TcdPrjWorktimeDao(con);
        List<TcdPrjWorktimeModel> prjWorkTimeList = tcdPrjDao.select(sessionUsrSid, udate);
        List<Tcd020PrjWorkTimeInputModel> workTimeList = new ArrayList<Tcd020PrjWorkTimeInputModel>();
        for (int i = 0; i < 5; i++) {
            String workTime = "";
            String prjSid = "";
            if (prjWorkTimeList.size() > i) {
                TcdPrjWorktimeModel model = prjWorkTimeList.get(i);
                workTime = NullDefault.getString(String.valueOf(model.getTcdWorkTime()), workTime);
                prjSid = NullDefault.getString(String.valueOf(model.getPrjSid()), prjSid);
            }
            Tcd020PrjWorkTimeInputModel workTimeInput = new Tcd020PrjWorkTimeInputModel();
            workTimeInput.setTcd020PrjSid(prjSid);
            workTimeInput.setTcd020PrjWorkTime(workTime);
            workTimeList.add(workTimeInput);
        }
        form.setTcd020WorkTimes(workTimeList);
    }

    /**
     * <br>[�@  �\] ����\��(����)
     * <br>[��  ��]
     * <br>[��  �l]
     *
     * @param map �}�b�v
     * @param form �t�H�[��
     * @param req ���N�G�X�g
     * @param res ���X�|���X
     * @param con �R�l�N�V����
     * @throws SQLException SQL���s����O
     */
    private void __doInitDatas(ActionMapping map, Tcd020Form form, HttpServletRequest req, HttpServletResponse res, Connection con) throws SQLException {
        String[] days = form.getSelectDay();
        HttpSession session = req.getSession();
        BaseUserModel usModel = (BaseUserModel) session.getAttribute(GSConst.SESSION_KEY);
        int sessionUsrSid = usModel.getUsrsid();
        TimecardBiz tBiz = new TimecardBiz();
        TcdAdmConfModel admConf = tBiz.getTcdAdmConfModel(sessionUsrSid, con);
        setTimeCombo(form, sessionUsrSid, con);
        setProjectCombo(form, sessionUsrSid, con);
        int userSid = Integer.parseInt(form.getUsrSid());
        CmnUsrmInfDao uiDao = new CmnUsrmInfDao(con);
        CmnUsrmInfModel uiMdl = uiDao.select(userSid);
        form.setTcd020Name(uiMdl.getUsiSei() + " " + uiMdl.getUsiMei());
        int year = Integer.parseInt(form.getYear());
        int month = Integer.parseInt(form.getMonth());
        String editDate = getMultiDaysString(year, month, days, GSConstTimecard.DAYS_SEP, admConf.getTacSimebi(), 0);
        form.setTcd020Date(editDate);
        TcdPriConfModel priConf = tBiz.getTcdPriConfModel(sessionUsrSid, con);
        String tcd020InHour = String.valueOf(priConf.getTpcInHour());
        String tcd020InMinute = String.valueOf(priConf.getTpcInMin());
        String tcd020OutHour = String.valueOf(priConf.getTpcOutHour());
        String tcd020OutMinute = String.valueOf(priConf.getTpcOutMin());
        String tcd020Biko = null;
        String tcd020HolKbn = "0";
        String tcd020ChkKbn = "";
        String tcd020SouKbn = "";
        String tcd020ShgKbn = "";
        String tcd020HolValue = null;
        String tcd020HolDays = null;
        form.setTcd020InHour(NullDefault.getString(form.getTcd020InHour(), tcd020InHour));
        form.setTcd020InMinute(NullDefault.getString(form.getTcd020InMinute(), tcd020InMinute));
        form.setTcd020OutHour(NullDefault.getString(form.getTcd020OutHour(), tcd020OutHour));
        form.setTcd020OutMinute(NullDefault.getString(form.getTcd020OutMinute(), tcd020OutMinute));
        form.setTcd020Biko(NullDefault.getString(form.getTcd020Biko(), tcd020Biko));
        form.setTcd020HolKbn(NullDefault.getString(form.getTcd020HolKbn(), tcd020HolKbn));
        form.setTcd020HolValue(NullDefault.getString(form.getTcd020HolValue(), tcd020HolValue));
        form.setTcd020HolDays(NullDefault.getString(form.getTcd020HolDays(), tcd020HolDays));
        form.setTcd020ChkKbn(NullDefault.getString(form.getTcd020ChkKbn(), tcd020ChkKbn));
        form.setTcd020SouKbn(NullDefault.getString(form.getTcd020SouKbn(), tcd020SouKbn));
        form.setTcd020ShgKbn(NullDefault.getString(form.getTcd020ShgKbn(), tcd020ShgKbn));
        form.setTcd020ChkKbnFlag(!"".equals(form.getTcd020ChkKbn()));
        form.setTcd020SouKbnFlag(!"".equals(form.getTcd020SouKbn()));
        form.setTcd020ShgKbnFlag(!"".equals(form.getTcd020ShgKbn()));
        TcdPrjWorktimeDao tcdPrjDao = new TcdPrjWorktimeDao(con);
        List<Tcd020PrjWorkTimeInputModel> workTimeList = new ArrayList<Tcd020PrjWorkTimeInputModel>();
        for (int i = 0; i < 5; i++) {
            String workTime = "";
            String prjSid = "";
            Tcd020PrjWorkTimeInputModel workTimeInput = new Tcd020PrjWorkTimeInputModel();
            workTimeInput.setTcd020PrjSid(prjSid);
            workTimeInput.setTcd020PrjWorkTime(workTime);
            workTimeList.add(workTimeInput);
        }
        form.setTcd020WorkTimes(workTimeList);
    }

    /**
     * @param form
     * @param sessionUsrSid
     * @param con
     * @throws SQLException
     */
    private void setProjectCombo(Tcd020Form form, int sessionUsrSid, Connection con) throws SQLException {
        ProjectSearchDao pjSearchDao = new ProjectSearchDao(con);
        ProjectSearchModel search = new ProjectSearchModel();
        search.setGetKbn(ProjectSearchModel.GET_BELONG);
        search.setUserSid(sessionUsrSid);
        search.setPrjMyKbn(GSConstProject.KBN_MY_PRJ_DEF);
        search.setOrder(GSConstProject.SORT_PRJECT_ID);
        List<ProjectItemModel> projectList = pjSearchDao.getAllProjectList(search);
        List<LabelValueBean> projectLabelList = new ArrayList<LabelValueBean>(projectList.size() + 1);
        projectLabelList.add(new LabelValueBean("", ""));
        for (ProjectItemModel project : projectList) {
            LabelValueBean label = new LabelValueBean(project.getProjectId() + ":" + project.getProjectRyakuName(), String.valueOf(project.getProjectSid()));
            projectLabelList.add(label);
        }
        form.setTcd020ProjectLabel(projectLabelList);
    }

    /**
     * �\���N����ƒ��ߓ��ҏW��t�𐶐����܂��B
     * <br>[�@  �\]
     * <br>[��  ��]
     * <br>[��  �l]
     * @param year �\���N
     * @param month �\����
     * @param day �ҏW��
     * @param sime ���ߓ�
     * @return UDate �ҏW��t
     */
    public UDate getEditDate(int year, int month, int day, int sime) {
        UDate ret = new UDate();
        ret.setDate(year, month, day);
        ret.setZeroHhMmSs();
        if (sime == GSConstTimecard.TIMECARD_LIMITDAY[5]) {
            return ret;
        } else {
            int fday = sime - 1;
            if (sime >= day) {
                ret.addMonth(1);
            }
            return ret;
        }
    }

    /**
     * YYYY�NMM��DD,DD,DD,DD��`���̕�����𐶐�����B
     * <br>[�@  �\]
     * <br>[��  ��]
     * <br>[��  �l]
     * @param year �N
     * @param month ��
     * @param days ��z��
     * @param sep ��؂蕶��
     * @param sime ���ߓ�
     * @param brcount ��s���錏��
     * @return String
     */
    public String getMultiDaysString(int year, int month, String[] days, String sep, int sime, int brcount) {
        String ret = "";
        UDate udate = getEditDate(year, month, Integer.parseInt(days[0]), sime);
        log__.debug("�ҏW��[0]==>" + udate.toString());
        StringBuffer buf = new StringBuffer();
        buf.append(UDateUtil.getYymJ(udate));
        int day = 0;
        for (int i = 0; i < days.length; i++) {
            day = Integer.parseInt(days[i]);
            UDate subDate = new UDate();
            subDate = getEditDate(year, month, day, sime);
            log__.debug("�ҏW��==>" + subDate.toString());
            if (udate.compareDateYM(subDate) != UDate.EQUAL) {
                buf.delete(buf.lastIndexOf(sep, buf.length()), buf.length());
                buf.append("�� ");
                buf.append(UDateUtil.getYymJ(subDate));
                udate = subDate;
            }
            if (brcount > 0 && i == 0) {
                buf.append("<br>");
            }
            buf.append(StringUtil.toDecFormat(String.valueOf(day), "00"));
            buf.append(sep);
            if (brcount > 0 && (i + 1) % brcount == 0) {
                buf.append("<br>");
            }
        }
        buf.delete(buf.lastIndexOf(sep, buf.length()), buf.length());
        buf.append("��");
        ret = buf.toString();
        return ret;
    }

    /**
     *<br>[�@  �\]���X�g�{�b�N�X�p���ԃ��x���̐���
     *<br>[��  ��]
     *<br>[��  �l]
     * @return ���ԃ��x��
     */
    public static ArrayList<LabelValueBean> getHourLavel() {
        ArrayList<LabelValueBean> labelList = new ArrayList<LabelValueBean>();
        labelList.add(new LabelValueBean("", "-1"));
        for (int hour = 0; hour < 24; hour++) {
            labelList.add(new LabelValueBean(hour + "��", String.valueOf(hour)));
        }
        return labelList;
    }

    /**
     *<br>[�@  �\]���X�g�{�b�N�X�p�����x���̐���
     *<br>[��  ��]
     *<br>[��  �l]
     * @return �����x��
     */
    public static ArrayList<LabelValueBean> getMinuteLavel() {
        ArrayList<LabelValueBean> labelList = new ArrayList<LabelValueBean>();
        labelList.add(new LabelValueBean("", "-1"));
        for (int hm = 0; hm < 6; hm++) {
            labelList.add(new LabelValueBean(String.valueOf(hm), String.valueOf(hm)));
        }
        return labelList;
    }

    /**
     * ���ԃR���{�@���F�������ꂼ�ꐶ�����܂��B
     * <br>[�@  �\]
     * <br>[��  ��]
     * <br>[��  �l]
     * @param form �t�H�[��
     * @param usrSid ���[�USID
     * @param con �R�l�N�V����
     * @throws SQLException SQL���s����O
     */
    public void setTimeCombo(Tcd020Form form, int usrSid, Connection con) throws SQLException {
        TimecardBiz tBiz = new TimecardBiz();
        TcdAdmConfModel admMdl = tBiz.getTcdAdmConfModel(usrSid, con);
        form.setTcd020HourLavel(tBiz.getHourLabelList());
        form.setTcd020MinuteLavel(tBiz.getMinLabelList(admMdl));
    }

    /**
     * �^�C���J�[�h��o�^�E�X�V���܂�
     * <br>[�@  �\]��f�[�^������ꍇ��update �����ꍇ��insert���܂�
     * <br>[��  ��]
     * <br>[��  �l]
     * @param form �t�H�[��
     * @param usrSid �X�V�҃��[�USID
     * @param con �R�l�N�V����
     * @return int �X�V����
     * @throws SQLException SQL���s����O
     */
    public int updateTcdTcdata(Tcd020Form form, int usrSid, Connection con) throws SQLException {
        int ret = 0;
        TcdTcdataModel tcMdl = new TcdTcdataModel();
        UDate sysDate = new UDate();
        TimecardBiz tcBiz = new TimecardBiz();
        TcdAdmConfModel admConf = tcBiz.getTcdAdmConfModel(usrSid, con);
        tcMdl.setUsrSid(Integer.parseInt(form.getUsrSid()));
        UDate tcdDate = new UDate();
        tcdDate.setYear(Integer.parseInt(form.getYear()));
        tcdDate.setMonth(Integer.parseInt(form.getMonth()));
        if (!form.getTcd020InHour().equals("-1") && !form.getTcd020InMinute().equals("-1")) {
            tcdDate.setHour(Integer.parseInt(form.getTcd020InHour()));
            tcdDate.setMinute(Integer.parseInt(form.getTcd020InMinute()));
            tcdDate.setSecond(0);
            Time inTime = new Time(tcdDate.getTime());
            tcMdl.setTcdIntime(inTime);
        }
        if (!form.getTcd020OutHour().equals("-1") && !form.getTcd020OutMinute().equals("-1")) {
            tcdDate.setHour(Integer.parseInt(form.getTcd020OutHour()));
            tcdDate.setMinute(Integer.parseInt(form.getTcd020OutMinute()));
            tcdDate.setSecond(0);
            Time outTime = new Time(tcdDate.getTime());
            tcMdl.setTcdOuttime(outTime);
        }
        tcMdl.setTcdStatus(GSConstTimecard.TCD_STATUS_EDIT);
        tcMdl.setTcdBiko(form.getTcd020Biko());
        tcMdl.setTcdHolkbn(NullDefault.getInt(form.getTcd020HolKbn(), GSConstTimecard.HOL_KBN_UNSELECT));
        tcMdl.setTcdHolother(NullDefault.getString(form.getTcd020HolValue(), ""));
        if (!StringUtil.isNullZeroStringSpace(form.getTcd020HolDays())) {
            tcMdl.setTcdHolcnt(new BigDecimal(form.getTcd020HolDays()));
            log__.debug("�x���==>" + tcMdl.getTcdHolcnt().toString());
        }
        tcMdl.setTcdChkkbn(null);
        if (form.isTcd020ChkKbnFlag()) {
            tcMdl.setTcdChkkbn(NullDefault.getBigDecimal(form.getTcd020ChkKbn(), null));
        }
        tcMdl.setTcdSoukbn(null);
        if (form.isTcd020SouKbnFlag()) {
            tcMdl.setTcdSoukbn(NullDefault.getBigDecimal(form.getTcd020SouKbn(), null));
        }
        tcMdl.setTcdShgkbn(null);
        if (form.isTcd020SouKbnFlag()) {
            tcMdl.setTcdShgkbn(NullDefault.getBigDecimal(form.getTcd020ShgKbn(), null));
        }
        tcMdl.setTcdAuid(usrSid);
        tcMdl.setTcdAdate(sysDate);
        tcMdl.setTcdEuid(usrSid);
        tcMdl.setTcdEdate(sysDate);
        List<TcdPrjWorktimeModel> workTimeList = new ArrayList<TcdPrjWorktimeModel>(form.getTcd020WorkTimes().size());
        for (Tcd020PrjWorkTimeInputModel inputModel : form.getTcd020WorkTimes()) {
            if (inputModel.getTcd020PrjSid() == null || inputModel.getTcd020PrjSid().length() == 0) {
                continue;
            }
            TcdPrjWorktimeModel model = new TcdPrjWorktimeModel();
            model.setUsrSid(usrSid);
            model.setPrjSid(Integer.parseInt(inputModel.getTcd020PrjSid()));
            model.setTcdWorkTime(new BigDecimal(inputModel.getTcd020PrjWorkTime()));
            model.setTcdAuid(usrSid);
            model.setTcdAdate(sysDate);
            model.setTcdEuid(usrSid);
            model.setTcdEdate(sysDate);
            workTimeList.add(model);
        }
        ArrayList<String> dayList = new ArrayList<String>();
        if (StringUtil.isNullZeroString(form.getEditDay())) {
            log__.debug("�����X�V");
            String[] days = form.getSelectDay();
            for (int i = 0; i < days.length; i++) {
                dayList.add(days[i]);
            }
        } else {
            log__.debug("�P�̍X�V");
            dayList.add(form.getEditDay());
        }
        int year = Integer.parseInt(form.getYear());
        int month = Integer.parseInt(form.getMonth());
        int sime = admConf.getTacSimebi();
        TcdTcdataDao tcDao = new TcdTcdataDao(con);
        TcdPrjWorktimeDao tcdPrjDao = new TcdPrjWorktimeDao(con);
        for (int j = 0; j < dayList.size(); j++) {
            tcdDate = getEditDate(year, month, Integer.parseInt(dayList.get(j)), sime);
            tcMdl.setTcdDate(tcdDate);
            ret = tcDao.update(tcMdl);
            if (ret < 1) {
                tcDao.insert(tcMdl);
            }
            tcdPrjDao.delete(tcMdl.getUsrSid(), tcdDate);
            for (TcdPrjWorktimeModel workTimeModel : workTimeList) {
                workTimeModel.setTcdDate(tcdDate);
                tcdPrjDao.insert(workTimeModel);
            }
        }
        return ret;
    }
}
