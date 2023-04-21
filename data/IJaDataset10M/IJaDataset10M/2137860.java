package kr.godsoft.egovframe.generatorwebapp.migr_generation_order.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.migr_generation_order.service.MigrGenerationOrderService;
import kr.godsoft.egovframe.generatorwebapp.migr_generation_order.service.MigrGenerationOrderVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MigrGenerationOrderServiceImpl.java
 * @Description : MigrGenerationOrder Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("migrGenerationOrderService")
public class MigrGenerationOrderServiceImpl extends AbstractServiceImpl implements MigrGenerationOrderService {

    @Resource(name = "migrGenerationOrderDAO")
    private MigrGenerationOrderDAO migrGenerationOrderDAO;

    /**
	 * MIGR_GENERATION_ORDER을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MigrGenerationOrderVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertMigrGenerationOrder(MigrGenerationOrderVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        migrGenerationOrderDAO.insertMigrGenerationOrder(vo);
        return null;
    }

    /**
	 * MIGR_GENERATION_ORDER을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MigrGenerationOrderVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateMigrGenerationOrder(MigrGenerationOrderVO vo) throws Exception {
        migrGenerationOrderDAO.updateMigrGenerationOrder(vo);
    }

    /**
	 * MIGR_GENERATION_ORDER을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MigrGenerationOrderVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteMigrGenerationOrder(MigrGenerationOrderVO vo) throws Exception {
        migrGenerationOrderDAO.deleteMigrGenerationOrder(vo);
    }

    /**
	 * MIGR_GENERATION_ORDER을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MigrGenerationOrderVO
	 * @return 조회한 MIGR_GENERATION_ORDER
	 * @exception Exception
	 */
    public MigrGenerationOrderVO selectMigrGenerationOrder(MigrGenerationOrderVO vo) throws Exception {
        MigrGenerationOrderVO resultVO = migrGenerationOrderDAO.selectMigrGenerationOrder(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * MIGR_GENERATION_ORDER 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MIGR_GENERATION_ORDER 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectMigrGenerationOrderList(MigrGenerationOrderVO vo) throws Exception {
        return migrGenerationOrderDAO.selectMigrGenerationOrderList(vo);
    }

    /**
	 * MIGR_GENERATION_ORDER 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MIGR_GENERATION_ORDER 총 갯수
	 * @exception
	 */
    public int selectMigrGenerationOrderListTotCnt(MigrGenerationOrderVO vo) {
        return migrGenerationOrderDAO.selectMigrGenerationOrderListTotCnt(vo);
    }
}
