package kr.godsoft.egovframe.generatorwebapp.mgv_all_captured_sql.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_captured_sql.service.MgvAllCapturedSqlService;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_captured_sql.service.MgvAllCapturedSqlVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MgvAllCapturedSqlServiceImpl.java
 * @Description : MgvAllCapturedSql Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("mgvAllCapturedSqlService")
public class MgvAllCapturedSqlServiceImpl extends AbstractServiceImpl implements MgvAllCapturedSqlService {

    @Resource(name = "mgvAllCapturedSqlDAO")
    private MgvAllCapturedSqlDAO mgvAllCapturedSqlDAO;

    /**
	 * MGV_ALL_CAPTURED_SQL을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MgvAllCapturedSqlVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertMgvAllCapturedSql(MgvAllCapturedSqlVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        mgvAllCapturedSqlDAO.insertMgvAllCapturedSql(vo);
        return null;
    }

    /**
	 * MGV_ALL_CAPTURED_SQL을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MgvAllCapturedSqlVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateMgvAllCapturedSql(MgvAllCapturedSqlVO vo) throws Exception {
        mgvAllCapturedSqlDAO.updateMgvAllCapturedSql(vo);
    }

    /**
	 * MGV_ALL_CAPTURED_SQL을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MgvAllCapturedSqlVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteMgvAllCapturedSql(MgvAllCapturedSqlVO vo) throws Exception {
        mgvAllCapturedSqlDAO.deleteMgvAllCapturedSql(vo);
    }

    /**
	 * MGV_ALL_CAPTURED_SQL을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MgvAllCapturedSqlVO
	 * @return 조회한 MGV_ALL_CAPTURED_SQL
	 * @exception Exception
	 */
    public MgvAllCapturedSqlVO selectMgvAllCapturedSql(MgvAllCapturedSqlVO vo) throws Exception {
        MgvAllCapturedSqlVO resultVO = mgvAllCapturedSqlDAO.selectMgvAllCapturedSql(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * MGV_ALL_CAPTURED_SQL 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MGV_ALL_CAPTURED_SQL 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectMgvAllCapturedSqlList(MgvAllCapturedSqlVO vo) throws Exception {
        return mgvAllCapturedSqlDAO.selectMgvAllCapturedSqlList(vo);
    }

    /**
	 * MGV_ALL_CAPTURED_SQL 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MGV_ALL_CAPTURED_SQL 총 갯수
	 * @exception
	 */
    public int selectMgvAllCapturedSqlListTotCnt(MgvAllCapturedSqlVO vo) {
        return mgvAllCapturedSqlDAO.selectMgvAllCapturedSqlListTotCnt(vo);
    }
}
