package src.estadisticas;

public class EstadisticaAtaque {
int turno;
int da�oProferido =0;
int da�oRecibido=0;


/**
 * en el create, si el da�o es negativo significa que fue recibido, sino, realizado
 * @param turno
 */
public EstadisticaAtaque(int iTurno,int iDa�o){
	this.turno = iTurno;
	if(iDa�o < 0)
		da�oRecibido = -iDa�o;
	else
		da�oProferido= iDa�o;
	
}


/**
 * @return the da�oProferido
 */
public int getDa�oProferido() {
	return da�oProferido;
}


/**
 * @param da�oProferido the da�oProferido to set
 */
public void setDa�oProferido(int da�oProferido) {
	this.da�oProferido = da�oProferido;
}


/**
 * @return the da�oRecibido
 */
public int getDa�oRecibido() {
	return da�oRecibido;
}


/**
 * @param da�oRecibido the da�oRecibido to set
 */
public void setDa�oRecibido(int da�oRecibido) {
	this.da�oRecibido = da�oRecibido;
}


/**
 * @return the turno
 */
public int getTurno() {
	return turno;
}


/**
 * @param turno the turno to set
 */
public void setTurno(int turno) {
	this.turno = turno;
}
}
