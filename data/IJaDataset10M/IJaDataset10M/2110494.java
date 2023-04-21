package com.hk.svr;

import java.util.List;
import com.hk.bean.CmpUnionFeed;
import com.hk.bean.CmpUnionNotice;
import com.hk.bean.CmpUnionReq;

/**
 * 联盟消息中心(请求、通知、动态)
 * 
 * @author akwei
 */
public interface CmpUnionMessageService {

    /**
	 * 创建请求
	 * 
	 * @param cmpUnionReq
	 */
    void createCmpUnionReq(CmpUnionReq cmpUnionReq);

    /**
	 * 删除请求
	 * 
	 * @param reqid
	 */
    void deleteCmpUnionReq(long reqid);

    /**
	 * 统计联盟请求数量
	 * 
	 * @param uid 联盟id
	 * @param dealflg 为-1时，忽略此条件
	 * @return
	 */
    int countCmpUnionReqByUid(long uid, byte dealflg);

    /**
	 * 获取联盟请求记录
	 * 
	 * @param uid 联盟id
	 * @param dealflg 为-1时，忽略此条件
	 * @param begin
	 * @param size
	 * @return
	 */
    List<CmpUnionReq> getCmpUnionReqListByUid(long uid, byte dealflg, int begin, int size);

    /**
	 * 创建动态
	 * 
	 * @param cmpUnionFeed
	 */
    void createCmpUnionFeed(CmpUnionFeed cmpUnionFeed);

    /**
	 * 创建通知
	 * 
	 * @param cmpUnionNotice
	 */
    void createCmpUnionNotice(CmpUnionNotice cmpUnionNotice);

    /**
	 * 统计通知数量
	 * 
	 * @param uid
	 * @param readflg 通知状态已读，未读 <0时忽略此条件
	 * @return
	 */
    int countCmpUnionNoticeByUid(long uid, byte readflg);

    /**
	 * 获取动态数据
	 * 
	 * @param uid
	 * @param begin
	 * @param size
	 * @return
	 */
    List<CmpUnionFeed> getCmpUnionFeedListByUid(long uid, int begin, int size);

    /**
	 * 获取通知数据
	 * 
	 * @param uid
	 * @param readflg通知状态已读，未读 <0时忽略此条件
	 * @param begin
	 * @param size
	 * @return
	 */
    List<CmpUnionNotice> getCmpUnionNoticeList(long uid, byte readflg, int begin, int size);

    CmpUnionReq getCmpUnionReq(long reqid);

    void updateCmpUnionReqDeaded(long reqid);

    void updateCmpUnionNoticeReaded(long noticeId);
}
