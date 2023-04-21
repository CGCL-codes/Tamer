package kr.godsoft.egovframe.generatorwebapp.mgv_all_schema.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MgvAllSchemaService.java
 * @Description : MgvAllSchema Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface MgvAllSchemaService {

    /**
	 * MGV_ALL_SCHEMA을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MgvAllSchemaVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertMgvAllSchema(MgvAllSchemaVO vo) throws Exception;

    /**
	 * MGV_ALL_SCHEMA을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MgvAllSchemaVO
	 * @return void형
	 * @exception Exception
	 */
    void updateMgvAllSchema(MgvAllSchemaVO vo) throws Exception;

    /**
	 * MGV_ALL_SCHEMA을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MgvAllSchemaVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteMgvAllSchema(MgvAllSchemaVO vo) throws Exception;

    /**
	 * MGV_ALL_SCHEMA을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MgvAllSchemaVO
	 * @return 조회한 MGV_ALL_SCHEMA
	 * @exception Exception
	 */
    MgvAllSchemaVO selectMgvAllSchema(MgvAllSchemaVO vo) throws Exception;

    /**
	 * MGV_ALL_SCHEMA 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MGV_ALL_SCHEMA 목록
	 * @exception Exception
	 */
    List<EgovMap> selectMgvAllSchemaList(MgvAllSchemaVO vo) throws Exception;

    /**
	 * MGV_ALL_SCHEMA 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MGV_ALL_SCHEMA 총 갯수
	 * @exception
	 */
    int selectMgvAllSchemaListTotCnt(MgvAllSchemaVO vo);
}
