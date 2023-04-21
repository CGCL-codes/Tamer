package zimbragh;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Klasa reprezentująca kontakt z książki adresowej. Zawiera najczęciej spotykane dane osobowe.
 *
 */
public class Contact {

    Map<String, String> infoMap = new HashMap<String, String>();

    public Contact(Map<String, String> infoMap) {
        this.infoMap = infoMap;
    }

    /**
	 * Zwraca tekstową reprezentację kontaktu w postaci: 'email: imie nazwisko'
	 */
    public String toString() {
        return getEmail() + ": " + getFirstName() + " " + getLastName();
    }

    public String getEmail() {
        return infoMap.get("email");
    }

    public String getFirstName() {
        return infoMap.get("firstName");
    }

    public String getFullName() {
        return infoMap.get("fullName");
    }

    public String getJobTitle() {
        return infoMap.get("jobTitle");
    }

    public String getLastName() {
        return infoMap.get("lastName");
    }

    public String getWorkCity() {
        return infoMap.get("workCity");
    }

    public String getWorkPhone() {
        return infoMap.get("workPhone");
    }

    public String getWorkState() {
        return infoMap.get("workState");
    }

    public String getWorkStreet() {
        return infoMap.get("workStreet");
    }

    /**
	 * Zwraca reprezentację kontaktu w postaci Comma Separated Values
	 * 
	 * @return Łańcuch tekstowy w formacie CSV
	 */
    public String toCSV() {
        StringWriter writ = new StringWriter();
        CSVWriter writer = new CSVWriter(writ, ',', '\"', "\r\n");
        List<String> header = new ArrayList<String>();
        List<String> row = new ArrayList<String>();
        for (Map.Entry<String, String> pair : infoMap.entrySet()) {
            header.add(pair.getKey());
            row.add(pair.getValue());
        }
        String[] headerArr = new String[header.size()];
        String[] rowArr = new String[row.size()];
        for (int i = 0; i < header.size(); i++) {
            headerArr[i] = header.get(i);
            rowArr[i] = row.get(i);
        }
        writer.writeNext(headerArr);
        writer.writeNext(rowArr);
        return writ.toString();
    }

    /**
	 * Ustawia adres e-mail identyfikujący wpis w książce adresowej
	 */
    public void setEmail(String email) {
        infoMap.put("email", email);
    }
}
