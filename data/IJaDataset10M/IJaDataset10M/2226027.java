package kr.godsoft.egovframe.generatorwebapp.comtnanswer.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtnanswer.service.ComtnanswerVO;

/**
 * @Class Name : ComtnanswerDAO.java
 * @Description : Comtnanswer DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtnanswerDAO")
public class ComtnanswerDAO extends EgovAbstractDAO {

    /**
	 * COMTNANSWER을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnanswerVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnanswer(ComtnanswerVO vo) throws Exception {
        return (String) insert("comtnanswerDAO.insertComtnanswer_S", vo);
    }

    /**
	 * COMTNANSWER을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnanswerVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnanswer(ComtnanswerVO vo) throws Exception {
        update("comtnanswerDAO.updateComtnanswer_S", vo);
    }

    /**
	 * COMTNANSWER을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnanswerVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnanswer(ComtnanswerVO vo) throws Exception {
        delete("comtnanswerDAO.deleteComtnanswer_S", vo);
    }

    /**
	 * COMTNANSWER을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnanswerVO
	 * @return 조회한 COMTNANSWER
	 * @exception Exception
	 */
    public ComtnanswerVO selectComtnanswer(ComtnanswerVO vo) throws Exception {
        return (ComtnanswerVO) selectByPk("comtnanswerDAO.selectComtnanswer_S", vo);
    }

    /**
	 * COMTNANSWER 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNANSWER 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtnanswerList(ComtnanswerVO vo) throws Exception {
        return list("comtnanswerDAO.selectComtnanswerList_D", vo);
    }

    /**
	 * COMTNANSWER 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNANSWER 총 갯수
	 * @exception
	 */
    public int selectComtnanswerListTotCnt(ComtnanswerVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtnanswerDAO.selectComtnanswerListTotCnt_S", vo);
    }
}
