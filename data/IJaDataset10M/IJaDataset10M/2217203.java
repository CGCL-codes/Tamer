package kr.godsoft.egovframe.generatorwebapp.comtnfilesysmntrng.service.impl;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnfilesysmntrng.service.ComtnfilesysmntrngService;
import kr.godsoft.egovframe.generatorwebapp.comtnfilesysmntrng.service.ComtnfilesysmntrngVO;
import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : ComtnfilesysmntrngServiceImpl.java
 * @Description : Comtnfilesysmntrng Business Implement class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("comtnfilesysmntrngService")
public class ComtnfilesysmntrngServiceImpl extends AbstractServiceImpl implements ComtnfilesysmntrngService {

    @Resource(name = "comtnfilesysmntrngDAO")
    private ComtnfilesysmntrngDAO comtnfilesysmntrngDAO;

    /**
	 * COMTNFILESYSMNTRNG을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnfilesysmntrngVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnfilesysmntrng(ComtnfilesysmntrngVO vo) throws Exception {
        log.debug(vo.toString());
        log.debug(vo.toString());
        comtnfilesysmntrngDAO.insertComtnfilesysmntrng(vo);
        return null;
    }

    /**
	 * COMTNFILESYSMNTRNG을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnfilesysmntrngVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnfilesysmntrng(ComtnfilesysmntrngVO vo) throws Exception {
        comtnfilesysmntrngDAO.updateComtnfilesysmntrng(vo);
    }

    /**
	 * COMTNFILESYSMNTRNG을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnfilesysmntrngVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnfilesysmntrng(ComtnfilesysmntrngVO vo) throws Exception {
        comtnfilesysmntrngDAO.deleteComtnfilesysmntrng(vo);
    }

    /**
	 * COMTNFILESYSMNTRNG을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnfilesysmntrngVO
	 * @return 조회한 COMTNFILESYSMNTRNG
	 * @exception Exception
	 */
    public ComtnfilesysmntrngVO selectComtnfilesysmntrng(ComtnfilesysmntrngVO vo) throws Exception {
        ComtnfilesysmntrngVO resultVO = comtnfilesysmntrngDAO.selectComtnfilesysmntrng(vo);
        if (resultVO == null) throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * COMTNFILESYSMNTRNG 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNFILESYSMNTRNG 목록
	 * @exception Exception
	 */
    public List<EgovMap> selectComtnfilesysmntrngList(ComtnfilesysmntrngVO vo) throws Exception {
        return comtnfilesysmntrngDAO.selectComtnfilesysmntrngList(vo);
    }

    /**
	 * COMTNFILESYSMNTRNG 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return COMTNFILESYSMNTRNG 총 갯수
	 * @exception
	 */
    public int selectComtnfilesysmntrngListTotCnt(ComtnfilesysmntrngVO vo) {
        return comtnfilesysmntrngDAO.selectComtnfilesysmntrngListTotCnt(vo);
    }
}
