package kr.godsoft.egovframe.generatorwebapp.md_stored_programs.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.md_stored_programs.service.MdStoredProgramsService;
import kr.godsoft.egovframe.generatorwebapp.md_stored_programs.service.MdStoredProgramsVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : MdStoredProgramsServiceImpl.java
 * @Description : MdStoredPrograms Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("mdStoredProgramsService")
public class MdStoredProgramsServiceImpl extends AbstractServiceImpl implements MdStoredProgramsService {

    @Resource(name = "mdStoredProgramsDAO")
    private MdStoredProgramsDAO mdStoredProgramsDAO;

    /**
	 * MD_STORED_PROGRAMS을 등록한다.
	 * @param vo - 등록할 정보가 담긴 MdStoredProgramsVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertMdStoredPrograms(MdStoredProgramsVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        mdStoredProgramsDAO.insertMdStoredPrograms(vo);
        return null;
    }

    /**
	 * MD_STORED_PROGRAMS을 수정한다.
	 * @param vo - 수정할 정보가 담긴 MdStoredProgramsVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateMdStoredPrograms(MdStoredProgramsVO vo) throws Exception {
        mdStoredProgramsDAO.updateMdStoredPrograms(vo);
    }

    /**
	 * MD_STORED_PROGRAMS을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 MdStoredProgramsVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteMdStoredPrograms(MdStoredProgramsVO vo) throws Exception {
        mdStoredProgramsDAO.deleteMdStoredPrograms(vo);
    }

    /**
	 * MD_STORED_PROGRAMS을 조회한다.
	 * @param vo - 조회할 정보가 담긴 MdStoredProgramsVO
	 * @return 조회한 MD_STORED_PROGRAMS
	 * @exception Exception
	 */
    public MdStoredProgramsVO selectMdStoredPrograms(MdStoredProgramsVO vo) throws Exception {
        MdStoredProgramsVO resultVO = mdStoredProgramsDAO.selectMdStoredPrograms(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * MD_STORED_PROGRAMS 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_STORED_PROGRAMS 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectMdStoredProgramsList(MdStoredProgramsVO vo) throws Exception {
        return mdStoredProgramsDAO.selectMdStoredProgramsList(vo);
    }

    /**
	 * MD_STORED_PROGRAMS 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return MD_STORED_PROGRAMS 총 갯수
	 * @exception
	 */
    public int selectMdStoredProgramsListTotCnt(MdStoredProgramsVO vo) {
        return mdStoredProgramsDAO.selectMdStoredProgramsListTotCnt(vo);
    }
}
