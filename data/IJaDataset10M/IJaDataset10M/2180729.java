package org.commonlibrary.lcms.web.springmvc.support.tags;

import org.commonlibrary.lcms.model.*;
import org.commonlibrary.lcms.standard.service.StandardHierarchyService;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

/**
 * @author: Rodrigo Bartels
 * Date: 07/10/2008
 * Time: 10:29:25 AM
 */
public class LearningObjectTopicsTag extends TagSupport {

    private LearningObject learningObject;

    private StandardHierarchyService standardHierarchyService;

    public void setLearningObject(Object learningObject) {
        if (learningObject instanceof LearningObject) this.learningObject = (LearningObject) learningObject;
    }

    public void setStandardHierarchyService(Object standardHierarchyService) {
        if (standardHierarchyService instanceof StandardHierarchyService) {
            this.standardHierarchyService = (StandardHierarchyService) standardHierarchyService;
        }
    }

    public int doStartTag() throws JspException {
        if (learningObject == null) throw new JspException("Please set the Learning Object to a valid " + LearningObject.class.getName() + " object!");
        try {
            JspWriter out = pageContext.getOut();
            List<StandardTopic> topics = learningObject.getAlignedTopics();
            out.write("[\n");
            printNodes(out, topics);
            out.write("\n]");
            return SKIP_BODY;
        } catch (IOException e) {
            e.printStackTrace();
            throw new JspException(e);
        }
    }

    private void printNodes(JspWriter out, List<StandardTopic> topics) throws IOException {
        if (topics != null) {
            boolean first = true;
            for (StandardTopic standardTopic : topics) {
                if (first) first = false; else out.write(",");
                StringBuilder builder = new StringBuilder();
                out.write("{");
                this.buildPathToTopic(standardTopic, builder);
                out.write("\"text\" : \"" + builder.toString() + "\",");
                out.write("\"id\" : \"" + standardTopic.getId() + "\",");
                out.write("\"leaf\" : " + true + ",");
                out.write("\"children\" : [ ]");
                out.write("}\n");
            }
        }
    }

    private StringBuilder buildPathToTopic(StandardElement standardElement, StringBuilder name) {
        boolean isLeaf = false;
        if (standardElement.getStandardElementType().compareTo(StandardElementType.CATEGORY) == 0) {
            StandardCategory standardCategoryParent = standardHierarchyService.getStandardCategoryParent(standardElement.getId());
            if (standardCategoryParent != null) {
                buildPathToTopic(standardCategoryParent, name);
            }
            isLeaf = standardHierarchyService.getStandardCategoryChildren(standardElement.getId()).isEmpty();
        } else {
            StandardElement standardTopicParent = standardHierarchyService.getStandardTopicParent(standardElement.getId());
            if (standardTopicParent != null) {
                buildPathToTopic(standardTopicParent, name);
            }
            isLeaf = standardHierarchyService.getStandardTopicChildren(standardElement.getId()).isEmpty();
        }
        name.append(standardElement.getLabel());
        if (!isLeaf) name.append(" >> ");
        return name;
    }

    private StringBuilder buildPathUpToTopic(StandardElement standardElement, StringBuilder name) {
        if (standardElement.getStandardElementType().compareTo(StandardElementType.CATEGORY) == 0) {
            StandardCategory standardCategory = (StandardCategory) standardElement;
            buildPathToTopic(standardCategory.getParent(), name);
            name.append(standardCategory.getLabel()).append("> ");
        } else {
            StandardTopic standardTopic = (StandardTopic) standardElement;
            buildPathToTopic(standardTopic.getParent(), name);
            name.append(standardTopic.getLabel()).append("> ");
        }
        return name;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
