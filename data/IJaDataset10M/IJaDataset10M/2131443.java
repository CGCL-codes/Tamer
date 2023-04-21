package kr.godsoft.egovframe.generatorwebapp.comtnintnetsvc.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnintnetsvc.service.ComtnintnetsvcService;
import kr.godsoft.egovframe.generatorwebapp.comtnintnetsvc.service.ComtnintnetsvcVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnintnetsvcServiceImpl.java
 * @Description : Comtnintnetsvc Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtnintnetsvcService")
public class ComtnintnetsvcServiceImpl extends AbstractServiceImpl implements ComtnintnetsvcService {

    @Resource(name = "comtnintnetsvcDAO")
    private ComtnintnetsvcDAO comtnintnetsvcDAO;

    /**
	 * COMTNINTNETSVC을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnintnetsvcVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtnintnetsvcDAO.insertComtnintnetsvc(vo);
        return null;
    }

    /**
	 * COMTNINTNETSVC을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnintnetsvcVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        comtnintnetsvcDAO.updateComtnintnetsvc(vo);
    }

    /**
	 * COMTNINTNETSVC을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnintnetsvcVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        comtnintnetsvcDAO.deleteComtnintnetsvc(vo);
    }

    /**
	 * COMTNINTNETSVC을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnintnetsvcVO
	 * @return 조회한 COMTNINTNETSVC
	 * @exception Exception
	 */
    public ComtnintnetsvcVO selectComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        ComtnintnetsvcVO resultVO = comtnintnetsvcDAO.selectComtnintnetsvc(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNINTNETSVC 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNINTNETSVC 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtnintnetsvcList(ComtnintnetsvcVO vo) throws Exception {
        return comtnintnetsvcDAO.selectComtnintnetsvcList(vo);
    }

    /**
	 * COMTNINTNETSVC 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNINTNETSVC 총 갯수
	 * @exception
	 */
    public int selectComtnintnetsvcListTotCnt(ComtnintnetsvcVO vo) {
        return comtnintnetsvcDAO.selectComtnintnetsvcListTotCnt(vo);
    }
}
