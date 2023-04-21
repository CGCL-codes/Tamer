package kr.godsoft.egovframe.generatorwebapp.comtncntcservice.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtncntcservice.service.ComtncntcserviceService;
import kr.godsoft.egovframe.generatorwebapp.comtncntcservice.service.ComtncntcserviceVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtncntcserviceServiceImpl.java
 * @Description : Comtncntcservice Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtncntcserviceService")
public class ComtncntcserviceServiceImpl extends AbstractServiceImpl implements ComtncntcserviceService {

    @Resource(name = "comtncntcserviceDAO")
    private ComtncntcserviceDAO comtncntcserviceDAO;

    /**
	 * COMTNCNTCSERVICE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtncntcserviceVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtncntcserviceDAO.insertComtncntcservice(vo);
        return null;
    }

    /**
	 * COMTNCNTCSERVICE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtncntcserviceVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        comtncntcserviceDAO.updateComtncntcservice(vo);
    }

    /**
	 * COMTNCNTCSERVICE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtncntcserviceVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        comtncntcserviceDAO.deleteComtncntcservice(vo);
    }

    /**
	 * COMTNCNTCSERVICE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtncntcserviceVO
	 * @return 조회한 COMTNCNTCSERVICE
	 * @exception Exception
	 */
    public ComtncntcserviceVO selectComtncntcservice(ComtncntcserviceVO vo) throws Exception {
        ComtncntcserviceVO resultVO = comtncntcserviceDAO.selectComtncntcservice(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNCNTCSERVICE 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCNTCSERVICE 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtncntcserviceList(ComtncntcserviceVO vo) throws Exception {
        return comtncntcserviceDAO.selectComtncntcserviceList(vo);
    }

    /**
	 * COMTNCNTCSERVICE 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNCNTCSERVICE 총 갯수
	 * @exception
	 */
    public int selectComtncntcserviceListTotCnt(ComtncntcserviceVO vo) {
        return comtncntcserviceDAO.selectComtncntcserviceListTotCnt(vo);
    }
}
