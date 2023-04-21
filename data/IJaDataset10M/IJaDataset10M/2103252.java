package org.gaea.ui.treatment;

/**
 * Class that includes a reference to all treatment classes needed.
 * 
 * @author mdmajor
 */
public class Treatment {

    private static Treatment _instance = null;

    private Connection _connection;

    private Data _data;

    private Structure _structure;

    private Metadata _metadata;

    /**
	 * Constructor
	 */
    private Treatment() {
        setConnection(new Connection());
        setData(new Data());
        setStructure(new Structure());
        setMetadata(new Metadata());
    }

    /**
	 * @return Unique instance of this class
	 */
    public static synchronized Treatment getInstance() {
        if (_instance == null) {
            _instance = new Treatment();
        }
        return _instance;
    }

    /**
	 * @return the connection
	 */
    public Connection getConnection() {
        return _connection;
    }

    /**
	 * @param connection the connection to set
	 */
    public void setConnection(Connection connection) {
        _connection = connection;
    }

    /**
	 * @return the data
	 */
    public Data getData() {
        return _data;
    }

    /**
	 * @param data the data to set
	 */
    private void setData(Data data) {
        _data = data;
    }

    /**
	 * @return the structure
	 */
    public Structure getStructure() {
        return _structure;
    }

    /**
	 * @param structure the structure to set
	 */
    private void setStructure(Structure structure) {
        _structure = structure;
    }

    /**
	 * @return the metadata
	 */
    public Metadata getMetadata() {
        return _metadata;
    }

    /**
	 * @param metadata the metadata to set
	 */
    private void setMetadata(Metadata metadata) {
        _metadata = metadata;
    }
}
