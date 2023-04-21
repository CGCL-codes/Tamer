package de.grogra.imp3d.glsl.material.channel;

import java.awt.image.BufferedImage;
import de.grogra.imp3d.glsl.utility.ShaderConfiguration;
import de.grogra.vecmath.Math2;

/**
 * Static class that offers simplex noise function and a turbulence function
 * based on noise. Most work done by Stefan Gustavson
 * http://webstaff.itn.liu.se/~stegu/simplexnoise/ "Simplex noise demystified"
 * 
 * @author Konni Hartmann
 */
public class SimplexNoise {

    static final String simplexSig = "void simplex( const in vec3 P, out vec3 offset1, out vec3 offset2 )";

    static final String simplex = "			vec3 offset0;\n" + "			vec2 isX = step( P.yz, P.xx );\n" + "			offset0.x  = dot( isX, vec2( 1.0 ) );\n" + "			offset0.yz = 1.0 - isX;\n" + "			float isY = step( P.z, P.y );\n" + "			offset0.y += isY;\n" + "			offset0.z += 1.0 - isY;\n" + "			offset2 = clamp(   offset0, 0.0, 1.0 );\n" + "			offset1 = clamp( --offset0, 0.0, 1.0 );";

    static final String noise3dSig = "float inoise(vec3 P)";

    static final String noise3dbody = "		#define ONE 0.00390625\n" + "		#define ONEHALF 0.001953125\n" + "		#define F3 0.333333333333\n" + "		#define G3 0.166666666667\n" + "		  float s = (P.x + P.y + P.z) * F3;\n" + "		  vec3 Pi = floor(P + s);\n" + "		  float t = (Pi.x + Pi.y + Pi.z) * G3;\n" + "		  vec3 P0 = Pi - t;\n" + "		  Pi = Pi * ONE + ONEHALF;\n" + "		  vec3 Pf0 = P - P0;\n" + "		  vec3 o1;\n" + "		  vec3 o2;\n" + "		  simplex(Pf0, o1, o2);\n" + "		  float perm0 = perm(Pi.xy).a;\n" + "		  vec3  grad0 = perm(vec2(perm0, Pi.z)).rgb * 4.0 - 1.0;\n" + "		  float t0 = 0.6 - dot(Pf0, Pf0);\n" + "		  float t20 = 0.0;\n" + "		  float t40 = 0.0;\n" + "		  float n0;\n" + "		  if (t0 < 0.0) n0 = 0.0;\n" + "		  else {\n" + "		    t20 = t0 * t0;\n" + "		    t40 = t20 * t20;\n" + "		    n0 = t40 * dot(grad0, Pf0);\n" + "		  }\n" + "		  vec3 Pf1 = Pf0 - o1 + G3;\n" + "		  float perm1 = perm(Pi.xy + o1.xy*ONE).a;\n" + "		  vec3  grad1 = perm(vec2(perm1, Pi.z + o1.z*ONE)).rgb * 4.0 - 1.0;\n" + "		  float t1 = 0.6 - dot(Pf1, Pf1);\n" + "		  float t21 = 0.0;\n" + "		  float t41 = 0.0;\n" + "		  float n1;\n" + "		  if (t1 < 0.0) n1 = 0.0;\n" + "		  else {\n" + "		    t21 = t1 * t1;\n" + "		    t41 = t21 * t21;\n" + "		    n1 = t41 * dot(grad1, Pf1);\n" + "		  }\n" + "		  vec3 Pf2 = Pf0 - o2 + 2.0 * G3;\n" + "		  float perm2 = perm(Pi.xy + o2.xy*ONE).a;\n" + "		  vec3  grad2 = perm(vec2(perm2, Pi.z + o2.z*ONE)).rgb * 4.0 - 1.0;\n" + "		  float t2 = 0.6 - dot(Pf2, Pf2);\n" + "		  float t22 = 0.0;\n" + "		  float t42 = 0.0;\n" + "		  float n2;\n" + "		  if (t2 < 0.0) n2 = 0.0;\n" + "		  else {\n" + "		    t22 = t2 * t2;\n" + "		    t42 = t22 * t22;\n" + "		    n2 = t42 * dot(grad2, Pf2);\n" + "		  }\n" + "		  vec3 Pf3 = Pf0 - vec3(1.0-3.0*G3);\n" + "		  float perm3 = perm(Pi.xy + vec2(ONE, ONE)).a;\n" + "		  vec3  grad3 = perm(vec2(perm3, Pi.z + ONE)).rgb * 4.0 - 1.0;\n" + "		  float t3 = 0.6 - dot(Pf3, Pf3);\n" + "		  float t23 = 0.0;\n" + "		  float t43 = 0.0;\n" + "		  float n3;\n" + "		  if(t3 < 0.0) n3 = 0.0;\n" + "		  else {\n" + "		    t23 = t3 * t3;\n" + "		    t43 = t23 * t23;\n" + "		    n3 = t43 * dot(grad3, Pf3);\n" + "		  }\n";

    static final String noise3d = noise3dbody + "return 0.00104006 + 32.741 * (n0 + n1 + n2 + n3);";

    static final String dnoise3dSig = "vec3 dinoise(vec3 P)";

    static final String dnoise3d = noise3dbody + "vec3 D = -8. * t20 * t0 * Pf0 * dot(grad0, Pf0) + t40 * grad0;\n" + "D += -8. * t21 * t1 * Pf1 * dot(grad1, Pf1) + t41 * grad1;\n" + "D += -8. * t22 * t2 * Pf2 * dot(grad2, Pf2) + t42 * grad2;\n" + "D += -8. * t23 * t3 * Pf3 * dot(grad3, Pf3) + t43 * grad3;\n" + "return D * 16.9446;\n";

    static final String turb3dSig = "float turbulence(vec3 f, int octaves, float lambda, float omega)";

    static final String turb3d = "float v = inoise (f);" + "float l = lambda, o = omega;" + "for (int i = octaves-1; i > 0; --i)" + "{" + "v += o * inoise (f * l);" + "if (i > 1)" + "{" + "l *= lambda;" + "o *= omega;" + "}" + "}" + "return v;";

    static final String dturb3dSig = "vec3 dturbulence(vec3 f, int octaves, float lambda, float omega)";

    static final String dturb3d = "vec3 v = dinoise (f);" + "float l = lambda, o = omega;" + "for (int i = octaves-1; i > 0; --i)" + "{" + "v += o * dinoise (f * l);" + "if (i > 1)" + "{" + "l *= lambda;" + "o *= omega;" + "}" + "}" + "return v;";

    static BufferedImage permImage, gradImage;

    static String permSampler;

    static boolean init = false;

    static int PermutationTexture = 0;

    static final int grad3[][] = { { 0, 1, 1 }, { 0, 1, -1 }, { 0, -1, 1 }, { 0, -1, -1 }, { 1, 0, 1 }, { 1, 0, -1 }, { -1, 0, 1 }, { -1, 0, -1 }, { 1, 1, 0 }, { 1, -1, 0 }, { -1, 1, 0 }, { -1, -1, 0 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, -1, 1 }, { 0, 1, 1 } };

    public static BufferedImage generatePermutationTexture() {
        BufferedImage result = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        int pixel[] = new int[4];
        for (char i = 0; i < 256; i++) for (char j = 0; j < 256; j++) {
            char value = Math2.random((char) ((j + Math2.random(i)) & 0xFF));
            pixel[0] = grad3[value & 0x0F][0] * 64 + 64;
            pixel[1] = grad3[value & 0x0F][1] * 64 + 64;
            pixel[2] = grad3[value & 0x0F][2] * 64 + 64;
            pixel[3] = value;
            result.setRGB(j, i, (pixel[3] << 24) | (pixel[0] << 16) | (pixel[1] << 8) | (pixel[2]));
        }
        return result;
    }

    static void regLookUpTexture(ShaderConfiguration p) {
        if (!init) {
            permImage = generatePermutationTexture();
            init = true;
        }
        permSampler = p.registerTexture(permImage);
    }

    public static String registerTurbWithUnroll(ShaderConfiguration phong, int octaves) {
        String sig = "float turbulence_" + octaves + "(vec3 f, float lambda, float omega)";
        String func = "float v = inoise (f);" + "float l = lambda, o = omega;";
        for (int i = octaves - 1; i > 1; --i) {
            func += "v += o * inoise (f * l);" + "l *= lambda;" + "o *= omega;";
        }
        if (octaves - 1 > 1) func += "v += o * inoise (f * l);";
        func += "return v;";
        phong.registerFunc(sig, func);
        return "turbulence_" + octaves;
    }

    public static String registerDTurbWithUnroll(ShaderConfiguration phong, int octaves) {
        String sig = "vec3 dturbulence_" + octaves + "(vec3 f, float lambda, float omega)";
        String func = "vec3 v = dinoise (f);" + "float l = lambda, o = omega;";
        for (int i = octaves - 1; i > 1; --i) {
            func += "v += o * dinoise (f * l);" + "l *= lambda;" + "o *= omega;";
        }
        if (octaves - 1 > 1) func += "v += o * dinoise (f * l);";
        func += "return v;";
        phong.registerFunc(sig, func);
        return "dturbulence_" + octaves;
    }

    public static void registerNoiseFunctions(ShaderConfiguration phong) {
        SimplexNoise.regLookUpTexture(phong);
        phong.registerFunc("vec4 perm(vec2 x)", "return texture2D(" + permSampler + ", x);");
        phong.registerFunc(simplexSig, simplex);
        phong.registerFunc(noise3dSig, noise3d);
        phong.registerFunc(turb3dSig, turb3d);
    }

    public static void registerdNoiseFunctions(ShaderConfiguration phong) {
        SimplexNoise.regLookUpTexture(phong);
        phong.registerFunc("vec4 perm(vec2 x)", "return texture2D(" + permSampler + ", x);");
        phong.registerFunc(simplexSig, simplex);
        phong.registerFunc(dnoise3dSig, dnoise3d);
        phong.registerFunc(dturb3dSig, dturb3d);
    }
}
