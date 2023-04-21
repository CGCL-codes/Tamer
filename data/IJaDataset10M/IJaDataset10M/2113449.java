package polysema.annotator.helper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.util.Vector;

public class CountriesList {

    /** A country item is a String line,read from the countries file and contains the
	 * information about a country. All the information is stored as a one line argument
	 * and it is parsed when is requested. We remind that the format in the countries
	 * file is: countryName|countryCode|AdministrativeUnit. Perhaps in the future, we may
	 * use RDF/OWL file instead of a flat file.
	 * */
    private class CountryItem {

        private String countryInfo;

        private final String delimiter = "\\|";

        public CountryItem(String s) {
            countryInfo = s;
        }

        private String getName() {
            return countryInfo.split(delimiter)[0];
        }

        private String getAdministrativeUnit() {
            return countryInfo.split(delimiter)[2];
        }

        private String getCode() {
            return countryInfo.split(delimiter)[1];
        }
    }

    private static TreeMap<String, CountryItem> countries = null;

    public String[] getCountryNames() {
        return getCountries().keySet().toArray(new String[getCountries().keySet().size()]);
    }

    public Vector<String> getCountryAdministrativeUnits() {
        Vector<String> v = new Vector<String>();
        for (String s : getCountries().keySet()) v.add(getCountries().get(s).getAdministrativeUnit());
        return v;
    }

    public Vector<String> getCountryCodes() {
        Vector<String> v = new Vector<String>();
        for (String s : getCountries().keySet()) v.add(getCountries().get(s).getCode());
        return v;
    }

    public String getCountryAdministrativeUnit(String countryName) {
        return getCountries().get(countryName).getAdministrativeUnit();
    }

    /** Function that given a country name, returns the 2 digit international code the country
	 * has. */
    public String getCountryCode(String countryName) {
        return getCountries().get(countryName).getCode();
    }

    /** Function that given a country's 2-digit international code, returns the country's name. */
    public String getCountryName(String countryCode) {
        for (CountryItem ci : getCountries().values()) if (ci.getCode().equals(countryCode)) return ci.getName();
        return null;
    }

    private TreeMap<String, CountryItem> getCountries() {
        if (countries == null) {
            CountryItem ci;
            countries = new TreeMap<String, CountryItem>();
            try {
                String strLine;
                DataInputStream in = new DataInputStream(new FileInputStream("./Resources/countries"));
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while ((strLine = br.readLine()) != null) {
                    ci = new CountryItem(strLine);
                    countries.put(ci.getName(), ci);
                }
                in.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        return countries;
    }
}
