package kr.or.common.web.taglib;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import kr.or.javacafe.board.dao.BoardDAO;
import kr.or.javacafe.board.domain.Bbs;
import kr.or.javacafe.member.dao.MemberDAO;
import kr.or.javacafe.member.domain.User;

public class BoardTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private String type = null;

    private String bbsId = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBbsId() {
        return bbsId;
    }

    public void setBbsId(String bbsId) {
        this.bbsId = bbsId;
    }

    public void writeTag(String tag) {
        try {
            pageContext.getOut().print(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int doStartTag() {
        if ("bbsId".equalsIgnoreCase(type)) {
            writeTag(getBoardName());
        }
        return SKIP_BODY;
    }

    private String getBoardName() {
        if (null != bbsId) {
            ServletContext sc = ((HttpServletRequest) pageContext.getRequest()).getSession().getServletContext();
            WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
            BoardDAO boardDAO = (BoardDAO) ctx.getBean("boardDAOImpl");
            Bbs param = new Bbs();
            param.setBbsId(bbsId);
            Bbs bbs = boardDAO.selectBbs(param);
            return bbs.getBbsNm();
        }
        return "";
    }
}
