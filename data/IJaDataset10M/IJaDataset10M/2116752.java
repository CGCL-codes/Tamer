package com.avatal.factory.assembler.view;

import java.util.ArrayList;
import java.util.Locale;
import com.avatal.content.vo.course.CourseReportingVo;
import com.avatal.factory.assembler.AbstractObjectAssembler;
import com.avatal.view.course.CourseReportingListView;
import com.avatal.view.course.CourseReportingView;

/**
 * 
 * @author c. ferdinand
 * @date 20.08.2003
 *
 *
 */
public class CourseReportingListViewAssembler extends AbstractObjectAssembler {

    private CourseReportingListView courseReportingListView;

    /**
	 * constructor
	 */
    public CourseReportingListViewAssembler() {
        courseReportingListView = new CourseReportingListView();
    }

    public void addCourseReportings(ArrayList courseReportings, Locale locale) {
        ArrayList containerCourseReports = new ArrayList();
        for (int i = 0; i < courseReportings.size(); i++) {
            CourseReportingVo courseReporting = (CourseReportingVo) courseReportings.get(i);
            CourseReportingViewAssembler courseReportingViewAssembler = new CourseReportingViewAssembler();
            courseReportingViewAssembler.addCourseReporting(courseReporting, locale);
            CourseReportingView courseReportingView = courseReportingViewAssembler.getCourseReportingView();
            containerCourseReports.add(courseReportingView);
        }
        courseReportingListView.put(CourseReportingListView.COURSE_REPORTS_LIST, containerCourseReports);
    }

    /**
	 * @return
	 */
    public CourseReportingListView getCourseReportingListView() {
        return courseReportingListView;
    }
}
