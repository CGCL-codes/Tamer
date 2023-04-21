package com.hk.web.cmpunion.action;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.CmpUnionBoard;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpUnionService;

@Component("/union/board")
public class CmpUnionBoardAction extends CmpUnionBaseAction {

    @Autowired
    private CmpUnionService cmpUnionService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        long boardId = req.getLongAndSetAttr("boardId");
        CmpUnionBoard cmpUnionBoard = this.cmpUnionService.getCmpUnionBoard(boardId);
        req.setAttribute("cmpUnionBoard", cmpUnionBoard);
        return this.getUnionWapJsp("board/board.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String list(HkRequest req, HkResponse resp) throws Exception {
        long uid = req.getLong("uid");
        SimplePage page = req.getSimplePage(20);
        List<CmpUnionBoard> list = this.cmpUnionService.getCmpUnionBoardListByUid(uid, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, list);
        req.setAttribute("list", list);
        return this.getUnionWapJsp("board/boardlist.jsp");
    }
}
