package com.javaeedev.web.bbs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.javaeedev.domain.Board;
import com.javaeedev.domain.BoardCategory;
import com.javaeedev.domain.Topic;
import com.javaeedev.util.HttpUtil;
import com.javaeedev.web.AbstractBaseController;
import com.javaeedev.web.Page;

/**
 * Display a board.
 * 
 * @author Xuefeng
 * 
 * @spring.bean name="/bbs/board.jspx"
 * @spring.property name="signon" value="false"
 */
public class BoardController extends AbstractBaseController {

    @Override
    @SuppressWarnings("unchecked")
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = HttpUtil.getString(request, "id");
        int pageIndex = HttpUtil.getInt(request, "page", 1);
        Page page = new Page(pageIndex);
        Board board = facade.queryBoard(id);
        List<Topic> topics = facade.queryTopics(board, page);
        List<BoardCategory> boardCategories = facade.queryBoardCategories();
        List<Topic> hotTopics = facade.queryHotTopics(board, StartController.MAX_HOT_TOPICS);
        Map map = new HashMap();
        map.put("boardCategories", boardCategories);
        map.put("groupedBoards", facade.queryAllGroupedBoards(boardCategories));
        map.put("board", board);
        map.put("topics", topics);
        map.put("page", page);
        map.put("hotTopics", hotTopics);
        return new ModelAndView("/bbs/board.htm", map);
    }
}
