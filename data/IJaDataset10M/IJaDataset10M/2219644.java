package com.widen.prima;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import com.widen.prima.finance.entites.AccountSubjectBo;
import com.widen.prima.finance.entites.Direction;
import com.widen.prima.finance.entites.SubjectType;
import com.widen.prima.finance.services.AccountSubjectService;
import com.widen.prima.system.entites.Properties;
import com.widen.prima.util.Util;
import com.widen.prima.util.XmlHelper;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String PERSPECTIVE_ID = "com.widen.prima.perspective";

    private static final AccountSubjectService subjectService = ServiceLocator.instance().getAccountSubjectService();

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    public void initialize(IWorkbenchConfigurer configurer) {
        configurer.setSaveAndRestore(true);
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, "TOP_RIGHT");
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR, false);
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        checkDefaultSubject();
        initProperties();
    }

    /**
     * �����ݿ�û�л�ƿ�Ŀ��ݣ����Զ�����Ĭ�ϵĻ�ƿ�Ŀ���
     */
    private void checkDefaultSubject() {
        int subjectCount = subjectService.countAll();
        if (subjectCount <= 0) {
            this.initSubjects();
        }
    }

    private void initSubjects() {
        InputStream subjectsInputStream = getClass().getResourceAsStream("/default-subjects.xml");
        Document document = XmlHelper.parse(subjectsInputStream);
        Element root = document.getRootElement();
        for (Iterator iter = root.elementIterator(); iter.hasNext(); ) {
            Element subjectNode = (Element) iter.next();
            this.saveSubject(null, subjectNode);
        }
    }

    /**
     * �õݹ��㷨����һ����Ŀ�ڵ�
     * 
     * @param menuNode
     */
    private void saveSubject(AccountSubjectBo parent, Element subjectNode) {
        String type = subjectNode.attributeValue("type");
        SubjectType subjectType = SubjectType.fromInt(Integer.parseInt(type));
        String code = subjectNode.attributeValue("code");
        String name = subjectNode.attributeValue("name");
        String direction = subjectNode.attributeValue("direction");
        Direction subjectDirection = Direction.fromInt(Integer.parseInt(direction));
        String description = subjectNode.attributeValue("description");
        AccountSubjectBo subject = AccountSubjectBo.Factory.newInstance();
        subject.setParent(parent);
        subject.setSubjectType(subjectType);
        subject.setDirection(subjectDirection);
        if (code != null && code.length() > 0) {
            subject.setSubjectCode(code);
        }
        if (name != null && name.length() > 0) {
            subject.setSubjectName(name);
        }
        if (description != null && description.length() > 0) {
            subject.setDescription(description);
        }
        AccountSubjectBo savedSubject = ServiceLocator.instance().getAccountSubjectService().store(subject);
        List children = subjectNode.elements("subject");
        if (children != null && children.size() > 0) {
            for (Iterator iter = children.iterator(); iter.hasNext(); ) {
                Element childSubjectNode = (Element) iter.next();
                this.saveSubject(savedSubject, childSubjectNode);
            }
        }
    }

    private void initProperties() {
        String backupDir = Util.propertiesService.getProperty(Properties.BACKUP_DIRECTORY.getValue());
        if (backupDir == null || backupDir.trim().length() <= 0) {
            Util.propertiesService.setProperty(Properties.BACKUP_DIRECTORY.getValue(), "prima-backup");
        }
    }
}
