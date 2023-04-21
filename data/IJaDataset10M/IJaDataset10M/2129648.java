package com.google.code.booktogether.web.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import com.google.code.booktogether.service.BookGradeService;
import com.google.code.booktogether.service.BookMarkService;
import com.google.code.booktogether.service.BookReviewService;
import com.google.code.booktogether.service.LibraryBoardService;
import com.google.code.booktogether.service.LibraryService;
import com.google.code.booktogether.web.controller.abst.AbstractController;
import com.google.code.booktogether.web.domain.BookGrade;
import com.google.code.booktogether.web.domain.BookMark;
import com.google.code.booktogether.web.domain.BookReview;
import com.google.code.booktogether.web.domain.Library;
import com.google.code.booktogether.web.domain.LibraryBoard;
import com.google.code.booktogether.web.domain.LibraryBook;
import com.google.code.booktogether.web.domain.PossessBook;
import com.google.code.booktogether.web.domain.User;
import com.google.code.booktogether.web.page.PageBean;

/**
 * Library에 관련된 Controller
 * 
 * @author ParkHaeCheol
 */
@Controller
public class LibraryController extends AbstractController {

    /**
	 * LibraryService
	 */
    @Resource(name = "libraryService")
    private LibraryService libraryService;

    /**
	 * BookReviewService
	 */
    @Resource(name = "bookReviewService")
    private BookReviewService bookReviewService;

    /**
	 * BookGradeService
	 */
    @Resource(name = "bookGradeService")
    private BookGradeService bookGradeService;

    /**
	 * BookMarkService
	 */
    @Resource(name = "bookMarkService")
    private BookMarkService bookMarkService;

    /**
	 * LibraryBoardService
	 */
    @Resource(name = "libraryBoardService")
    private LibraryBoardService libraryBoardService;

    private Logger log = Logger.getLogger(this.getClass());

    /**
	 * 비공개화면보기
	 * 
	 * @param req
	 * @return 비공개화면
	 */
    @RequestMapping("/library/unOpenLibraryView.do")
    public ModelAndView handleUnOpenLibraryView(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/library/unOpenLibrary");
        return mav;
    }

    /**
	 * 수정화면 보기
	 * 
	 * @param req
	 * @return 수정화면
	 */
    @RequestMapping("/library/modifyLibraryView.do")
    public ModelAndView handleModifyLibraryView(HttpServletRequest req) {
        Library library = libraryService.getLibrary(getLoginUserId(), getLoginUserIdNum());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("library/modifyLibrary");
        mav.addObject("library", library);
        return mav;
    }

    /**
	 * 서재 수정하기
	 * 
	 * @param req
	 * @return 수정하기
	 */
    @RequestMapping("/library/modifyLibrary.do")
    public ModelAndView handleModifyLibrary(HttpServletRequest req, Library library) {
        library.getUser().setIdNum(getLoginUserIdNum());
        boolean result = libraryService.modifyLibrary(library);
        if (!result) {
            RequestContextHolder.getRequestAttributes().setAttribute("message", "내서재 정보 수정 실패", RequestAttributes.SCOPE_SESSION);
            return new ModelAndView("redirect:/message.do");
        }
        return new ModelAndView("redirect:/library/getLibrary.do?userId=" + getLoginUserId());
    }

    /**
	 * 서재 조회하기
	 * 
	 * @param req
	 * @return 조회화면
	 */
    @RequestMapping("/library/getLibrary.do")
    public ModelAndView handleGetLibrary(HttpServletRequest req) {
        Library library = getLibrary();
        int libraryBookDbCount0 = 0;
        int libraryBookDbCount1 = 0;
        int libraryBookDbCount2 = 0;
        int possessDbCount = 0;
        int interestLibraryDbCount = 0;
        int reviewDbCount = 0;
        int gradeDbCount = 0;
        int bookMarkDbCount = 0;
        int boardDbCount = 0;
        int userIdNum = library.getUser().getIdNum();
        String userId = library.getUser().getUserId();
        PageBean pageBean = new PageBean();
        pageBean.setPageNo(1);
        pageBean.setLimit(1);
        pageBean.setPageSize(2);
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setLibrary(library);
        List<User> interestLibraryList = libraryService.getListInterestLibrary(library.getUser().getIdNum(), pageBean);
        interestLibraryDbCount = pageBean.getDbCount();
        libraryBook.setState(0);
        List<LibraryBook> libraryBookList0 = libraryService.getListLibraryBook(libraryBook, pageBean);
        libraryBookDbCount0 = pageBean.getDbCount();
        libraryBook.setState(1);
        List<LibraryBook> libraryBookList1 = libraryService.getListLibraryBook(libraryBook, pageBean);
        libraryBookDbCount1 = pageBean.getDbCount();
        libraryBook.setState(2);
        List<LibraryBook> libraryBookList2 = libraryService.getListLibraryBook(libraryBook, pageBean);
        libraryBookDbCount2 = pageBean.getDbCount();
        List<PossessBook> possessBookList = libraryService.getListPossessBook(userId, pageBean);
        possessDbCount = pageBean.getDbCount();
        pageBean.setPageSize(5);
        List<BookReview> bookReviewList = bookReviewService.getListMyBookReview(userIdNum, pageBean);
        reviewDbCount = pageBean.getDbCount();
        List<BookGrade> bookGradeList = bookGradeService.getListMyBookGrade(userIdNum, pageBean);
        gradeDbCount = pageBean.getDbCount();
        List<BookMark> bookMarkList = bookMarkService.getListMyBookMark(userIdNum, pageBean);
        bookMarkDbCount = pageBean.getDbCount();
        List<LibraryBoard> libraryBoardList = libraryBoardService.getListLibraryBoard(library.getIdNum(), pageBean);
        boardDbCount = pageBean.getDbCount();
        library.setNotice(library.getNotice().replaceAll("\r\n", "<br/>"));
        ModelAndView mav = new ModelAndView();
        mav.setViewName("library/library");
        mav.addObject("libraryBookDbCount0", libraryBookDbCount0);
        mav.addObject("libraryBookDbCount1", libraryBookDbCount1);
        mav.addObject("libraryBookDbCount2", libraryBookDbCount2);
        mav.addObject("possessDbCount", possessDbCount);
        mav.addObject("interestLibraryDbCount", interestLibraryDbCount);
        mav.addObject("reviewDbCount", reviewDbCount);
        mav.addObject("gradeDbCount", gradeDbCount);
        mav.addObject("bookMarkDbCount", bookMarkDbCount);
        mav.addObject("boardDbCount", boardDbCount);
        mav.addObject("interestLibraryList", interestLibraryList);
        mav.addObject("libraryBookList0", libraryBookList0);
        mav.addObject("libraryBookList1", libraryBookList1);
        mav.addObject("libraryBookList2", libraryBookList2);
        mav.addObject("possessBookList", possessBookList);
        mav.addObject("bookReviewList", bookReviewList);
        mav.addObject("bookGradeList", bookGradeList);
        mav.addObject("bookMarkList", bookMarkList);
        mav.addObject("libraryBoardList", libraryBoardList);
        return mav;
    }

    /**
	 * 서재 검색
	 * 
	 * @param req
	 * @return
	 */
    @RequestMapping("/main/searchLibrary.do")
    public ModelAndView handleSearchLibrary(HttpServletRequest req, @RequestParam(value = "query", required = false) String query) {
        List<User> userList = null;
        if (query != null) {
            userList = libraryService.getListSearchLibrary(query);
        }
        List<User> libraryRankList = libraryService.getLibraryRank();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("library/searchLibrary");
        mav.addObject("userList", userList);
        mav.addObject("searchType", "library");
        mav.addObject("libraryRankList", libraryRankList);
        return mav;
    }

    /**
	 * 서재내 책 검색
	 * 
	 * @param req
	 * @return
	 */
    @RequestMapping("/library/searchBookInLibrary.do")
    public ModelAndView handleSearchBookInLibrary(HttpServletRequest req, @RequestParam(value = "libraryIdNum", required = false) Integer libraryIdNum, @RequestParam(value = "bookName", required = false) String bookName, @RequestParam(value = "pageNo", required = false) Integer pageNo) {
        pageNo = (pageNo == null) ? 1 : pageNo;
        bookName = (bookName == null) ? "" : bookName.trim();
        PageBean pageBean = new PageBean();
        pageBean.setPageNo(pageNo);
        pageBean.setPageSize(5);
        List<LibraryBook> libraryBookList = libraryService.getListLibraryBook(libraryIdNum, bookName, pageBean);
        boolean moreLibraryBookList = (libraryBookList.size() >= pageBean.getPageSize()) ? true : false;
        List<PossessBook> possessBookList = libraryService.getListPossessBook(libraryIdNum, bookName, pageBean);
        boolean morePossessBookList = (possessBookList.size() >= pageBean.getPageSize()) ? true : false;
        ModelAndView mav = new ModelAndView();
        mav.setViewName("library/searchBookInLibrary");
        mav.addObject("libraryBookList", libraryBookList);
        mav.addObject("possessBookList", possessBookList);
        mav.addObject("moreLibraryBookList", moreLibraryBookList);
        mav.addObject("morePossessBookList", morePossessBookList);
        mav.addObject("libraryIdNum", libraryIdNum);
        mav.addObject("bookName", bookName);
        return mav;
    }

    /**
	 * 관심 서재 등록
	 * 
	 * @param req
	 * @return
	 */
    @RequestMapping("/library/insertInterestLibrary.do")
    public ModelAndView handleInsertInterestLibrary(HttpServletRequest req, @RequestParam(value = "target", required = false) Integer target, @RequestParam(value = "userId", required = false) String userId) {
        Integer userIdNum = getLoginUserIdNum();
        boolean result = false;
        if (target.equals(userIdNum)) {
            RequestContextHolder.getRequestAttributes().setAttribute("message", "자기 자신을 관심서재로 등록할 수 없습니다", RequestAttributes.SCOPE_SESSION);
            return new ModelAndView("redirect:/message.do");
        }
        result = libraryService.duplicateInterestLibrary(target, userIdNum);
        if (result) {
            RequestContextHolder.getRequestAttributes().setAttribute("message", "이미 등록 되어있습니다", RequestAttributes.SCOPE_SESSION);
            return new ModelAndView("redirect:/message.do");
        } else {
            result = libraryService.insertInterestLibrary(userIdNum, target);
            if (!result) {
                RequestContextHolder.getRequestAttributes().setAttribute("message", "등록 실패", RequestAttributes.SCOPE_SESSION);
                return new ModelAndView("redirect:/message.do");
            }
        }
        return new ModelAndView("redirect:/library/getLibrary.do?userId=" + userId);
    }

    /**
	 * 관심 서재 삭제
	 * 
	 * @param req
	 * @return
	 */
    @RequestMapping("/library/deleteInterestLibrary.do")
    public ModelAndView handleDeleteInterestLibrary(HttpServletRequest req, @RequestParam(value = "libraryIdNum", required = false) Integer libraryIdNum, @RequestParam(value = "target", required = false) Integer target) {
        Integer userIdNum = getLoginUserIdNum();
        boolean result = libraryService.deleteInterestLibrary(target, userIdNum);
        if (!result) {
            RequestContextHolder.getRequestAttributes().setAttribute("message", "삭제 실패", RequestAttributes.SCOPE_SESSION);
            return new ModelAndView("redirect:/message.do");
        }
        return new ModelAndView("redirect:/library/getListInterestLibrary.do?userIdNum=" + userIdNum + "&libraryIdNum=" + libraryIdNum);
    }

    /**
	 * 관심 서재 목록
	 * 
	 * @param req
	 * @return
	 */
    @RequestMapping("/library/getListInterestLibrary.do")
    public ModelAndView handleListInterestLibrary(HttpServletRequest req, @RequestParam(value = "userIdNum", required = false) Integer userIdNum) {
        List<User> interestLibraryList = libraryService.getListInterestLibrary(userIdNum);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("library/getListInterestLibrary");
        mav.addObject("interestLibraryList", interestLibraryList);
        return mav;
    }
}
