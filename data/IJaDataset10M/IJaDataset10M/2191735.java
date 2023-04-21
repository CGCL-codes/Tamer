package weka.ptolemy.actor;

import ptolemy.actor.TypedIOPort;
import ptolemy.actor.lib.Source;
import ptolemy.data.BooleanToken;
import ptolemy.data.ObjectToken;
import ptolemy.data.StringToken;
import ptolemy.data.expr.StringParameter;
import ptolemy.data.type.BaseType;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.DatabaseLoader;

/**
 * A reader that reads data from a database via Weka's DatabaseLoader.
 * 
 * @author fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 185 $
 * @see weka.core.converters.DatabaseLoader
 */
public class DatabaseReader extends Source {

    /** for serialization. */
    private static final long serialVersionUID = 3582451315209244158L;

    /** the JDBC URL to use. */
    protected StringParameter url;

    /** the database user to use. */
    protected StringParameter user;

    /** the database password to use. */
    protected StringParameter password;

    /** the SQL query to use. */
    protected StringParameter query;

    /** the columns to use as primary key. */
    protected StringParameter keys;

    /** An output port for providing data instance-wise. */
    protected TypedIOPort instance;

    /** the "finished" port. */
    protected TypedIOPort finished;

    /** the source object to use for reading. */
    protected DatabaseLoader _source;

    /** the structure of the dataset. */
    protected Instances _structure;

    /** the next instance. */
    protected Instance _nextInstance;

    /** whether all rows have been provided. */
    protected boolean _finished;

    /**
   * Constructor.
   * 
   * @param container 	The container.
   * @param name 	The name of this actor.
   */
    public DatabaseReader(CompositeEntity container, String name) throws NameDuplicationException, IllegalActionException {
        super(container, name);
        DatabaseLoader dl = null;
        try {
            dl = new DatabaseLoader();
        } catch (Exception e) {
            throw new IllegalActionException(this, e, "Error instantiating DatabaseLoader!");
        }
        output.setName("dataset");
        output.setTypeEquals(BaseType.OBJECT);
        instance = new TypedIOPort(this, "instance", false, true);
        instance.setTypeEquals(BaseType.OBJECT);
        finished = new TypedIOPort(this, "finished", false, true);
        finished.setTypeEquals(BaseType.BOOLEAN);
        finished.setMultiport(true);
        url = new StringParameter(this, "URL");
        url.setExpression(dl.getUrl());
        user = new StringParameter(this, "user");
        user.setExpression(dl.getUser());
        password = new StringParameter(this, "password");
        password.setExpression(dl.getPassword());
        query = new StringParameter(this, "query");
        query.setExpression(dl.getQuery());
        keys = new StringParameter(this, "keys");
        keys.setExpression(dl.getKeys());
    }

    /** 
   * Resets source and structure. 
   * 
   * @throws IllegalActionException 	If the parent class throws it,
   *   					which could occur if, for example, 
   *   					the director will not accept
   *   					sequence actors.
   */
    public void initialize() throws IllegalActionException {
        super.initialize();
        _source = null;
        _structure = null;
        _finished = false;
        _nextInstance = null;
    }

    /**
   * Initializes the source, if necessary, and sends a weka.core.Instances
   * object.
   * 
   * @throws IllegalActionException If there is no director.
   */
    public void fire() throws IllegalActionException {
        Instances data;
        super.fire();
        if (_source == null) {
            try {
                _source = new DatabaseLoader();
            } catch (Exception e) {
                throw new IllegalActionException(this, e, "Error instantiating DatabaseLoader!");
            }
            _source.setUrl(((StringToken) url.getToken()).stringValue());
            _source.setUser(((StringToken) user.getToken()).stringValue());
            _source.setPassword(((StringToken) password.getToken()).stringValue());
            _source.setQuery(((StringToken) query.getToken()).stringValue());
            _source.setKeys(((StringToken) keys.getToken()).stringValue());
            try {
                _structure = _source.getStructure();
            } catch (Exception e) {
                throw new IllegalActionException(this, e, "Error obtaining dataset structure!");
            }
        }
        if (output.getWidth() > 0) {
            data = null;
            try {
                data = _source.getDataSet();
            } catch (Exception e) {
                throw new IllegalActionException(this, e, "Failed to obtain full dataset!");
            }
            if (data == null) throw new IllegalActionException(this, "Error loading data - check console for error message!");
            output.broadcast(new ObjectToken(data));
            _finished = true;
        } else if (instance.getWidth() > 0) {
            try {
                if (_nextInstance == null) _nextInstance = _source.getNextInstance(_structure);
                instance.broadcast(new ObjectToken(_nextInstance));
                _nextInstance = _source.getNextInstance(_structure);
                _finished = (_nextInstance == null);
            } catch (Exception e) {
                throw new IllegalActionException(this, e, "Failed to obtain next row!");
            }
        }
        if (finished.getWidth() > 0) finished.broadcast(new BooleanToken(_finished));
        if (_finished) {
            _source = null;
            _structure = null;
            _nextInstance = null;
        }
    }

    /** 
   * Returns true as long as there are more rows to read (or triggered again), 
   * otherwise false.
   * 
   * @return				true if continuing, false otherwise
   * @throws IllegalActionException 	does not happen
   */
    public boolean postfire() throws IllegalActionException {
        if (_stopRequested) {
            return false;
        } else {
            if (trigger.getWidth() > 0) {
                boolean result = false;
                for (int i = 0; i < trigger.getWidth(); i++) {
                    if (trigger.hasToken(i)) {
                        result = true;
                        break;
                    }
                }
                return result;
            } else {
                return !_finished;
            }
        }
    }
}
