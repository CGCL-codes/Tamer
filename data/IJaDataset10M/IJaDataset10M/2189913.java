package com.siasal.documentos.business;

import com.siasal.documentos.commons.CategoriaTO;

/**
 * @hibernate.class table="doc_categoria"
 */
public class Categoria {

    /** @modelguid {33E1EF4C-CEE5-49D5-A837-061E59C90102} */
    private Integer id;

    /** @modelguid {A932AE9C-5FDC-4278-847B-DFE405AB827F} */
    private float valor;

    /** @modelguid {0B2BB4E5-2668-4D2B-AE22-C16A51618D09} */
    private int dias;

    /** @modelguid {C1BDCDAF-4AA8-43E1-A26C-0A9A0964157C} */
    private String nombre;

    private int estado;

    /**
	 * @hibernate.property column="cat_dias" type="int"
	 * @return
	 */
    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    /**
	 * @hibernate.id column="cat_id" type="int"
	 *               generator-class="com.common.persistence.secuenciales.MaxIdentifierGenerator"
	 * @hibernate.generator-param name="table" value="doc_categoria"
	 * @hibernate.generator-param name="column" value="cat_id"
	 * 
	 * @return
	 */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @hibernate.property column="cat_nombre" type="string"
	 * @return
	 */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * @hibernate.property column="cat_valor" type="float"
	 * @return
	 */
    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Categoria() {
        super();
    }

    public Categoria(CategoriaTO categoriaTO) {
        setDias(categoriaTO.getDias());
        setNombre(categoriaTO.getNombre());
        setId(categoriaTO.getId());
        setValor(categoriaTO.getValor());
    }

    public CategoriaTO getTO() {
        return new CategoriaTO(this.getDias(), this.getId(), this.getNombre(), this.getValor());
    }

    public CategoriaTO getCategoriaTO() {
        return new CategoriaTO(this.getDias(), this.getId(), this.getNombre(), this.getValor());
    }

    public boolean equals(Object obj) {
        Categoria categoriaTmp = (Categoria) obj;
        if (categoriaTmp != null && getId() != null && categoriaTmp.getId() != null && getId().equals(categoriaTmp.getId())) {
            return true;
        }
        return false;
    }

    public String toString() {
        return getNombre();
    }

    /**
	 * @hibernate.property column="cat_estado" type="int"
	 */
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
