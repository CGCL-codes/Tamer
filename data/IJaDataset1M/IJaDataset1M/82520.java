package playground.balmermi.census2000.modules;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import org.matsim.plans.Person;
import org.matsim.plans.Plan;
import org.matsim.plans.algorithms.PersonAlgorithm;
import org.matsim.plans.algorithms.PlanAlgorithmI;
import playground.balmermi.census2000.data.Household;
import playground.balmermi.census2000.data.Municipality;
import playground.balmermi.census2000.data.Persons;

public class PersonMunicipalitySummaryTable extends PersonAlgorithm implements PlanAlgorithmI {

    private static final String YES = "yes";

    private FileWriter fw = null;

    private BufferedWriter out = null;

    private final Persons persons;

    TreeMap<Municipality, int[]> munis = new TreeMap<Municipality, int[]>();

    TreeSet<Household> hhs = new TreeSet<Household>();

    public PersonMunicipalitySummaryTable(String outfile, Persons persons) {
        super();
        System.out.println("    init " + this.getClass().getName() + " module...");
        this.persons = persons;
        try {
            fw = new FileWriter(outfile);
            out = new BufferedWriter(fw);
            out.write("m_id\tk_id\treg_type\tincome\tfuelcost\t" + "p_cnt\thh_cnt\tage0-17\tage18-65\tage66-\t" + "male\tswiss\temployed\tlicense\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("    done.");
    }

    public final void close() {
        try {
            Iterator<Municipality> m_it = this.munis.keySet().iterator();
            while (m_it.hasNext()) {
                Municipality muni = m_it.next();
                int[] v = this.munis.get(muni);
                out.write(muni.getId() + "\t");
                out.write(muni.getCantonId() + "\t");
                out.write(muni.getRegType() + "\t");
                out.write(muni.getIncome() + "\t");
                out.write(muni.getFuelCost() + "");
                for (int i = 0; i < v.length; i++) {
                    out.write("\t" + v[i]);
                }
                out.write("\n");
                out.flush();
            }
            out.flush();
            out.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void run(Person person) {
        playground.balmermi.census2000.data.Person p = persons.getPerson(Integer.valueOf(person.getId().toString()));
        Household hh = p.getHousehold();
        Municipality muni = hh.getMunicipality();
        int[] v;
        if (this.munis.containsKey(muni)) {
            v = this.munis.get(muni);
        } else {
            v = new int[9];
            v[0] = v[1] = v[2] = v[3] = v[4] = v[5] = v[6] = v[7] = v[8] = 0;
            this.munis.put(muni, v);
        }
        v[0]++;
        if (!this.hhs.contains(hh)) {
            v[1]++;
            this.hhs.add(hh);
        }
        if (person.getAge() < 18) {
            v[2]++;
        } else if (person.getAge() < 66) {
            v[3]++;
        } else {
            v[4]++;
        }
        if (p.isMale()) {
            v[5]++;
        }
        if (p.isSwiss()) {
            v[6]++;
        }
        if (person.isEmpoyed()) {
            v[7]++;
        }
        if (YES.equals(person.getLicense())) {
            v[8]++;
        }
    }

    public void run(Plan plan) {
    }
}
