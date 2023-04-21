package kr.godsoft.egovframe.generatorwebapp.comtcadministcoderecptnlog.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtcadministcoderecptnlog.service.ComtcadministcoderecptnlogVO;

/**
 * @Class Name : ComtcadministcoderecptnlogDAO.java
 * @Description : Comtcadministcoderecptnlog DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtcadministcoderecptnlogDAO")
public class ComtcadministcoderecptnlogDAO extends EgovAbstractDAO {

    /**
	 * COMTCADMINISTCODERECPTNLOG을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtcadministcoderecptnlogVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO vo) throws Exception {
        return (String) insert("comtcadministcoderecptnlogDAO.insertComtcadministcoderecptnlog_S", vo);
    }

    /**
	 * COMTCADMINISTCODERECPTNLOG을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtcadministcoderecptnlogVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO vo) throws Exception {
        update("comtcadministcoderecptnlogDAO.updateComtcadministcoderecptnlog_S", vo);
    }

    /**
	 * COMTCADMINISTCODERECPTNLOG을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtcadministcoderecptnlogVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO vo) throws Exception {
        delete("comtcadministcoderecptnlogDAO.deleteComtcadministcoderecptnlog_S", vo);
    }

    /**
	 * COMTCADMINISTCODERECPTNLOG을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtcadministcoderecptnlogVO
	 * @return 조회한 COMTCADMINISTCODERECPTNLOG
	 * @exception Exception
	 */
    public ComtcadministcoderecptnlogVO selectComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO vo) throws Exception {
        return (ComtcadministcoderecptnlogVO) selectByPk("comtcadministcoderecptnlogDAO.selectComtcadministcoderecptnlog_S", vo);
    }

    /**
	 * COMTCADMINISTCODERECPTNLOG 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTCADMINISTCODERECPTNLOG 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtcadministcoderecptnlogList(ComtcadministcoderecptnlogVO vo) throws Exception {
        return list("comtcadministcoderecptnlogDAO.selectComtcadministcoderecptnlogList_D", vo);
    }

    /**
	 * COMTCADMINISTCODERECPTNLOG 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTCADMINISTCODERECPTNLOG 총 갯수
	 * @exception
	 */
    public int selectComtcadministcoderecptnlogListTotCnt(ComtcadministcoderecptnlogVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtcadministcoderecptnlogDAO.selectComtcadministcoderecptnlogListTotCnt_S", vo);
    }
}
