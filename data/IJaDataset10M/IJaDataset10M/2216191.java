package kr.godsoft.egovframe.generatorwebapp.comtnsystemcntc.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnsystemcntc.service.ComtnsystemcntcService;
import kr.godsoft.egovframe.generatorwebapp.comtnsystemcntc.service.ComtnsystemcntcVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnsystemcntcServiceImpl.java
 * @Description : Comtnsystemcntc Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtnsystemcntcService")
public class ComtnsystemcntcServiceImpl extends AbstractServiceImpl implements ComtnsystemcntcService {

    @Resource(name = "comtnsystemcntcDAO")
    private ComtnsystemcntcDAO comtnsystemcntcDAO;

    /**
	 * COMTNSYSTEMCNTC을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnsystemcntcVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnsystemcntc(ComtnsystemcntcVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtnsystemcntcDAO.insertComtnsystemcntc(vo);
        return null;
    }

    /**
	 * COMTNSYSTEMCNTC을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnsystemcntcVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnsystemcntc(ComtnsystemcntcVO vo) throws Exception {
        comtnsystemcntcDAO.updateComtnsystemcntc(vo);
    }

    /**
	 * COMTNSYSTEMCNTC을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnsystemcntcVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnsystemcntc(ComtnsystemcntcVO vo) throws Exception {
        comtnsystemcntcDAO.deleteComtnsystemcntc(vo);
    }

    /**
	 * COMTNSYSTEMCNTC을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnsystemcntcVO
	 * @return 조회한 COMTNSYSTEMCNTC
	 * @exception Exception
	 */
    public ComtnsystemcntcVO selectComtnsystemcntc(ComtnsystemcntcVO vo) throws Exception {
        ComtnsystemcntcVO resultVO = comtnsystemcntcDAO.selectComtnsystemcntc(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNSYSTEMCNTC 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNSYSTEMCNTC 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtnsystemcntcList(ComtnsystemcntcVO vo) throws Exception {
        return comtnsystemcntcDAO.selectComtnsystemcntcList(vo);
    }

    /**
	 * COMTNSYSTEMCNTC 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNSYSTEMCNTC 총 갯수
	 * @exception
	 */
    public int selectComtnsystemcntcListTotCnt(ComtnsystemcntcVO vo) {
        return comtnsystemcntcDAO.selectComtnsystemcntcListTotCnt(vo);
    }
}
