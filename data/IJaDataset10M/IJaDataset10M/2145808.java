package egovframework.com.dam.spe.req.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import egovframework.com.dam.spe.req.service.EgovRequestOfferService;
import egovframework.com.dam.spe.req.service.RequestOfferVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

/**
 * 지식정보제공/지식정보요청를 처리하는 ServiceImpl Class 구현
 * @author 공통서비스 장동한
 * @since 2010.08.30
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2010.08.30  장동한          최초 생성
 * 
 * </pre>
 */
@Service("egovRequestOffeService")
public class EgovRequestOfferServiceImpl extends AbstractServiceImpl implements EgovRequestOfferService {

    @Resource(name = "RequestOfferDao")
    private RequestOfferDao dao;

    @Resource(name = "egovRequestOfferIdGnrService")
    private EgovIdGnrService idgenService;

    /**
     * 삭제시 하위 답변 건수를 조회한다.
     * @param RequestOfferVO  조회할 정보가 담긴 객체
     * @return int
     * @throws Exception
     */
    public int selectRequestOfferDelCnt(Map map) throws Exception {
        return (Integer) dao.selectRequestOfferDelCnt(map);
    }

    /**
	 * 등록된 지식전문가 건수를 조회한다.
	 * @param map  조회할 정보가 담긴 객체
	 * @return List
	 * @throws Exception
	 */
    public boolean selectRequestOfferSpeCheck(Map map) throws Exception {
        int nSpeCnt = (Integer) dao.selectRequestOfferSpeCnt(map);
        boolean booleanRtn = false;
        if (nSpeCnt > 0) {
            booleanRtn = true;
        }
        return booleanRtn;
    }

    /**
     * 지식정보제공/지식정보요청를(을) 목록을 조회 한다.
     * @param RequestOfferVO 조회할 정보가 담긴 객체
     * @return List
     * @throws Exception
     */
    public List selectRequestOfferList(RequestOfferVO RequestOfferVO) throws Exception {
        return dao.selectRequestOfferList(RequestOfferVO);
    }

    /**
     * 지식정보제공/지식정보요청를(을) 목록 전체 건수를(을) 조회한다.
     * @param searchVO  조회할 정보가 담긴 객체
     * @return int
     * @throws Exception
     */
    public int selectRequestOfferListCnt(RequestOfferVO RequestOfferVO) throws Exception {
        return dao.selectRequestOfferListCnt(RequestOfferVO);
    }

    /**
     * 지식정보제공/지식정보요청를(을) 상세조회 한다.
     * @param searchVO 조회할 정보가 담긴 객체
     * @return List
     * @throws Exception
     */
    public RequestOfferVO selectRequestOfferDetail(RequestOfferVO RequestOfferVO) throws Exception {
        return dao.selectRequestOfferDetail(RequestOfferVO);
    }

    /**
     * 지식정보제공/지식정보요청를(을) 등록한다.
     * @param RequestOfferVO 지식정보제공/지식정보요청 정보가 담긴 객체
     * @throws Exception
     */
    public void insertRequestOffer(RequestOfferVO RequestOfferVO) throws Exception {
        RequestOfferVO.setKnoId((String) idgenService.getNextStringId());
        dao.insertRequestOffer(RequestOfferVO);
    }

    /**
     * 지식정보제공/지식정보요청를(을) 수정한다.
     * @param RequestOfferVO 지식정보제공/지식정보요청 정보가 담긴 객체
     * @throws Exception
     */
    public void updateRequestOffer(RequestOfferVO RequestOfferVO) throws Exception {
        dao.updateRequestOffer(RequestOfferVO);
    }

    /**
     * 지식정보제공/지식정보요청를(을) 삭제한다.
     * @param RequestOfferVO 지식정보제공/지식정보요청 정보가 담긴 객체
     * @throws Exception
     */
    public void deleteRequestOffer(RequestOfferVO RequestOfferVO) throws Exception {
        dao.deleteRequestOffer(RequestOfferVO);
    }
}
