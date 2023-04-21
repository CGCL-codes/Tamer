package playground.balmermi.census2000v2.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.matsim.basic.v01.Id;
import org.matsim.basic.v01.IdImpl;
import org.matsim.facilities.Facilities;
import org.matsim.facilities.Facility;
import org.matsim.gbl.Gbl;
import org.matsim.plans.Person;
import org.matsim.plans.Plans;
import org.matsim.world.Layer;
import playground.balmermi.census2000.data.Municipalities;
import playground.balmermi.census2000.data.Municipality;

public class Households {

    private final HashMap<Id, Household> households = new HashMap<Id, Household>();

    private final Municipalities municipalities;

    public Households(Municipalities municipalities) {
        super();
        this.municipalities = municipalities;
    }

    public final Household getHousehold(final Id id) {
        return this.households.get(id);
    }

    public final HashMap<Id, Household> getHouseholds() {
        return this.households;
    }

    public final Municipalities getMunicipalities() {
        return this.municipalities;
    }

    public final void addHH(Household hh) {
        this.households.put(hh.getId(), hh);
    }

    public final void parse(String infile, Plans plans) {
        Layer fl = Gbl.getWorld().getLayer(Facilities.LAYER_TYPE);
        int line_cnt = 0;
        try {
            FileReader fr = new FileReader(infile);
            BufferedReader br = new BufferedReader(fr);
            String curr_line = br.readLine();
            line_cnt++;
            while ((curr_line = br.readLine()) != null) {
                String[] entries = curr_line.split("\t", -1);
                Id hhid = new IdImpl(entries[0]);
                Id zid = new IdImpl(entries[1]);
                int mid = Integer.parseInt(zid.toString());
                Id fid = new IdImpl(entries[2]);
                int hhtpw = Integer.parseInt(entries[3]);
                int hhtpz = Integer.parseInt(entries[4]);
                Municipality m = this.municipalities.getMunicipality(mid);
                Facility f = (Facility) fl.getLocation(fid);
                Household hh = new Household(hhid, m, f);
                if (hhtpw != Integer.MIN_VALUE) {
                    hh.setHHTPW(hhtpw);
                }
                if (hhtpz != Integer.MIN_VALUE) {
                    hh.setHHTPZ(hhtpz);
                }
                this.addHH(hh);
                String p_w_list = entries[5];
                String p_z_list = entries[6];
                entries = p_w_list.split(";", -1);
                for (int i = 0; i < entries.length - 1; i++) {
                    Id pid = new IdImpl(entries[i]);
                    Person p = plans.getPerson(pid);
                    if (p == null) {
                        Gbl.errorMsg("that should not happen!");
                    }
                    if (hh.getPersonsW().put(p.getId(), p) != null) {
                        Gbl.errorMsg("that should not happen!");
                    }
                    if (p.getCustomAttributes().put(CAtts.HH_W, hh) != null) {
                        Gbl.errorMsg("hhid=" + hh.getId() + ", pid=" + p.getId() + ": person does already have a " + CAtts.HH_W + " assigned!");
                    }
                }
                entries = p_z_list.split(";", -1);
                for (int i = 0; i < entries.length - 1; i++) {
                    Id pid = new IdImpl(entries[i]);
                    Person p = plans.getPerson(pid);
                    if (p == null) {
                        Gbl.errorMsg("that should not happen!");
                    }
                    if (hh.getPersonsZ().put(p.getId(), p) != null) {
                        Gbl.errorMsg("that should not happen!");
                    }
                    if (p.getCustomAttributes().put(CAtts.HH_Z, hh) != null) {
                        Gbl.errorMsg("hhid=" + hh.getId() + ", pid=" + p.getId() + ": person does already have a " + CAtts.HH_Z + " assigned!");
                    }
                }
                line_cnt++;
            }
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public final void print() {
        System.out.println("---------- printing households ----------");
        System.out.println(this.toString());
        for (Household hh : this.households.values()) {
            System.out.println(hh.toString());
        }
        System.out.println("------- printing households done. -------");
    }

    public final void writeTable(String outfile) {
        try {
            FileWriter fw = new FileWriter(outfile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write("hh_id\tz_id\tf_id\thhtpw\thhtpz\tp_w_list\tp_z_list\n");
            out.flush();
            for (Household hh : this.households.values()) {
                out.write(hh.getId() + "\t" + hh.getMunicipality().getZone().getId() + "\t" + hh.getFacility().getId() + "\t" + hh.getHHTPW() + "\t" + hh.getHHTPZ() + "\t");
                for (Id id : hh.getPersonsW().keySet()) {
                    out.write(id + ";");
                }
                out.write("\t");
                for (Id id : hh.getPersonsZ().keySet()) {
                    out.write(id + ";");
                }
                out.write("\n");
            }
            out.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public final String toString() {
        return "[nof_munis=" + this.municipalities.getMunicipalities().size() + "]" + "[nof_hhs=" + this.households.size() + "]";
    }
}
