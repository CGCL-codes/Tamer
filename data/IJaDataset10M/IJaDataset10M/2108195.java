package cn.edu.jju.psytest.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.edu.jju.psytest.model.constant.UserConstant;
import cn.edu.jju.psytest.model.entity.User;
import cn.edu.jju.psytest.model.util.EntityVoConverter;
import cn.edu.jju.psytest.model.vo.QueryStuInfoCriteriaVo;
import cn.edu.jju.psytest.model.vo.QueryStuTRCriteriaVo;
import cn.edu.jju.psytest.model.vo.QueryTeacherCriteriaVo;
import cn.edu.jju.psytest.model.vo.UserVo;
import cn.edu.jju.psytest.service.QuizRecordService;
import cn.edu.jju.psytest.service.QuizService;
import cn.edu.jju.psytest.service.UserService;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("/user")
public class UserController {

    private QuizService quizService;

    private UserService userService;

    private QuizRecordService quizRecordService;

    @RequestMapping(method = RequestMethod.GET)
    public String renderUserView(ModelMap model, HttpSession session) {
        String username = ((UserVo) session.getAttribute("userVo")).getUsername();
        User user = userService.getUser(username);
        if (user.getUserType().equals(UserConstant.STUDENT)) {
            model.addAttribute("username", username);
            model.addAttribute("quizzesVo", EntityVoConverter.getQuizzesVo(quizService.loadQuizzes()));
            return "student";
        } else if (user.getUserType().equals(UserConstant.TEACHER)) {
            model.addAttribute("username", username);
            model.addAttribute("privilege", userService.getDetailInfoOfTeacherUser(username).getPrivilege());
            return "teacher";
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/queryStuTestResultByCriteria", method = RequestMethod.POST)
    @ResponseBody
    public String queryStuTestResultByCriteria(QueryStuTRCriteriaVo queryCriteria) {
        return userService.queryStuTestResultByCriteria(queryCriteria);
    }

    @RequestMapping(value = "/queryStuInfoByCriteria", method = RequestMethod.POST)
    @ResponseBody
    public String queryStuInfoByCriteria(QueryStuInfoCriteriaVo queryCriteria) {
        return userService.queryStuInfoByCriteria(queryCriteria);
    }

    @RequestMapping(value = "/queryTeacherInfoByCriteria", method = RequestMethod.POST)
    @ResponseBody
    public String queryTeacherInfoByCriteria(QueryTeacherCriteriaVo queryCriteria) {
        return userService.queryTeacherInfoByCriteria(queryCriteria);
    }

    @RequestMapping(value = "/{userId}/testResult", method = RequestMethod.GET)
    public String queryQuizTestResultByUserId(@PathVariable String userId, ModelMap model) {
        model.addAttribute("testResult", quizRecordService.getQuizTestResult(userId));
        return "testResult";
    }

    @RequestMapping(value = "/{teacherId}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteTeacherById(@PathVariable String teacherId) {
        userService.deleteTeacherById(teacherId);
        return "删除成功！";
    }

    /**
	 * Getters And Setters
	 */
    public QuizService getQuizService() {
        return quizService;
    }

    @Resource
    public void setQuizService(QuizService quizService) {
        this.quizService = quizService;
    }

    public UserService getUserService() {
        return userService;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public QuizRecordService getQuizRecordService() {
        return quizRecordService;
    }

    @Resource
    public void setQuizRecordService(QuizRecordService quizRecordService) {
        this.quizRecordService = quizRecordService;
    }
}
