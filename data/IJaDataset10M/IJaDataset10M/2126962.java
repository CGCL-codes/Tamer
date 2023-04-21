package dinamica;

import motor3d.*;

public class RotacionInterpolada implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public boolean activado;

    public boolean conAceleracion;

    public double maximaVelocidadAngular;

    public double aceleracionAngular;

    public Cuaternion cuaternionInicial, cuaternionFinal;

    public TransformacionEje transformacion;

    public Vector3d direccionAdelante;

    public Vector3d direccionArriba;

    private double velocidadAngularActual;

    private double recorridoTotal;

    private double recorridoActual;

    private double tiempoInicial;

    private double tiempoFinal;

    private double tiempoAcel;

    private double tiempoCte;

    private double tiempoDecel;

    private boolean flagAcelCritica;

    private double acelCritica;

    public static Vector3d vectorAdelanteActual = new Vector3d();

    public static Vector3d vectorArribaActual = new Vector3d();

    private static Vector3d direccionDerecha = new Vector3d();

    private static double matrizCuaternionFinal[] = new double[16];

    private static Cuaternion cuaternionIntermedio = new Cuaternion();

    public RotacionInterpolada() {
        direccionAdelante = new Vector3d();
        direccionArriba = new Vector3d();
        cuaternionInicial = new Cuaternion();
        cuaternionFinal = new Cuaternion();
        maximaVelocidadAngular = 1.0d;
        aceleracionAngular = 1.0d;
    }

    public boolean iniciar(double tiempoInicial, Vector3d dirAdelante, Vector3d dirArriba, boolean direccionAdelanteNoArribaComoVectorPrincipal) {
        activado = false;
        if (conAceleracion && aceleracionAngular <= 0.0d) {
            return false;
        }
        direccionAdelante.set(dirAdelante);
        direccionArriba.set(dirArriba);
        if (!direccionAdelante.unitario()) {
            return false;
        }
        direccionDerecha.prodVectorial(direccionAdelante, direccionArriba);
        if (!direccionDerecha.unitario()) {
            return false;
        }
        if (direccionAdelanteNoArribaComoVectorPrincipal) {
            direccionArriba.prodVectorial(direccionDerecha, direccionAdelante);
            direccionArriba.unitario();
        } else {
            direccionAdelante.prodVectorial(direccionArriba, direccionDerecha);
            direccionArriba.unitario();
        }
        vectorAdelanteActual.x = -transformacion.matrizMundo[Matriz4.M20];
        vectorAdelanteActual.y = -transformacion.matrizMundo[Matriz4.M21];
        vectorAdelanteActual.z = -transformacion.matrizMundo[Matriz4.M22];
        vectorArribaActual.x = transformacion.matrizMundo[Matriz4.M10];
        vectorArribaActual.y = transformacion.matrizMundo[Matriz4.M11];
        vectorArribaActual.z = transformacion.matrizMundo[Matriz4.M12];
        double adelanteProdDirAdelante = direccionAdelante.prodEscalar(vectorAdelanteActual);
        if (adelanteProdDirAdelante > 1.0d) {
            adelanteProdDirAdelante = 1.0d;
        }
        if (adelanteProdDirAdelante < -1.0d) {
            adelanteProdDirAdelante = -1.0d;
        }
        double anguloRecorridoAdelante = Math.acos(adelanteProdDirAdelante);
        double arribaProdDirArriba = direccionArriba.prodEscalar(vectorArribaActual);
        if (arribaProdDirArriba > 1.0d) {
            arribaProdDirArriba = 1.0d;
        }
        if (arribaProdDirArriba < -1.0d) {
            arribaProdDirArriba = -1.0d;
        }
        double anguloRecorridoArriba = Math.acos(arribaProdDirArriba);
        this.tiempoInicial = tiempoInicial;
        recorridoTotal = Math.max(anguloRecorridoAdelante, anguloRecorridoArriba);
        recorridoActual = 0.0d;
        if (recorridoTotal == 0.0d) {
            return true;
        }
        if (velocidadAngularActual > maximaVelocidadAngular) {
            velocidadAngularActual = maximaVelocidadAngular;
        }
        if (conAceleracion) {
            tiempoDecel = velocidadAngularActual / aceleracionAngular;
            double rDecel = 0.5d * aceleracionAngular * tiempoDecel * tiempoDecel;
            if (rDecel > recorridoTotal) {
                flagAcelCritica = true;
                acelCritica = velocidadAngularActual * velocidadAngularActual / (2.0d * recorridoTotal);
                tiempoFinal = velocidadAngularActual / acelCritica;
            } else {
                flagAcelCritica = false;
                tiempoDecel = maximaVelocidadAngular / aceleracionAngular;
                rDecel = 0.5d * aceleracionAngular * tiempoDecel * tiempoDecel;
                if (rDecel > 0.5d * recorridoTotal) {
                    tiempoDecel = Math.sqrt(recorridoTotal / aceleracionAngular);
                    tiempoAcel = tiempoDecel;
                    tiempoCte = 0.0d;
                } else {
                    tiempoAcel = (maximaVelocidadAngular - velocidadAngularActual) / aceleracionAngular;
                    double rAcel = velocidadAngularActual * tiempoAcel + 0.5d * aceleracionAngular * tiempoAcel * tiempoAcel;
                    tiempoCte = (recorridoTotal - rDecel - rAcel) / maximaVelocidadAngular;
                }
                tiempoFinal = tiempoAcel + tiempoCte + tiempoDecel;
                tiempoAcel = this.tiempoInicial + tiempoAcel;
                tiempoCte = tiempoAcel + tiempoCte;
                tiempoDecel = tiempoCte + tiempoDecel;
                tiempoFinal += this.tiempoInicial;
            }
        } else {
            tiempoFinal = this.tiempoInicial + recorridoTotal / maximaVelocidadAngular;
        }
        matrizCuaternionFinal[Matriz4.M00] = direccionDerecha.x;
        matrizCuaternionFinal[Matriz4.M01] = direccionDerecha.y;
        matrizCuaternionFinal[Matriz4.M02] = direccionDerecha.z;
        matrizCuaternionFinal[Matriz4.M10] = direccionArriba.x;
        matrizCuaternionFinal[Matriz4.M11] = direccionArriba.y;
        matrizCuaternionFinal[Matriz4.M12] = direccionArriba.z;
        matrizCuaternionFinal[Matriz4.M20] = -direccionAdelante.x;
        matrizCuaternionFinal[Matriz4.M21] = -direccionAdelante.y;
        matrizCuaternionFinal[Matriz4.M22] = -direccionAdelante.z;
        cuaternionFinal.setDesdeMatriz(matrizCuaternionFinal);
        cuaternionInicial.setDesdeEjeAngulo(transformacion.eje, transformacion.angulo);
        activado = true;
        return true;
    }

    public void tick(double tiempo, double dt) {
        if (activado) {
            if (conAceleracion) {
                if (flagAcelCritica) {
                    velocidadAngularActual -= acelCritica * dt;
                } else {
                    if (tiempo < tiempoAcel) {
                        velocidadAngularActual += aceleracionAngular * dt;
                        if (velocidadAngularActual > maximaVelocidadAngular) {
                            velocidadAngularActual = maximaVelocidadAngular;
                        }
                    } else if (tiempo < tiempoCte && tiempoCte != 0.0d) {
                        velocidadAngularActual = maximaVelocidadAngular;
                    } else {
                        velocidadAngularActual -= aceleracionAngular * dt;
                        if (velocidadAngularActual < 0.0d) {
                            velocidadAngularActual = 0.0d;
                        }
                    }
                }
                recorridoActual += velocidadAngularActual * dt;
            } else {
                recorridoActual += dt * maximaVelocidadAngular;
            }
            if (recorridoActual > recorridoTotal || tiempo > tiempoFinal) {
                recorridoActual = recorridoTotal;
                velocidadAngularActual = 0.0d;
                activado = false;
            }
            double t = recorridoActual / recorridoTotal;
            if (t < 0.0d) {
                t = 0.0d;
            }
            if (t > 1.0d) {
                t = 1.0d;
            }
            cuaternionIntermedio.interpolar(cuaternionInicial, cuaternionFinal, t);
            transformacion.angulo = cuaternionIntermedio.obtenerEjeYAngulo(transformacion.eje);
        }
    }
}
