package kr.godsoft.egovframe.generatorwebapp.comtnmemotodo.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtnmemotodo.service.ComtnmemotodoVO;

/**
 * @Class Name : ComtnmemotodoDAO.java
 * @Description : Comtnmemotodo DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtnmemotodoDAO")
public class ComtnmemotodoDAO extends EgovAbstractDAO {

    /**
	 * COMTNMEMOTODO을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnmemotodoVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnmemotodo(ComtnmemotodoVO vo) throws Exception {
        return (String) insert("comtnmemotodoDAO.insertComtnmemotodo_S", vo);
    }

    /**
	 * COMTNMEMOTODO을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnmemotodoVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnmemotodo(ComtnmemotodoVO vo) throws Exception {
        update("comtnmemotodoDAO.updateComtnmemotodo_S", vo);
    }

    /**
	 * COMTNMEMOTODO을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnmemotodoVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnmemotodo(ComtnmemotodoVO vo) throws Exception {
        delete("comtnmemotodoDAO.deleteComtnmemotodo_S", vo);
    }

    /**
	 * COMTNMEMOTODO을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnmemotodoVO
	 * @return 조회한 COMTNMEMOTODO
	 * @exception Exception
	 */
    public ComtnmemotodoVO selectComtnmemotodo(ComtnmemotodoVO vo) throws Exception {
        return (ComtnmemotodoVO) selectByPk("comtnmemotodoDAO.selectComtnmemotodo_S", vo);
    }

    /**
	 * COMTNMEMOTODO 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNMEMOTODO 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtnmemotodoList(ComtnmemotodoVO vo) throws Exception {
        return list("comtnmemotodoDAO.selectComtnmemotodoList_D", vo);
    }

    /**
	 * COMTNMEMOTODO 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNMEMOTODO 총 갯수
	 * @exception
	 */
    public int selectComtnmemotodoListTotCnt(ComtnmemotodoVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtnmemotodoDAO.selectComtnmemotodoListTotCnt_S", vo);
    }
}
