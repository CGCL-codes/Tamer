package org.sidora.core.context;

import java.awt.BasicStroke;
import java.awt.Color;
import java.net.URL;
import java.util.HashSet;
import javax.swing.ImageIcon;
import org.sidora.core.Category;
import org.sidora.core.MeasureSet;
import org.sidora.core.Variable;

/**
 * EmptyedContext
 * @author Enric Tartera, Juan Manuel Gimeno, Roger Masgoret
 * @version 1.0
 */
public class EmptyedContext extends Context {

    public Category plan;

    public Category section;

    private MeasureSet measures;

    public EmptyedContext() {
        measures = new MeasureSet();
    }

    public ImageIcon getIcon() {
        return new javax.swing.ImageIcon(getClass().getResource("/org/sidora/core/context/resources/emptyed16.png"));
    }

    public String getStringClass() {
        return "Emptyed";
    }

    public HashSet<Variable> getVariableSet() {
        HashSet<Variable> vSet = new HashSet<Variable>();
        String[] labels = { "Planta", "Secci�", "Amplada", "Llargada", "Al?ada" };
        for (int i = 0; i < labels.length; i++) {
            Variable var = new Variable();
            var.setLabel(labels[i]);
            var.setReferencedClass(this.getStringClass());
            vSet.add(var);
        }
        return vSet;
    }

    public String outputVariables() {
        String pl = "";
        if (getPlan() != null) pl = getPlan().getValue();
        String sc = "";
        if (getSection() != null) sc = getSection().getValue();
        StringBuffer buf = new StringBuffer();
        buf.append("<html>");
        buf.append("<body>");
        buf.append("<table width=\'100%\' border=\'0\' cellspacing=\'0\' cellpadding=\'1\' bgcolor=\'white\'>");
        buf.append("		<tr align=\'right\'bgcolor=\'#C384B3\'>");
        buf.append("			<th align=\'right\'><font size=\'3\'color=\'black\' face=\'Lucida\'>Categories:</font></th>");
        buf.append("			<th align=\'left\'></th>");
        buf.append("		</tr>");
        buf.append("		<tr>");
        buf.append("			<td align=\'right\' width=\'25%\'><font size=\'3\'color=\'black\' face=\'Lucida\'><em>Planta:</em></font></td>");
        buf.append("			<td><font size=\'3\'color=\'black\' face=\'Lucida\'>" + pl + "</font></td>");
        buf.append("		</tr>");
        buf.append("		<tr>");
        buf.append("			<td align=\'right\' width=\'25%\'><font size=\'3\'color=\'black\' face=\'Lucida\'><em>Secci�:</em></font></td>");
        buf.append("			<td><font size=\'3\'color=\'black\' face=\'Lucida\'>" + sc + "</font></td>");
        buf.append("		</tr>");
        buf.append("</table>");
        if (getMeasures() != null) {
            buf.append("<table width=\'100%\' border=\'0\' cellspacing=\'0\' cellpadding=\'1\' bgcolor=\'white\'>");
            buf.append("		<tr align=\'right\'bgcolor=\'#fac1a2\'>");
            buf.append("			<th align=\'right\'><font size=\'3\'color=\'black\' face=\'Lucida\'>Mides:</font></th>");
            buf.append("			<th align=\'left\'></th>");
            buf.append("		</tr>");
            buf.append("		<tr>");
            buf.append("			<td align=\'right\' width=\'25%\'><font size=\'3\'color=\'black\' face=\'Lucida\'><em>Ample:</em></font></td>");
            buf.append("			<td><font size=\'3\'color=\'black\' face=\'Lucida\'>" + getMeasures().getLength() + "</font></td>");
            buf.append("		</tr>");
            buf.append("		<tr>");
            buf.append("			<td align=\'right\' width=\'25%\'><font size=\'3\'color=\'black\' face=\'Lucida\'><em>Llarg:</em></font></td>");
            buf.append("			<td><font size=\'3\'color=\'black\' face=\'Lucida\'>" + getMeasures().getWidth() + "</font></td>");
            buf.append("		</tr>");
            buf.append("		<tr>");
            buf.append("			<td align=\'right\' width=\'25%\'><font size=\'3\'color=\'black\' face=\'Lucida\'><em>Alt:</em></font></td>");
            buf.append("			<td><font size=\'3\'color=\'black\' face=\'Lucida\'>" + getMeasures().getHeight() + "</font></td>");
            buf.append("		</tr>");
            buf.append("</table>");
        }
        buf.append("</body>");
        buf.append("</html>");
        return buf.toString();
    }

    public Category getPlan() {
        return plan;
    }

    public void setPlan(Category plan) {
        this.plan = plan;
    }

    public Category getSection() {
        return section;
    }

    public void setSection(Category section) {
        this.section = section;
    }

    public MeasureSet getMeasures() {
        return measures;
    }

    public void setMeasures(MeasureSet measures) {
        this.measures = measures;
    }
}
