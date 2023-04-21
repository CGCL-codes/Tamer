package spring;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.gdpu.project.service.TeacherService;
import com.gdpu.project.vo.Email;
import com.gdpu.project.vo.News;
import com.gdpu.project.vo.Teacher;

public class TeacherServiceTest extends TestCase {

    private BeanFactory factory;

    /**
	 *读取配置文件
	 */
    protected void setUp() throws Exception {
        factory = new ClassPathXmlApplicationContext("applicationContext-*.xml");
    }

    /**
	 * 测试addTeacher
	 */
    public void addTeacher() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId("张赞赞");
        teacher.setPassword("zan");
        teacher.setRole(1);
        TeacherService teacherService = (TeacherService) factory.getBean("teacherService");
        Boolean flag = teacherService.addTeacher(teacher);
        if (flag == true) {
            System.out.println("新增Teacher成功");
        } else {
            System.out.println("新增Teacher失败!");
        }
    }

    /**
	 * 测试修改Teacher
	 */
    public void updateTeacher() {
        TeacherService teacherService = (TeacherService) factory.getBean("teacherService");
        Teacher teacher = teacherService.findTeacherById("zan1");
        teacher.setRole(0);
        Boolean flag = teacherService.updateTeacher(teacher);
        if (flag == true) {
            System.out.println("===修改成功!===");
        } else {
            System.out.println("===修改失败===");
        }
    }

    /**
	 * 测试新增邮件
	 */
    public void addEmail() {
        TeacherService teacherService = (TeacherService) factory.getBean("teacherService");
        Email email = new Email();
        email.setEmailInfo("zzzzzzzzzz");
        email.setEmailTitle("zan");
        email.setFsTime(new Date());
        email.setReceiver("zan");
        email.setSender("zan");
        email.setReadFlag(0);
        Boolean flag = teacherService.addEmail(email);
        if (flag) {
            System.out.println("=====Email新增成功=====");
        } else {
            System.out.println("=====Email新增失败=====");
        }
    }

    /**
	 * 测试新增新闻
	 */
    public void addNews() {
        TeacherService teacherService = (TeacherService) factory.getBean("teacherService");
        News news = new News();
        news.setNewsTitle("zzzzzzzz");
        news.setNewsInfo("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        news.setFbTime(new Date());
        news.setNewsType("zz");
        Boolean flag = teacherService.addNews(news);
        if (flag) {
            System.out.println("=======news新增成功======");
        } else {
            System.out.println("=====news新增失败=====");
        }
    }

    /**
	 * 测试查找最新新闻（6条）
	 */
    public void findRecentNews() {
        TeacherService teacherService = (TeacherService) factory.getBean("teacherService");
        List<News> newslist = teacherService.findRecentNews();
        if (newslist != null) {
            Iterator<News> it = newslist.iterator();
            while (it.hasNext()) {
                News news = new News();
                news = it.next();
                System.out.println(news.getNewsInfo());
            }
        }
    }
}
