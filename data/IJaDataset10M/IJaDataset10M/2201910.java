package assays.com;

import java.util.List;
import assays.hibernate.Parameter;
import java.util.Iterator;
import assays.hibernate.Technology;
import assays.hibernate.TechnologyAccess;
import java.util.HashMap;
import assays.hibernate.StudyGroup;
import assays.hibernate.AssayPersistent;
import java.io.File;
import java.util.HashSet;

public final class PromptUtil {

    private static final int maxParam = 55;

    private static final int paramCount = maxParam + 1;

    public static String getFreeParameterPrompter(List paramList, Long paramId, boolean forAssays) {
        boolean[] freeFields = new boolean[paramCount];
        for (int i = 0; i < paramCount; i++) freeFields[i] = true;
        StringBuffer xyz = new StringBuffer();
        xyz.append("<option value=''>---select value---");
        int selNum = 0;
        if (paramList != null) {
            Iterator paramIter = paramList.iterator();
            while (paramIter.hasNext()) {
                Parameter param = (Parameter) paramIter.next();
                String fieldName = (forAssays) ? param.getAssayColumnName() : param.getStgrColumnName();
                if (fieldName != null && !fieldName.equals("")) {
                    int num = Util.getInt(fieldName.substring(1));
                    if (param.getId().equals(paramId)) selNum = num; else freeFields[num] = false;
                }
            }
            for (int i = 1; i < paramCount; i++) if (freeFields[i]) {
                String fieldName = Util.getFieldNameL(i);
                xyz.append("<option value='").append(fieldName).append("'");
                if (i == selNum) xyz.append(" selected");
                xyz.append(">").append(fieldName);
            }
        }
        return xyz.toString();
    }

    public static List promptGroupsForTechnology(List groupList, Long technolId) {
        int sakums = -1;
        int tekosais = 0;
        if (groupList != null && technolId != null) {
            Long currTechnolId = null;
            Iterator groupIter = groupList.iterator();
            while (groupIter.hasNext()) {
                StudyGroup stgr = (StudyGroup) groupIter.next();
                Technology technol = (Technology) stgr.getTechnology();
                if (currTechnolId == null) {
                    currTechnolId = technol.getId();
                    if (currTechnolId.equals(technolId)) sakums = 0;
                } else if (!currTechnolId.equals(technol.getId())) {
                    if (sakums >= 0) break;
                    currTechnolId = technol.getId();
                    if (currTechnolId.equals(technolId)) sakums = tekosais;
                }
                tekosais++;
            }
        }
        return (sakums >= 0) ? groupList.subList(sakums, tekosais) : null;
    }

    public static String getStudyGroupPrompterFromList(List list, Long objId) {
        StringBuffer xyz = new StringBuffer();
        if (list != null) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                StudyGroup obj = (StudyGroup) iter.next();
                if (obj.getId().equals(objId)) xyz.append("<option selected value=\""); else xyz.append("<option value=\"");
                xyz.append(obj.getId()).append("\">").append(obj.getVisibleIdentifString());
            }
        }
        return xyz.toString();
    }

    public static String getTechnologyPrompterFromAccessList(List list) {
        StringBuffer xyz = new StringBuffer();
        if (list != null) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                TechnologyAccess obj = (TechnologyAccess) iter.next();
                Technology obj1 = obj.getTechnology();
                if (obj1 != null) {
                    xyz.append("<option value=\"");
                    xyz.append(obj1.getId()).append("\">").append(Util.getHTMLString(obj1.getVisibleName()));
                }
            }
        }
        return xyz.toString();
    }

    public static String getTechnologyPrompterFromTechnologyList(List list1, List list2) {
        StringBuffer xyz = new StringBuffer();
        HashSet uvw = new HashSet();
        if (list1 != null) {
            Iterator iter = list1.iterator();
            while (iter.hasNext()) {
                TechnologyAccess obj = (TechnologyAccess) iter.next();
                Technology obj1 = obj.getTechnology();
                if (obj1 != null) {
                    Long id = obj1.getId();
                    if (uvw.add(id)) {
                        xyz.append("<option value=\"");
                        xyz.append(obj1.getId()).append("\">").append(Util.getHTMLString(obj1.getVisibleName()));
                    }
                }
            }
        }
        if (list2 != null) {
            Iterator iter = list2.iterator();
            while (iter.hasNext()) {
                Technology obj = (Technology) iter.next();
                if (obj != null && obj.isPublic()) {
                    Long id = obj.getId();
                    if (uvw.add(id)) {
                        xyz.append("<option value=\"");
                        xyz.append(obj.getId()).append("\">").append(Util.getHTMLString(obj.getVisibleName()));
                    }
                }
            }
        }
        return xyz.toString();
    }

    public static String getObjectPrompterFromList(List list, Long objId) {
        StringBuffer xyz = new StringBuffer();
        if (list != null) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                AssayPersistent obj = (AssayPersistent) iter.next();
                if (obj.getId().equals(objId)) xyz.append("<option selected value=\""); else xyz.append("<option value=\"");
                xyz.append(obj.getId()).append("\">").append(Util.getHTMLString(obj.getVisibleName()));
            }
        }
        return xyz.toString();
    }

    public static String getObjectPrompterFrom2Lists(List list1, List list2, Long objId) {
        StringBuffer xyz = new StringBuffer();
        if (list1 != null) {
            Iterator iter = list1.iterator();
            while (iter.hasNext()) {
                AssayPersistent obj = (AssayPersistent) iter.next();
                if (obj.getId().equals(objId)) xyz.append("<option selected value=\""); else xyz.append("<option value=\"");
                xyz.append(obj.getId()).append("\">").append(Util.getHTMLString(obj.getVisibleName()));
            }
        }
        if (list2 != null) {
            Iterator iter = list2.iterator();
            while (iter.hasNext()) {
                AssayPersistent obj = (AssayPersistent) iter.next();
                if (obj.getId().equals(objId)) xyz.append("<option selected value=\""); else xyz.append("<option value=\"");
                xyz.append(obj.getId()).append("\">").append(Util.getHTMLString(obj.getVisibleName()));
            }
        }
        return xyz.toString();
    }

    public static String getAssayUserPrompter(List list1, Long objId) {
        StringBuffer xyz = new StringBuffer();
        if (list1 != null) {
            Iterator iter = list1.iterator();
            while (iter.hasNext()) {
                Object[] obj = (Object[]) iter.next();
                Long id = (Long) obj[0];
                String name = (String) obj[1];
                if (id.equals(objId)) xyz.append("<option selected value=\""); else xyz.append("<option value=\"");
                xyz.append(id).append("\">").append(Util.getHTMLString(name));
            }
        }
        return xyz.toString();
    }
}
