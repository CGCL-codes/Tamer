package com.hand.dao;

import java.util.List;
import com.hand.model.TGeneralMasterDataAdm;

/**
 * 系统名：HCSMobileApp
 * 子系统名：手机后台数据管理DAO接口
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 12, 2011
 */
public interface IMobileBackedDao {

    /**
	 * 根据传入的值和定义的类型获取手机后台数据管理数据列表
	 * 说明：本方法将在程序启动或数据更新时调用，主要用来加载下拉列表中内容
	 * 
	 * @param value 
	 * 			   值
	 * @param dataCode 
	 * 			   类型（1.数据分类 2.语种 3.代理店ID 4.Code）
	 * @return 数据管理数据列表
	 */
    public List<TGeneralMasterDataAdm> getMasterDataByValueAndType(String value, int dataCode);

    /**
	 * 根据传入的值和定义的类型获取手机后台数据管理数据列表
	 * 说明：本方法用来更新数据列表
	 * 
	 * @param value 
	 * 			   值
	 * @return 数据管理数据列表
	 */
    public List<TGeneralMasterDataAdm> getMasterDataByValueAndLang(String value, String langType);

    /**
	 * 新增手机后台数据
	 * 
	 * @param generalMasterDataAdm 手机后台数据
	 * @return 0为失败 否则为成功
	 */
    public int addMobileBacked(TGeneralMasterDataAdm generalMasterDataAdm);

    /**
	 * 根据id获取手机后台信息
	 * 
	 * @param id 
	 * @return 0为失败 否则为成功
	 */
    public TGeneralMasterDataAdm getMobileBackedById(int id);

    /**
	 * 根据DataCode和DataType获取手机后台信息
	 * 
	 * @param dataType 
	 * @param dataCode 
	 * @return 手机后台信息列表
	 */
    public List<TGeneralMasterDataAdm> getMobileBackedById(String dataType, int dataCode);

    /**
	 * 根据语言类型获取手机后台信息
	 * 
	 * @param langTypeC 语言类型
	 * @return 手机后台信息列表
	 */
    public List<TGeneralMasterDataAdm> getMobileBackedListByLang(String langTypeC);

    /**
	 * 删除手机后台数据
	 * 
	 * @param generalMasterDataAdm 
	 * 			   手机后台数据
	 * @return 0为失败 1为成功
	 */
    public int deleteMobileBacked(TGeneralMasterDataAdm generalMasterDataAdm);

    /**
	 * 更新手机后台数据
	 * 
	 * @param generalMasterDataAdm 
	 * 			   手机后台数据
	 * @return 0为失败 1为成功
	 */
    public int updateMobileBacked(TGeneralMasterDataAdm generalMasterDataAdm);

    /**
	 * 根据语言类型和数据类型值获取手机后台数据总记录数
	 * @param value 
	 * 			   数据类型
	 * @param langType 
	 * 			   语言类型
	 * @return 手机后台数据总记录数
	 */
    public int getMasterDataTotal(String value, String langType);

    /**
	 * 根据语言类型和数据类型值获取手机后台数据列表
	 * @param value 
	 * 			   数据类型
	 * @param langType 
	 * 			   语言类型
	 * @param startRow
	 * 			   开始行数
	 * @param pageSize
	 * 			   提取记录条数
	 * @return 手机后台数据列表
	 */
    public List<TGeneralMasterDataAdm> getMasterDataByValueAndLang(String value, String langType, int startRow, int pageSize);
}
