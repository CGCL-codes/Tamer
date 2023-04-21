package com.air.demo.controller.imp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.air.common.controller.imp.BaseControllerImp;
import com.air.common.exception.NoSuchEntityException;
import com.air.common.util.BasicDBDictionary;
import com.air.common.util.OrderExpression;
import com.air.common.util.QueryCondition;
import com.air.common.vo.PageResultListVO;
import com.air.common.vo.ResponseVO;
import com.air.demo.controller.IBookController;
import com.air.demo.mo.Book;
import com.air.demo.service.IBookService;
import com.air.demo.vo.CreateBookRequestVO;

@Controller
@RequestMapping("book")
public class BookControllerImp extends BaseControllerImp implements IBookController {

    @Autowired
    IBookService bookService;

    @Override
    @RequestMapping("createBook")
    public ModelAndView createBook(HttpServletRequest request, HttpServletResponse response, CreateBookRequestVO requestVO) throws Exception {
        Book book = new Book();
        book.setAuthor(requestVO.getAuthor());
        book.setDescription(requestVO.getDescription());
        book.setHasSold(BasicDBDictionary._common_boolean_false);
        book.setStatus(BasicDBDictionary._common_status_available);
        book.setTitle(requestVO.getTitle());
        book = bookService.insert(book);
        return this.viewDetail(request, response, book.getId());
    }

    @Override
    @RequestMapping(value = "view/{id}")
    public ModelAndView viewDetail(HttpServletRequest request, HttpServletResponse response, Integer id) throws Exception {
        Book book = bookService.findById(id);
        if (book == null) {
            throw new NoSuchEntityException();
        }
        ResponseVO responseVO = new ResponseVO();
        responseVO.setViewName("demo/book/detail");
        responseVO.addObject("book", book);
        return parse2Mav(request, responseVO);
    }

    @RequestMapping("toCreatBookView")
    public ModelAndView toCreatBookView(HttpServletRequest request, HttpServletResponse response) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setViewName("demo/book/create");
        return parse2Mav(request, responseVO);
    }

    @Override
    @RequestMapping("list/{curPageNum}/{pageLimit}")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer curPageNum, @PathVariable Integer pageLimit) throws Exception {
        QueryCondition condition = new QueryCondition();
        condition.addOrderExpression("id", OrderExpression._order_direction_asc);
        PageResultListVO pageResultVO = bookService.queryByCondition(condition, curPageNum, pageLimit);
        ResponseVO responseVO = new ResponseVO();
        responseVO.addObject("pageResultVO", pageResultVO);
        responseVO.setViewName("demo/book/list");
        return parse2Mav(request, responseVO);
    }
}
