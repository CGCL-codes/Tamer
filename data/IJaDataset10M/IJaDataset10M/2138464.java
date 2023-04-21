package util;

import ftcomputing.roboTX.Dir;
import ftcomputing.roboTX.Unv;
import robottx.Control;

/**
 *
 * @author Laura
 */
public class HiloPlay extends Thread {

    private boolean continuar = true;

    private MoverRobot movimiento;

    private Instruccion instrucciones;

    private Control control;

    public HiloPlay(MoverRobot movimientoRobot, Instruccion instrucciones, Control control) {
        this.movimiento = movimientoRobot;
        this.instrucciones = instrucciones;
        this.control = control;
    }

    public void detenElHilo() {
        continuar = false;
    }

    public void run() {
        while (continuar) {
            for (int i = 0; i < instrucciones.getNumeroDeInstrucciones(); i++) {
                switch(instrucciones.getInstruccion(i)) {
                    case MUEVE_MOTOR_IZQ_ART1:
                        if (movimiento.gradosArticulacion1 < 225.0) {
                            movimiento.giraTantosGradosXArticulacion(1, 1.5, Dir.Right);
                            movimiento.gradosArticulacion1 += 1.5d;
                            System.out.println("art1: " + movimiento.gradosArticulacion1);
                        }
                        break;
                    case MUEVE_MOTOR_DER_ART1:
                        if (movimiento.tx.getInput(Unv.I5)) {
                            movimiento.giraTantosGradosXArticulacion(1, 1.5, Dir.Left);
                            movimiento.gradosArticulacion1 -= 1.5d;
                            System.out.println("art1: " + movimiento.gradosArticulacion1);
                        } else {
                            movimiento.gradosArticulacion1 = 0.0d;
                        }
                        break;
                    case MUEVE_MOTOR_IZQ_ART2:
                        if (movimiento.tx.getInput(Unv.I4)) {
                            movimiento.giraTantosGradosXArticulacion(2, 26.0, Dir.Left);
                            movimiento.gradosArticulacion2 += 26.0d;
                            System.out.println("art2: " + movimiento.gradosArticulacion2);
                        }
                        break;
                    case MUEVE_MOTOR_DER_ART2:
                        if (movimiento.tx.getInput(Unv.I3)) {
                            movimiento.giraTantosGradosXArticulacion(2, 26.0, Dir.Right);
                            movimiento.gradosArticulacion2 -= 26.0d;
                            System.out.println("art2: " + movimiento.gradosArticulacion2);
                        } else {
                            movimiento.gradosArticulacion2 = -52.0d;
                        }
                        break;
                    case MUEVE_PINZA_UP:
                        if (movimiento.tx.getInput(Unv.I2)) {
                            movimiento.muevePinza(0.06, true);
                            movimiento.distanciaTornillo -= 0.03d;
                            System.out.println("art1: " + movimiento.distanciaTornillo);
                        } else {
                            movimiento.distanciaTornillo = 0.0d;
                        }
                        break;
                    case MUEVE_PINZA_DOWN:
                        if (movimiento.distanciaTornillo < 10.6) {
                            movimiento.muevePinza(0.06, false);
                            movimiento.distanciaTornillo += 0.06d;
                            System.out.println("art1: " + movimiento.distanciaTornillo);
                        }
                        break;
                    case AGARRA_OBJETO:
                        movimiento.agarraObjeto();
                        break;
                    case SUELTA_OBJETO:
                        if (movimiento.tx.getInput(Unv.I1)) {
                            movimiento.sueltaObjeto();
                        }
                        break;
                    case IR_HOME:
                        movimiento.irHome();
                        break;
                }
            }
        }
    }
}
