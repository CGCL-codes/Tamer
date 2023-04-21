package prefwork.rating.datasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import prefwork.core.Utils;
import prefwork.rating.Rating;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SparseInstance;

public class IMDbMemory extends OracleMultiDataSource {

    Long IMDb = 0L;

    Long Oracle = 0L;

    Attribute[] allAttributes = null;

    protected String[] names;

    private static Logger log = Logger.getLogger(IMDbMemory.class);

    protected List<Integer> usersList = Utils.getList();

    IMDbRelation imdb = new IMDbRelation();

    public String getName() {
        return name + imdb.getName();
    }

    public void restart() {
        IMDb = 0L;
        Oracle = 0L;
        try {
            clearRecords();
            recordsSelect = "SELECT " + userColumn + ", " + objectColumn + ", ";
            for (Attribute attr : attributes) {
                if (attr.isRelationValued() || attr.name().isEmpty()) continue;
                recordsSelect += attr.name() + " ,";
            }
            recordsSelect = recordsSelect.substring(0, recordsSelect.length() - 1);
            recordsSelect += " FROM " + recordsTable;
            if (betweenCondition != null) {
                recordsSelect += " WHERE " + betweenCondition;
            }
            if (userID != null) {
                if (betweenCondition == null || !recordsSelect.endsWith(betweenCondition)) recordsSelect += " WHERE "; else recordsSelect += " AND ";
                recordsSelect += userColumn + " = " + userID;
            }
            recordsStatement = provider.getConn().prepareStatement(recordsSelect);
            records = recordsStatement.executeQuery();
            records.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getAttributes();
    }

    public IMDbMemory() {
    }

    public String[] getAttributesNames() {
        if (allAttributes != null) getInstances();
        if (names != null) return names;
        names = new String[allAttributes.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = allAttributes[i].name();
        }
        return names;
    }

    public Instances getInstances() {
        getAttributes();
        return this.instances;
    }

    public Attribute[] getAttributes() {
        if (allAttributes != null) return allAttributes;
        Attribute[] IMDbAttributes = imdb.getAttributes();
        int size = attributes.length + IMDbAttributes.length;
        Attribute[] attrs = new Attribute[size];
        for (int i = 0; i < attributes.length; i++) {
            attrs[i] = attributes[i];
        }
        for (int i = 0; i < IMDbAttributes.length; i++) {
            attrs[attributes.length + i] = IMDbAttributes[i];
        }
        allAttributes = attrs;
        ArrayList<Attribute> list = new ArrayList<Attribute>();
        for (int i = 0; i < attrs.length; i++) {
            list.add(attrs[i]);
        }
        instances = new Instances("name", list, 10);
        instances.setClassIndex(0);
        return attrs;
    }

    public double[] getClasses() {
        return new double[] { 1, 2, 3, 4, 5 };
    }

    public void setAttributes(Attribute[] attrs) {
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].name().endsWith(".csv")) {
                Attribute[] attrs2 = new Attribute[attrs.length - 1];
                for (int j = 0; j < i; j++) {
                    attrs2[j] = attrs[j];
                }
                for (int j = i; j < attrs2.length; j++) {
                    attrs2[j] = attrs[j + 1];
                }
                attrs = attrs2;
                i = -1;
            }
        }
        super.setAttributes(attrs);
        allAttributes = null;
    }

    /**
	 * Overriden method from SQLMultiSource
	 */
    public Rating next() {
        if (!hasNext()) return null;
        Rating l = new Rating(instances);
        Attribute[] attributes = getAttributes();
        SparseInstance d = new SparseInstance(attributes.length - 2);
        Long start;
        try {
            l.setUserId(Utils.objectToInteger(records.getObject(1)));
            l.setObjectId(Utils.objectToInteger(records.getObject(2)));
            int flixId = Integer.parseInt(records.getObject(2).toString());
            int i = 0;
            for (; i < this.attributes.length; i++) {
                start = System.currentTimeMillis();
                if (attributes[i].isNominal()) d.setValue(i, records.getObject(i + 3).toString()); else d.setValue(i, Utils.objectToDouble(records.getObject(i + 3)));
                Oracle += System.currentTimeMillis() - start;
            }
            SparseInstance movie = imdb.getMovie(flixId);
            d = (SparseInstance) d.mergeInstance(movie);
            d.setDataset(instances);
            start = System.currentTimeMillis();
            records.next();
            Oracle += System.currentTimeMillis() - start;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        l.setRecord(d);
        return l;
    }

    public Integer getUserId() {
        if (!usersList.isEmpty()) {
            int index;
            if (userID == null) {
                index = 0;
            } else {
                index = usersList.indexOf(userID.toString()) + 1;
            }
            if (index >= usersList.size()) {
                userID = null;
                return null;
            }
            userID = Utils.objectToInteger(usersList.get(index));
            return userID;
        }
        try {
            if (users.next() == false) return null;
            int userId = users.getInt(1);
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                usersStatement.close();
                users.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return 0;
        }
    }

    public void restartUserId() {
        try {
            if (usersList != null && !usersList.isEmpty()) {
                clearUsers();
                userID = null;
            } else super.restartUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void configDataSource(XMLConfiguration config, String section, String dataSourceName) {
        super.configDataSource(config, section, dataSourceName);
        Configuration dsConf = config.configurationAt(section);
        imdb.configDataSource(config, section + ".IMDb");
        if (dsConf.containsKey("users")) {
            usersList = dsConf.getList("users");
        }
        usersToLoad = Utils.getIntFromConfIfNotNull(dsConf, "usersToLoad", usersToLoad);
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        new IMDbMemory();
    }
}
