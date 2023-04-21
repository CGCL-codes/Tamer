package net.woodstock.rockapi.domain.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.woodstock.rockapi.domain.Entity;
import net.woodstock.rockapi.domain.business.validation.local.Operation;
import net.woodstock.rockapi.domain.business.validation.local.annotation.ValidateExpression;
import net.woodstock.rockapi.domain.business.validation.local.annotation.ValidateIntRange;
import net.woodstock.rockapi.domain.business.validation.local.annotation.ValidateLength;
import net.woodstock.rockapi.domain.business.validation.local.annotation.ValidateNotEmpty;
import net.woodstock.rockapi.domain.business.validation.local.annotation.ValidateNotNull;
import net.woodstock.rockapi.domain.business.validation.local.annotation.ValidateNull;
import net.woodstock.rockapi.domain.business.validation.local.annotation.ValidateReference;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

@XmlRootElement(name = "foo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Foo implements Entity<Integer> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    @ValidateNull(operation = { Operation.CREATE })
    @ValidateNotNull(operation = { Operation.RETRIEVE, Operation.UPDATE, Operation.DELETE })
    @ValidateIntRange(min = 0, max = 10)
    @XmlElement(name = "id")
    private Integer id;

    @NotNull
    @Min(value = 5)
    @Max(value = 50)
    @ValidateNotEmpty(operation = { Operation.CREATE, Operation.UPDATE })
    @ValidateLength(min = 5, max = 50)
    @ValidateExpression(expression = "foo.name eq 'Teste'", useParent = true)
    @XmlElement(name = "name")
    private String name;

    @NotNull
    @Valid
    @ValidateNotNull(operation = { Operation.CREATE, Operation.UPDATE })
    @ValidateReference(operation = { Operation.CREATE, Operation.UPDATE })
    @XmlElement(name = "bar")
    private Bar bar;

    public Foo() {
        super();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bar getBar() {
        return this.bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }
}
