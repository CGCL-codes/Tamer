package net.sf.mustang.xservice.task;

import org.dom4j.Document;
import net.sf.mustang.bean.Bean;
import net.sf.mustang.xbean.XBean;
import net.sf.mustang.xbean.XBeanManager;
import net.sf.mustang.xservice.TaskInfo;
import net.sf.mustang.service.ServiceContext;
import net.sf.mustang.xml.BeanXMLTool;

public class XFillTask implements Task {

    public void execute(ServiceContext serviceContext, TaskInfo taskInfo) throws Exception {
        String refName = taskInfo.getAttribute("xbean");
        XBean xBean = XBeanManager.getInstance().getXBean(serviceContext.getRequest(), refName);
        if (xBean == null) throw new Exception("no xbean to xfill for: " + refName);
        if (!serviceContext.containsKey(xBean.getFlatRefName())) serviceContext.put(xBean.getFlatRefName(), xBean);
        Document doc = serviceContext.getRequest().getDocument();
        if (doc != null) {
            Bean bean = BeanXMLTool.docToBean(doc, xBean.getFlatRefName());
            if (bean != null) xBean.fetchFromBean(serviceContext.getRequest(), bean);
        }
    }
}
