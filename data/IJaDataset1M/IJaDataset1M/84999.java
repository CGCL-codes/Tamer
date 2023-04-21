package kr.godsoft.egovframe.generatorwebapp.comvnusermaster.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comvnusermaster.service.ComvnusermasterVO;

/**
 * @Class Name : ComvnusermasterDAO.java
 * @Description : Comvnusermaster DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comvnusermasterDAO")
public class ComvnusermasterDAO extends EgovAbstractDAO {

    /**
	 * COMVNUSERMASTER을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComvnusermasterVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComvnusermaster(ComvnusermasterVO vo) throws Exception {
        return (String) insert("comvnusermasterDAO.insertComvnusermaster_S", vo);
    }

    /**
	 * COMVNUSERMASTER을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComvnusermasterVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComvnusermaster(ComvnusermasterVO vo) throws Exception {
        update("comvnusermasterDAO.updateComvnusermaster_S", vo);
    }

    /**
	 * COMVNUSERMASTER을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComvnusermasterVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComvnusermaster(ComvnusermasterVO vo) throws Exception {
        delete("comvnusermasterDAO.deleteComvnusermaster_S", vo);
    }

    /**
	 * COMVNUSERMASTER을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComvnusermasterVO
	 * @return 조회한 COMVNUSERMASTER
	 * @exception Exception
	 */
    public ComvnusermasterVO selectComvnusermaster(ComvnusermasterVO vo) throws Exception {
        return (ComvnusermasterVO) selectByPk("comvnusermasterDAO.selectComvnusermaster_S", vo);
    }

    /**
	 * COMVNUSERMASTER 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMVNUSERMASTER 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComvnusermasterList(ComvnusermasterVO vo) throws Exception {
        return list("comvnusermasterDAO.selectComvnusermasterList_D", vo);
    }

    /**
	 * COMVNUSERMASTER 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMVNUSERMASTER 총 갯수
	 * @exception
	 */
    public int selectComvnusermasterListTotCnt(ComvnusermasterVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comvnusermasterDAO.selectComvnusermasterListTotCnt_S", vo);
    }
}
