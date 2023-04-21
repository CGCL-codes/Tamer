package net.woodstock.rockapi.bean.test;

import net.woodstock.rockapi.pojo.Pojo;
import net.woodstock.rockapi.validation.Operation;
import net.woodstock.rockapi.validation.annotations.ValidateExpression;
import net.woodstock.rockapi.validation.annotations.ValidateIntRange;
import net.woodstock.rockapi.validation.annotations.ValidateLength;
import net.woodstock.rockapi.validation.annotations.ValidateNotNull;
import net.woodstock.rockapi.validation.annotations.ValidateNull;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

public class XPojo implements Pojo {

    private static final long serialVersionUID = 1L;

    @ValidateNull(operation = Operation.CREATE)
    @ValidateNotNull(operation = { Operation.RETRIEVE, Operation.UPDATE, Operation.DELETE })
    @ValidateIntRange(min = 1, max = Integer.MAX_VALUE)
    @NotNull
    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE)
    private Integer id;

    @ValidateNotNull(operation = { Operation.CREATE, Operation.UPDATE })
    @ValidateLength(min = 1, max = 50)
    @NotNull
    @Length(min = 1, max = 50)
    private String nome;

    @ValidateNotNull(operation = { Operation.CREATE, Operation.UPDATE })
    @ValidateLength(min = 1, max = 50)
    @ValidateExpression(expression = "nome == confirmacaoNome")
    @NotNull
    @Length(min = 1, max = 50)
    private String confirmacaoNome;

    @ValidateNotNull(operation = { Operation.CREATE, Operation.UPDATE })
    @Valid
    private YPojo pojo;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getConfirmacaoNome() {
        return this.confirmacaoNome;
    }

    public void setConfirmacaoNome(String confirmacaoNome) {
        this.confirmacaoNome = confirmacaoNome;
    }

    public YPojo getPojo() {
        return this.pojo;
    }

    public void setPojo(YPojo pojo) {
        this.pojo = pojo;
    }
}
