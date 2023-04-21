package kr.godsoft.egovframe.generatorwebapp.md_other_objects.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MdOtherObjectsService.java
 * @Description : MdOtherObjects Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface MdOtherObjectsService {

    /**
	 * MD_OTHER_OBJECTS을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MdOtherObjectsVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertMdOtherObjects(MdOtherObjectsVO vo) throws Exception;

    /**
	 * MD_OTHER_OBJECTS을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MdOtherObjectsVO
	 * @return void형
	 * @exception Exception
	 */
    void updateMdOtherObjects(MdOtherObjectsVO vo) throws Exception;

    /**
	 * MD_OTHER_OBJECTS을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MdOtherObjectsVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteMdOtherObjects(MdOtherObjectsVO vo) throws Exception;

    /**
	 * MD_OTHER_OBJECTS을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MdOtherObjectsVO
	 * @return 조회한 MD_OTHER_OBJECTS
	 * @exception Exception
	 */
    MdOtherObjectsVO selectMdOtherObjects(MdOtherObjectsVO vo) throws Exception;

    /**
	 * MD_OTHER_OBJECTS 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_OTHER_OBJECTS 목록
	 * @exception Exception
	 */
    List<EgovMap> selectMdOtherObjectsList(MdOtherObjectsVO vo) throws Exception;

    /**
	 * MD_OTHER_OBJECTS 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_OTHER_OBJECTS 총 갯수
	 * @exception
	 */
    int selectMdOtherObjectsListTotCnt(MdOtherObjectsVO vo);
}
