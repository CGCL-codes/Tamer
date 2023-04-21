package kr.godsoft.egovframe.generatorwebapp.comtnleaderschdulde.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnleaderschduldeService.java
 * @Description : Comtnleaderschdulde Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComtnleaderschduldeService {

    /**
	 * COMTNLEADERSCHDULDE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnleaderschduldeVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComtnleaderschdulde(ComtnleaderschduldeVO vo) throws Exception;

    /**
	 * COMTNLEADERSCHDULDE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnleaderschduldeVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComtnleaderschdulde(ComtnleaderschduldeVO vo) throws Exception;

    /**
	 * COMTNLEADERSCHDULDE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnleaderschduldeVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComtnleaderschdulde(ComtnleaderschduldeVO vo) throws Exception;

    /**
	 * COMTNLEADERSCHDULDE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnleaderschduldeVO
	 * @return 조회한 COMTNLEADERSCHDULDE
	 * @exception Exception
	 */
    ComtnleaderschduldeVO selectComtnleaderschdulde(ComtnleaderschduldeVO vo) throws Exception;

    /**
	 * COMTNLEADERSCHDULDE 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNLEADERSCHDULDE 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComtnleaderschduldeList(ComtnleaderschduldeVO vo) throws Exception;

    /**
	 * COMTNLEADERSCHDULDE 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNLEADERSCHDULDE 총 갯수
	 * @exception
	 */
    int selectComtnleaderschduldeListTotCnt(ComtnleaderschduldeVO vo);
}
