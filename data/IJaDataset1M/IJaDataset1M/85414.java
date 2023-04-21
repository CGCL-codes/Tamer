package kr.godsoft.egovframe.generatorwebapp.comtnbndtceckmanage.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnbndtceckmanage.service.ComtnbndtceckmanageService;
import kr.godsoft.egovframe.generatorwebapp.comtnbndtceckmanage.service.ComtnbndtceckmanageVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnbndtceckmanageServiceImpl.java
 * @Description : Comtnbndtceckmanage Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtnbndtceckmanageService")
public class ComtnbndtceckmanageServiceImpl extends AbstractServiceImpl implements ComtnbndtceckmanageService {

    @Resource(name = "comtnbndtceckmanageDAO")
    private ComtnbndtceckmanageDAO comtnbndtceckmanageDAO;

    /**
	 * COMTNBNDTCECKMANAGE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnbndtceckmanageVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnbndtceckmanage(ComtnbndtceckmanageVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtnbndtceckmanageDAO.insertComtnbndtceckmanage(vo);
        return null;
    }

    /**
	 * COMTNBNDTCECKMANAGE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnbndtceckmanageVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnbndtceckmanage(ComtnbndtceckmanageVO vo) throws Exception {
        comtnbndtceckmanageDAO.updateComtnbndtceckmanage(vo);
    }

    /**
	 * COMTNBNDTCECKMANAGE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnbndtceckmanageVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnbndtceckmanage(ComtnbndtceckmanageVO vo) throws Exception {
        comtnbndtceckmanageDAO.deleteComtnbndtceckmanage(vo);
    }

    /**
	 * COMTNBNDTCECKMANAGE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnbndtceckmanageVO
	 * @return 조회한 COMTNBNDTCECKMANAGE
	 * @exception Exception
	 */
    public ComtnbndtceckmanageVO selectComtnbndtceckmanage(ComtnbndtceckmanageVO vo) throws Exception {
        ComtnbndtceckmanageVO resultVO = comtnbndtceckmanageDAO.selectComtnbndtceckmanage(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNBNDTCECKMANAGE 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNBNDTCECKMANAGE 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtnbndtceckmanageList(ComtnbndtceckmanageVO vo) throws Exception {
        return comtnbndtceckmanageDAO.selectComtnbndtceckmanageList(vo);
    }

    /**
	 * COMTNBNDTCECKMANAGE 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNBNDTCECKMANAGE 총 갯수
	 * @exception
	 */
    public int selectComtnbndtceckmanageListTotCnt(ComtnbndtceckmanageVO vo) {
        return comtnbndtceckmanageDAO.selectComtnbndtceckmanageListTotCnt(vo);
    }
}
