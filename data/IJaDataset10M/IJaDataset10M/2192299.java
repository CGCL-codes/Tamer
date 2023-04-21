package egovframework.com.utl.pao.web;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import egovframework.com.utl.pao.service.EgovPrntngOutpt;
import egovframework.com.utl.pao.service.PrntngOutptVO;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 *
 * 전자관인에 관한 Util 테스트를 위한 화면 Controller
 * @author 공통서비스 개발팀 이중호
 * @since 2009.04.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.01  이중호          최초 생성
 *
 * </pre>
 */
@Controller
public class EgovErncslController extends HttpServlet {

    @Resource(name = "PrntngOutpt")
    private EgovPrntngOutpt prntngOutpt;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 서블릿 초기화
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * 관인이미지를 조회하여 출력
     * @param
     * @param
     * @return
     * @exception MyException
     * @see
    */
    @RequestMapping(value = "/utl/pao/EgovErncsl.do")
    public void doGet(@RequestParam("sOrgCode") String orgCode, @RequestParam("sErncslSe") String erncslSe, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("EgovErncsl start....");
        PrntngOutptVO req = new PrntngOutptVO();
        req.setOrgCode(orgCode);
        req.setErncslSe(erncslSe);
        PrntngOutptVO res = null;
        try {
            res = prntngOutpt.selectErncsl(req);
        } catch (Exception e) {
            logger.error(e);
        }
        if (res == null) {
            throw new RuntimeException("image not found!!!");
        }
        byte[] img = res.getImgInfo();
        String imgtype = res.getImgType();
        String type = "";
        if (imgtype != null && !"".equals(imgtype)) {
            type = "image/" + imgtype;
        } else {
            logger.debug("Image fileType is null.");
        }
        if (img == null) {
            logger.debug("Image fileInfo is null.");
            return;
        }
        response.setHeader("Content-Type", type.replaceAll("\r", "").replaceAll("\n", ""));
        response.setHeader("Content-Length", "" + img.length);
        response.getOutputStream().write(img);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        logger.info("EgovErncsl end....");
    }
}
