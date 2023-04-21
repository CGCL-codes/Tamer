package com.germinus.xpression.groupware.educative;

import com.germinus.xpression.cms.categories.Category;
import com.germinus.xpression.groupware.util.GroupwareConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.germinus.xpression.cms.CMSRuntimeException;
import java.util.List;
import java.util.Map;

/**
 *
 * User: ecanuriag
 * Date: 27-abr-2007
 * Time: 18:43:09
 *
 */
public class SubjectMatterDataSourceHelper {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(SubjectMatterDataSourceHelper.class);

    public static List<Subject> getSubjectsCatalog() {
        return getSubjectMatterDataSource().getSubjectsCatalog();
    }

    @SuppressWarnings("unchecked")
    public static Map getSubjetsDisciplines() {
        return getSubjectMatterDataSource().getSubjetsDisciplines();
    }

    public static List<Category> getDisciplinesBySubjectNames(String coursePath, String subject) {
        return getSubjectMatterDataSource().getDisciplinesBySubjectNames(coursePath, subject);
    }

    public static List<Subject> getSubjectsByCourseName(String path) {
        return getSubjectMatterDataSource().getSubjectsByCourseName(path);
    }

    private static SubjectMatterDataSource subjectMatterDataSource;

    public static SubjectMatterDataSource getSubjectMatterDataSource() {
        if (subjectMatterDataSource == null) {
            subjectMatterDataSource = buildSubjectMatterDataSource();
        }
        return subjectMatterDataSource;
    }

    private static SubjectMatterDataSource buildSubjectMatterDataSource() {
        try {
            Class<? extends SubjectMatterDataSource> clazz = GroupwareConfig.getSubjectMatterDataSourceImpl();
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new CMSRuntimeException("Instantiation error at SubjectMatterDataSource class", e);
        } catch (IllegalAccessException e) {
            throw new CMSRuntimeException("Error while trying to create SubjectMatterDataSource class", e);
        }
    }
}
