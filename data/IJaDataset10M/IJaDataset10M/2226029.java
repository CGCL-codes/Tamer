package kr.godsoft.egovframe.generatorwebapp.comtnclub.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnclubService.java
 * @Description : Comtnclub Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComtnclubService {

    /**
	 * COMTNCLUB을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnclubVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComtnclub(ComtnclubVO vo) throws Exception;

    /**
	 * COMTNCLUB을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnclubVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComtnclub(ComtnclubVO vo) throws Exception;

    /**
	 * COMTNCLUB을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnclubVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComtnclub(ComtnclubVO vo) throws Exception;

    /**
	 * COMTNCLUB을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnclubVO
	 * @return 조회한 COMTNCLUB
	 * @exception Exception
	 */
    ComtnclubVO selectComtnclub(ComtnclubVO vo) throws Exception;

    /**
	 * COMTNCLUB 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCLUB 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComtnclubList(ComtnclubVO vo) throws Exception;

    /**
	 * COMTNCLUB 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCLUB 총 갯수
	 * @exception
	 */
    int selectComtnclubListTotCnt(ComtnclubVO vo);
}
