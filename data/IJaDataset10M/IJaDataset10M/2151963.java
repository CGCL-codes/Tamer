package kr.godsoft.egovframe.generatorwebapp.comtnntwrkinfo.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnntwrkinfoService.java
 * @Description : Comtnntwrkinfo Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComtnntwrkinfoService {

    /**
	 * COMTNNTWRKINFO을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnntwrkinfoVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComtnntwrkinfo(ComtnntwrkinfoVO vo) throws Exception;

    /**
	 * COMTNNTWRKINFO을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnntwrkinfoVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComtnntwrkinfo(ComtnntwrkinfoVO vo) throws Exception;

    /**
	 * COMTNNTWRKINFO을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnntwrkinfoVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComtnntwrkinfo(ComtnntwrkinfoVO vo) throws Exception;

    /**
	 * COMTNNTWRKINFO을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnntwrkinfoVO
	 * @return 조회한 COMTNNTWRKINFO
	 * @exception Exception
	 */
    ComtnntwrkinfoVO selectComtnntwrkinfo(ComtnntwrkinfoVO vo) throws Exception;

    /**
	 * COMTNNTWRKINFO 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNNTWRKINFO 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComtnntwrkinfoList(ComtnntwrkinfoVO vo) throws Exception;

    /**
	 * COMTNNTWRKINFO 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNNTWRKINFO 총 갯수
	 * @exception
	 */
    int selectComtnntwrkinfoListTotCnt(ComtnntwrkinfoVO vo);
}
