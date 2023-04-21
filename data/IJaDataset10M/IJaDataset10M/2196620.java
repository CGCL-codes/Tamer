package pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice;

import pt.ips.estsetubal.mig.academicCloud.client.IFrameworkEvents;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.CreateCompetenceCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.CreateCurricularCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.CreateDegreeCurricularPlanPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.CreateDegreePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DeleteCompetenceCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DeleteCurricularCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DeleteDegreeCurricularPlanPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DeleteDegreePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DetailCompetenceCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DetailCurricularCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DetailDegreeCurricularPlanPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.DetailDegreePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.EditCompetenceCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.EditCurricularCoursePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.EditDegreeCurricularPlanPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.EditDegreePresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.ManageCompetenceCourseVersionsPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.ManageCompetenceCoursesPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.ManageCurricularCoursesPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.ManageDegreeCurricularPlansPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.ManageDegreesPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.SearchCompetenceCourseCriteriaPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.presenter.SearchDegreeCriteriaPresenter;
import pt.ips.estsetubal.mig.academicCloud.client.module.curricularOffice.view.SearchCompetenceCourseCriteriaView;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.CompetenceCourseDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.CurricularCourseDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.DegreeCurricularPlanDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.DegreeDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.domain.degree.DepartmentDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.CreateCompetenceCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.CreateCurricularCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.CreateDegreeCurricularPlanViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.CreateDegreeViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DeleteCompetenceCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DeleteCurricularCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DeleteDegreeCurricularPlanViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DeleteDegreeViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DetailCompetenceCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DetailCurricularCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DetailDegreeCurricularPlanViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.DetailDegreeViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.EditCompetenceCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.EditCurricularCourseViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.EditDegreeCurricularPlanViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.EditDegreeViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.SearchCompetenceCourseCriteriaDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.SearchCompetenceCourseCriteriaViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.SearchCompetenceCourseResultDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.SearchDegreeCriteriaDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.SearchDegreeCriteriaViewInitDataDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.view.curricularOffice.SearchDegreeResultDTO;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

/**
 * Curricular office module event bus.
 * 
 * @author António Casqueiro
 */
@Events(startView = SearchCompetenceCourseCriteriaView.class, module = CurricularOfficeModule.class)
public interface CurricularOfficeEventBus extends EventBus, IFrameworkEvents {

    @Event(handlers = { SearchDegreeCriteriaPresenter.class, SearchCompetenceCourseCriteriaPresenter.class })
    public void initPresenterInCurricularOfficeModule(String selected);

    @Event(handlers = SearchCompetenceCourseCriteriaPresenter.class)
    void setDataSearchCompetenceCourseCriteria(SearchCompetenceCourseCriteriaViewInitDataDTO model);

    @Event(handlers = SearchCompetenceCourseCriteriaPresenter.class)
    public void backToSearchCompetenceCourseCriteria();

    @Event(handlers = ManageCompetenceCoursesPresenter.class)
    void executeGetSearchCompetenceCourseResult(SearchCompetenceCourseCriteriaDTO data);

    @Event(handlers = ManageCompetenceCoursesPresenter.class)
    public void setDataManageCompetenceCourses(SearchCompetenceCourseResultDTO data);

    @Event(handlers = ManageCompetenceCoursesPresenter.class)
    public void backToManageCompetenceCourses(boolean isToRefresh);

    @Event(handlers = CreateCompetenceCoursePresenter.class)
    public void executeGetCreateCompetenceCourse();

    @Event(handlers = CreateCompetenceCoursePresenter.class)
    public void setDataCreateCompetenceCourse(CreateCompetenceCourseViewInitDataDTO data);

    @Event(handlers = EditCompetenceCoursePresenter.class)
    public void executeGetEditCompetenceCourse(CompetenceCourseDTO data);

    @Event(handlers = EditCompetenceCoursePresenter.class)
    public void setDataEditCompetenceCourse(EditCompetenceCourseViewInitDataDTO data);

    @Event(handlers = DeleteCompetenceCoursePresenter.class)
    public void executeGetDeleteCompetenceCourse(CompetenceCourseDTO data);

    @Event(handlers = DeleteCompetenceCoursePresenter.class)
    public void setDataDeleteCompetenceCourse(DeleteCompetenceCourseViewInitDataDTO data);

    @Event(handlers = DetailCompetenceCoursePresenter.class)
    public void executeGetDetailCompetenceCourse(CompetenceCourseDTO data);

    @Event(handlers = DetailCompetenceCoursePresenter.class)
    public void setDataDetailCompetenceCourse(DetailCompetenceCourseViewInitDataDTO data);

    @Event(handlers = SearchDegreeCriteriaPresenter.class)
    public void setDataSearchDegreeCriteria(SearchDegreeCriteriaViewInitDataDTO data);

    @Event(handlers = SearchDegreeCriteriaPresenter.class)
    public void backToSearchDegreeCriteria();

    @Event(handlers = ManageDegreesPresenter.class)
    public void executeGetSearchDegreeResult(SearchDegreeCriteriaDTO data);

    @Event(handlers = ManageDegreesPresenter.class)
    public void setDataManageDegrees(SearchDegreeResultDTO data);

    @Event(handlers = ManageDegreesPresenter.class)
    public void backToManageDegrees(boolean isToRefresh);

    @Event(handlers = CreateDegreePresenter.class)
    public void executeGetCreateDegree();

    @Event(handlers = CreateDegreePresenter.class)
    public void setDataCreateDegree(CreateDegreeViewInitDataDTO data);

    @Event(handlers = DeleteDegreePresenter.class)
    public void executeGetDeleteDegree(DegreeDTO data);

    @Event(handlers = DeleteDegreePresenter.class)
    public void setDataDeleteDegree(DeleteDegreeViewInitDataDTO data);

    @Event(handlers = DetailDegreePresenter.class)
    public void executeGetDetailDegree(DegreeDTO data);

    @Event(handlers = DetailDegreePresenter.class)
    public void setDataDetailDegree(DetailDegreeViewInitDataDTO data);

    @Event(handlers = EditDegreePresenter.class)
    public void executeGetEditDegree(DegreeDTO data);

    @Event(handlers = EditDegreePresenter.class)
    public void setDataEditDegree(EditDegreeViewInitDataDTO data);

    @Event(handlers = ManageDegreeCurricularPlansPresenter.class)
    public void executeGetManageDegreeCurricularPlans(DegreeDTO data);

    @Event(handlers = ManageDegreeCurricularPlansPresenter.class)
    public void setDataManageDegreeCurricularPlans(DegreeDTO data);

    @Event(handlers = ManageDegreeCurricularPlansPresenter.class)
    public void backToManageDegreeCurricularPlans(boolean isToRefresh);

    @Event(handlers = CreateDegreeCurricularPlanPresenter.class)
    public void executeGetCreateDegreeCurricularPlan(DegreeDTO data);

    @Event(handlers = CreateDegreeCurricularPlanPresenter.class)
    public void setDataCreateDegreeCurricularPlan(CreateDegreeCurricularPlanViewInitDataDTO data);

    @Event(handlers = DeleteDegreeCurricularPlanPresenter.class)
    public void executeGetDeleteDegreeCurricularPlan(DegreeCurricularPlanDTO data);

    @Event(handlers = DeleteDegreeCurricularPlanPresenter.class)
    public void setDataDeleteDegreeCurricularPlan(DeleteDegreeCurricularPlanViewInitDataDTO data);

    @Event(handlers = DetailDegreeCurricularPlanPresenter.class)
    public void setDataDetailDegreeCurricularPlan(DetailDegreeCurricularPlanViewInitDataDTO data);

    @Event(handlers = DetailDegreeCurricularPlanPresenter.class)
    public void executeGetDetailDegreeCurricularPlan(DegreeCurricularPlanDTO data);

    @Event(handlers = EditDegreeCurricularPlanPresenter.class)
    public void executeGetEditDegreeCurricularPlan(DegreeCurricularPlanDTO data);

    @Event(handlers = EditDegreeCurricularPlanPresenter.class)
    public void setDataEditDegreeCurricularPlan(EditDegreeCurricularPlanViewInitDataDTO data);

    @Event(handlers = ManageCurricularCoursesPresenter.class)
    public void executeGetManageCurricularCourses(DegreeCurricularPlanDTO data);

    @Event(handlers = ManageCurricularCoursesPresenter.class)
    public void setDataManageCurricularCourses(DegreeCurricularPlanDTO data);

    @Event(handlers = ManageCurricularCoursesPresenter.class)
    public void backToManageCurricularCourses(boolean isToRefresh);

    @Event(handlers = CreateCurricularCoursePresenter.class)
    public void executeGetCreateCurricularCourse(DegreeCurricularPlanDTO data);

    @Event(handlers = CreateCurricularCoursePresenter.class)
    public void setDataCreateCurricularCourse(CreateCurricularCourseViewInitDataDTO data);

    @Event(handlers = CreateCurricularCoursePresenter.class)
    public void setDataCreateCurricularCourseUpdateCompetenceCourses(DepartmentDTO data);

    @Event(handlers = DeleteCurricularCoursePresenter.class)
    public void executeGetDeleteCurricularCourse(CurricularCourseDTO data);

    @Event(handlers = DetailCurricularCoursePresenter.class)
    public void executeGetDetailCurricularCourse(CurricularCourseDTO data);

    @Event(handlers = DetailCurricularCoursePresenter.class)
    public void setDataDetailCurricularCourse(DetailCurricularCourseViewInitDataDTO data);

    @Event(handlers = EditCurricularCoursePresenter.class)
    public void executeGetEditCurricularCourse(CurricularCourseDTO data);

    @Event(handlers = EditCurricularCoursePresenter.class)
    public void setDataEditCurricularCourse(EditCurricularCourseViewInitDataDTO data);

    @Event(handlers = DeleteCurricularCoursePresenter.class)
    public void setDataDeleteCurricularCourse(DeleteCurricularCourseViewInitDataDTO data);

    @Event(handlers = ManageCompetenceCourseVersionsPresenter.class)
    public void executeGetManageCompetenceCourseVersions(CompetenceCourseDTO data);

    @Event(handlers = ManageCompetenceCourseVersionsPresenter.class)
    public void setDataManageCompetenceCourseVersions(CompetenceCourseDTO data);
}
