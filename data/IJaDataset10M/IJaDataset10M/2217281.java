package kr.godsoft.egovframe.generatorwebapp.comtncmmnty.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtncmmnty.service.ComtncmmntyService;
import kr.godsoft.egovframe.generatorwebapp.comtncmmnty.service.ComtncmmntyVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtncmmntyServiceImpl.java
 * @Description : Comtncmmnty Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtncmmntyService")
public class ComtncmmntyServiceImpl extends AbstractServiceImpl implements ComtncmmntyService {

    @Resource(name = "comtncmmntyDAO")
    private ComtncmmntyDAO comtncmmntyDAO;

    /**
	 * COMTNCMMNTY을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtncmmntyVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtncmmnty(ComtncmmntyVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtncmmntyDAO.insertComtncmmnty(vo);
        return null;
    }

    /**
	 * COMTNCMMNTY을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtncmmntyVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtncmmnty(ComtncmmntyVO vo) throws Exception {
        comtncmmntyDAO.updateComtncmmnty(vo);
    }

    /**
	 * COMTNCMMNTY을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtncmmntyVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtncmmnty(ComtncmmntyVO vo) throws Exception {
        comtncmmntyDAO.deleteComtncmmnty(vo);
    }

    /**
	 * COMTNCMMNTY을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtncmmntyVO
	 * @return 조회한 COMTNCMMNTY
	 * @exception Exception
	 */
    public ComtncmmntyVO selectComtncmmnty(ComtncmmntyVO vo) throws Exception {
        ComtncmmntyVO resultVO = comtncmmntyDAO.selectComtncmmnty(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNCMMNTY 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCMMNTY 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtncmmntyList(ComtncmmntyVO vo) throws Exception {
        return comtncmmntyDAO.selectComtncmmntyList(vo);
    }

    /**
	 * COMTNCMMNTY 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCMMNTY 총 갯수
	 * @exception
	 */
    public int selectComtncmmntyListTotCnt(ComtncmmntyVO vo) {
        return comtncmmntyDAO.selectComtncmmntyListTotCnt(vo);
    }
}
