package org.matsim.population.algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.matsim.api.basic.v01.Coord;
import org.matsim.core.api.facilities.ActivityOption;
import org.matsim.core.api.population.Person;
import org.matsim.core.gbl.Gbl;
import org.matsim.knowledges.ActivitySpace;
import org.matsim.knowledges.ActivitySpaceBean;
import org.matsim.knowledges.ActivitySpaceCassini;
import org.matsim.knowledges.ActivitySpaceEllipse;
import org.matsim.knowledges.ActivitySpaceSuperEllipse;
import org.matsim.knowledges.Knowledge;
import org.matsim.knowledges.Knowledges;

public class PersonDrawActivtiySpaces extends AbstractPersonAlgorithm {

    private Knowledges knowledges;

    public PersonDrawActivtiySpaces(Knowledges knowledges) {
        super();
        this.knowledges = knowledges;
    }

    private final void writeLocations(ActivityOption a, FileWriter fw) throws IOException {
        BufferedWriter out = new BufferedWriter(fw);
        Coord c = a.getFacility().getCoord();
        out.write(c.getX() + "\t" + c.getY() + "\n");
        out.flush();
        out.close();
    }

    private final double beanPointCheck(double x0, double y0, double theta, double x, double y, double a, double b) {
        double tmp_x = (Math.cos(theta) * x + Math.sin(theta) * y) - ((Math.cos(theta) * x0 + Math.sin(theta) * y0) - a / 2.0);
        double tmp_y = (-Math.sin(theta) * x + Math.cos(theta) * y) - (-Math.sin(theta) * x0 + Math.cos(theta) * y0);
        double indicator = Math.pow(tmp_y, 4.0) - Math.pow(tmp_y, 2.0) * b * b * (tmp_x / a) * (1.0 - tmp_x / a) + Math.pow(tmp_x / a, 4.0) * Math.pow(b, 4.0) - Math.pow(b, 4.0) * Math.pow(tmp_x / a, 3.0);
        return indicator;
    }

    @Override
    public void run(Person person) {
        final Knowledge know = this.knowledges.getKnowledgesByPersonId().get(person.getId());
        if (know == null) {
            Gbl.errorMsg("Knowledge is not defined!");
        }
        try {
            FileWriter fw_gpl = new FileWriter("output/pid" + person.getId() + "_act-spaces.gpl");
            BufferedWriter out_gpl = new BufferedWriter(fw_gpl);
            out_gpl.write("set term postscript eps color solid 20\n");
            out_gpl.write("set output \"pid" + person.getId() + "_act-spaces.eps\"\n");
            out_gpl.write("set pointsize 2\n");
            out_gpl.write("set grid\n");
            out_gpl.write("set xlabel \"x coordinate\"\n");
            out_gpl.write("set ylabel \"y coordinate\"\n");
            out_gpl.write("#unset key\n");
            out_gpl.write("plot \\\n");
            Iterator<ActivityOption> a_it = this.knowledges.getKnowledgesByPersonId().get(person.getId()).getActivities().iterator();
            while (a_it.hasNext()) {
                ActivityOption a = a_it.next();
                String at = a.getType();
                if (at.equals("home")) {
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_locations_home.dat");
                    this.writeLocations(a, fw);
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_locations_home.dat\" using 1:2 title \"home location\" with points 1 6, \\\n");
                } else if (at.equals("work")) {
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_locations_work.dat");
                    this.writeLocations(a, fw);
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_locations_work.dat\" using 1:2 title \"work location\" with points 7 8, \\\n");
                } else if (at.equals("education")) {
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_locations_education.dat");
                    this.writeLocations(a, fw);
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_locations_education.dat\" using 1:2 title \"education location\" with points 4 3, \\\n");
                } else if (at.equals("shop")) {
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_locations_shop.dat");
                    this.writeLocations(a, fw);
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_locations_shop.dat\" using 1:2 title \"shop location\" with points 9 1, \\\n");
                } else if (at.equals("leisure")) {
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_locations_leisure.dat");
                    this.writeLocations(a, fw);
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_locations_leisure.dat\" using 1:2 title \"leisure location\" with points 3 2, \\\n");
                } else {
                    Gbl.errorMsg("[at=" + at + " not known]");
                }
            }
            int nof_as = this.knowledges.getKnowledgesByPersonId().get(person.getId()).getActivitySpaces().size();
            int cnt = 0;
            Iterator<ActivitySpace> as_it = this.knowledges.getKnowledgesByPersonId().get(person.getId()).getActivitySpaces().iterator();
            while (as_it.hasNext()) {
                cnt++;
                ActivitySpace as = as_it.next();
                String act_type = as.getActType();
                if (as instanceof ActivitySpaceEllipse) {
                    ActivitySpaceEllipse ase = (ActivitySpaceEllipse) as;
                    double a = ase.getParam("a").doubleValue();
                    double b = ase.getParam("b").doubleValue();
                    double theta = ase.getParam("theta").doubleValue();
                    double x = ase.getParam("x").doubleValue();
                    double y = ase.getParam("y").doubleValue();
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_ellipse_" + act_type + ".dat");
                    BufferedWriter out = new BufferedWriter(fw);
                    for (double t = 0.0; t < 2.0 * Math.PI; t = t + 2.0 * Math.PI / 360.0) {
                        double p_x = a * Math.cos(t) * Math.cos(theta) - b * Math.sin(t) * Math.sin(theta) + x;
                        double p_y = a * Math.cos(t) * Math.sin(theta) + b * Math.sin(t) * Math.cos(theta) + y;
                        out.write(p_x + "\t" + p_y + "\n");
                    }
                    out.flush();
                    out.close();
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_ellipse_" + act_type + ".dat\" using 1:2 title \"" + act_type + " ellipse\" with lines");
                    if (act_type.equals("home")) {
                        out_gpl.write(" 1 2");
                    } else if (act_type.equals("work")) {
                        out_gpl.write(" 7 2");
                    } else if (act_type.equals("leisure")) {
                        out_gpl.write(" 3 2");
                    } else if (act_type.equals("education")) {
                        out_gpl.write(" 4 2");
                    } else if (act_type.equals("shop")) {
                        out_gpl.write(" 9 2");
                    } else if (act_type.equals("all")) {
                        out_gpl.write(" 0 2");
                    } else {
                        Gbl.errorMsg("[act_type=" + act_type + " not known!]");
                    }
                    if (nof_as != cnt) {
                        out_gpl.write(", \\\n");
                    }
                } else if (as instanceof ActivitySpaceCassini) {
                    ActivitySpaceCassini asc = (ActivitySpaceCassini) as;
                    double a = asc.getParam("a").doubleValue();
                    double b = asc.getParam("b").doubleValue();
                    double theta = asc.getParam("theta").doubleValue();
                    double x = asc.getParam("x").doubleValue();
                    double y = asc.getParam("y").doubleValue();
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_cassini_" + act_type + ".dat");
                    BufferedWriter out = new BufferedWriter(fw);
                    for (double t = 0.0; t < 2.0 * Math.PI; t = t + 2.0 * Math.PI / 360.0) {
                        double p_x = a * (Math.pow((Math.cos(2 * t) + Math.pow((Math.pow((b / a), 4) - Math.pow((Math.sin(2 * t) / a), 2)), 0.5)), 0.5)) * Math.cos(theta + t) + x;
                        double p_y = a * (Math.pow((Math.cos(2 * t) + Math.pow((Math.pow((b / a), 4) - Math.pow((Math.sin(2 * t) / a), 2)), 0.5)), 0.5)) * Math.sin(theta + t) + y;
                        out.write(p_x + "\t" + p_y + "\n");
                    }
                    out.flush();
                    out.close();
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_cassini_" + act_type + ".dat\" using 1:2 title \"" + act_type + " cassini\" with lines");
                    if (act_type.equals("home")) {
                        out_gpl.write(" 1 2");
                    } else if (act_type.equals("work")) {
                        out_gpl.write(" 7 2");
                    } else if (act_type.equals("leisure")) {
                        out_gpl.write(" 3 2");
                    } else if (act_type.equals("education")) {
                        out_gpl.write(" 4 2");
                    } else if (act_type.equals("shop")) {
                        out_gpl.write(" 9 2");
                    } else if (act_type.equals("all")) {
                        out_gpl.write(" 0 2");
                    } else {
                        Gbl.errorMsg("[act_type=" + act_type + " not known!]");
                    }
                    if (nof_as != cnt) {
                        out_gpl.write(", \\\n");
                    }
                } else if (as instanceof ActivitySpaceSuperEllipse) {
                    ActivitySpaceSuperEllipse ass = (ActivitySpaceSuperEllipse) as;
                    double a = ass.getParam("a").doubleValue();
                    double b = ass.getParam("b").doubleValue();
                    double theta = ass.getParam("theta").doubleValue();
                    double x = ass.getParam("x").doubleValue();
                    double y = ass.getParam("y").doubleValue();
                    double r = ass.getParam("r").doubleValue();
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_superellipse_" + act_type + ".dat");
                    BufferedWriter out = new BufferedWriter(fw);
                    double[] p_xs = new double[360];
                    double[] p_ys = new double[360];
                    int n = 0;
                    for (double t = 0.0; t < Math.PI / 2.0; t = t + Math.PI / 180.0) {
                        double p_x1 = a * (Math.pow((Math.cos(t)), (2.00 / r))) * Math.cos(theta) - b * (Math.pow((Math.sin(t)), (2.00 / r))) * Math.sin(theta) + x;
                        double p_y1 = a * (Math.pow((Math.cos(t)), (2.00 / r))) * Math.sin(theta) + b * (Math.pow((Math.sin(t)), (2.00 / r))) * Math.cos(theta) + y;
                        double p_x3 = 2.0 * x - p_x1;
                        double p_y3 = 2.0 * y - p_y1;
                        double s = ((p_y3 - y) - (p_x3 - x) * Math.tan(theta)) / (1.0 + Math.tan(theta) * Math.tan(theta));
                        double xm = p_x3 + Math.tan(theta) * s;
                        double ym = p_y3 - s;
                        double p_x2 = 2.0 * xm - p_x3;
                        double p_y2 = 2.0 * ym - p_y3;
                        s = ((p_y1 - y) - (p_x1 - x) * Math.tan(theta)) / (1.0 + Math.tan(theta) * Math.tan(theta));
                        xm = p_x1 + Math.tan(theta) * s;
                        ym = p_y1 - s;
                        double p_x4 = 2.0 * xm - p_x1;
                        double p_y4 = 2.0 * ym - p_y1;
                        p_xs[n] = p_x1;
                        p_ys[n] = p_y1;
                        p_xs[179 - n] = p_x2;
                        p_ys[179 - n] = p_y2;
                        p_xs[180 + n] = p_x3;
                        p_ys[180 + n] = p_y3;
                        p_xs[359 - n] = p_x4;
                        p_ys[359 - n] = p_y4;
                        if (n >= 90) {
                            Gbl.errorMsg("[n=" + n + "] Something is wrong!");
                        }
                        n++;
                    }
                    for (int i = 0; i < p_xs.length; i++) {
                        out.write(p_xs[i] + "\t" + p_ys[i] + "\n");
                    }
                    out.flush();
                    out.close();
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_superellipse_" + act_type + ".dat\" using 1:2 title \"" + act_type + " superellipse\" with lines");
                    if (act_type.equals("home")) {
                        out_gpl.write(" 1 2");
                    } else if (act_type.equals("work")) {
                        out_gpl.write(" 7 2");
                    } else if (act_type.equals("leisure")) {
                        out_gpl.write(" 3 2");
                    } else if (act_type.equals("education")) {
                        out_gpl.write(" 4 2");
                    } else if (act_type.equals("shop")) {
                        out_gpl.write(" 9 2");
                    } else if (act_type.equals("all")) {
                        out_gpl.write(" 0 2");
                    } else {
                        Gbl.errorMsg("[act_type=" + act_type + " not known!]");
                    }
                    if (nof_as != cnt) {
                        out_gpl.write(", \\\n");
                    }
                } else if (as instanceof ActivitySpaceBean) {
                    ActivitySpaceBean asb = (ActivitySpaceBean) as;
                    double a = asb.getParam("a").doubleValue();
                    double b = asb.getParam("b").doubleValue();
                    double theta = asb.getParam("theta").doubleValue();
                    double x = asb.getParam("x").doubleValue();
                    double y = asb.getParam("y").doubleValue();
                    FileWriter fw = new FileWriter("output/pid" + person.getId() + "_bean_" + act_type + ".dat");
                    BufferedWriter out = new BufferedWriter(fw);
                    for (double t = 0.0; t < 2.0 * Math.PI; t = t + 2.0 * Math.PI / 360.0) {
                        double vx_in = 0.0;
                        double vy_in = 0.0;
                        double vx_out = Math.cos(theta + t);
                        double vy_out = Math.sin(theta + t);
                        double vx_new = vx_in;
                        double vy_new = vy_in;
                        double prev_vx_new = vx_out;
                        double prev_vy_new = vy_out;
                        while (beanPointCheck(x, y, theta, x + vx_out, y + vy_out, a, b) <= 0.0) {
                            vx_out = 2.0 * vx_out;
                            vy_out = 2.0 * vy_out;
                        }
                        while (beanPointCheck(x, y, theta, x + vx_new, y + vy_new, a, b) != beanPointCheck(x, y, theta, x + prev_vx_new, y + prev_vy_new, a, b)) {
                            prev_vx_new = vx_new;
                            prev_vy_new = vy_new;
                            vx_new = (vx_in + vx_out) / 2.0;
                            vy_new = (vy_in + vy_out) / 2.0;
                            if (beanPointCheck(x, y, theta, x + vx_new, y + vy_new, a, b) < 0.0) {
                                vx_in = vx_new;
                                vy_in = vy_new;
                            } else if (beanPointCheck(x, y, theta, x + vx_new, y + vy_new, a, b) > 0.0) {
                                vx_out = vx_new;
                                vy_out = vy_new;
                            }
                        }
                        out.write((x + vx_new) + "\t" + (y + vy_new) + "\n");
                    }
                    out.flush();
                    out.close();
                    fw.close();
                    out_gpl.write("  \"pid" + person.getId() + "_bean_" + act_type + ".dat\" using 1:2 title \"" + act_type + " bean\" with lines");
                    if (act_type.equals("home")) {
                        out_gpl.write(" 1 2");
                    } else if (act_type.equals("work")) {
                        out_gpl.write(" 7 2");
                    } else if (act_type.equals("leisure")) {
                        out_gpl.write(" 3 2");
                    } else if (act_type.equals("education")) {
                        out_gpl.write(" 4 2");
                    } else if (act_type.equals("shop")) {
                        out_gpl.write(" 9 2");
                    } else if (act_type.equals("all")) {
                        out_gpl.write(" 0 2");
                    } else {
                        Gbl.errorMsg("[act_type=" + act_type + " not known!]");
                    }
                    if (nof_as != cnt) {
                        out_gpl.write(", \\\n");
                    }
                }
            }
            out_gpl.flush();
            out_gpl.close();
            fw_gpl.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
