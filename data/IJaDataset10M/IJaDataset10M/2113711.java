package motor3d;

import java.util.Vector;
import javax.media.opengl.*;

public class Escena implements Mundo, CallbackDibujadoObjeto, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public Fondo fondo;

    private Camara camaraActual;

    public Vector<Objeto3d> objetos;

    public LibreriaMateriales libreriaMateriales;

    public Iluminacion iluminacion;

    public double minZ;

    public double maxZ;

    public boolean cullingPorMaxZ;

    public boolean comprobacionZ;

    public boolean ordenarObjetosOpacos;

    public boolean ordenarObjetosTransparentes;

    public CallbackDibujadoObjeto callbackDibujadoObjeto;

    public transient boolean primerFotograma;

    private transient Vector<ObjetoSort> listaObjetosOpacos;

    private transient Vector<ObjetoSort> listaObjetosTransparentes;

    private transient boolean texturasDirty;

    private transient boolean shadersDirty;

    private static Vector3d vectorTraslacionCamaraInversa = new Vector3d();

    private static Vector3d vectorPosicionCamara = new Vector3d();

    private static Vector3d vectorDireccionCamara = new Vector3d();

    private static Vector3d vectorPosicionObjeto = new Vector3d();

    private static Vector3d vectorPosicionObjetoMundo = new Vector3d();

    private static Vector3d vectorNormalObjetoDistante = new Vector3d();

    private static Vector3d vectorAux1 = new Vector3d();

    private static float array4float[] = new float[4];

    public Escena() {
        fondo = null;
        objetos = new Vector<Objeto3d>();
        libreriaMateriales = new LibreriaMateriales();
        libreriaMateriales.setEscena(this);
        iluminacion = new Iluminacion();
        minZ = 0.1d;
        maxZ = 1000000.0d;
        cullingPorMaxZ = true;
        comprobacionZ = true;
        ordenarObjetosOpacos = false;
        ordenarObjetosTransparentes = true;
        callbackDibujadoObjeto = this;
        primerFotograma = true;
        listaObjetosOpacos = new Vector<ObjetoSort>();
        listaObjetosTransparentes = new Vector<ObjetoSort>();
    }

    public void inicializar() {
        if (listaObjetosOpacos == null) {
            listaObjetosOpacos = new Vector<ObjetoSort>();
        }
        if (listaObjetosTransparentes == null) {
            listaObjetosTransparentes = new Vector<ObjetoSort>();
        }
        setTexturasDirty(true);
        setShadersDirty(true);
    }

    public void setFondo(Fondo fondo) {
        this.fondo = fondo;
        fondo.inicializar(this);
    }

    public boolean getTexturasDirty() {
        return texturasDirty;
    }

    public void setTexturasDirty(boolean dirty) {
        texturasDirty = dirty;
    }

    public boolean getShadersDirty() {
        return shadersDirty;
    }

    public void setShadersDirty(boolean dirty) {
        shadersDirty = dirty;
    }

    public Camara getCamaraActual() {
        return camaraActual;
    }

    public void setCamaraActual(Camara camara) {
        camaraActual = camara;
    }

    public double getEscalaTiempo() {
        return 1.0d;
    }

    public void setEscalaTiempo(double escalaTiempo) {
    }

    public void tick(double tiempo, double dt) {
        for (int o = 0; o < objetos.size(); o++) {
            Objeto3d obj = objetos.elementAt(o);
            obj.calcularEMundo();
            obj.tick(tiempo, dt);
        }
        camaraActual.calcularEMundo();
        primerFotograma = false;
    }

    public void representar(PuertoVisual puerto) {
        GL2 gl = puerto.gl;
        puerto.cargarTexturas(this);
        puerto.cargarShaders(this, gl);
        if (camaraActual == null) {
            return;
        }
        if (comprobacionZ) {
            gl.glEnable(GL.GL_DEPTH_TEST);
        } else {
            gl.glDisable(GL.GL_DEPTH_TEST);
        }
        GLDrawable drawable = puerto.drawable;
        double width = drawable.getWidth();
        double height = drawable.getHeight();
        Matriz4.proyeccionConica(camaraActual.fovY, width / height, minZ, maxZ, puerto.matrizProyeccion);
        Matriz4.copiarDeMatrizAFloat(puerto.matrizProyeccionFloat, puerto.matrizProyeccion);
        Matriz4.copiarDe(puerto.matrizCamara, camaraActual.transformacion.matrizMundo);
        Matriz4.invierteMatriz(puerto.matrizCamara, puerto.matrizCamaraInvertida);
        Matriz4.obtenerTraslacion(puerto.matrizCamaraInvertida, vectorTraslacionCamaraInversa);
        puerto.matrizCamaraInvertida[Matriz4.M30] = 0.0d;
        puerto.matrizCamaraInvertida[Matriz4.M31] = 0.0d;
        puerto.matrizCamaraInvertida[Matriz4.M32] = 0.0d;
        if (fondo != null) {
            fondo.dibujar(puerto);
        }
        puerto.matrizCamaraInvertida[Matriz4.M30] = vectorTraslacionCamaraInversa.x;
        puerto.matrizCamaraInvertida[Matriz4.M31] = vectorTraslacionCamaraInversa.y;
        puerto.matrizCamaraInvertida[Matriz4.M32] = vectorTraslacionCamaraInversa.z;
        puerto.numLucesActivadas = iluminacion.establecer(puerto);
        puerto.iluminacion = iluminacion;
        puerto.width2 = width * 0.5d;
        puerto.height2 = height * 0.5d;
        double fovYRadianes = (Math.PI / 180.0d) * camaraActual.fovY;
        puerto.distancia = puerto.height2 / Math.tan(fovYRadianes / 2.0d);
        puerto.minZ = minZ;
        puerto.maxZ = maxZ;
        puerto.parametrosPerspectivaCalculados = true;
        double diagonal = Math.sqrt(puerto.width2 * puerto.width2 + puerto.height2 * puerto.height2);
        double anguloDiagonalCamara = Math.atan(diagonal / puerto.distancia);
        vectorAux1.set(0.0d, 0.0d, -1.0d);
        Matriz4.vector3dLibreXMatriz4(vectorAux1, puerto.matrizCamara, vectorDireccionCamara);
        Matriz4.obtenerTraslacion(puerto.matrizCamara, vectorPosicionCamara);
        Matriz4.copiarDeMatrizAFloat(puerto.matrizCamaraFloat, puerto.matrizCamara);
        Matriz4.copiarDeMatrizAFloat(puerto.matrizCamaraInvertidaFloat, puerto.matrizCamaraInvertida);
        int no = objetos.size();
        if (listaObjetosOpacos.size() < no) {
            listaObjetosOpacos.ensureCapacity(no * 2);
            for (int i = listaObjetosOpacos.size(); i < no * 2; i++) {
                listaObjetosOpacos.add(new ObjetoSort());
            }
        }
        if (ordenarObjetosTransparentes) {
            if (listaObjetosTransparentes.size() < no) {
                listaObjetosTransparentes.ensureCapacity(no * 2);
                for (int i = listaObjetosTransparentes.size(); i < no * 2; i++) {
                    listaObjetosTransparentes.add(new ObjetoSort());
                }
            }
        }
        int numObjetosOpacos = 0;
        int numObjetosTransparentes = 0;
        for (int i = 0; i < no; i++) {
            Objeto3d obj = objetos.elementAt(i);
            if (!obj.oculto) {
                double tamAparentePixels = 0.0d;
                boolean dibujar = true;
                Matriz4.obtenerTraslacion(obj.transformacion.matrizMundo, vectorPosicionObjetoMundo);
                vectorPosicionObjeto.resta(vectorPosicionObjetoMundo, vectorPosicionCamara);
                double distanciaObjeto = vectorPosicionObjeto.modulo();
                double prodEscPosObjeto = 0.0d;
                if (distanciaObjeto > 0.0d) {
                    if (cullingPorMaxZ && distanciaObjeto - obj.radio >= maxZ) {
                        dibujar = false;
                    } else {
                        double anguloAparenteObjeto = Math.asin(obj.radio / distanciaObjeto);
                        double anguloCulling = anguloDiagonalCamara + anguloAparenteObjeto;
                        if (Double.isNaN(anguloAparenteObjeto)) {
                            tamAparentePixels = height;
                        } else {
                            tamAparentePixels = height * anguloAparenteObjeto / fovYRadianes;
                        }
                        vectorPosicionObjeto.unitario(vectorAux1);
                        prodEscPosObjeto = vectorDireccionCamara.prodEscalar(vectorAux1);
                        if (prodEscPosObjeto < 0.0d) {
                            double distNegZ = -vectorDireccionCamara.prodEscalar(vectorPosicionObjeto);
                            if (distNegZ > obj.radio) {
                                dibujar = false;
                            }
                        } else {
                            if (!Double.isNaN(anguloCulling) && prodEscPosObjeto < Math.cos(anguloCulling)) {
                                dibujar = false;
                            }
                        }
                    }
                } else {
                    tamAparentePixels = height;
                }
                if (dibujar) {
                    ObjetoSort objSort = null;
                    if (!ordenarObjetosOpacos && ordenarObjetosTransparentes && obj.tieneTransparencia) {
                        objSort = listaObjetosTransparentes.elementAt(numObjetosTransparentes);
                        numObjetosTransparentes++;
                    } else {
                        objSort = listaObjetosOpacos.elementAt(numObjetosOpacos);
                        numObjetosOpacos++;
                    }
                    objSort.objeto = obj;
                    objSort.zOjo = distanciaObjeto;
                    objSort.tamAparentePixels = tamAparentePixels;
                    objSort.posRelObjetoCamara.set(vectorPosicionObjeto);
                    obj.regenerarBufer(puerto);
                }
            }
        }
        if (ordenarObjetosOpacos) {
            OrdenacionObjetos.ordenarListaObjetos(listaObjetosOpacos, numObjetosOpacos);
        }
        for (int i = 0; i < numObjetosOpacos; i++) {
            ObjetoSort objSort = listaObjetosOpacos.elementAt(i);
            puerto.posRelObjetoCamaraMundo.set(objSort.posRelObjetoCamara);
            callbackDibujadoObjeto.dibujarObjeto(objSort.objeto, objSort.posRelObjetoCamara, puerto.matrizCamaraInvertida, objSort.tamAparentePixels, puerto);
        }
        if (ordenarObjetosTransparentes) {
            OrdenacionObjetos.ordenarListaObjetos(listaObjetosTransparentes, numObjetosTransparentes);
            for (int i = 0; i < numObjetosTransparentes; i++) {
                ObjetoSort objSort = listaObjetosTransparentes.elementAt(i);
                puerto.posRelObjetoCamaraMundo.set(objSort.posRelObjetoCamara);
                callbackDibujadoObjeto.dibujarObjeto(objSort.objeto, objSort.posRelObjetoCamara, puerto.matrizCamaraInvertida, objSort.tamAparentePixels, puerto);
            }
        }
    }

    public void dibujarObjeto(Objeto3d objeto, Vector3d posRelObjetoCamaraMundo, double[] matrizCamaraInvertida, double tamAparentePixels, PuertoVisual puerto) {
        if (tamAparentePixels < 1.0d) {
            Matriz4.obtenerTraslacion(objeto.transformacion.matrizMundo, vectorAux1);
            vectorNormalObjetoDistante.set(posRelObjetoCamaraMundo);
            vectorNormalObjetoDistante.unitario();
            vectorNormalObjetoDistante.niega();
        } else {
            objeto.dibujarRecursivo(puerto, tamAparentePixels);
        }
    }

    private static void dibujarPuntoDistante(Vector3d color, boolean emisivo, Vector3d posMundo, Vector3d normal, double tamAparentePixels, GL2 gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        float factorAtenuacion = 1.0f;
        if (!emisivo) {
            factorAtenuacion = (float) (tamAparentePixels * tamAparentePixels);
        }
        array4float[0] = (float) color.x;
        array4float[1] = (float) color.y;
        array4float[2] = (float) color.z;
        array4float[3] = factorAtenuacion;
        if (emisivo) {
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION, array4float, 0);
        } else {
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, array4float, 0);
        }
        array4float[0] = 0.0f;
        array4float[1] = 0.0f;
        array4float[2] = 0.0f;
        array4float[3] = factorAtenuacion;
        if (!emisivo) {
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION, array4float, 0);
        } else {
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, array4float, 0);
        }
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, array4float, 0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, array4float, 0);
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, 1);
        gl.glBegin(GL.GL_POINTS);
        gl.glNormal3d(normal.x, normal.y, normal.z);
        gl.glVertex3d(posMundo.x, posMundo.y, posMundo.z);
        gl.glEnd();
    }
}
