package kr.godsoft.egovframe.generatorwebapp.comtncntcservice.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtncntcservice.service.ComtncntcserviceVO;

/**
 * @Class Name : ComtncntcserviceDAO.java
 * @Description : Comtncntcservice DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtncntcserviceDAO")
public class ComtncntcserviceDAO extends EgovAbstractDAO {

    /**
	 * COMTNCNTCSERVICE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtncntcserviceVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        return (String) insert("comtncntcserviceDAO.insertComtncntcservice_S", vo);
    }

    /**
	 * COMTNCNTCSERVICE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtncntcserviceVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        update("comtncntcserviceDAO.updateComtncntcservice_S", vo);
    }

    /**
	 * COMTNCNTCSERVICE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtncntcserviceVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        delete("comtncntcserviceDAO.deleteComtncntcservice_S", vo);
    }

    /**
	 * COMTNCNTCSERVICE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtncntcserviceVO
	 * @return 조회한 COMTNCNTCSERVICE
	 * @exception Exception
	 */
    public ComtncntcserviceVO selectComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        return (ComtncntcserviceVO) selectByPk("comtncntcserviceDAO.selectComtncntcservice_S", vo);
    }

    /**
	 * COMTNCNTCSERVICE 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNCNTCSERVICE 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtncntcserviceList(ComtncntcserviceVO vo) throws Exception {
        return list("comtncntcserviceDAO.selectComtncntcserviceList_D", vo);
    }

    /**
	 * COMTNCNTCSERVICE 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNCNTCSERVICE 총 갯수
	 * @exception
	 */
    public int selectComtncntcserviceListTotCnt(ComtncntcserviceVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtncntcserviceDAO.selectComtncntcserviceListTotCnt_S", vo);
    }
}
