package cz.kibo.ekonom.controller.manager;

import cz.kibo.ekonom.model.School;
import cz.kibo.ekonom.model.Testset;
import cz.kibo.ekonom.propertyEditor.TestsetPropertyEditor;
import cz.kibo.ekonom.service.QuestionDao;
import cz.kibo.ekonom.service.RegistrationMailSender;
import cz.kibo.ekonom.service.SchoolDao;
import cz.kibo.ekonom.service.TestsetDao;
import cz.kibo.ekonom.validator.SchoolValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author tomas
 */
@Controller
public class ManagerSchoolController extends AController {

    @Autowired
    private SchoolDao schoolDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private RegistrationMailSender registrationMailSender;

    @Autowired
    private TestsetDao testsetDao;

    @Autowired
    private SchoolValidator schoolValidator;

    @RequestMapping(value = "/lektor/skoly.htm", method = RequestMethod.GET)
    public List<School> allSchool() {
        return schoolDao.all();
    }

    @ModelAttribute("menuItem")
    public String getMenuItem() {
        return "school";
    }

    @ModelAttribute("allSchoolOwnedTestsets")
    public List<Testset> getAllSchoolOwnedTestsets() {
        return schoolDao.findByEmail(super.getEmail()).getAllSchoolOwnedTestsets();
    }

    @Override
    protected String getEmail() {
        return super.getEmail();
    }

    @RequestMapping(value = "/lektor/skola_sendRegistrationInfo.htm", method = RequestMethod.GET)
    public String sendRegistrationInfo(Integer schoolId) {
        registrationMailSender.sendRegistrationMail(schoolDao.findById(schoolId));
        return "redirect:/lektor/skoly.htm";
    }

    @RequestMapping(value = "/lektor/skola_delete.htm", method = RequestMethod.GET)
    public String schoolDelete(Integer schoolId) {
        schoolDao.delete(schoolDao.findById(schoolId));
        return "redirect:/lektor/skoly.htm";
    }

    @RequestMapping(value = "/lektor/skola_edit.htm", method = RequestMethod.GET)
    public School schoolEdit(Integer schoolId) {
        for (School school : schoolDao.all()) {
            if (school.getId().equals(schoolId)) {
                return schoolDao.findById(schoolId);
            }
        }
        return schoolDao.getNewSchool();
    }

    @RequestMapping(value = "/lektor/skola_edit.htm", method = RequestMethod.POST)
    public String schoolEdit(School school, Errors errors) {
        schoolValidator.validate(school, errors);
        if (errors.hasErrors()) {
            return "lektor/skola_edit";
        }
        schoolDao.saveOrUpdate(school);
        shareChoiseTestset(school);
        return "redirect:/lektor/skoly.htm";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Testset.class, new TestsetPropertyEditor(testsetDao));
    }

    private void shareChoiseTestset(School school) {
        if (school.getAllSchoolSharedTestsets() == null) return;
        for (Testset ts : school.getAllSchoolSharedTestsets()) {
            Testset shareTestset = testsetDao.shareTestset(ts, school);
            questionDao.shareQuestions(ts.getQuestions(), shareTestset);
        }
    }
}
