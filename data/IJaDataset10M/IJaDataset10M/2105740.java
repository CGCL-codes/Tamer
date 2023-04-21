package edu.cibertec.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

public class BeanProducto implements Serializable {

    int codProducto;

    int codCategoria;

    String descProducto;

    String marca;

    float precioUnitario;

    float stock;

    public int getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(int codProducto) {
        this.codProducto = codProducto;
    }

    public int getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(int codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getDescProducto() {
        return descProducto;
    }

    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public String getFormattedPrice(int cents) {
        String formato = new DecimalFormat("$#,##0.00").format((float) (cents / 100));
        System.out.println("getFormattedPrice: " + cents + "--> " + formato);
        return formato;
    }

    /**
	   * Tests for equality with another object
	   * @param o Object to test for equality
	   * @return true if the objects is equal to this Item
	   */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof BeanProducto)) return false;
        int i = ((BeanProducto) o).getCodProducto();
        boolean x = new Integer(i).toString().equals(new Integer(this.codProducto).toString());
        System.out.println("BeanProducto .... this " + this.getCodProducto() + " es igual a " + i + " ? --> " + x);
        return x;
    }

    /**
	   * @return the hashcode for this Item instance
	   */
    public int hashCode() {
        return new Integer(this.codProducto).hashCode();
    }
}
