package com.google.code.booktogether.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import com.google.code.booktogether.service.RecoBookService;
import com.google.code.booktogether.web.controller.abst.AbstractController;

/**
 * RecoBook에 관련된 Controller
 * 
 * @author ParkHaeCheol
 */
@Controller()
public class RecoBookController extends AbstractController {

    /**
	 * RecoBookService
	 */
    @Resource(name = "recoBookService")
    private RecoBookService recoBookService;

    /**
	 * 추천책 등록
	 * 
	 * @param req
	 * @return 로그인화면
	 */
    @RequestMapping("/book/insertRecoBook.do")
    public ModelAndView handleInsertGoodWriter(HttpServletRequest req, @RequestParam(value = "bookIdNum", required = false) Integer bookIdNum) {
        boolean result = recoBookService.insertRecoBook(bookIdNum);
        if (!result) {
            RequestContextHolder.getRequestAttributes().setAttribute("message", "추천책에 등록 실패 되어있습니다.", RequestAttributes.SCOPE_SESSION);
            return new ModelAndView("redirect:/message.do");
        }
        return new ModelAndView("redirect:/book/getBook.do?bookIdNum=" + bookIdNum);
    }
}
