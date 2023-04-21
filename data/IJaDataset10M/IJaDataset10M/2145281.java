package uy.gub.imm.sae.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ae_dias_semana")
public class DiasDeLaSemana implements Serializable {

    private static final long serialVersionUID = -4732790685121630546L;

    private Integer id;

    private DiasSemanaType diaDeLaSemana;

    private Plantilla plantilla;

    public enum DiasSemanaType {

        LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_diasemana")
    @SequenceGenerator(name = "seq_diasemana", initialValue = 1, sequenceName = "s_ae_dia_semana", allocationSize = 1)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false, name = "dia_semana")
    @Enumerated(EnumType.ORDINAL)
    public DiasSemanaType getdiaDeLaSemana() {
        return diaDeLaSemana;
    }

    public void setdiaDeLaSemana(DiasSemanaType diaDeLaSemana) {
        this.diaDeLaSemana = diaDeLaSemana;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "aepl_id", nullable = false)
    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }
}
