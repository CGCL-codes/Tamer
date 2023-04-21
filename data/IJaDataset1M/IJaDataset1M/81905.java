package name.jelen.reqtop.facelets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import name.jelen.reqtop.entity.AttributeType;
import name.jelen.reqtop.entity.Folder;
import name.jelen.reqtop.entity.Project;
import name.jelen.reqtop.entity.ProjectTemplate;
import name.jelen.reqtop.entity.Requirement;
import name.jelen.reqtop.entity.RequirementType;
import name.jelen.reqtop.entity.Versionable;
import name.jelen.reqtop.entity.View;

public class Utils {

    public static String getTimeAgo(Date when) {
        String result = "";
        if (when == null) return result;
        long diff = (System.currentTimeMillis() - when.getTime()) / 1000;
        if (diff < 60) result = "< 1 minute ago"; else if (diff < 60 * 60) result = "" + diff / 60 + " minutes ago"; else if (diff < 60 * 60 * 24) result = "" + diff / 60 / 60 + " hours ago"; else if (diff < 60 * 60 * 24 * 365) result = "" + diff / 60 / 60 / 24 + " days ago";
        return result;
    }

    public static boolean checkInstanceOf(Object obj, String className) {
        boolean result = false;
        try {
            Class<?> clazz = Class.forName(className);
            result = clazz.isInstance(obj);
        } catch (ClassNotFoundException e) {
            result = false;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List reverse(List aList) {
        List result = new ArrayList();
        result.addAll(aList);
        Collections.reverse(result);
        return result;
    }

    public static String getBaseViewId(String viewId) {
        return (viewId.substring(1, viewId.lastIndexOf('.')));
    }

    public static String getVersionableType(Versionable obj) {
        if (obj instanceof Requirement) return "requirement"; else if (obj instanceof RequirementType) return "requirementType"; else if (obj instanceof Project) return "project"; else if (obj instanceof View) return "view"; else if (obj instanceof ProjectTemplate) return "template"; else if (obj instanceof AttributeType) return "attributeType"; else if (obj instanceof Folder) return "folder"; else return "unknown";
    }

    public static String getViewURL(Versionable obj) {
        return "/" + getBaseViewURL(obj) + ".xhtml";
    }

    public static String getBaseViewURL(Versionable obj) {
        if (obj instanceof Requirement) return "Requirement"; else if (obj instanceof RequirementType) return "RequirementType"; else if (obj instanceof Project) return "Project"; else if (obj instanceof View) return "View"; else if (obj instanceof ProjectTemplate) return "Template"; else if (obj instanceof AttributeType) return "AttributeType";
        return "home";
    }

    public static String getIdAttributeName(Versionable obj) {
        if (obj instanceof Requirement) return "requirementId"; else if (obj instanceof RequirementType) return "requirementTypeId"; else if (obj instanceof Project) return "projectId"; else if (obj instanceof View) return "viewId"; else if (obj instanceof ProjectTemplate) return "templateId"; else if (obj instanceof AttributeType) return "attributeTypeId";
        return "unknownId";
    }

    @SuppressWarnings("unchecked")
    public static java.util.List asList(java.util.Collection data) {
        return new ArrayList(data);
    }

    public static String concat(Object o1, Object o2) {
        return (o1 != null ? o1.toString() : "") + (o2 != null ? o2.toString() : "");
    }
}
