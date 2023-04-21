package pl.o2.asdluki.oszwo.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.o2.asdluki.oszwo.controller.Constant;
import pl.o2.asdluki.oszwo.model.MenagerProject;
import pl.o2.asdluki.oszwo.model.Project;

public class CreateProjectValidator implements Validator {

    private MenagerProject menagerProject;

    @SuppressWarnings("unchecked")
    public boolean supports(Class arg0) {
        return arg0.equals(Project.class);
    }

    public void validate(Object arg0, Errors arg1) {
        Project project = (Project) arg0;
        if (project.getName().equals("")) arg1.reject(Constant.ERROR_NAME_IS_EMPTY); else if (menagerProject.getProject(project.getName()) != null) arg1.reject(Constant.ERROR_NAME_IS_UNAVAILABLE);
    }

    public MenagerProject getMenagerProject() {
        return menagerProject;
    }

    public void setMenagerProject(MenagerProject menagerProject) {
        this.menagerProject = menagerProject;
    }
}
