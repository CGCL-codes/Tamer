package com.proy.edu.pe.entitys;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the itemrecurso database table.
 * 
 */
@Entity
@Table(name = "itemrecurso")
public class Itemrecurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int idItemRecurso;

    @Column(nullable = false, length = 50)
    private String descripcionItemRecurso;

    @Column(nullable = false, length = 30)
    private String nombreItemRecurso;

    @OneToMany(mappedBy = "itemrecurso1")
    private Set<Empleado> empleados1;

    @OneToMany(mappedBy = "itemrecurso2")
    private Set<Empleado> empleados2;

    @OneToMany(mappedBy = "itemrecurso")
    private Set<Medico> medicos;

    public Itemrecurso() {
    }

    public int getIdItemRecurso() {
        return this.idItemRecurso;
    }

    public void setIdItemRecurso(int idItemRecurso) {
        this.idItemRecurso = idItemRecurso;
    }

    public String getDescripcionItemRecurso() {
        return this.descripcionItemRecurso;
    }

    public void setDescripcionItemRecurso(String descripcionItemRecurso) {
        this.descripcionItemRecurso = descripcionItemRecurso;
    }

    public String getNombreItemRecurso() {
        return this.nombreItemRecurso;
    }

    public void setNombreItemRecurso(String nombreItemRecurso) {
        this.nombreItemRecurso = nombreItemRecurso;
    }

    public Set<Empleado> getEmpleados1() {
        return this.empleados1;
    }

    public void setEmpleados1(Set<Empleado> empleados1) {
        this.empleados1 = empleados1;
    }

    public Set<Empleado> getEmpleados2() {
        return this.empleados2;
    }

    public void setEmpleados2(Set<Empleado> empleados2) {
        this.empleados2 = empleados2;
    }

    public Set<Medico> getMedicos() {
        return this.medicos;
    }

    public void setMedicos(Set<Medico> medicos) {
        this.medicos = medicos;
    }
}
