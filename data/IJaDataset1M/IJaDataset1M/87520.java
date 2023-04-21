package br.usp.poli.util;

import java.io.InputStream;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.usp.poli.entity.Occurrence;
import br.usp.poli.entity.Variable;
import br.usp.poli.util.db.HibernateUtil;
import br.usp.poli.ws.ExperimentManager;
import com.ricebridge.csvman.CsvManager;

/**
 * @author Leonardo Bessa
 * 
 */
public class WheatherStationImport {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    @SuppressWarnings("unchecked")
    public static void fillOccurrences(InputStream inputStream, String instrumentSerial) {
        ExperimentManager manager = new ExperimentManager();
        Set<Occurrence> occurences = new HashSet<Occurrence>();
        CsvManager csvManager = new CsvManager();
        csvManager.setRunInBackground(false);
        csvManager.setStartLine(4);
        csvManager.setSeparator("\t");
        csvManager.getCsvSpec().setMergeSeparators(true);
        List<List<String>> data = csvManager.loadAsLists(inputStream);
        Variable tempVar = findVariable("WheatherStation", instrumentSerial, "Temperature");
        Variable humVar = findVariable("WheatherStation", instrumentSerial, "Humidity");
        for (List<String> line : data) {
            String dateString = line.get(0);
            String timeString = line.get(1);
            String[] dateArray = dateString.split("/");
            String[] time = timeString.split(":");
            int second = 0;
            int minute = Integer.parseInt(time[1]);
            ;
            int hourOfDay = Integer.parseInt(time[0]);
            ;
            int dayOfMonth = Integer.parseInt(dateArray[0]);
            ;
            int month = Integer.parseInt(dateArray[1]);
            ;
            int year = 2000 + Integer.parseInt(dateArray[2]);
            ;
            GregorianCalendar date = new GregorianCalendar(year, month - 1, dayOfMonth, hourOfDay, minute, second);
            Occurrence tempOccurrence = new Occurrence();
            tempOccurrence.setVariable(tempVar);
            tempOccurrence.setDate(date);
            tempOccurrence.setValue(line.get(3));
            Occurrence humidityOccurrence = new Occurrence();
            humidityOccurrence.setVariable(humVar);
            humidityOccurrence.setDate(date);
            humidityOccurrence.setValue(line.get(5));
            occurences.add(tempOccurrence);
            occurences.add(humidityOccurrence);
        }
        manager.submitOccurrences(occurences);
    }

    private static Variable findVariable(String instrumentModel, String instrumentSerial, String variableName) {
        Session session = HibernateUtil.getSession();
        Variable uniqueResult = (Variable) session.createCriteria(Variable.class).add(Restrictions.eq("name", variableName)).createAlias("instrument", "i").add(Restrictions.eq("i.model", instrumentModel)).add(Restrictions.eq("i.serial", instrumentSerial)).uniqueResult();
        return uniqueResult;
    }
}
