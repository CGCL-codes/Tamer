package kr.godsoft.egovframe.generatorwebapp.comtnwikmnthngreprt.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnwikmnthngreprtService.java
 * @Description : Comtnwikmnthngreprt Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComtnwikmnthngreprtService {

    /**
	 * COMTNWIKMNTHNGREPRT을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnwikmnthngreprtVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComtnwikmnthngreprt(ComtnwikmnthngreprtVO vo) throws Exception;

    /**
	 * COMTNWIKMNTHNGREPRT을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnwikmnthngreprtVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComtnwikmnthngreprt(ComtnwikmnthngreprtVO vo) throws Exception;

    /**
	 * COMTNWIKMNTHNGREPRT을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnwikmnthngreprtVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComtnwikmnthngreprt(ComtnwikmnthngreprtVO vo) throws Exception;

    /**
	 * COMTNWIKMNTHNGREPRT을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnwikmnthngreprtVO
	 * @return 조회한 COMTNWIKMNTHNGREPRT
	 * @exception Exception
	 */
    ComtnwikmnthngreprtVO selectComtnwikmnthngreprt(ComtnwikmnthngreprtVO vo) throws Exception;

    /**
	 * COMTNWIKMNTHNGREPRT 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNWIKMNTHNGREPRT 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComtnwikmnthngreprtList(ComtnwikmnthngreprtVO vo) throws Exception;

    /**
	 * COMTNWIKMNTHNGREPRT 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNWIKMNTHNGREPRT 총 갯수
	 * @exception
	 */
    int selectComtnwikmnthngreprtListTotCnt(ComtnwikmnthngreprtVO vo);
}
