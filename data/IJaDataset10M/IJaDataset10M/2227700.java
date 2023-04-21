package kr.godsoft.egovframe.generatorwebapp.comtnauthorrolerelate.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtnauthorrolerelate.service.ComtnauthorrolerelateVO;

/**
 * @Class Name : ComtnauthorrolerelateDAO.java
 * @Description : Comtnauthorrolerelate DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtnauthorrolerelateDAO")
public class ComtnauthorrolerelateDAO extends EgovAbstractDAO {

    /**
	 * COMTNAUTHORROLERELATE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnauthorrolerelateVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnauthorrolerelate(ComtnauthorrolerelateVO vo) throws Exception {
        return (String) insert("comtnauthorrolerelateDAO.insertComtnauthorrolerelate_S", vo);
    }

    /**
	 * COMTNAUTHORROLERELATE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnauthorrolerelateVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnauthorrolerelate(ComtnauthorrolerelateVO vo) throws Exception {
        update("comtnauthorrolerelateDAO.updateComtnauthorrolerelate_S", vo);
    }

    /**
	 * COMTNAUTHORROLERELATE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnauthorrolerelateVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnauthorrolerelate(ComtnauthorrolerelateVO vo) throws Exception {
        delete("comtnauthorrolerelateDAO.deleteComtnauthorrolerelate_S", vo);
    }

    /**
	 * COMTNAUTHORROLERELATE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnauthorrolerelateVO
	 * @return 조회한 COMTNAUTHORROLERELATE
	 * @exception Exception
	 */
    public ComtnauthorrolerelateVO selectComtnauthorrolerelate(ComtnauthorrolerelateVO vo) throws Exception {
        return (ComtnauthorrolerelateVO) selectByPk("comtnauthorrolerelateDAO.selectComtnauthorrolerelate_S", vo);
    }

    /**
	 * COMTNAUTHORROLERELATE 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNAUTHORROLERELATE 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtnauthorrolerelateList(ComtnauthorrolerelateVO vo) throws Exception {
        return list("comtnauthorrolerelateDAO.selectComtnauthorrolerelateList_D", vo);
    }

    /**
	 * COMTNAUTHORROLERELATE 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNAUTHORROLERELATE 총 갯수
	 * @exception
	 */
    public int selectComtnauthorrolerelateListTotCnt(ComtnauthorrolerelateVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtnauthorrolerelateDAO.selectComtnauthorrolerelateListTotCnt_S", vo);
    }
}
