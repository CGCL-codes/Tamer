package edu.napier.soc.xfdm.model;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * @author Thomas
 */
public final class EntityType implements Serializable {

    private static final long serialVersionUID = 34567452495674l;

    private final List<EntityType> children;

    private final List<Constraint> constraints;

    private final EntityType supertype;

    private final String name;

    private final List<Entity> entities;

    private final List<Function> functions;

    /**
	 * @param name
	 */
    public EntityType(String name) {
        this(name, null, null);
    }

    /**
	 * Removes an entity from this entity type.
	 *
	 * @param e
	 */
    public void removeEntity(Entity e) {
        entities.remove(e);
        for (Function f : getFunctions()) {
            if (!f.isEntityReturn()) {
                continue;
            }
            if (((EntityFunction) f).getEntityType() == e.getEntityType()) {
                removeFunction(f);
            }
        }
    }

    public boolean removeFunction(Function f) {
        if (isSupertypeFunction(f)) {
            return supertype.removeFunction(f);
        }
        boolean removed = functions.remove(f);
        if (!removed) {
            return false;
        }
        Iterator<Constraint> iter = getConstraints().iterator();
        while (iter.hasNext()) {
            if (iter.next().containsFunction(f)) {
                iter.remove();
            }
        }
        for (Entity e : getEntities()) {
            e.justifyColumns();
        }
        return true;
    }

    /**
	 * @param name
	 * @param inherit
	 */
    public EntityType(String name, EntityType inherit) {
        this(name, null, inherit);
    }

    /**
	 * Checks if a function is inherited from its supertype.
	 *
	 * @param f the function to check.
	 * @return {@code true} if the function is inherited from its supertype.
	 */
    public boolean isSupertypeFunction(Function f) {
        return supertype != null && supertype.containsFunction(f);
    }

    public boolean isSupertypeConstraint(Constraint c) {
        return supertype != null && supertype.containsConstraint(c);
    }

    /**
	 * @param name
	 * @param functions
	 */
    public EntityType(String name, List<Function> functions) {
        this(name, functions, null);
    }

    /**
	 * @param name
	 * @param functions
	 * @param supertype
	 */
    public EntityType(String name, List<Function> functions, EntityType supertype) {
        if (name == null) {
            throw new NullPointerException("name must not be null.");
        }
        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("name must be 1 or more" + "characters.");
        }
        this.name = name;
        this.supertype = supertype;
        this.children = new LinkedList<EntityType>();
        this.constraints = new LinkedList<Constraint>();
        this.entities = new LinkedList<Entity>();
        this.functions = new LinkedList<Function>();
        if (functions != null) {
            this.functions.addAll(functions);
            this.functions.removeAll(Collections.singleton(null));
        }
        if (supertype != null) {
            supertype.addChild(this);
        }
    }

    /**
	 * @param c
	 * @return
	 * @throws edu.napier.soc.xfdm.model.ConstraintException
	 *
	 */
    public boolean addConstraint(Constraint c) throws ConstraintException {
        if (getConstraints().contains(c)) {
            return false;
        }
        c.validateValues();
        return constraints.add(c);
    }

    /**
	 * @param c
	 * @return
	 */
    public boolean removeConstraint(Constraint c) {
        if (isSupertypeConstraint(c)) {
            return supertype.removeConstraint(c);
        }
        return constraints.remove(c);
    }

    /**
	 * @param name
	 * @return
	 */
    public Constraint getConstraint(String name) {
        for (Constraint c : getConstraints()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    /**
	 * Adds a function to this {@code EntityType}. This method also adds the
	 * function to this {@code EntityType}'s children.
	 *
	 * @param f the function to add.
	 * @return {@code true} if the function is added to this entity type and
	 *         all of its children, otherwise {@code false}.
	 */
    public boolean addFunction(Function f) {
        if (f == null) {
            throw new NullPointerException("function must not be null.");
        }
        if (containsFunction(f)) {
            return false;
        }
        functions.add(f);
        for (Entity e : getEntities()) {
            e.justifyColumns();
        }
        return true;
    }

    /**
	 * @return the constraints associated with this {@code EntityType}.
	 */
    public List<Constraint> getConstraints() {
        return populateConstraintList(new LinkedList<Constraint>());
    }

    private List<Constraint> populateConstraintList(List<Constraint> list) {
        if (supertype != null) {
            supertype.populateConstraintList(list);
        }
        list.addAll(constraints);
        return list;
    }

    /**
	 * @param e
	 * @return
	 */
    public boolean containsEntity(Entity e) {
        return getEntities().contains(e);
    }

    public boolean isChildEntity(Entity e) {
        for (EntityType child : children) {
            if (child.isChildEntity(e)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Adds an entity to this {@code EntityType}. Checks TOTAL and UNIQUE
	 * constraint types. Adds the {@code Entity} to all of this
	 * {@code EntityType}s supertypes (to allow generalization).
	 *
	 * @param e the {@code Entity} to add.
	 * @return {@code true} if the {@code Entity} is added to this
	 *         {@code EntityType} and all of its supertypes.
	 * @throws edu.napier.soc.xfdm.model.ConstraintException
	 *          if {@code e} invalidates one or more constraints.
	 */
    public boolean addEntity(Entity e) throws ConstraintException {
        if (e == null) {
            return false;
        }
        for (Constraint c : constraints) {
            switch(c.getConstraintType()) {
                case TOTAL:
                case UNIQUE:
                    c.validateValue(e);
                    break;
            }
        }
        return entities.add(e);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof EntityType)) {
            return false;
        }
        EntityType e = (EntityType) that;
        return (e.name.equalsIgnoreCase(this.name));
    }

    /**
	 * @return a read-only list of the functions in this {@code EntityType}.
	 */
    public List<Function> getFunctions() {
        return populateFunctionsList(new LinkedList<Function>());
    }

    private List<Function> populateFunctionsList(List<Function> list) {
        if (supertype != null) {
            supertype.populateFunctionsList(list);
        }
        list.addAll(functions);
        return list;
    }

    public List<Entity> getEntities() {
        return populateEntityList(new LinkedList<Entity>());
    }

    private List<Entity> populateEntityList(List<Entity> list) {
        for (EntityType child : children) {
            child.populateEntityList(list);
        }
        list.addAll(entities);
        return list;
    }

    /**
	 * @return the supertype of this {@code EntityType}, otherwise {@code null}.
	 */
    public EntityType getSupertype() {
        return supertype;
    }

    /**
	 * @return the name of this {@code EntityType}.
	 */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    /**
	 * @param f the function to be checked if it exists in this
	 *          {@code Entity Type}.
	 * @return {@code true} if it exists, otherwise {@code false}.
	 */
    public boolean containsFunction(Function f) {
        return getFunctions().contains(f);
    }

    /**
	 * @param c the constraint to be checked if it exists in this
	 *          {@code Entity Type}.
	 * @return {@code true} if it exists, otherwise {@code false}.
	 */
    public boolean containsConstraint(Constraint c) {
        return getConstraints().contains(c);
    }

    /**
	 * @param name the name of the requested function.
	 * @return the requested function if it exists, otherwise {@code null}.
	 */
    public Function getFunction(String name) {
        Function result = null;
        for (Function a : getFunctions()) {
            if (a.getName().equalsIgnoreCase(name)) {
                result = a;
                break;
            }
        }
        return result;
    }

    /**
	 * Parses entities from file.
	 *
	 * @param in the input stream to be parsed.
	 * @throws java.io.IOException      if there is a problem with the stream.
	 * @throws java.text.ParseException if the data is in the incorrect format.
	 * @throws edu.napier.soc.xfdm.model.ConstraintException
	 *                                  if the parsed entities invalidate one or more constraints.
	 */
    public void parseEntities(BufferedReader in) throws IOException, ParseException, ConstraintException {
        CsvReader csv = new CsvReader(in);
        List<Function> funcs = new ArrayList<Function>();
        if (!csv.readHeaders()) {
            return;
        }
        for (int i = 0; i < csv.getHeaderCount(); i++) {
            Function f = getFunction(csv.getHeader(i));
            if (f == null) {
                throw new ParseException("function " + csv.getHeader(i) + " does not exist in file", 0);
            }
            funcs.add(f);
        }
        while (csv.readRecord()) {
            Entity e = new Entity(this);
            for (int i = 0; i < csv.getColumnCount(); i++) {
                Object value = null;
                if (csv.get(i).length() != 0) {
                    value = convertObject(funcs.get(i), csv.get(i));
                    if (value == null) {
                        throw new ParseException("entry " + csv.get(i) + " cannot be converted to required type " + "in file", 0);
                    }
                }
                e.setValue(funcs.get(i), value);
            }
            addEntity(e);
        }
    }

    /**
	 * @param in
	 * @param destination
	 * @throws java.io.IOException
	 * @throws java.text.ParseException
	 * @throws edu.napier.soc.xfdm.model.ConstraintException
	 *
	 */
    public void parseRelationships(BufferedReader in, Function destination) throws IOException, ParseException, ConstraintException {
        if (in == null) {
            throw new NullPointerException("in must not be null");
        }
        if (destination == null) {
            throw new NullPointerException("f must not be null");
        }
        CsvReader csv = new CsvReader(in);
        if (!csv.readHeaders()) {
            return;
        }
        EntityType et = ((EntityFunction) destination).getEntityType();
        Map<String, Function> thisEntity = new HashMap<String, Function>();
        Map<String, Function> relationEntity = new HashMap<String, Function>();
        for (int i = 0; i < csv.getHeaderCount(); i++) {
            String header = csv.getHeader(i);
            Function func;
            if (header.endsWith("*")) {
                String fixed = header.substring(0, header.length() - 1);
                func = et.getFunction(fixed);
                if (func == null) {
                    throw new ParseException("function " + csv.getHeader(i) + " does not exist in file", 0);
                }
                relationEntity.put(header, func);
            } else {
                func = getFunction(header);
                if (func == null) {
                    throw new ParseException("function " + csv.getHeader(i) + " does not exist in file", 0);
                }
                thisEntity.put(header, func);
            }
        }
        while (csv.readRecord()) {
            Entity source = isolateEntity(thisEntity, getEntities(), csv, this);
            Entity relation = isolateEntity(relationEntity, et.getEntities(), csv, et);
            source.addValue(destination, relation);
        }
        csv.close();
    }

    private Entity isolateEntity(Map<String, Function> functions, List<Entity> entities, CsvReader csv, EntityType entityType) throws ParseException, IOException {
        for (Map.Entry<String, Function> e : functions.entrySet()) {
            Object o = convertObject(e.getValue(), csv.get(e.getKey()));
            entities = entityType.elminateEntities(entities, e.getValue(), o);
        }
        if (entities.size() != 1) {
            throw new ParseException("singleton expected at record " + csv.getCurrentRecord(), 0);
        }
        return entities.get(0);
    }

    private List<Entity> elminateEntities(List<Entity> e, Function f, Object o) {
        List<Entity> result = new ArrayList<Entity>();
        for (Entity e1 : e) {
            Object o1 = e1.getValue(f);
            if (o1 instanceof String && o instanceof String) {
                if (((String) o).equalsIgnoreCase((String) (o1))) {
                    result.add(e1);
                }
            } else if (e1.getValue(f).equals(o)) {
                result.add(e1);
            }
        }
        return result;
    }

    private Object convertObject(Function f, String value) {
        ReturnType rt = f.getReturnType();
        switch(rt) {
            case STRING:
                return value;
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            case INTEGER:
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    return null;
                }
            case CHAR:
                if (value.length() == 1) {
                    return value.charAt(0);
                } else {
                    return null;
                }
            case REAL:
                try {
                    return Double.parseDouble(value);
                } catch (NumberFormatException ex) {
                    return null;
                }
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    private void addChild(EntityType child) {
        children.add(child);
    }
}
