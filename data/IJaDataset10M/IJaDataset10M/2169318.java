package kr.godsoft.egovframe.generatorwebapp.comtnscrap.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnscrapService.java
 * @Description : Comtnscrap Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComtnscrapService {

    /**
	 * COMTNSCRAP을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnscrapVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComtnscrap(ComtnscrapVO vo) throws Exception;

    /**
	 * COMTNSCRAP을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnscrapVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComtnscrap(ComtnscrapVO vo) throws Exception;

    /**
	 * COMTNSCRAP을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnscrapVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComtnscrap(ComtnscrapVO vo) throws Exception;

    /**
	 * COMTNSCRAP을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnscrapVO
	 * @return 조회한 COMTNSCRAP
	 * @exception Exception
	 */
    ComtnscrapVO selectComtnscrap(ComtnscrapVO vo) throws Exception;

    /**
	 * COMTNSCRAP 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNSCRAP 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComtnscrapList(ComtnscrapVO vo) throws Exception;

    /**
	 * COMTNSCRAP 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNSCRAP 총 갯수
	 * @exception
	 */
    int selectComtnscrapListTotCnt(ComtnscrapVO vo);
}
