package kr.godsoft.egovframe.generatorwebapp.md_migr_parameter.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.md_migr_parameter.service.MdMigrParameterService;
import kr.godsoft.egovframe.generatorwebapp.md_migr_parameter.service.MdMigrParameterVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MdMigrParameterServiceImpl.java
 * @Description : MdMigrParameter Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("mdMigrParameterService")
public class MdMigrParameterServiceImpl extends AbstractServiceImpl implements MdMigrParameterService {

    @Resource(name = "mdMigrParameterDAO")
    private MdMigrParameterDAO mdMigrParameterDAO;

    /**
	 * MD_MIGR_PARAMETER을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MdMigrParameterVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertMdMigrParameter(MdMigrParameterVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        mdMigrParameterDAO.insertMdMigrParameter(vo);
        return null;
    }

    /**
	 * MD_MIGR_PARAMETER을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MdMigrParameterVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateMdMigrParameter(MdMigrParameterVO vo) throws Exception {
        mdMigrParameterDAO.updateMdMigrParameter(vo);
    }

    /**
	 * MD_MIGR_PARAMETER을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MdMigrParameterVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteMdMigrParameter(MdMigrParameterVO vo) throws Exception {
        mdMigrParameterDAO.deleteMdMigrParameter(vo);
    }

    /**
	 * MD_MIGR_PARAMETER을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MdMigrParameterVO
	 * @return 조회한 MD_MIGR_PARAMETER
	 * @exception Exception
	 */
    public MdMigrParameterVO selectMdMigrParameter(MdMigrParameterVO vo) throws Exception {
        MdMigrParameterVO resultVO = mdMigrParameterDAO.selectMdMigrParameter(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * MD_MIGR_PARAMETER 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_MIGR_PARAMETER 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectMdMigrParameterList(MdMigrParameterVO vo) throws Exception {
        return mdMigrParameterDAO.selectMdMigrParameterList(vo);
    }

    /**
	 * MD_MIGR_PARAMETER 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_MIGR_PARAMETER 총 갯수
	 * @exception
	 */
    public int selectMdMigrParameterListTotCnt(MdMigrParameterVO vo) {
        return mdMigrParameterDAO.selectMdMigrParameterListTotCnt(vo);
    }
}
