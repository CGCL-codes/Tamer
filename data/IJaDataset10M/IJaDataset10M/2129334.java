//Source file: D:\\FACULTAD\\5� A�o\\PROYECTO FINAL\\Proyecto final\\Tesis\\negocio\\Novedad.java

package negocio.administracion;

import java.util.Date;



public class Novedad 
{
   private int idNovedad;
   private int a�o;
   private int mes;
   private int quincenalONo;
   private Date fechaIngreso;
   private DetalleNovedad detalles;
   
   /**
    * @roseuid 3C312A750167
    */
   public Novedad() 
   {
    
   }
   
   
   public Novedad(int idNovedad, int a�o, int mes, int quincenalONo,
		Date fechaIngreso, DetalleNovedad detalles) {
	super();
	this.idNovedad = idNovedad;
	this.a�o = a�o;
	this.mes = mes;
	this.quincenalONo = quincenalONo;
	this.fechaIngreso = fechaIngreso;
	this.detalles = detalles;
}


public int getIdNovedad() {
	return idNovedad;
}


public void setIdNovedad(int idNovedad) {
	this.idNovedad = idNovedad;
}


public int getA�o() {
	return a�o;
}


public void setA�o(int a�o) {
	this.a�o = a�o;
}


public int getMes() {
	return mes;
}


public void setMes(int mes) {
	this.mes = mes;
}


public int getQuincenalONo() {
	return quincenalONo;
}


public void setQuincenalONo(int quincenalONo) {
	this.quincenalONo = quincenalONo;
}


public Date getFechaIngreso() {
	return fechaIngreso;
}


public void setFechaIngreso(Date fechaIngreso) {
	this.fechaIngreso = fechaIngreso;
}


public DetalleNovedad getDetalles() {
	return detalles;
}


public void setDetalles(DetalleNovedad detalles) {
	this.detalles = detalles;
}


/**
    * @return boolean
    * @roseuid 489A517000EA
    */
   public boolean esMiPeriodo() 
   {
    return true;
   }
}
