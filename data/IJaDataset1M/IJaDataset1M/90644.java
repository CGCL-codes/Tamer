package org.adapit.wctoolkit.uml.ext.fomda.xml;

import java.util.Iterator;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.FeatureElement;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.FeatureRelation;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.TransformationFeatureElement;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;

@SuppressWarnings({ "unchecked" })
public class XMLFeatureExporter {

    public String getFeatureElementAttributes(FeatureElement feature, int tab) {
        String str = "";
        if (feature.getParentRelation() == null) str += "<bean id=\"" + "root" + "\" class=\"" + this.getClass().getName() + "\" singleton=\"true\">"; else str += "<bean id=\"" + "paramValue" + feature.getId() + "\" class=\"" + this.getClass().getName() + "\" singleton=\"true\">";
        str += '\n';
        str += '\t';
        str += "<property name=\"name\">";
        str += '\n';
        str += '\t';
        str += '\t';
        str += "<value>" + feature.getName() + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"id\">";
        str += '\n';
        str += '\t';
        str += '\t';
        str += "<value>" + feature.getId() + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"description\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String desc = "";
        if (feature.getDescription() != null) desc += feature.getDescription();
        str += "<value>" + desc + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"bindingTime\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String bin = "";
        if (feature.getBindingTime() != null) bin += feature.getBindingTime();
        str += "<value>" + bin + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"category\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String cat = "";
        if (feature.getCategory() != null) cat += feature.getCategory();
        str += "<value>" + cat + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        str += "<property name=\"nature\">";
        str += '\n';
        str += '\t';
        str += '\t';
        String nat = "";
        if (feature.getNature() != null) nat += feature.getNature();
        str += "<value>" + nat + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        str += '\n';
        str += '\t';
        {
            Iterator it2 = feature.getIssuesAndDecisions().iterator();
            if (feature.getIssuesAndDecisions().size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"issuesAndDecisions\">";
                str += '\n';
                str += '\t';
                str += "<list>";
                while (it2.hasNext()) {
                    String s = (String) it2.next();
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += "<value>" + s + "</value>";
                }
                str += '\n';
                str += '\t';
                str += "</list>";
                str += '\n';
                str += '\t';
                str += "</property>";
            }
        }
        {
            Iterator it2 = feature.getRationals().iterator();
            if (feature.getRationals().size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"rationals\">";
                str += '\n';
                str += '\t';
                str += "<list>";
                while (it2.hasNext()) {
                    String s = (String) it2.next();
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += "<value>" + s + "</value>";
                }
                str += '\n';
                str += '\t';
                str += "</list>";
                str += '\n';
                str += '\t';
                str += "</property>";
            }
        }
        {
            Iterator it2 = feature.getCompositionRules().iterator();
            if (feature.getCompositionRules().size() > 0) {
                str += '\n';
                str += '\t';
                str += "<property name=\"compositionRules\">";
                str += '\n';
                str += '\t';
                str += "<list>";
                while (it2.hasNext()) {
                    String s = (String) it2.next();
                    str += '\n';
                    str += '\t';
                    str += '\t';
                    str += "<value>" + s + "</value>";
                }
                str += '\n';
                str += '\t';
                str += "</list>";
                str += '\n';
                str += '\t';
                str += "</property>";
            }
        }
        str += '\n';
        str += '\t';
        str += "<property name=\"selected\">";
        str += '\n';
        str += '\t';
        str += '\t';
        str += "<value>" + feature.isSelected() + "</value>";
        str += '\n';
        str += '\t';
        str += "</property>";
        Iterator it = feature.getRelations().iterator();
        if (feature.getRelations().size() > 0) {
            str += '\n';
            str += '\t';
            str += "<property name=\"relations\">";
            str += '\n';
            str += '\t';
            str += "<list>";
            while (it.hasNext()) {
                FeatureRelation r = (FeatureRelation) it.next();
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<ref bean=\"" + "paramValue" + r.getId() + "\"/>";
            }
            str += '\n';
            str += '\t';
            str += "</list>";
            str += '\n';
            str += '\t';
            str += "</property>";
        }
        return str;
    }

    public String getFOMDAFeatureAttributes(TransformationFeatureElement feature, int tab) {
        String str = "";
        Iterator it3 = feature.getTransformationDescriptors().iterator();
        if (feature.getTransformationDescriptors().size() > 0) {
            str += '\n';
            str += '\t';
            str += "<property name=\"transformationDescriptors\">";
            str += '\n';
            str += '\t';
            str += "<list>";
            while (it3.hasNext()) {
                TransformationDescriptor tr = (TransformationDescriptor) it3.next();
                str += '\n';
                str += '\t';
                str += '\t';
                str += "<ref bean=\"" + "paramValue" + tr.getId() + "\"/>";
            }
            str += '\n';
            str += '\t';
            str += "</list>";
            str += '\n';
            str += '\t';
            str += "</property>";
        }
        return str;
    }

    public String toXMLBeanFactory(FeatureElement feature, int tab) {
        String str = "";
        str += getFeatureElementAttributes(feature, tab);
        str += getFOMDAFeatureAttributes((TransformationFeatureElement) feature, tab);
        str += '\n';
        str += "</bean>";
        return str;
    }
}
