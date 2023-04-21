package kr.godsoft.egovframe.generatorwebapp.comtncntcsystem.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtncntcsystemService.java
 * @Description : Comtncntcsystem Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComtncntcsystemService {

    /**
	 * COMTNCNTCSYSTEM을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtncntcsystemVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComtncntcsystem(ComtncntcsystemVO vo) throws Exception;

    /**
	 * COMTNCNTCSYSTEM을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtncntcsystemVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComtncntcsystem(ComtncntcsystemVO vo) throws Exception;

    /**
	 * COMTNCNTCSYSTEM을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtncntcsystemVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComtncntcsystem(ComtncntcsystemVO vo) throws Exception;

    /**
	 * COMTNCNTCSYSTEM을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtncntcsystemVO
	 * @return 조회한 COMTNCNTCSYSTEM
	 * @exception Exception
	 */
    ComtncntcsystemVO selectComtncntcsystem(ComtncntcsystemVO vo) throws Exception;

    /**
	 * COMTNCNTCSYSTEM 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCNTCSYSTEM 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComtncntcsystemList(ComtncntcsystemVO vo) throws Exception;

    /**
	 * COMTNCNTCSYSTEM 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCNTCSYSTEM 총 갯수
	 * @exception
	 */
    int selectComtncntcsystemListTotCnt(ComtncntcsystemVO vo);
}
