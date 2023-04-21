package inyo;

import javax.vecmath.*;

public class RtShader {

    /**
	 * Return the reflection vector given an incident direction I and a normal vector N.
	 * @param I incident vector
	 * @param N surface normal
	 * @return reflection vector
	 */
    static Vector3d reflect(Vector3d I, Vector3d N) {
        Vector3d Vr = new Vector3d(N);
        Vr.scale(2 * (I.dot(N)));
        Vr.negate();
        Vr.add(I);
        return Vr;
    }

    /**
	 * Return the transmitted vector given an incident direction I, the normal vector N 
	 * and the relative index of refraction eta. eta is the ratio of the index of refraction 
	 * in the volume containing the incident vector to that of the volume being entered. This 
	 * vector is computed using Snell's law. If the returned vector has zero length, then there 
	 * is no transmitted light because of total internal reflection. 
	 * @param I incident direction
	 * @param N surface normal
	 * @param eta relative index of refraction
	 * @return
	 */
    static Vector3d refract(Vector3d I, Vector3d N, double eta) {
        double IdotN = I.dot(N);
        double k = 1 - eta * eta * (1 - IdotN * IdotN);
        if (k < 0) {
            return new Vector3d();
        }
        Vector3d result = new Vector3d(I);
        result.scale(eta);
        Vector3d temp = new Vector3d(N);
        temp.scale(eta * IdotN + Math.sqrt(k));
        result.sub(temp);
        return result;
    }

    /**
	 * Calculates fresnel coefficient for a surface with an index of refraction 
	 * of n, and an angle of phi between m (normal at hit pt) and s (dir from hit 
	 * pt to light). The equation is given by:
	 * 
	 * F = 1/2 * (g-c)^2 / (g+c)^2 * { 1 + [ (c(g+c)-1) / (c(g-c)+1) ]^2 }
	 * where c = cos(phi), g = sqrt( n^2 + c^2 - 1 )
	 * 
	 * @param phi
	 * @param n
	 * @return
	 */
    public static double fresnel(double phi, double n) {
        double c = Math.cos(phi);
        double g = Math.sqrt((n * n) + (c * c) - 1);
        if (g < 0) {
            g = 0;
        }
        double F = 0.5;
        F *= ((g - c) * (g - c)) / ((g + c) * (g + c));
        double temp = ((c * (g + c)) - 1) / ((c * (g - c)) + 1);
        temp *= temp;
        temp += 1;
        F *= temp;
        return F;
    }

    /**
	 * Flips N, if needed, so it faces in the direction opposite to I.
	 * @param N
	 * @param I
	 * @return new forward facing vector.
	 */
    static final Vector3d faceForward(Vector3d N, Vector3d I) {
        Vector3d result = new Vector3d(N);
        if (N.dot(I) >= 0) {
            result.negate();
        }
        return result;
    }

    static double fresnel(Vector3d i, Vector3d n, double eta, Vector3d r, Vector3d t) {
        t.set(refract(i, n, eta));
        r.set(reflect(i, n));
        double c1 = -i.dot(n);
        double cs2 = 1.0 - eta * eta * (1.0 - c1 * c1);
        double tflag = (cs2 >= 0.0) ? 1 : 0;
        double ndott = -n.dot(t);
        double cosr_div_cosi = ndott / c1;
        double cosi_div_cosr = c1 / ndott;
        double fs = (cosr_div_cosi - eta) / (cosr_div_cosi + eta);
        fs = fs * fs;
        double fp = (cosi_div_cosr - eta) / (cosi_div_cosr + eta);
        fp = fp * fp;
        double kr = 0.5 * (fs + fp);
        double result = tflag * kr + (1 - tflag);
        return result;
    }

    static Color3f imageBasedShader(Vector3d normal, Vector3d view, RtImageMap imageMap) {
        Vector2d n = new Vector2d(normal.x, normal.z);
        n.normalize();
        Vector2d v = new Vector2d(-view.x, -view.z);
        v.normalize();
        double a = n.dot(v);
        n = new Vector2d(normal.y, normal.z);
        n.normalize();
        v = new Vector2d(-view.y, -view.z);
        v.normalize();
        double b = n.dot(v);
        return imageMap.getMapColor(a, -view.x < normal.x, b, -view.y < normal.y);
    }

    /**
	 * Lambert diffuse shader. Lighting is independant of the viewpoint. This gives
	 * the impression of rough plastic.
	 * @param normal Surface normal
	 * @param light Vector to light
	 * @return intensity of diffuse component
	 */
    static double lambertDiffuse(Vector3d normal, Vector3d light) {
        return Math.max(0, (normal.dot(light)));
    }

    /**
	 * Wrapped diffuse shader. This is a variation of the Lambert shader, but it takes
	 * an additional "wrap" angle to extend the angle that the light falls off, giving the
	 * impression that the material is lit by diffuse light all around. 
	 * @param normal Surface normal
	 * @param light Vector to light
	 * @param wrappedangle Actual angle where shadow begins
	 * @return intensity of diffuse lighting
	 */
    static double wrappedLambertDiffuse(Vector3d normal, Vector3d light, double wrappedangle) {
        double intensity = normal.dot(light);
        return (1 - Math.acos(intensity) / wrappedangle);
    }

    static double povDiffuse(Vector3d normal, Vector3d light, Vector3d view, double diffuse, double roughness, double brilliance) {
        double illumination = diffuse * light.dot(normal);
        if (illumination > 0) {
            illumination = Math.pow(illumination, brilliance) * diffuse;
        }
        return illumination;
    }

    /**
	 * Oren Nayer diffuse component. This is better at representing rough surfaces
	 * better than the Lambert model.
	 * 
	 */
    static double orenNayarDiffuse(Vector3d normal, Vector3d light, Vector3d view, double rough) {
        double a, b;
        Vector3d halfway = new Vector3d(light);
        halfway.add(view);
        halfway.normalize();
        double nh = normal.dot(halfway);
        if (nh < 0.0) {
            nh = 0.0;
        }
        double nv = normal.dot(view);
        if (nv <= 0.0) {
            nv = 0;
        }
        double nl = normal.dot(light);
        if (nl <= 0.0) {
            return 0;
        }
        double vh = view.dot(halfway);
        if (vh < 0.0) {
            vh = 0.0;
        }
        double litA = Math.acos(nl);
        double viewA = Math.acos(nv);
        Vector3d litB = new Vector3d(light.x - (nl * normal.x), light.y - (nl * normal.y), light.z - (nl * normal.z));
        litB.normalize();
        Vector3d viewB = new Vector3d(view.x - (nv * normal.x), view.y - (nv * normal.y), view.z - (nv * normal.z));
        viewB.normalize();
        double t = litB.dot(viewB);
        if (t < 0) {
            t = 0;
        }
        if (litA > viewA) {
            a = litA;
            b = viewA;
        } else {
            a = viewA;
            b = litA;
        }
        double A = 1 - (0.5 * ((rough * rough) / ((rough * rough) + 0.33)));
        double B = 0.45 * ((rough * rough) / ((rough * rough) + 0.09));
        b *= 0.95;
        return nl * (A + (B * t * Math.sin(a) * Math.tan(b)));
    }

    /**
	 * Minnaert diffuse component. This adds a darkening effect when the material is
	 * viewed at certain angles, making it useful for simulating materials such as 
	 * velvet.
	 */
    static double minnaertDiffuse(Vector3d normal, Vector3d light, Vector3d view, double darkness) {
        double nl = normal.dot(light);
        if (nl <= 0.0) return 0;
        double nv = normal.dot(view);
        if (nv < 0.0) {
            nv = 0;
        }
        double i = nl * Math.pow(Math.max(nv * nl, 0.1), darkness);
        if (darkness < 0.0) {
            i /= Math.pow(0.1, darkness);
        }
        return i;
    }

    /**
	 * 
	 * @param normal Surface normal
	 * @param light Vector to light
	 * @param view Vector from view
	 * @param hardness Hardness of specular component
	 * @return illumination factor
	 */
    static double povSpecular(Vector3d normal, Vector3d light, Vector3d view, double specular, double hardness) {
        Vector3d lv = new Vector3d(light);
        lv.sub(view);
        lv.scale(1.0 / Math.sqrt(lv.dot(lv)));
        lv.normalize();
        double intensity = lv.dot(normal);
        if (intensity > 0) {
            intensity *= Math.pow(intensity, 1.0 / hardness) * specular;
        } else {
            intensity = 0;
        }
        return intensity;
    }

    /**
	 * Phong specular component. This gives the general impression of plasic.
	 * @param normal Surface normal
	 * @param light Vector to light
	 * @param view Vector to eye
	 * @param hardness Hardness scale
	 * @return intensity of specular component
	 */
    static double phongSpecular(Vector3d normal, Vector3d light, Vector3d view, double hardness) {
        Vector3d halfway = new Vector3d(light);
        halfway.add(view);
        halfway.normalize();
        double nh = normal.dot(halfway);
        if (nh <= 0.0) {
            return 0;
        }
        return Math.pow(nh, 1.0 / hardness);
    }

    /**
	 * Blinn specular. Based on the Cook-Torrence model, but cheaper to calculate.
	 * @param normal Surface normal
	 * @param light Vector to light
	 * @param view Vector to eye
	 * @param refrac Refraction scale
	 * @param spec_power Specular power scale
	 * @return Intensity of specular component
	 */
    static double blinnSpecular(Vector3d normal, Vector3d light, Vector3d view, double refrac, double spec_power) {
        if (refrac < 1.0) {
            return 0.0;
        }
        if (spec_power == 0.0) {
            return 0.0;
        }
        if (spec_power < 100.0) {
            spec_power = Math.sqrt(1.0 / spec_power);
        } else {
            spec_power = 10.0 / spec_power;
        }
        Vector3d halfway = new Vector3d(light);
        halfway.add(view);
        halfway.normalize();
        double nh = normal.dot(halfway);
        if (nh < 0.0) {
            return 0.0;
        }
        double nv = normal.dot(view);
        if (nv <= 0.0) {
            nv = 0.01;
        }
        double nl = normal.dot(light);
        if (nl <= 0.0) {
            return 0.0;
        }
        double vh = view.dot(halfway);
        if (vh <= 0.0) {
            vh = 0.01;
        }
        double g = 0;
        double a = 1.0;
        double b = (2.0 * nh * nv) / vh;
        double c = (2.0 * nh * nl) / vh;
        if (a < b && a < c) {
            g = a;
        } else if (b < a && b < c) {
            g = b;
        } else if (c < a && c < b) {
            g = c;
        }
        double p = Math.sqrt((double) ((refrac * refrac) + (vh * vh) - 1.0));
        double f = (((p - vh) * (p - vh)) / ((p + vh) * (p + vh))) * (1 + ((((vh * (p + vh)) - 1.0) * ((vh * (p + vh)) - 1.0)) / (((vh * (p - vh)) + 1.0) * ((vh * (p - vh)) + 1.0))));
        double ang = Math.acos(nh);
        return f * g * Math.exp((-(ang * ang) / (2.0 * spec_power * spec_power)));
    }

    /**
	 * Cook-Torrence specular. This is a physically-based model, that simulates a
	 * material with microfaucets based on the Beckman distribution function.
	 * 
	 * @param normal Surface normal
	 * @param light Vector to light
	 * @param view Vector to eye
	 * @param roughness Roughness factor
	 * @param ior Index of refraction
	 * @return Amount of illumination
	 */
    static double cookTorrenceSpecular(Vector3d normal, Vector3d light, Vector3d view, double roughness, double ior) {
        Vector3d halfway = new Vector3d(light);
        halfway.add(view);
        halfway.normalize();
        double nv = normal.dot(view);
        if (nv <= 0) {
            return 0;
        }
        double nh = normal.dot(halfway);
        if (nh <= 0) {
            return 0;
        }
        double nl = normal.dot(light);
        double hl = halfway.dot(light);
        double phi = Math.acos(nl);
        double sigma = Math.acos(nh);
        double temp = Math.tan(sigma) / roughness;
        temp *= temp;
        double D = 1.0 / Math.cos(sigma);
        D *= D;
        D *= D;
        D /= (4.0 * roughness * roughness);
        D *= Math.exp(-temp);
        double G = 1.0;
        double Gm = 2.0 * nh * nl / hl;
        double Gs = 2.0 * nh * nv / hl;
        if (Gm < G) G = Gm;
        if (Gs < G) G = Gs;
        temp = (D * G / (nl * nv));
        return (fresnel(phi, ior)) / Math.PI * temp;
    }

    /**
	 * Reduced Cook-Torrence specular model. No IOR factor.
	 * @param normal Surface normal
	 * @param light Vector to light
	 * @param view Vector to eye
	 * @param hardness Hardness factor
	 * @return Amount of specular
	 */
    static double reducedCookTorranceSpecular(Vector3d normal, Vector3d light, Vector3d view, double hardness) {
        Vector3d halfway = new Vector3d(light);
        halfway.add(view);
        halfway.normalize();
        double nh = normal.dot(halfway);
        if (nh < 0.0) {
            return 0.0;
        }
        double nv = normal.dot(view);
        if (nv <= 0.0) {
            return 0;
        }
        return Math.pow(nv, 1.0 / hardness) / (0.1 + nv);
    }

    /**
	 * Ward isotropic gaussian specular highlight
	 */
    static double wardIsotropicSpecular(Vector3d normal, Vector3d light, Vector3d view, double rms) {
        Vector3d halfway = new Vector3d(light);
        halfway.add(view);
        halfway.normalize();
        double nh = halfway.dot(normal);
        if (nh <= 0.0) {
            nh = 0.001;
        }
        double nv = normal.dot(view);
        if (nv <= 0.0) {
            nv = 0.001;
        }
        double nl = normal.dot(light);
        if (nl <= 0.0) {
            nl = 0.001;
        }
        double angle = Math.tan(Math.acos(nh));
        double alpha = Math.max(rms, 0.001);
        return (1.0 / (4 * Math.PI * alpha * alpha)) * (Math.exp(-(angle * angle) / (alpha * alpha)) / (Math.sqrt(nv * nl)));
    }

    /**
	 * Evaluate the Henyey-Greenstein phase function for two vectors with
	 * an asymmetry value g.  v1 and v2 should be normalized and g should 
	 * be in the range (-1, 1).  Negative values of g correspond to more
	 * back-scattering and positive values correspond to more forward scattering.
	*/
    static double phase(Vector3d v1, Vector3d v2, double g) {
        double costheta = -v1.dot(v2);
        return (1. - g * g) / Math.pow(1. + g * g - 2. * g * costheta, 1.5);
    }

    static double singleScatter(Vector3d wi, Vector3d wo, Vector3d normal, double g, double albedo, double thickness) {
        double wIn = Math.abs(wi.dot(normal));
        double wOut = Math.abs(wo.dot(normal));
        return albedo * phase(wi, wo, g) / (wIn + wOut) * (1. - Math.exp(-(1 / wIn + 1 / wOut) * thickness));
    }

    /**
	 * step returns 0 if value is less than min; otherwise it returns 1. smoothstep returns 0 
	 * if value is less than min, 1 if value is greater than or equal to max, and performs a 
	 * smooth Hermite interpolation between 0 and 1 in the interval min to max.
	 * 
	 * @param min
	 * @param max
	 * @param value
	 * @return
	 */
    static double smoothstep(double min, double max, double value) {
        if (value < min) return 0;
        if (value > max) return 1;
        double result = ((value - min) / (max - min));
        return result * result * (3 - 2 * result);
    }

    static double pharrSkinScatter(Vector3d Vf, Vector3d Nn, Vector3d Ln, double thickness) {
        double eta = (1.0 / 1.4);
        Vector3d flip = new Vector3d(Vf);
        flip.negate();
        Vector3d R = new Vector3d();
        Vector3d T = new Vector3d();
        double Kr = fresnel(flip, Nn, eta, R, T);
        Kr = smoothstep(0.0, 0.5, Kr);
        double Kt = 1.0 - Kr;
        T.normalize();
        flip = new Vector3d(Nn);
        flip.negate();
        Vector3d R2 = new Vector3d();
        Vector3d T2 = new Vector3d();
        double Kr2 = fresnel(flip, Nn, eta, R2, T2);
        T2.normalize();
        Kr2 = smoothstep(0.0, 0.5, Kr2);
        double Kt2 = 1.0 - Kr2;
        return Kt * Kt2 * (singleScatter(T, T2, Nn, .8, .8, thickness) + singleScatter(T, T2, Nn, .3, .5, thickness) + singleScatter(T, T2, Nn, 0., .4, thickness));
    }

    public static double pharrSkin(Vector3d normal, Vector3d light, Vector3d view, double thickness) {
        Vector3d Nn = faceForward(normal, view);
        Vector3d Vf = new Vector3d(view);
        Vf.negate();
        return pharrSkinScatter(Vf, Nn, light, thickness);
    }
}
