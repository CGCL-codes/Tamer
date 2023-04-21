package kr.godsoft.egovframe.generatorwebapp.md_migr_dependency.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.md_migr_dependency.service.MdMigrDependencyService;
import kr.godsoft.egovframe.generatorwebapp.md_migr_dependency.service.MdMigrDependencyVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MdMigrDependencyServiceImpl.java
 * @Description : MdMigrDependency Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("mdMigrDependencyService")
public class MdMigrDependencyServiceImpl extends AbstractServiceImpl implements MdMigrDependencyService {

    @Resource(name = "mdMigrDependencyDAO")
    private MdMigrDependencyDAO mdMigrDependencyDAO;

    /**
	 * MD_MIGR_DEPENDENCY을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MdMigrDependencyVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertMdMigrDependency(MdMigrDependencyVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        mdMigrDependencyDAO.insertMdMigrDependency(vo);
        return null;
    }

    /**
	 * MD_MIGR_DEPENDENCY을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MdMigrDependencyVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateMdMigrDependency(MdMigrDependencyVO vo) throws Exception {
        mdMigrDependencyDAO.updateMdMigrDependency(vo);
    }

    /**
	 * MD_MIGR_DEPENDENCY을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MdMigrDependencyVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteMdMigrDependency(MdMigrDependencyVO vo) throws Exception {
        mdMigrDependencyDAO.deleteMdMigrDependency(vo);
    }

    /**
	 * MD_MIGR_DEPENDENCY을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MdMigrDependencyVO
	 * @return 조회한 MD_MIGR_DEPENDENCY
	 * @exception Exception
	 */
    public MdMigrDependencyVO selectMdMigrDependency(MdMigrDependencyVO vo) throws Exception {
        MdMigrDependencyVO resultVO = mdMigrDependencyDAO.selectMdMigrDependency(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * MD_MIGR_DEPENDENCY 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_MIGR_DEPENDENCY 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectMdMigrDependencyList(MdMigrDependencyVO vo) throws Exception {
        return mdMigrDependencyDAO.selectMdMigrDependencyList(vo);
    }

    /**
	 * MD_MIGR_DEPENDENCY 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_MIGR_DEPENDENCY 총 갯수
	 * @exception
	 */
    public int selectMdMigrDependencyListTotCnt(MdMigrDependencyVO vo) {
        return mdMigrDependencyDAO.selectMdMigrDependencyListTotCnt(vo);
    }
}
