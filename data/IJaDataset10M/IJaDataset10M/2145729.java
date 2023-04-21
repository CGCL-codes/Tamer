package espacio;

import javax.media.opengl.*;
import juego.genSS.GenSS;
import motor3d.*;

public class VisualAstro extends Objeto3d implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public double radioAstro;

    public static final int NUM_MAX_RECURSIONES = 8;

    public MaterialShader materialAstro;

    public Luz luzEscenaAstros;

    public Luz luzEscenaObjetos;

    public Vector3d direccionLuzEscenaObjetos;

    public boolean tieneAtmosfera;

    public boolean pintarAtmosfera;

    public double presionAtmosferica;

    public double alturaAtmosfera;

    public double escalaAtmosfera;

    public Vector3d colorAtmosfera;

    public MaterialShader materialAtmosfera;

    private transient int localizacionUniformAtmosphereAltitude;

    private transient int localizacionUniformAtmosphericScale;

    private transient int localizacionUniformAtmosphericPressure;

    protected transient int localizacionUniformAtmosphereColor;

    private transient int localizacionUniformPlanetRadius;

    private transient int localizacionUniformPlanetEyePosition;

    private transient int localizacionUniformViewPlanetModelInverse;

    private static GL2 dibGL;

    private static float radioAstroFloat;

    private static Vector3d direccionCamara = new Vector3d();

    private static int maximoNivelDibujado;

    private static final float unTercio = 1.0f / 3.0f;

    private static float direccionCamaraX;

    private static float direccionCamaraY;

    private static float direccionCamaraZ;

    private static Vector3d[] puntosDiamante = new Vector3d[6];

    static {
        puntosDiamante[0] = new Vector3d(0.0d, 0.0d, 1.0d);
        puntosDiamante[1] = new Vector3d(0.0f, -1.0f, 0.0d);
        puntosDiamante[2] = new Vector3d(1.0f, 0.0f, 0.0d);
        puntosDiamante[3] = new Vector3d(0.0f, 1.0f, 0.0d);
        puntosDiamante[4] = new Vector3d(-1.0f, 0.0f, 0.0d);
        puntosDiamante[5] = new Vector3d(0.0d, 0.0d, -1.0d);
    }

    private static final float PI_INV = (float) (1.0f / Math.PI);

    private static Vector3d vectorAux1 = new Vector3d();

    private static double[] matrizAux1 = new double[16];

    private static double[] matrizAux2 = new double[16];

    private static float[] matrizFloatAux1 = new float[16];

    public VisualAstro() {
        radioAstro = 1.0d;
        transformacion = new TransformacionYXZ();
        direccionLuzEscenaObjetos = new Vector3d();
    }

    public void recalcularRadio() {
        radio = radioAstro;
    }

    public boolean getTieneTransparencia() {
        return materialAstro.tipoTransparencia != MaterialEstandar.TRANSPARENCIA_OFF;
    }

    public void dibujarSuperficiePlaneta(PuertoVisual puerto, double tamAparentePixels) {
        dibGL = puerto.gl;
        Matriz4.vector3dLibreXMatriz4Inversa(puerto.posRelObjetoCamaraMundo, this.transformacion.matrizMundo, direccionCamara);
        direccionCamaraX = (float) direccionCamara.x;
        direccionCamaraY = (float) direccionCamara.y;
        direccionCamaraZ = (float) direccionCamara.z;
        radioAstroFloat = (float) radioAstro;
        maximoNivelDibujado = 3;
        if (tamAparentePixels > 82) {
            maximoNivelDibujado = 4;
            if (tamAparentePixels > 400) {
                maximoNivelDibujado = 5;
                if (tamAparentePixels > 1000) {
                    maximoNivelDibujado = 6;
                    if (tamAparentePixels > 1350) {
                        maximoNivelDibujado = 7;
                        if (tamAparentePixels > 1450) {
                            maximoNivelDibujado = 8;
                        }
                    }
                }
            }
        }
        dibGL.glBegin(GL.GL_TRIANGLES);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[0].x, (float) puntosDiamante[0].y, (float) puntosDiamante[0].z, (float) puntosDiamante[1].x, (float) puntosDiamante[1].y, (float) puntosDiamante[1].z, (float) puntosDiamante[2].x, (float) puntosDiamante[2].y, (float) puntosDiamante[2].z, false);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[0].x, (float) puntosDiamante[0].y, (float) puntosDiamante[0].z, (float) puntosDiamante[2].x, (float) puntosDiamante[2].y, (float) puntosDiamante[2].z, (float) puntosDiamante[3].x, (float) puntosDiamante[3].y, (float) puntosDiamante[3].z, false);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[0].x, (float) puntosDiamante[0].y, (float) puntosDiamante[0].z, (float) puntosDiamante[3].x, (float) puntosDiamante[3].y, (float) puntosDiamante[3].z, (float) puntosDiamante[4].x, (float) puntosDiamante[4].y, (float) puntosDiamante[4].z, true);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[0].x, (float) puntosDiamante[0].y, (float) puntosDiamante[0].z, (float) puntosDiamante[4].x, (float) puntosDiamante[4].y, (float) puntosDiamante[4].z, (float) puntosDiamante[1].x, (float) puntosDiamante[1].y, (float) puntosDiamante[1].z, true);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[5].x, (float) puntosDiamante[5].y, (float) puntosDiamante[5].z, (float) puntosDiamante[2].x, (float) puntosDiamante[2].y, (float) puntosDiamante[2].z, (float) puntosDiamante[1].x, (float) puntosDiamante[1].y, (float) puntosDiamante[1].z, false);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[5].x, (float) puntosDiamante[5].y, (float) puntosDiamante[5].z, (float) puntosDiamante[3].x, (float) puntosDiamante[3].y, (float) puntosDiamante[3].z, (float) puntosDiamante[2].x, (float) puntosDiamante[2].y, (float) puntosDiamante[2].z, false);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[5].x, (float) puntosDiamante[5].y, (float) puntosDiamante[5].z, (float) puntosDiamante[4].x, (float) puntosDiamante[4].y, (float) puntosDiamante[4].z, (float) puntosDiamante[3].x, (float) puntosDiamante[3].y, (float) puntosDiamante[3].z, true);
        recursivoDibujarTriangulo(0, (float) puntosDiamante[5].x, (float) puntosDiamante[5].y, (float) puntosDiamante[5].z, (float) puntosDiamante[1].x, (float) puntosDiamante[1].y, (float) puntosDiamante[1].z, (float) puntosDiamante[4].x, (float) puntosDiamante[4].y, (float) puntosDiamante[4].z, true);
        dibGL.glEnd();
    }

    public void recursivoDibujarTriangulo(int nivel, float v0x, float v0y, float v0z, float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, boolean hemisferioOeste) {
        boolean hacerCulling = nivel >= 2 && nivel <= maximoNivelDibujado - 2;
        float nv0x = 0.0f;
        float nv0y = 0.0f;
        float nv0z = 0.0f;
        float nv1x = 0.0f;
        float nv1y = 0.0f;
        float nv1z = 0.0f;
        float nv2x = 0.0f;
        float nv2y = 0.0f;
        float nv2z = 0.0f;
        if (hacerCulling || nivel == maximoNivelDibujado) {
            float normInv = (float) (1.0d / Math.sqrt(v0x * v0x + v0y * v0y + v0z * v0z));
            nv0x = v0x * normInv;
            nv0y = v0y * normInv;
            nv0z = v0z * normInv;
            v0x = nv0x * radioAstroFloat;
            v0y = nv0y * radioAstroFloat;
            v0z = nv0z * radioAstroFloat;
            normInv = (float) (1.0d / Math.sqrt(v1x * v1x + v1y * v1y + v1z * v1z));
            nv1x = v1x * normInv;
            nv1y = v1y * normInv;
            nv1z = v1z * normInv;
            v1x = nv1x * radioAstroFloat;
            v1y = nv1y * radioAstroFloat;
            v1z = nv1z * radioAstroFloat;
            normInv = (float) (1.0d / Math.sqrt(v2x * v2x + v2y * v2y + v2z * v2z));
            nv2x = v2x * normInv;
            nv2y = v2y * normInv;
            nv2z = v2z * normInv;
            v2x = nv2x * radioAstroFloat;
            v2y = nv2y * radioAstroFloat;
            v2z = nv2z * radioAstroFloat;
        }
        if (hacerCulling) {
            float d0 = nv0x * (direccionCamaraX + v0x) + nv0y * (direccionCamaraY + v0y) + nv0z * (direccionCamaraZ + v0z);
            float d1 = nv1x * (direccionCamaraX + v1x) + nv1y * (direccionCamaraY + v1y) + nv1z * (direccionCamaraZ + v1z);
            float d2 = nv2x * (direccionCamaraX + v2x) + nv2y * (direccionCamaraY + v2y) + nv2z * (direccionCamaraZ + v2z);
            float trix = unTercio * (v0x + v1x + v2x);
            float triy = unTercio * (v0y + v1y + v2y);
            float triz = unTercio * (v0z + v1z + v2z);
            float normTri1x = v1x - v0x;
            float normTri1y = v1y - v0y;
            float normTri1z = v1z - v0z;
            float normTri2x = v2x - v0x;
            float normTri2y = v2y - v0y;
            float normTri2z = v2z - v0z;
            float normTrix = normTri1y * normTri2z - normTri1z * normTri2y;
            float normTriy = normTri1z * normTri2x - normTri1x * normTri2z;
            float normTriz = normTri1x * normTri2y - normTri1y * normTri2x;
            float normTri = 1.0f / (normTrix * normTrix + normTriy * normTriy + normTriz * normTriz);
            normTrix *= normTri;
            normTriy *= normTri;
            normTriz *= normTri;
            float dTri = normTrix * (direccionCamaraX + trix) + normTriy * (direccionCamaraY + triy) + normTriz * (direccionCamaraZ + triz);
            if (d0 > 0.0f && d1 > 0.0f && d2 > 0.0f && dTri > 0.0f) {
                return;
            }
        }
        if (nivel >= maximoNivelDibujado) {
            float lonv0 = calculaLongitud(v0x, v0y, hemisferioOeste);
            float latv0 = calculaLatitud(v0x, v0y, v0z);
            float lonv1 = calculaLongitud(v1x, v1y, hemisferioOeste);
            float latv1 = calculaLatitud(v1x, v1y, v1z);
            float lonv2 = calculaLongitud(v2x, v2y, hemisferioOeste);
            float latv2 = calculaLatitud(v2x, v2y, v2z);
            dibGL.glNormal3f(nv0x, nv0y, nv0z);
            dibGL.glTexCoord2f(lonv0, latv0);
            dibGL.glVertex3f(v0x, v0y, v0z);
            dibGL.glNormal3f(nv1x, nv1y, nv1z);
            dibGL.glTexCoord2f(lonv1, latv1);
            dibGL.glVertex3f(v1x, v1y, v1z);
            dibGL.glNormal3f(nv2x, nv2y, nv2z);
            dibGL.glTexCoord2f(lonv2, latv2);
            dibGL.glVertex3f(v2x, v2y, v2z);
        } else {
            nivel++;
            float v01x = 0.5f * v0x + 0.5f * v1x;
            float v01y = 0.5f * v0y + 0.5f * v1y;
            float v01z = 0.5f * v0z + 0.5f * v1z;
            float v12x = 0.5f * v1x + 0.5f * v2x;
            float v12y = 0.5f * v1y + 0.5f * v2y;
            float v12z = 0.5f * v1z + 0.5f * v2z;
            float v20x = 0.5f * v2x + 0.5f * v0x;
            float v20y = 0.5f * v2y + 0.5f * v0y;
            float v20z = 0.5f * v2z + 0.5f * v0z;
            recursivoDibujarTriangulo(nivel, v0x, v0y, v0z, v01x, v01y, v01z, v20x, v20y, v20z, hemisferioOeste);
            recursivoDibujarTriangulo(nivel, v01x, v01y, v01z, v1x, v1y, v1z, v12x, v12y, v12z, hemisferioOeste);
            recursivoDibujarTriangulo(nivel, v20x, v20y, v20z, v12x, v12y, v12z, v2x, v2y, v2z, hemisferioOeste);
            recursivoDibujarTriangulo(nivel, v01x, v01y, v01z, v12x, v12y, v12z, v20x, v20y, v20z, hemisferioOeste);
        }
    }

    public void dibujar(PuertoVisual puerto, double tamAparentePixels) {
        if (materialAstro.dummyActivado == false) {
            return;
        }
        dibujarPlanetaTrazadoPorRayos(puerto, tamAparentePixels);
    }

    public void dibujarAtmosfera(PuertoVisual puerto) {
        if (!pintarAtmosfera) {
            return;
        }
        GL2 gl = puerto.gl;
        if (!materialAtmosfera.variablesShaderCargadas) {
            localizacionUniformAtmosphereAltitude = gl.glGetUniformLocation(materialAtmosfera.idPrograma, "atmosphereAltitude");
            localizacionUniformAtmosphericScale = gl.glGetUniformLocation(materialAtmosfera.idPrograma, "atmosphericScale");
            localizacionUniformAtmosphericPressure = gl.glGetUniformLocation(materialAtmosfera.idPrograma, "atmosphericPressure");
            localizacionUniformAtmosphereColor = gl.glGetUniformLocation(materialAtmosfera.idPrograma, "atmosphereColor");
            localizacionUniformPlanetRadius = gl.glGetUniformLocation(materialAtmosfera.idPrograma, "planetRadius");
            localizacionUniformPlanetEyePosition = gl.glGetUniformLocation(materialAtmosfera.idPrograma, "planetEyePosition");
        }
        materialAtmosfera.establecer(puerto);
        gl.glUniform1f(localizacionUniformAtmosphereAltitude, (float) alturaAtmosfera);
        gl.glUniform1f(localizacionUniformAtmosphericScale, (float) escalaAtmosfera);
        gl.glUniform1f(localizacionUniformAtmosphericPressure, (float) presionAtmosferica);
        gl.glUniform3f(localizacionUniformAtmosphereColor, (float) this.colorAtmosfera.x, (float) this.colorAtmosfera.y, (float) this.colorAtmosfera.z);
        gl.glUniform1f(localizacionUniformPlanetRadius, (float) radioAstro);
        Matriz4.vector3dLibreXMatriz4(puerto.posRelObjetoCamaraMundo, puerto.matrizCamaraInvertida, vectorAux1);
        gl.glUniform3f(localizacionUniformPlanetEyePosition, (float) vectorAux1.x, (float) vectorAux1.y, (float) vectorAux1.z);
        puerto.dibujarFullScreenQuad();
        materialAtmosfera.desestablecer(puerto);
    }

    private float calculaLongitud(float x, float y, boolean hemisferioOeste) {
        float lon = 0.0f;
        if (y != 0.0f) {
            lon = 0.5f * ((float) Math.atan(x / y)) * PI_INV;
            if (y > 0.0f) {
                if (lon <= 0.0f && hemisferioOeste) {
                    lon = 1.0f + lon;
                }
            } else {
                lon = 0.5f + lon;
            }
        } else {
            if (x > 0.0f) {
                lon = 0.25f;
            } else {
                lon = 0.75f;
            }
        }
        return 1.0f - lon;
    }

    private float calculaLatitud(float x, float y, float z) {
        float lat = 0.0f;
        float xy = (float) Math.sqrt(x * x + y * y);
        if (xy == 0.0f) {
            if (z > 0.0f) {
                lat = 1.0f;
            } else {
                lat = 0.0f;
            }
        } else {
            lat = 0.5f + (float) Math.atan(z / xy) * PI_INV;
        }
        return lat;
    }

    public void cargarLocalizacionesUniforms(PuertoVisual puerto) {
        GL2 gl = puerto.gl;
        if (!materialAstro.variablesShaderCargadas) {
            localizacionUniformPlanetRadius = gl.glGetUniformLocation(materialAstro.idPrograma, "planetRadius");
            localizacionUniformPlanetEyePosition = gl.glGetUniformLocation(materialAstro.idPrograma, "planetEyePosition");
            localizacionUniformViewPlanetModelInverse = gl.glGetUniformLocation(materialAstro.idPrograma, "viewPlanetModelInverse");
            if (pintarAtmosfera) {
                localizacionUniformAtmosphereAltitude = gl.glGetUniformLocation(materialAstro.idPrograma, "atmosphereAltitude");
                localizacionUniformAtmosphericScale = gl.glGetUniformLocation(materialAstro.idPrograma, "atmosphericScale");
                localizacionUniformAtmosphericPressure = gl.glGetUniformLocation(materialAstro.idPrograma, "atmosphericPressure");
                localizacionUniformAtmosphereColor = gl.glGetUniformLocation(materialAstro.idPrograma, "atmosphereColor");
            }
        }
    }

    public void establecerValoresUniforms(PuertoVisual puerto) {
        GL2 gl = puerto.gl;
        gl.glUniform1f(localizacionUniformPlanetRadius, (float) radioAstro);
        Matriz4.vector3dLibreXMatriz4(puerto.posRelObjetoCamaraMundo, puerto.matrizCamaraInvertida, vectorAux1);
        gl.glUniform3f(localizacionUniformPlanetEyePosition, (float) vectorAux1.x, (float) vectorAux1.y, (float) vectorAux1.z);
        gl.glUniform3f(localizacionUniformPlanetEyePosition, (float) vectorAux1.x, (float) vectorAux1.y, (float) vectorAux1.z);
        Matriz4.invierteMatriz(transformacion.matrizMundo, matrizAux1);
        Matriz4.matriz4XMatriz4(matrizAux2, matrizAux1, puerto.matrizCamara);
        Matriz4.copiarDeMatrizAFloat(matrizFloatAux1, matrizAux2);
        gl.glUniformMatrix4fv(localizacionUniformViewPlanetModelInverse, 1, false, matrizFloatAux1, 0);
        if (pintarAtmosfera) {
            gl.glUniform1f(localizacionUniformAtmosphereAltitude, (float) alturaAtmosfera);
            gl.glUniform1f(localizacionUniformAtmosphericScale, (float) escalaAtmosfera);
            gl.glUniform1f(localizacionUniformAtmosphericPressure, (float) presionAtmosferica);
            gl.glUniform3f(localizacionUniformAtmosphereColor, (float) this.colorAtmosfera.x, (float) this.colorAtmosfera.y, (float) this.colorAtmosfera.z);
        }
    }

    public void dibujarPlanetaTrazadoPorRayos(PuertoVisual puerto, double tamAparentePixels) {
        cargarLocalizacionesUniforms(puerto);
        materialAstro.establecer(puerto);
        establecerValoresUniforms(puerto);
        puerto.dibujarFullScreenQuad();
        materialAstro.desestablecer(puerto);
    }
}
