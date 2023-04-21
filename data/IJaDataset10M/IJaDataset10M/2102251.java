package br.ufmg.lcc.pcollecta.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import br.ufmg.lcc.arangi.commons.StringHelper;

/**
 *Global parameters that can be used in several scripts in ETL configuration.
 *Each parameter can be used in script in the following format: 
 *
 *#{PARAMETER_NAME} 
 *
 *During the import process the system change this expression to
 *the value wrote in database.
 */
@Entity
@Table(name = "PC_GLOBAL_PARAMETER")
public class GlobalParameter extends PCollectaDTO implements Comparable<GlobalParameter> {

    /**
	 *The value of global parameter.
	 */
    private String value;

    @Id
    @Column(name = "ID_GLOBAL_PARAMETER")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "SE_GLOBAL_PARAMETER")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQUENCE")
    @Override
    public Long getId() {
        return super.getId();
    }

    public void setName(String name) {
        if (name != null) {
            super.setName((StringHelper.removeSpaces(name)).trim());
        } else {
            super.setName(name);
        }
    }

    @Column(name = "NAME")
    @OrderBy("name desc")
    public String getName() {
        return super.getName();
    }

    @Column(name = "VALUE")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value != null) {
            this.value = StringHelper.removeDuplicatedSpaces(value);
        } else {
            this.value = value;
        }
    }

    @Override
    public int compareTo(GlobalParameter o) {
        return this.getName().compareToIgnoreCase(o.getName()) > 0 ? +1 : -1;
    }
}
