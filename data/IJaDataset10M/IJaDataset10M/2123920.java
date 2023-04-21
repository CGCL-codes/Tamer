package kr.godsoft.egovframe.generatorwebapp.comtnindvdlinfopolicy.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnindvdlinfopolicy.service.ComtnindvdlinfopolicyService;
import kr.godsoft.egovframe.generatorwebapp.comtnindvdlinfopolicy.service.ComtnindvdlinfopolicyVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnindvdlinfopolicyServiceImpl.java
 * @Description : Comtnindvdlinfopolicy Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtnindvdlinfopolicyService")
public class ComtnindvdlinfopolicyServiceImpl extends AbstractServiceImpl implements ComtnindvdlinfopolicyService {

    @Resource(name = "comtnindvdlinfopolicyDAO")
    private ComtnindvdlinfopolicyDAO comtnindvdlinfopolicyDAO;

    /**
	 * COMTNINDVDLINFOPOLICY을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnindvdlinfopolicyVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnindvdlinfopolicy(ComtnindvdlinfopolicyVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtnindvdlinfopolicyDAO.insertComtnindvdlinfopolicy(vo);
        return null;
    }

    /**
	 * COMTNINDVDLINFOPOLICY을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnindvdlinfopolicyVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnindvdlinfopolicy(ComtnindvdlinfopolicyVO vo) throws Exception {
        comtnindvdlinfopolicyDAO.updateComtnindvdlinfopolicy(vo);
    }

    /**
	 * COMTNINDVDLINFOPOLICY을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnindvdlinfopolicyVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnindvdlinfopolicy(ComtnindvdlinfopolicyVO vo) throws Exception {
        comtnindvdlinfopolicyDAO.deleteComtnindvdlinfopolicy(vo);
    }

    /**
	 * COMTNINDVDLINFOPOLICY을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnindvdlinfopolicyVO
	 * @return 조회한 COMTNINDVDLINFOPOLICY
	 * @exception Exception
	 */
    public ComtnindvdlinfopolicyVO selectComtnindvdlinfopolicy(ComtnindvdlinfopolicyVO vo) throws Exception {
        ComtnindvdlinfopolicyVO resultVO = comtnindvdlinfopolicyDAO.selectComtnindvdlinfopolicy(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNINDVDLINFOPOLICY 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNINDVDLINFOPOLICY 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtnindvdlinfopolicyList(ComtnindvdlinfopolicyVO vo) throws Exception {
        return comtnindvdlinfopolicyDAO.selectComtnindvdlinfopolicyList(vo);
    }

    /**
	 * COMTNINDVDLINFOPOLICY 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNINDVDLINFOPOLICY 총 갯수
	 * @exception
	 */
    public int selectComtnindvdlinfopolicyListTotCnt(ComtnindvdlinfopolicyVO vo) {
        return comtnindvdlinfopolicyDAO.selectComtnindvdlinfopolicyListTotCnt(vo);
    }
}
