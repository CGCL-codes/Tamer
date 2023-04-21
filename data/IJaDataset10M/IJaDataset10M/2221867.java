package kr.godsoft.egovframe.generatorwebapp.comtnsynchrnserverinfo.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtnsynchrnserverinfo.service.ComtnsynchrnserverinfoVO;

/**
 * @Class Name : ComtnsynchrnserverinfoDAO.java
 * @Description : Comtnsynchrnserverinfo DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtnsynchrnserverinfoDAO")
public class ComtnsynchrnserverinfoDAO extends EgovAbstractDAO {

    /**
	 * COMTNSYNCHRNSERVERINFO을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnsynchrnserverinfoVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnsynchrnserverinfo(ComtnsynchrnserverinfoVO vo) throws Exception {
        return (String) insert("comtnsynchrnserverinfoDAO.insertComtnsynchrnserverinfo_S", vo);
    }

    /**
	 * COMTNSYNCHRNSERVERINFO을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnsynchrnserverinfoVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnsynchrnserverinfo(ComtnsynchrnserverinfoVO vo) throws Exception {
        update("comtnsynchrnserverinfoDAO.updateComtnsynchrnserverinfo_S", vo);
    }

    /**
	 * COMTNSYNCHRNSERVERINFO을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnsynchrnserverinfoVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnsynchrnserverinfo(ComtnsynchrnserverinfoVO vo) throws Exception {
        delete("comtnsynchrnserverinfoDAO.deleteComtnsynchrnserverinfo_S", vo);
    }

    /**
	 * COMTNSYNCHRNSERVERINFO을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnsynchrnserverinfoVO
	 * @return 조회한 COMTNSYNCHRNSERVERINFO
	 * @exception Exception
	 */
    public ComtnsynchrnserverinfoVO selectComtnsynchrnserverinfo(ComtnsynchrnserverinfoVO vo) throws Exception {
        return (ComtnsynchrnserverinfoVO) selectByPk("comtnsynchrnserverinfoDAO.selectComtnsynchrnserverinfo_S", vo);
    }

    /**
	 * COMTNSYNCHRNSERVERINFO 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNSYNCHRNSERVERINFO 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtnsynchrnserverinfoList(ComtnsynchrnserverinfoVO vo) throws Exception {
        return list("comtnsynchrnserverinfoDAO.selectComtnsynchrnserverinfoList_D", vo);
    }

    /**
	 * COMTNSYNCHRNSERVERINFO 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNSYNCHRNSERVERINFO 총 갯수
	 * @exception
	 */
    public int selectComtnsynchrnserverinfoListTotCnt(ComtnsynchrnserverinfoVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtnsynchrnserverinfoDAO.selectComtnsynchrnserverinfoListTotCnt_S", vo);
    }
}
