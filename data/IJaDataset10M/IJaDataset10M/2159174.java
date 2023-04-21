package src.estadisticas;

public class EstadisticaAtaque {
int turno;
int dañoProferido =0;
int dañoRecibido=0;


/**
 * en el create, si el daño es negativo significa que fue recibido, sino, realizado
 * @param turno
 */
public EstadisticaAtaque(int iTurno,int iDaño){
	this.turno = iTurno;
	if(iDaño < 0)
		dañoRecibido = -iDaño;
	else
		dañoProferido= iDaño;
	
}


/**
 * @return the dañoProferido
 */
public int getDañoProferido() {
	return dañoProferido;
}


/**
 * @param dañoProferido the dañoProferido to set
 */
public void setDañoProferido(int dañoProferido) {
	this.dañoProferido = dañoProferido;
}


/**
 * @return the dañoRecibido
 */
public int getDañoRecibido() {
	return dañoRecibido;
}


/**
 * @param dañoRecibido the dañoRecibido to set
 */
public void setDañoRecibido(int dañoRecibido) {
	this.dañoRecibido = dañoRecibido;
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
