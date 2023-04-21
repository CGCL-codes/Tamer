package kr.godsoft.egovframe.generatorwebapp.mgv_all_views.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_views.service.MgvAllViewsService;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_views.service.MgvAllViewsVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MgvAllViewsServiceImpl.java
 * @Description : MgvAllViews Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("mgvAllViewsService")
public class MgvAllViewsServiceImpl extends AbstractServiceImpl implements MgvAllViewsService {

    @Resource(name = "mgvAllViewsDAO")
    private MgvAllViewsDAO mgvAllViewsDAO;

    /**
	 * MGV_ALL_VIEWS을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MgvAllViewsVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertMgvAllViews(MgvAllViewsVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        mgvAllViewsDAO.insertMgvAllViews(vo);
        return null;
    }

    /**
	 * MGV_ALL_VIEWS을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MgvAllViewsVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateMgvAllViews(MgvAllViewsVO vo) throws Exception {
        mgvAllViewsDAO.updateMgvAllViews(vo);
    }

    /**
	 * MGV_ALL_VIEWS을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MgvAllViewsVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteMgvAllViews(MgvAllViewsVO vo) throws Exception {
        mgvAllViewsDAO.deleteMgvAllViews(vo);
    }

    /**
	 * MGV_ALL_VIEWS을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MgvAllViewsVO
	 * @return 조회한 MGV_ALL_VIEWS
	 * @exception Exception
	 */
    public MgvAllViewsVO selectMgvAllViews(MgvAllViewsVO vo) throws Exception {
        MgvAllViewsVO resultVO = mgvAllViewsDAO.selectMgvAllViews(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * MGV_ALL_VIEWS 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MGV_ALL_VIEWS 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectMgvAllViewsList(MgvAllViewsVO vo) throws Exception {
        return mgvAllViewsDAO.selectMgvAllViewsList(vo);
    }

    /**
	 * MGV_ALL_VIEWS 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MGV_ALL_VIEWS 총 갯수
	 * @exception
	 */
    public int selectMgvAllViewsListTotCnt(MgvAllViewsVO vo) {
        return mgvAllViewsDAO.selectMgvAllViewsListTotCnt(vo);
    }
}
