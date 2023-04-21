package au.com.southsky.cashbooks.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import au.com.southsky.cashbooks.CashbooksInjectorModule;
import au.com.southsky.cashbooks.utils.CSVutil;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public abstract class Loader {

    protected static Injector injector = Guice.createInjector(new CashbooksInjectorModule());

    protected EntityManagerFactory entityManagerFactory;

    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    protected BufferedReader in;

    protected int count;

    protected void prepareReader(File f) throws LoaderException {
        try {
            this.in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            checkCsvColumnNames(in.readLine());
            count = 1;
        } catch (IOException e) {
            throw new LoaderException(e);
        }
    }

    @Inject
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    protected abstract String[] getCsvFieldNames();

    protected void checkCsvColumnNames(String rec) throws LoaderException {
        List<String> fileFieldNames = CSVutil.parse(rec);
        String[] csvFieldNames = getCsvFieldNames();
        if (csvFieldNames.length > fileFieldNames.size()) {
            throw new LoaderException("Insufficient fields in file");
        }
        if (csvFieldNames.length < fileFieldNames.size()) {
            throw new LoaderException("Too many fields in file");
        }
        for (int i = 0; i < csvFieldNames.length; i++) {
            if (!(csvFieldNames[i].equals(fileFieldNames.get(i)))) {
                throw new LoaderException(String.format("In field %d, was expection '%s' but got '%s'", i, csvFieldNames[i], fileFieldNames.get(i)));
            }
        }
    }

    protected List<String> getNextRecord() throws LoaderException {
        String rec = null;
        try {
            rec = in.readLine();
            if (rec == null) {
                in.close();
                return null;
            }
        } catch (IOException e) {
            throw new LoaderException(e);
        }
        count++;
        List<String> fields = CSVutil.parse(rec);
        if (fields.size() < getCsvFieldNames().length) {
            throw new LoaderException("Insuffient fields to construct event at line " + count);
        }
        return fields;
    }
}
