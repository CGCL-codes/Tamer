package mmf;

import mmf.api.IGetterSetter;
import mmf.api.IModel;
import mmf.api.IPKGetterSetter;
import mmf.api.IPrimaryKey;
import mmf.reflection.AttributeNotFoundException;
import mmf.reflection.ReflectionGetterSetter;
import mmf.reflection.ReflectionPKGetterSetter;
import util.EqualsHelper;

/**
 * Metadaten eines Attributs
 * 
 * @author Frank R�ber
 * @version $Revision$ $Date$
 */
public abstract class MetaAttribute<TAttribute, TSubClass extends MetaAttribute<TAttribute, TSubClass>> {

    public static final int FLAG_NULLABLE = 0;

    public static final int FLAG_NOTNULL = 1;

    public static final int FLAG_PRIMARYKEY = 2;

    public static final int FLAG_INSERTONLY = 4;

    private MetaModel<?, ?> metaModel;

    private final Class<? extends TAttribute> attributeType;

    private String name;

    private IGetterSetter<TAttribute> getterSetter;

    private IPKGetterSetter<TAttribute> pkGetterSetter;

    private boolean nullable;

    private boolean insertOnly;

    private boolean primaryKey;

    private TAttribute uninitializedValue;

    /**
    * Basiskonstruktion
    * @param attributeType Klasse des Attributs
    * @param name Name des Attributs, null wenn unbekannt
    * @param flags Kombination von FLAG_*s
    * @see #setFlags(int)
    */
    public MetaAttribute(final Class<? extends TAttribute> attributeType, String name, int flags) {
        this.attributeType = attributeType;
        this.name = name;
        setFlags(flags);
    }

    public TSubClass setName(String name) {
        this.name = name;
        return (TSubClass) this;
    }

    public TSubClass setNullable(boolean nullable) {
        this.nullable = nullable;
        return (TSubClass) this;
    }

    public TSubClass setInsertOnly(TAttribute uninitializedValue) {
        this.insertOnly = true;
        this.uninitializedValue = uninitializedValue;
        return (TSubClass) this;
    }

    public TSubClass setInPrimaryKey() {
        this.insertOnly = true;
        this.primaryKey = true;
        return (TSubClass) this;
    }

    /**
    * Default-GetterSetter erzeugen und registrieren. Default ist hier der ReflectionGetterSetter.
    * Funktioniert erst nach der Registrierung des Owners
    * @throws AttributeNotFoundException Attribut konnte nicht gefunden werden
    */
    public void createDefaultGetterSetter() throws AttributeNotFoundException {
        if (primaryKey) {
            setPKGetterSetter(new ReflectionPKGetterSetter<TAttribute>(metaModel.keyClass, (Class<TAttribute>) getAttributeType(), name, convertToGetterName(name), convertToSetterName(name)));
        } else {
            setGetterSetter(new ReflectionGetterSetter<TAttribute>(metaModel.objectClass, (Class<TAttribute>) getAttributeType(), name, convertToGetterName(name), convertToSetterName(name)));
        }
    }

    public TSubClass setGetterSetter(IGetterSetter<TAttribute> getterSetter) {
        if (primaryKey) throw new AssertionError(toString() + ": IGetterSetter not allowed in primary key");
        this.getterSetter = getterSetter;
        return (TSubClass) this;
    }

    public TSubClass setPKGetterSetter(IPKGetterSetter<TAttribute> pkGetterSetter) {
        if (!primaryKey) throw new AssertionError(toString() + ": IPKGetterSetter only in primary key useful");
        this.pkGetterSetter = pkGetterSetter;
        return (TSubClass) this;
    }

    /**
    * Flags auswerten
    * @param flags
    */
    protected void setFlags(int flags) {
        this.nullable = (flags & FLAG_NOTNULL) == 0;
        this.primaryKey = (flags & FLAG_PRIMARYKEY) > 0;
        this.insertOnly = this.primaryKey || (flags & FLAG_INSERTONLY) > 0;
    }

    /**
    * Bei der Initialisierung registriert sich das �bergeordnete Meta-Object
    * @param owner
    */
    final void register(MetaModel<?, ?> owner) {
        if (this.metaModel != null) throw new AssertionError("MetaModel already set");
        this.metaModel = owner;
    }

    public final MetaModel<?, ?> getMetaModel() {
        return metaModel;
    }

    public final Class<? extends TAttribute> getAttributeType() {
        return attributeType;
    }

    /**
    * Attributname
    * @return NULL, wenn noch nicht initialisiert
    */
    public final String getName() {
        return name;
    }

    /**
    * Attributname, durch Modellname qualifiziert
    * @return
    */
    public final String getFullName() {
        return metaModel.getName() + "." + name;
    }

    public final boolean isInPrimaryKey() {
        return primaryKey;
    }

    public final boolean isNullable() {
        return nullable;
    }

    public final boolean isInsertOnly() {
        return insertOnly;
    }

    public final TAttribute getUninitializedValue() {
        return uninitializedValue;
    }

    /**
    * Pr�fung, ob das Attribut bereits initialisiert ist. Bei insertOnly interessant.
    * @param target 
    * @return TRUE: Wert != uninitializedValue
    */
    public boolean isInitialized(IModel target) {
        if (!metaModel.objectClass.isAssignableFrom(target.getClass())) throw new AssertionError("Invalid class: " + target.getClass());
        TAttribute value = getValue(target);
        if (value == null && uninitializedValue == null) return false;
        if (value != null && uninitializedValue != null) return !value.equals(uninitializedValue);
        return true;
    }

    /**
    * Pr�fung, ob das Attribut bereits initialisiert ist. Bei insertOnly interessant.
    * @param target 
    * @return TRUE: Wert != uninitializedValue
    */
    public boolean isInitialized(IPrimaryKey target) {
        if (!metaModel.keyClass.isAssignableFrom(target.getClass())) throw new AssertionError("Invalid class: " + target.getClass());
        TAttribute value = getValue(target);
        if (value == null && uninitializedValue == null) return false;
        if (value != null && uninitializedValue != null) return !value.equals(uninitializedValue);
        return true;
    }

    /**
    * InsertOnly-Attribute initialisieren 
    * @param target
    * @param value
    * @throws AssertionError wenn bereits initialisiert
    */
    public final void initValue(IModel target, TAttribute value) {
        if (!isInitialized(target)) {
            if (primaryKey) {
                pkGetterSetter.setValue(metaModel.getPrimaryKeyUnsafe(target), value);
            } else {
                getterSetter.setValue(target, value);
            }
        } else throw new AssertionError(toString() + " already initialized: " + String.valueOf(getValue(target)));
    }

    /**
    * InsertOnly-Attribute initialisieren 
    * @param target
    * @param value
    * @throws AssertionError wenn bereits initialisiert
    */
    public final void initValue(IPrimaryKey target, TAttribute value) {
        if (!isInitialized(target)) pkGetterSetter.setValue(target, value); else throw new AssertionError(toString() + " already initialized: " + String.valueOf(getValue(target)));
    }

    /**
    * Attributwert schreiben. Geht nicht bei insertOnly-Attributen
    * @param target
    * @param value
    */
    public final void setValue(IModel target, TAttribute value) {
        if (isInsertOnly()) throw new AssertionError(toString() + " is insertOnly");
        getterSetter.setValue(target, value);
    }

    /**
    * Attributwert lesen
    * @param target
    */
    public final TAttribute getValue(IModel target) {
        if (primaryKey) return pkGetterSetter.getValue(metaModel.getPrimaryKeyUnsafe(target)); else return getterSetter.getValue(target);
    }

    /**
    * Attributwert lesen
    * @param target
    */
    public final TAttribute getValue(IPrimaryKey targetKey) {
        return pkGetterSetter.getValue(targetKey);
    }

    public final IGetterSetter<TAttribute> getGetterSetter() {
        return getterSetter;
    }

    public final IPKGetterSetter<TAttribute> getPKGetterSetter() {
        return pkGetterSetter;
    }

    /**
    * Pr�fung, ob get und set bereits gekl�rt ist.
    * @return TRUE: passende (PK)GetterSetter bereits registriert
    */
    public final boolean isGetterSetterDefined() {
        return primaryKey ? pkGetterSetter != null : getterSetter != null;
    }

    /**
    * Pr�fung eines Attributwerts einer Modellinstanz au�erhalb des Modellkontexts
    * @param target
    * @throws ValidationException
    */
    public void validateValueOf(IModel target) throws ValidationException {
        validate(getValue(target));
    }

    /**
    * Pr�fung eines Attributwerts au�erhalb des Modellkontexts
    * @param value zu pr�fender Wert
    * @throws ValidationException Pr�fung nicht bestanden
    */
    public void validate(TAttribute value) throws ValidationException {
        if (isInsertOnly() && valueEquals(value, uninitializedValue)) throw validationIsNotInitializedException();
        if (value == null && !nullable) throw validationIsNullException();
    }

    /**
    * Pr�fung eines Attributwerts au�erhalb des Modellkontexts.
    * Die Pr�fung auf fehlende Initialisierung und NotNull ist bereits erfolgt
    * @param value
    * @throws ValidationException
    */
    public abstract void doValidate(TAttribute value) throws ValidationException;

    /**
    * Zwei Attributwerte auf schwache Gleichheit pr�fen, also bei Objektreferenzen
    * nur, ob immer noch dasselbe Objekt referenziert wird, nicht, ob das referenzierte
    * Objekt sich ge�ndert hat.
    * @param val1
    * @param val2
    * @return TRUE: Values sind als gleich/unver�ndert zu betrachten
    */
    public boolean valueEquals(TAttribute val1, TAttribute val2) {
        return EqualsHelper.equals(val1, val2);
    }

    /**
    * Zwei Attributwerte auf starke Gleichheit pr�fen, also bei Objektreferenzen
    * auch, ob das referenzierte Objekt sich unterscheidet, obwohl die Referenz selbst 
    * sich nicht unterscheidet/ge�ndert hat.
    * @param val1
    * @param val2
    * @return TRUE beide Werte sind vollst�ndig gleich/unver�ndert
    */
    public boolean valueEqualsDeep(TAttribute val1, TAttribute val2) {
        return valueEquals(val1, val2);
    }

    @Override
    public String toString() {
        return "(" + getClass().getName() + ") '" + getFullName() + "': " + attributeType + (nullable ? " (NULL)" : " (NOT NULL)");
    }

    public String toString(IModel target) {
        return String.valueOf(getValue(target));
    }

    /**
    * Eine Exception erzeugen. Wird nicht geworfen.
    * @param msg
    */
    public ValidationException validationException(String msg) {
        return new ValidationException(getFullName() + ": " + msg);
    }

    /**
    * Eine Exception erzeugen. Wird nicht geworfen.
    * @param msg
    */
    public ValidationException validationIsNullException() {
        return new ValidationException(getFullName() + ": not set (null)");
    }

    /**

    * Eine Exception erzeugen. Wird nicht geworfen.
    * @param msg
    */
    public ValidationException validationIsNotInitializedException() {
        return new ValidationException(getFullName() + ": not set");
    }

    public String convertToGetterName(String attributeName) {
        if (Boolean.TYPE.equals(attributeType)) return "is" + Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
        return "get" + Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
    }

    public String convertToSetterName(String attributeName) {
        return "set" + Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
    }

    @Override
    public int hashCode() {
        return metaModel.hashCode() * 27 ^ name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(getClass())) {
            MetaAttribute<TAttribute, ?> mo = (MetaAttribute<TAttribute, ?>) o;
            return name.equals(mo.name) && metaModel.equals(mo.metaModel);
        }
        return false;
    }
}
