package kr.godsoft.egovframe.generatorwebapp.comthemplyrinfochangedtls.service;

import java.util.List;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComthemplyrinfochangedtlsService.java
 * @Description : Comthemplyrinfochangedtls Business class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface ComthemplyrinfochangedtlsService {

    /**
	 * COMTHEMPLYRINFOCHANGEDTLS을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComthemplyrinfochangedtlsVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    String insertComthemplyrinfochangedtls(ComthemplyrinfochangedtlsVO vo) throws Exception;

    /**
	 * COMTHEMPLYRINFOCHANGEDTLS을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComthemplyrinfochangedtlsVO
	 * @return void형
	 * @exception Exception
	 */
    void updateComthemplyrinfochangedtls(ComthemplyrinfochangedtlsVO vo) throws Exception;

    /**
	 * COMTHEMPLYRINFOCHANGEDTLS을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComthemplyrinfochangedtlsVO
	 * @return void형 
	 * @exception Exception
	 */
    void deleteComthemplyrinfochangedtls(ComthemplyrinfochangedtlsVO vo) throws Exception;

    /**
	 * COMTHEMPLYRINFOCHANGEDTLS을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComthemplyrinfochangedtlsVO
	 * @return 조회한 COMTHEMPLYRINFOCHANGEDTLS
	 * @exception Exception
	 */
    ComthemplyrinfochangedtlsVO selectComthemplyrinfochangedtls(ComthemplyrinfochangedtlsVO vo) throws Exception;

    /**
	 * COMTHEMPLYRINFOCHANGEDTLS 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTHEMPLYRINFOCHANGEDTLS 목록
	 * @exception Exception
	 */
    List<EgovMap> selectComthemplyrinfochangedtlsList(ComthemplyrinfochangedtlsVO vo) throws Exception;

    /**
	 * COMTHEMPLYRINFOCHANGEDTLS 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTHEMPLYRINFOCHANGEDTLS 총 갯수
	 * @exception
	 */
    int selectComthemplyrinfochangedtlsListTotCnt(ComthemplyrinfochangedtlsVO vo);
}
