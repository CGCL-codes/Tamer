package org.brandao.brutos.mapping;

import org.brandao.brutos.BrutosException;
import org.brandao.brutos.Invoker;
import org.brandao.brutos.ScopeType;
import org.brandao.brutos.scope.Scope;
import org.brandao.brutos.Scopes;
import org.brandao.brutos.type.ArrayType;
import org.brandao.brutos.type.CollectionType;
import org.brandao.brutos.type.Type;
import org.brandao.brutos.validator.Validator;

/**
 *
 * @author Afonso Brandao
 */
public class UseBeanData {

    private String nome;

    private ScopeType scopeType;

    private Bean mapping;

    private Object staticValue;

    private Type type;

    private Validator validate;

    private boolean nullable;

    public UseBeanData() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bean getMapping() {
        return mapping;
    }

    public void setMapping(Bean mapping) {
        this.mapping = mapping;
    }

    public Object getValue() {
        Object value = null;
        if (!isNullable()) {
            if (mapping != null) value = mapping.getValue(); else if (staticValue != null) value = type.getValue(staticValue); else if (type instanceof CollectionType || type instanceof ArrayType) {
                value = nome == null ? null : getScope().getCollection(nome);
                value = type.getValue(value);
            } else {
                value = nome == null ? null : getScope().get(nome);
                value = type.getValue(value);
            }
        }
        if (validate != null) validate.validate(this, value);
        return value;
    }

    public Class getClassType() {
        return type == null ? null : type.getClassType();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Scope getScope() {
        Scopes scopes = Invoker.getApplicationContext().getScopes();
        Scope objectScope = scopes.get(scopeType.toString());
        if (objectScope == null) throw new BrutosException("scope not allowed in context: " + scopeType);
        return objectScope;
    }

    public void setScopeType(ScopeType scope) {
        this.scopeType = scope;
    }

    public ScopeType getScopeType() {
        return this.scopeType;
    }

    public Validator getValidate() {
        return validate;
    }

    public void setValidate(Validator validate) {
        this.validate = validate;
    }

    public Object getStaticValue() {
        return staticValue;
    }

    public void setStaticValue(Object staticValue) {
        this.staticValue = staticValue;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
