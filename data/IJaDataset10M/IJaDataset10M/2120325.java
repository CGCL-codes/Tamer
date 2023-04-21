package kr.godsoft.egovframe.generatorwebapp.comtncpyrhtinfo.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtncpyrhtinfo.service.ComtncpyrhtinfoService;
import kr.godsoft.egovframe.generatorwebapp.comtncpyrhtinfo.service.ComtncpyrhtinfoVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtncpyrhtinfoServiceImpl.java
 * @Description : Comtncpyrhtinfo Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtncpyrhtinfoService")
public class ComtncpyrhtinfoServiceImpl extends AbstractServiceImpl implements ComtncpyrhtinfoService {

    @Resource(name = "comtncpyrhtinfoDAO")
    private ComtncpyrhtinfoDAO comtncpyrhtinfoDAO;

    /**
	 * COMTNCPYRHTINFO을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtncpyrhtinfoVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtncpyrhtinfo(ComtncpyrhtinfoVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtncpyrhtinfoDAO.insertComtncpyrhtinfo(vo);
        return null;
    }

    /**
	 * COMTNCPYRHTINFO을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtncpyrhtinfoVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtncpyrhtinfo(ComtncpyrhtinfoVO vo) throws Exception {
        comtncpyrhtinfoDAO.updateComtncpyrhtinfo(vo);
    }

    /**
	 * COMTNCPYRHTINFO을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtncpyrhtinfoVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtncpyrhtinfo(ComtncpyrhtinfoVO vo) throws Exception {
        comtncpyrhtinfoDAO.deleteComtncpyrhtinfo(vo);
    }

    /**
	 * COMTNCPYRHTINFO을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtncpyrhtinfoVO
	 * @return 조회한 COMTNCPYRHTINFO
	 * @exception Exception
	 */
    public ComtncpyrhtinfoVO selectComtncpyrhtinfo(ComtncpyrhtinfoVO vo) throws Exception {
        ComtncpyrhtinfoVO resultVO = comtncpyrhtinfoDAO.selectComtncpyrhtinfo(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNCPYRHTINFO 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCPYRHTINFO 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtncpyrhtinfoList(ComtncpyrhtinfoVO vo) throws Exception {
        return comtncpyrhtinfoDAO.selectComtncpyrhtinfoList(vo);
    }

    /**
	 * COMTNCPYRHTINFO 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCPYRHTINFO 총 갯수
	 * @exception
	 */
    public int selectComtncpyrhtinfoListTotCnt(ComtncpyrhtinfoVO vo) {
        return comtncpyrhtinfoDAO.selectComtncpyrhtinfoListTotCnt(vo);
    }
}
