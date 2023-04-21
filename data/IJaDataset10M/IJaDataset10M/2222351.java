package kr.godsoft.egovframe.generatorwebapp.comtnfile.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtnfile.service.ComtnfileVO;

/**
 * @Class Name : ComtnfileDAO.java
 * @Description : Comtnfile DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtnfileDAO")
public class ComtnfileDAO extends EgovAbstractDAO {

    /**
	 * COMTNFILE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnfileVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnfile(ComtnfileVO vo) throws Exception {
        return (String) insert("comtnfileDAO.insertComtnfile_S", vo);
    }

    /**
	 * COMTNFILE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnfileVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnfile(ComtnfileVO vo) throws Exception {
        update("comtnfileDAO.updateComtnfile_S", vo);
    }

    /**
	 * COMTNFILE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnfileVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnfile(ComtnfileVO vo) throws Exception {
        delete("comtnfileDAO.deleteComtnfile_S", vo);
    }

    /**
	 * COMTNFILE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnfileVO
	 * @return 조회한 COMTNFILE
	 * @exception Exception
	 */
    public ComtnfileVO selectComtnfile(ComtnfileVO vo) throws Exception {
        return (ComtnfileVO) selectByPk("comtnfileDAO.selectComtnfile_S", vo);
    }

    /**
	 * COMTNFILE 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNFILE 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtnfileList(ComtnfileVO vo) throws Exception {
        return list("comtnfileDAO.selectComtnfileList_D", vo);
    }

    /**
	 * COMTNFILE 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNFILE 총 갯수
	 * @exception
	 */
    public int selectComtnfileListTotCnt(ComtnfileVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtnfileDAO.selectComtnfileListTotCnt_S", vo);
    }
}
