package redora.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redora.api.Persistable;
import java.util.List;

/**
 * Container for a business rule violation. Holds information on what rule
 * has been violated, when, for which object and what id.
 * The rule id is set in the object model where the business rules are
 * defined (see the manual for more about the object model).
 * Redora generates standard rules, like the NotNull rule. These rules
 * have a negative rule id and they are defined in the StandardRule enum.
 * From the rule id the matching error message can be found. Also the error
 * message is set in the object model and available in the generated
 * properties files.
 * <br>
 * When creating custom business rules you must not only define the rule in the
 * object model. It must also be implemented in the [modelName]BusinessRules class.
 *
 * @author Nanjing RedOrange (http://www.red-orange.cn)
 */
public class BusinessRuleViolation {

    /**
     * Standard (usually generated) business rules, available for all projects
     * using this library.
     */
    public enum StandardRule {

        /** Check if the attribute has null value. */
        NotNull(-1), /** Check if the attribute's value is larger than max length. */
        MaxLength(-2), /**
         * Check if the attribute's value match the "regexp" string defined in
         * model.
         */
        MatchRegexp(-3), /** Wrapper for unique key exception from the database. */
        UniqueKey(-4);

        public final Integer ruleId;

        private StandardRule(Integer ruleId) {
            this.ruleId = ruleId;
        }
    }

    /** CrUD Action. */
    public enum Action {

        Insert, Update, Delete
    }

    public BusinessRuleViolation(@NotNull Persistable persistable, @Nullable Enum<?> field, @NotNull Integer businessRuleId, @NotNull Action action) {
        super();
        this.persistable = persistable;
        this.field = field;
        this.businessRuleId = businessRuleId;
        this.action = action;
    }

    public BusinessRuleViolation(@NotNull Persistable persistable, @Nullable Enum<?> field, @NotNull Integer businessRuleId, @NotNull Action action, @Nullable List<Object> messageArguments) {
        super();
        this.persistable = persistable;
        this.field = field;
        this.businessRuleId = businessRuleId;
        this.action = action;
        this.messageArguments = messageArguments;
    }

    public BusinessRuleViolation(@NotNull Persistable persistable, @Nullable Enum<?> field, @Nullable Class fieldCls, @NotNull Integer businessRuleId, @NotNull Action action, @Nullable List<Object> messageArguments) {
        super();
        this.persistable = persistable;
        this.field = field;
        this.fieldCls = fieldCls;
        this.businessRuleId = businessRuleId;
        this.action = action;
        this.messageArguments = messageArguments;
    }

    /** Pojo class where on the BR was violated. */
    private final Persistable persistable;

    /** Pojo field name for violation, see the respective 'Pojo'Field. */
    @Nullable
    private Enum<?> field;

    /** Pojo field name for violation, see the respective 'Pojo'Field. */
    @Nullable
    private Class fieldCls;

    /** Every business rule has an unique identifier. */
    private final Integer businessRuleId;

    /** Messages(optional) for violation. */
    @Nullable
    private List<Object> messageArguments;

    /** Action for business rule violated (Insert or Delete or Update). */
    private final Action action;

    /**
     * Indicates at what (CrUD) Action is the business rule violated.
     * @return The action
     */
    @NotNull
    public Action getAction() {
        return action;
    }

    /**
     * @return The pojo class
     */
    @NotNull
    public Persistable getPersistable() {
        return persistable;
    }

    /**
     * @return If applicable the violation field
     */
    @Nullable
    public Enum<?> getField() {
        return field;
    }

    /**
     * @return class of field
     */
    @Nullable
    public Class getFieldCls() {
        return fieldCls;
    }

    /**
     * @return The number as defined in the object model, if negative: as defined as StandardRule
     */
    @NotNull
    public Integer getBusinessRuleId() {
        return businessRuleId;
    }

    /**
     * @return Null or the message arguments, used as standard java messages and constants
     */
    @Nullable
    public List<Object> getMessageArguments() {
        return messageArguments;
    }

    /**
     * @return This as JSON.
     */
    @NotNull
    public String toJSON() {
        StringBuilder s = new StringBuilder("{\"persistable\":\"");
        s.append(getPersistable().getClass().getSimpleName()).append('\"');
        s.append(",\"id\":").append(getPersistable().getId());
        s.append(',');
        if (getField() != null) {
            s.append("\"field\":\"").append(getField().name()).append('\"');
            s.append(',');
        }
        if (getFieldCls() != null) {
            s.append("\"fieldClass\":\"").append(getFieldCls().getSimpleName()).append('\"');
            s.append(',');
        }
        s.append("\"businessRuleId\":").append(getBusinessRuleId());
        s.append(',');
        s.append("\"action\":\"").append(getAction().name()).append('\"');
        if (getMessageArguments() != null) {
            s.append(",arguments:[");
            char comma = ' ';
            for (Object o : getMessageArguments()) {
                s.append(comma);
                s.append("{argument:\"");
                s.append(o);
                s.append("\"}");
                comma = ',';
            }
            s.append(']');
        }
        s.append('}');
        return s.toString();
    }
}
