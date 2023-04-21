package net.sf.beanreports.jasper.classdesc;

import net.sf.beanreports.jasper.JasperGeneratorConfig;
import net.sf.beanreports.jasper.annotation.ReportInfo;

public class JasperCollectionField extends JasperDataField {

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public JasperCollectionField clone(String overrideFormula) {
        JasperCollectionField retVal = new JasperCollectionField(jasperGeneratorConfig, context, reportInfo, name, overrideFormula, shortCollection, classForCollection);
        retVal.label = label;
        return retVal;
    }

    public JasperCollectionField(JasperGeneratorConfig jasperGeneratorConfig, String context, ReportInfo reportInfo, String name, String formula, boolean shortCollection, Class<?> classForCollection) {
        super(jasperGeneratorConfig, context, reportInfo, name, formula);
        this.shortCollection = shortCollection;
        this.classForCollection = classForCollection;
    }

    boolean shortCollection;

    Class<?> classForCollection;

    public boolean isShortCollection() {
        return shortCollection;
    }

    public Class<?> getClassForCollection() {
        return classForCollection;
    }

    @Override
    public int getDeclaredHeight() {
        return 1;
    }

    @Override
    public String toString() {
        return "JasperCollectionField [classForCollection=" + classForCollection + ", shortCollection=" + shortCollection + ", formula=" + formula + ", name=" + name + ", context=" + context + ", height=" + height + ", jasperGeneratorConfig=" + jasperGeneratorConfig + ", reportInfo=" + reportInfo + ", verticalPosition=" + verticalPosition + "]";
    }
}
