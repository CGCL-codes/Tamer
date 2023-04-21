package desmoj.extensions.db.dao;

import desmoj.core.simulator.Condition;
import desmoj.core.simulator.Model;
import desmoj.extensions.db.DBConnection;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ConditionDAO extends NamedSingleDatasetDAO<Condition> implements SavableDAO<Condition>, LoadableDAO<Condition> {

    private ConditionArgumentDAO conditionArgumentDAO;

    private Model model;

    public ConditionDAO(DBConnection connection, String name, ExperimentDAO experimentDAO, Model model) {
        super(connection, name, experimentDAO);
        conditionArgumentDAO = new ConditionArgumentDAO(connection, this);
        this.model = model;
    }

    public PreparedStatement getIdStatement() throws SQLException {
        String sql = "SELECT id FROM condition WHERE name = ? AND experiment_fk = ?";
        PreparedStatement stmnt = connection.createPreparedStatement(sql);
        stmnt.setString(1, name);
        stmnt.setInt(2, parentDAO.getID());
        return stmnt;
    }

    public PreparedStatement getSaveStatement(Condition condition) throws SQLException {
        String sql = "INSERT INTO condition (type, name, showintrace, experiment_fk) VALUES (?, ?, ?, ?)";
        Object[] arguments = condition.getConstructArguments();
        PreparedStatement stmnt = connection.createPreparedStatement(sql);
        stmnt.setString(1, condition.getClass().getName());
        stmnt.setString(2, name);
        stmnt.setString(3, ((Boolean) arguments[2]).booleanValue() ? "true" : "false");
        stmnt.setInt(4, parentDAO.getID());
        return stmnt;
    }

    public PreparedStatement getLoadStatement() throws SQLException {
        String sql = "SELECT type, name, showintrace FROM condition WHERE experiment_fk = ?";
        PreparedStatement stmnt = connection.createPreparedStatement(sql);
        stmnt.setInt(1, parentDAO.getID());
        return stmnt;
    }

    public Condition toResult(ResultSet resultSet) throws SQLException {
        Condition result = null;
        ArrayList<Object> params = (ArrayList<Object>) conditionArgumentDAO.loadAll();
        Object[] paramValues = new Object[3 + params.size()];
        paramValues[0] = model;
        paramValues[1] = resultSet.getString("NAME");
        paramValues[2] = resultSet.getString("SHOWINTRACE").equals("true") ? true : false;
        Class[] paramTypes = new Class[3 + params.size()];
        paramTypes[0] = Model.class;
        paramTypes[1] = String.class;
        paramTypes[2] = Boolean.TYPE;
        for (int i = 0; i < params.size(); i++) {
            paramValues[i + 3] = params.get(i);
            paramTypes[i + 3] = getType(params.get(i));
        }
        String className = resultSet.getString("TYPE");
        try {
            Class<?> cls = Class.forName(className);
            result = (Condition) cls.getConstructor(paramTypes).newInstance(paramValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void save(Condition condition) {
        if (!condition.hasPrimitiveArguments()) {
            System.out.println("Can't save Condition!");
        }
        super.save(condition);
        Object[] arguments = condition.getConstructArguments();
        for (int i = 3; i < arguments.length; i++) {
            conditionArgumentDAO.save(arguments[i]);
        }
    }

    private Class getType(Object obj) {
        if (obj == null) return null;
        Class cls = obj.getClass();
        return cls.equals(Boolean.class) ? ((Boolean) obj).TYPE : cls.equals(Integer.class) ? ((Integer) obj).TYPE : cls.equals(Character.class) ? ((Character) obj).TYPE : cls.equals(Byte.class) ? ((Byte) obj).TYPE : cls.equals(Short.class) ? ((Short) obj).TYPE : cls.equals(Double.class) ? ((Double) obj).TYPE : cls.equals(Long.class) ? ((Long) obj).TYPE : cls.equals(Float.class) ? ((Float) obj).TYPE : cls.equals(String.class) ? String.class : null;
    }
}
