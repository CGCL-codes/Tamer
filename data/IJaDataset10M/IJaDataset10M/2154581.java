package kr.godsoft.egovframe.generatorwebapp.comtnctsnnmanage.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnctsnnmanageService.java
 * @Description : Comtnctsnnmanage Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComtnctsnnmanageService {

    /**
	 * COMTNCTSNNMANAGE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnctsnnmanageVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComtnctsnnmanage(ComtnctsnnmanageVO vo) throws Exception;

    /**
	 * COMTNCTSNNMANAGE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnctsnnmanageVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComtnctsnnmanage(ComtnctsnnmanageVO vo) throws Exception;

    /**
	 * COMTNCTSNNMANAGE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnctsnnmanageVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComtnctsnnmanage(ComtnctsnnmanageVO vo) throws Exception;

    /**
	 * COMTNCTSNNMANAGE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnctsnnmanageVO
	 * @return 조회한 COMTNCTSNNMANAGE
	 * @exception Exception
	 */
    ComtnctsnnmanageVO selectComtnctsnnmanage(ComtnctsnnmanageVO vo) throws Exception;

    /**
	 * COMTNCTSNNMANAGE 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCTSNNMANAGE 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComtnctsnnmanageList(ComtnctsnnmanageVO vo) throws Exception;

    /**
	 * COMTNCTSNNMANAGE 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCTSNNMANAGE 총 갯수
	 * @exception
	 */
    int selectComtnctsnnmanageListTotCnt(ComtnctsnnmanageVO vo);
}
