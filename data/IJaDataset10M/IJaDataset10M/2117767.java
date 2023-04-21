package com.techstar.dmis.common;

/**
 * 调度专业常量类
 * 
 * 
 * 
 * @author yjb
 * 
 */
public class DispatchConstants {

    public static String SYSTEM_NAME = "海淀";

    public static String BUSINESS_AREA_EXECUTED = "已执行";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_NEW = "草稿";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_MODIFY = "修改中";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_COURSIGNING = "会签中";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_APPROVING = "审批中";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_APPROVED = "已审批";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_SENTED = "已下发";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_EXECUTED = "已执行";

    public static String DdEnsuringpsmanage_BUSINESS_AREA_KEEPONED = "已归档";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_NEW = "1";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_MODIFY = "1m";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_COURSIGN = "2";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_APPROVE = "3";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_SENT = "4";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_EXEC = "5";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_KEEPON = "6";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_STATUS_OVER = "-1";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_PROCESSDIFINITION = "DdEnsuringpsmanage_city";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_SENT_TRANSIT_GO = "流转到执行";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_SENT_TRANSIT_REBACK = "撤回审批";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_COURSIGN_TRANSIT_YES = "流转到审批";

    public static String DdEnsuringpsmanage_WORKFLOW_AREA_COURSIGN_TRANSIT_NO = "驳回重填";

    public static String DdDayPlan_WORKFLOW_AREA_PROCESSDIFINITION = "DayPlan-Area";

    public static String DdDayPlan_WORKFLOW_AREA_SentIsType_TRANSIT_LS = "临时停电";

    public static String DdDayPlan_WORKFLOW_AREA_SentIsType_TRANSIT_LS_NO = "非临时停电";

    public static String DdDayPlan_WORKFLOW_AREA_SentIsType_TRANSIT_LS_YES = "临时停电";

    public static String DdDayPlan_WORKFLOW_AREA_SentIsType_TRANSIT_LSorOther_NO = "否";

    public static String DdDayPlan_WORKFLOW_AREA_SentIsType_TRANSIT_LSorOther_YES = "是";

    public static String DdDayPlan_WORKFLOW_AREA_APPROVE_TRANSIT = "调度通过";

    public static String DdDayPlan_WORKFLOW_AREA_APPROVE_TRANSIT_YES = "通过";

    public static String DdDayPlan_WORKFLOW_AREA_APPROVE_TRANSIT_NO = "不通过";

    public static String DdDayPlan_WORKFLOW_AREA_ISALLOVER_TRANSIT = "通知与核准完毕";

    public static String DdDayPlan_WORKFLOW_AREA_ISALLOVER_TRANSIT_YES = "通过";

    public static String DdDayPlan_WORKFLOW_AREA_ISALLOVER_TRANSIT_NO = "未通过";

    public static String DdDayPlan_WORKFLOW_AREA_COURTERSIGN_TRANSIT_YES = "通过";

    public static String DdDayPlan_WORKFLOW_AREA_COURTERSIGN_TRANSIT_NO = "驳回";

    public static String DdDayPlan_WORKFLOW_AREA_ARRANGE_TRANSIT_NEED = "需要其他调度审核";

    public static String DdDayPlan_WORKFLOW_AREA_ARRANGE_TRANSIT_NO_NEED = "不需要其他调度审核";

    public static String DdDayPlan_WORKFLOW_AREA_ARRANGE_TRANSIT_REBACK = "驳回";

    public static String DdDayPlan_WORKFLOW_AREA_SENT_TRANSIT_THIS = "调度权限属于本单位的计划";

    public static String DdDayPlan_WORKFLOW_AREA_SENT_TRANSIT_OTHER = "调度权限属于其他单位的计划";

    public static String DdDoutageplan_WORKFLOW_AREA_STATUS_APPROVE_TRANSIT_YES = "401";

    public static String DdDoutageplan_WORKFLOW_AREA_STATUS_APPROVE_TRANSIT_NO = "402";

    public static String DdDoutageplan_WORKFLOW_BOROUGH_STATUS_APPROVE_TRANSIT_YES = "403";

    public static String DdDoutageplan_WORKFLOW_BOROUGH_STATUS_APPROVE_TRANSIT_NO = "404";

    public static String DdDoutageplan_WORKFLOW_STATUS_NEW = "1";

    public static String DdDoutageplan_WORKFLOW_STATUS_ARRAY = "2";

    public static String DdDoutageplan_WORKFLOW_STATUS_COURSIGN = "3";

    public static String DdDoutageplan_WORKFLOW_STATUS_SENT = "4";

    public static String DdDoutageplan_WORKFLOW_STATUS_EXEC = "5";

    public static String DdDoutageplan_WORKFLOW_STATUS_KEEPON = "6";

    public static String DdDoutageplan_WORKFLOW_STATUS_check = "7";

    public static String DdDoutageplan_WORKFLOW_STATUS_message = "8";

    public static String DdDoutageplan_WORKFLOW_STATUS_OVER = "-1";

    /** *停电日计划开始** */
    public static String AREA_DdDoutageplan_NEW = "草稿";

    public static String AREA_DdDoutageplan_UNARRAY = "整理驳回";

    public static String AREA_DdDoutageplan_UNAPPROVEED = "已驳回";

    public static String AREA_DdDoutageplan_AUDITING = "审批中";

    public static String AREA_DdDoutageplan_APPROVEED = "已批准";

    public static String AREA_DdDoutageplan_DISPATCHED = "已下发";

    public static String AREA_DdDoutageplan_ORDERED = "已下令";

    public static String AREA_DdDoutageplan_PERFORMED = "已执行";

    public static String AREA_DdDoutageplan_CANCLED = "已取消";

    public static String AREA_DdDoutageplan_PIGEONHOLED = "已归档";

    public static String AREA_DdDoutageplan_FROMSOURCE_MONTH = "月计划";

    public static String AREA_DdDoutageplan_FROMSOURCE_ACCIDENT = "故障简报";

    public static String AREA_DdDoutageplan_COMMON = "正常";

    public static String AREA_DdDoutageplan_TEMPERORY = "调度员填写";

    public static String AREA_DdDoutageplan_RANGE = "市调";

    public static String DdDayPlan_WORKFLOW_AREA_TIDY = "整理审核";

    public static String DdDayPlan_WORKFLOW_AREA_COURSIGNING = "会签中";

    public static String DdDayPlan_WORKFLOW_AREA_APPROVE = "批准日计划";

    public static String DdDayPlan_WORKFLOW_AREA_EXE = "日计划执行";

    public static String DdDayPlan_WORKFLOW_AREA_KEEPON = "日计划归档";

    /** *操作票常量 开始** */
    public static String ddSeqbillDetailStepStatus_EXE = "已执行";

    public static String ddSwitchSeqbill_NEW = "草稿";

    public static String ddSeqbillDetailStepStatus_MODIFY = "修改中";

    public static String ddSeqbillDetailStepStatus_APPROVE = "审核中";

    public static String ddSeqbillDetailStepStatus_APPROVED = "已审核";

    public static String ddSwitchSeqbill_CANCEL = "作废";

    public static String ddSwitchSeqbill_EXE = "已执行";

    public static String ddSeqbillDetailSetpstatus_STOP = "终止";

    public static String ddSeqbillDetailSetpstatus_SOURCE_DAYPLAN = "日计划";

    public static String ddSeqbillDetailSetpstatus_SOURCE_ACCIDENT = "故障简报";

    public static String DdSwitchseqBill_WORKFLOW_AREA_PROCESSDIFINITION = "DDswitchBill_city";

    public static String DdSwitchseqBill_WORKFLOW_AREA_APPROVE_TRANSIT_YES = "审核通过";

    public static String DdSwitchseqBill_WORKFLOW_AREA_APPROVE_TRANSIT_NO = "审核未通过";

    public static String DdSwitchseqBill_WORKFLOW_AREA_EXEC_TRANSIT_YES = "结束";

    public static String DdSwitchseqBill_WORKFLOW_AREA_EXEC_TRANSIT_NO = "未执行";

    public static String DdSwitchseqBill_WORKFLOW_AREA_APPROVE_TRANSIT_YES_NUM = "1";

    public static String DdSwitchseqBill_WORKFLOW_AREA_APPROVE_TRANSIT_NO_NUM = "-1";

    public static String DdSwitchseqBill_WORKFLOW_AREA_APPROVE = "操作票审核";

    public static String DdSwitchseqBill_WORKFLOW_AREA_MODIFY = "修改票执行";

    public static String DdSwitchseqBill_WORKFLOW_AREA_EXEC = "操作票执行";

    public static String DdSwitchseqBill_WORKFLOW_AREA_STOP_YES_NUM = "302";

    public static String DdSwitchseqBill_WORKFLOW_AREA_EXEC_YES_NUM = "301";

    public static String DdSwitchseqBill_WORKFLOW_AREA_EXEC_NO_NUM = "303";

    public static String DdSwitchseqBill_WORKFLOW_AREA_STATUS_NEW = "1";

    public static String DdSwitchseqBill_WORKFLOW_AREA_STATUS_MODIFY = "101";

    public static String DdSwitchseqBill_WORKFLOW_AREA_STATUS_APPROVE = "2";

    public static String DdSwitchseqBill_WORKFLOW_AREA_STATUS_EXEC = "3";

    public static String DdSwitchseqBill_WORKFLOW_AREA_STATUS_OVER = "-1";

    /** **** 故障简报***** */
    public static String ddAccidentbrief_resume = "已恢复正常";

    public static String ddAccidentbrief_new = "草稿";

    public static String ddAccidentbrief_auditing = "处理中";

    public static String ddAccidentbrief_send_to_dayplan = "转检修票";

    public static String ddAccidentbrief_Switchseqbill_billsource = "故障简报";

    public static String ddAccidentbrief_WORKFLOW_AREA_STATUS_NEW = "1";

    public static String ddAccidentbrief_WORKFLOW_AREA_STATUS_MODIFY = "1m";

    public static String ddAccidentbrief_WORKFLOW_AREA_STATUS_APPROVE = "2";

    public static String ddAccidentbrief_WORKFLOW_AREA_STATUS_EXE = "3";

    public static String ddAccidentbrief_WORKFLOW_AREA_STATUS_OVER = "-1";

    public static String ddAccidentbrief_WORKFLOW_AREA_PROCESSDIFINITION = "DD_AccidentBrief_city";

    public static String ddAccidentbrief_WORKFLOW_AREA_APPROVE_YES = "审核通过";

    public static String ddAccidentbrief_WORKFLOW_AREA_APPROVE_NO = "驳回";

    public static String ddAccidentbrief_WORKFLOW_AREA_isFull_YES = "是";

    public static String ddAccidentbrief_WORKFLOW_AREA_isFull_GO = "送执行";

    public static String ddAccidentbrief_WORKFLOW_AREA_isFull_BACK = "未完";

    public static String ddCutofftitle_new = "草稿";

    public static String ddCutofftitle_approve = "审批中";

    public static String ddCutofftitle_release = "已发布";

    public static String ddCutofftitle_approve_YES = "已批准";

    public static String ddCutofftitle_approve_NO = "已驳回";

    public static String ddCutofftitle_SEND_TASK = "任务已下发";

    public static String ddCutofftitle_cancled = "已作废";

    public static String ddCutofftitle_WORKFLOW_AREA_PROCESSDIFINITION = "limitLineSeqForm-city";

    public static String ddCutofftitle_WORKFLOW_AREA_APPROVE_TRANSIT_YES = "通过";

    public static String ddCutofftitle_WORKFLOW_AREA_APPROVE_TRANSIT_NO = "未通过";

    public static String ddCutofftitle_WORKFLOW_AREA_ISFULL = "是";

    public static String ddCutofftitle_WORKFLOW_AREA_ISFULL_YES = "汇总表";

    public static String ddCutofftitle_WORKFLOW_AREA_ISFULL_NO = "非汇总表";

    public static String ddCutofftitle_WORKFLOW_STATUS_NEW = "1";

    public static String ddCutofftitle_WORKFLOW_STATUS_MODIFY = "1m";

    public static String ddCutofftitle_WORKFLOW_STATUS_APPROVE = "2";

    public static String ddCutofftitle_WORKFLOW_STATUS_APPROVE_FH = "4";

    public static String ddCutofftitle_WORKFLOW_STATUS_RELEASE = "3";

    public static String ddCutofftitle_WORKFLOW_STATUS_END = "-1";

    public static String ddMoutageplan_new = "草稿";

    public static String ddMoutageplan_merge_success = "Y";

    public static String ddMoutageplan_auditing = "审核中";

    public static String ddMoutageplan_CleanUp = "整理";

    public static String ddMoutageplan_approveed = "已批准";

    public static String ddMoutageplan_approveed_approveed_no = "已驳回";

    public static String ddMoutageplan_performed = "已执行";

    public static String ddMoutageplan_cancled = "已取消";

    public static String ddMoutageplan_unfinish = "未完成";

    public static String ddMoutageplan_finish = "已完成";

    public static String ddMoutageplan_pigeonholed = "已归档";

    public static String ddMoutageplan_plansourcetype_merge = "合并";

    public static String ddMoutageplan_plansourcetype_mplan = "月计划";

    public static String ddMoutagepla_fdispatchrange_city = "市调";

    public static String ddMoutagepla_fdispatchrange = "区调";

    public static String ddMoutageplan_WORKFLOW_PROCESSDIFINITION = "MonthPlan-Area";

    public static String ddMoutageplan_WORKFLOW_transition2 = "连接线3";

    public static String ddMoutageplan_WORKFLOW_APPROVE_YES = "通过";

    public static String ddMoutageplan_WORKFLOW_APPROVE_NO = "驳回";

    public static String ddMoutageplan_WORKFLOW_APPROVE_TRANSIT_YES = "通过";

    public static String ddMoutageplan_WORKFLOW_APPROVE_TRANSIT_NO = "不通过";

    public static String ddMoutageplan_WORKFLOW_LOCAL_DEPT = "本单位";

    public static String ddMoutageplan_WORKFLOW_DEPT = "非本单位";

    public static String ddMoutageplan_WORKFLOW_FDispatchRange_CITY = "市调";

    public static String ddMoutageplan_WORKFLOW_FDispatchRange_AREA = "区调";

    public static String ddMoutageplan_WORKFLOW_SjSpecArrange_TRANSIT_YES = "通过";

    public static String ddMoutageplan_WORKFLOW_SjSpecArrange_TRANSIT_NO = "驳回";

    public static String ddMoutageplan_WORKFLOW_IsDept_TRANSIT_THIS = "本单位";

    public static String ddMoutageplan_WORKFLOW_IsDept_TRANSIT_OTHER = "非本单位";

    public static String ddMoutageplan_WORKFLOW_IsDept2_TRANSIT_THIS = "本单位";

    public static String ddMoutageplan_WORKFLOW_IsDept2_TRANSIT_OTHER = "外单位";

    public static String ddMoutageplan_WORKFLOW_SjSpecApprove_TRANSIT_YES = "通过";

    public static String ddMoutageplan_WORKFLOW_SjSpecApprove_TRANSIT_NO = "驳回";

    public static String ddMoutageplan_WORKFLOW_Approve_TRANSIT_YES = "通过";

    public static String ddMoutageplan_WORKFLOW_Approve_TRANSIT_NO = "驳回";

    public static String ddMoutageplan_WORKFLOW_RANGE_TRANSIT_THIS = "本单位调度";

    public static String ddMoutageplan_WORKFLOW_RANGE_TRANSIT_OTHER = "其他调度";

    public static String ddMoutageplan_WORKFLOW_STATUS_NEW = "1";

    public static String ddMoutageplan_WORKFLOW_STATUS_Modify = "1m";

    public static String ddMoutageplan_WORKFLOW_STATUS_CleanUp = "2";

    public static String ddMoutageplan_WORKFLOW_STATUS_SentResult = "3";

    public static String ddMoutageplan_WORKFLOW_STATUS_ApproveAndSend = "4";

    public static String ddMoutageplan_WORKFLOW_STATUS_CleanUpResult = "5";

    public static String ddMoutageplan_WORKFLOW_STATUS_end = "-1";

    /** **********旬计划开始*********** */
    public static String DdWoutageplan_WORKFLOW_PROCESSDIFINITION = "DdWoutageplan-Area";

    public static String DdWoutageplan_WORKFLOW_SjspecArrange_TRANSIT_YES = "通过";

    public static String DdWoutageplan_WORKFLOW_SjspecArrange_TRANSIT_NO = "不通过";

    public static String DdWoutageplan_WORKFLOW_IsDeptApply_TRANSIT_THIS = "本单位";

    public static String DdWoutageplan_WORKFLOW_IsDeptApply_TRANSIT_OTHER = "非本单位";

    public static String DdWoutageplan_WORKFLOW_SpecDispense_TRANSIT_YES = "本单位调度";

    public static String DdWoutageplan_WORKFLOW_SpecDispense_TRANSIT_YES_S = "本单位调度";

    public static String DdWoutageplan_WORKFLOW_SpecDispense_TRANSIT_NO = "其他单位调度";

    public static String DdWoutageplan_WORKFLOW_IsYes_TRANSIT_YES = "通过";

    public static String DdWoutageplan_WORKFLOW_IsYes_TRANSIT_NO = "不通过";

    public static String DdWoutageplan_WORKFLOW_SjspecApprove_TRANSIT_YES = "通过";

    public static String DdWoutageplan_WORKFLOW_SjspecApprove_TRANSIT_NO = "不通过";

    public static String DdWoutageplan_WORKFLOW_IsFillDeptO_TRANSIT_THIS = "本单位";

    public static String DdWoutageplan_WORKFLOW_IsFillDeptO_TRANSIT_OTHER = "外单位";

    public static String DdWoutageplan_WORKFLOW_IsAllYes_TRANSIT_YES = "通过";

    public static String DdWoutageplan_WORKFLOW_IsAllYes_TRANSIT_NO = "未通过";

    public static String DdWoutageplan_WORKFLOW_IsDeptT_TRANSIT_THIS = "本单位";

    public static String DdWoutageplan_WORKFLOW_IsDeptT_TRANSIT_OTHER = "非本单位";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_NEW = "1";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_CleanUp = "2";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_SentResult = "3";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_ApproveAndSend = "4";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_CleanUpResult = "5";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_check = "6";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_message = "7";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_Modify = "8";

    public static String DdWoutageplan_WORKFLOW_10DAYS_STATUS_OVER = "-1";

    public static String ddWoutageplan_Type = "W_PLAN";

    public static String ddWoutageplan_new = "草稿";

    public static String ddWoutageplan_auditing = "审核中";

    public static String ddWoutageplan_approveed = "已批准";

    public static String ddWoutageplan_approveed_approveed_no = "已驳回";

    public static String ddWoutageplan_performed = "已执行";

    public static String ddWoutageplan_cancled = "已取消";

    public static String ddWoutageplan_unfinish = "未完成";

    public static String ddWoutageplan_finish = "已完成";

    public static String ddWoutageplan_pigeonholed = "已归档";

    public static String CITY_DdDoutageplan_FROMSOURCE_W = "旬计划";

    public static String CITY_Ddwoutageplan_FROMSOURCE_MONTH = "月计划";

    public static String ddWoutageplan_planUser_Status = "未通知";

    public static String ddWoutageplan_plansourcetype_mplan = "月计划";

    public static String ddWoutageplan_fdispatchrange_city = "市调";

    public static String ddWoutageplan_fdispatchrange = "区调";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_PROCESSDIFINITION = "PowerRiskAnalysis_city";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_APPROVE_TRANSIT_YES = "通过";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_APPROVE_TRANSIT_NO = "未通过";

    public static String PowerRiskAnalysis_STATUS_NEW = "草稿";

    public static String PowerRiskAnalysis_STATUS_APPROVE = "审批中";

    public static String PowerRiskAnalysis_STATUS_APPROVE_YES = "已批准";

    public static String PowerRiskAnalysis_STATUS_APPROVE_NO = "已驳回";

    public static String PowerRiskAnalysis_STATUS_RELEASE = "已发布";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_STATUS_NEW = "1";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_STATUS_APPROVE = "2";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_STATUS_MODIFY = "1m";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_STATUS_RELEASE = "3";

    public static String PowerRiskAnalysis_WORKFLOW_AREA_STATUS_END = "-1";

    public static String ddPremethod_new = "草稿";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_NEW = "草稿";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_MODIFY = "修改中";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_COURSIGNING = "会签中";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_APPROVING = "审批中";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_APPROVED = "已审批";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_SENTED = "已下发";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_EXECUTED = "已执行";

    public static String DdEnsuringpsmanage_BUSINESS_CITY_KEEPONED = "已归档";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_NEW = "1";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_MODIFY = "1m";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_COURSIGN = "2";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_APPROVE = "3";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_SENT = "4";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_EXEC = "5";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_KEEPON = "6";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_OVER = "-1";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_PROCESSDIFINITION = "DdEnsuringpsmanage_city";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_SENT_TRANSIT_GO = "流转到执行";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_SENT_TRANSIT_REBACK = "撤回审批";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_COURSIGN_TRANSIT_YES = "流转到审批";

    public static String DdEnsuringpsmanage_WORKFLOW_CITY_COURSIGN_TRANSIT_NO = "驳回重填";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_STATUS_NEW = "1";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_STATUS_APPROVE = "2";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_STATUS_MODIFY = "1m";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_STATUS_RELEASE = "3";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_STATUS_END = "-1";

    public static String DdSwitchseqBill_WORKFLOW_CITY_PROCESSDIFINITION = "DDswitchBill_city";

    public static String DdSwitchseqBill_WORKFLOW_CITY_APPROVE_TRANSIT_YES = "审核通过";

    public static String DdSwitchseqBill_WORKFLOW_CITY_APPROVE_TRANSIT_NO = "审核未通过";

    public static String DdSwitchseqBill_WORKFLOW_CITY_EXEC_TRANSIT_YES = "结束";

    public static String DdSwitchseqBill_WORKFLOW_CITY_EXEC_TRANSIT_NO = "未执行";

    public static String DdSwitchseqBill_WORKFLOW_CITY_APPROVE_TRANSIT_YES_NUM = "1";

    public static String DdSwitchseqBill_WORKFLOW_CITY_APPROVE_TRANSIT_NO_NUM = "-1";

    public static String DdSwitchseqBill_WORKFLOW_CITY_APPROVE = "操作票审核";

    public static String DdSwitchseqBill_WORKFLOW_CITY_MODIFY = "修改票执行";

    public static String DdSwitchseqBill_WORKFLOW_CITY_EXEC = "操作票执行";

    public static String DdSwitchseqBill_WORKFLOW_CITY_STOP_YES_NUM = "302";

    public static String DdSwitchseqBill_WORKFLOW_CITY_EXEC_YES_NUM = "301";

    public static String DdSwitchseqBill_WORKFLOW_CITY_EXEC_NO_NUM = "303";

    public static String DdSwitchseqBill_WORKFLOW_CITY_STATUS_NEW = "1";

    public static String DdSwitchseqBill_WORKFLOW_CITY_STATUS_MODIFY = "101";

    public static String DdSwitchseqBill_WORKFLOW_CITY_STATUS_APPROVE = "2";

    public static String DdSwitchseqBill_WORKFLOW_CITY_STATUS_EXEC = "3";

    public static String DdSwitchseqBill_WORKFLOW_CITY_STATUS_OVER = "-1";

    public static String ddAccidentbrief_WORKFLOW_CITY_STATUS_NEW = "1";

    public static String ddAccidentbrief_WORKFLOW_CITY_STATUS_MODIFY = "1m";

    public static String ddAccidentbrief_WORKFLOW_CITY_STATUS_APPROVE = "2";

    public static String ddAccidentbrief_WORKFLOW_CITY_STATUS_EXE = "3";

    public static String ddAccidentbrief_WORKFLOW_CITY_STATUS_OVER = "-1";

    public static String ddAccidentbrief_WORKFLOW_CITY_PROCESSDIFINITION = "DD_AccidentBrief_city";

    public static String ddAccidentbrief_WORKFLOW_CITY_APPROVE_YES = "审核通过";

    public static String ddAccidentbrief_WORKFLOW_CITY_APPROVE_NO = "驳回";

    public static String ddAccidentbrief_WORKFLOW_CITY_isFull_YES = "是";

    public static String ddAccidentbrief_WORKFLOW_CITY_isFull_GO = "送执行";

    public static String ddAccidentbrief_WORKFLOW_CITY_isFull_BACK = "未完";

    public static String ddCutofftitle_WORKFLOW_CITY_PROCESSDIFINITION = "limitLineSeqForm-city";

    public static String ddCutofftitle_WORKFLOW_CITY_APPROVE_TRANSIT_YES = "通过";

    public static String ddCutofftitle_WORKFLOW_CITY_APPROVE_TRANSIT_NO = "未通过";

    public static String ddCutofftitle_WORKFLOW_CITY_ISFULL = "是";

    public static String ddCutofftitle_WORKFLOW_CITY_ISFULL_YES = "汇总表";

    public static String ddCutofftitle_WORKFLOW_CITY_ISFULL_NO = "非汇总表";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_PROCESSDIFINITION = "PowerRiskAnalysis_city";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_APPROVE_TRANSIT_YES = "通过";

    public static String PowerRiskAnalysis_WORKFLOW_CITY_APPROVE_TRANSIT_NO = "未通过";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_check = "7";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_message = "8";

    public static String CITY_DdDoutageplan_NEW = "草稿";

    public static String CITY_DdDoutageplan_UNARRAY = "整理驳回";

    public static String CITY_DdDoutageplan_UNAPPROVEED = "已驳回";

    public static String CITY_DdDoutageplan_AUDITING = "审批中";

    public static String CITY_DdDoutageplan_APPROVEED = "已批准";

    public static String CITY_DdDoutageplan_DISPATCHED = "已下发";

    public static String CITY_DdDoutageplan_ORDERED = "已下令";

    public static String CITY_DdDoutageplan_PERFORMED = "已执行";

    public static String CITY_DdDoutageplan_CANCLED = "已取消";

    public static String CITY_DdDoutageplan_PIGEONHOLED = "已归档";

    public static String DdDayPlan_WORKFLOW_CITY_PROCESSDIFINITION = "DdDayPlan_city";

    public static String DdDayPlan_WORKFLOW_CITY_SentIsType_TRANSIT_LS = "临时停电";

    public static String DdDayPlan_WORKFLOW_CITY_SentIsType_TRANSIT_LS_YES = "临时停电";

    public static String DdDayPlan_WORKFLOW_CITY_SentIsType_TRANSIT_LS_NO = "检修计划或工程计划";

    public static String DdDayPlan_WORKFLOW_CITY_APPROVE_TRANSIT_YES = "通过";

    public static String DdDayPlan_WORKFLOW_CITY_APPROVE_TRANSIT_NO = "本单位不批准及外单位";

    public static String DdDayPlan_WORKFLOW_CITY_COURTERSIGN_TRANSIT_YES = "通过";

    public static String DdDayPlan_WORKFLOW_CITY_COURTERSIGN_TRANSIT_NO = "未通过";

    public static String DdDayPlan_WORKFLOW_CITY_ARRANGE_TRANSIT_YES = "整理通过";

    public static String DdDayPlan_WORKFLOW_CITY_ARRANGE_TRANSIT_NO = "未通过";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_APPROVE_TRANSIT_YES = "401";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_APPROVE_TRANSIT_NO = "402";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_NEW = "1";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_ARRAY = "2";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_COURSIGN = "3";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_APPROVE = "4";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_SENT = "5";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_EXEC = "6";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_KEEPON = "7";

    public static String DdDoutageplan_WORKFLOW_CITY_STATUS_OVER = "-1";

    public static String CITY_DdDoutageplan_TEMPERORY = "调度员填写";

    public static String CITY_DdDoutageplan_RANGE = "市调";

    public static String CITY_DdDoutageplan_COMMON = "正常";

    public static String CITY_DdDoutageplan_FROMSOURCE_MONTH = "月计划";

    public static String CITY_DdDoutageplan_FROMSOURCE_ACCIDENT = "故障简报";

    public static String AREA_DdDoutageplan_FROMSOURCE_WEEK = "旬计划";
}
