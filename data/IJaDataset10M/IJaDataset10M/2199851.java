package samples.web.livrariademoiselle.bean.implementation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "editoras")
@SequenceGenerator(name = "EditoraSequence", sequenceName = "editoras_id_editora_seq")
public class Editora extends samples.web.livrariademoiselle.bean.AbstractOnlyName {

    @Id
    @GeneratedValue(generator = "EditoraSequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_editora", columnDefinition = "serial")
    private Integer id;

    public Editora() {
        this.nome = "";
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
